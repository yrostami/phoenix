var app = angular.module('phoenix', ['ngProgress']);
app.controller('adminMainController', ['$window', '$anchorScroll', '$interval', '$sce', '$rootScope', '$scope', 'adminService',
		'ngProgressFactory', adminMainController]);
function adminMainController($window, $anchorScroll, $interval, $sce, $rootScope, $scope, adminService, ngProgressFactory) {
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

//	$scope.showPageLoading = true;
//	$scope.showApp = false;
	$rootScope.progress = ngProgressFactory.createInstance();
	$rootScope.progress.setParent(document.getElementById('progressBar'));
	$rootScope.progress.setHeight('5px');
	$rootScope.progress.setColor('#ec3543');
	$rootScope.requests = new Array();
	$scope.requestsLoaded = false;
	$scope.tabsShow = [true, false, false];
	$scope.tabsElement = [document.getElementById('settings'),
	          			document.getElementById('users'),
	          			document.getElementById('requests')];
	          	$rootScope.classEditor.add($scope.tabsElement[0], 'selected');
	          	
	          	
	          	
	
	adminService.getSystemInfo().then(function(data) {
		$rootScope.systemInfo = data;
	});
	
	$scope.popupUserMenu = function(popupId) {
		var popup = document.getElementById(popupId);
		$rootScope.classEditor.toggle(popup, "popup-content-show")
	};
	
	$scope.openTab = function(index) {
		if (index == 2 && !$scope.requestsLoaded)
			$rootScope.loadRequests();
		else
			initTab(index);
	};
	
	var initTab = function(index) {
		$scope.tabsShow = [false, false, false];
		for (var i = $scope.tabsElement.length - 1; i >= 0; i--)
			$rootScope.classEditor.remove($scope.tabsElement[i], 'selected');
		$rootScope.classEditor.add($scope.tabsElement[index], 'selected');
		$scope.tabsShow[index] = true;
	};
	
	$rootScope.loadRequests = function()
	{
		$rootScope.progress.start();
		adminService.getRequests($rootScope.requests.length).then(
				function success(data)
				{
					$rootScope.requests = $rootScope.requests.concat(data);
					$scope.requestsLoaded = true;
					initTab(2);
					$rootScope.progress.complete();
				},
				function fail(msg)
				{
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("بارگذاری درخواست ها انجام نشد."+"\n"+msg,5);
				});
	}
	
	$scope.logout = function()
	{
		$rootScope.progress.start();
		adminService.logout().then(
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
	};
	
	$scope.openOrCloseFieldset = function(id)
	{
		$rootScope.classEditor.toggle(document.getElementById('userInfoFieldset'+id),'open-fieldset');
		$rootScope.classEditor.toggle(document.getElementById('fieldset-icon'+id),'icon-down-open');
		$rootScope.classEditor.toggle(document.getElementById('fieldset-icon'+id),'icon-up-open');
	}
	
	$rootScope.getMultiline = function(text) {
		return $sce.trustAsHtml(text.replace(/\n/g, " <br /> "));
	};
	
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
}