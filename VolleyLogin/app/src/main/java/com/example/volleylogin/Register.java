package com.example.volleylogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText name,address,pin;
    Button button;
    String url="http://192.168.43.151/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        pin=findViewById(R.id.pin);
        button=findViewById(R.id.reg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString().length()!=0 &&
                        pin.getText().toString().length()==6)
                {
                    JSONObject params= new JSONObject();
                    try
                    {
                        params.put("mobileNumber",getIntent().getExtras().getString("mobNum"));
                        params.put("name",name.getText().toString());
                        params.put("address",address.getText().toString());
                        params.put("pin",pin.getText().toString());
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,params,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try
                                    {
                                        String status=response.getString("status");
                                        if(status.equals("success"))
                                        {
                                            //Goto Main Page
                                            Toast.makeText(Register.this,"LOGIN SUCCESSFUL !",Toast.LENGTH_LONG).show();
                                        }
                                        else if(status.equals("fail"))
                                        {
                                            Toast.makeText(Register.this,"Error!",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    catch (JSONException e)
                                    {
                                        e.printStackTrace();
                                        Toast.makeText(Register.this,"Error !",Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(Register.this,"Error !",Toast.LENGTH_LONG).show();
                                }
                            });
                    RequestQueue requestQueue= Volley.newRequestQueue(Register.this);
                    requestQueue.add(jsonObjectRequest);
                }
                else
                    Toast.makeText(Register.this,"Enter All Values !",Toast.LENGTH_LONG).show();
            }
        });
    }
//    void setLoading(Button button){
//            but
//    }
}
