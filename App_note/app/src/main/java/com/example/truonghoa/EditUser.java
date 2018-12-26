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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditUser extends AppCompatActivity {
    private String url_edituser = DB.url + "edituser";
    private String url_getInfo = DB.url + "getinfo";
    private EditText txtFullName;
    private EditText txtMobile;
    private EditText txtNewPassword;
    private EditText txtCurrentPassword;
    private Button btnBack;
    private Button btnEdit;

    private AlertDialog.Builder m_MsgDlg;

    private User currentUser ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        txtFullName = findViewById(R.id.txtFullName);
        txtMobile = findViewById(R.id.txtMobile);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtCurrentPassword = findViewById(R.id.txtCurrentPassword);

        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        m_MsgDlg = new AlertDialog.Builder(this);
        currentUser = SignIn.owner_User;
        txtFullName.setText(currentUser.fullname);
        txtMobile.setText(currentUser.phonenumber);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEditUser();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMainActivity();
            }
        });
    }

    private void doEditUser(){
        try {
            apiEditUser(txtFullName.getText().toString(),
                        txtMobile.getText().toString(),
                        txtNewPassword.getText().toString(),
                        txtCurrentPassword.getText().toString(),
                        currentUser.email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void apiEditUser(String fullname, String phonenumber, String newpassword, String currentpassword, String email) throws IOException{
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        formBodyBuilder.add("fullname",fullname);
        formBodyBuilder.add("phonenumber",phonenumber);
        formBodyBuilder.add("newpassword",newpassword);
        formBodyBuilder.add("oldpassword",currentpassword);
        formBodyBuilder.add("email",email);

        FormBody formBody = formBodyBuilder.build();

        Request request = new Request.Builder().url(url_edituser).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure called", e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResonse = response.body().string();
                int respcode = response.code();
                Log.d("Resonsed: ",myResonse);
                EditUser.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int code = Integer.parseInt(myResonse);
                        if(code == 1){
                            m_MsgDlg.setTitle("Thông báo");
                            m_MsgDlg.setMessage("Chỉnh sửa thành công");
                            m_MsgDlg.show();
                            doGetInfo(currentUser.email,txtCurrentPassword.getText().toString());
                        }
                        if(code == 2){
                            m_MsgDlg.setTitle("Thông báo");
                            m_MsgDlg.setMessage("Chỉnh sửa thành công");
                            m_MsgDlg.show();
                            doGetInfo(currentUser.email,txtNewPassword.getText().toString());
                        }
                        if(code == -2){
                            m_MsgDlg.setTitle("Thông báo");
                            m_MsgDlg.setMessage("Mật khẩu hiện tại không đúng");
                            m_MsgDlg.show();
                        }
                        if(code == -101){
                            m_MsgDlg.setTitle("Thông báo");
                            m_MsgDlg.setMessage("Lỗi không mong muốn");
                            m_MsgDlg.show();
                        }
                    }
                });
            }
        });
    }
    private  void doGetInfo(String email, String password){
        try {
            apiGetInfo(email,password);
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
                        SignIn.owner_User = getUserFromString(respond);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
    }
    private User getUserFromString(String jsonString) throws JSONException {
        JSONObject jObject = new JSONObject(jsonString);
        User user = new User(jObject.getString("fullname"),jObject.getString("password"),jObject.getString("email"),jObject.getString("phonenumber"));
        return user;
    }

    private void changeMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
