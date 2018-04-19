/**
 * [API description]
 * 全局配置
 * @type {[type]}
 */
var fp, userId, orgId, accountName, userName, companyName
var API = (location.host === 'localhost') ? 'http://'+location.host : 'http://'+location.host

var ssoLink = {
	com : 'http://sso.coolgua.com:202',
	net : 'https://home.coolgua.net',
	default : 'com'
}

/**
 * [if description]
 * @param  {[type]} getUrlParam("fp") [description]
 * @return {[type]}                   [description]
 */
if(getUrlParam("fp") === null) {
	fp = getCookie("fp")
}else{
	fp = getUrlParam("fp");
	setCookie("fp", fp)
}



function getParamsData() {
	var data = new Object();
	return data;
}

function getTokenUrl(){
	return API + "/common/login/"+ fp
}

function tokenParam(param){
	if(!param){
		param = "config"
	}
	return API + "/interface/"+param+"/"+ fp +"/"+orgId+"/"+userId
}

function filePath(param){
	return API + "/mail/file/"+fp+"/"+orgId+"/"+userId+"/"+param
}

function ERR_OK() {
	return 200
}

function taskType(num) {
	switch(num){
		case 0:
			return "触发任务"
			break;
		default:
	  	return ""
	}
}

/**
 * 邮件类型
 */
function mailTypeScale(num) {
	num = parseInt(num)
	switch(num){
		case 0:
			return "标准邮件"
			break;
		case 1:
			return "个性邮件"
			break;
		default:
	  	return ""
	}
}

/**
 * 邮件类型
 */
function dsTypeScale(num) {
	switch(num){
		case 0:
			return "本地上传"
			break;
		case 1:
			return "外部数据源"
			break;
		default:
	  	return ""
	}
}

