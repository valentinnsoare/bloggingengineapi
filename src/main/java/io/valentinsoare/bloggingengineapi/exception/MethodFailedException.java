package io.valentinsoare.bloggingengineapi.exception;

public class MethodFailedException extends RuntimeException {
    private final String msg;

    public MethodFailedException(String msg) {
        super(String.format("%s", msg));
        this.msg = msg;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
