package cn.minihand.plantime.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.minihand.plantime.model.Plan;


public class XmlUtil {
	
	private Logger logger = Logger.getLogger(XmlUtil.class);
	
	/**
	 * ��ȡXML�ļ�
	 * @param filePath
	 * @return ����һ��List
	 */
	public List<Plan> readXml(String filePath){
		List<Plan> contents = new ArrayList<Plan>();
		Plan plan = null;
		
		Document document = this.getDocumentByFileName(filePath);
		Element root = document.getRootElement(); //��Ŀ¼
		
		//����planĿ¼
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) { 
			plan = new Plan();
			Element element = (Element)iterator.next();
			logger.info("begin read nodeElement...");
			for (Iterator iterator2 = element.elementIterator(); iterator2
					.hasNext();) {
				Element inner = (Element) iterator2.next();
				if("planName".equals(inner.getName())){
					plan.setPlanName(inner.getText());
				}
				if("planType".equals(inner.getName())){
					plan.setPlanType(inner.getText());
				}
				if("planTime".equals(inner.getName())){
					plan.setPlanTime(Integer.parseInt(inner.getText())*60);
				}
				if("overPlanTime".equals(inner.getName())){
					plan.setOverPlanTime(Integer.parseInt(inner.getText())*60);
				}
				if("weekendPlanTime".equals(inner.getName())){
					plan.setWeekendPlanTime(Integer.parseInt(inner.getText())*60);
				}
				logger.info(inner.getName() + ":" + inner.getText() );
			}
			contents.add(plan);
		}
		return contents;
	}
	
	private Document getDocumentByFileName(String fileName){
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new File(fileName));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	
}
