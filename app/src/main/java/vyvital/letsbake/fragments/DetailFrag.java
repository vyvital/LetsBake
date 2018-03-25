package vyvital.letsbake.fragments;


import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.List;

import vyvital.letsbake.MainActivity;
import vyvital.letsbake.R;
import vyvital.letsbake.data.Recipe;
import vyvital.letsbake.data.Steps;

import static vyvital.letsbake.Utils.StepsAdapter.STEP_KEY;
import static vyvital.letsbake.Utils.StepsAdapter.STEP_LIST;
import static vyvital.letsbake.Utils.StepsAdapter.STEP_SIZE;

public class DetailFrag extends Fragment {
    public static final String TAG = DetailFrag.class.getSimpleName();
    private List<Steps> steps;
    private Recipe recipe;
    private Steps step;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private TextView instruct;
    private Button next;
    private Button back;
    private ImageView placeholder_img;
    private int size;
    private long position;

    public DetailFrag() {

    }

    public static DetailFrag newInstance() {
        return new DetailFrag();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
        if (!MainActivity.isTablet(getContext())) {
            ((MainActivity) getActivity()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            ((MainActivity) getActivity()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            ((MainActivity) getActivity()).getSupportActionBar().hide();
        }
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable("recipe");
            step = savedInstanceState.getParcelable("step");
            size = savedInstanceState.getInt("size");
            steps = recipe.getStepsList();
        } else {
            if (getArguments() != null) {
                recipe = getArguments().getParcelable(STEP_LIST);
                step = getArguments().getParcelable(STEP_KEY);
                size = getArguments().getInt(STEP_SIZE);
                steps = recipe.getStepsList();
            }
        }
        if (step != null) {
            initialize(view);
            stepCheck();
        }


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initialize(View view) {
        placeholder_img = view.findViewById(R.id.placeholder_img);
        placeholder_img.setVisibility(View.GONE);
        back = view.findViewById(R.id.back);
        next = view.findViewById(R.id.forward);
        mPlayerView = view.findViewById(R.id.exo_player_view);
        instruct = view.findViewById(R.id.instructions);
        instruct.setText(step.getDescription());

        if (!step.getVideoURL().equals("")) {
            if (getMimeType(step.getVideoURL()).equals("video/mp4"))
                initializePlayer(Uri.parse(step.getVideoURL()));
            else {
                placeholder_img.setVisibility(View.VISIBLE);
                mPlayerView.setVisibility(View.GONE);
                Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
            }
        } else if (!step.getThumbnailURL().equals("")) {
            if (getMimeType(step.getThumbnailURL()).equals("image/jpg")) {
                placeholder_img.setVisibility(View.VISIBLE);
                mPlayerView.setVisibility(View.GONE);
                Picasso.with(getActivity()).load(step.getThumbnailURL()).noFade().into(placeholder_img);
            } else {
                placeholder_img.setVisibility(View.VISIBLE);
                mPlayerView.setVisibility(View.GONE);
                Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
            }
        } else {
            placeholder_img.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
            Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
        }
        mPlayerView.hideController();
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_video));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step = steps.get(Integer.parseInt(step.getId()) - 1);
                instruct.setText(step.getDescription());
                placeholder_img.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                stepCheck();
                if (mExoPlayer != null) releasePlayer();
                if (!step.getVideoURL().equals("")) {
                    if (getMimeType(step.getVideoURL()).equals("video/mp4"))
                        initializePlayer(Uri.parse(step.getVideoURL()));
                    else {
                        placeholder_img.setVisibility(View.VISIBLE);
                        mPlayerView.setVisibility(View.GONE);
                        Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
                    }
                } else if (!step.getThumbnailURL().equals("")) {
                    if (getMimeType(step.getThumbnailURL()).equals("image/jpg")) {
                        placeholder_img.setVisibility(View.VISIBLE);
                        mPlayerView.setVisibility(View.GONE);
                        Picasso.with(getActivity()).load(step.getThumbnailURL()).noFade().into(placeholder_img);
                    } else {
                        placeholder_img.setVisibility(View.VISIBLE);
                        mPlayerView.setVisibility(View.GONE);
                        Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
                    }
                } else {
                    placeholder_img.setVisibility(View.VISIBLE);
                    mPlayerView.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
                }
                mPlayerView.hideController();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step = steps.get(Integer.parseInt(step.getId()) + 1);
                instruct.setText(step.getDescription());
                placeholder_img.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
                stepCheck();
                if (mExoPlayer != null) releasePlayer();
                if (!step.getVideoURL().equals("")) {
                    if (getMimeType(step.getVideoURL()).equals("video/mp4"))
                        initializePlayer(Uri.parse(step.getVideoURL()));
                    else {
                        placeholder_img.setVisibility(View.VISIBLE);
                        mPlayerView.setVisibility(View.GONE);
                        Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
                    }
                } else if (!step.getThumbnailURL().equals("")) {
                    if (getMimeType(step.getThumbnailURL()).equals("image/jpg")) {
                        placeholder_img.setVisibility(View.VISIBLE);
                        mPlayerView.setVisibility(View.GONE);
                        Picasso.with(getActivity()).load(step.getThumbnailURL()).noFade().into(placeholder_img);
                    } else {
                        placeholder_img.setVisibility(View.VISIBLE);
                        mPlayerView.setVisibility(View.GONE);
                        Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
                    }
                } else {
                    placeholder_img.setVisibility(View.VISIBLE);
                    mPlayerView.setVisibility(View.GONE);
                    Picasso.with(getActivity()).load(R.drawable.no_video).noFade().into(placeholder_img);
                }
                mPlayerView.hideController();
            }
        });
    }

    private void stepCheck() {
        if (step.getId().equals("0")) {
            back.setVisibility(View.INVISIBLE);
        } else if (step.getId().equals(String.valueOf(size - 1)))
            next.setVisibility(View.INVISIBLE);
        else {
            back.setVisibility(View.VISIBLE);
            next.setVisibility(View.VISIBLE);
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "Let's Bake");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("recipe", recipe);
        outState.putParcelable("step", step);
        outState.putInt("size", size);
    }

    public static String getMimeType(String url) {
        String type = "";
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }


}
