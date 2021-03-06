package com.cyber.kinoost.fragments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.listeners.OnSwipeTouchListener;
import com.cyber.kinoost.mediaplayer.SongsManager;
import com.cyber.kinoost.mediaplayer.Utilities;
import com.squareup.picasso.Picasso;

public class KPlayerFragment extends Fragment implements OnCompletionListener,
		SeekBar.OnSeekBarChangeListener {

	RelativeLayout menuContainer;
	RelativeLayout listContainer;
	RelativeLayout imgContainer;
	RelativeLayout headContainer;
	View myFragmentView;
	private final String MUSIC_CLASS_NAME = "com.cyber.kinoost.db.models.Music";
	private final String FILM_IMG_URL = "filmImgUrl";
	private Music music;
	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	private ImageView image;
	// Media Player
	private static MediaPlayer mp = new MediaPlayer();
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();
	private SongsManager songManager;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex = 0;
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	public KPlayerFragment() {
		// Empty constructor required for fragment subclasses
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		getActivity().getActionBar().show();			
	}
	
	private void startFilmFragment() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("music", music);
		Fragment newFragment = new FilmsByMusicFragment();
		newFragment.setArguments(bundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack("player fragment");
        transaction.commit();
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getActivity().getActionBar().hide();
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Get music
		music = (Music) getArguments().getSerializable(MUSIC_CLASS_NAME);
		String imgUrl = getArguments().getString(FILM_IMG_URL);
		
		myFragmentView = inflater.inflate(R.layout.player, container, false);
		myFragmentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
		    @Override
		    public void onSwipeLeft() {
		        startFilmFragment();
		    }
		});

		// All player buttons
		btnPlay = (ImageButton) myFragmentView.findViewById(R.id.btnPlay);
		btnForward = (ImageButton) myFragmentView.findViewById(R.id.btnForward);
		btnBackward = (ImageButton) myFragmentView.findViewById(R.id.btnBackward);
		songProgressBar = (SeekBar) myFragmentView.findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) myFragmentView.findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) myFragmentView.findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) myFragmentView.findViewById(R.id.songTotalDurationLabel);
		image = (ImageView) myFragmentView.findViewById(R.id.image);
		
		if (imgUrl.length() > 0)
			Picasso.with(getActivity()).load(imgUrl)
					.placeholder(R.drawable.placeholder).fit().into(image);

		// Mediaplayer
		songManager = new SongsManager();
		utils = new Utilities();

		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important

		// Getting all songs list
		songsList = songManager.getPlayList(music);

		// By default play first song
		playSong(0);

		/**
		 * Play button click event plays a song and changes button to pause
		 * image pauses a song and changes button to play image
		 * */
		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// check for already playing
				if (mp.isPlaying()) {
					if (mp != null) {
						mp.pause();
						// Changing button image to play button
						btnPlay.setImageResource(R.drawable.btn_play);
					}
				} else {
					// Resume song
					if (mp != null) {
						mp.start();
						// Changing button image to pause button
						btnPlay.setImageResource(R.drawable.btn_pause);
					}
				}

			}
		});

		/**
		 * Forward button click event Forwards song specified seconds
		 * */
		btnForward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// get current song position
				int currentPosition = mp.getCurrentPosition();
				// check if seekForward time is lesser than song duration
				if (currentPosition + seekForwardTime <= mp.getDuration()) {
					// forward song
					mp.seekTo(currentPosition + seekForwardTime);
				} else {
					// forward to end position
					mp.seekTo(mp.getDuration());
				}
			}
		});

		/**
		 * Backward button click event Backward song to specified seconds
		 * */
		btnBackward.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// get current song position
				int currentPosition = mp.getCurrentPosition();
				// check if seekBackward time is greater than 0 sec
				if (currentPosition - seekBackwardTime >= 0) {
					// forward song
					mp.seekTo(currentPosition - seekBackwardTime);
				} else {
					// backward to starting position
					mp.seekTo(0);
				}

			}
		});

		return myFragmentView;
	}

	/**
	 * Receiving song index from playlist view and play the song
	 * */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 100) {
			currentSongIndex = data.getExtras().getInt("songIndex");
			// play selected song
			playSong(currentSongIndex);
		}

	}

	/**
	 * Function to play a song
	 * 
	 * @param songIndex - index of song
	 * */
	public void playSong(int songIndex) {
		// Play song
		try {
			mp.reset();
			mp.setDataSource(songsList.get(songIndex).get("songPath"));
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.prepare();
			mp.start();

			// Displaying Song title
			String songTitle = songsList.get(songIndex).get("songTitle");
			songTitleLabel.setText(songTitle);

			// Changing Button Image to pause image
			btnPlay.setImageResource(R.drawable.btn_pause);

			// set Progress bar values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);

			// Updating progress bar
			updateProgressBar();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			mp.reset();
			playSong(songIndex);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			if (mp.isPlaying()) {
				long totalDuration = 0;
				long currentDuration = 0;
				try {
					totalDuration = mp.getDuration();
					currentDuration = mp.getCurrentPosition();
				} catch (Exception e) {

				}

				// Displaying Total Duration time
				songTotalDurationLabel.setText(""
						+ utils.milliSecondsToTimer(totalDuration));
				// Displaying time completed playing
				songCurrentDurationLabel.setText(""
						+ utils.milliSecondsToTimer(currentDuration));

				// Updating progress bar
				int progress = (int) (utils.getProgressPercentage(
						currentDuration, totalDuration));
				// Log.d("Progress", ""+progress);
				songProgressBar.setProgress(progress);
			}

			// Running this thread after 100 milliseconds
			mHandler.postDelayed(this, 100);
		}
	};

	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {

	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	/**
	 * On Song Playing completed if repeat is ON play same song again if shuffle
	 * is ON play random song
	 * */
	@Override
	public void onCompletion(MediaPlayer arg0) {

		// check for repeat is ON or OFF
		if (isRepeat) {
			// repeat is on play same song again
			playSong(currentSongIndex);
		} else if (isShuffle) {
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		} else {
			// no repeat or shuffle ON - play next song
			if (currentSongIndex < (songsList.size() - 1)) {
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			} else {
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.reset();
		mp.pause();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mp.reset();
		mp.pause();
	}
}
