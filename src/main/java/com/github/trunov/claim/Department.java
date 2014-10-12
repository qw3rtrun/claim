package com.github.trunov.claim;

import javax.persistence.*;
import java.util.List;

/**
 * Подразделение (отдел), где работают сотрудники.
 *
 * Created by qw3rtrun on 12.10.14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "Department.findAll",
                query = "SELECT d FROM Department d"),
        @NamedQuery(name = "Department.findByName",
                query = "SELECT d FROM Department d WHERE d.name = :name"),
        @NamedQuery(name = "Department.clear",
                query = "DELETE FROM Department")})
public class Department {

    public static List<Department> findAll(EntityManager em) {
        return em.createNamedQuery("Department.findAll", Department.class).getResultList();
    }

    public static void clear(EntityManager em) {
        em.createNamedQuery("Department.clear").executeUpdate();
    }

    public static Department findByName(String name, EntityManager em) {
        return em.createNamedQuery("Department.findByName", Department.class).setParameter("name", name).getSingleResult();
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    public Department() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
