package com.jspark.android.kardoc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

public class SignActivity extends AppCompatActivity {
    LoginButton signinFacebook;
    private CallbackManager callbackManager;

    TextView alertId, alertPw;
    EditText editId, editPw;
    Button btnSignin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_sign);

        setWidgets();

        checkId();

        setBtnSignin();

        //--------------Facebook Login-------------------
        signinFacebook = (LoginButton) findViewById(R.id.signinFacebook);
        signinFacebook.setReadPermissions(Arrays.asList("public_profile", "email"));
        signinFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("result", object.toString());
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Intent i  = new Intent(SignActivity.this, LobbyActivity.class);
                        startActivity(i);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr",error.toString());
            }
        });
        //-----------------------------------------------
    }
    //---------Facebook Login------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //-----------------------------

    private void setWidgets() {
        editId = (EditText)findViewById(R.id.editId);
        editPw = (EditText)findViewById(R.id.editPw);
        alertId = (TextView)findViewById(R.id.errorId);
        alertPw = (TextView)findViewById(R.id.errorPw);
        btnSignin = (Button)findViewById(R.id.btnSignin);
    }

    private void checkId() {
        // make Logic to check Id&Pw
    }

    private void setBtnSignin() {
        btnSignin.setOnClickListener((v) -> {
            Intent i  = new Intent(SignActivity.this, LobbyActivity.class);
            startActivity(i);
            finish();
        });
    }
}
