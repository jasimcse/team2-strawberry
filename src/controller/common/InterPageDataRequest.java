package controller.common;

import java.io.Serializable;


/**
 * 
 * служи за искане на данни от една страница и последващо връщане на исканите данни
 *
 */
@SuppressWarnings("serial")
public class InterPageDataRequest implements Serializable {

	/**
	 * съхранява данните на обекта който е поискал данни,
	 * за да може при връшане на данните, обекта да се самовъзстанови
	 */
	public Object requestObject;
	
	/**
	 * страница на която да бъдат върнати данните
	 */
	public String returnPage;
	
	/**
	 * страница от която искаме данните
	 */
	public String dataPage;
	
	/**
	 * искания обект или null, ако такъв няма все още
	 */
	public Object requestedObject;
}
