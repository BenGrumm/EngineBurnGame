package com.bgrummitt.engineburn.controller.other;

public class LowScoreException extends Exception{

    public LowScoreException(){
        super();
    }

    public LowScoreException(String message){
        super(message);
    }

    public LowScoreException(String message, Throwable cause){
        super(message, cause);
    }

    public LowScoreException(Throwable cause){
        super(cause);
    }

}
