app.factory('userService', ['$http', '$q', userService]);
function userService($http, $q) {

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

	function getPublisherUser() {
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
	
	function getSubscriberUser() {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/user'
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

	function getAllBoards(firstResult) {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/allboards/' + firstResult
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	};

	function subscribe(boardId) {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/board/'+boardId+'/subscribe'
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
			url : '/phoenix/subscriber/board/' + boardId + "/statistics"
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function getPostNotifications() {
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/postnotifications'
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
	
	function unsubscribe(boardId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/board/'+boardId+'/unsubscribe'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function updatePassword(updateData)
	{
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : updateData,
			url : '/phoenix/subscriber/user/updatepassword'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function updateDisplayName(updateData)
	{
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : updateData,
			url : '/phoenix/subscriber/user/updatename'
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
	}
	
	function deleteAccount(password)
	{
		var deferred = $q.defer();
		return $http({
			method : 'post',
			data: password,
			url : '/phoenix/deleteaccount'
		}).then(function success(response) {
			deferred.resolve(response);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function getBoardPostsAfter(boardId,date)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/board/'+boardId+'/posts/after/' + date
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function getpublishRequests()
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/subscriber/publishrequest'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function sendPublishRequest(postData)
	{
		var deferred = $q.defer();
		return $http({
			method : 'POST',
			data : postData,
			url : '/phoenix/subscriber/publishrequest'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function deletePublishRequest(reqId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'DELETE',
			url : '/phoenix/subscriber/publishrequest/' + reqId
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	function loadBoardSubscribers(boardId)
	{
		var deferred = $q.defer();
		return $http({
			method : 'GET',
			url : '/phoenix/publisher/board/' + boardId + '/subscribers'
		}).then(function success(response) {
			deferred.resolve(response.data);
			return deferred.promise;
		}, function fail(response) {
			deferred.reject(getErrorMessage(response));
			return deferred.promise;
		});
	}
	
	var services = {
		getSubscriberUser : getSubscriberUser,
		getPublisherUser : getPublisherUser,
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
		getPostNotifications : getPostNotifications,
		getBoardsPosts : getBoardsPosts,
		getBoardPostsBefore : getBoardPostsBefore,
		getBoardPostsAfter : getBoardPostsAfter,
		unsubscribe : unsubscribe,
		updateDisplayName : updateDisplayName,
		updatePassword : updatePassword,
		logout : logout,
		deleteAccount : deleteAccount,
		getpublishRequests : getpublishRequests,
		sendPublishRequest : sendPublishRequest,
		deletePublishRequest : deletePublishRequest,
		loadBoardSubscribers : loadBoardSubscribers 
	};
	
	return services;
};