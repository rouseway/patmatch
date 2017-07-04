package com.github.rouseway.nlp.patmatch.wordbean;


public class WordDictNodeBean {
	String word;				//��
	WordDictAttrBean property;	//�ʶ�Ӧ����������
	
	
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
 