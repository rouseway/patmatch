package com.meritit.tempo.nlp.engine;

import com.meritit.tempo.nlp.patternbean.PatternFuncNodeBean;


public class RegisterFuncDateRecog extends PatternFuncNodeBean{
	
	public RegisterFuncDateRecog(String name) {
		super.setName(name);
		super.setFlag(true);
	}
	
	
	/**
	 * @brief: 重载注册函数，实现标准日期格式的识别
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	@Override
	public int handler(char[] input, int beginPos) {
		int len = input.length, yearPos = 0, monthPos = 0;
		boolean year = false, month = false, day = false, number = false;
		for (int i = beginPos; i < len; ++i) {
			if (year == false && (input[i] == '年' || input[i] == '-' || input[i] == '/')) {
				year = true;
				yearPos = i;
				if (yearPos - beginPos > 4)
					return len;
			} else if (input[i] == '月' || input[i] == '-' || input[i] == '/') {
				month = true;
				monthPos = i;
				if (yearPos != 0 && (i - yearPos > 3 || i - yearPos < 2))
					return len;
			} else if (day == false && (input[i] == '日' || input[i] == '号')) {
				day = true;
				if (monthPos != 0 && (i - monthPos > 3 || i - monthPos < 2))
					return len;
			} else if (input[i] >= '0' && input[i] <= '9') {
				number = true;
			} else {
				if (number == true && ((year == true && month == true && i-monthPos > 1 && i-monthPos < 4) 
						|| (month == true && day == true)))
					return i-beginPos;
				else
					return len;
			}
		}
		return len;
	}

}
