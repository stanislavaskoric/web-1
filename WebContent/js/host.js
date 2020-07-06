$(document).ready(function(){
	
	var hostUserName;  //postavlja se pri logovanju
	/*$.ajax({
        type : "get",
        url : "rest/getActive",
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){   
            	hostUserName = response.username;
            }
        }
    });	*/
	
	var numberInitMap = 0; //brojac za inicijalizaciju liste samo jednom
	 var latitude="";
	 var longitude="";
	 var street = "";
	 var number = "";
	 var city = "";
	 var ZIPcode = "";
	 var pickUpAmenties = [];
     
	
	$('#datepickerMulti').datepicker({    //oogucavanje multiselecta datuma
        startDate: new Date(),
        multidate: true,
        format: "dd/mm/yyyy",
        daysOfWeekHighlighted: "5,6",
        datesDisabled: ['31/08/2017'],
        language: 'en'
    }).on('changeDate', function(e) {
        // `e` here contains the extra attributes
        $(this).find('.input-group-addon .count').text(' ' + e.dates.length);
    });
	
	
	/******** DODAVANJE MAPE***************************/
	
	$('#li_adminAddApartment').on("click",function(){
		console.log("KLIK NA ADD APARTMENT");
		$('#adminProfil').hide();
		$('#myAddApartmentModal').hide();
		$('#reservationViewHost').hide();
		$('#map').show();
		$('#dsearchUsersHOST').hide();
		$('#addCards').hide();
		$('#apartmentDetails').hide();
		$('#commentViewHost').hide();
		$('#infoAddApartment').show();
		if(numberInitMap === 0){
			numberInitMap++;
			//alert(numberInitMap);
			var olview = new ol.View({ center: [0, 0], zoom: 4 }),
		    baseLayer = new ol.layer.Tile({ source: new ol.source.OSM() }),
		    map = new ol.Map({
		      target: document.getElementById('map'),
		      view: olview,
		      layers: [baseLayer]
		    });
		    
		// popup for serach address
	     	var popup = new ol.Overlay.Popup();
		    map.addOverlay(popup);

		//Instantiate with some options and add the Control
	    	var geocoder = new Geocoder('nominatim', {
			  provider: 'osm',
			  lang: 'en',
			  placeholder: 'Search for ...',
			  limit: 5,
			  debug: false,
			  autoComplete: true,
			  keepOpen: true
			});
	    	map.addControl(geocoder);
		   
	 	//Listen when an address is chosen
		    geocoder.on('addresschosen', function (evt) {
		      var coords = evt.coordinate;
		      latitude = coords[0];
		      longitude = coords[1];
		      var address = evt.address.formatted;
			  var addressArray = address.split(',');
			  console.log(address);
			  console.log(addressArray);
			  city = addressArray[4];
			  var numberA = addressArray[0];
			  var numberArray = numberA.split('>');
			  number = numberArray[1];
			  console.log("BROJ"+number);
			  var streetA = addressArray[1];
			  var streetArray = streetA.split('<');
			  street = streetArray[0];
			  ZIPcode = addressArray[8];
			  window.setTimeout(function () {
			    popup.show(evt.coordinate, evt.address.formatted);
			  }, 3000);
			});
		}	   
			
	});
	
	
	/**********************DODAVANJE INFO O APARTMANU************************************************************/
	
	$('#button_inputNewApartment').on("click",function(){
		  console.log("klik na add info apartment");
		  $('#myAddApartmentModal').show();
		  $('#addApartmentError').text("");
		    var modal = document.getElementById("myAddApartmentModal");
			modal.style.display = "block";
			// Get the <span> element that closes the modal
			var span = document.getElementsByClassName("closeAddApartmentModal")[0];
			span.onclick = function() {
				  modal.style.display = "none";
			}	 
	});

	/**********************POTVRDA DODATOG SADRZAJA O APARTMNU************************************************************/
	$('#button_addNewAperment').on("click",function(){
		console.log("klik na dodavanje info o apartmanu");
		var tip_apartmana = $('#tip_apartmana').val();
		var broj_soba = $('#numberRooms').val();
		var broj_gostiju = $('#numberGuests').val();
		var cena = $('#costsPerNight').val();
		var datumi = $('#Dates').val();
		var vreme_prijave = $('#timeIn').val();
		var vreme_odjave = $('#timeOut').val();
		var slike = [];  
		$.map($('.multiupload').get(0).files, function(file) {  //ucitavam sve izabrane slike
			  slike.push(file.name);
	    });
		console.log("Slike");
		console.log(slike);
        //validacija forme
		if(latitude === ""){
			$('#addApartmentError').text("Morate izabrati lokaciju sa mape!");
		}else if(tip_apartmana === "" || broj_soba === "" || broj_gostiju === "" || cena === "" ||
				 datumi === "" || vreme_prijave === "" || vreme_odjave === "" || slike === ""){
			$('#addApartmentError').text("Morate popuniti sva polja!");
		}else{
		    //ok salji na bek
			var appInfo = {
				"tip_apartmana" : tip_apartmana,
			   	"broj_soba" : broj_soba,
				"broj_gostiju" : broj_gostiju,
				 "ulica" : street,
			     "grad" : city,
			     "broj_stana" : number,
			     "zip" : ZIPcode,
			     "geo_sirina" : latitude,
			     "geo_duzina" : longitude,
			     "datumi" : datumi,
			     "domacin" : hostUserName,
			     "slike" : slike,
			     "price" : cena,
			     "prijava" : vreme_prijave,
			     "odjava" :  vreme_odjave,
			     "sadrzaj" : pickUpAmenties
			}
			console.log("OBJEKAT");
			console.log(appInfo);
			 $.ajax({
	                type: 'POST',
	                url: 'rest/apartment/',
	                data: JSON.stringify(appInfo),
	                contentType : 'application/json',
	                success : function(){
	                  alert("Uspesno sacuvan apartman");
	                  $('#myAddApartmentModal').hide();
	                }
	            });
			
		}

		
		
	});
	
	
	
	
	/**********************IZBOR SADRZAJA ZA APARTMAN************************************************************/
	$('#amentiesIntoAp').on("click",function(){
		  console.log("klik na add amenties into apartment");
		  $('#myAddIntoAppModal').show();
		    var modal = document.getElementById("myAddIntoAppModal");
			modal.style.display = "block";
			// Get the <span> element that closes the modal
			var span = document.getElementsByClassName("closeAddIntoApp")[0];
			span.onclick = function() {
				  modal.style.display = "none";
			}
			getAmentiesFromBack();
	});
	
	/**********************POTVRDA IZBORA SADRZAJA ZA APARTMAN************************************************************/
	$('#button_pickUpAmenties').on("click",function(){
		 console.log("potvrda izbora za sadrzaj apartmana");
		 $.each($('input[name^="chkInv"]:checked'), function(){
             pickUpAmenties.push($(this).val());
         });
		 console.log(pickUpAmenties);
		 $('#myAddIntoAppModal').hide();
	});
	
	
	/*******************LISTA REZERVACIJA********************************************************************************/
	$('#li_hostReservation').on("click",function(){
		 console.log("rezervacije host");
		 $('#adminProfil').hide();
		 $('#myAddApartmentModal').hide();
		 $('#map').hide();
		 $('#infoAddApartment').hide();
		 $('#dsearchUsersHOST').hide();
		 $('#addCards').hide();
		 $('#commentViewHost').hide();
		 $('#reservationViewHost').show();
		 getReservationByHost();
	});
	
	
	$('#host_rezervacije').on('click','a',function(){
		var id = $(this).attr('id');
		var type = $(this).attr('class');
		console.log(type);
		if(type === "acceptResButton"){
			acceptReservation(id);
		}else if(type === "deniedResButton"){
			deniedReservation(id);
		}else if(type === "completeResBuuton"){
			completeReservation(id);
		}
	});
	 
	
	/*******************PREGLED KORISNIKA DOMACINOVOG APARTMANA*********************/
			$('#li_hostSearch').on("click",function(){
				 console.log("klik na pregled korisnija");
				 $('#dsearchUsersHOST').show();
				 $('#adminProfil').hide();
				 $('#myAddApartmentModal').hide();
				 $('#map').hide();
				 $('#infoAddApartment').hide();
				 $('#reservationViewHost').hide();
				 $('#addCards').hide();
				 $('#commentViewHost').hide();
				 getUsersByHost();
			});
	
		
			$('#searchButtonHOST').on("click",function(){
				
				var username = $('#searchUsernameHOST').val();
				var gender = $('#searchGenderHOST').val();
				var role = $('#searchRoleHOST').val();
				console.log(username+gender+role+"OVDEEEEEEEE");
				
				 $.ajax({
				        type : "get",
				        url : "rest/reservation/searchUsers?username="+username+"&gender="+gender+"&role="+role,
				        contentType : "application/json",
				        success : function(response){
				            $('#tabela_korisnikaHOST tbody').empty();
				            console.log(response);
				            for(var k of response){
				                console.log(k.username);
				                addUserIntoTableHOST(k);
				            }   
				        }
				    });
				
			});
	
	/*******************PREGLED APARTMANA*********************/
	$('#li_hostInApartment').on("click", function(){
		 console.log("pregled neaktivnih apartmana-domacin");
		 $('#dsearchUsersHOST').hide();
		 $('#adminProfil').hide();
		 $('#myAddApartmentModal').hide();
		 $('#map').hide();
		 $('#infoAddApartment').hide();
		 $('#reservationViewHost').hide();
		 $('#commentViewHost').hide();
		 $('#addCards').show();
		 loadApartmentInActiveHost();
	});
	
	
	$('#li_hostActiveApartment').on("click", function(){
		 console.log("pregled aktivnih apartmana-domacin");
		 $('#dsearchUsersHOST').hide();
		 $('#adminProfil').hide();
		 $('#myAddApartmentModal').hide();
		 $('#map').hide();
		 $('#infoAddApartment').hide();
		 $('#reservationViewHost').hide();
		 $('#commentViewHost').hide();
		 $('#addCards').show();
		 loadApartmentActiveHost();
	});
	
	
	/****************FILTRIRANJE APARTMANA********************************/
	var filterAmenties = [];
	$('#filterTipHOST').on("change", function(){
		var type = $(this).val();
		var s = "";
		$.ajax({
	        type : "post",
	        url : "rest/apartment/hfilter?t="+type+"&s="+s,
	        contentType : "application/json",
	        data: JSON.stringify(filterAmenties),
	        success : function(response){
	            if(response !== undefined){  
	               console.log(response);
	               $('#tabela_detaljiAp tbody td').empty();
	               $('#amentiesApartment tbody').empty();
	               $('#activeHostDetails').show();
	               $('#addCards').show();
	               $('#tabela_pocetna tbody').empty();
	               $('#apartmentDetails').hide();
	               brojacApartmanaHOST = 0;
	               for(var app of response){
	            	   addApartmentCardsHOST(app);
	               }
	            }
	        }
	    });	
		
	});
	
	$('#filterStatusHOST').on("change", function(){
		var s = $(this).val();
		var t = "";
		$.ajax({
	        type : "post",
	        url : "rest/apartment/hfilter?t="+t+"&s="+s,
	        contentType : "application/json",
	        data: JSON.stringify(filterAmenties),
	        success : function(response){
	            if(response !== undefined){  
	               console.log(response);
	               $('#tabela_detaljiAp tbody td').empty();
	               $('#amentiesApartment tbody').empty();
	               $('#activeHostDetails').show();
	               $('#addCards').show();
	               $('#tabela_pocetna tbody').empty();
	               $('#apartmentDetails').hide();
	               brojacApartmanaHOST = 0;
	               for(var app of response){
	            	   addApartmentCardsHOST(app);
	               }
	            }
	        }
	    });	
		
	});
	
	
	$('#filterSadrzajHost').on("click", function(){
		  console.log("klik na filter by amenties");
		  $('#myAddIntoAppModalF').show();
		    var modal = document.getElementById("myAddIntoAppModalF");
			modal.style.display = "block";
			// Get the <span> element that closes the modal
			var span = document.getElementsByClassName("closeAddIntoAppF")[0];
			span.onclick = function() {
				  modal.style.display = "none";
			}
			getAmentiesFromBackFILTER();
	});
	
	
	/**********************POTVRDA IZBORA SADRZAJA ZA FILTRIRANJE************************************************************/
	var pickUpAmentiesF = [];
	$('#button_pickUpAmentiesF').on("click",function(){
		 console.log("potvrda izbora za filtiranje apartmana");
		 $.each($('input[name^="chkFInv"]:checked'), function(){
             pickUpAmentiesF.push($(this).val());
         });
		 console.log(pickUpAmentiesF);
		 $('#myAddIntoAppModalF').hide();
		 $.ajax({
		        type : "post",
		        url : "rest/apartment/hfilter?t="+""+"&s="+"",
		        contentType : "application/json",
		        data: JSON.stringify(pickUpAmentiesF),
		        success : function(response){
		            if(response !== undefined){  
		               console.log(response);
		               $('#tabela_detaljiAp tbody td').empty();
		               $('#amentiesApartment tbody').empty();
		               $('#activeHostDetails').show();
		               $('#addCards').show();
		               $('#tabela_pocetna tbody').empty();
		               $('#apartmentDetails').hide();
		               brojacApartmanaHOST = 0;
		               pickUpAmentiesF = [];
		               for(var app of response){
		            	   addApartmentCardsHOST(app);
		               }
		            }
		        }
		    });	
		 
	});
	
	
	
	
	
	/*************SORTIRANJE APARTMANA*********************/
	$('#tip_sortiranjaHOST').on("change", function(){
		var vrsta = $(this).val(); //rastuce ili opadajuce
		if(vrsta !== ""){
			$.ajax({
		        type : "get",
		        url : "rest/apartment/hsort?type="+vrsta,
		        contentType : "application/json",
		        data: JSON.stringify(filterAmenties),
		        success : function(response){
		            if(response !== undefined){  
		               console.log(response);
		               $('#tabela_detaljiAp tbody td').empty();
		               $('#amentiesApartment tbody').empty();
		               $('#activeHostDetails').show();
		               $('#addCards').show();
		               $('#tabela_pocetna tbody').empty();
		               $('#apartmentDetails').hide();
		               brojacApartmanaHOST = 0;
		               for(var app of response){
		            	   addApartmentCardsHOST(app);
		               }
		            }
		        }
		    });	
		}
	});
	
	/*************PREGLED KOMENTARA********************************/
	$('#li_hostComments').on("click", function(){
		console.log("klik na pregled komentara domacin");
		 $('#dsearchUsersHOST').hide();
		 $('#adminProfil').hide();
		 $('#myAddApartmentModal').hide();
		 $('#map').hide();
		 $('#infoAddApartment').hide();
		 $('#reservationViewHost').hide();
		 $('#addCards').hide();
		 $('#apartmentDetails').hide();
		 $('#commentViewHost').show();
		 getCommentsByHost();
	});
	
	$('#host_komentari').on('click','a',function(){
		var id = $(this).attr('id');
		availableComment(id);
	});
	
	/************FILTRIRANJE/PRETRAGA/SORT REZERVACIJE****************/
	$('#searchResHOST').on("change", function(){
		console.log("PRETRAGA REZERVACIJAAAAA");
		var searchFilter = $(this).val(); 
		searchReservationHOST(searchFilter);
	});
	
	$('#filterResHOST').on("change", function(){
		console.log("FILTRIRANJE REZERVACIJA");
		var filter = $(this).val(); 
		filterReservationHOST(filter);
	});
	
    $('#sortResHOST').on("change", function(){
    	console.log("SORTIRANJE REZERVACIJA");
        var vrsta = $(this).val(); //rastuce ili opadajuce
        sortReservationHOST(vrsta);
	}); 
	
	
	
});
/*********komentari********/
function addCommentIntoTableHost(comment){	
	var id = $('<td>'+comment.apartment+'</td>');
	var text = $('<td>'+comment.text+'</td>');
	var ocena = $('<td>'+comment.evaluation+'</td>');
	var gost = $('<td>'+comment.guest+'<td>');
	var d1 = $('<td><a class="acceptCommentButton" id="'+ comment.id +'">'+"PRIKAZI"+ '</a></td>');
	var tr = $('<tr></tr>');
	tr.append(id).append(text).append(ocena).append(gost);
	if(!comment.odabran){
		tr.append(d1);
	}
	$('#host_komentari tbody').append(tr);
}

