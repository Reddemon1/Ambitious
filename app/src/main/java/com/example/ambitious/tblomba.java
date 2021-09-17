package com.example.ambitious;

public class tblomba {
    public String getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public String getUid() {
        return uid;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOpenDate() {
        return openDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getNoParticipant() {
        return noParticipant;
    }
    public String getUserid() {
        return userid;
    }

    public String getCategory() {
        return category;
    }
    public String getUsername() {
        return username;
    }
    public String image;
    public String name;
    public String desc;
    public String releaseDate;
    public String openDate;
    public String endDate;



    public String email;
    public String userid;
    public String imageCertificate;

    public String noParticipant;
    public String category;
    public String username;
    public String uid;

    public tblomba(String image, String name, String desc, String releaseDate, String openDate, String endDate , String category , String username , String uid , String noParticipant ,String userid,String imageCertificate){
        this.image = image;
        this.name = name;
        this.desc = desc;
        this.releaseDate = releaseDate;
        this.openDate = openDate;
        this.endDate = endDate;
        this.category = category;
        this.username = username;
        this.uid = uid;
        this.noParticipant = noParticipant;
        this.userid = userid;
        this.imageCertificate = imageCertificate;
    }
    public tblomba(){}
    public tblomba(String email){
        this.email = email;
    }


}
