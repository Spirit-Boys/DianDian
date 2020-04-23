package cn.edu.scujcc.diandian;


import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


public class PlayerActivity extends AppCompatActivity {

    private PlayerView tvPlayerView;
    private SimpleExoPlayer player;
    private  MediaSource videoSource;
    private Channel channel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        channel = (Channel) getIntent().getSerializableExtra("Channel");
        quality();
        updateUI();
        initPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clean();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tvPlayerView != null){
            tvPlayerView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(player != null){
            initPlayer();
            if(tvPlayerView != null){
                tvPlayerView.onResume();
            }
        }
    }

    //重构。初始化播放器
    private void initPlayer(){
        if (player == null) {
            tvPlayerView = findViewById(R.id.tv_player);
            //创建播放器
            player = ExoPlayerFactory.newSimpleInstance(this);
            player.setPlayWhenReady(true);
            //绑定界面与播放器
            tvPlayerView.setPlayer(player);
            //准备播放器
            Uri url = Uri.parse(channel.getUrl());
            DataSource.Factory factory = new DefaultDataSourceFactory(this, "DianDian");
            videoSource = new HlsMediaSource.Factory(factory).createMediaSource(url);
        }
        //把播放源传送给播放器（并开始播放）
        player.prepare(videoSource);
    }

    /**
     * 初始化界面
     */
    private void updateUI(){
        TextView tvName = findViewById(R.id.tv_name);
        tvName.setText(this.channel.getTitle());
    }

    private void quality(){
        TextView tvQuality = findViewById(R.id.tv_quality);
        tvQuality.setText(this.channel.getQuality());
    }
    //重构。释放与清理资源
    private void clean(){
        if (player != null) {
            player.release();
            player = null;
            videoSource = null;
        }
    }
}
