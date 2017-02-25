package com.meritit.tempo.nlp.engine;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.meritit.tempo.nlp.matchbean.SearchResultNodeBean;
import com.meritit.tempo.nlp.util.BaseDataDefine;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class InformationExtractor {
	private static Logger logger = Logger.getLogger(InformationExtractor.class.getName());
	
	static PatternMatch rulePatternEngine = new PatternMatch();
	
	
	/**
	 * @brief: 合并指定属性下标区间内的匹配槽位作为信息抽取的输出字符串
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	static String mergeValue(String destStr, SearchResultNodeBean maxResult, int leftIndex, int rightIndex) {
		
		if (leftIndex < 0 || (leftIndex == 0 && rightIndex != 0) || leftIndex > rightIndex ||
				leftIndex > maxResult.getTermsNum() || rightIndex > maxResult.getTermsNum())
			return null;
		
		if (leftIndex == 0 && rightIndex == 0) {
			leftIndex = 1;
			rightIndex = maxResult.getTermsNum();
		}
				
		StringBuffer value = new StringBuffer();
		for (int i = leftIndex; i <= rightIndex; ++i) {
			String slotValue = destStr.substring(maxResult.getSlotMatchInfo(i-1).getBegin(), 
					maxResult.getSlotMatchInfo(i-1).getBegin()+maxResult.getSlotMatchInfo(i-1).getLen());
			value.append(slotValue);
		}
		return value.toString();
	}
	
	
	/**
	 * @brief: 转换属性下标中的整数字符串为int类型
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	static int formatInt(String index) {
		int idx = -1;
		try {
			idx = Integer.parseInt(index);
		} catch (NumberFormatException e) {}
		return idx;
	}
	
	
	/**
	 * @brief: 转换数字字符串为Double类型
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	static double formatDouble(String slotValue) {
		double num = 0;
		try {
			num = Double.parseDouble(slotValue.replaceAll(",", ""));
		} catch (NumberFormatException e) {
			logger.error("can't convert ["+slotValue+"] to float number.");
		}
		return num;
	}
	
	
	/**
	 * @brief: 转换时间字符串为Date类型
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	static Date formatDate(String slotValue) {
		Date date = null;
		boolean convert = false;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy/MM/dd");
		try {
			date = sdf1.parse(slotValue);
			convert = true;
		} catch (Exception e) {}
		try {
			date = sdf2.parse(slotValue);
			convert = true;
		} catch (Exception e) {}
		try {
			date = sdf3.parse(slotValue);
			convert = true;
		} catch (Exception e) {}
		if (convert == false) {
			logger.error("can't convert ["+slotValue+"] to date type.");
		}
		return date;
	}
	
	
	/**
	 * @brief: 过滤匹配结果中的可忽略词信息
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	static void filterIgoreWords(SearchResultNodeBean maxResult) {
		int termNum = 0;
		for (int i = 0; i < maxResult.getTermsNum(); ++i) {
			if (maxResult.getSlotMatchInfo(i).getSlot() != BaseDataDefine.SLOT_TYPE_IGNO) {
				maxResult.setSlotMatchInfo(maxResult.getSlotMatchInfo(i), termNum);
				termNum += 1;
			}
		}
		maxResult.setTermsNum(termNum);
	}
	
	
	/**
	 * @brief: 集合模板属性对匹配结果进行解析
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	static void parsePatResult(String destStr, SearchResultNodeBean maxResult, Map<String, Object> patResult) {
		filterIgoreWords(maxResult); //过滤可忽略词，以确保属性下标的一致性
		String patAttr = maxResult.getPatternExtra();
		String[] attrList = patAttr.split(","); 
		for (String attr : attrList) {
			String slotValue = "";  //最终的槽位取值
			
			String[] attrType = attr.split(":");
			String[] attrIndex = attrType[0].split("_");
			String attrName = attrIndex[0];
			if (attrIndex.length == 2) { //属性带有下标索引
				String[] indexScope = attrIndex[1].split("-");
				int leftIndex = formatInt(indexScope[0]);
				if (leftIndex == -1) {
					logger.error("pattern attributes of ["+patAttr+"] is in wrong format!");
					continue;
				}
				int rightIndex = leftIndex;
				if (indexScope.length == 2) {
					rightIndex = formatInt(indexScope[1]);
					if (rightIndex == -1) {
						logger.error("pattern attributes of ["+patAttr+"] is in wrong format!");
						continue;
					}
				}
				//按下标索引合并结果
				slotValue = mergeValue(destStr, maxResult, leftIndex, rightIndex);
				if (slotValue == null) {
					logger.error("pattern attributes of ["+patAttr+"] is in wrong format!");
					continue;
				}
				
			} else { //无下标索引
				if (attrType.length == 2) {
					if (attrType[1].equals("true")) { //bool属性命中
						patResult.put(attrName, Boolean.parseBoolean("true"));
					} else if (attrType[1].equals("false")) {
						patResult.put(attrName, Boolean.parseBoolean("false"));
					} else {
						logger.error("pattern attributes of ["+patAttr+"] is in wrong format!"); //属性格式错误
					}
				}
				else {
					logger.error("pattern attributes of ["+patAttr+"] is in wrong format!"); //属性格式错误
				}
				
			}
			
			if (attrType.length == 2) { //属性带有类别标记，特殊处理float,date
				String type = attrType[1];
				if (type.equals("double")) {
					patResult.put(attrName, formatDouble(slotValue));
				} else if (type.equals("date")) {
					patResult.put(attrName, new Timestamp(formatDate(slotValue).getTime()));
				} else if (type.equals("string")) {
					patResult.put(attrName, slotValue);
				} else if (!type.equals("true") && !type.equals("false")) {
					logger.error("pattern attributes of ["+patAttr+"] is in wrong format!"); //属性格式错误
				}
			} else {
				patResult.put(attrName, slotValue); //无属性类别标记，默认按string处理
			}
		}
	}
	
	
	/**
	 * @brief: 初始化引擎，创建模板树、词典树
	 * @param: List<String> slotList : 读取槽位词典文件得到的每一行内容
	 *         List<String> patternList : 读取模板文件得到的每一行内容
	 * 		   List<String> ignoreList : 读取可忽略词文件得到的每一行内容
	 * @return: Map<String, String> : 
	 * 
	 **/
	public static Map<String, String> initialEngine(List<String> slotList, List<String> patternList, 
			List<String> ignoreList) {
		Map<String, String> attrMap = new HashMap<String, String>();
		for (String pattern : patternList) {
			String[] patAttrs = pattern.split("\t");
			if (patAttrs.length == 2) {
				String[] attrList = patAttrs[1].split(",");
				for (String attr : attrList) {
					String[] attrType = attr.split(":");
					String type = "string";
					if (attrType.length == 2) {
						type = attrType[1];
					}
					String[] attrIndex = attrType[0].split("_");
					String feature = attrIndex[0];
					
					if (attrMap.containsKey(feature)) {
						if (!attrMap.get(feature).equals(type)) {
							logger.error("the attribute of ["+feature+"] has different types!");
							return null;
						}
					} else {
						attrMap.put(feature, type);
					}
				}
			}
		}
		if (0 != rulePatternEngine.treesCreate(slotList, patternList, ignoreList)) {
			return null;
		} else {
			return attrMap;
		}
	}
	
	
	/**
	 * @brief: 对规则引擎进行信息抽取封装（开启可忽略词匹配）
	 * @param: String input : 待分析的文本字符串
	 * 
	 * @return: Map<String, Object> : 信息抽取结果的key-value对（字段：字段值）
	 * 
	 **/
	public static Map<String, Object> informationExtract(String input) {
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		int inputLen = input.length();
		if (inputLen > BaseDataDefine.MAX_DEST_STR_LEN) {
			logger.error("input string ["+input+"] is more than 2048-bytes!");
			return result;
		}
		//初始化存储匹配结果的数组
		SearchResultNodeBean[] patResult = new SearchResultNodeBean[BaseDataDefine.ENGINE_MAX_MATCH_NUM];
		for (int i = 0; i < patResult.length; ++i)
			patResult[i] = new SearchResultNodeBean();
		
		int beginIndex = 0;
		while (beginIndex < inputLen) {
			int retNum = rulePatternEngine.patMatch(input, beginIndex, inputLen, patResult, BaseDataDefine.COMMAND_DEFAULT, 
					BaseDataDefine.ENGINE_MAX_MATCH_NUM);
			if (retNum > 0) {
				int maxIndex = 0, maxNum = -1;
				for (int i = 0; i < retNum; ++i) {
					if (patResult[i].getTermsNum() > maxNum) {
						maxNum = patResult[i].getTermsNum();
						maxIndex = i;
					}
				}
				int offset = 0;
				for (int i = 0; i < patResult[maxIndex].getTermsNum(); ++i) {
					offset += patResult[maxIndex].getSlotMatchInfo(i).getLen();
				}
				parsePatResult(input.substring(beginIndex, inputLen), patResult[maxIndex], result);
				beginIndex += offset;
			} else {
				beginIndex++;
			}
		}
		return result;
	}

}
