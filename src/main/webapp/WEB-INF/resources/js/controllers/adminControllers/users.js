app.controller('usersController', ['$rootScope', '$scope','adminService', usersController]);

function usersController($rootScope,$scope,adminService)
{
	$scope.users = new Array();
	$scope.searchWordValidationMsg = true;
	$scope.searchWord = "";
	$scope.selectedUser = {};
	
	$scope.searchUsers = function()
	{
		$scope.searchWordValidationMsg = true;
		if($scope.searchWord.length == 0){
			$scope.searchWordValidationMsg = false;
			return;
		}
		var role = "allusers";
		var searchType = "name";
		var selectedRole = document.getElementById("roleSelection").value;
		var selectedType = document.getElementById("typeSelection").value;
		if(selectedRole == "2")
			role = "subscriber";
		else if(selectedRole == "3")
			role = "publisher";
		
		if(selectedType == "2")
			searchType = "email";
		
		$rootScope.progress.start();
		
		adminService.searchUser(role,searchType,$scope.searchWord)
			.then(function success(data)
			{
				if(data.length == 0){
					$rootScope.showGlobalMsg("نتیجه ای یافت نشد.", 3);
					$scope.users = new Array();
				}
				else
					$scope.users = data;
				
				$rootScope.progress.complete();
			},function fail()
			{
				$rootScope.progress.complete();
				$rootScope.showGlobalMsg("جستجو انجام نشد." + "\n" + msg, 5);
			})
	}
	
	$scope.showUserDetail = function(userId)
	{
		for(var i = $scope.users.length - 1; i>=0; i--){
			if($scope.users[i].id == userId){
			$scope.selectedUser =$scope.users[i];
			break;
			}
		}
		document.getElementById("userDetailModal").style.display = 'block'; 
	}
	
	$scope.hideUserDetail = function()
	{
		$scope.selectedUser = {};
		document.getElementById("userDetailModal").style.display = 'none';
	}
	
	$scope.deleteUser = function()
	{
		$rootScope.progress.start();
		adminService.deleteUser($scope.selectedUser.id)
		.then(function success(data)
		{
			for(var i = $scope.users.length - 1; i>=0; i--){
				if($scope.users[i].id == $scope.selectedUser.id){
					$scope.users.splice(i, 1);
					break;
				}
			}
			$scope.hideUserDetail();
			$rootScope.progress.complete();
			$rootScope.showGlobalMsg("انجام شد.", 5);
		},
		function fail(msg)
		{
			$rootScope.showGlobalMsg("حذف انجام نشد." + "\n" + msg, 5);
			$rootScope.progress.complete();
		});
		
	}
	
	$scope.switchUserRoleToSubscriber = function()
	{
		$rootScope.progress.start();
		adminService.switchUserRoleToSubscriber($scope.selectedUser.id).then(
				function success(data)
				{
					for(var i = $scope.users.length - 1; i>=0; i--){
						if($scope.users[i].id == $scope.selectedUser.id){
							$scope.users[i].role = "Subscriber" ;
							break;
						}
					}
					$scope.hideUserDetail();
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("انجام شد.", 5);
				},
				function fail(msg)
				{
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("تغییر اعمال نشد." + "\n" + msg, 5);
				});
	}
}