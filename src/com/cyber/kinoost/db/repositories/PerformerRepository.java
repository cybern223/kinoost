package com.cyber.kinoost.db.repositories;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.cyber.kinoost.db.DatabaseHelper;
import com.cyber.kinoost.db.models.Performer;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;

public class PerformerRepository {
	DatabaseHelper dbHelper;
	
	private RuntimeExceptionDao<Performer, Integer> performerDao = null;
	
	public PerformerRepository(Context context) {
		dbHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
		
		performerDao = dbHelper.getPerformerRuntimeExceptionDao();
	}
	
	public PerformerRepository(DatabaseHelper dbHelper) {
		this.dbHelper = dbHelper;
		
		performerDao = dbHelper.getPerformerRuntimeExceptionDao();
	}
	
	public void createPerformer(Performer performer) {
		if(performer == null) return;
		
		performerDao.create(performer);
	}
	
	public void createPerformerList(final List<Performer> performers) {
		if(performers == null) return;
		
        performerDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (Performer performer : performers) {
	            	performerDao.create(performer);
	            }
	            return null;
	        }
	    });
	}
	
	public void editPerformer(Performer performer) {
		if(performer == null) return;
		
		performerDao.createOrUpdate(performer);
	}
	
	public void editPerformerList(final List<Performer> performers) {
		if(performers == null) return;
		
        performerDao.callBatchTasks(new Callable<Void>() {
	        @Override
	        public Void call() throws Exception {
	            for (Performer performer : performers) {
	            	performerDao.createOrUpdate(performer);
	            }
	            return null;
	        }
	    });
	}
	
	public void deletePerformer(Performer performer) {
		if(performer == null) return;
		
		performerDao.delete(performer);
	}
	
	public void deletePerformerList(List<Performer> performers) {
		if(performers == null) return;
		
		performerDao.delete(performers);
	}
	
	public List<Performer> findPerformerByName(String name, int offset, int limit) throws SQLException {
	    List<Performer> performerList = new ArrayList<Performer>();
	    QueryBuilder<Performer, Integer> queryBuilder = performerDao.queryBuilder();
	    if(offset > 0) queryBuilder.offset(Long.valueOf(offset));
	    if(limit > 0) queryBuilder.limit(Long.valueOf(limit));
	    if(name.length() > 0) queryBuilder.where().like("name", "%"+name+"%");
	    performerList = performerDao.query(queryBuilder.prepare());

	    return performerList;
	}
}
