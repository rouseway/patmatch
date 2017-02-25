package com.meritit.tempo.nlp.wordbean;

import com.meritit.tempo.nlp.util.BaseDataDefine;


public class WordHashNodeBean {
	char character;		//当前需要匹配的字符（即转移条件）
	int next;			//指向下一个hash节点的编号
	int father;			//指向对应的Trie树节点的下标
	
	
	public WordHashNodeBean() {
		this.character = '\0';
		this.next = BaseDataDefine.WORD_NO_DATA;
		this.father = BaseDataDefine.WORD_NO_DATA;
	}
	
	public char getCharacter() {
		return character;
	}
	public void setCharacter(char character) {
		this.character = character;
	}
	
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	
	public int getFather() {
		return father;
	}
	public void setFather(int father) {
		this.father = father;
	}
}
