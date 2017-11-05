package com.example.utsav.ikailogin;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

class LoginRequest extends StringRequest
{
    private static final String LOGIN_REQUEST_URL = "http://www.ikai.co.in/api/login.php";
    private Map<String, String> params;

    LoginRequest(String name, String phn_no, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, errorListener);
        params = new HashMap<>();
        params.put("name", name);
        params.put("phn_no", phn_no);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}