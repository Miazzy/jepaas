var JE=JE||{};JE.urlMaps={},JE.getUrlMaps=function(e,o){return(JE.urlMaps||{})[e=o?e+"."+o:e]||""},JE.replaceOldUrl=function(e){return e&&(/^\//.test(e)||(e="/"+e),/^(\/je|je)/.test(e)||(e="je"+e),/^\/(.*?)\/(.*?)/.test(e)?e=(e=(e=(e=e.replace("je/dynaAction","je")).replace(/Action/g,"")).replace(/!/,"/")).replace(".action",""):/^(.*?)\/(.*?)/.test(e)&&(e=(e=(e=(e=e.replace("je/dynaAction","je")).replace(/Action/g,"")).replace(/!/,"/")).replace(".action",""))),e},function(){var e={load:"load",save:"doSave",copy:"doCopy",update:"doUpdate",remove:"doRemove",disable:"doDisable",insertUpdateList:"doInsertUpdateList",updateList:"doUpdateList",doSave:"doSave",doCopy:"doCopy",doUpdate:"doUpdate",doRemove:"doRemove",doDisable:"doDisable",doInsertUpdateList:"doInsertUpdateList",doUpdateList:"doUpdateList",listUpdate:"listUpdate",getInfoById:"getInfoById",getTree:"getTree",loadGridTree:"loadGridTree",treeMove:"treeMove",doUploadFuncFile:"doUploadFuncFile",uploadFile:"uploadFile",checkFieldUniquen:"checkFieldUniquen",executeBatchSql:"executeBatchSql",selectList:"selectList",selectOne:"selectOne",saveMark:"saveMark",savePostil:"savePostil",loadBadge:"loadBadge",getIditProcedureColumn:"getIditProcedureColumn"},o={"je.core":{base:"/je"},"je.core.login":{base:"/je/login",methods:{getIdentityByUserCode:"getIdentityByUserCode",checkUser:"checkUser",getJcaptImage:"getJcaptImage",createState:"createState"}},"je.core.sys.config":{base:"/je/sysConfig/sysConfig",methods:{loadSysVariables:"loadSysVariables",loadLoginSysVariables:"loadLoginSysVariables",loadProVariables:"loadProVariables",writeSysVariables:"writeSysVariables",writeDevelopVariables:"writeDevelopVariables",writeSysModeVariables:"writeSysModeVariables",removeFile:"removeFile"}},"je.core.rbac.user":{base:"/je/rbac/user",methods:{getCurrentUserInfo:"getCurrentUserInfo",loadQueryInfo:"loadQueryInfo",loadDeptInfo:"loadDeptInfo",loadSentryParams:"loadSentryParams",modifyPassWord:"modifyPassWord",doUpdateInfo:"doUpdateInfo",resetPassWord:"resetPassWord",resetLocked:"resetLocked",loadAppUser:"loadAppUser",doEnabled:"doEnabled",doUpdateDept:"doUpdateDept",getCurrentUserInfo:"getCurrentUserInfo",updateUserLocked:"updateUserLocked",doCallAt:"doCallAt"}},"je.core.rbac.adminPerm":{base:"/je/rbac/adminPerm",methods:{getAdminPerm:"getAdminPerm",getPermTree:"getPermTree",doUpdatePerm:"doUpdatePerm",doUpdateUsers:"doUpdateUsers",doRemoveUsers:"doRemoveUsers",getQuerySql:"getQuerySql"}},"je.core.rbac.perm":{base:"/je/rbac/perm",methods:{getMenuTree:"getMenuTree",getAppMenuPerm:"getAppMenuPerm",getAppFuncPerm:"getAppFuncPerm",getPublicCheckedPerm:"getPublicCheckedPerm",getAuthorPermTree:"getAuthorPermTree",getPermTree:"getPermTree",getFuncTree:"getFuncTree",initFuncTreeNode:"initFuncTreeNode",initAppTreeNode:"initAppTreeNode",impFuncTreeNode:"impFuncTreeNode",removeFuncTreeNode:"removeFuncTreeNode",getUserPermTree:"getUserPermTree",getCheckedPerm:"getCheckedPerm",getAppCheckedPerm:"getAppCheckedPerm",getFuncCheckedPerm:"getFuncCheckedPerm",doUpdatePerm:"doUpdatePerm",doUpdatePublicPerm:"doUpdatePublicPerm",getRoleTree:"getRoleTree",getDeptTree:"getDeptTree",getSentryTree:"getSentryTree",getMouleTree:"getMouleTree",getAppTree:"getAppTree",getProxyPermTree:"getProxyPermTree",doDevelopPerm:"doDevelopPerm",doButtonPerm:"doButtonPerm",doFuncchildPerm:"doFuncchildPerm",getRoleUserDiyPerm:"getRoleUserDiyPerm",updateRoleUserDiyPerm:"updateRoleUserDiyPerm",getRoleSelectPerm:"getRoleSelectPerm",doSaveRoleDept:"doSaveRoleDept",loadPermRoles:"loadPermRoles",loadCheckRolePerms:"loadCheckRolePerms",loadRolePermSee:"loadRolePermSee",loadUserDiyPerm:"loadUserDiyPerm",loadRoleModule:"loadRoleModule",loadRoleFunc:"loadRoleFunc",loadRoleAllPermSee:"loadRoleAllPermSee",loadRoleDiyPermSee:"loadRoleDiyPermSee",loadDeptRoles:"loadDeptRoles",doSaveRoleCheckPerm:"doSaveRoleCheckPerm",loadUserRoles:"loadUserRoles",doCheckUserPerm:"doCheckUserPerm",doCheckUserRole:"doCheckUserRole"}},"je.core.doact":{base:"/je/doAct/doAct",unextend:!0,methods:{loadMultiModels:"loadMultiModels",getModelFields:"getModelFields"}},"je.core.portal":{base:"/je/portal/portal",methods:{getCurrentInfo:"getCurrentInfo"}},"je.core.dbsql":{base:"/je/dbsql/dbsql",methods:{select:"select",loadMultiModels:"loadMultiModels",getSelectModel:"getSelectModel",removeBySql:"removeBySql",executeSql:"executeSql",selectSql:"selectSql",doTrans:"doTrans"}},"je.core.jms":{base:"/je/jms/jms",methods:{loadMsg:"loadMsg",loadImMsg:"loadImMsg",loadHistory:"loadHistory",loadRelation:"loadRelation",getChannle:"getChannle",loadGroup:"loadGroup"}},"je.core.menu":{base:"/je/menu/menu",methods:{menuMove:"menuMove",expSysExcel:"expSysExcel"}},"je.core.saasYh":{base:"/je/saas/saasYh",methods:{register:"register",registerDsf:"registerDsf",doUserRz:"doUserRz",modifyPw:"modifyPw",resetPassword:"resetPassword",saasManager:"saasManager.action",loadUseCp:"loadUseCp",validateRegister:"validateRegister",validateUserCode:"validateUserCode",sendRandom:"sendRandom",validateRandom:"validateRandom",doRzRandom:"doRzRandom",getUserAt:"getUserAt",createState:"createState"}},"je.core.saas.cpYhq":{base:"/je/saas/cpYhq",methods:{checkYhq:"checkYhq"}},"je.core.saas.saasNote":{base:"/je/saas/saasNote",methods:{loadInfo:"loadInfo",sqDx:"sqDx",updateDx:"updateDx",doPayOrder:"doPayOrder",complateCz:"complateCz"}},"je.core.saas.cpInfo":{base:"/je/saas/cpInfo",methods:{loadCpDicInfo:"loadCpDicInfo",loadCpPerm:"loadCpPerm",loadCps:"loadCps",loadSaasOrders:"loadSaasOrders",doInitDic:"doInitDic"}},"je.core.saas":{base:"/je/saas/saas",methods:{setUpCp:"setUpCp",setupSaasCp:"setupSaasCp",uninstallCp:"uninstallCp",initCompany:"initCompany",clearCompany:"clearCompany",loadCpPermTree:"loadCpPermTree",loadCpFuncPermTree:"loadCpFuncPermTree",updateCpPerm:"updateCpPerm",updateCpYhPerm:"updateCpYhPerm",loadCpYhPermTree:"loadCpYhPermTree",loadApkPermTree:"loadApkPermTree",loadCpYhFuncPermTree:"loadCpYhFuncPermTree",loadCpYhGhPermTree:"loadCpYhGhPermTree",loadCpYhDicPermTree:"loadCpYhDicPermTree",impCpYhFuncPerm:"impCpYhFuncPerm",addYhDic:"addYhDic",delYhDic:"delYhDic",doPayCpOrder:"doPayCpOrder",calculateTodayOperatingData:"calculateTodayOperatingData",checkYhFpInfo:"checkYhFpInfo",getInvoice:"getInvoice",deleteInvoice:"deleteInvoice",rechargeable:"rechargeable",operationOne:"operationOne",operationTwo:"operationTwo"}},"je.core.staticize":{base:"/je/staticize/staticize",methods:{loadStaticize:"loadStaticize",doFuncInfo:"doFuncInfo",clearFuncInfo:"clearFuncInfo",resetMenuStaticize:"resetMenuStaticize"}},"je.core.icon":{base:"/je/icon/icon",methods:{saveFile:"saveFile",loadFile:"loadFile",generateCSS:"generateCSS",saveCustomCss:"saveCustomCss",saveSysSingleton:"saveSysSingleton",importFont:"importFont"}},"je.core.vmt":{base:"/je/vmt/vmt",methods:{createC4T:"createC4T"}},"je.core.develop.funcInfo":{base:"/je/develop/funcInfo",methods:{doUpdateHelp:"doUpdateHelp",loadFuncs:"loadFuncs",doUpdateInfo:"doUpdateInfo",doSummary:"doSummary",funcCopy:"funcCopy",getFuncByCode:"getFuncByCode",getFuncPerm:"getFuncPerm",removeFuncs4cache:"removeFuncs4cache",copySoftFun:"copySoftFun",copyHard:"copyHard",getInfoByCode:"getInfoByCode",clearFuncInfo:"clearFuncInfo",removeFunRelyon:"removeFunRelyon",treeMove:"treeMove",returnInfo:"returnInfo",summary:"summary",checkIn:"checkIn",checkOut:"checkOut",checkDefault:"checkDefault",checkSync:"checkSync",loadFunc:"loadFunc",getInfoById:"getInfoById",clearStaticize:"clearStaticize"}},"je.core.develop.excel":{base:"/je/develop/excel",methods:{exp:"exp",doPreviewData:"doPreviewData",loadSheetInfo:"loadSheetInfo"}},"je.core.develop.associationField":{base:"/je/develop/associationField"},"je.core.develop.dataImpl":{base:"/je/develop/dataImpl",methods:{generateTemplate:"generateTemplate",implFields:"implFields",implExcelFields:"implExcelFields",clearHidden:"clearHidden",clearExcelHidden:"clearExcelHidden",quickColumn:"quickColumn",applyFunc:"applyFunc"}},"je.core.develop.funcPerm":{base:"/je/develop/funcPerm",methods:{getFuncRoleTree:"getFuncRoleTree",getFuncFieldDic:"getFuncFieldDic",getFuncPerm:"getFuncPerm",doUpdatePerm:"doUpdatePerm",getFuncYwcjPerm:"getFuncYwcjPerm"}},"je.core.develop.funRelation":{base:"/je/develop/funRelation",methods:{doImpl:"doImpl",insertAny:"insertAny"}},"je.core.develop.funRelyon":{base:"/je/develop/funRelyon",methods:{removeRelyon:"removeRelyon"}},"je.core.develop.gantt":{base:"/je/develop/gantt"},"je.core.develop.queryStrategy":{base:"/je/develop/queryStrategy"},"je.core.develop.resButton":{base:"/je/develop/resButton",methods:{initShBtnInfo:"initShBtnInfo"}},"je.core.develop.resColumn":{base:"/je/develop/resColumn",methods:{impl:"impl",doSync:"doSync",doQuerySave:"doQuerySave",getTreeTableCodes:"getTreeTableCodes"}},"je.core.develop.resField":{base:"/je/develop/resField",methods:{doSync:"doSync"}},"je.core.web.app":{base:"/je/web/app",methods:{execute:"execute"}},"je.core.phone.app":{base:"/je/phone/app",methods:{push:"push",getApkBadge:"getApkBadge",getAppDataNum:"getAppDataNum",loadHome:"loadHome",loadApk:"loadApk",loadLoginCfg:"loadLoginCfg",initWFData:"initWFData",getWfCommentDetail:"getWfCommentDetail",doInitList:"doInitList",doInitForm:"doInitForm",doReportInit:"doReportInit",doReportLoad:"doReportLoad",loadDS:"loadDS",doChartLoad:"doChartLoad",doDSLoad:"doDSLoad"}},"je.core.phone.phone":{base:"/je/phone",methods:{buildApp:"buildApp",buildPlugin:"buildPlugin",importPlugin:"importPlugin",buildFuncHtml:"buildFuncHtml",push:"push",doVersion:"doVersion",doCopyApp:"doCopyApp",doPluginAdd:"doPluginAdd",doAddApk:"doAddApk",doAddModel:"doAddModel",doTreeNodeMove:"doTreeNodeMove",importApps:"importApps",exportApps:"exportApps",downLoginFile:"downLoginFile"}},"je.core.doc.document":{base:"/je/doc/document",methods:{doLoadFile:"doLoadFile",downLoadZIP:"downLoadZIP",downLoadFiles:"downLoadFiles",download:"download",loadFormFiles:"loadFormFiles",uploadFile:"uploadFile",removeFormFiles:"removeFormFiles",getDocInfo:"getDocInfo",deleteFile:"deleteFile",writeFile:"writeFile",openOfficeFile:"openOfficeFile",openPageOffice:"openPageOffice"}},"je.core.doc.documentation":{base:"/je/doc/documentation",methods:{saveDirs:"saveDirs",download:"download",downloadPackage:"downloadPackage",downloadZip:"downloadZip",doRestore:"doRestore",doClear:"doClear",newFile:"newFile",doSaveFile:"doSaveFile",getFileMenuTree:"getFileMenuTree",paste:"paste",packageFile:"packageFile",getFuncInfoTree:"getFuncInfoTree",doOpenFile:"doOpenFile",treeMove:"treeMove",doUpdatePerm:"doUpdatePerm",addFaster:"addFaster",orderFaster:"orderFaster"}},"je.core.doc.file":{base:"/je/doc/file",methods:{newFile:"newFile",downloadPackage:"downloadPackage",paste:"paste"}},"je.core.doc.fileview":{base:"/je/doc/fileview",methods:{loadFileData:"loadFileData",loadEmlData:"loadEmlData",saveFile:"saveFile"}},"je.core.sys.chart":{base:"/je/sys/sysChart",methods:{loadFileData:"loadFileData",loadEmlData:"loadEmlData",saveFile:"saveFile"}},"je.core.email":{base:"/je/email/email",methods:{loadUserRoot:"loadUserRoot",doAddConfig:"doAddConfig",updateConfig:"updateConfig",testConfig:"testConfig",doSend:"doSend",doSendEmail:"doSendEmail",doReply:"doReply",doReceive:"doReceive",stopReceive:"stopReceive",getUnreadCount:"getUnreadCount",doClear:"doClear"}},"je.core.word":{base:"/je/word",methods:{report2word:"report2word",report2excel:"report2excel"}},"je.core.resourceTable":{base:"/je/resourceTable",methods:{applyTable:"applyTable",getFuncInfoByTable:"getFuncInfoByTable",copyTable:"copyTable",imp:"imp",createView:"createView",updateView:"updateView",syncView:"syncView",syncTable:"syncTable",impView:"impView",checkTableCode:"checkTableCode",loadTableData:"loadTableData",sql:"sql",physicsRemove:"physicsRemove",clearTableCache:"clearTableCache",clearTableAllCache:"clearTableAllCache",pasteTable:"pasteTable",getInfoById:"getInfoById",tableMove:"tableMove",getTableInfoByFunc:"getTableInfoByFunc",getPkCode4Table:"getPkCode4Table",syncTreePath:"syncTreePath",initSaasColumn:"initSaasColumn",toOracle:"toOracle",toMySql:"toMySql",generateSql:"generateSql",syncOracleData:"syncOracleData",syncMySqlData:"syncMySqlData",syncOracleJbpm:"syncOracleJbpm",syncTableField:"syncTableField",doSetPkName:"doSetPkName"}},"je.core.tableKey":{base:"/je/table/tableKey",methods:{removeKey:"removeKey",doAddPk:"doAddPk"}},"je.core.tableIndex":{base:"/je/table/tableIndex",methods:{removeIndex:"removeIndex",createIndexByColumn:"createIndexByColumn",removeIndexByColumn:"removeIndexByColumn"}},"je.core.tableColumn":{base:"/je/table/tableColumn",methods:{addField:"addField",removeColumn:"removeColumn",impNewCols:"impNewCols",checkColunmnCode:"checkColunmnCode",addColumnByDD:"addColumnByDD",addColumnByDDList:"addColumnByDDList",addColumnByTable:"addColumnByTable",addColumnByAtom:"addColumnByAtom",addAtomByColumn:"addAtomByColumn",generateUpdateInfo:"generateUpdateInfo",generateShInfo:"generateShInfo",findColumnCount:"findColumnCount"}},"je.core.btnlog":{base:"/je/login/btnLog",methods:{insertBtnLog:"insertBtnLog"}},"je.core.calendar":{base:"/je/calendar/calendar",methods:{loadPortal:"loadPortal",getMyTaskTree:"getMyTaskTree",getGroupTree:"getGroupTree",getShareTaskTree:"getShareTaskTree",removeGroup:"removeGroup",addGroupUsers:"addGroupUsers"}},"je.core.sysConfig.sysMode":{base:"/je/sysConfig/sysMode",methods:{expDevelop:"expDevelop",backDevelopData:"backDevelopData"}},"je.core.cache":{base:"/je/app/cache",methods:{clearAll:"clearAll",clearFunc:"clearFunc",clearDic:"clearDic",clearMenu:"clearMenu",getModel:"getModel",applyCacheConfig:"applyCacheConfig"}},"je.core.dd.dd":{base:"/je/dd/dd",methods:{initLoad:"initLoad",clearCache:"clearCache",getDicByCode:"getDicByCode",getDicItemByCode:"getDicItemByCode",getDicItemByCodes:"getDicItemByCodes"}},"je.core.dd.tree":{base:"/je/dd/tree",methods:{findAsyncNodes:"findAsyncNodes",loadTreeData:"loadTreeData",loadTree:"loadTree",loadLinkTree:"loadLinkTree",clearMenu:"clearMenu",getModel:"getModel",applyCacheConfig:"applyCacheConfig"}},"je.core.dd.ddItem":{base:"/je/dd/ddItem",methods:{checkDdItems:"checkDdItems"}},"je.core.db":{base:"/je/db/db",methods:{doDb:"doDb",doCheckInfo:"doCheckInfo",doSyncCoreInfo:"doSyncCoreInfo",doWfData:"doWfData",doYwData:"doYwData",doFuncInfo:"doFuncInfo",doSysInfo:"doSysInfo",expDbExcel:"expDbExcel"}},"je.core.dataSource":{base:"/je/dataSource",methods:{doTest:"doTest"}},"je.core.job":{base:"/je/job/job",methods:{addJobDesign:"addJobDesign",deleteTask:"deleteTask",stopTask:"stopTask",startTask:"startTask",receiveTask:"receiveTask",finishTask:"finishTask",setHistory:"setHistory",addStar:"addStar",cancelStar:"cancelStar"}},"je.core.message.message":{base:"/je/message/message",methods:{sendSys:"sendSys",testSendMsg:"testSendMsg",sendRtx:"sendRtx",sendDwr:"sendDwr",sendEmail:"sendEmail",sendNote:"sendNote",sendMsg:"sendMsg",sendNoteMsg:"sendNoteMsg",checkNoteMsg:"checkNoteMsg"}},"je.core.mk":{base:"/je/mk/mk",methods:{saveLogo:"saveLogo",loadLogo:"loadLogo",saveShowInfo:"saveShowInfo",createOrUpdate:"createOrUpdate"}},"je.core.mxGraph":{base:"/je/mxGraph/mxGraph",methods:{expImg:"expImg"}},"je.core.rbac.rbac":{base:"/je/rbac/rbac",methods:{queryUserByDeptId:"queryUserByDeptId",queryUserByDeptCode:"queryUserByDeptCode",countUserByDeptCode:"countUserByDeptCode",countUserByDeptId:"countUserByDeptId",queryUserByRoleId:"queryUserByRoleId",queryUserByRoleCode:"queryUserByRoleCode",countUserByRoleCode:"countUserByRoleCode",countUserByRoleId:"countUserByRoleId",queryUserBySentryId:"queryUserBySentryId",queryUserBySentryCode:"queryUserBySentryCode",countUserBySentryCode:"countUserBySentryCode",countUserBySentryId:"countUserBySentryId",queryUser:"queryUser",countUser:"countUser",queryDeptById:"queryDeptById",queryDeptByCode:"queryDeptByCode",countDeptByCode:"countDeptByCode",countDeptById:"countDeptById",queryRoleById:"queryRoleById",queryRoleByCode:"queryRoleByCode",countRoleByCode:"countRoleByCode",countRoleById:"countRoleById",querySentryById:"querySentryById",querySentryByCode:"querySentryByCode",countSentryByCode:"countSentryByCode",countSentryById:"countSentryById"}},"je.core.rbac.dept":{base:"/je/rbac/dept",methods:{doEnabled:"doEnabled",implUsers:"implUsers",checkDeptCode:"checkDeptCode",checkAdminDept:"checkAdminDept",syncCompany:"syncCompany",treeMove:"treeMove"}},"je.core.rbac.role":{base:"/je/rbac/role",methods:{roleMove:"roleMove",loadRoleUsers:"loadRoleUsers",doUpdateUsers:"doUpdateUsers",removeUsers:"removeUsers",syncDeptOrder:"syncDeptOrder",syncSentryOrder:"syncSentryOrder",doImplUser:"doImplUser"}},"je.core.rbac.cjgl":{base:"/je/rbac/cjgl",methods:{paste:"paste",doAddNode:"doAddNode",gsMove:"gsMove",saveNode:"saveNode",removeNode:"removeNode",saveDicNode:"saveDicNode"}},"je.core.rbac.roleGroup":{base:"/je/rbac/roleGroup",methods:{}},"je.core.rbac.workGroup":{base:"/je/rbac/workGroup",methods:{implUsers:"implUsers",removeUsers:"removeUserss"}},"je.core.rbac.sentry":{base:"/je/rbac/sentry",methods:{doEnabled:"doEnabled",loadSentryUsers:"loadSentryUsers",implUsers:"implUsers",removeUsers:"removeUsers",loadRoleUsers:"loadRoleUsers"}},"je.core.wf.taskInfo":{base:"/je/jbpm/taskInfo",methods:{getCurrentTask:"getCurrentTask",updateTaskPriority:"updateTaskPriority",updateTaskDelay:"updateTaskDelay",completeTask:"completeTask",completeBatchTask:"completeBatchTask",taskTask:"taskTask",doPrompt:"doPrompt",loadWfInfo:"loadWfInfo",loadWfPdInfo:"loadWfPdInfo",loadForkTaskInfo:"loadForkTaskInfo",loadEndInfo:"loadEndInfo",startProcess:"startProcess",sponsorProcess:"sponsorProcess",callProcess:"callProcess",endProcess:"endProcess",handUpProcess:"handUpProcess",activateProcess:"activateProcess",loadTaskAssgine:"loadTaskAssgine",loadTaskRejectAssgine:"loadTaskRejectAssgine",loadAppTaskAssgine:"loadAppTaskAssgine",loadRoundUser:"loadRoundUser",loadAppRoundUser:"loadAppRoundUser",loadAppPromptUser:"loadAppPromptUser",getProcessEndTask:"getProcessEndTask",getWfTaskName:"getWfTaskName",doSetDiyUser:"doSetDiyUser",getModel:"getModel"}},"je.core.wf.processInfo":{base:"/je/jbpm/processInfo",methods:{callDeploy:"callDeploy",deploy:"deploy",showImage:"showImage",getProcessTree:"getProcessTree",getCurrentTaskTree:"getCurrentTaskTree",getProcessInfo:"getProcessInfo",loadProcessHistory:"loadProcessHistory",clearDirtyData:"clearDirtyData"}},"je.core.test.testAll":{base:"/je/test/testAll",methods:{addRootNodeByDD:"addRootNodeByDD",syncDicCore:"syncDicCore",syncSyPath:"syncSyPath",updateKeys:"updateKeys",updateTableField:"updateTableField",syncYhyg:"syncYhyg",updateDataVal:"updateDataVal",updateFieldVal:"updateFieldVal",updateIntFieldVal:"updateIntFieldVal",updateSysField:"updateSysField",syncFuncData:"syncFuncData",printTest:"printTest",doTree:"doTree",doChildTree:"doChildTree",test:"test",exp:"exp",syncFuncInfo:"syncFuncInfo",syncMenu:"syncMenu",syncTableColumn:"syncTableColumn",syncDicInfo:"syncDicInfo",syncIndex:"syncIndex",testBean:"testBean",testProcedure:"testProcedure",id:"id"}},"je.core.crm.crmHr":{base:"/je/CRM/crmHr",methods:{createOrUpdateCost:"createOrUpdateCost"}},"je.core.crm.zclr":{base:"/je/CRM/zclr",methods:{}}},a={"sys/iconAction!importFont.action":"/je/icon/icon/importFont","je/appAction!doInitList.action":"/je/phone/app/doInitList","rbac/userAction!getCurrentUserInfo.action":"/je/rbac/user/getCurrentUserInfo","sys/expressAction!load.action":"/je/express/express/load","je/appAction!loadHome.action":"/je/phone/app/loadHome","/je/upgradeAction!implUpgradeFile.action":"/je/upgrade/implUpgradeFile"};for(var d in e){var t="/"+e[d];JE.urlMaps["method."+d]=t}for(var r in o){var s=o[r],l=s.methods=s.methods||{};if(!s.unextend)for(var d in e)l[d]=l[d]?l[d]:e[d];var n=JE.urlMaps[r]=s.base;for(var d in l){t="/"+l[d];JE.urlMaps[r+"."+d]=n+t}}for(var c in a)JE.urlMaps[c]=a[c]}();