<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>سامانه بردهای اطلاع رسانی ققنوس</title>

<link href="<spring:url value="/resources/css/phoenix-icons.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>
<%-- <link href="<spring:url value="/resources/css/iconAnimation.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/> --%>
<link href="<spring:url value="/resources/css/phoenix.css" htmlEscape="true"/>" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="<spring:url value="/resources/js/angular.min.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/ngprogress.min.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/publisher/app.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/publisher/userService.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/allboardscontroller.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/myboardscontroller.js" htmlEscape="true"/>"></script>
<script type="text/javascript" src="<spring:url value="/resources/js/controllers/postscontroller.js" htmlEscape="true"/>"></script>

</head>
<body ng-app="phoenix" ng-controller="publisher" ng-cloak>

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

	<div id="content" ng-show="mainContentShow">
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
			
			<div class="list-item" ng-show="user.subscribedBoards.length !== 0" ng-click="showPosts(-1, -1)">
				<span id="board-1" class="item-name wide-item selected">
					همه اطلاعیه های جدید
				</span>
			</div>
			
			<div class="list-item" ng-repeat="board in user.subscribedBoards">
				<div class="item-modal-btn" ng-click="showSubscribedBoardInfo($index, board.id)">
					<i class="demo-icon icon-ellipsis-vert"></i>
				</div>
				<span id="board{{$index}}" class="item-name" ng-click="showPosts($index, board.id)">
					{{board.name}}
				</span>
			</div>
			</div>
			
			<div class="list-item-content">
			
			<div class="no-selected-msg" ng-show="user.subscribedBoards.length == 0">
				شما هیچ بردی را دنبال نمی کنید.<br />
				برای دنبال کردن بردها به قسمت "همه ی بردها" بروید. 
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
				
				<div class="more-post" ng-click="loadMorePosts()" ng-show="user.subscribedBoards.length !== 0">
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
		
		<!-- my boards panel -->
		<div class="tabContent" ng-hide="tabsHide[1]" ng-controller="myboardscontroller">
		<div ng-show="myBoardsMainPanelShow">
		<div class="toolsbar">
			<div class="button" ng-click="showNewBoardPanel()">
			<div class="btn-icon">
			<i class="demo-icon  icon-plus"></i>
			</div>
			<span>
			برد اطلاع رسانی جدید 
			</span>
			</div>
			
			<div class="button" ng-click="showNewPostPanel()" ng-show="boardContentShow">
					<div class="btn-icon">
						<i class="demo-icon  icon-pin"></i>
					</div>
					<span>
						نصب اطلاعیه جدید روی برد
					</span>
				</div>
			
		</div>
			<div class="list">
			
			<div class="list-item" ng-show="user.myBoards.length == 0">
				<span class="item-name wide-item no-item">
					هیچ بردی وجود ندارد.
				</span>
			</div>
			
			<div class="list-item" ng-repeat="board in user.myBoards">
				<div class="item-modal-btn" ng-click="showMyBoardInfo($index)">
					<i class="demo-icon icon-ellipsis-vert"></i>
				</div>
				<span id="myBoard{{$index}}" class="item-name" ng-click="selectBoard($index)">
					{{board.name}}
				</span>
			</div>	
			</div>
			
			<div class="list-item-content">
			
			<div class="no-selected-msg" ng-show="user.myBoards.length == 0">
				شما هیچ بردی ندارید.<br />
				برای ایجاد برد جدید روی گزینه "برد اطلاع رسانی جدید" کلیک کنید. 
			</div>
			
			<div class="no-selected-msg" ng-show="user.myBoards.length !== 0 && boardIndex == -1">
				برای دیدن اطلاعیه ها روی برد مورد نظر کلیک کنید.
			</div>

			<div class="board-content" ng-show="boardContentShow">
				<div class="post-container" ng-repeat="post in selectedBoard.posts | orderBy : '-creationDate'">
				<div class="post-title"><span>{{post.title}}</span>
				<div class="delete-btn" ng-click="deletePostDialogShow(post.id)">
					<div class="btn-icon">
						<i class="demo-icon  icon-trash"></i>
					</div>
				</div></div>
				<div class="image-div" id="myPostImageDiv{{$index}}"></div>
				<div id="postContent{{$index}}" class="post-content">
					<p class="post-text" ng-bind-html="getMultiline(post.content)"></p>
					<div class="post-footer">
						<span id="myPostImageTitle{{$index}}" ng-if="post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') !== -1">
						این اطلاعیه دارای عکس می باشد:</span> 
						<span ng-if="post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') == -1">
						این اطلاعیه دارای فایل می باشد:</span>
						<div class="download-file">
							<span ng-if=" post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') !== -1" 
								id="myPostImageViewTitle{{$index}}" ng-click="getImage($index, post.fileInfo.filePath)">نمایش داده شود</span>
								
    						<a ng-if="post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') == -1"
    						 target="_blank" ng-href="/phoenix/subscriber/getfile{{post.fileInfo.filePath}}">دانلود شود</a>
						</div>
						<div id="myPostImageLoader{{$index}}" class="loader micro-loader post-image-loader" 
								ng-if=" post.fileInfo !== null && post.fileInfo.fileType.indexOf('image') !== -1"></div>
						<span class="post-date-container">{{getDate(post.creationDate)}}</span>
					</div>
				</div>
				</div>
				
				<div class="more-post" ng-click="loadMorePost()">
					<div ng-show="!morePostLoad">
						<span ng-show="!$scope.selectedBoard.loadFail">دیدن اطلاعیه های بیشتر</span>
						<span ng-show="$scope.selectedBoard.loadFail">تلاش دوباره</span>
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
		
		<div class="form-container" ng-show="newBoardPanelShow">
			<div class="panel-title" ng-hide="boardEditMod">ایجاد برد اطلاع رسانی جدید</div>
			<div class="panel-title" ng-show="boardEditMod">ویرایش اطلاعات برد اطلاع رسانی</div>
			<div class="form-content">
			<label class="label">&rsaquo; نام برد اطلاع رسانی:</label>
			<input ng-model="newBoard.name" ng-change="editBoardChangeCheck(1)" class="input" type="text" placeholder="نام را اینجا وارد کنید">
			<label class="vlidation-message" ng-hide="validationMessageHide[0]">&rsaquo; نام برد اطلاع رسانی باید شامل حداقل پنج و حداکثر صد حرف باشد.</label>
			<label class="label">&rsaquo; دسته بندی برد اطلاع رسانی:</label>
			<select ng-options="category as category.name for category in allCategories track by category.id" 
			ng-model="newBoard.category" ng-change="editBoardChangeCheck(2)" class="input"></select>
			<label class="vlidation-message" ng-hide="validationMessageHide[1]">&rsaquo; باید یک دسته بندی معتبر انتخاب کنید.</label>
			<label class="label">&rsaquo; درباره برد اطلاع رسانی:</label>
			<textarea ng-model="newBoard.about" ng-change="editBoardChangeCheck(3)" class="input texterea" rows="5" cols="40" maxlength="1000" placeholder="درباره این برد اطلاع رسانی بنویسید ..." ></textarea>
			<label class="vlidation-message" ng-hide="validationMessageHide[2]">&rsaquo; درباره برد نباید خالی باشد.</label>
			
			<label class="label" ng-if="boardEditMod">
			<span>&rsaquo; تعداد دنبال کننده ها: </span>
			<span ng-if="editBoard.subscriberCount !== undefined">{{editBoard.subscriberCount}}</span>
			<div class="loader micro-loader" ng-if="editBoard.subscriberCount == undefined"></div>
			</label>
			
			<label class="label" ng-if="boardEditMod">
			<span>&rsaquo; تعداد پست ها: </span>
			<span ng-if="editBoard.postCount !== undefined">{{editBoard.postCount}}</span>
			<div class="loader micro-loader" ng-if="editBoard.postCount == undefined"></div>
			</label>
			</div>
		
			<div class="buttons-bar" ng-show="boardEditMod">
				<div class="form-btn" ng-click="hideNewBoardPanel()">
					<span>
						بازگشت
					</span>
				</div>
				<div ng-click="boardValidationAndUpdate()" 
				ng-class="{'disable-btn': !editBoardChange , 'form-btn': editBoardChange}">
					<span>
						اعمال تغییرات
					</span>
				</div>
				<div class="form-btn" ng-click="deleteBoardDialogShow()">
					<span>
						حذف برد
					</span>
				</div>
			</div>
			
			<div class="buttons-bar" ng-hide="boardEditMod">
				<div class="form-btn" ng-click="hideNewBoardPanel()">
					<span>
						منصرف شدم
					</span>
				</div>
				<div class="form-btn" ng-click="newBoardValidationAndSend()">
					<span>
						ایجاد کن
					</span>
				</div>
			</div>
			
		</div>
		
		<div class="form-container" ng-show="newPostPanelShow">
		<div class="panel-title">نصب اطلاعیه جدید</div>
			<div class="form-content">
				<label class="label">&rsaquo; عنوان اطلاعیه:</label>
				<input ng-model="newPost.title" class="input" type="text" placeholder="عنوان اطلاعیه را اینجا وارد کنید">
				<label class="vlidation-message" ng-hide="validationMessageHide[0]">&rsaquo; عنوان اطلاعیه نباید خالی باشد و می تواند شامل حداکثر 150 حرف باشد.</label>
				<label class="label">&rsaquo; متن اطلاعیه:</label>
				<textarea ng-model="newPost.content" class="input texterea" rows="5" cols="40" maxlength="1500" placeholder="متن اطلاعیه را اینجا بنویسید ..." ></textarea>
				<label class="vlidation-message" ng-hide="validationMessageHide[1]">&rsaquo; متن اطلاعیه نباید خالی باشد و می تواند شامل حداکثر 1500 حرف باشد.</label>
 				<div>
 					<div ng-show="imageCheckShow" class="checkbox"><input type="checkbox" id="imageCheck" ng-click="checked('imageCheck')">پیوست عکس</div>
 					<div ng-show="fileCheckShow" class="checkbox"><input type="checkbox" id="fileCheck" ng-click="checked('fileCheck')">پیوست فایل</div>
				</div>
				<div ng-show="fileUploadShow" class="file-upload-container">
					<div class="form-btn file-upload"> 
    					<span>{{uploadInputTitle}}</span>
    					<input id="uploadFile" type="file" class="upload" file-upload>
					</div>
					<label>{{fileName}}</label>
					<span ng-show="uploadStrogeMsgShow()" class="stroge-msg">{{strogeSize}}</span>
				</div>
			</div>
			<div class="buttons-bar">
				<div class="form-btn" ng-click="hideNewPostPanel()">
					<span>
						منصرف شدم
					</span>
				</div>
				<div class="form-btn" ng-click="newPostValidationAndSend()">
					<span>
						نصب کن
					</span>
				</div>
			</div>
		</div>
		
		
		
		<div class="modal" id="dialogModal">
		<div class="modal-content dialog"><span>آیا برای حذف مطمئن هستید؟</span>
		<div class="buttons-bar">
				<div class="form-btn" onclick="document.getElementById('dialogModal').style.display='none';">
					<span>
						خیر
					</span>
				</div>
				<div class="form-btn" ng-click="deletePost()" ng-hide="boardEditMod">
					<span>
						بله
					</span>
				</div>
				<div class="form-btn" ng-click="deleteBoard()" ng-show="boardEditMod">
					<span>
						بله
					</span>
				</div>
			</div>
		</div>
		</div>
		
		</div>

		<!-- all boards panel -->
		<div class="tabContent" ng-controller="allboardscontroller" ng-hide="tabsHide[2]">
		<div class="toolsbar">
			<div class="button" ng-click="reload()">
			<div class="btn-icon">
				<i class="demo-icon  icon-arrows-cw"></i>
			</div>
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
				<P ng-bind-html="getMultiline(board.about);" ></P>
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

<!--
<fieldset id="userInfoFieldset4" class="userInfo-fieldset">
	<legend ng-click="openOrCloseFieldset('4')">
		<div class="btn-icon">
			<i id="fieldset-icon4" class="demo-icon  icon-down-open" ></i>
		</div>
		<span>نشست های فعال</span>
	</legend>
	
</fieldset>
-->

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