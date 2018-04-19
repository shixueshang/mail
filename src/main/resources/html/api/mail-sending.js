(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	var delay = {time: 1500}
	var siderbar = $('.coolguaSidebar')
	var templateName = siderbar.find('input[name=templateName]')
	var mailType = siderbar.find('select[name=mailType]')
	var recipient = siderbar.find('input[name=recipient]')
	var mailSendModle = {
		dom : function() {
			//条件搜素
		  $('#search').bind('click',function(){  
		  	getFormData(1, 10)
		  })
		},
		getData: function() {			
			checkParams(getFormData, [1, 10])
			$("#pageSection").Page({"callback":getFormData});
		},
		init : function() {
			mailSendModle.dom()
			mailSendModle.getData()
		}
	}
	window.mailSendModle = mailSendModle
	mailSendModle.init();



	function getFormData(pageNum, pageSize) {
		var status = clearNull(siderbar.find('input[name=submitResult]:checked').val())
		var arr = getParamsData()
		arr.page = parseInt(pageNum)
		arr.size = parseInt(pageSize)
		arr.templateName = templateName.val()
		arr.mailType = mailType.val()  === "" ? "" : parseInt(mailType.val())
		arr.recipient = recipient.val()
		arr.status = status === "" ? "" : parseInt(status)
		var link = tokenParam("mail") + "/list"
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
				html += '<td>'+itemIndex+'</td>'
				html += '<td>'+list[key].templateName+'</td>'
				html += '<td>'+mailTypeScale(list[key].mailType)+'</td>'
				html += '<td>'+list[key].recipient+'</td>'
				html += '<td>'+list[key].subject+'</td>'
				html += '<td>'+result.map.mailDetailStatus[list[key].status]+'</td>'
				html += '<td>'+formatDate(list[key].createTime)+'</td>'
				html += '</tr>'
			}
			$("#pageSection").resetPageParameter(total,pageNum,pages);
			$('#tabDataLict').html(html)
		}
	}


})()


