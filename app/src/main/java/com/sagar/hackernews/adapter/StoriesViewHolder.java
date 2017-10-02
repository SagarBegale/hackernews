package com.sagar.hackernews.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.sagar.hackernews.CommentsActivity;
import com.sagar.hackernews.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author sbegale
 */

public class StoriesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_desc)
    TextView tvDescription;

    public StoriesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindData(final StoriesAdapter.StoryInfo storyInfo) {

        tvTitle.setText(storyInfo.getTitle());
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(storyInfo.getStoryUrl()));
                v.getContext().startActivity(i);
            }
        });
        tvDescription.setText(storyInfo.getDescription());
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        tvDescription.setHighlightColor(Color.TRANSPARENT);
    }
}
