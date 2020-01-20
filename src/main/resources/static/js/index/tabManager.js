/**
 * 标签管理
 */
(function(w) {
	w.Tab = function(id, title, url, index) {
		this.id = id;
		this.title = title;
		this.url = url;
		this.index = index;

		this.toHtml = function() {
			return ``;
		}
	}
	w.TabManager = function() {
		this.data = [];

		this.getTab = function(id) {
			let _tab = null;
			this.data.forEach(function(tab) {
				if (tab.id == id) {
					_tab = tab;
					return _tab;
				}
			});
			return _tab;
		}

		this.removeTab = function(tab) {
			if (tab.index) {
				this.data.splice(index, 1);
			} else if (tab.id) {
				this.data.splice(this.getTab(tab.id).index, 1);
			} else {
				throw '参数必须有index或id属性';
			}
		}

		this.addTab = function(id, title, url) {
			let tab = new Tab(id, title, url, this.data.lenth);
			this.data.push(tab);
		}
		
		
	}
})(window);