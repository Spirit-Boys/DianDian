package cn.edu.scujcc.diandian;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.io.Serializable;

import retrofit2.Retrofit;


public class PlayerActivity extends AppCompatActivity {

    private PlayerView tvPlayerView;
    private SimpleExoPlayer player;
    private  MediaSource videoSource;
    private Channel currentChannel;
    private TextView tvName,tvQuality;
    private TextView c1Content,c1Author,c1Dt;
    private ChannelLab lab = ChannelLab.getInstance();
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //TODO 接收并显示数据
            if (msg.what == 2){
                if (msg.obj != null){
                    currentChannel = (Channel) msg.obj;
                    //TODO 更新界面
                    updateUI();
                }else{
                    Log.w("DianDian","收到的当前频道数据为null");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Serializable s = getIntent().getSerializableExtra("channel");
        Log.d("DianDian","取得的当前频道对象时："+s);
        if (s != null && s instanceof Channel){
            currentChannel = (Channel) s;
        }
//        updateUI();
        quality();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    protected void onStart() {
        super.onStart();
        init();
        if (tvPlayerView != null) {
            tvPlayerView.onResume();
        }
    }

    protected void onStop() {
        super.onStop();
        if (tvPlayerView != null) {
            tvPlayerView.onPause();
        }
        clean();
    }


    @Override
    protected void onResume() {
        super.onResume();
        //TODO 访问网络获取当前频道最新数据（含热门评论数据）
        lab.getChannelData(currentChannel.getId(),handler);

        if (player == null) {
            init();
            if (tvPlayerView != null) {
                tvPlayerView.onResume();
            }
        }
    }

    //重构。初始化播放器
    private void init(){
        player = ExoPlayerFactory.newSimpleInstance(this);
        player.setPlayWhenReady(true);
        //从界面查找视图
        tvPlayerView = findViewById(R.id.tv_player);
        //关联视图与播放器
        tvPlayerView.setPlayer(player);
        //准备播放的媒体
        Uri videoUrl = Uri.parse("http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        if (null != currentChannel) {
            //使用当前频道的网址
            videoUrl = Uri.parse(currentChannel.getUrl());
        }
        DataSource.Factory factory =
                new DefaultDataSourceFactory(this, "DianDian");
        MediaSource videoSource = new HlsMediaSource.Factory(factory).createMediaSource(videoUrl);
        player.prepare(videoSource);
    }

    /**
     * 初始化界面
     */
    private void updateUI(){
        tvName = findViewById(R.id.tv_name);
        tvQuality = findViewById(R.id.tv_quality);
        tvName.setText(currentChannel.getTitle());
        tvQuality.setText(currentChannel.getQuality());

        c1Content = findViewById(R.id.c1_content);
        c1Author =findViewById(R.id.c1_author);
        c1Dt = findViewById(R.id.c1_dt);
        if(null != currentChannel.getComments()){
            Comment c1 = currentChannel.getComments().get(0);
            c1Content.setText(c1.getContent());
            c1Author.setText(c1.getAuthor());
            c1Dt.setText(c1.getDt().toString());
        }
    }

    private void quality(){
        TextView tvQuality = findViewById(R.id.tv_quality);
        tvQuality.setText(this.currentChannel.getQuality());
    }
    //重构。释放与清理资源
    private void clean() {
        if (player != null) {
            player.release();
            player = null;
        }

        Retrofit b = RetrofitClient.getInstance();
    }
}
