package com.logistics.baseinfo;

import com.logistics.common.model.Baseinfo;
import com.logistics.common.model.Doorhardware;
import com.logistics.common.model.ExcelUtil;
import com.logistics.common.model.Factoryinfo;
import com.logistics.common.model.Weighthardware;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.jfinal.core.Controller;
import com.jfinal.core.JFinal;

/**
 * BaseInfoController
 * 所有 sql 与业务逻辑写在 Model 或 Service 中，不要写在 Controller 中，养成好习惯，有利于大型项目的开发与维护
 */
public class BaseInfoController extends Controller {
	public void index() {
		renderJson(Baseinfo.dao.findAllByEmail(getPara("email")));
	}
	
	public void add() {
		
	}
	
	public void save() {
		String email = getPara("email");//邮箱
		String industry = getPara("industry");//行业选择
		String company = getPara("company");//公司名称
		String saleCompany = getPara("sale_company");//销售分公司
		String saleAdviser = getPara("sale_adviser");//销售顾问
		String softDiscount = getPara("soft_discount");//软件折扣
		String factoryCount = getPara("factory_count");//报价工厂数
		String isBuyReport = getPara("is_buy_report");//购买报表平台
		
		Baseinfo info = new Baseinfo();
		info.set("id", "SEQ_ON_BASEINFO.nextval");
		info.set("email", email);
		info.set("industry", industry);
		info.set("company", company);
		info.set("sale_company", saleCompany);
		info.set("sale_adviser", saleAdviser);
		info.set("soft_discount", softDiscount);
		info.set("factory_count", factoryCount);
		info.set("is_buy_report", isBuyReport);
		boolean flag = info.save();
		if(flag){
			renderJson(info.getId());
		}else{
			renderJson(-1);
		}
	}
	
	public Baseinfo edit() {
		Baseinfo info = Baseinfo.dao.findById(getParaToInt());
		return info;
	}
	
	public void update() {
		String id = getPara("id");
		String industry = getPara("industry");//行业选择
		String company = getPara("company");//公司名称
		String saleCompany = getPara("sale_company");//销售分公司
		String saleAdviser = getPara("sale_adviser");//销售顾问
		String softDiscount = getPara("soft_discount");//软件折扣
		String factoryCount = getPara("factory_count");//报价工厂数
		String isBuyReport = getPara("is_buy_report");//购买报表平台
		String updateDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(Calendar.getInstance().getTime());//更新时间
		
		Baseinfo info = new Baseinfo();
		info.set("id", id);
		info.set("industry", industry);
		info.set("company", company);
		info.set("sale_company", saleCompany);
		info.set("sale_adviser", saleAdviser);
		info.set("soft_discount", softDiscount);
		info.set("factory_count", factoryCount);
		info.set("is_buy_report", isBuyReport);
		info.set("update_date", updateDate);
		renderJson(info.update());
	}
	
