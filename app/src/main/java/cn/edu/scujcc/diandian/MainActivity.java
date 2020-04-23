package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity
    implements ChannelRvAdapter.ChannelClickListener {
    private RecyclerView channelRv;
    private ChannelRvAdapter rvAdapter;
    private ChannelLab lab = ChannelLab.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.channelRv = findViewById(R.id.channel_rv);
        rvAdapter = new ChannelRvAdapter(this);
        this.channelRv.setAdapter(rvAdapter);
        this.channelRv.setLayoutManager(new LinearLayoutManager(this));
        initData();
    }

    @Override
    public void onChannelClick(int position) {
        Log.d("DianDian","用户点击的频道编号是"+position);
        Channel c = lab.getChannel(position);
        //跳转到新界面，使用意图Intent
        Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
        //传递用户选中的频道到下一个界面
        intent.putExtra("Channel",c);
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
