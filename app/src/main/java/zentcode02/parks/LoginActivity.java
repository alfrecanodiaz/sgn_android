package zentcode02.parks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zentcode02.parks.network.Config;
import zentcode02.parks.network.CustomObjectRequest;
import zentcode02.parks.network.PreferenceManager;
import zentcode02.parks.network.ServerRoutes;
import zentcode02.parks.utils.Helper;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private PreferenceManager preferenceManager;
    private EditText edtUserEmail, edtUserPassword;
    private String user_email, user_password, api_token, status_login, user_name;
    private Integer user_id;
    private String respuestaLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager(LoginActivity.this);

        checkLoggedUser();

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarLogin);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Inicio de Sesión");

        edtUserEmail = (EditText) findViewById(R.id.edtUserEmail);
        edtUserPassword = (EditText) findViewById(R.id.edtUserPassword);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    private void checkLoggedUser() {
        Integer user_logged_id = preferenceManager.getUserId();
        if (user_logged_id > 0) {
            launchApp();
        }
    }

    private void launchApp() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void userLogin() {
        user_email = edtUserEmail.getText().toString().trim();
        user_password = edtUserPassword.getText().toString().trim();
        if (!emailValidator(user_email)) {
            Toast.makeText(LoginActivity.this, Config.ERR_INVALID_MAIL , Toast.LENGTH_LONG).show();
        }
        else if (!passwordValidator(user_password)) {
            Toast.makeText(LoginActivity.this, Config.ERR_INVALID_PASS , Toast.LENGTH_LONG).show();
        } else  {
            postDataToServer();
        }
    }

    private void postDataToServer() {
        final ProgressDialog pDialog = Helper.showProgressDialog(LoginActivity.this, Config.LOADING_MSG);
        String url = ServerRoutes.getLoginUrl();
        CustomObjectRequest request = new CustomObjectRequest(this);
        request.SendRequest(Request.Method.POST, url, postBody(), new CustomObjectRequest.ServerCallback() {
            @Override
            public void onResponse(JSONObject response) {
                edtUserPassword.setText("");
                Helper.hideProgressDialog(pDialog);
                if (response.has("status")) {
                    try {
                        status_login = response.getString("status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(LoginActivity.this, status_login, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        user_id = response.getInt("user_id");
                        user_name = response.getString("user_name");
                        api_token = response.getString("api_token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    saveUserPreferences();
                }
            }
            @Override
            public void onErrorResponse() {
                Helper.hideProgressDialog(pDialog);
                Toast.makeText(LoginActivity.this, Config.NETWORK_ERROR_MSG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String postBody() {
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("email", user_email);
            jsonObj.put("password", user_password);
            respuestaLogin = String.valueOf(jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respuestaLogin;
    }

    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return !email.matches("") && matcher.matches();
    }

    public boolean passwordValidator(String password) {
        return password.length() > 0 || !password.isEmpty() || !password.matches("");
    }

    private void saveUserPreferences() {
        preferenceManager.clearApiToken();
        preferenceManager.saveApiToken(api_token);
        preferenceManager.clearUserId();
        preferenceManager.saveUserId(user_id);
        preferenceManager.clearUserName();
        preferenceManager.saveUserName(user_name);
        launchApp();
    }

    public static void hideSoftKeyboard (Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    @Override
    public void onClick(View v) {
        hideSoftKeyboard(LoginActivity.this, v);
        userLogin();
    }
}
