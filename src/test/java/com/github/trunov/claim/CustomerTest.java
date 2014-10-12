package com.github.trunov.claim;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.Date;
import java.util.List;

import static javax.persistence.Persistence.createEntityManagerFactory;
import static org.junit.Assert.*;

public class CustomerTest {

    private static EntityManagerFactory emf;

    private EntityManager em;

    @BeforeClass
    public static void staticInit() {
        emf = createEntityManagerFactory("claim");
    }

    @Before
    public void init() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void clean() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        em.getTransaction().begin();
        Customer.clear(em);
        em.getTransaction().commit();
    }

    @Test
    public void persistTest(){
        Customer c = new Customer();
        c.setName("Martin");
        c.setBirthDate(new Date());
        c.setRegistrationDate(new Date());
        c.setSex(true);
        em.persist(c);
        assertNotNull(c.getId());
    }

    @Test
    public void findAllTest(){
        Customer c1 = new Customer();
        c1.setName("Martin");
        c1.setBirthDate(new Date());
        c1.setRegistrationDate(new Date());
        c1.setSex(true);
        em.persist(c1);
        Customer c2 = new Customer();
        c2.setName("Robert");
        c2.setBirthDate(new Date());
        c2.setRegistrationDate(new Date());
        c2.setSex(true);
        em.persist(c2);
        Customer c3 = new Customer();
        c3.setName("John");
        c3.setBirthDate(new Date(123454000l));
        c3.setRegistrationDate(new Date());
        c3.setSex(true);
        em.persist(c3);
        Customer c4 = new Customer();
        c4.setName("John");
        c4.setBirthDate(new Date());
        c4.setRegistrationDate(new Date());
        c4.setSex(true);
        em.persist(c4);

        List<Customer> list = Customer.findAll(em);
        assertEquals(4, list.size());
        assertTrue(list.contains(c1));
        assertTrue(list.contains(c2));
        assertTrue(list.contains(c3));
        assertTrue(list.contains(c4));
    }

    @Test
    public void findByNameTest(){
        Customer c1 = new Customer();
        c1.setName("Martin");
        c1.setBirthDate(new Date());
        c1.setRegistrationDate(new Date());
        c1.setSex(true);
        em.persist(c1);
        Customer c2 = new Customer();
        c2.setName("Robert");
        c2.setBirthDate(new Date());
        c2.setRegistrationDate(new Date());
        c2.setSex(true);
        em.persist(c2);
        Customer c3 = new Customer();
        c3.setName("John");
        c3.setBirthDate(new Date(123454000l));
        c3.setRegistrationDate(new Date());
        c3.setSex(true);
        em.persist(c3);
        Customer c4 = new Customer();
        c4.setName("John");
        c4.setBirthDate(new Date());
        c4.setRegistrationDate(new Date());
        c4.setSex(true);
        em.persist(c4);

        List<Customer> list = Customer.findByName("John", em);
        assertEquals(2, list.size());
        assertTrue(list.contains(c3));
        assertTrue(list.contains(c4));
    }

    @Test
    public void findCustomerTest(){
        Customer c1 = new Customer();
        c1.setName("Martin");
        c1.setBirthDate(new Date());
        c1.setRegistrationDate(new Date());
        c1.setSex(true);
        em.persist(c1);
        Customer c2 = new Customer();
        c2.setName("Robert");
        c2.setBirthDate(new Date());
        c2.setRegistrationDate(new Date());
        c2.setSex(true);
        em.persist(c2);
        Customer c3 = new Customer();
        c3.setName("John");
        c3.setBirthDate(new Date(123454000l));
        c3.setRegistrationDate(new Date());
        c3.setSex(false);
        em.persist(c3);
        Customer c4 = new Customer();
        c4.setName("John");
        c4.setBirthDate(new Date());
        c4.setRegistrationDate(new Date());
        c4.setSex(true);
        em.persist(c4);

        Customer customer = Customer.findCustomer("John", new Date(123454000l), em);
        assertNotNull(customer);
        assertEquals("John", customer.getName());
        assertEquals(false, customer.isSex());
    }

    @Test(expected = NoResultException.class)
    public void noResultTest() {
        assertTrue(Customer.findByName("John", em).isEmpty());
        Customer.findCustomer("John", new Date(), em);
    }

    @Test(expected = PersistenceException.class)
    public void constraintUniqueTest() {
        Customer a = new Customer();
        Customer b = new Customer();
        a.setName("John");
        a.setRegistrationDate(new Date());
        a.setBirthDate(new Date(12364000l));
        b.setName("John");
        b.setRegistrationDate(new Date());
        b.setBirthDate(new Date(12364000l));
        em.persist(a);
        em.persist(b);
        em.getTransaction().commit();
    }

    @Test(expected = PersistenceException.class)
    public void constraintNullableTest() {
        Customer a = new Customer();
        a.setName("John");
        a.setBirthDate(new Date(12364000l));
        em.persist(a);
        em.getTransaction().commit();
    }

}

