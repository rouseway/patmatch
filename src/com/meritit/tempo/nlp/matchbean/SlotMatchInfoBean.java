package com.meritit.tempo.nlp.matchbean;

public class SlotMatchInfoBean {
	int slot;		//槽位类型：槽位词、固定词、注册函数、通配符、可忽略词 （SLOT_TYPE_GEN ...）
	int begin;		//该匹配的槽位对应在目标串中的起始位置
	int len;		//该槽位匹配成功的字符串长度
	String str;		//槽位本身的描述字符串：槽位标记、固定词、注册函数名、通配符名、可忽略词
	String extra;	//额外信息（仅支持获取槽位词在槽位词典中配置的信息）
	
	
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
