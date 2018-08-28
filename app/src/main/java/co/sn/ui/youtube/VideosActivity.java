package co.sn.ui.youtube;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.sn.app.R;
import co.sn.data.model.YoutubeItem;
import co.sn.ui.base.BaseActivity;

public class VideosActivity extends BaseActivity implements VideosView {

    @BindView(R.id.layout_search)
    LinearLayout layout_search;
    @BindView(R.id.search_view)
    SearchView search_view;
    @BindView(R.id.tv_search)
    TextView tv_search;

    @BindView(R.id.rv_videos)
    RecyclerView rv_videos;
    @BindView(R.id.lv_no_data)
    LottieAnimationView lv_no_data;

    @Inject
    VideosPresenter<VideosView> presenter;

    private VideosAdapter adapter;
    private ArrayList<YoutubeItem> youtubeItems;
    private final Handler handler = new Handler();
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setupDependencies();
        setupUi();
    }

    private void setupDependencies() {
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        presenter.onAttach(this);
    }

    private void setupUi() {
        setupSearchView();
        timer = new Timer();

        youtubeItems = new ArrayList<>();
        adapter = new VideosAdapter(this, presenter, youtubeItems);
        rv_videos.setHasFixedSize(true);
        rv_videos.setLayoutManager(new LinearLayoutManager(this));
        rv_videos.setAdapter(adapter);

        rv_videos.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                if ((lastVisibleItemPosition + 1) == manager.getItemCount() && !presenter.isDataLoading() && presenter.isMoreData()) {
                    presenter.setDataLoading(true);
                    if (TextUtils.isEmpty(presenter.getSearch())) {
                        presenter.getMostPopularVideos();
                    } else {
                        presenter.getSearchVideos();
                    }
                }
            }
        });
        presenter.getMostPopularVideos();
    }

    @OnClick(R.id.layout_search)
    public void onSearchClicked() {
        if (search_view.isIconified()) {
            tv_search.setVisibility(View.GONE);
            search_view.setIconified(false);
        }
    }

    @Override
    public void onVideosFetched(ArrayList<? extends YoutubeItem> items) {
        youtubeItems.addAll(items);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() == 0) {
            lv_no_data.setVisibility(View.VISIBLE);
        } else {
            lv_no_data.setVisibility(View.GONE);
        }
    }

    private void refreshVideos() {
        youtubeItems.clear();
        presenter.resetValues();
    }

    private void setupSearchView() {

        // hack for removing the underline in searchview
        View view = search_view.findViewById(android.support.v7.appcompat.R.id.search_plate);
        view.setBackgroundColor(getResources().getColor(R.color.white));

        search_view.setOnSearchClickListener(v -> tv_search.setVisibility(View.GONE));
        search_view.setOnCloseListener(() -> {
            tv_search.setVisibility(View.VISIBLE);
            return false;
        });
        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    if (search_view.getWidth() > 0) {
                        presenter.setSearch(null);
                        refreshVideos();
                        presenter.getMostPopularVideos();
                    }
                } else {
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(() -> {
                                presenter.setSearch(newText);
                                refreshVideos();
                                presenter.getSearchVideos();
                            });
                        }
                    }, 500);
                }
                return true;
            }
        });
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.onDetach();
        }
        super.onDestroy();
    }
}
