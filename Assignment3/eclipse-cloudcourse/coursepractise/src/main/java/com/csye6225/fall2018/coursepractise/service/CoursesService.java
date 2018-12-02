package com.csye6225.fall2018.coursepractise.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.csye6225.fall2018.coursepractise.datamodel.Course;
import com.csye6225.fall2018.coursepractise.datamodel.CourseBoard;
import com.csye6225.fall2018.coursepractise.datamodel.CourseRoster;
import com.csye6225.fall2018.coursepractise.datamodel.DynamoDbConnector;
import com.csye6225.fall2018.coursepractise.datamodel.InMemoryDatabase;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.csye6225.fall2018.coursepractise.datamodel.Lecture;
import com.csye6225.fall2018.coursepractise.datamodel.Professor;
import com.csye6225.fall2018.coursepractise.datamodel.SnsConnector;
import com.csye6225.fall2018.coursepractise.datamodel.Student;

public class CoursesService {
	static HashMap<String, Course> cour_Map = InMemoryDatabase.getCourseDB();
	CourseRostersService rosterService = new CourseRostersService();
	CourseBoardsService boardService = new CourseBoardsService();
	LecturesService lectureService = new LecturesService();
	static DynamoDbConnector dynamoDb;
	DynamoDBMapper mapper;	
	static SnsConnector sns;
	AmazonSNS snsClient;
	
	public CoursesService() {
		dynamoDb = new DynamoDbConnector();
		dynamoDb.init();
		mapper = new DynamoDBMapper(dynamoDb.getClient());
		
		sns = new SnsConnector();
		sns.init();
		snsClient = sns.getClient();
		
	}
	
	//Getting a list of all courses
	//GET "..webapi/courses
	public List<Course> getAllCourses(){
/*		ArrayList<Course> list = new ArrayList<>();
		for(Course cour : cour_Map.values()) {
			list.add(cour);
		}
		return list;
*/
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		return scanResult;
	}
	
	//Adding a course
	public void addCourse(String courseId, String taId, String professorId, List<Long> lectures, String department) {
		//Create a Course Object
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<CourseRoster> scanResult = mapper.scan(CourseRoster.class, scanExpression);
		List<CourseBoard> scanResult2 = mapper.scan(CourseBoard.class, scanExpression);
		long rosterId = scanResult.size()+1;
		long boardId = scanResult2.size()+1;
		Course cour=new Course(courseId,taId,professorId,lectures,boardId,rosterId,department);
		rosterService.addCourseRoster(rosterId,courseId);
		boardService.addCourseBoard(boardId,courseId);
		lectureService.addLecture(courseId, lectures);
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(courseId);
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		System.out.println(createTopicResult);
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
		cour.setNotificationTopic(createTopicResult.getTopicArn());
		mapper.save(cour);
	}
	
	public Course addCourse(Course cour) {
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
		List<CourseRoster> scanResult = mapper.scan(CourseRoster.class, scanExpression);
		List<CourseBoard> scanResult2 = mapper.scan(CourseBoard.class, scanExpression);
		long rosterId = scanResult.size()+1;
		long boardId = scanResult2.size()+1;
		System.out.println(rosterId);
		cour.setBoardId(boardId);
		cour.setRosterId(rosterId);
		rosterService.addCourseRoster(cour.getRosterId(),cour.getCourseId());
		boardService.addCourseBoard(cour.getBoardId(),cour.getCourseId());
		lectureService.addLecture(cour.getCourseId(), cour.getLectures());
		CreateTopicRequest createTopicRequest = new CreateTopicRequest(cour.getCourseId());
		CreateTopicResult createTopicResult = snsClient.createTopic(createTopicRequest);
		System.out.println(createTopicResult);
		System.out.println("CreateTopicRequest - " + snsClient.getCachedResponseMetadata(createTopicRequest));
		cour.setNotificationTopic(createTopicResult.getTopicArn());
		mapper.save(cour);
		return mapper.load(Course.class,cour.getId());
	}
	
