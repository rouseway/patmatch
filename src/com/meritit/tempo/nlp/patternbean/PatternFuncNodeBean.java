package com.meritit.tempo.nlp.patternbean;


public class PatternFuncNodeBean {
	String name;	//功能函数的名称
	boolean flag;	//函数功能是否被实现
	
	
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
