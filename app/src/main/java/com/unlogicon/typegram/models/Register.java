package com.unlogicon.typegram.models;

/**
 * Nikita Korovkin 16.10.2018.
 */
public class Register {

    private String username;
    private String password;
    private String privacy;
    private String terms;

    public Register(String username, String password, String privacy, String terms){
        this.username = username;
        this.password = password;
        this.privacy = privacy;
        this.terms = terms;
    }
}
