<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
	<title>Student View</title>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
<!--===============================================================================================-->	
	<link rel="icon" type="image/png" href="images/icons/favicon.ico"/>
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/bootstrap/css/bootstrap.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="fonts/font-awesome-4.7.0/css/font-awesome.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animate/animate.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="vendor/css-hamburgers/hamburgers.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/animsition/css/animsition.min.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="vendor/select2/select2.min.css">
<!--===============================================================================================-->	
	<link rel="stylesheet" type="text/css" href="vendor/daterangepicker/daterangepicker.css">
<!--===============================================================================================-->
	<link rel="stylesheet" type="text/css" href="css/util.css">
	<link rel="stylesheet" type="text/css" href="css/main.css">
	<link rel="stylesheet" type="text/css" href="css/student_view.css">
	
<!--===============================================================================================-->
</head>
<body>
	
	<div class="limiter">
		<div class="container-login100 background_style">
			<div class="wrap-login100-V2">

		
		
				<form class="login100-form validate-form p-l-55 p-r-55 p-t-178">
					<span class="login100-form-title">
						Internships
					</span>
			
				<div class="wrap-input100 validate-input m-b-16" data-validate = "Filter on the programs">
					<select name="programs" id="programs" class="input100">
				    	<option value="0">All Programs</option>
					</select>
				</div>	
					
				<div class="container" id="list">
					  
				</div>
					
					
					<script>
		//Loading the data
		var programs_categories = new Map();
		var program_name_id = new Map();
		var categories_to_subjects = new Map();
		
		<c:forEach items="${programs}" var="program">
			var program_categories = [];
		    <c:forEach items="${program.getCategories()}" var="category">
			    program_categories.push({
			   		 key: "${category.getId()}",
			   		 value: "${category.getName()}"
			   	 });
		    </c:forEach>
		    program_name_id.set("${program.getId()}", "${program.getName()}");
		    programs_categories.set("${program.getId()}", program_categories);
		</c:forEach>
		
		<c:forEach items="${subjectsPerCategory}" var="categoryAndSubjects">
			var subjects = [];
			<c:forEach items="${categoryAndSubjects.getSubjects()}" var="subject">
			 	subjects.push({title: "${subject.getTitle()}", id: "${subject.getId()}", supervisorEmail: "${subject.getSupervisorEmail()}", supervisorName: "${subject.getSupervisorName()}"})
			</c:forEach>
			var categoryId = "${categoryAndSubjects.getCategoryId()}";
			var programId = "${categoryAndSubjects.getProgramId()}";
			
			if(categories_to_subjects.has(programId)) {
				categories_to_subjects.get(programId).set(categoryId, subjects);
			} else {
				var new_categories_subjects = new Map();
				new_categories_subjects.set(categoryId, subjects);
				categories_to_subjects.set(programId, new_categories_subjects);
			}
		</c:forEach>
		
		
		window.onload = function() {
			showAllPrograms();
			
			var programSel = document.getElementById("programs");
			programs_categories.forEach((value, key) => {
				programSel.options[programSel.options.length] = new Option(program_name_id.get(key), key);
			});
			programSel.onchange = function(){
				var pId = this.value
				if(pId==="0") {
					//Print all progams with their categories
					showAllPrograms();
				} else {
					//Print certain program (based on pId)
					showProgram(pId)
				}
			}
			
		}
		
		function showAllPrograms() {
			var programList = document.getElementById('list');
			programList.innerHTML = '';
			
			programs_categories.forEach((v, k) => {
				programList.innerHTML += '<div class="program", id="'+k+'">'; 
				programList.innerHTML += '<div class="container-login100-form-btn-V2  p-t-50 p-b-25 p-l-250 p-r-250">'+
											'<h2 class="login100-form-btn-V2 p-l-5 p-r-5">' + program_name_id.get(k) + '</h2></div>';
				
				
				for (const category of v) {
					
					var subjects = categories_to_subjects.get(k).get(category.key);
					if(subjects.length>0) {
						
						programList.innerHTML += '<div class="category", id="'+category.key+'">';					
						programList.innerHTML += '<div class="container-login100-form-btn-V3  p-t-50 p-b-25 p-r-250">'+
						'<h2 class="login100-form-btn-V3 p-l-5 p-r-5">' + category.value + '</h2></div>';
						
						//New table for each category
						programList.innerHTML += '<ul class="responsive-table" id="'+k.concat(category.key)+'pctable">';
						
						var newRow = document.getElementById(k.concat(category.key).concat("pctable"));
						
						
						newRow.innerHTML += '<li class="table-header"><div class="col col-1"> Id </div><div class="col col-2">Subject Title</div><div class="col col-3">Supervisor Name</div><div class="col col-4">Supervisor Email</div><div class="col col-5">Subject</div></li>';		
						
						for(const subject of subjects) {
							var downloadForm = '<a href="downloadsubject?internshipId='+subject.id+'" target="_blank">Download</a>';
							var newRowE = document.getElementById(k.concat(category.key).concat("pctable"));
							newRowE.innerHTML += '<li class="table-row"><div class="col col-1" data-label="Id">' + subject.id + '</div>'+
															'<div class="col col-2" data-label="Subject Title">'+subject.title+'</div>'+
															'<div class="col col-3" data-label="Supervisor Name">'+subject.supervisorName + '</div>'+
															'<div class="col col-4" data-label="Supervisor Email">'+ subject.supervisorEmail +'</div>'+
															'<div class="col col-5" data-label="Subject">'+downloadForm+'</div></li>';
						}
						programList.innerHTML += '</ul>'; 
					}
					
					programList.innerHTML += '</div>';
				}
				programList.innerHTML += '</div>';
			});
		}
		
		function showProgram(pId) {
			var programList = document.getElementById('list');
			programList.innerHTML = '<div class="program", id="'+pId+'">'; 
			programList.innerHTML += '<div class="container-login100-form-btn-V2  p-t-50 p-b-25 p-l-250 p-r-250">'+
										'<h2 class="login100-form-btn-V2 p-l-5 p-r-5">' + program_name_id.get(pId) + '</h2></div>';
			
			
			for (const category of programs_categories.get(pId)) {
				
				var subjects = categories_to_subjects.get(pId).get(category.key);
				if(subjects.length>0) {
					
					programList.innerHTML += '<div class="category", id="'+category.key+'">';					
					programList.innerHTML += '<div class="container-login100-form-btn-V3  p-t-50 p-b-25 p-r-250">'+
					'<h2 class="login100-form-btn-V3 p-l-5 p-r-5">' + category.value + '</h2></div>';
					
					//New table for each category
					programList.innerHTML += '<ul class="responsive-table" id="'+pId.concat(category.key)+'pctable">';
					
					var newRow = document.getElementById(pId.concat(category.key).concat("pctable"));
					
					
					newRow.innerHTML += '<li class="table-header"><div class="col col-1"> Id </div><div class="col col-2">Subject Title</div><div class="col col-3">Supervisor Name</div><div class="col col-4">Supervisor Email</div><div class="col col-5">Subject</div></li>';		
					
					for(const subject of subjects) {
						var downloadForm = '<a href="downloadsubject?internshipId='+subject.id+'" target="_blank">Download</a>';
						var newRowE = document.getElementById(pId.concat(category.key).concat("pctable"));
						newRowE.innerHTML += '<li class="table-row"><div class="col col-1" data-label="Id">' + subject.id + '</div>'+
														'<div class="col col-2" data-label="Subject Title">'+subject.title+'</div>'+
														'<div class="col col-3" data-label="Supervisor Name">'+subject.supervisorName + '</div>'+
														'<div class="col col-4" data-label="Supervisor Email">'+ subject.supervisorEmail +'</div>'+
														'<div class="col col-5" data-label="Subject">'+downloadForm+'</div></li>';
					}
					programList.innerHTML += '</ul>'; 
				}
				
				programList.innerHTML += '</div>';
			}
			programList.innerHTML += '</div>';
		}
		
		</script>		
					
					
					
					
				</form>
				
			</div>
		</div>
	</div>
	
	
							
	

</body>
</html>