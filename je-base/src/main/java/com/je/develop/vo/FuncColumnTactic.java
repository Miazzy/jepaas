package com.je.develop.vo;
/**
 * 表格列策略类
 * @author YUNFENGCHENG
 *
 */
public class FuncColumnTactic {
	/**策略名称*/
	private String name;
	/**策略配置项*/
	private FuncColumnTacticInfo v;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public FuncColumnTacticInfo getV() {
		return v;
	}
	public void setV(FuncColumnTacticInfo v) {
		this.v = v;
	}
	
}
