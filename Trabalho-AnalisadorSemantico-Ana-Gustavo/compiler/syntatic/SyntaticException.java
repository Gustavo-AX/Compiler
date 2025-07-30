package syntatic;

public class SyntaticException extends RuntimeException {

    public SyntaticException(int line, String reason) {
       super(String.format("%02d: %s", line, reason));
    }

}