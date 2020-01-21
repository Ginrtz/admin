/**
 * 标签管理
 */
(function(w) {
	Tab = function(){};
	Tab.prototype.id = null;
	Tab.prototype.title = null;
	Tab.prototype.url = null;
	Tab.prototype.index = null;
	
	Tab.prototype.toHtml = function() {
		let url = this.url;
		let id = this.id;
		let index = this.index;
		let title = this.title;
		return `<a href="${url}" tab-iId="${id}" tab-idx="${index}">
				${title}
				<i class="fa fa-times-circle"></i>
				</a>`;
	}
	
	TabManager = function(){}
	TabManager.prototype.createTab = function(id,title,url){
		let tab = new Tab();
		tab.id = id;
		tab.title = title;
		tab.url = url;
		tab.index = this.data.length;
		return tab;
	}
	TabManager.prototype.data = [];
	TabManager.prototype.currTab = null;
	TabManager.prototype.size = function(){
		return this.data.length;
	}
	TabManager.prototype.getTab = function(id) {
		let _tab = null;
		this.data.forEach(function(tab) {
			if (tab.id == id) {
				_tab = tab;
				return _tab;
			}
		});
		return _tab;
	}

	TabManager.prototype.removeTab = function(id) {
		$('#tabnav a[tab-id="'+id+'"]').remove();
		this.data.splice(this.getTab(id).index, 1);
	}

	TabManager.prototype.addTab = function(id, title, url) {
		let tab = this.createTab(id, title, url);
		this.currTab = tab;
		this.data.push(tab);
		let nav = $('#tabnav');
		nav.append(tab.toHtml());
	}
	
	TabManager.activeTab = function(id){
		if(currTab){
			$('#'+currTab.id).removeClass('current');
		}
		$('#tabnav a[tab-id="'+id+'"]').addClass('current');
	}
	
	w.tabMgr = new TabManager();
})(window);