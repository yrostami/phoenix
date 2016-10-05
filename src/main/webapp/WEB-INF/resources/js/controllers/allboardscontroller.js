app.controller('allboardscontroller', ['$rootScope', '$scope','userService',
                                       function($rootScope, $scope, userService)
{
	$scope.filterByCatObj={};
	$scope.listIndex = -1;
	
	$scope.openOrCloseAccordion = function(index){
	    $rootScope.classEditor.toggle(document.getElementById('acrdn'+index), 'accordion-item-show');
	    $rootScope.classEditor.toggle(document.getElementById('acrdninfo'+index), 'accorrdion-open');
		var acrdnicn = document.getElementById('acrdnicn'+index);
		$rootScope.classEditor.toggle(acrdnicn,"icon-down-open");
		$rootScope.classEditor.toggle(acrdnicn,"icon-up-open")
	};
	
	$scope.subscribe = function(index){
	    $rootScope.classEditor.toggle(document.getElementById('flwbtn'+index), 'hidden');
	    $rootScope.classEditor.toggle(document.getElementById('flwbtnwt'+index), 'hidden');
	    
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
					$rootScope.classEditor.toggle(document.getElementById('flwbtn'+index), 'hidden');
					$rootScope.classEditor.toggle(document.getElementById('flwbtnwt'+index), 'hidden');
				}
		);
	};
	
$scope.filterByCat = function(index){
	if(index !== $scope.listIndex){
		$scope.searchBoardName = "";
		if(index == -1)
			$scope.filterByCatObj = {};
		else
			$scope.filterByCatObj = {category: {id: $scope.allCategories[index].id}};
		$rootScope.classEditor.remove(document.getElementById('catItem' + $scope.listIndex), 'selected');
		$rootScope.classEditor.add(document.getElementById('catItem'+index),'selected');
		$scope.listIndex = index;
		}
	};

	$scope.reload = function(){
		$rootScope.initAllBoards();
		$scope.filterByCat(-1);
	};
}])