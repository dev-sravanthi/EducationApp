package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bluebase.educationapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bean.ErrorBean;
import bean.LoginData;
import bean.PD_DataBean;
import bean.PD_FatherDetails;
import bean.PD_MotherDetails;
import bean.PD_PassportDetails;
import bean.ProfileDatabean;
import bean.ProfileEditBean;
import bean.ReligionBean;
import bean.ReligionDataBean;
import bean.StudentDataBean_sample;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.MarshMallowPermission;
import util.Utility;

public class ProfileDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    CircularImageView fimage_edit,fimageview,mimageview,mimage_edit;

    EditText ed_fname,ed_femailid,ed_fmobno,ed_fwatsappno,ed_flandlineno,ed_fmonthlyincome,ed_fofficeaddress,
            ed_mname,ed_memailid,ed_mmobno,ed_mwatsappno,ed_mlandlineno,ed_mmonthlyincome,ed_mofficeaddress,
            ed_fpassportno,ed_fplaceofissue,ed_fdateofissue,ed_fdateofexpiry,ed_frespermitno,ed_fcivilidno,
            ed_fdateofresperno,ed_fdateofresperexpiry;
    Spinner spin_fprofession,spin_freligion,spin_mprofession,spin_mreligion;
    Button btn_edit;
    String status,code,user_name,data_code,email,phone,photo,city,pincode,address,fprofession,fname,femail,fofficeaddress,
            ftelephone_no,fphone_no,fwatsapp_np,fphoto,mprofession,mname,memail,mofficeaddress,mtelephone_no,mphone_no,
            mwatsapp_np,mphoto,passport_no,date_of_issue,date_of_expiry,place_of_issue,civil_id_no,residence_permit_no,
            date_of_res_permit_issue,entered_country_date,st_ed_fname,st_ed_femailid,st_ed_fmobno,st_ed_fwatsappno,
            st_ed_flandlineno,st_ed_fmonthlyincome,st_ed_fofficeaddress,st_ed_mname,st_ed_memailid,st_ed_mmobno,
            st_ed_mwatsappno,st_ed_mlandlineno,st_ed_mmonthlyincome,st_ed_mofficeaddress,st_ed_fpassportno,
            st_ed_fplaceofissue,st_ed_fdateofissue,st_ed_fdateofexpiry,st_ed_frespermitno,st_ed_fcivilidno,
            st_ed_fdateofresperno,st_ed_fdateofresperexpiry,st_spin_fprofession,st_spin_freligion,st_spin_mprofession,
            st_spin_mreligion,p_address,p_city,p_state_id,p_country_id,p_pin_code,r_address,r_city,r_state_id,
            r_country_id,r_pin_code;
    ArrayList<String> list_religion,list_profession;
    boolean networkAvailability=false;
    private MarshMallowPermission marshMallowPermission;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION};
    File destination1,destination2;
    String mCurrentPhotoPath;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    Bitmap bitMap2=null,bitMap1=null;
    String picpath1,picpath2,st_img_one="",st_img_two="",token;
    private static final int CAMERA_PIC_REQUEST_ONE = 1,CAMERA_PIC_REQUEST_TWO=2;
    private AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);

        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        prefs = getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        token=prefs.getString("token","");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Data");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        networkAvailability= Utility.isConnectingToInternet(ProfileDetails.this);

        if(networkAvailability==true){
            findviewids();
            loadProfileJSON();
           // loadStudenJSON();
            loadReligionData();
            loadProfessionData();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                marshMallowPermission = new MarshMallowPermission(ProfileDetails.this);
                if (!marshMallowPermission.hasPermissions(ProfileDetails.this, PERMISSIONS)) {
                    ActivityCompat.requestPermissions(ProfileDetails.this, PERMISSIONS, PERMISSION_ALL);
                }
            }

            destination1 = new File(Environment.getExternalStorageDirectory(),"MDM.jpg");
            if (!isDeviceSupportCamera()) {
                Toast.makeText(getApplicationContext(),"Sorry! Your device doesn't support camera",
                        Toast.LENGTH_LONG).show();
                finish();
            }

        }else{
            Utility.getAlertNetNotConneccted(ProfileDetails.this, "Internet Connection");
        }

    }

    private void findviewids() {

        fimageview=findViewById(R.id.fimageview);
        fimage_edit=findViewById(R.id.fimage_edit);
        mimageview=findViewById(R.id.mimageview);
        mimage_edit=findViewById(R.id.mimage_edit);

        fimage_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (marshMallowPermission.hasPermissions(ProfileDetails.this, PERMISSIONS)) {
                        captureImage_one();
                    } else {
                        ActivityCompat.requestPermissions(ProfileDetails.this, PERMISSIONS, PERMISSION_ALL);
                    }
                } else {
                    captureImage_one();
                }
            }
        });

        mimage_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (marshMallowPermission.hasPermissions(ProfileDetails.this, PERMISSIONS)) {
                        captureImage_two();
                    } else {
                        ActivityCompat.requestPermissions(ProfileDetails.this, PERMISSIONS, PERMISSION_ALL);
                    }
                } else {
                    captureImage_two();
                }
            }
        });

        ed_fname=findViewById(R.id.ed_fname);
        ed_femailid=findViewById(R.id.ed_femailid);
        ed_fmobno=findViewById(R.id.ed_fmobno);
        ed_fwatsappno=findViewById(R.id.ed_fwatsappno);
        ed_flandlineno=findViewById(R.id.ed_flandlineno);
        ed_fmonthlyincome=findViewById(R.id.ed_fmonthlyincome);
        ed_fofficeaddress=findViewById(R.id.ed_fofficeaddress);
        ed_mname=findViewById(R.id.ed_mname);
        ed_memailid=findViewById(R.id.ed_memailid);
        ed_mmobno=findViewById(R.id.ed_mmobno);
        ed_mwatsappno=findViewById(R.id.ed_mwatsappno);
        ed_mlandlineno=findViewById(R.id.ed_mlandlineno);
        ed_mmonthlyincome=findViewById(R.id.ed_mmonthlyincome);
        ed_mofficeaddress=findViewById(R.id.ed_mofficeaddress);
        ed_fpassportno=findViewById(R.id.ed_fpassportno);
        ed_fplaceofissue=findViewById(R.id.ed_fplaceofissue);
        ed_fdateofissue=findViewById(R.id.ed_fdateofissue);
        ed_fdateofexpiry=findViewById(R.id.ed_fdateofexpiry);
        ed_frespermitno=findViewById(R.id.ed_frespermitno);
        ed_fcivilidno=findViewById(R.id.ed_fcivilidno);
        ed_fdateofresperno=findViewById(R.id.ed_fdateofresperno);
        ed_fdateofresperexpiry=findViewById(R.id.ed_fdateofresperexpiry);

        spin_fprofession=findViewById(R.id.spin_fprofession);
        spin_fprofession.setOnItemSelectedListener(this);

        spin_freligion=findViewById(R.id.spin_freligion);
        spin_freligion.setOnItemSelectedListener(this);

        spin_mprofession=findViewById(R.id.spin_mprofession);
        spin_mprofession.setOnItemSelectedListener(this);

        spin_mreligion=findViewById(R.id.spin_mreligion);
        spin_mreligion.setOnItemSelectedListener(this);

        list_profession=new ArrayList<>();
        list_religion=new ArrayList<>();

        btn_edit=findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fimageview.getDrawable() != null){
                    BitmapDrawable drawable = (BitmapDrawable) fimageview.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    if(bitmap==null){
                        Toast.makeText(ProfileDetails.this, "Capture Father Image", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        st_img_one=getEncoded64ImageStringFromBitmap(bitmap);
                    }
                }else{
                    if(bitMap1==null){
                        Toast.makeText(ProfileDetails.this, "Capture Father Image", Toast.LENGTH_SHORT).show();
                        return;
                    }else{

                        picpath1=destination1.getAbsolutePath();
                        st_img_one=getBase64Str(picpath1);

                    }
                }

                if (mimageview.getDrawable() != null){
                    BitmapDrawable drawable = (BitmapDrawable) mimageview.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    if(bitmap==null){
                        Toast.makeText(ProfileDetails.this, "Capture Mother Image", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        st_img_two=getEncoded64ImageStringFromBitmap(bitmap);
                    }
                }else{
                    if(bitMap2==null){
                        Toast.makeText(ProfileDetails.this, "Capture Mother Image", Toast.LENGTH_SHORT).show();                        return;
                    }else{
                        picpath2=destination2.getAbsolutePath();
                        st_img_two=getBase64Str(picpath2);
                    }

                }

                st_ed_fname = ed_fname.getText().toString();
                if (st_ed_fname == null || ed_fname.getText() == null || ed_fname.getText().toString().length() == 0) {
                    builder.setMessage("Enter Father Name").setCancelable(true).create().show();
                    ed_fname.requestFocus();
                    return;
                }

                st_ed_femailid = ed_femailid.getText().toString();
                if (st_ed_femailid == null || ed_femailid.getText() == null || ed_femailid.getText().toString().length()==0) {
                    builder.setMessage("Enter Father Email ID").setCancelable(true).create().show();
                    ed_femailid.requestFocus();
                    return;
                }

                st_ed_fmobno = ed_fmobno.getText().toString();
                if (st_ed_fmobno == null || ed_fmobno.getText() == null || ed_fmobno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Father Mobile No.").setCancelable(true).create().show();
                    ed_fmobno.requestFocus();
                    return;
                }

                st_ed_fwatsappno = ed_fwatsappno.getText().toString();
                if (st_ed_fwatsappno == null || ed_fwatsappno.getText() == null || ed_fwatsappno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Father whatsapp no.").setCancelable(true).create().show();
                    ed_fwatsappno.requestFocus();
                    return;
                }

                st_ed_flandlineno= ed_flandlineno.getText().toString();
                if (st_ed_flandlineno == null || ed_flandlineno.getText() == null || ed_flandlineno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Father Landline no.").setCancelable(true).create().show();
                    ed_flandlineno.requestFocus();
                    return;
                }

                st_ed_fmonthlyincome= ed_fmonthlyincome.getText().toString();
                if (st_ed_fmonthlyincome == null || ed_fmonthlyincome.getText() == null || ed_fmonthlyincome.getText().toString().length() == 0) {
                    builder.setMessage("Enter Father Monthly Income").setCancelable(true).create().show();
                    ed_fmonthlyincome.requestFocus();
                    return;
                }

                st_spin_fprofession= spin_fprofession.getSelectedItem().toString();
                if (spin_fprofession.getSelectedItem().equals("Select Profession")) {
                    builder.setMessage("Please Select Father Profession").setCancelable(true).create().show();
                    return;
                }

                st_spin_freligion= spin_freligion.getSelectedItem().toString();
                if (spin_freligion.getSelectedItem().equals("Select Religion")) {
                    builder.setMessage("Please Select Father Religion").setCancelable(true).create().show();
                    return;
                }

                st_ed_fofficeaddress= ed_fofficeaddress.getText().toString();
                if (st_ed_fofficeaddress == null || ed_fofficeaddress.getText() == null || ed_fofficeaddress.getText().toString().length() == 0) {
                    builder.setMessage("Enter Father Office Address").setCancelable(true).create().show();
                    ed_fmonthlyincome.requestFocus();
                    return;
                }

                st_ed_mname = ed_mname.getText().toString();
                System.out.println("ed_fname==="+ed_mname.getText().toString().length());
                if (st_ed_mname == null || ed_mname.getText() == null || ed_mname.getText().toString().length() == 0) {
                    builder.setMessage("Enter Mother Name").setCancelable(true).create().show();
                    ed_mname.requestFocus();
                    return;
                }

                st_ed_memailid = ed_memailid.getText().toString();
                if (st_ed_memailid == null || ed_memailid.getText() == null || ed_memailid.getText().toString().length()==0) {
                    builder.setMessage("Enter Mother Email ID").setCancelable(true).create().show();
                    ed_memailid.requestFocus();
                    return;
                }

                st_ed_mmobno = ed_mmobno.getText().toString();
                if (st_ed_mmobno == null || ed_mmobno.getText() == null || ed_mmobno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Mother Mobile No.").setCancelable(true).create().show();
                    ed_mmobno.requestFocus();
                    return;
                }

                st_ed_mwatsappno = ed_mwatsappno.getText().toString();
                if (st_ed_mwatsappno == null || ed_mwatsappno.getText() == null || ed_mwatsappno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Mother whatsapp no.").setCancelable(true).create().show();
                    ed_mwatsappno.requestFocus();
                    return;
                }

                st_ed_mlandlineno= ed_mlandlineno.getText().toString();
                if (st_ed_mlandlineno == null || ed_mlandlineno.getText() == null || ed_mlandlineno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Mother Landline no.").setCancelable(true).create().show();
                    ed_mlandlineno.requestFocus();
                    return;
                }

                st_ed_mmonthlyincome= ed_mmonthlyincome.getText().toString();
                if (st_ed_mmonthlyincome == null || ed_mmonthlyincome.getText() == null || ed_mmonthlyincome.getText().toString().length() == 0) {
                    builder.setMessage("Enter Mother Monthly Income").setCancelable(true).create().show();
                    ed_mmonthlyincome.requestFocus();
                    return;
                }

                st_spin_mprofession= spin_mprofession.getSelectedItem().toString();
                if (spin_mprofession.getSelectedItem().equals("Select Profession")) {
                    builder.setMessage("Please Select Mother Profession").setCancelable(true).create().show();
                    return;
                }

                st_spin_mreligion= spin_mreligion.getSelectedItem().toString();
                if (spin_mreligion.getSelectedItem().equals("Select Religion")) {
                    builder.setMessage("Please Select Mother Religion").setCancelable(true).create().show();
                    return;
                }

                st_ed_mofficeaddress= ed_mofficeaddress.getText().toString();
                if (st_ed_mofficeaddress == null || ed_mofficeaddress.getText() == null || ed_mofficeaddress.getText().toString().length() == 0) {
                    builder.setMessage("Enter Mother Office Address").setCancelable(true).create().show();
                    ed_mofficeaddress.requestFocus();
                    return;
                }

                st_ed_fpassportno= ed_fpassportno.getText().toString();
                if (st_ed_fpassportno == null || ed_fpassportno.getText() == null || ed_fpassportno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Passport No.").setCancelable(true).create().show();
                    ed_fpassportno.requestFocus();
                    return;
                }

                st_ed_fplaceofissue= ed_fplaceofissue.getText().toString();
                if (st_ed_fplaceofissue == null || ed_fplaceofissue.getText() == null || ed_fplaceofissue.getText().toString().length() == 0) {
                    builder.setMessage("Enter Place of Issue").setCancelable(true).create().show();
                    ed_fplaceofissue.requestFocus();
                    return;
                }

                st_ed_fdateofissue= ed_fdateofissue.getText().toString();
                if (st_ed_fdateofissue == null || ed_fdateofissue.getText() == null || ed_fdateofissue.getText().toString().length() == 0) {
                    builder.setMessage("Enter Date of Issue").setCancelable(true).create().show();
                    ed_fdateofissue.requestFocus();
                    return;
                }

                st_ed_fdateofexpiry= ed_fdateofexpiry.getText().toString();
                if (st_ed_fdateofexpiry == null || ed_fdateofexpiry.getText() == null || ed_fdateofexpiry.getText().toString().length() == 0) {
                    builder.setMessage("Enter Date of Expiry").setCancelable(true).create().show();
                    ed_fdateofexpiry.requestFocus();
                    return;
                }

                st_ed_frespermitno= ed_frespermitno.getText().toString();
                if (st_ed_frespermitno == null || ed_frespermitno.getText() == null || ed_frespermitno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Residence Permit No.").setCancelable(true).create().show();
                    ed_frespermitno.requestFocus();
                    return;
                }

                st_ed_fcivilidno= ed_fcivilidno.getText().toString();
                if (st_ed_fcivilidno == null || ed_fcivilidno.getText() == null || ed_fcivilidno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Civil ID No.").setCancelable(true).create().show();
                    ed_fcivilidno.requestFocus();
                    return;
                }

                st_ed_fdateofresperno= ed_fdateofresperno.getText().toString();
                if (st_ed_fdateofresperno == null || ed_fdateofresperno.getText() == null || ed_fdateofresperno.getText().toString().length() == 0) {
                    builder.setMessage("Enter Date of Res. Permit Issue").setCancelable(true).create().show();
                    ed_fdateofresperno.requestFocus();
                    return;
                }

//                st_ed_f= ed_fdateofresperexpiry.getText().toString();
//                if (st_ed_fpassportno == null || ed_fdateofresperexpiry.getText() == null || ed_fdateofresperexpiry.getText().toString().length() == 0) {
//                    builder.setMessage("Enter Date of Res. Permit Expiry").setCancelable(true).create().show();
//                    ed_fdateofresperexpiry.requestFocus();
//                    return;
//                }


                setProfileData();
//
//                android.app.AlertDialog.Builder builder = new
//                        android.app.AlertDialog.Builder(ProfileDetails.this);
//                builder.setTitle("Success");
//                builder.setIcon(R.drawable.success);
//                builder.setMessage("Parents Data Edited Successfully")
//                        .setCancelable(false)
//                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                Intent i =new Intent(getApplicationContext(),Dashboard.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        });
//                android.app.AlertDialog alert = builder.create();
//                alert.show();

            }
        });

    }

    private void setProfileData() {
        showBar();

        Call<ProfileEditBean> call=RetrofitClient.getInstance().getApi().setProfileFatherMotherEdit(
                st_ed_fname,st_ed_femailid,st_ed_fofficeaddress,st_ed_flandlineno,st_ed_fmobno,st_ed_fwatsappno,
                st_spin_freligion,st_spin_fprofession,st_img_one,st_ed_mname,st_ed_memailid,st_ed_mofficeaddress,
                st_ed_mlandlineno,st_ed_mmobno,st_ed_mwatsappno,st_spin_mreligion,st_spin_mprofession,st_img_two);

        call.enqueue(new Callback<ProfileEditBean>() {

            @Override
            public void onResponse(Call<ProfileEditBean> call, Response<ProfileEditBean> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    ProfileEditBean profileEditBean=response.body();
                    String status=profileEditBean.getStatus();

                    System.out.println("status========"+status);

                    Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                    startActivity(intent);
                    finish();

                }else{
                    progressDialog.dismiss();

                    try {
                        String errorBean=response.errorBody().string();
                        System.out.println("errorBean===="+errorBean);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("error");
                        String status=jObjError.getString("status");

                        Utility.showAlertDialog(ProfileDetails.this,"Error",error,false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ProfileEditBean> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(ProfileDetails.this,"Error",t.getMessage(),false);
            }
        });

    }

    private void loadProfileJSON() {
        showBar();
        Call<ProfileDatabean> call= RetrofitClient.getInstance().getApi().getProfileData("Bearer "+token);
        call.enqueue(new Callback<ProfileDatabean>() {

            @Override
            public void onResponse(Call<ProfileDatabean> call, Response<ProfileDatabean> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    ProfileDatabean profileDatabean=response.body();

                    status=profileDatabean.getStatus();
                    code=profileDatabean.getCode();

                    PD_DataBean pd_dataBean=profileDatabean.getPd_dataBean();
                    user_name=pd_dataBean.getUser_name();
                    data_code=pd_dataBean.getCode();
                    email=pd_dataBean.getEmail();
                    phone=pd_dataBean.getPhone();
                    photo=pd_dataBean.getPhoto();
                    city=pd_dataBean.getCity();
                    pincode=pd_dataBean.getPincode();
                    address=pd_dataBean.getAddress();

                    PD_FatherDetails pd_fatherDetails=pd_dataBean.getPd_fatherDetails();
                    fprofession=pd_fatherDetails.getProfession();
                    fname=pd_fatherDetails.getName();
                    femail=pd_fatherDetails.getEmail();
                    fofficeaddress=pd_fatherDetails.getOffice_address();
                    ftelephone_no=pd_fatherDetails.getTelephone_no();
                    fphone_no=pd_fatherDetails.getPhone_no();
                    fwatsapp_np=pd_fatherDetails.getWhats_app_no();
                    fphoto=pd_fatherDetails.getPhoto();

                    ed_fname.setText(fname);
                    ed_femailid.setText(femail);
                    ed_fmobno.setText(ftelephone_no);
                    ed_fwatsappno.setText(fwatsapp_np);
                    ed_flandlineno.setText(fphone_no);
                    Glide.with(getApplicationContext())
                            .load(fphoto)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(fimageview);

                    PD_MotherDetails pd_motherDetails=pd_dataBean.getPd_motherDetails();
                    mprofession=pd_motherDetails.getProfession();
                    mname=pd_motherDetails.getName();
                    memail=pd_motherDetails.getEmail();
                    mofficeaddress=pd_motherDetails.getOffice_address();
                    mtelephone_no=pd_motherDetails.getTelephone_no();
                    mphone_no=pd_motherDetails.getPhone_no();
                    mwatsapp_np=pd_motherDetails.getWhats_app_no();
                    mphoto=pd_motherDetails.getPhoto();

                    ed_mname.setText(mname);
                    ed_memailid.setText(memail);
                    ed_mmobno.setText(mphone_no);
                    ed_mwatsappno.setText(mwatsapp_np);
                    ed_mlandlineno.setText(mtelephone_no);
                    ed_mmonthlyincome.setText("");
                    ed_mofficeaddress.setText(mofficeaddress);
                    Glide.with(getApplicationContext())
                            .load(mphoto)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mimageview);

                    PD_PassportDetails pd_passportDetails=pd_dataBean.getPd_passportDetails();

                    passport_no=pd_passportDetails.getPassport_no();
                    date_of_issue=pd_passportDetails.getDate_of_issue();
                    date_of_expiry=pd_passportDetails.getDate_of_expiry();
                    place_of_issue=pd_passportDetails.getPlace_of_issue();
                    civil_id_no=pd_passportDetails.getCivil_id_no();
                    residence_permit_no=pd_passportDetails.getResidence_permit_no();
                    date_of_res_permit_issue=pd_passportDetails.getDate_of_res_permit_issue();
                    entered_country_date=pd_passportDetails.getEntered_country_date();

                    ed_fpassportno.setText(passport_no);
                    ed_fdateofissue.setText(date_of_issue);
                    ed_fdateofexpiry.setText(date_of_expiry);
                    ed_fplaceofissue.setText(place_of_issue);
                    ed_fcivilidno.setText(civil_id_no);
                    ed_frespermitno.setText(residence_permit_no);
                    ed_fdateofresperno.setText(date_of_res_permit_issue);

                }else{
                    try {
                        progressDialog.dismiss();
                        System.out.println("todayHomeWork_bean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ProfileDatabean> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    private void loadStudenJSON() {
        showBar();
        Call<StudentDataBean_sample> call= RetrofitClient.getInstance().getApi().getStudentDetails("Bearer "+token);
        call.enqueue(new Callback<StudentDataBean_sample>() {

            @Override
            public void onResponse(Call<StudentDataBean_sample> call, Response<StudentDataBean_sample> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    StudentDataBean_sample studentDataBean_sample=response.body();

                    status=studentDataBean_sample.getStatus();
                    code=studentDataBean_sample.getCode();

                    List<StudentDataBean_sample.S_data_bean> s_data_bean_list=
                            studentDataBean_sample.getS_data_beanList();
                    for(int i=0;i<s_data_bean_list.size();i++){
                        StudentDataBean_sample.permanent_address permanent_address=s_data_bean_list.get(i).getPermanent_address();
                        p_address=permanent_address.getAddress();
                        p_city=permanent_address.getCity();
                        p_country_id=permanent_address.getCountry_id();
                        p_pin_code=permanent_address.getPin_code();
                        p_state_id=permanent_address.getState_id();

                        StudentDataBean_sample.residential_address residential_address=
                                s_data_bean_list.get(i).getResidential_address();
                        r_address=residential_address.getAddress();
                        r_city=residential_address.getCity();
                        r_country_id=residential_address.getCountry_id();
                        r_pin_code=residential_address.getPin_code();
                        r_state_id=residential_address.getState_id();
                    }

                }else{
                    try {
                        progressDialog.dismiss();
                        System.out.println("todayHomeWork_bean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<StudentDataBean_sample> call, Throwable t) {
                progressDialog.dismiss();
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    private void loadReligionData() {
        Call<ReligionBean> call= RetrofitClient.getInstance().getApi().getRelegionList("Bearer "+token);
        call.enqueue(new Callback<ReligionBean>() {

            @Override
            public void onResponse(Call<ReligionBean> call, Response<ReligionBean> response) {
                if(response.isSuccessful()){
                    ReligionBean profileDatabean=response.body();

                    List<ReligionDataBean> religionDataBean=profileDatabean.getReligionDataBean();
                    list_religion.add("Select Religion");
                    for (int i=0;i<religionDataBean.size();i++){
                        String rel_name=religionDataBean.get(i).getName();
                        list_religion.add(rel_name);
                    }

                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(getApplicationContext(),
                                    android.R.layout.simple_spinner_dropdown_item, list_religion);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_freligion.setAdapter(adapter);
                    spin_mreligion.setAdapter(adapter);

                }else{
                    try {
                        System.out.println("getReligionDataBean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReligionBean> call, Throwable t) {
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    private void loadProfessionData() {
        Call<ReligionBean> call= RetrofitClient.getInstance().getApi().getProfessionList("Bearer "+token);
        call.enqueue(new Callback<ReligionBean>() {

            @Override
            public void onResponse(Call<ReligionBean> call, Response<ReligionBean> response) {
                if(response.isSuccessful()){
                    ReligionBean profileDatabean=response.body();

                    List<ReligionDataBean> professionDataBeans=profileDatabean.getReligionDataBean();
                    list_profession.add("Select Profession");
                    for (int i=0;i<professionDataBeans.size();i++){
                        String rel_name=professionDataBeans.get(i).getName();
                        list_profession.add(rel_name);
                    }

                    ArrayAdapter<String> adapter =new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_dropdown_item, list_profession);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_fprofession.setAdapter(adapter);
                    spin_mprofession.setAdapter(adapter);

                }else{
                    try {
                        System.out.println("getReligionDataBean====fail"+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ReligionBean> call, Throwable t) {
                System.out.println("todayHomeWork_bean===="+t.getMessage());
            }

        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private void captureImage_one() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                    ProfileDetails.this,
                    this.getApplicationContext().getPackageName()
                            + ".my.package.name.provider", createImageFile()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST_ONE);
        }

    }

    private void captureImage_two() {
        Intent takePictureIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ProfileDetails.this,
                    this.getApplicationContext().getPackageName()
                            + ".my.package.name.provider", createImageFile()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST_TWO);
        }

    }

    private File createImageFile() throws IOException {
        File image=null;
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "MDM2_" + timeStamp + "_";
            File storageDir = this.getExternalCacheDir();
            Log.i("storageDir==",""+storageDir);
            if(storageDir!=null){
                storageDir.mkdirs();
                image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                mCurrentPhotoPath = "file:" + image.getAbsolutePath();

            }else{
                Toast.makeText(getApplicationContext(), "No Storage Directory", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == CAMERA_PIC_REQUEST_ONE && resultCode == Activity.RESULT_OK) {
                try {
                    // img_preview_one.setVisibility(View.VISIBLE);
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    destination1 = new File(imageUri.getPath());

                    InputStream in = new FileInputStream(destination1);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    options.inJustDecodeBounds = false;
                    bitMap1 = BitmapFactory.decodeStream(in, null, options);
                    FileOutputStream outputStream = new FileOutputStream(destination1);

                    bitMap1.compress(Bitmap.CompressFormat.JPEG, 80 , outputStream);
                    fimageview.setImageBitmap(bitMap1);
                }
                catch (Exception e) {
                }
            }

            if (requestCode == CAMERA_PIC_REQUEST_TWO && resultCode == Activity.RESULT_OK) {
                try {
                    Uri imageUri = Uri.parse(mCurrentPhotoPath);
                    destination2 = new File(imageUri.getPath());

                    InputStream in = new FileInputStream(destination2);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    options.inJustDecodeBounds = false;
                    bitMap2 = BitmapFactory.decodeStream(in, null, options);
                    FileOutputStream outputStream = new FileOutputStream(destination2);

                    bitMap2.compress(Bitmap.CompressFormat.JPEG, 80 , outputStream);

                    mimageview.setImageBitmap(bitMap2);
                }
                catch (Exception e) {
                }
            }

        } catch (Exception er) {
            er.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Sorry! Failed to capture image.Try again", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case MarshMallowPermission.CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(this, "Camera permission is denied.", Toast.LENGTH_SHORT).show();
                }
                break;

            case MarshMallowPermission.READ_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println(" SD Card permitted " );
                } else {
                    Toast.makeText(this, "SD Card permission is denied. Please allow in App Settings for additional functionality.", Toast.LENGTH_SHORT).show();
                }
                break;
            case MarshMallowPermission.WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    System.out.println(" write sd card permitted " );
                } else {
                    Toast.makeText(this, "Memory permission is denied. Please allow in App Settings for additional functionality.", Toast.LENGTH_SHORT).show();
                }
                break;
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                    }

                } else {
                    Toast.makeText(this, "Location Permission denied. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }

    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);

                        //supportMapFragment.getMapAsync(SaveLocation.this);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    private boolean isDeviceSupportCamera() {
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    public static String getBase64Str(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        Bitmap converetdImage = getResizedBitmap(bitmap, 200);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        converetdImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        System.out.println("byteArray===="+byteArray.length);
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public String getEncoded64ImageStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        byte[] byteFormat = stream.toByteArray();
        // get the base 64 string
        String imgString = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
        return imgString;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    public void onBackPressed() {
        Intent dashboard = new Intent(getBaseContext(),Dashboard.class);
        dashboard.addCategory(Intent.CATEGORY_HOME);
        dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(dashboard);
        ProfileDetails.this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void showBar(){
        builder = new AlertDialog.Builder(this);
        progressDialog = new ProgressDialog(this);
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

}