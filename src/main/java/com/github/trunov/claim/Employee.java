package com.github.trunov.claim;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Сотрудник, оформляющий заявки.
 *
 * Created by qw3rtrun on 12.10.14.
 */
@Entity
@DiscriminatorValue("E")
@NamedQueries({
        @NamedQuery(name="Employee.findAll",
                query="SELECT e FROM Employee e"),
        @NamedQuery(name="Employee.findByName",
                query="SELECT e FROM Employee e WHERE e.name = :name"),
        @NamedQuery(name="Employee.findEmployee",
                query="SELECT e FROM Employee e WHERE e.name = :name AND e.birthDate = :birthDate"),
        @NamedQuery(name="Employee.clear",
                query="DELETE FROM Employee")})
public class Employee extends Person {

    public static List<Employee> findAll(EntityManager em) {
        return em.createNamedQuery("Employee.findAll", Employee.class).getResultList();
    }

    public static void clear(EntityManager em){
        em.createNamedQuery("Employee.clear").executeUpdate();
    }

    public static List<Employee> findByName(String name, EntityManager em){
        return em.createNamedQuery("Employee.findByName", Employee.class).setParameter("name", name).getResultList();
    }

    public static Employee findEmployee(String name, Date birthDate, EntityManager em){
        return em.createNamedQuery("Employee.findEmployee", Employee.class).setParameter("name", name).setParameter("birthDate", birthDate).getSingleResult();
    }

    @JoinColumn(nullable = false)
    private Department department;

    @Column(nullable = false)
    private String post;

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
