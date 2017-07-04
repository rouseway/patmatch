package com.github.rouseway.nlp.patmatch.wordbean;

import com.github.rouseway.nlp.patmatch.util.BaseDataDefine;


public class WordHashNodeBean {
	char character;		//��ǰ��Ҫƥ����ַ�����ת��������
	int next;			//ָ����һ��hash�ڵ�ı��
	int father;			//ָ���Ӧ��Trie���ڵ���±�
	
	
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
