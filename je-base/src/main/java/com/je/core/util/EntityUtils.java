package com.je.core.util;

import com.je.core.ann.entity.TreeNode;
import com.je.core.ann.entity.TreeSelectNode;
import com.je.core.constants.tree.TreeNodeType;
import com.je.core.entity.BaseEntity;
import com.je.core.entity.EntityInfo;
import com.je.core.entity.TreeBaseEntity;
import com.je.core.entity.extjs.JSONTreeNode;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * 实体工具类,有关对实体的一起公共化操作
 * @author YUNFENGCHENG
 * 2011-8-30 下午03:21:26
 */
public class EntityUtils {
	private static ReflectionUtils reflectionUtils = null;
	private static Logger logger = LoggerFactory.getLogger(EntityUtils.class);
	private EntityUtils(){
		//初始化对象的时候实例化反射工具类
		reflectionUtils = ReflectionUtils.getInstance();
	}
	/**
	 * 实例化此类
	 * 研发部:云凤程 
	 * 2011-8-30 下午03:26:33
	 * @return
	 */
	public static EntityUtils getInstance(){
		return EntityUtilsHolder.ENTITY_UTILS;
	}
	/**
	 * 得到实体类的结构信息
	 * 研发部:云凤程 
	 * 2011-8-30 下午04:25:59
	 * @param object
	 * @return
	 */
	public EntityInfo getEntityInfo(Object object){
		String className = null;
		if(String.class.equals(object.getClass())){
			className = object.toString();
		}else{
			className = ((Class<?>)object).getName();
		}
		if(EntityUtilsHolder.classEntitys.containsKey(className)){
			//缓存中去取
			EntityInfo entityInfo = EntityUtilsHolder.classEntitys.get(className);
			return entityInfo;
		}else{
			//loading到缓存中并且返回类信息
			EntityInfo entityInfo = loadingEntityInfo(object);
			entityInfo.init();
			EntityUtilsHolder.classEntitys.put(className, entityInfo);
			return entityInfo;
		}
	}
	/**
	 * 如果OBJ对象不存在相同的属性，将entity的属性拷贝到OBJ对象。
	 * 研发部:云凤程 
	 * 2011-8-30 下午05:09:08
	 * @param obj
	 * @param entity
	 * @return
	 */
	public Object copyNewField(Object obj,Object entity){
		//1.得到entity的属性集合
		Field[] fields = getEntityInfo(entity.getClass()).getFields();
		//2.遍历entity的每一个属性的数值,如果不是null的话就把值付给OBJ
		Object value = null;
		for(Field field : fields){
			value = reflectionUtils.invokeMethod(entity, getReadMethod(field.getName()), null);
			if(value != null && !(value instanceof BaseEntity) && !(value instanceof Collection)){//只保存基础信息值
				logger.debug(field.getType().getSimpleName());
				reflectionUtils.invokeMethod(obj, getWriteMethod(field.getName()), new Object[]{value});
			} else if(value != null && value instanceof BaseEntity) { // 如果关联对象主键有值，则尝试更新
				String sId = EntityUtils.getInstance().getEntityIdValue(value);
				if(StringUtil.isNotEmpty(sId)) {
					reflectionUtils.invokeMethod(obj, getWriteMethod(field.getName()), new Object[]{value});
				}
			} else {
				logger.debug("ignore field : " + field.getName());
			}
		}
		//3.返回原对象
		return obj;
	}
	/**
	 * 装载类的结构信息
	 * 研发部:云凤程 
	 * 2011-8-30 下午04:53:13
	 * @param object
	 * @return
	 * @throws  
	 * @throws  
	 */
	private EntityInfo loadingEntityInfo(Object object){
		Class<?> c = null;
		if(String.class.equals(object.getClass())){
			try {
				c = Class.forName(object.toString()).newInstance().getClass();
			} catch (Exception e) {
				logger.error("没有找到相应的类["+object.toString()+"]ClassNotFoundException");
				e.printStackTrace();
			}
		}else{
			c = (Class<?>)object;
		}
		//1.实例化这个类
		EntityInfo entityInfo = new EntityInfo();
		//2.装载函数集合
		entityInfo.setMethods(reflectionUtils.getClassMethods(c));
		//3.装载idName
		Annotation[] anns = null;
		Method[] methods = entityInfo.getMethods();
		if(methods == null || methods.length == 0){
			logger.error("类"+c.getSimpleName()+"没有函数,或者程序出错类信息装载失败");
		}else{
			for(Method method : entityInfo.getMethods()){
				anns = reflectionUtils.getMethodAnns(c,method);
				for(Annotation ann : anns){
					if("Id".equals(ann.annotationType().getSimpleName())){
						entityInfo.setIdName(method.getName().substring(3));
						break;
					}
				}
			}
		}
		//4.装载属性集合
		entityInfo.setFields(reflectionUtils.getClassFields(c, false));
		//5.装载类的类类
		entityInfo.setC(c);
		//6.装载类的注解
		entityInfo.setClassAnns(c.getAnnotations());
		//7.装载类的全部基本类型字段
		entityInfo.setAllBaseFields(entityInfo.getAllBaseFields());
		//8.验证类的配置信息是否正确
		if(entityInfo.checking()){
			return entityInfo;
		}else{
			logger.error("类["+c.getSimpleName()+"]验证ORM配置没有通过");
			return null;
		}
	}
	/**
	 * 内部静态类用于实例化本类
	 * @author YUNFENCGHENG
	 * 2011-8-30 下午03:48:07
	 */
	private static class EntityUtilsHolder{
		private static final EntityUtils ENTITY_UTILS = new EntityUtils();
		/**
		 * 装在这对类描述信息的集合
		 */
		private static Map<String, EntityInfo> classEntitys = new HashMap<String, EntityInfo>();
	}
	/**
	 * 得到属性的get方法
	 * 研发部:云凤程 
	 * 2011-8-30 下午05:52:15
	 * @param fName
	 * @return
	 */
	public String getReadMethod(String fName){
		return "get"+(fName.substring(0,1)).toUpperCase()+fName.substring(1, fName.length());
	}
	/**
	 * 得到属性的set方法
	 * 研发部:云凤程 
	 * 2011-8-30 下午05:52:37
	 * @param fName
	 * @return
	 */
	public String getWriteMethod(String fName){
		return "set"+(fName.substring(0,1)).toUpperCase()+fName.substring(1, fName.length());
	}
	/**
	 * 得到对象id的值
	 * 研发部:孙万祥
	 * @param entity
	 * @return
	 */
	public String getEntityIdValue(Object entity) {
		String idValue = null;
		if(null != entity) {
			String idName = EntityUtils.getInstance().getEntityIdName((BaseEntity)entity);
			if(StringUtils.isNotEmpty(idName)) {
				idValue = (String) reflectionUtils.invokeMethod(entity, this.getReadMethod(idName), null);
			}
			
			
		}
		return idValue;
	}
	
