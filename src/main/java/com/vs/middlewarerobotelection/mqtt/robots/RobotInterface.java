package com.vs.middlewarerobotelection.mqtt.robots;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RobotInterface extends Remote {
    String getIdentifier() throws RemoteException;
    // Add more methods that you want to expose
}
