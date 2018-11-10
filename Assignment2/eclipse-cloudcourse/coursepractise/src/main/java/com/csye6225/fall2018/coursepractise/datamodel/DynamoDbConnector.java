package com.csye6225.fall2018.coursepractise.datamodel;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class DynamoDbConnector {
	static AmazonDynamoDB dynamoDb;
	public static void init() {
		if(dynamoDb == null) {
//			InstanceProfileCredentialsProvider credentialsProvider = new InstanceProfileCredentialsProvider(false);
//			ProfileCredentialsProvider credentialsProvider = new ProfileCredentialsProvider();
//			credentialsProvider.getCredentials();
			
			AWSCredentialsProvider credentialsProvider;
			try
			{
				credentialsProvider = new InstanceProfileCredentialsProvider(false);
				credentialsProvider.getCredentials();
			}
			catch(Exception e)
			{
				credentialsProvider = new ProfileCredentialsProvider();
				credentialsProvider.getCredentials();
			}
			
			dynamoDb = AmazonDynamoDBClientBuilder
					.standard()
					.withCredentials(credentialsProvider)
					.withRegion("us-east-2")
					.build();
			System.out.println("I created the client");
		}
	}
	public AmazonDynamoDB getClient() {
		return dynamoDb;
	}

}
