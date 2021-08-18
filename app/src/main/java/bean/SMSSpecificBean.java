package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SMSSpecificBean {
    private String current_page;
    @SerializedName("data")
    @Expose
    List<SMSSpecificData> smsSpecificDataList;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public List<SMSSpecificData> getSmsSpecificDataList() {
        return smsSpecificDataList;
    }

    public void setSmsSpecificDataList(List<SMSSpecificData> smsSpecificDataList) {
        this.smsSpecificDataList = smsSpecificDataList;
    }

    public class SMSSpecificData{
        private String id,date,title,sms_detail;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSms_detail() {
            return sms_detail;
        }

        public void setSms_detail(String sms_detail) {
            this.sms_detail = sms_detail;
        }
    }
}
