package cn.minihand.plantime.manager;

import cn.minihand.plantime.model.Time;

public interface TimeManager {

	public Time findTime(int plan_id); //����idName,idValue������Time����
	
	public void updateTime(Time time, int updateTime);
	
	public void updateTime(Time time);
	
	public void validatePlanTime(int plan_id);
	
	public void addTime(int plan_id);
	
	public void deleteAllPlans(); //ɾ����������
	
	public int[] getTaskNum();//��ȡ�����¼�� add 2015-02-11 10:53:42
	
	public String truncCountToHour(int count);
}
