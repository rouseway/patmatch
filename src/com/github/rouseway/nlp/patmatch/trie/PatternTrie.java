package com.github.rouseway.nlp.patmatch.trie;

import java.util.ArrayList;
import java.util.List;

import com.github.rouseway.nlp.patmatch.matchbean.PatMatchStatusBean;
import com.github.rouseway.nlp.patmatch.matchbean.PatTransNodeBean;
import com.github.rouseway.nlp.patmatch.matchbean.SearchResultNodeBean;
import com.github.rouseway.nlp.patmatch.patternbean.*;
import com.github.rouseway.nlp.patmatch.util.BaseDataDefine;
import com.github.rouseway.nlp.patmatch.util.ProcessUtils;

import org.apache.log4j.Logger;

public class PatternTrie {
	private static Logger logger = Logger.getLogger(PatternTrie.class.getName());
	
	List<PatternHashNodeBean> _hashList = null;		//trie���ڵ��hash��
	List<PatternTreeNodeBean> _nodeList = null;		//�洢trie���ڵ�����Ա�
    List<PatternElemNodeBean> _patternList = null;	//�洢ģ��Ԫ�ַ������Ա�
	
	int _h_max_len = -1;	//hashList����󳤶�
	int _h_seg_len = -1;	//hashList��ÿ���εĳ���
    int _h_now_len = -1;	//hashList��ǰ�ĳ���
    
    int _n_max_len = -1;	//nodeList����󳤶�
    int _n_now_len = -1;	//nodeList�ĵ�ǰ����
    
    int _p_max_len = -1;	//patternList����󳤶�
    
    
    PatternFuncNodeBean[] _funcList = null;		//�洢ע�ắ�������Ա�
    PatternWildcNodeBean[] _wildcList = null;	//�洢ͨ��������Ա�
    
