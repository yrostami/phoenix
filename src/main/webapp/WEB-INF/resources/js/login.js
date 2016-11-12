if (!window.JSON) {
  window.JSON = {
    parse: function(sJSON) { return eval('(' + sJSON + ')'); },
    stringify: (function () {
      var toString = Object.prototype.toString;
      var isArray = Array.isArray || function (a) { return toString.call(a) === '[object Array]'; };
      var escMap = {'"': '\\"', '\\': '\\\\', '\b': '\\b', '\f': '\\f', '\n': '\\n', '\r': '\\r', '\t': '\\t'};
      var escFunc = function (m) { return escMap[m] || '\\u' + (m.charCodeAt(0) + 0x10000).toString(16).substr(1); };
      var escRE = /[\\"\u0000-\u001F\u2028\u2029]/g;
      return function stringify(value) {
        if (value == null) {
          return 'null';
        } else if (typeof value === 'number') {
          return isFinite(value) ? value.toString() : 'null';
        } else if (typeof value === 'boolean') {
          return value.toString();
        } else if (typeof value === 'object') {
          if (typeof value.toJSON === 'function') {
            return stringify(value.toJSON());
          } else if (isArray(value)) {
            var res = '[';
            for (var i = 0; i < value.length; i++)
              res += (i ? ', ' : '') + stringify(value[i]);
            return res + ']';
          } else if (toString.call(value) === '[object Object]') {
            var tmp = [];
            for (var k in value) {
              if (value.hasOwnProperty(k))
                tmp.push(stringify(k) + ': ' + stringify(value[k]));
            }
            return '{' + tmp.join(', ') + '}';
          }
        }
        return '"' + value.toString().replace(escRE, escFunc) + '"';
      };
    })()
  };
}

if (typeof XMLHttpRequest === "undefined") {
  XMLHttpRequest = function () {
    try { return new ActiveXObject("Msxml2.XMLHTTP.6.0"); }
    catch (e) {}
    try { return new ActiveXObject("Msxml2.XMLHTTP.3.0"); }
    catch (e) {}
    try { return new ActiveXObject("Microsoft.XMLHTTP"); }
    catch (e) {    
	 var msgContainer = document.getElementById("browserMsg");
	 msgContainer.className = "browser-msg";
	 msgContainer.innerHTML="مرورگر شما از AJAX پشتیبانی نمیکند.";}
  };
}

function submit() {
    var flag = true;

    var usernameVlidationErrorObj = document.getElementById("usernameValidationErrorMessage");
    var passwordVlidationErrorObj = document.getElementById("passwordValidationErrorMessage");
    var usernameObj = document.getElementById("username");
    var passwordObj = document.getElementById("password");

    usernameVlidationErrorObj.innerHTML="";
    passwordVlidationErrorObj.innerHTML="";

    usernameObj.className = usernameObj.className.replace(" error", "");
    passwordObj.className = passwordObj.className.replace(" error", "");

    if (usernameObj.value == "" || usernameObj.value.length < 8){
        usernameVlidationErrorObj.innerHTML = "&rsaquo; نام کاربری باید شامل حداقل 8 حرف باشد.</br>";
        usernameObj.className = usernameObj.className + " error";
        flag = false;
    }
     
    if (passwordObj.value == "" || passwordObj.value.length < 8) {
        passwordVlidationErrorObj.innerHTML = "&rsaquo; گذر واژه  باید شامل حداقل 8 حرف باشد.</br>";
        passwordObj.className = passwordObj.className + " error";
        flag = false;
    }

    if(flag)
    {
	var rememberMe = false;
	if(remeberMeCheckBox.checked){
	    rememberMe = true;}
	post(usernameObj.value, passwordObj.value, rememberMe);
    }
};

function checkBoxOnClick(checkBoxId,labelId){
    var checkBox = document.getElementById(checkBoxId); 
    var label = document.getElementById(labelId);
    if(checkBox.checked) label.className = label.className + " checked";
    else label.className = label.className.replace(" checked", "");
};

function post(username,password,rememberMe){
    var data = {};
    data.email = username;
    data.password = password;
    data.rememberMe = rememberMe;
    
    var xhr = new XMLHttpRequest();
    xhr.open('post', 'http://localhost:8080/phoenix/login/webLogin', true);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    var button = document.getElementById("button");
    button.innerHTML = '<div class="loader mini-loader"></div><span> صبر کنید</span>';
    button.disabled = true;
    xhr.send(JSON.stringify(data));
    xhr.onreadystatechange = function(){
	if(xhr.readyState == 4){
	    button.disabled = false;
	    if(xhr.status == 200)
		window.location.href = xhr.responseText;
	    else if(xhr.status !== 200){
		document.getElementById("failMsg").innerHTML = "ورود انجام نشد." + "<br />" + "نام کاربری یا گذر واژه اشتباه هست. ";
		document.getElementById("button").innerHTML = 'ورود';
		
	    }
	}
    }
};
