package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceOverallBean {
    private String status;
    private String code;
    @SerializedName("data")
    @Expose
    private AttendanceOveralldataBean attendanceOverallBean;

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

    public AttendanceOveralldataBean getAttendanceOverallBean() {
        return attendanceOverallBean;
    }

    public void setAttendanceOverallBean(AttendanceOveralldataBean attendanceOverallBean) {
        this.attendanceOverallBean = attendanceOverallBean;
    }

    public class AttendanceOveralldataBean{
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
