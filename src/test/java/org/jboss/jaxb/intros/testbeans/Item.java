/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
