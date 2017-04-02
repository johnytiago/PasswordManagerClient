
package ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for message complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="message">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="domain" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="usernameHash" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="domainHash" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="counter" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="publicKey" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "message", propOrder = {
    "domain",
    "username",
    "password",
    "usernameHash",
    "domainHash",
    "counter",
    "publicKey"
})
public class Message {

    protected byte[] domain;
    protected byte[] username;
    protected byte[] password;
    protected byte[] usernameHash;
    protected byte[] domainHash;
    protected int counter;
    protected byte[] publicKey;

    /**
     * Gets the value of the domain property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDomain() {
        return domain;
    }

    /**
     * Sets the value of the domain property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDomain(byte[] value) {
        this.domain = value;
    }

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setUsername(byte[] value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPassword(byte[] value) {
        this.password = value;
    }

    /**
     * Gets the value of the usernameHash property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getUsernameHash() {
        return usernameHash;
    }

    /**
     * Sets the value of the usernameHash property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setUsernameHash(byte[] value) {
        this.usernameHash = value;
    }

    /**
     * Gets the value of the domainHash property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getDomainHash() {
        return domainHash;
    }

    /**
     * Sets the value of the domainHash property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setDomainHash(byte[] value) {
        this.domainHash = value;
    }

    /**
     * Gets the value of the counter property.
     * 
     */
    public int getCounter() {
        return counter;
    }

    /**
     * Sets the value of the counter property.
     * 
     */
    public void setCounter(int value) {
        this.counter = value;
    }

    /**
     * Gets the value of the publicKey property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getPublicKey() {
        return publicKey;
    }

    /**
     * Sets the value of the publicKey property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setPublicKey(byte[] value) {
        this.publicKey = value;
    }

}
