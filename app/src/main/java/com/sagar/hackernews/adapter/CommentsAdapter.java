package com.sagar.hackernews.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sagar.hackernews.R;

import java.util.List;

/**
 * Created by Sagar on 10/1/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentInfo> commentInfoList;

    public CommentsAdapter(final List<CommentsAdapter.CommentInfo> commentInfoList) {
        this.commentInfoList = commentInfoList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        return new CommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CommentsViewHolder viewHolder = (CommentsViewHolder) holder;
        viewHolder.bindData(commentInfoList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentInfoList.size();
    }

    public static class CommentInfo {
        private final String by;
        private final String comment;
        private final String reply;

        public CommentInfo(String by, String comment, String reply) {
            this.by = by;
            this.comment = comment;
            this.reply = reply;
        }

        public String getBy() {
            return by;
        }

        public String getComment() {
            return comment;
        }

        public String getReply() {
            return reply;
        }
    }
}
