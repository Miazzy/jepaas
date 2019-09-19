package com.je.core.util;

import com.je.core.exception.PlatformException;
import com.je.core.exception.PlatformExceptionEnum;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 日历节假日工具类
 * @author zhangshuaipeng
 *
 */
public class CalendarHoliday {

	/**
	 * 节日list
	 */
	private List<Calendar> holidayList = new ArrayList<Calendar>();
	/**
	 * 节假日
	 */
	private String[] sxrs=new String[]{};
	//上班开始时间   H
	private int start=8;
	//下班结束时间  H
	private int end=18;
	 /**  
	  *   
	  * <p>Title: addDateByWorkDay </P>  
	  * <p>Description: TODO  计算相加day天，并且排除节假日和周末后的日期</P>  
	  * @param calendar  当前的日期  
	  * @param day  相加天数  
	  * @return     
	  * return Calendar    返回类型   返回相加day天，并且排除节假日和周末后的日期  
	  * throws   
	  * date 2014-11-24 上午10:32:55  
	  */  
	 public Calendar addDateByWorkDay(Calendar calendar,int day){  

	     try {
	        for (int i = 0; i < day; i++) {  
	              
	             calendar.add(Calendar.DAY_OF_MONTH, 1);  
	               
	             if(checkHoliday(calendar)){  
	                 i--;  
	             }  
	        }  
	        return calendar;  
	          
	    } catch (Exception e) {
	       throw new PlatformException("计算相加day天，并且排除节假日和周末后的日期", PlatformExceptionEnum.JE_CORE_UTIL_CALENDARHOLIDAY_ERROR,new Object[]{calendar,day},e);
	    }
	 }
	 /**  
	  *   
	  * <p>Title: addDateByWorkDay </P>  
	  * <p>Description: TODO  计算相加day天，并且排除节假日和周末后的日期</P>  
	  * @param calendar  当前的日期  
	  * @param hour  相加小时数  
	  * @return     
	  * return Calendar    返回类型   返回相加day天，并且排除节假日和周末后的日期  
	  * throws   
	  * date 2014-11-24 上午10:32:55  
	  */  
	 public Calendar addHourByWorkDay(Calendar calendar,int hour){  
	       
	     try {  
	        for (int i = 0; i < hour; i++) {  
	              
	             calendar.add(Calendar.HOUR_OF_DAY, 1);  
                 int h=calendar.get(Calendar.HOUR_OF_DAY);
               	 if(h>end && h<start){
               		 i--;
               	 }
	             if(checkHoliday(calendar)){  
	                 i--;  
	             }  
	        }  
	        return calendar;  
	          
	    } catch (Exception e) {
			 throw new PlatformException("计算相加hour时，并且排除节假日和周末后的日期", PlatformExceptionEnum.JE_CORE_UTIL_CALENDARHOLIDAY_ERROR,new Object[]{calendar,hour},e);
	    }  
	 }
	/**  
	  *   
	  * <p>Title: checkHoliday </P>  
	  * <p>Description: TODO 验证日期是否是节假日</P>  
	  * @param calendar  传入需要验证的日期  
	  * @return   
	  * return boolean    返回类型  返回true是节假日，返回false不是节假日  
	  * throws   
	  * date 2014-11-24 上午10:13:07  
	  */  
	 public boolean checkHoliday(Calendar calendar) throws Exception{  
	       
	     //判断日期是否是节假日  
	     for (Calendar ca : holidayList) {  
	        if(ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&  
	                ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)&&  
	                ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)){  
	            return true;
	        }  
	    }
	    //判断日期是否是假日
	    int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK)-1; 
	    if(dayOfWeek==7){
	    	dayOfWeek=0;
	    }
		 if(!ArrayUtils.contains(this.sxrs,dayOfWeek+"")) {
			 return true;
		 }     
	     return false;  
	 } 
	 /**  
	  *   
	  * <p>Title: initHolidayList </P>  
	  * <p>Description: TODO  把所有节假日放入list</P>  
	  * @param date  从数据库查 查出来的格式2014-05-09  
	  * return void    返回类型   
	  * throws   
	  * date 2014-11-24 上午10:11:35  
	  */  
	public void initHolidayList( String date){  
	        String [] da = date.split("-");  
	        Calendar calendar = Calendar.getInstance();  
	        calendar.set(Calendar.YEAR, Integer.parseInt(da[0]));
	        calendar.set(Calendar.MONTH, Integer.parseInt(da[1]) - 1);//月份比正常小1,0代表一月
	        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(da[2]));
	        holidayList.add(calendar);  
	} 
	public void initXqt(String[] xqrs,int start,int end){
		this.start=start;
		this.end=end;
		this.sxrs=xqrs;
	}
}
