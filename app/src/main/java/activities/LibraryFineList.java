package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bluebase.educationapp.R;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import bean.LibraryFineListBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.AndroidUtil;
import util.ObjectSerializer;
import util.Utility;

public class LibraryFineList extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    boolean networkAvailability=false;
    private String id,code,date,accession_no,book_name,renew_get_date,renew_due_date,renew_total_day,return_renew_date,
            type,total_fine_days,single_day_fine,fine_amount,lost_book_amount,total_fine_amount,discount,discount_description,
            fine_status,academic;
    RecyclerView recyclerView;
    private List<LibFineListBean> list=new ArrayList<>();
    private LibFineListAdapter libFineListAdapter;
    private Spinner spin_siblings;
    ArrayList<String> list_spinner, list_id, list_name, list_section, list_code;
    String spin_id, spin_id_code, selected_id, s_id, sibling_id,token;
    int spin_id_pos = 0;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    AndroidUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_fine_list);

        builder = new AlertDialog.Builder(this);
        prefs = getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fine List");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        spin_siblings=findViewById(R.id.spin_siblings);
        spin_siblings.setOnItemSelectedListener(this);

        list_spinner = new ArrayList<>();

        list_id = new ArrayList<String>();
        list_name = new ArrayList<String>();
        list_section = new ArrayList<String>();
        list_code = new ArrayList<String>();
        list_spinner.add("-Siblings-");

        try {
            list_id = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString
                    ("student_id", ObjectSerializer.serialize(new ArrayList<String>())));
            list_name = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString
                    ("student_name", ObjectSerializer.serialize(new ArrayList<String>())));
            list_section = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString
                    ("student_sec", ObjectSerializer.serialize(new ArrayList<String>())));
            list_code = (ArrayList<String>) ObjectSerializer.deserialize(prefs.getString
                    ("student_code", ObjectSerializer.serialize(new ArrayList<String>())));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list_id.size(); i++) {
            String id = list_id.get(i);
            String name = list_name.get(i);
            String section = list_section.get(i);
            String code = list_code.get(i);

            list_spinner.add(code + "-" + name + "(" + section + ")");
        }

        util = new AndroidUtil(LibraryFineList.this);
        if (!util.isOnline()) {
            builder.setMessage("No Internet Connection").setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }
        loadjson_beforeitemchange();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), android.R.layout.simple_spinner_item, list_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_siblings.setAdapter(adapter);

    }

    private void findviewid() {
        recyclerView = findViewById(R.id.recyclerView);
        list.clear();
        recyclerView.setHasFixedSize(true);
        libFineListAdapter = new LibFineListAdapter(list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), 0));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(libFineListAdapter);
    }

    private void loadjson_beforeitemchange(){
        findviewid();
        loadJSON();
    }

    private void loadjson_onitemchange(){
        findviewid();
        sibling_id=prefs.getString("sibling_id_finelist","");
        System.out.println("sibling_id_transport id---"+sibling_id);

        loadJSON();
    }


    private void loadJSON() {
        showBar();
        Call<LibraryFineListBean> call= RetrofitClient.getInstance().getApi().getLibraryFineList("Bearer "+token,sibling_id);
        call.enqueue(new Callback<LibraryFineListBean>() {
            @Override
            public void onResponse(Call<LibraryFineListBean> call, Response<LibraryFineListBean> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()){
                    LibraryFineListBean libraryFineListBean=response.body();

                    List<LibraryFineListBean.LibFLData> libFLData=libraryFineListBean.getLibFLDataList();

                    for (int i=0;i<libFLData.size();i++){
                        id=libFLData.get(i).getId();
                        code=libFLData.get(i).getCode();
                        date=libFLData.get(i).getDate();
                        accession_no=libFLData.get(i).getAccession_no();
                        book_name=libFLData.get(i).getBook_name();
                        renew_get_date=libFLData.get(i).getRenew_get_date();
                        renew_due_date=libFLData.get(i).getRenew_due_date();
                        renew_total_day=libFLData.get(i).getRenew_total_day();
                        return_renew_date=libFLData.get(i).getReturn_renew_date();
                        type=libFLData.get(i).getType();
                        total_fine_days=libFLData.get(i).getTotal_fine_days();
                        single_day_fine=libFLData.get(i).getSingle_day_fine();
                        fine_amount=libFLData.get(i).getFine_amount();
                        lost_book_amount=libFLData.get(i).getLost_book_amount();
                        total_fine_amount=libFLData.get(i).getTotal_fine_amount();
                        discount=libFLData.get(i).getDiscount();
                        discount_description=libFLData.get(i).getDiscount_description();
                        fine_status=libFLData.get(i).getFine_status();
                        academic=libFLData.get(i).getAcademic();

                        LibFineListBean fineListBean=new LibFineListBean(id,code,date,accession_no,book_name,
                                renew_get_date,renew_due_date,renew_total_day,return_renew_date,type,
                                total_fine_days,single_day_fine,fine_amount,lost_book_amount,total_fine_amount,
                                discount,discount_description,fine_status,academic);
                        list.add(fineListBean);
                    }

                    libFineListAdapter.notifyDataSetChanged();

                }else{
                    progressDialog.dismiss();
                    Utility.showAlertDialog(LibraryFineList.this,"Error",response.errorBody().toString(),false);
                }

            }

            @Override
            public void onFailure(Call<LibraryFineListBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(LibraryFineList.this,"Error",t.getMessage(),false);
            }

        });

    }

    public class LibFineListAdapter extends RecyclerView.Adapter<LibFineListAdapter.ViewHolder> {
        private List<LibFineListBean> libFineListBeans;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView text_bookname,text_borrow_no,text_accession_no,text_return_renew_date,text_total_fine,
                    text_fine_status,text_status,text_finedays,text_singledayfine,text_lostbookamount;

            public ViewHolder(View view) {
                super(view);
                text_bookname = (TextView) view.findViewById(R.id.text_bookname);
                text_borrow_no = (TextView) view.findViewById(R.id.text_borrow_no);
                text_accession_no= (TextView) view.findViewById(R.id.text_accession_no);
                text_return_renew_date= (TextView) view.findViewById(R.id.text_return_renew_date);
                text_total_fine= (TextView) view.findViewById(R.id.text_total_fine);
                text_fine_status= (TextView) view.findViewById(R.id.text_fine_status);
                text_status= (TextView) view.findViewById(R.id.text_status);
                text_finedays= (TextView) view.findViewById(R.id.text_finedays);
                text_singledayfine= (TextView) view.findViewById(R.id.text_singledayfine);
                text_lostbookamount= (TextView) view.findViewById(R.id.text_lostbookamount);

            }
        }

        public LibFineListAdapter(List<LibFineListBean> mlist) {
            this.libFineListBeans = mlist;
            this.context = context;
        }

        @Override
        public LibFineListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_library_fine_list, parent, false);

            return new LibFineListAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(LibFineListAdapter.ViewHolder holder, int position) {
            LibFineListBean data = libFineListBeans.get(position);
            holder.text_bookname.setText(data.getBook_name());
            holder.text_borrow_no.setText(data.getCode());
            holder.text_accession_no.setText(data.getAccession_no());
            holder.text_return_renew_date.setText(data.getReturn_renew_date());
            holder.text_total_fine.setText(data.getTotal_fine_amount());
            holder.text_fine_status.setText(data.getFine_status());
            holder.text_status.setText(data.getType());
            holder.text_finedays.setText(data.getTotal_fine_days());
            holder.text_singledayfine.setText(data.getSingle_day_fine());
            holder.text_lostbookamount.setText(data.getLost_book_amount());

        }

        @Override
        public int getItemCount() {
            return libFineListBeans.size();
        }
    }

    public class LibFineListBean{
        private String id,code,date,accession_no,book_name,renew_get_date,renew_due_date,renew_total_day,
                return_renew_date,type,total_fine_days,single_day_fine,fine_amount,lost_book_amount,
                total_fine_amount,discount,discount_description,fine_status,academic;

        public LibFineListBean(String id,String code,String date,String accession_no,String book_name,
                                 String renew_get_date,String renew_due_date,String renew_total_day,
                               String return_renew_date,String type,String total_fine_days,String single_day_fine,
                               String fine_amount,String lost_book_amount,String total_fine_amount,
                               String discount,String discount_description,String fine_status,String academic){
            this.id=id;
            this.code=code;
            this.date=date;
            this.accession_no=accession_no;
            this.book_name=book_name;
            this.renew_get_date=renew_get_date;
            this.renew_due_date=renew_due_date;
            this.renew_total_day=renew_total_day;
            this.return_renew_date=return_renew_date;
            this.type=type;
            this.total_fine_days=total_fine_days;
            this.single_day_fine=single_day_fine;
            this.fine_amount=fine_amount;
            this.lost_book_amount=lost_book_amount;
            this.total_fine_amount=total_fine_amount;
            this.discount=discount;
            this.discount_description=discount_description;
            this.fine_status=fine_status;
            this.academic=academic;
        }

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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spin_siblings) {
            spin_id_pos = position;
            if (spin_id_pos > 0) {
                selected_id = list_spinner.get(spin_id_pos).toString().trim();
                s_id = list_spinner.get(spin_id_pos).toString().trim();

                if (list_spinner.contains(s_id)) {
                    int id_code = list_spinner.indexOf(s_id);
                    sibling_id=list_id.get(id_code-1);

                    editor.putString("sibling_id_finelist",sibling_id);
                    editor.apply();

                    loadjson_onitemchange();

                }
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void showBar(){
        builder = new AlertDialog.Builder(LibraryFineList.this);
        progressDialog = new ProgressDialog(LibraryFineList.this);
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), Library.class);
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        LibraryFineList.this.finish();
    }

}