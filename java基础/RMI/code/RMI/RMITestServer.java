package RMI;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMITestServer {

    public static void main(String[] args) {
        try {
            RemoteHelloWorld hello=new RemoteHelloWorldImpl();
            RemoteHelloWorld stub=(RemoteHelloWorld)UnicastRemoteObject.exportObject(hello,9999);
           // RemoteHelloWorld stub=(RemoteHelloWorld)UnicastRemoteObject.exportObject(hello,9999);
            Registry registry= LocateRegistry.createRegistry(2099);
           // Registry registry=LocateRegistry.getRegistry();
            registry.bind("HelloWord", stub);
            System.out.println("绑定成功!");
        } catch (RemoteException  | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

}
