/**
 * 资源文件，app专用
 */
(function() {
	//网络路径
	var basicPath = 'http://localhost:13131/_www/';
	
	//开发模式
	var scriptEls = document.getElementsByTagName('script');
	for(var i=0;i<scriptEls.length;i++){
		var src = scriptEls[i].getAttribute('src');
		if(src){
			index = src.indexOf('js/include-je.js');
		    	if(index != -1){
		    		basicPath = src.substring(0,index);
		    		break;
		    	}
		}
	}
	
	var files = {
			css:[
				'css/JE.min.css',
				'css/JE.core.min.css'
			],
			js:[
				'js/JEPF.min.js',
				'js/url-map.js',
				'js/override.js',
				'js/JEPF.core.min.js',
				'js/common.js'
			]
		};
	//发布模式	
    for(var p in files){
    	for(var i=0;i<files[p].length;i++){
			var file = files[p][i];
			if(file.substring(0,1) != '/'){
				file = basicPath+file;
			}
	    	if(p=='js'){
    			document.write('<script type="text/javascript" src="' + file + '"></script>');
	    	}else{
        		document.write('<link rel="stylesheet" type="text/css" href="' + file + '"/>');
	    	} 
    	}
    }
})();
