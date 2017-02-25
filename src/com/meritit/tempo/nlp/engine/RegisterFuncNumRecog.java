package com.meritit.tempo.nlp.engine;

import com.meritit.tempo.nlp.patternbean.PatternFuncNodeBean;


public class RegisterFuncNumRecog extends PatternFuncNodeBean{
	
	public RegisterFuncNumRecog(String name) {
		super.setName(name);
		super.setFlag(true);
	}
	
	
	/**
	 * @brief: ����ע�ắ����ʵ��ʵ�����ֵ�ʶ��
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	@Override
	public int handler(char[] input, int beginPos) {
		int len = input.length;
		int pointNum = 0;
		for (int i = beginPos; i < len; ++i) {
			if (input[i] != '.' && input[i] != ',' && (input[i] > '9' || input[i] < '0') && pointNum < 2)
				return i-beginPos;
			else if (input[i] == '.')
				pointNum += 1;
		}
		return len;
	}
}
