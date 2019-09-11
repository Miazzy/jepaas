var APP = APP || {};
var type = document.documentElement.getAttribute('data-type') || '';
//头部样式
if(APP.info){
	JE.buildHeaderCfg(APP.info);
}else if(type == 'chart'){//网页端的预览
	//解析功能编码
	var params = window.location.href.split('?')[1].split("&");
	var appCode = '';
	for(var i=0;i<params.length;i++){
		var param = params[i].split('=');
		if(param[0] == 'app'){
			appCode = param[1];
		}
	}
	//获得图表的数据
	JE.ajax({
		url:JE.getUrlMaps("je.core.load"),
		params:{
			tableCode:'JE_PHONE_APP',
			whereSql:"and APP_CODE ='"+appCode+"'",
			limit:-1
		},
		callback:function(success,obj){
			var app = obj.rows[0];
			if(!app)return;
			JE.buildHeaderCfg(app);
		}
	});
}
