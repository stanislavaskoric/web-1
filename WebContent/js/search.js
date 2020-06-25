$(document).ready(function(){
	
	
	//pretraga apartmana
	$('#li_searchApartment').on("click",function(){
		$('#mod_pretraga').show();
		var modal = document.getElementById("mod_pretraga");
		modal.style.display = "block";
		// Get the <span> element that closes the modal
		var span = document.getElementsByClassName("closeSearch")[0];
		span.onclick = function() {
			  modal.style.display = "none";
		}
	});
	
	
	$('#pretraziButton').on("click",function(){
		search(); 
	});
	

	
});



function search(){
	
	 emptySearchModal();
	 var noEmpty = true;
	 var datumOd = $('#datumOd').val();
	 var datumDo = $('#datumDo').val();
	 if((datumOd !== "" && datumDo === "") || (datumOd === "" && datumDo !== "")){
		 $('#errDatum').text('Morate uneti oba datuma');
		 noEmpty = false;
	 }else{
		 if(new Date(datumDo) < new Date(datumOd)){
			 $('#errDatum').text('Opseg datuma nije pravilan');
			 noEmpty = false;
		 }
	 }
	 
	 var cenaOd = $('#cenaOd').val();
	 var cenaDo = $('#cenaDo').val();
	 if((cenaOd !== "" && cenaDo === "") || (cenaOd === "" && cenaDo !== "")){
		 $('#errCena').text('Morate uneti obe cene');
		 noEmpty = false;
	 }else{
		 if(parseFloat(cenaDo)<parseFloat(cenaOd)){
			 $('#errCena').text('Opseg cena nije pravilan');
			 noEmpty = false;
		 }
	 }
			 
	 var brojsobaOd = $('#brojsobaOd').val();
	 var brojsobaDo = $('#brojsobaDo').val();
	 if((brojsobaOd !== "" && brojsobaDo === "") || (brojsobaOd === "" && brojsobaDo !== "")){
		 $('#errBrojSoba').text('Morate uneti oba broja');
		 noEmpty = false;
	 }else{
		 if(parseInt(brojsobaDo)<parseInt(brojsobaOd)){
			 $('#errBrojSoba').text('Opseg broja soba nije pravilan');
			 noEmpty = false;
		 }
	 }
	 
	 var brojOsoba = $('#brojOsoba').val();
	 
	 if(noEmpty){
		 $.ajax({
		        type : "get",
		        url : "rest/apartment/search?start="+datumOd+"&end="+datumDo+"&cO="+cenaOd+"&cD="+cenaDo
		                                    +"&sO="+brojsobaOd+"&sD="+brojsobaDo+"&o="+brojOsoba,
		        contentType : "application/json",
		        success : function(response){
		        	$('#mod_pretraga').hide(); 
		   		    emptySearchModal();
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



function emptySearchModal(){
	$('#datumOd').val("");
	$('#datumDo').val("");
	$('#cenaOd').val("");
	$('#cenaDo').val("");
	$('#brojsobaOd').val("");
	$('#brojsobaDo').val("");
	$('#brojOsoba').val("");
	$('#errDatum').val('');
	$('#errCena').val('');
	$('#errBrojSoba').val('');
	$('#errOsoba').val('');
}