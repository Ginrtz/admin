/**
 * 标签管理
 */
(function(w) {
	w.Tab = function(title,url){
		this.title = title;
		this.url = url;
		this.index = null;
		
		this.toHtml = function(){
			return ``;
		}
	}
	w.TabManager = function() {
		this.data = [];

		this.getTab = function(id) {
			let _tab = null;
			this.data.forEach(function(tab, index) {
				if (tab.id == id) {
					_tab = tab;
					return _tab;
				}
			});
			return _tab;
		}
		
		
	}
})(window);