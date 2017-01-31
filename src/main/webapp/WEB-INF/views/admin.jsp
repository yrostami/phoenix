<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>پنل مدیریت سامانه بردهای اطلاع رسانی ققنوس</title>

<link href="<spring:url value="/resources/css/phoenix-icons.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>
<link href="<spring:url value="/resources/css/phoenix.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<spring:url value="/resources/js/angular.min.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/ngprogress.min.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/admin/app.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/adminControllers/requests.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/adminControllers/users.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/adminControllers/settings.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/service/adminService.js" htmlEscape="true"/>"></script>
</head>
<body ng-app="phoenix" ng-controller="adminMainController" ng-cloak>

<div id="globalMsg"></div>
<!-- <div id="pageLoading" ng-show="showPageLoading">
	<h3>
		درحال بار گذاری ...
	</h3><br/>
	<h5>لطفا صبر کنید.</h5>
	<div id="pageLoadingBar"></div>
</div> -->

<!-- ng-show="showApp" -->

<div id="container">

	<div id="header">
		<div id="progressBar"></div>
		<div class="icon-container popup-container" ng-click="popupUserMenu('user-menu');">
			<i class="demo-icon icon-user"></i>
			<div id="user-menu" class="popup-content" >
			<div class="popup-item" ng-click="logout()">
					<div class="mini-icon-container"><div><i class="demo-icon icon-logout"></i></div></div>
					<span>
						خروج
					</span>
				</div>
			</div>
		</div>
		<span class="header-item">مدیر سامانه</span>
	</div>
	
	<div id="content">
		<div id="tabs">
			<div class="tab" id="settings" ng-click="openTab(0)">
				تنظیمات
			</div>
			<div class="tab" id="users" ng-click="openTab(1)">
				کاربران
			</div>
			<div class="tab" id="requests" ng-click="openTab(2)">
				درخواست ها
			</div>
		</div>
		
		<!-- Settings controller -->
		<div class="tabContent" ng-show="tabsShow[0]" ng-controller="settingsController">
		
		<fieldset id="userInfoFieldset1" class="userInfo-fieldset">
		<legend ng-click="openOrCloseFieldset('1')">
		<div class="btn-icon">
			<i id="fieldset-icon1" class="demo-icon  icon-down-open" ></i>
		</div>
		<span>تغییر گذرواژه</span>
		</legend>
	
		<label class="label">گذرواژه فعلی:</label>
		<input class="input" maxlength="50" type="password" id="passwordForUpdate" placeholder="گذرواژه فعلی">
		<label class="vlidation-message" id="passForUpdateValidationMsg"></label>
		<label class="label">گذرواژه جدید:</label>
		<input class="input" maxlength="50" type="password" id="newPassword" placeholder="گذرواژه جدید">
		<label class="vlidation-message" id="newPassValidationMsg"></label>
		<div class="buttons-bar">
		<div class="form-btn" ng-click="updatePassword()">
			<span>
				اعمال تغییر
			</span>
		</div>
		</div>
		
	</fieldset>
	
	<fieldset id="userInfoFieldset2" class="userInfo-fieldset">
	<legend ng-click="openOrCloseFieldset('2')">
		<div class="btn-icon">
			<i id="fieldset-icon2" class="demo-icon  icon-down-open" ></i>
		</div>
		<span>تغییر حجم فضای آپلود کاربران</span>
	</legend>
	
		<label class="label">مقدار فعلی:</label> {{systemInfo.maxStroge /1024 /1024}}	
		
		<label class="label">گذرواژه:</label>
		<input class="input" type="password" maxlength="50" placeholder="گذرواژه" id="passwordForNameUpdate">
		<label class="vlidation-message" id="PassFNUValidationMsg"></label>
		<label class="label">مقدار جدید (مگا بایت):</label>
		<input class="input" type="number" id="newMaxStorage" value="{{systemInfo.maxStroge /1024 /1024}}" placeholder="مقدار جدید">
		<label class="vlidation-message" id="newMaxStorageValidationMsg"></label>
		<div class="buttons-bar">
		<div class="form-btn" ng-click="updateMaxStorage()">
			<span>
				اعمال تغییر
			</span>
		</div>
	</div>
		
</fieldset>

<fieldset id="userInfoFieldset3" class="userInfo-fieldset">
	<legend ng-click="openOrCloseFieldset('3')">
		<div class="btn-icon">
			<i id="fieldset-icon3" class="demo-icon  icon-down-open" ></i>
		</div>
		<span>دسته بندی</span>
	</legend>
	
		<label class="label">ایجاد دسته بندی جدید:</label>
		
		<label class="label">نام دسته:</label>
		<input class="input float-right" type="text" maxlength="100" placeholder="نام دسته جدید" id="newCategoryName">
		<label class="vlidation-message" id="categoryNameValidationMsg"></label>
		<div class="form-btn float-right-btn" ng-click="sendNewCategory()">
			<span>
				ایجاد
			</span>
		</div>
		
		<div style="clear:both;"></div>
		
		<div class="loader normal-loader" ng-show="allCategoriesLoadingSpinnerShow"></div>
		<label class="label">تمامی دسته ها:</label>
		<div class="category-name" ng-repeat="category in allCategories"
		ng-click="showCategoryEditPanel(category.id, $index)">
			<span>{{category.name}}</span>
		</div>
		
		<div class="no-selected-msg" ng-show="allCategories.length == 0 && allCategoriesLoaded">
				هیچ دسته ای وجود ندارد. 
		</div>
		
