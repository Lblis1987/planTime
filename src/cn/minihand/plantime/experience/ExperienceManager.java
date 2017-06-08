/**
 * 
 */
package cn.minihand.plantime.experience;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.minihand.plantime.util.SocketUtil;

import com.newland.bg.commontools.file.FileUtil;

/**
 * <p>Title: cn.minihand.plantime.experience.ExperienceManager.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2001-2013 Newland SoftWare Company</p>
 *
 * <p>Company: Newland SoftWare Company</p>
 *
 * @author Lewis.Lynn
 *
 * @version 1.0 CreateTime��2013��8��27�� ����3:15:05
 */

public class ExperienceManager {
	
	private static Logger logger = Logger.getLogger(ExperienceManager.class);
	
	/**
	 * ��ȡ��ǰ�ľ�����Ϣ
	 * @return
	 */
	private static Map<String, Integer> getCurrentLevelInfo(File file){
		Map<String, Integer> reMap = new HashMap<String, Integer>();
		
		String content = FileUtil.readFile(file);
		int level = 1;//��ǰ�ȼ�
		int currentExp = 0;//��ǰ����
		if(content.contains("###")){//����ָ���ָ���
			String[] strArray = content.split("###");
			if(strArray.length == 2){
				String experience = strArray[0];
				level = Integer.parseInt(strArray[1].trim());
				
				String[] expArray = experience.split("/");
				currentExp = Integer.parseInt(expArray[0].trim());
			}
			
		}else {//�ļ����ݲ���ȷ��Ϊ�ȼ�1
			FileUtil.writeContentToFile(file, "0/10###1");
		}
//		System.out.println("current level : " + level);
//		System.out.println("current exp: " + currentExp);
		reMap.put("level", level);
		reMap.put("experience", currentExp);
		return reMap;
	}

