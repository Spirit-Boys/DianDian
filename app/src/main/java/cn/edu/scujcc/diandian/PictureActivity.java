package cn.edu.scujcc.diandian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PictureActivity extends AppCompatActivity {
    private ImageView avatar;
    private Button xiazai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);

        avatar = findViewById(R.id.avatar);
        xiazai = findViewById(R.id.xiazai);

        xiazai.setOnClickListener(v -> {
            Glide.with(this)
                    .load("https://images.idgesg.net/images/article/2019/01/android-robot-security-100785201-large.3x2.jpg")
                    .into(avatar);
        });
    }
}
