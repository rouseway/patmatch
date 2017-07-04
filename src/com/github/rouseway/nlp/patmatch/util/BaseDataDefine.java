package com.github.rouseway.nlp.patmatch.util;


public class BaseDataDefine {
	
	public static final int WORD_NO_DATA = -1;
	public static final int WORD_HASH_SEG_LEN = 2;
	
	public static final int MAX_WORD_HASH_SIZE = 1000000;
	public static final int MAX_WORD_NODE_SIZE = 50000;
	public static final int MAX_WORD_LIST_SIZE = 50000;
	
	
	public static final int PAT_NO_DATA = -1;
	public static final int PAT_HASH_SEG_LEN = 2;
	
	public static final int MAX_PAT_HASH_SIZE = 2000;
	public static final int MAX_PAT_NODE_SIZE = 2000;
	public static final int MAX_PAT_LIST_SIZE = 1000;
	public static final int MAX_PAT_FUNC_NUM = 20;
	public static final int MAX_PAT_WILD_NUM = 20;
	public static final int MAX_PAT_MULTI_PROP = 15;
	
	
	public static final int MAX_DEST_STR_LEN = 2048;
	public static final int MAX_PAT_ATTR_LEN = 512;
	public static final int MAX_WORDS_LEN = 256;
	
	public static final int IGN_WORD_PROP = -3;
	public static final int NODE_HAVE_FUNC = -2;
	public static final int NODE_HAVE_WILDCARD = -3;
	public static final int NODE_HAVE_BOTH = -4;
	
	
	public static final int RESULT_TEMRS_SIZE = 30;
	
	public static final int COMMAND_ONE_RESULT = 0x1;
	public static final int COMMAND_WANQUAN_PIPEI = 0x2;
	public static final int COMMAND_USE_IGNORETERM = 0x4;
	public static final int COMMAND_DEFAULT = 0x6;
	
	
	public static final int SLOT_TYPE_GEN = 1;
	public static final int SLOT_TYPE_FIX = 2;
	public static final int SLOT_TYPE_WILD = 3;
	public static final int SLOT_TYPE_FUNC = 4;
	public static final int SLOT_TYPE_IGNO = 5;
	
	
	public static final int ENGINE_MAX_MATCH_NUM = 5;
	
}
