package com.csye6225.fall2018.coursepractise.service;

import java.util.HashMap;
import java.util.List;

import com.csye6225.fall2018.coursepractise.datamodel.CourseRoster;
import com.csye6225.fall2018.coursepractise.datamodel.InMemoryDatabase;
import com.csye6225.fall2018.coursepractise.datamodel.Student;

public class CourseRostersService {
	static HashMap<Long,CourseRoster> roster_Map = InMemoryDatabase.getCourseRosterDB();
	
	//adding a roster
	public void addCourseRoster(String courseId, List<Student> rosterlist) {
		long nextAvailableId = roster_Map.size() + 1;
		CourseRoster roster = new CourseRoster(nextAvailableId, courseId, rosterlist);
		roster_Map.put(nextAvailableId, roster);
	}
	
	public CourseRoster addCourseRoster(CourseRoster roster) {
		long nextAvailableId = roster_Map.size() + 1;
		roster.setRosterId(nextAvailableId);
		roster_Map.put(nextAvailableId, roster);
		return roster_Map.get(nextAvailableId);
	}
	
	//getting a roster by courseId
	public CourseRoster getRoster(String courseId) {
		for (CourseRoster roster : roster_Map.values()) {
			if(roster.getCourseId().equals(courseId)) {
				return roster;
			}
		}
		return new CourseRoster();
		 
	}
	
	//clear a roster's rosterlist
	public CourseRoster clearRoster(String courseId) {
		for (CourseRoster roster : roster_Map.values()) {
			if(roster.getCourseId().equals(courseId)) {
				roster.getRosterlist().clear();
				return roster;
			}
		}
		return new CourseRoster();
	}
	
	//delete a roster: used when deleting a course
	public void deleteRoster(String courseId) {
		for(CourseRoster roster : roster_Map.values()) {
			if(roster.getCourseId().equals(courseId)) {
				roster_Map.remove(roster.getRosterId());
			}
		}
	}
	
	//add a new student into roster of a course
	public CourseRoster updateRosterAddStudent(String courseId, Student student) {
		CourseRoster updateRoster = new CourseRoster();
		for(CourseRoster roster : roster_Map.values()) {
			if(roster.getCourseId().equals(courseId)) {
				roster.getRosterlist().add(student);
				updateRoster = roster;
			}
		}
		return updateRoster;
	}
	
	//delete a student from a roster of a course
	public CourseRoster updateRosterDeleteStudent(String courseId, Student student) {
		CourseRoster updateRoster = new CourseRoster();
		for(CourseRoster roster : roster_Map.values()) {
			if(roster.getCourseId().equals(courseId)) {
				for( int i =0; i < roster.getRosterlist().size(); i++)
				{
					if(roster.getRosterlist().get(i).getStudentId() == student.getStudentId()){
						roster.getRosterlist().remove(i);
					}
				}
				updateRoster = roster;
			}
		}
		return updateRoster;
	}	
}
 