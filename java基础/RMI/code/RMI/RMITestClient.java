package RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMITestClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost",2099);
            RemoteHelloWorld hello = (RemoteHelloWorld) registry.lookup( "HelloWord");
            String ret = hello.getHelloStr();
            System. out.println( ret);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }

}
