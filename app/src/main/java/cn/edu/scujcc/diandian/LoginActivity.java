package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private final static String TAG = "DianDian";


    private TextInputLayout username, password;
    private String user;
    private Button loginButton, registerButton;
    private UserLab lab = UserLab.getInstance();
    private MyPreference prefs = MyPreference.getInstance();
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (null != msg) {
                switch (msg.what) {
                    case UserLab.USER_LOGIN_SUCCESS:
                        loginSuccess(msg.obj);
                        break;
                    case UserLab.USER_LOGIN_PASSWORD_ERROR:
                        loginPasswordError();
                        break;
                    case UserLab.USER_LOGIN_NET_ERROR:
                        loginFailed();
                        break;
                }
            }
        }
    };

    private void loginSuccess(Object token) {
        Toast.makeText(LoginActivity.this,
                "登录成功！",
                Toast.LENGTH_LONG).show();
        //FIXME 替换tom，从服务器获取登录成功的真实用户名。
        prefs.saveUser(user, (String) token);
        Log.d(TAG, "用户" + user + "登录成功，token＝" + token);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void loginPasswordError(){
        Toast.makeText(LoginActivity.this,
                "密码错误，请稍后再试！",
                Toast.LENGTH_LONG).show();
    }

    private void loginFailed() {
        Toast.makeText(LoginActivity.this,
                "网络连接失败，请稍后再试！",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs.setup(getApplicationContext());
        username = findViewById(R.id.r_username);
        password = findViewById(R.id.r_password);
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_login);

        loginButton.setOnClickListener(v -> {
            user = username.getEditText().getText().toString();
            String p = password.getEditText().getText().toString();
            lab.login(user, p, handler);
        });

        registerButton.setOnClickListener(v ->{
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        prefs.setup(getApplicationContext());
    }
}
