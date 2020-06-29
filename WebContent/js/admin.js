$(document).ready(function(){
		
/**********KREIRANJE PRAZNIKA****************************/	
	
	$('#li_holidayDays').on("click",function(){
		clearHoliday();
		$('#modHoliday').show();
		$('#dsearchUsers').hide();
		$('#adminProfil').hide();
		$('#adminAmentiesTable').hide();
		var modal = document.getElementById("modHoliday");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("closeHoliday")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}
	});

	
	
	$('#holidayButton').on("click",function(){
		var isOk = true;
		var date = $('#holidayDate').val();
		if(date === ""){
			$('#errHolidayDate').text("Morate izabrati datum");
			isOk = false;
		}
		var name = $('#holidayName').val();
		if(name === ""){
			$('#errHolidayName').text("Morate uneti ime praznika");
			isOk = false;
		}
		if(isOk){
			 $.ajax({
			        type : "post",
			        url : "rest/codebook/?date="+date+"&desc="+name,
			        contentType : "application/json",
			        success : function(){
			        	$('#modHoliday').hide();
			        	alert("Uspesno ste dodali praznik");
			        },
			        error : function(message){
			        	alert(message.responseText);
		            }
			  });
		}
		
	});
	
	
	
	
	
/*****PRETRAGA KORISNIKA*****/
		
$('#searchButton').on("click",function(){
	
	var username = $('#searchUsername').val();
	var gender = $('#searchGender').val();
	var role = $('#searchRole').val();
	
	 $.ajax({
	        type : "get",
	        url : "rest/searchUsers?username="+username+"&gender="+gender+"&role="+role,
	        contentType : "application/json",
	        success : function(response){
	            $('#tabela_korisnika tbody').empty();
	            console.log(response);
	            for(var k of response){
	                console.log(k.username);
	                addUserIntoTable(k);
	            }   
	        }
	    });
	
});



/*****ODRZAVANJE SADRZAJA APARTMANA*****/

$('#li_adminAmenties').on("click",function(){
	console.log("KLIK NA PRIKAZ LISTE SADRZAJA");
	$('#dsearchUsers').hide();
	$('#adminProfil').hide();
	$('#adminAmentiesTable').show();
	getAmentiesFromFile();
});


 $('#button_addAmenties').on("click",function(){
	    $('#inputAddAmenites').text("");
	    $('#addAmentiesError').text("");
	    var modal = document.getElementById("myAmentiesModal");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("closeAmenties")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}	  
 });
 
 $('#button_saveNewAmenties').on("click",function(){
	   var name =  $('#inputAddAmenites').val();
	   if(name === ""){
		   $('#addAmentiesError').text("Polje morate popuniti!");
	   }else{
		   $.ajax({
		         type: 'POST',
		         url: 'rest/amenties/?name='+name,
		         success : function(){
		             $('#myAmentiesModal').hide();
		             alert('Uspesno sacuvana stavka!');
		             getAmentiesFromFile();
		         },
		         error : function(message){
		        	  $('#addAmentiesError').text(message.responseText);
	             }
		   });
	   }
 });

 
 $('#button_changeAmenties').on("click",function(){
	    $('#inputChangeAmenites').text("");
	    $('#changeAmentiesError').text("");
	    var modal = document.getElementById("myChangeAmentiesModal");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("closeChangeAmenties")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}	  
});

$('#button_changeNewAmenties').on("click",function(){
	   var oldName =  $('#inputChangeAmenites').val();
	   var newName = $('#inputChangeNewAmenites').val();
	   if(oldName === "" || newName === ""){
		   $('#changeAmentiesError').text("Oba polja morate popuniti!");
	   }else{
		   $.ajax({
		         type: 'PUT',
		         url: 'rest/amenties/?oldName='+oldName+'&newName='+newName,
		         success : function(){
		             $('#myChangeAmentiesModal').hide();
		             alert('Uspesno izmenjena stavka!');
		             getAmentiesFromFile();
		         },
		         error : function(message){
		        	  $('#changeAmentiesError').text(message.responseText);
	             }
		   });
	   }
});
 


$('#button_deleteAmenties').on("click",function(){
    $('#inputDeleteAmenites').text("");
    $('#deleteAmentiesError').text("");
    var modal = document.getElementById("myDeleteAmentiesModal");
	modal.style.display = "block";
	// Get the <span> element that closes the modal
	var span = document.getElementsByClassName("closeDeleteAmenties")[0];
	span.onclick = function() {
		  modal.style.display = "none";
	}	  
});

$('#button_deleteNewAmenties').on("click",function(){
   var name =  $('#inputDeleteAmenites').val();
   if(name === ""){
	   $('#deleteAmentiesError').text("Polje morate popuniti!");
   }else{
	   $.ajax({
	         type: 'DELETE',
	         url: 'rest/amenties/?name='+name,
	         success : function(){
	             $('#myDeleteAmentiesModal').hide();
	             alert('Uspesno obrisana stavka!');
	             getAmentiesFromFile();
	         },
	         error : function(message){
	        	  $('#deleteAmentiesError').text(message.responseText);
             }
	   });
   }
});




});


/*******PRAZNIK-CISCENJE DIJALOGA*******/
function clearHoliday(){
	 $('#holidayDate').val("");
	 $('#errHolidayDate').val("");
	 $('#holidayName').val("");
	 $('#errHolidayName').val("");
}



function addUserIntoTable(user){	
	var username = $('<td>'+user.username+'</td>');
	var firstName = $('<td>'+user.firstName+'</td>');
	var lastName = $('<td>'+user.lastName+'</td>');
	var gender = $('<td>'+user.gender+'<td>');
	var role = $('<td>'+user.role+'<td>');
	var tr = $('<tr></tr>');
	tr.append(username).append(firstName).append(lastName).append(gender).append(role);
	$('#tabela_korisnika tbody').append(tr);
}	


function getAmentiesFromFile(){
	 $.ajax({
	        type : "get",
	        url : "rest/amenties/",
	        contentType : "application/json",
	        success : function(response){
	            $('#amentiesTable tbody').empty();
	            console.log(response);
	            addAmentiesIntoTable(response); 
	        }
	    });	
}


function addAmentiesIntoTable(listAmenties){	
	var brojac = 0; //po 4 stavke u jedan red smestam
	var tr = $('<tr></tr>');
	var td;
	for(var a of listAmenties){
		console.log(a);
		if(brojac === 4){
			 $('#amentiesTable tbody').append(tr);
			 tr =  $('<tr></tr>');
			 td = $('<td> • '+a.name+'</td>');
			 tr.append(td);
			 brojac = 1;
			 console.log(brojac);
		}else{
			 console.log("ovde sam u elsy")
			 td = $('<td> • '+a.name+'</td>');
			 tr.append(td);
			 brojac++;
		}
	}
	
	if(brojac !== 4 ){
		while(brojac !== 4){
			var td = $('<td></td>');
			tr.append(td);
			brojac++;
		}
	}
	
	 $('#amentiesTable tbody').append(tr);	
	
}