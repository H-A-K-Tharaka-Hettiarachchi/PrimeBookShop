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
@Table(name = "district")
public class District implements Serializable{

    @Id
    @Column(name = "district_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "district", length = 45, nullable = false)
    private String district;

    @ManyToOne
    @JoinColumn(name = "province_id")
    private Province province;

    public District() {
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
     * @return the district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district the district to set
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return the province
     */
    public Province getProvince() {
        return province;
    }

    /**
     * @param province the province to set
     */
    public void setProvince(Province province) {
        this.province = province;
    }
    
    
}
