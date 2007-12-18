package org.jboss.jaxb.intros.testbeans;

import java.io.Serializable;

public class Party implements Serializable {

protected String company;

protected Contact contact;

protected Address address;
public Party(){}

public Party(String company, Contact contact, Address address){
this.company=company;
this.contact=contact;
this.address=address;
}
public String getCompany() { return company ;}

public void setCompany(String company){ this.company=company; }

public Contact getContact() { return contact ;}

public void setContact(Contact contact){ this.contact=contact; }

public Address getAddress() { return address ;}

public void setAddress(Address address){ this.address=address; }

}
