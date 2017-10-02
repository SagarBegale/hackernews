package com.sagar.hackernews.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sagar.hackernews.R;
import com.sagar.hackernews.http.models.StoriesResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author sbegale
 */

public class StoriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StoryInfo> storyInfoList;

    public StoriesAdapter(final List<StoryInfo> storyInfoList) {
        this.storyInfoList = storyInfoList;
    }

    public void clear() {
        this.storyInfoList.clear();
        notifyDataSetChanged();
    }

    public void addAll(final List<StoryInfo> storyInfoList) {
        this.storyInfoList = storyInfoList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_stories, parent, false);
        return new StoriesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StoriesViewHolder viewHolder = (StoriesViewHolder) holder;
        viewHolder.bindData(storyInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return storyInfoList.size();
    }

    public static class StoryInfo {
        private final String title;
        private final SpannableString description;
        private final String storyUrl;
        private final List<Integer> commentids;

        public StoryInfo(String title, SpannableString description, String url, List<Integer> commentIds) {
            this.title = title;
            this.description = description;
            this.storyUrl = url;
            this.commentids = commentIds;
        }

        public String getTitle() {
            return title;
        }

        public SpannableString getDescription() {
            return description;
        }

        public String getStoryUrl() {
            return storyUrl;
        }

        public List<Integer> getCommentids() {
            return commentids;
        }
    }
}
