package cn.minihand.plantime.manager.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import cn.minihand.plantime.manager.PlanManager;
import cn.minihand.plantime.manager.TimeManager;
import cn.minihand.plantime.model.Plan;
import cn.minihand.plantime.util.JDBCTemplate;

public class PlanManagerImpl implements PlanManager{
	
	private static Logger logger = Logger.getLogger(PlanManagerImpl.class);
	private JDBCTemplate template ;
	private TimeManager timeManager ;
	
	public PlanManagerImpl(){
		template = new JDBCTemplate();
		timeManager = new TimeManagerImpl();
	}
	
	//��Ӽƻ��Ƕ�Ӧ�����һ��time
	public void addPlan(Plan plan) {
		template.add(plan);	//��Ӽƻ�
		//��ȡplan_id
		Plan newPlan = (Plan)this.template.findByField(new Plan(), "planName", plan.getPlanName());
		timeManager.addTime(newPlan.getPlan_id());
	}

	public void deletePlan(Plan plan) {
		this.template.delete(plan);
	}

	public List findAll() {
		
		return template.findAll(new Plan());
	}

	public void updatePlan(Plan plan) {
		template.update(plan);
	}
	
	public void deleteAllPlans(){
		this.template.deleteAll("t_plan");
	}

	//�����ͣ�������������plan
	public List<Plan> findByDate() {
		List<Plan> plans = this.template.findAll(new Plan());
		List<Plan> temp = new ArrayList<Plan>();
		for (Iterator<Plan> iterator = plans.iterator(); iterator.hasNext();) {
			Plan plan = (Plan) iterator.next();
			//�ж��Ƿ�����ĩ
			Calendar current = new GregorianCalendar();
			int weekend = current.get(Calendar.DAY_OF_WEEK);
			if(weekend!=1 && weekend!=7){//������ĩ,�Ƴ���ĩ�ļƻ�
				if(plan.getPlanType().equalsIgnoreCase("week")){//�ж��Ƿ���week
					//temp.add(plan);
					logger.info("���첻����ĩ���Ƴ��ƻ���" + plan.getPlanName());
				} else {
					temp.add(plan);
				}
			}else{
				temp.add(plan);
			}
		}
		return temp;
	}

	public Plan findByPlanName(String planName) {
		Plan plan = (Plan)this.template.findByField(new Plan(), "planName", planName);
		return plan;
	}
	
}
