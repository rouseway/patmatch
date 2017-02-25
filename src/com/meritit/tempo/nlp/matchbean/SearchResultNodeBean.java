package com.meritit.tempo.nlp.matchbean;

import com.meritit.tempo.nlp.util.BaseDataDefine;
import com.meritit.tempo.nlp.util.ProcessUtils;


public class SearchResultNodeBean {
	char[] patternStr;				//模板原文
	char[] patternExtra;			//模板对应的属性信息
	int termsNum;					//匹配的槽位个数
	
	SlotMatchInfoBean[] termRes;	//每个槽位对应的信息
	int matchLen;					//实际匹配的长度
	
	
	public SearchResultNodeBean() {
		this.patternStr = new char[BaseDataDefine.MAX_DEST_STR_LEN];
		this.patternExtra = new char[BaseDataDefine.MAX_PAT_ATTR_LEN];
		this.termRes = new SlotMatchInfoBean[BaseDataDefine.RESULT_TEMRS_SIZE];
		for (int i = 0; i < BaseDataDefine.RESULT_TEMRS_SIZE; ++i) {
			this.termRes[i] = new SlotMatchInfoBean();
		}
	}
	
	public void setPatternStr(int idx, char ch) {
		this.patternStr[idx] = ch;
	}
	public int getPatternStrLen() {
		return ProcessUtils.charsToString(this.patternStr).length();
	}
	
	public void setPatternExtra(int idx, char ch) {
		this.patternExtra[idx] = ch;
	}
	public String getPatternExtra() {
		return ProcessUtils.charsToString(this.patternExtra);
	}
	public void setPatternExtraStr(String extra) {
		char[] temp = extra.toCharArray();
		for (int i = 0; i < temp.length; ++i) 
			this.patternExtra[i] = temp[i];
	}
	
	public SlotMatchInfoBean getSlotMatchInfo(int idx) {
		return this.termRes[idx];
	}
	public void setSlotMatchInfo(SlotMatchInfoBean info, int idx) {
		this.termRes[idx] = info;
	}

	public int getTermsNum() {
		return termsNum;
	}
	public void setTermsNum(int termsNum) {
		this.termsNum = termsNum;
	}

	public int getMatchLen() {
		return matchLen;
	}
	public void setMatchLen(int matchLen) {
		this.matchLen = matchLen;
	}
	
}
