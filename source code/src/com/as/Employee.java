package com.as;

import com.github.javafaker.Faker;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.Date;

import java.math.BigDecimal;


import static java.util.concurrent.TimeUnit.DAYS;


public class Employee extends Pearson implements Serializable {
    private String employerName;
    private String employementDate;
    private BigDecimal salary;
    private String jobtitle;
    private transient BufferedImage employeeFace;
    private byte[] imageBytes;
    Employee(){
        super();
    }

    Employee(boolean generacja) throws IOException {
        super(true);
        Date dzis2,dzis3;
        Faker dzis=new Faker();
        dzis2=dzis.date().future(15,1,DAYS);
        dzis3=dzis.date().past(100,10,DAYS);
        Faker faker=new Faker();
        employerName=faker.name().fullName();
        employementDate=super.getDateFormat().format(faker.date().between(dzis3,dzis2));
        salary=BigDecimal.valueOf((Math.random()*1200)+3000);
        jobtitle=faker.job().title();
        create generator=new create();
        employeeFace=generator.createGraphics();
        imageBytes=enbuffer(employeeFace);
    }
    void whoAmI()
    {
        super.whoAmI();
        System.out.println("Employer name: "+employerName);
        System.out.println("Emloyment date: "+employementDate);
        System.out.println("Salary: "+salary);
        System.out.println("Job title: "+jobtitle);
    }
    public String getEmployementDate(){ return employementDate; }
    public BigDecimal getSalary(){ return salary; }

    public String getEmployerName() {
        return employerName;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setEmployementDate(String employementDate) {
        this.employementDate = employementDate;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BufferedImage getEmployeeFace() {
        return employeeFace;
    }
    public void setemployeeFace(BufferedImage employeeIamge) {
        this.employeeFace = employeeIamge;
    }

    public byte[] getImageButByte() {
        return imageBytes;
    }

    public void setImageButByte(byte[] imageButByte) {
        this.imageBytes = imageButByte;
    }

    public byte[] enbuffer(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", baos);
        byte[] bytes = baos.toByteArray();
        return bytes;
    }

    public BufferedImage debuffer(byte[] bytearray) throws IOException {
        InputStream in = new ByteArrayInputStream(bytearray);
        return  ImageIO.read(in);
    }
}
