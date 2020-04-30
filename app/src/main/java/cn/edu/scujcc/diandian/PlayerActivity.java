package cn.edu.scujcc.diandian;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import retrofit2.Retrofit;


public class PlayerActivity extends AppCompatActivity {

    private PlayerView tvPlayerView;
    private SimpleExoPlayer player;
    private  MediaSource videoSource;
    private Channel currentChannel;
    private TextView tvName,tvQuality;
    private Button sendButton;
    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<Comment> hotComments;
    private ChannelLab lab = ChannelLab.getInstance();
    private final Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //TODO 接收并显示数据
           switch (msg.what){
               case ChannelLab.MSG_HOT_COMMENTS:
                   //显示热门评论
                   hotComments = (List<Comment>) msg.obj;
                   updateUI();
                   break;
               case ChannelLab.MSG_ADD_COMMENTS: //评论成功了，提示一下用户
                   Toast.makeText(PlayerActivity.this,"感谢您的留言！",
                           Toast.LENGTH_LONG)
                           .show();
                   break;
               case ChannelLab.MSG_NET_FAILURE: //评论失败了，提示一下用户
                   Toast.makeText(PlayerActivity.this,"评论失败！",
                           Toast.LENGTH_LONG)
                           .show();
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
            updateUI();
            sendButton = findViewById(R.id.send);
            sendButton.setOnClickListener(v->{
                EditText t = findViewById(R.id.message);
                Comment c = new Comment();
                c.setAuthor("MyApp");
                //随机点赞1-100
                Random random = new Random();
                c.setStar(random.nextInt(100));
                c.setContent(t.getText().toString());
                lab.addComent(currentChannel.getId(), c , handler);
            });;
        }
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
        lab.getHotComments(currentChannel.getId(),handler);

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

        if (hotComments != null && hotComments.size() > 0) {
            Comment c1 = hotComments.get(0);
            TextView username1, date1, content1, score1;
            username1 = findViewById(R.id.username1);
            date1 = findViewById(R.id.date1);
            content1 = findViewById(R.id.content1);
            score1 = findViewById(R.id.score1);
            username1.setText(c1.getAuthor());
            date1.setText(dateFormat.format(c1.getDt()));
            content1.setText(c1.getContent());
            score1.setText(c1.getStar() + "");
        }
        if (hotComments != null && hotComments.size() > 1) {
            Comment c2 = hotComments.get(1);
            TextView username2, date2, content2, score2;
            username2 = findViewById(R.id.username2);
            date2 = findViewById(R.id.date2);
            content2 = findViewById(R.id.content2);
            score2 = findViewById(R.id.score2);
            username2.setText(c2.getAuthor());
            date2.setText(dateFormat.format(c2.getDt()));
            content2.setText(c2.getContent());
            score2.setText(c2.getStar() + "");
        }
        if (hotComments != null && hotComments.size() > 1) {
            Comment c3 = hotComments.get(1);
            TextView username3, date3, content3, score3;
            username3 = findViewById(R.id.username3);
            date3 = findViewById(R.id.date3);
            content3 = findViewById(R.id.content3);
            score3 = findViewById(R.id.score3);
            username3.setText(c3.getAuthor());
            date3.setText(dateFormat.format(c3.getDt()));
            content3.setText(c3.getContent());
            score3.setText(c3.getStar() + "");
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