	public void delete() {
		renderJson(Baseinfo.dao.deleteById(getParaToInt()));
	}
	/**
	 * 生成报价单并下载
	 * @throws IOException
	 */
	public void updateExcel() throws IOException{
		System.out.println("文档开始创建");
		int baseId = Integer.valueOf(getPara("id"));
		Baseinfo baseInfo = Baseinfo.dao.findById(baseId);
		double softDiscount = baseInfo.getSoftDiscount().doubleValue();
		int factoryCount = baseInfo.getFactoryCount().intValue();
		
		//获取excel数据位置
		Map<String,ExcelUtil> excelMap = ExcelUtil.dao.findAll();
		
		String filename = "res/1.xlsx";
		FileInputStream fis = new FileInputStream(filename);
		XSSFWorkbook xwb = new XSSFWorkbook(fis);
		XSSFSheet xSheet = xwb.getSheetAt(0);
		
		//软件折扣
		ExcelUtil position = excelMap.get("SOFT_DISCOUNT");
		xSheet.getRow(position.getRs().intValue())
		.getCell(position.getCs().intValue()).setCellValue(softDiscount);
		
		
		Factoryinfo faInfo = null;
		for(int i=0;i<factoryCount;i++){
			faInfo = Factoryinfo.dao.findByBaseIdAndIndex(baseId, i+1);
			if(faInfo!=null){
				//工厂名称
				position = excelMap.get("FACTORY_NAME");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(faInfo.getFacName());
				if(i>1){
					position = excelMap.get("FACTORY_NAME");
					xSheet.getRow(position.getRs().intValue()+1)
					.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
					.setCellValue("数量");
					xSheet.getRow(position.getRs().intValue()+1)
					.getCell(position.getCs().intValue()+i*2+1,XSSFRow.CREATE_NULL_AS_BLANK)
					.setCellValue("报价");
				}
				
				//LE计量衡器数
				position = excelMap.get("LE_WEI_COUNT");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(Integer.valueOf(faInfo.getWeiCount()));
				
				//无人值守衡器数量
				position = excelMap.get("LE_NO_WEI_COUNT");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(Integer.valueOf(faInfo.getNodMonitors()));
				
				//开单室发卡器
				position = excelMap.get("TICKETS");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(Integer.valueOf(faInfo.getTicCount()));
				//门岗
				int doorA = 0;
				int doorB = 0;
				int doorC = 0;
				int doorD = 0;
				List<Doorhardware> doorList = Doorhardware.dao.findByBaseIdAndIndex(baseId, i+1);
				String doorHardware = null;
				for(int j=0;j<doorList.size();j++){
					doorHardware = doorList.get(j).getDoorHardware();
					if(doorHardware!=null){
						if(doorHardware.indexOf("A")!=-1){
							doorA++;
						}
						if(doorHardware.indexOf("B")!=-1){
							doorB++;
						}
						if(doorHardware.indexOf("C")!=-1){
							doorC++;
						}
						if(doorHardware.indexOf("D")!=-1){
							doorD++;
						}
					}
				}
				
				
				//门岗-门岗自助控制系统
				position = excelMap.get("DOORS_A");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(doorA);
				//门岗-计量单打印终端
				position = excelMap.get("DOORS_B");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(doorB);
				//门岗-语音提示与对讲系统
				position = excelMap.get("DOORS_C");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(doorC);
				//门岗-门岗视频监控
				position = excelMap.get("DOORS_D");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(doorD);
				
				//计量室
				int weightA = 0;
				int weightB = 0;
				int weightC = 0;
				int weightD = 0;
				List<Weighthardware> weightList = Weighthardware.dao.findByBaseIdAndIndex(baseId, i+1);
				String weightHardware = null;
				for(int j=0;j<weightList.size();j++){
					weightHardware = weightList.get(j).getWeiHardware();
					if(weightHardware!=null){
						if(weightHardware.indexOf("A")!=-1){
							weightA++;
						}
						if(weightHardware.indexOf("B")!=-1){
							weightB++;
						}
						if(weightHardware.indexOf("C")!=-1){
							weightC++;
						}
						if(weightHardware.indexOf("D")!=-1){
							weightD++;
						}
					}
				}
				//计量室-无人值守系统
				position = excelMap.get("WEIGHTS_A");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(weightA);
				//计量室-红外检测系统
				position = excelMap.get("WEIGHTS_B");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(weightB);
				//计量室-视频监控视频监控
				position = excelMap.get("WEIGHTS_C");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(weightC);
				//计量室-IC卡读写器
				position = excelMap.get("WEIGHTS_D");
				xSheet.getRow(position.getRs().intValue())
				.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
				.setCellValue(weightD);
				
				//监控室-语音对讲中控主机
				if(faInfo.getMonHardware()!=null){
					position = excelMap.get("MONITORS");
					xSheet.getRow(position.getRs().intValue())
					.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
					.setCellValue(Integer.valueOf(faInfo.getNodMonitors()));
				}
				//手持收料-手持终端
				if(faInfo.getMatHardware()!=null){
					position = excelMap.get("MATERIAL");
					xSheet.getRow(position.getRs().intValue())
					.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
					.setCellValue(Integer.valueOf(faInfo.getPersonCount()));
				}
				//质检加密-手持终端
				if(faInfo.getQuaHardware()!=null){
					position = excelMap.get("QUA_RW_COUNT");
					xSheet.getRow(position.getRs().intValue())
					.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
					.setCellValue(Integer.valueOf(faInfo.getIcRwCount()));
					position = excelMap.get("QUA_IC_COUNT");
					xSheet.getRow(position.getRs().intValue())
					.getCell(position.getCs().intValue()+i*2,XSSFRow.CREATE_NULL_AS_BLANK)
					.setCellValue(Integer.valueOf(faInfo.getIcCount()));
				}
				
			}
			
		}
		
		fis.close();
		
		String newFilename = Calendar.getInstance().getTimeInMillis()+".xlsx";
		FileOutputStream out = new FileOutputStream(
				JFinal.me().getServletContext()
					.getRealPath("/")+"/download/"+newFilename);
		xwb.write(out);
		out.close();
		System.out.println("文档创建完成");
		renderFile(newFilename);
	}
	
}


