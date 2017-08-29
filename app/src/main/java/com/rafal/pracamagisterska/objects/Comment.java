package com.rafal.pracamagisterska.objects;

/**
 * Created by Rafal on 2017-06-09.
 */

public class Comment {

    private int id;
    private int concernsId;
    private int fromId;
    private String type;
    private String message;

    public Comment() {
    }

    public Comment(int id, int concernsId, int fromId, String type, String message) {
        this.concernsId = concernsId;
        this.fromId = fromId;
        this.id = id;
        this.message = message;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "concernsId=" + concernsId +
                ", id=" + id +
                ", fromId=" + fromId +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
