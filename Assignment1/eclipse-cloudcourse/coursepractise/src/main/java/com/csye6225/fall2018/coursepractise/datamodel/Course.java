package com.csye6225.fall2018.coursepractise.datamodel;

import java.util.List;

public class Course {
	private String courseId;
	private Student studentTA;
	private Professor associatedProfessor;
	private List<Lecture> lectures;
	private List<String> board;
	private List<Long> roster;
	
	public Course() {
		
	}
	public Course(String courseId, Student studentTA, Professor accociatedProfessor, List<Lecture> lecures, List<String> board, List<Long> roster) {
		this.setCourseId(courseId);
		this.setStudentTA(studentTA);
		this.setAssociatedProfessor(accociatedProfessor);
		this.setLectures(lecures);
		this.setBoard(board);
		this.setRoster(roster);
	}
	public String getCourseId() {
		return courseId;
	}
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	public Student getStudentTA() {
		return studentTA;
	}
	public void setStudentTA(Student studentTA) {
		this.studentTA = studentTA;
	}
	public Professor getAssociatedProfessor() {
		return associatedProfessor;
	}
	public void setAssociatedProfessor(Professor associatedProfessor) {
		this.associatedProfessor = associatedProfessor;
	}
	public List<String> getBoard() {
		return board;
	}
	public void setBoard(List<String> board) {
		this.board = board;
	}
	public List<Long> getRoster() {
		return roster;
	}
	public void setRoster(List<Long> roster) {
		this.roster = roster;
	}
	public List<Lecture> getLectures() {
		return lectures;
	}
	public void setLectures(List<Lecture> lectures) {
		this.lectures = lectures;
	}

}
