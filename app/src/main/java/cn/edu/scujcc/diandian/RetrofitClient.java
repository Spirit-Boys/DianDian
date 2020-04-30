package cn.edu.scujcc.diandian;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitClient {
    private static Retrofit INSTANCE = null;

    public static synchronized Retrofit getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new Retrofit.Builder()
                    .baseUrl("http://47.112.240.160:8080")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build();
        }
        return INSTANCE;
    }
}
