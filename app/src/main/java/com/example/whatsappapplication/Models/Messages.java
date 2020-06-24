package com.example.whatsappapplication.Models;

public class Messages {
    private String message_Id;
    private String from;
    private String message;
    private String type;
    private String to;
    private long timestamp;

    public Messages(String message_Id, String from, String message, String type, String to, long timestamp) {
        this.message_Id = message_Id;
        this.from = from;
        this.message = message;
        this.type = type;
        this.to = to;
        this.timestamp = timestamp;
    }

    public Messages() {
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage_Id() {
        return message_Id;
    }

    public void setMessage_Id(String message_Id) {
        this.message_Id = message_Id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
