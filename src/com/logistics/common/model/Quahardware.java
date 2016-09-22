package com.logistics.common.model;

import java.io.Serializable;

/**
 * 质检加密gson用
 * @author Administrator
 *
 */
public class Quahardware implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 质检加密硬件
	 */
	private String encrypHardware;
	/**
	 * IC卡读写器数量
	 */
	private String icRwCount;
	/**
	 * IC卡数量
	 */
	private String icCount;
	public String getEncrypHardware() {
		return encrypHardware;
	}
	public void setEncrypHardware(String encrypHardware) {
		this.encrypHardware = encrypHardware;
	}
	public String getIcRwCount() {
		return icRwCount;
	}
	public void setIcRwCount(String icRwCount) {
		this.icRwCount = icRwCount;
	}
	public String getIcCount() {
		return icCount;
	}
	public void setIcCount(String icCount) {
		this.icCount = icCount;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "icRwCount="+icRwCount+",icCount="+icCount+",encrypHardware="+encrypHardware;
	}
}
