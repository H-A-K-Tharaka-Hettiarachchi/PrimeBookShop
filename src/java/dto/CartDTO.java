package dto;

import entity.Product;
import java.io.Serializable;

/**
 *
 * @author ASUS
 */
public class CartDTO implements Serializable {

    private Product product;

    private int qty;

    public CartDTO() {
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }

    /**
     * @return the qty
     */
    public int getQty() {
        return qty;
    }

    /**
     * @param qty the qty to set
     */
    public void setQty(int qty) {
        this.qty = qty;
    }
    
    

}
