package introsde.assignment.soap;
import introsde.assignment.model.Measure;
import introsde.assignment.model.MeasureType;
import introsde.assignment.model.Person;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

/**
 * Service implementation
 * @author Toomas
 *
 */
@WebService(endpointInterface = "introsde.assignment.soap.People",
    serviceName="PeopleService")
public class PeopleImpl implements People {


    @Override
    public List<Person> readPersonList() {
        return Person.getAll();
    }

    @Override
    public Person readPerson(int id) {
        return Person.getPersonById(id);
    }

    @Override
    public Person createPerson(Person person) {
        return Person.savePerson(person);
    }

    @Override
    public int updatePerson(Person person) {
    	// Updating only person information
    	person.setHealthProfile(null);
        Person updPerson = Person.updatePerson(person);
        return updPerson.getIdPerson();
    }

    @Override
    public boolean deletePerson(int id) {
        Person p = Person.getPersonById(id);
        if (p!=null) {
            Person.removePerson(p);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Measure> readPersonHistory(int id, String measureType) {
        List<Measure> measures = Measure.getMeasureHistory(id, measureType, null, null);
        return measures;
    }

    @Override
    public List<MeasureType> readMeasureTypes() {
        return MeasureType.getAll();
    }

    @Override
    public Measure readPersonMeasure(int id, String measureType, int mid) {
        Measure m = Measure.getMeasureById(mid);
        // Check that measure equals the given measure type and person id
        if (m.getMeasure().equals(measureType) && m.getPerson().getIdPerson() == id) {
            return m;
        } else {
            System.out.println("Could not find measure " + measureType + " with id " + mid + " from person " + id);
            return null;
        }
    }

    @Override
    public Measure savePersonMeasure(int id, Measure measure) {
        Person person = Person.getPersonById(id);
        // Set person and healthProfile for measure
        measure.setPerson(person);
        measure.setHealthProfile(person.getHealthProfile());
        // If no created date, set the current date as measure created date
        if (measure.getCreated() == null) {
            measure.setCreated(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        return Measure.saveMeasure(measure);
    }

    @Override
    public int updatePersonMeasure(int id, Measure measure) {
        Person person = Person.getPersonById(id);
        // Set person and healthProfile for measure
        measure.setPerson(person);
        measure.setHealthProfile(person.getHealthProfile());
        Measure.updateMeasure(measure);
        return measure.getMid();
    }

}