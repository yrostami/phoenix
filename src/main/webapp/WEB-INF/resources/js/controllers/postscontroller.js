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

	$rootScope.showPosts = function(index, boardId) {
		if (index !== $scope.selectedBoardIndex && !httpBusy) {
			$rootScope.classEditor.remove(document.getElementById('board'+ $scope.selectedBoardIndex), 'selected');
			$rootScope.classEditor.add(document.getElementById('board'+index), 'selected');
			if (index !== -1){
				
				var postsArray = new Array();
				for(var i=$rootScope.posts.length - 1; i>=0; i--)
					if($rootScope.posts[i].boardId == boardId)
						postsArray.push($rootScope.posts[i]);
				
				for(var i=implicitPosts.length - 1; i>=0; i--)
					if(implicitPosts[i].boardId == boardId)
						postsArray.push(implicitPosts[i]);
				
				if(postsArray.length == 0)
				{	
					httpBusy = true;
					$rootScope.progress.start();
					getBoardPosts(boardId);
				}
				else $rootScope.targetPosts = postsArray; 
			}
			else
				$rootScope.targetPosts = $rootScope.posts;
			
			$scope.selectedBoardIndex = index;
			$scope.selectedBoardId = boardId;
			$scope.loadFail = false;
		}
	};
	
	$scope.loadMorePosts = function()
	{
		if(httpBusy)
			return;
		$scope.morePostLoad = true;
		if($scope.selectedBoardId == -1)
		{
			if($rootScope.targetPosts !== 0){
			httpBusy = true;
			userService
			.getPostsBefore($rootScope.posts[$rootScope.posts.length - 1].creationDate)
			.then(function success(data)
			{
				if(data.length == 0)
						$rootScope.showGlobalMsg("اطلاعیه دیگری وجود ندارد.", 4);
				else{
					$rootScope.posts = $rootScope.posts.concat(data);
					$rootScope.targetPosts = $rootScope.posts;
				}
				httpBusy = false;
				$scope.morePostLoad = false;
			},
			function fail(msg)
			{
				$rootScope.showGlobalMsg("بار گذاری اطلاهیه ها انجام نشد." + msg, 4);
				httpBusy = false;
				$scope.morePostLoad = false;
				$scope.loadFail = true;
			});
			}else{
				userService.getPosts().then(
						function success(data)
						{
							$rootScope.posts = data;
							$rootScope.targetPosts = $rootScope.posts;
							httpBusy = false;
							$scope.morePostLoad = false;
						},
						function fail(msg)
						{
							$rootScope.showGlobalMsg("بار گذاری اطلاهیه ها انجام نشد." + msg, 4);
							httpBusy = false;
							$scope.morePostLoad = false;
						});
			}
			
		}
		else
		{
			httpBusy = true;
			if($rootScope.targetPosts.length !== 0){
			userService
			.getBoardPostsBefore($scope.selectedBoardId,
					$rootScope.targetPosts[$rootScope.targetPosts.length - 1].creationDate)
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
	}
	
	function getBoardPosts(boardId)
	{
		$rootScope.progress.start();
		userService.getBoardsPosts(boardId)
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