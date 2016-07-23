package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/7/22.
 */
public class CommitOrder {

    /**
     * is_ok : 1
     * else_info : {"money":2,"order_sn":"TS1469241291"}
     */

    private int is_ok;
    /**
     * money : 2
     * order_sn : TS1469241291
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

        private int money;
        private String order_sn;
        private String time;

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public String toString() {
            return "ElseInfoBean{" +
                    "money=" + money +
                    ", order_sn='" + order_sn + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }
    }
}
