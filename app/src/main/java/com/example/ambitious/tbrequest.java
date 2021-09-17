package com.example.ambitious;

public class tbrequest {
    String photoKTP;

    public String getPhotoKTP() {
        return photoKTP;
    }

    public String getPhotoWithKTP() {
        return photoWithKTP;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return Email;
    }

    public String getName() {
        return Name;
    }

    String photoWithKTP;
    String uid;
    String Email;
    String Name;
    public tbrequest(){

    }
    public tbrequest(String uid,String Email){
        this.uid = uid;
        this.Email = Email;
    }

}
