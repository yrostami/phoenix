if (typeof XMLHttpRequest === "undefined") {
  XMLHttpRequest = function () {
    try { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); }
    catch (e) {}
    try { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); }
    catch (e) {}
    try { return new ActiveXObject("Microsoft.XMLHTTP"); }
    catch (e) {    
	document.getElementById("browserMsg").innerHTML="مرورگر شما از AJAX پشتیبانی نمیکند.";}
  };
}

function submit() {

    console.log("submit()");
    var flag = true;

    var usernameVlidationErrorObj = document.getElementById("usernameValidationErrorMessage");
    var passwordVlidationErrorObj = document.getElementById("passwordValidationErrorMessage");
    var usernameObj = document.getElementById("username");
    var passwordObj = document.getElementById("password");

    usernameVlidationErrorObj.innerHTML="";
    passwordVlidationErrorObj.innerHTML="";

    usernameObj.className = usernameObj.className.replace(" error", "");
    passwordObj.className = passwordObj.className.replace(" error", "");

    if (usernameObj.value == "" || usernameObj.value.length < 8 || usernameObj.value.length > 50){
        usernameVlidationErrorObj.innerHTML = "&rsaquo; نام کاربری باید شامل حداقل 8 وحداکثر 50 حرف باشد.</br>";
        usernameObj.className = usernameObj.className + " error";
        flag = false;
    }
     
    if (passwordObj.value == "" || passwordObj.value.length < 8 || passwordObj.value.length > 50) {
        passwordVlidationErrorObj.innerHTML = "&rsaquo; گذر واژه  باید شامل حداقل 8 وحداکثر 50 حرف باشد.</br>";
        passwordObj.className = passwordObj.className + " error";
        flag = false;
    }

    if(flag)
    {
	console.log("if flag");
	var rememberMe = false;
	if(remeberMeCheckBox.checked){
	    rememberMe = true;}
	post(usernameObj.value, passwordObj.value, rememberMe);
    }
};

var checkBoxOnClick = function(checkBoxId,labelId){
    var checkBox = document.getElementById(checkBoxId); 
    var label = document.getElementById(labelId);
    if(checkBox.checked) label.classList.add("checked");
    else label.classList.remove("checked");
};

function post(username,password,rememberMe){
    console.log("post");
    var data = {};
    data.username = username;
    data.password = password;
    data.rememberMe = rememberMe;
    
    var xhr = new XMLHttpRequest();
    xhr.open('post', 'http://localhost:8080/phoenix/login/webLogin', false);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

    xhr.send(JSON.stringify(data));
    if(xhr.status == 200)
	  window.location.href = xhr.responseText;
    else if(xhr.status !== 200)
	  document.getElementById("failMsg").innerHTML = "ورود انجام نشد.";
};
