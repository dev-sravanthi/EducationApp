package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CircularFragBean {
    private String code,status,msg;
    @SerializedName("data")
    @Expose
    private List<CircularDataBean> circularDataBeanList;

    public List<CircularDataBean> getCircularDataBeanList() {
        return circularDataBeanList;
    }

    public void setCircularDataBeanList(List<CircularDataBean> circularDataBeanList) {
        this.circularDataBeanList = circularDataBeanList;
    }

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

    public class CircularDataBean{

        private String id;
        private String uuid;
        private String date;
        private String title,description,attach_url;

        public String getAttach_url() {
            return attach_url;
        }

        public void setAttach_url(String attach_url) {
            this.attach_url = attach_url;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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
    }

}
