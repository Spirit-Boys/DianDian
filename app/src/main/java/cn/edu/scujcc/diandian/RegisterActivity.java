package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class RegisterActivity extends AppCompatActivity {
    private TextInputLayout birthdayInput;
    private final static String TAG = "DianDian";
    private Button registerButton;
    private Date birthday = new Date();
    private UserLab lab = UserLab.getInstance();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg != null) {
                switch (msg.what) {
                    case UserLab.USER_REGISTER_SUCCESS:
                        Toast.makeText(RegisterActivity.this, "注册成功！欢迎你的加入！", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        break;
                    case UserLab.USER_REGISTER_FAIL:
                        Toast.makeText(RegisterActivity.this, "注册失败！请稍候再试。", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        birthdayInput = findViewById(R.id.r_birthday);
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener( (v) ->{
            register();
        });

        birthdayInput.setEndIconOnClickListener(v ->{
            Log.d(TAG,"生日图标被点击了");
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            //告诉builder我们想要的效果
            builder.setTitleText(R.string.birthday_title);
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(getSupportFragmentManager(),picker.toString());
            picker.addOnPositiveButtonClickListener(s -> {
               Log.d(TAG,"日历结果是："+s);
               Log.d(TAG,"标题是："+picker.getHeaderText());
               birthday.setTime(s);
               birthdayInput.getEditText().setText(picker.getHeaderText());
            });
        });
    }

    private void register(){
        User u = new User();
        boolean error = false;
        String errorMessage;
        //获取用户名
        TextInputLayout usernameInput = findViewById(R.id.r_username);
        Editable username = usernameInput.getEditText().getText();
        u.setUsername(username!=null ? username.toString():"");

        //检查密码是否一致
        TextInputLayout passwordInput1 = findViewById(R.id.r_password);
        TextInputLayout passwordInput2 = findViewById(R.id.r_password2);
        Editable password1 = passwordInput1.getEditText().getText();
        Editable password2 = passwordInput2.getEditText().getText();
        if(password1 != null && password2 != null){
            if (! password2.equals(password1)){//两次密码不相同
                error = true;
                errorMessage = "两次密码不相同";
            }else{
                u.setPassword(password1.toString());
            }
        }

        //获取手机号
        TextInputLayout phoneInput = findViewById(R.id.r_phone);
        Editable phone = phoneInput.getEditText().getText();
        u.setPhone(phone!=null ? phone.toString():"");

        //获取性别
        RadioGroup genderGroup = findViewById(R.id.r_gender);
        int gender = genderGroup.getCheckedRadioButtonId();
        switch (gender){
            case R.id.male:
                u.setGender("男");
                break;
            case R.id.female:
                u.setGender("女");
                break;
            default:
                u.setGender("保密");
        }

        //获得生日
        u.setBirthday(birthday);

        //把u发送到服务器
        lab.register(u, handler);
    }
}
