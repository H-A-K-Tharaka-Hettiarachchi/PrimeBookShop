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
@Table(name = "sub_category")
public class SubCategory implements Serializable{

    @Id
    @Column(name = "sub_category_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sub_category", length = 45, nullable = false)
    private String subCategory;

    @ManyToOne
    @JoinColumn(name = "main_category_id")
    private MainCategory mainCategory;

    public SubCategory() {
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
     * @return the subCategory
     */
    public String getSubCategory() {
        return subCategory;
    }

    /**
     * @param subCategory the subCategory to set
     */
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    /**
     * @return the mainCategory
     */
    public MainCategory getMainCategory() {
        return mainCategory;
    }

    /**
     * @param mainCategory the mainCategory to set
     */
    public void setMainCategory(MainCategory mainCategory) {
        this.mainCategory = mainCategory;
    }

}
