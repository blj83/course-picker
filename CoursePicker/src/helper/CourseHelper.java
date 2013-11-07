package helper;

import java.sql.*;
import java.util.*;

import DTO.*;

/**
 * 
 * @author Michael Crosby
 * @author Tanya Siclait
 *
 */
public class CourseHelper {
	private static final String  JDBC_URL = "jdbc:mysql://172.17.152.59/CourseRegistration";
	private int requirementId, courseNumber;
	private String coursePrefix; 
	
	/**
	 * Retrieves the static list of Requirements
	 */
	private PreparedStatement getRequirementListStatement;
	/**
	 * Retrieves the list of Sections for the specified Course
	 */
	private PreparedStatement getSectionListStatement; 
	/**
	 * Retrieves the list of meetings for the specified Section
	 */
	private PreparedStatement getMeetingListStatement; 
	/**
	 * Retrieves courses for specific section
	 */
	private PreparedStatement getCourseStatement;
	/**
	 * Retrieve a specific Section
	 */
	private PreparedStatement getSectionStatement;
	/**
	 * Retrieve specific course info for specified course id
	 */
	private PreparedStatement getSpecificCourseStatement;
	/**
	 * Retrieve specific section info for specified section id
	 */
	private PreparedStatement getSpecificSectionStatement;


