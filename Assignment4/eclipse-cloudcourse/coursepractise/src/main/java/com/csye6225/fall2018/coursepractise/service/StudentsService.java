package com.csye6225.fall2018.coursepractise.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.csye6225.fall2018.coursepractise.datamodel.Announcement;
import com.csye6225.fall2018.coursepractise.datamodel.Course;
import com.csye6225.fall2018.coursepractise.datamodel.DynamoDbConnector;
import com.csye6225.fall2018.coursepractise.datamodel.InMemoryDatabase;
import com.csye6225.fall2018.coursepractise.datamodel.Professor;
import com.csye6225.fall2018.coursepractise.datamodel.SnsConnector;
import com.csye6225.fall2018.coursepractise.datamodel.Student;

public class StudentsService {
	static HashMap<Long, Student> stud_Map = InMemoryDatabase.getStudentDB();
	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;
	static SnsConnector sns;
	AmazonSNS snsClient;

	public StudentsService() {
		dynamoDb = new DynamoDbConnector();
		dynamoDb.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
		
		sns = new SnsConnector();
		sns.init();
		snsClient = sns.getClient();
	}
	//Getting a list of all students
	//GET "..webapi/students"
	public List<Student> getAllStudents(){
		//Getting the list
//		ArrayList<Student> list = new ArrayList<>();
//		for(Student stud : stud_Map.values()) {
//			list.add(stud);
//		}
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<Student> scanResult = mapper.scan(Student.class, scanExpression);
		return scanResult;
	}
	
	//Adding a student
	public void addStudent(String firstName, String lastName, String studentId, String imageUrl, List<String> courses, String department, String joiningDate, String emailId) {
		Student stud = new Student(firstName, lastName, studentId, imageUrl, courses, department, joiningDate,emailId);
		mapper.save(stud);
	}
	
	public Student addStudent(Student stud) {
		mapper.save(stud);
		return mapper.load(Student.class,stud.getId());
	}
	
	//Getting one Student
	public Student getStudent(String studentId) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(studentId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("studentId = :val2").withExpressionAttributeValues(eav);
		List<Student> scanResult = mapper.scan(Student.class, scanExpression);
		if(scanResult.size()!=0) {
			return scanResult.get(0);
		}
		return null;
	}
	
	//Deleting a Student
	public Student deleteStudent(String studentId) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(studentId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("studentId = :val2").withExpressionAttributeValues(eav);
		List<Student> scanResult = mapper.scan(Student.class, scanExpression);
		if(scanResult.size()!=0) {
			Student stud = scanResult.get(0);
			mapper.delete(stud);
			return stud;
		}
		return null;
	}
	
	//Updating a Student Info
	public Student updateStudent(String studentId, Student stud) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(studentId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("studentId = :val2").withExpressionAttributeValues(eav);
		List<Student> scanResult = mapper.scan(Student.class, scanExpression);
		if(scanResult.size()!=0) {
			String Id = scanResult.get(0).getId();
			stud.setId(Id);
			mapper.save(stud);
			return mapper.load(Student.class, Id);
		}
		return null;
	}
	
	//Get students in a program
	public List<Student> getStudentByDepartment(String department){
		//Getting the list
//		ArrayList<Student> list = new ArrayList<>();
//		for(Student stud : stud_Map.values()) {
//			if(stud.getDepartment().equals(program)) {
//				list.add(stud);
//			}
//		}
//		return list;
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(department));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("department = :val2").withExpressionAttributeValues(eav);
		List<Student> scanResult = mapper.scan(Student.class, scanExpression);
		return scanResult;
	}
	
	//Register Student for Course action
	//if lists have more than 3 courses, we only register the first 3 courses
	public Student registerCoursetoStudent(String studentId, ArrayList<Course> lists) {
		CourseRostersService crs = new CourseRostersService();
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":stud", new AttributeValue().withS(studentId));
		DynamoDBQueryExpression<Student> queryExpression = new DynamoDBQueryExpression<Student>()
				.withIndexName("studentId-index").withConsistentRead(false)
				.withKeyConditionExpression("studentId = :stud").withExpressionAttributeValues(eav);
		List<Student> result = mapper.query(Student.class, queryExpression);
		Student stud = new Student();
		if(result.size()!=0) {
			stud = result.get(0);
		}
		List<String> courselist = stud.getCourses();
		for(int i = 0; i<lists.size();i++) {
			if(stud.getCourses().size()<3) {
				courselist.add(lists.get(i).getCourseId());
				this.subscribeTopic(stud, lists.get(i).getCourseId());
				crs.updateRosterAddStudent(lists.get(i).getCourseId(), stud);
				
			}
		}
		stud.setCourses(courselist);
		mapper.save(stud);
		return mapper.load(Student.class, stud.getId());
	}
	
	public void subscribeTopic(Student stud, String courseId) {
		String topicArn = this.getCourseTopic(courseId);
		SubscribeRequest subRequest = new SubscribeRequest(topicArn, "email",stud.getEmailId());
		snsClient.subscribe(subRequest);
		System.out.println("SubscribeRequest - " + snsClient.getCachedResponseMetadata(subRequest));
		System.out.println("Check your email and confirm subscription");
	}
	
	public String getCourseTopic(String courseId) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":cour", new AttributeValue().withS(courseId));
		DynamoDBQueryExpression<Course> queryExpression = new DynamoDBQueryExpression<Course>()
				.withIndexName("courseId-index").withConsistentRead(false)
				.withKeyConditionExpression("courseId = :cour").withExpressionAttributeValues(eav);
		List<Course> result = mapper.query(Course.class, queryExpression);
		Course cour = new Course();
		if(result.size()!=0) {
			cour = result.get(0);
		}
		return cour.getNotificationTopic();
	}
}
