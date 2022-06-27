package com.exceldemo.entity;

public class User {

	private Integer uid;
	private String uname;
	private String pwd;

	public User(Integer uid, String uname, String pwd) {
		this.uid = uid;
		this.uname = uname;
		this.pwd = pwd;
	}

	public User() {
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

}