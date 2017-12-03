package server.Startup;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.Controller.Controller;

public class Main {
    public static void main(String args[]){
        try{
            new Main().startRegistry();
            Naming.rebind(Controller.FILE_CATALOG_NAME_IN_REGISTRY,new Controller());
            System.out.println("Start server");
        }
        catch(RemoteException | MalformedURLException e) {
            System.out.println("Sorry, unable to start");
        }
    }

    private void startRegistry() throws RemoteException{
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException e) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}
