package RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteHelloWorld extends Remote {
    String getHelloStr() throws RemoteException;
}
