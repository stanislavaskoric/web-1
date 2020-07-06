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
                $('#forAllUsers').show();
                $('#addCards').hide();
                if(loginUser.role === "ADMIN"){
                	$('#adminSidebar').show();
                	$('#forAllUsers').show();
                	loadApartmentAdAdmin();
                }
                if(loginUser.role === "HOST"){
                	hostUsername=loginUser.username;
                	$('#hostSidebar').show();
                	$('#forAllUsers').show();
                	loadApartmentAdAdmin();
                }
                if(loginUser.role === "GUEST"){
                	guestUsername=loginUser.username;
                	$('#guestSidebar').show();
                	$('#forAllUsers').show();
                	loadApartmentAdAdmin();
                }
            }
        }
    });	


	/* BAR SA STRANE */	
	$('#li_adminProfil').on("click",function(){
		console.log("KLIK NA ADMIN PROFIL");
		$('#adminProfil').show();
		$('#dsearchUsers').hide();
		$('#adminAmentiesTable').hide();
		$('#modHoliday').hide();
		$('#myAddApartmentModal').hide();
		$('#map').hide();
		$('#infoAddApartment').hide();
		$('#addCards').hide();
		$('#reservationViewAdmin').hide();
		$('#reservationViewHost').hide();
		$('#guestViewHost').hide();
		$('#dsearchUsersHOST').hide();
		$('#apartmentDetails').hide();
		$('#commentViewAdmin').hide();
		$('#commentViewHost').hide();
		 $('#commentViewGuest').hide();
		changeAdmin();
		
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
		writeAdmin(loginUser);
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
					  loginUser.password = changePassword;
					  changeUserInfo(loginUser);
				  }
			  }
		  }
		  
	  });
	
	
	/*************************DETALJI O APARTMANU***************************************/
	var choosen_apartmentID;
	$('#tabela_pocetna').on("click", "h4", function(){
		console.log('Kliknuto na ' + $(this).attr('id'));
		choosen_apartmentID = $(this).attr('id');
		$('#addCards').hide();
		$('#apartmentDetails').attr("name",choosen_apartmentID);
		$('#apartmentDetails').show();
		if(loginUser !== undefined){
			showApartmentDetails(choosen_apartmentID,loginUser.role);	
		}else{
			showApartmentDetails(choosen_apartmentID,"");
		}
		
	});
	
	
	/***********************KREEIRAJ REZERVACIJU*******************************************/
	$('#addReservationButton').on("click", function(){
		  console.log("kliknuto na dodavanje rezervacije");
		  cleanReservationModal();
		  datesForReservation(choosen_apartmentID);
		  var modal = document.getElementById("modCreateReservation");
		  modal.style.display = "block";
		  // Get the <span> element that closes the modal
		  var span = document.getElementsByClassName("closeCreateReservation")[0];
		  span.onclick = function() {
			 modal.style.display = "none";
		  }
		
	});
	
	
	/*********************POTVRDI REZERVACIJU*************************************/
	$('#submitReservation').on("click",function(){
		 console.log("kliknuto na dodaj rezervaciju");
		 var dateR = $('#datapicker').val();
		 var nightsR = $('#resNumber').val();
		 var mess = $('#messageForHost').val();
		 var isEmpty = false;
		 if(dateR === ""){
			 $('#errReservationStartDate').text("Izaberite datum pocetka boravka");
			 isEmpty = true;
		 }
		 if(nightsR === ""){
			 $('#errReservationNumber').text("Unesite broj nocenja");
			 isEmpty = true;
		 }
		 
		 guestUsername = "guest1";   ////////////////////////////OVO PROMENIIIIIIIIIIIIIIIIII
		 if(!isEmpty){
			 var obj = {
					 "apartment" : choosen_apartmentID,
			         "startDate" : dateR,
			         "nightsNumber" : nightsR,
			         "message" : mess,
			         "guest" : guestUsername
			 }
			 console.log(obj);
			 
			 $.ajax({
		         type: 'POST',
		         url: 'rest/reservation/',
		         data: JSON.stringify(obj),
		         contentType : 'application/json',
		         success : function(){
		            alert("Uspesno kreirana rezervacija");
		            $('#modCreateReservation').hide();
		         },
		         error : function(message){
	                 alert(message.responseText);
	             }
		   });
		 }
	});
	
	/**************PREGLED KOMENTARA-GOST/NEREGISTROVANI********************/
	$('#viewCommentsButton').on("click", function(){
		  console.log("komentari na apartman");
		  var modal = document.getElementById("commentViewGuest");
		  modal.style.display = "block";
		  // Get the <span> element that closes the modal
		  var span = document.getElementsByClassName("closeComment")[0];
		  span.onclick = function() {
			 modal.style.display = "none";
		  }
		  getCommentsAboutAp(choosen_apartmentID);
	});
	
	$('#viewCommentsInfo').on("click", function(){
		  console.log("komentari na apartman");
		  var modal = document.getElementById("commentViewGuest");
		  modal.style.display = "block";
		  // Get the <span> element that closes the modal
		  var span = document.getElementsByClassName("closeComment")[0];
		  span.onclick = function() {
			 modal.style.display = "none";
		  }
		  getCommentsAboutAp(choosen_apartmentID);
	});
	
	
	/**********************SACUVAJ KOMENTAR***************************/
	$('#submitComment').on("click", function(){
		 console.log("klik na cuvanje komentara ");
		 var isEmpty = false;
		 var ocena = $('#evaluation').val();
		 var text = $('#commentText').val();
		 if(ocena === ""){
			 $('#errEvaluation').text('Morate dodeliti ocenu apartmanu');
			 isEmpty = true;
		 }
		 if(ocena<1 || ocena>10){
			 $('#errEvaluation').text('Ocena mora biti u opsegu 1-10');
			 isEmpty = true;
		 }
		 if(!isEmpty){
			 var comment = { "apartment" : choosen_apartmentID,
						      "text" : text,
						      "evaluation" : ocena
					        };
			 saveComment(comment);
		 }
		 
	});
	
	/**********PRETRAGA APARTMANA*********************************/
	//pretraga apartmana
	$('#li_searchApartment').on("click",function(){
		cleanSerach();
		$('#apartmentDetails').hide();
		$('#addCards').show();
		$('#mod_pretraga').show();
		$('#adminProfil').hide();
		$('#dsearchUsers').hide();
		$('#adminAmentiesTable').hide();
		$('#modHoliday').hide();
		$('#myAddApartmentModal').hide();
		$('#map').hide();
		$('#infoAddApartment').hide();
		$('#reservationViewAdmin').hide();
		$('#reservationViewHost').hide();
		$('#guestViewHost').hide();
		$('#dsearchUsersHOST').hide();
		$('#apartmentDetails').hide();
		$('#commentViewAdmin').hide();
		$('#commentViewHost').hide();
		 $('#commentViewGuest').hide();
		var modal = document.getElementById("mod_pretraga");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("closeSearch")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}
	});
	
	
	$('#pretraziApartmaneButton').on("click", function(){
		  console.log("klik na pretragu apartmana");
		  searchAps();
	});
	
	
	
	
});


