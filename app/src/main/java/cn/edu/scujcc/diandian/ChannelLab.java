package cn.edu.scujcc.diandian;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

/**
 * 单例数据源，提供频道数据
 */
/**
 * 数据源这里存放了频道所有数据
 * 使用了单例模式保证这个类仅有一个对象
 */
public class ChannelLab {
    private static ChannelLab INSTANCE = null;

    private List<Channel> data = new ArrayList<>();

    private ChannelLab(){
    }

    public static ChannelLab getInstance(){
        if (INSTANCE == null){
            INSTANCE = new ChannelLab();
        }
        return INSTANCE;
    }

    public void setData(List<Channel> newData){
        this.data = newData;
    }

    /**
     * 生成测试数据，用于引入网络的项目
     */
//    private void test(){
//        data = new ArrayList<>();
//        Channel c = new Channel();
//        c.setTitle("CCTV-1 综合");
//        c.setQuality("高清");
//        c.setCover(R.drawable.cctv1);
//        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv1.m3u8");
//        data.add(c);
//        c = new Channel();
//        c.setTitle("CCTV-2 财经");
//        c.setQuality("高清");
//        c.setCover(R.drawable.cctv2);
//        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv2.m3u8");
//        data.add(c);
//        c = new Channel();
//        c.setTitle("CCTV-3 综艺");
//        c.setQuality("超清");
//        c.setCover(R.drawable.cctv3);
//        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv3.m3u8");
//        data.add(c);
//        c = new Channel();
//        c.setTitle("CCTV-4 中文国际");
//        c.setQuality("标清");
//        c.setCover(R.drawable.cctv4);
//        c.setUrl("http://ivi.bupt.edu.cn/hls/cctv4.m3u8");
//        data.add(c);
//    }
    /**
     * 获取当前数据源总共有多少个频道
     * @return
     */
    public int getSize(){
        return data.size();
    }

    /**
     * 获取一个指定频道
     * @param position 频道的序号
     * @return 频道对象的Channel
     */
    public Channel getChannel(int position){
        return data.get(position);
    }

    /**
     * 访问网络得到真实数据，代替以前的test()方法
     */
    public void getData(Handler handler){
        Retrofit retrofit = RetrofitClient.getInstance();
        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<List<Channel>> call = api.getAllChannels();
        //enqueue把代码放在子线程去运行
        call.enqueue(new Callback<List<Channel>>() {
            @Override
            public void onResponse(Call<List<Channel>> call,
                                   Response<List<Channel>> response) {
                //如果网络访问成功
                if(null != response && null != response.body()){
                    Log.d("DianDian", "从阿里云得到的数据是：");
                    Log.d("DianDian", response.body().toString());
                    //不能在此操作Recyclerview刷新界面，只能使用线程通讯将数据传送到主线程
                    Message msg = new Message();
                    msg.obj = response.body();
                    handler.sendMessage(msg);
                } else {
                    Log.w("DianDian","respomse没有数据！");
                }
            }

            @Override
            public void onFailure(Call<List<Channel>> call, Throwable t) {
                //如果网络访问失败
                Log.d("DianDian","访问网络出错了",t);
            }
        });
    }

    /**
     *访问网络得到一个频道的信息
     */
    public void getChannelData(String id,Handler handler){
        //通过Retrofit访问服务器得到当前频道信息
        //调用单列
        Retrofit retrofit = RetrofitClient.getInstance();
        ChannelApi api = retrofit.create(ChannelApi.class);
        Call<Channel> call = api.getChannel(id);
        call.enqueue(new Callback<Channel>() {
            @Override
            public void onResponse(Call<Channel> call, Response<Channel> response) {
                //假设网络访问成功，把数据传递给主线程
                Log.d("DianDian", "从阿里云得到的数据是：");
                Log.d("DianDian", response.body().toString());
                Channel channel = response.body();
                //发出通知
                Message msg = new Message();
                msg.what = 2; //自己规定2代表从阿里云获取单个频道
                msg.obj = channel;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<Channel> call, Throwable t) {
                //假设网络访问失败，一个打印详细信息以解决
                Log.d("DianDian","访问网络出错了",t);
            }
        });
    }
}
