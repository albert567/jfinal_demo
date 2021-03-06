package com.logistics.common.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.logistics.common.model.base.BaseExcelUtil;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class ExcelUtil extends BaseExcelUtil<ExcelUtil> {
	public static final ExcelUtil dao = new ExcelUtil();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Map<String,ExcelUtil> findAll() {
		List<ExcelUtil> excelList = find("select * from excelutil order by rs,cs");
		Map<String, ExcelUtil> map = new HashMap<String, ExcelUtil>();
		for(ExcelUtil excel:excelList){
			map.put(excel.getName(),excel);
		}
		return map;
	}
}
