package introsde.assignment.model;

import introsde.assignment.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;


/**
 * The persistent class for the "MeasureType" database table.
 * 
 */
@Entity
@Cacheable(false)
@Table(name="MeasureType")
@NamedQuery(name="MeasureType.findAll", query="SELECT m FROM MeasureType m")

/**
 * Class for handling MeasureType database table
 * @author Toomas
 *
 */
public class MeasureType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_measuretype")
	@TableGenerator(name="sqlite_measuretype", table="sqlite_sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="MeasureType")
	@Column(name="idMeasureType")
	private int idMeasureType;

	@Column(name="measureName")
	private String measureName;

	// Getters
	@XmlTransient
	public int getIdMeasureType() {
		return this.idMeasureType;
	}
	
	public String getMeasureName() {
		return this.measureName;
	}

	// Setters
	public void setIdMeasureType(int idMeasureType) {
		this.idMeasureType = idMeasureType;
	}

	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	// database operations
	public static MeasureType getMeasureTypeById(int measureTypeId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		MeasureType p = em.find(MeasureType.class, measureTypeId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}
	
	public static List<MeasureType> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
	    List<MeasureType> list = em.createNamedQuery("MeasureType.findAll", MeasureType.class).getResultList();
	    LifeCoachDao.instance.closeConnections(em);
	    return list;
	}
	
	public static MeasureType saveMeasureType(MeasureType p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static MeasureType updateMeasureType(MeasureType p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	    return p;
	}
	
	public static void removeMeasureType(MeasureType p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
	    p=em.merge(p);
	    em.remove(p);
	    tx.commit();
	    LifeCoachDao.instance.closeConnections(em);
	}
}
