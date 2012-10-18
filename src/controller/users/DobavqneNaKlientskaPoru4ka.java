package controller.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import model.ClientOrder;
import model.ClientOrderPart;
import model.ClientOrderService;
import model.Service;
import model.SparePart;
import model.Vehicle;
import controller.common.CurrentEmployee;
import controller.common.InterPageDataRequest;

@ManagedBean(name="dobavqneNaKlientskaPoru4ka")
@ViewScoped
public class DobavqneNaKlientskaPoru4ka {
	
	@ManagedProperty(value="#{currentEmployee}")
	private CurrentEmployee currEmployee;
	
	private ClientOrder poru4ka = new ClientOrder();
	private String errorMessage;

	private List <ClientOrderPart> spisukRezervni4asti = new ArrayList<ClientOrderPart>();
	private List <ClientOrderService> spisukUslugi = new ArrayList<ClientOrderService>();
	
	@SuppressWarnings("unchecked")
	public DobavqneNaKlientskaPoru4ka() {
//		Stack<InterPageDataRequest> dataRequestStack = (Stack<InterPageDataRequest>)FacesContext.getCurrentInstance().getExternalContext().getFlash().get("dataRequestStack");
//	
//		if (dataRequestStack != null) {
//			InterPageDataRequest dataRequest = dataRequestStack.peek();
//			if (FacesContext.getCurrentInstance().getExternalContext().getRequestServletPath().equals(dataRequest.returnPage)) 
//			{
//				this.diagnostika = ((DobavqneNaDiagnostika)dataRequest.requestObject).diagnostika;
//				this.spisukUslugi = ((DobavqneNaDiagnostika)dataRequest.requestObject).spisukUslugi;
//				this.spisukRezervni4asti = ((DobavqneNaDiagnostika)dataRequest.requestObject).spisukRezervni4asti;
//				
//				if(dataRequest.dataPage.equals("/users/AktualiziraneNaAvtomobil.jsf"))
//				{
//					this.diagnostika.setVehicle((Vehicle)dataRequest.requestedObject);
//				}
//				else
//					if(dataRequest.dataPage.equals("/users/AktualiziraneNaUsluga.jsf"))
//					{
//						DiagnosisService diagService = new DiagnosisService();
//						diagService.setService((Service)dataRequest.requestedObject);
//
//						this.spisukUslugi.add( diagService);
//					}
//					else
//						if(dataRequest.dataPage.equals("/users/PregledNaNali4niteRezervni4asti.jsf"))
//						{
//								DiagnosisPart diagPart = new DiagnosisPart();
//								diagPart.setSparePart((SparePart)dataRequest.requestedObject);
//								diagPart.setQuantity(1);
//								this.spisukRezervni4asti.add(diagPart);
//						}
//			}
//		}
	}

}
