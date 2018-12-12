package com.amazonaws.lambda.demo;

import java.util.Map;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LambdaFunctionHandler implements RequestHandler<Object, Object> {

	private static AmazonDynamoDB dynamoDb;
	private static DynamoDBMapper mapper;
	
    @Override
    public Object handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        
        this.initDynamoDbClient();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(input, Map.class);
        String courseId = map.get("courseId").toString();
        String department = map.get("department").toString();
        String registrationId = "regis"+courseId;
        String offeringId = courseId;
        String offeringType = "Course";
        float perUnitPrice = 100;
        
        Registrar newregistrar = new Registrar();
        newregistrar.setDepartment(department);
        newregistrar.setOfferingId(offeringId);
        newregistrar.setOfferingType(offeringType);
        newregistrar.setPerUnitPrice(perUnitPrice);
        newregistrar.setRegistrationId(registrationId);
        
        mapper.save(newregistrar);
        
        return input;
    }
    
    private void initDynamoDbClient() {
    	if(dynamoDb == null) {				
			dynamoDb = AmazonDynamoDBClientBuilder
					.standard()
					.withCredentials(new EnvironmentVariableCredentialsProvider())
					.withRegion("us-east-2")
					.build();
		}
    }

}
