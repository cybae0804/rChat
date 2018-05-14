package com.example.james.rchat;

public class UsersSearch {
    public String name;
    public String status;
    public String image;

    public UsersSearch() {
    }

    public UsersSearch(String name, String status, String image) {
        this.name = name;
        this.image = image;
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

