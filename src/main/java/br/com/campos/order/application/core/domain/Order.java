package br.com.campos.order.application.core.domain;

import java.util.List;

public class Order {

    private Long id;
    private String customerName;
    private List<OrderItem> items;

    public Order() {
    }

    public Order(Long id, String customerName, List<OrderItem> items) {
        this.id = id;
        this.customerName = customerName;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", customerName=" + customerName + ", items=" + items + "]";
    }

}
