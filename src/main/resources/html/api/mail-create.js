(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	var delay = {time: 1500}
	var providerId, sourceData, historyData
	var mailDom = $('.item-mail')
	var campaignId = mailDom.find('input[name=campaignId]')
	var tackName = mailDom.find('input[name=name]')
	var senderName = mailDom.find('input[name=senderName]')
	var senderAddress = mailDom.find('input[name=senderAddress]')
	var replyAddress = mailDom.find('input[name=replyAddress]')
	var subject = mailDom.find('input[name=subject]')
	var dsType = mailDom.find('select[name=dsType]')
	var mailType = mailDom.find('select[name=mailType]')
	var mailField = mailDom.find('select[name=mailField]')
	var content = $('#formnote')
	var scheduled = mailDom.find('input[name=scheduled]')
	var unsubscribe = mailDom.find('input[name=unsubscribe]')
	
	var mailCreateModle = {
		dom : function() {
		  //保存设置
		  $('#saveTask').bind('click',function(){
		  	var type = getUrlParam("id") ? "copy" : "create"
		  	saveTask(type)
		  })
		  scheduled.bind('click', function(){
		  	$('#sendTime').toggle()
		  })
		  $('#configField').change(function(){
		  	chooseMailType(mailType[0])
		  })
		  //标准个性邮件
		  mailType.change(function(){
		  	chooseMailType(this)
		  })
		},
		getData: function() {
			checkParams(historyTemplate)
			checkParams(getMailChannel)
			checkParams(getSourceData)
			//url存在id,copy
			if(getUrlParam("id")){
				initCopyData()
			}
		},
		init : function() {
			mailCreateModle.getData()
			mailCreateModle.dom()
		}
	}
	window.mailCreateModle = mailCreateModle
	mailCreateModle.init();

	function historyTemplate() {
		var link = tokenParam("template") + "/list/history"
		var result = ajaxBackData(link, null, "GET", false)
		console.log(result)
		if(result.code === ERR_OK()){
			var html = renderSelect(result.map.historyTemplate)
			$('#history-task').html(html)
		}
	}

	function getMailChannel() {
		var arr = getParamsData()
		var link = tokenParam() + "/getConfig"
		var result = ajaxBackData(link, null, "GET", false)
		if(result.code === ERR_OK()){
			providerId = result.map.config.providerId
			if(result.map.config.providerId === 2){
				campaignId.parent('li').show()
			}else{
				$('#webpower-transmission').hide()
			}
		}
	}

	function renderSelect(obj) {
		var html = '<option value="">请选择</option>'
		for(key in obj) {
			html += '<option value="'+obj[key].id+'">'+obj[key].name+'</option>'
		}
		return html
	}

	function testTask() {
		var MAXLENGTH = 20
		var staff = true
		if(providerId === 2){
			if(campaignId.val() == ''){
				layer.msg('请填写活动ID', delay)
				staff = false
				return false
			}
		}
		if(tackName.val() === ''){
			layer.msg('请填写任务名称', delay)
			staff = false
			return false
		}
		if(senderName.val() === ''){
			layer.msg('请填写发件人姓名', delay)
			staff = false
			return false
		}else if(senderName.val().length > MAXLENGTH){
			layer.msg('字数超过限制', delay)
			staff = false
			return false
		}

		if(senderAddress.val() === ''){
			layer.msg('请填写发件人邮址', delay)
			staff = false
			return false
		}else if(!isEmail(senderAddress.val())){
			layer.msg('邮件格式错误', delay)
			staff = false
			return false
		}

		if(replyAddress.val() === ''){
			layer.msg('请填写回复邮址', delay)
			staff = false
			return false
		}else if(!isEmail(replyAddress.val())){
			layer.msg('邮件格式错误', delay)
			staff = false
			return false
		}

		if(subject.val() === ''){
			layer.msg('请填写邮件主题', delay)
			staff = false
			return false
		}
		if(dsType.val() === ''){
			layer.msg('请选择数据源', delay)
			staff = false
			return false
		}
		if(mailType.val() === ''){
			layer.msg('请填写邮件类型', delay)
			staff = false
			return false
		}
		if(mailField.val() === ''){
			layer.msg('请填写邮件字段', delay)
			staff = false
			return false
		}
		if(content.val() === ''){
			layer.msg('请填写邮件内容', delay)
			staff = false
			return false
		}

		if(scheduled[0].checked){
			if($('#sendTime').val() == ''){
				layer.msg('请填写定时发送时间', delay)
				staff = false
				return false
			}
		}
		return staff
	}

	function saveTask(type) {
		if(!testTask()){
			return
		}
		var arr = getParamsData()
		arr.name = tackName.val()
		arr.dsType = parseInt(dsType.val())
		arr.mailType = parseInt(mailType.val())
		arr.dsId = $('#configField').val()
		arr.mailField = mailField.val()
		arr.senderName = senderName.val()
		arr.senderAddress = senderAddress.val()
		arr.replyAddress = replyAddress.val()
		arr.subject = subject.val()
		arr.content = content.val()
		if(scheduled[0].checked){
			arr.scheduled = 1
			arr.sendTime = $('#sendTime').val()
		}else{
			arr.scheduled = 0
		}
		if(unsubscribe[0].checked){
			arr.unsubscribe = 1
			arr.unsubscribeLanguage = $('#langSelect').val()
		}else{
			arr.unsubscribe = 0
		}
		if(providerId === 2){
			arr.campaignId = campaignId.val()
		}
		if(type === 'create'){
			var link = tokenParam("template") + "/add"
		}else{
			var link = tokenParam("template") + "/copy"
		}
		//添加附件
		var attachs = []
		$('.attachment-box .attachment-list').each(function(num, value){
			var obj = $(value).find('input[type=hidden]')
			var link = obj.val()
			var name = obj.attr('name')
			attachs.push(name+","+link)
		})
		arr.attachs = attachs
		var result = ajaxBackData(link, arr, "POST")
		if(result.code === ERR_OK()){
			layer.msg('保存任务成功', delay, function(){
				gotoPage('mail_list.html')
			})
		}
	}

	function getSourceData() {
		var arr = getParamsData()
		var link = tokenParam() + "/datasource/list"
		var result = ajaxBackData(link, null, "GET", false)
		if(result.code === ERR_OK()){
			sourceData = result
			var html = ""
			for(key in result.map.page.list){
				var item = result.map.page.list[key]
				html += '<option value="'+item.id+'">'+item.name+'</option>'
			}
			$('#configField').append(html)
		}
	}

	function chooseMailType(target) {
		var obj = $(target).siblings('.item-left')
		if(!target.value || target.value == '0'){
			obj.hide().html("")
			return
		}else{
			obj.show()
		}
		var index = eachItem(sourceData.map.page.list, $('#configField').val())
		var list = sourceData.map.page.list[index].fields.split(',')
		var str = ''
		for(key in list) {
			str += '<option value="'+list[key]+'">'+list[key]+'</option>'
		}
		if(target.value){
			html = ''
			html += '<div class="source-item">'
			html += '<select class="source-field"><option value="">请选择</option>'+str+'</select><span></span>'
			html += '</div>'
			html += '<div style="margin-top:10px;"><span class="icon-jia" ></span></div>'
			obj.html(html)
			bindEvent()
			$('.item-left .icon-jia').bind('click', function(){
				var oSelect = ''
				oSelect += '<div class="source-item">'
				oSelect += '<select class="source-field"><option value="">请选择</option>'+str+'</select><span></span>'
				oSelect += '</div>'
				$(this).parent().before(oSelect)
				bindEvent()
			})

		}
	}

	function bindEvent(){
		$('.source-field').change(function(){
			if(this.value !== ''){
				$(this).siblings('span').html("${"+this.value+"}")
			}
		})
	}

})()

