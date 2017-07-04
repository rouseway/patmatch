package com.github.rouseway.nlp.patmatch.patternbean;


public class PatternFuncNodeBean {
	String name;	//���ܺ���������
	boolean flag;	//���������Ƿ�ʵ��
	
	
	public PatternFuncNodeBean() {
		this.flag = false;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	
	public int handler(char[] input, int beginPos) {
		return 0;
	}
	
}
