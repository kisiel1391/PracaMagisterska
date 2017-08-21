package com.rafal.pracamagisterska.objects;

/**
 * Created by Rafal on 2017-06-09.
 */

public class Rate {
    private int id;
    private int concernsId;
    private int fromId;
    private Double value;
    private String message;

    public Rate() {
    }

    public Rate(int id, int concernsId, int fromId, Double value, String message) {
        this.concernsId = concernsId;
        this.fromId = fromId;
        this.id = id;
        this.message = message;
        this.value = value;
    }

    public int getConcernsId() {
        return concernsId;
    }

    public void setConcernsId(int concernsId) {
        this.concernsId = concernsId;
    }

    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Rate{" +
                "concernsId=" + concernsId +
                ", id=" + id +
                ", fromId=" + fromId +
                ", value=" + value +
                ", message='" + message + '\'' +
                '}';
    }
}
