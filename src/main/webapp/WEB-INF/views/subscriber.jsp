<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title> سامانه بردهای اطلاع رسانی ققنوس</title>

<link href="<spring:url value="/resources/css/phoenix-icons.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>

<link href="<spring:url value="/resources/css/phoenix.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<spring:url value="/resources/js/angular.min.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/ngprogress.min.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/subscriber/app.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/service/userService.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/allboardscontroller.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/postscontroller.js" htmlEscape="true"/>"></script>

</head>
<body ng-app="phoenix" ng-controller="subscriber" ng-cloak>

<div id="globalMsg"></div>
<div id="pageLoading" ng-show="showPageLoading">
	<h3>
		درحال بار گذاری ...
	</h3><br/>
	<h5>لطفا صبر کنید.</h5>
	<div id="pageLoadingBar"></div>
</div>

<div id="container" ng-show="showApp">
	<div id="header">
	<div id="progressBar"></div>
		<div class="icon-container popup-container" ng-click="popupUserMenu('user-menu');">
			<i class="demo-icon icon-user"></i>
			<div id="user-menu" class="popup-content" >
				<div class="popup-item" ng-click="showUserInfoPanel()">
					<div class="mini-icon-container"><div><i class="demo-icon icon-info"></i></div></div>
					<span >
						اطلاعات کاربری من
					</span>
				</div>
				<div class="popup-item" ng-click="logout()">
					<div class="mini-icon-container"><div><i class="demo-icon icon-logout"></i></div></div>
					<span>
						خروج
					</span>
				</div>
			</div>
		</div>
		<span class="header-item">{{user.displayName}}</span>
		
	</div>

	<div id="content" ng-show="mainContentShow">
		<div id="tabs">
			<div class="tab subscriber-tab" id="news" ng-click="openTab(0);">
				اطلاعیه های جدید
			</div>
			
			<div class="tab subscriber-tab" id="allBoards" ng-click="openTab(1);">
				همه ی بردها
			</div>
		</div>
		
		<!-- subscribed boards panel -->
		<div class="tabContent" ng-hide="tabsHide[0]" ng-controller="postscontroller">
		<div>
		<div class="toolsbar">
		<div class="buttons"></div>
			<div class="tools"></div>
		</div>
			<div class="list">
			
			<div class="list-item" ng-show="user.subscribedBoards.length == 0">
				<span class="item-name wide-item no-item">
					هیچ بردی وجود ندارد.
				</span>
			</div>
					
			<div class="list-item" ng-repeat="board in user.subscribedBoards">
				<div class="item-modal-btn" ng-click="showSubscribedBoardInfo($index, board.id)">
					<i class="demo-icon icon-ellipsis-vert"></i>
				</div>
				<span id="board{{$index}}" class="item-name" ng-click="showPosts($index, board)">
					{{board.name}}
				</span>
				<span ng-if="board.notificationCount > 0" class="notification-count">{{board.notificationCount}}</span>
			</div>
			</div>
			
			<div class="list-item-content">
			
			<div class="no-selected-msg" ng-show="user.subscribedBoards.length == 0">
				شما هیچ بردی را دنبال نمی کنید.<br />
				برای دنبال کردن بردها به قسمت "همه ی بردها" بروید. 
			</div>
			
			<div class="no-selected-msg" ng-show="user.subscribedBoards.length !== 0 && selectedBoardIndex == -1">
				برای دیدن اطلاعیه ها روی برد مورد نظر کلیک کنید. 
			</div>
			
			<div class="board-content">
				<div class="post-container" ng-repeat="post in targetPosts | unique : 'id' | orderBy : '-creationDate' ">
				<div class="post-title"><span>{{post.title}}</span></div>
				
				<div class="image-div" id="postImageDiv{{$index}}"></div>
				<div id="postContent{{$index}}" class="post-content">
					<p class="post-text" ng-bind-html="getMultiline(post.content)"></p>
					<p class="post-board-name" ng-if="selectedBoardIndex == -1">برد اطلاع رسانی {{getBoardName(post.boardId)}}</p>
					<div class="post-footer">
						<span id="postImageTitle{{$index}}" ng-if="post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') !== -1">
						این اطلاعیه دارای عکس می باشد:</span> 
						<span ng-if="post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') == -1">
						این اطلاعیه دارای فایل می باشد:</span>
						<div class="download-file">
							<span ng-if=" post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') !== -1" 
								id="postImageViewTitle{{$index}}" ng-click="getImage($index, post.fileInfo.filePath)">نمایش داده شود</span>
								
    						<a ng-if="post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') == -1"
    						 target="_blank" ng-href="/phoenix/subscriber/getfile{{post.fileInfo.filePath}}">دانلود شود</a>
						</div>
						<div id="postImageLoader{{$index}}" class="loader micro-loader post-image-loader" 
								ng-if=" post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') !== -1"></div>
						<span class="post-date-container">{{getDate(post.creationDate)}}</span>
					</div>
				</div>
				</div>
				
				<div class="no-selected-msg" ng-show="targetPosts.length == 0 && selectedBoardIndex !== -1">
				هیچ اطلاعیه ای وجود ندارد. 
				</div>
				
				<div class="more-post" ng-click="loadMorePosts()" ng-show="targetPosts.length !== 0 && selectedBoardIndex !== -1">
					<div ng-show="!morePostLoad">
						<span ng-show="!loadFail">اطلاعیه های قدیمی تر</span>
						<span ng-show="loadFail">تلاش دوباره</span>
					</div>
					<div ng-show="morePostLoad">
						<div class="loader mini-loader"></div>
						<span>
							صبر کنید
						</span>
					</div>
				</div>
				
				</div>
			
			</div>
			</div>
			
			<div class="modal" id="boardInfoModal">
				<div class="modal-content board-info">
				
				<div class="board-info-container">
				<div class="board-info-header" >
					<span>
						{{showBoard.name}}
					</span>
				</div>
				<div class="board-info-content">
				<p>صاحب برد : {{showBoard.publisher.displayName}}</p>
				<p>دسته بندی : {{showBoard.category.name}}</p>
				<p>درباره این برد : </p>
				<P ng-bind-html="getMultiline(showBoard.about);" ></P>
				
				<label class="label">
			<span>تعداد دنبال کننده ها: </span>
			<span ng-if="showBoard.subscriberCount !== undefined">{{showBoard.subscriberCount}}</span>
			<div class="loader micro-loader" ng-if="showBoard.subscriberCount == undefined"></div>
			</label>
			
			<label class="label">
			<span>تعداد پست ها: </span>
			<span ng-if="showBoard.postCount !== undefined">{{showBoard.postCount}}</span>
			<div class="loader micro-loader" ng-if="showBoard.postCount == undefined"></div>
			</label>
				</div>
				
				<div class="buttons-bar">
					<div class="form-btn" onclick="document.getElementById('boardInfoModal').style.display='none';">
						<span>
							بستن	
						</span>
					</div>
				<div class="form-btn" ng-click="unsubscribe()">
					<span>
						لغو دنبال کردن
					</span>
				</div>
			</div>
				
			</div>
				
				
		</div>
		</div>
			
		</div>
		
		<!-- all boards panel -->
		<div class="tabContent" ng-controller="allboardscontroller" ng-hide="tabsHide[1]">
		<div class="toolsbar">
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
		<div class="no-selected-msg" ng-show="allBoards.length == 0">
				هیچ بردی وجود ندارد.
			</div>
			
			<div class="accordion-item" ng-repeat="board in allBoards | filter: filterByCatObj | filter: {name:searchBoardName}">
				<div class="accordion-item-header" 
				ng-click="openOrCloseAccordion($index, board.isSubscribed)">
					<div class="accordion-item-info" id="acrdninfo{{$index}}" 
					ng-click="openOrCloseAccordion($index, !board.isSubscribed)">
						<div class="btn-icon">
							<i class="demo-icon  icon-down-open" id="acrdnicn{{$index}}"></i>
						</div>
						<span class="accordion-item-name">
							{{board.name}}
						</span>
					</div>
					<div id="flwbtn{{$index}}" class="follow-btn" 
					ng-click="subscribe($index, board.id)" ng-if="!board.isSubscribed">
						<div class="btn-icon">
							<i class="demo-icon  icon-plus"></i>
						</div>
						<span>
							دنبال کن
						</span>
					</div>
					
					<div id="flwbtnwt{{$index}}" class="follow-btn hidden"
					ng-if="!board.isSubscribed">
						<div class="loader mini-loader"></div>
						<span>
							صبر کنید
						</span>
					</div>
					
				</div>
				<div class="accordion-item-content" id="acrdn{{$index}}">
				<p>صاحب برد : {{board.publisher.displayName}}</p>
				<p>دسته بندی : {{board.category.name}}</p>
				<p>درباره این برد : </p>
				<P ng-bind-html="getMultiline(board.about)" ></P>
				</div>
			</div>
			
			<div class="more-post" ng-click="loadMoreBoard()">
					<div ng-show="!moreBoardsLoad">
						<span ng-show="!moreBoardLoadFail">بردهای بیشتر</span>
						<span ng-show="moreBoardLoadFail">تلاش دوباره</span>
					</div>
					<div ng-show="moreBoardsLoad">
						<div class="loader mini-loader"></div>
						<span>
							صبر کنید
						</span>
					</div>
				</div>
			
		</div>
		</div>
	</div>	

