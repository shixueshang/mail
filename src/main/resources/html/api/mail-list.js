(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	window.delay = {time: 1500}
	var ITEMINDEX = 0
	var mailDom = $('.coolguaSidebar')
	var tackName = mailDom.find('input[name=name]')
	var subject = mailDom.find('input[name=subject]')
	var dsType = mailDom.find('select[name=dsType]')
	var mailType = mailDom.find('select[name=mailType]')
	var startTime = mailDom.find('input[name=startTime]')
	var endTime = mailDom.find('input[name=endTime]')
	var configListModle = {
		dom : function() {
			$('.task-nav li').bind('click',function(){
				var index = $(this).index()
				ITEMINDEX = index
		    $(this).addClass('active').siblings('li').removeClass('active')
		    var item = $(this).index()+1
		    $('.toolbar input[type=button]').hide()
		    $('.toolbar').find('.btn'+item).show()
		    getFormData(1, 10)
		  })	

		  //条件搜素
		  $('#search').bind('click',function(){	  
		  	getFormData(1, 10)
		  })
		},
		getData: function() {			
			$("#pageSection").Page({"callback":getFormData});
			checkParams(getFormData, [1, 10])
		},
		init : function() {
			configListModle.dom()
			configListModle.getData()
		}
	}
	window.configListModle = configListModle
	configListModle.init();

	function settleBtn(status, id) {
		var html = ""
		if(status === 0) {
			html = '<a href="mail_detail.html?id='+id+'">任务概述</a><a href="mail_view.html?id='+id+'">任务内容</a><a href="javascript:;" onclick="updateStatus(1, \''+id+'\')">关闭</a><a href="mail_create.html?id='+id+'">复制</a>'
		}else if(status === 1) {
			html = '<a href="mail_detail.html?id='+id+'">任务概述</a><a href="mail_view.html?id='+id+'">任务内容</a><a href="javascript:;" onclick="updateStatus(2, \''+id+'\')">删除</a><a href="javascript:;" onclick="updateStatus(0, \''+id+'\')">开启</a>'
		}else{
			html = '<a href="mail_detail.html?id='+id+'">任务概述</a><a href="mail_view.html?id='+id+'">任务内容</a><a href="javascript:;" onclick="updateStatus(1, \''+id+'\')">还原</a>'
		}
		return html
	}

	function searchJson(status, name, mailType, dsType, startTime, endTime, page, size) {
		var obj = {
			status: status,
			name: name,
			mailType: mailType,
			dsType: dsType,
			startTime: startTime,
			endTime: endTime,
			page: page,
			size: size
		}
		return obj
	}

	function getFormData(pageNum, pageSize) {
		var activeIndex = parseInt($('.task-nav .active').attr('state'))
		var ret = searchJson(
			activeIndex,
			tackName.val(),
			clearNull(mailType.val()),
			clearNull(dsType.val()),
			startTime.val(),
			endTime.val(),
			pageNum,
			pageSize
		)
		var arr = getParamsData()
		var link = tokenParam("template") + "/list"
		var result = ajaxBackData(link, ret, "GET")
		if(result.code === ERR_OK()){
			var status = result.map.template.status
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
				html += '<td>'+settleBtn(status, list[key].id)+'</td>'
				html += '<td>'+list[key].name+'</td>'
				html += '<td>'+mailTypeScale(list[key].mailType)+'</td>'
				html += '<td>'+dsTypeScale(list[key].dsType)+'</td>'
				html += '<td>'+list[key].subject+'</td>'
				html += '<td>'+list[key].creator+'</td>'
				html += '<td>'+formatDate(list[key].createTime)+'</td>'
				html += '</tr>'
			}
			
			$("#pageSection").resetPageParameter(total,pageNum,pages);
			$('#tabDataLict').html(html)
		}
	}


})()

function updateStatus(status, id) {
	var arr = getParamsData();
	if(!id){
		var staff = testCheckbox()
		if(staff && staff.code){
			var link = tokenParam("template")+"/batch"
			arr.ids = staff.list
			arr.status = status
			var result = ajaxBackData(link, arr, "POST")
			if(result.code === ERR_OK()){
				layer.msg("操作成功", delay, function(){
					location.reload()
				})
			}
		}
	}else{
		var link = tokenParam("template")+"/single/"+id
		arr.status = status
		console.log(arr)
		var result = ajaxBackData(link, arr, "POST")
		if(result.code === ERR_OK()){
			layer.msg("操作成功", delay, function(){
				location.reload()
			})
		}
	}
}

