/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

/**
 *
 * @author VICTUS
 */
public class Order {

    private Integer id;
    private Integer product_id;
    private Integer user_id;
    private Integer quantity;
    private String date;

    public Order() {
    }

    public Order(Integer id, Integer product_id, Integer user_id, Integer quantity, String date) {
        this.id = id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.quantity = quantity;
        this.date = date;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the product_id
     */
    public Integer getProduct_id() {
        return product_id;
    }

    /**
     * @param product_id the product_id to set
     */
    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    /**
     * @return the user_id
     */
    public Integer getUser_id() {
        return user_id;
    }

    /**
     * @param user_id the user_id to set
     */
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

}
