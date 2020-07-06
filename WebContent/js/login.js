$(document).ready(function() {

    $('#login-form').submit(login());
    $('#register-form').submit(registerGuest());

});

function login() {
	return function(event) {
		event.preventDefault();
        let username = $('input[name = "email"]').val();
        let password = $('input[name = "password"]').val();
        if(!username){
            $('#errUsername').text('Neophodno je uneti korisnicko ime.');
            $('#errPassword').val("");
            $("#err").val("");
            $('input[name="email"]').focus();
        }else if(!password){
            $('#errPassword').text('Neophodno je uneti lozinku');
            $('#errUsername').val("");
            $("#err").val("");
            $('input[name="password"]').focus();
        }
        else{
            var obj = {
                "username" : username,
                "password" : password
            }
            $.ajax({
                type: 'POST',
                url: 'rest/login',
                data: JSON.stringify(obj),
                contentType : 'application/json',
                success : function(){
                  //  alert(window.location.href);
                    window.location.href = "http://localhost:8080/PocetniREST/";
                    alert('Dobrodosli!');
                },
               error : function(message){
                    $('#err').text(message.responseText);
                    $('#errUsername').val("");
                    $('#errPassword').val("");
               }
            });
        }
    }
}

function registerGuest(){
    return function(event){
        event.preventDefault();
        let username = $('input[name = "username"]').val();
        let firstName = $('input[name = "firstName"]').val();
        let lastName = $('input[name = "lastName"]').val();
        let gender="";
        if(document.getElementById("female").checked===true){
            gender = "female";
        }else if(document.getElementById("male").checked===true){
            gender = "male";
        }
        let password = $('input[name = "signupPassword"]').val();
        let passwordAgain = $('input[name = "signupPasswordAgain"]').val();
        if(!username){
            $(errUsername).text("Obavezan unos korisnickog imena!");
            cleanErrors("username");
        }else if(!firstName){
            $(errfirstName).text("Obavezan unos imena");
            cleanErrors("firstName");
        }else if(!lastName){
            $(errlastName).text("Obavezan unos prezimena!");
            cleanErrors("lastName");
        }else if(gender===""){
            $(errGender).text("Obavezan izbor pola!");
            cleanErrors("gender");
        }else if(!password){
            $(errPass).text("Obavezan unos lozinke!");
            cleanErrors("password");
        }else if(!passwordAgain){
            $(errPassA).text("Obavezan ponovni unos lozinke!");
            cleanErrors("passwordAgain");
        }else if(password!==passwordAgain){
            $(errPassA).text("Lozinke se moraju poklapati!");
            cleanErrors("passwordAgain");
        }else{
            var obj = {
                "username" : username,
                "password" : password,
                "firstName" : firstName,
                "lastName" : lastName,
                "gender" : gender,
                "role" : "GUEST"
            }
            $.ajax({
                type: 'POST',
                url: 'rest/registration',
                data: JSON.stringify(obj),
                contentType : 'application/json',
                success : function(){
                    alert('Uspesno ste se registrovali!');
                    window.location.href = "http://localhost:8080/PocetniREST/";
                    alert('Dobrodosli!');
                },
               error : function(message){
                    alert(message.responseText);
                    $('input[name="username"]').text("");
                    $('input[name="username"]').focus();
               }
            });
        }
    }
}

function cleanErrors(name){
    if(name === "username"){
        $(errfirstName).val("");
        $(errlastName).val("");
        $(errGender).val("");
        $(errPass).val("");
        $(errPassA).val("");
    }else if(name === "firstName"){
        $(errUsername).val("");
        $(errlastName).val("");
        $(errGender).val("");
        $(errPass).val("");
        $(errPassA).val("");
    }else if(name === "lastName"){
        $(errUsername).val("");
        $(errfirstName).val("");
        $(errGender).val("");
        $(errPass).val("");
        $(errPassA).val("");
    }else if(name === "gender"){
        $(errUsername).val("");
        $(errfirstName).val("");
        $(errlastName).val("");
        $(errPass).val("");
        $(errPassA).val("");
    }else if(name === "password"){
        $(errUsername).val("");
        $(errfirstName).val("");
        $(errlastName).val("");
        $(errGender).val("");
        $(errPassA).val("");
    }else if(name === "passwordAgain"){
        $(errUsername).val("");
        $(errfirstName).val("");
        $(errlastName).val("");
        $(errGender).val("");
        $(errPass).val("");
    }
}