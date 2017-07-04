package com.github.rouseway.nlp.patmatch.matchbean;


import com.github.rouseway.nlp.patmatch.util.BaseDataDefine;


public class PatMatchStatusBean {
	char[] characters;		//Ŀ���ı��ַ�������ʽ
	int strLen;				//��ƥ��Ŀ���ı��ĳ���
	int command;			//�������Ʋ��ԣ�Ĭ��-����һ-��ȫƥ��-������-
	int strPos;				//�ݹ�������Ŀ���ı���λ��
	int current;			//����ʱ�����ĵ�ǰ���ڵ��λ��
	int resultSize;			//����ƥ�����������Ŀ
	int resultNum;			//ʵ��ƥ��������Ŀ
	int termNum;			//ƥ��ʵĸ���
	
	int[] patternIDResult;	//ģ��ƥ���˳��
	String[] wordExtra;		//��λ�ʵ��������Ϣ
	int[] pos;				//ƥ���λ��
	int[] len;				//ƥ��ʳ���
	
	
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
