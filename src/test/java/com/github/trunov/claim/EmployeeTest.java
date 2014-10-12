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
import static org.junit.Assert.assertEquals;

public class EmployeeTest {

    private static EntityManagerFactory emf;

    private EntityManager em;

    private Department dev;

    private Department qa;

    @BeforeClass
    public static void staticInit() {
        emf = createEntityManagerFactory("claim");
    }

    @Before
    public void init() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        dev = new Department();
        dev.setName("DEV");
        em.persist(dev);
        qa = new Department();
        qa.setName("QA");
        em.persist(qa);
    }

    @After
    public void clean() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        em.getTransaction().begin();
        Employee.clear(em);
        em.getTransaction().commit();
    }

    @Test
    public void persistTest(){
        Employee e = new Employee();
        e.setName("Martin");
        e.setBirthDate(new Date());
        e.setPost("Developer");
        e.setSex(true);
        e.setDepartment(qa);
        em.persist(e);
        assertNotNull(e.getId());
    }

    @Test
    public void findAllTest(){
        Employee e1 = new Employee();
        e1.setName("Martin");
        e1.setBirthDate(new Date());
        e1.setDepartment(qa);
        e1.setPost("Tester");
        e1.setSex(true);
        em.persist(e1);
        Employee e2 = new Employee();
        e2.setName("Robert");
        e2.setBirthDate(new Date());
        e2.setDepartment(dev);
        e2.setPost("Developer");
        e2.setSex(true);
        em.persist(e2);
        Employee e3 = new Employee();
        e3.setName("John");
        e3.setBirthDate(new Date(123454000l));
        e3.setDepartment(qa);
        e3.setPost("Tester");
        e3.setSex(true);
        em.persist(e3);
        Employee e4 = new Employee();
        e4.setName("John");
        e4.setBirthDate(new Date());
        e4.setDepartment(dev);
        e4.setPost("Developer");
        e4.setSex(true);
        em.persist(e4);

        List<Employee> list = Employee.findAll(em);
        assertEquals(4, list.size());
        assertTrue(list.contains(e1));
        assertTrue(list.contains(e2));
        assertTrue(list.contains(e3));
        assertTrue(list.contains(e4));
    }

    @Test
    public void findByNameTest(){
        Employee e1 = new Employee();
        e1.setName("Martin");
        e1.setBirthDate(new Date());
        e1.setDepartment(qa);
        e1.setPost("Tester");
        e1.setSex(true);
        em.persist(e1);
        Employee e2 = new Employee();
        e2.setName("Robert");
        e2.setBirthDate(new Date());
        e2.setDepartment(dev);
        e2.setPost("Developer");
        e2.setSex(true);
        em.persist(e2);
        Employee e3 = new Employee();
        e3.setName("John");
        e3.setBirthDate(new Date(123454000l));
        e3.setDepartment(qa);
        e3.setPost("Tester");
        e3.setSex(true);
        em.persist(e3);
        Employee e4 = new Employee();
        e4.setName("John");
        e4.setBirthDate(new Date());
        e4.setDepartment(qa);
        e4.setPost("Tester");
        e4.setSex(true);
        em.persist(e4);

        List<Employee> list = Employee.findByName("John", em);
        assertEquals(2, list.size());
        assertTrue(list.contains(e3));
        assertTrue(list.contains(e4));
    }

    @Test
    public void findEmployeeTest(){
        Employee e1 = new Employee();
        e1.setName("Martin");
        e1.setBirthDate(new Date());
        e1.setDepartment(qa);
        e1.setPost("Tester");
        e1.setSex(true);
        em.persist(e1);
        Employee e2 = new Employee();
        e2.setName("Robert");
        e2.setBirthDate(new Date());
        e2.setDepartment(dev);
        e2.setPost("Developer");
        e2.setSex(true);
        em.persist(e2);
        Employee e3 = new Employee();
        e3.setName("John");
        e3.setBirthDate(new Date(123454000l));
        e3.setDepartment(qa);
        e3.setPost("Tester");
        e3.setSex(false);
        em.persist(e3);
        Employee e4 = new Employee();
        e4.setName("John");
        e4.setBirthDate(new Date());
        e4.setDepartment(qa);
        e4.setPost("Tester");
        e4.setSex(true);
        em.persist(e4);

        Employee emp = Employee.findEmployee("John", new Date(123454000l), em);
        assertNotNull(emp);
        assertEquals("John", emp.getName());
        assertEquals(false, emp.isSex());
    }

    @Test(expected = NoResultException.class)
    public void noResultTest() {
        assertTrue(Employee.findByName("John", em).isEmpty());
        Employee.findEmployee("John", new Date(), em);
    }

    @Test(expected = PersistenceException.class)
    public void constraintUniqueTest() {
        Employee a = new Employee();
        Employee b = new Employee();
        a.setName("John");
        a.setDepartment(qa);
        a.setPost("Tester");
        a.setBirthDate(new Date(12364000l));
        b.setName("John");
        b.setDepartment(dev);
        b.setPost("Developer");
        b.setBirthDate(new Date(12364000l));
        em.persist(a);
        em.persist(b);
        em.getTransaction().commit();
    }

    @Test(expected = PersistenceException.class)
    public void constraintNullableTest() {
        Employee a = new Employee();
        a.setName("John");
        a.setBirthDate(new Date());
        em.persist(a);
        em.getTransaction().commit();
    }

}

