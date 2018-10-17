package com.csye6225.fall2018.coursepractise.datamodel;

import java.util.List;

public class CourseRoster {
	
	private long rosterId;
	private String courseId;
	private List<Student> rosterlist;
	
	public CourseRoster() {
		
	}
	
	public CourseRoster(long rosterId, String courseId, List<Student> rosterlist) {
		this.setRosterId(rosterId);
		this.setCourseId(courseId);
		this.setRosterlist(rosterlist);
	}

	public long getRosterId() {
		return rosterId;
	}

	public void setRosterId(long rosterId) {
		this.rosterId = rosterId;
	}

	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public List<Student> getRosterlist() {
		return rosterlist;
	}

	public void setRosterlist(List<Student> rosterlist) {
		this.rosterlist = rosterlist;
	}

}
