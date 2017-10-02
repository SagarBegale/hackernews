package com.sagar.hackernews.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.sagar.hackernews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sagar on 10/1/2017.
 */

public class CommentsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_comment)
    TextView tvComment;

    @BindView(R.id.tv_reply_desc)
    TextView tvReplyDesc;

    @BindView(R.id.tv_reply)
    TextView tvReply;

    public CommentsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final CommentsAdapter.CommentInfo commentInfo) {
        tvTitle.setText(commentInfo.getBy());
        tvComment.setText(Html.fromHtml(commentInfo.getComment(),Html.FROM_HTML_MODE_LEGACY));
        tvComment.setMovementMethod(LinkMovementMethod.getInstance());
        final String replyDesc = commentInfo.getReply();
        if (replyDesc.isEmpty()) {
            tvReply.setVisibility(View.GONE);
            tvReplyDesc.setVisibility(View.GONE);
        } else {
            tvReplyDesc.setVisibility(View.VISIBLE);
            tvReplyDesc.setText(Html.fromHtml(replyDesc,Html.FROM_HTML_MODE_LEGACY));
            tvReplyDesc.setMovementMethod(LinkMovementMethod.getInstance());
            tvReply.setVisibility(View.VISIBLE);
        }
    }
}
