(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	var delay = {time: 1500}
	var sourceDom = $('#questTablelist')
	var sourceName = sourceDom.find('input[name=name]')
	var sourceField = sourceDom.find('textarea[name=fields]')
	var configModle = {
		dom : function() {
		  //保存设置
		  $('#saveSource').bind('click',function(){
		  	saveSource()
		  })

		  $('#file').change(function(){
		  	uploadFile(this, this.id)
		  })
		  
		},
		init : function() {
			configModle.dom()
		}
	}
	window.configModle = configModle
	configModle.init();


	function renderSelect(obj) {
		var html = ''
		for(key in obj) {
			html += '<option value="'+key+'">'+obj[key]+'</option>'
		}
		return html
	}

	function testSource() {
		var staff = true
		if($.trim(sourceName.val()) === ''){
			layer.msg('请填写名称', delay)
			staff = false
			return false
		}
		if($.trim(sourceField.val()) === ''){
			layer.msg('请填写字段', delay)
			staff = false
			return false
		}
		return staff
	}

	function saveSource() {
		if(!testSource()){
			return
		}
		var arr = getParamsData()
		arr.name = sourceName.val()
		arr.fields = sourceField.val()
		var link = tokenParam() + "/datasource/add"
		var result = ajaxBackData(link, arr, "POST")
		if(result.code === ERR_OK()){
			layer.msg('配置成功', delay)
		}
	}

	function uploadFile(obj, id) {
		var link = filePath("uploadLocalSource")
		uploadAttachment(link, obj, id, function(data){
			var result = data
			var index1 = result.indexOf('<pre style="word-wrap: break-word; white-space: pre-wrap;">')
			var index2 = result.indexOf('</pre>')
			result = result.substring(index1+59, index2)
			result = $.parseJSON(result);
			console.log(result)
			if(result.code === ERR_OK()){
				layer.msg('上传成功', delay)
				var list = result.map.fields.join()
				$('#fields').val(list)
			}
		})
	}

})()


