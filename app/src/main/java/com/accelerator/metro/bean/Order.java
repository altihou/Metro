package com.accelerator.metro.bean;

import java.util.List;

/**
 * Created by Nicholas on 2016/7/23.
 */
public class Order {

    private int is_ok;
    private String user_id;
    private String session_id;

    private List<ElseInfoBean> else_info;

    public int getIs_ok() {
        return is_ok;
    }

    public void setIs_ok(int is_ok) {
        this.is_ok = is_ok;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public List<ElseInfoBean> getElse_info() {
        return else_info;
    }

    public void setElse_info(List<ElseInfoBean> else_info) {
        this.else_info = else_info;
    }

    public static class ElseInfoBean {

        private String order_sn;
        private int order_type;
        private int is_complete;
        private String order_money;
        private int time;
        private String start_point;
        private String end_point;
        private String user_id;
        private String start_id;
        private String end_id;
        private String count;

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

        public int getIs_complete() {
            return is_complete;
        }

        public void setIs_complete(int is_complete) {
            this.is_complete = is_complete;
        }

        public String getOrder_money() {
            return order_money;
        }

        public void setOrder_money(String order_money) {
            this.order_money = order_money;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public String getStart_point() {
            return start_point;
        }

        public void setStart_point(String start_point) {
            this.start_point = start_point;
        }

        public String getEnd_point() {
            return end_point;
        }

        public void setEnd_point(String end_point) {
            this.end_point = end_point;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getStart_id() {
            return start_id;
        }

        public void setStart_id(String start_id) {
            this.start_id = start_id;
        }

        public String getEnd_id() {
            return end_id;
        }

        public void setEnd_id(String end_id) {
            this.end_id = end_id;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "ElseInfoBean{" +
                    "order_sn='" + order_sn + '\'' +
                    ", order_type=" + order_type +
                    ", is_complete=" + is_complete +
                    ", order_money='" + order_money + '\'' +
                    ", time=" + time +
                    ", start_point='" + start_point + '\'' +
                    ", end_point='" + end_point + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", start_id='" + start_id + '\'' +
                    ", end_id='" + end_id + '\'' +
                    ", count='" + count + '\'' +
                    '}';
        }
    }
}
