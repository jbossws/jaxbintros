package org.jboss.jaxb.intros.testbeans;

import java.io.Serializable;

public class Contact implements Serializable
{

   private static final long serialVersionUID = 1L;

   protected String name;

   protected String phone;

   protected String fax;

   protected String email;

   public Contact()
   {
   }

   public Contact(String name, String phone, String fax, String email)
   {
      this.name = name;
      this.phone = phone;
      this.fax = fax;
      this.email = email;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getPhone()
   {
      return phone;
   }

   public void setPhone(String phone)
   {
      this.phone = phone;
   }

   public String getFax()
   {
      return fax;
   }

   public void setFax(String fax)
   {
      this.fax = fax;
   }

   public String getEmail()
   {
      return email;
   }

   public void setEmail(String email)
   {
      this.email = email;
   }

}
