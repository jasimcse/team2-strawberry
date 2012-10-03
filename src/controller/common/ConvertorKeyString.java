package controller.common;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;


/**
 * 
 * служи за конвертиране на ключовете от google datastore към String и обратно
 * нужен е на JSF, най вече при "h:selectOneMenu"
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
