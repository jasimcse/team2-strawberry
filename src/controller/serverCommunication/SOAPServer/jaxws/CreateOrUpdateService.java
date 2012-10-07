
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateService", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateService", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "serviceID",
    "description"
})
public class CreateOrUpdateService {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "serviceID", namespace = "")
    private String serviceID;
    @XmlElement(name = "description", namespace = "")
    private String description;

    /**
     * 
     * @return
     *     returns String
     */
    public String getElectronicShopID() {
        return this.electronicShopID;
    }

    /**
     * 
     * @param electronicShopID
     *     the value for the electronicShopID property
     */
    public void setElectronicShopID(String electronicShopID) {
        this.electronicShopID = electronicShopID;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getServiceID() {
        return this.serviceID;
    }

    /**
     * 
     * @param serviceID
     *     the value for the serviceID property
     */
    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * 
     * @param description
     *     the value for the description property
     */
    public void setDescription(String description) {
        this.description = description;
    }

}
