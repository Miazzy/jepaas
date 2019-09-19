package com.je.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author 研发部:云凤程
 * 人民币阿拉伯数字转汉字
 */
public class RMBChange {
	/**
	 * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
	 * 要用到正则表达式
	 */
	public static String digitUppercase(double n){
		String fraction[] = {"角", "分"};
	    String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	    String unit[][] = {{"元", "万", "亿"},{"", "拾", "佰", "仟"}};

	    String head = n < 0? "负": "";
	    n = Math.abs(n);
	    
	    String s = "";
	    for (int i = 0; i < fraction.length; i++) {
	        s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
	    }
	    if(s.length()<1){
		    s = "整";	
	    }
	    int integerPart = (int)Math.floor(n);

	    for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
	        String p ="";
	        for (int j = 0; j < unit[1].length && n > 0; j++) {
	            p = digit[integerPart%10]+unit[1][j] + p;
	            integerPart = integerPart/10;
	        }
	        s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
	    }
	    return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
	}
	/**
	 * 把一段文字中的所有数字变为大写汉字
	 * @param str
	 * @return
	 */
	public static String phrase2Uppercase(String str){
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(str);
		while(m.find()){
			str = str.replace(m.group(), uppercase(m.group()));
		}		
		return str;
	}
	private static String uppercase(String str){
	    String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
	    String s = "";
	    for (int i = 0; i < str.length(); i++) {
			s+=digit[Integer.parseInt(str.substring(i, i+1))];
		}
	    return  s;
	}
}











