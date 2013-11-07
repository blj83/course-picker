<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Course Picker</title>
<script src="jquery-1.9.1.min.js"></script>
<script>
function findMeeting(){
	array = '${meetingList}';
	console.log(array);
	try{
		meetings = jQuery.parseJSON(array);
	} catch (err){
		console.log("Stay calm, it'll work once you pick a meeting");
	}
	console.log(meetings);
}
</script>
<script type="text/javascript" src="script.js"></script>
</head>
<body onload="getSchedule()">
	<h1 style="text-align: center;">Unicorn Destroyers Course Picker</h1>

	<c:set var="courseDisplay" scope="session" value="none" />
	<c:set var="sectionDisplay" scope="session" value="none" />
	<div style="width:100%; position:relative">
<table style="width: 50%;">
	<tr>
	<td>
	<h2>Requirements</h2>
	<div>
	<!-- Drop-down menu (temporarily) of Reqs -->
	<form action="CourseController" method="GET" onSubmit= "return showCourses();" >
		<select id="requirement" name="requirement" onchange="hideCourseSelect()">
			<option value="1">Cultural Diversity Requirement</option>
			<option value="2">Environmental Literacy Requirement</option>
			<option value="3">Core Curriculum I: Foundation Courses</option>
			<option value="4">Core Curriculum II: Physical Sciences</option>
			<option value="5">Core Curriculum II: Life Sciences</option>
			<option value="6">Core Curriculum III: Quantitative Reasoning</option>
			<option value="7">Core Curriculum IV: World Languages and Culture</option>
			<option value="8">Core Curriculum IV: Humanities and Arts</option>
			<option value="9">Core Curriculum V: Social Sciences</option>
			<option value="10">Franklin College Foreign Language</option>
			<option value="11">Franklin College Literature</option>
			<option value="12">Franklin College Fine Arts/Philosophy/Religion</option>
			<option value="13">Franlin College History</option>
			<option value="14">Franklin CollegeSocial Sciences other than History</option>
			<option value="15">Franklin College Biological Sciences</option>
			<option value="16">Franklin College:Physical Sciences</option>
			<option value="17">Franklin College Multicultural Requirement</option>
			<option value="18">Core Curriculum VI: Major related courses</option>
			<option value="19">Computer Science Major Courses</option>
		</select>
		<p><input type="submit" value="Get Courses"></p>
	</form>
	</div>
	</td>
	</tr>
    
	<tr>
	<td>
	<div id="courseSelects">
	<h2 style="display:${courseDisplay};">Courses that fulfill ${selectedReq}</h2>
	<!-- Courses will appear here -->
	<form action=CourseController method="GET" onSubmit="return showSections();">
		<select onchange="hideSections()" id="course" name="course" style="display:${courseDisplay};">
		<c:forEach items="${courseList}" var="course">
			<%--Creates a select option that represents a Course --%>
			<option value="${course.id}">${course.coursePrefix} ${course.courseNumber}: ${course.courseName }</option>
		</c:forEach>
		</select>
		<p><input style="display: ${courseDisplay}" type="submit" value="Get Sections"/></p>
	</form>
	</div>
	</td>
	</tr>
	
	<tr>
	<td>
	<div id="sectionSelects">
	<h2 style="display:${sectionDisplay};">Sections Available for ${selectedCourse} </h2>
	<h3 style="display:${sectionDisplay}">Click a class to add it to your schedule</h3>
	<h3 style="color: red">${message}</h3>
	<div id="sections">
	 
	<table> 
		<c:forEach items="${sectionList}" var="section">
			<%--Prints the section id and Professor as a link to add the class, and prints the list of meetings for the section --%>
			<tr><td>${section.id }</td><td><a onclick="hideSections()" href="CourseController?section=${section.id }">${section.prof }</a></td></tr>
			<c:forEach items="${section.meetings}" var="meeting">	
				<%--Prints the meeting's string description --%>			
				<tr><td></td><td>${meeting.timeString}</td></tr>
			</c:forEach>
		</c:forEach>
	</table>
	
	</div>
	</div>
	</td>
	</tr>
	
    <tr>
    <td>
	<h1>Sections!!!!</h1>
	<form action="CourseController" method="GET">
		<select id="section" name="sectionList">
		<c:forEach items="${schedule}" var = "section">
			<%--Creates a selection option that represents the Section --%>
			<option value="${section.id}">${section.meetings[0].mtgString }</option>
		</c:forEach>
        </select>
		<input type="submit" value="Delete"/>
	</form>
    </td>
    </tr>
    </table>
    <div style="width: 40%; position: absolute; left: 500px; top: 10px;">
        <div><h2 style="text-align: center;">Your Schedule</h2></div>
        
        <div id = "test" style ="width: 700px;
            height: 350px;
            margin: 0px auto;">
            <canvas id="schedule" width="650" height = "1040"></canvas>
        </div>
    </div>
</body>
</html>
