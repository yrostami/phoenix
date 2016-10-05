app.factory('userService', ['$http', '$q', function($http,$q){
	
	var getErrorMessage = function(response){
		if(response.status == 400) 
			return " انجام نشد. خطا: درخواست اشتباه است." + response.data.errorMessage + response.data.errors;
		if(response.status == 404) 
			return " انجام نشد. خطا: منبع درخواست شده وجود ندارد.";
		if(response.status == 403) 
			return " انجام نشد. خطا: اجازه دسترسی برای شما وجود ندارد.";
		if(response.status == 408) 
			return " انجام نشد. خطا: انتظار سرور برای درخواست به پایان رسیده است.";
		if(response.status == 500) 
			return " انجام نشد. خطا: سرور دچار خطای داخلی شده است.";
		if(response.status == 406) 
			return " انجام نشد. خطا: اطلاعات فرستاده شده قابل قبول نیست.";
		else
			return " انجام نشد. خطا: انجام نشد." + "\nstatus : " + response.status;
	};
	
	var services = {
	 	getUser : function()
		{
	 		var deferred= $q.defer();
			return	$http({method: 'GET', url: '/phoenix/publisher/user'}).then(
				function success(response){
					deferred.resolve(response.data);
					return deferred.promise;
					}, 
				function fail(response) {
					deferred.reject(getErrorMessage(response));
					return deferred.promise;
				});
		},

		getAllCategories : function()
		{
			var deferred= $q.defer();
			return $http({method:'GET', url:'/phoenix/subscriber/allcategories'}).then(
				function success(response){
					deferred.resolve(response.data);
					return deferred.promise;
				}, 
				function fail(response) {
					deferred.reject(getErrorMessage(response));
					return deferred.promise;
				});
		},

		getAllBoards : function()
		{
			var deferred= $q.defer();
			return $http({method:'GET', url:'/phoenix/subscriber/allboards'}).then(
				function success(response){
					deferred.resolve(response.data);
					return deferred.promise;
				}, 
				function fail(response) {
					deferred.reject(getErrorMessage(response));
					return deferred.promise;
				});
		},
		
		subscribe : function(postData)
		{
			var deferred= $q.defer();
			return $http({method:'POST', data:postData , url:'/phoenix/subscriber/subscribe'}).then(
				function success(response){
					deferred.resolve(response.data);
					return deferred.promise;
				}, 
				function fail(response) {
				    deferred.reject(getErrorMessage(response));
				    return deferred.promise;
				});
		},
		createNewBoard : function(newBoard)
		{
			var deferred= $q.defer();
			return $http({method:'POST', data:newBoard , url:'/phoenix/publisher/board'}).then(
				function success(response){
					deferred.resolve(response.data);
					return deferred.promise;
				}, 
				function fail(response) {
				    deferred.reject(getErrorMessage(response));
				    return deferred.promise;
				});
		},
		sendPost : function(postData, boardId){
		    var deferred = $q.defer();
		    var URL = '/phoenix/publisher/' + boardId + '/post';
		    var formData = new FormData();
		    var newPostBlob = new Blob([angular.toJson(postData.boardPost)],
			    {type: 'application/json'});
	                formData.append("boardPost", newPostBlob);
	                formData.append("file", postData.file);
		    return $http({
			method:'POST',
			url: URL,
			headers:{'Content-Type': undefined},
			transformRequest: angular.identity,
			data: formData })
			.then(function success(response){
			deferred.resolve(response.data);
	                return deferred.promise;
		    	}, 
		    	function fail(response) {
		    	    deferred.reject(getErrorMessage(response));
		    	    return deferred.promise;
		    	});
		},
		
		getSystemInfo: function(){
		    var deferred = $q.defer();
		    return $http({method:'GET', url:'/phoenix/subscriber/systeminfo'})
		    .then(function success(response){
			deferred.resolve(response.data);
			return deferred.promise;
		    },
		    function fail(response){
			deferred.reject(getErrorMessage(response));
		    	return deferred.promise;
		    });
		    return $http();
		}
	};
	 return services;
}]);
