(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	var delay = {time: 1500}
	var config
	var mailChannel = $('#mailChannel')
	var providerId = mailChannel.find('select')
	var accountName = mailChannel.find('input[name=accountName]')
	var accountPass = mailChannel.find('input[name=accountPass]')
	var configModle = {
		dom : function() {
			$('.task-nav li').bind('click',function(){
				var index = $(this).index()
		    $(this).addClass('active').siblings('li').removeClass('active')
		    $('.tab-box').eq(index).show().siblings('.tab-box').hide()
		    if(index === 0){
 					$('.configture_btn').show()
		    }else{
		    	$('.configture_btn').hide()
		    }
		  })

			$('#providerId').change(function(){
				if(config.providerId === parseInt(this.value)){
					accountName.val(config.accountName)
					accountPass.val(config.accountPass)
				}else{
					accountName.val("")
					accountPass.val("")
				}
			})

		  //保存设置
		  $('#saveConfig').bind('click',function(){
		  	saveConfig()
		  })
		  
		},
		getData: function() {			
			checkParams(getMailChannel, [1])
			$("#pageSection").Page({"callback":getSourceData});
			checkParams(getSourceData, [1, 10])
		},
		init : function() {
			configModle.dom()
			configModle.getData()
		}
	}
	window.configModle = configModle
	configModle.init();



	function getMailChannel() {
		var arr = getParamsData()
		var link = tokenParam() + "/getConfig"
		var result = ajaxBackData(link, null, "GET", false)
		if(result.code === ERR_OK()){
			config = result.map.config
			providerId.html(renderSelect(result.map.mailProviders))
			accountName.val(result.map.config.accountName)
			accountPass.val(result.map.config.accountPass)
		}
	}

	function renderSelect(obj) {
		var html = ''
		for(key in obj) {
			html += '<option value="'+key+'">'+obj[key]+'</option>'
		}
		return html
	}

	function testConfig() {
		var staff = true
		if(accountName.val() === ''){
			layer.msg('请填写邮件账号', delay)
			staff = false
			return false
		}
		if(accountPass.val() === ''){
			layer.msg('请填写邮件密码', delay)
			staff = false
			return false
		}
		return staff
	}

	function saveConfig() {
		if(!testConfig()){
			return
		}
		var arr = getParamsData()
		arr.providerId = parseInt(providerId.val())
		arr.accountName = accountName.val()
		arr.accountPass = accountPass.val()
		var link = tokenParam() + "/addOrUpdate"
		var result = ajaxBackData(link, arr, "POST")
		if(result.code === ERR_OK()){
			layer.msg('配置成功', delay)
		}
	}

	function getSourceData() {
		var arr = getParamsData()
		var link = tokenParam() + "/datasource/list"
		var result = ajaxBackData(link, null, "GET", false)
		if(result.code === ERR_OK()){
			var html = splitTable(result.map.page.list, ['name', 'fields'])
			var total = result.map.page.total
			var pageNum = result.map.page.pageNum
			var pages = result.map.page.pages
			$("#pageSection").resetPageParameter(total,pageNum,pages);
			$('#tbodyMain').html(html)
		}
	}

})()


