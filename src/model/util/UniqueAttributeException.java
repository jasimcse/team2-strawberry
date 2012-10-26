package model.util;

/**
 * 
 * Показва че не е изпълнено условието за уникалност на атрибут,
 * който е деклариран като уникален за даден обект от базата данни 
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
