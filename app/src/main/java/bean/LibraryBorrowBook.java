package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LibraryBorrowBook {
    private String msg,status,code;
    @SerializedName("data")
    @Expose
    private List<LibBorrowBookData> libBorrowBookDataList;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<LibBorrowBookData> getLibBorrowBookDataList() {
        return libBorrowBookDataList;
    }

    public void setLibBorrowBookDataList(List<LibBorrowBookData> libBorrowBookDataList) {
        this.libBorrowBookDataList = libBorrowBookDataList;
    }

    public class LibBorrowBookData {
        public String id, code, date, accession_no, book_name, get_date, due_date, d_status, return_renew_data,
                fine_days, single_day_fine, fine_amount, lost_book_amount, total_fine_amount, fine_status, academic;

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

        public String getGet_date() {
            return get_date;
        }

        public void setGet_date(String get_date) {
            this.get_date = get_date;
        }

        public String getDue_date() {
            return due_date;
        }

        public void setDue_date(String due_date) {
            this.due_date = due_date;
        }

        public String getD_status() {
            return d_status;
        }

        public void setD_status(String d_status) {
            this.d_status = d_status;
        }

        public String getReturn_renew_data() {
            return return_renew_data;
        }

        public void setReturn_renew_data(String return_renew_data) {
            this.return_renew_data = return_renew_data;
        }

        public String getFine_days() {
            return fine_days;
        }

        public void setFine_days(String fine_days) {
            this.fine_days = fine_days;
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
