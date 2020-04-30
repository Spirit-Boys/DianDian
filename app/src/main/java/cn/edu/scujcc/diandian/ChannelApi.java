package cn.edu.scujcc.diandian;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ChannelApi {
    @GET("/channel")
    Call<List<Channel>> getAllChannels();

    @GET("/channel/{channelId}")
    Call<Channel> getChannel(@Path("channelId")String channelId);
}
