package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class ModifyUser {


    /**
     * is_ok : 1
     * else_info : {"nickname":"05mhi9p46914ff8jp1srhreak4","user_sex":"05mhi9p46914ff8jp1srhreak4","user_headpic":"111"}
     */

    private int is_ok;
    /**
     * nickname : 05mhi9p46914ff8jp1srhreak4
     * user_sex : 05mhi9p46914ff8jp1srhreak4
     * user_headpic : 111
     */

    private ElseInfoBean else_info;

    public int getIs_ok() {
        return is_ok;
    }

    public void setIs_ok(int is_ok) {
        this.is_ok = is_ok;
    }

    public ElseInfoBean getElse_info() {
        return else_info;
    }

    public void setElse_info(ElseInfoBean else_info) {
        this.else_info = else_info;
    }

    public static class ElseInfoBean {
        private String nickname;
        private String user_sex;
        private String user_headpic;
        private String session_id;
        private String user_id;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_sex() {
            return user_sex;
        }

        public void setUser_sex(String user_sex) {
            this.user_sex = user_sex;
        }

        public String getUser_headpic() {
            return user_headpic;
        }

        public void setUser_headpic(String user_headpic) {
            this.user_headpic = user_headpic;
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
            return "ElseInfoBean{" +
                    "nickname='" + nickname + '\'' +
                    ", user_sex='" + user_sex + '\'' +
                    ", user_headpic='" + user_headpic + '\'' +
                    ", session_id='" + session_id + '\'' +
                    ", user_id='" + user_id + '\'' +
                    '}';
        }
    }
}
