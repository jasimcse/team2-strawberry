package controller.common;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/**
 * 
 * ����� �� ������������ �� ��������� �� google datastore ��� String � �������
 * ����� � �� JSF, ��� ���� ��� "h:selectOneMenu"
 *
 */
public class ConvertorKeyString implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return KeyFactory.stringToKey(value);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		return KeyFactory.keyToString((Key)value);
	}
	

}
