package com.csye6225.fall2018.coursepractise.datamodel;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

public class Program {
	private long programId;
	private String programName;
	private List<Student> students;
	private List<Course> courses;
	
	public Program() {
		
	}
	
	public Program(long programId,String programName, List<Student> students, List<Course> courses) {
		this.setProgramId(programId);
		this.setProgramName(programName);
		this.setStudents(students);
		this.setCourses(courses);
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public long getProgramId() {
		return programId;
	}

	public void setProgramId(long programId) {
		this.programId = programId;
	}
}
