<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="spring"  %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8"/>
        <title>ورود به سامانه</title>

        <link href="<spring:url value="/resources/css/loginPage.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"></link>
        <link href="<spring:url value="/resources/css/icon.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>

        <script type="text/javascript" src="<spring:url value="/resources/js/login.js" htmlEscape="true"/>"></script>
    </head>
    <body>
            
        <div id="main">
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
        	<label class="errorMessage">
        	<%
        		if(session.getAttribute("login") != null)
        		{
        			if((boolean) session.getAttribute("login") == false)
						out.print("نام کاربری یا گذر واژه اشتباه است.");
        			session.removeAttribute("login");
        		}
        	%>
        	</label>
            <form method="post" action="<spring:url value="/login/webLogin" htmlEscape="true"/>" onsubmit="return validation();" target="_self" novalidate>
                <label id="usernamelbl">
                &rsaquo; نام کاربری:
                </label>
                <br/>
                <input class="textinput" id="username" name="username" type="text" minlength="8" maxlength="50" 
                placeholder="نام کاربری را وارد کنید." required />
                <br/>
                <label class="errorMessage" id="usernameValidationErrorMessage">
                </label>
                <label>
              &rsaquo; رمز ورود:
                </label>
                <br/>
                <input class="textinput" id="password" name="password" type="password" minlength="8" maxlength="50" required />
                <label class="errorMessage" id="passwordValidationErrorMessage">
                </label>
                <input id="remeberMeCheckBox" name="rememberMe" onclick="checkBoxOnClick('remeberMeCheckBox','remeberMeLabel');" type="checkbox" ><label id="remeberMeLabel">مرا به یاد داشته باش.</label>
            </div>
            <br/>
            <div id="buttonContainer">
                <button type="submit" id="button">ورود</button>
            </div>
            </form>

            <p id="forget"><a href="">
رمز عبور را فراموش کرده ام.!
            </a></p>

            <div id="footer">
                تمامی حقوق این سایت محفوظ می باشد. &copy;
                </div>
        </div>
    </body>
</html>