package controller.common;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import com.google.appengine.api.utils.SystemProperty;



/**
 * 
 * ������������ ������
 *
 */
public class Utils {
	
	// for generating random strings --------------------------------------------------------------------------
	static final String smallCaseToPickFrom = "abcdefghjkmnpqrstuvwxyz";           // without 'i', 'l' and 'o'
	static final String upperCaseToPickFrom = "ABCDEFGHJKLMNPQRSTUVWXYZ";          // without 'I' and 'O'
	static final String numbersToPickFrom   = "123456789";                         // without '0' and '1'
	static final String symbolsToPickFrom   = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{}~"; // without '|'
	
	static final String charsToPickFromPassword = smallCaseToPickFrom + upperCaseToPickFrom + numbersToPickFrom + symbolsToPickFrom;
	static final String charsToPickFromCode = upperCaseToPickFrom + numbersToPickFrom;
	// --------------------------------------------------------------------------------------------------------
	
	//NOTE: ��� �� �� ������������ �� ��� ��������, �� �� ���� �����
	static final Random rand = new Random(System.nanoTime());
	
	static final private Logger logger = Logger.getLogger(Utils.class.getName());
	
	/**
	 * �������� ���������� ������ � ���������� ������� 
	 * 
	 * @param length - ������� �� ������������ ������
	 * @return
	 */
	public static String generateRandomPassword(int length) {
		return generateRandomString(length, charsToPickFromPassword);
	}
	
	
	/**
	 * �������� ���������� ��� � ���������� ������� 
	 * 
	 * @param length - ������� �� ����������� ���
	 * @return
	 */
	public static String generateRandomCode(int length) {
		return generateRandomString(length, charsToPickFromCode);
	}
	
	
	/**
	 *  ������� ��������� �� �������� �� �����������
	 *  
	 * @param mail - ����� �� ������������ ���� �� ����� ������ �� �� ������� �������
	 * @param password - �������� ����� ������ �� �� �������
	 */
	public static void sendPasswordToUser(String mail, String password) {
		Properties prop = new Properties();
		Session session = Session.getDefaultInstance(prop);
		
		Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress("admin@" + SystemProperty.applicationId.get() + ".appspotmail.com",
					                        SystemProperty.applicationId.get() + " admin"));
			msg.addRecipient(RecipientType.TO, new InternetAddress(mail));
			msg.setSubject("Your password for " + SystemProperty.applicationId.get());
			msg.setText("Your password is \"" + password + "\" ");
			
			Transport.send(msg);
			
		} catch (Exception e) {
			logger.log(Level.WARNING, "Sending e-mail with password to " + mail + " failed!", e);
		}
	}
	
	
	/**
	 * �������� ���������� ��� �� ������� � ���������� ������� 
	 * 
	 * @param length - ������� �� ����������� ��� �� �������
	 * @param charsToPiskFrom - ��� �� ������� �� ����� ���� �� �� ������
	 * @return
	 */
	private static String generateRandomString(int length, String charsToPickFrom) {
		StringBuilder sb = new StringBuilder(length);
		int i;

		
		for (i=0; i<length; i++) {
			int curr = rand.nextInt(charsToPickFrom.length());
			sb.append(charsToPickFrom.charAt(curr));
		}
		
		return sb.toString();
	}
}
