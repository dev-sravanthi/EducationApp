package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PD_DataBean {
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("photo")
    @Expose
    private String photo;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("father_detail")
    @Expose
    private PD_FatherDetails pd_fatherDetails;
    @SerializedName("mother_detail")
    @Expose
    private PD_MotherDetails pd_motherDetails;
    @SerializedName("passport_detail")
    @Expose
    private PD_PassportDetails pd_passportDetails;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PD_FatherDetails getPd_fatherDetails() {
        return pd_fatherDetails;
    }

    public void setPd_fatherDetails(PD_FatherDetails pd_fatherDetails) {
        this.pd_fatherDetails = pd_fatherDetails;
    }

    public PD_MotherDetails getPd_motherDetails() {
        return pd_motherDetails;
    }

    public void setPd_motherDetails(PD_MotherDetails pd_motherDetails) {
        this.pd_motherDetails = pd_motherDetails;
    }

    public PD_PassportDetails getPd_passportDetails() {
        return pd_passportDetails;
    }

    public void setPd_passportDetails(PD_PassportDetails pd_passportDetails) {
        this.pd_passportDetails = pd_passportDetails;
    }
}
