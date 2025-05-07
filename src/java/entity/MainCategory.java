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
@Table(name = "main_category")
public class MainCategory implements Serializable{

    @Id
    @Column(name = "main_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "main_category", length = 45, nullable = false)
    private String mainCategory;

    public MainCategory() {
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
     * @return the mainCategory
     */
    public String getMainCategory() {
        return mainCategory;
    }

    /**
     * @param mainCategory the mainCategory to set
     */
    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    
    
}
