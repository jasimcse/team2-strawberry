
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateVehicleModel", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateVehicleModel", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "vehicleModelID",
    "brand",
    "model",
    "characteristics"
})
public class CreateOrUpdateVehicleModel {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "vehicleModelID", namespace = "")
    private String vehicleModelID;
    @XmlElement(name = "brand", namespace = "")
    private String brand;
    @XmlElement(name = "model", namespace = "")
    private String model;
    @XmlElement(name = "characteristics", namespace = "")
    private String characteristics;

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
    public String getBrand() {
        return this.brand;
    }

    /**
     * 
     * @param brand
     *     the value for the brand property
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getModel() {
        return this.model;
    }

    /**
     * 
     * @param model
     *     the value for the model property
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getCharacteristics() {
        return this.characteristics;
    }

    /**
     * 
     * @param characteristics
     *     the value for the characteristics property
     */
    public void setCharacteristics(String characteristics) {
        this.characteristics = characteristics;
    }

}
