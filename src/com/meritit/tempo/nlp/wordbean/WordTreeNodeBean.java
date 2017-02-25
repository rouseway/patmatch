package com.meritit.tempo.nlp.wordbean;

import com.meritit.tempo.nlp.util.BaseDataDefine;


public class WordTreeNodeBean {
	int dictPos;	//trie树节点的出口（指向词典）
	int segPos;		//该节点对应的hash表段首下标
	
	
	public WordTreeNodeBean() {
		this.dictPos = BaseDataDefine.WORD_NO_DATA;
		this.segPos = BaseDataDefine.WORD_NO_DATA;
	}
	
	public int getDictPos() {
		return dictPos;
	}
	public void setDictPos(int dictPos) {
		this.dictPos = dictPos;
	}
	
	public int getSegPos() {
		return segPos;
	}
	public void setSegPos(int segPos) {
		this.segPos = segPos;
	}
	
}
