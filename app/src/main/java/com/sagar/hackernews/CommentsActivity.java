package com.sagar.hackernews;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.sagar.hackernews.adapter.CommentsAdapter;
import com.sagar.hackernews.http.api.HackerApi;
import com.sagar.hackernews.http.models.CommentsResponse;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.sagar.hackernews.MainActivity.EXTRA_COMMENT_IDS;
import static com.sagar.hackernews.MainActivity.TAG_ID;
import static com.sagar.hackernews.http.api.HackerApi.BASE_URL;

/**
 * Created by sagar on 10/1/2017.
 */

public class CommentsActivity extends AppCompatActivity {
    private static final int MAX_COMMENTS_TO_LOAD = 10;
    @BindView(R.id.commentsView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_message)
    TextView tvMessage;

    private ProgressDialog progressDialog;

    private List<Integer> commentIds;

    private List<CommentsAdapter.CommentInfo> commentInfoList = new ArrayList<>();
    private CommentsAdapter adapter;
    private final Queue<Integer> commentIdQueue = new ArrayDeque<>();
    private AsyncCommentsLoader commentsLoader = new AsyncCommentsLoader();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        setTitle(R.string.comment);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);
        commentIds = getIntent().getIntegerArrayListExtra(EXTRA_COMMENT_IDS);

        adapter = new CommentsAdapter(commentInfoList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    if (!commentIdQueue.isEmpty()) {
                        progressDialog.show();
                        if (commentsLoader.getStatus() != AsyncTask.Status.RUNNING) {
                            commentsLoader = new AsyncCommentsLoader();
                            commentsLoader.execute();
                        }
                    }
                }
            }
        });
        if (commentIds != null && commentIds.size() > 0) {
            progressDialog.show();
            commentIdQueue.addAll(commentIds);
            Log.d(TAG_ID, "Number of comments: " + commentIds.size());
            if (commentsLoader.getStatus() != AsyncTask.Status.RUNNING) {
                commentsLoader = new AsyncCommentsLoader();
                commentsLoader.execute();
            }
        } else {
            tvMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private final class AsyncCommentsLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < MAX_COMMENTS_TO_LOAD; i++) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory
                        (JacksonConverterFactory.create()).build();
                HackerApi hackerApi = retrofit.create(HackerApi.class);

                if (commentIdQueue.isEmpty()) {
                    return null;
                }
                Call<CommentsResponse> commentCall = hackerApi.getComment(commentIdQueue.poll());

                try {
                    Response<CommentsResponse> response = commentCall.execute();
                    final CommentsResponse commentsResponse = response.body();
                    List<Integer> replies = commentsResponse.getKids();
                    String reply = "";
                    if (replies != null && replies.size() > 0) {
                        Retrofit replyRetrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory
                                (JacksonConverterFactory.create()).build();
                        HackerApi replyApi = replyRetrofit.create(HackerApi.class);


                        Call<CommentsResponse> replyCall = replyApi.getComment(commentsResponse.getKids().get(0));
                        final Response<CommentsResponse> response1 = replyCall.execute();
                        final CommentsResponse replyResponse = response1.body();
                        reply = replyResponse.getText();
                    }
                    final String replyString = reply;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (commentsResponse.getText() != null) {
                                commentInfoList.add(new CommentsAdapter.CommentInfo(commentsResponse.getBy(),
                                        commentsResponse.getText(), replyString));
                            }
                        }
                    });
                } catch (IOException e) {
                    Log.d(TAG_ID, e.toString());
                    Snackbar.make(findViewById(R.id.comment_layout), R.string.error_message, Snackbar.LENGTH_LONG)
                            .show();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG_ID, "Number of comments: " + commentInfoList.size());
            adapter.notifyDataSetChanged();
            tvMessage.setVisibility(commentInfoList.size() <= 0 ? View.VISIBLE : View.GONE);
            progressDialog.dismiss();
        }
    }
}
