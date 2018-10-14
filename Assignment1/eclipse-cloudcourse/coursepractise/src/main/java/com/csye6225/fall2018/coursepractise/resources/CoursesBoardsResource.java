package com.csye6225.fall2018.coursepractise.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.csye6225.fall2018.coursepractise.datamodel.CourseBoard;
import com.csye6225.fall2018.coursepractise.service.CourseBoardsService;

@Path("courses")
public class CoursesBoardsResource {
	CourseBoardsService boardService = new CourseBoardsService();
	
	@GET
	@Path("{courseId}/board")
	@Produces(MediaType.APPLICATION_JSON)
	public CourseBoard getBoardByCourse(@PathParam("courseId") String courseId) {
		return boardService.getBoard(courseId);
	}
	
}
