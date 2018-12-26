package com.example.truonghoa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.truonghoa.app_note.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddContact extends AppCompatActivity {
    private String url_addcontack = DB.url + "addcontact";
    private EditText txtFullName;
    private EditText txtMobile;
    private EditText txtAddress;
    private EditText txtEmail;
    private ImageView ivAvatar;

    private Button btnAdd;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        txtFullName = findViewById(R.id.txtFullName);
        txtMobile = findViewById(R.id.txtMobile);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        ivAvatar = findViewById(R.id.ivAvatar);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMainActivity();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doAddContact();
            }
        });
    }
    private void doAddContact(){
        try {
            apiAddContact(txtFullName.getText().toString(),txtMobile.getText().toString(),txtAddress.getText().toString(),txtEmail.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void apiAddContact(String fullname, String mobile, String address, String email) throws IOException{
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        formBodyBuilder.add("owner_email",SignIn.owner_User.email);
        formBodyBuilder.add("contact_fullname",fullname);
        formBodyBuilder.add("contact_phonenumber",mobile);
        formBodyBuilder.add("contact_address",address);
        formBodyBuilder.add("contact_email",email);

        FormBody formBody = formBodyBuilder.build();

        Request request = new Request.Builder().url(url_addcontack).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure called",e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String myResponse = response.body().string();
                int respCode = response.code();
                Log.d("Resonse CODE: ", myResponse);
                int code = Integer.parseInt(myResponse);
                if(code == 1){
                    changeMainActivity();
                }
            }
        });
    }
    private void changeMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
