package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/7/18.
 */
public class User {

    /**
     * is_ok : 1
     * else_info : {"session_id":"5a04pn8s2l07iq1obl4jkm0nm1","user_id":"VkZkak9WQlJQVDA9"}
     */

    private int is_ok;
    /**
     * session_id : 5a04pn8s2l07iq1obl4jkm0nm1
     * user_id : VkZkak9WQlJQVDA9
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

        private String session_id;
        private String user_id;

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
    }

    @Override
    public String toString() {
        return "User{" +
                "is_ok=" + is_ok +
                ", else_info=" + else_info +
                '}';
    }
}
