package com.unlogicon.typegram.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Nikita Korovkin 18.10.2018.
 */
public class Error {

    @Expose
    @SerializedName("error")
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
