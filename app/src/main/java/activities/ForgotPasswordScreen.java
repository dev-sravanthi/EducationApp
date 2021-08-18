package activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bluebase.educationapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import bean.APIError;
import bean.ForgetVerificationBean;
import bean.LoginData;
import data.repo.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class ForgotPasswordScreen extends AppCompatActivity {

    private EditText ed_addmision_no,ed_phoneno,ed_rec_code,ed_set_new_pwd;
    private String st_ed_addmision_no,st_ed_phoneno,st_ed_rec_code,st_ed_set_new_pwd,status,code,messages,error;
    private Button btn_sendotp,btn_reset_pwd;
    private LinearLayout ll_otpcode,ll_setnewpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpasswordscreen);

        findviewbyid();
    }

    private void findviewbyid() {
        ed_addmision_no=findViewById(R.id.ed_addmision_no);
        ed_phoneno=findViewById(R.id.ed_phoneno);
        ed_rec_code=findViewById(R.id.ed_rec_code);
        ed_set_new_pwd=findViewById(R.id.ed_set_new_pwd);
        btn_reset_pwd=findViewById(R.id.btn_reset_pwd);
        btn_sendotp=findViewById(R.id.btn_sendotp);
        ll_otpcode=findViewById(R.id.ll_otpcode);
        ll_setnewpwd=findViewById(R.id.ll_setnewpwd);

        btn_sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                st_ed_addmision_no=ed_addmision_no.getText().toString().trim();
                if (st_ed_addmision_no.length()==0){
                    Utility.getAlertMsgEnter(ForgotPasswordScreen.this,"Admission No.");
                    return;
                }

                st_ed_phoneno=ed_phoneno.getText().toString().trim();
                if (st_ed_phoneno.length()==0){
                    Utility.getAlertMsgEnter(ForgotPasswordScreen.this,"Phone no.");
                    return;
                }

                loadJSON_getOTP();
            }
        });

        btn_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                st_ed_rec_code=ed_rec_code.getText().toString().trim();
                if (st_ed_rec_code.length()==0){
                    Utility.getAlertMsgEnter(ForgotPasswordScreen.this,"Received OTP");
                    return;
                }

                st_ed_set_new_pwd=ed_set_new_pwd.getText().toString().trim();
                if (st_ed_set_new_pwd.length()==0){
                    Utility.getAlertMsgEnter(ForgotPasswordScreen.this,"New Password");
                    return;
                }

                loadJSON_PwdReset();
            }
        });
    }

    private void loadJSON_PwdReset() {
        Call<ForgetVerificationBean> call= RetrofitClient.getInstance().getApi().setNewPassword(st_ed_addmision_no,
                st_ed_phoneno,st_ed_rec_code,st_ed_set_new_pwd);
        call.enqueue(new Callback<ForgetVerificationBean>() {

            @Override
            public void onResponse(Call<ForgetVerificationBean> call, Response<ForgetVerificationBean> response) {

                if(response.isSuccessful()){
                    ForgetVerificationBean forgetVerificationBean=response.body();
                    code=forgetVerificationBean.getCode();
                    status=forgetVerificationBean.getStatus();
                    messages=forgetVerificationBean.getMessages();

                    if(code=="200"){
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());

                        builder.setTitle("Success");
                        if (status != null)
                            builder.setIcon((true) ? R.mipmap.ic_launcher : R.mipmap.ic_launcher);
                        builder.setMessage(messages)
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i=new Intent(getApplicationContext(),LoginScreen.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                        final AlertDialog alert = builder.create();

                        alert.show();


                    }

                }else{
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("error");

                        Utility.showAlertDialog(ForgotPasswordScreen.this,"Error",error,false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ForgetVerificationBean> call, Throwable t) {
                Utility.showAlertDialog(ForgotPasswordScreen.this,"Error",t.getMessage(),false);
            }
        });
    }

    private void loadJSON_getOTP() {
        Call<ForgetVerificationBean> call= RetrofitClient.getInstance().getApi().getOTP(st_ed_addmision_no,st_ed_phoneno);
        call.enqueue(new Callback<ForgetVerificationBean>() {

            @Override
            public void onResponse(Call<ForgetVerificationBean> call, Response<ForgetVerificationBean> response) {

                if(response.isSuccessful()){
                    ForgetVerificationBean forgetVerificationBean=response.body();
                    code=forgetVerificationBean.getCode();
                    status=forgetVerificationBean.getStatus();
                    messages=forgetVerificationBean.getMessages();

                    Utility.showAlertDialog(ForgotPasswordScreen.this,"Message",messages,true);

                    btn_sendotp.setVisibility(View.GONE);
                    ll_otpcode.setVisibility(View.VISIBLE);
                    ll_setnewpwd.setVisibility(View.VISIBLE);
                    btn_reset_pwd.setVisibility(View.VISIBLE);
                }else{
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("error");

                        Utility.showAlertDialog(ForgotPasswordScreen.this,"Error",error,false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<ForgetVerificationBean> call, Throwable t) {
                Utility.showAlertDialog(ForgotPasswordScreen.this,"Error",t.getMessage(),false);
            }
        });

    }
}