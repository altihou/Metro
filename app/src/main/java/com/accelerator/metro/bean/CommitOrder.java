package com.accelerator.metro.bean;

/**
 * Created by Nicholas on 2016/7/22.
 */
public class CommitOrder {


    /**
     * is_ok : 1
     * else_info : {"money":"05mhi9p46914ff8jp1srhreak4","order_sn":"Vkcxak9WQlJQVDA9"}
     */

    private int is_ok;
    /**
     * money : 05mhi9p46914ff8jp1srhreak4
     * order_sn : Vkcxak9WQlJQVDA9
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
        private String order_sn;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }

        @Override
        public String toString() {
            return "ElseInfoBean{" +
                    "money='" + money + '\'' +
                    ", order_sn='" + order_sn + '\'' +
                    '}';
        }
    }
}
