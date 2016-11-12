app.factory('userService', ['$http', '$q', userService]);
function userService($http, $q) {

	function getErrorMessage(response) {
		if (response.status == 400) {
			msg = "خطا: درخواست اشتباه است." + response.data.message;
			if (response.data.errors !== undefined)
				msg += "<br />" + response.data.errors.join("<br />");
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

	function getUser() {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/publisher/user'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};

	function getAllCategories() {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/allcategories'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};

	function getAllBoards() {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/allboards'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};

	function subscribe(postData) {
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : postData,
			url : '/phoenix/subscriber/subscribe'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};

	function createNewBoard(newBoard) {
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : newBoard,
			url : '/phoenix/publisher/board'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}

	function sendPost(postData, boardId) {
		var deferred = $q.defer();
		var URL = '/phoenix/publisher/board/' + boardId + '/post';
		var formData = new FormData();
		var newPostBlob = new Blob([angular.toJson(postData.boardPost)], {
			type : 'application/json'
		});
		formData.append("boardPost", newPostBlob);
		formData.append("file", postData.file);
		return $http({
			method : 'POST',
			url : URL,
			headers : {
				'Content-Type' : undefined
			},
			transformRequest : angular.identity,
			data : formData
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}

	function getSystemInfo() {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/systeminfo'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}

	function getMyBoardPosts(boardId, start) {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/publisher/board/' + boardId + '/' + start
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}

	function deletePost(post) {
		var deferred = $q.defer();
		return $http(
				{
					method : 'DELETE',
					url : '/phoenix/publisher/board/' + post.boardId + '/post/'
							+ post.id
				}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}

	function updateBoard(board) {
		var deferred = $q.defer();
		return $http({
			method : 'PUT',
			data : board,
			url : '/phoenix/publisher/board'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}

	function deleteBoard(boardId) {
		var deferred = $q.defer();
		return $http({
			method : 'DELETE',
			url : '/phoenix/publisher/board/' + boardId
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}

	function getBoardStatistics(boardId) {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/publisher/board/' + boardId + "/statistics"
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function getPosts() {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/posts'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};
	
	function getBoardsPosts(boardId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/board/'+boardId+'/posts'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}

	function getPostsBefore(date) {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/posts/before/'+date
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};
	
	function getBoardPostsBefore(boardId,date)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/board/'+boardId+'/posts/before/'+ date
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	var services = {
		getUser : getUser,
		getAllCategories : getAllCategories,
		getAllBoards : getAllBoards,
		subscribe : subscribe,
		createNewBoard : createNewBoard,
		sendPost : sendPost,
		getSystemInfo : getSystemInfo,
		getMyBoardPosts : getMyBoardPosts,
		deletePost : deletePost,
		updateBoard : updateBoard,
		deleteBoard : deleteBoard,
		getBoardStatistics : getBoardStatistics,
		getPosts : getPosts,
		getBoardsPosts : getBoardsPosts,
		getPostsBefore : getPostsBefore,
		getBoardPostsBefore : getBoardPostsBefore
	};
	
	return services;
};