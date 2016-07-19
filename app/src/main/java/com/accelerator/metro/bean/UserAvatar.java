package com.accelerator.metro.bean;

import java.util.List;

/**
 * Created by Nicholas on 2016/7/10.
 */
public class UserAvatar {


    /**
     * is_ok : 1
     * else_info : [{"headpic":"1/1.jpg"}]
     */

    private int is_ok;
    /**
     * headpic : 1/1.jpg
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
        private String headpic;

        public String getHeadpic() {
            return headpic;
        }

        public void setHeadpic(String headpic) {
            this.headpic = headpic;
        }
    }
}
