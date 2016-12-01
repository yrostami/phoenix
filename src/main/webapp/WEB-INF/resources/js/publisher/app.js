var app = angular.module('phoenix', ['ngProgress']);
app.controller('publisher', ['$window', '$anchorScroll', '$interval', '$sce', '$rootScope', '$scope', 'userService',
		'ngProgressFactory', publisher]);
function publisher($window, $anchorScroll, $interval, $sce, $rootScope, $scope, userService, ngProgressFactory) {
	$rootScope.classEditor = {
		add : function(element, newClass) {
			if (element.className.lastIndexOf(" ") !== (element.className.length - 1))
				element.className += " ";
			element.className += newClass;
		},
		remove : function(element, className) {
			if (element.className.indexOf(" ") !== 0)
				element.className = " " + element.className;
			if (element.className.lastIndexOf(" ") !== (element.className.length - 1))
				element.className += " ";
			element.className = element.className.replace(
					" " + className + " ", " ");
		},
		contain : function(element, className) {
			if (element.className.indexOf(" ") !== 0)
				element.className = " " + element.className;
			if (element.className.lastIndexOf(" ") !== (element.className.length - 1))
				element.className += " ";
			return !(element.className.indexOf(" " + className + " ") == -1)
		},
		toggle : function(element, className) {
			if (this.contain(element, className))
				this.remove(element, className);
			else
				this.add(element, className);
		}
	};

	$scope.showPageLoading = true;
	$scope.showApp = false;
	$scope.mainContentShow = true;
	$scope.userInfoPanelShow = false;
	$scope.abtouched = false;
	$rootScope.progress = ngProgressFactory.createInstance();
	$rootScope.progress.setParent(document.getElementById('pageLoadingBar'));
	$rootScope.progress.setHeight('5px');
	$rootScope.progress.setColor('#ec3543');
	$rootScope.progress.start();
	$rootScope.haveNotification = false;
	$rootScope.notificationCount = 0;
	$rootScope.lastPostDate = -1;
	$rootScope.user = {};
	$rootScope.posts = new Array();
	$rootScope.targetPosts = new Array();
	$rootScope.allBoards = new Array();
	$rootScope.allCategories = new Array();
	$scope.editDisplayName = "";
	$scope.tabsHide = [false, true, true];
	$scope.tabsElement = [document.getElementById('news'),
			document.getElementById('myBoards'),
			document.getElementById('allBoards')];
	$rootScope.classEditor.add($scope.tabsElement[0], 'selected');
	
	var nlc = document.getElementById('notificationLogoContainer');
	nlc.style.left = (window.innerWidth - nlc.offsetWidth) / 2 + 'px';

	userService.getUser().then(
			function success(user) {
				$rootScope.user = user;
				if(user.subscribedBoards.length == 0){
					$rootScope.progress.complete();
					$rootScope.progress.setHeight('3px');
					$rootScope.progress.setParent(document.getElementById('progressBar'));
					$scope.showPageLoading = false;
					$scope.showApp = true;
					return;
				}
				userService.getPosts().then(
						function success(data)
						{
							if(data.length == 0)
								$rootScope.showGlobalMsg("هیچ اطلاعیه ای وجود ندارد." + msg, 4);
							$rootScope.posts = data;
							$rootScope.lastPostDate = getLastPostDate(data);
							$rootScope.targetPosts = $rootScope.posts;
							$rootScope.progress.complete();
							$rootScope.progress.setHeight('5px');
							$rootScope.progress.setParent(document.getElementById('progressBar'));
							$scope.showPageLoading = false;
							$scope.showApp = true;
						},
						function fail(msg)
						{
							$rootScope.progress.complete();
							$rootScope.progress.setHeight('5px');
							$rootScope.progress.setParent(document.getElementById('progressBar'));
							$scope.showPageLoading = false;
							$scope.showApp = true;
							$rootScope.showGlobalMsg("بار گذاری اطلاهیه ها انجام نشد." + msg, 4);
						});
			},
			function fail(msg) {
				$rootScope.showGlobalMsg("بار گذاری اظلاعات کاربری انجام نشد."+ "\n" + msg, 5);
			});
	userService.getSystemInfo().then(function(data) {
		$rootScope.systemInfo = data;
	});
	
	$scope.popupUserMenu = function(popupId) {
		var popup = document.getElementById(popupId);
		$rootScope.classEditor.toggle(popup, "popup-content-show")
	};

	$rootScope.initAllBoards = function() {
		$rootScope.progress.start()
		userService.getAllCategories().then(
				function(data) {
					$rootScope.allCategories = data;
					userService.getAllBoards().then(
							function(data) {
								$rootScope.allBoards = data;
								checkAllBoardsSubscribing();
								$scope.abtouched = true;
								initTab(2);
								$rootScope.progress.complete();
							},
							function fail(msg) {
								$rootScope.showGlobalMsg(
										"بار گذاری اظلاعات دسته بندی بردها انجام نشد."
												+ "\n" + msg, 5);
								$rootScope.progress.complete();
							});
				},
				function fail(msg) {
					$rootScope.showGlobalMsg(
							"بار گذاری اظلاعات بردها انجام نشد." + "\n" + msg, 5);
					$rootScope.progress.complete();
				});
	};
	
	function checkAllBoardsSubscribing()
	{
		for(var i=$rootScope.allBoards.length - 1; i>=0; i--)
		{
			$rootScope.allBoards[i].isSubscribed = false;
			for(var j=$rootScope.user.subscribedBoards.length -1; j>=0; j--)
			{
				if($rootScope.allBoards[i].id == $rootScope.user.subscribedBoards[j].id)
				{
					$rootScope.allBoards[i].isSubscribed = true;
					break;
				}
			}
		}
	}

	var initTab = function(index) {
		$scope.tabsHide = [true, true, true];
		for (var i = $scope.tabsElement.length - 1; i >= 0; i--)
			$rootScope.classEditor.remove($scope.tabsElement[i], 'selected');
		$rootScope.classEditor.add($scope.tabsElement[index], 'selected');
		$scope.tabsHide[index] = false;
	};

	$scope.openTab = function(index) {
		if (index == 2 && !$scope.abtouched)
			$rootScope.initAllBoards();
		else
			initTab(index);
	};
	
	$scope.showUserInfoPanel = function()
	{
		$scope.mainContentShow = false;
		$scope.userInfoPanelShow = true;
	};

	$scope.hideUserInfoPanel = function()
	{
		$scope.mainContentShow = true;
		$scope.userInfoPanelShow = false;
	};
	
	$scope.openOrCloseFieldset = function(id)
	{
		$rootScope.classEditor.toggle(document.getElementById('userInfoFieldset'+id),'open-fieldset');
		$rootScope.classEditor.toggle(document.getElementById('fieldset-icon'+id),'icon-down-open');
		$rootScope.classEditor.toggle(document.getElementById('fieldset-icon'+id),'icon-up-open');
	}
	
	$rootScope.showGlobalMsg = function(msg, seconds) {
		var x = document.getElementById("globalMsg");
		x.innerText = msg;
		$rootScope.globalMsg = msg;
		x.className = "show";
		var right = ((window.innerWidth - x.offsetWidth) - 20) / 2;
		x.style.right = right + "px";
		setTimeout(function() {
			x.className = x.className.replace("show", "");
		}, seconds * 1000);
	};
	
	$rootScope.getMultiline = function(text) {
		return $sce.trustAsHtml(text.replace(/\n/g, " <br /> "));
	};
	
	$scope.updateDisplayName = function()
	{
		var flag = true;
		
		var passValidationMsg = document.getElementById('PassFNUValidationMsg');
		var newNameValidationMsg = document.getElementById('newNameValidationMsg');
		
		passValidationMsg.innerText = "";
		newNameValidationMsg.innerText = "";
		
		var pass = document.getElementById('passwordForNameUpdate').value;
		if(pass.length < 8)
		{
			passValidationMsg.innerText = "گذرواژه باید شامل حداقل 8 حرف باشد.";
			flag = false;
		}
		if(!isValidDisplayName($scope.editDisplayName))
		{
			newNameValidationMsg.innerText = "نام نباید خالی باشد.";
			flag = false;
		}
		if(flag)
		{
			var updateData = 
			{
				password: pass,
				displayName: $scope.editDisplayName
			};
			
			$rootScope.progress.start();
			userService.updateDisplayName(updateData).then(
				function success(data)
				{
					$rootScope.user.displayName = data.displayName;
					$rootScope.progress.complete();
				},
				function fail(msg)
				{
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("انجام نشد." + "\n" + msg, 4);
				});
		}
	};
	
	$scope.logout = function()
	{
		$rootScope.progress.start();
		userService.logout().then(
				function success()
				{
					window.location.href = 'http://localhost:8080/phoenix/';
					$rootScope.progress.complete();
				},
				function()
				{
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("انجام نشد." + "\n" + msg, 4);
				});
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
	
	$scope.updatePassword = function()
	{
		var flag = true;
		
		var currentPass = document.getElementById('passwordForUpdate').value;
		var newPass = document.getElementById('newPassword').value;
		
		var passValidationMsg = document.getElementById('passForUpdateValidationMsg');
		var newPassValidationMsg = document.getElementById('newPassValidationMsg');
		
		passValidationMsg.innerText = "";
		newPassValidationMsg.innerText = "";
		
		if(currentPass.length < 8)
		{
			passValidationMsg.innerText = "گذرواژه باید شامل حداقل 8 حرف باشد.";
			flag = false;
		}
		
		if(newPass.length < 8)
		{
			newPassValidationMsg.innerText = "گذرواژه باید شامل حداقل 8 حرف باشد.";
			flag = false;
		}
		if(flag)
		{
			var updateData=
			{
				currentPassword: currentPass,
				newPassword: newPass
			};
			$rootScope.progress.start();
			userService.updatePassword(updateData).then(
					function success(data)
					{
						$rootScope.progress.complete();
						$rootScope.showGlobalMsg("انجام شد.", 4);
					},
					function fail(msg)
					{
						$rootScope.progress.complete();
						$rootScope.showGlobalMsg("انجام نشد." + "\n" + msg, 4);
					});
		}
	}
	
	$scope.deleteAccount  = function()
	{
		var passForDelete = document.getElementById('passForDeleteAccount').value;
		if(passForDelete.length > 7 && passForDelete.length <= 50)
		{
			$rootScope.progress.start();
			userService.deleteAccount(passForDelete).then(
					function success()
					{
						$rootScope.progress.complete();
						$rootScope.showGlobalMsg("انجام شد.", 4);
						window.location.href = 'http://localhost:8080/phoenix/';
					},
					function fail(msg)
					{
						$rootScope.progress.complete();
						$rootScope.showGlobalMsg("انجام نشد." + "\n" + msg, 4);
					});
		}else{
			document.getElementById('passForDeleteAccountValidationMsg')
			.innerText = "گذرواژه باید شامل حداقل 8 حرف باشد.";
		}
	}
	
	$scope.getNewPosts = function()
	{
		if($rootScope.lastPostDate == -1)
		{
			userService.getPosts().then(
					function success(data)
					{
						if(data.length == 0)
							$rootScope.showGlobalMsg("هیچ اطلاعیه ای وجود ندارد." + msg, 4);
						$rootScope.posts = data;
						$rootScope.lastPostDate = getLastPostDate(data);
						$rootScope.targetPosts = $rootScope.posts;
						$rootScope.notificationCount = data.length;
					},
					function fail(msg)
					{
						
					});
		}
		else{
			userService.getPostsAfter($rootScope.lastPostDate).then(
					function success(data)
					{
						if(data.length > 0){
							$rootScope.posts = $rootScope.posts.concat(data);
							$rootScope.lastPostDate = getLastPostDate(data);
							$rootScope.haveNotification = true;
							$rootScope.notificationCount = data.length;
						}
					},
					function fail()
					{
						
					});
		}
	}
	
	$scope.showNewPosts = function()
	{
		$rootScope.haveNotification = false;
		$rootScope.targetPosts = $rootScope.posts;
		$rootScope.showPosts(-1);
		$scope.openTab(0);
		$scope.hideUserInfoPanel();
		$window.scrollTo(60, 0);
	}
	
	$interval($scope.getNewPosts, 60 * 1000);
	
	function getLastPostDate(postList)
	{
		var date = 0;
		for(var i = postList.length - 1 ; i >= 0 ; i--)
		{
			if(postList[i].creationDate > date)
				date = postList[i].creationDate;
		}
		return date;
	}
	
	$rootScope.getDate = function(miliseconds)
	{
		week= new Array("يكشنبه","دوشنبه","سه شنبه","چهارشنبه","پنج شنبه","جمعه","شنبه");
		months = new Array("فروردين","ارديبهشت","خرداد","تير","مرداد","شهريور","مهر","آبان","آذر","دي","بهمن","اسفند");
		a = new Date(miliseconds);
		d= a.getDay();
		day= a.getDate();
		month = a.getMonth()+1;
		year= a.getYear();
		year = (year == 0)?2016:year;
		(year<1000)? (year += 1900):true;
		year -= ( (month < 3) || ((month == 3) && (day < 21)) )? 622:621;
		switch (month) {
		case 1: (day<21)? (month=10, day+=10):(month=11, day-=20); break;
		case 2: (day<20)? (month=11, day+=11):(month=12, day-=19); break;
		case 3: (day<21)? (month=12, day+=9):(month=1, day-=20); break;
		case 4: (day<21)? (month=1, day+=11):(month=2, day-=20); break;
		case 5:
		case 6: (day<22)? (month-=3, day+=10):(month-=2, day-=21); break;
		case 7:
		case 8:
		case 9: (day<23)? (month-=3, day+=9):(month-=2, day-=22); break;
		case 10:(day<23)? (month=7, day+=8):(month=8, day-=22); break;
		case 11:
		case 12:(day<22)? (month-=3, day+=9):(month-=2, day-=21); break;
		default: break;
		}
		
		var min = (a.getMinutes()>9)? a.getMinutes():'0'+ a.getMinutes();
		var hour = (a.getHours()>9)? a.getHours() : '0'+ a.getHours(); 
		return week[d]+' '+day+' '+months[month-1]+' '+ year+' , '+ hour + ':' + min;
	};

};

app.directive('fileUpload', function() {
	return {
		scope : true,
		link : function(scope, el, attrs) {
			el.bind('change', function(event) {
				var files = event.target.files;
				try {
					scope.$emit("fileSelected", {
						file : files[0]
					});
				} catch (e) {
					scope.$emit("fileSelectedError", e.message);
				}
			});
		}
	};
});

app.filter('unique', function() {
	   return function(collection, keyname) {
	      var output = [], 
	          keys = [];
	      angular.forEach(collection, function(item) {
	          var key = item[keyname];
	          var keyIndex;
	          for( keyIndex = keys.length - 1; keyIndex>=0; keyIndex--)
	        	  if(keys[keyIndex] == key)
	        		  break;
	          
	          if(keys.indexOf(key) === -1) {
	              keys.push(key); 
	              output.push(item);
	          }
	      });
	      return output;
	   };
	});
