<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>سامانه اطلاع رسانی ققنوس</title>

<link href="<spring:url value="/resources/css/icon.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>
<link href="<spring:url value="/resources/css/iconAnimation.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>
<link href="<spring:url value="/resources/css/phoenix.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<spring:url value="/resources/js/angular.min.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/ngprogress.min.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/publisher/app.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/publisher/userService.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/allboardscontroller.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/myboardscontroller.js" htmlEscape="true"/>"></script>

</head>
<body ng-app="phoenix" ng-controller="publisher" ng-cloak>
<div id="pageLoading" ng-show="showPageLoading">
	<h3>
		درحال بار گذاری ...
	</h3><br/>
	<h5>لطفا صبر کنید.</h5>
	<div id="pageLoadingBar"></div>
</div>

<div id="container" ng-show="showApp">
	<div id="header">
		<div class="icon-container popup-container" ng-click="popupUserMenu('user-menu');">
			<i class="demo-icon icon-user-2"></i>
			<div id="user-menu" class="popup-content" >
				<div class="popup-item">
					<div class="mini-icon-container"><div><i class="demo-icon icon-info"></i></div></div>
					<span>
						اطلاعات کاربری من
					</span>
				</div>
				<hr/>
				<div class="popup-item" onClick="location.href='<spring:url value="/logout" htmlEscape="true"/>'">
					<div class="mini-icon-container"><div><i class="demo-icon icon-logout"></i></div></div>
					<span>
						خروج
					</span>
				</div>
			</div>
		</div>
		<span class="header-item">{{user.displayName}}</span>

		<div class="notification-icon"><i class="demo-icon icon-bell"></i>
			<div id="tooltip">
				<div class="popup-item"><span>{{notification.message}}</span></div>
			</div>
		</div>
	</div>

		<div id="progressBar"></div>

	<div id="content">
		<div id="tabs">
			<div class="tab" id="news" ng-click="openTab(0);">
				اطلاعیه های جدید
			</div>
			<div class="tab" id="myBoards" ng-click="openTab(1);">
				بردهای من
			</div>
			<div class="tab" id="allBoards" ng-click="openTab(2);">
				همه ی بردها
			</div>
		</div>
		
		<!-- subscribed boards panel -->
		<div class="tabContent" ng-hide="tabsHide[0]">
		<div class="toolsbar">
		<div class="buttons"></div>
			<div class="tools"></div>
		</div>
			<div class="list">
			<div class="list-item" ng-click="showPosts(-1)">
				<span class="item-name wide-item selected">
					همه اطلاعیه های جدید
				</span>
			</div>
			
			<div class="list-item" ng-repeat="board in user.subscribedBoards">
				<div class="item-modal-btn" ng-click="showSubscribedInfo($index)"><i class="demo-icon icon-ellipsis-vert"></i></div>
				<span class="item-name" ng-click="showPosts($index)">
					{{board.name}}
				</span>
			</div>
			</div>
			
			<div id="subscribedBoradPostPane" class="list-item-content"></div>
			
		</div>
		
		<!-- my boards panel -->
		<div class="tabContent" ng-hide="tabsHide[1]" ng-controller="myboardscontroller">
		<div ng-show="myBoardsMainPanelShow">
		<div class="toolsbar">
			<div class="button" ng-click="showNewBoardPanel()">
			<div class="btn-icon">
			<i class="demo-icon  icon-plus-1"></i>
			</div>
			<span>
			ایجاد برد جدید 
			</span>
			</div>
		</div>
			<div class="list">
			<div class="list-item" ng-repeat="board in user.myBoards">
				<div class="item-modal-btn" ng-click="showMyBoardInfo($index)">
					<i class="demo-icon icon-ellipsis-vert"></i>
				</div>
				<span class="item-name" ng-click="showMyPosts($index)">
					{{board.name}}
				</span>
			</div>	
			</div>
			
			<div id="myBoradPostPane" class="list-item-content"></div>			
			
		</div>
		
		<div class="form-container" ng-show="newBoardPanelShow">
			<div class="fom-content">
			<label class="label">&rsaquo; نام برد اطلاع رسانی جدید:</label>
			<input ng-model="newBoard.name" class="input" type="text" placeholder="نام را اینجا وارد کنید">
			<label class="vlidation-message" ng-hide="validationMessageHide[0]">&rsaquo; نام برد اطلاع رسانی باید شامل حداقل پنج و حداکثر صد حرف باشد.</label>
			<label class="label">&rsaquo; دسته بندی برد اطلاع رسانی جدید:</label>
			<select ng-model="selectedItemValue" ng-focus="selectFirstOptionShow = false" class="input" id="categorySelect">
				<option ng-show="selectFirstOptionShow" value="-1" selected="selected">برای انتخاب کلیک کنید.</option>
				<option ng-repeat="category in allCategories" value="{{$index}}">{{category.name}}</option>
			</select>
			<label class="vlidation-message" ng-hide="validationMessageHide[1]">&rsaquo; باید یک دسته بندی معتبر انتخاب کنید.</label>
			<label class="label">&rsaquo; درباره برد اطلاع رسانی جدید:</label>
			<textarea ng-model="newBoard.about" class="input texterea" rows="5" cols="40" maxlength="1000" placeholder="درباره این برد اطلاع رسانی بنویسید ..." ></textarea>
			<label class="vlidation-message" ng-hide="validationMessageHide[2]">&rsaquo; درباره برد نباید خالی باشد.</label>
			</div>
		
			<div class="buttons-bar">
				<div class="form-btn" ng-click="hideNewBoardPanel()">
					<span>
						منصرف شدم
					</span>
				</div>
				<div class="form-btn" ng-click="validationAndSend()">
					<span>
						ایجاد کن
					</span>
				</div>
			</div>
		</div>
		
		</div>

		<!-- all boards panel -->
		<div class="tabContent" ng-controller="allboardscontroller" ng-hide="tabsHide[2]">
		<div class="toolsbar">
			<div class="button" ng-click="reload()">
			<span>
			بار گذاری مجدد
			</span>
			</div>
			<div class="tools"><input type="text" class="input" ng-model="searchBoardName" placeholder="جستجوی برد ..." ></div>
		</div>
		<div class="list">
			<div class="list-item" ng-click="filterByCat(-1)">
				<span id="catItem-1" class="item-name wide-item selected">
					همه دسته ها
				</span>
			</div>
			<div class="list-item" ng-repeat="category in allCategories">
				<span id="catItem{{$index}}" class="item-name wide-item" ng-click="filterByCat($index)">
					{{category.name}}
				</span>
			</div>
			</div>
			
		<div class="list-item-content" >
			<div class="accordion-item" ng-repeat="board in allBoards | filter: filterByCatObj | filter: {name:searchBoardName}">
				<div class="accordion-item-header">
					<div class="accordion-item-info" id="acrdninfo{{$index}}" ng-click="openOrCloseAccordion($index)">
						<div class="btn-icon">
							<i class="demo-icon  icon-down-open" id="acrdnicn{{$index}}"></i>
						</div>
						<span class="accordion-item-name">
							{{board.name}}
						</span>
					</div>
					<div id="flwbtn{{$index}}" class="follow-btn" ng-click="subscribe($index)">
						<div class="btn-icon">
							<i class="demo-icon  icon-plus-1"></i>
						</div>
						<span>
							دنبال کن
						</span>
					</div>
					
					<div id="flwbtnwt{{$index}}" class="follow-btn hidden">
						<div class="btn-icon animate-spin">
							<i class="demo-icon  icon-spin4"></i>
						</div>
						<span>
							صبر کنید
						</span>
					</div>
					
				</div>
				<div class="accordion-item-content" id="acrdn{{$index}}">
				<p>صاحب برد: {{board.publisher.displayName}}</p>
				<p>درباره این برد: </p>
				<P>{{board.about}}</P>
				</div>
			</div>
		</div>
		</div>
	</div>

</div>

</body>
</html>