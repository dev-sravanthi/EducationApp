package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeePendingBean {
    private String status,code;
    @SerializedName("data")
    @Expose
    List<FeePendingData> feePendingDataList;

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

    public List<FeePendingData> getFeePendingDataList() {
        return feePendingDataList;
    }

    public void setFeePendingDataList(List<FeePendingData> feePendingDataList) {
        this.feePendingDataList = feePendingDataList;
    }

    public class FeePendingData{
        private String id,name,total,discount,paid,pending;
        @SerializedName("fees")
        @Expose
        List<FP_FeesBean> fp_feesBeans;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPaid() {
            return paid;
        }

        public void setPaid(String paid) {
            this.paid = paid;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

        public List<FP_FeesBean> getFp_feesBeans() {
            return fp_feesBeans;
        }

        public void setFp_feesBeans(List<FP_FeesBean> fp_feesBeans) {
            this.fp_feesBeans = fp_feesBeans;
        }
    }

    public class FP_FeesBean{
        private String id,name,total,discount,paid,pending;
        @SerializedName("sub_fee")
        @Expose
        List<FP_SubFeeBean> fp_subFeeBeans;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPaid() {
            return paid;
        }

        public void setPaid(String paid) {
            this.paid = paid;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

        public List<FP_SubFeeBean> getFp_subFeeBeans() {
            return fp_subFeeBeans;
        }

        public void setFp_subFeeBeans(List<FP_SubFeeBean> fp_subFeeBeans) {
            this.fp_subFeeBeans = fp_subFeeBeans;
        }
    }

    public class FP_SubFeeBean{
        private String id,name,total,discount,paid,pending;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getPaid() {
            return paid;
        }

        public void setPaid(String paid) {
            this.paid = paid;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }
    }
}
