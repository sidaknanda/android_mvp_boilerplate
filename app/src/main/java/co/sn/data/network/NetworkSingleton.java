package co.sn.data.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import co.sn.app.BuildConfig;
import co.sn.data.network.retrofit.RxErrorHandlingCallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkSingleton {

    private static NetworkSingleton instance = null;
    private Retrofit retrofit;

    private NetworkSingleton() {
    }

    public static NetworkSingleton getInstance() {

        if (instance == null) {
            instance = new NetworkSingleton();
        }
        return instance;
    }

    Retrofit getNetworkClient() {
        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            // add logging for debugging
            if (BuildConfig.IS_DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.networkInterceptors().add(interceptor);
            }

            builder.connectTimeout(10, TimeUnit.SECONDS);
            builder.readTimeout(10, TimeUnit.SECONDS);
            builder.writeTimeout(10, TimeUnit.SECONDS);
            OkHttpClient okHttpClient = builder.build();

            Gson gson = new GsonBuilder().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://test.test.com")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}