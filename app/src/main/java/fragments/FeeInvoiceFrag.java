package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluebase.educationapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import activities.FeesScreen;
import activities.HomeWork;
import bean.FeeStudentInvoiceBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class FeeInvoiceFrag extends Fragment {
    View v;
    String status,code,d_id,d_fr_no,d_bill_date,d_total,d_student_name,d_standard_section,d_discount_type,
            d_bill_pay_type,d_student_type,d_created_by,d_created_at;
    RecyclerView recyclerView;
    FeeInvoiceAdapter feeInvoiceAdapter;
    List<FeeInvoiceBean> feeInvoiceBeanList=new ArrayList<>();
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fee_invoice_frag, container, false);

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((FeesScreen)getActivity()).setFragmentRefreshListener(new FeesScreen.FragmentRefreshListener_fees() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
//                Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
                sibling_id = prefs.getString("sibling_id","");
                findviewbyids();
            }
        });

        networkAvailability= Utility.isConnectingToInternet(getActivity());

        if(networkAvailability==true){
            findviewbyids();
        }else{
            Utility.getAlertNetNotConneccted(getContext(), "Internet Connection");
        }
        return v;
    }

    private void findviewbyids() {
        recyclerView = v.findViewById(R.id.recyclerView);
        feeInvoiceBeanList.clear();
        recyclerView.setHasFixedSize(true);
        feeInvoiceAdapter = new FeeInvoiceAdapter(feeInvoiceBeanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(feeInvoiceAdapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<FeeStudentInvoiceBean> call= RetrofitClient.getInstance().getApi().getFeeStudentInvoice("Bearer "+token,sibling_id);
        call.enqueue(new Callback<FeeStudentInvoiceBean>() {

            @Override
            public void onResponse(Call<FeeStudentInvoiceBean> call, Response<FeeStudentInvoiceBean> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    FeeStudentInvoiceBean feeStudentInvoiceBean=response.body();
                    status=feeStudentInvoiceBean.getStatus();
                    code=feeStudentInvoiceBean.getCode();

                    List<FeeStudentInvoiceBean.FS_InvoiceDataBean> fs_invoiceDataBeans=
                            feeStudentInvoiceBean.getFs_invoiceDataBeans();
                    for (int i=0;i<fs_invoiceDataBeans.size();i++){
                        d_id=fs_invoiceDataBeans.get(i).getId();
                        d_fr_no=fs_invoiceDataBeans.get(i).getFr_no();
                        d_bill_date=fs_invoiceDataBeans.get(i).getBill_date();
                        d_total=fs_invoiceDataBeans.get(i).getTotal();
                        d_student_name=fs_invoiceDataBeans.get(i).getStudent_name();
                        d_standard_section=fs_invoiceDataBeans.get(i).getStandard_section();
                        d_discount_type=fs_invoiceDataBeans.get(i).getDiscount_type();
                        d_bill_pay_type=fs_invoiceDataBeans.get(i).getBill_pay_type();
                        d_student_type=fs_invoiceDataBeans.get(i).getStudent_type();
                        d_created_by=fs_invoiceDataBeans.get(i).getCreated_by();
                        d_created_at=fs_invoiceDataBeans.get(i).getCreated_at();

                        FeeInvoiceBean feeInvoiceBean=new FeeInvoiceBean(d_id,d_fr_no,d_bill_date,d_total,
                                d_student_name,d_standard_section,d_discount_type,d_bill_pay_type,d_student_type,
                                d_created_by,d_created_at);
                        feeInvoiceBeanList.add(feeInvoiceBean);
                    }
                    feeInvoiceAdapter.notifyDataSetChanged();


                }else{
                    progressDialog.dismiss();
                    Utility.showAlertDialog(getActivity(),"Error",response.errorBody().toString(),false);
                }

            }

            @Override
            public void onFailure(Call<FeeStudentInvoiceBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(getActivity(),"Error",t.getMessage(),false);            }

        });

    }

    public class FeeInvoiceAdapter extends RecyclerView.Adapter<FeeInvoiceAdapter.ViewHolder> {
        private List<FeeInvoiceBean> feeInvoiceBeans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv_date,tv_name_section,tv_frno,tv_category,tv_amount;
            ImageButton imgbtn_download;

            public ViewHolder(View view) {
                super(view);
                tv_date=(TextView) view.findViewById(R.id.tv_date);
                tv_name_section=(TextView) view.findViewById(R.id.tv_name_section);
                tv_frno=(TextView) view.findViewById(R.id.tv_frno);
                tv_category=(TextView) view.findViewById(R.id.tv_category);
                tv_amount=(TextView) view.findViewById(R.id.tv_amount);
                imgbtn_download=(ImageButton)view.findViewById(R.id.imgbtn_download);
            }
        }

        public FeeInvoiceAdapter(List<FeeInvoiceBean> mlist)
        {
            this.feeInvoiceBeans = mlist;
            this.context=context;
        }

        @Override
        public FeeInvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fee_invoice_frag_list, parent, false);

            return new FeeInvoiceAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FeeInvoiceAdapter.ViewHolder holder, int position) {
            FeeInvoiceBean data = feeInvoiceBeans.get(position);
            holder.tv_date.setText(data.getBill_date());
            holder.tv_frno.setText(data.getFr_no());
            holder.tv_name_section.setText(data.getStudent_name() +"("+data.getStandard_section()+")");
            holder.tv_category.setText(data.getDiscount_type());
            holder.tv_amount.setText(data.getTotal());

        }

        @Override
        public int getItemCount() {
            return feeInvoiceBeans.size();
        }
    }

    public class FeeInvoiceBean {
        private String id,fr_no,bill_date,total,student_name,standard_section,discount_type,bill_pay_type,
                student_type,created_by,created_at;

        public FeeInvoiceBean(String id,String fr_no,String bill_date,String total,String student_name,
                              String standard_section,String discount_type,String bill_pay_type,String student_type,
                              String created_by,String created_at){
            this.id=id;
            this.fr_no=fr_no;
            this.bill_date=bill_date;
            this.total=total;
            this.student_name=student_name;
            this.standard_section=standard_section;
            this.discount_type=discount_type;
            this.bill_pay_type=bill_pay_type;
            this.student_type=student_type;
            this.created_by=created_by;
            this.created_at=created_at;

        }

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


    public void showBar(){
        builder = new AlertDialog.Builder(getActivity());
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing Data...");
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