	private static File loadFile(){
		File file = null;
		try {
			file = new File("./experience");
		} catch (Exception e) {
			logger.error("file experience is not exist! " + e.toString(), e);
		}
		
		if(!file.exists()){//�ļ������ڣ��򴴽�
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error(e.toString(), e);
			}
		}
		return file;
	}
	
	/**
	 * ����
	 */
	public static void upgrade(){
		File file = loadFile();
		Map<String, Integer> currentInfo = getCurrentLevelInfo(file);
		int level = currentInfo.get("level");//��ǰ�ȼ�
		int exp = currentInfo.get("experience");//��ǰ����
		int totalExp = getExpByLevel(level);//��ǰ�ȼ����ܾ���
		int newestExp = 0;//���µľ���ֵ����������
		int newestLevel = 0;//���µĵȼ�����������
		int newestTotalExp = 0;
		
		exp = exp+1;//�Ǿ���
		if(exp >= totalExp){//�ﵽ��ǰ�ȼ����ܾ��飬������
			newestLevel = level+1;
			newestExp = 0;
		}else {
			newestLevel = level;
			newestExp = exp;
		}
		
		newestTotalExp = getExpByLevel(newestLevel);
		
		FileUtil.writeContentToFile(file, newestExp + "/" + newestTotalExp + "###" + newestLevel);
	}
	
	/**
	 * ��ȡ��ǰ�ȼ��ķ�����������
	 * @param level
	 * @return
	 */
	private static int getExpByLevel(int level){
		int totalExp = 0;
		switch (level) {
		case 1:
			totalExp = Experience.Level_1;
			break;
		case 2:
			totalExp = Experience.Level_2;
			break;
		case 3:
			totalExp = Experience.Level_3;
			break;
		case 4:
			totalExp = Experience.Level_4;
			break;
		case 5:
			totalExp = Experience.Level_5;
			break;
		case 6:
			totalExp = Experience.Level_6;
			break;
		case 7:
			totalExp = Experience.Level_7;
			break;
		case 8:
			totalExp = Experience.Level_8;
			break;
		case 9:
			totalExp = Experience.Level_9;
			break;
		case 10:
			totalExp = Experience.Level_10;
			break;
		default:
			logger.error("your input level is incorrect!");
			break;
		}
		
		return totalExp;
	}
	
	/**
	 * ��ȡ��ǰ�ȼ�
	 * @return
	 */
	public static int getCurrentLevel(){
		File file = loadFile();
		Map<String, Integer> info = getCurrentLevelInfo(file);
		return info.get("level");
	}
	
	/**
	 * ��ȡ��ǰ�������
	 * @return
	 */
	public static String getCurrentExpInfo(){
		File file = loadFile();
		Map<String, Integer> info = getCurrentLevelInfo(file);
		int level = info.get("level");
		int currentExp = info.get("experience");
		int totalExp = getExpByLevel(level);
		return currentExp + "/" + totalExp;
	}
	
	/**
	 * ��ȡ��ǰʱ��
	 * add since v1.4.1
	 * @return
	 */
	public static String getCurrentTime(){
		DateFormat format = new SimpleDateFormat("HHmm");
		return format.format(new Date());
	}
	
	/**
	 * ����ÿ�μ�¼���ļ�
	 * add since v1.4.1
	 * @param beginTime
	 * @param end_time
	 */
	public static void saveTimeToFile(String monthDir, String dayDir, String fileName) {
		String baseDir = "./data/";
		File dir = new File(baseDir + monthDir);
		if(!dir.exists()){
			dir.mkdir();
		}
		
		File dayDirFile = new File(baseDir + monthDir + "/" + dayDir);
		if(!dayDirFile.exists()){
			dayDirFile.mkdir();
		}
		
		File time = new File(baseDir + monthDir + "/" + dayDir + "/" + fileName);
		if(!time.exists()){
			try {
				time.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * ��ȡ���շ��Ѵ���
	 * add since v1.4.1
	 * @return
	 */
	public static int getTodayCount(){
		DateFormat format = new SimpleDateFormat("yyyyMM");
		String monthName = format.format(new Date());
		
		File dir = new File("./data/" + monthName);
		if(!dir.exists()){
			return 0;
		}
		
		format = new SimpleDateFormat("yyyyMMdd");
		File dayDir = new File(dir.getAbsolutePath() + "/" + format.format(new Date()));
		if(!dayDir.exists()){
			return 0;
		}
		
		return dayDir.listFiles().length;
	}
	
	/**
	 * add since v1.4.1
	 * ���浽upfile��ȴ��´�����ͨ��ʱ���ϴ�
	 * @param monthDir
	 * @param dayDir
	 * @param fileName
	 */
	public static void saveToUpFile(String monthDir, String dayDir, String fileName){
		File file = new File("./upfile/" + monthDir + "###" + dayDir + "###" + fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error(e.toString(), e);
			}
		}
	}
	
	/**
	 * add since v1.4.1
	 * ���ϴ�ʧ�ܵ��ļ��ϴ���������
	 */
	public static String sendUpFileToServer(){
		if(!SocketUtil.isConnected()) {
			logger.error("can't not connect to server!");
			return "can't not connect to server!";
		}
		File dir = new File("./upfile");
		if(dir.isDirectory()){
			File[] files = dir.listFiles();
			if(files.length > 0){
				for (File file : files) {
					boolean isSend = SocketUtil.sendToServer(file.getName().split("###"));
					if(isSend){
						file.delete();
					}
				}
			}
		}else {
			logger.error("upfile�ļ��в�����");
			return "upfile�ļ��в�����";
		}
		return "upload success!";
	}
	
	/**
	 * ��ȡ�ϴε���������
	 */
	public static String getLastContent(){
		File lastContentFile = new File("./lastContent");
		if(!lastContentFile.exists()) return "";
		return FileUtil.readFile(lastContentFile);
	}
		
	/**
	 * ��¼������������
	 * @param task_name
	 */
	public static void saveLastContent(String task_name){
		File lastContentFile = new File("./lastContent");
		if(!lastContentFile.exists()){
			try {
				lastContentFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileUtil.writeContentToFile(lastContentFile, task_name);	
	}
	
	
	
	public static void main(String[] args) {
//		System.out.println(ExperienceManager.isCurrentMonth("201502"));
//		System.out.println(ExperienceManager.isCurrentWeek("20150216"));
//		System.out.println(ExperienceManager.isCurrentDay("20150212"));
//		int[] count = ExperienceManager.getTaskNum();
//		System.out.println("day count is " + count[0]);
//		System.out.println("week count is " + count[1]);
//		System.out.println("month count is " + count[2]);
//		System.out.println("all count is " + count[3]);
	}
}
