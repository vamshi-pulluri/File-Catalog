package server.Model;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import server.Integration.FileServerDAO;
import server.Manager.ServerManager;

import client.Common.ClientFileCatalog;
import client.Common.DTOFIile;

public class FileHandler {
    private HashMap<Long,ClientFileCatalog> notifyUsers = new HashMap<Long, ClientFileCatalog>();

    ServerManager s= new ServerManager();
    public byte[] downloadFile(Account account, String filename) throws IOException, FileException, UnauthorizedAccessException {
        FileServerDAO fileServerDAO= new FileServerDAO();
    	ProcessFile file = fileServerDAO.findFileByName(filename, true);

        if(file == null){
            throw new FileException("File not found");
        }

        byte[] result;

        if(file.getOwner().getUserID() == account.getUserID()){
        	System.out.println("reading new");
            result = s.readFile(filename);
            
        }
        else if(file.getPublicFile().equals("true") && file.getPublicRead().equals("true")){
            result= s.readFile(filename);
            /*if(file.getNotify().equalsIgnoreCase("true")){System.out.println("notify");
                ClientFileCatalog client = notifyUsers.get(file.getOwner().getUserID());
                if(client != null){
                    try{
                        client.handleMsg(new DTOFIile(filename, account.getUsername(),"File read"));
                    }
                    catch(RemoteException e){
                        System.out.println("Could not notify user " + file.getOwner().getUsername());
                    }

                }
            }*/
        }
        else{
            throw new UnauthorizedAccessException("Permission denied");
        }

        return result;
    }

    public ArrayList<ProcessFile> listFiles(long userID){
    	FileServerDAO fileServerDAO= new FileServerDAO();
        return fileServerDAO.getUserFiles(userID);
    }

    public void deleteFile(Account account, String filename) throws FileException, IOException, UnauthorizedAccessException {
    	FileServerDAO fileServerDAO= new FileServerDAO();
    	ProcessFile file = fileServerDAO.findFileByName(filename,true);
        if(file != null){
            if(file.getOwner().getUserID() == account.getUserID()){
                fileServerDAO.deleteFile(file);
                s.deleteFile(filename);
                System.out.println("Deleted File Successfuly");
            }
            else if(file.getPublicWrite().equals("true")){
                fileServerDAO.deleteFile(file);
                s.deleteFile(filename);
                /*if(file.getNotify().equalsIgnoreCase("true")){
                    ClientFileCatalog client = notifyUsers.get(file.getOwner().getUserID());
                    if(client != null){
                        try{
                            client.handleMsg(new DTOFIile(filename, account.getUsername(),"File deleted"));
                        }
                        catch(RemoteException e){
                            System.out.println("Could not notify user " + file.getOwner().getUsername());
                        }
                    }
                }*/
            }
            else{
                throw new UnauthorizedAccessException("Permission denied to delete file");
            }
        }
        else{
            throw new FileNotFoundException("File "+ filename +" not found");
        }
    }

    public void modifyFile(Account account, String filename, String publicFile, String publicWrite, String publicRead) throws UnauthorizedAccessException, FileNotFoundException {
    	FileServerDAO fileServerDAO= new FileServerDAO();
    	ProcessFile processFile = fileServerDAO.findFileByName(filename,false);System.out.println(account.getUsername());
    	System.out.println(processFile.getOwner().getUsername());
        if(processFile != null && processFile.getOwner().getUsername().equalsIgnoreCase(account.getUsername())){
            processFile.setPublicFile(publicFile);
            processFile.setPublicWrite(publicWrite);
            processFile.setPublicRead(publicRead);
            processFile.setUsername(account.getUsername());
            fileServerDAO.updateFile(processFile.getName(),publicFile,publicWrite,publicRead);
            System.out.println("modified file");
        }
       else {
            throw new UnauthorizedAccessException("Permission denied to modify file!");
        }
    }

    public void uploadFile(Account owner, String filename, byte[] data, String publicFile, String publicWrite, String publicRead) throws IOException, FileException, UnauthorizedAccessException {
    	FileServerDAO fileServerDAO= new FileServerDAO();
    	ProcessFile processFile = fileServerDAO.findFileByName(filename,false);
        if(processFile == null){
        	System.out.println("upload"+"in here"+ " "+ owner.getUsername());
            fileServerDAO.createFile(new ProcessFile(filename,data.length,owner.getUsername(),publicFile,publicWrite,publicRead));
            s.writeFile(filename, data);
            System.out.println("uploaded file");
        }
        else if(processFile.getOwner().getUserID() == owner.getUserID()){
        	s.writeFile(filename, data);
            processFile.setSize(data.length);
            processFile.setPublicFile(publicFile);
            processFile.setPublicWrite(publicWrite);
            processFile.setPublicRead(publicRead);
            fileServerDAO.updateFile(processFile.getName(),publicFile,publicWrite,publicRead);
        }
        else if(processFile.getPublicWrite().equals("true")){
        	s.writeFile(filename, data);
            processFile.setSize(data.length);
            fileServerDAO.updateFile(processFile.getName(),publicFile,publicWrite,publicRead);
            /*if(processFile.getNotify().equalsIgnoreCase("true")){
                ClientFileCatalog client = notifyUsers.get(processFile.getOwner().getUserID());
                if(client != null){
                    try{
                        client.handleMsg(new DTOFIile(filename, owner.getUsername(),"File overwritten"));
                    }
                    catch(RemoteException e){
                        System.out.println("Could not notify user " + processFile.getOwner().getUsername());
                    }

                }
            }*/
        }
        else{
            throw new UnauthorizedAccessException("Write access to file denied");
        }
    }

    public void notifyFile(Account account, String filename, String notify) throws UnauthorizedAccessException, FileNotFoundException {
    	/*FileServerDAO fileServerDAO= new FileServerDAO();
    	ProcessFile file = fileServerDAO.findFileByName(filename,false);
        if(file != null && file.getOwner().getUserID() == account.getUserID()){
            file.setNotify(notify);
            fileServerDAO.updateFile(file);
        }
        else if(file != null){
            fileServerDAO.updateFile(file);
            throw new UnauthorizedAccessException("Permission denied to modify file!");
        }
        else{
            fileServerDAO.updateFile(file);
            throw new FileNotFoundException("File "+ filename +" not found!");
        }*/
    }

    public void addNotifyUser(long userID, ClientFileCatalog client) {
        notifyUsers.put(userID,client);
    }
}