var QRCODE = {
	getUrlParams : function(){
		var url = window.location.href;
		//解析功能编码
		var params = url.split('?')[1].split("&");
		var obj = {};
		for(var i=0;i<params.length;i++){
			var param = params[i].split('=');
			obj[param[0]] = param[1];
		}
		return obj;
	},
	toText:function(obj){
		if(obj.type == 'text'){
			return obj.text;
		}else{
			return JE.decodeURI(obj.html);
		}
	},
	downloadUrl:JE.getMethodUrl('je.core.doc.documentation', 'download') + '?fileName={name}&path={url}',
	buildWZ:function(el,cfg){
		el.update(QRCODE.toText(cfg));
	},
	buildWWW:function(el,cfg){
		if(JE.isNotEmpty(cfg.url)){
			window.location.href = cfg.url;
			return;
		}
		var urls = Ext.decode(Ext.value(cfg.urls,'[]'));
		var tpl = [
			'<ul class="mui-table-view mui-card qrcode-url">',
				'<tpl for=".">',
					'<li class="mui-table-view-cell">',
						'<a href="{value}" class="mui-navigate-right" target="_blank">{text}</a>',
					'</li>',
				'</tpl>',
			'</ul>'
		].join('');
		el.update(JE.toXT(tpl,urls));
	},
	buildFile:function(el,cfg){
		var file = Ext.decode(cfg.file);
		var innerTpl = '';
		if(cfg.type == 'TP'){
			innerTpl = '<img src="'+file.url+'" />';
		}else{
			innerTpl = '<i class="fa fa-file-text-o"></i>'+
						'<div class="qrcode-file-name">'+file.name+'</div>'+
						'<p class="qrcode-file-size">'+file.size+'</p>';
		}
		var tpl = [
			'<div class="qrcode-file">',
				innerTpl,
			'</div>'
		].join('');
		el.update(tpl);
		if(cfg.type == 'WJ'){
			var url = JE.toXT(QRCODE.downloadUrl,file);
			var footer = '<footer class="mui-bar mui-bar-footer">'+
					'<a class="mui-btn mui-btn-green mui-btn-block " target="_blank" href="'+url+'">下载</a>'+
				'</footer>';
			el.insertHtml('beforeBegin',footer);
		}
	},
	buildImage:function(el,cfg){
		var tpl = [];
		
		var title = cfg.title.text;
		if(cfg.title.type == 'html'){
			title = JE.decodeURI(cfg.title.html);
		}
		if(JE.isNotEmpty(title)){
			tpl.push('<div style="padding:10px;">'+title+'</div>');
		}
		var files = cfg.images;
		if(JE.isNotEmpty(files)){
			files = Ext.isString(files)?Ext.decode(files):files;
			files = [files[files.length-1]].concat(files).concat(files[0]);
			files[0].itemCls = 'mui-slider-item-duplicate';
			var fileTpl = JE.toXT([
				'<div class="mui-slider" style="height:240px;">',
					'<div class="mui-slider-group mui-slider-loop">',
						'<tpl for="imgs">',
							'<div class="mui-slider-item {itemCls}">',
								'<img src="{path}" style="height:240px;width:100%;">',
							'</div>',
						'</tpl>',
					'</div>',
					'<div class="mui-slider-indicator">',
						'{% for(var i=0;i<values.imgCount;i++) { var cls = "";if(i==0){cls="mui-active";}       %}',
							'<div class="mui-indicator {[cls]}"></div>',
						'{% } %}',
					'</div>',
				'</div>'
			].join(''),{imgs:files,imgCount:files.length-2});
			tpl.push(fileTpl);
		}
		el.update(tpl.join(''));
		mui(".mui-slider").slider({interval: 5000});
	},
	cardDefTpl:[
			"<div class='mui-input-group'>",
				'<tpl for="data">',
					'<tpl if="JE.isNotEmpty(value)">',
						"<div class='mui-input-row'>",
							"<label>{fieldLabel}</label>",
							"<tpl if='name==\"telphone\" || name==\"phone\"'>",
								"<a class='value' href='tel:{value}'>{value}</a>",
							"<tpl elseif='name==\"email\"'>",
								"<a class='value' href='mailto:{value}'>{value}</a>",
							"<tpl elseif='name==\"website\"'>",
								"<a class='value' href='{value}'>{value}</a>",
							"<tpl else>",
								"<div class='value'>{value}</div>",
							"</tpl>",
						"</div>",
					'</tpl>',
				'</tpl>',
			"</div>"
		].join(''),
	buildMP:function(el,cfg){
		var defTpl = QRCODE.cardDefTpl;
		var headerTpl = [
			'<div class="vcard_head" style="<tpl if="bgType==\'bgColor\'">background-color:{bgColor};<tpl else>background-image: url({bgImg});</tpl>">',
				'<div class="vcard_face_wrap">',
					'<div class="vcard_face_bg"></div>',
					'<tpl if="JE.isNotEmpty(headImg)">',
						'<img class="vcard_face" src="{headImg}"></img>',
					'<tpl else>',
						'<div class="vcard_face_lang lang_fake lang" data-lang-key="Avatar">头像</div>',
					'</tpl>',
				'</div>',
				'<div class="vcard_career_wrap">',
					'<p data-lang-key="Name" class="vcard_data_fullname lang_fake">{name}</p>',
					'<p class="vcard_data_appointment vcard_career lang_fake" data-lang-key="Position">{job}</p>',
					'<p class="vcard_data_company vcard_career lang_fake" data-lang-key="Company">{company}</p>',
				'</div>',
			'</div>'
		].join('');
		var tpl = [];
		Ext.each(cfg.info,function(item){
			if(item.type == 'header'){
				tpl.push(JE.toXT(headerTpl,item.data));
				if(JE.isNotEmpty(item.data.depts)){
					tpl.push(JE.toXT(defTpl,{data:item.data.depts}));
				}
			}else{
				tpl.push(JE.toXT(defTpl,item));
			}
		});
		
		el.update(tpl.join(''));
//		var footer = '<footer class="mui-bar mui-bar-footer">'+
//				'<a class="mui-btn mui-btn-green mui-btn-block ">保存到通讯录</a>'+
//			'</footer>';
//		el.insertHtml('beforeBegin',footer);
		delete cfg.remark;
	},
	buildAPP:function(el,cfg){
		var tpl = [
			'<div class="qrcode-app">',
				'<img src="'+cfg.icon+'" class="qrcode-app-icon">',
				'<div class="qrcode-app-name">'+cfg.name+'</div>'+
			'</div>'
		].join('');
		el.update(tpl);
		var footer = '<footer class="mui-bar mui-bar-footer">'+
				'<button class="mui-btn mui-icon fa fa-apple mui-btn-green mui-btn-block" href="'+cfg.ios+'"></button>'+
				'<button class="mui-btn mui-icon fa fa-android mui-btn-blue mui-btn-block" href="'+cfg.android+'"></button>'+
			'</footer>';
		el.insertHtml('beforeBegin',footer);
		mui('footer').on('click','button',function(){
			window.open(this.getAttribute('href'));
		});
	},
	buildQYXC:function(el,cfg){
		var headerTpl = [
			'<div class="company_head">',
				'<div class="imgdiv">',
					'<div class="bgimg" style="<tpl if="JE.isNotEmpty(bgImg)">background-image:url({bgImg});</tpl>"></div>',
					'<div class="triangle-white"></div>',
					'<div class="title_wrap">' ,
						'<div class="logo" style="<tpl if="JE.isNotEmpty(headImg)">background-image:url({headImg});</tpl>">' +
							'<tpl if="JE.isEmpty(headImg)"><div class="logo_bg">LOGO</div></tpl>',
						'</div>',
						'<div class="title">',
							'<div>{name}</div>',
						'</div>',
					'</div>',
				'</div>',
				'<div class="head_desc">{desc}</div>',
			'</div>'
		].join('');
		var imageTpl = [
			'<div class="mui-card image-card">',
				'<div class="mui-card-header">{title}</div>',
				'<div class="mui-card-content">',
					'<tpl if="JE.isNotEmpty(image)">',
						'<img src="{image}" style="max-width: 100%;"/>',
					'</tpl>',
					'<div class="mui-card-content-inner">{[Ext.htmlDecode(values.content)]}</div>',
					'<div class="mui-table-view">',
						'<tpl for="files">',
							'<div class="mui-table-view-cell"><a class="mui-navigate-right" href="{path}">{name}</a></div>',
						'</tpl>',
					'</div>',
				'</div>',
			'</div>'
			].join('');
		var defTpl = QRCODE.cardDefTpl;
		
		var tpl = [];
		Ext.each(cfg.info,function(item){
			if(item.type == 'header'){
				tpl.push(JE.toXT(headerTpl,item.data));
			}else if(item.type == 'phone'){
				tpl.push(JE.toXT(defTpl,item));
			}else{
				item.data.files = Ext.decode(Ext.value(item.data.files,'[]'));
				JE.log(item.data.files);
				tpl.push(JE.toXT(imageTpl,item.data));
			}
		});
		
		el.update(tpl.join(''));
		delete cfg.remark;
	},
	buildRemark:function(el,cfg){
		if(cfg.remark){
			var remark = cfg.remark.text;
			if(cfg.remark.type == 'html'){
				remark = JE.decodeURI(cfg.remark.html);
			}
			if(JE.isNotEmpty(remark)){
				var tpl = '<div class="qrcode-remark">'+remark+'</div>';
				el.insertHtml('beforeEnd',tpl);
			}
		}
	},
	buildBGSound:function(el,cfg){
		if(cfg.bgsound){
			var tpl = '<audio id="bg-sound" src="'+cfg.bgsound+'" autoplay="autoplay" loop="true" hidden="true"></audio>';
			el.insertHtml('beforeEnd',tpl);
			var play = document.createElement('div');
			play.setAttribute('class','play-sound');
			play = Ext.getBody().appendChild(play);
			play.on('click',function(){
				var bgSound = document.getElementById('bg-sound');
				if(bgSound.paused){                 
					bgSound.play();// 这个就是播放  
					play.removeCls('play');
				}else{
					bgSound.pause();// 这个就是暂停
					play.addCls('play');
				}
			});
		}
	},
	buildAddInfo:function(el,cfg){
		var info = Ext.value(cfg.addInfo,{});
		if(info.enableAdd != '1'){
			return;
		}
		var tpl = '<div style="padding:10px;text-align:right;"><button id="_qrcode_addinfo" class="mui-btn mui-btn-green">添加记录</button></div>';
		el.insertHtml('beforeEnd',tpl);
		var types = JE.decode(info.type);
		//打开菜单
		var openMenuUrl = function(index){
			var id = cfg.pkValue,//二维码主键
				username = (window.USER||{}).userCode,//用户名
				system = browser.getSYS(),//操作系统
				login = info.enablePwd == '1'?(info.login=='1'?'2':'1'):'0';//0不绑定密码，1绑定密码，2绑定系统用户
			var params = 'id='+id+'&username='+username+'&system='+browser.getSYS()+'&login='+login;
			var url = types[index-1].url;
			url += (url.split('?').length>1?'&':'?')+params;
			window.open(url);
		};
		JE.on('_qrcode_addinfo','tap',function(){
			if(info.enablePwd == '1'){
				if(info.login == '1'){
					JE.win({
						title:'密码验证',
						items:[{
							name:'j_username',
							label:'用户名'
						},{
							name:'j_password',
							inputType:'password',
							label:'密码'
						}],
						callback:function(obj){
							if(JE.isEmpty(obj.j_username)){
								JE.alert('请输入用户名！');
								return false;
							}
							if(JE.isEmpty(obj.j_password)){
								JE.alert('请输入密码！');
								return false;
							}
							//登录
							var info = JE.ajax({url:'/j_spring_security_check',params:obj,async:false});
							if(info.success){
								window.USER = JE.ajax({url:JE.getMethodUrl('je.core.rbac.user', 'getCurrentUserInfo'),async:false});
								//权限处理
								var dept = _info.SY_CREATEORG;//数据创建部门
								//用户部门和监管部门
								if(dept == USER.deptCode || USER.monitorDeptCode.indexOf(dept) != -1){
									//打开菜单
			          				JE.actionSheet('',function(e){
			          					openMenuUrl(e.index);
			          				},types);
								}else{
									JE.alert('您没有添加的权限！');
									return false;
								}
							}else{
								JE.alert('用户名或密码错误！');
								return false;
							}
						}
					});
				}else{
					mui.prompt('','','密码验证',['取消','验证密码'],function(e){
		          		var index = e.index;//按钮的索引
		          		var text = e.value;//'输入的文字'
		          		if(index == 1){
		          			if(e.value == info.pwd){
		          				JE.actionSheet('',function(e){
		          					openMenuUrl(e.index);
		          				},types);
		          			}else{
		          				JE.msg(JE.isEmpty(e.value)?'密码不能为空':'密码错误！');
		          				return false;
		          			}
		          		}
					});
					JE.select('.mui-popup-input input').setAttribute('type','password');
				}
				return;
			}
			
			JE.actionSheet('',function(e){
		          openMenuUrl(e.index);
			},types);
		});
	},
	buildCountInfo:function(el){
		var num = JE.toNum(_info.MK_LJSMCS);
		var zan = JE.toNum(_info.MK_DZS);
		var tpl = '<p class="qrcode-countinfo">' +
					'<span>阅读<span>'+num+'</span></span>' +
					'<span id="_qrcode_zan"><i class="fa fa-thumbs-o-up"></i><span id="_arcode_zan_num">'+zan+'</span></span>' +
				'</p>';
		el.insertHtml('beforeEnd',tpl);
		JE.on('_qrcode_zan','tap',function(){
			if(window._qrcode_zaned){
				JE.msg('不可频繁点赞！');
				return;
			}
			window._qrcode_zaned = true;
			setTimeout(function(){
				window._qrcode_zaned = false;
			},1000*60*60);
			var dom = JE.get('_arcode_zan_num'),
				zNum = JE.toNum(dom.innerText)+1;
			dom.innerHTML = zNum;
			JE.msg('+1');
			JE.ajax({
				url:JE.getMethodUrl('je.core.mk', 'doUpdate'),
				params:{
					tableCode:'JE_SYS_MK',
					async:false,
					JE_SYS_MK_ID:_info.JE_SYS_MK_ID,
					MK_DZS:zNum
				}
			});
		});
	},
	previewImage:function(el){
		var imgs = el.query('img');
		if(JE.isNotEmpty(imgs)){
			Ext.each(imgs,function(img){
				img.setAttribute('data-preview-src','');
				img.setAttribute('data-preview-group','preview');
			});
			mui.previewImage();
		}
	
	}
	
}//判断访问终端
var browser={
    versions:function(){
        var u = navigator.userAgent, app = navigator.appVersion;
        return {
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1,//火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1, //android终端
            iPhone: u.indexOf('iPhone') > -1 , //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1, //是否web应该程序，没有头部与底部
            weixin: u.indexOf('MicroMessenger') > -1, //是否微信 （2015-01-22新增）
            qq: u.match(/\sQQ/i) == " qq" //是否QQ
        };
    }(),
    language:(navigator.browserLanguage || navigator.language).toLowerCase(),
    getSYS:function(){
    	var vs = browser.versions; 
    	if(vs.ios || vs.iPhone || vs.iPad){
    		return 'IOS';
    	}else if(vs.android){
    		return 'ANDROID';
    	}else{
    		return 'WINDOWS';
    	}
    }
}

