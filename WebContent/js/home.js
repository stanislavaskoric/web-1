$(document).ready(function(){
 //provera ko je ulogovan
	var loginUser;
	$.ajax({
        type : "get",
        url : "rest/getActive",
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){     
                loginUser = response;
                console.log("ulogovan");
                console.log(loginUser);
                $('#li_logout').show();
                $('#li_login').hide();
                $('#li_signup').hide();
            }
        }
    });	
		
/* BAR SA STRANE */	
$('#li_adminProfil').on("click",function(){
	console.log("KLIK NA ADMIN PROFIL");
	$('#adminProfil').show();
	$('#dsearchUsers').hide();
	changeAdmin();
	
});


$('#li_search').on("click",function(){
	$('#dsearchUsers').show();
	$('#adminProfil').hide();
});


/* LOGOUT USERA */ 
$('#li_logout').on("click",function(){
	$.ajax({
        type : "get",
        url : "rest/logout",
        contentType : "application/json",
        success : function(){
        	 loginUser = undefined;
        	 $('#li_logout').hide();
             $('#li_login').show();
             $('#li_signup').show();
        }
    });
});


/* IZMENA PROFILA ADMINA */
    var change_property; //koje polje na profilu menjam

	function changeAdmin(){
		console.log("change");
		writeAdmin(loginUser)
	}
	
	$('#admin_change1').on("click",function(){   //na klik ime, iskace modalni za izmenu imena
		console.log("change admin name");
		change_property = "firstName";
		$('#firstLastBody').show();
		$('#passwordBody').hide();
		$('#modalChangeErr').text("");
		$('#inputThChange').text("Unesi novo ime:");
		var modal = document.getElementById("myAdminModal");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("close")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}
		
	});
	
	$('#admin_change2').on("click",function(){   //na klik ime, iskace modalni za izmenu prezimena
		console.log("change admin last name");
		change_property = "lastName";
		$('#firstLastBody').show();
		$('#passwordBody').hide();
		$('#modalChangeErr').text("");
		$('#inputThChange').text("Unesi novo prezime:");
		var modal = document.getElementById("myAdminModal");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("close")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}
		
	});
	
	$('#admin_change3').on("click",function(){   //na klik ime, iskace modalni za izmenu passworda
		console.log("change admin pass");		
		change_property = "password";
		$('#modalChangeErr2').text("");
		$('#firstLastBody').hide();
		$('#passwordBody').show();
		var modal = document.getElementById("myAdminModal");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("close")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}
		
	});
	
	

	$('#button_changeAdmin').on("click",function(){  //funkcija koja cuva izmenu nekog polja profila korisnika
		 if(change_property === "firstName"){
			 var change = $('#modalChangeAdmin').val();
			 if(change === ""){ 
				 $('#modalChangeErr').text("Polje morate popuniti!");
		     }else{
		    	 loginUser.firstName = change;
		    	 changeUserInfo(loginUser);
		     }
		  }else if(change_property === "lastName"){
			  var change = $('#modalChangeAdmin').val();
			  if(change === ""){ 
				 $('#modalChangeErr').text("Polje morate popuniti!");
			  }else{
				  loginUser.lastName = change;
			      changeUserInfo(loginUser);
			  }
		  }else{
			  var changePassword = $('#modalChangePassword').val();
			  var changePasswordAgain = $('#modalChangePassword2').val();
			  if(changePassword === "" || changePasswordAgain === ""){
				  $('#modalChangeErr2').text("Polja morate popuniti!");
			  }
			  if (changePassword !== changePasswordAgain){
				  $('#modalChangeErr2').text("Lozinke se moraju podudarati!");
			  }
			  if(changePassword === changePasswordAgain){
				  if(changePassword === "" ){
					  $('#modalChangeErr2').text("Polja morate popuniti!");
				  }else{
					  console.log("ovde sam peko");
					  loginUser.password = changePassword;
					  changeUserInfo(loginUser);
				  }
			  }
		  }
		  
	  });
	
	
	
	
});


function writeAdmin(admin){ //ispisem podatke admina
	console.log("write");
	console.log(admin);
	var username = $('#admin_username');
	username.empty();  //brisem prethodni sadrzaj ukoliko je postojao
	username.text(admin.username);
	var firstName = $('#admin_firstname');
	firstName.empty();
	firstName.append('<p>'+admin.firstName+'</p>');
	var lastName = $('#admin_lastname');
	lastName.empty();
	lastName.append('<p>'+admin.lastName+'</p>');
	var password = $('#admin_password');
	password.empty();
	password.append('<p>'+admin.password+'</p>');
}


function changeUserInfo(admin){
	 console.log("salji promenu");
	 writeAdmin(admin);
 	 $.ajax({
         type: 'PUT',
         url: 'rest/',
         data: JSON.stringify(admin),
         contentType : 'application/json',
         success : function(){
             $('#myAdminModal').hide();
             $('#myPasswordModal').hide();
             $('#modalChangeAdmin').text("");
             $('#modalChangePassword').text("");
             $('#modalChangePassword2').text("");
             $('#modalChangeErr').text("");
             $('#modalChangeErr2').text("");
             alert('Uspesno izmenjeni podaci!');
         }
   });
}