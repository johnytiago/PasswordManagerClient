
package ws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "PasswordManagerWS", targetNamespace = "http://ws/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface PasswordManagerWS {


    /**
     * 
     * @param arg0
     * @return
     *     returns ws.Envelope
     * @throws PasswordManagerException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "get", targetNamespace = "http://ws/", className = "ws.Get")
    @ResponseWrapper(localName = "getResponse", targetNamespace = "http://ws/", className = "ws.GetResponse")
    @Action(input = "http://ws/PasswordManagerWS/getRequest", output = "http://ws/PasswordManagerWS/getResponse", fault = {
        @FaultAction(className = PasswordManagerException_Exception.class, value = "http://ws/PasswordManagerWS/get/Fault/PasswordManagerException")
    })
    public Envelope get(
        @WebParam(name = "arg0", targetNamespace = "")
        Envelope arg0)
        throws PasswordManagerException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns ws.Envelope
     * @throws PasswordManagerException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "put", targetNamespace = "http://ws/", className = "ws.Put")
    @ResponseWrapper(localName = "putResponse", targetNamespace = "http://ws/", className = "ws.PutResponse")
    @Action(input = "http://ws/PasswordManagerWS/putRequest", output = "http://ws/PasswordManagerWS/putResponse", fault = {
        @FaultAction(className = PasswordManagerException_Exception.class, value = "http://ws/PasswordManagerWS/put/Fault/PasswordManagerException")
    })
    public Envelope put(
        @WebParam(name = "arg0", targetNamespace = "")
        Envelope arg0)
        throws PasswordManagerException_Exception
    ;

    /**
     * 
     * @param arg0
     * @return
     *     returns ws.Envelope
     * @throws PasswordManagerException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "")
    @RequestWrapper(localName = "register", targetNamespace = "http://ws/", className = "ws.Register")
    @ResponseWrapper(localName = "registerResponse", targetNamespace = "http://ws/", className = "ws.RegisterResponse")
    @Action(input = "http://ws/PasswordManagerWS/registerRequest", output = "http://ws/PasswordManagerWS/registerResponse", fault = {
        @FaultAction(className = PasswordManagerException_Exception.class, value = "http://ws/PasswordManagerWS/register/Fault/PasswordManagerException")
    })
    public Envelope register(
        @WebParam(name = "arg0", targetNamespace = "")
        Envelope arg0)
        throws PasswordManagerException_Exception
    ;

}
