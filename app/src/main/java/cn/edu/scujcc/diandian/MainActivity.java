package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ChannelRvAdapter.ChannelClickListener {
    private RecyclerView channelRv;
    private ChannelRvAdapter rvAdapter;
    private ChannelLab lab = ChannelLab.getInstance();
    private final static String TAG = "DianDian";
    //线程通讯第1步，在主线程创建Handler
    private Handler handler = new Handler() {
        //按快捷键Ctrl o
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case ChannelLab.MSG_NET_FAILURE:
                    rvAdapter.notifyDataSetChanged();
                    break;
                case ChannelLab.MSG_FAILURE:
                    failed();
                    break;
            }
        }
    };

    private void failed() {
        Toast.makeText(MainActivity.this, "Token无效，禁止访问", Toast.LENGTH_LONG).show();
        Log.w(TAG, "服务器禁止访问，因为token无效。");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.channelRv = findViewById(R.id.channel_rv);
        rvAdapter = new ChannelRvAdapter(MainActivity.this, p -> {
            //跳转到新界面，使用意图Intent
            Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
            //通过位置p得到当前频道channel，传递用户选中的频道到下一个界面
            Channel c = lab.getChannel(p);
            intent.putExtra("channel", c);
            startActivity(intent);
        });
        this.channelRv.setAdapter(rvAdapter);
        this.channelRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onChannelClick(int position) {
        Log.d("DianDian","用户点击的频道编号是"+position);
        Channel c = lab.getChannel(position);
        //跳转到新界面，使用意图Intent
        Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
        //传递用户选中的频道到下一个界面
        intent.putExtra("channel",c);
        startActivity(intent);
    }
    private void initData() {
        //得到网络上的数据后，去更新界面
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                //若收到来自其他线程的数据，则运行以下代码
                List<Channel> channels = (List<Channel>) msg.obj;
                lab.setData(channels);
                rvAdapter.notifyDataSetChanged();
            }
        };
        lab.getData(handler);
    }

}
