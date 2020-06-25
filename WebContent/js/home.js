$(document).ready(function(){
	
	$('#addCards').show();
	$('#unknownUser').show();
	loadApartmentAd();         //ucitavam apartmane za prikaz
	
    
	var loginUser;           //trenutno ulogovani korisnik
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
                $('#addCards').hide();
                $('#forAllUsers').show();
                if(loginUser.role === "ADMIN"){
                	$('#adminSidebar').show();
                	$('#unknownUser').hide();
                }
                if(loginUser.role === "HOST"){
                	console.log("host usla")
                	$('#hostSidebar').show();
                	$('#unknownUser').hide();
                }
            }
        }
    });	

		



/****************************************************** LOGOUT USERA **************************************************/ 
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


/***************************************** IZMENA PROFILA SVIH KORISNIKA *********************************************/
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
	
	

	$('#button_changeAdmin').on("click",function(){  //funkcija koja cuva izmenu polja profila korisnika
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

/*************funkcije za profil info****************/
function writeAdmin(admin){ //ispisem podatke admina u tabelu
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


/********************funkcije za prikaz kartica apartmana*********************/
var brojacApartmana = 0;

function addApartmentCards(apartment){
	if((brojacApartmana % 3) === 0){        // u red idu 3 oglasa
        $('#tabela_pocetna tbody').append($('<tr></tr>'));
    }
	//dodati jos negde id
	var td = $('<td style="padding: 10px">'+
			   '<div class="container" style=" width:300px">'+
			   '<div class="card" id="'+  apartment.id +'">'+
			   '<img class="card-img-top" alt="Card image" style="width:100%" src='+ apartment.img + '>'+
			   '<div class="card-body">'+
			   '<h4 class="card-title">'+ apartment.address + '</h4>'+
			   '<p class="card-text">'+apartment.description+'</p></div></div></div></td>');

	 $("#tabela_pocetna tbody:last-child").append(td);
	    brojacApartmana += 1;
}

function loadApartmentAd(){
	
	$.ajax({
        type : "get",
        url : "rest/apartment/",
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){     
               console.log(response)
               for(var app of response){
            	   console.log(app);
            	   addApartmentCards(app);
               }
            }
        }
    });	
}











