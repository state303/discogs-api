package io.dsub.discogs.api.core.exception;

public class ItemNotFoundException extends BaseCheckedException {
    public ItemNotFoundException(String message) {
        super(message);
    }
}