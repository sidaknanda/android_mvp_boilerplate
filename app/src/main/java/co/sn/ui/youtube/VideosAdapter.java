package co.sn.ui.youtube;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.sn.app.R;
import co.sn.data.model.YoutubeItem;
import co.sn.ui.base.BaseViewHolder;
import co.sn.ui.play.PlayVideoActivity;
import co.sn.utils.ViewUtils;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideoViewHolder> {

    private Context context;
    private VideosPresenter<VideosView> presenter;
    private ArrayList<YoutubeItem> youtubeItems;

    public VideosAdapter(Context context, VideosPresenter<VideosView> presenter, ArrayList<YoutubeItem> youtubeItems) {
        this.context = context;
        this.presenter = presenter;
        this.youtubeItems = youtubeItems;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder viewHolder, int position) {
        YoutubeItem item = youtubeItems.get(position);
        ViewUtils.setImageWithDrawable(viewHolder.iv_video_thumbnail,
                item.getSnippet().getThumbnails().getMediumVal().getUrl(),
                new ColorDrawable(ContextCompat.getColor(context, R.color.black)));
        viewHolder.tv_video_title.setText(item.getSnippet().getTitle());
        viewHolder.tv_description.setText(item.getSnippet().getDescription());
        if (item.getContentDetails() != null && !TextUtils.isEmpty(item.getContentDetails().getDuration())) {
            viewHolder.tv_video_duration.setText(presenter.parseVideoDurationForApi(item.getContentDetails().getDuration()));
            viewHolder.tv_video_duration.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tv_video_duration.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return youtubeItems.size();
    }

    class VideoViewHolder extends BaseViewHolder {

        @BindView(R.id.iv_video_thumbnail)
        ImageView iv_video_thumbnail;
        @BindView(R.id.tv_video_duration)
        TextView tv_video_duration;
        @BindView(R.id.tv_video_title)
        TextView tv_video_title;
        @BindView(R.id.tv_description)
        TextView tv_description;

        public VideoViewHolder(View itemView) {
            super(context, itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        public void onVideoClicked() {
            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                YoutubeItem item = youtubeItems.get(getAdapterPosition());
                Intent playIntent = new Intent(context, PlayVideoActivity.class);
                playIntent.putExtra(PlayVideoActivity.PARAM_VIDEO_ID, item.getVideoId());
                context.startActivity(playIntent);
            } else {
                showSnackBar("Error playing video !!");
            }
        }
    }
}
