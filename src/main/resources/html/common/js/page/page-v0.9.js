/**使用方法：
 * 1、页面初始化时需初始化分页插件，默认每页显示10条，当前页为1，总记录数为0，页码显示10个。
 * $("#xxx").Page({"callback":callBackFun});
 * 2、查询数据时,获取当前页、每页显示条数
 * var pagePar = $("#xxx").getPageParameter();var pageNow = pagePar.pageNow;var pageSize = pagePar.pageSize;
 * 3、数据返回时,重置记录总数和当前页，当前页可不传
 * $("#xxx").resetPageParameter(count);
 * 
 */
(function($) {
	$.addPage = function(t, p) {
		if (t.grid)
			/**
			 * 如果Grid已经存在则返回
			 */
			return false;
		/**
		 * 初始化数据
		 * pageNow：当前第几页
		 * pageSize：每页显示多少条
		 * count：总记录数
		 * pageNum：每一页显示的页码数	
		 * callback：点击上一页、下一页和页码数时回调的函数
		 */
		p = $.extend({
			pageNow : 1, 
			pageSize : 10, 
			count : 0, 
			pageNum : 10, 
			pages:0,
			callback:function(){return false;},
			obj:''
		}, p);
		var g = {
				/**
				 * 渲染页面
				 * @param obj obj对象包含两个参数，pageStart：页码条开始数，pageEnd：页码条结束数
				 */
			buildHtml : function(obj) {
				var pageCount = g.getTotalPage();
				var pageStart = obj["pageStart"];
				var pageEnd = obj["pageEnd"];
				var pages = p.pages;
				var htmlarr = [];
				htmlarr.push("<ul class=\"progress_ul\">");
				htmlarr.push("<li><span class=\"jdlang\" jdlang=\"总计\">总计</span> "+p.count+" <span class=\"jdlang\" jdlang=\"条\">条</span></li>");
				htmlarr.push("<li><span class=\"jdlang\" jdlang=\"共\">共</span> "+pages+" <span class=\"jdlang\" jdlang=\"页\">页</span></li>");
				htmlarr.push("<select name='selectSize'>");
				if(p.pageSize==5){htmlarr.push("<option selected='selected' value ='5'>5</option>");}else{htmlarr.push("<option  value ='5'>5</option>");}
				if(p.pageSize==10){htmlarr.push("<option selected='selected' value ='10'>10</option>");}else{htmlarr.push("<option  value ='10'>10</option>");}
				if(p.pageSize==20){htmlarr.push("<option selected='selected' value ='20'>20</option>");}else{htmlarr.push("<option  value ='20'>20</option>");}
				if(p.pageSize==50){htmlarr.push("<option selected='selected' value ='50'>50</option>");}else{htmlarr.push("<option  value ='50'>50</option>");}
				if(p.pageSize==100){htmlarr.push("<option selected='selected' value ='100'>100</option>");}else{htmlarr.push("<option  value ='100'>100</option>");}
				if(p.pageSize==200){htmlarr.push("<option selected='selected' value ='200'>200</option>");}else{htmlarr.push("<option  value ='200'>200</option>");}
				if(p.pageSize==500){htmlarr.push("<option selected='selected' value ='500'>500</option>");}else{htmlarr.push("<option  value ='500'>500</option>");}
				if(p.pageSize==1000){htmlarr.push("<option selected='selected' value ='1000'>1000</option>");}else{htmlarr.push("<option  value ='1000'>1000</option>");}
				htmlarr.push("</select>");
				/**
				 * 如果结束页码不等于1，显示上一页按钮
				 */
				console.log("pageEnd="+pageEnd+";pageCount="+pageCount+";pages="+pages);
				if(pageEnd != 1){
					htmlarr.push("<a   name=\"backpage\"  class=\"prev_btn jdlang\" jdlang=\"上一页\">上一页</a>");
				}			
				/**
				 * 从页码开始也循环显示页码条
				 */
				for ( var i = pageStart; i <= pageEnd; i++) {
					/**
					 * 如果页码条等于当前页，样式为选中状态
					 */
					if (i == p.pageNow) {
						htmlarr.push("<li><a style=\"cursor:pointer\" class=\"active\">" + i + "</a></li>");
					} else {
						htmlarr.push("<li><a style=\"cursor:pointer\">" + i + "</a></li>");
					}
				}
				/**
				 * 如果页码结束数小于总页数，显示......
				 */
				if (pageEnd < pageCount) {
					htmlarr.push("<li>......</li>");
				}
				/**
				 * 如果结束页码不等于1，显示下一页按钮
				 */
				if(pageEnd != 1){
					htmlarr.push("<a style=\"cursor:pointer\" name=\"nextpage\"  class=\"next_btn jdlang\" jdlang=\"下一页\">下一页</a>");
				}			
				htmlarr.push("</ul>");
				/**
				 * 渲染进页面
				 */
				$(t).html(htmlarr.join(""));
			},
			/**
			 * 获取总页数
			 * @returns 总页数
			 */
			getTotalPage : function(){
				return Math.ceil(p.count / p.pageSize);
			},
			/**
			 * 移除所有页码数选中样式
			 * 为当前页设置选中样式
			 * 初始化操作
			 * 回调
			 */
			clickProcess : function(){
				/**
				 * 移除所有页码数选中状态，并为当前页设置选中样式
				 */
				$(".page li a").each(function(){
					 $(this).removeClass("active");
					 if(parseInt($(this).text()) == p.pageNow){
						  $(this).addClass("active");
					  }
				});
				/**
				 * 初始化操作
				 */
				g.init();
				/**
				 * 回调
				 */
				console.log("p.pageNow="+p.pageNow+"|p.pageSize="+p.pageSize);
				p.callback(p.pageNow, p.pageSize,this);
				
			},
			/**
			 * 点击页码数触发此方法
			 * @param t 页码对象
			 */
			clickATag:function(t){
				console.log("p.pageNow="+p.pageNow);
				/**
				 * 设置当前页尾选中页
				 */
				p.pageNow = parseInt(t.text());
				g.clickProcess();
				
			},
			/**
			 * 点击上一页触发此方法
			 * @returns {Boolean}
			 */
			backpageprocess : function() {			  
				if (p.pageNow == 1) return false;				
				p.pageNow--;
				g.clickProcess();
			},
			/**
			 * 点击下一页触发此方法
			 * @returns {Boolean}
			 */
			nextpageprocess : function() {
				var pageCount = g.getTotalPage();
				if (p.pageNow + 1 > pageCount) return false;	
				p.pageNow++;		
				g.clickProcess();

			},
			/**
			 * 计算页码开始数和结束数
			 * 渲染页面
			 * 绑定上一页、下一页、页码数点击事件
			 */
			init : function(){
				var obj = g.getStartAndEndPage();
				g.buildHtml(obj);
				$("html,body").animate({
					scrollTop : 0
				}, 0);				
				$(t).find("a[name='backpage']").click(function() {g.backpageprocess();});
				$(t).find("a[name='nextpage']").click(function() {g.nextpageprocess();});
				$(t).find("select[name='selectSize']").change(function() {g.setPageSize();});
				$(t).find("li a").each(function(){
					$(this).click(function() {g.clickATag($(this));});		
				});
			},
			/**
			 * 计算页码开始数和结束数
			 * @returns {___obj0}
			 */
			getStartAndEndPage : function() {
				// 总页数
				var pageCount = Math.ceil(p.count / p.pageSize);
				var pageStart;
				var pageEnd;
				if (p.pageNow <= (p.pageNum / 2 + 1)) {
					pageStart = 1;
					pageEnd = p.pageNum;
				} else if (p.pageNow > (p.pageNum / 2 + 1)) {
					pageStart = p.pageNow - (p.pageNum / 2);
					pageEnd = (p.pageNow + (p.pageNum / 2) - 1);					
				}
				if (pageEnd > pageCount) {
					pageEnd = pageCount == 0 ? 1 : pageCount;
				}
				// 当不足pageNum数目时，要全部显示，所以pageStart要始终置为1
				if(pageEnd == pageCount){
					pageStart = pageCount - p.pageNum  + 1;
				}
				if (pageEnd <= p.pageNum) {
					pageStart = 1;
				}
				if(pageEnd < p.pageNow){
					pageEnd = p.pageNow;
				}
				//alert("pageCount:"+pageCount+",pageNow:"+p.pageNow+","+"pageStart:"+pageStart+",pageEnd:"+pageEnd);
				var obj = new Object();
				obj['pageStart'] = pageStart;
				obj['pageEnd'] = pageEnd;
				return obj;
			},
			/**
			 * 重置总数和当前页，重新渲染页面
			 * @param count
			 * @param pageNow
			 */
			resetPageParameter : function(count,pageNow,pages){
				if(count){
					p.count = count;
				}
				if(pageNow){
					p.pageNow = pageNow;
				}			
				if(pages){
					p.pages = pages;
				}			
				g.init();
			},
			/**
			 * 设置页面显示多少条数据
			 */
			setPageSize :function(){
				p.pageSize=$(t).find("select[name='selectSize']").val();
				p.pageNow=1;
				g.clickProcess();
			}

		};
		/**
		 * 初始化分页
		 */
		g.init();
		t.p = p;
		t.grid = g;
		return t;

	};
	$.fn.Page = function(p) {
		return this.each(function() {
			var t = this;
			$.addPage(t, p);
		});

	};
	$.fn.getPageParameter = function() {
			return this[0].p;
	}
	$.fn.resetPageParameter = function(count,pageNow,pages) {
		if(count == 0 && pages == 0){
			$(this).html("");
			return false;
		}else{
			return this.each(function() {
				this.grid.resetPageParameter(count,pageNow,pages);
			});
		}
		

	}

})(jQuery);