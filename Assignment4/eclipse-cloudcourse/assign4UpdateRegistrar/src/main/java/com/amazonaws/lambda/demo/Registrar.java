package com.amazonaws.lambda.demo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="Registrar")
public class Registrar {
	private String RegistrationId;
	private String OfferingId;
	private String OfferingType;
	private String Department;
	private float PerUnitPrice;
	
	public Registrar() {
		
	}
	
	public Registrar(String RegistrationId,String OfferingId,String OfferingType, String Department,float PerUnitPrice) {
		this.setRegistrationId(RegistrationId);
		this.setOfferingId(OfferingId);
		this.setOfferingType(OfferingType);
		this.setDepartment(Department);
		this.setPerUnitPrice(PerUnitPrice);
	}

	@DynamoDBHashKey(attributeName = "RegistrationId")
	public String getRegistrationId() {
		return RegistrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.RegistrationId = registrationId;
	}

	@DynamoDBAttribute(attributeName = "OfferingId")
	public String getOfferingId() {
		return OfferingId;
	}

	public void setOfferingId(String offeringId) {
		this.OfferingId = offeringId;
	}

	@DynamoDBAttribute(attributeName = "OfferingType")
	public String getOfferingType() {
		return OfferingType;
	}

	public void setOfferingType(String offeringType) {
		this.OfferingType = offeringType;
	}

	@DynamoDBAttribute(attributeName = "Department")
	public String getDepartment() {
		return Department;
	}

	public void setDepartment(String department) {
		this.Department = department;
	}

	@DynamoDBAttribute(attributeName = "PerUnitPrice")
	public float getPerUnitPrice() {
		return PerUnitPrice;
	}

	public void setPerUnitPrice(float perUnitPrice) {
		this.PerUnitPrice = perUnitPrice;
	}

}