package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import activities.SMSScreen;
import bean.SMSSpecificBean;
import bean.SMS_OverallBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class SMS_Specific extends Fragment {

    View v;
    String current_page,d_id,d_date,d_title,d_sms_details;
    private List<SMSSpecificAdapterBean> list=new ArrayList<>();
    private RecyclerView recyclerView;
    private SMSSpecificAdapter smsSpecificAdapter;
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    LinearLayout ll_nodatafound;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.sms_specific, container, false);

        networkAvailability= Utility.isConnectingToInternet(getActivity());


        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");


        ((SMSScreen)getActivity()).setFragmentRefreshListener(new SMSScreen.FragmentRefreshListener_sms() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
                Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
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
        smsSpecificAdapter = new SMSSpecificAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(smsSpecificAdapter);
        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<SMSSpecificBean> call= RetrofitClient.getInstance().getApi().getSMSSpecific("Bearer "+token,sibling_id);
        call.enqueue(new Callback<SMSSpecificBean>() {

            @Override
            public void onResponse(Call<SMSSpecificBean> call, Response<SMSSpecificBean> response) {

                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    SMSSpecificBean smsSpecificBean=response.body();
                    current_page=smsSpecificBean.getCurrent_page();

                    List<SMSSpecificBean.SMSSpecificData> smsSpecificDataList=smsSpecificBean.getSmsSpecificDataList();
                    if(smsSpecificDataList.size()>0){
                        recyclerView.setVisibility(View.VISIBLE);
                        for (int i=0;i<smsSpecificDataList.size();i++){
                            d_id=smsSpecificDataList.get(i).getId();
                            d_date=smsSpecificDataList.get(i).getDate();
                            d_title=smsSpecificDataList.get(i).getTitle();
                            d_sms_details=smsSpecificDataList.get(i).getSms_detail();

                            SMSSpecificAdapterBean smsSpecificAdapter=new SMSSpecificAdapterBean(d_id,d_date,d_title,
                                    d_sms_details);
                            list.add(smsSpecificAdapter);

                        }
                        smsSpecificAdapter.notifyDataSetChanged();
                    }else{
                        ll_nodatafound=v.findViewById(R.id.ll_nodatafound);
                        ll_nodatafound.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }


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
            public void onFailure(Call<SMSSpecificBean> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    public class SMSSpecificAdapter extends RecyclerView.Adapter<SMSSpecificAdapter.ViewHolder> {
        private List<SMSSpecificAdapterBean> smsSpecificAdapterBeans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_title,text_sms_details;

            public ViewHolder(View view) {
                super(view);
                text_title = (TextView) view.findViewById(R.id.text_title);
                text_sms_details = (TextView) view.findViewById(R.id.text_sms_details);
            }
        }

        public SMSSpecificAdapter(List<SMSSpecificAdapterBean> mlist) {
            this.smsSpecificAdapterBeans = mlist;
            this.context = context;
        }

        @Override
        public SMSSpecificAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_sms_specific, parent, false);


            return new SMSSpecificAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(SMSSpecificAdapter.ViewHolder holder, int position) {
            SMSSpecificAdapterBean data = smsSpecificAdapterBeans.get(position);
            holder.text_title.setText(data.getTitle());
            holder.text_sms_details.setText(data.getSms_details());
        }

        @Override
        public int getItemCount() {
            return smsSpecificAdapterBeans.size();
        }
    }

    public class SMSSpecificAdapterBean{
        private String id,date,time_stamp,title,sms_details;

        public SMSSpecificAdapterBean(String id,String date,String title,String sms_details){
            this.id=id;
            this.date=date;
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
