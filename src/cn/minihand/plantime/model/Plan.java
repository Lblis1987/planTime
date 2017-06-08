package cn.minihand.plantime.model;

public class Plan {
	
	private int plan_id;
	
	private String planName;
	
	private String planType;
	
	private int planTime;	//�����ƻ�ʱ��
	
	private int overPlanTime;	//�Ӱ�ƻ�ʱ��
	
	private int weekendPlanTime;	//��ĩ�ƻ�ʱ��

	public int getPlan_id() {
		return plan_id;
	}

	public void setPlan_id(int plan_id) {
		this.plan_id = plan_id;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public int getPlanTime() {
		return planTime;
	}

	public void setPlanTime(int planTime) {
		this.planTime = planTime;
	}

	public int getOverPlanTime() {
		return overPlanTime;
	}

	public void setOverPlanTime(int overPlanTime) {
		this.overPlanTime = overPlanTime;
	}

	public int getWeekendPlanTime() {
		return weekendPlanTime;
	}

	public void setWeekendPlanTime(int weekendPlanTime) {
		this.weekendPlanTime = weekendPlanTime;
	}
}