	/**
	 * Creates a CourseHelper object that connects to the database and prepares the mysql statements
	 */
	public CourseHelper(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection(JDBC_URL, "tanya", "courseDB");
			getRequirementListStatement = conn.prepareStatement("Select * from Requirement where requirementId = ?");
			getSectionListStatement = conn.prepareStatement("Select * from Section where courseId= ?");
			getMeetingListStatement = conn.prepareStatement("Select * from Meeting where sectionId = ?");
			getCourseStatement = conn.prepareStatement("Select * from Course where coursePrefix = ? AND courseNum = ?");
			getSectionStatement = conn.prepareStatement("Select * from Section where id= ? ");
			getSpecificCourseStatement= conn.prepareStatement("Select * from Course where id= ?");
			getSpecificSectionStatement= conn.prepareStatement("Select * from Section where id= ?");

		} catch (Exception e) {
			System.out.println("Problem connecting to MySQL: " + e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Retrieves a list of courses that satisfy the chosen Requirement
	 * @param id
	 * @return
	 */
	public ArrayList<Course> getCoursesForRequirement(int id){
		ArrayList<Course> list = new ArrayList<Course>();
		String coursePrefix, courseNumber, courseName;
		int courseId;
		try{
			getRequirementListStatement.setInt(1, id);
			ResultSet set = getRequirementListStatement.executeQuery();
			while(set.next()){
				//creates a course using attributes from set item and adds it to the list
				courseId=set.getInt("requirementId");
				coursePrefix=set.getString("coursePrefix");
				courseNumber=set.getString("courseNum");
				courseName=null;
				Course r = new Course(courseId, coursePrefix, courseNumber, courseName);
				list.add(r);
			}
		}catch(Exception e){
			System.out.println("Error getting courses for specified req from table\n " + e.getClass().getName() + ": " + e.getMessage());
		}
		return list;
	}
	/**
	 * Retrieves a list of courses that satisfy the chosen requirement
	 * @param id
	 * @return
	 */
	public ArrayList<Course> getAvailableCoursesForRequirement(int id){
		String coursePrefix, courseNumber, courseName;
		int courseId;
		ArrayList<Course> tempList= new ArrayList<Course>();
		ArrayList<Course> finalList= new ArrayList<Course>();
		tempList=getCoursesForRequirement(id);
		for(Course course:tempList){
			//creates a list of Course objects from the resultSet found by using the course id
			try{
				getCourseStatement.setString(1, course.getCoursePrefix());
				getCourseStatement.setString(2, course.getCourseNumber());
				ResultSet set= getCourseStatement.executeQuery();
				while(set.next()){
					//creates a Course using attributes from set item and adds it to the list
					courseId=set.getInt("id");
					coursePrefix=set.getString("coursePrefix");
					courseNumber=set.getString("courseNum");
					courseName=set.getString("courseName");
					Course c=new Course(courseId, coursePrefix, courseNumber, courseName);
					finalList.add(c);
				}
			}
			catch(Exception e){
				System.out.println("Error getting courses for specified req from table\n " + e.getClass().getName() + ": " + e.getMessage());
			}
		}
		return finalList;
	}

	/**
	 * Retrieves a list of sections for the specified Course
	 * @param cId
	 * @return
	 */
	public ArrayList<Section> getSectionsForCourse(int cId){
		//TODO use get specific course to get correct sections
		ArrayList<Section> list = new ArrayList<Section>();
		String prof;
		int id, courseId;
		try{
			getSectionListStatement.setInt(1, cId);
			ResultSet set = getSectionListStatement.executeQuery();
			while(set.next()){
				//creates a Section using attributes from set item and adds it to the list
				id=set.getInt("id");
				prof=set.getString("prof");
				courseId=set.getInt("courseId");
				Section s = new Section(id, prof, getMeetingsForSection(id), courseId);
				list.add(s);
			}
		}catch(Exception e){
			System.out.println("Error getting sections from table\n " + e.getClass().getName() + ": " + e.getMessage());
		}
		return list;
	}

	/**
	 * Retrieves the list of meetings for the chosen Section
	 * @param sId
	 * @return
	 */
	public ArrayList<Meeting> getMeetingsForSection(int sId){
		ArrayList<Meeting> list = new ArrayList<Meeting>();
		int id, startTime, endTime, sectionId;
		String day, mtgString, timeString;
		try{
			getMeetingListStatement.setInt(1, sId);
			ResultSet set = getMeetingListStatement.executeQuery();
			while(set.next()){
				//creates a Meeting using attributes from set item and adds it to the list
				id=set.getInt("id");
				day=set.getString("day");
				startTime=set.getInt("startTime");
				endTime=set.getInt("endTime");
				mtgString=set.getString("mtgString");
				timeString=set.getString("timeString");
				sectionId=set.getInt("sectionId");
				Meeting m = new Meeting(id, day, startTime, endTime, mtgString, timeString, sectionId);
				list.add(m);
			}
		}catch(Exception e){
			System.out.println("Error getting meetings from table\n " + e.getClass().getName() + ": " + e.getMessage());
		}
		return list;
	}
	/**
	 * Retrieves a specific course using the courseId
	 * @param id
	 * @return
	 */
	public Course getCourseWithId(int id){
		Course course=null;
		String coursePrefix, courseNumber, courseName;
		int courseId;
		try{
			getSpecificCourseStatement.setInt(1, id);
			ResultSet set= getSpecificCourseStatement.executeQuery();
			while(set.next()){
				//creates a Course using attributes from set item
				courseId=set.getInt("id");
				coursePrefix=set.getString("coursePrefix");
				courseNumber=set.getString("courseNum");
				courseName=set.getString("courseName");
				course=new Course(courseId, coursePrefix, courseNumber, courseName);
			}
		}
		catch(Exception e){
			System.out.println("Error getting course from table\n " + e.getClass().getName() + ": " + e.getMessage());
		}
		return course;
	}
	/**
	 * Retrieves a specific section using the sectionId
	 * @param id
	 * @return
	 */
	public Section getSectionWithId(int id){
		Section s = null;
		int courseId;
		String prof;
		try{
			getSpecificSectionStatement.setInt(1,id);
			ResultSet set = getSpecificSectionStatement.executeQuery();
			while(set.next()){
				//creates a Section using attributes from set item
				courseId=set.getInt("courseId");
				prof=set.getString("prof");
				s=new Section(id, prof, getMeetingsForSection(id), courseId);
			}
		} catch (Exception e){
			System.out.println("Error getting section from table\n " + e.getClass().getName() + ": " + e.getMessage());
		}
		return s;
	}
}
