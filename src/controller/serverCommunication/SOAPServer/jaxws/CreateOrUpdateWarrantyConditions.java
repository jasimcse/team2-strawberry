
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateWarrantyConditions", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateWarrantyConditions", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "warrantyConditionsID",
    "months",
    "milage",
    "otherConditions"
})
public class CreateOrUpdateWarrantyConditions {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "warrantyConditionsID", namespace = "")
    private String warrantyConditionsID;
    @XmlElement(name = "months", namespace = "")
    private long months;
    @XmlElement(name = "milage", namespace = "")
    private long milage;
    @XmlElement(name = "otherConditions", namespace = "")
    private String otherConditions;

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
    public String getWarrantyConditionsID() {
        return this.warrantyConditionsID;
    }

    /**
     * 
     * @param warrantyConditionsID
     *     the value for the warrantyConditionsID property
     */
    public void setWarrantyConditionsID(String warrantyConditionsID) {
        this.warrantyConditionsID = warrantyConditionsID;
    }

    /**
     * 
     * @return
     *     returns long
     */
    public long getMonths() {
        return this.months;
    }

    /**
     * 
     * @param months
     *     the value for the months property
     */
    public void setMonths(long months) {
        this.months = months;
    }

    /**
     * 
     * @return
     *     returns long
     */
    public long getMilage() {
        return this.milage;
    }

    /**
     * 
     * @param milage
     *     the value for the milage property
     */
    public void setMilage(long milage) {
        this.milage = milage;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getOtherConditions() {
        return this.otherConditions;
    }

    /**
     * 
     * @param otherConditions
     *     the value for the otherConditions property
     */
    public void setOtherConditions(String otherConditions) {
        this.otherConditions = otherConditions;
    }

}
