package activities;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bluebase.educationapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.io.IOException;
import java.util.ArrayList;

import bean.TransportRouStoppingBean;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.AndroidUtil;
import util.ObjectSerializer;
import util.Utility;

public class TransportRouteStoppingDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private String bus_no,bus_photo,pickup_time,trap_time,driver,driver_phone_no,driver_photo,care_taker,
            care_taker_phone_no,care_taker_photo,route,stopping_point,discount_cat,status,code;
    private TextView text_route,text_stoppingpoint,text_discount_cat,text_busno,text_pickuptime,text_trap_time,
            text_drivername,text_driverphoneno,text_caretakername,text_caretakerphoneno;
    private CircularImageView cir_busphoto,cir_driverphoto,cir_caretakerphoto;
    private Spinner spin_siblings;
    ArrayList<String> list_spinner, list_id, list_name, list_section, list_code;
    String spin_id, spin_id_code, selected_id, s_id, sibling_id,token;
    int spin_id_pos = 0;
    boolean networkAvailability=false;
    TextView text_date;
    View root;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    AndroidUtil util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_route_stopping_details);

        builder = new AlertDialog.Builder(this);
        prefs = getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token= prefs.getString("token","");
        sibling_id=prefs.getString("login_student_id","");

        System.out.println("siblign id---"+sibling_id);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Transport");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        util = new AndroidUtil(TransportRouteStoppingDetails.this);
        if (!util.isOnline()) {
            builder.setMessage("No Internet Connection").setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getSupportActionBar().getThemedContext(), android.R.layout.simple_spinner_item, list_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_siblings.setAdapter(adapter);

        loadjson_beforeitemchange();
    }

    private void findviewbyid() {
        text_route=findViewById(R.id.text_route);
        text_stoppingpoint=findViewById(R.id.text_stoppingpoint);
        text_discount_cat=findViewById(R.id.text_discount_cat);
        text_busno=findViewById(R.id.text_busno);
        text_pickuptime=findViewById(R.id.text_pickuptime);
        text_trap_time=findViewById(R.id.text_trap_time);
        text_drivername=findViewById(R.id.text_drivername);
        text_driverphoneno=findViewById(R.id.text_driverphoneno);
        text_caretakername=findViewById(R.id.text_caretakername);
        text_caretakerphoneno=findViewById(R.id.text_caretakerphoneno);

        cir_busphoto=findViewById(R.id.cir_busphoto);
        cir_driverphoto=findViewById(R.id.cir_driverphoto);
        cir_caretakerphoto=findViewById(R.id.cir_caretakerphoto);


    }

    private void loadjson_beforeitemchange(){
        findviewbyid();
        loadJSON();
    }

    private void loadjson_onitemchange(){
        findviewbyid();
        sibling_id=prefs.getString("sibling_id_transport","");
        System.out.println("sibling_id_transport id---"+sibling_id);

        loadJSON();
    }

    private void loadJSON() {

        showBar();
        Call<TransportRouStoppingBean> call= RetrofitClient.getInstance().getApi().getTransportRouteStoppingDetails("Bearer "+token,sibling_id);
        call.enqueue(new Callback<TransportRouStoppingBean>() {

            @Override
            public void onResponse(Call<TransportRouStoppingBean> call, Response<TransportRouStoppingBean> response) {

                progressDialog.dismiss();
                if(response.isSuccessful()){
                    TransportRouStoppingBean transportRouStoppingBean=response.body();

                    status=transportRouStoppingBean.getStatus();
                    code=transportRouStoppingBean.getCode();

                    TransportRouStoppingBean.TRSData trsData=transportRouStoppingBean.getTrsData();
                    route=trsData.getRoute();
                    stopping_point=trsData.getStopping_point();
                    discount_cat= trsData.getDiscount_category();

                    TransportRouStoppingBean.TRSDataVehicle trsDataVehicle=trsData.getTrsDataVehicle();
                    bus_no=trsDataVehicle.getBus_no();
                    bus_photo=trsDataVehicle.getBus_photo();
                    pickup_time=trsDataVehicle.getPickup_time();
                    trap_time=trsDataVehicle.getTrap_time();
                    driver=trsDataVehicle.getDriver();
                    driver_phone_no=trsDataVehicle.getDriver_phone_no();
                    driver_photo=trsDataVehicle.getDriver_photo();
                    care_taker=trsDataVehicle.getCare_taker();
                    care_taker_phone_no=trsDataVehicle.getCare_taker_phone_no();
                    care_taker_photo=trsDataVehicle.getCare_taker_photo();

                    if(route!=null){
                        text_route.setText(route);
                    }else{
                        text_route.setText("Not Available");
                    }

                    if(stopping_point!=null){
                        text_stoppingpoint.setText(stopping_point);
                    }else{
                        text_stoppingpoint.setText("Not Available");
                    }

                    if(discount_cat!=null){
                        text_discount_cat.setText(discount_cat);
                    }else{
                        text_discount_cat.setText("Not Available");
                    }

                    if(bus_no!=null){
                        text_busno.setText(bus_no);
                    }else{
                        text_busno.setText("Not Available");
                    }

                    if(pickup_time!=null){
                        text_pickuptime.setText(pickup_time);
                    }else{
                        text_pickuptime.setText("Not Available");
                    }

                    if(trap_time!=null){
                        text_trap_time.setText(trap_time);
                    }else{
                        text_trap_time.setText("Not Available");
                    }

                    if(driver!=null){
                        text_drivername.setText(driver);
                    }else{
                        text_drivername.setText("Not Available");
                    }

                    if(driver!=null){
                        text_drivername.setText(driver);
                    }else{
                        text_drivername.setText("Not Available");
                    }

                    if(driver_phone_no!=null){
                        text_driverphoneno.setText(driver_phone_no);
                    }else{
                        text_driverphoneno.setText("Not Available");
                    }

                    if(care_taker!=null){
                        text_caretakername.setText(care_taker);
                    }else{
                        text_caretakername.setText("Not Available");
                    }

                    if(care_taker_phone_no!=null){
                        text_caretakerphoneno.setText(care_taker_phone_no);
                    }else{
                        text_caretakerphoneno.setText("Not Available");
                    }

                    if(bus_photo!=null){
                        cir_busphoto.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(bus_photo).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(cir_busphoto);
                    }else{
                        cir_busphoto.setVisibility(View.GONE);
                    }

                    if(driver_photo!=null){
                        cir_driverphoto.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(driver_photo).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(cir_driverphoto);
                    }else{
                        cir_driverphoto.setVisibility(View.GONE);
                    }

                    if(care_taker_photo!=null){
                        cir_caretakerphoto.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(care_taker_photo).diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(cir_caretakerphoto);
                    }else{
                        cir_caretakerphoto.setVisibility(View.GONE);
                    }


                }else{
                    progressDialog.dismiss();
                    Utility.showAlertDialog(TransportRouteStoppingDetails.this,"Error",response.errorBody().toString(),false);
                }

            }

            @Override
            public void onFailure(Call<TransportRouStoppingBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(TransportRouteStoppingDetails.this,"Error",t.getMessage(),false);
            }

        });

    }

    @Override
    public void onBackPressed() {
        Intent MainActivity = new Intent(getBaseContext(), Dashboard.class);
        editor.putString("checkstring", "transport").commit();
        MainActivity.addCategory(Intent.CATEGORY_HOME);
        MainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(MainActivity);
        TransportRouteStoppingDetails.this.finish();
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

                    editor.putString("sibling_id_transport",sibling_id);
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
        builder = new AlertDialog.Builder(TransportRouteStoppingDetails.this);
        progressDialog = new ProgressDialog(TransportRouteStoppingDetails.this);
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