package com.je.core.util;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * 系统通用反射工具类
 * @author YUNFENGCHENG
 * 2011-8-30 下午03:30:00
 */
public class ReflectionUtils {
    private static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);
    private ReflectionUtils(){};
    /**
     * 实例化此类
     * 研发部:云凤程
     * 2011-8-30 下午03:33:36
     * @return
     */
    public static ReflectionUtils getInstance(){
        return ReflectionUtilsHolder.REFLECTION_UTILS;
    }
    /**
     * 内部静态类用于实例化本类
     * @author YUNFENCGHENG
     * 2011-8-30 下午03:48:07
     */
    private static class ReflectionUtilsHolder{
        private static final ReflectionUtils REFLECTION_UTILS = new ReflectionUtils();
        /**
         * 函数集合缓存
         */
        private static Map<String,Method[]> methods = new HashMap<String, Method[]>();
        /**
         * 字段集合缓存
         */
        private static Map<String, Field[]> fields = new HashMap<String, Field[]>();
        /**
         * 全部（包括父类）符合Ext-JS要求的字段集合缓存
         */
        private static Map<String, Field[]> validFields = new HashMap<String, Field[]>();
        /**
         * 函数的注解集合缓存
         */
        private static Map<String, Annotation[]> anns4method = new HashMap<String, Annotation[]>();
        /**
         * 字段的注解集合缓存
         */
        private static Map<String, Annotation[]> anns4field = new HashMap<String, Annotation[]>();
        /**
         * 类的注解集合缓存
         */
        private static Map<String, Annotation[]> anns4class = new HashMap<String, Annotation[]>();
    }
    /**
     * 得到类的函数集合
     * 研发部:云凤程
     * 2011-8-30 下午04:35:31
     * @param c
     * @return
     */
    public Method[] getClassMethods(Class<?> c){
        String className = c.getName();
        if(ReflectionUtilsHolder.methods.get(className) != null){
            return ReflectionUtilsHolder.methods.get(className);
        }else{
            Method[] methods = c.getMethods();
            ReflectionUtilsHolder.methods.put(className, methods);
            return methods;
        }
    }
    /**
     * 得到类的属性集合
     * @param c
     * @param itself 是否包含继承类信息
     * @return
     */
    public Field[] getClassFields(Class<?> c, boolean itself){
        String className = c.getName();
        if(itself) {
            if(ReflectionUtilsHolder.fields.get(className) != null){
                return ReflectionUtilsHolder.fields.get(className);
            }else{
                Field[] fields = c.getDeclaredFields();
                ReflectionUtilsHolder.fields.put(className, fields);
                return fields;
            }
        } else {
            if(ReflectionUtilsHolder.validFields.get(className) != null) {
                return ReflectionUtilsHolder.validFields.get(className);
            } else {
                Field[] f = null;
                List<Field> fields = new ArrayList<Field>();
                getAllDeclaredFields(c, fields);
                f = new Field[fields.size()];
                fields.toArray(f);
                ReflectionUtilsHolder.validFields.put(className, f);
                return f;
            }
        }

    }

    /**
     * 从c类中取得全部字段,包括父类
     * @param c
     * @param fields
     */
    private void getAllDeclaredFields(Class<?> c, List<Field> fields) {
        Field[] fieldArray = c.getDeclaredFields();
        Collections.addAll(fields, fieldArray);
        Class<?> parent = c.getSuperclass();
        if(!parent.equals(Object.class)) {
            getAllDeclaredFields(parent, fields);
        }
    }

    /**
     * 得到类的注解集合
     * 研发部:云凤程
     * 2011-8-30 下午04:35:31
     * @param c
     * @return
     */
    public Annotation[] getClassAnns(Class<?> c){
        String className = c.getName();
        if(ReflectionUtilsHolder.anns4class.get(className) != null){
            return ReflectionUtilsHolder.anns4class.get(className);
        }else{
            Annotation[] anns = c.getAnnotations();
            ReflectionUtilsHolder.anns4class.put(className, anns);
            return anns;
        }
    }
    /**
     * 得到函数的注解集合
     * 研发部:云凤程
     * 2011-8-30 下午05:37:15
     * @param c
     * @param method
     * @return
     */
    public Annotation[] getMethodAnns(Class<?> c,Method method){
        String className = c.getName()+"|"+method.getName();
        if(ReflectionUtilsHolder.anns4method.get(className) != null){
            return ReflectionUtilsHolder.anns4method.get(className);
        }else{
            Annotation[] anns  = method.getAnnotations();
            ReflectionUtilsHolder.anns4method.put(className, anns);
            return anns;
        }

    }
    /**
     * 得到属性的注解集合
     * 研发部:云凤程
     * 2011-8-30 下午05:49:27
     * @param c
     * @param field
     * @return
     */
    public Annotation[] getfieldAnns(Class<?> c,Field field){
        String className = c.getName()+"|"+field.getName();
        if(ReflectionUtilsHolder.anns4field.get(className) != null){
            return ReflectionUtilsHolder.anns4field.get(className);
        }else{
            Annotation[] anns  = field.getAnnotations();
            ReflectionUtilsHolder.anns4field.put(className, anns);
            return anns;
        }
    }
    /**
     * 反射执行类中函数
     * 研发部:云凤程
     * 2011-8-30 下午03:36:36
     * @param owner  函数所在的类
     * @param methodName 函数名称
     * @param args 参数
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Object invokeMethod(Object owner, String methodName, Object[] args){
        Class ownerClass = owner.getClass();
        Class[] argsClass = null;
        if(args != null){
            argsClass = new Class[args.length];
            for (int i = 0, j = args.length; i < j; i++) {
                if(args[i] != null){
                    if("class java.lang.Boolean".equals(""+args[i].getClass())){
                        argsClass[i] = boolean.class;
                    } else {
                        argsClass[i] = args[i].getClass();
                    }
                }else{
                    argsClass[i] = Object.class;
                }
            }
        }
        //后的函数对象
        Method method = null;
        try {
            method = ownerClass.getMethod(methodName, argsClass);
        } catch (Exception e) { //异常出现默认就为Object对象
            logger.error("获得"+owner.toString()+"类方法【"+methodName+"】失败,系统自动获取Object参数方法: " + e);
            try {
                method = ownerClass.getMethod(methodName, Object.class);
            } catch (Exception e2) {
                throw new PlatformException("反射工具类获取方法异常", PlatformExceptionEnum.JE_CORE_UTIL_REFLECTION_GETMETHOD_ERROR,new Object[]{owner,toString(),methodName,args},e2);
            }
//            e.printStackTrace();
        }
        Object object = null;
        try {
            object = method.invoke(owner, args);
        } catch (Exception e) {
            throw new PlatformException("反射工具类执行方法异常",PlatformExceptionEnum.JE_CORE_UTIL_REFLECTION_INVOKE_ERROR,new Object[]{owner,toString(),methodName,args},e);
//            logger.error("执行函数["+method.getName()+"]出错: "+ e);
//            e.printStackTrace();
        }
        return object;
    }
    /**
     * 执行无参数函数  异常抛出
     * @param owner
     * @param methodName
     * @return
     */
    public Object invokeMethod(Object owner, String methodName) throws Exception{
        Class ownerClass = owner.getClass();
        Method method = ownerClass.getMethod(methodName, null);
        Object object = method.invoke(owner, null);
        return object;
    }
    /**
     * 为一个对象的某个字段执行get方法
     * @param owner
     * @param fieldName
     * @param args
     * @return
     */
    public Object invokeGetMethod(Object owner, String fieldName, Object[] args) {
        String readMethod = EntityUtils.getInstance().getReadMethod(fieldName);
        return invokeMethod(owner, readMethod, args);
    }

    /**
     * 为一个对象的某个字段执行set方法
     * @param owner
     * @param fieldName
     * @param args
     * @return
     */
    public void invokeSetMethod(Object owner, String fieldName, Object[] args) {
        String writeMethod = EntityUtils.getInstance().getWriteMethod(fieldName);
        invokeMethod(owner, writeMethod, args);
    }

    @SuppressWarnings("unchecked")
    public boolean hasField(Class c, String fieldName) {
        try {
            c.getDeclaredField(fieldName);
        } catch (SecurityException e) {
            return false;
        } catch (NoSuchFieldException e) {
            return false;
        }
        return true;
    }

    /**
     * 查找一个类型中拥有某个Annotation注解的全部字段
     * @param objType
     * @param annType
     * @return
     */
    public List<Field> getHasAnnForFields(Class<? extends Object> objType, Class<? extends Annotation> annType) {
        List<Field> hasAnnFields = new ArrayList<Field>();
        Field[] fields = objType.getDeclaredFields();
        for(Field f : fields) {
            Annotation annotation = f.getAnnotation(annType);
            if(null != annotation) {
                hasAnnFields.add(f);
            }
        }
        return hasAnnFields;
    }
    /**
     * 根据实体名称获取Class对象
     * @param className
     * @return
     */
    public static Class<?> getClassByName(String className){
        Class<?> c=null;
        try{
            c=Class.forName(className);
        }catch(Exception e){
            throw new PlatformException("反射工具类根据实体名称获取类异常",PlatformExceptionEnum.JE_CORE_UTIL_REFLECTION_GETCLASS_ERROR,new Object[]{className},e);
        }
        return c;
    }

}







