package com.je.core.util;

import com.je.cache.service.config.BackCacheManager;
import com.je.cache.service.config.FrontCacheManager;
import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import com.je.rbac.model.Department;
import com.je.rbac.model.EndUser;
import jxl.common.Logger;
import org.apache.commons.io.IOUtils;

import javax.mail.internet.MimeUtility;
import java.io.*;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class StringUtil {
    private static Logger logger=Logger.getLogger(PropFileUtils.class);
    /** 单引号 */
    public static final String SINGLE_QUOTES = "'";
    /** 双引号 */
    public static final String DOUBLE_QUOTES = "\"";
    /** 空格 */
    public static final String BLANK_SPACE = " ";
    public static final String DOLLAR = "$";
    public static final String EMPTY_JSON_ARRAY = "[]";
    /**
     * 默认缓冲区大小。
     */
    public static final int bufferSize = 4096;
    /**
     * 将一个字符串数组拼为一个带分隔符的字符串
     * @param array
     * @return
     */
    public static String buildSplitString(String[] array, String split) {
        StringBuffer str = new StringBuffer();
        if(null != array && 0 != array.length && null != split) {
            for (int i = 0; i < array.length; i++) {
                str.append(array[i]);
                str.append(split);
            }
            str = new StringBuffer(str.substring(0, str.length() - split.length()));
        }
        return str.toString();

    }
    /**
     * 根据数据对象得到一个可执行sql条件查询的字符串
     * @param array
     * @return
     */
    public static String buildArrayToString(String[] array) {
        StringBuffer str = new StringBuffer("");
        if(null != array && 0 != array.length) {
            for (int i = 0; i < array.length; i++) {
                if(isNotEmpty(array[i])){
                    str.append("'");
                    str.append(array[i]);
                    str.append("',");
                }else{
                    str.append("'',");
                }
            }
            str.deleteCharAt(str.length()-1);
        }else{
            str.append("''");
        }
        return str.toString();

    }

    /**
     * 根据数据对象得到一个可执行sql条件查询的字符串
     * @param array
     * @return
     */
    public static String buildArrayToString(List<String> array) {
        StringBuffer str = new StringBuffer("");
        if(null != array && 0 != array.size()) {
            for (int i = 0; i < array.size(); i++) {
                if(isNotEmpty(array.get(i))){
                    str.append("'");
                    str.append(array.get(i));
                    str.append("',");
                }else{
                    str.append("'',");
                }
            }
            str.deleteCharAt(str.length()-1);
        }else{
            str.append("''");
        }
        return str.toString();
    }

    /**
     * 校验一个字符串是否不为null,或除空格外长度为0
     * @param str 字符串值
     * @return
     */
    public static Boolean isNotEmpty(String str) {
        if(null != str && 0 != str.trim().length() && !"NULL".equalsIgnoreCase(str)) {
            return true;
        }
        return false;
    }

    /**
     * 检验一个字符串是否为null.
     * @param str
     * @return
     */
    public static Boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }
    /**
     * 研发部:云凤程
     * 检验一个字符串是否为null.
     * @param str
     * @param special 特殊字符排除默认是null
     * @return
     */
    public static Boolean isEmpty(String str ,String special) {
        if(special == null){
            special = "null";
        }
        if(null == str || 0 == str.trim().length() || special.equals(str)) {
            return true;
        }
        return false;
    }
    /**
     * 校验一个字符串是否为数字（包括以逗号分隔的数字）
     * @param number
     * @return
     */
    public static boolean isNumber(String number) {
        boolean isNumber = false;
        if(StringUtil.isNotEmpty(number)) {
            int index = number.indexOf(ArrayUtils.SPLIT);
            if (index >= 0) {
                //有逗号等分隔符的数字
                isNumber = number.matches("[+-]?[1-9]+[0-9]*(,[0-9]{3})+(\\.[0-9]+)?");
            } else {
                isNumber = number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");
            }
        }
        return isNumber;
    }

    /**
     * 检验一个字符串是否为整数
     * @param number
     * @return
     */
    public static boolean isInteger(String number) {
        boolean isNumber = false;
        if(StringUtil.isNotEmpty(number)) {
            isNumber = number.matches("^([1-9]\\d*)|(0)$");
        }
        return isNumber;
    }
    /***
     * 传入任意长度的字符数组，将其"去重"后拼为一个字符串数组
     * chenmeng
     * 2012-1-6 下午02:20:14
     * @param array
     */
    public static String[] mergeStringArray(String[]...array) {
        Set<String> arraySet = new HashSet<String>();
        String[] newArray = null;
        for(int i=0; i<array.length; i++) {
            String[] a1 = array[i];
            for(int j=0; j<a1.length; j++) {
                arraySet.add(a1[j]);
            }
        }
        newArray = new String[arraySet.size()];
        arraySet.toArray(newArray);
        return newArray;
    }

    /**
     * 将字符串转为double，出错则返回0.0
     * @param str
     * @return
     */
    public static Double stringToDouble(String str) {
        try {
            Double d = 0.0;
            if(isNotEmpty(str)) {
                d = Double.parseDouble(str);
            } else {
                d = 0.0;
            }
            return d;
        } catch(NumberFormatException ex) {
            return 0.0;
        }

    }

    public static String getVarDefaultValue(String value,EndUser currentUser){
        if(StringUtil.isNotEmpty(value)){
            value=codeToValue(value,currentUser);
            Set<Entry> varSet=new HashSet<Entry>();
            //加入登录信息
            varSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
            //加入用户变量
            varSet.addAll(FrontCacheManager.getCacheValues().entrySet());
            varSet.addAll(BackCacheManager.getCacheValues().entrySet());
            //加入系统设置
            varSet.addAll(WebUtils.getAllSysVar().entrySet());
            for(Entry entry:varSet){
                String key=entry.getKey()+"";
                if(("{"+key+"}").equals(key)){
                    value=entry.getValue()+"";
                }
            }
            return value;
        }else{
            return "";
        }
    }
    /**
     * 将通配代码解析为实际值
     * @param code
     * @return
     */
    public static String codeToValue(String code) {
        String value = code;
        if(isNotEmpty(code)) {
            code = code.trim();
            if("@USER_CODE@".equalsIgnoreCase(code)) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                if(null != currentUser) {
                    value = currentUser.getUserCode();
                }
            }else if("@USER_ID@".equalsIgnoreCase(code)) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                if(null != currentUser) {
                    value = currentUser.getUserId();
                }
            }else if("@USER_NAME@".equalsIgnoreCase(code)) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                if(null != currentUser) {
                    value = currentUser.getUsername();
                }
            }else if("@DEPT_CODE@".equalsIgnoreCase(code)) {
                Department currentUserDept = SecurityUserHolder.getCurrentUserDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getDeptCode();
                }
            }else if("@DEPT_ID@".equalsIgnoreCase(code)) {
                Department currentUserDept = SecurityUserHolder.getCurrentUserDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getDeptId();
                }
            }else if("@DEPT_NAME@".equalsIgnoreCase(code)) {
                Department currentUserDept = SecurityUserHolder.getCurrentUserDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getDeptName();
                }
            }else if("@NOW_DATE@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDate(now);
            }else if("@NOW_MONTH@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDate(now,"yyyy-MM");
            }else if("@NOW_YEAR@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDate(now,"yyyy");
            }else if("@NOW_ONLYMONTH@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDate(now,"MM");
            }else if("@NOW_TIME@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDateTime(now);
            }else if("@USER.dept.jtgsMc@".equalsIgnoreCase(code)) {
                Department currentUserDept = SecurityUserHolder.getCurrentUserDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getJtgsMc();
                }
            }else if("@USER.dept.jtgsDm@".equalsIgnoreCase(code)) {
                Department currentUserDept = SecurityUserHolder.getCurrentUserDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getJtgsDm();
                }
            }else if("@USER.dept.jtgsId@".equalsIgnoreCase(code)) {
                Department currentUserDept = SecurityUserHolder.getCurrentUserDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getJtgsId();
                }
            }else if("@USER.dept.jtgsId@".equalsIgnoreCase(code)) {
                Department currentUserDept = SecurityUserHolder.getCurrentUserDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getJtgsId();
                }
            }else if("@USER.companyEmail@".equalsIgnoreCase(code)) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                if(null != currentUser) {
                    value = currentUser.getCompanyEmail();
                }
            }else if("@USER.executiveName@".equalsIgnoreCase(code)) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                if(null != currentUser) {
                    value = currentUser.getExecutiveName();
                }
            }else if("@USER.executiveCode@".equalsIgnoreCase(code)) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                if(null != currentUser) {
                    value = currentUser.getExecutiveCode();
                }
            }else if("@USER.phone@".equalsIgnoreCase(code)) {
                EndUser currentUser = SecurityUserHolder.getCurrentUser();
                if(null != currentUser) {
                    value = currentUser.getPhone();
                }
            }
        }
        return value;
    }
    public static String codeToValue(String code,EndUser currentUser) {
        String value = code;
        if(isNotEmpty(code)) {
            code = code.trim();
            if("@USER_CODE@".equalsIgnoreCase(code)) {
                if(null != currentUser) {
                    value = currentUser.getUserCode();
                }
            }else if("@USER_ID@".equalsIgnoreCase(code)) {
                if(null != currentUser) {
                    value = currentUser.getUserId();
                }
            }else if("@USER_NAME@".equalsIgnoreCase(code)) {
                if(null != currentUser) {
                    value = currentUser.getUsername();
                }
            }else if("@DEPT_CODE@".equalsIgnoreCase(code)) {
                Department currentUserDept = currentUser.getDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getDeptCode();
                }
            }else if("@DEPT_ID@".equalsIgnoreCase(code)) {
                Department currentUserDept = currentUser.getDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getDeptId();
                }
            }else if("@DEPT_NAME@".equalsIgnoreCase(code)) {
                Department currentUserDept = currentUser.getDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getDeptName();
                }
            }else if("@NOW_DATE@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDate(now);
            }else if("@NOW_MONTH@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDate(now,"yyyy-MM");
            }else if("@NOW_YEAR@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDate(now,"yyyy");
            }else if("@NOW_ONLYMONTH@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDate(now,"MM");
            }else if("@NOW_TIME@".equalsIgnoreCase(code)) {
                Date now = new Date();
                value = DateUtils.formatDateTime(now);
            }else if("@USER.dept.jtgsMc@".equalsIgnoreCase(code)) {
                Department currentUserDept = currentUser.getDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getJtgsMc();
                }
            }else if("@USER.dept.jtgsDm@".equalsIgnoreCase(code)) {
                Department currentUserDept = currentUser.getDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getJtgsDm();
                }
            }else if("@USER.dept.jtgsId@".equalsIgnoreCase(code)) {
                Department currentUserDept = currentUser.getDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getJtgsId();
                }
            }else if("@USER.dept.jtgsId@".equalsIgnoreCase(code)) {
                Department currentUserDept = currentUser.getDept();
                if(null != currentUserDept) {
                    value = currentUserDept.getJtgsId();
                }
            }else if("@USER.companyEmail@".equalsIgnoreCase(code)) {
                if(null != currentUser) {
                    value = currentUser.getCompanyEmail();
                }
            }else if("@USER.executiveName@".equalsIgnoreCase(code)) {
                if(null != currentUser) {
                    value = currentUser.getExecutiveName();
                }
            }else if("@USER.executiveCode@".equalsIgnoreCase(code)) {
                if(null != currentUser) {
                    value = currentUser.getExecutiveCode();
                }
            }else if("@USER.phone@".equalsIgnoreCase(code)) {
                if(null != currentUser) {
                    value = currentUser.getPhone();
                }
            }
        }
        return value;
    }

    /**
     * 将message中的信息用单引号围住
     * @param message
     * @return
     */
    public static String singleQuoteMessage(String message) {
        StringBuffer mb = new StringBuffer();
        mb.append(StringUtil.SINGLE_QUOTES);
        mb.append(message);
        mb.append(StringUtil.SINGLE_QUOTES);
        return mb.toString();
    }

    /**
     * 前位填充
     * @param value　原值
     * @param length 目标长充
     * @param filler 填充值
     * @return
     */
    public static String preFillUp(String value, Integer length, char filler) {
        char[] ary1 = value.toCharArray();
        if(ary1.length >= length) {
            return value;
        } else {
            char[] ary2 = new char[length];
            for(int i=0; i<ary2.length; i++) {
                ary2[i] = filler;
            }
            System.arraycopy(ary1, 0, ary2, ary2.length-ary1.length, ary1.length);
            return new String(ary2);
        }
    }
    /**
     * 得到相同的字符串
     * @param str
     * @param length
     * @return
     */
    public static String getSameString(String str,Integer length){
        String resStr="";
        for(int i=0;i<length;i++){
            resStr+=str;
        }
        return resStr;
    }
    /**
     * 得到默认值 如果为null则返回空字符串
     * @param value
     * @return
     */
    public static String getDefaultValue(String value){
        if(value==null){
            return "";
        }else{
            return value;
        }
    }
    /**
     * 得到默认值 如果为null或空字符串则返回默认值
     * @param value 值
     * @param defValue  默认
     * @return
     */
    public static String getDefaultValue(Object value,String defValue){
        String v=value+"";
        if(StringUtil.isEmpty(v)){
            return defValue;
        }else{
            return v;
        }
    }
    /**
     * 根据截取规则获取截取后的值
     * @param value
     * @param jqFormat
     * @return
     */
    public static String getSubValue(String value,String jqFormat){
        if(StringUtil.isEmpty(jqFormat)){
            return value;
        }
        if(jqFormat.indexOf("*")>=0){
            jqFormat=jqFormat.replaceAll("\\*", value.length()+"");
        }
        String[] jqArray=jqFormat.split(",");
        String startFormat=jqArray[0];
        String endFormat=jqArray[1];
        Integer startIndex=Integer.parseInt(startFormat);
        Integer endIndex=Integer.parseInt(endFormat);
        if(endIndex<0){
            startIndex=(startIndex+endIndex);
            endIndex=(startIndex-endIndex);
        }else if(endIndex>0){
            endIndex=startIndex+endIndex;
        }else{
//            throw new PlatformException("字符串工具类获取后缀信息异常", PlatformExceptionEnum.JE_CORE_UTIL_STRING_GETSUBVALUE_ERROR,new Object[]{value,jqFormat});
            System.out.println("数据【"+value+"】解析出错，请查看格式【"+jqFormat+"】是否正确！");
        }
        return value.substring(startIndex, endIndex);
    }
    /**
     * 移除多个逗号分割的逗号字符   错误情况例如： ,1,2    1,,2   1,2,  经处理后返回  1,2
     * @param value
     * @return
     */
    public static String replaceSplit(String value){
        if(StringUtil.isEmpty(value)){
            return "";
        }
        while(value.indexOf(",,")>=0){
            value=value.replaceAll(",,", ",");
        }
        if(value.indexOf(",")==0){
            value=value.substring(1);
        }
        if(value.length()>0 && value.lastIndexOf(",")==(value.length()-1)){
            value=value.substring(0,value.length()-1);
        }
        return value;
    }
    /**
     * 解析表达式
     * @param value
     * @param varSet
     * @return
     */
    public static String parseKeyWord(String value,Set<Entry> varSet){
        for(Entry<String, Object> entry:varSet){
            while(value.indexOf("{"+entry.getKey()+"}")!=-1){
                value=value.replace("{"+entry.getKey()+"}", StringUtil.getDefaultValue(entry.getValue(), ""));
            }
        }
        return value;
    }
    /**
     * 字符串转Unicode
     * @param str
     * @return
     */
    public static String enUnicode(String str){
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8); //取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }
    /**
     * Unicode转字符串
     * @param str 待转字符串
     * @return 普通字符串
     */
    public static String deUnicode(String str)
    {
        str = (str == null ? "" : str);
        if (str.indexOf("\\u") == -1)//如果不是unicode码则原样返回
            return str;

        StringBuffer sb = new StringBuffer(1000);

        for (int i = 0; i < str.length();)
        {
            String strTemp = str.substring(i, i + 6);
            String value = strTemp.substring(2);
            int c = 0;
            for (int j = 0; j < value.length(); j++)
            {
                char tempChar = value.charAt(j);
                int t = 0;
                switch (tempChar)
                {
                    case 'a':
                        t = 10;
                        break;
                    case 'b':
                        t = 11;
                        break;
                    case 'c':
                        t = 12;
                        break;
                    case 'd':
                        t = 13;
                        break;
                    case 'e':
                        t = 14;
                        break;
                    case 'f':
                        t = 15;
                        break;
                    default:
                        t = tempChar - 48;
                        break;
                }

                c += t * ((int) Math.pow(16, (value.length() - j - 1)));
            }
            sb.append((char) c);
            i = i + 6;
        }
        return sb.toString();
    }
    /**
     * 转化字符串为十六进制编码
     * @param s
     * @param mark 是否启用16进制标记
     * @return
     */
    public static String enHex(String s,Boolean mark) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return (mark==false?"":"0x") + str;// 0x表示十六进制
    }

    /**
     * 转换十六进制编码为字符串
     * @param s
     * @return
     */
    public static String deHex(String s) {
        if ("0x".equals(s.substring(0, 2))) {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                throw new PlatformException("字符串工具类解密字符异常",PlatformExceptionEnum.JE_CORE_UTIL_STRING_DEHEX_ERROR,new Object[]{s},e);
            }
        }

        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            throw new PlatformException("字符串工具类解密字符异常",PlatformExceptionEnum.JE_CORE_UTIL_STRING_DEHEX_ERROR,new Object[]{s},e1);
        }
        return s;
    }
    /**
     * 获取clob的值
     * @param value
     * @return
     */
    public static String getClobValue(Object value){
        String valueStr="";
        if(value instanceof Clob){
            try {
                Clob clob=(Clob)value;
                valueStr = clob.getSubString(new Long(1), Integer.parseInt(new Long(clob.length()).toString()));
            } catch (NumberFormatException e) {
                logger.error("字符串工具类获取Clob字段值异常",e);
            } catch (SQLException e) {
                logger.error("字符串工具类获取Clob字段值异常",e);
            }
        }else if(value!=null){
            valueStr=value.toString();
        }
        return valueStr;
    }
    /**
     * 判断指定字符串值逻辑是否为真，'false'(不区分大小写)，'0'，''，null返回false其他返回true。
     * @param value 判断的字符串值。
     * @return 布尔值。
     */
    public static boolean getBool(String value) {
        if (value == null || value.equalsIgnoreCase("false") || value.equals("0") || value.isEmpty())
            return false;
        else
            return true;
    }
    /**
     * 对BASE64编码的字符串解码，并生成解码后的字节数组。
     * @param data 需要解码的字符串。
     * @return 使用BASE64解码的字节数组。
     * @throws Exception 解码过程发生异常。
     */
    public static byte[] decodeBase64(String data) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        InputStream base64is;

        base64is = MimeUtility.decode(is, "base64");
        try {
            IOUtils.copy(base64is, os);
        }catch (Exception e){
            throw new PlatformException("字符串工具类解密base64异常",PlatformExceptionEnum.JE_CORE_UTIL_STRING_DEBASE64_ERROR,new Object[]{data},e);
        }finally{
            FileOperate.close(is,os,null,null);
            FileOperate.close(base64is,null,null,null);
        }
        return os.toByteArray();
    }
    /**
     * 把以字符串形式表达的布尔值转换为对应的数字。字符串比较将忽略大小写。
     * @param value 以字符串表达的布尔值。
     * @return 布尔值对应的数字，"true"转换为"1", "false"转换为"0"，其他值保持不变。
     */
    public static String convertBool(String value) {
        if ("true".equalsIgnoreCase(value))
            return "1";
        else if ("false".equalsIgnoreCase(value))
            return "0";
        else
            return value;
    }
    /**
     * 从Reader对象读取字符串。读取完成后关闭reader。
     * @param reader Reader对象。
     * @return 读取的字符串。
     * @throws IOException 读取过程发生异常。
     */
    public static String readString(Reader reader) throws IOException {
        try {
            char buf[] = new char[bufferSize];
            StringBuilder sb = new StringBuilder();
            int len;

            while ((len = reader.read(buf)) > 0) {
                sb.append(buf, 0, len);
            }
            return sb.toString();
        } finally {
            reader.close();
        }
    }
    /**
     * 获取默认值
     * @param value
     * @return
     */
    public static String getVarDefaultValue(String value){
        if(StringUtil.isNotEmpty(value)){
            value=codeToValue(value);
            Set<Entry> varSet=new HashSet<Entry>();
            //加入登录信息
            EndUser currentUser=SecurityUserHolder.getCurrentUser();
            varSet.addAll(SecurityUserHolder.getCurrentInfo(currentUser).entrySet());
            //加入用户变量
            varSet.addAll(FrontCacheManager.getCacheValues().entrySet());
            varSet.addAll(BackCacheManager.getCacheValues().entrySet());
            //加入系统设置
            varSet.addAll(WebUtils.getAllSysVar().entrySet());
            for(Entry entry:varSet){
                String key=entry.getKey()+"";
                if(("{"+key+"}").equals(key)){
                    value=entry.getValue()+"";
                }
            }
            return value;
        }else{
            return "";
        }
    }

    /**
     * 字符串格式化
     *
     *  示例：
     *  String format = StringUtil.format("{0}-{1}-{2}", "a", "b", "c");
     *  format 值为  "a-b-c"
     * @param format
     * @return
     */
    public static String format(String format, Object... args){

        if(isEmpty(format)) return null;
        for (int i = 0; i < args.length; i++) {
            format = format.replaceAll("\\{"+i+"\\}", args[i]!=null ? args[i].toString() : "null");
        }
        return format;
    }
    public static String buildExcpetionStr(Exception e){
        StringWriter sw = new StringWriter();
        PrintWriter pw=null;
        String exceptionStr="";
        try {
            pw = new PrintWriter(sw, true);

            e.printStackTrace(pw);
            String stackTrace = sw.toString();
            exceptionStr= stackTrace.replaceAll("\n", "<br />&nbsp;&nbsp;&nbsp;&nbsp;");
        }catch (Exception e1){
            throw new PlatformException("字符串工具类获取异常信息异常",PlatformExceptionEnum.JE_CORE_UTIL_STRING_EXCEPTION_ERROR,new Object[]{e.getMessage()});
        }finally {
            if(pw!=null){
                pw.close();
            }
        }
        return exceptionStr;
    }
    public static void main(String[] args) throws Exception {
//		Interpreter i = new Interpreter();
//		SimpleDateFormat sdf = new SimpleDateFormat(WebUtils.getConfigVar("sys.commons.dateFormat"));
//		SimpleDateFormat stf = new SimpleDateFormat(WebUtils.getConfigVar("sys.commons.timeFormat"));
//		i.set("now", new Date());
//		i.set("dateFormat", sdf);
//		i.set("timeFormat", stf);
//		System.out.println(i.eval("dateFormat.format(now)"));
//		System.out.println(i.eval("timeFormat.format(now)"));
//		String v="工程\\";
//		 if(StringUtil.isNotEmpty(v) && v.indexOf("\\\\")!=-1){
//			v=v.replaceAll("\\\\","");
//		  }
//		 System.out.println(v);
//		System.out.println(v.lastIndexOf(".0"));
//		System.out.println(v.substring(0,v.length()-2));
//		String val="callback( {\"client_id\":\"101372778\",\"openid\":\"0A50FBBAFE3DD345980F0CBE10E924AF\"} );";
//		System.out.println(val.substring(val.indexOf("callback(")+9, val.lastIndexOf(")")).trim());
        String code="0102";
        System.out.println(code.substring(code.length()-2));
    }

}
