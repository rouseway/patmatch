package com.meritit.tempo.nlp.patternbean;


public class PatternWildcNodeBean {
	String name;	//通配符名称：[W:1-2]
	int lower;		//通配字符串（unicode）字数下限
	int upper;		//通配字符串（unicode）字数上限
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLower() {
		return lower;
	}
	public void setLower(int lower) {
		this.lower = lower;
	}
	
	public int getUpper() {
		return upper;
	}
	public void setUpper(int upper) {
		this.upper = upper;
	}
	
}
