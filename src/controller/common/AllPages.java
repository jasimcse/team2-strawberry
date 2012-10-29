package controller.common;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import model.EmployeeAutoservice;

@ManagedBean(name="allPages")
@ApplicationScoped
public class AllPages {
	
	private List<PageAttributes> allPages;
	
	public AllPages() {
		allPages = new ArrayList<PageAttributes>();
		
		allPages.add(zapisvaneNa4as());
		allPages.add(pregledNaZapazeni4asove());
		
		allPages.add(dobavqneNaSlujitel());
		allPages.add(aktualiziraneNaSlujitel());
		
		allPages.add(dobavqneNaAvtoserviz());
		allPages.add(aktualiziraneNaAvtoserviz());
		
		allPages.add(dobavqneNaPotrebitel());
		
		allPages.add(dobavqneNaZastrahovatel());
		allPages.add(aktualiziraneNaZastrahovatel());
		
		allPages.add(dobavqneNaDostav4ik());
		allPages.add(aktualiziraneNaDostav4ik());
		
		allPages.add(dobavqneNaKlient());
		allPages.add(aktualiziraneNaKlient());
		
		allPages.add(dobavqneNaAvtomobil());
		allPages.add(aktualiziraneNaAvtomobil());
		
		allPages.add(dobavqneNaDiagnostika());
		allPages.add(aktualiziraneNaDiagnostika());
		
		allPages.add(dobavqneNaKlientskaPoru4ka());
		allPages.add(aktualiziraneNaKlientskaPoru4ka());
		
		allPages.add(poru4kaNa4asti());
		allPages.add(pregledNaPoru4kaNa4asti());
		
		allPages.add(priemaneNa4asti());
		allPages.add(pregledNaPriemaneNa4asti());
		
		allPages.add(pregledNaGarancionniUsloviq());
		allPages.add(pregledNaModelAvtomobil());
		allPages.add(aktualiziraneNaUsluga());
		allPages.add(aktualiziraneNaRezervna4ast());
		allPages.add(pregledNaNali4niteRezervni4asti());
		
		
		allPages.add(dobavqneNaGarancionniUsloviq());
		allPages.add(dobavqneNaModelAvtomobil());
		allPages.add(dobavqneNaUsluga());
		allPages.add(dobavqneNaGrupaRezervni4asti());
		allPages.add(dobavqneNaRezervna4ast());
		allPages.add(dobavqneNaUslugaZaModelAvtomobil());
		allPages.add(dobavqneNaRezervna4astZaModelAvtomobil());
		allPages.add(dobavqneNaKlientDebug());
		allPages.add(dobavqneNaAvtomobilDebug());
		allPages.add(dobavqneNaZaqvkaOtZastrahovatel());
		allPages.add(DobavqneNaRezervna4astOtDrugDostav4ik());
		
	}
	
	public String getTitle() {
		String path = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		
		for (PageAttributes pa : allPages) {
			if (pa.getUrl().equals(path)) {
				return pa.getTitle();
			}
		}
		
		throw new RuntimeException("Page hasn't been defined!");
	}
	
	public boolean getReadRight(String position) {
		String path = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		
		for (PageAttributes pa : allPages) {
			if (pa.getUrl().equals(path)) {
				return pa.getReadRight(position);
			}
		}
		
		return false;
	}
	
	public boolean getWriteRight(String position) {
		String path = FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath();
		
		for (PageAttributes pa : allPages) {
			if (pa.getUrl().equals(path)) {
				return pa.getWriteRight(position);
			}
		}
		
		return false;
	}
	
	private PageAttributes zapisvaneNa4as() {
		return new PageAttributes("/clients/ZapazvaneNa4as.jsf", "Запазване на час");
	}
	
	private PageAttributes dobavqneNaSlujitel() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		readRights.setRight(EmployeeAutoservice.MANAGER,       false);
		readRights.setRight(EmployeeAutoservice.CASHIER,       false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/DobavqneNaSlujitel.jsf", "Добавяне на служител");
	}
	
