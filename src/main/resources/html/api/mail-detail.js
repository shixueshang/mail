(function(){
	/**
	 * [存储全局对象]
	 * @type {Object}
	 */
	var delay = {time: 1500}

	var sendDetailModle = {
		dom : function() {

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
		var link = tokenParam("template") + "/overview/"+getUrlParam("id")
		var result = ajaxBackData(link, null, "GET", false)
		if(result.code === ERR_OK()){
			var list = result.map.templateTotalMap
			var html = ""
			for(key in list){
				$('.describe_task_ul').find('p[name='+key+']').html(list[key])
			}
			var str = ""
			for(pn in result.map.mails){
				var item = result.map.mails[pn]
				str += '<tr>'
				str += '<td>'+formatDate(item.send_time)+'</td>'
				str += '<td>'+item.subject+'</td>'
				str += '<td>'+item.total+'</td>'
				str += '<td><a href="send_detail.html?id='+item.mail_id+'">'+item.not_submit+'</a></td>'
				str += '<td>'+item.not_submit_percent+'</td>'
				str += '<td><a href="send_detail.html?id='+item.mail_id+'">'+item.submit_success+'</a></td>'
				str += '<td>'+item.submit_success_percent+'</td>'
				str += '<td><a href="send_detail.html?id='+item.mail_id+'">'+item.submit_failure+'</a></td>'
				str += '<td>'+item.submit_failure_percent+'</td>'
				str += '<td><a href="send_detail.html?id='+item.mail_id+'">'+item.request+'</td></a>'
				str += '<td><a href="send_detail.html?id='+item.mail_id+'">'+item.deliver+'</td></a>'
				str += '<td><a href="send_detail.html?id='+item.mail_id+'">'+item.invalid_address+'</a></td>'
				str += '<td>'+item.open_total+'</td>'
				str += '<td>'+item.unique_open+'</td>'
				str += '</tr>'
			}

			$('#tabDataLict').html(str)
			hightChartsInit(result)
		}
	}

	function chartDatas(data) {
		var chart = {}
		var dateTime = []
		var param1 = []
		var param2 = []
		var param3 = []
		var param4 = []
		var param5 = []
		var param6 = []
		var param7 = []
		for(key in data.chartData){
			var list = data.chartData[key]
			dateTime.push(list.send_date)
			param1.push(list.total)
			param2.push(list.submit_success)
			param3.push(list.not_submit)
			param4.push(list.submit_failure)
			param5.push(list.invalid_address)
			param6.push(list.request)
			param7.push(list.deliver)
		}
		var ret = [
			{
        name: '邮件总数',
        data: param1
	    }, {
        name: '提交成功数',
        data: param2
	    }, {
        name: '未提交数',
        data: param3
	    }, {
        name: '提交失败',
        data: param4
	    }, {
        name: '无效邮址',
        data: param5
	    },
	    {
        name: '寄出',
        data: param6
	    },
	    {
        name: '送达',
        data: param7
	    }
		]
		chart.data = ret
		chart.time = dateTime
		return chart
	}

	function hightChartsInit(option, dateTime) {
		var params = chartDatas(option.map)
		Highcharts.chart('chartsbox', {
		  title: {
        text: '邮件统计分析'
      },
      yAxis: {
				title: {
          text: '数量'
        }
      },
      xAxis: {
        categories: params.time
      },
      series: params.data
		});
	}

})()



