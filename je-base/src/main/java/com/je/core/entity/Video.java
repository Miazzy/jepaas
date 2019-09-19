package com.je.core.entity;
/**
 * 视频信息描述类
 * @author YUNFENGCHENG
 * 2011-9-19 下午05:04:49
 */
public class Video {
	private String flash;
	private String pic;
	private String time;
	@Override
	public String toString() {
		super.toString();
		return flash+": "+flash+"\n pic : "+pic+"\n time : "+time;
	}
	public String getFlash() {
		return flash;
	}
	public void setFlash(String flash) {
		this.flash = flash;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
}