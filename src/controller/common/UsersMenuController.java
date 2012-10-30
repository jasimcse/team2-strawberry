package controller.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import model.EmployeeAutoservice;


@SuppressWarnings("serial")
@ManagedBean(name="usersMenuController")
@ApplicationScoped
public class UsersMenuController implements Serializable {
	
	private static final MenuEntry ClientsSeparator = new MenuEntry("За клиенти");
	
	private static final MenuEntry ZapazvaneNa4as = new MenuEntry("Запазване на час", "/clients/ZapazvaneNa4as.jsf");
	
	
	private static final MenuEntry SlujitelSeparator = new MenuEntry("Служител");
	private static final MenuEntry DobavqneNaSlujitel = new MenuEntry("Добавяне", "/admin/DobavqneNaSlujitel.jsf");
	private static final MenuEntry AktualiziraneNaSlujitel = new MenuEntry("Актуализиране", "/admin/AktualiziraneNaSlujitel.jsf");
	
	private static final MenuEntry AvtoservizSeparator = new MenuEntry("Автосервиз");
	private static final MenuEntry DobavqneNaAvtoserviz = new MenuEntry("Добавяне", "/admin/DobavqneNaAvtoserviz.jsf");
	private static final MenuEntry AktualiziraneNaAvtoserviz = new MenuEntry("Актуализиране", "/admin/AktualiziraneNaAvtoserviz.jsf");
	
	private static final MenuEntry PotrebitelSeparator = new MenuEntry("Потребител");
	private static final MenuEntry DobavqneNaPotrebitel = new MenuEntry("Добавяне", "/admin/DobavqneNaPotrebitel.jsf");
	
	private static final MenuEntry ZastrahovatelSeparator = new MenuEntry("Застраховател");
	private static final MenuEntry DobavqneNaZastrahovatel = new MenuEntry("Добавяне", "/admin/DobavqneNaZastrahovatel.jsf");
	private static final MenuEntry AktualiziraneNaZastrahovatel = new MenuEntry("Актуализиране", "/admin/AktualiziraneNaZastrahovatel.jsf");
	
	private static final MenuEntry Dostav4ikSeparator = new MenuEntry("Доставчик");
	private static final MenuEntry DobavqneNaDostav4ik = new MenuEntry("Добавяне", "/admin/DobavqneNaDostav4ik.jsf");
	private static final MenuEntry AktualiziraneNaDostav4ik = new MenuEntry("Актуализиране", "/admin/AktualiziraneNaDostav4ik.jsf");
	
	private static final MenuEntry KlientSeparator = new MenuEntry("Клиент");
	private static final MenuEntry DobavqneNaKlient = new MenuEntry("Добавяне", "/users/DobavqneNaKlient.jsf");
	private static final MenuEntry AktualiziraneNaKlient = new MenuEntry("Актуализиране", "/users/AktualiziraneNaKlient.jsf");
	
	private static final MenuEntry AvtomobilSeparator = new MenuEntry("Автомобил");
	private static final MenuEntry DobavqneNaAvtomobil = new MenuEntry("Добавяне", "/users/DobavqneNaAvtomobil.jsf");
	private static final MenuEntry AktualiziraneNaAvtomobil = new MenuEntry("Актуализиране", "/users/AktualiziraneNaAvtomobil.jsf");
	
	private static final MenuEntry DiagnostikaSeparator = new MenuEntry("Диагностика");
	private static final MenuEntry DobavqneNaDiagnostika = new MenuEntry("Добавяне", "/users/DobavqneNaDiagnostika.jsf");
	private static final MenuEntry AktualiziraneNaDiagnostika = new MenuEntry("Актуализиране", "/users/AktualiziraneNaDiagnostika.jsf");
	
	private static final MenuEntry KlientskaPoru4kaSeparator = new MenuEntry("Клиентска поръчка");
	private static final MenuEntry DobavqneNaKlientskaPoru4ka = new MenuEntry("Добавяне", "/users/DobavqneNaKlientskaPoru4ka.jsf");
	private static final MenuEntry AktualiziraneNaKlientskaPoru4ka = new MenuEntry("Актуализиране", "/users/AktualiziraneNaKlientskaPoru4ka.jsf");
	
