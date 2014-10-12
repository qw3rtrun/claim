package com.github.trunov.claim;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Абстрактный класс человека. В оформлении заявки человек тожет участвовать в статусе {@link com.github.trunov.claim.Employee} или {@link com.github.trunov.claim.Customer}.
 *
 * Created by qw3rtrun on 12.10.14.
 */

@Entity
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.JOINED)
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "birthDate"})})
public abstract class Person {
    @GeneratedValue()
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    private boolean sex;

    @Column(nullable = false)
    private Date birthDate;

    public Person() {
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

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
