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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import activities.Attendance;
import bean.AttendanceOverallBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class AttendanceOverall extends Fragment {
    View v;
    TextView tv_tot_working_days,tv_tot_present,tv_tot_absent,tv_percentage;
    PieChart pieChart;
    String no_of_working_days,present,absent,percentage,status,code;
    boolean networkAvailability=false;
    TextView text_date;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.attendance_overall, container, false);

        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((Attendance)getActivity()).setFragmentRefreshListener(new Attendance.FragmentRefreshListener_att() {
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
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
        pieChart=v.findViewById(R.id.pieChart);
        tv_tot_working_days=v.findViewById(R.id.tv_tot_working_days);
        tv_tot_present=v.findViewById(R.id.tv_tot_present);
        tv_tot_absent=v.findViewById(R.id.tv_tot_absent);
        tv_percentage=v.findViewById(R.id.tv_percentage);

        loadJSON();
    }

    private void loadJSON() {
        showBar();
        Call<AttendanceOverallBean> call= RetrofitClient.getInstance().getApi().getAttendanceOverall("Bearer "+token,sibling_id);
        call.enqueue(new Callback<AttendanceOverallBean>() {

            @Override
            public void onResponse(Call<AttendanceOverallBean> call, Response<AttendanceOverallBean> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    AttendanceOverallBean attendanceOverallBean=response.body();
                    status=attendanceOverallBean.getStatus();
                    code=attendanceOverallBean.getCode();

                    AttendanceOverallBean.AttendanceOveralldataBean attendanceOveralldataBean=
                            attendanceOverallBean.getAttendanceOverallBean();
                    no_of_working_days=attendanceOveralldataBean.getNo_of_working_days();
                    present=attendanceOveralldataBean.getPresent();
                    absent=attendanceOveralldataBean.getAbsent();
                    percentage=attendanceOveralldataBean.getPercentage();

                    tv_tot_working_days.setText(no_of_working_days);
                    tv_tot_absent.setText(absent);
                    tv_tot_present.setText(present);
                    tv_percentage.setText(percentage);

                    ArrayList<Entry> NoOfEmp = new ArrayList<Entry>();

                    NoOfEmp.add(new Entry(Float.parseFloat(no_of_working_days), 0));
                    NoOfEmp.add(new Entry(Float.parseFloat(present), 1));
                    NoOfEmp.add(new Entry(Float.parseFloat(absent), 2));
                    PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

                    ArrayList<String> year = new ArrayList<String>();

                    year.add("No of working days");
                    year.add("Present");
                    year.add("Absent");

                    PieData data = new PieData(year, dataSet);
                    pieChart.setData(data);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieChart.animateXY(5000, 5000);
                    pieChart.setDrawSliceText(false);
                    pieChart.getData().setDrawValues(false);

                }else{
                    progressDialog.dismiss();
                    Utility.showAlertDialog(getActivity(),"Error",response.errorBody().toString(),false);
                }

            }

            @Override
            public void onFailure(Call<AttendanceOverallBean> call, Throwable t) {
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
