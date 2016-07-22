package com.accelerator.metro.bean;

import java.util.List;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class ExpenseCalendar {

    /**
     * is_ok : 1
     * else_info : [{"money_num":"2","time":"2","money_type":"13","id":"1223"}]
     */

    private int is_ok;
    /**
     * money_num : 2
     * time : 2
     * money_type : 13
     * id : 1223
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
        private String money_num;
        private String time;
        private String money_type;
        private String id;

        public String getMoney_num() {
            return money_num;
        }

        public void setMoney_num(String money_num) {
            this.money_num = money_num;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMoney_type() {
            return money_type;
        }

        public void setMoney_type(String money_type) {
            this.money_type = money_type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
