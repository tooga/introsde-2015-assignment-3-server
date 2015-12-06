package introsde.assignment.model;

import introsde.assignment.dao.LifeCoachDao;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
@Entity  // indicates that this class is an entity to persist in DB
@Cacheable(false)
@Table(name="HealthProfile") // to whole table must be persisted 
@NamedQuery(name="HealthProfile.findAll", query="SELECT hp FROM HealthProfile hp")

/**
 * Class for handling HealthProfile database table
 * @author Toomas
 *
 */
public class HealthProfile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id // defines this attributed as the one that identifies the entity
    @GeneratedValue(generator="sqlite_healthprofile")
    @TableGenerator(name="sqlite_healthprofile", table="sqlite_sequence",
        pkColumnName="name", valueColumnName="seq",
        pkColumnValue="HealthProfile")
    
    @Column(name="idHealthProfile")
    private int idHealthProfile;
    
    // Join healthProfile to person with OneToOne link
	@OneToOne
	@JoinColumn(name="idPerson",referencedColumnName="idPerson")
	private Person person;
	
    // Join one healthProfile to measures with OneToMany link
    @OneToMany(mappedBy="healthProfile",cascade=CascadeType.ALL)
    private List<Measure> measures;
    
    // getters
    @XmlTransient
    public int getIdHealthProfile(){
        return idHealthProfile;
    }
    
    @XmlTransient
    public Person getPerson() {
    	return person;
    }
    
    // Get current measures for healthprofile
    public List<Measure> getMeasures() {
    	if (measures == null) {
    		return measures;
    	}
    	DateFormat df = new SimpleDateFormat("yyyy-MM-DD"); 
    	List<Measure> currentMeasures = new ArrayList<Measure>();
    	// Loop through measures
    	for (int i = 0; i < measures.size(); i++) {
    		boolean currentMeasure = true;
    			// Loop through current measures
    		   for (int k = 0; k < currentMeasures.size(); k++) {
    			  // If same measureType already exist in current measures
    		      if(measures.get(i).getMeasure().equals(currentMeasures.get(k).getMeasure())) {
    		    	  try {
    		    		//  Compare creation dates and
						if (df.parse(measures.get(i).getCreated()).after(df.parse(currentMeasures.get(k).getCreated()))) {
							// If measure is newer than current one, remove the current one  
							currentMeasures.remove(k);
						  } else {
							 // Else set measure as not current 
							  currentMeasure = false;
						  }
					} catch (ParseException e) {
						e.printStackTrace();
					}
    		      }
    		   }
    		   // If measure is current measure, add to list
    		   if (currentMeasure) {
    			   currentMeasures.add(measures.get(i));
    		   }
    	}
        return currentMeasures;
    }
    
    // setters
    public void setMeasures(List<Measure> measures) {
    	this.measures = measures;
    }
    
    public void setIdHealthProfile(int idHealthProfile){
        this.idHealthProfile = idHealthProfile;
    }
    
    public void setPerson(Person person) {
    	this.person = person;
    }
    
    @XmlTransient
    public List<Measure> getAllMeasures() {
    	return measures;
    }
    
    
    // Database queries
    public static HealthProfile getHealthProfileById(int healthProfileId) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        HealthProfile hp = em.find(HealthProfile.class, healthProfileId);
        LifeCoachDao.instance.closeConnections(em);
        return hp;
    }

    public static List<HealthProfile> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        List<HealthProfile> list = em.createNamedQuery("HealthProfile.findAll", HealthProfile.class)
            .getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }

    public static HealthProfile saveHealthProfile(HealthProfile hp) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(hp);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return hp;
    } 

    public static HealthProfile updateHealthProfile(HealthProfile hp) {
        EntityManager em = LifeCoachDao.instance.createEntityManager(); 
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        hp=em.merge(hp);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return hp;
    }

    public static void removeHealthProfile(HealthProfile hp) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        hp=em.merge(hp);
        em.remove(hp);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }
    
}