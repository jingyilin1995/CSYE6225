package com.csye6225.fall2018.assign4Registrar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.csye6225.fall2018.assign4Registrar.datamodel.DynamoDbConnector;
import com.csye6225.fall2018.assign4Registrar.datamodel.Registrar;

public class RegistrarsService {
	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;
	
	public RegistrarsService() {
		dynamoDb = new DynamoDbConnector();
		dynamoDb.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
	}
	
	//get a list of all registrar
	public List<Registrar> getAllRegistrars(){
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<Registrar> scanResult = mapper.scan(Registrar.class,scanExpression);
		return scanResult;
	}
	
	//Add a registrar
	public Registrar addRegistrar(Registrar regi) {
		System.out.println(regi.getOfferingId());
		mapper.save(regi);
		return mapper.load(Registrar.class,regi.getRegistrationId());
	}
	
	//getting one registrar
	public Registrar getRegistrar(String RegistrationId) {
		return mapper.load(Registrar.class,RegistrationId);
	}
	
	//deleting one registrar
	public Registrar deleteRegistrar(String RegistrationId) {
		Registrar deleteDetails = mapper.load(Registrar.class,RegistrationId);
		mapper.delete(deleteDetails);
		return deleteDetails;
	}
	
}
