package server.Model;

public class UserException extends Exception {
    public UserException(String reason){
        super(reason);
    }

    public UserException(String reason, Throwable cause){
        super(reason,cause);
    }
}
