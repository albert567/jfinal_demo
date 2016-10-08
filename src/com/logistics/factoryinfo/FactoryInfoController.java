package com.logistics.factoryinfo;

import com.logistics.common.model.Doorhardware;
import com.logistics.common.model.Factoryinfo;
import com.logistics.common.model.Mathardware;
import com.logistics.common.model.Quahardware;
import com.logistics.common.model.Weighthardware;

import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

/**
 * FactoryInfoController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class FactoryInfoController extends Controller {
	public void index() {
		renderJson(Factoryinfo.dao.findAllByBaseId(getParaToInt(0,1)));
	}
	
	public void add() {
		
	}
	
	public void save() {
		String baseId = getPara("base_id");//父级ID
		System.out.println("baseId="+baseId);
		String facName = getPara("fac_name");//工厂名称
		String itemInOut = getPara("item_inout");//大宗物资进出厂门数
		String weiCount = getPara("wei_count");//计量衡器数量
		String ticCount = getPara("tic_count");//开单员/班
		String metCount = getPara("met_count");//计量员/班
		String isNoduty = getPara("is_noduty");//是否无人值守计量
		String nodMonitors = getPara("nod_monitors");//无人值守监控员/班
		String faIndex = getPara("fa_index");//工厂编号
		/*String ticHardware = getPara("tic_hardware");//开单室硬件选择
		String faIndex = getPara("fa_index");//工厂编号
		String monHardware = getPara("mon_hardware");//监控室硬件选择
		String quaHardware = getPara("qua_hardware");//质检加密硬件选择
		String icRwCount = getPara("ic_rw_count");//IC卡读写器数量
		String icCount = getPara("ic_count");//IC卡数量
		String matHardware = getPara("mat_hardware");//料场硬件选择
		String personCount = getPara("person_count");//料场使用人数
*/		
		Factoryinfo info = new Factoryinfo();
		info.set("id", "SEQ_ON_FACTORY.nextval");
		info.set("base_id", baseId);
		info.set("fac_name", facName);
		info.set("item_inout", itemInOut);
		info.set("wei_count", weiCount);
		info.set("tic_count", ticCount);
		info.set("met_count", metCount);
		info.set("is_noduty", isNoduty);
		info.set("nod_monitors", nodMonitors);
		info.set("fa_index", faIndex);
		/*info.set("tic_hardware", ticHardware);
		info.set("fa_index", faIndex);
		info.set("mon_hardware", monHardware);
		info.set("qua_hardware", quaHardware);
		info.set("ic_rw_count", icRwCount);
		info.set("ic_count", icCount);
		info.set("mat_hardware", matHardware);
		info.set("person_count", personCount);*/
		boolean flag = info.save();
		if(flag){
			renderJson(info.getId());
		}else{
			renderJson(-1);
		}
	}
	
	public Factoryinfo edit() {
		Factoryinfo info = Factoryinfo.dao.findById(getParaToInt());
		return info;
	}
	
	public void update() {
		String id = getPara("id");//工厂id
		String baseId = getPara("base_id");//父级ID
		String faIndex = getPara("fa_index");//工厂编号
		String facName = getPara("fac_name");//工厂名称
		String itemInOut = getPara("item_inout");//大宗物资进出厂门数
		String weiCount = getPara("wei_count");//计量衡器数量
		String ticCount = getPara("tic_count");//开单员/班
		String metCount = getPara("met_count");//计量员/班
		String isNoduty = getPara("is_noduty");//是否无人值守计量
		String nodMonitors = getPara("nod_monitors");//无人值守监控员/班
		
		Factoryinfo info = new Factoryinfo();
		info.set("id",id);
		info.set("base_id", baseId);
		info.set("fa_index", faIndex);
		info.set("fac_name", facName);
		info.set("item_inout", itemInOut);
		info.set("wei_count", weiCount);
		info.set("tic_count", ticCount);
		info.set("met_count", metCount);
		info.set("is_noduty", isNoduty);
		info.set("nod_monitors", nodMonitors);
		
		renderJson(info.update());
	}
	
	public void delete() {
		renderJson(Factoryinfo.dao.deleteById(getParaToInt(0),getParaToInt(1)));
	}
	
	public void saveOther(){
		Gson gson = new Gson();
		
		final int baseId = Integer.valueOf(getPara("base_id"));//报价主表id
		final int faIndex = Integer.valueOf(getPara("fa_index"));//工厂序号
		final List<String> ticHardware = gson.fromJson(getPara("tic_hardware"), 
				new TypeToken<List<String>>(){}.getType());//开单室硬件
		final List<String> monHardware = gson.fromJson(getPara("mon_hardware"), 
				new TypeToken<List<String>>(){}.getType());//监控室硬件
		final List<Quahardware> quaHardware = gson.fromJson(getPara("qua_hardware"), 
				new TypeToken<List<Quahardware>>(){}.getType());//质检加密硬件
		final List<Mathardware> matHardware = gson.fromJson(getPara("mat_hardware"), 
				new TypeToken<List<Mathardware>>(){}.getType());//料场硬件
		final List<String[]> doorHardware = gson.fromJson(getPara("door_hardware"), 
				new TypeToken<List<String[]>>(){}.getType());//门岗硬件
		final List<String[]> weiHardware = gson.fromJson(getPara("wei_hardware"), 
				new TypeToken<List<String[]>>(){}.getType());//计量衡器硬件
		
		System.out.println("ticHardware="+ticHardware);
		System.out.println("monHardware="+monHardware);
		boolean succeed = Db.tx(new IAtom(){
			public boolean run() throws SQLException {
				Factoryinfo info = null;
				boolean infoFlag = false;
				boolean doorFlag = false;
				boolean weightFlag = false;
				for(int i=1;i<=faIndex;i++){
					info = Factoryinfo.dao.findByBaseIdAndIndex(baseId,i);
					info.set("tic_hardware", ticHardware.get(i-1));//开单室硬件
					info.set("mon_hardware", monHardware.get(i-1));//监控室硬件
					
					Quahardware qua = quaHardware.get(i-1);
					info.set("qua_hardware", qua.getEncrypHardware());//质检加密硬件
					info.set("ic_rw_count", qua.getIcRwCount());//IC卡读写器数量
					info.set("ic_count", qua.getIcCount());//IC卡数量
					
					Mathardware mat = matHardware.get(i-1);
					info.set("mat_hardware", mat.getMaHardware());//料场硬件
					info.set("person_count", mat.getMaPersonCount());//使用人数
					infoFlag = info.update();
					
					Doorhardware.dao.deleteByBaseIdAndIndex(baseId,i);
					String[] doors = doorHardware.get(i-1);
					Doorhardware door = new Doorhardware();
					for(int j=0;j<doors.length;j++){
						door.set("id", "SEQ_ON_DOOR.nextval");
						door.set("base_id", baseId);
						door.set("fa_index", i);
						door.set("door_hardware", doors[j]);
						door.set("door_index", j+1);
						doorFlag = door.save();
						System.out.println("保存成功，doorHardware["+(i-1)+"]["+j+"]=" + doors[j]);
					}
					
					Weighthardware.dao.deleteByBaseIdAndIndex(baseId,i);
					String[] weights = weiHardware.get(i-1);
					Weighthardware weight = new Weighthardware();
					for(int j=0;j<weights.length;j++){
						weight.set("id", "SEQ_ON_WEIGHT.nextval");
						weight.set("base_id", baseId);
						weight.set("fa_index", i);
						weight.set("wei_hardware", weights[j]);
						weight.set("weight_index", j+1);
						weightFlag = weight.save();
						System.out.println("保存成功，weiHardware["+(i-1)+"]["+j+"]=" + weights[j]);
					}
				}
				System.out.println("infoFlag="+infoFlag);
				System.out.println("doorFlag="+doorFlag);
				System.out.println("weightFlag="+weightFlag);
				return infoFlag && doorFlag && weightFlag;
			}});
		System.out.println("succeed="+succeed);
		renderJson(succeed);
	}
	
	public void updateOther(){
		
	}
}


