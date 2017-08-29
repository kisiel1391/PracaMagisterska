package com.rafal.pracamagisterska.objects;

/**
 * Created by Rafal on 2017-06-09.
 */

public class User {
    private int id;
    private  String name;
    private String surname;
    private String adress;
    private String mail;
    private String phone;
    private String idDriver;

    public User() {
    }

    public User(int id, String name, String surname , String adress, String mail, String phone, String idDriver) {
        this.adress = adress;
        this.id = id;
        this.idDriver = idDriver;
        this.mail = mail;
        this.name = name;
        this.phone = phone;
        this.surname = surname;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "User{" +
                "adress='" + adress + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", mail='" + mail + '\'' +
                ", phone='" + phone + '\'' +
                ", idDriver='" + idDriver + '\'' +
                '}';
    }
}