	//Getting One Course
	public Course getCourse(String courId) {
		Map<String, AttributeValue> eav = new HashMap<String,AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(courId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("courseId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		if(scanResult.size()!=0)
		{
			return scanResult.get(0);
		}
		return null;
	}
	
	//Deleting a course
	public Course deleteCourse(String courId) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(courId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("courseId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		if(scanResult.size()!=0)
		{
			Course course = scanResult.get(0);
			mapper.delete(course);
			rosterService.deleteRoster(course.getRosterId());
			boardService.deleteBoard(course.getBoardId());
			lectureService.deleteLecture(courId);
			return course;
		}
		return null;
	}
	
	//Updating Course Info
	public Course updateCourseInformation(String courseId, Course cour) {
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(courseId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("courseId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		if(scanResult.size()!=0)
		{
			String id = scanResult.get(0).getId();
			cour.setId(id);
			mapper.save(cour);
			return mapper.load(Course.class, cour.getId());
		}
		return null;
	}
	
	//Get courses belongs to an associated professor
	public List<Course> getCourseByProfessor(String professorId){
/*		ArrayList<Course> list = new ArrayList<>();
		for(Course cour : cour_Map.values()) {
			if(cour.getAssociatedProfessor().getFirstName().equals(professor)) {
				list.add(cour);
			}
		}
		return list;
*/
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(professorId));
		
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("professorId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		return scanResult;
	}
	
	//Get course belongs to a studentTA
	public List<Course> getCourseByTA(String taId){
/*		ArrayList<Course> list = new ArrayList<>();
		for(Course cour : cour_Map.values()) {
			if(cour.getStudentTA().getFirstName().equals(ta)) {
				list.add(cour);
			}
		}
		return list;	
*/
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(taId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("taId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		return scanResult;
	}
		
	//Get a course's TA's info
	public Student getTAInfo(String courseId) {
/*		for(Course cour : cour_Map.values()) {
			if(cour.getCourseId().equals(courseId)) {
				return cour.getStudentTA();
			}
		}
		return new Student();
*/
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(courseId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("courseId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		if(scanResult.size()!=0)
		{
			String studentId = scanResult.get(0).getTaId();
			Map<String, AttributeValue> eav2 = new HashMap<String, AttributeValue>();
			eav2.put(":val2", new AttributeValue().withS(studentId));
			DynamoDBScanExpression scanExpression2 = new DynamoDBScanExpression()
					.withFilterExpression("studentId = :val2").withExpressionAttributeValues(eav2);
			List<Student> scanResult2 = mapper.scan(Student.class, scanExpression2);
			return scanResult2.get(0);
		}
		return null;
	}
	
	//Get the associated professor's info of a course
	public Professor getProfInfo(String courseId) {
/*		for(Course cour: cour_Map.values()) {
			if(cour.getCourseId().equals(courseId)) {
				return cour.getAssociatedProfessor();
			}
		}
		return new Professor();
*/	
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(courseId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("courseId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		if(scanResult.size()!=0)
		{
			String professorId = scanResult.get(0).getProfessorId();
			Map<String, AttributeValue> eav2 = new HashMap<String, AttributeValue>();
			eav2.put(":val2", new AttributeValue().withS(professorId));
			DynamoDBScanExpression scanExpression2 = new DynamoDBScanExpression()
					.withFilterExpression("professorId = :val2").withExpressionAttributeValues(eav2);
			List<Professor> scanResult2 = mapper.scan(Professor.class, scanExpression2);
			return scanResult2.get(0);
		}
		return null;

	}
	
	//Update a course's TA's info
	public Student updateTAInfo(String courseId, Student student) {
/*		for(Course cour: cour_Map.values()) {
			if(cour.getCourseId().equals(courseId)) {
				cour.setStudentTA(student);
				cour_Map.put(courseId, cour);
				return cour_Map.get(courseId).getStudentTA();
			}
		}
		return null;
*/
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(courseId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("courseId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		if(scanResult.size()!=0)
		{
			scanResult.get(0).setTaId(student.getStudentId());
			mapper.save(scanResult.get(0));
			return student;
		}
		return null;
	}
	
	//Update a course's Professor's info
	public Professor UpdateProfInfo(String courseId, Professor professor) {
/*		for(Course cour: cour_Map.values()) {
			if(cour.getCourseId().equals(courseId)) {
				cour.setAssociatedProfessor(professor);
				cour_Map.put(courseId, cour);
				return cour_Map.get(courseId).getAssociatedProfessor();
			}
		}
		return null;
*/
		Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val2", new AttributeValue().withS(courseId));
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("courseId = :val2").withExpressionAttributeValues(eav);
		List<Course> scanResult = mapper.scan(Course.class, scanExpression);
		if(scanResult.size()!=0)
		{
			scanResult.get(0).setProfessorId(professor.getProfessorId());
			mapper.save(scanResult.get(0));
			return professor;
		}
		return null;
	}
}