	/**
	 * 得到实体主键的名称
	 * @param entity
	 * @return
	 * 2012-2-7 chenmeng
	 */
	public String getEntityIdName(BaseEntity entity) {
		String idName = null;
		if(null != entity) {
			idName = this.getEntityInfo(entity.getClass()).getIdName();
		}
		return idName;
	}
	
	public void clearCache() {
		EntityUtilsHolder.classEntitys.clear();
	}
	
	public String getParentIdValue(TreeBaseEntity entity) {
		TreeBaseEntity treeEntity = entity.getParent();
		String parentIdValue = getEntityIdValue(treeEntity);
		return parentIdValue;
	}
	
	public JSONTreeNode buildJSONTreeNode(TreeBaseEntity treeEntity) {
		JSONTreeNode template = new JSONTreeNode();
		Field[] fields = loadingEntityInfo(treeEntity.getClass()).getFields();
		for(Field f : fields) {
			TreeNode treeNode = f.getAnnotation(TreeNode.class);
			if(null != treeNode) {
				if(TreeNodeType.ID.equals(treeNode.type())) {
					template.setId(f.getName());
				}else if(TreeNodeType.CODE.equals(treeNode.type())) {
					template.setCode(f.getName());
				}else if(TreeNodeType.TEXT.equals(treeNode.type())) {
					template.setText(f.getName());
				}else if(TreeNodeType.PARENT.equals(treeNode.type())) {
					template.setParent(f.getName());
				}else if(TreeNodeType.NODEINFO.equals(treeNode.type())) {
					template.setNodeInfo(f.getName());
				}else if(TreeNodeType.NODEINFOTYPE.equals(treeNode.type())) {
					template.setNodeInfoType(f.getName());
				}else if(TreeNodeType.NODETYPE.equals(treeNode.type())) {
					template.setNodeType(f.getName());
				}else if(TreeNodeType.ICON.equals(treeNode.type())) {
					template.setIcon(f.getName());
				}else if(TreeNodeType.ICONCLS.equals(treeNode.type())){
					template.setIconCls(f.getName());
				}else if(TreeNodeType.DISABLED.equals(treeNode.type())){
					template.setDisabled(f.getName());
				}else if(TreeNodeType.NODEPATH.equals(treeNode.type())){
					template.setNodePath(f.getName());
				}else if(TreeNodeType.PARENTPATH.equals(treeNode.type())){
					template.setParentPath(f.getName());
				}else if(TreeNodeType.HREF.equals(treeNode.type())){
					template.setHref(f.getName());
				}else if(TreeNodeType.HREFTARGET.equals(treeNode.type())){
					template.setHrefTarget(f.getName());
				}else if(TreeNodeType.DESCRIPTION.equals(treeNode.type())){
					template.setDescription(f.getName());
				}else if(TreeNodeType.ORDERINDEX.equals(treeNode.type())){
					template.setOrderIndex(f.getName());
				}else if(TreeNodeType.LAYER.equals(treeNode.type())){
					template.setOrderIndex(f.getName());
				}
			}else{	
				TreeSelectNode selectNode=f.getAnnotation(TreeSelectNode.class);
				if(selectNode!=null){
					template.setFieldCodes(template.getFieldCodes()+","+f.getName()+"");
				}
			}
		}
		return template;
		
	}
	
}





















