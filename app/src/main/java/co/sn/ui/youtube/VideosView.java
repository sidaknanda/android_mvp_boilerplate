package co.sn.ui.youtube;

import java.util.ArrayList;

import co.sn.data.model.SearchYoutubeItem;
import co.sn.data.model.YoutubeItem;
import co.sn.ui.base.MvpView;

public interface VideosView extends MvpView {

    void onVideosFetched(ArrayList<? extends YoutubeItem> youtubeItems);
}
