package controller.common;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;


/**
 * 
 * съдържа конфигурационните настройки
 * 
 * TODO - да се четат от файл или от базата данни
 * 
 */
@SuppressWarnings("serial")
@ManagedBean(name="configurationProperties")
@ApplicationScoped
public class ConfigurationProperties implements Serializable {

	public static int getPageSize() {
		return 20;
	}
}
