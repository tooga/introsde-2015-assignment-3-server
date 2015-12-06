package introsde.assignment.model;

import introsde.assignment.dao.LifeCoachDao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * The persistent class for the "Measure" database table.
 * 
 */
@XmlType(name = "Measure", propOrder = {
	    "mid",
	    "created",
	    "measure",
	    "value",
	    "valueType"
	})
@Entity
@Cacheable(false)
@Table(name = "Measure")
@NamedQuery(name = "Measure.findAll", query = "SELECT m FROM Measure m")

/**
 * Class for handling Measure database table
 * @author Toomas
 *
 */
public class Measure implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_measure")
	@TableGenerator(name="sqlite_measure", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="Measure")
	
	@Column(name = "idMeasure")
	private int mid;

	@Column(name = "value")
	private double value;
	
	@Column(name = "created")
	private String created;
	
	@Column(name="measure")
	private String measure;
	
	@Column(name="valueType")
	private String valueType;
	
	// Join measure to person with ManyToOne link
	@ManyToOne
	@JoinColumn(name="idPerson",referencedColumnName="idPerson")
	private Person person;
	
	// Join measure to healthProfile with ManyToOne link
	@ManyToOne
	@JoinColumn(name="idHealthProfile",referencedColumnName="idHealthProfile")
	private HealthProfile healthProfile;

	// Getters
	public int getMid() {
		return mid;
	}
	
	public String getMeasure() {
		return measure;
	}
	
	public double getValue() {
		return value;
	}
	
	public String getCreated() {
		return created;
	}
	
	@XmlTransient
	public Person getPerson() {
		return person;
	}
	
	@XmlTransient
	public HealthProfile getHealthProfile() {
		return healthProfile;
	}
	
	public String getValueType() {
		return this.valueType;
	}
	
	// Setters
	
	public void setMid(int mid) {
		this.mid = mid;
	}
	
	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	public void setCreated(String created) {
		this.created = created;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public void setHealthProfile(HealthProfile healthProfile) {
		this.healthProfile = healthProfile;
	}
	
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	
	// Database operations
	public static Measure getMeasureById(int measureId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Measure m = em.find(Measure.class, measureId);
		LifeCoachDao.instance.closeConnections(em);
		return m;
	}
	
	public static List<Measure> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<Measure> list = em.createNamedQuery("Measure.findAll", Measure.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static List<Measure> getMeasureHistory(int id, String measureType, String beforeDate, String afterDate) {
        
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<Measure> measureList = em.createNamedQuery("Measure.findAll", Measure.class).getResultList();
	    List<Measure> measureHistory = new ArrayList<Measure>();
	    LifeCoachDao.instance.closeConnections(em);
	    
	    DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
	    
	    for (Measure measure: measureList) {
	    	// Check that measure has same type and id than in the uri request
	    	if (measure.getMeasure().equals(measureType) && measure.getPerson().getIdPerson() == id) {
	    			// If queryParams presented, check that measure date fits the query params
					try {
						if (beforeDate == null || (beforeDate != null && df.parse(measure.getCreated()).before(df.parse(beforeDate)))) {
							if (afterDate == null || (afterDate != null && df.parse(measure.getCreated()).after(df.parse(afterDate)))) {
								// If measure passes checks, add to list
								measureHistory.add(measure);
							}
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
	    	}
	    }
	    return measureHistory;
	}
	
	public static Measure saveMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(m);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return m;
	}
	
	public static Measure updateMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		m=em.merge(m);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return m;
	}
	
	public static void removeMeasure(Measure m) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    m=em.merge(m);
	    em.remove(m);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
}
