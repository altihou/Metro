package com.accelerator.metro.bean;

import java.util.List;

/**
 * Created by Nicholas on 2016/7/23.
 */
public class Order {


    /**
     * is_ok : 1
     * else_info : [{"order_sn":"2","order_type":"2","order_money":"13","time":"1970-01-01 08:00:22","start_point":"","end_point":"","user_id":"1"}]
     */

    private int is_ok;
    /**
     * order_sn : 2
     * order_type : 2
     * order_money : 13
     * time : 1970-01-01 08:00:22
     * start_point :
     * end_point :
     * user_id : 1
     */

    private List<ElseInfoBean> else_info;

    public int getIs_ok() {
        return is_ok;
    }

    public void setIs_ok(int is_ok) {
        this.is_ok = is_ok;
    }

    public List<ElseInfoBean> getElse_info() {
        return else_info;
    }

    public void setElse_info(List<ElseInfoBean> else_info) {
        this.else_info = else_info;
    }

    public static class ElseInfoBean {

        private String order_sn;
        private String order_type;
        private String order_money;
        private String time;
        private String start_point;
        private String end_point;
        private String user_id;

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getOrder_type() {
            return order_type;
        }

        public void setOrder_type(String order_type) {
            this.order_type = order_type;
        }

        public String getOrder_money() {
            return order_money;
        }

        public void setOrder_money(String order_money) {
            this.order_money = order_money;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
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

        @Override
        public String toString() {
            return "ElseInfoBean{" +
                    "order_sn='" + order_sn + '\'' +
                    ", order_type='" + order_type + '\'' +
                    ", order_money='" + order_money + '\'' +
                    ", time='" + time + '\'' +
                    ", start_point='" + start_point + '\'' +
                    ", end_point='" + end_point + '\'' +
                    ", user_id='" + user_id + '\'' +
                    '}';
        }
    }
}
