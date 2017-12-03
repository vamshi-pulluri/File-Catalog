package server.Model;

public class UnauthorizedAccessException extends Exception {
    public UnauthorizedAccessException(String reason){
        super(reason);
    }

    public UnauthorizedAccessException(String reason, Throwable cause){
        super(reason,cause);
    }
}
