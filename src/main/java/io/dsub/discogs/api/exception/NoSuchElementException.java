package io.dsub.discogs.api.exception;

public class NoSuchElementException extends BaseCheckedException {
    private static final String MESSAGE = "no such element";
    private static final NoSuchElementException INSTANCE = new NoSuchElementException();

    public NoSuchElementException() {
        super(MESSAGE);
    }

    public NoSuchElementException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public static NoSuchElementException getInstance() {return INSTANCE;}
}
