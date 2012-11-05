package controller.common;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * 
 * ������ ������� � ������������� �� ��������� �������
 * 
 * ����� � ���� �� http://java.zacheusz.eu/google-app-engine-http-session-vs-jsf-en/394/
 *
 */
@SuppressWarnings("serial")
public class ForceSessionSerializationPhaseListener implements PhaseListener {

	/**
	 * ��������� ���� � Map-� �� �������, �� �� �� ������� ������� ���� "������" � �� �� ����������� ������
	 */
	private void touchSession() {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        final Map<String, Object> sessionMap = facesContext.getExternalContext().getSessionMap();
        sessionMap.put("forceGaeSessionSerialization", System.currentTimeMillis());
    }
	
	
	/**
	 * ����� ������ ���� ������ �� ����� ��� �� ������������ 
	 */
    public void afterPhase(final PhaseEvent event) {
        touchSession();
    }
    
    
    /**
     * ������������ �� �� ������ �� JSF �������� �� ��������	 
     */
    public PhaseId getPhaseId() {
        return PhaseId.INVOKE_APPLICATION;
    }
    
    /**
	 * ����� ������ ����� ����� ������ �� ����� ��� �� ������������ 
	 */
    public void beforePhase(PhaseEvent event) {
    }
}
