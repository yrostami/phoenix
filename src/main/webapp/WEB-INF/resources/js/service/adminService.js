app.factory('adminService', ['$http', '$q', adminService]);

function adminService($http,$q)
{
	function getErrorMessage(response) {
		if (response.status == 400) {
			msg = "خطا: درخواست اشتباه است." + response.data.message;
			if (response.data.errors !== undefined)
				msg += " " + response.data.errors.join("<br />");
			return msg;
		}
		if (response.status == 401)
			window.location.href = "http://localhost:8080/phoenix/";
		if (response.status == 404)
			return "خطا: منبع درخواست شده وجود ندارد.";
		if (response.status == 403)
			return "خطا: اجازه دسترسی برای شما وجود ندارد.";
		if (response.status == 408)
			return "خطا: انتظار سرور برای درخواست به پایان رسیده است.";
		if (response.status == 500)
			return " خطا: سرور دچار خطای داخلی شده است.";
		if (response.status == 406)
			return " خطا: اطلاعات فرستاده شده قابل قبول نیست.";
		else
			return "  خطا: انجام نشد." + "\nstatus : " + response.status;
	};
	
	function getSystemInfo() {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/admin/systeminfo'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function getRequests(firstResult) {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/admin/publishrequest/' + firstResult
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};
	
	function escapeRequest(reqId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/admin/publishrequest/escape/' + reqId
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};
	
	function acceptRequest(reqId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/admin/publishrequest/accept/' + reqId
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};
	
	function updatePassword(updateData)
	{
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : updateData,
			url : '/phoenix/admin/updatepassword'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function updateMaxStorage(updateData)
	{
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : updateData,
			url : '/phoenix/admin/updatesetmaxstorage'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function searchUser(role,searchType,searchWord)
	{
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : searchWord,
			url : '/phoenix/admin/searchuser/'+role+'/'+searchType
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function deleteUser(userId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/admin/deleteuser/' + userId
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function switchUserRoleToSubscriber(userId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/admin/switchtosubscriber/' + userId
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function logout()
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/logout'
		}).then(function success(response) {
			deferred.resolve(response);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};
	
	function sendNewCategory(category)
	{
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : category,
			url : '/phoenix/admin/category'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function deleteCategory(categoryId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'DELETE',
			url : '/phoenix/admin/category/' + categoryId
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function updateCategory(categoryId, categoryNewName)
	{
		var deferred = $q.defer();
		return $http({
			method : 'PUT',
			data : categoryNewName,
			url : '/phoenix/admin/category/'+ categoryId +'/updatename'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function getAllCategories()
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/admin/allcategories'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	var service = 
	{
			logout : logout,
			getSystemInfo : getSystemInfo,
			getRequests : getRequests,
			acceptRequest : acceptRequest,
			escapeRequest : escapeRequest,
			updateMaxStorage : updateMaxStorage,
			updatePassword : updatePassword,
			searchUser : searchUser,
			deleteUser : deleteUser,
			switchUserRoleToSubscriber : switchUserRoleToSubscriber,
			sendNewCategory : sendNewCategory,
			deleteCategory : deleteCategory,
			updateCategory : updateCategory,
			getAllCategories : getAllCategories
	};
	
	return service;
}
