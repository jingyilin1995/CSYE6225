package com.csye6225.fall2018.coursepractise.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.csye6225.fall2018.coursepractise.datamodel.Course;
import com.csye6225.fall2018.coursepractise.datamodel.Student;
import com.csye6225.fall2018.coursepractise.service.StudentsService;

//.. /webapi/myresource
@Path("students")
public class StudentsResource {

		StudentsService studService = new StudentsService();
		
//		@GET
//		@Path("/allstudents")
//		@Produces(MediaType.APPLICATION_JSON)
//		public List<Student> getAllStudents(){
//			return studService.getAllStudents();
//		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public List<Student> getStudentByDepartment(@QueryParam("department") String department){
			if(department == null) {
				return studService.getAllStudents();
			}
			return studService.getStudentByDepartment(department);
		}
		
		//...webapi/student/1
		@GET
		@Path("/{studentId}")
		@Produces(MediaType.APPLICATION_JSON)
		public Student getStudent(@PathParam("studentId") String studentId) {
			return studService.getStudent(studentId);
		}
		
		@DELETE
		@Path("/{studentId}")
		@Produces(MediaType.APPLICATION_JSON)
		public Student deleteStudent(@PathParam("studentId") String studId) {
			return studService.deleteStudent(studId);
		}
		
		@POST
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public Student addStudent(Student stud) {
			return studService.addStudent(stud);
		}
		
		@PUT
		@Path("/{studentId}")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public Student updateStudent(@PathParam("studentId") String studentId, Student stud) {
			return studService.updateStudent(studentId, stud);
		}
		
		//Register Student for Course action
		@POST
		@Path("/{studentId}/register")
		@Produces(MediaType.APPLICATION_JSON)
		@Consumes(MediaType.APPLICATION_JSON)
		public Student registerCoursetoStudent(@PathParam("studentId") String studentId, ArrayList<Course> lists) {
			return studService.registerCoursetoStudent(studentId, lists);
		}
		
		public void addStudent(String firstName, String lastName, String studentId,String imageUrl, List<String> courses, String department, String joiningDate, String emailId) {
			studService.addStudent(firstName, lastName, studentId, imageUrl, courses, department, joiningDate,emailId);
		}
}
