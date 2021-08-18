package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReligionBean {
    private String status;
    private String code;
    @SerializedName("data")
    @Expose
    private List<ReligionDataBean> religionDataBean;

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

    public List<ReligionDataBean> getReligionDataBean() {
        return religionDataBean;
    }

    public void setReligionDataBean(List<ReligionDataBean> religionDataBean) {
        this.religionDataBean = religionDataBean;
    }
}
