package com.unlogicon.typegram.models;

/**
 * Nikita Korovkin 16.10.2018.
 */
public class Register {

    private String username;
    private String password;
    private boolean privacy;
    private boolean terms;

    public Register(String username, String password, boolean privacy, boolean terms){
        this.username = username;
        this.password = password;
        this.privacy = privacy;
        this.terms = terms;
    }
}
