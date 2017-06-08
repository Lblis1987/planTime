package cn.minihand.plantime.manager;

import cn.minihand.plantime.model.Week;

public interface WeekManager {

	public void validateWeek(int plan_id);

	public String getWeekTime(String currentTime);	//���ݵ�ǰ������ȡ�ñ������ڷ�Χ
	
	public void addWeek(Week week);
	
	public void updateWeek(int plan_id);
	
	public Week findWeek(int plan_id);
	
	public void deleteAllPlans(); //ɾ����������
}
