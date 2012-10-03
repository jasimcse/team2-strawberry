package controller.common;

import java.io.Serializable;


/**
 * 
 * ����� �� ������ �� ����� �� ���� �������� � ���������� ������� �� �������� �����
 *
 */
@SuppressWarnings("serial")
public class InterPageDataRequest implements Serializable {

	/**
	 * ��������� ������� �� ������ ����� � ������� �����,
	 * �� �� ���� ��� ������� �� �������, ������ �� �� ��������������
	 */
	public Object requestObject;
	
	/**
	 * �������� �� ����� �� ����� ������� �������
	 */
	public String returnPage;
	
	/**
	 * �������� �� ����� ������ �������
	 */
	public String dataPage;
	
	/**
	 * ������� ����� ��� null, ��� ����� ���� ��� ���
	 */
	public Object requestedObject;
}
