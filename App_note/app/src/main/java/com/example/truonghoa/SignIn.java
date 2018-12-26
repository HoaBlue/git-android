package com.example.truonghoa;


import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.truonghoa.app_note.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignIn extends AppCompatActivity {
    private String url = "http://192.168.1.4:3000/";
    private String url_register = DB.url + "register";
    private String url_login = DB.url + "login";
    private String url_getInfo = DB.url + "getinfo";

    private EditText txtEmail;
    private EditText txtPassword;

    private Button btnSignIn;
    private Button btnSignUp;

    public static User owner_User;

    private AlertDialog.Builder m_MsgDlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);

        m_MsgDlg = new AlertDialog.Builder(this);

        if(owner_User != null){
            txtEmail.setText(owner_User.getEmail());
            txtPassword.setText(owner_User.getPassword());
            doLogin();
        }

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeViewSignUp();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });

    }

    private void changeViewSignUp(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
    private void doLogin(){
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        try {
            apiLoginAccount(email,password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private  void doGetInfo(){
        try {
            apiGetInfo(txtEmail.getText().toString(),txtPassword.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void apiGetInfo(String email, String password) throws IOException{
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        formBodyBuilder.add("email",email);
        formBodyBuilder.add("password",password);

        FormBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(url_getInfo).post(formBody).build();
        client.newCall(request).enqueue((new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure called",e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final int respCode = response.code();
                final String respond = response.body().string();
                Log.d("Response code ", Integer.toString(respCode));
                if(respCode == 201){
                    try {
                        owner_User = getUserFromString(respond);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    changeMainActivity();
                }
            }
        }));
    }
    private void apiLoginAccount(String email, String password) throws IOException {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        formBodyBuilder.add("email", email);
        formBodyBuilder.add("password",password);

        FormBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(url_login).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure called",e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int respCode = Integer.parseInt(response.body().string());
                Log.d(" 1. RESPONSE CODE: ", Integer.toString(respCode));
                boolean isError = false;
                String error = "";
                if(respCode == 404){
                    isError = true;
                    error = "Sai email hoặc mật khẩu";
                }
                if(respCode == -1){
                    isError = true;
                    error = "Lỗi không mong muốn";
                }
                if(respCode == 1){
                    //changeMainActivity();
                    doGetInfo();
                }
                if(isError){
                    final String finalError = error;
                    SignIn.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(" RESPONSE:", String.valueOf(respCode));
                            m_MsgDlg.setTitle("Lỗi");
                            m_MsgDlg.setMessage(finalError);
                            m_MsgDlg.show();
                        }
                    });
                }

            }
        });
    }

    private void changeMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private User getUserFromString(String jsonString) throws JSONException {
        JSONObject jObject = new JSONObject(jsonString);
        User user = new User(jObject.getString("fullname"),jObject.getString("password"),jObject.getString("email"),jObject.getString("phonenumber"));
        return user;
    }

}
