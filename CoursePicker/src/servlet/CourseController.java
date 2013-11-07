package servlet;
import java.io.IOException;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import DTO.*;
import helper.*;

/**
 * Servlet implementation class CourseController
 */
@WebServlet("/CourseController")
public class CourseController extends HttpServlet {
	private int tempReqId;
	private int tempCourseId;
	private int tempSectionId;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CourseController() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ArrayList<Meeting> meetingList = new ArrayList<Meeting>();
		ArrayList<Section> schedule;
		if(session.getAttribute("meetingList")==null){		
			//meetingList = new ArrayList<Meeting>();
			schedule = new ArrayList<Section>();
			//session.setAttribute("meetingList", meetingList);
			session.setAttribute("schedule", schedule);
		} else{
			//meetingList = (ArrayList<Meeting>) session.getAttribute("meetingList");
			schedule = (ArrayList<Section>) session.getAttribute("schedule");
		}
		//Make the course select menu appear when a req is selected
		if(request.getParameter("requirement")!=null && !(request.getParameter("requirement").equals(""))){
			request.setAttribute("courseDisplay", "block");
		}
		
		//This says that even though requirement will appear to be empty, as long as a course has
		//been selected from the drop down menu, keep the course menu visible while the section menu
		//appears underneath
		else{
			if(request.getParameter("course")!=null){
				request.setAttribute("courseDisplay", "block");
			}
		}

		
		//Display the sections only if there is a course selected and the request obj isn't null
		if(request.getParameter("course")!=null && !(request.getParameter("course").equals(""))){
			request.setAttribute("sectionDisplay", "block");

		}
		
	    //Creates a helper object and retrieves array of courses from db based on req number
		ServletContext ctx = this.getServletContext();
		RequestDispatcher dispatcher = ctx.getRequestDispatcher("/index.jsp");
		CourseHelper helper=new CourseHelper();
		
		
		/*If a requirement is selected from the menu, use the req id to pull corresponding courses from db
		 *and fill course menu
		 *Happens AFTER a user has selected a requirement and chooses Get Courses
		 */
		if(request.getParameter("requirement")!=null){

			int reqId=Integer.valueOf(request.getParameter("requirement"));
			//The chosen req should appear as the "selected" item in the requirement menu
			request.setAttribute("selectedReq", reqId);
			tempReqId=reqId;
			ArrayList<Course> courseArray=new ArrayList<Course>();
			courseArray=helper.getAvailableCoursesForRequirement(reqId);
			request.setAttribute("courseList", courseArray);
		} else
		/*If a course is selected from the menu, use that course id to pull corresponding
		 * sections from the db
		 * Happens AFTER a user has selected a course from the menu and chooses Get Sections
		 */
		if(request.getParameter("course")!=null){
			request.setAttribute("selectedReq", tempReqId);
			ArrayList<Course> courseArray=new ArrayList<Course>();
			courseArray=helper.getAvailableCoursesForRequirement(tempReqId);
			request.setAttribute("courseList", courseArray);
			
			/*
			 * After a user selects a course and hits submit, the default value of the select menu
			 * is set to the course prefix and number.
			 */
			Course course;
			course=helper.getCourseWithId(Integer.valueOf(request.getParameter("course")));
			String sC=course.getCoursePrefix() + " " + course.getCourseNumber();
			request.setAttribute("selectedCourse", sC);
			tempCourseId=Integer.valueOf(request.getParameter("course"));
			
			/*
			 * We use this next part to retrieve the current course value (courseId) and
			 * use that value to get the corresponding sections. We then create an array 
			 * of those sections to be used later.
			 */
			ArrayList<Section> sectionArray = helper.getSectionsForCourse(Integer.valueOf(request.getParameter("course")));
			request.setAttribute("sectionList", sectionArray);
			
		} else 
		
		/*
		 * Happens AFTER the user clicks link to add to Schedule 
		 */
		if(request.getParameter("section")!=null){
			/*
			 *Make the select menus visible 
			 */
			request.setAttribute("sectionDisplay", "block");
			request.setAttribute("courseDisplay", "block");
			
			request.setAttribute("selectedReq", tempReqId);
			ArrayList<Course> courseArray=new ArrayList<Course>();
			courseArray=helper.getAvailableCoursesForRequirement(tempReqId);
			request.setAttribute("courseList", courseArray);
			
			/*
			 * After a user selects a course and hits submit, the default value of the select menu
			 * is set to the course prefix and number.
			 */
			Course course;
			course=helper.getCourseWithId(tempCourseId);
			String courseString=course.getCoursePrefix() + " " + course.getCourseNumber();
			request.setAttribute("selectedCourse", courseString);
			
			/*
			 * We use this next part to retrieve the current course value (courseId) and
			 * use that value to get the corresponding sections. We then create an array 
			 * of those sections to be used later.
			 */
			Section s;
			s=helper.getSectionWithId(Integer.valueOf(request.getParameter("section")));
			boolean isConflict = false;
			
			//populates meetingList
			for(Section section:schedule){
				//adds the meetings of the section to the meetingList
				for(Meeting m:section.getMeetings()){
					//adds the meeting to the meeting list
					meetingList.add(m);
				}
			}
			
			for(Meeting m:s.getMeetings()){
				//checks the meeting of the section does not conflict with any of the meetings in current schedule
				for(Meeting m2:meetingList){
					//checks the meeting against the meeting in the schedule for a conflict
					if(m.getDay().equalsIgnoreCase(m2.getDay())){
						if(((m.getStartTime() < m2.getStartTime()) && (m.getEndTime() < m2.getStartTime())) 
						|| ((m.getStartTime() > m2.getEndTime()) && (m.getEndTime() > m2.getEndTime()))){
							//if the class is fully before or after the current meeting in the schedule, there is no conflict
							//so do nothing....
						} else {
							isConflict = true;
						}
					}
				}	
			}
			/*Displays an error message on the JSP if there is a class conflict
			*If not, adds the section to the list of sections in the current schedule
			*/
			if(isConflict){
				request.setAttribute("conflict", "conflict()");
			} else {
				schedule.add(s);
			}
			meetingList = 	new ArrayList<Meeting>();
		} else {
			Section toBeRemoved = null;
			for(Section temp: schedule){
				//compares the section's ID with the ID returned by the JSP, if it matches, the section is placed in the toBeRemoved variable
				int id = Integer.parseInt(request.getParameter("sectionList"));
				if(temp.getId() == id){
					toBeRemoved = temp;
				}
			}
			
			schedule.remove(toBeRemoved);
			
			meetingList = 	new ArrayList<Meeting>();
		}
		
		for(Section section:schedule){
			//adds the meetings of the section to the meetingList
			for(Meeting m:section.getMeetings()){
				//adds the meeting to the meeting list
				meetingList.add(m);
			}
		}
		session.setAttribute("schedule", schedule);			
		session.setAttribute("meetingList", meetingList);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
