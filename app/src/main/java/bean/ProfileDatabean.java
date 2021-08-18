package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDatabean {
    private String status;
    private String code;
    @SerializedName("data")
    @Expose
    private PD_DataBean pd_dataBean;


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

    public PD_DataBean getPd_dataBean() {
        return pd_dataBean;
    }

    public void setPd_dataBean(PD_DataBean pd_dataBean) {
        this.pd_dataBean = pd_dataBean;
    }

}
