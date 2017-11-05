package com.example.utsav.ikailogin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    final Context context = this;
    EditText nam,phn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nam = (EditText) findViewById(R.id.name);
        phn = (EditText) findViewById(R.id.phn_no);

        final Button login_button = (Button) findViewById(R.id.logIn_button);
        login_button.setOnClickListener(null);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog loading = ProgressDialog.show(context,"Logging in...","Please wait...",false,false);
                final String name = nam.getText().toString();
                final String phn_no = phn.getText().toString();

                if(validateName(name) && validatePhone(phn_no)) {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                loading.dismiss();
                                JSONObject jsonResponse = new JSONObject(response);
                                int error = jsonResponse.getInt("Error");
                                String message = jsonResponse.getString("Message");

                                //As per the assignment message will have "Congratulation" as response if no error occurs.

                                if (error==0 && message.equals("Congratulation")) {
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, Unit.class);
                                    LoginActivity.this.startActivity(intent);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setTitle("Login Failed!")
                                            .setMessage(message)
                                            .setNegativeButton("Retry", null)
                                            .create()
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    Response.ErrorListener errorListener = new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            loading.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setTitle("Login Failed!")
                                    .setMessage("Server Error")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    };

                    LoginRequest loginRequest = new LoginRequest(name, phn_no, responseListener, errorListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
                else
                    loading.dismiss();
            }
        });
    }

    protected boolean validateName(String name){
        if(name.matches("[a-zA-Z]+"))
            return true;
        else {
            nam.setError("Not a Valid Name");
            return false;
        }
    }
    protected boolean validatePhone(String phn_num){
        boolean check;
        if(phn_num.matches("[0-9]+") && phn_num.length() == 10)
            check = true;
        else {
            check = false;
            phn.setError("Not a Valid Number");
        }
        return check;
    }
}