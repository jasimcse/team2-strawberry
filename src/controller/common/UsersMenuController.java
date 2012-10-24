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
	
	private static final MenuEntry ClientsSeparator = new MenuEntry("----- Клиенти -----", "");
	private static final MenuEntry ZapazvaneNa4as = new MenuEntry("Запазване на час", "/clients/ZapazvaneNa4as.jsf");
	
	private static final MenuEntry UsersSeparator = new MenuEntry("----- Служители -----", "");
	private static final MenuEntry DobavqneNaSlujitel = new MenuEntry("Добавяне на служител", "/admin/DobavqneNaSlujitel.jsf");
	private static final MenuEntry AktualiziraneNaSlujitel = new MenuEntry("Актуализиране на служител", "/admin/AktualiziraneNaSlujitel.jsf");
	private static final MenuEntry DobavqneNaAvtoserviz = new MenuEntry("Добавяне на автосервиз", "/admin/DobavqneNaAvtoserviz.jsf");
	private static final MenuEntry AktualiziraneNaAvtoserviz = new MenuEntry("Актуализиране на автосервиз", "/admin/AktualiziraneNaAvtoserviz.jsf");
	private static final MenuEntry DobavqneNaPotrebitel = new MenuEntry("Добавяне на потребител", "/admin/DobavqneNaPotrebitel.jsf");
	private static final MenuEntry DobavqneNaZastrahovatel = new MenuEntry("Добавяне на застраховател", "/admin/DobavqneNaZastrahovatel.jsf");
	private static final MenuEntry AktualiziraneNaZastrahovatel = new MenuEntry("Актуализиране на застраховател", "/admin/AktualiziraneNaZastrahovatel.jsf");
	private static final MenuEntry DobavqneNaDostav4ik = new MenuEntry("Добавяне на доставчик", "/admin/DobavqneNaDostav4ik.jsf");
	private static final MenuEntry AktualiziraneNaDostav4ik = new MenuEntry("Актуализиране на доставчик", "/admin/AktualiziraneNaDostav4ik.jsf");
	private static final MenuEntry DobavqneNaKlient = new MenuEntry("Добавяне на клиент", "/users/DobavqneNaKlient.jsf");
	private static final MenuEntry AktualiziraneNaKlient = new MenuEntry("Актуализиране на клиент", "/users/AktualiziraneNaKlient.jsf");
	private static final MenuEntry PregledNaGarancionniUsloviq = new MenuEntry("Преглед на гаранционни условия", "/users/PregledNaGarancionniUsloviq.jsf");
	private static final MenuEntry PregledNaModelAvtomobil = new MenuEntry("Преглед на модел автомобил", "/users/PregledNaModelAvtomobil.jsf");
	private static final MenuEntry AktualiziraneNaUsluga = new MenuEntry("Актуализиране на услуга", "/users/AktualiziraneNaUsluga.jsf");
	private static final MenuEntry AktualiziraneNaRezervna4ast = new MenuEntry("Актуализиране на резервна част", "/users/AktualiziraneNaRezervna4ast.jsf");
	private static final MenuEntry PregledNaNali4niteRezervni4asti = new MenuEntry("Преглед на наличните резервни части", "/users/PregledNaNali4niteRezervni4asti.jsf");
	private static final MenuEntry DobavqneNaAvtomobil = new MenuEntry("Добавяне на автомобил", "/users/DobavqneNaAvtomobil.jsf");
	private static final MenuEntry AktualiziraneNaAvtomobil = new MenuEntry("Актуализиране на автомобил", "/users/AktualiziraneNaAvtomobil.jsf");
	private static final MenuEntry DobavqneNaDiagnostika = new MenuEntry("Добавяне на диагностика", "/users/DobavqneNaDiagnostika.jsf");
	private static final MenuEntry AktualiziraneNaDiagnostika = new MenuEntry("Актуализиране на диагностика", "/users/AktualiziraneNaDiagnostika.jsf");
	private static final MenuEntry PregledNaZapazeni4asove = new MenuEntry("Преглед на запазени часове", "/users/pregledNaZapazeni4asove.jsf");
	private static final MenuEntry DobavqneNaKlientskaPoru4ka = new MenuEntry("Добавяне на клиентска поръчка", "/users/DobavqneNaKlientskaPoru4ka.jsf");
	private static final MenuEntry Poru4kaNa4asti = new MenuEntry("Поръчка на части и консумативи", "/users/Poru4kaNa4asti.jsf");
	private static final MenuEntry PregledNaPoru4kaNa4asti = new MenuEntry("Преглед на поръчките на части и консумативи", "/users/PregledNaPoru4kaNa4asti.jsf");
	private static final MenuEntry PriemaneNa4asti = new MenuEntry("Приемане на части и консумативи", "/users/PriemaneNa4asti.jsf");
	private static final MenuEntry PregledNaPriemaneNa4asti = new MenuEntry("Преглед на приемане на части и консумативи", "/users/PregledNaPriemaneNa4asti.jsf");
	
	private static final MenuEntry WebServicesSeparator = new MenuEntry("------- Уеб услуги -------", "");
	private static final MenuEntry ElectronicShopService = new MenuEntry("Уеб услуга \"Електронен магазин\"", "/wsdl/ElectronicShopService.wsdl");
	private static final MenuEntry InsurerService = new MenuEntry("Уеб услуга \"Застрахователи\"", "/wsdl/InsurerService.wsdl");
	private static final MenuEntry SupplierService = new MenuEntry("Уеб услуга \"Доставчици на резервни части и консумативи\"", "/wsdl/SupplierService.wsdl");
	
	private static final MenuEntry ForTestingSeparator = new MenuEntry("--- За тестване на WSDL ---", "");
	private static final MenuEntry DobavqneNaGarancionniUsloviq = new MenuEntry("Добавяне на гаранционни условия", "/debug/DobavqneNaGarancionniUsloviq.jsf");
	private static final MenuEntry DobavqneNaModelAvtomobil = new MenuEntry("Добавяне на модел автомобил", "/debug/DobavqneNaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaUsluga = new MenuEntry("Добавяне на услуга", "/debug/DobavqneNaUsluga.jsf");
	private static final MenuEntry DobavqneNaGrupaRezervni4asti = new MenuEntry("Добавяне на група резервни части", "/debug/DobavqneNaGrupaRezervni4asti.jsf");
	private static final MenuEntry DobavqneNaRezervna4ast = new MenuEntry("Добавяне на резервна част", "/debug/DobavqneNaRezervna4ast.jsf");
	private static final MenuEntry DobavqneNaUslugaZaModelAvtomobil = new MenuEntry("Добавяне на услуга за модел автомобил", "/debug/DobavqneNaUslugaZaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaRezervna4astZaModelAvtomobil = new MenuEntry("Добавяне на резервна част за модел автомобил", "/debug/DobavqneNaRezervna4astZaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaKlientDebug = new MenuEntry("Добавяне на клиент", "/debug/DobavqneNaKlient.jsf");
	private static final MenuEntry DobavqneNaAvtomobilDebug = new MenuEntry("Добавяне на автомобил", "/debug/DobavqneNaAvtomobil.jsf");
	private static final MenuEntry DobavqneNaZaqvkaOtZastrahovatel = new MenuEntry("Добавяне на заявка от застраховател", "/debug/DobavqneNaZaqvkaOtZastrahovatel.jsf");
	private static final MenuEntry DobavqneNaRezervna4astOtDrugDostav4ik = new MenuEntry("Добавяне на резервна част от друг доставчик", "/debug/DobavqneNaRezervna4astOtDrugDostav4ik.jsf");
	
	
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
		
		//TODO - така ли да остава?
		return "Няма такова заглавие до сега. Обърни се към Венци";
		//throw new RuntimeException("No such path");
	}
	 

}
