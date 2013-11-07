package DTO;
public class Course {
	private int id;
	private String coursePrefix;
	private String courseNumber;
	private String courseName;
	
	/**
	 * Creates a Course object
	 * @param id
	 * @param coursePrefix
	 * @param courseNumber
	 * @param courseName
	 * @param creditHours
	 */
	public Course(int id, String coursePrefix, String courseNumber, String courseName) {
		this.id = id;
		this.coursePrefix = coursePrefix;
		this.courseNumber = courseNumber;
		this.courseName= courseName;
	}

	public int getId() {
		return id;
	}

	public String getCoursePrefix() {
		return coursePrefix;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public String getCourseName() {
		return courseName;
	}
	
	
}
