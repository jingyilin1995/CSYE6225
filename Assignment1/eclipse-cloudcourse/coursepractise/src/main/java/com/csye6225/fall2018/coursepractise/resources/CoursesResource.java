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
import com.csye6225.fall2018.coursepractise.datamodel.CourseBoard;
import com.csye6225.fall2018.coursepractise.datamodel.CourseRoster;
import com.csye6225.fall2018.coursepractise.datamodel.Professor;
import com.csye6225.fall2018.coursepractise.datamodel.Student;
import com.csye6225.fall2018.coursepractise.datamodel.Lecture;
import com.csye6225.fall2018.coursepractise.service.CoursesService;

@Path("courses")
public class CoursesResource {
	
	CoursesService courService = new CoursesService();
	
	@GET
	@Path("/allcourses")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getAllCourses(){
		String courseId = "CYSE6225";
		CourseRoster roster = new CourseRoster(0, courseId, new ArrayList<Student>());
		CourseBoard board = new CourseBoard(0,courseId);
		courService.addCourse(courseId, new Student(), new Professor(), new ArrayList<Lecture>(), board, roster);	
		return courService.getAllCourses();
	}
	
	@GET
	@Path("/{professor}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCoursesByProfessor(@PathParam("professor") String professor){
		if(professor == null) {
			return courService.getAllCourses();
		}
		return courService.getCourseByProfessor(professor);
	}
			
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> getCoursesByTA(@QueryParam("studentta") String studentta){
		if(studentta == null) {
			return courService.getAllCourses();
		}
		return courService.getCourseByTA(studentta);
	}
	
	@DELETE
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course deleteCourse(@PathParam("courseId") String courId) {
		return courService.deleteCourse(courId);
	}
		
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Course addCourse(Course cour) {
		return courService.addCourse(cour);
	}
	
	@PUT
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Course updateCourse(@PathParam("courseId") String coursId, Course cour) {
		return courService.updateCourseInformation(cour);
	}

}
