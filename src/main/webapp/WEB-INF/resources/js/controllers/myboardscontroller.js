app.controller('myboardscontroller', [
		'$scope',
		'$rootScope',
		'userService',
		function($scope, $rootScope, userService) {
			$scope.newBoardPanelShow = false;
			$scope.myBoardsMainPanelShow = true;
			var firstNewBoardCreate = true;
			$scope.newBoard = {};
			$scope.selectedItemValue= "-1";
			$scope.validationMessageHide= [true,true,true];
			$scope.selectFirstOptionShow = true;
			$scope.files = [];

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
			
			var doValidatin = function(){
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
			
			$scope.validationAndSend = function(){	
				if(doValidatin()){
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
				
			// توابع مربوط به ارسال پست جدید
			$scope.$on("fileSelected", function (event, args) {
			    $scope.$apply(function () {            
			            $scope.files.push(args.file);
				        });
				    });
			
		}]);