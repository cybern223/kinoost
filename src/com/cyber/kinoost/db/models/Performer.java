package com.cyber.kinoost.db.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "performer")
public class Performer {
	public final static String ID_FIELD_NAME = "id";
	
	@DatabaseField(id = true, columnName = ID_FIELD_NAME)
	int id;
	@DatabaseField(index = true)
	String name;
	
	public Performer() {
	}

	public Performer(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Performer [id=" + id + ", name=" + name + "]";
	}	
}