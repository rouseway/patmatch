package com.github.rouseway.nlp.patmatch.trie;

import java.util.ArrayList;
import java.util.List;

import com.github.rouseway.nlp.patmatch.matchbean.PatTransNodeBean;
import com.github.rouseway.nlp.patmatch.util.BaseDataDefine;
import com.github.rouseway.nlp.patmatch.wordbean.*;


public class WordTrie {
	
	int _h_max_len = -1;
	int _h_now_len = -1;
	
	int _n_max_len = -1;
	int _n_now_len = -1;
	
	int _w_max_len = -1;
	int _w_now_len = -1;
	
	
	List<WordHashNodeBean> _hashList = null;
	List<WordTreeNodeBean> _nodeList = null;
	List<WordDictNodeBean> _wordList = null;
 	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	int init() {
		//��ʼ��_hashList
		_h_max_len = BaseDataDefine.MAX_WORD_HASH_SIZE;
		_h_now_len = 0;
		_hashList = new ArrayList<WordHashNodeBean>(_h_max_len);
		if (_hashList == null) {
			return -1;
		}
		for (int i = 0; i < _h_max_len; ++i) {
			WordHashNodeBean node = new WordHashNodeBean();
			_hashList.add(node);
		}
		
		//��ʼ��_nodeList
		_n_max_len = BaseDataDefine.MAX_WORD_NODE_SIZE;
		_n_now_len = 1;
		_nodeList = new ArrayList<WordTreeNodeBean>(_n_max_len);
		if (_nodeList == null) {
			return -1;
		}
		for (int i = 0; i < _n_max_len; ++i) {
			WordTreeNodeBean node = new WordTreeNodeBean();
			_nodeList.add(node);
		}
		
		//��ʼ��_wordList
		_w_max_len = BaseDataDefine.MAX_WORD_LIST_SIZE;
		_w_now_len = 0;
		_wordList = new ArrayList<WordDictNodeBean>(_w_max_len);
		if (_wordList == null) {
			return -1;
		}
		for (int i = 0; i < _w_max_len; ++i) {
			WordDictNodeBean node = new WordDictNodeBean();
			_wordList.add(node);
		}
		return 0;
	}
	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	int reallocHashList() {
		for (int i = 0; i < _h_max_len; ++i) {
			WordHashNodeBean node = new WordHashNodeBean();
			_hashList.add(node);
		}
		return _h_max_len*2;
	}
	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	int reallocNodeList() {
		for (int i = 0; i < _n_max_len; ++i) {
			WordTreeNodeBean node = new WordTreeNodeBean();
			_nodeList.add(node);
		}
		return _n_max_len*2;
	}
	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	int reallocWordList() {
		for (int i = 0; i < _w_max_len; ++i) {
			WordDictNodeBean node = new WordDictNodeBean();
			_wordList.add(node);
		}
		return _w_max_len*2;
	}
	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	int getPos(int segPos, int segLen, char ch) {
		//Java unicode�����ַ�Ϊ˫�ֽ�
		int tmp = (int)ch;
		tmp = tmp % segLen;
		return segPos + tmp + 1;
	}
	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	void compressHash() {
		int spare = 0;
		while (spare < _h_max_len && _hashList.get(spare).getFather() != BaseDataDefine.WORD_NO_DATA 
			&& _hashList.get(spare).getNext() != BaseDataDefine.WORD_NO_DATA) {
			spare += (_hashList.get(spare).getNext() + 1);
		}
		if (spare >= _h_max_len)
			return;
		
		int idx = spare;
		while(true) {
			for (; idx < _h_max_len; ++idx) {
				if(_hashList.get(idx).getFather() != BaseDataDefine.WORD_NO_DATA &&
					_hashList.get(idx).getNext() != BaseDataDefine.WORD_NO_DATA) {
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
					WordHashNodeBean node = _hashList.get(idx+j);
					_hashList.get(spare+j).setCharacter(node.getCharacter());
					_hashList.get(spare+j).setFather(node.getFather());
					_hashList.get(spare+j).setNext(node.getNext());
					
					_hashList.get(idx+j).setFather(BaseDataDefine.WORD_NO_DATA);
					_hashList.get(idx+j).setNext(BaseDataDefine.WORD_NO_DATA);
					_hashList.get(idx+j).setCharacter('\0');
				}
				_nodeList.get(father).setSegPos(spare);
			}
			spare += len;
			idx += len;
		}
		_h_now_len = spare;
	}
	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	boolean moveSegment(int oldSeg, int oldLen, int newSeg, int newLen, char newChar) {
		//�ƶ��ɽڵ�
		for (int i = 0; i < oldLen; ++i) {
			int old = oldSeg + i + 1;
			if (_hashList.get(old).getNext() != BaseDataDefine.WORD_NO_DATA) {
				char ch = _hashList.get(old).getCharacter();
				int next = _hashList.get(old).getNext();
				int pos = getPos(newSeg, newLen, ch);
				if (_hashList.get(pos).getNext() != BaseDataDefine.WORD_NO_DATA) { //���γ�ͻ
					for (int j = newSeg + 1; j < newSeg+newLen+1; ++j) {
						_hashList.get(j).setFather(BaseDataDefine.WORD_NO_DATA);
						_hashList.get(j).setNext(BaseDataDefine.WORD_NO_DATA);
						_hashList.get(j).setCharacter('\0');
					}
					return true;
				}
				_hashList.get(pos).setCharacter(ch);
				_hashList.get(pos).setNext(next);
			}
		}
		//�����½ڵ�
		int pos = getPos(newSeg, newLen, newChar);
		if (_hashList.get(pos).getNext() != BaseDataDefine.WORD_NO_DATA) {
			for (int j = newSeg+1; j < newSeg+newLen+1; ++j) {
				_hashList.get(j).setFather(BaseDataDefine.WORD_NO_DATA);
				_hashList.get(j).setNext(BaseDataDefine.WORD_NO_DATA);
				_hashList.get(j).setCharacter('\0');
			}
			return true;
		}
		_hashList.get(pos).setCharacter(newChar);
		_hashList.get(pos).setNext(_n_now_len);
		_n_now_len++;
		if (_n_now_len == _n_max_len) {
			reallocNodeList();
		}
		_hashList.get(newSeg).setNext(newLen);
		_h_now_len = _h_now_len + newLen + 1;
		
		//����ɶ�
		for (int i = oldSeg; i < oldSeg+oldLen+1; ++i) {
			_hashList.get(i).setFather(BaseDataDefine.WORD_NO_DATA);
			_hashList.get(i).setNext(BaseDataDefine.WORD_NO_DATA);
			_hashList.get(i).setCharacter('\0');
		}
		return false;
	}
	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	int insert(int patternID, String word, String proper) {
		int current = 0, strPos = 0;
		char[] characters = word.toCharArray();
		int charCnt = characters.length;
		
		while (strPos < charCnt) {
			char curChar = characters[strPos];
			if (_nodeList.get(current).getSegPos() == BaseDataDefine.WORD_NO_DATA) {
				int segPos = _h_now_len;
				int segLen = BaseDataDefine.WORD_HASH_SEG_LEN;
				if (segPos + segLen + 1 > _h_max_len) {
					compressHash();
					segPos = _h_now_len;
					if (segPos + segLen + 1 > _h_max_len) {
						reallocHashList();
					}
				}
				_nodeList.get(current).setSegPos(segPos);
				_hashList.get(segPos).setNext(segLen);
				_hashList.get(segPos).setFather(current);
				_h_now_len += (segLen + 1);
			}
			//_hashList�в�����һ���ڵ�
			int segPos = _nodeList.get(current).getSegPos();
			int segLen = _hashList.get(segPos).getNext();
			int pos = getPos(segPos, segLen, curChar);
			if (_hashList.get(pos).getNext() == BaseDataDefine.WORD_NO_DATA) {
				_hashList.get(pos).setCharacter(curChar);
				_hashList.get(pos).setNext(_n_now_len);
				_n_now_len++;
				
				if (_n_now_len == _n_max_len) {
					reallocNodeList();
				}
			} else {
				if (_hashList.get(pos).getCharacter() != curChar) {
					int oldSeg = _nodeList.get(current).getSegPos();
					int oldLen = _hashList.get(oldSeg).getNext();
					int newSeg = _h_now_len;
					int newLen = oldLen;
					do {
						newLen++;
						if (newSeg + newLen + 1 > _h_max_len) {
							compressHash();
							oldSeg = _nodeList.get(current).getSegPos();
							oldLen = _hashList.get(oldSeg).getNext();
							newSeg = _h_now_len;
							if (newSeg + newLen + 1 > _h_max_len) {
								reallocHashList();
							}
						}
					} while(moveSegment(oldSeg, oldLen, newSeg, newLen, curChar));
					_nodeList.get(current).setSegPos(newSeg);
					_hashList.get(newSeg).setFather(current);
					pos = getPos(newSeg, newLen, curChar);
				}
			}
			current = _hashList.get(pos).getNext();
			strPos++;
		}
		
		//���ֱ�������������ڽڵ�
		int dictPos = _nodeList.get(current).getDictPos();
		if (dictPos != BaseDataDefine.WORD_NO_DATA) { //�õ����ڴʱ���
			WordDictAttrBean p = _wordList.get(dictPos).getProperty();
			while (p.getNext() != null) {
				if (p.getType() == patternID)
					break;
				p = p.getNext();
			}
			if (p.getType() != patternID) {
				WordDictAttrBean attr = new WordDictAttrBean();
				p.setNext(attr);
				p.getNext().setType(patternID);
				p.getNext().setAttr(proper);
				p.getNext().setNext(null);
			}
		} else { //�õ��ʲ��ڴʱ��У�����֮
			if (_w_now_len == _w_max_len) {
				reallocWordList();
			}
			_wordList.get(_w_now_len).setWord(word);
			_wordList.get(_w_now_len).setProperty(new WordDictAttrBean());
			_wordList.get(_w_now_len).getProperty().setAttr(proper);
			_wordList.get(_w_now_len).getProperty().setType(patternID);
			_wordList.get(_w_now_len).getProperty().setNext(null);
			_nodeList.get(current).setDictPos(_w_now_len);
			_w_now_len++;
		}
		return 0;
	}
	
	
	/**
	 * @brief:
	 * @param: 
	 * 
	 * @return:
	 * 
	 **/
	int search(char[] characters, int beginPos, PatTransNodeBean[] trans, int maxTransNum) {
		int current = 0;
		int strPos = beginPos;
		int typeNum = 0;
		int charCnt = characters.length;
		if (charCnt == 0)
			return 0;
		
		while (strPos <= charCnt) {
			if (_nodeList.get(current).getDictPos() != BaseDataDefine.WORD_NO_DATA) {
				WordDictAttrBean p = _wordList.get(_nodeList.get(current).getDictPos()).getProperty();
				while (p != null) {
					trans[typeNum].setPatternID(p.getType());
					trans[typeNum].setLen(strPos-beginPos);
					trans[typeNum].setProper(p.getAttr());
					p = p.getNext();
					typeNum++;
					if (typeNum >= maxTransNum) {
						return typeNum;
					}
				}
			}
			if (strPos >= charCnt || characters[strPos] == '\0')
				break;
			char curChar = characters[strPos];
			
			int segPos = _nodeList.get(current).getSegPos();
			if (segPos == BaseDataDefine.WORD_NO_DATA)
				break;
			int segLen = _hashList.get(segPos).getNext();
			int pos = getPos(segPos, segLen, curChar);
			if (_hashList.get(pos).getCharacter() == curChar) {
				current = _hashList.get(pos).getNext();
				strPos++;
			} else {
				break;
			}
		}
		return typeNum;
	}
	
}
