package com.unlogicon.typegram.di.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.unlogicon.typegram.Constants;
import com.unlogicon.typegram.interfaces.api.RestApi;
import com.unlogicon.typegram.utils.SharedPreferencesUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Nikita Korovkin 11.10.2018.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(SharedPreferencesUtils sharedPreferencesUtils){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .addHeader("content-type", "application/json")
                            .addHeader("Authorization", "token "+ sharedPreferencesUtils.getToken())
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);})
                .addInterceptor(interceptor)
                .build();
    }

    @Provides
    @Singleton
    public Gson provideGson(){
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @Provides
    @Singleton
    public Retrofit provideRestAdapter(OkHttpClient client, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
    }

    @Provides
    @Singleton
    public RestApi provideRestApi(Retrofit adapter) {
        return adapter.create(RestApi.class);
    }
}
