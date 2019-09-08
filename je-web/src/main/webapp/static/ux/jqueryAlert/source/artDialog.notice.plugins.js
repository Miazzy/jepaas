/*
 * 通知框
 * 由屏幕右下方弹上提示
*/

(function ($) {
	
	
$.noticeManager = {};
artDialog.notice = function (options) {
    var opt = options || {},
        duration = 1000;
        
    var config = {
        left: '100%',
        top: '100%',
        fixed: true,
        drag: false,
        resize: false,
        follow: null,
        lock: false,
        initialize: function(here){
            var me = this,height = 0;
            //提示框
            var wrap = me.dom.wrap;
            //高度
            for(var p in $.noticeManager){
            	height+=$.noticeManager[p][0].offsetHeight;
            }
            $.noticeManager[opt.id] = wrap;
            var hide = JE.getBodyHeight();
            var left = JE.getBodyWidth()
            
            var top = hide - wrap[0].offsetHeight - height;
            left = left - wrap[0].offsetWidth;
            
            wrap.css('top', top + 'px').css('left',left+'px');
//                .animate({top: top + 'px'}, duration, function () {
//                    opt.initialize && opt.initialize.call(api, here);
//                });
        }
    };	
    
    for (var i in opt) {
        if (config[i] === undefined) config[i] = opt[i];
    };
    var art = artDialog(config);
    
    art.close = function(){
        var me = this;
    	var hide = JE.getBodyHeight();
        //提示框
        var wrap = me.dom.wrap;
        wrap.animate({top: hide + 'px'}, duration, function () {
            opt.close && opt.close.call(this);
            wrap.remove();
            delete $.noticeManager[opt.id];
            me.closed = true;
        });
    };
    return artDialog(config);
};
$.notice = $.dialog.notice;


}(this.art || this.jQuery));

