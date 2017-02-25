package com.meritit.tempo.nlp.wordbean;


public class WordDictNodeBean {
	String word;				//词
	WordDictAttrBean property;	//词对应的属性链表
	
	
	public WordDictNodeBean() {
		this.word = "";
		this.property = null;
	}
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	public WordDictAttrBean getProperty() {
		return property;
	}
	public void setProperty(WordDictAttrBean property) {
		this.property = property;
	}
	
}
 