function closelayer(){
	layer.closeAll();
}

function clearNull(name){
	if(name == null || name == undefined || name == 'undefined' ){
		return "";
	}
	return name;
}

function transParam(obj){
	var str = "";
	for(key in obj){
		var param = "&"+key+"="+obj[key]
		str += param
	}
	return str
}

function getJsonp(url){
  var script = document.createElement("script");
  script.type = "text/javascript";
  script.src = url;
  document.getElementsByTagName("head")[0].appendChild(script);
}

function getUrlParam(name){
	var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r!=null)return  unescape(r[2]); return null;
}

function setCookie(name,value){
	delCookie(name)
	var Days = 30;
	var exp = new Date();
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString()+";path=/";
}

function getCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg)){
		return unescape(arr[2]);
	}else{
		return null;
	}
}
function delCookie(name){ 
  var exp = new Date(); 
  exp.setTime(exp.getTime() - 1); 
  var cval=getCookie(name); 
  if(cval!=null){ 
    document.cookie= name + "="+cval+";expires="+exp.toGMTString()+";path=/"; 
  }
} 

function checkAll(target,name){
	if(target.checked){
		$('input[type=checkbox][name='+name+']').prop('checked',true);
	}else{
		$('input[type=checkbox][name='+name+']').prop('checked',false);
	}
}

function commonRequire() {
	var index = layer.load(0, {shade: false});
	return index
}

function commonCallback(index) {
	setTimeout(function(){
		$('#loading').hide()
		$('.wrapper, .task-nav').css('visibility','visible');
	},400)
}

function checkParams(fn, args) {
	var timer = setInterval(function(){
		if(orgId && userId){
			clearInterval(timer)
			return fn.apply(this, args); 
		}
	},200)
}

function sendSms(){
	smsPlatform("checkName");
};

function getPageIndex(pageSize, pageNo, index) {
	var num = pageSize*(pageNo-1);
	num = num + parseInt(index) + 1;
	return num
}

function formatDate(timestamp) {
  var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
  Y = date.getFullYear()
  M = (date.getMonth()+1)
  D = date.getDate()
  h = date.getHours()
  m = date.getMinutes()
  s = date.getSeconds()
  return Y+"-"+getzf(M)+"-"+getzf(D)+" "+getzf(h)+":"+getzf(m)+":"+getzf(s)
  function getzf(num){  
    if(parseInt(num) < 10){  
        num = '0'+num;  
    }  
    return num;  
  }
}

function splitTable(arr,need) {
	var html = ""
	for(key in arr) {
		html += "<tr>"
		for(pn in need){
			var item = need[pn]
			html += '<td>'+arr[key][item]+'</td>'
		}
		html += "</tr>"
	}
	
	return html
}

function reset(parent, btn) {
	$(parent).find('input[type=text], select').val("")
	$(btn).click()
}

function eachItem(list, id) {
	var index = 0
	for(key in list){
		if(list[key].id === id){
			index = key
		}
	}
	return index
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

/**
 * 公共校验JS
 * 
 * @returns {String}
 */
function gotoPage(url) {
	location.href = url;
}

function isNumber(val) {
	var re = /^[0-9]+\.?[0-9]{0,9}$/; //判断字符串是否为数字 //判断正整数 /^[1-9]+[0-9]*]*$/ 
	return re.test(val);
}

function isInt(val, length) {
	return new RegExp("^[0-9]{1}[0-9]{0," + length + "}$").test(val);
}

function isFloat(val, length, decimalLength) {
	var aaa = new RegExp("^[0-9]{1," + length + "}(.[0-9]{1," + decimalLength
			+ "})?$");
	return aaa.test(val);
}

function isEmail(value) {
	return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/g.test(value);
}

function isMobile(value) {
	return /^1[3|4|5|7|8][0-9]{9}$/.test(value);
}

function isTel(value){
	var pattern =/^(([0\+]\d{2,3}-)?(0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/;
	return pattern.exec(value);
}


function transJson() {

}

/**
 * [ajaxBackData description]
 * @param  {[type]}  url     [description]
 * @param  {[type]}  data    [description]
 * @param  {[type]}  type    [ajax类型]
 * @param  {Boolean} isParam [是否需要参数data]
 * @return {[type]}          [description]
 */
function ajaxBackData(url, data, type, isParam){
	isParam = isParam == undefined ? true : false
	if(!isParam){
		data = ""
	}
	console.log("请求参数")
	console.log(data)
	var backData
	$.ajax({
		url: url,
		data : data,
		type : type,
		async : false,
		dataType : "json",
		success: function (data) {
		  backData = data;	   
		},
		error : function(error){
			console.info(error);
		}
	});
	return backData
}

function indexOf(arr, value) {
	if(typeof arr == "object"){
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] == value) {
				return true;
			}
		}
		return false;
	}
	if(typeof arr == "string"){
		var reg = new RegExp(value);
		var result = reg.test(arr);
		return result;
	}
	
}

function uploadAttachment(url, obj, id, callback){
	var allow = types = ["xlsx", "xls"];
	var file = $(obj).val();
	var fileExt = file.substring(file.lastIndexOf('.') + 1);
	if (indexOf(allow, fileExt) === false) {
		layer.msg('请上传Excell');
		return;
	}
	var name = obj.files[0].name
	var arr = {}
	arr.file = name
	console.log(arr)
	$.ajaxFileUpload({
		url : url,
		data: arr,
		secureuri : false,
		async: false,
		fileElementId : id,
		type: 'post',
		dataType : 'text',
		success : function(data) {
			callback(data)
		},
		error : function(error) {
			callback(error)
		}
	});
}