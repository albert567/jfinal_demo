package com.logistics.common.model;

import java.io.Serializable;

/**
 * 料场gson用
 * @author Administrator
 *
 */
public class Mathardware implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 料场硬件
	 */
	private String maHardware;
	/**
	 * 使用人数
	 */
	private String maPersonCount;
	public String getMaHardware() {
		return maHardware;
	}
	public void setMaHardware(String maHardware) {
		this.maHardware = maHardware;
	}
	public String getMaPersonCount() {
		return maPersonCount;
	}
	public void setMaPersonCount(String maPersonCount) {
		this.maPersonCount = maPersonCount;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "maHardware="+maHardware+",maPersonCount="+maPersonCount;
	}
}
