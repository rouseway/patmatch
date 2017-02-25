package com.meritit.tempo.nlp.patternbean;

import com.meritit.tempo.nlp.util.BaseDataDefine;


public class PatternHashNodeBean {
	int patternID;		//元字符在模板列表中的ID
	int next;			//指向下一个hash节点的编号（段首节点为该段包含的hash节点个数）
	int father;			//指向对应trie树节点的下标
	
	
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