	private static final MenuEntry Poru4kaNa4astiSeparator = new MenuEntry("Поръчка на части");
	private static final MenuEntry Poru4kaNa4asti = new MenuEntry("Поръчка", "/users/Poru4kaNa4asti.jsf");
	private static final MenuEntry PregledNaPoru4kaNa4asti = new MenuEntry("Преглед", "/users/PregledNaPoru4kaNa4asti.jsf");
	
	private static final MenuEntry PriemaneNa4astiSeparator = new MenuEntry("Приемане");
	private static final MenuEntry PriemaneNa4asti = new MenuEntry("Приемане", "/users/PriemaneNa4asti.jsf");
	private static final MenuEntry PregledNaPriemaneNa4asti = new MenuEntry("Преглед", "/users/PregledNaPriemaneNa4asti.jsf");
	
	private static final MenuEntry OthersSeparator = new MenuEntry("Други");
	private static final MenuEntry PregledNaZapazeni4asove = new MenuEntry("Преглед на запазени часове", "/users/PregledNaZapazeni4asove.jsf");
	private static final MenuEntry PregledNaGarancionniUsloviq = new MenuEntry("Преглед на гаранционни условия", "/users/PregledNaGarancionniUsloviq.jsf");
	private static final MenuEntry PregledNaModelAvtomobil = new MenuEntry("Преглед на модел автомобил", "/users/PregledNaModelAvtomobil.jsf");
	private static final MenuEntry AktualiziraneNaUsluga = new MenuEntry("Актуализиране на услуга", "/users/AktualiziraneNaUsluga.jsf");
	private static final MenuEntry AktualiziraneNaRezervna4ast = new MenuEntry("Актуализиране на резервна част",  "/users/AktualiziraneNaRezervna4ast.jsf");
	private static final MenuEntry PregledNaNali4niteRezervni4asti = new MenuEntry("Преглед на наличните резервни части", "/users/PregledNaNali4niteRezervni4asti.jsf");
	
	
	private static final MenuEntry EShopSeparator = new MenuEntry("Услуга \"Електронен магазин\"");
	private static final MenuEntry DobavqneNaGarancionniUsloviq = new MenuEntry("Добавяне на гаранционни условия", "/debug/DobavqneNaGarancionniUsloviq.jsf");
	private static final MenuEntry DobavqneNaModelAvtomobil = new MenuEntry("Добавяне на модел автомобил", "/debug/DobavqneNaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaUsluga = new MenuEntry("Добавяне на услуга", "/debug/DobavqneNaUsluga.jsf");
	private static final MenuEntry DobavqneNaGrupaRezervni4asti = new MenuEntry("Добавяне на група резервни части", "/debug/DobavqneNaGrupaRezervni4asti.jsf");
	private static final MenuEntry DobavqneNaRezervna4ast = new MenuEntry("Добавяне на резервна част", "/debug/DobavqneNaRezervna4ast.jsf");
	private static final MenuEntry DobavqneNaUslugaZaModelAvtomobil = new MenuEntry("Добавяне на услуга за модел автомобил", "/debug/DobavqneNaUslugaZaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaRezervna4astZaModelAvtomobil = new MenuEntry("Добавяне на резервна част за модел автомобил", "/debug/DobavqneNaRezervna4astZaModelAvtomobil.jsf");
	private static final MenuEntry DobavqneNaKlientDebug = new MenuEntry("Добавяне на клиент", "/debug/DobavqneNaKlient.jsf");
	private static final MenuEntry DobavqneNaAvtomobilDebug = new MenuEntry("Добавяне на автомобил", "/debug/DobavqneNaAvtomobil.jsf");
	
	private static final MenuEntry InsurerSeparator = new MenuEntry("Услуга \"Застраховател\"");
	private static final MenuEntry DobavqneNaZaqvkaOtZastrahovatel = new MenuEntry("Добавяне на заявка от застраховател", "/debug/DobavqneNaZaqvkaOtZastrahovatel.jsf");
	
	private static final MenuEntry SupplierSeparator = new MenuEntry("Услуга \"Доставчик\"");
	private static final MenuEntry DobavqneNaRezervna4astOtDrugDostav4ik = new MenuEntry("Добавяне на резервна част от друг доставчик", "/debug/DobavqneNaRezervna4astOtDrugDostav4ik.jsf");
	
	
	private static final MenuEntry WebServicesSeparator = new MenuEntry("Уеб услуги");
	
	private static final MenuEntry ElectronicShopService = new MenuEntry("Електронен магазин", "/wsdl/ElectronicShopService.wsdl");
	private static final MenuEntry InsurerService = new MenuEntry("Застрахователи", "/wsdl/InsurerService.wsdl");
	private static final MenuEntry SupplierService = new MenuEntry("Доставчици на резервни части и консумативи", "/wsdl/SupplierService.wsdl");
	
	
	@SuppressWarnings("unused")
	private static final List<MenuEntry> ALL_MENU_ENTRIES =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		ClientsSeparator.swallow(
			    			ZapazvaneNa4as),
			    		
			    		SlujitelSeparator.swallow(
				    		DobavqneNaSlujitel,
				    		AktualiziraneNaSlujitel),
			    		AvtoservizSeparator.swallow(
				    		DobavqneNaAvtoserviz,
				    		AktualiziraneNaAvtoserviz),
			    		PotrebitelSeparator.swallow(
			    			DobavqneNaPotrebitel),
			    		ZastrahovatelSeparator.swallow(
				    		DobavqneNaZastrahovatel,
				    		AktualiziraneNaZastrahovatel),
			    		Dostav4ikSeparator.swallow(
				    		DobavqneNaDostav4ik,
				    		AktualiziraneNaDostav4ik),
			    		KlientSeparator.swallow(
				    		DobavqneNaKlient,
				    		AktualiziraneNaKlient),
			    		AvtomobilSeparator.swallow(
				    		DobavqneNaAvtomobil,
				    		AktualiziraneNaAvtomobil),
			    		DiagnostikaSeparator.swallow(
				    		DobavqneNaDiagnostika,
				    		AktualiziraneNaDiagnostika),
			    		KlientskaPoru4kaSeparator.swallow(
				    		DobavqneNaKlientskaPoru4ka,
				    		AktualiziraneNaKlientskaPoru4ka),
			    		Poru4kaNa4astiSeparator.swallow(
				    		Poru4kaNa4asti,
				    		PregledNaPoru4kaNa4asti),
			    		PriemaneNa4astiSeparator.swallow(
				    		PriemaneNa4asti,
				    		PregledNaPriemaneNa4asti),
			    		OthersSeparator.swallow(
				    		PregledNaZapazeni4asove,
				    		PregledNaGarancionniUsloviq,
				    		PregledNaModelAvtomobil,
				    		AktualiziraneNaUsluga,
				    		AktualiziraneNaRezervna4ast,
				    		PregledNaNali4niteRezervni4asti),
			    		
			    		EShopSeparator.swallow(
				    		DobavqneNaGarancionniUsloviq,
				    		DobavqneNaModelAvtomobil,
				    		DobavqneNaUsluga,
				    		DobavqneNaGrupaRezervni4asti,
				    		DobavqneNaRezervna4ast,
				    		DobavqneNaUslugaZaModelAvtomobil,
				    		DobavqneNaRezervna4astZaModelAvtomobil,
				    		DobavqneNaKlientDebug,
				    		DobavqneNaAvtomobilDebug),
			    		InsurerSeparator.swallow(
			    			DobavqneNaZaqvkaOtZastrahovatel),
			    		SupplierSeparator.swallow(
			    			DobavqneNaRezervna4astOtDrugDostav4ik),
			    		
			    		WebServicesSeparator.swallow(
				    		ElectronicShopService,
				    		InsurerService,
				    		SupplierService),
			    		})));

	private static final List<MenuEntry> ADMINISTRATOR_MENU =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		SlujitelSeparator.swallow(
				    		DobavqneNaSlujitel,
				    		AktualiziraneNaSlujitel),
			    		AvtoservizSeparator.swallow(
				    		DobavqneNaAvtoserviz,
				    		AktualiziraneNaAvtoserviz),
			    		PotrebitelSeparator.swallow(
			    			DobavqneNaPotrebitel),
			    		ZastrahovatelSeparator.swallow(
				    		DobavqneNaZastrahovatel,
				    		AktualiziraneNaZastrahovatel),
			    		Dostav4ikSeparator.swallow(
				    		DobavqneNaDostav4ik,
				    		AktualiziraneNaDostav4ik),
			    		
			    		EShopSeparator.swallow(
				    		DobavqneNaGarancionniUsloviq,
				    		DobavqneNaModelAvtomobil,
				    		DobavqneNaUsluga,
				    		DobavqneNaGrupaRezervni4asti,
				    		DobavqneNaRezervna4ast,
				    		DobavqneNaUslugaZaModelAvtomobil,
				    		DobavqneNaRezervna4astZaModelAvtomobil,
				    		DobavqneNaKlientDebug,
				    		DobavqneNaAvtomobilDebug),
			    		InsurerSeparator.swallow(
			    			DobavqneNaZaqvkaOtZastrahovatel),
			    		SupplierSeparator.swallow(
			    			DobavqneNaRezervna4astOtDrugDostav4ik),
			    		
			    		WebServicesSeparator.swallow(
				    		ElectronicShopService,
				    		InsurerService,
				    		SupplierService),
			    		})));
	
	private static final List<MenuEntry> MANAGER_MENU =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		ClientsSeparator.swallow(
			    			ZapazvaneNa4as),
			    		
			    		SlujitelSeparator.swallow(
			    			AktualiziraneNaSlujitel),
			    		AvtoservizSeparator.swallow(
			    			AktualiziraneNaAvtoserviz),
			    		ZastrahovatelSeparator.swallow(
			    			AktualiziraneNaZastrahovatel),
			    		Dostav4ikSeparator.swallow(
			    			AktualiziraneNaDostav4ik),
			    		KlientSeparator.swallow(
				    		DobavqneNaKlient,
				    		AktualiziraneNaKlient),
			    		AvtomobilSeparator.swallow(
				    		DobavqneNaAvtomobil,
				    		AktualiziraneNaAvtomobil),
			    		DiagnostikaSeparator.swallow(
			    			AktualiziraneNaDiagnostika),
			    		KlientskaPoru4kaSeparator.swallow(
				    		DobavqneNaKlientskaPoru4ka,
				    		AktualiziraneNaKlientskaPoru4ka),
			    		Poru4kaNa4astiSeparator.swallow(
			    			PregledNaPoru4kaNa4asti),
			    		PriemaneNa4astiSeparator.swallow(
			    			PregledNaPriemaneNa4asti),
			    		OthersSeparator.swallow(
				    		PregledNaZapazeni4asove,
				    		PregledNaGarancionniUsloviq,
				    		PregledNaModelAvtomobil,
				    		AktualiziraneNaUsluga,
				    		AktualiziraneNaRezervna4ast,
				    		PregledNaNali4niteRezervni4asti),
			    })));
	
	private static final List<MenuEntry> CASHIER_MENU =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		ClientsSeparator.swallow(
			    			ZapazvaneNa4as),
			    		
			    		SlujitelSeparator.swallow(
			    			AktualiziraneNaSlujitel),
			    		AvtoservizSeparator.swallow(
			    			AktualiziraneNaAvtoserviz),
			    		ZastrahovatelSeparator.swallow(
			    			AktualiziraneNaZastrahovatel),
			    		Dostav4ikSeparator.swallow(
			    			AktualiziraneNaDostav4ik),
			    		KlientSeparator.swallow(
				    		DobavqneNaKlient,
				    		AktualiziraneNaKlient),
			    		AvtomobilSeparator.swallow(
				    		DobavqneNaAvtomobil,
				    		AktualiziraneNaAvtomobil),
			    		DiagnostikaSeparator.swallow(
			    			AktualiziraneNaDiagnostika),
			    		KlientskaPoru4kaSeparator.swallow(
				    		DobavqneNaKlientskaPoru4ka,
				    		AktualiziraneNaKlientskaPoru4ka),
			    		Poru4kaNa4astiSeparator.swallow(
			    			PregledNaPoru4kaNa4asti),
			    		PriemaneNa4astiSeparator.swallow(
			    			PregledNaPriemaneNa4asti),
			    		OthersSeparator.swallow(
				    		PregledNaZapazeni4asove,
				    		PregledNaGarancionniUsloviq,
				    		PregledNaModelAvtomobil,
				    		PregledNaNali4niteRezervni4asti),
			    		})));
	
	private static final List<MenuEntry> WAREHOUSEMAN_MENU =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		SlujitelSeparator.swallow(
			    			AktualiziraneNaSlujitel),
			    		
			    		Dostav4ikSeparator.swallow(
			    			AktualiziraneNaDostav4ik),
			    		KlientskaPoru4kaSeparator.swallow(
			    			AktualiziraneNaKlientskaPoru4ka),
			    		Poru4kaNa4astiSeparator.swallow(
				    		Poru4kaNa4asti,
				    		PregledNaPoru4kaNa4asti),
			    		PriemaneNa4astiSeparator.swallow(
				    		PriemaneNa4asti,
				    		PregledNaPriemaneNa4asti),
			    		OthersSeparator.swallow(
				    		PregledNaModelAvtomobil,
				    		PregledNaNali4niteRezervni4asti)
			    		})));
	
	private static final List<MenuEntry> DIAGNOSTICIAN_MENU =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		SlujitelSeparator.swallow(
			    			AktualiziraneNaSlujitel),
			    		
			    		KlientSeparator.swallow(
			    			AktualiziraneNaKlient),
			    		AvtomobilSeparator.swallow(
			    			AktualiziraneNaAvtomobil),
			    		DiagnostikaSeparator.swallow(
				    		DobavqneNaDiagnostika,
				    		AktualiziraneNaDiagnostika),
			    		OthersSeparator.swallow(
				    		PregledNaModelAvtomobil,
				    		AktualiziraneNaUsluga,
				    		PregledNaNali4niteRezervni4asti),
			    		})));
	
	private static final List<MenuEntry> AUTO_MECHANIC_MENU =
			Collections.unmodifiableList(new ArrayList<MenuEntry>(Arrays.asList(
			    new MenuEntry[] {
			    		SlujitelSeparator.swallow(
			    			AktualiziraneNaSlujitel),
			    		AvtomobilSeparator.swallow(
			    			AktualiziraneNaAvtomobil),
			    		KlientskaPoru4kaSeparator.swallow(
			    			AktualiziraneNaKlientskaPoru4ka),
			    		OthersSeparator.swallow(
				    		PregledNaModelAvtomobil,
				    		PregledNaNali4niteRezervni4asti),
			    		})));
	
	public static List<MenuEntry> getMenu(String position) {
		if (EmployeeAutoservice.ADMINISTRATOR.equals(position)) {
			return ADMINISTRATOR_MENU;
		} else if (EmployeeAutoservice.MANAGER.equals(position)) {
			return MANAGER_MENU;
		} else if (EmployeeAutoservice.CASHIER.equals(position)) {
			return CASHIER_MENU;
		} else if (EmployeeAutoservice.WAREHOUSEMAN.equals(position)) {
			return WAREHOUSEMAN_MENU;
		} else if (EmployeeAutoservice.DIAGNOSTICIAN.equals(position)) {
			return DIAGNOSTICIAN_MENU;
		} else if (EmployeeAutoservice.AUTO_MECHANIC.equals(position)) {
			return AUTO_MECHANIC_MENU;
		}
		return null;
	}
	
}