</div>
<div class="modal" id="imgModal">
<span class="modalClose" onclick="document.getElementById('imgModal').style.display='none';">&times;</span>
<img class="modal-content" id="modalImg">
</div>

<div class="userInfo-container" ng-show="userInfoPanelShow">
<div class="panel-title" >اطلاعات کاربری من</div>
<fieldset id="userInfoFieldset1" class="userInfo-fieldset">
	<legend ng-click="openOrCloseFieldset('1')">
		<div class="btn-icon">
			<i id="fieldset-icon1" class="demo-icon  icon-down-open" ></i>
		</div>
		<span>اطلاعات من</span>
	</legend>
	
	<label class="label">ایمیل : {{user.email}}</label>
	<label class="label">نام : {{user.displayName}}</label>
	<label class="label" ng-if="user.role == 'Publisher'">مقدار حافظه استفاده شده : {{(user.strogeUsage - (user.strogeUsage%1024)) / 1024}}KB از {{systemInfo.maxStroge / 1024}}KB</label>
</fieldset>

<fieldset id="userInfoFieldset2" class="userInfo-fieldset">
	<legend ng-click="openOrCloseFieldset('2')">
		<div class="btn-icon">
			<i id="fieldset-icon2" class="demo-icon  icon-down-open" ></i>
		</div>
		<span>تغییر نام</span>
	</legend>
	
		<label class="label">گذرواژه:</label>
		<input class="input" type="password" maxlength="50" placeholder="گذرواژه" id="passwordForNameUpdate">
		<label class="vlidation-message" id="PassFNUValidationMsg"></label>
		<label class="label">نام جدید:</label>
		<input class="input" type="text" ng-model="editDisplayName" placeholder="نام جدید">
		<label class="vlidation-message" id="newNameValidationMsg"></label>
		<div class="buttons-bar">
		<div class="form-btn" ng-click="updateDisplayName()">
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

