package fragments;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.bluebase.educationapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import activities.FeesScreen;
import activities.HomeWork;
import bean.FeePendingBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class PendingFeesFrag extends Fragment {

    View v;
    String status,code,d_id,d_name,d_total,d_discount,d_paid,d_pending,fp_id,fp_name,fp_total,fp_discount,fp_paid,
            fp_pending,fp_subf_id,fp_subf_name,fp_subf_total,fp_subf_discount,fp_subf_paid,fp_subf_pending;
    ArrayList<String> list_data_id,list_fee_id,list_subfee_id;
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pending_fees_frag, container, false);

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((FeesScreen)getActivity()).setFragmentRefreshListener(new FeesScreen.FragmentRefreshListener_fees() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
                Toast.makeText(getActivity(), "refreshed", Toast.LENGTH_SHORT).show();
                sibling_id = prefs.getString("sibling_id","");
                findviewids();
            }
        });

        networkAvailability= Utility.isConnectingToInternet(getActivity());

        if(networkAvailability==true){
            findviewids();
        }else{
            Utility.getAlertNetNotConneccted(getContext(), "Internet Connection");
        }

        return v;
    }

    private void findviewids() {
        list_data_id=new ArrayList<>();
        list_fee_id=new ArrayList<>();
        list_subfee_id=new ArrayList<>();

       // loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<FeePendingBean> call= RetrofitClient.getInstance().getApi().getFeePendingData("Bearer "+token,sibling_id);
        call.enqueue(new Callback<FeePendingBean>() {

            @Override
            public void onResponse(Call<FeePendingBean> call, Response<FeePendingBean> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()){
                    FeePendingBean feePendingBean=response.body();
                    status=feePendingBean.getStatus();
                    code=feePendingBean.getCode();
                    List<FeePendingBean.FeePendingData> feePendingDataList=feePendingBean.getFeePendingDataList();
                    for (int i=0;i<feePendingDataList.size();i++){
                        d_id=feePendingDataList.get(i).getId();
                        d_total=feePendingDataList.get(i).getTotal();
                        d_discount=feePendingDataList.get(i).getDiscount();
                        d_name=feePendingDataList.get(i).getName();
                        d_paid=feePendingDataList.get(i).getPaid();
                        d_pending=feePendingDataList.get(i).getPending();

                        list_data_id.add(d_id);

                        List<FeePendingBean.FP_FeesBean> fp_feesBeanList=feePendingDataList.get(i).getFp_feesBeans();
                        for(int j=0;j<fp_feesBeanList.size();j++) {
                            fp_id=fp_feesBeanList.get(j).getId();
                            fp_name=fp_feesBeanList.get(j).getName();
                            fp_total=fp_feesBeanList.get(j).getTotal();
                            fp_discount=fp_feesBeanList.get(j).getDiscount();
                            fp_paid=fp_feesBeanList.get(j).getPaid();
                            fp_pending=fp_feesBeanList.get(j).getPending();

                            list_fee_id.add(fp_id);

                            List<FeePendingBean.FP_SubFeeBean> subFeeBeanList=fp_feesBeanList.get(j).getFp_subFeeBeans();

                            for(int k=0;k<subFeeBeanList.size();k++){
                                fp_subf_id=subFeeBeanList.get(k).getId();
                                fp_subf_name=subFeeBeanList.get(k).getName();
                                fp_subf_total=subFeeBeanList.get(k).getTotal();
                                fp_subf_discount=subFeeBeanList.get(k).getDiscount();
                                fp_subf_paid=subFeeBeanList.get(k).getPaid();
                                fp_subf_pending=subFeeBeanList.get(k).getPending();

                                list_subfee_id.add(fp_subf_id);
                            }

                        }

                    }

                }else{
                    progressDialog.dismiss();
                    Utility.showAlertDialog(getActivity(),"Error",response.errorBody().toString(),false);
                }

            }

            @Override
            public void onFailure(Call<FeePendingBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(getActivity(),"Error",t.getMessage(),false);
            }

        });

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