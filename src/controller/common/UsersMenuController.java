package controller.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;


@SuppressWarnings("serial")
@ManagedBean(name="usersMenuController")
@ApplicationScoped
public class UsersMenuController implements Serializable {
	
	private static final MenuEntry DobavqneNaSlujitel = new MenuEntry("�������� �� ��������", "/admin/DobavqneNaSlujitel.jsf");
	private static final MenuEntry AktualiziraneNaSlujitel = new MenuEntry("������������� �� ��������", "/admin/AktualiziraneNaSlujitel.jsf");
	private static final MenuEntry DobavqneNaAvtoserviz = new MenuEntry("�������� �� ����������", "/admin/DobavqneNaAvtoserviz.jsf");
	private static final MenuEntry AktualiziraneNaAvtoserviz = new MenuEntry("������������� �� ��������", "/admin/AktualiziraneNaAvtoserviz.jsf");
	
	private static final List<MenuEntry> ALL_MENU_ENTRIES =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		DobavqneNaSlujitel,
			    		AktualiziraneNaSlujitel,
			    		DobavqneNaAvtoserviz,
			    		AktualiziraneNaAvtoserviz,
			    		})));
	/*
	private static final List<MenuEntry> ADMINISTRATOR_MENU =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		DobavqneNaSlujitel,
			    		new MenuEntry("test1", "/test1.jsf"), 
			    		new MenuEntry("test100", "hop/test100.html"),
			    		})));
	*/
	
	public static List<MenuEntry> getMenu(String position) {
		//TODO - get the menu for the concrete user position
		return ALL_MENU_ENTRIES;
	}
	
	public static String getPageTitle(String path) {
		for (MenuEntry entry : ALL_MENU_ENTRIES) {
			if (entry.getPath().equals(path)) {
				return entry.getValue();
			}
		}
		
		//TODO - ���� �� �� ������?
		return "���� ������ �������� �� ����. ������ �� ��� �����";
		//throw new RuntimeException("No such path");
	}
	 

}
