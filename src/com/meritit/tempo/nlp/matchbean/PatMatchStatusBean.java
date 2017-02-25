package com.meritit.tempo.nlp.matchbean;


import com.meritit.tempo.nlp.util.BaseDataDefine;


public class PatMatchStatusBean {
	char[] characters;		//目标文本字符数组形式
	int strLen;				//待匹配目标文本的长度
	int command;			//搜索控制策略：默认-；单一-；全匹配-；忽略-
	int strPos;				//递归搜索到目标文本的位置
	int current;			//搜索时遍历的当前树节点的位置
	int resultSize;			//允许匹配结果的最大数目
	int resultNum;			//实际匹配结果的数目
	int termNum;			//匹配词的个数
	
	int[] patternIDResult;	//模板匹配的顺序
	String[] wordExtra;		//槽位词典的属性信息
	int[] pos;				//匹配词位置
	int[] len;				//匹配词长度
	
	
	public PatMatchStatusBean(String destStr, int command, int resultSize) {
		this.characters = destStr.toCharArray();
		this.strLen = destStr.length();
		this.command = command;
		this.strPos = 0;
		this.current = 0;
		this.resultSize = resultSize;
		this.resultNum = 0;
		this.termNum = 0;
		
		this.patternIDResult = new int[BaseDataDefine.RESULT_TEMRS_SIZE];
		this.wordExtra = new String [BaseDataDefine.RESULT_TEMRS_SIZE];
		this.pos = new int[BaseDataDefine.RESULT_TEMRS_SIZE];
		this.len = new int[BaseDataDefine.RESULT_TEMRS_SIZE];
	}
	
	
	public char[] getCharacters() {
		return this.characters;
	}

	public int getPatternID(int idx) {
		return this.patternIDResult[idx];
	}
	public void setPatternID(int idx, int id) {
		this.patternIDResult[idx] = id;
	}
	
	public int getPos(int idx) {
		return this.pos[idx];
	}
	public void setPos(int idx, int id) {
		this.pos[idx] = id;
	}
	
	public int getLen(int idx) {
		return this.len[idx];
	}
	public void setLen(int idx, int id) {
		this.len[idx] = id;
	}
	
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}

	public int getStrPos() {
		return strPos;
	}
	public void setStrPos(int strPos) {
		this.strPos = strPos;
	}
	public void addStrPos(int delta) {
		this.strPos += delta;
	}
	
	public int getStrLen() {
		return strLen;
	}

	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		this.current = current;
	}

	public int getResultSize() {
		return resultSize;
	}
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}

	public int getResultNum() {
		return resultNum;
	}
	public void setResultNum(int resultNum) {
		this.resultNum = resultNum;
	}
	public void upResultNum() {
		this.resultNum += 1;
	}
	
	public int getTermNum() {
		return termNum;
	}
	public void setTermNum(int termNum) {
		this.termNum = termNum;
	}
	public void addTermNum(int delta) {
		this.termNum += delta;
	}

	public String getWordExtra(int idx) {
		return wordExtra[idx];
	}
	public void setWordExtra(int idx, String extra) {
		this.wordExtra[idx] = extra;
	}
	
}
