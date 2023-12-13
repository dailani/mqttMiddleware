package com.vs.middlewarerobotelection.mqtt.robots;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.vs.middlewarerobotelection.mqtt.robots.Robot;

public class RobotClient {
    public static void main(String[] args) {
        try {
            // Lookup the RMI registry to find the remote robot
            Registry registry = LocateRegistry.getRegistry("localhost", 1099); // Replace with the appropriate host and port
            Robot robot = (Robot) registry.lookup("RobotService");

            // Call a remote method on the robot to send a message
            String message = "Hello, Robot!"; // The message you want to send
            robot.sendMessage("test/topic", "Hello MQTT from " + robot.getIdentifier());

            System.out.println("Message sent to the robot: " + message);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}