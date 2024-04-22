package com.People;

public class PeopleService {
    private String name = "";
    private int age = 0;
    private String gender = "";
    public String getUserDetail(String name, int age, String
    gender) {
        String output = "Name :" + name + "\nage : " + age + "\nGender : " + gender;
        this.name = name;
        this.age = age;
        this.gender = gender;
        return output;
    }
}