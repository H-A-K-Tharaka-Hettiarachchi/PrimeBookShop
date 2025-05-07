/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import com.google.gson.annotations.Expose;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
public class ResponseDTO implements Serializable {

    @Expose
    private boolean success;
    @Expose
    private Object content;

    public ResponseDTO() {
    }

    public ResponseDTO(boolean success, Object content) {
        this.success = success;
        this.content = content;
    }

    /**
     * @return the success
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * @return the content
     */
    public Object getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(Object content) {
        this.content = content;
    }

}
