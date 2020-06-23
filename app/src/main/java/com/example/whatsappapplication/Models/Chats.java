package com.example.whatsappapplication.Models;

public class Chats {
    private String chat_Id;
    private String name;
    private String profilePic;

    public Chats(String chat_Id, String name, String profilePic) {
        this.chat_Id = chat_Id;
        this.name = name;
        this.profilePic = profilePic;
    }

    public Chats() {
    }

    public String getChat_Id() {
        return chat_Id;
    }

    public void setChat_Id(String chat_Id) {
        this.chat_Id = chat_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}
