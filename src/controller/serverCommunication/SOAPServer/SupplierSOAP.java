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

import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateSparePartSupplier;
import controller.serverCommunication.SOAPServer.jaxws.CreateOrUpdateSparePartSupplierResponse;

/**
 * 
 * обработва SOAP заявките свързани с доставчиците на резервни части и консумативи
 *
 */
@SuppressWarnings("serial")
public class SupplierSOAP extends HttpServlet {
	
	/**
	 * тези се вземат от генерираните класове jaxws/X.java и jaxws/XResponse.java
	 */
	private static final String NAMESPACE_URI = "http://SOAPServer.serverCommunication.controller/";
	private static final QName CREATE_OR_UPDATE_SPARE_PART_SUPPLIER_QNAME = new QName(NAMESPACE_URI, "createOrUpdateSparePartSupplier");
	
	
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
				if(CREATE_OR_UPDATE_SPARE_PART_SUPPLIER_QNAME.equals(qname)) {
					responsePojo = createOrUpdateSparePartSupplier(soapElement);
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
	CreateOrUpdateSparePartSupplierResponse createOrUpdateSparePartSupplier(SOAPElement soapElement) {
		CreateOrUpdateSparePartSupplier request = JAXB.unmarshal(new DOMSource(soapElement), CreateOrUpdateSparePartSupplier.class);
		
		boolean ret = new Supplier().createOrUpdateSparePartSupplier(
				request.getSupplierID(),
				request.getOriginalSparePartID(),
				request.getSupplierSparePartID(),
				request.getDeliveryPrice());
		
		CreateOrUpdateSparePartSupplierResponse response = new CreateOrUpdateSparePartSupplierResponse();
		response.setReturn(ret);
		
		return response;
	}

}
