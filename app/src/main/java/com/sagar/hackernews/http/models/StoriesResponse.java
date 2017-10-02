package com.sagar.hackernews.http.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author sbegale
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoriesResponse {

    private final String by;
    private final int descendants;
    private final long id;
    private final List<Integer> kids;
    private final int score;
    private final long time;
    private final String title;
    private final String type;
    private final String url;

    @JsonCreator
    public StoriesResponse(@JsonProperty("by") String by, @JsonProperty("descendants") int descendants, @JsonProperty
            ("id") long id, @JsonProperty("kids") List<Integer> kids, @JsonProperty("score") int score, @JsonProperty
            ("time") long time, @JsonProperty("title") String title, @JsonProperty("type") String type, @JsonProperty
            ("url") String url) {

        this.by = by;
        this.descendants = descendants;
        this.id = id;
        this.kids = kids;
        this.score = score;
        this.time = time;
        this.title = title;
        this.type = type;
        this.url = url;
    }

    @JsonProperty("by")
    public String getBy() {
        return by;
    }

    @JsonProperty("descendants")
    public int getDescendants() {
        return descendants;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("kids")
    public List<Integer> getKids() {
        return kids;
    }

    @JsonProperty("score")
    public int getScore() {
        return score;
    }

    @JsonProperty("time")
    public long getTime() {
        return time;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }
}
