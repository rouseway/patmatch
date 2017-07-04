package com.github.rouseway.nlp.patmatch.patternbean;

import com.github.rouseway.nlp.patmatch.util.BaseDataDefine;


public class PatternTreeNodeBean {
	String exit;	//trie���ڵ�ĳ��ڣ�ָ��ƥ��ɹ�����ؽ����
	int segPos;		//�ýڵ��Ӧ��hash������±�
	
	
	public PatternTreeNodeBean() {
		this.exit = null;
		this.segPos = BaseDataDefine.PAT_NO_DATA;
	}
	
	public String getExit() {
		return exit;
	}
	public void setExit(String exit) {
		this.exit = exit;
	}
	
	public int getSegPos() {
		return segPos;
	}
	public void setSegPos(int segPos) {
		this.segPos = segPos;
	}
	
}
