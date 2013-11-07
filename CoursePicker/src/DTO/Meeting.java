package DTO;

public class Meeting {
	private int id;
	private String day;
	private int startTime;
	private int endTime;
	private String mtgString;
	private String timeString;
	private int sectionId;
	
	/**
	 * Creates a Meeting object
	 * @param id
	 * @param day
	 * @param startTime
	 * @param endTime
	 * @param mtgString
	 * @param sectionId
	 */
	public Meeting(int id, String day, int startTime, int endTime, String mtgString, String timeString, int sectionId) {
		this.id = id;
		this.day = day;
		this.startTime = startTime;
		this.endTime = endTime;
		this.mtgString= mtgString;
		this.timeString = timeString;
		this.sectionId = sectionId;
	}
	/**
	 * Used to create correct output for JSON creation is JSP
	 */
	public String toString(){
		return "{\"mtgString\": " + "\"" + this.mtgString + "\", \"day\":" + "\"" + this.day + "\"," + 
				"\"startTime\":" + this.startTime  + ",\"endTime\":" + this.endTime  + "}";
	}
	
	public int getId() {
		return id;
	}

	public String getDay() {
		return day;
	}

	public int getStartTime() {
		return startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public int getSectionId() {
		return sectionId;
	}
	
	public String getMtgString(){
		return mtgString;
	}
	
	public String getTimeString(){
		return timeString;
	}
	
}
