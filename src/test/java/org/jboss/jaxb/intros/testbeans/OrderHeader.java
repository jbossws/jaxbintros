package org.jboss.jaxb.intros.testbeans;

import java.io.Serializable;

public class OrderHeader implements Serializable {

    protected String customerNumber;

    protected String poNumber;

    protected java.util.Calendar orderDate;

    protected java.math.BigDecimal orderTotal;

    protected Party billTo;

    protected Party shipTo;

    protected String billTerms;

    protected String shipTerms;

    public OrderHeader() {
    }

    public OrderHeader(String customerNumber, String poNumber, java.util.Calendar orderDate, java.math.BigDecimal orderTotal, Party billTo, Party shipTo, String billTerms, String shipTerms) {
        this.customerNumber = customerNumber;
        this.poNumber = poNumber;
        this.orderDate = orderDate;
        this.orderTotal = orderTotal;
        this.billTo = billTo;
        this.shipTo = shipTo;
        this.billTerms = billTerms;
        this.shipTerms = shipTerms;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public java.util.Calendar getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(java.util.Calendar orderDate) {
        this.orderDate = orderDate;
    }

    public java.math.BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(java.math.BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Party getBillTo() {
        return billTo;
    }

    public void setBillTo(Party billTo) {
        this.billTo = billTo;
    }

    public Party getShipTo() {
        return shipTo;
    }

    public void setShipTo(Party shipTo) {
        this.shipTo = shipTo;
    }

    public String getBillTerms() {
        return billTerms;
    }

    public void setBillTerms(String billTerms) {
        this.billTerms = billTerms;
    }

    public String getShipTerms() {
        return shipTerms;
    }

    public void setShipTerms(String shipTerms) {
        this.shipTerms = shipTerms;
    }

}
