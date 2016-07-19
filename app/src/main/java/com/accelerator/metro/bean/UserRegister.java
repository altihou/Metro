package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/7/9.
 */
public class UserRegister {

    private String userId;
    private String userSession;
    private String userAvatar;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserSession() {
        return userSession;
    }

    public void setUserSession(String userSession) {
        this.userSession = userSession;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    @Override
    public String toString() {
        return "UserRegister{" +
                "userId='" + userId + '\'' +
                ", userSession='" + userSession + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}
