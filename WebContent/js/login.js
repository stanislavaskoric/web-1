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
            $('#errPassword').text("");
            $("#err").text("");
            $('input[name="email"]').focus();
        }else if(!password){
            $('#errPassword').text('Neophodno je uneti lozinku');
            $('#errUsername').text("");
            $("#err").text("");
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
                    alert('Logovanje je uspesno!');
                },
               error : function(message){
                    $('#err').text(message.responseText);
                    $('#errUsername').text("");
                    $('#errPassword').text("");
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
        $(errfirstName).text("");
        $(errlastName).text("");
        $(errGender).text("");
        $(errPass).text("");
        $(errPassA).text("");
    }else if(name === "firstName"){
        $(errUsername).text("");
        $(errlastName).text("");
        $(errGender).text("");
        $(errPass).text("");
        $(errPassA).text("");
    }else if(name === "lastName"){
        $(errUsername).text("");
        $(errfirstName).text("");
        $(errGender).text("");
        $(errPass).text("");
        $(errPassA).text("");
    }else if(name === "gender"){
        $(errUsername).text("");
        $(errfirstName).text("");
        $(errlastName).text("");
        $(errPass).text("");
        $(errPassA).text("");
    }else if(name === "password"){
        $(errUsername).text("");
        $(errfirstName).text("");
        $(errlastName).text("");
        $(errGender).text("");
        $(errPassA).text("");
    }else if(name === "passwordAgain"){
        $(errUsername).text("");
        $(errfirstName).text("");
        $(errlastName).text("");
        $(errGender).text("");
        $(errPass).text("");
    }
}