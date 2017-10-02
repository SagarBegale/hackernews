package com.sagar.hackernews.http.api;

import java.util.List;

/**
 * Created by sbegale on 9/30/2017.
 */

public class StoryRequest {
    private final List<Integer> storiesIds;

    public StoryRequest(List<Integer> storiesIds) {
        this.storiesIds = storiesIds;
    }


}
