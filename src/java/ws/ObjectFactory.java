
package ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PutResponse_QNAME = new QName("http://ws/", "putResponse");
    private final static QName _GetResponse_QNAME = new QName("http://ws/", "getResponse");
    private final static QName _RegisterResponse_QNAME = new QName("http://ws/", "registerResponse");
    private final static QName _Get_QNAME = new QName("http://ws/", "get");
    private final static QName _Put_QNAME = new QName("http://ws/", "put");
    private final static QName _Register_QNAME = new QName("http://ws/", "register");
    private final static QName _PutArg3_QNAME = new QName("", "arg3");
    private final static QName _PutArg2_QNAME = new QName("", "arg2");
    private final static QName _PutArg1_QNAME = new QName("", "arg1");
    private final static QName _PutArg0_QNAME = new QName("", "arg0");
    private final static QName _GetResponseReturn_QNAME = new QName("", "return");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link PutResponse }
     * 
     */
    public PutResponse createPutResponse() {
        return new PutResponse();
    }

    /**
     * Create an instance of {@link GetResponse }
     * 
     */
    public GetResponse createGetResponse() {
        return new GetResponse();
    }

    /**
     * Create an instance of {@link RegisterResponse }
     * 
     */
    public RegisterResponse createRegisterResponse() {
        return new RegisterResponse();
    }

    /**
     * Create an instance of {@link Get }
     * 
     */
    public Get createGet() {
        return new Get();
    }

    /**
     * Create an instance of {@link Put }
     * 
     */
    public Put createPut() {
        return new Put();
    }

    /**
     * Create an instance of {@link Register }
     * 
     */
    public Register createRegister() {
        return new Register();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PutResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "putResponse")
    public JAXBElement<PutResponse> createPutResponse(PutResponse value) {
        return new JAXBElement<PutResponse>(_PutResponse_QNAME, PutResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "getResponse")
    public JAXBElement<GetResponse> createGetResponse(GetResponse value) {
        return new JAXBElement<GetResponse>(_GetResponse_QNAME, GetResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "registerResponse")
    public JAXBElement<RegisterResponse> createRegisterResponse(RegisterResponse value) {
        return new JAXBElement<RegisterResponse>(_RegisterResponse_QNAME, RegisterResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Get }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "get")
    public JAXBElement<Get> createGet(Get value) {
        return new JAXBElement<Get>(_Get_QNAME, Get.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Put }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "put")
    public JAXBElement<Put> createPut(Put value) {
        return new JAXBElement<Put>(_Put_QNAME, Put.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Register }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://ws/", name = "register")
    public JAXBElement<Register> createRegister(Register value) {
        return new JAXBElement<Register>(_Register_QNAME, Register.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg3", scope = Put.class)
    public JAXBElement<byte[]> createPutArg3(byte[] value) {
        return new JAXBElement<byte[]>(_PutArg3_QNAME, byte[].class, Put.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg2", scope = Put.class)
    public JAXBElement<byte[]> createPutArg2(byte[] value) {
        return new JAXBElement<byte[]>(_PutArg2_QNAME, byte[].class, Put.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg1", scope = Put.class)
    public JAXBElement<byte[]> createPutArg1(byte[] value) {
        return new JAXBElement<byte[]>(_PutArg1_QNAME, byte[].class, Put.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg0", scope = Put.class)
    public JAXBElement<byte[]> createPutArg0(byte[] value) {
        return new JAXBElement<byte[]>(_PutArg0_QNAME, byte[].class, Put.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg2", scope = Get.class)
    public JAXBElement<byte[]> createGetArg2(byte[] value) {
        return new JAXBElement<byte[]>(_PutArg2_QNAME, byte[].class, Get.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg1", scope = Get.class)
    public JAXBElement<byte[]> createGetArg1(byte[] value) {
        return new JAXBElement<byte[]>(_PutArg1_QNAME, byte[].class, Get.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg0", scope = Get.class)
    public JAXBElement<byte[]> createGetArg0(byte[] value) {
        return new JAXBElement<byte[]>(_PutArg0_QNAME, byte[].class, Get.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "arg0", scope = Register.class)
    public JAXBElement<byte[]> createRegisterArg0(byte[] value) {
        return new JAXBElement<byte[]>(_PutArg0_QNAME, byte[].class, Register.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "return", scope = GetResponse.class)
    public JAXBElement<byte[]> createGetResponseReturn(byte[] value) {
        return new JAXBElement<byte[]>(_GetResponseReturn_QNAME, byte[].class, GetResponse.class, ((byte[]) value));
    }

}
