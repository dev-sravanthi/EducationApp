package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LibraryFineListBean {
    private String code,msg,status;
    @SerializedName("data")
    @Expose
    List<LibFLData> libFLDataList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<LibFLData> getLibFLDataList() {
        return libFLDataList;
    }

    public void setLibFLDataList(List<LibFLData> libFLDataList) {
        this.libFLDataList = libFLDataList;
    }

    public class LibFLData{
        private String id,code,date,accession_no,book_name,renew_get_date,renew_due_date,renew_total_day,return_renew_date,
                type,total_fine_days,single_day_fine,fine_amount,lost_book_amount,total_fine_amount,discount,
                discount_description,fine_status,academic;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAccession_no() {
            return accession_no;
        }

        public void setAccession_no(String accession_no) {
            this.accession_no = accession_no;
        }

        public String getBook_name() {
            return book_name;
        }

        public void setBook_name(String book_name) {
            this.book_name = book_name;
        }

        public String getRenew_get_date() {
            return renew_get_date;
        }

        public void setRenew_get_date(String renew_get_date) {
            this.renew_get_date = renew_get_date;
        }

        public String getRenew_due_date() {
            return renew_due_date;
        }

        public void setRenew_due_date(String renew_due_date) {
            this.renew_due_date = renew_due_date;
        }

        public String getRenew_total_day() {
            return renew_total_day;
        }

        public void setRenew_total_day(String renew_total_day) {
            this.renew_total_day = renew_total_day;
        }

        public String getReturn_renew_date() {
            return return_renew_date;
        }

        public void setReturn_renew_date(String return_renew_date) {
            this.return_renew_date = return_renew_date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTotal_fine_days() {
            return total_fine_days;
        }

        public void setTotal_fine_days(String total_fine_days) {
            this.total_fine_days = total_fine_days;
        }

        public String getSingle_day_fine() {
            return single_day_fine;
        }

        public void setSingle_day_fine(String single_day_fine) {
            this.single_day_fine = single_day_fine;
        }

        public String getFine_amount() {
            return fine_amount;
        }

        public void setFine_amount(String fine_amount) {
            this.fine_amount = fine_amount;
        }

        public String getLost_book_amount() {
            return lost_book_amount;
        }

        public void setLost_book_amount(String lost_book_amount) {
            this.lost_book_amount = lost_book_amount;
        }

        public String getTotal_fine_amount() {
            return total_fine_amount;
        }

        public void setTotal_fine_amount(String total_fine_amount) {
            this.total_fine_amount = total_fine_amount;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDiscount_description() {
            return discount_description;
        }

        public void setDiscount_description(String discount_description) {
            this.discount_description = discount_description;
        }

        public String getFine_status() {
            return fine_status;
        }

        public void setFine_status(String fine_status) {
            this.fine_status = fine_status;
        }

        public String getAcademic() {
            return academic;
        }

        public void setAcademic(String academic) {
            this.academic = academic;
        }
    }

}