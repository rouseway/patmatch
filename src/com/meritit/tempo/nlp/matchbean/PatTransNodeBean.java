package com.meritit.tempo.nlp.matchbean;

public class PatTransNodeBean {
	int len;		//记录当前匹配的长度
	int patternID;	//当前前缀标识：槽位词典、固定词直接使用下标
	String proper;	//匹配词的属性
	
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	
	public int getPatternID() {
		return patternID;
	}
	public void setPatternID(int patternID) {
		this.patternID = patternID;
	}
	
	public String getProper() {
		return proper;
	}
	public void setProper(String proper) {
		this.proper = proper;
	}

}
