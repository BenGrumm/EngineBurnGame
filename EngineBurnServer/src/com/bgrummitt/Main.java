package com.bgrummitt;

public class Main {

    public static void main(String[] args) {
        DatabaseManager databaseManager;
        System.out.println("Starting");
        try {
            System.out.println("Opening DB");
            databaseManager = new DatabaseManager();
            System.out.println("Getting Scores");
            databaseManager.getGlobalScores();
            System.out.println("Starting Add");
            databaseManager.addNewUser("Al", "75");
            System.out.println("ADD Complete");
            databaseManager.getGlobalScores();
            System.out.println("Fini");
            databaseManager.CloseOutstanding();
        }catch (Exception except){
            System.out.println(except.toString());
            except.printStackTrace();
        }
    }
}
