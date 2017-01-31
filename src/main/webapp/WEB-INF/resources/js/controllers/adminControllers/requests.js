app.controller('requestsController', ['$rootScope', '$scope','adminService', requestsController]);

function requestsController($rootScope,$scope,adminService)
{
	var httpBusy = false;
	$scope.moreReqsLoad = false;
	
	$scope.escapeReq = function(reqId)
	{
		if(httpBusy)
			return;
		
		httpBusy = true;
		$rootScope.progress.start();
		
		adminService.escapeRequest(reqId)
			.then(function success(data)
			{
				for(var i = $rootScope.requests.length -1; i>=0; i--)
				{
					if($rootScope.requests[i].id == reqId)
					{
						$rootScope.requests[i].checked = true;
						$rootScope.requests[i].agreement = false;
						break;
					}
				}
				httpBusy = false;
				$rootScope.progress.complete();
			},
			function fail(msg)
			{
				$rootScope.progress.complete();
				httpBusy = false;
				$rootScope.showGlobalMsg("رد درخواست انجام نشد."+"\n"+msg,5);
			});
	};
	
	$scope.acceptReq = function(reqId)
	{
		if(httpBusy)
			return;
		
		httpBusy = true;
		$rootScope.progress.start();
		
		adminService.acceptRequest(reqId)
			.then(function success(data)
			{
				for(var i = $rootScope.requests.length -1; i>=0; i--)
				{
					if($rootScope.requests[i].id == reqId)
					{
						$rootScope.requests[i].checked = true;
						$rootScope.requests[i].agreement = true;
						break;
					}
				}
				httpBusy = false;
				$rootScope.progress.complete();
			},
			function fail(msg)
			{
				$rootScope.progress.complete();
				httpBusy = false;
				$rootScope.showGlobalMsg("موافقت با درخواست انجام نشد."+"\n"+msg,5);
			});
		
	};
	
	$scope.loadMoreReqs = function()
	{
		$scope.moreReqsLoad = true;
		adminService.getRequests($rootScope.requests.length).then(
				function success(data)
				{
					if(data.length > 0)
						$rootScope.requests = $rootScope.requests.concat(data);
					else
						$rootScope.showGlobalMsg("درخواست دیگری وجود ندارد.",5);
					
					$scope.moreReqsLoad = false;
				},
				function fail(msg)
				{
					$scope.moreReqsLoad = false;
					$rootScope.showGlobalMsg("بارگذاری درخواست ها انجام نشد."+"\n"+msg,5);
				});
	}
}