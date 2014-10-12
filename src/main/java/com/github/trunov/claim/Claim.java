package com.github.trunov.claim;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность заявки клиента на некий товар.
 * Created by qw3rtrun on 12.10.14.
 */
@Entity
@NamedQueries({
        @NamedQuery(name="Claim.findAll",
                query="SELECT c FROM Claim c"),
        @NamedQuery(name="Claim.findByNum",
                query="SELECT c FROM Claim c WHERE c.num = :num"),
        @NamedQuery(name="Claim.findByProduct",
                query="SELECT c FROM Claim c WHERE c.product = :product"),
        @NamedQuery(name="Claim.findByCustomer",
                query="SELECT c FROM Claim c WHERE c.customer.id = :id"),
        @NamedQuery(name="Claim.findByEmployee",
                query="SELECT c FROM Claim c WHERE c.employee.id = :id"),
        @NamedQuery(name="Claim.maxNum",
                query="SELECT MAX(c.num) FROM Claim c"),
        @NamedQuery(name="Claim.clear",
                query="DELETE FROM Claim")})
public class Claim {

    public static List<Claim> findAll(EntityManager em) {
        return em.createNamedQuery("Claim.findAll", Claim.class).getResultList();
    }

    public static void clear(EntityManager em) {
        em.createNamedQuery("Claim.clear").executeUpdate();
    }

    public static Claim findByNum(long num, EntityManager em) {
        return em.createNamedQuery("Claim.findByNum", Claim.class).setParameter("num", num).getSingleResult();
    }

    public static List<Claim> findByProduct(String product, EntityManager em) {
        return em.createNamedQuery("Claim.findByProduct", Claim.class).setParameter("product", product).getResultList();
    }

    public static List<Claim> findByCustomer(Customer customer, EntityManager em) {
        return em.createNamedQuery("Claim.findByCustomer", Claim.class).setParameter("id", customer.getId()).getResultList();
    }

    public static List<Claim> findByEmployee(Employee employee, EntityManager em) {
        return em.createNamedQuery("Claim.findByEmployee", Claim.class).setParameter("id", employee.getId()).getResultList();
    }

    public static Long maxNum(EntityManager em){
        return em.createNamedQuery("Claim.maxNum", Long.class).getSingleResult();
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private Long num;

    @Column(nullable = false)
    private String product;

    private int prodCount;

    @JoinColumn(nullable = false)
    private Customer customer;

    @JoinColumn(nullable = false)
    private Employee employee;

    public Claim() {
    }

    public Long getId() {
        return id;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getCount() {
        return prodCount;
    }

    public void setCount(int count) {
        this.prodCount = count;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
