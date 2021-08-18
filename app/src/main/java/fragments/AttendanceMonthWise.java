package fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.bluebase.educationapp.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import activities.Attendance;
import activities.LoginScreen;
import bean.AttendanceMonthWiseBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.AndroidUtil;
import util.Utility;

public class AttendanceMonthWise extends Fragment implements AdapterView.OnItemSelectedListener{
    View v;
    TextView tv_tot_full_day_abs,tv_tot_morning_abs,tv_tot_evening_abs,tv_percentage;
    PieChart pieChart;
    String no_of_working_days,present,absent,percentage,status,code,msg,id,date,leave_type,remark,created_at;
    boolean networkAvailability=false;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    String sibling_id,token,st_spin_month;
    int month_no;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    Spinner spin_month;
    private ArrayAdapter<CharSequence> adapter;
    String spin_id, spin_id_code, selected_id, s_id;
    int spin_id_pos = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.attendance_month_wise, container, false);
        prefs = getActivity().getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        ((Attendance)getActivity()).setFragmentRefreshListener(new Attendance.FragmentRefreshListener_att() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onRefresh() {
                // Refresh Your Fragment
                sibling_id = prefs.getString("sibling_id","");
                initView();
            }
        });

        networkAvailability= Utility.isConnectingToInternet(getActivity());

        if(networkAvailability==true){
            initView();
        }else{
            Utility.getAlertNetNotConneccted(getContext(), "Internet Connection");
        }

        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        spin_month=v.findViewById(R.id.spin_month);
        spin_month.setOnItemSelectedListener(this);

        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.month, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_month.setAdapter(adapter);

        pieChart=v.findViewById(R.id.pieChart);
        tv_tot_full_day_abs=v.findViewById(R.id.tv_tot_full_day_abs);
        tv_tot_morning_abs=v.findViewById(R.id.tv_tot_morning_abs);
        tv_tot_evening_abs=v.findViewById(R.id.tv_tot_evening_abs);
        tv_percentage=v.findViewById(R.id.tv_percentage);

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month_no = localDate.getMonthValue();
        String monname=localDate.getMonth().name();
        System.out.println("month====="+month_no);

        loadJSON();

        if (monname!= null) {
            int spinnerPosition = adapter.getPosition(monname);
            spin_month.setSelection(spinnerPosition);
        }

    }

    private void loadJSON() {
        Call<AttendanceMonthWiseBean> call= RetrofitClient.getInstance().getApi().getAttendanceMonthWise("Bearer "+token,
                sibling_id,String.valueOf(month_no));
        call.enqueue(new Callback<AttendanceMonthWiseBean>() {

            @Override
            public void onResponse(Call<AttendanceMonthWiseBean> call, Response<AttendanceMonthWiseBean> response) {

                if(response.isSuccessful()){
                    AttendanceMonthWiseBean attendanceMonthWiseBean=response.body();
                    status=attendanceMonthWiseBean.getStatus();
                    code=attendanceMonthWiseBean.getCode();
                    msg=attendanceMonthWiseBean.getMsg();

                    AttendanceMonthWiseBean.AttendanceMWPercentage attendanceMWPercentage=
                            attendanceMonthWiseBean.getAttendanceMWPercentage();
                    no_of_working_days=attendanceMWPercentage.getNo_of_working_days();
                    present=attendanceMWPercentage.getPresent();
                    absent=attendanceMWPercentage.getAbsent();
                    percentage=attendanceMWPercentage.getPercentage();

                    List<AttendanceMonthWiseBean.AttendanceMWDataBean> attendanceMWDataBeanList=
                            attendanceMonthWiseBean.getAttendanceMWDataBeanList();
                    for (int i=0;i<attendanceMWDataBeanList.size();i++){
                        id=attendanceMWDataBeanList.get(i).getId();
                        date=attendanceMWDataBeanList.get(i).getDate();
                        leave_type=attendanceMWDataBeanList.get(i).getLeave_type();
                        remark=attendanceMWDataBeanList.get(i).getRemark();
                        created_at=attendanceMWDataBeanList.get(i).getCreated_at();
                    }

                    tv_tot_full_day_abs.setText(no_of_working_days);
                    tv_tot_morning_abs.setText(absent);
                    tv_tot_evening_abs.setText(present);
                    tv_percentage.setText(percentage);

                    ArrayList<Entry> NoOfEmp = new ArrayList<Entry>();

                    NoOfEmp.add(new Entry(Float.parseFloat(no_of_working_days), 0));
                    NoOfEmp.add(new Entry(Float.parseFloat(present), 1));
                    NoOfEmp.add(new Entry(Float.parseFloat(absent), 2));
                    PieDataSet dataSet = new PieDataSet(NoOfEmp, "");

                    ArrayList<String> year = new ArrayList<String>();

                    year.add("Total full days absent");
                    year.add("Total Morning Absent");
                    year.add("Total Evening Absent");
                    PieData data = new PieData(year, dataSet);
                    pieChart.setData(data);
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    pieChart.animateXY(5000, 5000);
                    pieChart.setDrawSliceText(false);
                    pieChart.getData().setDrawValues(false);

                }else{
                    Utility.showAlertDialog(getActivity(),"Error",response.errorBody().toString(),false);
                }

            }

            @Override
            public void onFailure(Call<AttendanceMonthWiseBean> call, Throwable t) {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent == spin_month) {
            spin_id_pos = position;
            if (spin_id_pos > 0) {
                selected_id = spin_month.getSelectedItem().toString().trim();
                month_no=AndroidUtil.getMonthNumber(selected_id);
                loadJSON();
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
