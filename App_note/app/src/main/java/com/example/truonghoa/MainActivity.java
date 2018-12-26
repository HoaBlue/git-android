package com.example.truonghoa;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truonghoa.app_note.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ContactAdapter.OnCallBack {
    private String url_listcontact = DB.url + "listcontact";
    private String url_searchcontact = DB.url + "searchname";
    private RecyclerView rwContacts;
    private ContactAdapter adapter;
    private TextView tvUserName;
    private TextView tvPhoneNumber;
    private List<Contact> listContact;
    private EditText txtSearch;
    private Button btnSearch;
    private Button btnAdd;
    private Button btnEdit;

    private User currentUser;
    private AlertDialog.Builder m_MsgDlg;
    public static Contact selectedContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvUserName = findViewById(R.id.tvUserName);
        tvPhoneNumber = findViewById(R.id.tvPhoneNumber);
        txtSearch = findViewById(R.id.txtSearch);
        rwContacts = findViewById(R.id.rwContacts);
        rwContacts.setHasFixedSize(true);
        rwContacts.setLayoutManager(new LinearLayoutManager(this));
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnSearch = findViewById(R.id.btnSearch);

//        for(int i  = 0;i < 10 ; i++){
//            listContact.add(new Contact("","Contact " + i,"0000"+i,"","",String.valueOf(R.drawable.user)));
//        }

        currentUser = SignIn.owner_User;
        doGetList();
        tvUserName.setText(currentUser.fullname);
        tvPhoneNumber.setText(currentUser.phonenumber);


        m_MsgDlg = new AlertDialog.Builder(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeViewAdd();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeViewEdit();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSearch();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        selectedContact = listContact.get(position);
        changeViewEditContact();
    }
    private void doGetList(){
        try {
            apiGetListContact(currentUser.email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void doSearch(){
        try {
            apiSearchContact(currentUser.email, txtSearch.getText().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void apiSearchContact(String email,String name) throws  IOException{
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        formBodyBuilder.add("owner_email", email);
        formBodyBuilder.add("contact_fullname", name);

        FormBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(url_searchcontact).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure called", e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                final int respCode = response.code();
                Log.d("MY RESPONSE: ",myResponse);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(respCode == 201){
                            try {
                                InitList(myResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        if(respCode == 200){
                            Toast.makeText(MainActivity.this,"Không tìm thấy kết quả nào",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    private void apiGetListContact(String email) throws  IOException{
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        formBodyBuilder.add("owner_email",email);

        FormBody formBody = formBodyBuilder.build();
        Request request = new Request.Builder().url(url_listcontact).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("onFailure called",e.toString());
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String myResponse = response.body().string();
                final int respCode = response.code();
                Log.d(" RESPONSE: ", myResponse);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(respCode == 201){
                            try {
                                InitList(myResponse);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("loi parse json",e.toString());
                            }
                        }else{
                            if(respCode == 200){
                                int code = Integer.parseInt(myResponse);
                                if(code == -1){
                                    m_MsgDlg.setTitle("Lỗi không mong muốn");
                                    m_MsgDlg.setMessage("code: " + myResponse);
                                    m_MsgDlg.show();
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    private void InitList(String input) throws JSONException {

        listContact = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(input);
        for(int i = 0; i<jsonArray.length();i++){
            JSONObject jObject = jsonArray.getJSONObject(i);
            Contact contact = new Contact(jObject.getString("_id"),jObject.getString("owner_email"),
                    jObject.getString("contact_fullname"),
                    jObject.getString("contact_phonenumber"),
                    jObject.getString("contact_address"),
                    jObject.getString("contact_email"),
                    String.valueOf(R.drawable.user));
            listContact.add(contact);
        }
        adapter = new ContactAdapter(this,listContact);
        rwContacts.setAdapter(adapter);
    }
    private void changeViewAdd(){
        Intent intent = new Intent(this, AddContact.class);
        startActivity(intent);
    }
    private void changeViewEdit(){
        Intent intent = new Intent(this, EditUser.class);
        startActivity(intent);
    }
    private void changeViewEditContact(){
        Intent intent = new Intent(this, EditContact.class);
        startActivity(intent);
    }
}
