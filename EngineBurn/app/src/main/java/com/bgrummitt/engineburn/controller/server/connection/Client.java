package com.bgrummitt.engineburn.controller.server.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.bgrummitt.engineburn.controller.leaderboard.UserScore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;

public class Client {

    final static public String TAG = Client.class.getSimpleName();

    // TODO Update when needed
    final static private String ip = "?";
    final static public int PORT = 9001;

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    /**
     * Function to start up the connection to the server
     * @throws IOException if a connection cannot be established
     */
    public void Start() throws IOException{
        // Connect to the socket
        Log.d(TAG, "Starting Up Socket");
        socket = new Socket(ip, PORT);

        // Create new in out streams for receiving and sending info
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Receive a new line
        String line = in.readLine();

        Log.d(TAG, line);

        // If the line does not signal a succesfull connection close the socket
        if(!line.equals("CONNECTED")){
            socket.close();
            throw new IOException("Could Not Successfully Open Socket");
        }
        Log.d(TAG, "Connected To Socket");
    }

    /**
     * Function to get the scores from the server
     * @return array of UserScore with information retrieved from server
     * @throws IOException if there is a problem with the connection
     */
    public UserScore[] getScores() throws IOException{
        Log.d(TAG, "Retrieving Scores");
        UserScore[] userScores = new UserScore[10];

        // Send signal to get return the scores
        out.println("GET_SCORES");

        // Create an array of length three for strings
        String[] user = new String[3];
        String input;

        // For each user and then for each piece of information for that user
        for(int i = 0; i < 10; i++){
            for(int x = 0; x < 3; x++){
                // Read the information and then store the information in the user array
                input = in.readLine();
                user[x] = input;
            }
            // Store the information retrieved in a UserScore class and add to userScores array
            userScores[i] = new UserScore(user[0], user[1], user[2]);
        }

        Log.d(TAG, "UserScores Collected");

        return userScores;
    }

    /**
     * Function to add the a score to the database of top scores on server
     * @param userScore the users information to add
     * @throws IOException if there is a problem with the connection
     */
    public void addScore(UserScore userScore) throws IOException{
        // Send message to expect score
        out.println("ADD_SCORE");

        //TODO Add full score save function

        String inputLine = in.readLine();
        Log.d(TAG, inputLine);
    }

    /**
     * Function to close the socket connection
     * @throws IOException if there is a problem closing the connection
     */
    public void close() throws IOException{
        // If the socket is not already closed close it
        if(!socket.isClosed()){
            socket.close();
        }
    }

}