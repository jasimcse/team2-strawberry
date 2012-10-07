
package controller.serverCommunication.SOAPServer.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "createOrUpdateCompany", namespace = "http://SOAPServer.serverCommunication.controller/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "createOrUpdateCompany", namespace = "http://SOAPServer.serverCommunication.controller/", propOrder = {
    "electronicShopID",
    "clientID",
    "name",
    "contactPerson",
    "addressCity",
    "addressLine",
    "phoneNumber",
    "mail",
    "ibanNumber",
    "swiftCode",
    "registrationNumber",
    "vatNumber"
})
public class CreateOrUpdateCompany {

    @XmlElement(name = "electronicShopID", namespace = "")
    private String electronicShopID;
    @XmlElement(name = "clientID", namespace = "")
    private String clientID;
    @XmlElement(name = "name", namespace = "")
    private String name;
    @XmlElement(name = "contactPerson", namespace = "")
    private String contactPerson;
    @XmlElement(name = "addressCity", namespace = "")
    private String addressCity;
    @XmlElement(name = "addressLine", namespace = "")
    private String addressLine;
    @XmlElement(name = "phoneNumber", namespace = "")
    private String phoneNumber;
    @XmlElement(name = "mail", namespace = "")
    private String mail;
    @XmlElement(name = "IBANNumber", namespace = "")
    private String ibanNumber;
    @XmlElement(name = "SWIFTCode", namespace = "")
    private String swiftCode;
    @XmlElement(name = "registrationNumber", namespace = "")
    private String registrationNumber;
    @XmlElement(name = "VATNumber", namespace = "")
    private String vatNumber;

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
    public String getName() {
        return this.name;
    }

    /**
     * 
     * @param name
     *     the value for the name property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getContactPerson() {
        return this.contactPerson;
    }

    /**
     * 
     * @param contactPerson
     *     the value for the contactPerson property
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getAddressCity() {
        return this.addressCity;
    }

    /**
     * 
     * @param addressCity
     *     the value for the addressCity property
     */
    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getAddressLine() {
        return this.addressLine;
    }

    /**
     * 
     * @param addressLine
     *     the value for the addressLine property
     */
    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * 
     * @param phoneNumber
     *     the value for the phoneNumber property
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getMail() {
        return this.mail;
    }

    /**
     * 
     * @param mail
     *     the value for the mail property
     */
    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getIbanNumber() {
        return this.ibanNumber;
    }

    /**
     * 
     * @param ibanNumber
     *     the value for the ibanNumber property
     */
    public void setIbanNumber(String ibanNumber) {
        this.ibanNumber = ibanNumber;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getSwiftCode() {
        return this.swiftCode;
    }

    /**
     * 
     * @param swiftCode
     *     the value for the swiftCode property
     */
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    /**
     * 
     * @param registrationNumber
     *     the value for the registrationNumber property
     */
    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    /**
     * 
     * @return
     *     returns String
     */
    public String getVatNumber() {
        return this.vatNumber;
    }

    /**
     * 
     * @param vatNumber
     *     the value for the vatNumber property
     */
    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

}
