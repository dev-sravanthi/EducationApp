package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeeStudentInvoiceBean {
    private String status,code;
    @SerializedName("data")
    @Expose
    List<FS_InvoiceDataBean> fs_invoiceDataBeans;

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

    public List<FS_InvoiceDataBean> getFs_invoiceDataBeans() {
        return fs_invoiceDataBeans;
    }

    public void setFs_invoiceDataBeans(List<FS_InvoiceDataBean> fs_invoiceDataBeans) {
        this.fs_invoiceDataBeans = fs_invoiceDataBeans;
    }

    public class FS_InvoiceDataBean{
        private String id,fr_no,bill_date,total,student_name,standard_section,discount_type,bill_pay_type,student_type,
                created_by,created_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFr_no() {
            return fr_no;
        }

        public void setFr_no(String fr_no) {
            this.fr_no = fr_no;
        }

        public String getBill_date() {
            return bill_date;
        }

        public void setBill_date(String bill_date) {
            this.bill_date = bill_date;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
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

        public String getDiscount_type() {
            return discount_type;
        }

        public void setDiscount_type(String discount_type) {
            this.discount_type = discount_type;
        }

        public String getBill_pay_type() {
            return bill_pay_type;
        }

        public void setBill_pay_type(String bill_pay_type) {
            this.bill_pay_type = bill_pay_type;
        }

        public String getStudent_type() {
            return student_type;
        }

        public void setStudent_type(String student_type) {
            this.student_type = student_type;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
