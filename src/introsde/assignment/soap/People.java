package introsde.assignment.soap;
import introsde.assignment.model.Measure;
import introsde.assignment.model.MeasureType;
import introsde.assignment.model.Person;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL)
public interface People {

	// Method 1
    @WebMethod(operationName="readPersonList")
    @WebResult(name="people") 
    public List<Person> readPersonList();

    // Method 2
    @WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person readPerson(@WebParam(name="personId") int id);

    // Method 3
    @WebMethod(operationName="updatePerson")
    @WebResult(name="personId") 
    public int updatePerson(@WebParam(name="person") Person person);

    // Method 4
    @WebMethod(operationName="createPerson")
    @WebResult(name="newPerson") 
    public Person createPerson(@WebParam(name="person") Person person);

    // Method 5
    @WebMethod(operationName="deletePerson")
    @WebResult(name="deletedValue") 
    public boolean deletePerson(@WebParam(name="personId") int id);
    
    // Method 6
    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="measurehistory") 
    public List<Measure> readPersonHistory(@WebParam(name="personId") int id, @WebParam(name="measureType") String measureType);
    
    // Method 7
    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measuretypes") 
    public List<MeasureType> readMeasureTypes();
    
    // Method 8
    @WebMethod(operationName="readPersonMeasure")
    @WebResult(name="measure") 
    public Measure readPersonMeasure(@WebParam(name="personId") int id, @WebParam(name="measureType") String measureType, @WebParam(name="measureId") int mid);
    
    // Method 9
    @WebMethod(operationName="savePersonMeasure")
    @WebResult(name="newMeasure") 
    public Measure savePersonMeasure(@WebParam(name="personId") int id, @WebParam(name="measure") Measure measure);
    
    // Method 10
    @WebMethod(operationName="updatePersonMeasure")
    @WebResult(name="measureId") 
    public int updatePersonMeasure(@WebParam(name="personId") int id, @WebParam(name="measure") Measure measure);

}
