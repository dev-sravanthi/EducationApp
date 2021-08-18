package activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluebase.educationapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.StudentBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class StudentDetails extends AppCompatActivity {

    boolean networkAvailability=false;
    StudentAdapter studentAdapter;
    List<ST_bean> st_beanList=new ArrayList<>();
    RecyclerView recyclerView;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_layout);

        prefs = getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token=prefs.getString("token","");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student Details");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        networkAvailability= Utility.isConnectingToInternet(StudentDetails.this);

        if(networkAvailability==true){
            findviewid();
        }else{
            Utility.getAlertNetNotConneccted(StudentDetails.this, "Internet Connection");
        }

    }

    private void findviewid() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        studentAdapter = new StudentAdapter(st_beanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StudentDetails.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(StudentDetails.this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(studentAdapter);

        loadJSON();
    }

    private void loadJSON() {
        Call<StudentBean> call= RetrofitClient.getInstance().getApi().getStudentData("Bearer "+token);
        call.enqueue(new Callback<StudentBean>() {

            @Override
            public void onResponse(Call<StudentBean> call, Response<StudentBean> response) {

                if(response.isSuccessful()){
                    StudentBean studentBean=response.body();
                    System.out.println("code======"+studentBean.getCode());

                    List<StudentBean.StudentDataBean> list_studdatabean=studentBean.getStudentDataBeanList();
                    for (int i=0;i<list_studdatabean.size();i++){
                        System.out.println("academic_student_id===="+list_studdatabean.get(i).getAcademic_student_id());
                        String code=list_studdatabean.get(i).getCode();
                        String student_name=list_studdatabean.get(i).getStudent_name();
                        String standard_section=list_studdatabean.get(i).getStandard_section();
                        String community=list_studdatabean.get(i).getCommunity();
                        String caste=list_studdatabean.get(i).getCaste();
                        String sub_caste=list_studdatabean.get(i).getSub_caste();
                        String photo=list_studdatabean.get(i).getPhoto();
                        String gender=list_studdatabean.get(i).getGender();
                        String dob=list_studdatabean.get(i).getDob();
                        String doj=list_studdatabean.get(i).getDoj();
                        String academic=list_studdatabean.get(i).getAcademic();

                        ST_bean st_bean= new ST_bean(code,student_name,standard_section,student_name,community,caste,sub_caste,
                                photo,gender,dob,doj,academic);
                        st_beanList.add(st_bean);
                    }
                    studentAdapter.notifyDataSetChanged();

                }else{
                    try {
                        System.out.println("todayHomeWork_bean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<StudentBean> call, Throwable t) {
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    public class ST_bean{

        private String stu_id,stu_name,stu_class,stu_firstname,stu_community,stu_caste,stu_subcaste,
        stu_photo,stu_gender,stu_dob,stu_doj,stu_academic;

        public ST_bean(String stu_id, String stu_name, String stu_class,String stu_firstname,String stu_community,
                       String stu_caste,String stu_subcaste,String stu_photo,String stu_gender,String stu_dob,
                       String stu_doj,String stu_academic) {
            this.stu_id = stu_id;
            this.stu_name = stu_name;
            this.stu_class = stu_class;
            this.stu_firstname=stu_firstname;
            this.stu_community=stu_community;
            this.stu_caste=stu_caste;
            this.stu_subcaste=stu_subcaste;
            this.stu_photo=stu_photo;
            this.stu_gender=stu_gender;
            this.stu_dob=stu_dob;
            this.stu_doj=stu_doj;
            this.stu_academic=stu_academic;
        }

        public String getStu_photo() {
            return stu_photo;
        }

        public void setStu_photo(String stu_photo) {
            this.stu_photo = stu_photo;
        }

        public String getStu_gender() {
            return stu_gender;
        }

        public void setStu_gender(String stu_gender) {
            this.stu_gender = stu_gender;
        }

        public String getStu_dob() {
            return stu_dob;
        }

        public void setStu_dob(String stu_dob) {
            this.stu_dob = stu_dob;
        }

        public String getStu_doj() {
            return stu_doj;
        }

        public void setStu_doj(String stu_doj) {
            this.stu_doj = stu_doj;
        }

        public String getStu_academic() {
            return stu_academic;
        }

        public void setStu_academic(String stu_academic) {
            this.stu_academic = stu_academic;
        }

        public String getStu_id() {
            return stu_id;
        }

        public void setStu_id(String stu_id) {
            this.stu_id = stu_id;
        }

        public String getStu_name() {
            return stu_name;
        }

        public void setStu_name(String stu_name) {
            this.stu_name = stu_name;
        }

        public String getStu_class() {
            return stu_class;
        }

        public void setStu_class(String stu_class) {
            this.stu_class = stu_class;
        }

        public String getStu_firstname() {
            return stu_firstname;
        }

        public void setStu_firstname(String stu_firstname) {
            this.stu_firstname = stu_firstname;
        }

        public String getStu_community() {
            return stu_community;
        }

        public void setStu_community(String stu_community) {
            this.stu_community = stu_community;
        }

        public String getStu_caste() {
            return stu_caste;
        }

        public void setStu_caste(String stu_caste) {
            this.stu_caste = stu_caste;
        }

        public String getStu_subcaste() {
            return stu_subcaste;
        }

        public void setStu_subcaste(String stu_subcaste) {
            this.stu_subcaste = stu_subcaste;
        }
    }

    public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
        private List<ST_bean> st_beans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView text_idnameclass,text_stu_name,text_dob,text_doj,text_gender,text_academic,
                    text_community,text_caste,text_subcaste;
            public CircularImageView imgv_studentphoto;

            public ViewHolder(View view) {
                super(view);
                text_idnameclass = (TextView) view.findViewById(R.id.text_idnameclass);
                text_stu_name = (TextView) view.findViewById(R.id.text_stu_name);
                text_dob = (TextView) view.findViewById(R.id.text_dob);
                text_doj = (TextView) view.findViewById(R.id.text_doj);
                text_gender = (TextView) view.findViewById(R.id.text_gender);
                text_academic = (TextView) view.findViewById(R.id.text_academic);
                text_community = (TextView) view.findViewById(R.id.text_community);
                text_caste = (TextView) view.findViewById(R.id.text_caste);
                text_subcaste = (TextView) view.findViewById(R.id.text_subcaste);
                imgv_studentphoto = (CircularImageView) view.findViewById(R.id.imgv_studentphoto);
            }
        }

        public StudentAdapter(List<ST_bean> mlist)
        {
            this.st_beans = mlist;
            this.context=context;
        }

        @Override
        public StudentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_student_details, parent, false);

            return new StudentAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(StudentAdapter.ViewHolder holder, int position) {
            ST_bean data = st_beans.get(position);
            holder.text_idnameclass.setText(data.getStu_id()+" , "+data.getStu_name()+" , "+data.getStu_class());
            holder.text_stu_name.setText(data.getStu_name());
            holder.text_dob.setText(data.getStu_dob());
            holder.text_doj.setText(data.getStu_doj());
            holder.text_gender.setText(data.getStu_gender());
            holder.text_academic.setText(data.getStu_academic());
            if(data.getStu_community()==null){
                holder.text_community.setText("Not Available");
            }else{
                holder.text_community.setText(data.getStu_community());
            }

            if(data.getStu_community()==null){
                holder.text_caste.setText("Not Available");
            }else{
                holder.text_caste.setText(data.getStu_caste());
            }

            if(data.getStu_community()==null){
                holder.text_subcaste.setText("Not Available");
            }else{
                holder.text_subcaste.setText(data.getStu_subcaste());
            }

            Glide.with(holder.imgv_studentphoto.getContext())
                    .load(data.getStu_photo())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgv_studentphoto);

        }

        @Override
        public int getItemCount() {
            return st_beans.size();
        }
    }

    @Override
    public void onBackPressed() {
        Intent dashboard = new Intent(getBaseContext(), Dashboard.class);
        dashboard.addCategory(Intent.CATEGORY_HOME);
        dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dashboard);
        StudentDetails.this.finish();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case R.id.action_menu:
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivityForResult(intent, 0);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


}
