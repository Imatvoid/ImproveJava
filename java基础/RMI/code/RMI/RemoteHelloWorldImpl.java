package RMI;

import java.rmi.RemoteException;

public class RemoteHelloWorldImpl implements  RemoteHelloWorld{
    @Override
    public String getHelloStr() throws RemoteException {
        return "Hello World";
    }
}
