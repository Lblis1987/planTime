package cn.minihand.plantime.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cn.minihand.plantime.experience.ExperienceManager;
import cn.minihand.plantime.view.MainView.TimerView;

import com.newland.bg.commontools.file.FileUtil;
import com.newland.bg.commontools.time.TimeTool;

public class TestView implements ActionListener, KeyListener {
	private static Logger logger = Logger.getLogger(TestView.class);
	private static JFrame timeFrame;
	private JPanel timePanel;
	private JLabel minutes;
	private JLabel timeField;
	private int count;//���ڼ�����2��25����Ҫ������
	private String preTime;//ǰһ����¼��ʱ��

	public void lunchFrame() {
		try {
			lunchCurrentContent();
		} catch (IOException e1) {
			logger.error(e1.toString(), e1);
		}
		
		timeFrame = new JFrame();
		timeFrame.setVisible(true);
		timeFrame.setSize(180, 95);
		timeFrame.setTitle("PlanTime");
		timeFrame.setAlwaysOnTop(true);
//		Image icon = Toolkit.getDefaultToolkit().getImage("./cup.png");
//		timeFrame.setIconImage(icon);
//		timeFrame.setDefaultCloseOperation(3);
		
//		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
//		
//		timeFrame.setLocation((int)width-180,(int)height-125); 

		this.timePanel = new JPanel();

		int seconds = 25 * 60;
//		int seconds = 3;
		this.timeField = new JLabel("��ʼ��ʱ");
		this.timePanel.add(this.timeField);
//		timeFrame.add(this.timePanel);
//		timeFrame.setFocusableWindowState(false);//����ÿ�ζ���ȡ��ǰ���ڵĽ���
		
		JDialog d = new JDialog();
		d.add(timePanel);
		
		while (seconds != 0) {
			int min = seconds / 60;
			int sec = seconds - min * 60;
			String secStr = "" + sec;
			if(sec < 10){
				secStr = "0" + sec;
			}
			
			if(min == 0){
				timeField.setText(secStr + "��");
			}else {
				timeField.setText(min + "��" + secStr + "��");
			}
			
			seconds--;
//			timeFrame.repaint();
			
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		/*
		 * add 2013-08-27 15:16:21
		 * �������鿪ʼ
		 */
		ExperienceManager.upgrade();
		
		
		if(count == 2){
			JOptionPane.showMessageDialog(timeFrame, "��"+count+"������ʱ�䵽�ˡ����ף��ú�ˮ�ˣ���ǰ�ȼ�" + ExperienceManager.getCurrentLevel() + ", ����ֵ��" + ExperienceManager.getCurrentExpInfo());
		}else if(count == 4){
			JOptionPane.showMessageDialog(timeFrame, "��"+count+"������ʱ�䵽�ˡ����ף����������ˣ���ǰ�ȼ�" + ExperienceManager.getCurrentLevel() + ", ����ֵ��" + ExperienceManager.getCurrentExpInfo());
			count = 1;//����count
		}else {
			JOptionPane.showMessageDialog(timeFrame, "��"+count+"������ʱ�䵽�ˡ�����ǰ�ȼ�" + ExperienceManager.getCurrentLevel() + ", ����ֵ��" + ExperienceManager.getCurrentExpInfo());
		}
		count ++;
		updateCount();
		
		System.exit(0);
	}

	public static void main(String[] args) {
		TestView v = new TestView();
		v.lunchFrame();
	}

	public void lunchCurrentContent() throws IOException{
		File file = new File("./count");
		String currentTime = TimeTool.getCurrentDate();
		if(!file.exists()){
			file.createNewFile();
			FileUtil.writeContentToFile(file, "1###" + currentTime);
			count=1;
			return;
		}
		
		String content = FileUtil.readFile(file);
		String[] strArray = content.split("###");
		
		if(content.length() > 0){
			count=Integer.parseInt(strArray[0]);
			preTime = strArray[1];
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date current = null;
			Date last = null;
			try {
				current = format.parse(currentTime);
				last = format.parse(preTime);
			} catch (ParseException e) {
				logger.error(e.toString(), e);
			}
			
			long currentMillis = current.getTime();
			long lastMillis = last.getTime();
			
			if(currentMillis > (lastMillis + 2*25*60*1000)){//����2������ʱ��,���¼���������ʱ��
				count = 1;
				FileUtil.writeContentToFile(file, "1###" + currentTime);
			}
		}
	}
	
	private void updateCount(){
		FileUtil.writeContentToFile(new File("./count"), String.valueOf(count) + "###" + TimeTool.getCurrentDate());
	}
	
	public void actionPerformed(ActionEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
		if (e.getSource() == this.minutes)
			System.out.println("1111");
	}
}