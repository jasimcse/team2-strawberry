package controller.common;


/**
 * 
 * ����� �� ������ �� ����� �� ���� �������� � ���������� ������� �� �������� �����
 *
 */
public class InterPageDataRequest {

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
