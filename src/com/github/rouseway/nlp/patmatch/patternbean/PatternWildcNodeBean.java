package com.github.rouseway.nlp.patmatch.patternbean;


public class PatternWildcNodeBean {
	String name;	//ͨ������ƣ�[W:1-2]
	int lower;		//ͨ���ַ�����unicode����������
	int upper;		//ͨ���ַ�����unicode����������
	
	
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
