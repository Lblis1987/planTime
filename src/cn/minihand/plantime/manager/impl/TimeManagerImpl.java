package cn.minihand.plantime.manager.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.minihand.plantime.manager.TimeManager;
import cn.minihand.plantime.model.Time;
import cn.minihand.plantime.util.JDBCTemplate;
import cn.minihand.plantime.util.SendMail;

public class TimeManagerImpl implements TimeManager {

	private static Logger logger = Logger.getLogger(TimeManagerImpl.class);
	private JDBCTemplate template;
	
	public TimeManagerImpl() {
		template = new JDBCTemplate();
	}

	public Time findTime(int plan_id) {

		return (Time) template.findByField(new Time(), "plan_id", plan_id);
	}

	// ����time���ۼ�����ʱ��,updateTimeΪ�����ӵ�����
	public void updateTime(Time time, int updateTime) {
		time.setDayCompleteTime(time.getDayCompleteTime() + updateTime);
		time.setWeekCompleteTime(time.getWeekCompleteTime() + updateTime);
		time.setMonthCompleteTime(time.getMonthCompleteTime() + updateTime);
		time.setYearCompleteTime(time.getYearCompleteTime() + updateTime);
		time.setTotalCompleteTime(time.getYearCompleteTime() + updateTime);
		
		time.setLastUpdateTime(new Date());
		template.update(time);
	}

	/**
	 * ���ϴθ���ʱ������������бȶԣ������������ɵģ������dayCompleteTime����Ϊ0
	 */
	public void validatePlanTime(int plan_id) {
		Time time = findTime(plan_id);
		int dayTime = time.getDayCompleteTime();
		Date lastUpdateTime = time.getLastUpdateTime();
		
		//lastUpdateTime��Calendarʵ��
		if(lastUpdateTime != null){
			Calendar last = Calendar.getInstance();
			last.clear();
			last.setTime(lastUpdateTime);
		
			//��ǰ��Calendarʵ��
			Calendar now = Calendar.getInstance();
			now.clear();
			now.setTime(new Date());
			
			//�ж��ǲ����µ�һ��,�����賿3��Ϊ�µ�һ��
			if((now.get(Calendar.DAY_OF_YEAR) != last.get(Calendar.DAY_OF_YEAR)) || (now.get(Calendar.DAY_OF_YEAR) == last.get(Calendar.DAY_OF_YEAR) && last.get(Calendar.HOUR_OF_DAY) <= 3)){
				time.setDayCompleteTime(0);
				logger.info("�µ�һ�죬" + plan_id + "ʱ������");
				
				this.updateTime(time);
				
				//�ж��ǲ����µ�����,���ж��ǲ�������һ
				if(now.get(Calendar.DAY_OF_WEEK) == 2){
					time.setWeekCompleteTime(0);
					logger.info("�µ�һ�ܣ�" + plan_id + " ʱ������");
				}
				//�ж��ǲ����µ��·ݣ��ж��ǲ���һ��
				if(now.get(Calendar.DAY_OF_MONTH) == 1){
					time.setMonthCompleteTime(0);
					logger.info("�µ�һ�£�" + plan_id + " ʱ������");
					//�ж��ǲ����µ����,�ǲ���һ�·�
					if(now.get(Calendar.MONTH) == 1){
						time.setYearCompleteTime(0);
						logger.info("�µ�һ�꣬" + plan_id + " ʱ������");
					}
				}
			}
		}
	}
	
	public void updateTime(Time time){
		this.template.update(time);
	}

	public void addTime(int plan_id) {
		Time time = new Time();
		time.setPlan_id(plan_id);
		this.template.add(time);
	}

	public void deleteAllPlans() {
		this.template.deleteAll("t_time");
	}
	
	public static void main(String[] args) {
		TimeManagerImpl impl = new TimeManagerImpl();
		int[] nums = impl.getTaskNum();
		System.out.println(nums[0]);
	}
	
	/**
	 * ��ȡ�����¼��
	 * @return
	 */
	public int[] getTaskNum(){
		int[] reInt = new int[4];
		
		int days = 0;
		int weeks = 0;
		int months = 0;
		int all = 0;
		
		File data_file = new File("H:\\myEngineering\\planTime\\package\\data");
		if(!data_file.exists() && !data_file.isDirectory()) return reInt;
		File[] monthsDir = data_file.listFiles();
		if(monthsDir.length == 0) return reInt;
		for (File monthFile : monthsDir) {
			boolean isCurrentMonth = isCurrentMonth(monthFile.getName());
			for (File dayDir : monthFile.listFiles()) {
				boolean isCurrentWeek = isCurrentWeek(dayDir.getName());
				boolean isCurrentDay = isCurrentDay(dayDir.getName());
				for (File dayFile : dayDir.listFiles()) {
					all ++;
					if(isCurrentDay) days ++;//����
					if(isCurrentWeek) weeks ++;//����
					if(isCurrentMonth) months ++;//�Ǳ��£�����+1
				}
			}
		}
		reInt[0] = days;
		reInt[1] = weeks;
		reInt[2] = months;
		reInt[3] = all;
		return reInt;
	}
	
	private boolean isCurrentDay(String dayStr){
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		String currentDayStr = format.format(cal.getTime());
		if(currentDayStr.equals(dayStr)){
			return true;
		}
		return false;
	}
	
	/**
	 * �ж�ָ�������Ƿ��Ǳ���
	 * @param dayStr
	 * @return
	 */
	private boolean isCurrentWeek(String dayStr){
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String[] weekStr = new String[7];
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.DAY_OF_WEEK) == 1) cal.add(Calendar.DAY_OF_MONTH, -7);
		cal.set(Calendar.DAY_OF_WEEK, 2);
		for (int i = 0; i < 7; i++) {
			weekStr[i] = format.format(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		for (String string : weekStr) {
			if(string.equals(dayStr)) return true;
		}
			
		return false;
	}
	
	/**
	 * �ж�ָ�������Ƿ��Ǳ��·�
	 * @param monthStr
	 * @return
	 */
	private boolean isCurrentMonth(String monthStr){
		DateFormat format = new SimpleDateFormat("yyyyMM");
		try {
			Date monDate = format.parse(monthStr);
			Calendar cal = Calendar.getInstance();
			int currentMonth = cal.get(Calendar.MONTH);//��ǰ�·�
			cal.setTime(monDate);
			int specMonth = cal.get(Calendar.MONTH);
			if(currentMonth == specMonth) return true;
		} catch (ParseException e) {
			logger.error(e.toString(), e);
		}
		return false;
	}
	
	/**
	 * �ѷ���ʱ��ת����Сʱ
	 * @param count
	 * @return
	 */
	public String truncCountToHour(int count){
		String hour_unit = "Сʱ";
		String min_unit = "����";
		if(count == 0) return count + hour_unit;
		int minutes = 25 * count;
		if(minutes < 60) return minutes + min_unit;
		int hour = minutes / 60;
		int leftMinutes = minutes % 60;
		return hour + hour_unit + leftMinutes + min_unit;
	}

}
