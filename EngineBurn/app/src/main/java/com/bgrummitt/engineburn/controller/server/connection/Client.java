package com.bgrummitt.engineburn.controller.server.connection;

import android.util.Log;

import com.bgrummitt.engineburn.controller.leaderboard.UserScore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.IOException;

public class Client{

    final static public String TAG = Client.class.getSimpleName();

    // TODO Update when needed
    final static private String ip = "?";
    final static public int PORT = 9001;

    private BufferedReader in;
    private PrintWriter out;
    private Socket socket;

    public static void main(String[] args){
        Client user = new Client();
        try{
            user.run();
            UserScore[] scores = user.getScores();
            for(int i = 0; i < 10; i++){
                System.out.println(String.format("Name = %s, Score = %s, Position = %s", scores[i].getName(), scores[i].getScore(), scores[i].getPosition()));
            }
        }catch(IOException except){
            System.out.println(except.toString());
        }
    }

    public void run() throws IOException{
        socket = new Socket(ip, PORT);

        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        String line = in.readLine();

        Log.d(TAG, line);

        if(!line.equals("CONNECTED")){
            socket.close();
            throw new IOException("Could Not Successfully Open Socket");
        }
    }

    public UserScore[] getScores() throws IOException{
        UserScore[] userScores = new UserScore[10];

        out.println("GET_SCORES");

        String[] user = new String[3];
        String input;

        for(int i = 0; i < 10; i++){
            for(int x = 0; x < 3; x++){
                input = in.readLine();
                user[x] = input;
            }
            userScores[i] = new UserScore(user[0], user[1], user[2]);
        }

        return userScores;
    }

    public void addScore(UserScore userScore) throws IOException{
        out.println("ADD_SCORE");

        //TODO Add full score save function

        String inputLine = in.readLine();
        Log.d(TAG, inputLine);
    }

    public void close() throws IOException{
        if(!socket.isClosed()){
            socket.close();
        }
    }

}