<fieldset id="userInfoFieldset4" class="userInfo-fieldset">
	<legend ng-click="openOrCloseFieldset('4')">
		<div class="btn-icon">
			<i id="fieldset-icon4" class="demo-icon  icon-down-open" ></i>
		</div>
		<span>حذف حساب کاربری</span>
	</legend>
	<p>پس از حذف حساب کاربری همه اطلاعات مربوط به حساب کاربری شما حذف خواهد شد و به هیچ وجه قابل بازیابی نخواهد بود.</p>
	
	<label class="label">گذرواژه:</label>
		<input class="input" type="password" maxlength="50" placeholder="گذرواژه" id="passForDeleteAccount">
		<label class="vlidation-message" id="passForDeleteAccountValidationMsg"></label>
	
	<div class="buttons-bar">
		<div class="form-btn" ng-click="deleteAccount()">
			<span>
				حذف حساب
			</span>
		</div>
	</div>
</fieldset>


<fieldset id="userInfoFieldset5" class="userInfo-fieldset" ng-if="user.role == 'Subscriber'">
	<legend ng-click="openOrCloseFieldset('5')">
		<div class="btn-icon">
			<i id="fieldset-icon5" class="demo-icon  icon-down-open" ></i>
		</div>
		<span>ارتقاء کاربری به منتشر کننده</span>
	</legend>
	
	<span class="info-span">با ارتقاع حساب کاربری به منتشر کننده شما خواهید توانست برد اطلاع رسانی ایجاد کرده و روی آنها اطلاعیه منتشر کنید. ارتقاع حساب کاربری شما با تایید مدیر سامانه انجام خواهد شد.</span>
	<label class="label" ng-if="user.publishReqs.length > 0">درخواست ها : </label>
	<div class="publish-req" ng-repeat="request in user.publishReqs">
		<div class="publish-req-info">
			<span class="publish-req-info-cell">{{getDate(request.creationDate)}}</span>
			<span class="publish-req-info-cell">بررسی شده : 
				<span ng-if="request.checked">بله</span>
				<span ng-if="!request.checked">خیر</span>
			</span>
			
			<span class="publish-req-info-cell">تایید شده :
				<span ng-if="!request.checked">نامعلوم</span>
				<span ng-if="request.checked && request.agreement">بله</span>
				<span ng-if="request.checked && !request.agreement">خیر</span> 
			</span>	
			<div class="req-delete-btn" ng-click="deletePublishRequest(request.id, $index)">
				<i class="demo-icon  icon-trash"></i>
			</div>
		</div>
		
		<div class="publish-req-description" ng-bind-html="getMultiline(request.description)"></div>
	</div>
		<br/>
		<label class="label">توضیحات درخواست جدید</label>
		<textarea class="input normal-textarea" id="publishReqDescription" placeholder="توضیح درخواست" rows="5" cols="150" maxlength="1000"></textarea>
		<label class="vlidation-message" id="publishReqvalidationMsg"></label>
		<div class="buttons-bar">
		<div class="form-btn" ng-click="sendPublishRequest()">
			<span>
				ثبت درخواست
			</span>
		</div>
	</div>
</fieldset>


	<div class="buttons-bar">
		<div class="form-btn" ng-click="hideUserInfoPanel()">
			<span>
				بازگشت
			</span>
		</div>
	</div>
</div>

</body>
</html>