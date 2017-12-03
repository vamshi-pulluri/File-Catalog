package server.Model;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException(String reason){
        super(reason);
    }

    public AccountAlreadyExistsException(String reason, Throwable cause){
        super(reason,cause);
    }
}
