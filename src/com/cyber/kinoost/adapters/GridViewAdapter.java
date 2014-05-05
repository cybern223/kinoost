package com.cyber.kinoost.adapters;

import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyber.kinoost.KinoostActivity;
import com.cyber.kinoost.R;
import com.cyber.kinoost.db.models.Film;
import com.cyber.kinoost.fragments.FilmFragment;
import com.cyber.kinoost.img.ImageLoader;

public class GridViewAdapter extends BaseAdapter {
    
	private Context mContext;
    
	private List<Film> films;
    
	private ImageLoader imageLoader;

    public GridViewAdapter(Context c, List<Film> films) {
        mContext = c;
        this.films = films;
        this.imageLoader = new ImageLoader(c);
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
	}
    
    public View getView(final int position, View convertView, ViewGroup parent) {
    	View row = convertView; 
    	ViewHolder holder = null;
    	
    	if (row == null) { 
    		LayoutInflater inflater = LayoutInflater.from(mContext);
    		row = inflater.inflate(R.layout.row_grid, parent, false); 
    		holder = new ViewHolder(); 
    		holder.name = (TextView) row.findViewById(R.id.item_text); 
    		holder.image = (ImageView) row.findViewById(R.id.item_image); 
    		row.setTag(holder); 
    		} 
    	else { 
    		holder = (ViewHolder) row.getTag(); 
    	} 
    	Film film = films.get(position); 
    	holder.name.setText(film.getName()); 
    	//holder.image.setImageResource(R.drawable.sample_2);
    	imageLoader.DisplayImage(film.getImgUrl(), holder.image);

    	LinearLayout filmLayout = (LinearLayout) row;
    	filmLayout.setOnClickListener(new OnClickListener() {

             @Override
             public void onClick(View v) {
                 startFilmFragment(position);
             }
         });
    	
    	return row;
    }
    
    private void startFilmFragment(int position) {
    	KinoostActivity activity = (KinoostActivity) mContext;
		Fragment newFragment = new FilmFragment(); 
		Bundle bundle = new Bundle();
		bundle.putSerializable("film", films.get(position));
		newFragment.setArguments(bundle);
		
        FragmentManager fragmentManager = activity.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, newFragment).commit();
//      transaction.addToBackStack(null);

    	
    }

}