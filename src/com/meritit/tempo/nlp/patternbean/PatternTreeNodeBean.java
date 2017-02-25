package com.meritit.tempo.nlp.patternbean;

import com.meritit.tempo.nlp.util.BaseDataDefine;


public class PatternTreeNodeBean {
	String exit;	//trie树节点的出口（指向匹配成功的相关结果）
	int segPos;		//该节点对应的hash表段首下标
	
	
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