var _params = QRCODE.getUrlParams();
var _info = JE.ajax({
	async:false,
	url:JE.getMethodUrl('je.core.mk', 'getInfoById'),
	params:{
		tableCode:'JE_SYS_MK',
		pkValue:_params.id
	}
});
if(JE.isEmpty(_params.type)){
	JE.ajax({
		url:JE.getMethodUrl('je.core.mk', 'saveShowInfo'),
		params:{
			mId:_params.id
		}
	});
	_info.MK_LJSMCS = JE.toNum(_info.MK_LJSMCS)+1;
}
document.title = _info.MK_MM;
mui.ready(function(){
	mui.init();
	var type = _info.MK_MKLX_CODE;
	var cfg = Ext.decode(_info.MK_PZXX);
	cfg.pkValue = _info.JE_SYS_MK_ID;
	var el = Ext.get('content');
	
	if(type == 'WZ'){
		QRCODE.buildWZ(el,cfg);
	}else if(type == 'WWW'){
		QRCODE.buildWWW(el,cfg);
	}else if(['WJ'].indexOf(type) != -1){
		cfg.type = type;
		QRCODE.buildFile(el,cfg);
	}else if(type == 'TP'){
		QRCODE.buildImage(el,cfg);
	}else if(type == 'MP'){
		QRCODE.buildMP(el,cfg);
	}else if(type == 'APP'){
		QRCODE.buildAPP(el,cfg);
	}else if(type == 'QYXC'){
		QRCODE.buildQYXC(el,cfg);
	}
	//备注
	QRCODE.buildRemark(el,cfg);
	QRCODE.buildAddInfo(el,cfg);
	QRCODE.buildCountInfo(el,cfg);
	QRCODE.buildBGSound(el,cfg);
	//预览图片
	QRCODE.previewImage(el);
	if(['MP','QYXC'].indexOf(type) == -1){
		var css = ['html,body,.mui-content{background-color: #ffffff;}.mui-content{padding: 10px;}'];
		if(type == 'APP'){
			css.push('.mui-btn-block{display: inline-block;width: 50%;}.mui-bar-footer .mui-btn{padding:3px 0px;}');
		}else if(type == 'TP'){
			css = ['html,body,.mui-content{background-color: #ffffff;}'];
		}
		JE.createStyleSheet(css.join(''));
	}
});