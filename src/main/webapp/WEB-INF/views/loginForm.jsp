<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"  %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>ورود به سامانه</title>

        <link href="<spring:url value="/resources/css/loginPage.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"></link>
        <link href="<spring:url value="/resources/css/phoenix-icons.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="<spring:url value="/resources/js/login.js" htmlEscape="true"/>"></script>
    </head>
    <body>
            <div id="browserMsg"></div>
        <div class="main login-form" >
        <div id="header">
        <div class="icon-container">
        	<div>
        		<i class="demo-icon icon-login"></i>
			</div>
        </div>
        <div id="header-title">
            ورود به سامانه
        </div>
        </div>

        <div id="content">
        
            <div>
                <label>
                &rsaquo; نام کاربری:
                </label>
                <br/>
                <input class="textinput" id="username" name="username" type="text" minlength="8" maxlength="50" 
                placeholder="ایمیل را وارد کنید."/>
                <br/>
                <label class="errorMessage" id="usernameValidationErrorMessage">
                </label>
                <label>
              &rsaquo; گذرواژه:
                </label>
                <br/>
                <input class="textinput" id="password" name="password" type="password" minlength="8" maxlength="50"
                placeholder="گذرواژه را وارد کنید."/>
                <label class="errorMessage" id="passwordValidationErrorMessage">
                </label>
                <input id="remeberMeCheckBox" name="rememberMe" onclick="checkBoxOnClick('remeberMeCheckBox','remeberMeLabel');" type="checkbox" ><label id="remeberMeLabel">مرا به یاد داشته باش.</label>
            </div>
            <br/>
            <div class="buttonContainer">
                <button id="button" class="button" onclick="submit();" >ورود</button>
            </div>
            <label class="errorMessage" id="failMsg">
                </label>
            </div>
            
            <p class="link"><a href="<spring:url value="/registration" htmlEscape="true"/>">
				ثبت نام در سامانه
            </a></p>
            
            <!-- <p class="link"><a href="">
				رمز عبور را فراموش کرده ام.!
            </a></p> -->

            <div id="footer">
                تمامی حقوق این سایت محفوظ می باشد. &copy;
                </div>
        </div>
    </body>
</html>