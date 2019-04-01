/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.api;


import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import tutorial.adit.com.onelabsdemo.model.SearchResultmodel;
import tutorial.adit.com.onelabsdemo.model.Wallpaper;

/**
 * Created by shaz on 21/5/16.
 */
public interface WallpaperApi {

    String BASE_URL = "https://api.unsplash.com/";
    String API_KEY = "3718fb6751f784124dfcc824555b4af83f19da0805b1b22977d31bacb14f036a";


    @GET("photos/?client_id=" + API_KEY)
    Call<List<Wallpaper>> getWallpapers(//@Query("order_by") String orderBy,
                                        @Query("per_page") int perPage,
                                        @Query("page") int page);


    @GET("search/photos/?client_id=" + API_KEY )
    Call<SearchResultmodel> getSearchPhotos(//@Query("client_id") String apiKey,
                                            @Query("query") String query,
                                            @Query("page") int page,
                                            @Query("per_page") int pageLimit);

    @GET
    @Streaming
    Call<ResponseBody> downloadImage(@Url String fileURL);

    class Client {
        private static WallpaperApi service;
        public static WallpaperApi getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(BASE_URL)
                        .build();
                service = retrofit.create(WallpaperApi.class);
                return service;
            } else {
                return service;
            }
        }
    }
}