function taskReset() {
	$('.item-mail').find("input[type=text],select[name!=langSelect]").val("")
	$("#configField").val("").hide()
	$('#mailField').html('<option value="">请选择</option>')
	editor.$txt.html("")
	$('#langSelect').find('option[value=cn]').prop('selected', true)
	$('.item-mail').find("input[type=checkbox]").prop("checked",false)
	$("#sendTime").hide()
}

/**
 * [selectItem 选择历史模板]
 * @param  {[type]} copyId [description]
 * @return {[type]}        [description]
 */
function selectItem(copyId) {
	var target = $('.item-mail')
	if(!copyId.value){
		$('.item-mail').find("input[type=text],select[name=]").val("")
		taskReset()
		return
	}
	var id = copyId.value
	var link = tokenParam("template") + "/get/"+id
	var result = ajaxBackData(link, null, "GET", false)
	if(result.code === ERR_OK()){
		console.log(result)
		var list = result.data
		for(key in list){
			if(key == 'name'){
				$('.item-mail').find('input[name=name]').val(list.name+"-副本")
			}else{
				$('.item-mail').find('input[name='+key+']').val(list[key])
			}
		}
		editor.$txt.html(list.content);
		target.find('select[name=dsType]').children('option[value='+list.dsType+']').prop('selected', true)
		target.find('select[name=mailType]').children('option[value='+list.mailType+']').prop('selected', true)
		target.find('select[name=dsId]').children('option[value='+list.dsId+']').prop('selected', true)
		
		if(list.mailField){
			target.find('select[name=mailField]').html('<option value="">请选择</option><option selected value="'+list.mailField+'">'+list.mailField+'</option>')
		}
		if(list.dsType === 0){
			$('#mailUpload').css('display','inline-block').siblings().hide()
		}else{
			$('#configField').css('display','inline-block').siblings().hide()
		}
		if(list.scheduled === 1){
			target.find('input[name=scheduled]').prop('checked', true)
			$('#sendTime').show()
		}
		if(list.unsubscribe === 1){
			target.find('input[name=unsubscribe]').prop('checked', true)
			$('#langSelect').find('option[value='+list.unsubscribeLanguage+']').prop("selected", true)
		}
		//附件回显
		var strs = ""
		for(pn in list.attachs){
			var item = list.attachs[pn].split()
			strs += '<div class="attachment-list">'
			strs += '<a href="'+item[1]+'">'+item[0]+'</a>'
			strs += '<span class="icon-bin deleteIcon">删除</span>'
			strs += '<input type="hidden" name="'+item[0]+'" value="'+item[1]+'"/>'
			strs += '</div>'
			$('.attachment-box').html(strs)
			$('.attachment-list .deleteIcon').bind('click', function(){
				$(this).parent().remove()
			})
		}

	}
}



