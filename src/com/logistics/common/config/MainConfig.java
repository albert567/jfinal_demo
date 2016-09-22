package com.logistics.common.config;

import com.logistics.common.model._MappingKit;
import com.logistics.doors.DoorsController;
import com.logistics.factoryinfo.FactoryInfoController;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.CaseInsensitiveContainerFactory;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import com.logistics.baseinfo.BaseInfoController;
import com.logistics.blog.BlogController;
import com.logistics.index.IndexController;
import com.logistics.weights.WeightsController;

/**
 * API引导式配置
 */
public class MainConfig extends JFinalConfig {

	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		PropKit.use("jdbc.properties");// 加载配置文件
		me.setDevMode(PropKit.getBoolean("config.devMode", false));
		me.setViewType(ViewType.FREE_MARKER);
		me.setEncoding("UTF-8");
		//me.setBaseViewPath("/res");
	}

	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		me.add("/", IndexController.class, "/index"); // 第三个参数为该Controller的视图存放路径
		me.add("/blog", BlogController.class); // 第三个参数省略时默认与第一个参数值相同，在此即为
												// "/blog"
		me.add("/baseinfo", BaseInfoController.class);//基本信息维护
		me.add("/factory", FactoryInfoController.class);//工厂信息维护
		me.add("/door", WeightsController.class);//门岗
		me.add("/weight", DoorsController.class);//计量衡器
	}

	public static C3p0Plugin createC3p0Plugin() {
		return new C3p0Plugin(PropKit.get("oracle.url"), PropKit.get("oracle.username"), 
				PropKit.get("oracle.password"), PropKit.get("oracle.driver"));
	}

	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		C3p0Plugin cp = createC3p0Plugin();
		me.add(cp);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cp);
		me.add(arp);
		// 配置Oracle方言
		arp.setDialect(new OracleDialect());
		// 配置属性名(字段名)大小写不敏感容器工厂
		arp.setContainerFactory(new CaseInsensitiveContainerFactory());
		_MappingKit.mapping(arp);
	}

	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {

	}

	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {

	}

	/**
	 * 建议使用 JFinal 手册推荐的方式启动项目 运行此 main
	 * 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 80, "/", 5);
	}
}
