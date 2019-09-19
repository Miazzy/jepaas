package com.je.core.util;
import java.math.BigDecimal;

/**
 * 数学运算的扩展功能类
 * @author 研发部:云凤程
 *
 */
public class MathExtend {
	// 默认除法运算精度

	private static final int DEFAULT_DIV_SCALE = 2;

	/**
	 * 提供精确的加法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的和
	 */

	public static double add(double v1, double v2)

	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 提供精确的加法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数数学加和，以字符串格式返回
	 */

	public static String add(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的差
	 */
	public static double subtract(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 提供精确的减法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数数学差，以字符串格式返回
	 */
	public static String subtract(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).toString();
	}

	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的积
	 */
	public static double multiply(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 提供精确的乘法运算
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的数学积，以字符串格式返回
	 */
	public static String multiply(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).toString();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的商
	 */
	public static double divide(double v1, double v2) {
		return divide(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static double divide(double v1, double v2, int scale) {
		return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            表示需要精确到小数点以后几位
	 * @param round_mode
	 *            表示用户指定的舍入模式
	 * @return 两个参数的商
	 */
	public static double divide(double v1, double v2, int scale, int round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, round_mode).doubleValue();
	}

	/**
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入,舍入模式采用ROUND_HALF_EVEN
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的商，以字符串格式返回
	 */
	public static String divide(String v1, String v2) {
		return divide(v1, v2, DEFAULT_DIV_SCALE);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用ROUND_HALF_EVEN
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            表示需要精确到小数点以后几位
	 * @return 两个参数的商，以字符串格式返回
	 */
	public static String divide(String v1, String v2, int scale) {
		return divide(v1, v2, scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。舍入模式采用用户指定舍入模式
	 * 
	 * @param v1
	 * @param v2
	 * @param scale
	 *            表示需要精确到小数点以后几位
	 * @param round_mode
	 *            表示用户指定的舍入模式
	 * @return 两个参数的商，以字符串格式返回
	 */
	public static String divide(String v1, String v2, int scale, int round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.divide(b2, scale, round_mode).toString();
	}

	/**
	 * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale) {
		return round(v, scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @param round_mode
	 *            指定的舍入模式
	 * @return 四舍五入后的结果
	 */
	public static double round(double v, int scale, int round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		return b.setScale(scale, round_mode).doubleValue();
	}

	/**
	 * 提供精确的小数位四舍五入处理,舍入模式采用ROUND_HALF_EVEN
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @return 四舍五入后的结果，以字符串格式返回
	 */
	public static String round(String v, int scale) {
		return round(v, scale, BigDecimal.ROUND_HALF_EVEN);
	}

	/**
	 * 提供精确的小数位四舍五入处理
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * @param scale
	 *            小数点后保留几位
	 * @param round_mode
	 *            指定的舍入模式
	 * @return 四舍五入后的结果，以字符串格式返回
	 */
	public static String round(String v, int scale, int round_mode) {
		if (scale < 0) {
			throw new IllegalArgumentException(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(v);
		return b.setScale(scale, round_mode).toString();
	}
	/**
	 * 单差值算法
	 * @param k   视值
	 * @param a1  k的下线
	 * @param a2  k的上线
	 * @param c1       值的下线
	 * @param c2       值的上线
	 * @param scale 表示需要精确到小数点以后几位
	 * @return
	 */
	public static double getV(double k,double a1 ,double a2,double c1,double c2,int scale){
		/**
		 * 原始对象算法展示
		 * double v = ((k-a1)/(a2-a1))*(c2-c1)+c1;
		 */
		double ka1 = MathExtend.subtract(k,a1);
		double a2a1 = MathExtend.subtract(a2,a1);
		double c2c1 = MathExtend.subtract(c2,c1);
		double v = MathExtend.add(MathExtend.multiply(MathExtend.divide(ka1, a2a1),c2c1),c1);
		return MathExtend.round(v, scale,BigDecimal.ROUND_HALF_UP);
	}	
	/**
	 * 双差值算法
	 * @param a 第一个视值
	 * @param b 第二个视值
	 * @param a_s 第一个视值_上线
	 * @param a_x 第一个视值_下线
	 * @param b_s 第二个视值_上线
	 * @param b_x 第二个视值_下线
	 * @param a_x_b_x 第一个视值_下线_第二个视值_下线   的真是值
	 * @param a_x_b_s 第一个视值_下线_第二个视值_上线   的真是值
	 * @param a_s_b_x 第一个视值_上线_第二个视值_下线   的真是值
	 * @param a_s_b_s 第一个视值_上线_第二个视值_上线   的真是值
	 * @param scale 表示需要精确到小数点以后几位
	 * @return
	 */
	public static double getV(double a ,double b,double a_s ,double a_x,double b_s,double b_x,
			                  double a_x_b_x,double a_x_b_s,double a_s_b_x , double a_s_b_s,int scale){
		//第一步 算a 对 b_x 单差
		double a_b_x = getV(a, a_x, a_s, a_x_b_x, a_s_b_x,scale);
		//第二部算a 对 b_s 单差
		double a_b_s = getV(a, a_x, a_s,a_x_b_s,a_s_b_s,scale);
		//第三部算 b 对 a 的单差
		double value = getV(b, b_x, b_s, a_b_x, a_b_s,scale);
		return value;
	}	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}