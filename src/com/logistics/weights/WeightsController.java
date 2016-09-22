package com.logistics.weights;

import com.jfinal.core.Controller;
import com.logistics.common.model.Weighthardware;

/**
 * WeightsController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class WeightsController extends Controller {
	public void index() {
		renderJson(Weighthardware.dao.findByBaseIdAndIndex(getParaToInt(0,1),getParaToInt(1,1)));
	}
}


