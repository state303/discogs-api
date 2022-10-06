package io.dsub.discogs.api.core.exception;

public class InvalidR2DBCConnectionUrlException extends BaseCheckedException {
    private static final String MESSAGE = "invalid r2dbc connection url";

    public InvalidR2DBCConnectionUrlException() {
        super(MESSAGE);
    }
}
