package com.as;

import com.github.javafaker.Faker;

import java.io.IOException;
import java.io.Serializable;


public class Employees extends Employee implements Serializable {
    private String insurerName;
    Employees()
    {
        super();
    }
    Employees(boolean generacja) throws IOException {
        super(true);
        Faker faker=new Faker();
        insurerName=faker.company().name();
    }
    void whoAmI()
    {
        super.whoAmI();
        System.out.println("Insurer company name: "+insurerName);
    }
}
