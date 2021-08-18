package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransportRouStoppingBean {
    private String status,code;
    @SerializedName("data")
    @Expose
    TRSData trsData;

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

    public TRSData getTrsData() {
        return trsData;
    }

    public void setTrsData(TRSData trsData) {
        this.trsData = trsData;
    }

    public class TRSData{
        private String route,stopping_point,discount_category;
        @SerializedName("vehicle")
        @Expose
        TRSDataVehicle trsDataVehicle;

        public String getRoute() {
            return route;
        }

        public void setRoute(String route) {
            this.route = route;
        }

        public String getStopping_point() {
            return stopping_point;
        }

        public void setStopping_point(String stopping_point) {
            this.stopping_point = stopping_point;
        }

        public String getDiscount_category() {
            return discount_category;
        }

        public void setDiscount_category(String discount_category) {
            this.discount_category = discount_category;
        }

        public TRSDataVehicle getTrsDataVehicle() {
            return trsDataVehicle;
        }

        public void setTrsDataVehicle(TRSDataVehicle trsDataVehicle) {
            this.trsDataVehicle = trsDataVehicle;
        }
    }

    public class TRSDataVehicle{
        private String bus_no,bus_photo,pickup_time,trap_time,driver,driver_phone_no,driver_photo,care_taker,
                care_taker_phone_no,care_taker_photo;

        public String getBus_no() {
            return bus_no;
        }

        public void setBus_no(String bus_no) {
            this.bus_no = bus_no;
        }

        public String getBus_photo() {
            return bus_photo;
        }

        public void setBus_photo(String bus_photo) {
            this.bus_photo = bus_photo;
        }

        public String getPickup_time() {
            return pickup_time;
        }

        public void setPickup_time(String pickup_time) {
            this.pickup_time = pickup_time;
        }

        public String getTrap_time() {
            return trap_time;
        }

        public void setTrap_time(String trap_time) {
            this.trap_time = trap_time;
        }

        public String getDriver() {
            return driver;
        }

        public void setDriver(String driver) {
            this.driver = driver;
        }

        public String getDriver_phone_no() {
            return driver_phone_no;
        }

        public void setDriver_phone_no(String driver_phone_no) {
            this.driver_phone_no = driver_phone_no;
        }

        public String getDriver_photo() {
            return driver_photo;
        }

        public void setDriver_photo(String driver_photo) {
            this.driver_photo = driver_photo;
        }

        public String getCare_taker() {
            return care_taker;
        }

        public void setCare_taker(String care_taker) {
            this.care_taker = care_taker;
        }

        public String getCare_taker_phone_no() {
            return care_taker_phone_no;
        }

        public void setCare_taker_phone_no(String care_taker_phone_no) {
            this.care_taker_phone_no = care_taker_phone_no;
        }

        public String getCare_taker_photo() {
            return care_taker_photo;
        }

        public void setCare_taker_photo(String care_taker_photo) {
            this.care_taker_photo = care_taker_photo;
        }
    }
}
