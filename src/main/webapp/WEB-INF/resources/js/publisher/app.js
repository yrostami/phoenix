var app = angular.module('phoenix', ['ngProgress']);
app.controller('publisher', ['$sce','$rootScope', '$scope','userService', 'ngProgressFactory' ,
                             function($sce, $rootScope, $scope, userService,ngProgressFactory)
{	
    $rootScope.classEditor = {
		add : function(element, newClass){
		    if(element.className.lastIndexOf(" ") !== (element.className.length - 1))
			element.className += " ";
		    element.className += newClass;
		} ,
		remove : function(element, className){
		    if(element.className.indexOf(" ") !== 0)
		    	element.className = " " + element.className;
		    if(element.className.lastIndexOf(" ") !== (element.className.length - 1))
			element.className += " ";
		    element.className = element.className.replace(" " + className + " "," ");
		},
		contain : function(element, className){
		    if(element.className.indexOf(" ") !== 0)
		    	element.className = " " + element.className;
		    if(element.className.lastIndexOf(" ") !== (element.className.length - 1))
			element.className += " ";
		    return !(element.className.indexOf(" " + className + " ") == -1)
		},
		toggle : function(element, className){
		    if(this.contain(element, className))
			this.remove(element, className);
		    else 
			this.add(element, className);
		} 
	};
    
	$scope.showPageLoading = true;
	$scope.showApp = false;
	$scope.abtouched = false;
	$rootScope.progress = ngProgressFactory.createInstance();
	$rootScope.progress.setParent(document.getElementById('pageLoadingBar'));
	$rootScope.progress.setHeight('6px');
	$rootScope.progress.setColor('#ec3543');
	$rootScope.progress.start();
	$rootScope.notification = {message : "هیچ اعلانی وجود ندارد.", count : 0};
	$rootScope.globalMsg = "";
	$rootScope.user = {};
	$rootScope.allBoards = new Array();
	$rootScope.allCategories = new Array();
	$scope.tabsHide = [false, true, true];
	$scope.tabsElement = [document.getElementById('news'),
	                   document.getElementById('myBoards'),
	                   document.getElementById('allBoards')];
	$rootScope.classEditor.add($scope.tabsElement[0],'selected');
	
	userService.getUser().then(
		function success(user)
		{
		    $rootScope.user = user;
		    userService.getSystemInfo().then(function(data){$rootScope.systemInfo=data;});
		    $rootScope.progress.complete();
		    $scope.showPageLoading = false;
		    $scope.showApp = true;
		    $rootScope.progress.setParent(document.getElementById('progressBar'));
		},
		function fail(msg)
		{
		    $rootScope.showGlobalMsg("بار گذاری اظلاعات کاربری انجام نشد." + "\n" + msg, 5);
		}
	);

	$scope.popupUserMenu = function(popupId){
		var popup = document.getElementById(popupId);
		$rootScope.classEditor.toggle(popup,"popup-content-show")
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
	};
	
	$rootScope.initAllBoards = function(){
		$rootScope.progress.start()
		userService.getAllCategories().then(function(data){
			$rootScope.allCategories = data;
			userService.getAllBoards().then(function(data){
				$rootScope.allBoards = filterAllBoards(data,$rootScope.user.subscribedBoards);
				$scope.abtouched = true;
				initTab(2);
				$rootScope.progress.complete();
			},
			function fail(msg)
			{
			    $rootScope.showGlobalMsg("بار گذاری اظلاعات دسته بندی بردها انجام نشد." + "\n" + msg, 5);
			    $rootScope.progress.complete();
			}
			);
		},
		function fail(msg)
		{
		    $rootScope.showGlobalMsg("بار گذاری اظلاعات بردها انجام نشد." + "\n" + msg, 5);
		    $rootScope.progress.complete();
		}
		);
	};
	
	var initTab = function(index){
		$scope.tabsHide = [true, true, true];
		for (var i = $scope.tabsElement.length - 1; i >= 0; i--)
			$rootScope.classEditor.remove($scope.tabsElement[i], 'selected');
		$rootScope.classEditor.add($scope.tabsElement[index], 'selected');
		$scope.tabsHide[index] = false;
	};
	
	$scope.openTab = function(index){
		if(index == 2 && !$scope.abtouched)
			$rootScope.initAllBoards();		
		else
			initTab(index);
	};
	
	$rootScope.showGlobalMsg = function(msg, seconds)
	{
	    var x = document.getElementById("globalMsg");
	    $rootScope.globalMsg = msg;
	    x.className = "show";
	    var right = ((window.innerWidth - x.offsetWidth) - 20) / 2;
	    x.style.right =  right + "px";
	    setTimeout(function(){x.className = x.className.replace("show", "");}, seconds * 1000);
	};
	
	$rootScope.getMultiline = function(text)
	{
	    return $sce.trustAsHtml(text.replace(/\n/g , " <br /> "));
	};
	
}]);

app.directive('fileUpload', function () {
    return {
        scope: true,
        link: function (scope, el, attrs) {
            el.bind('change', function (event) {
                var files = event.target.files;
                try{
                scope.$emit("fileSelected", { file: files[0] });
                }catch(e){
                    scope.$emit("fileSelectedError", e.message);
                }
            });
        }
    };
});