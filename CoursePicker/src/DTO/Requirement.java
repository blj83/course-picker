package DTO;

public class Requirement {
	private int requirementId;
	private String coursePrefix;
	private int courseNumber;
	
	/**
	 * Creates a Requirement object
	 * @param requirementId
	 * @param coursePrefix
	 * @param courseNumber
	 */
	public Requirement(int requirementId, String coursePrefix, int courseNumber) {
		this.requirementId = requirementId;
		this.coursePrefix = coursePrefix;
		this.courseNumber = courseNumber;
	}

	public int getrequirementId() {
		return requirementId;
	}


	public String getCoursePrefix() {
		return coursePrefix;
	}


	public int getCourseNumber() {
		return courseNumber;
	}
	
	
}
