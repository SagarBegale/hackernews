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
public class CommentsResponse {
    private final String by;
    private final long id;
    private final List<Integer> kids;
    private final long parent;
    private final String text;
    private final long time;
    private final String type;

    @JsonCreator
    public CommentsResponse(@JsonProperty("by") String by, @JsonProperty("id") long id, @JsonProperty("kids")
            List<Integer> kids, @JsonProperty("parent") long parent, @JsonProperty("text") String text, @JsonProperty
            ("time") long time, @JsonProperty("type") String type) {
        this.by = by;
        this.id = id;
        this.kids = kids;
        this.parent = parent;
        this.text = text;
        this.time = time;
        this.type = type;
    }

    @JsonProperty("by")
    public String getBy() {
        return by;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("kids")
    public List<Integer> getKids() {
        return kids;
    }

    @JsonProperty("parent")
    public long getParent() {
        return parent;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("time")
    public long getTime() {
        return time;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }
}
