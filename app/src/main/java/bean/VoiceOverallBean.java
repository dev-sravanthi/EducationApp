package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VoiceOverallBean {
    private String code,status,msg;
    @SerializedName("data")
    @Expose
    List<VoiceOverallDataBean> voiceOverallDataBeanList;

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

    public List<VoiceOverallDataBean> getVoiceOverallDataBeanList() {
        return voiceOverallDataBeanList;
    }

    public void setVoiceOverallDataBeanList(List<VoiceOverallDataBean> voiceOverallDataBeanList) {
        this.voiceOverallDataBeanList = voiceOverallDataBeanList;
    }

    public class VoiceOverallDataBean{
        private String id,date,time_stamp,title,voice_msg_file;

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

        public String getVoice_msg_file() {
            return voice_msg_file;
        }

        public void setVoice_msg_file(String voice_msg_file) {
            this.voice_msg_file = voice_msg_file;
        }
    }
}
