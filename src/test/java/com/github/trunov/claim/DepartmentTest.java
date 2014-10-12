package com.github.trunov.claim;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import java.util.List;

import static javax.persistence.Persistence.createEntityManagerFactory;
import static org.junit.Assert.*;

public class DepartmentTest {

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
        Department.clear(em);
        em.getTransaction().commit();
    }

    @Test
    public void persistTest() {
        Department d = new Department();
        d.setName("DEV");
        em.persist(d);
        assertNotNull(d.getId());
    }

    @Test
    public void findAllTest() {
        Department d1 = new Department();
        d1.setName("DEV");
        em.persist(d1);
        Department d2 = new Department();
        d2.setName("QA");
        em.persist(d2);
        Department d3 = new Department();
        d3.setName("OFFICE");
        em.persist(d3);
        List<Department> list = Department.findAll(em);
        assertEquals(3, list.size());
        assertTrue(list.contains(d1));
        assertTrue(list.contains(d2));
        assertTrue(list.contains(d3));
    }

    @Test
    public void findByNameTest() {
        Department d1 = new Department();
        d1.setName("DEV");
        em.persist(d1);
        Department d2 = new Department();
        d2.setName("QA");
        em.persist(d2);
        Department d3 = new Department();
        d3.setName("OFFICE");
        em.persist(d3);
        Department office = Department.findByName("OFFICE", em);
        assertNotNull(office);
        assertEquals("OFFICE", office.getName());
    }

    @Test(expected = NoResultException.class)
    public void noResultTest() {
        Department.findByName("OFFICE", em);
    }

    @Test(expected = PersistenceException.class)
    public void constraintUniqueTest() {
        Department a = new Department();
        Department b = new Department();
        Department c = new Department();
        a.setName("DEV");
        b.setName("QA");
        c.setName("DEV");

        em.persist(a);
        em.persist(b);
        em.persist(c);

        em.getTransaction().commit();
    }

    @Test(expected = PersistenceException.class)
    public void constraintNullableTest() {
        Department a = new Department();
        Department b = new Department();
        Department c = new Department();
        a.setName("DEV");
        b.setName("QA");

        em.persist(a);
        em.persist(b);
        em.persist(c);

        em.getTransaction().commit();
    }
}

