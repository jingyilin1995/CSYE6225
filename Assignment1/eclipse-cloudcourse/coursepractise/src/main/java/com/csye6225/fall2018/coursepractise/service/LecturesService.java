package com.csye6225.fall2018.coursepractise.service;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.csye6225.fall2018.coursepractise.datamodel.Course;
import com.csye6225.fall2018.coursepractise.datamodel.InMemoryDatabase;
import com.csye6225.fall2018.coursepractise.datamodel.Lecture;

public class LecturesService {
	static HashMap<Long, Lecture> lect_Map = InMemoryDatabase.getLectureDB();
	static HashMap<String, Course> cour_Map = InMemoryDatabase.getCourseDB();
	
	//Getting a list of all lectures
	//GET "..webapi/lectures"
	public List<Lecture> getAllLectures(){
		//Getting the list
		ArrayList<Lecture> list = new ArrayList<>();
		for(Lecture lect : lect_Map.values()) {
			list.add(lect);
		}
		return list;
	}
	
	//Getting a list of all lectures of a course
	//GET "..webapi/courses/{courseId}/lectures
	public List<Lecture> getAllLectures(String courseId){
		List<Lecture> lectureList = new ArrayList<Lecture>();
		for(Lecture lect : lect_Map.values()) {
			if(lect.getCourseId().equals(courseId)) {
				lectureList.add(lect);
			}
		}
		return lectureList;
	}
	
	//Adding a lecture: used when create a course
	public void addLecture(Lecture lecture) {
		long nextAvailabledId = lect_Map.size() + 1;
		lecture.setLectureId(nextAvailabledId);
		lect_Map.put(nextAvailabledId, lecture);
	}
	public void addLecture(String courseId, List<String> notes, List<String> materials) {
		long nextAvailabledId = lect_Map.size() +1;
		Lecture lect = new Lecture(nextAvailabledId, courseId, notes, materials);
		lect_Map.put(nextAvailabledId, lect);
	}

	//Adding a lecture: used when add a lecture to a course 
	public List<Lecture> addLecture(String courseId, Lecture lect) {
		long nextAvailabledId = lect_Map.size() + 1;
		lect.setLectureId(nextAvailabledId);
		lect_Map.put(nextAvailabledId, lect);
		for(Course cour : cour_Map.values()) {
			if(cour.getCourseId().equals(courseId)) {
				cour.getLectures().add(lect);
				cour_Map.put(courseId, cour);
			}
		}
		return cour_Map.get(courseId).getLectures();
	}
	
	//Getting one lecture
	public Lecture getLecture(Long lectId) {
		return lect_Map.get(lectId);
	}
	
	//Deleting lecture: used when deleteing a course
	public void deleteLecture(String courseId) {
		Set<java.util.Map.Entry<Long, Lecture>> set = lect_Map.entrySet();
		Iterator<java.util.Map.Entry<Long, Lecture>> iterator = set.iterator();
		while(iterator.hasNext()) {
			java.util.Map.Entry<Long, Lecture> entry = iterator.next();
			if(entry.getValue().getCourseId().equals(courseId)) {
				iterator.remove();
			}
		}
	}
	
	//Deleting a lecture of a course
	public List<Lecture> deleteLectureofaCourse(String courseId, Long lectId){
		List<Lecture> lectureList = new ArrayList<Lecture>();
		for(Lecture lect : lect_Map.values()) {
			if(lect.getLectureId() == lectId) {
				lect_Map.remove(lectId);
				break;
			}
		}
		for(Lecture lect : lect_Map.values()) {
			if(lect.getCourseId().equals(courseId)) {
				lectureList.add(lect);
			}
		}
		for(Course cour : cour_Map.values()) {
			if(cour.getCourseId().equals(courseId)) {
				cour.setLectures(lectureList);
				cour_Map.put(courseId, cour);
			}
		}
		return lectureList;	
	}
	
	//Updating a lecture Info
	public List<Lecture> updateLecture(String courseId, Lecture lect) {
		Lecture oldLectObject = lect_Map.get(lect.getLectureId());
		long lectId = oldLectObject.getLectureId();
		lect.setLectureId(lectId);
		//publishing new values
		lect_Map.put(lectId, lect);
		Course newcourse = new Course();
		for(Course course: cour_Map.values()) {
			if(course.getCourseId().equals(courseId)) {
				for(int i = 0; i< course.getLectures().size() ;i++) {
					if(course.getLectures().get(i).getLectureId() == lect.getLectureId()) {
						course.getLectures().set(i, lect);
						newcourse = course;
						cour_Map.put(courseId, course);
					}
				}
			}
		}
		return newcourse.getLectures();
	}
	
}
