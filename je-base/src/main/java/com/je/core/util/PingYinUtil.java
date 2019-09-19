package com.je.core.util;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 汉字和拼音转换工具类
 * @author YUNFENGCHENG
 *
 */
public class PingYinUtil {
	private static Logger logger = LoggerFactory.getLogger(PingYinUtil.class);
	/**
	 * 实例化此类
	 * 研发部:云凤程
	 * 2013-1-10
	 * @return
	 */
	public static PingYinUtil getInstance(){
		return PingYinUtilsHolder.PING_YIN_UTIL;
	}
	private static class PingYinUtilsHolder{
		private static final PingYinUtil PING_YIN_UTIL = new PingYinUtil();
	}
	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 *
	 * @param inputString
	 * @return
	 */
	public String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		char[] input = inputString.trim().toCharArray();
		String output = "";
		try {
			for (int i = 0; i < input.length; i++) {
				if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					output += temp[0];
				} else
					output += Character.toString(input[i]);
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			throw new PlatformException("拼音工具类获取拼音异常", PlatformExceptionEnum.JE_CORE_UTIL_PINGYIN_GET_ERROR,new Object[]{inputString},e);
		}
		return output;
	}
	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 * @param chinese 汉字串
	 * @return 汉语拼音首字母
	 */
	public String getFirstSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					throw new PlatformException("拼音工具类获取拼音异常", PlatformExceptionEnum.JE_CORE_UTIL_PINGYIN_GET_ERROR,new Object[]{chinese},e);
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}
	/**
	 * 获取汉字串拼音，英文字符不变
	 * @param chinese 汉字串
	 * @return 汉语拼音
	 */
	public String getFullSpell(String chinese) {
		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					throw new PlatformException("拼音工具类获取拼音异常", PlatformExceptionEnum.JE_CORE_UTIL_PINGYIN_GET_ERROR,new Object[]{chinese},e);
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}
	public static void main(String[] args) {
		System.out.println(PingYinUtil.getInstance().getPingYin("中国111"));
	}
}
