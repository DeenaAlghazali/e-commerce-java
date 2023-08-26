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
public class Invoices {

    private Integer id;
    private Integer order_id;
    private Double total_price;
    private String date;

    public Invoices() {
    }

    public Invoices(Integer id, Integer order_id, Double total_price, String date) {
        this.id = id;
        this.order_id = order_id;
        this.total_price = total_price;
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
     * @return the order_id
     */
    public Integer getOrder_id() {
        return order_id;
    }

    /**
     * @param order_id the order_id to set
     */
    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    /**
     * @return the total_price
     */
    public Double getTotal_price() {
        return total_price;
    }

    /**
     * @param total_price the total_price to set
     */
    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
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