function selectSource(obj) {
	var parent = $(obj).siblings('.item-left')
	if($(obj).val() === "0"){
		parent.find('.upload-file').css('display','inline-block').siblings().hide()
	}else{
		parent.find('#configField').css('display','inline-block').siblings().hide()
	}
}

function selectConfigField(obj){
	var id = $(obj).val()
	if(!id){
		$('#mailField').html('<option value="">请选择</option>')
		return
	}
	var link = tokenParam("template") + "/getDataSource/"+id
	var result = ajaxBackData(link, null, "GET", false)
	if(result.code === ERR_OK()){
		var html = '<option value="">请选择</option>'
		for(key in result.data){
			html += '<option value="'+result.data[key]+'">'+result.data[key]+'</option>'
		}
		$('#mailField').html(html)
	}else{
		layer.msg(result.message, delay)
		return
	}
}

function initCopyData() {
	var id = getUrlParam("id")
	selectItem(id)
}

function uploadFile(obj, id) {
	var link = filePath("uploadMailAttachments")
	uploadAttachment(link, obj, id, function(data){
		var result = data.match(/\{[^\}]+\}/)[0];
		result = JSON.parse(result)
		if (result.code === ERR_OK()) {
			layer.msg('上传成功', delay)
			if(id === 'source-file'){
				var fields = result.map.fields
				var html = '<option value="">请选择</option>'
				for(key in fields){
					html += '<option value="'+fields[key]+'">'+fields[key]+'</option>'
				}
				$('#mailField').html(html)
			}else{
				var html = ''
				html += '<div class="attachment-list">'
				html += '<a href="'+result.map.address+'">'+result.fileName+'</a>'
				html += '<span class="icon-bin deleteIcon">删除</span>'
				html += '<input type="hidden" name="'+result.fileName+'" value="'+result.map.address+'"/>'
				html += '</div>'
				var target = $('#'+id).parent().siblings('.attachment-box')
				target.append(html)
				$('.attachment-list .deleteIcon').bind('click', function(){
					$(this).parent().remove()
				})
			}
		} else {
			return
		}
	})
	
}

