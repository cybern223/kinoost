package com.cyber.kinoost.db.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.MusicRating;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class MusicRatingRepository {
	DatabaseHelper dbHelper;
	
	private RuntimeExceptionDao<MusicRating, Integer> musicRatingDao = null;
	
	public MusicRatingRepository(Context context) {
		dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		
		musicRatingDao = dbHelper.getMusicRatingRuntimeExceptionDao();
	}
	
	public MusicRatingRepository(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
		musicRatingDao = dbHelper.getMusicRatingRuntimeExceptionDao();
	}
	
	public void createMusicRating(MusicRating musicRating) {
		if(musicRating == null) return;
		
		musicRatingDao.create(musicRating);
	}
	
	public void createMusicRatingList(final List<MusicRating> musicRating) {
		if(musicRating == null) return;
		
        musicRatingDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (MusicRating mr : musicRating) {
	                musicRatingDao.create(mr);
	            }
	            return null;
	        }
	    });
	}
	
	public void editMusicRating(MusicRating musicRating) {
		if(musicRating == null) return;
		
		musicRatingDao.createOrUpdate(musicRating);
	}
	
	public void editMusicRatingList(final List<MusicRating> musicRating) {
		if(musicRating == null) return;
		
        musicRatingDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (MusicRating mr : musicRating) {
	                musicRatingDao.createOrUpdate(mr);
	            }
	            return null;
	        }
	    });
	}
	
	public void deleteMusicRating(MusicRating musicRating) {
		if(musicRating == null) return;
		
		musicRatingDao.delete(musicRating);
	}
	
	public void deleteMusicRatingList(List<MusicRating> musicRating) {
		if(musicRating == null) return;
		
		musicRatingDao.delete(musicRating);
	}
	
	public List<MusicRating> getMusicRating(int offset, int limit) throws SQLException {
	    List<MusicRating> musicRatingList = new ArrayList<MusicRating>();
	    QueryBuilder<MusicRating, Integer> queryBuilder = musicRatingDao.queryBuilder();
	    if(offset > 0) queryBuilder.offset(Long.valueOf(offset));
	    if(limit > 0) queryBuilder.limit(Long.valueOf(limit));
	    musicRatingList = musicRatingDao.query(queryBuilder.prepare());

	    return musicRatingList;
	}
	
	public List<MusicRating> getMusicRatingByDate(Date date, int offset, int limit) throws SQLException {
	    List<MusicRating> musicRatingList = new ArrayList<MusicRating>();
	    QueryBuilder<MusicRating, Integer> queryBuilder = musicRatingDao.queryBuilder();
	    if(offset > 0) queryBuilder.offset(Long.valueOf(offset));
	    if(limit > 0) queryBuilder.limit(Long.valueOf(limit));
	    if(date.getTime() > 0) queryBuilder.where().gt(MusicRating.DATE_TIME, date);
	    musicRatingList = musicRatingDao.query(queryBuilder.prepare());

	    return musicRatingList;
	}
}
