package com.cyber.kinoost.adapters;

import java.util.List;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyber.kinoost.R;
import com.cyber.kinoost.adapters.FilmOstListViewAdapter.FilmInfoHolder;
import com.cyber.kinoost.api.Account;
import com.cyber.kinoost.api.ApiHelper;
import com.cyber.kinoost.api.vk.sources.Api;
import com.cyber.kinoost.db.models.Music;
import com.cyber.kinoost.db.models.Performer;

public class SavedOstListViewAdapter extends BaseAdapter {

	private Context mContext;

	private List<Music> music;

	final ApiHelper apiHelper = new ApiHelper();

	public SavedOstListViewAdapter(Context c, List<Music> sounds) {
		mContext = c;
		this.music = sounds;
	}

	public SavedOstListViewAdapter(FragmentActivity activity, List<Music> sounds) {
		mContext = activity;
		this.music = sounds;
		// TODO Auto-generated constructor stub
	}

	public int getCount() {
		return music.size();
	}

	public Object getItem(int position) {
		return music.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public ImageView image;
		public TextView performer;
		public TextView songName;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TextView musicHolder;
		if (row == null || row.getTag() instanceof FilmInfoHolder) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			row = inflater.inflate(R.layout.film_ost_row, parent, false);
			musicHolder = (TextView) row.findViewById(R.id.film_name);
			musicHolder.setText(music.get(position).getName());
			row.setTag(musicHolder);
			FrameLayout musicRow = (FrameLayout) row;
			musicRow.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.i("ListAdapter", music.get(position).getName());
					Account account = new Account(mContext);
					Api api = new Api(account);

					apiHelper.getSongMusic(mContext, api, music.get(position));

				}
			});
		} else
			musicHolder = (TextView) row.getTag();
		musicHolder.setText(music.get(position).getName());
		return row;
	}

}