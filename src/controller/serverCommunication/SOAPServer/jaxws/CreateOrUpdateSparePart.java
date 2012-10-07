
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateSparePart", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateSparePart", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "sparePartID",
    "sparePartGroupID",
    "description",
    "deliveryPrice",
    "measuringUnit"
})
public class CreateOrUpdateSparePart {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "sparePartID", namespace = "")
    private String sparePartID;
    @XmlElement(name = "sparePartGroupID", namespace = "")
    private String sparePartGroupID;
    @XmlElement(name = "description", namespace = "")
    private String description;
    @XmlElement(name = "deliveryPrice", namespace = "")
    private double deliveryPrice;
    @XmlElement(name = "measuringUnit", namespace = "")
    private String measuringUnit;

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

    /**
     * 
     * @return
     *     returns double
     */
    public double getDeliveryPrice() {
        return this.deliveryPrice;
    }

    /**
     * 
     * @param deliveryPrice
     *     the value for the deliveryPrice property
     */
    public void setDeliveryPrice(double deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getMeasuringUnit() {
        return this.measuringUnit;
    }

    /**
     * 
     * @param measuringUnit
     *     the value for the measuringUnit property
     */
    public void setMeasuringUnit(String measuringUnit) {
        this.measuringUnit = measuringUnit;
    }

}