	private PageAttributes aktualiziraneNaSlujitel() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  true);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  true);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  true);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/AktualiziraneNaSlujitel.jsf", "Актуализиране на служител");
	}
	
	private PageAttributes dobavqneNaAvtoserviz() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  true);
		readRights.setRight(EmployeeAutoservice.MANAGER,        false);
		readRights.setRight(EmployeeAutoservice.CASHIER,        false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/DobavqneNaAvtoserviz.jsf", "Добавяне на автосервиз");
	}
	
	private PageAttributes aktualiziraneNaAvtoserviz() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  true);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/AktualiziraneNaAvtoserviz.jsf", "Актуализиране на автосервиз");
	}
	
	private PageAttributes dobavqneNaPotrebitel() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  true);
		readRights.setRight(EmployeeAutoservice.MANAGER,        false);
		readRights.setRight(EmployeeAutoservice.CASHIER,        false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/DobavqneNaPotrebitel.jsf", "Добавяне на потребител");
	}
	
	private PageAttributes dobavqneNaZastrahovatel() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  true);
		readRights.setRight(EmployeeAutoservice.MANAGER,        false);
		readRights.setRight(EmployeeAutoservice.CASHIER,        false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/DobavqneNaZastrahovatel.jsf", "Добавяне на застраховател");
	}
	
	private PageAttributes aktualiziraneNaZastrahovatel() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  true);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/AktualiziraneNaZastrahovatel.jsf", "Актуализиране на застраховател");
	}
	
	private PageAttributes dobavqneNaDostav4ik() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  true);
		readRights.setRight(EmployeeAutoservice.MANAGER,        false);
		readRights.setRight(EmployeeAutoservice.CASHIER,        false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/DobavqneNaDostav4ik.jsf", "Добавяне на доставчик");
	}
	
	private PageAttributes aktualiziraneNaDostav4ik() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  true);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, true);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/admin/AktualiziraneNaDostav4ik.jsf", "Актуализиране на доставчик");
	}
	
	private PageAttributes dobavqneNaKlient() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/DobavqneNaKlient.jsf", "Добавяне на клиент");
	}
	
	private PageAttributes aktualiziraneNaKlient() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/AktualiziraneNaKlient.jsf", "Актуализиране на клиент");
	}

	private PageAttributes dobavqneNaAvtomobil() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/DobavqneNaAvtomobil.jsf", "Добавяне на автомобил");
	}
	
	private PageAttributes aktualiziraneNaAvtomobil() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  true);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  true);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/AktualiziraneNaAvtomobil.jsf", "Актуализиране на автомобил");
	}
	
	private PageAttributes dobavqneNaDiagnostika() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        false);
		readRights.setRight(EmployeeAutoservice.CASHIER,        false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  true);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, true);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/DobavqneNaDiagnostika.jsf", "Добавяне на диагностика");
	}
	
	private PageAttributes aktualiziraneNaDiagnostika() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  true);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/AktualiziraneNaDiagnostika.jsf", "Актуализиране на диагностика");
	}
	
	private PageAttributes dobavqneNaKlientskaPoru4ka() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/DobavqneNaKlientskaPoru4ka.jsf", "Добавяне на клиентска поръчка");
	}
	
	private PageAttributes aktualiziraneNaKlientskaPoru4ka() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  true);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  true);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, true);
		
		return new PageAttributes("/users/AktualiziraneNaKlientskaPoru4ka.jsf", "Актуализиране на клиентска поръчка");
	}
	
	private PageAttributes poru4kaNa4asti() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        false);
		readRights.setRight(EmployeeAutoservice.CASHIER,        false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  true);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/poru4kaNa4asti.jsf", "Поръчка на части и консумативи");
	}
	
	private PageAttributes pregledNaPoru4kaNa4asti() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  true);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/PregledNaPoru4kaNa4asti.jsf", "Преглед на поръчките на части и консумативи");
	}
	
	private PageAttributes priemaneNa4asti() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        false);
		readRights.setRight(EmployeeAutoservice.CASHIER,        false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       false);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  true);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/PriemaneNa4asti.jsf", "Приемане на части и консумативи");
	}
	
	private PageAttributes pregledNaPriemaneNa4asti() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/PregledNaPriemaneNa4asti.jsf", "Преглед на приемане на части и консумативи");
	}
	
	private PageAttributes pregledNaZapazeni4asove() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/PregledNaZapazeni4asove.jsf", "Преглед на запазени часове");
	}
	
	private PageAttributes pregledNaGarancionniUsloviq() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/PregledNaGarancionniUsloviq.jsf", "Преглед на гаранционни условия");
	}
	
	private PageAttributes pregledNaModelAvtomobil() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  true);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  true);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       true);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  true);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, true);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, true);
		
		return new PageAttributes("/users/PregledNaModelAvtomobil.jsf", "Преглед на модел автомобил");
	}
	
	private PageAttributes aktualiziraneNaUsluga() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  true);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/AktualiziraneNaUsluga.jsf", "Актуализиране на услуга");
	}
	
	private PageAttributes aktualiziraneNaRezervna4ast() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        false);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   false);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  false);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  false);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  false);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/AktualiziraneNaRezervna4ast.jsf", "Актуализиране на резервна част");
	}
	
	private PageAttributes pregledNaNali4niteRezervni4asti() {
		PageAttributes.Rights readRights = new PageAttributes.Rights();
		PageAttributes.Rights writeRights = new PageAttributes.Rights();
		
		readRights.setRight(EmployeeAutoservice.ADMINISTRATOR,  false);
		readRights.setRight(EmployeeAutoservice.MANAGER,        true);
		readRights.setRight(EmployeeAutoservice.CASHIER,        true);
		readRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,   true);
		readRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN,  true);
		readRights.setRight(EmployeeAutoservice.AUTO_MECHANIC,  true);
		
		writeRights.setRight(EmployeeAutoservice.ADMINISTRATOR, false);
		writeRights.setRight(EmployeeAutoservice.MANAGER,       true);
		writeRights.setRight(EmployeeAutoservice.CASHIER,       false);
		writeRights.setRight(EmployeeAutoservice.WAREHOUSEMAN,  true);
		writeRights.setRight(EmployeeAutoservice.DIAGNOSTICIAN, false);
		writeRights.setRight(EmployeeAutoservice.AUTO_MECHANIC, false);
		
		return new PageAttributes("/users/PregledNaNali4niteRezervni4asti.jsf", "Преглед на наличните резервни части");
	}
	
	private PageAttributes dobavqneNaGarancionniUsloviq() {
		return new PageAttributes("/debug/DobavqneNaGarancionniUsloviq.jsf", "DEBUG - ElectronicShopService : Добавяне на гаранционни условия");
	}
	
	private PageAttributes dobavqneNaModelAvtomobil() {
		return new PageAttributes("/debug/DobavqneNaModelAvtomobil.jsf", "DEBUG - ElectronicShopService : Добавяне на модел автомобил");
	}
	
	private PageAttributes dobavqneNaUsluga() {
		return new PageAttributes("/debug/DobavqneNaUsluga.jsf", "DEBUG - ElectronicShopService : Добавяне на услуга");
	}
	
	private PageAttributes dobavqneNaGrupaRezervni4asti() {
		return new PageAttributes("/debug/DobavqneNaGrupaRezervni4asti.jsf", "DEBUG - ElectronicShopService : Добавяне на група резервни части");
	}
	
	private PageAttributes dobavqneNaRezervna4ast() {
		return new PageAttributes("/debug/DobavqneNaRezervna4ast.jsf", "DEBUG - ElectronicShopService : Добавяне на резервна част");
	}
	
	private PageAttributes dobavqneNaUslugaZaModelAvtomobil() {
		return new PageAttributes("/debug/DobavqneNaUslugaZaModelAvtomobil.jsf", "DEBUG - ElectronicShopService : Добавяне на услуга за модел автомобил");
	}
	
	private PageAttributes dobavqneNaRezervna4astZaModelAvtomobil() {
		return new PageAttributes("/debug/DobavqneNaRezervna4astZaModelAvtomobil.jsf", "DEBUG - ElectronicShopService : Добавяне на резервна част за модел автомобил");
	}
	
	private PageAttributes dobavqneNaKlientDebug() {
		return new PageAttributes("/debug/DobavqneNaKlientDebug.jsf", "DEBUG - ElectronicShopService : Добавяне на клиент");
	}
	
	private PageAttributes dobavqneNaAvtomobilDebug() {
		return new PageAttributes("/debug/DobavqneNaAvtomobilDebug.jsf", "DEBUG - ElectronicShopService : Добавяне на автомобил");
	}
	
	private PageAttributes dobavqneNaZaqvkaOtZastrahovatel() {
		return new PageAttributes("/debug/DobavqneNaZaqvkaOtZastrahovatel.jsf", "DEBUG - InsurerService : Добавяне на заявка от застраховател");
	}
	
	private PageAttributes DobavqneNaRezervna4astOtDrugDostav4ik() {
		return new PageAttributes("/debug/DobavqneNaRezervna4astOtDrugDostav4ik.jsf", "DEBUG - SupplierService : Добавяне на резервна част от друг доставчик");
	}
	
}
