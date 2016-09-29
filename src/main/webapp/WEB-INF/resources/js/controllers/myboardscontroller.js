app.controller('myboardscontroller', [
		'$scope',
		'$rootScope',
		'userService',
		function($scope, $rootScope, userService)
		{
			$scope.newBoardPanelShow = false;
			$scope.newPostPanelShow = false;
			$scope.myBoardsMainPanelShow = true;
			var firstNewBoardCreate = true;
			$scope.newBoard = {};
			$scope.newPost = {};
			$scope.selectedItemValue= "-1"; //new board select element item index
			$scope.validationMessageHide= [true,true,true];
			$scope.selectFirstOptionShow = true;
			$scope.file = {};
			$scope.fileName = "هیچ فایلی انتخاب نشده است";
			$scope.boardIndex = -1;
			    
			
			$scope.selectBoard = function(index){
			    if($scope.boardIndex == -1)
				document.getElementById("myBoard"+index).classList.add('selected');
			    else{
			    document.getElementById("myBoard"+$scope.boardIndex).classList.remove('selected');
			    document.getElementById("myBoard"+index).classList.add('selected');
			    }
			    $scope.boardIndex = index;
			    $scope.selectedBoard = $rootScope.user.myBoards[index];
			    
			    if($scope.selectedBoard.posts == undefined || $scope.selectedBoard.posts == null)
			    {
				$scope.selectedBoard.posts = [];
				//load board posts ...
			    }
			}

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
						$rootScope.globalMessage = msg + "\n بارگیری دسته بندی ها انجام نشد.";
						$rootScope.progress.complete();
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
				$scope.selectFirstOptionShow = true;
				$scope.newBoard = {};
				$scope.selectedItemValue = "-1";
				$scope.validationMessageHide= [true,true,true];
			};
			
			var doNewBoardValidatin = function(){
				var isValid = true;
				if($scope.newBoard.name == null 
						|| $scope.newBoard.name.length < 5 
						|| $scope.newBoard.name.length > 50){
					isValid = false;
					$scope.validationMessageHide[0] = false; 
				}
				if($scope.selectedItemValue == "-1"){
					isValid = false;
					$scope.validationMessageHide[1] = false;
				}else{
					$scope.newBoard.category = $rootScope.allCategories[Number($scope.selectedItemValue)];
				}
				if($scope.newBoard.about == null){
					isValid = false;
					$scope.validationMessageHide[2] = false; 
				}
				return isValid;
			} 
			
			$scope.newBoardValidationAndSend = function(){	
				if(doNewBoardValidatin()){
					$rootScope.progress.start();
					$scope.newBoard.publisherId = $rootScope.user.id;
					userService.createNewBoard($scope.newBoard).then(
							function success(data){
								$rootScope.user.myBoards.push(data);
								$rootScope.progress.complete();
								$rootScope.globalMessage = "انجام شد.";
								$scope.hideNewBoardPanel();
							},
							function fail(msg){
								$rootScope.globalMessage = msg;
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
			
			$scope.showNewPostPanel = function()
			{
			    $scope.myBoardsMainPanelShow = false;
			    $scope.newPostPanelShow = true;
			    document.getElementById("imageCheckDiv").style.display = 'inline-block';
			    document.getElementById("fileCheckDiv").style.display = 'inline-block';
			    document.getElementById("imageCheck").checked = false;
			    document.getElementById("fileCheck").checked = false;
			    document.getElementById("uploadInput").style.display = 'none';
			    
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
			    var imageCheckDiv = document.getElementById("imageCheckDiv");
			    var fileCheckDiv = document.getElementById("fileCheckDiv");
			    var uploadInput = document.getElementById("uploadInput");
			    uploadInput.style.display = 'none';
			    imageCheckDiv.style.display = 'inline-block';
			    fileCheckDiv.style.display = 'inline-block';
			    if(checkId == "imageCheck" && document.getElementById("imageCheck").checked){
				fileCheckDiv.style.display = 'none';
				$scope.uploadInputTitle = "انتخاب عکس";
				uploadInput.style.display = 'block';
				$scope.file = {};
				$scope.fileName = "هیچ عکسی انتخاب نشده است";
				document.getElementById("uploadFile").setAttribute("accept","image/*");
			    }
			    else 
				if(checkId == "fileCheck" && document.getElementById("fileCheck").checked){
				imageCheckDiv.style.display = 'none';
				$scope.uploadInputTitle = "انتخاب فایل";
				uploadInput.style.display = 'block';
				$scope.file = {};
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
				
				if($scope.file === {})
				    $scope.sendFile = false;
				else
				    $scope.sendFile = true;
				return isValid;
			}
			
			$scope.newPostValidationAndSend = function(){
			    if(doNewPostValidation()){
				$rootScope.progress.start();
				$scope.newPost.boardId = $rootScope.user.myBoards[$scope.boardIndex].id;
				var postData = { boardPost : $scope.newPost, file : $scope.file};
				userService.sendPost(postData, $scope.newPost.boardId, $scope.sendFile).then(
					function success(data){
					    	$scope.selectedBoard.posts.push(data);
						$rootScope.progress.complete();
						$rootScope.globalMessage = "انجام شد.";
						$scope.hideNewBoardPanel();
					},
					function fail(msg){
						$rootScope.globalMessage = msg;
						$rootScope.progress.complete();
					});
			    }
			}
			
		}]);