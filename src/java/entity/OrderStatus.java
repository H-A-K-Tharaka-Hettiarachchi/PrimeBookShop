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
import javax.persistence.Table;

/**
 *
 * @author ASUS
 */
@Entity
@Table(name = "order_status")
public class OrderStatus implements Serializable {

    @Id
    @Column(name = "order_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_status", length = 10, nullable = false)
    private String order_status;

    public OrderStatus() {
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
     * @return the order_status
     */
    public String getOrder_status() {
        return order_status;
    }

    /**
     * @param order_status the order_status to set
     */
    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

}
