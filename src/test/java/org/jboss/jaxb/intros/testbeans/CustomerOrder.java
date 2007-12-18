package org.jboss.jaxb.intros.testbeans;

import java.io.Serializable;

public class CustomerOrder implements Serializable {

    protected OrderHeader header;

    protected Item[] items;

    public OrderHeader getHeader() {
        return header;
    }

    public void setHeader(OrderHeader header) {
        this.header = header;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

}
