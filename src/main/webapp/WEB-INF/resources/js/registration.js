if (!window.JSON) {
    window.JSON = {
	parse : function(sJSON) {
	    return eval('(' + sJSON + ')');
	},
	stringify : (function() {
	    var toString = Object.prototype.toString;
	    var isArray = Array.isArray || function(a) {
		return toString.call(a) === '[object Array]';
	    };
	    var escMap = {
		'"' : '\\"',
		'\\' : '\\\\',
		'\b' : '\\b',
		'\f' : '\\f',
		'\n' : '\\n',
		'\r' : '\\r',
		'\t' : '\\t'
	    };
	    var escFunc = function(m) {
		return escMap[m] || '\\u'
			+ (m.charCodeAt(0) + 0x10000).toString(16).substr(1);
	    };
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
			for ( var k in value) {
			    if (value.hasOwnProperty(k))
				tmp.push(stringify(k) + ': '
					+ stringify(value[k]));
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
    XMLHttpRequest = function() {
	try {
	    return new ActiveXObject("Msxml2.XMLHTTP.6.0");
	} catch (e) {
	}
	try {
	    return new ActiveXObject("Msxml2.XMLHTTP.3.0");
	} catch (e) {
	}
	try {
	    return new ActiveXObject("Microsoft.XMLHTTP");
	} catch (e) {
	    var msgContainer = document.getElementById("browserMsg");
	    msgContainer.className = "browser-msg";
	    msgContainer.innerHTML = "مرورگر شما از AJAX پشتیبانی نمیکند.";
	}
    };
}

var emailChanged = false;
var emailValidation = false;

function submit() {
    var displayName = document.getElementById("displayName");
    var password = document.getElementById("password");
    var passwordRepeat = document.getElementById("passwordRepeat");

    var emailValidationMsg = document.getElementById("emailValidationMsg");
    var displayNameValidationMsg = document
	    .getElementById("displayNameValidationMsg");
    var passwordValidationMsg = document
	    .getElementById("passwordValidationMsg");
    var passwordRepeatValidationMsg = document
	    .getElementById("passwordRepeatValidationMsg");

    displayNameValidationMsg.innerHTML = "";
    passwordValidationMsg.innerHTML = "";
    passwordRepeatValidationMsg.innerHTML = "";

    displayName.className = displayName.className.replace(" error", "");
    password.className = password.className.replace(" error", "");
    passwordRepeat.className = passwordRepeat.className.replace(" error", "");
    
    if(!emailChanged)
    {
	emailValidationMsg.innerHTML = "&rsaquo; ایمیل معتبر نیست.<br />";
	email.className = email.className + " error";
    }
    
    var flag = emailValidation;
    
    if (!isValidDisplayName(displayName.value)) {
	displayNameValidationMsg.innerHTML = "&rsaquo; نام نمایشی نباید خالی باشد.<br />";
	displayName.className = displayName.className + " error";
	flag = false;
    }

    if (password.value == "" || password.value.length < 8) {
	passwordValidationMsg.innerHTML = "&rsaquo; گذر واژه  باید شامل حداقل 8 حرف باشد.<br />";
	password.className = password.className + " error";
	flag = false;
    }

    if (passwordRepeat.value == "" || passwordRepeat.value !== password.value) {
	passwordRepeatValidationMsg.innerHTML = "&rsaquo; تکرار گذر واژه معتبر نیست.<br />";
	passwordRepeat.className = passwordRepeat.className + " error";
	flag = false;
    }

    if (flag) {
	postRegisterInfo(email.value, password.value, displayName.value);
    }
}

function isValidDisplayName(displayName) {
    if (displayName.length == 0 || displayName == null)
	return false;
    var valid = false;
    for (var i = 0; i < displayName.length; i--) {
	if (displayName.charAt(i) !== ' ') {
	    valid = true;
	    break;
	}
    }
    return valid;
}

function isValidEmail(email) {
    var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function postRegisterInfo(email, password, displayName) {
    var data = {};
    data.email = email;
    data.password = password;
    data.displayName = displayName;

    var xhr = new XMLHttpRequest();
    xhr.open('post', 'http://localhost:8080/phoenix/registration', true);
    xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');
    var button = document.getElementById("button");
    button.innerHTML = '<div class="loader mini-loader"></div><span> صبر کنید</span>';
    button.disabled = true;
    xhr.send(JSON.stringify(data));
    xhr.onreadystatechange = function() {
	if (xhr.readyState == 4) {
	    button.disabled = false;
	    if (xhr.status == 200) {
		data = {};
		data = JSON.parse(xhr.responseText);
		if (data.success) {
		    document.getElementById("formContainer").style.display = 'none';
		    document.getElementById("resultMsg").innerHTML = data.massege.replace(/\n/g , " <br /> ");
		    document.getElementById("successMsgPanel").style.display = 'block';
		}
	    } else if (xhr.status !== 200) {
		document.getElementById("failMsg").innerHTML = "انجام نشد."
		document.getElementById("button").innerHTML = 'ثبت کن';
	    }
	}
    }
}

function goToLoginPage()
{
    window.location.href = 'http://localhost:8080/phoenix/';
}