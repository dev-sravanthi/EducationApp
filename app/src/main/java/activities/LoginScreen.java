package activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bluebase.educationapp.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import bean.LoginData;
import data.repo.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.Utility;

public class LoginScreen extends AppCompatActivity {
    EditText ed_username,ed_password;
    TextView text_forget_pwd;
    Button btn_login;
    String st_ed_username,st_ed_password;
    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;
    private String code,username,phone,student_id,photo,token_type,message,expires_at,token;
    private Boolean error;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginscreen);

        prefs = getSharedPreferences("sms", Context.MODE_PRIVATE);
        editor = prefs.edit();

        ed_username=findViewById(R.id.ed_username);
        ed_password=findViewById(R.id.ed_password);

        btn_login=findViewById(R.id.btn_login);

        text_forget_pwd=findViewById(R.id.text_forget_pwd);
        text_forget_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPasswordScreen.class);
                startActivity(i);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                st_ed_username=ed_username.getText().toString().trim();
                if (st_ed_username.length()==0){
                    Utility.getAlertMsgEnter(LoginScreen.this,"UserName");
                    return;
                }

                st_ed_password=ed_password.getText().toString().trim();
                if (st_ed_password.length()==0){
                    Utility.getAlertMsgEnter(LoginScreen.this,"Password");
                    return;
                }

                checkLogin();

            }
        });
    }

    private void checkLogin() {
        showBar();

        Call<LoginData> call=RetrofitClient.getInstance().getApi().getLoginData(st_ed_username,
                st_ed_password);
        call.enqueue(new Callback<LoginData>() {

            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {

                progressDialog.dismiss();

                if(response.isSuccessful()){
                    LoginData loginData=response.body();
                    token=loginData.getToken();
                    code=loginData.getCode();
                    username=loginData.getUsername();
                    phone=loginData.getPhone();
                    photo=loginData.getPhoto();
                    token_type=loginData.getToken_type();
                    error=loginData.getError();
                    message=loginData.getMessage();
                    expires_at=loginData.getExpires_at();
                    student_id=loginData.getStudent_id();

                    editor.putString("token",token);
                    editor.putString("code",code);
                    editor.putString("username",username);
                    editor.putString("phone",phone);
                    editor.putString("photo",photo);
                    editor.putString("token_type",token_type);
                    editor.putString("message",message);
                    editor.putString("expires_at",expires_at);
                    editor.putString("login_student_id",student_id);
                    editor.apply();

                    Intent intent=new Intent(getApplicationContext(),Dashboard.class);
                    startActivity(intent);
                    finish();

                }else{
                    progressDialog.dismiss();
                    JSONObject jObjError = null;
                    try {
                        jObjError = new JSONObject(response.errorBody().string());
                        String error=jObjError.getString("message");

                        Utility.showAlertDialog(LoginScreen.this,"Error",error,false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                progressDialog.dismiss();
                Utility.showAlertDialog(LoginScreen.this,"Error",t.getMessage(),false);
            }
        });

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

    private void displayToast() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginScreen.this);
        builder.setTitle("Confirmation");
        builder.setIcon(R.drawable.stop_sign);
        builder.setMessage("Are you sure to quit from this APP?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {

        displayToast();
    }


}