function saveComment(comment){
	 $.ajax({
        type: 'POST',
        url: 'rest/comment/add',
        data: JSON.stringify(comment),
        contentType : 'application/json',
        success : function(){
          alert("Uspesno poslat komentar");
          $('#modCreateComment').hide();
        },
        error : function(message){
	        	alert(message.responseText);
	        	$('#modCreateComment').hide();
        }
    });	
}


/**********pregled komentara************************/
function addCommentIntoTableGost(comment){	
	var text = $('<td>'+comment.text+'</td>');
	var ocena = $('<td>'+comment.evaluation+'</td>');
	var gost = $('<td>'+comment.guest+'<td>');
	var tr = $('<tr></tr>');
	tr.append(text).append(ocena);
	$('#gost_komentari tbody').append(tr);
}

function getCommentsAboutAp(id){
	 console.log("komentari gost");
	 $.ajax({
	        type : "get",
	        url : "rest/comment/guest/?id="+id,
	        contentType : "application/json",
	        success : function(response){
	            $('#gost_komentari tbody').empty();
	            console.log(response);
	            for(var k of response){
	                addCommentIntoTableGost(k);
	            }   
	        }
	    });
}

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
			   '<div class="card">'+
			   '<img class="card-img-top" alt="Card image" style="width:300px;height:200px" src='+ apartment.img + '>'+
			   '<div class="card-body">'+
			   '<h4 class="card-title" id="'+  apartment.id +'">'+ apartment.address + '</h4>'+
			   '<p class="card-text">'+apartment.description+'</p></div></div></div></td>');

	 $("#tabela_pocetna tbody:last-child").append(td);
	    brojacApartmana += 1;
}

