package org.jboss.jaxb.intros.testbeans;

import java.io.Serializable;

public class Item implements Serializable
{

   private static final long serialVersionUID = 1L;

   protected String partNumber;

   protected String description;

   protected int quantity;

   protected java.math.BigDecimal price;

   protected java.math.BigDecimal extensionAmount;

   public Item()
   {
   }

   public Item(String partNumber, String description, int quantity, java.math.BigDecimal price, java.math.BigDecimal extensionAmount)
   {
      this.partNumber = partNumber;
      this.description = description;
      this.quantity = quantity;
      this.price = price;
      this.extensionAmount = extensionAmount;
   }

   public String getPartNumber()
   {
      return partNumber;
   }

   public void setPartNumber(String partNumber)
   {
      this.partNumber = partNumber;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public int getQuantity()
   {
      return quantity;
   }

   public void setQuantity(int quantity)
   {
      this.quantity = quantity;
   }

   public java.math.BigDecimal getPrice()
   {
      return price;
   }

   public void setPrice(java.math.BigDecimal price)
   {
      this.price = price;
   }

   public java.math.BigDecimal getExtensionAmount()
   {
      return extensionAmount;
   }

   public void setExtensionAmount(java.math.BigDecimal extensionAmount)
   {
      this.extensionAmount = extensionAmount;
   }

}
