package com.logistics.common.model.base;

import java.util.List;

import com.logistics.common.model.Baseinfo;
import com.logistics.common.model.Factoryinfo;

public class BaseHistory {
	private Baseinfo baseInfo;
	private List<Factoryinfo> facList;
	private List<List<String>> allDoors;
	private List<List<String>> allWeights;
	public Baseinfo getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(Baseinfo baseInfo) {
		this.baseInfo = baseInfo;
	}
	public List<Factoryinfo> getFacList() {
		return facList;
	}
	public void setFacList(List<Factoryinfo> facList) {
		this.facList = facList;
	}
	public List<List<String>> getAllDoors() {
		return allDoors;
	}
	public void setAllDoors(List<List<String>> allDoors) {
		this.allDoors = allDoors;
	}
	public List<List<String>> getAllWeights() {
		return allWeights;
	}
	public void setAllWeights(List<List<String>> allWeights) {
		this.allWeights = allWeights;
	}
}
