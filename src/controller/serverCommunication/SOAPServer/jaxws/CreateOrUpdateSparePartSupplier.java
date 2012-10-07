
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateSparePartSupplier", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateSparePartSupplier", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "supplierID",
    "originalSparePartID",
    "supplierSparePartID",
    "deliveryPrice"
})
public class CreateOrUpdateSparePartSupplier {

    @XmlElement(name = "supplierID", namespace = "")
    private String supplierID;
    @XmlElement(name = "originalSparePartID", namespace = "")
    private String originalSparePartID;
    @XmlElement(name = "supplierSparePartID", namespace = "")
    private String supplierSparePartID;
    @XmlElement(name = "deliveryPrice", namespace = "")
    private double deliveryPrice;

    /**
     * 
     * @return
     *     returns String
     */
    public String getSupplierID() {
        return this.supplierID;
    }

    /**
     * 
     * @param supplierID
     *     the value for the supplierID property
     */
    public void setSupplierID(String supplierID) {
        this.supplierID = supplierID;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getOriginalSparePartID() {
        return this.originalSparePartID;
    }

    /**
     * 
     * @param originalSparePartID
     *     the value for the originalSparePartID property
     */
    public void setOriginalSparePartID(String originalSparePartID) {
        this.originalSparePartID = originalSparePartID;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getSupplierSparePartID() {
        return this.supplierSparePartID;
    }

    /**
     * 
     * @param supplierSparePartID
     *     the value for the supplierSparePartID property
     */
    public void setSupplierSparePartID(String supplierSparePartID) {
        this.supplierSparePartID = supplierSparePartID;
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

}
