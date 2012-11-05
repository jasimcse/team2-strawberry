package controller.common;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * 
 * оправя проблем с незапазването на сесийните бийнове
 * 
 * кодът е взет от http://java.zacheusz.eu/google-app-engine-http-session-vs-jsf-en/394/
 *
 */
@SuppressWarnings("serial")
public class ForceSessionSerializationPhaseListener implements PhaseListener {

	/**
	 * променяме нещо в Map-а на сесията, за да се маркира сесията като "мръсна" и да се сериализира наново
	 */
	private void touchSession() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        sessionMap.put("forceGaeSessionSerialization", System.currentTimeMillis());
    }
	
	
	/**
	 * какво правим след фазата за която сме се регистрирали 
	 */
    public void afterPhase(final PhaseEvent event) {
        touchSession();
    }
    
    
    /**
     * регистрираме се за фазата на JSF рендване на отговора	 
     */
    public PhaseId getPhaseId() {
        return PhaseId.INVOKE_APPLICATION;
    }
    
    /**
	 * какво правим точно преди фазата за която сме се регистрирали 
	 */
    public void beforePhase(PhaseEvent event) {
    }
}
