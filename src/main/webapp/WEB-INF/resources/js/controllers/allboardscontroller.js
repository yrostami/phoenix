app.controller('allboardscontroller', ['$rootScope', '$scope','userService', allboardscontroller]);
function allboardscontroller($rootScope, $scope, userService)
{
	$scope.filterByCatObj={};
	$scope.listIndex = -1;
	$scope.moreBoardLoadFail = false;
	$scope.moreBoardsLoad = false;
	
	$scope.openOrCloseAccordion = function(index, condition){
		if(!condition)
			return;
	    $rootScope.classEditor.toggle(document.getElementById('acrdn'+index), 'accordion-item-show');
	    $rootScope.classEditor.toggle(document.getElementById('acrdninfo'+index), 'accorrdion-open');
		var acrdnicn = document.getElementById('acrdnicn'+index);
		$rootScope.classEditor.toggle(acrdnicn,"icon-down-open");
		$rootScope.classEditor.toggle(acrdnicn,"icon-up-open")
	};
	
	$scope.subscribe = function(index,id){
	    $rootScope.classEditor.toggle(document.getElementById('flwbtn'+index), 'hidden');
	    $rootScope.classEditor.toggle(document.getElementById('flwbtnwt'+index), 'hidden');
	    
		userService.subscribe(id).then(
				function success(data){
				    var boardIndex = -1; 
				    for(i = $rootScope.allBoards.length - 1; i>=0 ; i--){
					if($rootScope.allBoards[i].id == id){
					    boardIndex = i;
					}
				    }
				    $rootScope.allBoards[boardIndex].isSubscribed = true;
				    $rootScope.user.subscribedBoards.push($rootScope.allBoards[boardIndex]);
				    
				},
				function fail(message){
					$rootScope.showGlobalMsg(
							"انجام نشد." + "\n" + msg, 4);
					$rootScope.classEditor.toggle(document.getElementById('flwbtn'+index), 'hidden');
					$rootScope.classEditor.toggle(document.getElementById('flwbtnwt'+index), 'hidden');
				}
		);
	};
	
$scope.filterByCat = function(index){
		$scope.searchBoardName = "";
		if(index == -1)
			$scope.filterByCatObj = {};
		else
			$scope.filterByCatObj = {category: {id: $scope.allCategories[index].id}};
		$rootScope.classEditor.remove(document.getElementById('catItem' + $scope.listIndex), 'selected');
		$rootScope.classEditor.add(document.getElementById('catItem'+index),'selected');
		$scope.listIndex = index;
	};

//	$scope.reload = function(){
//		$rootScope.initAllBoards();
//		$scope.filterByCat(-1);
//	};
	
	$scope.loadMoreBoard = function()
	{
		$scope.moreBoardsLoad = true;
		
		userService.getAllBoards($rootScope.allBoards.length).
			then(function success(data)
			{
				if(data.length > 0)
					$rootScope.allBoards = $rootScope.allBoards.concat(data);
				else $rootScope.showGlobalMsg(
						"برد دیگری وجود ندارد." + "\n", 4);
				$scope.moreBoardLoadFail = false;
				$scope.moreBoardsLoad = false;
				$scope.filterByCat(-1);
			},
			function fail(msg)
			{
				$scope.moreBoardLoadFail = true;
				$scope.moreBoardsLoad = false;
				$rootScope.showGlobalMsg(
						"بار گذاری بردها بردها انجام نشد." + "\n" + msg, 4);
			});
	}
	
	$scope.isSubscribed = function(boardId)
	{
		for(var i=$rootScope.user.subscribedBoards.length - 1; i>=0 ; i--)
			if($rootScope.user.subscribedBoards[i].id == boardId)
				return true;
		
		return false;
	}
}