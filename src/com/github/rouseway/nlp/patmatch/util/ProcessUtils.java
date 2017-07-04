package com.github.rouseway.nlp.patmatch.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class ProcessUtils {
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	public static List<String> readFileLines(String path, String code) {
		List<String> list = new ArrayList<String>();
		BufferedReader br = null; 
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), code));
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	
	/**
	 * @brief: ��char����ת��Ϊ�ַ���String
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	public static String charsToString(char[] characters) {
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < characters.length && characters[i] != 0; ++i)
			bf.append(characters[i]);
		return bf.toString();
	}
	
}
