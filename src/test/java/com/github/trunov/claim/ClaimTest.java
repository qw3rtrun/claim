package com.github.trunov.claim;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

import static javax.persistence.Persistence.createEntityManagerFactory;
import static org.junit.Assert.*;

public class ClaimTest {

    private static EntityManagerFactory emf;

    private EntityManager em;

    private Employee empFred;

    private Employee empStiv;

    private Customer cBob;

    private Customer cJake;


    @BeforeClass
    public static void staticInit() {
        emf = createEntityManagerFactory("claim");
    }

    @Before
    public void init() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        Department dev = new Department();
        dev.setName("DEV");
        em.persist(dev);
        Department qa = new Department();
        qa.setName("QA");
        em.persist(qa);
        empFred = new Employee();
        empFred.setName("Fred");
        empFred.setBirthDate(new Date());
        empFred.setDepartment(qa);
        empFred.setPost("Tester");
        empFred.setSex(true);
        em.persist(empFred);
        empStiv = new Employee();
        empStiv.setName("Stiv");
        empStiv.setBirthDate(new Date());
        empStiv.setDepartment(dev);
        empStiv.setPost("Developer");
        empStiv.setSex(true);
        em.persist(empStiv);
        cBob = new Customer();
        cBob.setName("Bob");
        cBob.setBirthDate(new Date());
        cBob.setRegistrationDate(new Date());
        cBob.setSex(true);
        em.persist(cBob);
        Customer cMary = new Customer();
        cMary.setName("Mary");
        cMary.setBirthDate(new Date());
        cMary.setRegistrationDate(new Date());
        cMary.setSex(true);
        em.persist(cMary);
        Customer cJohn = new Customer();
        cJohn.setName("John");
        cJohn.setBirthDate(new Date(123454000l));
        cJohn.setRegistrationDate(new Date());
        cJohn.setSex(true);
        em.persist(cJohn);
        cJake = new Customer();
        cJake.setName("Jake");
        cJake.setBirthDate(new Date());
        cJake.setRegistrationDate(new Date());
        cJake.setSex(true);
        em.persist(cJake);
    }

    @After
    public void clean() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        em.getTransaction().begin();
        Claim.clear(em);
        em.getTransaction().commit();
    }

    @Test
    public void persistTest() {
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        claim.setCustomer(cBob);
        claim.setEmployee(empFred);
        em.persist(claim);
    }

    @Test
    public void maxNumTest() {
        assertNull(Claim.maxNum(em));
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        claim.setCustomer(cBob);
        claim.setEmployee(empFred);
        em.persist(claim);
        assertEquals(new Long(1l), Claim.maxNum(em));
    }

    @Test
    public void byNumTest() {
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        claim.setCustomer(cBob);
        claim.setEmployee(empFred);
        em.persist(claim);
        claim = new Claim();
        claim.setNum(2l);
        claim.setCount(2);
        claim.setProduct("Milk");
        claim.setCustomer(cJake);
        claim.setEmployee(empFred);
        em.persist(claim);
        claim = Claim.findByNum(2l, em);
        assertNotNull(claim);
        assertEquals(cJake, claim.getCustomer());
    }

    @Test
    public void byCustomerTest() {
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        claim.setCustomer(cBob);
        claim.setEmployee(empFred);
        em.persist(claim);
        claim = new Claim();
        claim.setNum(2l);
        claim.setCount(2);
        claim.setProduct("Milk");
        claim.setCustomer(cJake);
        claim.setEmployee(empFred);
        em.persist(claim);
        List<Claim> list = Claim.findByCustomer(cBob, em);
        assertEquals(1, list.size());
        assertEquals(10, list.get(0).getCount());
    }

    @Test
    public void byEmployeeTest() {
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        claim.setCustomer(cBob);
        claim.setEmployee(empFred);
        em.persist(claim);
        claim = new Claim();
        claim.setNum(2l);
        claim.setCount(2);
        claim.setProduct("Milk");
        claim.setCustomer(cJake);
        claim.setEmployee(empStiv);
        em.persist(claim);
        List<Claim> list = Claim.findByEmployee(empFred, em);
        assertEquals(1, list.size());
        assertEquals(10, list.get(0).getCount());
    }

    @Test(expected = PersistenceException.class)
    public void duplicateNumTest() {
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        claim.setCustomer(cBob);
        claim.setEmployee(empFred);
        em.persist(claim);
        claim = new Claim();
        claim.setNum(1l);
        claim.setCount(2);
        claim.setProduct("Milk");
        claim.setCustomer(cJake);
        claim.setEmployee(empStiv);
        em.persist(claim);
        em.getTransaction().commit();
    }

    @Test(expected = PersistenceException.class)
    public void nullableTest(){
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        em.persist(claim);
        em.getTransaction().commit();
    }

    @Test
    public void updateCountTest(){
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        em.persist(claim);
        claim = em.find(Claim.class, claim.getId());
        assertNotNull(claim);
        claim.setCount(20);
        em.merge(claim);
        claim = em.find(Claim.class, claim.getId());
        assertNotNull(claim);
        assertEquals(20, claim.getCount());
    }

    @Test(expected =  PersistenceException.class)
    public void updateNullProductTest(){
        Claim claim = new Claim();
        claim.setNum(1l);
        claim.setCount(10);
        claim.setProduct("Milk");
        em.persist(claim);
        claim = em.find(Claim.class, claim.getId());
        assertNotNull(claim);
        claim.setProduct(null);
        em.merge(claim);
        em.getTransaction().commit();
    }
}
