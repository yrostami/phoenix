var app = angular.module('phoenix', ['ngProgress']);
app.controller('publisher', ['$rootScope', '$scope','userService', 'ngProgressFactory' ,
                             function($rootScope, $scope, userService,ngProgressFactory)
{	
	$scope.showPageLoading = true;
	$scope.showApp = false;
	$scope.abtouched = false;
	$rootScope.progress = ngProgressFactory.createInstance();
	$rootScope.progress.setParent(document.getElementById('pageLoadingBar'));
	$rootScope.progress.setHeight('6px');
	$rootScope.progress.setColor('#ec3543');
	$rootScope.progress.start();
	$rootScope.notification = {message : "هیچ اعلانی وجود ندارد.", count : 0};
	$rootScope.globalMessage = "";
	$rootScope.user = {};
	$rootScope.allBoards = new Array();
	$rootScope.allCategories = new Array();
	$scope.tabsHide = [false, true, true];
	$scope.tabsElement = [document.getElementById('news'),
	                   document.getElementById('myBoards'),
	                   document.getElementById('allBoards')];
	$scope.tabsElement[0].classList.add('selected');
	
	userService.getUser().then(function(user)
			{
		$rootScope.user = user;
		$rootScope.progress.complete();
		$scope.showPageLoading = false;
		$scope.showApp = true;
		$rootScope.progress.setParent(document.getElementById('progressBar'));
		});

	$scope.popupUserMenu = function(popupId){
		document.getElementById(popupId).classList.toggle("popup-content-show");
	};
	
	function filterAllBoards(list,list2){
		var newList = new Array();
		for (var i = list.length - 1; i >= 0; i--){
			var flag = true;
			for (var j = $rootScope.user.subscribedBoards.length - 1; j >= 0; j--){
				if(list2[j].id == list[i].id)
					flag = false;}
			if(flag)
				newList.push(list[i]);}
		return newList;
	}
	
	$rootScope.initAllBoards = function(){
		$rootScope.progress.start()
		userService.getAllCategories().then(function(data){
			$rootScope.allCategories = data;
			userService.getAllBoards().then(function(data){
				$rootScope.allBoards = filterAllBoards(data,$rootScope.user.subscribedBoards);
				$scope.abtouched = true;
				initTab(2);
				$rootScope.progress.complete();
			});
		});
	};
	
	var initTab = function(index){
		$scope.tabsHide = [true, true, true];
		for (var i = $scope.tabsElement.length - 1; i >= 0; i--)
			$scope.tabsElement[i].classList.remove('selected');
		$scope.tabsElement[index].classList.add('selected');
		$scope.tabsHide[index] = false;
	};
	
	$scope.openTab = function(index){
		
		if(index == 2 && !$scope.abtouched)
			$rootScope.initAllBoards();		
		else
			initTab(index);
	};
	
	var showGlobalMessage = function(){
		alert($scope.globalMessage);
	};
	
}]);

app.directive('fileUpload', function () {
    return {
        scope: true,
        link: function (scope, el, attrs) {
            el.bind('change', function (event) {
                var files = event.target.files;

                for (var i = 0;i<files.length;i++) {
                    scope.$emit("fileSelected", { file: files[i] });
                }                                       
            });
        }
    };
});