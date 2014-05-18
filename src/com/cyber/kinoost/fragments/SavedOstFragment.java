package com.cyber.kinoost.fragments;

import java.io.File;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.SavedOstListViewAdapter;

import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.repositories.MusicRepository;

public class SavedOstFragment extends Fragment {

	private Context mContext;
	final ApiHelper apiHelper = new ApiHelper();

	public SavedOstFragment() {
		// Empty constructor required for fragment subclasses
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View saved = inflater.inflate(R.layout.fragment_ost, container, false);

		/*
		 * String songName; try { songName = (String)
		 * getArguments().getSerializable("songName"); } catch(Exception e) {
		 * songName = ""; }
		 */

		final List<Music> music = getMusic();

		ListView listView = (ListView) saved.findViewById(R.id.listView);

		listView.setAdapter(new SavedOstListViewAdapter(getActivity(), music));
		return saved;
	}

	private List<Music> getMusic() {

		List<Music> music = null;
		MusicRepository musicRepo = new MusicRepository(getActivity()
				.getBaseContext());
		final String MEDIA_PATH = new String(getActivity().getCacheDir()
				.toString());

		File home = new File(MEDIA_PATH);

		if (home.listFiles(new FileExtensionFilter()).length > 0) {
			for (File file : home.listFiles(new FileExtensionFilter())) {
				String title = file.getName().substring(0,
						(file.getName().length() - 4));
				try {
					music = musicRepo.findMusicByName(title, 0, 30);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return music;

	}

}
