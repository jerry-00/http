package com.muxi.http.model;

import lombok.Data;

@Data
public class User {

    private String name;
    private Integer age;
    private String sex;
    private String motto;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", motto='" + motto + '\'' +
                '}';
    }
}
