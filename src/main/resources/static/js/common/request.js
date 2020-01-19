/**
 * ajax请求
 */
;
(function(w) {
	axios.defaults.baseURL = '/';
	axios.defaults.timeout = 10000;

	axios.defaults.headers = {
		'Content-Type' : 'application/x-www-form-urlencoded'
	};

	axios.interceptors.response.use(response => {
		return Promise.resolve(response.data);
	}, error => {
		return Promise.reject(error);
	});

	axios.interceptors.request.use(config => {
		if (sessionStorage.getItem('X-Token')) {
			config.headers['X-Token'] = sessionStorage.getItem('X-Token');
		}
		config.data = Qs.stringify(config.data);
		return config;
	},error => {
		console.log(error);
		return Promise.reject(error);
	});

	w.get = function(url, params, option) {
		if(option) {
			for(var property in option) {
				axios.defaults[property] = option[property];
			}
		}
		return new Promise((resolve, reject) => {
			axios.get(url, {params : params})
			.then(response => {
				resolve(response);
			})
			.catch(error => {
				reject(error);
			});
		});
	};

	/**
	 * @description 统一 POST 请求
	 * @param url
	 * @param body
	 *            --> POST 请求 data
	 */
	w.post = function(url, body, option) {
		if(option) {
			for(var property in option) {
				axios.defaults[property] = option[property];
			}
		}
		return new Promise((resolve, reject) => {
			axios.post(url, body)
			.then(response => {
				resolve(response);
			})
			.catch(error => {
				reject(error);
			});
		});
	}
})(window);