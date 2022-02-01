package com.gp.health.di.module;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.gp.health.BuildConfig;
import com.gp.health.R;
import com.gp.health.data.AppDataManager;
import com.gp.health.data.DataManager;
import com.gp.health.data.apis.ApiHelper;
import com.gp.health.data.apis.AppApiHelper;
import com.gp.health.data.apis.NetworkService;
import com.gp.health.data.prefs.AppPreferencesHelper;
import com.gp.health.data.prefs.PreferencesHelper;
import com.gp.health.di.DatabaseInfo;
import com.gp.health.di.PreferenceInfo;
import com.gp.health.utils.AppConstants;
import com.gp.health.utils.LanguageHelper;
import com.gp.health.utils.Token;
import com.gp.health.utils.rx.AppSchedulerProvider;
import com.gp.health.utils.rx.SchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static com.gp.health.BaseApp.getContext;

@Module
public class AppModule {

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    public Token provideToken(NetworkService networkService) {
        return new Token(networkService);
    }

    @Provides
    SchedulerProvider provideSchedulerProvider() {
        return new AppSchedulerProvider();
    }


    @SuppressLint("LogNotTimber")
    @Provides
    public OkHttpClient provideClient(PreferencesHelper preferencesHelper) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

//        Log.i("token", "" + preferencesHelper.getAccessToken());

        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();

                    Log.i("token", "" + preferencesHelper.getAccessToken());

                    requestBuilder.addHeader("Authorization", "Bearer " + preferencesHelper.getAccessToken());
                    requestBuilder.addHeader("Accept", "application/json");
                    requestBuilder.addHeader("Content-Type", "application/json");
                    requestBuilder.addHeader("Content-Language", LanguageHelper.getLanguage(getContext()));
                    return chain.proceed(requestBuilder.build());
                }).build();
    }

    /**
     * provide Retrofit instances
     *
     * @param baseURL base url for api calling
     * @param client  OkHttp client
     * @return Retrofit instances
     */

    @Provides
    public Retrofit provideRetrofit(String baseURL, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Provide Api service
     *
     * @return ApiService instances
     */
    @Provides
    public NetworkService provideApiService(PreferencesHelper preferencesHelper) {
        return provideRetrofit(BuildConfig.BASE_URL, provideClient(preferencesHelper)).create(NetworkService.class);
    }

}
