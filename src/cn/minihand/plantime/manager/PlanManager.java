package cn.minihand.plantime.manager;

import java.util.List;

import cn.minihand.plantime.model.Plan;

public interface PlanManager {
	
	public void addPlan(Plan plan);	//add plan
	
	public List<Plan> findAll();	// find all plan
	
	public void deletePlan(Plan plan); //delete plan
	
	public void updatePlan(Plan plan);
	
	public void deleteAllPlans(); //ɾ����������
	
	public List<Plan> findByDate();	//�������������Ƿ����
	
	public Plan findByPlanName(String planName); //���ݼƻ���������ȡ�ƻ�
}
