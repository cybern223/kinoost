package com.cyber.kinoost.adapters;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyber.kinoost.KinoostActivity;
import com.cyber.kinoost.R;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.fragments.MusicByFilmFragment;
import com.squareup.picasso.Picasso;

public class GridViewAdapter extends BaseAdapter {
    
	private Context mContext;
    
	private List<Film> films;

    public GridViewAdapter(Context c, List<Film> films) {
        mContext = c;
        this.films = films;
    }

    public int getCount() {
        return films.size();
    }

    public Object getItem(int position) {
        return films.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder
	{
		public ImageView image;
		public TextView name;
		public TextView year;
	}
    
    public View getView(final int position, View convertView, ViewGroup parent) {
    	View row = convertView; 
    	ViewHolder holder = null;
    	
    	if (row == null) { 
    		LayoutInflater inflater = LayoutInflater.from(mContext);
    		row = inflater.inflate(R.layout.film_row, parent, false); 
    		holder = new ViewHolder(); 
    		holder.name = (TextView) row.findViewById(R.id.name); 
    		holder.year = (TextView) row.findViewById(R.id.year); 
    		holder.image = (ImageView) row.findViewById(R.id.image); 
    		row.setTag(holder); 
    		} 
    	else { 
    		holder = (ViewHolder) row.getTag(); 
    	} 
    	Film film = films.get(position); 
    	holder.name.setText(film.getName());  	
    	holder.name.setSelected(true);
    	holder.year.setText(String.valueOf(film.getYear()));
    	
		Picasso.with(mContext).load(film.getImgUrl())
				.placeholder(R.drawable.placeholder).fit().into(holder.image);
    	
    	holder.image.setOnClickListener(new OnClickListener() {

             @Override
             public void onClick(View v) {
                 startFilmFragment(position);
             }
         });
    	
    	return row;
    }
    
    private void startFilmFragment(int position) {
    	KinoostActivity activity = (KinoostActivity) mContext;
		Fragment newFragment = new MusicByFilmFragment(); 
		Bundle bundle = new Bundle();
		bundle.putSerializable("film", films.get(position));
		newFragment.setArguments(bundle);
		
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack("ost fragment");
        transaction.commit();
       
    }

}