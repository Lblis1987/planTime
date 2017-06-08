package cn.minihand.plantime.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cn.minihand.plantime.manager.TimeManager;
import cn.minihand.plantime.manager.WeekManager;
import cn.minihand.plantime.manager.impl.TimeManagerImpl;
import cn.minihand.plantime.manager.impl.WeekManagerImpl;
import cn.minihand.plantime.model.Plan;
import cn.minihand.plantime.model.Time;
import cn.minihand.plantime.util.DbUtil;
import cn.minihand.plantime.util.JDBCTemplate;
import cn.minihand.plantime.util.SendMail;

public class PlanView extends WindowAdapter implements Runnable, ActionListener {
	
	private Logger logger = Logger.getLogger(PlanView.class);
	public int leftTime = 0; // ��Ҫִ�е�ʱ��
	public boolean isRun = false; // �ж��Ƿ�ִ��
	public String leftTimeValue = null; // ��ʾ�ƻ�ʣ��ʱ���string
	public String planName = null;	//�ƻ�����
	private String buttonName = "��ʼ";	//��ʱ��ť���ƣ�Ĭ��Ϊ��ʼ
	private JButton control;
	private JLabel leftTimeL ;	//��ʾʣ��ʱ��
	private Thread time;	//����ʱ�߳�
	private JPanel panel;
	private Plan plan;
	private Time myTime;
	private TimeManager timeManager;
	private WeekManager weekManager;
	private int tempTime,tempPlanTime; 
	
	public JPanel draw(Plan plan, JFrame frame, boolean isOverTime) {
		timeManager = new TimeManagerImpl();
		weekManager = new WeekManagerImpl();
		this.plan = plan;
		this.planName = plan.getPlanName();	
		timeManager.validatePlanTime(plan.getPlan_id()); //�����Ƿ��ǵ�����������������ʱ��������ʱ��
		this.myTime = timeManager.findTime(plan.getPlan_id()); //���ݼƻ�ID��ȡTime��
		
		int dayTime = myTime.getDayCompleteTime(); //��ȡ������ִ��ʱ��
		if(dayTime < 0) dayTime = 0;	//�������ִ��ʱ��Ϊ���������Ϊ0
		logger.info("����ִ��ʱ��Ϊ" + dayTime/60 + "����");
		//�ж��Ƿ���ĩ��Ӱ�
		Calendar current = new GregorianCalendar();
		int weekend = current.get(Calendar.DAY_OF_WEEK);
		if(weekend!=1 && weekend!=7){//������ĩ
			if(isOverTime == true){ //�Ӱ�
				tempPlanTime = plan.getOverPlanTime();
				logger.info("�Ӱ࣬'" + this.planName +"'�ƻ�ʱ��=" + tempPlanTime);
			}else{ //���Ӱ�
				tempPlanTime = plan.getPlanTime();
				logger.info("���Ӱ࣬'" + this.planName +"'�ƻ�ʱ��=" + tempPlanTime);
			}
		}else{//��ĩ
			tempPlanTime = plan.getWeekendPlanTime();
			logger.info("��ĩ��ִ��ʱ��Ϊ��" + tempPlanTime);
		}
		if(dayTime != 0){	//���ǵ�һ��ִ��
			this.tempPlanTime = this.leftTime = tempPlanTime - dayTime;
			logger.info("���ǵ�һ��ִ�У�ʣ��ʱ��Ϊ:" + leftTime/60 + "����");
		}else{	//��һ��ִ��
			this.leftTime = tempPlanTime;
			logger.info("���յ�һ��ִ�У�ʣ��ʱ��Ϊ��" + leftTime/60 + "����");
		}
		
		this.leftTimeValue = this.getLeftTime(leftTime);
		panel = drawPanel();
		panel.setName(String.valueOf(plan.getPlan_id()));
		frame.add(panel);
		frame.addWindowListener(this);	//��ӹرռ�������������ʱ��
		
		return panel;
	}

	public JPanel drawPanel() {
		panel = new JPanel();
		// ��� plan��Ϣ
		panel.setLayout(new FlowLayout()); // ���ϲ���

		// �ƻ�����
		JLabel planLabel = new JLabel();
		planLabel.setText(planName);
		planLabel.setForeground(Color.red);
		panel.add(planLabel);
		
		leftTimeL = new JLabel();
		panel.add(leftTimeL);

		control = new JButton();
		control.setText(buttonName);
		control.addActionListener(this);
		panel.add(control);
		
		if(leftTime <= 0){
			leftTimeL.setText("ʣ��ʱ�䣺�����Ѿ���ɣ�");
			panel.remove(control);
		}else{
			leftTimeL.setText("ʣ��ʱ�䣺" + formatLeftTime());
		}
		
		return panel;
	}
	
