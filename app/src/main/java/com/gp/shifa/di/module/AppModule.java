package com.gp.shifa.di.module;

import static com.gp.shifa.BaseApp.getContext;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gp.shifa.BuildConfig;
import com.gp.shifa.R;
import com.gp.shifa.data.AppDataManager;
import com.gp.shifa.data.DataManager;
import com.gp.shifa.data.apis.ApiHelper;
import com.gp.shifa.data.apis.AppApiHelper;
import com.gp.shifa.data.apis.NetworkService;
import com.gp.shifa.data.prefs.AppPreferencesHelper;
import com.gp.shifa.data.prefs.PreferencesHelper;
import com.gp.shifa.di.DatabaseInfo;
import com.gp.shifa.di.PreferenceInfo;
import com.gp.shifa.utils.AppConstants;
import com.gp.shifa.utils.LanguageHelper;
import com.gp.shifa.utils.Token;
import com.gp.shifa.utils.rx.AppSchedulerProvider;
import com.gp.shifa.utils.rx.SchedulerProvider;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
                    requestBuilder.addHeader("api_password", "slaughterhouse_2021_m3m");
                    requestBuilder.addHeader("api_lang", LanguageHelper.getLanguage(getContext()));
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
