package server.Model;

public class FileException extends Exception {
    public FileException(String reason){
        super(reason);
    }

    public FileException(String reason, Throwable cause){
        super(reason,cause);
    }
}
