package com.krishibazaar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.krishibazaar.Models.NewUser;
import com.krishibazaar.Utils.SharedPreferenceManager;
import com.krishibazaar.Utils.VolleyRequestMaker;

import static com.krishibazaar.Utils.Constants.TOKEN;

public class RegisterActivity extends AppCompatActivity {
    EditText name, address, pin;
    Button button;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        token = getIntent().getStringExtra(TOKEN);
        if (token == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            name = findViewById(R.id.name);
            address = findViewById(R.id.address);
            pin = findViewById(R.id.pin);
            button = findViewById(R.id.reg);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (name.getText().toString().length() != 0 && pin.getText().toString().length() == 6) {
                        NewUser user = new NewUser(name.getText().toString(),
                                token,
                                address.getText().toString(),
                                Integer.parseInt(pin.getText().toString()));
                        VolleyRequestMaker.register(getApplicationContext(), user, new VolleyRequestMaker.TaskFinishListener<Integer>() {
                            @Override
                            public void onSuccess(Integer response) {
                                SharedPreferenceManager.setToken(RegisterActivity.this, token);
                                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else
                        Toast.makeText(getApplicationContext(), "Enter All Values !", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}

