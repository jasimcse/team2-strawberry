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
	
	private static final MenuEntry ClientsSeparator = new MenuEntry("----- ������� -----", "");
	private static final MenuEntry ZapazvaneNa4as = new MenuEntry("��������� �� ���", "/clients/ZapazvaneNa4as.jsf");
	
	private static final MenuEntry UsersSeparator = new MenuEntry("----- ��������� -----", "");
	private static final MenuEntry DobavqneNaSlujitel = new MenuEntry("�������� �� ��������", "/admin/DobavqneNaSlujitel.jsf");
	private static final MenuEntry AktualiziraneNaSlujitel = new MenuEntry("������������� �� ��������", "/admin/AktualiziraneNaSlujitel.jsf");
	private static final MenuEntry DobavqneNaAvtoserviz = new MenuEntry("�������� �� ����������", "/admin/DobavqneNaAvtoserviz.jsf");
	private static final MenuEntry AktualiziraneNaAvtoserviz = new MenuEntry("������������� �� ����������", "/admin/AktualiziraneNaAvtoserviz.jsf");
	private static final MenuEntry DobavqneNaPotrebitel = new MenuEntry("�������� �� ����������", "/admin/DobavqneNaPotrebitel.jsf");
	private static final MenuEntry DobavqneNaZastrahovatel = new MenuEntry("�������� �� �������������", "/admin/DobavqneNaZastrahovatel.jsf");
	private static final MenuEntry AktualiziraneNaZastrahovatel = new MenuEntry("������������� �� �������������", "/admin/AktualiziraneNaZastrahovatel.jsf");
	private static final MenuEntry DobavqneNaDostav4ik = new MenuEntry("�������� �� ���������", "/admin/DobavqneNaDostav4ik.jsf");
	private static final MenuEntry AktualiziraneNaDostav4ik = new MenuEntry("������������� �� ���������", "/admin/AktualiziraneNaDostav4ik.jsf");
	private static final MenuEntry DobavqneNaKlient = new MenuEntry("�������� �� ������", "/users/DobavqneNaKlient.jsf");
	private static final MenuEntry AktualiziraneNaKlient = new MenuEntry("������������� �� ������", "/users/AktualiziraneNaKlient.jsf");
	private static final MenuEntry PregledNaGarancionniUsloviq = new MenuEntry("������� �� ����������� �������", "/users/PregledNaGarancionniUsloviq.jsf");
	private static final MenuEntry PregledNaModelAvtomobil = new MenuEntry("������� �� ����� ���������", "/users/PregledNaModelAvtomobil.jsf");
	private static final MenuEntry AktualiziraneNaUsluga = new MenuEntry("������������� �� ������", "/users/AktualiziraneNaUsluga.jsf");
	private static final MenuEntry AktualiziraneNaRezervna4ast = new MenuEntry("������������� �� �������� ����", "/users/AktualiziraneNaRezervna4ast.jsf");
	private static final MenuEntry PregledNaNali4niteRezervni4asti = new MenuEntry("������� �� ��������� �������� �����", "/users/PregledNaNali4niteRezervni4asti.jsf");
	private static final MenuEntry DobavqneNaAvtomobil = new MenuEntry("�������� �� ���������", "/users/DobavqneNaAvtomobil.jsf");
	private static final MenuEntry AktualiziraneNaAvtomobil = new MenuEntry("������������� �� ���������", "/users/AktualiziraneNaAvtomobil.jsf");
	private static final MenuEntry DobavqneNaDiagnostika = new MenuEntry("�������� �� �����������", "/users/DobavqneNaDiagnostika.jsf");
	private static final MenuEntry AktualiziraneNaDiagnostika = new MenuEntry("������������� �� �����������", "/users/AktualiziraneNaDiagnostika.jsf");
	private static final MenuEntry PregledNaZapazeni4asove = new MenuEntry("������� �� �������� ������", "/users/pregledNaZapazeni4asove.jsf");
	private static final MenuEntry DobavqneNaKlientskaPoru4ka = new MenuEntry("�������� �� ��������� �������", "/users/DobavqneNaKlientskaPoru4ka.jsf");
	private static final MenuEntry Poru4kaNa4asti = new MenuEntry("������� �� ����� � �����������", "/users/Poru4kaNa4asti.jsf");
	private static final MenuEntry PregledNaPoru4kaNa4asti = new MenuEntry("������� �� ��������� �� ����� � �����������", "/users/PregledNaPoru4kaNa4asti.jsf");
	private static final MenuEntry PriemaneNa4asti = new MenuEntry("�������� �� ����� � �����������", "/users/PriemaneNa4asti.jsf");
	private static final MenuEntry PregledNaPriemaneNa4asti = new MenuEntry("������� �� �������� �� ����� � �����������", "/users/PregledNaPriemaneNa4asti.jsf");
	
	private static final MenuEntry WebServicesSeparator = new MenuEntry("------- ��� ������ -------", "");
	private static final MenuEntry ElectronicShopService = new MenuEntry("��� ������ \"���������� �������\"", "/wsdl/ElectronicShopService.wsdl");
	private static final MenuEntry InsurerService = new MenuEntry("��� ������ \"��������������\"", "/wsdl/InsurerService.wsdl");
	private static final MenuEntry SupplierService = new MenuEntry("��� ������ \"���������� �� �������� ����� � �����������\"", "/wsdl/SupplierService.wsdl");
	
	private static final MenuEntry ForTestingSeparator = new MenuEntry("--- �� �������� �� WSDL ---", "");
	private static final MenuEntry DobavqneNaGarancionniUsloviq = new MenuEntry("�������� �� ����������� �������", "/debug/DobavqneNaGarancionniUsloviq.jsf");
	private static final MenuEntry DobavqneNaModelAvtomobil = new MenuEntry("�������� �� ����� ���������", "/debug/DobavqneNaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaUsluga = new MenuEntry("�������� �� ������", "/debug/DobavqneNaUsluga.jsf");
	private static final MenuEntry DobavqneNaGrupaRezervni4asti = new MenuEntry("�������� �� ����� �������� �����", "/debug/DobavqneNaGrupaRezervni4asti.jsf");
	private static final MenuEntry DobavqneNaRezervna4ast = new MenuEntry("�������� �� �������� ����", "/debug/DobavqneNaRezervna4ast.jsf");
	private static final MenuEntry DobavqneNaUslugaZaModelAvtomobil = new MenuEntry("�������� �� ������ �� ����� ���������", "/debug/DobavqneNaUslugaZaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaRezervna4astZaModelAvtomobil = new MenuEntry("�������� �� �������� ���� �� ����� ���������", "/debug/DobavqneNaRezervna4astZaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaKlientDebug = new MenuEntry("�������� �� ������", "/debug/DobavqneNaKlient.jsf");
	private static final MenuEntry DobavqneNaAvtomobilDebug = new MenuEntry("�������� �� ���������", "/debug/DobavqneNaAvtomobil.jsf");
	private static final MenuEntry DobavqneNaZaqvkaOtZastrahovatel = new MenuEntry("�������� �� ������ �� �������������", "/debug/DobavqneNaZaqvkaOtZastrahovatel.jsf");
	private static final MenuEntry DobavqneNaRezervna4astOtDrugDostav4ik = new MenuEntry("�������� �� �������� ���� �� ���� ���������", "/debug/DobavqneNaRezervna4astOtDrugDostav4ik.jsf");
	
	
	private static final List<MenuEntry> ALL_MENU_ENTRIES =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		ClientsSeparator,
			    		ZapazvaneNa4as,
			    		
			    		UsersSeparator,
			    		DobavqneNaSlujitel,
			    		AktualiziraneNaSlujitel,
			    		DobavqneNaAvtoserviz,
			    		AktualiziraneNaAvtoserviz,
			    		DobavqneNaPotrebitel,
			    		DobavqneNaZastrahovatel,
			    		AktualiziraneNaZastrahovatel,
			    		DobavqneNaDostav4ik,
			    		AktualiziraneNaDostav4ik,
			    		DobavqneNaKlient,
			    		AktualiziraneNaKlient,
			    		PregledNaGarancionniUsloviq,
			    		PregledNaModelAvtomobil,
			    		AktualiziraneNaUsluga,
			    		AktualiziraneNaRezervna4ast,
			    		PregledNaNali4niteRezervni4asti,
			    		DobavqneNaAvtomobil,
			    		AktualiziraneNaAvtomobil,
			    		DobavqneNaDiagnostika,
			    		AktualiziraneNaDiagnostika,
			    		PregledNaZapazeni4asove,
			    		DobavqneNaKlientskaPoru4ka,
			    		Poru4kaNa4asti,
			    		PregledNaPoru4kaNa4asti,
			    		PriemaneNa4asti,
			    		PregledNaPriemaneNa4asti,
			    		
			    		WebServicesSeparator,
			    		ElectronicShopService,
			    		InsurerService,
			    		SupplierService,
			    		
			    		ForTestingSeparator,
			    		DobavqneNaGarancionniUsloviq,
			    		DobavqneNaModelAvtomobil,
			    		DobavqneNaUsluga,
			    		DobavqneNaGrupaRezervni4asti,
			    		DobavqneNaRezervna4ast,
			    		DobavqneNaUslugaZaModelAvtomobil,
			    		DobavqneNaRezervna4astZaModelAvtomobil,
			    		DobavqneNaKlientDebug,
			    		DobavqneNaAvtomobilDebug,
			    		DobavqneNaZaqvkaOtZastrahovatel,
			    		DobavqneNaRezervna4astOtDrugDostav4ik,
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
