package dto;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
public class UserDTO implements Serializable {

    @Expose
    private String fname;
    @Expose
    private String lname;
    @Expose
    private String email;
    @Expose(deserialize = true, serialize = false)
    private String password;
    @Expose(deserialize = true, serialize = false)
    private String confiremPassword;
    @Expose
    private boolean rememberme;
    @Expose
    private boolean blockStatus;
    @Expose
    private boolean status;

    public UserDTO() {
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the rememberme
     */
    public boolean isRememberme() {
        return rememberme;
    }

    /**
     * @param rememberme the rememberme to set
     */
    public void setRememberme(boolean rememberme) {
        this.rememberme = rememberme;
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

    /**
     * @return the status
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * @return the confiremPassword
     */
    public String getConfiremPassword() {
        return confiremPassword;
    }

    /**
     * @param confiremPassword the confiremPassword to set
     */
    public void setConfiremPassword(String confiremPassword) {
        this.confiremPassword = confiremPassword;
    }

}
