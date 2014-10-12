package com.github.trunov.claim;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Сущность клиента.
 *
 * Created by qw3rtrun on 12.10.14.
 */

@Entity
@DiscriminatorValue("C")
@NamedQueries({
        @NamedQuery(name="Customer.findAll",
                query="SELECT c FROM Customer c"),
        @NamedQuery(name="Customer.findByName",
                query="SELECT c FROM Customer c WHERE c.name = :name"),
        @NamedQuery(name="Customer.findCustomer",
                query="SELECT c FROM Customer c WHERE c.name = :name AND c.birthDate = :birthDate"),
        @NamedQuery(name="Customer.clear",
                query="DELETE FROM Customer")})
public class Customer extends Person {

    public static List<Customer> findAll(EntityManager em) {
        return em.createNamedQuery("Customer.findAll", Customer.class).getResultList();
    }

    public static void clear(EntityManager em){
        em.createNamedQuery("Customer.clear").executeUpdate();
    }

    public static List<Customer> findByName(String name, EntityManager em){
        return em.createNamedQuery("Customer.findByName", Customer.class).setParameter("name", name).getResultList();
    }

    public static Customer findCustomer(String name, Date birthDate, EntityManager em){
        return em.createNamedQuery("Customer.findCustomer", Customer.class).setParameter("name", name).setParameter("birthDate", birthDate).getSingleResult();
    }

    @Column(nullable = false)
    private Date registrationDate;

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
