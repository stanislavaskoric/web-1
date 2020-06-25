$(document).ready(function(){
	
	var hostUserName;
	$.ajax({
        type : "get",
        url : "rest/getActive",
        contentType : "application/json",
        success : function(response){
            if(response !== undefined){   
            	hostUserName = response.username;
            }
        }
    });	
	
	var numberInitMap = 0; //brojac za inicijalizaciju liste samo jednom
	 var latitude="";
	 var longitude="";
	 var street = "";
	 var number = "";
	 var city = "";
	 var ZIPcode = "";
	 var pickUpAmenties = [];
     
	$('#datepicker').datepicker({    //oogucavanje multiselecta datuma
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
		$('#map').show();
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
			  city = addressArray[3];
			  var numberA = addressArray[0];
			  var numberArray = numberA.split('>');
			  number = numberArray[1];
			  console.log("BROJ"+number);
			  var streetA = addressArray[1];
			  var streetArray = streetA.split('<');
			  street = streetArray[0];
			  ZIPcode = addressArray[7];
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
	
	
	
	
	
	
	
});


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

