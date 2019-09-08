package com.je.core.entity;

import javax.persistence.MappedSuperclass;

import com.je.core.ann.entity.FieldChineseName;

/**
 * 包含扩展字段的树型基础实体
 * @author chenmeng
 *
 */
@MappedSuperclass
public abstract class ExtendTreeBaseEntity extends TreeBaseEntity {
	@FieldChineseName(value="备用字段1")
	private String ext01;
	@FieldChineseName(value="备用字段2")
	private String ext02;
	@FieldChineseName(value="备用字段3")
	private String ext03;
	@FieldChineseName(value="备用字段4")
	private String ext04;
	@FieldChineseName(value="备用字段5")
	private String ext05;
	
	public String getExt01() {
		return ext01;
	}
	public void setExt01(String ext01) {
		this.ext01 = ext01;
	}
	public String getExt02() {
		return ext02;
	}
	public void setExt02(String ext02) {
		this.ext02 = ext02;
	}
	public String getExt03() {
		return ext03;
	}
	public void setExt03(String ext03) {
		this.ext03 = ext03;
	}
	public String getExt04() {
		return ext04;
	}
	public void setExt04(String ext04) {
		this.ext04 = ext04;
	}
	public String getExt05() {
		return ext05;
	}
	public void setExt05(String ext05) {
		this.ext05 = ext05;
	}
}
