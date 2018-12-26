package com.example.truonghoa;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.truonghoa.app_note.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {
    private String url_register = DB.url +  "register";
    private String url_login = DB.url + "login";

    private EditText txtFullname;
    private EditText txtEmail;
    private EditText txtPhoneNumber;
    private EditText txtPassword;
    private EditText txtConfirm;

    private Button btnSignIn;
    private Button btnSignUp;


    private AlertDialog.Builder m_MsgDlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        txtFullname = findViewById(R.id.txtFullName);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirm = findViewById(R.id.txtConfirm);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        m_MsgDlg = new AlertDialog.Builder(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRegister();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSignInActivity();
            }
        });
    }

    public void doRegister(){
        String fullname = txtFullname.getText().toString();
        String email = txtEmail.getText().toString();
        String phonenumber = txtPhoneNumber.getText().toString();
        String password = txtPassword.getText().toString();
        String confirm = txtConfirm.getText().toString();
//        Log.d(" USERNAME:",szUser);
//        Log.d(" PASSWORD:",szPass);

        try {
            apiCreateAccount(fullname,email,phonenumber,password,confirm);
        } catch (IOException e) {
            Log.d(" RESPONSE FAILED",e.toString());
            e.printStackTrace();
        }
    }
    private void apiCreateAccount(final String fullname, final String email, final String phonenumber, final String password, String confirm) throws IOException
    {
        OkHttpClient client = new OkHttpClient();

        // Create okhttp3 form body builder.
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        // Add form parameter
        formBodyBuilder.add("fullname", fullname);
        formBodyBuilder.add("email", email);
        formBodyBuilder.add("phonenumber",phonenumber);
        formBodyBuilder.add("password",password);
        formBodyBuilder.add("confirm",confirm);

        // Build form body.
        FormBody formBody = formBodyBuilder.build();
        // Create a http request object.
        Request request = new Request.Builder()
                .url(url_register)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.d("onFailure called",e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int respCode = Integer.parseInt(response.body().string());
                Log.d(" 1. RESPONSE CODE: ", Integer.toString(respCode));
                boolean isError = false;
                String error = "";
                if(respCode == -2){
                    isError = true;
                    error += "Họ tên không đúng";
                }
                if(respCode == -3){
                    isError = true;
                    error += "Điện thoại không đúng";
                }
                if(respCode == -4){
                    isError = true;
                    error += "Mật khẩu không đúng";
                }
                if(respCode == -5){
                    isError = true;
                    error += "Xác nhận mật khẩu không đúng";
                }
                if(respCode == -6){
                    isError = true;
                    error += "Email này đã được sử dụng";
                }
                if(respCode == -7){
                    isError = true;
                    error += "Email không đúng";
                }
                if(respCode == -101){
                    isError = true;
                    error += "Lỗi không mong muốn";
                }
                if(respCode == 1){
                    SignIn.owner_User = new User(fullname,password,email,phonenumber);
                    changeSignInActivity();
                }
                Log.d(" Loi: ", String.valueOf(isError));
                if(isError){
                    final String finalError = error;
                    SignUp.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            m_MsgDlg.setTitle("Lỗi");
                            m_MsgDlg.setMessage(finalError);
                            m_MsgDlg.show();
                        }
                    });
                }


            }
        });


    }
    private void changeSignInActivity(){
        Intent intent = new Intent(this,SignIn.class);
        startActivity(intent);
    }

}
