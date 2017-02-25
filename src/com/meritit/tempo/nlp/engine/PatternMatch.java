package com.meritit.tempo.nlp.engine;

import java.util.List;
import org.apache.log4j.Logger;

import com.meritit.tempo.nlp.matchbean.PatMatchStatusBean;
import com.meritit.tempo.nlp.matchbean.SearchResultNodeBean;
import com.meritit.tempo.nlp.trie.PatternTrie;
import com.meritit.tempo.nlp.util.BaseDataDefine;

public class PatternMatch {
	private static Logger logger = Logger.getLogger(PatternMatch.class.getName());
	
	PatternTrie pTree = new PatternTrie();	//引擎总入口  —— 模板树
	
	
	/**
	 * @brief: 将输入的槽位词保存至词典trie树结构中
	 * @param: 
	 * 
	 * @return: int : 0=成功，-1=失败
	 * 
	 **/
	int inputWord(List<String> slotList) {
		int patternID = 0;
		for (String word : slotList) {
			if (word.length() >= BaseDataDefine.MAX_WORDS_LEN) {
				logger.error("length of ["+word+"] in slot-word dict is more than 256-bytes!");
				return -1;
			}
			if (word.length() == 0 || word.trim().length() == 0)
				continue;
			
			String[] lst = word.split("\t");
			String proper = "";
			if (lst.length == 2) {
				word = lst[0];
				proper = lst[1];
			}
			if (word.length() > 2 && word.charAt(0) == '<' && 
					word.charAt(1) == 'D' && word.charAt(2) == ':') { //槽位类型标识
				patternID = pTree.getPatternID(word, proper,"wO".toCharArray());
			} else {
				if (0 != pTree.insertWordTrie(patternID, word, proper)) {
					logger.error("failed to insert slot-word ["+word+"] into word-trie.");
					return -1;
				}
			}
		}
		pTree.compressWordHash();
		return 0;
	}
	
	
	/**
	 * @brief: 将输入的可忽略词保存至词典trie树结构中
	 * @param: 
	 * 
	 * @return: int : 0=成功；-1=失败
	 * 
	 **/
	int inputIgn(List<String> ignoList) {		
		for (String word : ignoList) {
			if (word.length() >= BaseDataDefine.MAX_WORDS_LEN) {
				logger.error("length of ["+word+"] in ignore-word dict is more than 256-bytes!");
				return -1;
			}
			if (word.length() == 0 || word.trim().length() == 0)
				continue;
			if (0 != pTree.insertWordTrie(BaseDataDefine.IGN_WORD_PROP, word, "")) {
				logger.error("failed to insert ignore-word ["+word+"] into word-trie.");
				return -1;
			}
		}
		pTree.compressWordHash();
		return 0;
	}
	
	
	/**
	 * @brief: 将输入的规则模板保存至模板trie树结构中
	 * @param: 
	 * 
	 * @return: int : 0=成功；-1=失败
	 * 
	 **/
	int inputPattern(List<String> patList) {
		for (String pattern : patList) {
			if (pattern.length() >= BaseDataDefine.MAX_DEST_STR_LEN) {
				logger.error("length of ["+pattern+"] in pattern file is more than 1024-bytes!");
				return -1;
			}
			if (pattern.length() == 0 || pattern.trim().length() == 0)
				continue;
			
			String[] lst = pattern.split("\t");
			String proper = "";
			if (lst.length == 2) {
				pattern = lst[0];
				proper = lst[1];
			}
			if (0 != pTree.insertPatternTrie(pattern, proper)) {
				logger.error("failed to insert pattern ["+pattern+"] into pattern-trie.");
				return -1;
			}
		}
		return 0;
	}
	
	
	/**
	 * @brief: 创建词典trie树、模板trie树的对外接口
	 * @param: 
	 * 
	 * @return: int : 0=成功；-1=失败
	 * 
	 **/
	public int treesCreate(List<String> slotList, List<String> patList, List<String> ignoList) {
		logger.info("begin to create pattern-trie and word-trie ...");
		if (0 != pTree.init()) {
			logger.error("failed to initial resources.");
			return -1;
		}
		
		logger.info("success to initial resources.");
		if (0 != inputWord(slotList)) {
			logger.error("failed to create slot-word tree.");
			return -1;
		}
		logger.info("success to create slot word tree.");
		
		if (0 != inputIgn(ignoList)) {
			logger.info("failed to create ignore-word tree.");
			return -1;
		}
		logger.info("success to create ignore-word tree.");
		
		if (0 != inputPattern(patList)) {
			logger.error("failed to create pattern tree.");
			return -1;
		}
		logger.info("success to create pattern tree.");
		
		
		logger.info("register functions into engine.");
		RegisterFuncNumRecog numFunc = new RegisterFuncNumRecog("<F:float>");
		pTree.registerFunc("<F:float>", numFunc);		
		RegisterFuncDateRecog dateFunc = new RegisterFuncDateRecog("<F:date>");
		pTree.registerFunc("<F:date>", dateFunc);
		
		return 0;
	}
	
	
	/**
	 * @brief: 模板匹配的对外调用接口
	 * @param: 
	 * 
	 * @return: int : 匹配得到的结果数目
	 * 
	 **/
	public int patMatch(String input, int beginIndex, int len, SearchResultNodeBean[] result, int commandFlag, int resultSize) {
		String destStr = input.substring(beginIndex, len);
		PatMatchStatusBean status = new PatMatchStatusBean(destStr, commandFlag, resultSize);
		pTree.search(status, result);
		return status.getResultNum();
	}
	
}
