package cn.minihand.plantime.util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

/**
 * ������ͨ�ʼ���������ͨ�ʼ� ���ʹ��и������ʼ������մ��и������ʼ� ����html��ʽ���ʼ�������html��ʽ���ʼ� ���ʹ���ͼƬ���ʼ�������һ���ܽᡣ
 */
public class SendMail {
	private Logger logger = Logger.getLogger(SendMail.class);
	// ���������
	private String host = "smtp.qq.com";
	// �������������û���
	private String username = "274221276@qq.com";
	// �����������
	private String password = "linguoqiang27422";

	private String mail_head_name = "this is head of this mail";

	private String mail_head_value = "����ƻ��������";

	private String mail_to = "15960163650@qq.com";

	private String mail_from = "274221276@qq.com";

	private String mail_subject = "������������ʼ�";

	private String personalName = "�ҵ��ʼ�";

	public SendMail() {
	}

	/**
	 * �˶δ�������������ͨ�����ʼ�
	 */
	public int send(String mail_body){
		int res = -1;
		try {
			Properties props = new Properties(); // ��ȡϵͳ����
			Authenticator auth = new Email_Autherticator(); // �����ʼ��������û���֤
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props, auth);
			// ����session,���ʼ�����������ͨѶ��
			MimeMessage message = new MimeMessage(session);
			// message.setContent("foobar, "application/x-foobar"); // �����ʼ���ʽ
			message.setSubject(mail_subject); // �����ʼ�����
			message.setText(mail_body); // �����ʼ�����
			message.setHeader(mail_head_name, mail_head_value); // �����ʼ�����
			message.setSentDate(new Date()); // �����ʼ���������
			Address address = new InternetAddress(mail_from, personalName);
			message.setFrom(address); // �����ʼ������ߵĵ�ַ
			Address toAddress = new InternetAddress(mail_to); // �����ʼ����շ��ĵ�ַ
			message.addRecipient(Message.RecipientType.TO, toAddress);
			Transport.send(message); // �����ʼ�
			logger.info("�ʼ����ͳɹ���");
			res = 1;
			return res;
		} catch (Exception ex) {
			ex.printStackTrace();
			return res;
		}
	}

	/**
	 * �������з��������û�����֤
	 */
	public class Email_Autherticator extends Authenticator {
		public Email_Autherticator() {
			super();
		}

		public Email_Autherticator(String user, String pwd) {
			super();
			username = user;
			password = pwd;
		}

		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(username, password);
		}
	}

	public static void main(String[] args) {
		//�����ʼ�
		SendMail send = new SendMail();
		int res = send.send("�Ѿ��������");
		while(res != 1){
			res = send.send("�Ѿ��������");
		}
	}
}

