package com.vs.middlewarerobotelection.mqtt.robots;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Robot extends UnicastRemoteObject implements RobotInterface {
    private String identifier;
    private MqttClient mqttClient; // MQTT client instance

    public Robot(String identifier) throws RemoteException {
        super();
        this.identifier = identifier;

        // Initialize the MQTT client during robot creation
        String broker = "tcp://mqtt.eclipse.org:1883"; // Replace with your MQTT broker URL
        String clientId = "Robot-" + identifier; // Unique client ID for the robot
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            mqttClient = new MqttClient(broker, clientId, persistence);
            mqttClient.connect();
            System.out.println("Connected to MQTT broker as " + clientId);

            // Subscribe to topics or perform other MQTT setup here if needed
            // mqttClient.subscribe("your/topic");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getIdentifier() throws RemoteException {
        return identifier;
    }

    // Add a method to handle incoming messages
    public void handleIncomingMessage(String topic, String messageContent) {
        System.out.println("Message received by " + identifier + " on topic " + topic + ": " + messageContent);
        // Add logic to process the received message here
    }

    // Method to send an MQTT message
    public void sendMessage(String topic, String messageContent) {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                MqttMessage message = new MqttMessage(messageContent.getBytes());
                mqttClient.publish(topic, message);
                System.out.println("Sent message from " + identifier + " to topic: " + topic + ". Message: " + messageContent);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("MQTT client is not connected. Cannot send message.");
        }
    }

    // Method to cast a vote
    public void castVote(String candidateId) {
        String voteTopic = "election/vote";
        String voteMessage = "Vote:" + candidateId;
        sendMessage(voteTopic, voteMessage);
        System.out.println(identifier + " cast a vote for " + candidateId);
    }

    // Implement other remote methods
}
