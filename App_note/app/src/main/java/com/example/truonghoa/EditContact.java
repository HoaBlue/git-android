package com.example.truonghoa;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.truonghoa.app_note.R;

import java.io.IOException;
import java.text.Normalizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditContact extends AppCompatActivity {
    private String url_editcontact = DB.url + "editcontact";
    private String url_deletecontact = DB.url + "deletecontact";
    private EditText txtFullname;
    private EditText txtPhoneNumber;
    private EditText txtAddress;
    private EditText txtEmail;
    private ImageView ivAvatar;

    private Button btnBack;
    private Button btnEdit;
    private Button btnDelete;
    private AlertDialog.Builder m_MsgDlg;

    private Contact currentContactID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        currentContactID = MainActivity.selectedContact;

        txtFullname = findViewById(R.id.txtFullName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtAddress = findViewById(R.id.txtAddress);
        txtEmail = findViewById(R.id.txtEmail);
        ivAvatar = findViewById(R.id.ivAvatar);

        btnBack = findViewById(R.id.btnBack);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        m_MsgDlg = new AlertDialog.Builder(this);

        txtFullname.setText(currentContactID.getContact_fullname());
        txtPhoneNumber.setText(currentContactID.getContact_phonenumber());
        txtAddress.setText(currentContactID.getContact_address());
        txtEmail.setText(currentContactID.getContact_email());

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doEditContact();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMainActivity();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doDeleteContact();
            }
        });
    }
    private void doEditContact(){
        try {
            apiEditContact(currentContactID.get_ID(),txtFullname.getText().toString(),
                        txtPhoneNumber.getText().toString(), txtAddress.getText().toString(),
                        txtEmail.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void doDeleteContact(){
        try {
            apiDeleteContact(currentContactID.get_ID());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void apiDeleteContact(String _id) throws IOException{
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        formBodyBuilder.add("_id",_id);

        FormBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(url_deletecontact).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure called", e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResonse = response.body().string();
                Log.d("MY RESPOND: ", myResonse);
                EditContact.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int code = Integer.parseInt(myResonse);
                        if(code == 1){
                            Toast.makeText(EditContact.this,"Đã xóa " + currentContactID.getContact_fullname(),Toast.LENGTH_LONG).show();
                            changeMainActivity();
                        }
                        if(code == -1){
                            m_MsgDlg.setTitle("Thông báo");
                            m_MsgDlg.setMessage("Không thể xóa danh bạ này");
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
    private void apiEditContact(String _id, String fullname, String phonenumber, String address, String email) throws IOException{
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        formBodyBuilder.add("_id",_id);
        formBodyBuilder.add("contact_email",email);
        formBodyBuilder.add("contact_address",address);
        formBodyBuilder.add("contact_fullname",fullname);
        formBodyBuilder.add("contact_phonenumber",phonenumber);

        FormBody formBody = formBodyBuilder.build();

        Request request = new Request.Builder().url(url_editcontact).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure called",e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResonse = response.body().string();
                Log.d("MY RESPOND: ",myResonse);
                EditContact.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int code = Integer.parseInt(myResonse);
                        if(code == 1){
                            m_MsgDlg.setTitle("Thông báo");
                            m_MsgDlg.setMessage("Chỉnh sửa thành công");
                            m_MsgDlg.show();
                        }
                        if(code == -1){
                            m_MsgDlg.setTitle("Thông báo");
                            m_MsgDlg.setMessage("Chỉnh sửa không thành công");
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
    private void changeMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
