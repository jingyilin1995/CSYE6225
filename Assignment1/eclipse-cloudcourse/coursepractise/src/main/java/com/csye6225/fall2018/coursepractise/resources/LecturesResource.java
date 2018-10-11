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
import javax.ws.rs.core.MediaType;

import com.csye6225.fall2018.coursepractise.datamodel.Lecture;
import com.csye6225.fall2018.coursepractise.service.LecturesService;

//../webapi/myresource
@Path("lectures")
public class LecturesResource {
	LecturesService lectService = new LecturesService();
	
	@GET
	@Path("/alllectures")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Lecture> getAllLectures(){
//		List<String> notes = new ArrayList<String>();
//		List<String> materials = new ArrayList<String>();
//		String note1 = "This is the first lecture!";
//		String material1 ="http://icemoon.iteye.com/blog/717151";
//		notes.add(note1);
//		materials.add(material1);
//		addLecture(notes,materials);
		
		return lectService.getAllLectures();
	}
	
	//...webapi/lecture/1
	@GET
	@Path("/{lectureId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lecture getLecture(@PathParam("lectureId") long lectId) {
		return lectService.getLecture(lectId);
	}
	
	@DELETE
	@Path("/{lectureId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Lecture deleteLecture(@PathParam("lectureId") long lectId) {
		return lectService.deleteLecture(lectId);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Lecture addLecture(Lecture lect) {
		return lectService.addLecture(lect);
	}
	
	@PUT
	@Path("/{lectureId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Lecture updateLecture(@PathParam("lectureId") long lectId, Lecture lect) {
		return lectService.updateLecture(lectId, lect);
	}
	
	public void addLecture(List<String> notes, List<String> materials ) {
		lectService.addLecture(notes, materials);
	}
	

}
