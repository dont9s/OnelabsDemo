/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.onelabsdemo.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import tutorial.adit.com.onelabsdemo.model.SearchResultmodel;
import tutorial.adit.com.onelabsdemo.model.Wallpaper;


public interface UnsplashService {

    @GET("/photos")
    Call<List<Wallpaper>> getRecentPhotos(@Query("client_id") String apiKey,
                                          @Query("page") int page,
                                          @Query("per_page") int pageLimit);


    @GET("search/photos")
    Call<SearchResultmodel> getSearchPhotos(@Query("client_id") String apiKey,
                                            @Query("query") String query,
                                            @Query("page") int page,
                                            @Query("per_page") int pageLimit);

}
