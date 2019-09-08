package com.je.core.util;

import org.xvolks.jnative.JNative;
import org.xvolks.jnative.exceptions.NativeException;
import org.xvolks.jnative.Type;

/**
 * 实施数据工具类
 * @author zhangshuaipeng
 *
 */
public class TagUtil {
    public static JNative jnative=null;
    public static TagUtil getInstance(){
        return new TagUtil();
    }
    /**
     * 连接
     * @return
     * @throws NativeException
     * @throws IllegalAccessException
     */
    public JNative getConnection() throws NativeException, IllegalAccessException{
        if(jnative!=null){
            return jnative;
        }
        JNative jnat;
        System.loadLibrary("CtApi");//InterfaceFun是dll文件
        jnat = new JNative("CtApi","ctOpen");
        jnat.setRetVal(Type.LONG);
        jnat.setParameter(0, Type.STRING,WebUtils.getBackVar("SCADA_IP"));
        jnat.setParameter(1, Type.STRING,WebUtils.getBackVar("SCADA_USERNAME"));
        jnat.setParameter(2, Type.STRING,WebUtils.getBackVar("SCADA_PASSWORD"));
        jnat.setParameter(3, 0);
        jnat.invoke();
        long lt = Long.parseLong(jnat.getRetVal());
        if(lt != 0){//84564960
            TagUtil.jnative=jnat;
            return jnat;
        }else{
            return null;
        }
    }
    public void close() throws NativeException, IllegalAccessException{
        JNative jnativeClose = new JNative("CtApi","ctClose");
        jnativeClose.invoke();
        jnative=null;
    }
}
