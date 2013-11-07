package DTO;

import java.util.ArrayList;

/**
 * 
 * @author Michael Crosby
 *
 */
public class Section {
	private int id;
	private String prof;
	private ArrayList<Meeting> meetings;
	private int courseId;
	
	/**
	 * Creates a Section object
	 * @param id
	 * @param prof
	 * @param courseId
	 */
	public Section(int id, String prof, ArrayList<Meeting> list, int courseId) {
		this.id = id;
		this.prof = prof;
		this.meetings=list;
		this.courseId = courseId;
	}

	public int getId() {
		return id;
	}

	public String getProf() {
		return prof;
	}

	public ArrayList<Meeting> getMeetings(){
		return meetings;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	
	
	
}
