
package controller.serverCommunication.SOAPServer.jaxws;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateVehicle", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateVehicle", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "vin",
    "clientID",
    "vehicleModelID",
    "warrantyConditionsID",
    "engineNumber",
    "plateNumber",
    "purchaseDate"
})
public class CreateOrUpdateVehicle {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "VIN", namespace = "")
    private String vin;
    @XmlElement(name = "clientID", namespace = "")
    private String clientID;
    @XmlElement(name = "vehicleModelID", namespace = "")
    private String vehicleModelID;
    @XmlElement(name = "warrantyConditionsID", namespace = "")
    private String warrantyConditionsID;
    @XmlElement(name = "engineNumber", namespace = "")
    private String engineNumber;
    @XmlElement(name = "plateNumber", namespace = "")
    private String plateNumber;
    @XmlElement(name = "purchaseDate", namespace = "")
    private Date purchaseDate;

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

    /**
     * 
     * @return
     *     returns String
     */
    public String getClientID() {
        return this.clientID;
    }

    /**
     * 
     * @param clientID
     *     the value for the clientID property
     */
    public void setClientID(String clientID) {
        this.clientID = clientID;
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
     *     returns String
     */
    public String getEngineNumber() {
        return this.engineNumber;
    }

    /**
     * 
     * @param engineNumber
     *     the value for the engineNumber property
     */
    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getPlateNumber() {
        return this.plateNumber;
    }

    /**
     * 
     * @param plateNumber
     *     the value for the plateNumber property
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    /**
     * 
     * @return
     *     returns Date
     */
    public Date getPurchaseDate() {
        return this.purchaseDate;
    }

    /**
     * 
     * @param purchaseDate
     *     the value for the purchaseDate property
     */
    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}
