package com.github.rouseway.nlp.patmatch.wordbean;


public class WordDictAttrBean {
	int type;				//��������������
	String attr;			//���������ԣ��ڴʵ��еĵڶ���valueֵ��
	WordDictAttrBean next;	//����ָ��
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	
	public WordDictAttrBean getNext() {
		return next;
	}
	public void setNext(WordDictAttrBean next) {
		this.next = next;
	}
}
