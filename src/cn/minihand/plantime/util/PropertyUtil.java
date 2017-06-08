package cn.minihand.plantime.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * �������ļ��Ĳ���
 * 
 * @author Destiny
 * 
 */
public class PropertyUtil {

	private static Logger logger = Logger.getLogger(PropertyUtil.class);
	private String filePath ;

	public PropertyUtil(String filePath){
		this.filePath = filePath;
	}
	
	/**
	 * ��ȡ�����ļ�
	 * 
	 * @return
	 */
	private Properties getProperty() {
		Properties pro = new Properties();
		try {
			FileInputStream input = new FileInputStream(new File(filePath));
			pro.load(input);
		} catch (FileNotFoundException e) {
			logger.error(e.toString());
		} catch (IOException e) {
			logger.error(e.toString());
		}
		return pro;
	}

	/**
	 * ���ݲ�������ȡֵ
	 */
	public String getValue(String name) {
		Properties pro = getProperty();
		String value = (String) pro.get(name);
		if (name.equals("driverUrl")) {
			try {
				String ip = InetAddress.getLocalHost().getHostAddress();
				System.out.println("IP:" + ip);
				value = value.replace("myip", ip);
			} catch (UnknownHostException e) {
				logger.error(e.toString());
			}
		}
		return value;
	}
}
