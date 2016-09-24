function validation() {

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

    return flag;
}

