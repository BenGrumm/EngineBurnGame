package com.bgrummitt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{

    public static final int PORT = 9001;

    private static DatabaseManager serverDatabaseManager;
    private static String[][] leaderboardScores;
    private static Boolean isServerError = false;

    public static void main(String[] args) throws Exception {
        // Create a new server socket with the PORT to open
        ServerSocket listener = new ServerSocket(PORT);
        // Create new DatabaseManager in a static var
        serverDatabaseManager = new DatabaseManager();
        try {
            updateLeaderboardList();
            // Create infinite loop to accept new connections and print when the device connected
            while (true) {
                System.out.println("New Device Connected");
                new Thread(new Handler(listener.accept())).start();
            }
        }
        // Make sure to close the listener when the program is closing
        finally {
            listener.close();
            serverDatabaseManager.CloseOutstanding();
        }
    }

    /**
     * Synchronized function to update the leaderboard list so if multiple calls at one time it will be active at one point
     * in time
     */
    synchronized private static void updateLeaderboardList(){
        try {
            leaderboardScores = serverDatabaseManager.getGlobalScores();
        }catch (Exception e){
            isServerError = true;
        }
    }

    // Create a Handler class that implements a runnable so each user can run on its own thread
    // and multiple connections at the same time do not clog the system
    private static class Handler implements Runnable {

        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        private String receive;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                // Tell the connecting device that a stream has been successfully created
                out.println("CONNECTED");
                // While messages are being sent that are != null
                while((receive = in.readLine()) != null){
                    System.out.println("RUNNING");
                    // If the input is not null and the input is the command GET_SCORES
                    if(receive != null && receive.equals("GET_SCORES")){

                        if(isServerError || leaderboardScores[0] == null){
                            out.println("ERROR_WITH_SERVER_DATABASE");
                            break;
                        }

                        // Return all of the leaderboard scores
                        for(int i = 0; i < 10; i++){
                            out.println(leaderboardScores[i][0]);
                            out.println(leaderboardScores[i][1]);
                            out.println(leaderboardScores[i][2]);
                        }
                    }
                    // If the input is not null and the input is the command ADD_SCORE
                    else if(receive != null && receive.equals("ADD_SCORE")){
                        // Temporary output
                        out.println("SEND_SCORE");
                        String name = in.readLine();
                        String score = in.readLine();
                        try {
                            serverDatabaseManager.addNewUser(name, score);
                            out.println("SCORE_SUCCESSFULLY_ADDED");
                        }catch (Exception e){
                            out.println("ERROR_ADDING_SCORE");
                        }
                        updateLeaderboardList();
                    }
                }
            } catch (IOException e) {
                // If there is an error print the current received command
                System.out.println(receive);
            } finally {
                // This client is going down! Finish and close any outstanding
                try {
                    in.close();
                    out.close();
                    System.out.println("User Disconnected");
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        }

    }

}