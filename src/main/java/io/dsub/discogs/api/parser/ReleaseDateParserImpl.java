package io.dsub.discogs.api.parser;

import java.time.YearMonth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReleaseDateParserImpl implements ReleaseDateParser {
    private static final Pattern YEAR_MONTH_DAY_DATE_PATTERN =
            Pattern.compile("^[._/ -]*(?<year>[^._/ -]{4})[._/ -]*" +
                    "(?<month>[^._/ -]{2}[._/ -]*|[^._/ -]{1,2}[._/ -]+)" +
                    "(?<day>[^._/ -]{1,2})[._/ -]*$");

    private static final Pattern YEAR_MONTH_DATE_PATTERN =
            Pattern.compile("^[._/ -]*(?<year>[^._/ -]{4})[._/ -]*(?<month>[^._/ -]{1,2}[._/ -]*)$");
    private static final Pattern YEAR_DATE_PATTERN =
            Pattern.compile("^[._/ -]*(?<year>[^._/ -]{4})[._/ -]*$");

    private static final Pattern DIGIT_PATTERN = Pattern.compile("^\\d+$");
    private static final Pattern YEAR_PATTERN = Pattern.compile("^\\d{4}$");
    private static final Pattern MONTH_PATTERN = Pattern.compile("^(0?[1-9]|1[0-2])$");
    private static final Pattern DAY_PATTERN = Pattern.compile("^(0?[1-9]|[1-2][0-9]|3[0-1])$");
    private static final Pattern NON_DIGIT_NON_DELIM_PATTERN = Pattern.compile("[^\\d._/ -]");
    private static final Pattern DELIM_PATTERN = Pattern.compile("[._/ -]");
    private static final ReleaseDate FALLBACK = ReleaseDate.builder().build();

    @Override
    public ReleaseDate parse(String candidate) {
        if (isNullOrBlank(candidate)) {
            return FALLBACK;
        }
        var date = FALLBACK.withSource(candidate);

        var matcher = YEAR_MONTH_DAY_DATE_PATTERN.matcher(candidate);
        if (matcher.matches()) {
            return parseFullDateWithMatcher(date, matcher);
        }

        matcher = YEAR_MONTH_DATE_PATTERN.matcher(candidate);

        if (matcher.matches()) {
            return parseYearMonthWithMatcher(date, matcher);
        }

        matcher = YEAR_DATE_PATTERN.matcher(candidate);
        if (matcher.matches()) {
            return parseYearWithMatcher(date, matcher);
        }

        return date;
    }

    private ReleaseDate parseYearWithMatcher(ReleaseDate date, Matcher matcher) {
        return applyYear(date, matcher.group("year"));
    }

    private ReleaseDate parseYearMonthWithMatcher(ReleaseDate date, Matcher matcher) {
        var yStr = matcher.group("year");
        var mStr = matcher.group("month");
        date = applyYear(date, yStr);
        date = applyMonth(date, mStr);
        return date;
    }

    private ReleaseDate applyYear(ReleaseDate date, String yearStr) {
        if (isValidYear(yearStr)) {
            date = date.withYear(Integer.parseInt(yearStr), true);
        } else {
            var replaced = replaceNonDigits(yearStr);
            if (isValidYear(replaced) && isNumberStringAbove(replaced, 1000)) {
                date = date.withYear(Integer.parseInt(replaced), false);
            }
        }
        return date;
    }

    private ReleaseDate applyMonth(ReleaseDate date, String monthStr) {
        if (date.isValidYear() && isValidMonth(monthStr)) {
            return date.withMonth(Integer.parseInt(monthStr), true);
        } else {
            var replaced = replaceNonDigits(monthStr);
            if (isValidMonth(replaced)) {
                date = date.withMonth(Integer.parseInt(replaced), false);
            }
        }
        return date;
    }

    private ReleaseDate applyDay(ReleaseDate date, String dayStr) {
        if (date.isValidYear() && date.isValidMonth() && isValidDay(dayStr, date.getYear(), date.getMonth())) {
            date = date.withDay(Integer.parseInt(dayStr), true);
        } else {
            var replaced = replaceNonDigits(dayStr);
            if (isValidDay(replaced, date.getYear(), date.getMonth())) {
                date = date.withDay(Integer.parseInt(replaced), false);
            }
        }
        return date;
    }

    private ReleaseDate parseFullDateWithMatcher(ReleaseDate date, Matcher matcher) {
        var yStr = matcher.group("year");
        var mStr = replaceDelimiters(matcher.group("month"));
        var dStr = matcher.group("day");

        boolean skipMonthDay = false;

        if (isNumberStringOverTwelve(mStr)) {
            var tmpDayStr = mStr.substring(mStr.length() - 1) + dStr;
            var tmpMonthStr = mStr.substring(0, 1);
            if (tmpDayStr.length() <= 2 && !isNumberStringOverThirtyOne(tmpDayStr)) {
                mStr = tmpMonthStr;
                dStr = tmpDayStr;
            } else {
                skipMonthDay = true;
            }
        }

        date = applyYear(date, yStr);
        if (!skipMonthDay) {
            date = applyMonth(date, mStr);
            date = applyDay(date, dStr);
        }
        return date;
    }

    private String replaceDelimiters(String in) {
        return DELIM_PATTERN.matcher(in).replaceAll("");
    }

    private String replaceNonDigits(String in) {
        return NON_DIGIT_NON_DELIM_PATTERN.matcher(in).replaceAll("0");
    }

    private boolean isNumberStringOverTwelve(String in) {
        return isNumberStringAbove(in, 12);
    }

    private boolean isNumberStringOverThirtyOne(String in) {
        return isNumberStringAbove(in, 31);
    }

    private boolean isNumberStringAbove(String in, int limit) {
        if (DIGIT_PATTERN.matcher(in).matches()) {
            return Integer.parseInt(in) > limit;
        }
        return false;
    }
    private boolean isValidYear(String in) {
        return YEAR_PATTERN.matcher(in).matches();
    }

    private boolean isValidMonth(String in) {
        return MONTH_PATTERN.matcher(in).matches();
    }

    private boolean isValidDay(String in, int year, int month) {
        if (year < 0 || month < 1 || month > 12 || !DAY_PATTERN.matcher(in).matches()) {
            return false;
        }
        int day = Integer.parseInt(in);
        return YearMonth.of(year, month).isValidDay(day);
    }

    private boolean isNullOrBlank(String in) {
        return in == null || in.isBlank();
    }
}