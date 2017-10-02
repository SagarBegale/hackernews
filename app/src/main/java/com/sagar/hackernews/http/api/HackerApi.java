package com.sagar.hackernews.http.api;

import com.sagar.hackernews.http.models.CommentsResponse;
import com.sagar.hackernews.http.models.StoriesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author sbegale
 */

public interface HackerApi {

    public static final String BASE_URL="https://hacker-news.firebaseio.com/v0/";
    @GET("topstories.json")
    Call<List<Integer>> topStoriesList();

    @GET("item/{storyId}.json")
    Call<StoriesResponse> getStory(@Path("storyId") long storyId);

    @GET("item/{commentId}.json")
    Call<CommentsResponse> getComment(@Path("commentId") int commentId);
}
