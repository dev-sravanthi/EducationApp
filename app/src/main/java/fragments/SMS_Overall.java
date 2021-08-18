package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import activities.HomeWork;
import activities.SMSScreen;
import bean.SMS_OverallBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class SMS_Overall extends Fragment {
    View v;
    String code,status,d_id,d_date,d_time_stamp,d_title,d_sms_details;
    private List<SMSOverallAdapterBean> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private SMSOverallAdapter smsOverallAdapter;
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sms_overall, container, false);

        networkAvailability= Utility.isConnectingToInternet(getActivity());

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((SMSScreen)getActivity()).setFragmentRefreshListener(new SMSScreen.FragmentRefreshListener_sms() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
                sibling_id = prefs.getString("sibling_id","");
                findviewbyids();
            }
        });


        if(networkAvailability==true){
            findviewbyids();
        }else{
            Utility.getAlertNetNotConneccted(getContext(), "Internet Connection");
        }

        return v;
    }

    private void findviewbyids() {
        recyclerView = v.findViewById(R.id.recyclerView);
        list.clear();
        recyclerView.setHasFixedSize(true);
        smsOverallAdapter = new SMSOverallAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(smsOverallAdapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<SMS_OverallBean> call= RetrofitClient.getInstance().getApi().getSMSOverall("Bearer "+token,sibling_id);
        call.enqueue(new Callback<SMS_OverallBean>() {

            @Override
            public void onResponse(Call<SMS_OverallBean> call, Response<SMS_OverallBean> response) {

                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    SMS_OverallBean sms_overallBean=response.body();
                    code=sms_overallBean.getCode();
                    status=sms_overallBean.getStatus();
                    List<SMS_OverallBean.SMSOverallData> smsOverallDataList=sms_overallBean.getSmsOverallDataList();
                    if(smsOverallDataList.size()>0){
                        for (int i=0;i<smsOverallDataList.size();i++){
                            d_id=smsOverallDataList.get(i).getId();
                            d_date=smsOverallDataList.get(i).getDate();
                            d_time_stamp=smsOverallDataList.get(i).getTime_stamp();
                            d_title=smsOverallDataList.get(i).getTitle();
                            d_sms_details=smsOverallDataList.get(i).getSms_detail();

                            SMSOverallAdapterBean smsOverallAdapterBean=new SMSOverallAdapterBean(d_id,d_date,d_time_stamp,d_title,
                                    d_sms_details);
                            list.add(smsOverallAdapterBean);

                        }
                    }else{
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                        builder.setTitle("Alert");
                        builder.setIcon(R.drawable.error);
                        builder.setMessage("No data found")
                                .setCancelable(false)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        getActivity().finish();
                                    }
                                });
                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                    }


                    smsOverallAdapter.notifyDataSetChanged();

                }else{
                    progressDialog.dismiss();
                    try {
                        System.out.println("todayHomeWork_bean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<SMS_OverallBean> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    public class SMSOverallAdapter extends RecyclerView.Adapter<SMSOverallAdapter.ViewHolder> {
        private List<SMSOverallAdapterBean> smsOverallAdapterBeans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_title,text_date,text_sms_details;

            public ViewHolder(View view) {
                super(view);
                text_title = (TextView) view.findViewById(R.id.text_title);
                text_date = (TextView) view.findViewById(R.id.text_date);
                text_sms_details = (TextView) view.findViewById(R.id.text_sms_details);
            }
        }

        public SMSOverallAdapter(List<SMSOverallAdapterBean> mlist) {
            this.smsOverallAdapterBeans = mlist;
            this.context = context;
        }

        @Override
        public SMSOverallAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_sms_overall, parent, false);

            return new SMSOverallAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SMSOverallAdapter.ViewHolder holder, int position) {
            SMSOverallAdapterBean data = smsOverallAdapterBeans.get(position);
            holder.text_title.setText(data.getTitle());
            holder.text_date.setText(data.getDate());
            holder.text_sms_details.setText(data.getSms_details());
        }

        @Override
        public int getItemCount() {
            return smsOverallAdapterBeans.size();
        }
    }

    public class SMSOverallAdapterBean{
        private String id,date,time_stamp,title,sms_details;

        public SMSOverallAdapterBean(String id,String date,String time_stamp,String title,String sms_details){
            this.id=id;
            this.date=date;
            this.time_stamp=time_stamp;
            this.title=title;
            this.sms_details=sms_details;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime_stamp() {
            return time_stamp;
        }

        public void setTime_stamp(String time_stamp) {
            this.time_stamp = time_stamp;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSms_details() {
            return sms_details;
        }

        public void setSms_details(String sms_details) {
            this.sms_details = sms_details;
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
