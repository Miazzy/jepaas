function(record){ 
	var _res = JE.ajax({
		url : JE.getUrlMaps("je.core.load"),
		params:{
			tableCode : "JE_OA_WPLYMX",
			whereSql : " and JE_OA_WPLY_ID = '"+record.get("JE_OA_WPLY_ID")+"'",
			orderSql : "",
			queryFields : "",
			limit : -1
		}
	});
	var res = _res && _res.rows ?  _res.rows : JE.error("数据获取失败！");
    // var res = JE.DB.selectList("JE_OA_WPLYMX"," and JE_OA_WPLY_ID = '"+record.get("JE_OA_WPLY_ID")+"'");
	var tpl = new Ext.XTemplate(
    	'<table width="100%" border="1" style="text-align:center;border-collapse:collapse;border-spacing:0;"><tbody>' +
    	'<tr height="25" style="background:#9ADFFF;font-weight:bold;">'
				+'<td width="200">物品名称</td>'
				+'<td width="200">规格型号</td>'
				+'<td width="40">现存数量</td>'
				+'<td width="100">预计领用数量</td>'
				+'<td width="40">单位</td>'
				+'<td width="100">实际领用数量</td>'
				+'<td>领用时间</td>'+	
		    '</tr><tpl for=".">',
		        '<tr>' ,
		        '<td>{WPLYMX_WPMC}</td>'
				+'<td>{WPLYMX_GGXH}</td>'
				+'<td>{WPLYMX_SL}</td>'
				+'<td>{WPLYMX_YJSL}</td>'
				+'<td>{WPLYMX_DWZL_NAME}</td>'
				+'<td>{WPLYMX_SJLYSL}</td>'
				+'<td>{WPLYMX_LYSJ}</td>',
		        '</tr>',
		    '</tpl>' ,
		'</tbody></table>'
	);
	var html = tpl.apply(res);
    return html;
}