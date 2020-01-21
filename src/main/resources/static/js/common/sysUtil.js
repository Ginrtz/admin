/**
 * 系统js工具
 */
;
(function(w) {
	w.layer.sysLoading = null;
	w.layer.showLoading = function() {
		w.layer.sysLoading = w.layer.open({
			type : 3,
			content:`<div class="loading-wrap">
				      <div class="k-line k-line-1"></div>
				      <div class="k-line k-line-2"></div>
				      <div class="k-line k-line-3"></div>
				      <div class="k-line k-line-4"></div>
				      <div class="k-line k-line-5"></div>
				    </div>`
		});
	}
	w.layer.closeLoading = function(){
		w.layer.close(w.layer.sysLoading);
	}
})(window);