function getCommentsByHost(){
	 $.ajax({
	        type : "get",
	        url : "rest/comment/host",
	        contentType : "application/json",
	        success : function(response){
	            $('#host_komentari tbody').empty();
	            //console.log(response);
	            for(var k of response){
	                addCommentIntoTableHost(k);
	            }   
	        }
	    });
}

function availableComment(id){
	 $.ajax({
       type: 'PUT',
       url: 'rest/comment/?id='+id,
       contentType : 'application/json',
       success : function(){
         alert("Omogucen pregled komentara");
         $('#host_komentari tbody').empty();
         getCommentsByHost();
       },
	     error : function(message){
	    	 alert(message.responseText);
       }
   });
}



/******rezervacije*******/
function addReservationIntoHostTable(res){
	var tr = $('<tr></tr>');
	var sd = $('<td>'+ res.startDate + '</td>');
	var n = $('<td>'+ res.nightsNumber + '</td>');
	var f = $('<td>'+res.finalPrice+'</td>');
	var g = $('<td>'+res.guest+'</td>');
	var s = $('<td>'+res.status+'</td>');
	var d1 = $('<td><a class="acceptResButton" id="'+ res.id +'">'+"PRIHVATI"+ '</a></td>');			
	var d2 = $('<td><a class="deniedResButton" id="'+ res.id+'">'+"ODBIJ"+ '</a></td>');
	var d3 = $('<td><a class="completeResBuuton" id="'+ res.id+'">'+"ZAVRSI"+ '</a></td>');
	
	
	tr.append(sd).append(n).append(f).append(g).append(s);
	
	if(res.status === "CREATE"){
		tr.append(d1).append(d2);
	}else if(res.status === "ACCEPT"){
		if(res.finish === true){
			tr.append(d3);
		}else{
			tr.append(d2);
		}
	}
	
	$('#host_rezervacije tbody').append(tr);	
}

