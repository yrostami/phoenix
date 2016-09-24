app.controller('allboardscontroller', ['$rootScope', '$scope','userService',
                                       function($rootScope, $scope, userService)
{
	$scope.filterByCatObj={};
	$scope.listIndex = -1;
	
	$scope.openOrCloseAccordion = function(index){
		document.getElementById('acrdn'+index).classList.toggle('accordion-item-show');
		document.getElementById('acrdninfo'+index).classList.toggle('accorrdion-open');
		var acrdnicn = document.getElementById('acrdnicn'+index);
		acrdnicn.classList.toggle("icon-down-open");
		acrdnicn.classList.toggle("icon-up-open");
	};
	
	$scope.subscribe = function(index){
		document.getElementById('flwbtn'+index).classList.toggle('hidden');
		document.getElementById('flwbtnwt'+index).classList.toggle('hidden');
		var subscribedInfo = {
				subscriberId : $rootScope.user.id ,
				boardId : $rootScope.allBoards[index].id
		};
		userService.subscribe(subscribedInfo).then(
				function success(data){
					$rootScope.user.subscribedBoards.push($rootScope.allBoards[index]);
					$rootScope.allBoards.splice(index, 1);
				},
				function fail(message){
					$rootScope.globalMessage = message;
					document.getElementById('flwbtn'+index).classList.toggle('hidden');
					document.getElementById('flwbtnwt'+index).classList.toggle('hidden');
				}
		);
	};
	
$scope.filterByCat = function(index){
		
		$scope.searchBoardName = "";
		if(index == -1)
			$scope.filterByCatObj = {};
		else
			$scope.filterByCatObj = {category: {id: $scope.allCategories[index].id}};
		document.getElementById('catItem' + $scope.listIndex).classList.remove('selected');
		document.getElementById('catItem'+index).classList.add('selected');
		$scope.listIndex = index;
	};

	$scope.reload = function(){
		$rootScope.initAllBoards();
		$scope.filterByCat(-1);
	};
}])