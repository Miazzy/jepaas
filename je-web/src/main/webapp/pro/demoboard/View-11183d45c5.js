/**
 * 项目view
 */
/* eslint-disable */
Ext.define("PRO.demoboard.View", {
  extend: 'Ext.panel.Panel',
  alias:'widget.pro.demoboard',
  border: 0,
  layout: 'fit',
  autoScroll: true,
  initComponent: function () {
    var me = this;
    me.callParent(arguments);
  },
  afterRender: function () {
    var me = this;
    me.callParent(arguments);
    var folder = 'pro/demoboard';
    //加载本页面资源
    JE.loadScript([
      '/'+folder+'/index.min.js',
      '/'+folder+'/index.min.css'
    ]);
    //创建vue装载dom
    var tag = folder.split('/').pop().replace(/([A-Z])/g, '-$1').toLowerCase();
    var dom = me.body.insertHtml('beforeEnd', '<' + tag +' :callback="callback" :params="params"/>');
    //时间组件
    if(window.moment) {window.moment.locale('zh-cn')}
    //载入页面
    var vueInfo = me.vueInfo || {}
    me.vm = new Vue({
      el: dom,
      data: function () {
        return {
          params: vueInfo.params
        }
      },
      methods: {
        callback: vueInfo.callback
      }});

    me.on('activate',function(){
      if (me.created){
        me.load();
      }else{
        me.created = true;
      }
    })
  },
  load:function(){
    var vm = this.vm.$children[0];
    vm && vm.load && vm.load.apply(vm,arguments);
  }
})
