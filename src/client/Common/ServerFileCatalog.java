package client.Common;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;

import server.Model.*;

public interface ServerFileCatalog extends Remote{
    String FILE_CATALOG_NAME_IN_REGISTRY = "filecatalog";

    long login(Customer customer, ClientFileCatalog client) throws RemoteException, UserException;

    void logout(long key) throws RemoteException,UserException;

    void register(Customer customer) throws RemoteException, UserException, AccountAlreadyExistsException;

    void unRegister(String username) throws RemoteException, UserException;

    void upload(long key, String fileName, byte[] data, String publicFile, String publicWrite, String publicRead) throws IOException, FileException, UnauthorizedAccessException;

    byte[] download(long key, String filename) throws IOException, FileException, UnauthorizedAccessException;

    void deleteFileFromDB(long key, String filename) throws IOException, FileException, UserException, UnauthorizedAccessException;

    void modifyFileContents(long key, String filename, String publicFile, String publicWrite, String publicRead) throws RemoteException, FileException, UnauthorizedAccessException, UserException, FileNotFoundException;

    ArrayList<ProcessFile> listFiles(long key) throws RemoteException;

    void notifyFile(long key, String filename, String notify) throws RemoteException, UserException, UnauthorizedAccessException, FileException, FileNotFoundException;
}
