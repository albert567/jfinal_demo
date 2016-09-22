package com.logistics.common.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Page;
import com.logistics.common.model.base.BaseBaseinfo;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Baseinfo extends BaseBaseinfo<Baseinfo> {
	public static final Baseinfo dao = new Baseinfo();
	
	/**
	 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
	 */
	public Page<Baseinfo> paginate(int pageNumber, int pageSize,String email) {
		return paginate(pageNumber, pageSize, "select *", "from baseinfo where email=? order by id desc",email);
	}
	
	public List<Baseinfo> findAllByEmail(String email) {
		return find("select * from baseinfo where email=? order by id desc",email);
	}
}
