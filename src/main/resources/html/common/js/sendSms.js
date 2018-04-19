/**
 * SMS
 */
var alldataIds = null;//定义全局接受的val
function smsPlatform(boxname) {
	var dataIds = [];
	if ($('input[name="'+boxname+'"]:checked').length > 0) {
		$('input[name="'+boxname+'"]:checked').each(function(key, value) {
			dataIds.push($(value).val());
		});
	} else {
		layer.alert("请选择要操作的记录");
		return false;
	}
	console.info(dataIds);
	alldataIds = dataIds;
	loadSmsPanel("", dataIds.toString());
}

function sendSingleSms(dataId) {
	console.info()
	var dataIds = [];
	dataIds.push(dataId);
	loadSmsPanel("", dataIds.toString());
}

function loadSmsPanel(taskId, dataIds){
	$("#div_smsPlatform").empty();
	var contextPath = $('#contextPath').val();
	var exhibitionId = $('#exhibitionId').val();
	var arr = getParamsData();
	var child = {};
	child.ids = dataIds;
	child.module = $('#module').val();
	child.taskId = "";
	arr.jsonData = child;
	arr= JSON.stringify(arr);
	console.info(arr);
	var url = tokenParam()+"/getTaskList.action";
	$.ajax({
	    type : 'post',
	    contentType: "application/json",
		dataType : 'json',
	    url:url,
	    data : arr,
	    async: false,
	    success:function(data){
	    	console.info(data);
	    	var success = data['success'];
	    	if(success){
	    		var smslist = data['data'];
	    		if(smslist.length != 0){
	    			var ifCustomSms = false;
	    			var str = "<form action=\"\" method=\"post\" id=\"sendSmsForm\" >";
	    			str += "<table class=\"table\" id=\"table_smsPlatform\">";
	    			str += "<tr height=\"50px;\">";
		    		str += "<td width=\"100px\" align=\"right\">";
		    		str += "<input type=\"hidden\" name=\"dataIds\" value=\""+dataIds+"\" />";
		    		str += "选择短信任务&nbsp;</td>";
		    		str += "<td width=\"320px\"><select id=\"sms_tasklist\" name=\"taskId\" onchange=\"changeTask(this,'"+dataIds+"')\">";
		    		for (var i = 0; i < smslist.length; i++) {
		    			str += "<option " ;
		    			if(taskId == smslist[i]['id'] || taskId == ''){
		    				ifCustomSms = smslist[i]['ifSystemVal'];
		    				str += "selected=\"selected\"";
		    			}
		    			str += " value=\""+smslist[i]['id']+"\">"+smslist[i]['name']+"</option>";
					}
		    		str += "</select></td>";
		    		str += "</tr>";
		    		var task = data['task'];
		    		console.info(ifCustomSms);
		    		if(ifCustomSms){
		    			str += "<tr>";
			    		str += "<td align=\"center\">&nbsp;请输入<br>手写短信内容&nbsp;</td>";
			    		str += '<td><textarea id="smsContent" cols="40" rows="8" name="smsContent"></textarea></td>';
			    		str += "</tr>";
		    		}else{
		    			str += "<tr height=\"50px;\">";
			    		str += "<td align=\"right\">模&nbsp;板&nbsp;</td>";
			    		str += "<td><div style=\"border:1px solid #C7C7CB;\">"+task['smsContent']+"</div></td>";
			    		str += "</tr>";
			    		str += "<tr height=\"50px;\">";
			    		str += "<td align=\"right\">预&nbsp;览&nbsp;</td>";
			    		str += "<td><div style=\"border:1px solid #C7C7CB;margin-top:10px;\">"+task['smsMessage']+"</div></td>";
			    		str += "</tr>";
		    		}
		    		str += "</table></form>";
		    		$("#div_smsPlatform").append(str);
		    		if(taskId == ""){
		    			layer.open({
		    				type: 1,
		    				title:'发送短信',
		    				area: ['650px', '400px'],
		    				content: $('.wcodelayer_content')
		    			});
		    		}
	    		}else{
	    			layer.alert("短信平台无可用模板");
	    		}
	    	}else{
	    		layer.alert(data['msg']);
	    		return;
	    	}
	    }
	});
}

function sendSmsMessages(){
	$("#sendSmsMessagesBtn").prop("disabled", true);
	var contextPath = $('#contextPath').val();
	var exhibitionId = $('#exhibitionId').val();
	var arr = getParamsData();
	var child = {};
	alldataIds = alldataIds.join();
	child.ids = alldataIds;
	child.module = $('#module').val();
	if($('#smsContent').length<=0){
		child.smsContent = '';
	}else{
		child.smsContent = $('#smsContent').val();
	}
	child.taskId = $('#sms_tasklist option:selected').val();
	arr.jsonData = child;
	arr= JSON.stringify(arr);
	var url = tokenParam()+"/sendSmsMessages.action";
	$.ajax({
		url:url,
		data:arr,
		contentType: "application/json",
		dataType:'json',
		type:'post',
		success:function(data){
			console.info(data);
			$("#sendSmsMessagesBtn").prop("disabled",false);
			if(data.success){
				layer.msg('发送成功',{time:1300},function(){layer.closeAll()});
			}else{
				layer.msg('发送失败',{time:1300});
			}
		}
	});
}

function changeTask(obj, ids){
	var taskId = $(obj).val();
	loadSmsPanel(taskId, ids);
}
