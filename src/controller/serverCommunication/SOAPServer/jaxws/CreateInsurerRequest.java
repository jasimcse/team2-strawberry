
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createInsurerRequest", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createInsurerRequest", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "insurerID",
    "vin"
})
public class CreateInsurerRequest {

    @XmlElement(name = "insurerID", namespace = "")
    private String insurerID;
    @XmlElement(name = "VIN", namespace = "")
    private String vin;

    /**
     * 
     * @return
     *     returns String
     */
    public String getInsurerID() {
        return this.insurerID;
    }

    /**
     * 
     * @param insurerID
     *     the value for the insurerID property
     */
    public void setInsurerID(String insurerID) {
        this.insurerID = insurerID;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getVin() {
        return this.vin;
    }

    /**
     * 
     * @param vin
     *     the value for the vin property
     */
    public void setVin(String vin) {
        this.vin = vin;
    }

}
