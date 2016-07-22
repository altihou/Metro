package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/7/21.
 */
public class Recharge {

    /**
     * is_ok : 1
     * else_info : {"money":"05mhi9p46914ff8jp1srhreak4"}
     */

    private int is_ok;
    /**
     * money : 05mhi9p46914ff8jp1srhreak4
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
        private String money;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
