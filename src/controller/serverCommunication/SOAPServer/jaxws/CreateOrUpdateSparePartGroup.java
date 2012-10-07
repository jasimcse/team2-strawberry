
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateSparePartGroup", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateSparePartGroup", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "sparePartGroupID",
    "description"
})
public class CreateOrUpdateSparePartGroup {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "sparePartGroupID", namespace = "")
    private String sparePartGroupID;
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
    public String getSparePartGroupID() {
        return this.sparePartGroupID;
    }

    /**
     * 
     * @param sparePartGroupID
     *     the value for the sparePartGroupID property
     */
    public void setSparePartGroupID(String sparePartGroupID) {
        this.sparePartGroupID = sparePartGroupID;
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
