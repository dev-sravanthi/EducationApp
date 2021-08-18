package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClassTestPastBean {
    private String code,status,msg;
    @SerializedName("data")
    @Expose
    List<CTPastBeanData> ctPastBeanDataList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<CTPastBeanData> getCtPastBeanDataList() {
        return ctPastBeanDataList;
    }

    public void setCtPastBeanDataList(List<CTPastBeanData> ctPastBeanDataList) {
        this.ctPastBeanDataList = ctPastBeanDataList;
    }

    public class CTPastBeanData {
        private String id, date, description, posted_at;
        @SerializedName("subject")
        @Expose
        private List<CTPastBeanSubject> ctPastBeanSubjectList;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPosted_at() {
            return posted_at;
        }

        public void setPosted_at(String posted_at) {
            this.posted_at = posted_at;
        }

        public List<CTPastBeanSubject> getCtPastBeanSubjectList() {
            return ctPastBeanSubjectList;
        }

        public void setCtPastBeanSubjectList(List<CTPastBeanSubject> ctPastBeanSubjectList) {
            this.ctPastBeanSubjectList = ctPastBeanSubjectList;
        }
    }

    public class CTPastBeanSubject {
        private String class_test_id, subject_name, icon, title, description, posted_by, posted_at;

        public String getClass_test_id() {
            return class_test_id;
        }

        public void setClass_test_id(String class_test_id) {
            this.class_test_id = class_test_id;
        }

        public String getSubject_name() {
            return subject_name;
        }

        public void setSubject_name(String subject_name) {
            this.subject_name = subject_name;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPosted_by() {
            return posted_by;
        }

        public void setPosted_by(String posted_by) {
            this.posted_by = posted_by;
        }

        public String getPosted_at() {
            return posted_at;
        }

        public void setPosted_at(String posted_at) {
            this.posted_at = posted_at;
        }
    }
}
