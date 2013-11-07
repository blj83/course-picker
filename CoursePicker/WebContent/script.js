function showCourses(){
	var requirement=document.getElementById("requirement");
	if(requirement){
		value=requirement.value;
		if(value === ""){
			alert("Please choose a requirement to view available courses!");
			return false;
		}
		else{
			return true;
		}
	}
	else{
		alert("Sad Day :(");
		return false;
	}
	
}

function showSections(){
	var course=document.getElementById("course");
	if(course){
		value=course.value;
		if(value === ""){
			alert("Please choose a course to view available sections!");
			return false;
		}
		else{
			return true;
		}
	}
	else{
		alert("Sad Day :(");
		return false;
	}
	
}

function showSchedule(){
	var section=document.getElementById("section");
	if(section){
		value=section.value;
		if(value === ""){
			alert("Please choose a section to add to schedule!");
			return false;
		}
		else{
			return true;
		}
	}
	else{
		alert("Sad Day :(");
		return false;
	}
	
}

//Drawing the class schedule

var canvas;
var ctx;
var intervalID;
var cx;
var cy;
var days = ["Times", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
var times= ["8:00","9:00", "10:00", "11:00", "12:00", "1:00", "2:00", "3:00", "4:00", "5:00", "6:00", "7:00", "8:00", "9:00", "10:00", "11:00"];
var colors = ["red", "green", "purple", "#890f01", "blue", "#e988a1", "orange", "#8ec127"];
var w= 600, h = 515;

//Constant variables
var COLUMNS = 6;
var PADDING = 10;
var TABLE_HEIGHT = h - PADDING;
var TABLE_WIDTH = w - PADDING;
var COLUMN_WIDTH = (w- (PADDING*2))/ COLUMNS;
var ROW_HEIGHT = 45;
var MIN = 60;

//variables for drawing meeting
var pixelPerMin = .75;
var totalPixels;
var meetingTime;
var hours, minutes;
var totalStartMin, totalEndMin;
var meetingX, meetingY;

var colorIndex = 0;
var currentName = "";

var meetings = [];
var array = [];

//variables for determining dynamic schedule
var rows = 11;
var latestTime = 0;

function getSchedule(){
	canvas = document.getElementById("schedule");
	ctx = canvas.getContext("2d");
	draw();
}


//Method draws a single meeting object
function drawMeeting(){
	var tempStart;
	var tempEnd;
	
	if (meetings.length > 0){
		currentName = meetings[0].mtgString;
	}
	for (i=0; i < meetings.length; i++){
		//calculates the total course time, finds the x and y to place it in the canvas,
		//and draws a rectangle with the class name representing a class meeting.
		
		if (currentName != meetings[i].mtgString){
			currentName = meetings[i].mtgString;
			if (colorIndex < 7){
				colorIndex++;
			}else{
				colorIndex = 0;
			}
			
		}
		
		hours = Math.floor(meetings[i].startTime/100)* MIN;
		minutes = meetings[i].startTime % 100;
		totalStartMin = hours + minutes;
		hours = Math.floor(meetings[i].endTime/100)* MIN;
		minutes = meetings[i].endTime % 100;
		totalEndMin = hours + minutes;
		meetingTime = totalEndMin - totalStartMin;
		totalPixels = meetingTime * pixelPerMin;
		
		if (meetings[i].day == "M"){
			meetingX = PADDING + COLUMN_WIDTH;
		}else if(meetings[i].day == "T"){
			meetingX = PADDING + COLUMN_WIDTH *2;
		}else if(meetings[i].day == "W"){
			meetingX = PADDING + COLUMN_WIDTH *3;
		}else if(meetings[i].day == "R"){
			meetingX = PADDING + COLUMN_WIDTH *4;
		}else{
			meetingX = PADDING +COLUMN_WIDTH *5;
		}
		
		meetingY = (((totalStartMin/60)- 7) * ROW_HEIGHT) + PADDING;
		ctx.fillStyle = colors[colorIndex];
		ctx.fillRect(meetingX,meetingY,COLUMN_WIDTH,totalPixels);
		ctx.fillStyle = "white";
		ctx.font = 10 + "pt Arial";
		ctx.fillText(meetings[i].mtgString, meetingX + 15, meetingY + 15);
		if(meetings[i].startTime>1259){
			tempStart = meetings[i].startTime - 1200;
		} else {
			tempStart = meetings[i].startTime;
		}
		if(meetings[i].endTime>1259){
			tempEnd = meetings[i].endTime - 1200;
		} else {
			tempEnd = meetings[i].endTime;
		}
		ctx.fillText(tempStart + " - " + tempEnd, meetingX + 15, meetingY + 33);
	}
}

function getTableHeight(){
	
	
	if (meetings.length > 0){
		for (i = 0; i < meetings.length;i++){
			//Compare the end time to the latest time and reassign it if it is later.
			if (meetings[i].endTime > latestTime){
				latestTime = meetings[i].endTime;
			}
		}
	}
	console.log(latestTime);
	
	rows = Math.floor(latestTime/100 - 6);
	
	if (rows < 12){
		rows = 12;
	}
	console.log(rows);
	h = PADDING * 2 + ROW_HEIGHT * rows;
	TABLE_HEIGHT = h - PADDING;
	console.log(h + " " + TABLE_HEIGHT);
}

function background(){
	ctx.fillStyle = "black";
	ctx.beginPath();
	ctx.rect(0,0,w,h);
	ctx.closePath();
	ctx.fill();
}

function clear(){
	ctx.clearRect(0,0,w,h);
}

function table(){
	ctx.strokeStyle = "white";
	ctx.fillStyle = "white";
	ctx.font = 12 + "pt Arial";
	ctx.lineWidth = 2;
	ctx.strokeRect(PADDING,PADDING,w-20,h - 20);
	cx = PADDING + COLUMN_WIDTH;
	for (i= 0; i < COLUMNS; i++){
		//Draws a column with the corresponding text.
		ctx.beginPath();
		ctx.moveTo(cx, 10);
		ctx.lineTo(cx, TABLE_HEIGHT);
		ctx.stroke();
		ctx.fillText(days[i], cx - COLUMN_WIDTH + 8, 35);
		cx = cx + COLUMN_WIDTH; 
	}
	cy = PADDING + ROW_HEIGHT;
	for (i = 0; i < rows; i++){
		//Draws a row with the corresponding text
		ctx.beginPath();
		ctx.moveTo(PADDING, cy);
		ctx.lineTo(TABLE_WIDTH, cy);
		ctx.stroke();
		if (i < rows-1){
			ctx.fillText(times[i], 35, cy + 20);
		}
		cy = cy + ROW_HEIGHT;
	}
}

function draw(){
	findMeeting();
	getTableHeight();
	clear();
	background();
	table();
	drawMeeting();
}

function hideCourseSelect(){
	var course=document.getElementById("courseSelects");
	course.style.display="none";
	var section=document.getElementById("sectionSelects");
	section.style.display="none";
}

function hideSections(){
	var section=document.getElementById("sectionSelects");
	section.style.display="none";
}