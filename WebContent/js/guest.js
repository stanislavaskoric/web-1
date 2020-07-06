$(document).ready(function(){
	
	var guestUsername;


	$('#li_guestReservation').on("click",function(){
		console.log("klik na rez");
		$('#adminProfil').hide();
		$('#infoAddApartment').hide();
		$('#addCards').hide();
		$('#guestViewHost').show();
		$('#apartmentDetails').hide();
		getReservationByGuest();
	});
	
	
	$('#guest_rezervacije').on('click','a',function(){
		var id = $(this).attr('id');
		cancelReservation(id);
	});
	
	$('#li_guestActiveApartment').on("click", function(){
		$('#adminProfil').hide();
		$('#infoAddApartment').hide();
		$('#guestViewHost').hide();
		$('#apartmentDetails').hide();
		$('#addCards').show();
		$('#tabela_pocetna tbody').empty();
		loadApartmentAd(); 
	});
	
	
	/*********DODAVANJE KOMENTARA*************/
	$('#commentApartmentInfo').on("click", function(){
		console.log("klik na dodavanje komentara");
		  cleanCommentModal();
		  var modal = document.getElementById("modCreateComment");
		  modal.style.display = "block";
		  // Get the <span> element that closes the modal
		  var span = document.getElementsByClassName("closeCreateComment")[0];
		  span.onclick = function() {
			 modal.style.display = "none";
		  }
	});
	
	/*********SORTIRANJE REZERVACIJA PO CENI***************/
	$('#sortResGUEST').on("change", function(){
    	console.log("SORTIRANJE REZERVACIJA");
        var vrsta = $(this).val(); //rastuce ili opadajuce
        sortReservationGUEST(vrsta);
	}); 
	
	
	
});
/*****dodavanje komentara***/
function cleanCommentModal(){
	$('#evaluation').val('');
	$('#commentText').val('');
	$('#errEvaluation').val('');
}



/******rezervacije*******/
function addReservationIntoGuestTable(res){
	var tr = $('<tr></tr>');
	var sd = $('<td>'+ res.startDate + '</td>');
	var n = $('<td>'+ res.nightsNumber + '</td>');
	var f = $('<td>'+res.finalPrice+'</td>');
	var s = $('<td>'+res.status+'</td>');
	var d1 = $('<td><a class="cancelResButton" id="'+ res.id +'">'+"ODUSTANI"+ '</a></td>');				
	
	tr.append(sd).append(n).append(f).append(s);
	
	if(res.status === "CREATE" || res.status === "ACCEPT" ){
		tr.append(d1);
	}
	
	$('#guest_rezervacije tbody').append(tr);	
}

/****rez***/
var filterSearchSortReservationsGUEST = [];
function getReservationByGuest(){
	 $.ajax({
	        type : "get",
	        url : "rest/reservation/guest",
	        contentType : "application/json",
	        success : function(response){
	        	filterSearchSortReservationsGUEST = [];
	            $('#guest_rezervacije tbody').empty();
	            console.log("guest reyervacije");
	            console.log(response);
	            for(var res of response){
	            	filterSearchSortReservationsGUEST.push(res);
	            	addReservationIntoGuestTable(res);
	            }
	        }
	    });	
}

function sortReservationGUEST(searchFilter){
	if(searchFilter !== ""){
		$.ajax({
	        type : "post",
	        url : "rest/reservation/sort?type="+searchFilter,
	        contentType : "application/json",
	        data: JSON.stringify(filterSearchSortReservationsGUEST),
	        success : function(response){
	            if(response !== undefined){  
	            	console.log(response);
	            	 filterSearchSortReservationsGUEST = [];
		             $('#guest_rezervacije tbody').empty();
	                 for(var res of response){
	            	   filterSearchSortReservationsGUEST.push(res);
	            	   addReservationIntoGuestTable(res);
	                }
	            }
	        }
	    });	
	}else{
		getReservationByGuest();
	}
}



function cancelReservation(id){
	 $.ajax({
       type: 'PUT',
       url: 'rest/reservation/cancel/?id='+id,
       contentType : 'application/json',
       success : function(){
         alert("Uspesan odustanak od rezervacije");
         getReservationByGuest();
       },
	     error : function(message){
	    	 alert(message.responseText);
       }
   });
}

