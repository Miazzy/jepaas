(function( $ ){
// 实例化
var uploader = WebUploader.create({
    formData: {
        filePath: '/webuploader'
    },
    fileVal:'formUploadFiles',
    dnd: '#ddview',
    paste : '#ddview',
    swf: '/static/ux/webuploader/swf/Uploader.swf',
    server:JE.getUrlMaps("je.core.uploadFile"),
    disableGlobalDnd:true,
    prepareNextFile:true,
    fileNumLimit:10,
    fileSizeLimit:200 * 1024 * 1024,    // 200 M
    fileSingleSizeLimit:2 * 1024 * 1024,    // 50 M,
    /**
     * 当拖拽进入区域触发
     */
    dndDragEnter:function(dnd){
    },
    /**
     * 当停留在区域触发   一直会执行
     */
    dndDragOver:function(dnd){
    },
    /**
     * 当拖拽离开区域触发
     */
    dndDragLeave:function(dnd){
    }
});

//拒绝指定文件格式上传
uploader.on("dndAccept",function(items){
//	var denied = false,
//            len = items.length,
//            i = 0,
//            // 修改js类型
//            unAllowed = 'text/plain;application/javascript ';
//
//        for ( ; i < len; i++ ) {
//            // 如果在列表里面
//            if ( ~unAllowed.indexOf( items[ i ].type ) ) {
//                denied = true;
//                break;
//            }
//        }
//    return !denied;
});
//当单个文件被加入
uploader.on("beforeFileQueued",function(file){
	console.log(file)
	return false;
});
//当单个文件被加入
uploader.on("fileQueued",function(file){
//	console.log(file)
	uploader.upload();
});
//当一批文件被加入
uploader.on("filesQueued",function(items){
	uploader.upload();
});
/**
 * 
 *  object {Object}
 *  data {Object}默认的上传参数，可以扩展此对象来控制上传参数。
 *	headers {Object}可以扩展此对象来控制上传头部。
 */
uploader.on("uploadBeforeSend",function(object,data,headers){
	//console.log(object.file.fullPath);
	data.userName='marico';
});
//当文件上传成功时触发。
uploader.on('uploadSuccess',function(file,response){
	console.log(response);	
});
//上传过程中触发，携带上传进度
uploader.on('uploadProgress',function(file,percentage){
});

})( jQuery );