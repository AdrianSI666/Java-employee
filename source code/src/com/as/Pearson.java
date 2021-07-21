package com.as;

import com.github.javafaker.Faker;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Pearson implements Serializable {
    static int i=1;
    private int id;
    private String firstname;
    private String surname;
    private String addressLine;
    private String birthDate;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    private Integer money;
    Pearson(){

    }
    Pearson(boolean generacja)
    {
        id=i;
        Faker faker=new Faker();
        firstname=faker.name().firstName();
        surname=faker.name().lastName();
        addressLine=faker.address().fullAddress();
        birthDate = dateFormat.format(faker.date().birthday());
        i=i+1;
        money= (int)Math.floor(Math.random()*1000);
    }
    void whoAmI()
    {
        System.out.println("ID: "+id);
        System.out.println("First name: "+firstname);
        System.out.println("Surname: "+surname);
        System.out.println("address: "+addressLine);
        System.out.println("Birth Date: "+birthDate);
    }
    public String getSurname()
    {
        return surname;
    }
    public String getBirthDate()
    {
        return birthDate;
    }
    public int getId() {
        return id;
    }


    public String idtoString() {
        return ""+id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public static void dodajI() {
        Pearson.i = Pearson.i+1;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getMoney() {
        return money;
    }
}
