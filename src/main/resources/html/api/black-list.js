(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	var delay = {time: 1500}
	var mailDom = $('.coolguaSidebar')
	var recipient = mailDom.find('input[name=recipient]')
	var blackListModle = {
		dom : function() {
			//条件搜素
		  $('#search').bind('click',function(){	  
		  	getFormData(1, 10)
		  })
		  //移除黑名单
		  $('#removeBackList').bind('click',function(){	  
		  	removeBackList()
		  })
		},
		getData: function() {			
			$("#pageSection").Page({"callback":getFormData});
			checkParams(getFormData, [1, 10])
		},
		init : function() {
			blackListModle.dom()
			blackListModle.getData()
		}
	}
	window.blackListModle = blackListModle
	blackListModle.init();

	
	function getFormData(pageNum, pageSize) {
		var arr = getParamsData()
		arr.recipient = recipient.val()
		var link = tokenParam("blacklist") + "/list"
		var result = ajaxBackData(link, arr, "GET")
		if(result.code === ERR_OK()){
			var list = result.map.page.list
			var total = result.map.page.total
			var pageSize = result.map.page.pageSize
			var pageNum = result.map.page.pageNum
			var pages = result.map.page.pages
			var html = ""
			for(key in list){
				var itemIndex = getPageIndex(pageSize, pageNum, key)
				html += '<tr>'
				html += '<td><input type="checkbox" class="checkbox" name="checkbox" value="'+list[key].id+'"/>'+itemIndex+'</td>'
				html += '<td>'+list[key].recipient+'</td>'
				html += '<td>'+list[key].creator+'</td>'
				html += '<td>'+formatDate(list[key].createTime)+'</td>'
				html += '</tr>'
			}	
			$("#pageSection").resetPageParameter(total,pageNum,pages);
			$('#tabDataLict').html(html)
		}
	}

	function removeBackList() {
		var status = testCheckbox()
		if(status.code){
			var link = tokenParam("blacklist") + "/remove"
			var arr = getParamsData()
			arr.ids = status.list
			var result = ajaxBackData(link, arr, "POST")
			if(result.code === ERR_OK()){
				layer.msg('移除成功',delay, function(){
					location.reload()
				})
			}
		}
	}


})()



