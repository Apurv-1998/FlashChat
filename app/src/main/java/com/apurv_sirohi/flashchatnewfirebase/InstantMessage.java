package com.apurv_sirohi.flashchatnewfirebase;

//This class takes care of a single chat message that is sent

public class InstantMessage {

    private String message;
    private String author;

    public InstantMessage(String message,String author)
    {
        this.message = message;
        this.author = author;
    }

    //Now as per firebase rules we have to add an extra no argument constructor and getters


    public InstantMessage() {

    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }
}