var filterSearchSortReservationsHOST=[];
function getReservationByHost(){
	 $.ajax({
	        type : "get",
	        url : "rest/reservation/host",
	        contentType : "application/json",
	        success : function(response){
	            $('#host_rezervacije tbody').empty();
	            console.log("host reyervacije");
	            filterSearchSortReservationsHOST = [];
	            console.log(response);
	            for(var res of response){
	            	filterSearchSortReservationsHOST.push(res);
	            	addReservationIntoHostTable(res);
	            }
	        }
	    });	
}


function searchReservationHOST(searchFilter){
	if(searchFilter !== ""){
		$.ajax({
	        type : "post",
	        url : "rest/reservation/search?guest="+searchFilter,
	        contentType : "application/json",
	        data: JSON.stringify(filterSearchSortReservationsHOST),
	        success : function(response){
	            if(response !== undefined){  
	               console.log(response);
	               filterSearchSortReservationsHOST = [];
	               $('#host_rezervacije tbody').empty();
	               for(var res of response){
	            	   filterSearchSortReservationsHOST.push(res);
	            	   addReservationIntoHostTable(res);
	               }
	            }
	        }
	    });	
	}else{
		getReservationByHost();
	}
}

function filterReservationHOST(searchFilter){
	if(searchFilter !== ""){
		$.ajax({
	        type : "post",
	        url : "rest/reservation/filter?status="+searchFilter,
	        contentType : "application/json",
	        data: JSON.stringify(filterSearchSortReservationsHOST),
	        success : function(response){
	            if(response !== undefined){  
	            	console.log(response);
		            filterSearchSortReservationsHOST = [];
	                $('#host_rezervacije tbody').empty();
	               for(var res of response){
	            	 //  console.log(res);
	            	   filterSearchSortReservationsHOST.push(res);
	            	   addReservationIntoHostTable(res);
	               }
	            }
	        }
	    });	
	}else{
		getReservationByHost();
	}
}
 function sortReservationHOST(searchFilter){
		if(searchFilter !== ""){
			$.ajax({
		        type : "post",
		        url : "rest/reservation/sort?type="+searchFilter,
		        contentType : "application/json",
		        data: JSON.stringify(filterSearchSortReservationsHOST),
		        success : function(response){
		            if(response !== undefined){  
		            	console.log(response);
		            	 filterSearchSortReservationsHOST = [];
			             $('#host_rezervacije tbody').empty();
		                 for(var res of response){
		            	   filterSearchSortReservationsHOST.push(res);
		            	   addReservationIntoHostTable(res);
		                }
		            }
		        }
		    });	
		}else{
			getReservationByHost();
		}
}

 
function acceptReservation(id){
	 $.ajax({
         type: 'PUT',
         url: 'rest/reservation/accept/?id='+id,
         contentType : 'application/json',
         success : function(){
           alert("Uspesno prihvacena rezervacija");
           getReservationByHost();
         },
	     error : function(message){
	    	 alert(message.responseText);
         }
     });
}

