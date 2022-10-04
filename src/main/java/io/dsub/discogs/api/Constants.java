package io.dsub.discogs.api;

public class Constants {
    private Constants() {}

    public static final String POSTGRES_SCHEMA_DIR = "postgres-schema.sql";
    public static final String DB_USER_ENV_KEY = "DISCOGS_API_DB_USER";
    public static final String DB_PASS_ENV_KEY = "DISCOGS_API_DB_PASS";
    public static final String DB_URL_ENV_KEY = "DISCOGS_API_DB_URL";

    public static final int DEFAULT_PAGE_SIZE = 30;
    public static final int DEFAULT_PAGE_INDEX = 0;
}
