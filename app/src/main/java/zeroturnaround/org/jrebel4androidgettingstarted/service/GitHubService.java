package zeroturnaround.org.jrebel4androidgettingstarted.service;

import java.io.IOException;
import java.util.List;


import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by shelajev on 16/12/15.
 */
public interface GitHubService {
    @GET("repos/{owner}/{repo}/contributors")
    Call<List<Contributor>> repoContributors(
            @Path("owner") String owner,
            @Path("repo") String repo);


    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl.Builder originalUrlBuilder = originalHttpUrl.newBuilder();
            originalUrlBuilder.addQueryParameter("access_token", "YOUR VALUE HERE");

            HttpUrl url = originalUrlBuilder.build();

            Request.Builder reqBuilder = original.newBuilder().url(url);
            return chain.proceed(reqBuilder.build());
        }
    }).build();




    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
            .build();

    @GET("/users/{username}")
    Call<Contributor>user(
            @Path("username")String login);

}


