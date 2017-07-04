package com.github.rouseway.nlp.patmatch.wordbean;

import com.github.rouseway.nlp.patmatch.util.BaseDataDefine;


public class WordTreeNodeBean {
	int dictPos;	//trie���ڵ�ĳ��ڣ�ָ��ʵ䣩
	int segPos;		//�ýڵ��Ӧ��hash������±�
	
	
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
