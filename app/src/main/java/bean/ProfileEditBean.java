package bean;

public class ProfileEditBean {
    private String status,code;
    private ProfileEditDataBean profileEditDataBean;

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

    public ProfileEditDataBean getProfileEditDataBean() {
        return profileEditDataBean;
    }

    public void setProfileEditDataBean(ProfileEditDataBean profileEditDataBean) {
        this.profileEditDataBean = profileEditDataBean;
    }

    public class ProfileEditDataBean{
        private String user_name,code,email,phone,photo,city,pincode,address;
        private ProfileFatherDetails profileFatherDetails;
        private ProfileMotherDetails profileMotherDetails;
        private ProfilePassportDetails profilePassportDetails;

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

        public ProfileFatherDetails getProfileFatherDetails() {
            return profileFatherDetails;
        }

        public void setProfileFatherDetails(ProfileFatherDetails profileFatherDetails) {
            this.profileFatherDetails = profileFatherDetails;
        }

        public ProfileMotherDetails getProfileMotherDetails() {
            return profileMotherDetails;
        }

        public void setProfileMotherDetails(ProfileMotherDetails profileMotherDetails) {
            this.profileMotherDetails = profileMotherDetails;
        }

        public ProfilePassportDetails getProfilePassportDetails() {
            return profilePassportDetails;
        }

        public void setProfilePassportDetails(ProfilePassportDetails profilePassportDetails) {
            this.profilePassportDetails = profilePassportDetails;
        }
    }

    public class ProfileFatherDetails{
        private String profession,name,email,office_address,telephone_no,phone_no,whats_app_no,religion_id,religion_name,
                profession_id,profession_name,photo;

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOffice_address() {
            return office_address;
        }

        public void setOffice_address(String office_address) {
            this.office_address = office_address;
        }

        public String getTelephone_no() {
            return telephone_no;
        }

        public void setTelephone_no(String telephone_no) {
            this.telephone_no = telephone_no;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getWhats_app_no() {
            return whats_app_no;
        }

        public void setWhats_app_no(String whats_app_no) {
            this.whats_app_no = whats_app_no;
        }

        public String getReligion_id() {
            return religion_id;
        }

        public void setReligion_id(String religion_id) {
            this.religion_id = religion_id;
        }

        public String getReligion_name() {
            return religion_name;
        }

        public void setReligion_name(String religion_name) {
            this.religion_name = religion_name;
        }

        public String getProfession_id() {
            return profession_id;
        }

        public void setProfession_id(String profession_id) {
            this.profession_id = profession_id;
        }

        public String getProfession_name() {
            return profession_name;
        }

        public void setProfession_name(String profession_name) {
            this.profession_name = profession_name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

    public class ProfileMotherDetails{
        private String profession,name,email,office_address,telephone_no,phone_no,whats_app_no,religion_id,religion_name,
                profession_id,profession_name,photo;

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOffice_address() {
            return office_address;
        }

        public void setOffice_address(String office_address) {
            this.office_address = office_address;
        }

        public String getTelephone_no() {
            return telephone_no;
        }

        public void setTelephone_no(String telephone_no) {
            this.telephone_no = telephone_no;
        }

        public String getPhone_no() {
            return phone_no;
        }

        public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
        }

        public String getWhats_app_no() {
            return whats_app_no;
        }

        public void setWhats_app_no(String whats_app_no) {
            this.whats_app_no = whats_app_no;
        }

        public String getReligion_id() {
            return religion_id;
        }

        public void setReligion_id(String religion_id) {
            this.religion_id = religion_id;
        }

        public String getReligion_name() {
            return religion_name;
        }

        public void setReligion_name(String religion_name) {
            this.religion_name = religion_name;
        }

        public String getProfession_id() {
            return profession_id;
        }

        public void setProfession_id(String profession_id) {
            this.profession_id = profession_id;
        }

        public String getProfession_name() {
            return profession_name;
        }

        public void setProfession_name(String profession_name) {
            this.profession_name = profession_name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

    public class ProfilePassportDetails{
        private String passport_no,date_of_issue,date_of_expiry,place_of_issue,civil_id_no,residence_permit_no,
                date_of_res_permit_issue,entered_country_date;

        public String getPassport_no() {
            return passport_no;
        }

        public void setPassport_no(String passport_no) {
            this.passport_no = passport_no;
        }

        public String getDate_of_issue() {
            return date_of_issue;
        }

        public void setDate_of_issue(String date_of_issue) {
            this.date_of_issue = date_of_issue;
        }

        public String getDate_of_expiry() {
            return date_of_expiry;
        }

        public void setDate_of_expiry(String date_of_expiry) {
            this.date_of_expiry = date_of_expiry;
        }

        public String getPlace_of_issue() {
            return place_of_issue;
        }

        public void setPlace_of_issue(String place_of_issue) {
            this.place_of_issue = place_of_issue;
        }

        public String getCivil_id_no() {
            return civil_id_no;
        }

        public void setCivil_id_no(String civil_id_no) {
            this.civil_id_no = civil_id_no;
        }

        public String getResidence_permit_no() {
            return residence_permit_no;
        }

        public void setResidence_permit_no(String residence_permit_no) {
            this.residence_permit_no = residence_permit_no;
        }

        public String getDate_of_res_permit_issue() {
            return date_of_res_permit_issue;
        }

        public void setDate_of_res_permit_issue(String date_of_res_permit_issue) {
            this.date_of_res_permit_issue = date_of_res_permit_issue;
        }

        public String getEntered_country_date() {
            return entered_country_date;
        }

        public void setEntered_country_date(String entered_country_date) {
            this.entered_country_date = entered_country_date;
        }
    }
}
