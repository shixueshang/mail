(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	var delay = {time: 1500}
	var providerId
	var mailDom = $('.item-mail')
	var campaignId = mailDom.find('span[name=campaignId]')
	var tackName = mailDom.find('span[name=name]')
	var senderName = mailDom.find('span[name=senderName]')
	var senderAddress = mailDom.find('span[name=senderAddress]')
	var replyAddress = mailDom.find('span[name=replyAddress]')
	var subject = mailDom.find('span[name=subject]')
	var dsType = mailDom.find('select[name=dsType]')
	var mailType = mailDom.find('select[name=mailType]')
	var content = $('#formnote')
	var scheduled = mailDom.find('input[name=scheduled]')
	var unsubscribe = mailDom.find('input[name=unsubscribe]')
	var sendTime = mailDom.find('span[name=sendTime]')
	
	var mailCreateModle = {
		getData: function() {
			checkParams(getMailChannel)
			selectItem()
		},
		init : function() {
			mailCreateModle.getData()
		}
	}
	window.mailCreateModle = mailCreateModle
	mailCreateModle.init();


	function getMailChannel() {
		var arr = getParamsData()
		var link = tokenParam() + "/getConfig"
		var result = ajaxBackData(link, null, "GET", false)
		if(result.code === ERR_OK()){
			providerId = result.map.config.providerId
			if(result.map.config.providerId === 2){
				campaignId.parent('li').show()
			}
		}
	}



})()


function selectItem() {
	var target = $('.item-mail')
	var id = getUrlParam("id")
	var link = tokenParam("template") + "/get/"+id
	var result = ajaxBackData(link, null, "GET", false)
	if(result.code === ERR_OK()){
		var list = result.data
		for(key in list){
			$('.item-mail').find('span[name='+key+']').html(list[key])
		}
		$("#formnote").html(list.content);
		target.find('select[name=dsType]').children('option[value='+list.dsType+']').prop('selected', true)
		target.find('select[name=mailType]').children('option[value='+list.mailType+']').prop('selected', true)
		if(list.scheduled === 1){
			target.find('input[name=scheduled]').prop('checked', true)
		}
		if(list.unsubscribe === 1){
			target.find('input[name=unsubscribe]').prop('checked', true)
			$('#langSelect').find('option[value='+list.unsubscribeLanguage+']').prop("selected", true)
		}
	}
}


function initCopyData() {
	var id = getUrlParam("id")
	selectItem(id)
}