    int _func_num = 0;		//ע�ắ���ĸ���
    int _wildc_num = 0;		//ͨ����ĸ���
    int _other_num = 0;		//��λ�� + �̶��ʵĸ���
    
    
    WordTrie wTree;			//�ʵ�tire�����洢��λ�ʡ��̶��ʡ��ɺ��Դʣ�
    
    
	/**
	 * @brief: ��ʼ��ģ�������ڴ���Դ
	 * @param: void
	 * 
	 * @return: int : 0=�ɹ���1=ʧ��
	 * 
	 **/
    public int init() {
		//��ʼ��_hashList
		_h_max_len = BaseDataDefine.MAX_PAT_HASH_SIZE;
		_h_now_len = 0;
		_hashList = new ArrayList<PatternHashNodeBean>(_h_max_len);
		if (_hashList == null) {
			logger.error("failed to create hashlist for pattern tree.");
			return -1;
		}
		for (int i = 0; i < _h_max_len; ++i) {
			PatternHashNodeBean node = new PatternHashNodeBean();
			_hashList.add(node);
		}
		
		//��ʼ��_nodeList
		_n_max_len = BaseDataDefine.MAX_PAT_NODE_SIZE;
		_n_now_len = 1;
		_nodeList = new ArrayList<PatternTreeNodeBean>(_n_max_len);
		if (_nodeList == null) {
			logger.error("failed to create nodelist for pattern tree.");
			return -1;
		}
		for (int i = 0; i < _n_max_len; ++i) {
			PatternTreeNodeBean node = new PatternTreeNodeBean();
			_nodeList.add(node);
		}
		
		//��ʼ��_patternList
		_p_max_len = BaseDataDefine.MAX_PAT_LIST_SIZE;
		_patternList = new ArrayList<PatternElemNodeBean>(_p_max_len);
		if (_patternList == null) {
			logger.error("failed to create patternlist for pattern tree.");
			return -1;
		}
		for (int i = 0; i < _p_max_len; ++i) {
			PatternElemNodeBean node = new PatternElemNodeBean();
			_patternList.add(node);
		}
		
		//��ʼ��_funcList
		_funcList = new PatternFuncNodeBean[BaseDataDefine.MAX_PAT_FUNC_NUM];
		for (int i = 0; i < BaseDataDefine.MAX_PAT_FUNC_NUM; ++i) {
			_funcList[i] = new PatternFuncNodeBean();
		}
		
		//��ʼ��_wildcList
		_wildcList = new PatternWildcNodeBean[BaseDataDefine.MAX_PAT_WILD_NUM];
		for (int i = 0; i < BaseDataDefine.MAX_PAT_WILD_NUM; ++i) {
			_wildcList[i] = new PatternWildcNodeBean();
		}
		
		//��ʼ���ʵ���wtree
		wTree = new WordTrie();
		if (wTree.init() != 0) {
			logger.error("failed to initialize word tree.");
			return -1;
		}
		
		return 0;
	}
	
	
	/**
	 * @brief: ׷�ӷ���ģ������hash���ڴ���Դ
	 * @param: void
	 * 
	 * @return: int : �µ�hash���ȣ�ԭʼ����2����
	 * 
	 **/
	int reallocHashList() {
		for (int i = 0; i < _h_max_len; ++i) {
			PatternHashNodeBean node = new PatternHashNodeBean();
			_hashList.add(node);
		}
		return _h_max_len*2;
	}
	
	
	/**
	 * @brief: ׷�ӷ���ģ�����Ľڵ���ڴ���Դ
	 * @param: void
	 * 
	 * @return: int : �µ�node���ȣ�ԭʼ����2����
	 * 
	 **/
	int reallocNodeList() {
		for (int i = 0; i < _n_max_len; ++i) {
			PatternTreeNodeBean node = new PatternTreeNodeBean();
			_nodeList.add(node);
		}
		return _n_max_len*2;
	}
	
	
	/**
	 * @brief: ׷�ӷ���ģ�����ڴ���Դ
	 * @param: void
	 * 
	 * @return: int : �µ�pattern���ȣ�ԭʼ����2����
	 * 
	 **/
	int reallocPatternList() {
		for (int i = 0; i < _p_max_len; ++i) {
			PatternElemNodeBean node = new PatternElemNodeBean();
			_patternList.add(node);
		}
		return _p_max_len*2;
	}
	
	
	/**
	 * @brief: ����Ԫ�ַ��ı�ż�������hash��Ķ�λ��
	 * @param: 
	 * 
	 * @return: int : ��ǰԪ�ַ���hash���еĶ�λ���±�
	 * 
	 **/
	int getPos(int segPos, int segLen, int patternID) {
		int tmp = patternID % segLen;
		return segPos + tmp + 1;
	}
	
	
	/**
	 * @brief: ѹ��ģ������hash��ȥ�������м�Ŀհײ���
	 * @param: void
	 * 
	 * @return: void
	 * 
	 **/
	void compressHash() {
		int spare = 0;
		while (spare < _h_max_len && _hashList.get(spare).getFather() != BaseDataDefine.PAT_NO_DATA 
			&& _hashList.get(spare).getNext() != BaseDataDefine.PAT_NO_DATA) {
			spare += (_hashList.get(spare).getNext() + 1);
		}
		if (spare >= _h_max_len)
			return;
		
		int idx = spare;
		while(true) {
			for (; idx < _h_max_len; ++idx) {
				if(_hashList.get(idx).getFather() != BaseDataDefine.PAT_NO_DATA &&
					_hashList.get(idx).getNext() != BaseDataDefine.PAT_NO_DATA) {
					break;
				}
			}
			if (idx >= _h_max_len) {
				break;
			}
			
			int len = _hashList.get(idx).getNext() + 1;
			int father = _hashList.get(idx).getFather();
			if (idx != spare && idx != _h_max_len) {
				for (int j = 0; j < len; ++j) { //��ǰ�ƶ�ʵ��ѹ��
					PatternHashNodeBean node = _hashList.get(idx+j);
					_hashList.get(spare+j).setPatternID(node.getPatternID());
					_hashList.get(spare+j).setFather(node.getFather());
					_hashList.get(spare+j).setNext(node.getNext());
					
					_hashList.get(idx+j).setFather(BaseDataDefine.PAT_NO_DATA);
					_hashList.get(idx+j).setNext(BaseDataDefine.PAT_NO_DATA);
					_hashList.get(idx+j).setPatternID(BaseDataDefine.PAT_NO_DATA);;
				}
				_nodeList.get(father).setSegPos(spare);
			}
			spare += len;
			idx += len;
		}
		_h_now_len = spare;
	}
    
	
	/**
	 * @brief: �ƶ�һ��hash�Σ������¶��в����µ���Ϣ
	 * @param: 
	 * 
	 * @return: boolean : true=�ɹ���false=ʧ��
	 * 
	 **/
	boolean moveSegment(int oldSeg, int oldLen, int newSeg, int newLen, int newID) {
		//�ƶ��ɽڵ�
		for (int i = 0; i < oldLen; ++i) {
			int old = oldSeg + i + 1;
			if (_hashList.get(old).getNext() != BaseDataDefine.PAT_NO_DATA) {
				int oldID = _hashList.get(old).getPatternID();
				int next = _hashList.get(old).getNext();
				int pos = getPos(newSeg, newLen, oldID);
				if (_hashList.get(pos).getNext() != BaseDataDefine.PAT_NO_DATA) {
					for (int j = newSeg+1; j < newSeg+newLen+1; ++j) {
						_hashList.get(j).setFather(BaseDataDefine.PAT_NO_DATA);
						_hashList.get(j).setNext(BaseDataDefine.PAT_NO_DATA);
						_hashList.get(j).setPatternID(BaseDataDefine.PAT_NO_DATA);
					}
					return true;
				}
				_hashList.get(pos).setPatternID(oldID);
				_hashList.get(pos).setNext(next);
			}
		}
		//�����½ڵ�
		int pos = getPos(newSeg, newLen, newID);
		if (_hashList.get(pos).getNext() != BaseDataDefine.PAT_NO_DATA) {
			for (int j = newSeg+1; j < newSeg+newLen+1; ++j) {
				_hashList.get(j).setFather(BaseDataDefine.PAT_NO_DATA);
				_hashList.get(j).setNext(BaseDataDefine.PAT_NO_DATA);
				_hashList.get(j).setPatternID(BaseDataDefine.PAT_NO_DATA);
			}
			return true;
		}
		_hashList.get(pos).setPatternID(newID);
		_hashList.get(pos).setNext(_n_now_len);
		_n_now_len++;
		if (_n_now_len == _n_max_len) {
			reallocNodeList();
		}
		_hashList.get(newSeg).setNext(newLen);
		_hashList.get(newSeg).setPatternID(_hashList.get(oldSeg).getPatternID());
		_h_now_len = _h_now_len + newLen + 1;
		
		//����ɶ�
		for (int i = oldSeg; i < oldSeg+oldLen+1; ++i) {
			_hashList.get(i).setFather(BaseDataDefine.PAT_NO_DATA);
			_hashList.get(i).setNext(BaseDataDefine.PAT_NO_DATA);
			_hashList.get(i).setPatternID(BaseDataDefine.PAT_NO_DATA);
		}
		return false;
	}
	
	
	/**
	 * @brief: Ϊģ��Ԫ�ַ����б�ţ������뵽patternList�Ķ�Ӧλ��
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	public int getPatternID(String pattern, String proper, char[] tagChars) {
		int begin, end;
		if (tagChars[1] == 'W') { //ͨ���
			begin = 0;
			end = begin + _wildc_num;
		} else if (tagChars[1] == 'F') { //ע�ắ��
			begin = BaseDataDefine.MAX_PAT_WILD_NUM;
			end = begin + _func_num;
		} else { //�̶��ʺͲ�λ�ʵ�
			begin = BaseDataDefine.MAX_PAT_FUNC_NUM + BaseDataDefine.MAX_PAT_WILD_NUM;
			end = begin + _other_num;
		}
		//�����б�������
		for (int i = begin; i < end; ++i) {
			if (_patternList.get(i).getMetaStr().equals(pattern)) {
				return i;  //�ҵ�ֱ�ӷ����±�
			}
		}
		if (tagChars[0] == 'w') { //������β������
			_patternList.get(end).setMetaStr(pattern);
			if (tagChars[1] == 'W') {
				_wildc_num++;
			} else if (tagChars[1] == 'F') {
				if (_func_num > BaseDataDefine.MAX_PAT_FUNC_NUM) {
					//log
					return -1;
				}
				_funcList[_func_num].setName(pattern);
				_func_num++;
			} else {
				_other_num++;
				if (begin + _other_num >= _p_max_len) {
					reallocPatternList();
				}
			}
			_patternList.get(end).setAttrStr(proper);
			return end;
		}
		return -1;
	}
	
	
	/**
	 * @brief: ��ȡģ���е�Ԫ�ַ�
	 * @param: 
	 * 
	 * @return: int : ��ȡ���ַ�������
	 * 
	 **/
	int cutPattern(char[] nowStr, String pattern, int nowPos) {
		int i = -1, l = -1, r = -1;
		char[] patChars = pattern.toCharArray();
		int patLen = patChars.length;
		for (i = nowPos; i < patLen; ++i) { //�������� <
			if (patChars[i] == '<') {
				l = i;
				break;
			}
		}
		for (i = nowPos; i < patLen; ++i) { //�������� >
			if (patChars[i] == '>') {
				r = i;
				break;
			}
		}
		if (l > nowPos) { //'<'֮ǰ���ַ�
			for (i = nowPos; i < l; ++i) {
				nowStr[i-nowPos] = patChars[i];
			}
			nowStr[i-nowPos] = '\0';
		} else if ((l == nowPos) && (r > l)) { //'<'�ǵ�һ���ַ�����'>'�����
			for (i = l; i <= r; ++i) {
				nowStr[i-l] = patChars[i];
			}
			nowStr[i-l] = '\0';
		} else if (l == -1 && r == -1) { //������<>�����̶���ģ��
			for (i = nowPos; i < pattern.length(); ++i) {
				nowStr[i-nowPos] = pattern.charAt(i);
			}
			nowStr[i-nowPos] = '\0';
		}
		int len = 0;
		while(nowStr[len] != 0) {
			len++;
		}
		return len;
	}
	
	
	/**
	 * @brief: ע�Ṧ�ܺ���������ʶ������ʶ��
	 * @param: 
	 * 
	 * @return: int : 0=�ɹ���-1=ʧ��
	 * 
	 **/
	public int registerFunc(String name, PatternFuncNodeBean func) {
		if (name.length() == 0 || func.isFlag() == false) {
			//log error
			return -1;
		}
		int i;
		for (i = 0; i < _func_num; ++i) {
			if (_funcList[i].getName().equals(name)) {
				break;
			}
		}
		if (i == _func_num) { //ģ���в����ڸú���
			return -1;
		} else {
			_funcList[i] = func;
			return 0;
		}
	}
	
	
	/**
	 * @brief: ע��ͨ���
	 * @param: 
	 * 
	 * @return: int : 0=�ɹ���-1=ʧ��
	 * 
	 **/
	int registerWildcard(String name) {
		char[] nameChars = name.toCharArray();
		int charCnt = nameChars.length;
		if (charCnt == 0) {
			//log error
			return -1;
		}
		if (nameChars.length < 3 || nameChars[2] != ':') {
			//log error
			return -1;
		}
		int i;
		for (i = 3; i < charCnt && nameChars[i] != '-'; ++i) {
			if (!(nameChars[i] <= '9' && nameChars[i] >= '0')) {
				//log error
				return -1;
			}
		}
		if (nameChars[i] != '-') {
			//log error
			return -1;
		}
		for (i += 1; i < charCnt && nameChars[i] != '>'; ++i) {
			if (!(nameChars[i] <= '9' && nameChars[i] >= '0')) {
				//log error
				return -1;
			}
		}
		if (nameChars[i] != '>') {
			//log error
			return -1;
		}
		
		char[] upChar = new char[20];
		char[] downChar = new char[20];
		int l;
		for (l = 3; nameChars[l] != '-'; ++l) {
			downChar[l-3] = nameChars[l];
		}
		downChar[l-3] = 0;
		int r = l + 1;
		for (; nameChars[r] != '>'; ++r) {
			upChar[r-l-1] = nameChars[r];
		}
		upChar[r-l-1] = 0;
		int up = Integer.parseInt(ProcessUtils.charsToString(upChar));
		int down = Integer.parseInt(ProcessUtils.charsToString(downChar));
		if (up < 0 || down < 0 || down > up) {
			//log error
			return -1;
		}
		for (i = 0; i < _wildc_num; ++i) {
			if (name.equals(_wildcList[i].getName())) {
				return 0;
			}
		}
		if (i == _wildc_num && i < BaseDataDefine.MAX_PAT_WILD_NUM) {
			_wildcList[i].setName(name);
			_wildcList[i].setUpper(up);
			_wildcList[i].setLower(down);
			getPatternID(name, "", "wW".toCharArray());
			return 0;
		} else {
			//log error
			return -1;
		}
	}
	
	
	/**
	 * @brief: ��ģ����ʽ�Ͷ�Ӧ��������Ϣ����ģ����
	 * @param: 
	 * 
	 * @return: int : 0=�ɹ���-1=ʧ��
	 * 
	 **/
	public int insertPatternTrie(String input, String proper) {
		int strPos = 0, current = 0;
		char[] inputChars = input.toCharArray();
		int inputCnt = inputChars.length;
		while (strPos < inputCnt) {
			boolean isFunc = false;
			boolean isWild = false;
			char[] nowStr = new char[BaseDataDefine.MAX_PAT_ATTR_LEN];
			int len = cutPattern(nowStr, input, strPos);
			char[] tag = new char[3];
			tag[2] = '\0';
			tag[0] = 'r';
			if (nowStr[0] != '<') { //�̶��� tag='wO'
				tag[0] = 'w';
				tag[1] = 'O';
			} else if (nowStr[1] == 'D') { //��λ�ʵ� tag='rO'
				tag[1] = 'O';
			} else if (nowStr[1] == 'F') { //ע�ắ�� tag='wF'
				tag[1] = 'F';
				tag[0] = 'w';
				isFunc = true;
			} else if (nowStr[1] == 'W') { //ͨ��� tag='rW'
				if (0 != registerWildcard(ProcessUtils.charsToString(nowStr))) {
					return -1;
				}
				tag[1] = 'W';
				isWild = true;
			}
			int patternID = getPatternID(ProcessUtils.charsToString(nowStr), "", tag);
			if (patternID == -1) {
				return -1;
			}
			if (nowStr[0] != '<') { //�̶��ʣ�����ʵ�Trie��
				wTree.insert(patternID, ProcessUtils.charsToString(nowStr), "");
			}
			//_nodelistδ���䣬�����һ��hash�α�
			if (_nodeList.get(current).getSegPos() == BaseDataDefine.PAT_NO_DATA) {
				int segPos = _h_now_len;
				int segLen = BaseDataDefine.PAT_HASH_SEG_LEN;
				if (segPos+segLen+1 > _h_max_len) {
					compressHash();
					segPos = _h_now_len;
					if(segPos+segLen+1 > _h_max_len) {
						reallocHashList();
					}
				}
				_nodeList.get(current).setSegPos(segPos);
				_hashList.get(segPos).setNext(segLen);
				_hashList.get(segPos).setFather(current);
				_h_now_len += (segLen + 1);
			}
			//��_hashlist������һ���ڵ�
			int segPos = _nodeList.get(current).getSegPos();
			int segLen = _hashList.get(segPos).getNext();
			if (isFunc) {
				if (_hashList.get(segPos).getPatternID() == BaseDataDefine.NODE_HAVE_WILDCARD ||
						_hashList.get(segPos).getPatternID() == BaseDataDefine.NODE_HAVE_BOTH) {
					_hashList.get(segPos).setPatternID(BaseDataDefine.NODE_HAVE_BOTH);
				} else {
					_hashList.get(segPos).setPatternID(BaseDataDefine.NODE_HAVE_FUNC);
				}
			}
			if (isWild) {
				if (_hashList.get(segPos).getPatternID() == BaseDataDefine.NODE_HAVE_FUNC || 
						_hashList.get(segPos).getPatternID() == BaseDataDefine.NODE_HAVE_BOTH) {
					_hashList.get(segPos).setPatternID(BaseDataDefine.NODE_HAVE_BOTH);
				} else  {
					_hashList.get(segPos).setPatternID(BaseDataDefine.NODE_HAVE_WILDCARD);
				}
			}
			int pos = getPos(segPos, segLen, patternID);
			if (_hashList.get(pos).getNext() == BaseDataDefine.PAT_NO_DATA) {
				_hashList.get(pos).setPatternID(patternID);
				_hashList.get(pos).setNext(_n_now_len);
				_n_now_len++;
				if (_n_now_len == _n_max_len) {
					reallocHashList();
				}
			} else {
				if (_hashList.get(pos).getPatternID() != patternID) {
					int oldSeg = _nodeList.get(current).getSegPos();
					int oldLen = _hashList.get(oldSeg).getNext();
					int newSeg = _h_now_len;
					int newLen = oldLen;
					do {
						newLen++;
						if (newSeg+newLen+1 > _h_max_len) {
							compressHash();
							oldSeg = _nodeList.get(current).getSegPos();
							oldLen = _hashList.get(oldSeg).getNext();
							newSeg = _h_now_len;
							if (newSeg+newLen+1 > _h_max_len) {
								reallocHashList();
							}
						}
					} while(moveSegment(oldSeg, oldLen, newSeg, newLen, patternID));
					_nodeList.get(current).setSegPos(newSeg);
					_hashList.get(newSeg).setFather(current);
					pos = getPos(newSeg, newLen, patternID);
				}
			}
			current = _hashList.get(pos).getNext();
			strPos = strPos + len;
		} /* ����ģ���ַ�������  */
		
		//��ֵ������Ϣ 
		_nodeList.get(current).setExit(proper);
		
		return 0;
	}
	
	
	/**
	 * @brief: ����λ�ʵ�Ĵʲ���ʵ���
	 * @param: 
	 * 
	 * @return: int : 0=�ɹ���-1=ʧ��
	 * 
	 **/
	public int insertWordTrie(int patternID, String word, String proper) {
		return wTree.insert(patternID, word, proper);
	}
	
	
	/**
	 * @brief: ѹ���ʵ�����hash��ȥ�������м�Ŀհײ���
	 * @param: void
	 * 
	 * @return: void
	 * 
	 **/
	public void compressWordHash() {
		wTree.compressHash();
	}
	
	
	/**
	 * @brief: ʹ��ע�ắ������ƥ��
	 * @param: 
	 * 
	 * @return: void
	 * 
	 **/
	void matchFunc(PatMatchStatusBean nowStatus, SearchResultNodeBean[] result) {
		
		int segPos = _nodeList.get(nowStatus.getCurrent()).getSegPos();
		if (segPos == BaseDataDefine.PAT_NO_DATA) {
			return;
		}
		int specialMatch = _hashList.get(segPos).getPatternID();
		//�ڵ����ע�ắ��
		if (specialMatch == BaseDataDefine.NODE_HAVE_FUNC || specialMatch == BaseDataDefine.NODE_HAVE_BOTH) {
			for (int i = 0; i < _func_num; ++i) {
				int patternID = getPatternID(_funcList[i].getName(), "", "rF".toCharArray());
				if (patternID == -1)
					continue;
				int segLen = _hashList.get(segPos).getNext();
				int pos = getPos(segPos, segLen, patternID);
				if (_hashList.get(pos).getPatternID() == patternID) {
					if (_funcList[i].isFlag() == false) {
						return;
					}
					int len = _funcList[i].handler(nowStatus.getCharacters(), nowStatus.getStrPos());
					if (len == 0)
						continue;
					int current = nowStatus.getCurrent();
					nowStatus.setPatternID(nowStatus.getTermNum(), nowStatus.getStrPos());
					nowStatus.setPos(nowStatus.getTermNum(), nowStatus.getStrPos());
					nowStatus.setLen(nowStatus.getTermNum(), len);
					nowStatus.addStrPos(len);
					nowStatus.setCurrent(_hashList.get(pos).getNext());
					nowStatus.addTermNum(1);
					search(nowStatus, result);
					nowStatus.addTermNum(-1);
					nowStatus.setCurrent(current);
					nowStatus.addStrPos(-len);
				}
			}
		}
	}
    
	
	/**
	 * @brief: ʹ��ͨ�������ƥ��
	 * @param: 
	 * 
	 * @return: void
	 * 
	 **/
	void matchWildCard(PatMatchStatusBean nowStatus, SearchResultNodeBean[] result) {
				
		int segPos = _nodeList.get(nowStatus.getCurrent()).getSegPos();
		if (segPos == BaseDataDefine.PAT_NO_DATA) {
			return;
		}
		int specialMatch = _hashList.get(segPos).getPatternID();
		//�ڵ����ͨ���
		if (specialMatch == BaseDataDefine.NODE_HAVE_WILDCARD || specialMatch == BaseDataDefine.NODE_HAVE_BOTH) {
			for (int i = 0; i < _wildc_num; ++i) {
				int patternID = getPatternID(_wildcList[i].getName(), "", "rW".toCharArray());
				if (patternID == -1)
					continue;
				int segLen = _hashList.get(segPos).getNext();
				int pos = getPos(segPos, segLen, patternID);
				if (_hashList.get(pos).getPatternID() == patternID) {
					int current = nowStatus.getCurrent();
					for (int len = _wildcList[i].getLower(); len <= _wildcList[i].getUpper(); ++len) {
						if (nowStatus.getStrPos() + len > nowStatus.getStrLen())
							break;
						nowStatus.setPatternID(nowStatus.getTermNum(), patternID);
						nowStatus.setPos(nowStatus.getTermNum(), nowStatus.getStrPos());
						nowStatus.setLen(nowStatus.getTermNum(), len);
						nowStatus.addStrPos(len);
						nowStatus.setCurrent(_hashList.get(pos).getNext());
						nowStatus.addTermNum(1);
						search(nowStatus, result);
						nowStatus.addTermNum(-1);
						nowStatus.setCurrent(current);
						nowStatus.addStrPos(-len);
					}
				}
			}
		}
	}
	
	
	/**
	 * @brief: ʹ�ôʵ������й̶��ʡ���λ�ʡ��ɺ��Դʵ�ƥ��
	 * @param: 
	 * 
	 * @return: void
	 * 
	 **/
	void matchIgnoreWord(PatMatchStatusBean nowStatus, SearchResultNodeBean[] result) {
		
		PatTransNodeBean[] trans = new PatTransNodeBean[BaseDataDefine.MAX_PAT_MULTI_PROP];
		for (int i = 0; i < BaseDataDefine.MAX_PAT_MULTI_PROP; ++i) {
			PatTransNodeBean node = new PatTransNodeBean();
			trans[i] = node;
		}
		//��λ�ʡ��̶��ʡ��ɺ��Դ���wtree���ҵ���ƥ�����
		int num = wTree.search(nowStatus.getCharacters(), nowStatus.getStrPos(), trans, BaseDataDefine.MAX_PAT_MULTI_PROP);
		
		for (int i = 0; i < num; ++i) {
			if (trans[i].getPatternID() != BaseDataDefine.IGN_WORD_PROP) { //ģ��ʡ��̶���
				int segPos = _nodeList.get(nowStatus.getCurrent()).getSegPos();
				if (segPos == BaseDataDefine.PAT_NO_DATA)
					continue;
				int segLen = _hashList.get(segPos).getNext();
				int pos = getPos(segPos, segLen, trans[i].getPatternID());
				if (_hashList.get(pos).getPatternID() == trans[i].getPatternID()) {
					nowStatus.setPatternID(nowStatus.getTermNum(),trans[i].getPatternID());
					nowStatus.setWordExtra(nowStatus.getTermNum(), trans[i].getProper());
					nowStatus.setPos(nowStatus.getTermNum(), nowStatus.getStrPos());
					nowStatus.setLen(nowStatus.getTermNum(), trans[i].getLen());
					nowStatus.addTermNum(1);
					nowStatus.addStrPos(trans[i].getLen());
					
					int current = nowStatus.getCurrent();
					nowStatus.setCurrent(_hashList.get(pos).getNext());
					search(nowStatus, result);
					nowStatus.setCurrent(current);
					nowStatus.addStrPos(-(trans[i].getLen()));
					nowStatus.addTermNum(-1);
				}
			} else { //�ɺ��Դ�
				if ((nowStatus.getCommand() & BaseDataDefine.COMMAND_USE_IGNORETERM) != 0) {
					nowStatus.setPatternID(nowStatus.getTermNum(), BaseDataDefine.IGN_WORD_PROP);
					nowStatus.setPos(nowStatus.getTermNum(), nowStatus.getStrPos());
					nowStatus.setLen(nowStatus.getTermNum(), trans[i].getLen());
					nowStatus.addTermNum(1);
					nowStatus.addStrPos(trans[i].getLen());
					search(nowStatus, result);
					nowStatus.addTermNum(-1);
					nowStatus.addStrPos(-(trans[i].getLen()));
				}
			}
		}
	}
	
	
	/**
	 * @brief: ʹ��ģ�������ʵ��������ı�ƥ��Ķ���ӿ�
	 * @param: 
	 * 
	 * @return: void
	 * 
	 **/
	public void search(PatMatchStatusBean nowStatus, SearchResultNodeBean[] result) {
		
		int strLen = nowStatus.getStrLen();
		if (nowStatus.getStrPos() > strLen) {
			return;
		}
		if (nowStatus.getResultNum() >= nowStatus.getResultSize() || 
				(nowStatus.getResultNum() == 1 && ((nowStatus.getCommand() & BaseDataDefine.COMMAND_ONE_RESULT) != 0))) {
			return;
		}
		//�ߵ�����exitƥ��ɹ���������
		if (nowStatus.getStrPos() <= strLen && _nodeList.get(nowStatus.getCurrent()).getExit() != null) {
			int resIndex = nowStatus.getResultNum();
			result[resIndex].setTermsNum(nowStatus.getTermNum());
			result[resIndex].setPatternStr(0, '\0');
			result[resIndex].setPatternExtra(0, '\0');
			result[resIndex].setMatchLen(strLen);
			for (int i = 0; i < nowStatus.getTermNum(); ++i) {
				result[resIndex].getSlotMatchInfo(i).setStr("");
				result[resIndex].getSlotMatchInfo(i).setExtra("");
				result[resIndex].getSlotMatchInfo(i).setBegin(nowStatus.getPos(i));
				result[resIndex].getSlotMatchInfo(i).setLen(nowStatus.getLen(i));
				if (nowStatus.getPatternID(i) == BaseDataDefine.IGN_WORD_PROP) {
					result[resIndex].getSlotMatchInfo(i).setSlot(BaseDataDefine.SLOT_TYPE_IGNO);
				} else {
					//ģ������
					int index = result[resIndex].getPatternStrLen();
					int k = 0;
					for (; index < BaseDataDefine.MAX_DEST_STR_LEN; ++index) {
						result[resIndex].setPatternStr(index, _patternList.get(nowStatus.getPatternID(i)).getMeta(k));
						if (_patternList.get(nowStatus.getPatternID(i)).getMeta(k) == 0)
							break;
						k++;
					}
					result[resIndex].setPatternStr(BaseDataDefine.MAX_DEST_STR_LEN-1, '\0');
					//ƥ����ǣ�ģ��ʡ�ע�ắ����ͨ������̶���
					if (_patternList.get(nowStatus.getPatternID(i)).getMetaLen() + 
							_patternList.get(nowStatus.getPatternID(i)).getAttrLen() + 1 < BaseDataDefine.MAX_WORDS_LEN) {
						result[resIndex].getSlotMatchInfo(i).setStr(_patternList.get(nowStatus.getPatternID(i)).getMetaStr());
						if (_patternList.get(nowStatus.getPatternID(i)).getAttrLen() != 0) {
							result[resIndex].getSlotMatchInfo(i).mergeAttr(_patternList.get(nowStatus.getPatternID(i)).getAttrStr());
						}
					}
					char type = _patternList.get(nowStatus.getPatternID(i)).getMeta(1);
					if (type == 'D') {
						result[resIndex].getSlotMatchInfo(i).setSlot(BaseDataDefine.SLOT_TYPE_GEN);
						result[resIndex].getSlotMatchInfo(i).setExtra(nowStatus.getWordExtra(i));
					} else if (type == 'F') {
						result[resIndex].getSlotMatchInfo(i).setSlot(BaseDataDefine.SLOT_TYPE_FUNC);
					} else if (type == 'W') {
						result[resIndex].getSlotMatchInfo(i).setSlot(BaseDataDefine.SLOT_TYPE_WILD);
					} else {
						result[resIndex].getSlotMatchInfo(i).setSlot(BaseDataDefine.SLOT_TYPE_FIX);
						result[resIndex].getSlotMatchInfo(i).setStr("");
					}
				}
			}
			//ģ�����Բ���
			result[resIndex].setPatternExtraStr(_nodeList.get(nowStatus.getCurrent()).getExit());
			nowStatus.upResultNum();
		}
		if (nowStatus.getTermNum() >= BaseDataDefine.RESULT_TEMRS_SIZE) {
			return;
		}
		
		matchFunc(nowStatus, result);
		matchWildCard(nowStatus, result);
		matchIgnoreWord(nowStatus, result);
	}
}
