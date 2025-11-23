package uz.ems.maydon24.exeption;

public class InvalidHeadersException extends RuntimeException  {
    public InvalidHeadersException(String msg) {
        super(msg);
    }

    public InvalidHeadersException(String msg, Throwable cause) {
        super(msg, cause);
    }
}