</fieldset>


		<div class="modal" id="categoryInfoPanel">
			<div class="modal-content dialog">
				<label class="label">نام دسته:</label>
				<input class="input" type="text" placeholder="نام دسته" maxlength="100"  
				ng-change="categoryNameChangeCheck()" ng-model="selectedCategoryNewName">
				<br>
				<label class="vlidation-message">فقط دسته هایی که هیچ بردی ندارند قابل حذف هستند.</label>
				<div class="buttons-bar">
				<div class="form-btn" onclick="document.getElementById('categoryInfoPanel').style.display='none'">
					<span>بازگشت</span>
				</div>
				
				<div ng-click="updateCategoryName()" 
				ng-class="{'disable-btn': !categoryNameChange , 'form-btn': categoryNameChange}">
					<span>اعمال تغییر</span>
				</div>
				
				<div class="form-btn" ng-click="deleteSelectedCategory()"><span>حذف</span></div>
			</div>
		</div>

		</div>
	</div>
		
		<!-- Users controller -->
		<div class="tabContent" ng-show="tabsShow[1]" ng-controller="usersController">
		<div class="list">
		<br>
		<input class="input" type="text" ng-model="searchWord" placeholder="جستجو ...">
		<label class="vlidation-message" ng-hide="searchWordValidationMsg">&rsaquo; عبارت جستجو نباید خالی باشد.</label>
			<label class="label">جستجو در :</label>
			 <select id="roleSelection" class="input">
  				<option value="1" selected>همه کاربران</option>
  				<option value="2">دنبال کننده ها</option>
  				<option value="3">منتشر کننده ها</option>
			</select> 
			
			<label class="label">جستجو بر اساس :</label>
			 <select id="typeSelection" class="input">
  				<option value="1" selected>نام</option>
  				<option value="2">ایمیل</option>
			</select>
			
			<div class="buttons-bar">
					<div class="form-btn" ng-click="searchUsers()" >
						<span>
							جستجو کن
						</span>
					</div>
			</div>
			
			</div>
			
			<div class="list-item-content">
				
				<div class="users-item" ng-repeat="user in users" ng-click="showUserDetail(user.id)">
					<span class="user-info-cell">نام :</span>
					<span>{{user.displayName}}</span>
					<span class="user-info-cell">نوع دسترسی :</span>
					<span ng-if="user.role == 'Publisher'">منتشر کننده</span>
					<span ng-if="user.role == 'Subscriber'">دنبال کننده</span>
					<!-- <span>ایمیل :</span>
					<span>{{user.email}}</span> -->
				</div>
				
			</div>
			
			<div class="modal" id="userDetailModal">
				<div class="modal-content board-info">
				
				<div class="board-info-container">
				
				<div class="board-info-content">
				<p>نام: {{selectedUser.displayName}}</p>
				<p>نوع دسترسی:  
					<span ng-if="selectedUser.role == 'Publisher'">منتشر کننده</span>
					<span ng-if="selectedUser.role == 'Subscriber'">دنبال کننده</span>
				</p>
				<p>ایمیل: {{selectedUser.email}}</p>
				<P ng-if="selectedUser.role == 'Publisher'">حافظه استفاده شده: {{selectedUser.strogeUsage /1024}} KB</P>
				
				</div>
				
				<div class="buttons-bar">
					<div class="form-btn" ng-click="hideUserDetail()">
						<span>
							بازگشت	
						</span>
					</div>
					<div class="form-btn" ng-click="deleteUser()">
						<span>
							حذف کاربر	
						</span>
					</div>
				<div class="form-btn" ng-if="selectedUser.role == 'Publisher'" ng-click="switchUserRoleToSubscriber()">
					<span>
						تبدیل به دنبال کننده
					</span>
				</div>
			</div>
				
			</div>
				
				
		</div>
		</div>
			
		</div>
		
		<!-- Requests controller -->
		<div class="tabContent" ng-show="tabsShow[2]" ng-controller="requestsController">
		
		<div class="publish-req" ng-repeat="req in requests | orderBy : '-creationDate'">
		
		<div class="publish-req-info">
		<span class="publish-req-info-cell">
				نام :<span>{{req.user.displayName}}</span>
		</span>
		<span class="publish-req-info-cell">
				ایمیل :<span>{{req.user.email}}</span>
		</span>
		</div>
		
		<div class="publish-req-info">
			<span class="publish-req-info-cell">{{getDate(req.creationDate)}}</span>	
			<span class="publish-req-info-cell" ng-show="req.checked">
				موافقت شده :
				<span ng-if="req.agreement">بله</span>
				<span ng-if="!req.agreement">خیر</span> 
			</span>	
		</div>
		
		<div class="publish-req-description" ng-bind-html="getMultiline(req.description)"></div>
		<div class="buttons-bar" ng-show="!req.checked">
				<div class="form-btn" ng-click="escapeReq(req.id)">
					<span>
						رد درخواست
					</span>
				</div>
				<div class="form-btn" ng-click="acceptReq(req.id)">
					<span>
						موافقت با درخواست
					</span>
				</div>
			</div>
		
		
	</div>
	
	<div class="more-post" ng-click="loadMoreReqs()">
					<div ng-show="!moreReqsLoad">
						<span>درخواست های قدیمی تر</span>
					</div>
					<div ng-show="moreReqsLoad">
						<div class="loader mini-loader"></div>
						<span>
							در حال بارگذاری
						</span>
					</div>
				</div>
	
	</div>
		
	</div>

</div>

</body>
</html>