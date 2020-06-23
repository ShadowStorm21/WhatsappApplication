package com.example.whatsappapplication.Models;

public class Chats {
    private String chat_Id;
    private String receiverUsername;
    private String message;
    private long timestamp;
    private boolean seen;


    public Chats(String chat_Id, String receiverUsername, String message, long timestamp, boolean seen) {
        this.chat_Id = chat_Id;
        this.receiverUsername = receiverUsername;
        this.message = message;
        this.timestamp = timestamp;
        this.seen = seen;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Chats() {
    }

    public String getChat_Id() {
        return chat_Id;
    }

    public void setChat_Id(String chat_Id) {
        this.chat_Id = chat_Id;
    }

}
