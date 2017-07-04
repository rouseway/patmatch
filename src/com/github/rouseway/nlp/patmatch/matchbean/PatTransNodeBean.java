package com.github.rouseway.nlp.patmatch.matchbean;

public class PatTransNodeBean {
	int len;		//��¼��ǰƥ��ĳ���
	int patternID;	//��ǰǰ׺��ʶ����λ�ʵ䡢�̶���ֱ��ʹ���±�
	String proper;	//ƥ��ʵ�����
	
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
