$(document).ready(function(){
	

/*PRETRAGA KORISNIKA*/
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









});