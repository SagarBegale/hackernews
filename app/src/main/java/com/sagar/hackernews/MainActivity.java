package com.sagar.hackernews;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sagar.hackernews.adapter.StoriesAdapter;
import com.sagar.hackernews.http.api.HackerApi;
import com.sagar.hackernews.http.models.StoriesResponse;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.sagar.hackernews.http.api.HackerApi.BASE_URL;


public class MainActivity extends AppCompatActivity implements Callback<List<Integer>> {

    public static final String EXTRA_COMMENT_IDS = "EXTRA_COMMENT_IDS";
    private static final int MAX_STORIES_TO_LOAD = 12;
    @BindView(R.id.storiesView)
    RecyclerView recyclerView;

    @BindView(R.id.tv_message)
    TextView tvMessage;

    @BindView(R.id.swipe_to_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.overlay)
    View overlay;

    private List<StoriesAdapter.StoryInfo> storyInfoList = new ArrayList<>();
    private final Queue<Integer> storyIdQueue = new ArrayDeque<>();
    private StoriesAdapter adapter;
    private ProgressDialog progressDialog;
    public static final String TAG_ID = "HNS";
    private AsyncStoriesLoader storiesLoader = new AsyncStoriesLoader();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle(getString(R.string.loading));
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.setCancelable(false);

        adapter = new StoriesAdapter(storyInfoList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
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
                    if (storyIdQueue.isEmpty()) {
                        return;
                    }
                    progressDialog.show();
                    loadStories();
                }
            }
        });
        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DO NOTHING
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                loadTopStoriesid();
            }
        });
        progressDialog.show();
        loadTopStoriesid();
    }

    private void loadTopStoriesid() {
        storyInfoList.clear();
        adapter.notifyDataSetChanged();
        if (!swipeRefreshLayout.isRefreshing()) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
        overlay.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(JacksonConverterFactory
                .create()).build();
        HackerApi hackerApi = retrofit.create(HackerApi.class);
        Call<List<Integer>> call = hackerApi.topStoriesList();
        call.enqueue(this);
    }


    @Override
    public void onResponse(Call<List<Integer>> call, Response<List<Integer>> response) {
        List<Integer> topStoriesList = response.body();
        tvMessage.setVisibility(View.GONE);
        Log.d(TAG_ID, "Number of top stories: " + topStoriesList.size());
        storyIdQueue.addAll(topStoriesList);
        loadStories();

    }

    private void loadStories() {
        if (storiesLoader.getStatus() != AsyncTask.Status.RUNNING) {
            storiesLoader = new AsyncStoriesLoader();
            storiesLoader.execute();
        }
    }

    @Override
    public void onFailure(Call<List<Integer>> call, Throwable t) {
        Log.d(TAG_ID, t.toString());
        progressDialog.dismiss();
        if (storyInfoList.size() <= 0) {
            tvMessage.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setRefreshing(false);
        overlay.setVisibility(View.GONE);
        Snackbar.make(findViewById(R.id.main_layout), R.string.error_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                progressDialog.show();
                loadTopStoriesid();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private final class AsyncStoriesLoader extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < MAX_STORIES_TO_LOAD; i++) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory
                        (JacksonConverterFactory.create()).build();
                HackerApi hackerApi = retrofit.create(HackerApi.class);
                if (storyIdQueue.isEmpty()) {
                    return null;
                }
                Call<StoriesResponse> storyCall = hackerApi.getStory(storyIdQueue.poll());

                try {
                    Response<StoriesResponse> response = storyCall.execute();
                    final StoriesResponse storiesResponse = response.body();

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            final String desc = getString(R.string.story_desc, storiesResponse.getScore(),
                                    storiesResponse.getBy(), storiesResponse.getDescendants());
                            SpannableString ss = new SpannableString(desc);
                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View textView) {
                                    Intent intent = new Intent(MainActivity.this, CommentsActivity.class);
                                    intent.putIntegerArrayListExtra(EXTRA_COMMENT_IDS, (ArrayList) storiesResponse
                                            .getKids());
                                    startActivity(intent);
                                }
                            };
                            ss.setSpan(clickableSpan, desc.indexOf("|") + 2, desc.length(), Spanned
                                    .SPAN_EXCLUSIVE_EXCLUSIVE);
                            storyInfoList.add(new StoriesAdapter.StoryInfo(storiesResponse.getTitle(), ss,
                                    storiesResponse.getUrl(), storiesResponse.getKids()));
                            adapter.notifyDataSetChanged();
                        }
                    });
                } catch (IOException e) {
                    Log.d(TAG_ID, e.toString());
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG_ID, "Number of stories: " + storyInfoList.size());
            adapter.notifyDataSetChanged();
            if (storyInfoList.size() <= 0) {
                tvMessage.setVisibility(View.VISIBLE);
            }
            progressDialog.dismiss();
            overlay.setVisibility(View.GONE);
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }
}
