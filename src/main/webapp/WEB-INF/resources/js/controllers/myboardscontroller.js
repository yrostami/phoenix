app.controller('myboardscontroller',['$compile', '$sce', '$scope','$rootScope','userService',myboardscontroller]);

function myboardscontroller ($compile, $sce, $scope, $rootScope, userService)
{
	$scope.newBoardPanelShow = false;
	$scope.newPostPanelShow = false;
	$scope.myBoardsMainPanelShow = true;
	var firstNewBoardCreate = true;
	$scope.newBoard = {};
	$scope.newPost = {};
	$scope.validationMessageHide= [true,true,true];
	$scope.selectFirstOptionShow = true;
	$scope.file = {};
	$scope.fileName = "هیچ فایلی انتخاب نشده است";
	$scope.boardIndex = -1;
	$scope.boardContentShow = false;
	$scope.morePostLoad = false;
	$scope.editBoard = {};
	$scope.boardEditMod = false;
	$scope.editBoardChange = false;
	var httpBusy = false;
	var editBoardIndex = -1;			
			
	// توابع مربوط به نمایش پست های برد انتخاب شده
	var getPosts = function(board)
	{   
	    httpBusy = true;
	    userService.getMyBoardPosts(board.id, board.posts.length).then(
		    function success(data)
		    {
				$rootScope.progress.complete();
				if(data.length > 0){
					board.posts = board.posts.concat(data);
					$scope.boardContentShow = true;
				}else if(board.firstLoad || board.posts.length == 0)
						$rootScope.showGlobalMsg("هیچ اطلاعیه ای روی این برد وجود ندارد.", 3);
				else $rootScope.showGlobalMsg("اطلاعیه دیگری روی این برد وجود ندارد.", 3);
				httpBusy = false;
				$scope.boardContentShow = true;
				$scope.morePostLoad = false;
				board.firstLoad = false;
		    },
		    function fail(msg)
		    {
				board.loadFail = true;
				$scope.boardContentShow = true;
				$rootScope.progress.complete();
				httpBusy = false;
				$scope.morePostLoad = false;
				$rootScope.showGlobalMsg("بار گذاری اطلاهیه ها انجام نشد." + msg, 4);		
			});
	};
			
			$scope.getImage = function(index, path)
			{
			    var imageDiv = document.getElementById("imageDiv" + index);
			    imageDiv.innerHTML = '<img class="post-img" src="/phoenix/subscriber/getfile/'+path+'">';
			    imageDiv.onclick = function()
			    {
				document.getElementById('imgModal').style.display='block';
				document.getElementById('modalImg').src = '/phoenix/subscriber/getfile/'+path;
			    }
			};
		
			$scope.selectBoard = function(index)
			{
			    if(!httpBusy)
			    {
				if($scope.boardIndex == -1)
				    $rootScope.classEditor.add(document.getElementById("myBoard"+index), 'selected');
				else{
				    $rootScope.classEditor.remove(document.getElementById("myBoard"+$scope.boardIndex), 'selected')
				    $rootScope.classEditor.add(document.getElementById("myBoard"+index), 'selected');
				}
				$scope.boardIndex = index;
				$scope.selectedBoard = $rootScope.user.myBoards[index];
			    
				if($scope.selectedBoard.posts == undefined || $scope.selectedBoard.loadFail)
				{
				    $rootScope.progress.start();
				    $scope.boardContentShow = false;
				    $scope.selectedBoard.posts = new Array();
				    $scope.selectedBoard.loadFail = false;
				    $scope.loaderShow = true;
				    $scope.selectedBoard.firstLoad = true;
				    getPosts($scope.selectedBoard);
			    	}
			    }
			};

			$scope.loadMorePost = function()
			{
			    if(!httpBusy){
			    $scope.morePostLoad = true;
			    getPosts($scope.selectedBoard);
			    }
			};
			
			// توابع مربوط به حذف پست از برد
			$scope.deletePostDialogShow = function(id)
			{
			    document.getElementById('dialogModal').style.display = "block";
				$scope.deletePostId = id;
			}
			
			$scope.deletePost = function()
			{
			    var post = {};
			    var postIndex = -1;
			    for(i = $scope.selectedBoard.posts.length - 1 ; i >= 0 ; i--){
				if($scope.selectedBoard.posts[i].id == $scope.deletePostId)
				{
				    postIndex = i;
				    post = $scope.selectedBoard.posts[i];
				    break;
				}
			    }
			    $rootScope.progress.start();
			    document.getElementById('dialogModal').style.display='none';
			    userService.deletePost(post).then(
				    function success(){
					$scope.selectedBoard.posts.splice(postIndex, 1);
					if(post.fileInfo !== null) $rootScope.user.strogeUsage -= post.fileInfo.fileSize;
					$rootScope.progress.complete();
				    },
				    function(msg){
					$rootScope.showGlobalMsg("حذف اطلاعیه انجام نشد." + msg, 4);
					$rootScope.progress.complete();
				    });
			};
			
			// توابع مربوط به حذف برد
			$scope.deleteBoardDialogShow = function()
			{
			    document.getElementById('dialogModal').style.display = "block";
			}
			
			$scope.deleteBoard = function()
			{
			    $rootScope.progress.start();
			    document.getElementById('dialogModal').style.display='none';
			    userService.deleteBoard($scope.editBoard.id).then(
				    function success()
				    {
					for(i = $rootScope.user.myBoards.length - 1 ; i >= 0 ; i--)
					{
					    if($rootScope.user.myBoards[i].id == $scope.editBoard.id){
						$rootScope.user.myBoards.splice(i, 1);
						break;}
					}
					$rootScope.progress.complete();
					$scope.hideNewBoardPanel();
				    },
				    function(msg)
				    {
					$rootScope.showGlobalMsg("حذف برد انجام نشد." + msg, 4);
					$rootScope.progress.complete();
				    });
			};
			
			// توابع مربوط به ایجاد برد جدید
			$scope.showNewBoardPanel = function() {
				if (firstNewBoardCreate) {
					$rootScope.progress.start();
					userService.getAllCategories().then(function(data) {
						$rootScope.allCategories = data;
						$scope.myBoardsMainPanelShow = false;
						$scope.newBoardPanelShow = true;
						firstNewBoardCreate = false;
						$rootScope.progress.complete();
					}, function(msg) {
					    $rootScope.progress.complete();
					    $rootScope.showGlobalMsg("بارگیری دسته بندی ها انجام نشد."+"\n"+msg, 5);
					});
				}
				else{
					$scope.myBoardsMainPanelShow = false;
					$scope.newBoardPanelShow = true;
					}
			};
			
			$scope.hideNewBoardPanel = function() {
				$scope.myBoardsMainPanelShow = true;
				$scope.newBoardPanelShow = false;
				$scope.boardEditMod = false;
				$scope.editBoardChange = false;
				$scope.newBoard = {};
				$scope.validationMessageHide= [true,true,true];
			};
			
			var doNewBoardValidatin = function(){
			    $scope.validationMessageHide= [true,true,true];
				var isValid = true;
				if($scope.newBoard.name == null 
						|| $scope.newBoard.name.length < 5 
						|| $scope.newBoard.name.length > 50){
					isValid = false;
					$scope.validationMessageHide[0] = false; 
				}
				if($scope.newBoard.category == undefined || $scope.newBoard.category == null)
				{
				    isValid = false;
				    $scope.validationMessageHide[1] = false;
				}
				
				if($scope.newBoard.about == null){
					isValid = false;
					$scope.validationMessageHide[2] = false; 
				}
				return isValid;
			};
			
			$scope.newBoardValidationAndSend = function(){	
				if(doNewBoardValidatin()){
					$rootScope.progress.start();
					$scope.newBoard.publisherId = $rootScope.user.id;
					userService.createNewBoard($scope.newBoard).then(
							function success(data){
								$rootScope.user.myBoards.push(data);
								$rootScope.progress.complete();
								$rootScope.showGlobalMsg( "انجام شد.", 3);
								$scope.hideNewBoardPanel();
							},
							function fail(msg){
								$rootScope.showGlobalMsg("ایجاد برد جدید انجام نشد."+"\n"+msg, 5);
								$rootScope.progress.complete();
							});
				}
				};
				
			// توابع مربوط به ارسال اطلاعیه جدید
			$scope.$on("fileSelected", function (event, args) {
			    $scope.$apply(function () {            
			            $scope.file = args.file;
			            $scope.fileName = args.file.name;
				        });
				    });
			$scope.$on("fileSelectedError", function (event, args) {
			    $scope.$apply(function () {            
			            $rootScope.showGlobalMsg("مرورگر internet explorer در نسخه های پایین تر از 10 از پیوست فایل پشتیبانی نمی کند. لطفا از مرورکر دیکری استفاده کنید.", 5);
				        });
				    });
			
			$scope.uploadStrogeMsgShow = function(){
			    var show = false;
			    if($rootScope.systemInfo !== undefined )
			    {
				var stroge = $rootScope.systemInfo.maxStroge - $rootScope.user.strogeUsage;
				if(stroge !== 0)
				{  
				var B = stroge % (1024*1024);
				stroge = stroge - B;
				var MB = (stroge/1024)/1024;
				var KB = (B - (B % 1024)) / 1024;
				$scope.strogeSize = "حداکثر فضای آپلود شما: " + MB + "MB, " + KB +"KB";  
				show = true;
				}
			    }
			    return show;
			}
			
			$scope.showNewPostPanel = function()
			{
			    $scope.myBoardsMainPanelShow = false;
			    $scope.newPostPanelShow = true;
			    document.getElementById("imageCheck").checked = false;
			    document.getElementById("fileCheck").checked = false;
			    $scope.imageCheckShow = true;
			    $scope.fileCheckShow = true;
			    $scope.fileUploadShow = false;
			    
			}
			
			$scope.hideNewPostPanel = function()
			{
			    $scope.myBoardsMainPanelShow = true;
			    $scope.newPostPanelShow = false;
			    $scope.newPost = {};
			    $scope.file = {};
			    $scope.validationMessageHide = [true,true,true];
			}
			
			$scope.checked = function(checkId)
			{
			    $scope.file = {};
			    $scope.imageCheckShow = true;
			    $scope.fileCheckShow = true;
			    $scope.fileUploadShow = false;
			    if(checkId == "imageCheck" && document.getElementById("imageCheck").checked){
				$scope.fileCheckShow = false;
				$scope.uploadInputTitle = "انتخاب عکس";
				$scope.fileUploadShow = true;
				$scope.fileName = "هیچ عکسی انتخاب نشده است";
				document.getElementById("uploadFile").setAttribute("accept","image/*");
			    }
			    else 
				if(checkId == "fileCheck" && document.getElementById("fileCheck").checked){
				$scope.imageCheckShow = false;
				$scope.uploadInputTitle = "انتخاب فایل";
				$scope.fileUploadShow = true;
				$scope.fileName = "هیچ فایلی انتخاب نشده است";
				document.getElementById("uploadFile").setAttribute("accept","");
			    }
			}
			
			var doNewPostValidation = function(){
			    var isValid = true;
				if($scope.newPost.title == null 
					|| $scope.newPost.title == undefined 
						|| $scope.newPost.title.length > 150){
					isValid = false;
					$scope.validationMessageHide[0] = false;}
				if($scope.newPost.content == null
					|| $scope.newPost.title.length > 1500){
					isValid = false;
					$scope.validationMessageHide[1] = false; 
				}
				return isValid;
			}
			
			$scope.newPostValidationAndSend = function(){
			    if(doNewPostValidation()){
				$rootScope.progress.start();
				$scope.newPost.boardId = $rootScope.user.myBoards[$scope.boardIndex].id;
				var postData = { boardPost : $scope.newPost, file : $scope.file};
				userService.sendPost(postData, $scope.newPost.boardId).then(
					function success(data){
					    	$scope.selectedBoard.posts.push(data);
						$rootScope.progress.complete();
						$rootScope.globalMessage = "انجام شد.";
						if(data.fileInfo !== null) $rootScope.user.strogeUsage += data.fileInfo.fileSize;
						$scope.hideNewPostPanel();
					},
					function fail(msg){
					    	$rootScope.showGlobalMsg("نصب اطلاعیه جدید انجام نشد."+"\n"+msg, 5);
						$rootScope.progress.complete();
					});
			    }
			}
			
			// توابع مربوط به نمایش اطلاعات مربوط به بردها
			$scope.showMyBoardInfo = function(index)
			{
			    editBoardIndex = index;
			    $scope.boardEditMod = true;
			    $scope.editBoard = $rootScope.user.myBoards[index];
			    $scope.newBoard = {
				    id: $scope.editBoard.id,
				    publisherId: $scope.editBoard.publisherId,
				    name: $scope.editBoard.name,
				    about: $scope.editBoard.about,
				    category: 
				    	{
					name: $scope.editBoard.category.name,
					id: $scope.editBoard.category.id
					}
			    };
			    $scope.showNewBoardPanel();
			    
			    if($scope.editBoard.postCount == undefined 
				    || $scope.editBoard.subscriberCount == undefined
				    || $scope.editBoard.postCount == null
				    || $scope.editBoard.subscriberCount == null){
				
			    	userService.getBoardStatistics($scope.editBoard.id).then(
			    		function success(data)
			    		{
			    		    $scope.editBoard.postCount = data.postCount;
			    		    $scope.editBoard.subscriberCount = data.subscriberCount;
			    		},
			    		function fail()
			    		{
			    		    $scope.editBoard.postCount == null;
			    		    $scope.editBoard.subscriberCount == null;
			    		}
			    	);
			    }
			};
			
			$scope.editBoardChangeCheck = function(itemNumber)
			{
			    if($scope.boardEditMod){
				
			    	    if ($scope.editBoard.name !== $scope.newBoard.name
			    	    || $scope.editBoard.category.id !== $scope.newBoard.category.id
			    	    || $scope.editBoard.about !== $scope.newBoard.about)
					    $scope.editBoardChange = true;
			    	    else
			    		$scope.editBoardChange = false;
			    }
			};
			
			$scope.boardValidationAndUpdate = function()
			{
			    if(!doNewBoardValidatin() || !$scope.editBoardChange)
				return;
			    $rootScope.progress.start();
				userService.updateBoard($scope.newBoard).then(
						function success(data){
							$rootScope.user.myBoards[editBoardIndex] = data;
							$rootScope.progress.complete();
							$rootScope.showGlobalMsg("انجام شد.",3);
							$scope.hideNewBoardPanel();
						},
						function fail(msg){
							$rootScope.showGlobalMsg("اعمال تغییرات انجام نشد."+"\n"+msg, 5);
							$rootScope.progress.complete();
						});
			};
}