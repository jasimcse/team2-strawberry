package model.util;

/**
 * 
 * ������� �� �� � ��������� ��������� �� ���������� �� �������,
 * ����� � ���������� ���� �������� �� ����� ����� �� ������ ����� 
 *
 */
@SuppressWarnings("serial")
public class UniqueAttributeException extends RuntimeException {

	public UniqueAttributeException() {
		super();
	}
	
	public UniqueAttributeException(String str) {
		super(str);
	}
	
	public UniqueAttributeException(String str, Throwable e) {
		super(str, e);
	}
}
