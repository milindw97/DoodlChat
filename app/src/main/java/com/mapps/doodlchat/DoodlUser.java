package com.mapps.doodlchat;


class DoodlUser {

    public String name, email, username, status;

    public DoodlUser() {
    }

    DoodlUser(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
        status = "Available";
    }

    public String getname() {
        return name;
    }

    public String getemail() {
        return email;
    }

    public String getstatus() {
        return status;
    }

    public String getusername() {
        return username;
    }
}

