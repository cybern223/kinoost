package com.cyber.kinoost.db.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@JsonIgnoreProperties(ignoreUnknown = true)
@DatabaseTable(tableName = "user")
public class User {
	
	public final static String ID_FIELD_NAME = "id";
	
	@DatabaseField(id = true, columnName = ID_FIELD_NAME)
	int id;
	@DatabaseField
	String name;
	@JsonIgnore
	String token;
	
	public User() {
		this.id = 0;
	}
	
	public User(int id, String name) {
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
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}	
}