function loadApartmentAd(){
	brojacApartmana = 0;
	$.ajax({
        type : "get",
        url : "rest/apartment/",
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){     
               for(var app of response){
            	   addApartmentCards(app);
               }
            }
        }
    });	
}

/***************funkcije za rezarvaciju***********************/
var availableDates;
function datesForReservation(apartment_id){
	var url = "https://code.jquery.com/ui/1.12.1/jquery-ui.js";
	$.getScript( url, function(){
		$.ajax({
	        type : "get",
	        url : "rest/apartment/adates?id="+apartment_id,
	        contentType : "application/json",
	        success : function(response){
	            if(response !== undefined){
	               console.log("dates");
	               console.log(response);
	               availableDates = response;
	               console.log(availableDates);
	               $('#datapicker').datepicker({ beforeShowDay:
	            	      function(dt)
	            	      { 
	            	        return [available(dt), "" ];
	            	      }
	            	   , changeMonth: true, changeYear: false});
	            }
	        }
	    });
		
	});
	
}


function available(date) {
	  dmy = date.getDate() + "-" + (date.getMonth()+1) + "-" + date.getFullYear();
	  if ($.inArray(dmy, availableDates) != -1) {
	     return true;
	  } else {
	    return false;
	  }
}

function cleanReservationModal(){
	$('#datapicker').val("");
	$('#resNumber').val("");
	$('#messageForHost').val("");
	$('#errReservationStartDate').val("");
	$('#errReservationNumber').val("");
}


/********************detalji apartmana************************/
function amentiesFromApartment(listAmenties){
	var brojac = 0; //po 3 stavke u jedan red smestam
	var tr = $('<tr></tr>');
	var td;
	for(var a of listAmenties){
		if(brojac === 3){
			 $('#amentiesApartment tbody').append(tr);
			 tr =  $('<tr></tr>');
			 td = $('<td> • '+a+'</td>');
			 tr.append(td);
			 brojac = 1;
		}else{
			 td = $('<td> • '+a+'</td>');
			 tr.append(td);
			 brojac++;
		}
	}
	
	if(brojac !== 3 ){
		while(brojac !== 3){
			var td = $('<td></td>');
			tr.append(td);
			brojac++;
		}
	}
	
	 $('#amentiesApartment tbody').append(tr);	
	
}

function amentiesForApartment(id){
	$.ajax({
        type : "get",
        url : "rest/apartment/amenties/?id="+id,
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){  
               amentiesFromApartment(response);
            }
        }
    });	
}

function updateCarosel(slike){
	var link1 = "images/"+ slike[0];
	var link2 = "images/"+ slike[1];
	var link3 = "images/"+ slike[2];
	$('#slika1').attr("src", link1);
	$('#slika2').attr("src", link2);
	$('#slika3').attr("src", link3);
}

function updateApartment(ap){
	var tip = $('#app_tip');
	tip.append('<p>'+ap.type+'</p>');
	var adres = $('#app_address');
	adres.append('<p>'+ap.location.address+'</p>');
	var sobe = $('#app_rooms');
	sobe.append('<p>'+ap.roomsNumber+'</p>');
	var gosti = $('#app_g');
	gosti.append('<p>'+ap.guestsNumber+'</p>');
	var cena = $('#app_price');
	cena.append('<p>'+ap.price+'</p>');
	updateCarosel(ap.images);
	amentiesForApartment(ap.id);
}