	/*
	 * ��������ƻ���������
	 */
	public String getLeftTime(int seconds) {
		
		int hour = seconds / 3600;
		int minute = (seconds - hour * 3600) / 60;
		int second = seconds - hour * 3600 - minute * 60;
		return hour + ":" + minute + ":" + second + "   ";

	}

	/*
	 * ���ʱ���Ǹ�λ�ģ����0
	 */
	public String formatLeftTime(){
		String[] times = leftTimeValue.split(":");
		String strHour = times[0];
		String strMin = times[1];
		String strSec = times[2];
		
		if(strHour.trim().length() == 1){
			strHour = "0" + strHour;
		}
		if(strMin.trim().length() == 1){
			strMin = "0" + strMin;
		}
		if(strSec.trim().length() == 1) {
			strSec = "0" + strSec;
		}
		
		return strHour + ":" + strMin + ":" + strSec + "   ";
	}
	
	public void run() {

		while (Thread.currentThread().getName().equals(planName) && isRun) {
			
			leftTimeValue = getLeftTime(leftTime);
			leftTimeL.setText("ʣ��ʱ�䣺" + formatLeftTime());
			leftTime--;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (leftTime <= 0) {
				//����ʱ��
				int updateTime = this.tempPlanTime-leftTime-tempTime;
				logger.info("updateTime = tempPlanTime-leftTime-tempTime --->" + updateTime + "=" + tempPlanTime + "-" + leftTime + "-" + tempTime);
				if(updateTime != 0){
					logger.info(this.planName + ",����ͨ�������������" + updateTime/60 + "����" + (updateTime-(updateTime/60)) + "��");
					timeManager.updateTime(myTime, updateTime);
					tempTime = tempPlanTime-leftTime;
				}
				
				//�����ʼ�
				SendMail send = new SendMail();
				int res = send.send(this.planName + "�Ѿ��������");
				if(res != 1){
					logger.info(plan.getPlanName() + "�ʼ�����ʧ�ܣ���������");
					res = send.send(this.planName + "�Ѿ��������");
				}
				
				leftTimeL.setText("ʣ��ʱ�䣺�����Ѿ���ɣ�");
				panel.remove(control);
				panel.repaint();
				isRun = false;
				
				weekManager.validateWeek(plan.getPlan_id());	//��֤week�Ƿ񴴽�
				
				weekManager.updateWeek(plan.getPlan_id());	//����week������Ĵ�����Ϊ�Ѿ����
				
				JOptionPane pane = new JOptionPane();
				pane.setLocation(500,200);
				pane.showMessageDialog(panel, plan.getPlanName() + "�����Ѿ���ɣ�");
				
				Thread.currentThread().interrupt();
			}
		}
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == control) {
			time = new Thread(this, planName);
			if ("��ʼ".equals(buttonName)) {
				isRun = true;
				buttonName = "��ͣ";
				control.setText(buttonName);
				time.start();
			} else {
				buttonName = "��ʼ";
				control.setText(buttonName);
				isRun = false;
				//����ʱ��
				int updateTime = tempPlanTime-leftTime-tempTime;
				logger.info("updateTime = tempPlanTime-leftTime-tempTime --->" + updateTime + "=" + tempPlanTime + "-" + leftTime + "-" + tempTime);
				if(updateTime != 0){
					logger.info(this.planName + ",����ͨ����ͣ����" + updateTime/60 + "����" + (updateTime-(updateTime/60)) + "��");
					timeManager.updateTime(myTime, updateTime);
					tempTime = tempPlanTime-leftTime;
					System.out.println(tempTime);
				}
			}
		}
	}

	public void windowClosing(WindowEvent e) {
		//����ʱ��
		int updateTime = tempPlanTime-leftTime-tempTime;
		logger.info("updateTime = tempPlanTime-leftTime-tempTime --->" + updateTime + "=" + tempPlanTime + "-" + leftTime + "-" + tempTime);
		if(updateTime != 0){
			logger.info(this.planName + ",����ͨ���رմ�������" + updateTime/60 + "����" + (updateTime-(updateTime/60)) + "��");
			timeManager.updateTime(myTime, updateTime);
		}
	}

	public JPanel getPanel() {
		return panel;
	}

}
