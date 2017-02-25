package com.meritit.tempo.nlp.patternbean;

import com.meritit.tempo.nlp.util.BaseDataDefine;


public class PatternHashNodeBean {
	int patternID;		//Ԫ�ַ���ģ���б��е�ID
	int next;			//ָ����һ��hash�ڵ�ı�ţ����׽ڵ�Ϊ�öΰ�����hash�ڵ������
	int father;			//ָ���Ӧtrie���ڵ���±�
	
	
	public PatternHashNodeBean() {
		this.patternID = BaseDataDefine.PAT_NO_DATA;
		this.next = BaseDataDefine.PAT_NO_DATA;
		this.father = BaseDataDefine.PAT_NO_DATA;
	}
	
	public int getPatternID() {
		return patternID;
	}
	public void setPatternID(int patternID) {
		this.patternID = patternID;
	}
	
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	
	public int getFather() {
		return father;
	}
	public void setFather(int father) {
		this.father = father;
	}
	
}
