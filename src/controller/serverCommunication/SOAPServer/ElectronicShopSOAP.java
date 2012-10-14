package controller.serverCommunication.SOAPServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SAAJResult;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.dom.DOMSource;

import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateCompany;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateCompanyResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdatePerson;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdatePersonResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateService;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateServiceResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateSparePart;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateSparePartGroup;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateSparePartGroupResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateSparePartResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateVehicle;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateVehicleModel;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateVehicleModelPart;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateVehicleModelPartResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateVehicleModelResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateVehicleModelService;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateVehicleModelServiceResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateVehicleResponse;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateWarrantyConditions;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateWarrantyConditionsResponse;


/**
 * 
 * обработва SOAP заявките свързани с електронния магазин
 *
 */
@SuppressWarnings("serial")
public class ElectronicShopSOAP extends HttpServlet {
	
	/**
	 * тези се вземат от генерираните класове jaxws/X.java и jaxws/XResponse.java
	 */
	private static final String NAMESPACE_URI = "http://SOAPServer.serverCommunication.controller/";
	private static final QName CREATE_OR_UPDATE_WARRANTY_CONDITIONS_QNAME   = new QName(NAMESPACE_URI, "createOrUpdateWarrantyConditions");
	private static final QName CREATE_OR_UPDATE_VEHICLE_MODEL_QNAME         = new QName(NAMESPACE_URI, "createOrUpdateVehicleModel");
	private static final QName CREATE_OR_UPDATE_VEHICLE_MODEL_PART_QNAME    = new QName(NAMESPACE_URI, "createOrUpdateVehicleModelPart");
	private static final QName CREATE_OR_UPDATE_VEHICLE_MODEL_SERVICE_QNAME = new QName(NAMESPACE_URI, "createOrUpdateVehicleModelService");
	private static final QName CREATE_OR_UPDATE_SPARE_PART_QNAME            = new QName(NAMESPACE_URI, "createOrUpdateSparePart");
	private static final QName CREATE_OR_UPDATE_SERVICE_QNAME               = new QName(NAMESPACE_URI, "createOrUpdateService");
	private static final QName CREATE_OR_UPDATE_SPARE_PART_GROUP_QNAME      = new QName(NAMESPACE_URI, "createOrUpdateSparePartGroup");
	private static final QName CREATE_OR_UPDATE_VEHICLE_QNAME               = new QName(NAMESPACE_URI, "createOrUpdateVehicle");
	private static final QName CREATE_OR_UPDATE_PERSON_QNAME                = new QName(NAMESPACE_URI, "createOrUpdatePerson");
	private static final QName CREATE_OR_UPDATE_COMPANY_QNAME               = new QName(NAMESPACE_URI, "createOrUpdateCompany");

	
	/**
	 * обработва SOAP съобщение пакетирано в HTTP
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			// Get all the headers from the HTTP request
			MimeHeaders headers = getHeaders(req);

			// Construct a SOAPMessage from the XML in the request body
			InputStream is = req.getInputStream();
			SOAPMessage soapRequest = MessageFactory.newInstance().createMessage(headers, is);

			// Handle soapReqest
			SOAPMessage soapResponse = handleSOAPRequest(soapRequest);

			// Write to HttpServeltResponse
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("text/xml;charset=\"utf-8\"");
			OutputStream os = resp.getOutputStream();
			soapResponse.writeTo(os);
			os.flush();
		} catch (SOAPException e) {
			throw new IOException("Exception while creating SOAP message.", e);
		}
	}
	
	static MimeHeaders getHeaders(HttpServletRequest req) {
	    Enumeration<?> headerNames = req.getHeaderNames();
	    MimeHeaders headers = new MimeHeaders();
	    while (headerNames.hasMoreElements()) {
	      String headerName = (String) headerNames.nextElement();
	      String headerValue = req.getHeader(headerName);
	      StringTokenizer values = new StringTokenizer(headerValue, ",");
	      while (values.hasMoreTokens()) {
	        headers.addHeader(headerName, values.nextToken().trim());
	      }
	    }
	    return headers;
	  }


	/**
	 * обработва SOAP съобщението
	 */
	public SOAPMessage handleSOAPRequest(SOAPMessage request) throws SOAPException {
		SOAPBody soapBody = request.getSOAPBody();
		Iterator<?> iterator = soapBody.getChildElements();
		Object responsePojo = null;
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof SOAPElement) {
				SOAPElement soapElement = (SOAPElement) next;
				QName qname = soapElement.getElementQName();
				if(CREATE_OR_UPDATE_WARRANTY_CONDITIONS_QNAME.equals(qname)) {
					responsePojo = createOrUpdateWarrantyConditions(soapElement);
					break;
				} else if(CREATE_OR_UPDATE_VEHICLE_MODEL_QNAME.equals(qname)) {
		            responsePojo = createOrUpdateVehicleModel(soapElement);
		            break;
		        } else if(CREATE_OR_UPDATE_VEHICLE_MODEL_PART_QNAME.equals(qname)) {
		            responsePojo = createOrUpdateVehicleModelPart(soapElement);
		            break;
		        } else if(CREATE_OR_UPDATE_VEHICLE_MODEL_SERVICE_QNAME.equals(qname)) {
		            responsePojo = createOrUpdateVehicleModelService(soapElement);
		            break;
		        } else if(CREATE_OR_UPDATE_SPARE_PART_QNAME.equals(qname)) {
		            responsePojo = createOrUpdateSparePart(soapElement);
		            break;
		        } else if(CREATE_OR_UPDATE_SERVICE_QNAME.equals(qname)) {
		            responsePojo = createOrUpdateService(soapElement);
		            break;
		        } else if(CREATE_OR_UPDATE_SPARE_PART_GROUP_QNAME.equals(qname)) {
		            responsePojo = createOrUpdateSparePartGroup(soapElement);
		            break;
		        } else if(CREATE_OR_UPDATE_VEHICLE_QNAME.equals(qname)) {
		            responsePojo = createOrUpdateVehicle(soapElement);
		            break;
		        } else if(CREATE_OR_UPDATE_PERSON_QNAME.equals(qname)) {
		            responsePojo = createOrUpdatePerson(soapElement);
		            break;
		        } else if(CREATE_OR_UPDATE_COMPANY_QNAME.equals(qname)) {
		            responsePojo = createOrUpdateCompany(soapElement);
		            break;
		        }
			}
		}

		SOAPMessage soapResponse = MessageFactory.newInstance().createMessage();
		soapBody = soapResponse.getSOAPBody();
		if (responsePojo != null) {
			JAXB.marshal(responsePojo, new SAAJResult(soapBody));
		} else {
			SOAPFault fault = soapBody.addFault();
			fault.setFaultString("Unrecognized SOAP request.");
		}
		return soapResponse;
	}

	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateWarrantyConditionsResponse createOrUpdateWarrantyConditions(SOAPElement soapElement) {
		CreateOrUpdateWarrantyConditions request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateWarrantyConditions.class);
		
		boolean ret = new ElectronicShop().createOrUpdateWarrantyConditions(
				request.getElectronicShopID(),
				request.getWarrantyConditionsID(),
				request.getMonths(),
				request.getMilage(),
				request.getOtherConditions());
		
		CreateOrUpdateWarrantyConditionsResponse response = new CreateOrUpdateWarrantyConditionsResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateVehicleModelResponse createOrUpdateVehicleModel(SOAPElement soapElement) {
		CreateOrUpdateVehicleModel request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateVehicleModel.class);
		
		boolean ret = new ElectronicShop().createOrUpdateVehicleModel(
				request.getElectronicShopID(),
				request.getVehicleModelID(),
				request.getBrand(),
				request.getModel(),
				request.getCharacteristics());
		
		CreateOrUpdateVehicleModelResponse response = new CreateOrUpdateVehicleModelResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateVehicleModelPartResponse createOrUpdateVehicleModelPart(SOAPElement soapElement) {
		CreateOrUpdateVehicleModelPart request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateVehicleModelPart.class);
		
		boolean ret = new ElectronicShop().createOrUpdateVehicleModelPart(
				request.getElectronicShopID(),
				request.getVehicleModelID(),
				request.getSparePartID());
		
		CreateOrUpdateVehicleModelPartResponse response = new CreateOrUpdateVehicleModelPartResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateVehicleModelServiceResponse createOrUpdateVehicleModelService(SOAPElement soapElement) {
		CreateOrUpdateVehicleModelService request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateVehicleModelService.class);
		
		boolean ret = new ElectronicShop().createOrUpdateVehicleModelService(
				request.getElectronicShopID(),
				request.getVehicleModelID(),
				request.getServiceID(),
				request.getDurationHour(),
				request.getMonthsToNext(),
				request.getMilageToNext());
		
		CreateOrUpdateVehicleModelServiceResponse response = new CreateOrUpdateVehicleModelServiceResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateSparePartResponse createOrUpdateSparePart(SOAPElement soapElement) {
		CreateOrUpdateSparePart request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateSparePart.class);
		
		boolean ret = new ElectronicShop().createOrUpdateSparePart(
				request.getElectronicShopID(),
				request.getSparePartID(),
				request.getSparePartGroupID(),
				request.getName(),
				request.getDescription(),
				request.getDeliveryPrice(),
				request.getMeasuringUnit());
		
		CreateOrUpdateSparePartResponse response = new CreateOrUpdateSparePartResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateServiceResponse createOrUpdateService(SOAPElement soapElement) {
		CreateOrUpdateService request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateService.class);
		
		boolean ret = new ElectronicShop().createOrUpdateService(
				request.getElectronicShopID(),
				request.getServiceID(),
				request.getDescription());
		
		CreateOrUpdateServiceResponse response = new CreateOrUpdateServiceResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateSparePartGroupResponse createOrUpdateSparePartGroup(SOAPElement soapElement) {
		CreateOrUpdateSparePartGroup request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateSparePartGroup.class);
		
		boolean ret = new ElectronicShop().createOrUpdateSparePartGroup(
				request.getElectronicShopID(),
				request.getSparePartGroupID(),
				request.getDescription());
		
		CreateOrUpdateSparePartGroupResponse response = new CreateOrUpdateSparePartGroupResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateVehicleResponse createOrUpdateVehicle(SOAPElement soapElement) {
		CreateOrUpdateVehicle request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateVehicle.class);
		
		boolean ret = new ElectronicShop().createOrUpdateVehicle(
				request.getElectronicShopID(),
				request.getVin(),
				request.getClientID(),
				request.getVehicleModelID(),
				request.getWarrantyConditionsID(),
				request.getEngineNumber(),
				request.getPlateNumber(),
				request.getPurchaseDate());
		
		CreateOrUpdateVehicleResponse response = new CreateOrUpdateVehicleResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdatePersonResponse createOrUpdatePerson(SOAPElement soapElement) {
		CreateOrUpdatePerson request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdatePerson.class);
		
		boolean ret = new ElectronicShop().createOrUpdatePerson(
				request.getElectronicShopID(),
				request.getClientID(),
				request.getName(),
				request.getFamily(),
				request.getAddressCity(),
				request.getAddressLine(),
				request.getPhoneNumber(),
				request.getMail(),
				request.getIbanNumber(),
				request.getSwiftCode());
		
		CreateOrUpdatePersonResponse response = new CreateOrUpdatePersonResponse();
		response.setReturn(ret);
		
		return response;
	}
	
	/**
	 *  адаптер от SOAP извикване към истинското POJO извикване   
	 */
	CreateOrUpdateCompanyResponse createOrUpdateCompany(SOAPElement soapElement) {
		CreateOrUpdateCompany request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateCompany.class);
		
		boolean ret = new ElectronicShop().createOrUpdateCompany(
				request.getElectronicShopID(),
				request.getClientID(),
				request.getName(),
				request.getContactPerson(),
				request.getAddressCity(),
				request.getAddressLine(),
				request.getPhoneNumber(),
				request.getMail(),
				request.getIbanNumber(),
				request.getSwiftCode(),
				request.getRegistrationNumber(),
				request.getVatNumber());
		
		CreateOrUpdateCompanyResponse response = new CreateOrUpdateCompanyResponse();
		response.setReturn(ret);
		
		return response;
	}

}
