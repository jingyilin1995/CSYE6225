package com.amazonaws.lambda.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemUtils;
import com.amazonaws.services.dynamodbv2.document.internal.InternalUtils;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;
import com.amazonaws.services.stepfunctions.AWSStepFunctions;
import com.amazonaws.services.stepfunctions.AWSStepFunctionsClientBuilder;
import com.amazonaws.services.stepfunctions.model.StartExecutionRequest;

public class InvokeStepFunction implements RequestHandler<DynamodbEvent, Map<String, AttributeValue>> {

	static AmazonDynamoDB dynamoDb;
	
    @Override
    public Map<String, AttributeValue> handleRequest(DynamodbEvent event, Context context) {
        context.getLogger().log("Received event: " + event);

        Map<String, AttributeValue> newmap = new HashMap<String, AttributeValue>();
        for (DynamodbStreamRecord record : event.getRecords()) {
            context.getLogger().log(record.getEventID());
            context.getLogger().log(record.getEventName());
            context.getLogger().log(record.getDynamodb().toString());
            StreamRecord str = record.getDynamodb();
            Map<String, AttributeValue> item = str.getNewImage();
//           String input = item.toString();
            List<Map<String, AttributeValue>> listOfMaps = new ArrayList<Map<String, AttributeValue>>();
            listOfMaps.add(item);
            List<Item> itemList =ItemUtils.toItemList(listOfMaps);
            String input = itemList.get(0).toJSON();
            
            AWSStepFunctions awsStepfunctionClient = AWSStepFunctionsClientBuilder.defaultClient();
            awsStepfunctionClient.startExecution(new StartExecutionRequest()
            										.withStateMachineArn("arn:aws:states:us-east-2:619958465375:stateMachine:Assign4")
            										.withInput(input));
            
            return item;
        }
        return newmap;
    }
}