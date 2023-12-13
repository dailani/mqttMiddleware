package com.vs.middlewarerobotelection.mqtt.robots;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RobotServer {
    public static void main(String[] args) {
        try {
            // Create a new Robot
            Robot robot = new Robot("Robot123");

            // Create RMI registry and bind the robot
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("RobotService", robot);

            System.out.println("Robot server is running");

            // Simulate robot actions (e.g., casting votes)
            simulateRobotActions(robot);
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    // Method to simulate robot actions (e.g., casting votes)
    private static void simulateRobotActions(Robot robot) {
        // Simulate casting votes by calling the castVote method with candidate IDs
        robot.castVote("CandidateA");
        robot.castVote("CandidateB");
        robot.castVote("CandidateA");
    }
}
