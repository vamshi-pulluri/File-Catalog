package client.Common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientFileCatalog extends Remote{

    void handleMsg(DTOFIile message) throws RemoteException;
}
