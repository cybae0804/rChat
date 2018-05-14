package com.example.james.rchat;

public class UsersSearch {
    public String  name, status;

    public UsersSearch(){}

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
    public UsersSearch(String name, String status) {
        this.name = name;
        this.status = status;
    }
}
