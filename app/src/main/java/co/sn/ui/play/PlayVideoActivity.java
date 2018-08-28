package co.sn.ui.play;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.sn.app.R;
import co.sn.utils.AppConstants;

public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String PARAM_VIDEO_ID = "param_video_id";

    @BindView(R.id.layout_controls)
    View layout_controls;
    @BindView(R.id.iv_play_pause)
    ImageView iv_play_pause;
    @BindView(R.id.seekbar)
    SeekBar seekBar;
    @BindView(R.id.youtube_player_view)
    YouTubePlayerView youTubePlayerView;

    private Timer timer;
    private YouTubePlayer youTubePlayer;
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_play_video);

        if (getIntent() != null && !TextUtils.isEmpty(getIntent().getStringExtra(PARAM_VIDEO_ID))) {
            videoId = getIntent().getStringExtra(PARAM_VIDEO_ID);
        } else {
            Toast.makeText(this, "Error playing video !!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setupDependencies();
        setupUi();
    }

    private void setupDependencies() {
        ButterKnife.bind(this);
    }

    private void setupUi() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    if (youTubePlayer != null) {
                        youTubePlayer.seekToMillis(i);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        youTubePlayerView.initialize(AppConstants.getGapiKey(), this);
        setFullScreen();
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setImmersiveMode();
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void setImmersiveMode() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void toggle() {
        if (layout_controls.getVisibility() == View.VISIBLE) {
            layout_controls.setVisibility(View.GONE);
        } else {
            layout_controls.setVisibility(View.VISIBLE);
        }
    }

    private void setTimer() {
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    seekBar.setMax(youTubePlayer.getDurationMillis());
                    seekBar.setProgress(youTubePlayer.getCurrentTimeMillis());
                });
            }
        }, 0, 1000);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        youTubePlayer.cueVideo(videoId);
        this.youTubePlayer = youTubePlayer;
        setTimer();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
    }

    @OnClick(R.id.iv_play_pause)
    public void onPlayPauseClicked() {
        if (youTubePlayer != null) {
            if (youTubePlayer.isPlaying()) {
                youTubePlayer.pause();
                iv_play_pause.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_play_arrow));
            } else {
                youTubePlayer.play();
                iv_play_pause.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_pause));
            }
            seekBar.setMax(this.youTubePlayer.getDurationMillis());
            seekBar.setProgress(this.youTubePlayer.getCurrentTimeMillis());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (youTubePlayer != null) {
            setTimer();
            youTubePlayer.pause();
            iv_play_pause.setImageDrawable(ContextCompat.getDrawable(PlayVideoActivity.this, R.drawable.ic_play_arrow));
        }
    }

    @Override
    protected void onPause() {
        if (timer != null) {
            timer.cancel();
        }
        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setFullScreen();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
        if (youTubePlayer != null) {
            youTubePlayer.release();
            youTubePlayer = null;
        }
    }
}
