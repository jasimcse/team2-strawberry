package controller.serverCommunication;

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


import controller.serverCommunication.jaxws.CreateOrUpdateWarrantyConditions;
import controller.serverCommunication.jaxws.CreateOrUpdateWarrantyConditionsResponse;

/**
 * 
 * обработва SOAP заявките свързани с електронния магазин
 *
 */
@SuppressWarnings("serial")
public class ElectronicShopSOAP extends HttpServlet {
	
	/**
	 * тези се вземат от генерираните класове jaxws/XXX.java и jaxws/XXXResponse.java
	 */
	private static final String NAMESPACE_URI = "http://serverCommunication.controller/";
	private static final QName CREATE_OR_UPDATE_WARRANTY_CONDITIONS_QNAME = new QName(NAMESPACE_URI, "createOrUpdateWarrantyConditions");

	
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
				} /*else if(CREATE_OR_UPDATE_ITEM_QNAME.equals(qname)) {
		            responsePojo = handleCreateOrUpdateItemRequest(soapElement);
		            break;
		        }*/
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
		
		String ID = request.getId();
		long months = request.getMonths();
		long milage = request.getMilage();
		String otherConditions = request.getOtherConditions();
		
		boolean ret = new ElectronicShop().createOrUpdateWarrantyConditions(ID, months, milage, otherConditions);
		CreateOrUpdateWarrantyConditionsResponse response = new CreateOrUpdateWarrantyConditionsResponse();
		response.setReturn(ret);
		
		return response;
	}

}
