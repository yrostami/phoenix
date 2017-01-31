app.controller('settingsController', ['$rootScope', '$scope','adminService', settingsController]);

function settingsController($rootScope,$scope,adminService)
{
	$scope.allCategories = [];
	$scope.allCategoriesLoaded = false;
	$scope.allCategoriesLoadingSpinnerShow = false;
	$scope.selectedCategoryNewName = "";
	$scope.categoryNameChange = false;
	var selectedCategoryId = -1;
	var selectedCategoryIndex = -1;
	
	$scope.openOrCloseFieldset = function(id)
	{
		if(id == '3' && !$scope.allCategoriesLoaded)
			loadAllCategories();
		
		$rootScope.classEditor.toggle(document.getElementById('userInfoFieldset'+id),'open-fieldset');
		$rootScope.classEditor.toggle(document.getElementById('fieldset-icon'+id),'icon-down-open');
		$rootScope.classEditor.toggle(document.getElementById('fieldset-icon'+id),'icon-up-open');
	}
	
	$scope.updatePassword = function()
	{
		var flag = true;
		
		var currentPass = document.getElementById('passwordForUpdate').value;
		var newPass = document.getElementById('newPassword').value;
		
		var passValidationMsg = document.getElementById('passForUpdateValidationMsg');
		var newPassValidationMsg = document.getElementById('newPassValidationMsg');
		
		passValidationMsg.innerText = "";
		newPassValidationMsg.innerText = "";
		
		if(currentPass.length < 8)
		{
			passValidationMsg.innerText = "گذرواژه باید شامل حداقل 8 حرف باشد.";
			flag = false;
		}
		
		if(newPass.length < 8)
		{
			newPassValidationMsg.innerText = "گذرواژه باید شامل حداقل 8 حرف باشد.";
			flag = false;
		}
		if(flag)
		{
			var updateData=
			{
				currentPassword: currentPass,
				newPassword: newPass
			};
			$rootScope.progress.start();
			adminService.updatePassword(updateData).then(
					function success(data)
					{
						$rootScope.progress.complete();
						$rootScope.showGlobalMsg("انجام شد.", 4);
					},
					function fail(msg)
					{
						$rootScope.progress.complete();
						$rootScope.showGlobalMsg("انجام نشد." + "\n" + msg, 4);
					});
		}
	}
	
	
	$scope.updateMaxStorage = function()
	{
		var flag = true;
		
		var passValidationMsg = document.getElementById('PassFNUValidationMsg');
		var newNameValidationMsg = document.getElementById('newMaxStorageValidationMsg');
		
		passValidationMsg.innerText = "";
		newNameValidationMsg.innerText = "";
		
		var pass = document.getElementById('passwordForNameUpdate').value;
		if(pass.length < 8)
		{
			passValidationMsg.innerText = "گذرواژه باید شامل حداقل 8 حرف باشد.";
			flag = false;
		}
		
		var newMaxStorage =  document.getElementById('newMaxStorage').value;
		if(newMaxStorage.length == 0)
		{
			newNameValidationMsg.innerText = "مقدار نباید خالی باشد.";
			flag = false;
		}
		if(flag)
		{
			var updateData = 
			{
				password: pass,
				maxStorage: Number(newMaxStorage)*(1024*1024)
			};
			
			$rootScope.progress.start();
			adminService.updateMaxStorage(updateData).then(
				function success(data)
				{
					$rootScope.systemInfo.maxStroge = updateData.maxStorage;
					document.getElementById('passwordForNameUpdate').value = "";
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("انجام شد.", 4);
				},
				function fail(msg)
				{
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("انجام نشد." + "\n" + msg, 4);
				});
		}
	};
	
	$scope.sendNewCategory = function()
	{
		document.getElementById('categoryNameValidationMsg').innerText = "";
		var newCategoryName = document.getElementById('newCategoryName').value;
		if(newCategoryName.length == 0)
		{
			document.getElementById('categoryNameValidationMsg').innerText = " نام دسته نباید خالی باشد.";
			return;
		}
		
		$rootScope.progress.start();
		adminService.sendNewCategory({name : newCategoryName}).then(
				function success(data)
				{
					$rootScope.progress.complete();
					if($scope.allCategoriesLoaded)
						$scope.allCategories.push(data);
					document.getElementById('newCategoryName').value = "";
				},
				function fail(msg)
				{
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("ایجاد دسته انجام نشد." + "\n" + msg, 4);
				});
	}
	
	function loadAllCategories()
	{
		$scope.allCategoriesLoadingSpinnerShow = true;
		adminService.getAllCategories().then(
				function success(data)
				{
					$scope.allCategories = data;
					$scope.allCategoriesLoadingSpinnerShow = false;
					$scope.allCategoriesLoaded = true;
				},
				function fail(msg)
				{
					$scope.allCategoriesLoadingSpinnerShow = false;
					$rootScope.showGlobalMsg("بارگزاری اطلاعات دسته ها انجام نشد." + "\n" + msg, 4);
				});
	}
	
	$scope.showCategoryEditPanel = function(id,index)
	{
		selectedCategoryId = id;
		selectedCategoryIndex = index;
		$scope.selectedCategoryNewName = $scope.allCategories[index].name;
		document.getElementById('categoryInfoPanel').style.display = 'block';
	}
	
	$scope.categoryNameChangeCheck = function()
	{
		if($scope.selectedCategoryNewName !== $scope.allCategories[selectedCategoryIndex].name
				&& $scope.selectedCategoryNewName.length > 5)
			$scope.categoryNameChange = true;
		else
			$scope.categoryNameChange = false;
	}
	
	$scope.deleteSelectedCategory = function()
	{
		$rootScope.progress.start();
		adminService.deleteCategory(selectedCategoryId).then(
				function success(data)
				{
					$rootScope.progress.complete();
					$scope.allCategories.splice(selectedCategoryIndex, 1);
					document.getElementById('categoryInfoPanel').style.display = 'none';
				},
				function fail(msg)
				{
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("حذف دسته انجام نشد." + "\n" + msg, 5);
				});
	}
	
	$scope.updateCategoryName = function()
	{
		if(!$scope.categoryNameChange)
			return;
		
		var newName = $scope.selectedCategoryNewName;
		$rootScope.progress.start();
		adminService.updateCategory(selectedCategoryId, newName).then(
				function success(data)
				{
					$scope.allCategories[selectedCategoryIndex].name = newName;
					$rootScope.progress.complete();
					document.getElementById('categoryInfoPanel').style.display = 'none';
				},
				function fail(msg)
				{
					$rootScope.progress.complete();
					$rootScope.showGlobalMsg("تغییر انجام نشد." + "\n" + msg, 5);
				});
	}
}