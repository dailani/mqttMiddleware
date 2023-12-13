package com.vs.middlewarerobotelection.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttMiddlewareClient {
    private static final String BROKER_URL = "tcp://test.mosquitto.org:1883";
    private static final String CLIENT_ID = "MyMiddlewareClient";

    public static void main(String[] args) {
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient mqttClient = new MqttClient(BROKER_URL, CLIENT_ID, persistence);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable throwable) {
                    // Handle connection loss
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    System.out.println("Received message on topic: " + topic + ". Message: " + new String(message.getPayload()));

                    if (topic.equals("election/initiate")) {
                        System.out.println("Election initiation message received.");
                        handleElectionInitiate(mqttClient, "Robot123"); // Replace with actual identifier
                    } else if (topic.equals("election/vote")) {
                        System.out.println("Vote message received.");
                        handleVote();
                    } else if (topic.equals("election/result")) {
                        System.out.println("Election result message received.");
                        handleResultAnnouncement();
                    } else {
                        // Handle other topic logic here if needed
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Called when a message has been delivered to the server
                }
            });

            mqttClient.connect();
            System.out.println("Connected to MQTT broker");

            // Subscribe to election topics
            mqttClient.subscribe("election/#");
            System.out.println("Subscribed to election topics");

            // Additional code to publish messages for initiating elections, casting votes, etc.
            initiateElection(mqttClient);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void handleElectionInitiate(MqttClient client, String initiatorId) {
        // Implement logic for election initiation phase
        // Send initiation messages to robots or perform other actions
        String electionInitiateMessage = "ElectionInitiate:" + initiatorId;
        forwardElectionMessage(client, electionInitiateMessage);
    }

    private static void forwardElectionMessage(MqttClient client, String messageContent) {
        String nextRobotTopic = "election/initiate"; // Replace with the actual topic for the next robot
        publishMessage(client, nextRobotTopic, messageContent);
    }

    private static void handleVote() {
        // Implement logic for the voting phase
        // Collect and process votes
        String voteMessage = "Vote:" + CLIENT_ID; // Include the client's ID or other identifier
        String voteTopic = "election/vote";
        publishMessage(voteTopic, voteMessage);
    }

    private static void handleResultAnnouncement() {
        // Implement logic for announcing the election result
        // Determine the winner and send the result
        String leaderId = "WinnerRobot"; // Replace with the actual winner's ID
        String leaderAnnouncementMessage = "LeaderAnnouncement:" + leaderId;
        String resultTopic = "election/result";
        publishMessage(resultTopic, leaderAnnouncementMessage);
    }

    private static void initiateElection(MqttClient client) {
        // Implement logic for initiating the election
        // Send initiation message
        String initiationMessage = "Initiate Election:" + CLIENT_ID; // Include the client's ID or other identifier
        String initiateTopic = "election/initiate";
        publishMessage(client, initiateTopic, initiationMessage);
    }

    private static void publishMessage(String topic, String messageContent) {
        publishMessage(null, topic, messageContent);
    }

    private static void publishMessage(MqttClient client, String topic, String messageContent) {
        if (client != null && client.isConnected()) {
            try {
                MqttMessage message = new MqttMessage(messageContent.getBytes());
                client.publish(topic, message);
                System.out.println("Sent message to topic: " + topic + ". Message: " + messageContent);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("MQTT client is not connected. Cannot send message.");
        }
    }

    private static void announceLeader(MqttClient client, String leaderId) {
        String leaderAnnouncementMessage = createLeaderAnnouncementMessage(leaderId);
        String resultTopic = "election/result";
        publishMessage(client, resultTopic, leaderAnnouncementMessage);
    }

    // Method to create a leader announcement message
    private static String createLeaderAnnouncementMessage(String leaderId) {
        return "LeaderAnnouncement:" + leaderId;
    }

}
