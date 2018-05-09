

function checkForm(form)
{
    if(form.name.value == "") {
        alert("Hiba: A név nem maradhat üresen!");
        form.name.focus();
        return false;
    }
    if(form.username.value == "") {
        alert("Hiba: A felhasználónév nem maradhat üresen!");
        form.username.focus();
        return false;
    }
    re = /^\w+$/;
    if(!re.test(form.username.value)) {
        alert("Hiba: A felhasználónév csak betűket, számokat és aláhúzást tartalmazhat!");
        form.username.focus();
        return false;
    }
    if(form.email.value == "") {
        alert("Hiba: Az email nem maradhat üresen!");
        form.email.focus();
        return false;
    }

    if(form.pwd1.value != "" && form.pwd1.value == form.pwd2.value) {
        if(form.pwd1.value.length < 6) {
            alert("Hiba: A jelszónak legalább hat karakter hosszúnak kell lennie!");
            form.pwd1.focus();
            return false;
        }
        if(form.pwd1.value == form.username.value) {
            alert("Hiba: A jelszónak különböznie kell a felhasználónévtől!");
            form.pwd1.focus();
            return false;
        }
        re = /[0-9]/;
        if(!re.test(form.pwd1.value)) {
            alert("Hiba: A jelszónak tartalmaznia kell legalább egy számot (0-9)!");
            form.pwd1.focus();
            return false;
        }
        re = /[a-zA-Z]/;
        if(!re.test(form.pwd1.value)) {
            alert("Hiba: A jelszónak tartalmaznia kell betűt!");
            form.pwd1.focus();
            return false;
        }

    } else {
        alert("Hiba: Kérlek ellenőrizd le, hogy beírtad, és megerősítetted a jelszavad!");
        form.pwd1.focus();
        return false;
    }


    if(!form.terms.checked){
        alert("Hiba: A regisztrációhoz el kell fogadnod a felhasználói feltételeket!");
        form.terms.focus();
        return false;
    }

    addUser();


    return true;
}


function addUser(){
    var url = "http://localhost:8082/user/import/";
    var name = document.getElementById("name").value;
    var userName = document.getElementById("username").value;
    var pwd = document.getElementById("pwd1").value;
    var email = document.getElementById("email").value;

    var add = new HttpClient();
    add.post('http://localhost:8082/user/import', "name=" + name + "&username=" + userName +
    "&password=" + pwd + "&email=" + email);


}
/*function addUser() {
    var url = "http://localhost:8082/user/import/";
    var name = document.getElementById("name").value;
    var userName = document.getElementById("username").value;
    var pwd = document.getElementById("pwd1").value;
    var email = document.getElementById("email").value;

    var params = name + "/" + userName + "/" + pwd + "/" + email ;
  //  alert(url + params);

    var xhttp;
    if (window.XMLHttpRequest) {
        xhttp = new XMLHttpRequest();
    } else {
        // code for older browsers
        xhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xhttp.onreadystatechange = function () {
        if(xhttp.readyState == 4 && xhttp.status == 200){
            alert(xhttp.responseText);
        }
    }
    xhttp.open("GET", url+params, true);
    xhttp.send();
}*/

var HttpClient = function() {
    this.get = function(aUrl, aCallback) {
        var anHttpRequest = new XMLHttpRequest();
        anHttpRequest.onreadystatechange = function() {
            if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                aCallback(anHttpRequest.responseText);
        }
        anHttpRequest.open( "GET", aUrl, true );
        anHttpRequest.send( null );

        /*
         GET usage:
            var client = new HttpClient();
            client.get('http://localhost:8082/hello', function(response) {
                console.log(response);
            });
        */
    }

    this.post = function(URL, params){
        var xhr = new XMLHttpRequest();
        xhr.open("POST", URL, true);

        xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

        xhr.send(params);

        /*
         POST usage:
            var client = new HttpClient();
            client.post('http://localhost:8082/goodbye', "param=something");
            //use "param0=asd&param1=dsa" for multiple
        */
    }
}