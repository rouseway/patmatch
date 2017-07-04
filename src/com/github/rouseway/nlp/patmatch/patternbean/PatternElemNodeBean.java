package com.github.rouseway.nlp.patmatch.patternbean;

import com.github.rouseway.nlp.patmatch.util.BaseDataDefine;
import com.github.rouseway.nlp.patmatch.util.ProcessUtils;


public class PatternElemNodeBean {
	char[] meta;	//ģ��Ԫ�ַ�����λ�ʵ�<D:rmb>���̶��ʡ�ͨ���<W:1-2>��ע�ắ��<F:float>
	char[] attr;	//���ԣ�ƥ��ɹ�����Եõ����úõ�������Ϣ��
	
	
	public PatternElemNodeBean() {
		this.meta = new char[BaseDataDefine.MAX_DEST_STR_LEN];
		this.attr = new char[BaseDataDefine.MAX_PAT_ATTR_LEN];
	}
	
	
	public char getMeta(int idx) {
		return meta[idx];
	}
	public void setMeta(int idx, char ch) {
		this.meta[idx] = ch;
	}
	public int getMetaLen() {
		int i = 0;
		while (this.meta[i] != 0) {
			++i;
		}
		return i;
	}
	public String getMetaStr() {
		return ProcessUtils.charsToString(this.meta);
	}
	public void setMetaStr(String metaStr) {
		char[] temp = metaStr.toCharArray();
		for (int i = 0; i < temp.length; ++i)
			this.meta[i] = temp[i];
		this.meta[temp.length] = '\0';
	}
	
	
	public char getAttr(int idx) {
		return attr[idx];
	}
	public void setAttr(int idx, char ch) {
		this.attr[idx] = ch;
	}
	public int getAttrLen() {
		int i = 0;
		while (this.attr[i] != 0) {
			++i;
		}
		return i;
	}
	public String getAttrStr() {
		return ProcessUtils.charsToString(this.attr);
	}
	public void setAttrStr(String attrStr) {
		char[] temp = attrStr.toCharArray();
		for (int i = 0; i < temp.length; ++i)
			this.attr[i] = temp[i];
		this.attr[temp.length] = '\0';
	}
	
	
}
