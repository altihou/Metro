package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/7/20.
 */
public class MineInfo {


    /**
     * is_ok : 1
     * else_info : {"user_headpic":"3/3_14690241552661.jpg","user_money":"0","nickname":null,"phone_no":"13312345676"}
     */

    private int is_ok;
    /**
     * user_headpic : 3/3_14690241552661.jpg
     * user_money : 0
     * nickname : null
     * phone_no : 13312345676
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

        private String user_headpic;
        private String user_money;
        private String nickname;
        private String phone_no;

        public String getUser_headpic() {
            return user_headpic;
        }

        public void setUser_headpic(String user_headpic) {
            this.user_headpic = user_headpic;
        }

        public String getUser_money() {
            return user_money;
        }

        public void setUser_money(String user_money) {
            this.user_money = user_money;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        @Override
        public String toString() {
            return "ElseInfoBean{" +
                    "user_headpic='" + user_headpic + '\'' +
                    ", user_money='" + user_money + '\'' +
                    ", nickname=" + nickname +
                    ", phone_no='" + phone_no + '\'' +
                    '}';
        }
    }
}
