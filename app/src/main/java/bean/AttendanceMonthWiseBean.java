package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AttendanceMonthWiseBean {
    private String code,status,msg;
    @SerializedName("data")
    @Expose
    private List<AttendanceMWDataBean> attendanceMWDataBeanList;
    @SerializedName("percentage")
    @Expose
    private AttendanceMWPercentage attendanceMWPercentage;

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

    public List<AttendanceMWDataBean> getAttendanceMWDataBeanList() {
        return attendanceMWDataBeanList;
    }

    public void setAttendanceMWDataBeanList(List<AttendanceMWDataBean> attendanceMWDataBeanList) {
        this.attendanceMWDataBeanList = attendanceMWDataBeanList;
    }

    public AttendanceMWPercentage getAttendanceMWPercentage() {
        return attendanceMWPercentage;
    }

    public void setAttendanceMWPercentage(AttendanceMWPercentage attendanceMWPercentage) {
        this.attendanceMWPercentage = attendanceMWPercentage;
    }

    public class AttendanceMWDataBean{
        private String id,date,leave_type,remark,created_at;

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

        public String getLeave_type() {
            return leave_type;
        }

        public void setLeave_type(String leave_type) {
            this.leave_type = leave_type;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }

    public class AttendanceMWPercentage{
        private String no_of_working_days,present,absent,percentage;

        public String getNo_of_working_days() {
            return no_of_working_days;
        }

        public void setNo_of_working_days(String no_of_working_days) {
            this.no_of_working_days = no_of_working_days;
        }

        public String getPresent() {
            return present;
        }

        public void setPresent(String present) {
            this.present = present;
        }

        public String getAbsent() {
            return absent;
        }

        public void setAbsent(String absent) {
            this.absent = absent;
        }

        public String getPercentage() {
            return percentage;
        }

        public void setPercentage(String percentage) {
            this.percentage = percentage;
        }
    }
}
