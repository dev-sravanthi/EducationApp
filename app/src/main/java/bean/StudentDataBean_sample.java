package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StudentDataBean_sample {
    private String status,code;
    @SerializedName("data")
    @Expose
    List<S_data_bean> s_data_beanList;

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

    public List<S_data_bean> getS_data_beanList() {
        return s_data_beanList;
    }

    public void setS_data_beanList(List<S_data_bean> s_data_beanList) {
        this.s_data_beanList = s_data_beanList;
    }

    public class S_data_bean{
        private String academic_student_id,student_id,student_name,standard_section,code,gender,dob,doj,phone,
                emis_no,nationality,mother_tongue,blood_group,medium_of_study,community,caste,sub_caste,student_type,
                discount_category,board_id,place_of_birth,board_name,academic,academic_id,photo;
        @SerializedName("permanent_address")
        @Expose
        private permanent_address permanent_address;
        @SerializedName("residential_address")
        @Expose
        private residential_address residential_address;

        public String getAcademic_student_id() {
            return academic_student_id;
        }

        public void setAcademic_student_id(String academic_student_id) {
            this.academic_student_id = academic_student_id;
        }

        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getStudent_name() {
            return student_name;
        }

        public void setStudent_name(String student_name) {
            this.student_name = student_name;
        }

        public String getStandard_section() {
            return standard_section;
        }

        public void setStandard_section(String standard_section) {
            this.standard_section = standard_section;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getDoj() {
            return doj;
        }

        public void setDoj(String doj) {
            this.doj = doj;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getEmis_no() {
            return emis_no;
        }

        public void setEmis_no(String emis_no) {
            this.emis_no = emis_no;
        }

        public String getNationality() {
            return nationality;
        }

        public void setNationality(String nationality) {
            this.nationality = nationality;
        }

        public String getMother_tongue() {
            return mother_tongue;
        }

        public void setMother_tongue(String mother_tongue) {
            this.mother_tongue = mother_tongue;
        }

        public String getBlood_group() {
            return blood_group;
        }

        public void setBlood_group(String blood_group) {
            this.blood_group = blood_group;
        }

        public String getMedium_of_study() {
            return medium_of_study;
        }

        public void setMedium_of_study(String medium_of_study) {
            this.medium_of_study = medium_of_study;
        }

        public String getCommunity() {
            return community;
        }

        public void setCommunity(String community) {
            this.community = community;
        }

        public String getCaste() {
            return caste;
        }

        public void setCaste(String caste) {
            this.caste = caste;
        }

        public String getSub_caste() {
            return sub_caste;
        }

        public void setSub_caste(String sub_caste) {
            this.sub_caste = sub_caste;
        }

        public String getStudent_type() {
            return student_type;
        }

        public void setStudent_type(String student_type) {
            this.student_type = student_type;
        }

        public String getDiscount_category() {
            return discount_category;
        }

        public void setDiscount_category(String discount_category) {
            this.discount_category = discount_category;
        }

        public String getBoard_id() {
            return board_id;
        }

        public void setBoard_id(String board_id) {
            this.board_id = board_id;
        }

        public String getPlace_of_birth() {
            return place_of_birth;
        }

        public void setPlace_of_birth(String place_of_birth) {
            this.place_of_birth = place_of_birth;
        }

        public String getBoard_name() {
            return board_name;
        }

        public void setBoard_name(String board_name) {
            this.board_name = board_name;
        }

        public String getAcademic() {
            return academic;
        }

        public void setAcademic(String academic) {
            this.academic = academic;
        }

        public String getAcademic_id() {
            return academic_id;
        }

        public void setAcademic_id(String academic_id) {
            this.academic_id = academic_id;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public StudentDataBean_sample.permanent_address getPermanent_address() {
            return permanent_address;
        }

        public void setPermanent_address(StudentDataBean_sample.permanent_address permanent_address) {
            this.permanent_address = permanent_address;
        }

        public StudentDataBean_sample.residential_address getResidential_address() {
            return residential_address;
        }

        public void setResidential_address(StudentDataBean_sample.residential_address residential_address) {
            this.residential_address = residential_address;
        }
    }

    public class permanent_address{
        private String address,city,state_id,country_id,pin_code;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState_id() {
            return state_id;
        }

        public void setState_id(String state_id) {
            this.state_id = state_id;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public String getPin_code() {
            return pin_code;
        }

        public void setPin_code(String pin_code) {
            this.pin_code = pin_code;
        }
    }

    public class residential_address{
        private String address,city,state_id,country_id,pin_code;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState_id() {
            return state_id;
        }

        public void setState_id(String state_id) {
            this.state_id = state_id;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public String getPin_code() {
            return pin_code;
        }

        public void setPin_code(String pin_code) {
            this.pin_code = pin_code;
        }
    }
}
