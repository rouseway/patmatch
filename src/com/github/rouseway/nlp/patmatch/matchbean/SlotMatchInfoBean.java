package com.github.rouseway.nlp.patmatch.matchbean;

public class SlotMatchInfoBean {
	int slot;		//��λ���ͣ���λ�ʡ��̶��ʡ�ע�ắ����ͨ������ɺ��Դ� ��SLOT_TYPE_GEN ...��
	int begin;		//��ƥ��Ĳ�λ��Ӧ��Ŀ�괮�е���ʼλ��
	int len;		//�ò�λƥ��ɹ����ַ�������
	String str;		//��λ����������ַ�������λ��ǡ��̶��ʡ�ע�ắ������ͨ��������ɺ��Դ�
	String extra;	//������Ϣ����֧�ֻ�ȡ��λ���ڲ�λ�ʵ������õ���Ϣ��
	
	
	public SlotMatchInfoBean() {
		this.slot = -1;
		this.begin = -1;
		this.len = 0;
		this.str = "";
		this.extra = "";
	}
	
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	
	public int getBegin() {
		return begin;
	}
	public void setBegin(int begin) {
		this.begin = begin;
	}
	
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	
	public String getExtra() {
		return extra;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}
	
	public void mergeAttr(String attr) {
		this.str = this.str + "\t" + attr;
	}
	
}
