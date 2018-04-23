package com.example.ubuntu.seefood.pojo;

/**
 * Created by jayesh on 10/3/18.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RestResponse {

    @SerializedName("messages")
    @Expose
    private List<String> messages = null;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

}
