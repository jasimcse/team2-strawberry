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
	private static final MenuEntry AktualiziraneNaAvtoserviz = new MenuEntry("������������� �� ����������", "/admin/AktualiziraneNaAvtoserviz.jsf");
	private static final MenuEntry DobavqneNaPotrebitel = new MenuEntry("�������� �� ����������", "/admin/DobavqneNaPotrebitel.jsf");
	private static final MenuEntry DobavqneNaZastrahovatel = new MenuEntry("�������� �� �������������", "/admin/DobavqneNaZastrahovatel.jsf");
	private static final MenuEntry AktualiziraneNaZastrahovatel = new MenuEntry("������������� �� �������������", "/admin/AktualiziraneNaZastrahovatel.jsf");
	private static final MenuEntry DobavqneNaDostav4ik = new MenuEntry("�������� �� ���������", "/admin/DobavqneNaDostav4ik.jsf");
	private static final MenuEntry AktualiziraneNaDostav4ik = new MenuEntry("������������� �� ���������", "/admin/AktualiziraneNaDostav4ik.jsf");
	private static final MenuEntry ElectronicShopService = new MenuEntry("��� ������ \"���������� �������\"", "/wsdl/ElectronicShopService.wsdl");
	private static final MenuEntry InsurerService = new MenuEntry("��� ������ \"��������������\"", "/wsdl/InsurerService.wsdl");
	private static final MenuEntry SupplierService = new MenuEntry("��� ������ \"���������� �� �������� ����� � �����������\"", "/wsdl/SupplierService.wsdl");
	
	
	
	private static final List<MenuEntry> ALL_MENU_ENTRIES =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		DobavqneNaSlujitel,
			    		AktualiziraneNaSlujitel,
			    		DobavqneNaAvtoserviz,
			    		AktualiziraneNaAvtoserviz,
			    		DobavqneNaPotrebitel,
			    		DobavqneNaZastrahovatel,
			    		AktualiziraneNaZastrahovatel,
			    		DobavqneNaDostav4ik,
			    		AktualiziraneNaDostav4ik,
			    		ElectronicShopService,
			    		InsurerService,
			    		SupplierService,
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
