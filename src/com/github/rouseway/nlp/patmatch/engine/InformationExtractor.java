package com.github.rouseway.nlp.patmatch.engine;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.github.rouseway.nlp.patmatch.matchbean.SearchResultNodeBean;
import com.github.rouseway.nlp.patmatch.util.BaseDataDefine;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class InformationExtractor {
	private static Logger logger = Logger.getLogger(InformationExtractor.class.getName());
	
	static PatternMatch rulePatternEngine = new PatternMatch();
	
	
	/**
	 * @brief: �ϲ�ָ�������±������ڵ�ƥ���λ��Ϊ��Ϣ��ȡ������ַ���
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
	 * @brief: ת�������±��е������ַ���Ϊint����
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
	 * @brief: ת�������ַ���ΪDouble����
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
	 * @brief: ת��ʱ���ַ���ΪDate����
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	static Date formatDate(String slotValue) {
		Date date = null;
		boolean convert = false;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy��MM��dd��");
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
	 * @brief: ����ƥ�����еĿɺ��Դ���Ϣ
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
	 * @brief: ����ģ�����Զ�ƥ�������н���
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	static void parsePatResult(String destStr, SearchResultNodeBean maxResult, Map<String, Object> patResult) {
		filterIgoreWords(maxResult); //���˿ɺ��Դʣ���ȷ�������±��һ����
		String patAttr = maxResult.getPatternExtra();
		String[] attrList = patAttr.split(","); 
		for (String attr : attrList) {
			String slotValue = "";  //���յĲ�λȡֵ
			
			String[] attrType = attr.split(":");
			String[] attrIndex = attrType[0].split("_");
			String attrName = attrIndex[0];
			if (attrIndex.length == 2) { //���Դ����±�����
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
				//���±������ϲ����
				slotValue = mergeValue(destStr, maxResult, leftIndex, rightIndex);
				if (slotValue == null) {
					logger.error("pattern attributes of ["+patAttr+"] is in wrong format!");
					continue;
				}
				
			} else { //���±�����
				if (attrType.length == 2) {
					if (attrType[1].equals("true")) { //bool��������
						patResult.put(attrName, Boolean.parseBoolean("true"));
					} else if (attrType[1].equals("false")) {
						patResult.put(attrName, Boolean.parseBoolean("false"));
					} else {
						logger.error("pattern attributes of ["+patAttr+"] is in wrong format!"); //���Ը�ʽ����
					}
				}
				else {
					logger.error("pattern attributes of ["+patAttr+"] is in wrong format!"); //���Ը�ʽ����
				}
				
			}
			
			if (attrType.length == 2) { //���Դ�������ǣ����⴦��float,date
				String type = attrType[1];
				if (type.equals("double")) {
					patResult.put(attrName, formatDouble(slotValue));
				} else if (type.equals("date")) {
					patResult.put(attrName, new Timestamp(formatDate(slotValue).getTime()));
				} else if (type.equals("string")) {
					patResult.put(attrName, slotValue);
				} else if (!type.equals("true") && !type.equals("false")) {
					logger.error("pattern attributes of ["+patAttr+"] is in wrong format!"); //���Ը�ʽ����
				}
			} else {
				patResult.put(attrName, slotValue); //����������ǣ�Ĭ�ϰ�string����
			}
		}
	}
	
	
	/**
	 * @brief: ��ʼ�����棬����ģ�������ʵ���
	 * @param: List<String> slotList : ��ȡ��λ�ʵ��ļ��õ���ÿһ������
	 *         List<String> patternList : ��ȡģ���ļ��õ���ÿһ������
	 * 		   List<String> ignoreList : ��ȡ�ɺ��Դ��ļ��õ���ÿһ������
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
	 * @brief: �Թ������������Ϣ��ȡ��װ�������ɺ��Դ�ƥ�䣩
	 * @param: String input : ���������ı��ַ���
	 * 
	 * @return: Map<String, Object> : ��Ϣ��ȡ�����key-value�ԣ��ֶΣ��ֶ�ֵ��
	 * 
	 **/
	public static Map<String, Object> informationExtract(String input) {
		
		HashMap<String, Object> result = new HashMap<String, Object>();
		int inputLen = input.length();
		if (inputLen > BaseDataDefine.MAX_DEST_STR_LEN) {
			logger.error("input string ["+input+"] is more than 2048-bytes!");
			return result;
		}
		//��ʼ���洢ƥ����������
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
