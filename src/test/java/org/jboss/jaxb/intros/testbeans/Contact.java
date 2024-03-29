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
