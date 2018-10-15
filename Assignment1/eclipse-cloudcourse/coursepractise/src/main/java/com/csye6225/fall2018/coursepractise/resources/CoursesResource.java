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
		Lecture lecture = new Lecture(0,courseId, new ArrayList<String>(), new ArrayList<String>());
		List<Lecture> list = new ArrayList<Lecture>();
		list.add(lecture);
		courService.addCourse(courseId, new Student(), new Professor(), list, board, roster);	
		return courService.getAllCourses();
	}
	
	@GET
	@Path("/{courseId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Course getCoursesByCourseId(@PathParam("courseId") String courseId) {
		return courService.getCourse(courseId);
	}
	
	@GET
	@Path("/professor/{professor}")
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
	
	//get TA's information by courseId
	@GET
	@Path("/{courseId}/TA")
	@Produces(MediaType.APPLICATION_JSON)
	public Student getTAInfo(@PathParam("courseId") String courseId ) {
		return courService.getTAInfo(courseId);
	}
	
	//get Associated Professor's information by courseId
	@GET
	@Path("/{courseId}/professor")
	@Produces(MediaType.APPLICATION_JSON)
	public Professor getProfInfo(@PathParam("courseId") String courseId) {
		return courService.getProfInfo(courseId);
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
	
	//Update a course's TA Info
	@PUT
	@Path("/{courseId}/TA")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Student updateTAInfo(@PathParam("courseId") String courseId, Student student) {
		return courService.updateTAInfo(courseId, student);
	}
	
	//Update a course's professor Info
	@PUT
	@Path("/{courseId}/professor")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Professor updateProfInfo(@PathParam("courseId") String courseId, Professor professor) {
		return courService.UpdateProfInfo(courseId, professor);
	}

}
