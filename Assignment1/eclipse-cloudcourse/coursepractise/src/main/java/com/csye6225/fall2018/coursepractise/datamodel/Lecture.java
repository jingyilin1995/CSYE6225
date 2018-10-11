package com.csye6225.fall2018.coursepractise.datamodel;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

public class Lecture {
	private long lectureId;
	private List<String> notes;
	private List<String> materials;

	public Lecture() {
		
	}
	
	public Lecture(long lectureId, List<String> notes, List<String> materials) {
		this.setLectureId(lectureId);
		this.setNotes(notes);
		this.setMaterials(materials);
	}

	public long getLectureId() {
		return lectureId;
	}

	public void setLectureId(long lectureId) {
		this.lectureId = lectureId;
	}

	public List<String> getNotes() {
		return notes;
	}

	public void setNotes(List<String> notes) {
		this.notes = notes;
	}

	public List<String> getMaterials() {
		return materials;
	}

	public void setMaterials(List<String> materials) {
		this.materials = materials;
	}
}

