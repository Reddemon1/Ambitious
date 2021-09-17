package com.example.ambitious;

public class tbuser {
    public String Name , Email , Status , Image , Bio , Telp , Birth;
    public tbuser(){

    }
    public tbuser(String Name ,String Email ,String Status){
        this.Name = Name;
        this.Email = Email;
        this.Status = Status;
    }
    public tbuser(String Name ,String Email ,String Bio,String Birth , String Telp , String Image,String Status){
        this.Name = Name;
        this.Email = Email;
        this.Bio = Bio;
        this.Birth = Birth;
        this.Telp = Telp;
        this.Image = Image;
        this.Status = Status;
    }
}
