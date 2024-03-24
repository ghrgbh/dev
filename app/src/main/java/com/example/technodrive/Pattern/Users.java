package com.example.technodrive.Pattern;

public class Users {
    private  String name,email,password,image,surname,city;
    public Users(){

    }
    public Users(String name, String email, String password,String image,String Surname,String city) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.surname = surname;
        this.city=city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
