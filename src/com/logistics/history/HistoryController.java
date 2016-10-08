package com.logistics.history;

import com.logistics.common.model.Baseinfo;
import com.logistics.common.model.Doorhardware;
import com.logistics.common.model.Factoryinfo;
import com.logistics.common.model.Weighthardware;
import com.logistics.common.model.base.BaseHistory;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.core.Controller;

/**
 * HistoryController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class HistoryController extends Controller {
	public void index() {
		BaseHistory history = new BaseHistory();
		int baseId = getParaToInt();
		history.setBaseInfo(Baseinfo.dao.findById(baseId));
		history.setFacList(Factoryinfo.dao.findAllByBaseId(baseId));
		int factoryCount = history.getBaseInfo().getFactoryCount().intValue();
		List<List<String>> allDoors = new ArrayList<List<String>>();
		List<String> doors = new ArrayList<String>();
		List<Doorhardware> doorHardware = new ArrayList<Doorhardware>();
		List<List<String>> allWeights = new ArrayList<List<String>>();
		List<String> weights = new ArrayList<String>();
		List<Weighthardware> weightHardware = new ArrayList<Weighthardware>();
		for(int i=1;i<=factoryCount;i++){
			doors.clear();
			weights.clear();
			doorHardware = Doorhardware.dao.findByBaseIdAndIndex(baseId, i);
			for(Doorhardware hardware:doorHardware){
				doors.add(hardware.getDoorHardware());
			}
			allDoors.add(doors);
			weightHardware = Weighthardware.dao.findByBaseIdAndIndex(baseId, i);
			for(Weighthardware hardware:weightHardware){
				weights.add(hardware.getWeiHardware());
			}
			allWeights.add(weights);
		}
		history.setAllDoors(allDoors);
		history.setAllWeights(allWeights);
		renderJson(history);
	}
	
}


