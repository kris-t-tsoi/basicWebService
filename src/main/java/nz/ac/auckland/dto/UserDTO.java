package nz.ac.auckland.dto;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.xml.bind.annotation.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import nz.ac.auckland.purchaseItems.Purchase;
import nz.ac.auckland.userDetail.Address;

@XmlRootElement(name="user")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserDTO {

//		@XmlID
		@XmlAttribute(name="id")
		private long id;	
		
		@XmlElement(name="user-name")
		private String userName;
		
		@XmlElement(name="first-name")
		private String firstName;
		
		@XmlElement(name="last-name")
		private String lastName;

		@XmlElement(name="billing-address")
		private AddressDTO billingAddress;
		
		@XmlElement(name="shipping-address")
		private AddressDTO shippingAddress;

		public UserDTO(){}

		public UserDTO(String username, String lastName, String firstName, AddressDTO billing, AddressDTO shipping){
			this(0,username,lastName,firstName,billing,shipping);
		}
		
		public UserDTO(long l, String username, String lastName, String firstName, AddressDTO billing, AddressDTO shipping){
			this.id = l;
			this.userName = username;
			this.lastName = lastName;
			this.firstName = firstName;
			this.billingAddress = billing;
			this.shippingAddress = shipping;
		}

		public AddressDTO getBillingAddress() {
			return billingAddress;
		}

		public void changeBillingAddress(AddressDTO billingAddress) {
			this.billingAddress = billingAddress;
		}

		public AddressDTO getShippingAddress() {
			return shippingAddress;
		}

		public void changeShippingAddress(AddressDTO shippingAddress) {
			this.shippingAddress = shippingAddress;
		}

		public long getId() {
			return id;
		}

		public String getUserName() {
			return userName;
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}
		
		

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
//			return super.equals(obj);
			if (!(obj instanceof UserDTO))
	            return false;
	        if (obj == this)
	            return true;

	        UserDTO usr = (UserDTO) obj;
	        return new EqualsBuilder().
	            append(id, usr.getId()).
	            isEquals();
		}
		
		
		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
//			return super.hashCode();
			return new HashCodeBuilder(17, 31). 
					append(getClass().getName()).
		            append(id).
		            toHashCode();
		}

		public void setId(long id) {
			this.id = id;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		
		
}
