var tabMinxin = {
	extend : function(dest) {
		for (var key in this) {
      if (!dest[key]) {
        dest[key] = this[key]
      }
    }
    return dest
	},
	toggle : function(target, tab) {
		var child = target[0].children
		var box = tab[0].children
		for(var i=0; i<child.length; i++){
			(function (arg) {
				child[arg].onclick = function(){
					for(var i=0; i<child.length; i++){
						child[i].removeAttribute('class')
						box[i].style.display = 'none'
					}
					this.setAttribute('class',"active")
					box[arg].style.display = 'table'
				}
			})(i)
			
		}
	},
	render : function(data) {
		console.log(data);
		/**
		 * [str description]
		 * @type {String}
		 */
		var select = "",
				tabNav = "",
				table = ""
		tabNav += '<ul class="table-tab-ul">'		
		table += '<div class="tabbox">'

		for(key in data.list){
			var tab = data.list[key];
			var cls = key == "0" ? "active" : ""
			var sty = key == "0" ? "display:table" : "display:none"
			tabNav += '<li class="'+cls+'">'+tab.title+'</li>'	
			table += '<table cellspacing="0" cellpadding="0" class="tab-table" style="'+sty+'">'
      table += '<tbody>'
        table += '<tr>'
          table += '<th width="50%">单点登录系统字段</th>'
          table += '<th width="50%">问卷字段</th>'
        table += '</tr>'
      table += '</tbody>'
      table += '<tbody>'
			var ret = tab.attributes
			for(pn in ret){		
          table += '<tr><td name="'+pn+'">'+ret[pn]+'<span class="must"></span></td><td>'+this.renderSelect(data.quest.attributes, tab.mapping, pn)+'</td></tr>' 
			}
			table += '</tbody>'
			table += '</table>'
		}
		table += '</div>'
		tabNav += '</ul>'

		var ret = tabNav + table
		return ret
	},

	getRequire : function(state) {
		return state ? "*" : ""
	},

	renderSelect : function(data, mapping, item) {
		var html = ""
		var disable = mapping ? "disabled=disabled" : ""
		html += '<select '+disable+'><option value="">请选择</option>'
		for(key in data){
			if(key !== 'id'){
				//查看页面
				if(mapping){
					if(key === mapping[item]){
						html += '<option name="'+key+'" selected="selected">'+data[key]+'</option>'	
					}else{
						html += '<option name="'+key+'">'+data[key]+'</option>'	
					}
				}else{
					html += '<option name="'+key+'">'+data[key]+'</option>'
				}
				
			}
		}
		html += "</select>"
		return html
	}

}