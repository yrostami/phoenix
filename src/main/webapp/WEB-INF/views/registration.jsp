<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>ثبت نام در سامانه</title>

<link
	href="<spring:url value="/resources/css/loginPage.css" htmlEscape="true"/>"
	rel="stylesheet" type="text/css"></link>
<link
	href="<spring:url value="/resources/css/phoenix-icons.css" htmlEscape="true"/>"
	rel="stylesheet" type="text/css" />

<script type="text/javascript"
	src="<spring:url value="/resources/js/registration.js" htmlEscape="true"/>"></script>
</head>
<body>
	<div id="browserMsg"></div>
	<div id="formContainer" class="main registration-form">
		<div id="header">
			<div class="icon-container">
				<div>
					<i class="demo-icon icon-pencil"></i>
				</div>
			</div>
			<div id="header-title">ثبت نام</div>
		</div>

		<label class="xyz">اطلاعات درخواستی را با دقت وارد کنید.</label> <label
			class="xyz">همه موارد ضروری هستند.</label>

		<div id="content">
			<div>
				<label> &rsaquo; ایمیل: </label> <br /> 
				<input class="textinput" id="email" type="text" placeholder="ایمیل را وارد کنید." /> <br />
				<label class="errorMessage" id="emailValidationMsg"> </label> 
				<label> &rsaquo; نام نمایشی: </label> <br /> 
				<input class="textinput" id="displayName" type="text" minlength="1" maxlength="100"
					placeholder="نام نمایشی را وارد کنید." /> 
					<label class="errorMessage" id="displayNameValidationMsg"> </label> 
					<label>&rsaquo; گذرواژه: </label> <br /> 
					<input class="textinput" id="password" type="password" minlength="8" maxlength="50"
					placeholder="گذرواژه را وارد کنید." /> 
					<label class="errorMessage" id="passwordValidationMsg"> </label> 
					<label> &rsaquo; تکرار گذرواژه: </label> <br /> 
					<input class="textinput" id="passwordRepeat"
					type="password" minlength="8" maxlength="50" placeholder="گذرواژه را دوباره وارد کنید." /> 
					<label class="errorMessage" id="passwordRepeatValidationMsg"> </label>

				<div class="buttonContainer">
					<button id="button" class="button" onclick="submit()">ثبت کن</button>
				</div>
				<label class="errorMessage" id="failMsg"> </label>
			</div>
		</div>
		<div id="footer">تمامی حقوق این سایت محفوظ می باشد. &copy;</div>
	</div>
	<script type="text/javascript">
	(function initialize() {
	    var email = document.getElementById('email');
	    var emailValidationMsg = document.getElementById("emailValidationMsg");
	    
	var emailChangeHandler = function() {
	    
	    emailChanged = true;
	    
	    email.className = email.className.replace(" error", "");
	    emailValidationMsg.innerHTML = "";
	    
	    if (!isValidEmail(email.value)) {
		emailValidationMsg.innerHTML = "&rsaquo; ایمیل معتبر نیست.<br />";
		email.className = email.className + " error";
		emailValidation = false;
	    } else {
		var xhr = new XMLHttpRequest();
		xhr.open(
				'post',
				'http://localhost:8080/phoenix/registration/emailValidation',
				true);
		xhr.setRequestHeader('Content-Type',
			'text/plain; charset=UTF-8');
		xhr.send(email.value);
		xhr.onreadystatechange = function() {
		    if (xhr.readyState == 4) {
			if (xhr.status == 200) {
			    data = {};
			    data = JSON.parse(xhr.responseText);
			    if (data.success) {
				emailValidation = true;
			    } else {
				emailValidationMsg.innerHTML = "&rsaquo; "+data.massege+"<br />";
				email.className = email.className + " error";
				emailValidation = false;}}}
		}
	    }};
	    if(email.value !== "") emailChangeHandler();	    
	    email.onchange = emailChangeHandler; 
	})();
    </script>
    
    <div id="successMsgPanel" class="result-msg-container">
    <div id="resultMsg"></div>
    	<div class="buttonContainer mini">
			<button id="button" class="button" onclick="goToLoginPage()">ورود به سامانه</button>
		</div>
    </div>
    
</body>
</html>