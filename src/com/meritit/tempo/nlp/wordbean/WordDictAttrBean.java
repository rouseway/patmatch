package com.meritit.tempo.nlp.wordbean;


public class WordDictAttrBean {
	int type;				//词条所属的类型
	String attr;			//词条的属性（在词典中的第二列value值）
	WordDictAttrBean next;	//链表指针
	
	
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
