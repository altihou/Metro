package com.accelerator.metro.bean;

import java.util.List;

/**
 * Created by Nicholas on 2016/8/8.
 */
public class PlanTicket {

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

        private String id;
        private String auto_type;
        private String days;
        private String time;
        private String work_time;
        private String start_point;
        private String end_point;
        private String user_id;
        private String city_name;
        private String start_site;
        private String end_site;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuto_type() {
            return auto_type;
        }

        public void setAuto_type(String auto_type) {
            this.auto_type = auto_type;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getWork_time() {
            return work_time;
        }

        public void setWork_time(String work_time) {
            this.work_time = work_time;
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

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getStart_site() {
            return start_site;
        }

        public void setStart_site(String start_site) {
            this.start_site = start_site;
        }

        public String getEnd_site() {
            return end_site;
        }

        public void setEnd_site(String end_site) {
            this.end_site = end_site;
        }
    }
}
