app.controller('postscontroller', ['$rootScope', '$scope', 'userService',
		postscontroller]);
function postscontroller($rootScope, $scope, userService) {

	$scope.selectedBoardIndex = -1;
	$scope.selectedBoardId = -1;
	var implicitPosts = new Array();
	$scope.loadFail = false;
	$scope.morePostLoad = false;
	var httpBusy = false;
	$scope.showBoard = {about: ' '};
	$scope.postsControllerMainPanelShow = true;

	$rootScope.showPosts = function(index, board) {
		if (index !== $scope.selectedBoardIndex && !httpBusy) {
			if($scope.selectedBoardIndex !== -1)
				$rootScope.classEditor.remove(document.getElementById('board'+ $scope.selectedBoardIndex), 'selected');
			
			$rootScope.classEditor.add(document.getElementById('board'+index), 'selected');
				
				var postsArray = new Array();
				
				for(var i=implicitPosts.length - 1; i>=0; i--)
					if(implicitPosts[i].boardId == board.id)
						postsArray.push(implicitPosts[i]);
				
				if(postsArray.length == 0)
				{	
					httpBusy = true;
					$rootScope.progress.start();
					getBoardPosts(board);
				}else {
					$rootScope.targetPosts = postsArray;
					if(board.notificationCount > 0)
					{
						httpBusy = true;
						$rootScope.progress.start();
						getBoardNewPosts(board);
					}
				}
			
			$scope.selectedBoardIndex = index;
			$scope.selectedBoardId = board.id;
			$scope.loadFail = false;
		}
	};
	
	function getBoardPostsAfter(board)
	{
		var length = $rootScope.targetPosts.length;
		var date = $rootScope.targetPosts[0].creationDate;
		
		for(var i = 1; i < length -1 ; i++)
			if($rootScope.targetPosts[i].creationDate > date )
				date = $rootScope.targetPosts[i].creationDate;
		
		$rootScope.progress.start();
		userService.getBoardPostsAfter(board.id , date)
			.then(function success(data)
			{
				httpBusy = false;
				$rootScope.targetPosts = $rootScope.targetPosts.concat(data);
				implicitPosts = implicitPosts.concat(data);
				board.notificationCount = 0;
				$rootScope.progress.complete();
			},
			function fail(msg)
			{
				httpBusy = false;
				$rootScope.progress.complete();
				$rootScope.showGlobalMsg("بار گذاری اطلاهیه های جدید انجام نشد." + msg, 4);
			});
	}
	
	$scope.loadMorePosts = function()
	{
		if(httpBusy)
			return;
	
		var length = $rootScope.targetPosts.length;
		var date = $rootScope.targetPosts[length].creationDate;
		
		for(var i = 0; i < length -2 ; i++)
			if(date > $rootScope.targetPosts[i].creationDate)
				date = $rootScope.targetPosts[i].creationDate;
		
			$scope.morePostLoad = true;
			httpBusy = true;
			if(length !== 0){
			userService
			.getBoardPostsBefore($scope.selectedBoardId, date)
					.then(function success(data)
					{
						if(data.length == 0){
							if($rootScope.targetPosts.length == 0)
								$rootScope.showGlobalMsg("اطلاعیه ای روی این برد وجود ندارد.", 3);
							else
								$rootScope.showGlobalMsg("اطلاعیه دیگری روی این برد وجود ندارد.", 3);
						}
						else{
							$rootScope.targetPosts = $rootScope.targetPosts.concat(data);
							implicitPosts = implicitPosts.concat(data);
						}
						httpBusy = false;
						$scope.morePostLoad = false;
						$scope.loadFail = false;
					},
					function fail(msg)
					{
						$rootScope.showGlobalMsg("بار گذاری اطلاهیه ها انجام نشد." + msg, 4);
						httpBusy = false;
						$scope.loadFail = true;
						$scope.morePostLoad = false;
					});
			}else{
				$rootScope.progress.start();
				getBoardPosts($scope.selectedBoardId)
			}
	}
	
	function getBoardPosts(board)
	{
		$rootScope.progress.start();
		userService.getBoardsPosts(board.id)
		.then(
			function success(data) {
				if(data.length == 0){
					$rootScope.showGlobalMsg("هیچ اطلاعیه ای روی این برد وجود ندارد.", 4);
					$rootScope.targetPosts = [];
				}
				else{
					$rootScope.targetPosts = data;
					implicitPosts = implicitPosts.concat(data);
				}
				$rootScope.progress.complete();
				httpBusy = false;
				$scope.morePostLoad = false;
				board.notificationCount = 0;
			},
			function fail(msg) {
				$rootScope.progress.complete();
				httpBusy = false;
				$rootScope.showGlobalMsg("بار گذاری اطلاهیه ها انجام نشد." + msg, 4);
				$scope.morePostLoad = false;
			});
	}

	$scope.getImage = function(index, path) {
		var imageDiv = document.getElementById('postImageDiv' + index);
	    var postImageViewTitle = document.getElementById('postImageViewTitle'+index);
	    var postImageTitle = document.getElementById('postImageTitle'+index);
	    var postImageLoader = document.getElementById('postImageLoader'+index);
	    postImageViewTitle.style.display = 'none';
	    postImageLoader.style.display = 'inline-block';
	    var img = new Image();
	    img.className = 'post-img';
	    img.onload = function()
	    {
	    	postImageLoader.style.display = 'none';
	    	postImageTitle.style.display = 'none';
	    	imageDiv.appendChild(this);
	    };
	    
	    img.onerror = function()
	    {
	    	postImageLoader.style.display = 'none';
	    	postImageViewTitle.style.display = 'inline-block';
	    };
	    
	    img.src = '/phoenix/subscriber/getfile/'+path;
	    
	    imageDiv.onclick = function()
	    {
	    	document.getElementById('imgModal').style.display='block';
	    	document.getElementById('modalImg').src = '/phoenix/subscriber/getfile/'+path;
	    }
	};

	$scope.getBoardName = function(id) {
		for (var i = $rootScope.user.subscribedBoards.length - 1; i >= 0; i--)
			if ($rootScope.user.subscribedBoards[i].id == id)
				return $rootScope.user.subscribedBoards[i].name;
	}
	
	$scope.showSubscribedBoardInfo = function(index, boardId)
	{
		$scope.showBoard = $rootScope.user.subscribedBoards[index];
		document.getElementById('boardInfoModal').style.display='block';
		if($scope.showBoard.postCount == undefined 
			    || $scope.showBoard.subscriberCount == undefined
			    || $scope.showBoard.postCount == null
			    || $scope.showBoard.subscriberCount == null){
			
				httpBusy = true;
		    	userService.getBoardStatistics($scope.showBoard.id).then(
		    		function success(data)
		    		{
		    			httpBusy = false;
		    		    $scope.showBoard.postCount = data.postCount;
		    		    $scope.showBoard.subscriberCount = data.subscriberCount;
		    		},
		    		function fail()
		    		{
		    			httpBusy = false;
		    		    $scope.showBoard.postCount == null;
		    		    $scope.showBoard.subscriberCount == null;
		    		}
		    	);
		    }
	}
	
	$scope.unsubscribe = function()
	{
		document.getElementById('boardInfoModal').style.display='none';
		httpBusy = true
		$rootScope.progress.start();
		userService.unsubscribe($scope.showBoard.id)
		.then(function success(data)
		{
			if($scope.showBoard.id == $scope.selectedBoardId)
				$scope.showPosts(-1, -1);
			
			for(var i= $rootScope.user.subscribedBoards.length - 1 ; i>=0 ; i--)
				if($rootScope.user.subscribedBoards[i].id == $scope.showBoard.id)
				{
					$rootScope.user.subscribedBoards.splice(i,1);
					break;
				}
			$rootScope.progress.complete();
			httpBusy = false;
			
		},
		function fail(msg)
		{
			$rootScope.progress.complete();
			httpBusy = false;
			$rootScope.showGlobalMsg("لغو دنبال کردن انجام نشد." + msg, 4);
		}
		);
	}
}