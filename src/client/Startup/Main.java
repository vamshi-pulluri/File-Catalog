package client.Startup;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import client.Common.ServerFileCatalog;
import client.View.UserInterface;

public class Main {

    public static void main(String args[]){
        try{
            ServerFileCatalog server = (ServerFileCatalog) Naming.lookup(ServerFileCatalog.FILE_CATALOG_NAME_IN_REGISTRY);
            new UserInterface().start(server);
            System.out.println("Client Started");
        }
        catch (RemoteException | MalformedURLException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
