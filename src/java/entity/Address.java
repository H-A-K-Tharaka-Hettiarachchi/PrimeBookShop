/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "address")
public class Address implements Serializable {

    @Id
    @Column(name = "address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @Column(name = "address_line_1", length = 45, nullable = false)
    private String line1;

    @Column(name = "address_line_2", length = 45, nullable = false)
    private String line2;

    @Column(name = "postal_code", length = 10, nullable = false)
    private String postalCode;

    @Column(name = "mobile", length = 13, nullable = false)
    private String mobile;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private City city;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address() {
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the line1
     */
    public String getLine1() {
        return line1;
    }

    /**
     * @param line1 the line1 to set
     */
    public void setLine1(String line1) {
        this.line1 = line1;
    }

    /**
     * @return the line2
     */
    public String getLine2() {
        return line2;
    }

    /**
     * @param line2 the line2 to set
     */
    public void setLine2(String line2) {
        this.line2 = line2;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the city
     */
    public City getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

  

}
