package com.csye6225.fall2018.coursepractise.service;

import java.util.HashMap;

import com.csye6225.fall2018.coursepractise.datamodel.CourseBoard;
import com.csye6225.fall2018.coursepractise.datamodel.InMemoryDatabase;

public class CourseBoardsService {
	static HashMap<Long, CourseBoard> board_Map = InMemoryDatabase.getCourseBoardDB();
	
	//adding a board
	public void addCourseBoard(String courseId) {
		long nextAvailableId = board_Map.size() + 1;
		CourseBoard board = new CourseBoard(nextAvailableId, courseId);
		board_Map.put(nextAvailableId, board);
	}
	
	public CourseBoard addCourseBoard(CourseBoard board) {
		long nextAvailableId = board_Map.size() + 1;
		board.setBoardId(nextAvailableId);
		board_Map.put(nextAvailableId, board);
		return board_Map.get(nextAvailableId);
	}
	
	//getting a board by courseId
	public CourseBoard getBoard(String courseId) {
		for (CourseBoard board : board_Map.values()) {
			if(board.getCourseId().equals(courseId)) {
				return board;
			}
		}
		return new CourseBoard();
	}
	
	//delete a board: used when deleting a course
	public void deleteBoard(String courseId) {
		for(CourseBoard board: board_Map.values()) {
			if(board.getCourseId().equals(courseId)) {
				board_Map.remove(board.getBoardId());
			}
			break;//one course can only have on board
		}
	}
}
