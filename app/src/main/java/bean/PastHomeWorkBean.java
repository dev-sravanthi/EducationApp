package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PastHomeWorkBean {
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("data")
    @Expose
    private List<PHW_Databean> phw_databeans;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<PHW_Databean> getPhw_databeans() {
        return phw_databeans;
    }

    public void setPhw_databeans(List<PHW_Databean> phw_databeans) {
        this.phw_databeans = phw_databeans;
    }

    public class PHW_Databean {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("posted_at")
        @Expose
        private String posted_at;
        @SerializedName("subject")
        @Expose
        private List<PHW_Subjects> phw_subjects;

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

        public List<PHW_Subjects> getPhw_subjects() {
            return phw_subjects;
        }

        public void setPhw_subjects(List<PHW_Subjects> phw_subjects) {
            this.phw_subjects = phw_subjects;
        }
    }

    public class PHW_Subjects {
        @SerializedName("homework_id")
        @Expose
        private String homework_id;
        @SerializedName("subject_name")
        @Expose
        private String subject_name;
        @SerializedName("icon")
        @Expose
        private String icon;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("posted_by")
        @Expose
        private String posted_by;
        @SerializedName("posted_at")
        @Expose
        private String posted_at;

        public String getHomework_id() {
            return homework_id;
        }

        public void setHomework_id(String homework_id) {
            this.homework_id = homework_id;
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
