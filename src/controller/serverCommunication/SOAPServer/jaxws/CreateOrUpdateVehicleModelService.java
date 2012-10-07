
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateVehicleModelService", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateVehicleModelService", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "vehicleModelID",
    "serviceID",
    "durationHour",
    "monthsToNext",
    "milageToNext"
})
public class CreateOrUpdateVehicleModelService {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "vehicleModelID", namespace = "")
    private String vehicleModelID;
    @XmlElement(name = "serviceID", namespace = "")
    private String serviceID;
    @XmlElement(name = "durationHour", namespace = "")
    private double durationHour;
    @XmlElement(name = "monthsToNext", namespace = "")
    private long monthsToNext;
    @XmlElement(name = "milageToNext", namespace = "")
    private long milageToNext;

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
     *     returns double
     */
    public double getDurationHour() {
        return this.durationHour;
    }

    /**
     * 
     * @param durationHour
     *     the value for the durationHour property
     */
    public void setDurationHour(double durationHour) {
        this.durationHour = durationHour;
    }

    /**
     * 
     * @return
     *     returns long
     */
    public long getMonthsToNext() {
        return this.monthsToNext;
    }

    /**
     * 
     * @param monthsToNext
     *     the value for the monthsToNext property
     */
    public void setMonthsToNext(long monthsToNext) {
        this.monthsToNext = monthsToNext;
    }

    /**
     * 
     * @return
     *     returns long
     */
    public long getMilageToNext() {
        return this.milageToNext;
    }

    /**
     * 
     * @param milageToNext
     *     the value for the milageToNext property
     */
    public void setMilageToNext(long milageToNext) {
        this.milageToNext = milageToNext;
    }

}
