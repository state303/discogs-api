package io.dsub.discogs.api.exception;

public class ItemNotFoundException extends BaseCheckedException {
    private static final String DEFAULT_MESSAGE = "failed to locate item";
    private static final ItemNotFoundException INSTANCE = new ItemNotFoundException();

    public ItemNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public ItemNotFoundException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public static ItemNotFoundException getInstance() {return INSTANCE;}
}