function showApartmentDetails(id, loginUser){
	if(loginUser === "ADMIN"){
		$('#adminHostDetails').show();
		$('#unkonownDetails').hide();
		$('#guestDetails').hide();
	}else if(loginUser === "HOST"){
		$('#adminHostDetails').show();
		$('#unkonownDetails').hide();
		$('#guestDetails').hide();
	}else if(loginUser === "GUEST"){
		$('#adminHostDetails').hide();
		$('#unkonownDetails').hide();
		$('#guestDetails').show();
	}
	else{
		$('#adminHostDetails').hide();
		$('#guestDetails').hide();
		$('#unkonownDetails').show();
	}
	$.ajax({
        type : "get",
        url : "rest/apartment/details/?id="+id,
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){  
               $('#tabela_detaljiAp tbody td').empty();
               $('#amentiesApartment tbody').empty();
               updateApartment(response);
            }
        }
    });	
}

/***funkcije za pretragu i sortiranje*****/
function cleanSerach(){
	 $('#datumOd').val('');
	 $('#datumDo').val('');
	 $('#cenaOd').val('');
	 $('#cenaDo').val('');
	 $('#brojsobaOd').val('');
	 $('#brojsobaDo').val('');
	 $('#brojOsoba').val('');
	 $('#errCenaP').text('');
	 $('#errBrojSoba').text('');
	 $('#errOsoba').text('');
	 $('#errDatum').text('');
	 $('#gradPretraga').val('');
	 $('#tip_sortiranjaPRETRAGA').val('')
}


function searchAps(){
	console.log('SerachAps');
	 var dO = $('#datumOd').val();
	  var dD = $('#datumDo').val();
	  var cO = $('#cenaOd').val();
	  var cD = $('#cenaDo').val();
	  var sO = $('#brojsobaOd').val();
	  var sD = $('#brojsobaDo').val();
	  var bO = $('#brojOsoba').val();
	  var c = $('#gradPretraga').val();
	  var dr = $('#drzavaPretraga').val();
	  var sort = $('#tip_sortiranjaPRETRAGA').val();
	  console.log(sort);
	  var isOk = true;
	  if((dO !== "" && dD === "") || (dO === "" && dD !== "")){
			 $('#errDatum').text('Morate uneti oba datuma');
			 isOk = false;
		 }else{
			 if(new Date(dD) < new Date(dO)){
				 $('#errDatum').text('Opseg datuma nije pravilan');
				 isOk = false;
			 }
		 }
	  if(cO<0 || cD<0){
		  $('#errCenaP').text('Cena mora biti pozitivan broj');
		  isOk = false;
	  }
	  if(sO<0 || sD<0){
		  $('#errBrojSoba').text('Broj soba mora biti pozitivan broj');
		  isOk = false;
	  }
	  if(cO !=="" && cD !==""){
		  if(cO>cD){
		    $('#errCenaP').text('CENA DO mora biti veca od CENE OD');
		    isOk = false;
		  }
	  }
	  if(sO !=="" && sD !==""){
		  if(sO>sD){
			    $('#errBrojSoba').text('BROJ SOBA DO mora biti veci od BROJ SOBA OD');
			    isOk = false;
			  }
	  }
	  if(bO < 0){
		  $('#errOsoba').text('Broj soba mora biti pozitivan broj');
		  isOk = false;
	  }
	  if(isOk){
		  console.log("pretraga na becku");
		  $.ajax({
		        type : "get",
		        url : "rest/apartment/search/?start="+dO+"&end="+dD+"&cO="+cO+"&cD="+cD
		               +"&sO="+sO+"&sD="+sD+"&o="+bO+"&c="+c+"&dr="+dr+"&sort="+sort,
		        contentType : "application/json",
		        success : function(response){
		        	$('#mod_pretraga').hide();
		        	$('#addCards').show();
		            $('#tabela_pocetna tbody').empty();
		            brojacApartmana = 0;
		            console.log(response);
		            for(var k of response){
		            	 addApartmentCards(k);
		            }   
		        }
		    });
        }
}
	  	

