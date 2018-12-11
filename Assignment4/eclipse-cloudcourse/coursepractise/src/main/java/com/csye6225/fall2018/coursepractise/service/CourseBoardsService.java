package com.csye6225.fall2018.coursepractise.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.csye6225.fall2018.coursepractise.datamodel.CourseBoard;
import com.csye6225.fall2018.coursepractise.datamodel.DynamoDbConnector;
import com.csye6225.fall2018.coursepractise.datamodel.InMemoryDatabase;
import com.csye6225.fall2018.coursepractise.datamodel.Student;

public class CourseBoardsService {
	static HashMap<Long, CourseBoard> board_Map = InMemoryDatabase.getCourseBoardDB();
	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;
	
	public CourseBoardsService() {
		dynamoDb = new DynamoDbConnector();
		dynamoDb.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
	}
	
	//adding a board	
	public CourseBoard addCourseBoard(long boardId, String courseId) {
		CourseBoard board=new CourseBoard();
		board.setBoardId(boardId);
		board.setCourseId(courseId);
		mapper.save(board);
		return mapper.load(CourseBoard.class, board.getId());
	}
	
	//getting a board by courseId
	public CourseBoard getBoard(String courseId) {
//		for (CourseBoard board : board_Map.values()) {
//			if(board.getCourseId().equals(courseId)) {
//				return board;
//			}
//		}
//		return new CourseBoard();
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(courseId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("courseId = :val2").withExpressionAttributeValues(eav);
		List<CourseBoard> scanResult;
		scanResult = mapper.scan(CourseBoard.class, scanExpression);
		if(scanResult.size() != 0) {
			return scanResult.get(0);
		}
		return null;
	}
	
	//delete a board: used when deleting a course
	public void deleteBoard(long boardId) {
/*		for(CourseBoard board: board_Map.values()) {
			if(board.getCourseId().equals(courseId)) {
				board_Map.remove(board.getBoardId());
			}
			break;//one course can only have on board
		}
*/
		String boardIds=String.valueOf(boardId);
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withN(boardIds));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("boardId = :val2").withExpressionAttributeValues(eav);
		List<CourseBoard> scanResult;
		scanResult = mapper.scan(CourseBoard.class, scanExpression);
		if(scanResult.size() != 0) {
			mapper.delete(scanResult.get(0));
		}
	}
}
