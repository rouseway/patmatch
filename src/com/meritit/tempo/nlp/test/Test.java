package com.meritit.tempo.nlp.test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.meritit.tempo.nlp.engine.InformationExtractor;
import com.meritit.tempo.nlp.engine.PatternMatch;
import com.meritit.tempo.nlp.matchbean.SearchResultNodeBean;
import com.meritit.tempo.nlp.util.BaseDataDefine;
import com.meritit.tempo.nlp.util.ProcessUtils;

public class Test {
	public static void main(String[] args) {
		
		List<String> slotList = ProcessUtils.readFileLines("./dict/slot.txt", "gb18030");
		List<String> patList = ProcessUtils.readFileLines("./dict/pat.txt", "gb18030");
		List<String> ignoList = ProcessUtils.readFileLines("./dict/ign.txt", "gb18030");
		//List<String> destStrList = ProcessUtils.readFileLines("./dict/dest_str.txt", "gb18030");
		List<String> destStrList = new ArrayList<String>();
		String destStr = "已在支付宝的2016/01/03成功还款10000元";
		destStrList.add(destStr);
		
		
		Map<String, String> attrMap = InformationExtractor.initialEngine(slotList, patList, ignoList);
		Iterator<Entry<String, String>> ii = attrMap.entrySet().iterator();
		while (ii.hasNext()) {
			Map.Entry<String, String> entry = ii.next();
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
		System.out.println("----------------------------------------------------------------");
		for (int i = 0; i < destStrList.size(); ++i) {
			Map<String, Object> result = InformationExtractor.informationExtract(destStrList.get(i));
			Iterator<Entry<String, Object>> it = result.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, Object> entry = it.next();
				System.out.print(entry.getKey()+": "+entry.getValue()+"\t");
			}
			System.out.println("\n---------------------------- "+i+" ---------------------------------");
		}
		
		/*
		PatternMatch patmatch = new PatternMatch();
		int creatFlag = patmatch.treesCreate(slotList, patList, ignoList);
		System.out.println("finished. "+String.valueOf(creatFlag));
		
		//String destStr = "已在2016年11月14日成功还款1000元";
	
		
		SearchResultNodeBean[] result = new SearchResultNodeBean[BaseDataDefine.ENGINE_MAX_MATCH_NUM];
		for (int i = 0; i < result.length; ++i)
			result[i] = new SearchResultNodeBean();
		
		int retNum = patmatch.patMatch(destStr, 0, 0, result, BaseDataDefine.COMMAND_DEFAULT, BaseDataDefine.ENGINE_MAX_MATCH_NUM);
		System.out.println(retNum);
		for (int i = 0; i < retNum; ++i) {
			int cnt = result[i].getTermsNum();
			System.out.println("TERMSNUM: "+String.valueOf(cnt));
			for (int j = 0; j < cnt; ++j) {
				System.out.println(result[i].getSlotMatchInfo(j).getStr());
			}
		}
		*/
	}
}
