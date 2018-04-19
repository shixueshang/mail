(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	var delay = {time: 1500}

	var sendDetailModle = {
		dom : function() {
			$('#addBlack').bind('click', function(){
				addBlack()
			})
		},
		getData: function() {			
			$("#pageSection").Page({"callback":getFormData});
			checkParams(getFormData, [1, 10])
		},
		init : function() {
			sendDetailModle.dom()
			sendDetailModle.getData()
		}
	}
	window.sendDetailModle = sendDetailModle
	sendDetailModle.init();

	
	function getFormData(pageNum, pageSize) {
		
		var arr = getParamsData()
		var link = tokenParam("blacklist") + "/list"
		var result = ajaxBackData(link, null, "GET", false)
		if(result.code === ERR_OK()){
			console.log(result)
			/*var list = result.map.page.list
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
			*/
		}
	}

	function testCheckbox() {
		var staff = {
			code : true
		}
		var msg = "操作成功"
		var target = $('#tabDataLict').find('input[type=checkbox]')
		var checkput = 0;
		var ret = [];
		target.each(function(num, value){
			if(value.checked){
				ret.push($(value).val())
				checkput++
			}
		})
		if(checkput <= 0){
			layer.msg('请选择要操作的记录')
			staff.code = false
			return
		}
		staff.list = ret
		return staff
	}

	function addBlack() {
		var ret = []
		var staff = testCheckbox()
		if(staff && staff.code){
			for(key in staff.list){
				var obj = {}
				var item = staff.list
				obj.recipient = item[key].split('|')[0]
				obj.event = item[key].split('|')[1]
				ret.push(ret)
			}
		}

		var arr = getParamsData()
		var link = tokenParam("blacklist") + "/add"
		var result = ajaxBackData(link, null, "GET", false)
		result = '{"code":200,"list":null,"map":null,"message":"","data":null}'
		result = JSON.parse(result)
		if(result.code === ERR_OK()){
			layer.msg("添加黑名单成功", delay, function(){
				
			})
		}
	}

})()



