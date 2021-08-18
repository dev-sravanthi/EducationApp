package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SMS_OverallBean {
    private String code,status;
    @SerializedName("data")
    @Expose
    List<SMSOverallData> smsOverallDataList;

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

    public List<SMSOverallData> getSmsOverallDataList() {
        return smsOverallDataList;
    }

    public void setSmsOverallDataList(List<SMSOverallData> smsOverallDataList) {
        this.smsOverallDataList = smsOverallDataList;
    }

    public class SMSOverallData{
        private String id,date,time_stamp,title,sms_detail;

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

        public String getTime_stamp() {
            return time_stamp;
        }

        public void setTime_stamp(String time_stamp) {
            this.time_stamp = time_stamp;
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
