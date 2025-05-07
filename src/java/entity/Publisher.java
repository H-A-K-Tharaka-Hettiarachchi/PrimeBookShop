
package entity;


import java.io.Serializable;
import java.time.Year;
import java.util.Date;
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
@Table(name = "publisher")
public class Publisher implements Serializable{

    
    
    @Id
    @Column(name = "publisher_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "publisher_name", length = 45, nullable = false)
    private String publisherName;

    @Column(name = "publisher_contact", length = 13, nullable = false)
    private String publisherContact;

    @Column(name = "publisher_email", length = 50, nullable = false)
    private String publisherEmail;

    @Column(name = "established_year",length = 4, nullable = false)
    private String establishedYear;
    
    @Column(name = "block_status", nullable = false)
    private boolean blockStatus;

    public Publisher() {
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
     * @return the publisherName
     */
    public String getPublisherName() {
        return publisherName;
    }

    /**
     * @param publisherName the publisherName to set
     */
    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    /**
     * @return the publisherContact
     */
    public String getPublisherContact() {
        return publisherContact;
    }

    /**
     * @param publisherContact the publisherContact to set
     */
    public void setPublisherContact(String publisherContact) {
        this.publisherContact = publisherContact;
    }

    /**
     * @return the publisherEmail
     */
    public String getPublisherEmail() {
        return publisherEmail;
    }

    /**
     * @param publisherEmail the publisherEmail to set
     */
    public void setPublisherEmail(String publisherEmail) {
        this.publisherEmail = publisherEmail;
    }

    /**
     * @return the establishedYear
     */
    public String getEstablishedYear() {
        return establishedYear;
    }

    /**
     * @param establishedYear the establishedYear to set
     */
    public void setEstablishedYear(String establishedYear) {
        this.establishedYear = establishedYear;
    }

    /**
     * @return the blockStatus
     */
    public boolean isBlockStatus() {
        return blockStatus;
    }

    /**
     * @param blockStatus the blockStatus to set
     */
    public void setBlockStatus(boolean blockStatus) {
        this.blockStatus = blockStatus;
    }

  

}