function deniedReservation(id){
	 $.ajax({
        type: 'PUT',
        url: 'rest/reservation/denied/?id='+id,
        contentType : 'application/json',
        success : function(){
          alert("Uspesno odbijena rezervacija");
          getReservationByHost();
        },
	     error : function(message){
	    	 alert(message.responseText);
        }
    });
}

function completeReservation(id){
	 $.ajax({
        type: 'PUT',
        url: 'rest/reservation/complete/?id='+id,
        contentType : 'application/json',
        success : function(){
          alert("Uspesno zavrsena rezervacija");
          getReservationByHost();
        },
	     error : function(message){
	    	 alert(message.responseText);
        }
    });
}

/***sadryaj******/
function getAmentiesFromBack(){
	 $.ajax({
	        type : "get",
	        url : "rest/amenties/",
	        contentType : "application/json",
	        success : function(response){
	            $('#amentiesTableCheck tbody').empty();
	            console.log(response);
	            addAmentiesIntoForm(response); 
	        }
	    });	
}


function addAmentiesIntoForm(listAmenties){	
	var brojac = 0; //po 4 stavke u jedan red smestam
	var tr = $('<tr></tr>');
	var td;
	for(var a of listAmenties){
		if(brojac === 4){
			 $('#amentiesTableCheck tbody').append(tr);
			 tr =  $('<tr></tr>');
			 td = $('<td><input type="checkbox" name="chkInv" value="'+ a.id +'">'+a.name+'</td>');
			 tr.append(td);
			 brojac = 1;
		}else{
			 td = $('<td><input type="checkbox" name="chkInv" value="'+ a.id +'">'+a.name+'</td>');
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
	
	 $('#amentiesTableCheck tbody').append(tr);	
	
}



/*****pregled korisnika*********************/
function addUserIntoTableHOST(user){	
	var username = $('<td>'+user.username+'</td>');
	var firstName = $('<td>'+user.firstName+'</td>');
	var lastName = $('<td>'+user.lastName+'</td>');
	var gender = $('<td>'+user.gender+'<td>');
	var role = $('<td>'+user.role+'<td>');
	var tr = $('<tr></tr>');
	tr.append(username).append(firstName).append(lastName).append(gender).append(role);
	$('#tabela_korisnikaHOST tbody').append(tr);
}	

function getUsersByHost(){
	 console.log("get users by host");
	 $.ajax({
	        type : "get",
	        url : "rest/reservation/users",
	        contentType : "application/json",
	        success : function(response){
	            $('#tabela_korisnikaHOST tbody').empty();
	            console.log(response);
	            for(var k of response){
	                console.log(k.username);
	                addUserIntoTableHOST(k);
	            }   
	        }
	    });
}

/************prikaz apartmana******************************/
var brojacApartmanaHOST = 0;

function addApartmentCardsHOST(apartment){
	console.log("heree");
	if((brojacApartmanaHOST % 3) === 0){        // u red idu 3 oglasa
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
	    brojacApartmanaHOST += 1;
}

function loadApartmentInActiveHost(){
	console.log("ucitav neaktivne apartmane");
	$.ajax({
        type : "get",
        url : "rest/apartment/i",
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){  
               console.log(response);
               $('#tabela_detaljiAp tbody td').empty();
               $('#amentiesApartment tbody').empty();
               $('#activeHostDetails').hide();
               $('#addCards').show();
               $('#tabela_pocetna tbody').empty();
               $('#apartmentDetails').hide();
               brojacApartmanaHOST = 0;
               for(var app of response){
            	   addApartmentCardsHOST(app);
               }
            }
        }
    });	
}

function loadApartmentActiveHost(){
	console.log("ucitavam aktivne apartmane");
	$.ajax({
        type : "get",
        url : "rest/apartment/a",
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){  
               console.log(response);
               $('#tabela_detaljiAp tbody td').empty();
               $('#amentiesApartment tbody').empty();
               $('#activeHostDetails').show();
               $('#addCards').show();
               $('#tabela_pocetna tbody').empty();
               $('#apartmentDetails').hide();
               brojacApartmanaHOST = 0;
               for(var app of response){
            	   addApartmentCardsHOST(app);
               }
            }
        }
    });	
}


function getAmentiesFromBackFILTER(){
	 $.ajax({
	        type : "get",
	        url : "rest/amenties/",
	        contentType : "application/json",
	        success : function(response){
	            $('#amentiesTableCheckF tbody').empty();
	            console.log(response);
	            addAmentiesIntoFormFILTER(response); 
	        }
	    });	
}


function addAmentiesIntoFormFILTER(listAmenties){	
	var brojac = 0; //po 4 stavke u jedan red smestam
	var tr = $('<tr></tr>');
	var td;
	for(var a of listAmenties){
		if(brojac === 4){
			 $('#amentiesTableCheckF tbody').append(tr);
			 tr =  $('<tr></tr>');
			 td = $('<td><input type="checkbox" name="chkFInv" value="'+ a.name +'">'+a.name+'</td>');
			 tr.append(td);
			 brojac = 1;
		}else{
			 td = $('<td><input type="checkbox" name="chkFInv" value="'+ a.name +'">'+a.name+'</td>');
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
	
	 $('#amentiesTableCheckF tbody').append(tr);	
	
}