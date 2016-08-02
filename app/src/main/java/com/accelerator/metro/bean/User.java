package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/7/18.
 */
public class User {

    private int is_ok;
    private String session_id;
    private String user_id;

    public int getIs_ok() {
        return is_ok;
    }

    public void setIs_ok(int is_ok) {
        this.is_ok = is_ok;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "is_ok=" + is_ok +
                ", session_id='" + session_id + '\'' +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
