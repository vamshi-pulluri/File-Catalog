package server.Controller;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collection;

import server.Model.*;

import client.Common.Customer;
import client.Common.ClientFileCatalog;
import client.Common.ServerFileCatalog;

public class Controller extends UnicastRemoteObject implements ServerFileCatalog{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final AccountManager accountManager;
    private final FileHandler fileHandler;


    public Controller() throws RemoteException{
        super();
        accountManager = new AccountManager();
        fileHandler = new FileHandler();
    }

    @Override
    public long login(Customer customer, ClientFileCatalog client) throws RemoteException {
        long key = accountManager.login(customer);
        Account account = accountManager.getUser(key);
        if(account != null){
            fileHandler.addNotifyUser(account.getUserID(),client);
        }
        return key;
    }

    @Override
    public void logout(long key) throws RemoteException, UserException {
        accountManager.logout(key);
    }

    @Override
    public void register(Customer customer) throws AccountAlreadyExistsException {
    	System.out.println("hello all controller");
        accountManager.register(customer);

    }

    @Override
    public void unRegister(String username) throws UserException {
        accountManager.unRegister(username);
    }

    @Override
    public void upload(long key, String fileName, byte[] data, String publicFile, String publicWrite, String publicRead) throws IOException, FileException, UnauthorizedAccessException {
        Account account = accountManager.getUser(key);
        if(account != null){
            fileHandler.uploadFile(account,fileName, data, publicFile, publicWrite, publicRead);
        }
        else{
            throw new UnauthorizedAccessException("Account not logged in.");
        }
    }

    @Override
    public byte[] download(long key, String filename) throws IOException, FileException, UnauthorizedAccessException {
        Account account = accountManager.getUser(key);
        if(account == null){
            throw new UnauthorizedAccessException("Account not logged in!");
        }
        return fileHandler.downloadFile(account, filename);
    }


    @Override
    public void deleteFileFromDB(long key, String filename) throws IOException, FileException, UserException, UnauthorizedAccessException {
        Account account = accountManager.getUser(key);
        if(account != null){
            fileHandler.deleteFile(account,filename);
        }
        else{
            throw new UserException("Account is not logged in!");
        }
    }

    @Override
    public void modifyFileContents(long key, String filename, String publicFile, String publicWrite, String publicRead) throws RemoteException, FileException, UnauthorizedAccessException, UserException, FileNotFoundException {
        Account account = accountManager.getUser(key);
        if(account != null){
            fileHandler.modifyFile(account,filename,publicFile,publicWrite,publicRead);
        }
        else{
            throw new UserException("Account is not logged in!");
        }
    }

    @Override
    public ArrayList<ProcessFile> listFiles(long key) throws RemoteException {
        Account account = accountManager.getUser(key);
        long userID;
        if(account != null){
            userID = account.getUserID();
        }
        else{
            userID = -1;
        }
        return fileHandler.listFiles(userID);
    }

    @Override
    public void notifyFile(long key, String filename, String notify) throws RemoteException, UserException, UnauthorizedAccessException, FileException, FileNotFoundException {
        Account account = accountManager.getUser(key);
        if(account != null){
            fileHandler.notifyFile(account,filename,notify);
        }
        else{
            throw new UserException("Account is not logged in!");
        }
    }
}
