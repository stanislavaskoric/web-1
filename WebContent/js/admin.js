$(document).ready(function(){
	/************FILTRIRANJE/PRETRAGA/SORT REZERVACIJE****************/
	$('#searchResADMIN').on("change", function(){
		console.log("PRETRAGA REZERVACIJAAAAA");
		var searchFilter = $(this).val(); 
		searchReservation(searchFilter);
	});
	
	$('#filterResADMIN').on("change", function(){
		console.log("FILTRIRANJE REZERVACIJA");
		var filter = $(this).val(); 
		filterReservation(filter);
	});
	
    $('#sortResADMIN').on("change", function(){
    	console.log("SORTIRANJE REZERVACIJA");
        var vrsta = $(this).val(); //rastuce ili opadajuce
        sortReservation(vrsta);
	}); 
	
	/*************KREIRANJE DOMACINA************************************/
	$('#li_createHost').on("click", function(){
		console.log("kreiranje domacina");
		clearHost();
		$('#modCreateHost').show();
	    var modal = document.getElementById("modCreateHost");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("closeCreateHost")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}
	});
	
	$('#submitHost').on("click", function(){
		console.log("kreiranje domacina potvrda");
		 let username = $('#emailHost').val();
	     let firstName = $('#imeHost').val();
	     let lastName = $('#prezHost').val();
	     let gender= $('#polHost').val();
	     let password = $('#sifraHost').val();
	     var isOk = true;
	     if(username === ""){
	    	 $('#errEmailHost').text('Morate uneti email');
	    	 isOk=false;
	     }else if(firstName === ""){
	    	 $('#errImeHost').text('Morate uneti ime');
	    	 isOk=false;
	     }else if(lastName === ""){
	    	 $('#errPrezHost').text('Morate uneti prezime');
	    	 isOk=false;
	     }else if(gender === ""){
	    	 $('#errPolHost').text('Morate izabrati pol');
	    	 isOk=false;
	     }else if(password === ""){
	    	 $('#errSifraHost').text('Morate uneti sifru');
	    	 isOk=false;
	     }else{
	    	 isOk=true;
	     }
	     if(isOk){
	    	    var obj = {
	                "username" : username,
	                "password" : password,
	                "firstName" : firstName,
	                "lastName" : lastName,
	                "gender" : gender,
	                "role" : "HOST"
	            }
	            $.ajax({
	                type: 'POST',
	                url: 'rest/createh',
	                data: JSON.stringify(obj),
	                contentType : 'application/json',
	                success : function(){
	                    alert('Uspesno ste registrovali domacina!');
	                    $('#modCreateHost').hide();
	                },
	               error : function(message){
	                    alert(message.responseText);
	               }
	            });	    	     	 
           }
	});

		
	
/************PREGLED KOMENTARA*************************/
	$('#li_adminComments').on("click", function(){
		console.log("klik na pregled komentara-admin");
		$('#commentViewAdmin').show();
		$('#modHoliday').hide();
		$('#dsearchUsers').hide();
		$('#adminProfil').hide();
		$('#adminAmentiesTable').hide();
		$('#addCards').hide();
		$('#reservationViewAdmin').hide();
		$('#apartmentDetails').hide();
		getCommentsByAdmin();
	});
	
/****************BRISANJE APARTMANA*********************/
	$('#deleteApartmentInfo').on("click", function(){
		 console.log("brisanje apartmana");
		 var id = $('#apartmentDetails').attr('name');
		 $.ajax({
	         type: 'DELETE',
	         url: 'rest/apartment/?id='+id,
	         success : function(){
	             alert('Uspesno obrisan apartman!');
	             $('#apartmentDetails').hide();
	             loadApartmentAdAdmin();
	         },
	         error : function(message){
	        	  alert(message.responseText);
             }
	   });
	});
	
/******************MODIFIKACIJAA APARTMANA********************/
	$('#changeApartmentInfo').on("click", function(){
		 console.log("modifikacija apartmana");
		 var id = $('#apartmentDetails').attr('name');			
		 popuniModal(id);
		 $('#modChangeApartment').show();
		 var modal = document.getElementById("modChangeApartment");
			modal.style.display = "block";
			// Get the <span> element that closes the modal
			var span = document.getElementsByClassName("closeChangeApartmentInfo")[0];
			span.onclick = function() {
				  modal.style.display = "none";
		}
	});
	
	$('#amentiesIntoApC').on("click", function(){
		console.log("izmena stavki sadrzaja apartmana");
		var id = $('#apartmentDetails').attr('name');			
		 $('#myAddIntoAppModalC').show();
		 var modal = document.getElementById("myAddIntoAppModalC");
			modal.style.display = "block";
			// Get the <span> element that closes the modal
			var span = document.getElementsByClassName("closeAddIntoAppC")[0];
			span.onclick = function() {
				  modal.style.display = "none";
		}
	});
	
	/**izabrala sam novi sadrzaj za apartman***/
	var pickUpAmentiesC = [];
	$('#button_pickUpAmentiesC').on("click",function(){
		 console.log("potvrda izbora za sadrzaj apartmana-izmena");
		 $.each($('input[name^="chkCInv"]:checked'), function(){
             pickUpAmentiesC.push($(this).val());
         });
		 console.log(pickUpAmentiesC);
		 $('#myAddIntoAppModalC').hide();
	});
	
	/***cuvam izmenjen sadrzaj apartmana***/
	$('#changeApartmentButton').on("click",function(){
		console.log("klik na dodavanje izmena info o apartmanu");
		var apartman_id = $('#apartmentDetails').attr('name');	
		var tip_apartmana = $('#tip_apartmanaC').val();
		var broj_soba = $('#numberRoomsC').val();
		var broj_gostiju = $('#numberGuestsC').val();
		var cena = $('#costsPerNightC').val();
		var datumi = $('#DatesC').val();
		var vreme_prijave = $('#timeInC').val();
		var vreme_odjave = $('#timeOutC').val();
		var slike = [];  
		$.map($('.multiupload').get(0).files, function(file) {  //ucitavam sve izabrane slike
			  slike.push(file.name);
	    });
        //validacija forme
		 if(tip_apartmana === "" || broj_soba === "" || broj_gostiju === "" || cena === "" ||
				 datumi === "" || vreme_prijave === "" || vreme_odjave === "" || slike === ""){
			$('#addApartmentErrorC').text("Morate popuniti sva polja!");
		}else{
		    //ok salji na bek
			var appInfo = {
				"id_apartmana" : apartman_id,
				"tip_apartmana" : tip_apartmana,
			   	"broj_soba" : broj_soba,
				"broj_gostiju" : broj_gostiju,
				 "datumi" : datumi,
			     "slike" : slike,
			     "price" : cena,
			     "prijava" : vreme_prijave,
			     "odjava" :  vreme_odjave,
			     "sadrzaj" : pickUpAmentiesC
			}
			console.log("OBJEKAT ZA IZMENU");
			console.log(appInfo);
			 $.ajax({
	                type: 'POST',
	                url: 'rest/apartment/change',
	                data: JSON.stringify(appInfo),
	                contentType : 'application/json',
	                success : function(){
	                  alert("Uspesno izmenjen apartman");
	                  $('#modChangeApartment').hide();
	                  $('#tabela_detaljiAp tbody td').empty();
	                  $('#amentiesApartment tbody').empty();
	                  showApartmentDetails(apartman_id,"ADMIN");
	                }
	            });	
		}	
	});
	
	
	
/***************LISTA REZERVACIJA***********************/
	$('#li_adminReservation').on("click",function(){
		console.log("lista rezervacija-admin");
		$('#modHoliday').hide();
		$('#dsearchUsers').hide();
		$('#adminProfil').hide();
		$('#adminAmentiesTable').hide();
		$('#addCards').hide();
		$('#reservationViewAdmin').show();
		$('#apartmentDetails').hide();
		$('#commentViewAdmin').hide();
		getReservationByAdmin();
	});
	
	
/************LISTA APARTMANA****************************/
	$('#li_adminApartment').on("click",function(){
		console.log("lista apartmana-admin");
		$('#modHoliday').hide();
		$('#dsearchUsers').hide();
		$('#adminProfil').hide();
		$('#adminAmentiesTable').hide();
		$('#reservationViewAdmin').hide();
		$('#apartmentDetails').hide();
		$('#commentViewAdmin').hide();
		loadApartmentAdAdmin();
	});
	
		
/**********KREIRANJE PRAZNIKA****************************/	
	
	$('#li_holidayDays').on("click",function(){
		clearHoliday();
		$('#modHoliday').show();
		$('#dsearchUsers').hide();
		$('#adminProfil').hide();
		$('#adminAmentiesTable').hide();
		$('#reservationViewAdmin').hide();
		$('#addCards').hide();
		$('#apartmentDetails').hide();
		$('#commentViewAdmin').hide();
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
	$('#li_adminSearch').on("click",function(){
		$('#dsearchUsers').show();
		$('#adminProfil').hide();
		$('#adminAmentiesTable').hide();
		$('#modHoliday').hide();
		$('#reservationViewAdmin').hide();
		$('#addCards').hide();
		$('#apartmentDetails').hide();
		$('#commentViewAdmin').hide();
		getUsersByAdmin();
	});
	
		
$('#searchButton').on("click",function(){
	
	var username = $('#searchUsername').val();
	var gender = $('#searchGender').val();
	var role = $('#searchRole').val();
	console.log(username+gender+role+"OVDEEEEEEEE");
	
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
	$('#reservationViewAdmin').hide();
	$('#modHoliday').hide();
	$('#addCards').hide();
	$('#apartmentDetails').hide();
	$('#commentViewAdmin').hide();
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

/**********BLOKIRANJE KORISNIKAA**************/
$('#tabela_korisnika').on('click','a',function(){
	console.log("BLOKIRAAAAAAAAAAAJ");
	var username = $(this).attr('id');
	$.ajax({
	       type: 'GET',
	       url: 'rest/block/?username='+username,
	       contentType : 'application/json',
	       success : function(){
	    	 alert("Blokirali ste korisnika!")
	         $('#tabela_korisnika tbody').empty();
	         getUsersByAdmin();
	       },
		     error : function(message){
		    	 alert(message.responseText);
	       }
	   });
   });

});

/*****pregled komentara************/
function addCommentIntoTableAdmin(comment){	
	var id = $('<td>'+comment.apartment+'</td>');
	var text = $('<td>'+comment.text+'</td>');
	var ocena = $('<td>'+comment.evaluation+'</td>');
	var gost = $('<td>'+comment.guest+'<td>');
	var tr = $('<tr></tr>');
	tr.append(id).append(text).append(ocena).append(gost);
	$('#admin_komentari tbody').append(tr);
}


function getCommentsByAdmin(){
	 console.log("get comments by admin");
	 $.ajax({
	        type : "get",
	        url : "rest/comment",
	        contentType : "application/json",
	        success : function(response){
	            $('#admin_komentari tbody').empty();
	            console.log(response);
	            for(var k of response){
	                console.log(k.username);
	                addCommentIntoTableAdmin(k);
	            }   
	        }
	    });
}



/*****pregled korisnika*********************/
function getUsersByAdmin(){
	 console.log("get users by admin");
	 $.ajax({
	        type : "get",
	        url : "rest/",
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
}


/*******PRAZNIK-CISCENJE DIJALOGA*******/
function clearHoliday(){
	 $('#holidayDate').val("");
	 $('#errHolidayDate').val("");
	 $('#holidayName').val("");
	 $('#errHolidayName').val("");
}


/*************KORISNICI****************/
function addUserIntoTable(user){	
	var username = $('<td>'+user.username+'</td>');
	var firstName = $('<td>'+user.firstName+'</td>');
	var lastName = $('<td>'+user.lastName+'</td>');
	var gender = $('<td>'+user.gender+'<td>');
	var role = $('<td>'+user.role+'<td>');
	var d1 = $('<td><a class="blockButton" id="'+ user.username +'">'+"BLOKIRAJ"+ '</a></td>');
	var tr = $('<tr></tr>');
	tr.append(username).append(firstName).append(lastName).append(gender).append(role);
	if(!user.blocked){
		if(user.role !== "ADMIN"){
			tr.append(d1);
		}
	}
	$('#tabela_korisnika tbody').append(tr);
}	

/*************AMENTIES****************/
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


/******lista rezervacija*****/
function addReservationIntoTable(res){
	var tr = $('<tr></tr>');
	var sd = $('<td>'+ res.startDate + '</td>');
	var n = $('<td>'+ res.nightsNumber + '</td>');
	var f = $('<td>'+res.finalPrice+'</td>');
	var g = $('<td>'+res.guest+'</td>');
	var s = $('<td>'+res.status+'</td>');
	tr.append(sd).append(n).append(f).append(g).append(s);
	$('#admin_rezervacije tbody').append(tr);	
}


var filterSearchSortReservations = [];
function getReservationByAdmin(){
	 $.ajax({
	        type : "get",
	        url : "rest/reservation/",
	        contentType : "application/json",
	        success : function(response){
	            $('#admin_rezervacije tbody').empty();
	            console.log("rezervacije za admina");
	            console.log(response);
	            filterSearchSortReservations = [];
	            for(var res of response){
	            	filterSearchSortReservations.push(res);
	            	addReservationIntoTable(res);
	            }
	        }
	    });	
}

function searchReservation(searchFilter){
	if(searchFilter !== ""){
		$.ajax({
	        type : "post",
	        url : "rest/reservation/search?guest="+searchFilter,
	        contentType : "application/json",
	        data: JSON.stringify(filterSearchSortReservations),
	        success : function(response){
	            if(response !== undefined){  
	               console.log(response);
	               filterSearchSortReservations = [];
	               $('#admin_rezervacije tbody').empty();
	               for(var res of response){
	            	   filterSearchSortReservations.push(res);
	            	   addReservationIntoTable(res);
	               }
	            }
	        }
	    });	
	}else{
		getReservationByAdmin();
	}
}

function filterReservation(searchFilter){
	if(searchFilter !== ""){
		$.ajax({
	        type : "post",
	        url : "rest/reservation/filter?status="+searchFilter,
	        contentType : "application/json",
	        data: JSON.stringify(filterSearchSortReservations),
	        success : function(response){
	            if(response !== undefined){  
	            	console.log(response);
		            filterSearchSortReservations = [];
	               $('#admin_rezervacije tbody').empty();
	               for(var res of response){
	            	 //  console.log(res);
	            	   filterSearchSortReservations.push(res);
	            	   addReservationIntoTable(res);
	               }
	            }
	        }
	    });	
	}else{
		getReservationByAdmin();
	}
}
 function sortReservation(searchFilter){
		if(searchFilter !== ""){
			$.ajax({
		        type : "post",
		        url : "rest/reservation/sort?type="+searchFilter,
		        contentType : "application/json",
		        data: JSON.stringify(filterSearchSortReservations),
		        success : function(response){
		            if(response !== undefined){  
		            	console.log(response);
			            filterSearchSortReservations = [];
		               $('#admin_rezervacije tbody').empty();
		               for(var res of response){
		            	  // console.log(res);
		            	   filterSearchSortReservations.push(res);
		            	   addReservationIntoTable(res);
		               }
		            }
		        }
		    });	
		}else{
			getReservationByAdmin();
		}
}
/************prikaz apartmana******************************/
var brojacApartmanaADMIN = 0;

function addApartmentCardsADMIN(apartment){
	console.log("heree");
	if((brojacApartmanaADMIN % 3) === 0){        // u red idu 3 oglasa
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
	    brojacApartmanaADMIN += 1;
}

function loadApartmentAdAdmin(){
	
	$.ajax({
        type : "get",
        url : "rest/apartment/admin",
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){  
               console.log(response);
               $('#addCards').show();
               $('#tabela_pocetna tbody').empty();
               brojacApartmanaADMIN = 0;
               for(var app of response){
            	   addApartmentCardsADMIN(app);
               }
            }
        }
    });	
}

/*************modifikacija apartmana********************/
function popuniModal(id){
	console.log("usao u popuni");
	$.ajax({
        type : "get",
        url : "rest/apartment/details/?id="+id,
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){  
            	console.log(response);
            	var highlightedDates = [];
            	var datesForParse = response.availableDates;
            	//console.log("DATUMIIIIIIIIIIIIIIIIIIII");
            	for(var oneDate of datesForParse){
            		var splitsDate = oneDate.split("/");
            		var d = splitsDate[0].replace(/^0+/, '');
            		var m = splitsDate[1].replace(/^0+/, '');
            		var y = splitsDate[2];
            		//console.log(d+" "+m+" "+y);
            		var nd = new Date(y,m,d);
            		highlightedDates.push(nd);
            	}
               // highlightedDates = [new Date(2020, 7, 7), new Date(2020, 7, 19)];
            	 ////datapicker
       		     $('#datepickerMultiC').datepicker({  
       		        startDate: new Date(),
       		        multidate: true,
       		        format: "dd/mm/yyyy",
       		        daysOfWeekHighlighted: "5,6",
       		        language: 'en',
       		    }).on('changeDate', function(e) {
       		        // `e` here contains the extra attributes
       		        $(this).find('.input-group-addon .count').text(' ' + e.dates.length);
       		    });
       		    $('.date').datepicker('setDates', highlightedDates);
       		     
            	$('#tip_apartmanaC').val(response.type);
        	    $('#numberRoomsC').val(response.roomsNumber);
        		$('#numberGuestsC').val(response.guestsNumber);
        		$('#costsPerNightC').val(response.price);
        		$('#timeInC').val(response.check_in_time);
        		$('#timeOutC').val(response.check_out_time);
        		popuniModalSadrzajApartmana(response.amenties);
            }
        }
    });	
}


function addAmentiesIntoChangeForm(listAmenties, postojeci){
	var brojac = 0; //po 4 stavke u jedan red smestam
	var tr = $('<tr></tr>');
	var td;
	for(var a of listAmenties){
		if(brojac === 4){
			 $('#amentiesTableCheckC tbody').append(tr);
			 tr =  $('<tr></tr>');
			 if ($.inArray(a.id, postojeci) > -1)
			    {
			        //yourElement in yourArray
				   td = $('<td><input type="checkbox" checked= "true" name="chkCInv" value="'+ a.id +'">'+a.name+'</td>');

			    }else{
			    	 td = $('<td><input type="checkbox" name="chkCInv" value="'+ a.id +'">'+a.name+'</td>');
			    }
			 tr.append(td);
			 brojac = 1;
		}else{
			if ($.inArray(a.id, postojeci) > -1)
		    {
		        //yourElement in yourArray
			   td = $('<td><input type="checkbox" checked= "true" name="chkCInv" value="'+ a.id +'">'+a.name+'</td>');

		    }else{
		    	 td = $('<td><input type="checkbox" name="chkCInv" value="'+ a.id +'">'+a.name+'</td>');
		    }
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
	
	 $('#amentiesTableCheckC tbody').append(tr);	
	
}


function popuniModalSadrzajApartmana(amenties){
	 $.ajax({
	        type : "get",
	        url : "rest/amenties/",
	        contentType : "application/json",
	        success : function(response){
	            $('#amentiesTableCheckC tbody').empty();
	            console.log(response);
	            addAmentiesIntoChangeForm(response, amenties); 
	        }
	   });
}

function clearHost(){
	$('#emailHost').val('');
    $('#imeHost').val('');
    $('#prezHost').val('');
    $('#polHost').val('');
    $('#sifraHost').val('');
    $('#errEmailHost').val();
    $('#errImeHost').val();
    $('#errPrezlHost').val();
    $('#errPolHost').val();
    $('#errSifraHost').val();
}
