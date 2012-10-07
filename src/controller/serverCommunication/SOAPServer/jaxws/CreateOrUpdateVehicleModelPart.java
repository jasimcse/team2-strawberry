
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateVehicleModelPart", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateVehicleModelPart", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "vehicleModelID",
    "sparePartID"
})
public class CreateOrUpdateVehicleModelPart {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "vehicleModelID", namespace = "")
    private String vehicleModelID;
    @XmlElement(name = "sparePartID", namespace = "")
    private String sparePartID;

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
    public String getVehicleModelID() {
        return this.vehicleModelID;
    }

    /**
     * 
     * @param vehicleModelID
     *     the value for the vehicleModelID property
     */
    public void setVehicleModelID(String vehicleModelID) {
        this.vehicleModelID = vehicleModelID;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getSparePartID() {
        return this.sparePartID;
    }

    /**
     * 
     * @param sparePartID
     *     the value for the sparePartID property
     */
    public void setSparePartID(String sparePartID) {
        this.sparePartID = sparePartID;
    }

}
