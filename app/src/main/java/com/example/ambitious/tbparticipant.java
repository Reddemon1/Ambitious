package com.example.ambitious;

public class tbparticipant {
    public String name;
    public String telp;

    public String getName() {
        return name;
    }

    public String getTelp() {
        return telp;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getUid() {
        return uid;
    }

    public String getImage() {
        return image;
    }

    public String email;
    public String address;
    public String bankAccount;
    public String uid;
    public String image;

    public tbparticipant(){}

    public tbparticipant(String name, String telp , String email, String address, String bankAccount, String uid, String image){
        this.name = name;
        this.telp = telp;
        this.email = email;
        this.address = address;
        this.bankAccount = bankAccount;
        this.uid = uid;
        this.image = image;
    }

}
