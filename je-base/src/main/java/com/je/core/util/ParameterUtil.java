package com.je.core.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 描述: 请求参数工具类
 * <p>
 * 依赖:
 * org.apache.commons.beanutils.BeanUtils
 * org.apache.commons.lang3.StringUtils
 *
 * @auther: wangmm@ketr.com.cn
 * @date: 2019/3/31 9:50
 */
public class ParameterUtil {

    /**
     * @return boolean
     * @throws
     * @description: 验证参数中是否有null。
     * @params [parameters]
     * @author wangmm@ketr.com.cn
     * @date 2019/3/31 9:51
     */
    public static boolean isNull(Object... parameters) {
        for (Object parameter : parameters) {
            if (parameter == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return boolean
     * @throws
     * @description: 验证参数中是否有效。规则:值不为空且toString
     * @params [parameters]
     * @author wangmm@ketr.com.cn
     * @date 2019/3/31 9:51
     */
    public static boolean isInvalid(Object... parameters) {
        for (Object parameter : parameters) {
            if (parameter == null || StringUtils.isBlank(parameter.toString())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return boolean
     * @throws
     * @description: 验证Bean的属性是否为空
     * @params [properties]
     * @author wangmm@ketr.com.cn
     * @date 2019/3/31 9:52
     */
    public static boolean isPropertyNull(Object bean, String... properties) {

        try {
            for (String property : properties) {
                String value = BeanUtils.getProperty(bean, property);
                if (value == null) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return boolean
     * @throws
     * @description: 验证Bean的属性是否为有效值
     * @params [properties]
     * @author wangmm@ketr.com.cn
     * @date 2019/3/31 9:52
     */
    public static boolean isPropertyInvalid(Object bean, String... properties) {

        try {
            for (String property : properties) {
                String value = BeanUtils.getProperty(bean, property);
                if (value == null || StringUtils.isBlank(value.toString())) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @return boolean
     * @throws
     * @description: 校验一个字符串是否为数字（包括以逗号分隔的数字）
     * @params [number]
     * @author wangmm@ketr.com.cn
     * @date 2019/3/31 9:52
     */
    public static boolean isNumber(String number) {
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        if (number.indexOf(ArrayUtils.SPLIT) >= 0) {
            //有逗号等分隔符的数字
            return number.matches("[+-]?[1-9]+[0-9]*(,[0-9]{3})+(\\.[0-9]+)?");
        } else {
            return number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
        }
    }

    /**
     * @return boolean
     * @throws
     * @description: 校验一个字符串是否为整数
     * @params [number]
     * @author wangmm@ketr.com.cn
     * @date 2019/3/31 9:52
     */
    public static boolean isInteger(String number) {
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        return number.matches("^([1-9]\\d*)|(0)$");
    }
}
