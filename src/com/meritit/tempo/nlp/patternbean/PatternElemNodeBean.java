package com.meritit.tempo.nlp.patternbean;

import com.meritit.tempo.nlp.util.BaseDataDefine;
import com.meritit.tempo.nlp.util.ProcessUtils;


public class PatternElemNodeBean {
	char[] meta;	//模板元字符：槽位词典<D:rmb>、固定词、通配符<W:1-2>、注册函数<F:float>
	char[] attr;	//属性（匹配成功后可以得到配置好的属性信息）
	
	
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
