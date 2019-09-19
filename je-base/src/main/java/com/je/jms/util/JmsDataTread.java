package com.je.jms.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.je.core.service.PCDynaServiceTemplate;
import com.je.core.util.bean.DynaBean;
/**
 * 持久数据库数据线程
 * @author zhangshuaipeng
 *
 */
public class JmsDataTread extends Thread  {
	private Collection<DynaBean> lists=new ArrayList<DynaBean>();
	private PCDynaServiceTemplate serviceTemplate;
	private String oper="insert";
	public JmsDataTread(Collection<DynaBean> lists,
			PCDynaServiceTemplate serviceTemplate, String oper) {
		super();
		this.lists = lists;
		this.serviceTemplate = serviceTemplate;
		this.oper = oper;
	}
	/**
	 * 线程启动
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(DynaBean dynaBean:lists){
			if("update".equals(oper)){
				serviceTemplate.update(dynaBean);
			}else if("insert".equals(oper)){
				serviceTemplate.insert(dynaBean);
			}
		}
	}
	public Collection<DynaBean> getLists() {
		return lists;
	}
	public void setLists(Collection<DynaBean> lists) {
		this.lists = lists;
	}
	public PCDynaServiceTemplate getServiceTemplate() {
		return serviceTemplate;
	}
	public void setServiceTemplate(PCDynaServiceTemplate serviceTemplate) {
		this.serviceTemplate = serviceTemplate;
	}
	public String getOper() {
		return oper;
	}
	public void setOper(String oper) {
		this.oper = oper;
	}
	
	
}
