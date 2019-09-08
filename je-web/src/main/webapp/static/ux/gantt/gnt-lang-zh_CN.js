/*

Ext Gantt 2.2.1
Copyright(c) 2009-2013 Bryntum AB
http://bryntum.com/contact
http://bryntum.com/license

*/
/**
 * English translations for the Gantt component
 *
 * NOTE: To change locale for month/day names you have to use the Ext JS language pack.
 */
(function() {
	//修改下面的参数，会导致parser不正确
//    if (Gnt.util.DurationParser){
//        Gnt.util.DurationParser.prototype.unitsRegex = {
//            MILLI       : /^ms$|^毫秒/i,
//            SECOND      : /^s$|^秒/i,
//            MINUTE      : /^m$|^分/i,
//            HOUR        : /^h$|^hr$|^小时/i,
//            DAY         : /^d$|^天/i,
//            WEEK        : /^w$|^wk|^周/i,
//            MONTH       : /^mo|^月/i,
//            QUARTER     : /^q$|^quar|^季度/i,
//            YEAR        : /^y$|^yr|^年/i
//        };
//    }
	if(Sch.util.Date){
		Sch.util.Date._unitNames ={
				YEAR : {
					single : "年",
					plural : "年",
					abbrev : "年"
				},
				QUARTER : {
					single : "一刻",
					plural : "一刻钟",
					abbrev : "一刻"
				},
				MONTH : {
					single : "月",
					plural : "月",
					abbrev : "月"
				},
				WEEK : {
					single : "星期",
					plural : "星期",
					abbrev : "周"
				},
				DAY : {
					single : "天",
					plural : "天",
					abbrev : "天"
				},
				HOUR : {
					single : "小时",
					plural : "小时",
					abbrev : "时"
				},
				MINUTE : {
					single : "分钟",
					plural : "分钟",
					abbrev : "分"
				},
				SECOND : {
					single : "秒",
					plural : "秒",
					abbrev : "秒"
				},
				MILLI : {
					single : "毫秒",
					plural : "毫秒",
					abbrev : "毫秒"
				}
			}
			
		Ext.iterate(Sch.util.Date.unitHash,function(key,val){
			Sch.util.Date._unitNames[val] =Sch.util.Date._unitNames[key]; 
		});
	}
    if (Gnt.widget.DurationField){
        Gnt.widget.DurationField.prototype.invalidText = "错误的工期值";
    }

    if (Gnt.feature.DependencyDragDrop) {
        Gnt.feature.DependencyDragDrop.prototype.fromText = '从: <strong>{0}</strong> {1}<br/>';
        Gnt.feature.DependencyDragDrop.prototype.toText = '到: <strong>{0}</strong> {1}';
        Gnt.feature.DependencyDragDrop.prototype.startText = '开始';
        Gnt.feature.DependencyDragDrop.prototype.endText = '结束';
    }

    if (Gnt.Tooltip) {
        Gnt.Tooltip.prototype.startText = '开始: ';
        Gnt.Tooltip.prototype.endText = '结束: ';
        Gnt.Tooltip.prototype.durationText = '工期:';
        Gnt.Tooltip.prototype.dayText = '天';
    }

    if (Gnt.plugin.TaskContextMenu) {
        Gnt.plugin.TaskContextMenu.prototype.texts = {
            newTaskText : '新建任务',
            newMilestoneText : '新建里程碑',
            deleteTask : '删除任务',
            editLeftLabel : '修改左边标签',
            editRightLabel : '修改右边标签',
            add : '添加...',
            deleteDependency : '删除前后置关系...',
            addTaskAbove : '到上方',
            addTaskBelow : '到下方',
            addMilestone : '里程碑',
            addSubtask :'子节点',
            addSuccessor : '后继任务',
            addPredecessor : '前置任务',
            convertToMilestone:'转为里程碑',
            convertToRegular:'转为任务'
        };
    }

    if (Gnt.plugin.DependencyEditor) {
        Gnt.plugin.DependencyEditor.override({
            fromText : '从',
            toText : '到',
            typeText : '类型',
            lagText : '延迟',
            endToStartText : '结束开始',
            startToStartText : '开始开始',
            endToEndText : '结束结束',
            startToEndText : '开始结束'
        });
    }

    if (Gnt.widget.calendar.Calendar){
        Gnt.widget.calendar.Calendar.override({
            dayOverrideNameHeaderText : '名称',
            dateText           : '日期',
            addText            : '添加',
            editText           : '编辑',
            removeText         : '删除',
            workingDayText     : '工作日',
            weekendsText       : '周末',
            overriddenDayText  : '过载天',
            overriddenWeekText : '过载周',
            defaultTimeText    : '默认时间',
            workingTimeText    : '工作时间',
            nonworkingTimeText : '非工作时间',
            dayOverridesText   : '过载的天',
            weekOverridesText  : '过载的周',
            okText             : '确定',
            cancelText         : '取消',
            parentCalendarText : '父日历',
            noParentText       : '无父日历',
            selectParentText   : '请选择父日历',
            newDayName         : '[未命名]',
            calendarNameText   : '日历名称',
            tplTexts           : {
                tplWorkingHours : '工时',
                tplIsNonWorking : '非工作的',
                tplOverride     : '过载',
                tplInCalendar   : '在日历中',
                tplDayInCalendar: '日历中标准天'
            },
            overrideErrorText: '日期已过载',
            intersectDatesErrorText: "日期不能交叉包含",
            startDateErrorText: '开始时间比结束时间晚'
        });
    }

    if (Gnt.widget.calendar.DayAvailabilityGrid){
        Gnt.widget.calendar.DayAvailabilityGrid.override({
            startText          : '开始',
            endText            : '结束',
            addText            : '添加',
            removeText         : '删除',
            workingTimeText    : '工作时间',
            nonworkingTimeText : '非工作时间'
        });
    }

    if (Gnt.widget.calendar.DayGrid){
        Gnt.widget.calendar.DayGrid.override({
            nameText   : '名称',
            dateText   : '日期',
            noNameText : '[未命名日期]'
        });
    }

    if (Gnt.widget.calendar.WeekEditor){
        Gnt.widget.calendar.WeekEditor.override({
            startHeaderText    : '开始',
            endHeaderText      : '结束',
            defaultTimeText    : '默认时间',
            workingTimeText    : '工作时间',
            nonworkingTimeText : '非工作时间',
            addText            : '添加',
            removeText         : '删除'
        });
    }

    if (Gnt.widget.calendar.WeekGrid){
        Gnt.widget.calendar.WeekGrid.override({
            nameText      : '名称',
            startDateText : '开始时期',
            endDateText   : '结束日期'
        });
    }

    if (Gnt.widget.AssignmentField){
        Gnt.widget.AssignmentField.override({
            cancelText : '取消',
            closeText  : '保存并关闭'
        });
    }

    if (Gnt.widget.Calendar){
        Gnt.widget.Calendar.override({
            disabledDatesText   : '节假日'
        });
    }

    if (Gnt.column.AssignmentUnits) {
        Gnt.column.AssignmentUnits.override({
            text : '单位'
        });
    }

    if (Gnt.column.Duration) {
        Gnt.column.Duration.override({
            text : '工期'
        });
    }

    if (Gnt.column.Effort) {
        Gnt.column.Effort.override({
            text : '投入'
        });
    }

    if (Gnt.column.EndDate) {
        Gnt.column.EndDate.override({
            text : '结束'
        });
    }

    if (Gnt.column.PercentDone) {
        Gnt.column.PercentDone.override({
            text : '完成'
        });
    }

    if (Gnt.column.ResourceAssignment) {
        Gnt.column.ResourceAssignment.override({
            text : '已分配资源'
        });
    }

    if (Gnt.column.ResourceName) {
        Gnt.column.ResourceName.override({
            text : '资源名称'
        });
    }

    if (Gnt.column.SchedullingMode) {
        Gnt.column.SchedullingMode.override({
            text : '模式'
        });
    }

    if (Gnt.column.Predecessor) {
        Gnt.column.Predecessor.override({
            text : '前置任务'
        });
    }

    if (Gnt.column.Successor) {
        Gnt.column.Successor.override({
            text : '后续任务'
        });
    }

    if (Gnt.column.StartDate) {
        Gnt.column.StartDate.override({
            text : '开始'
        });
    }

    if (Gnt.column.WBS) {
        Gnt.column.WBS.override({
            text : '#'
        });
    }

    if (Gnt.widget.taskeditor.TaskForm) {
        Gnt.widget.taskeditor.TaskForm.override({
            taskNameText            : '名称',
            durationText            : '工期',
            datesText               : '日期',
            baselineText            : '基线',
            startText               : '开始',
            finishText              : '结束',
            percentDoneText         : '完成百分比',
            baselineStartText       : '开始',
            baselineFinishText      : '结束',
            baselinePercentDoneText : '完成百分比',
            effortText              : '工时',
            invalidEffortText       : '错误的投入值',
            calendarText            : '日历',
            schedulingModeText      : '排程模式'
        });
    }

    if (Gnt.widget.DependencyGrid) {
        Gnt.widget.DependencyGrid.override({
            idText                      : 'ID编号',
            taskText                    : '任务名称',
            blankTaskText               : '请选择任务',
            invalidDependencyText       : '错误的依赖关系',
            parentChildDependencyText   : '父子节点含有依赖关系',
            duplicatingDependencyText   : '有重复的依赖关系',
            transitiveDependencyText    : '存在传递的依赖关系',
            cyclicDependencyText        : '依赖关系中存在环路',
            typeText                    : '类型',
            lagText                     : '延隔时间',
            clsText                     : 'CSS类',
            endToStartText              : '结束开始',
            startToStartText            : '开始开始',
            endToEndText                : '结束结束',
            startToEndText              : '开始结束'
        });
    }

    if (Gnt.widget.AssignmentEditGrid) {
        Gnt.widget.AssignmentEditGrid.override({
            confirmAddResourceTitle : '确认',
            confirmAddResourceText  : '未找到{0}相关资源。要新建该资源?',
            noValueText             : '请选择要分配的资源。',
            noResourceText          : '未找到{0}相关资源。'
        });
    }

    if (Gnt.widget.taskeditor.TaskEditor) {
        Gnt.widget.taskeditor.TaskEditor.override({
            generalText         : '常规',
            resourcesText       : '资源',
            dependencyText      : '前置任务',
            addDependencyText   : '添加',
            dropDependencyText  : '删除',
            notesText           : '备注',
            advancedText        : '高级',
            wbsCodeText         : 'WBS代码',
            addAssignmentText   : '添加',
            dropAssignmentText  : '删除',
            noteText            : '备注'
        });
    }

    if (Gnt.plugin.TaskEditor) {
        Gnt.plugin.TaskEditor.override({
            title           : '任务信息',
            alertCaption    : '信息',
            alertText       : '请在保存前修改相关的错误',
            okText          : '确定',
            cancelText      : '取消'
        });
    }
    
    /////////////////////////////以下是原有的汉化程序///////////////////////////////////////////////////////////
    if (Sch.plugin) {
        if (Sch.plugin.SummaryColumn) {
            Ext.override(Sch.plugin.SummaryColumn, {
                dayText : 'd',
                hourText : 'h',
                minuteText : 'min'
            });
        }

        if (Sch.plugin.CurrentTimeLine) {
            Sch.plugin.CurrentTimeLine.prototype.tooltipText = '当前时间';
        }
    }
    
    var M = Sch.preset.Manager,
        vp = M.getPreset("hourAndDay");

    if (vp) {
        vp.displayDateFormat = 'g:i A';
        vp.headerConfig.middle.dateFormat = 'g A';
        vp.headerConfig.top.dateFormat = 'Y年m月d日';
    } 
    
    vp = M.getPreset("dayAndWeek");
    if (vp) {
        vp.displayDateFormat = 'm/d h:i A';
        vp.headerConfig.middle.dateFormat = 'Y年m月d日';
        vp.headerConfig.top.renderer = function(start, end, cfg) {
            var w = start.getWeekOfYear();
            return 'w.' + ((w < 10) ? '0' : '') + w + ' ' + Sch.util.Date.getShortMonthName(start.getMonth()) + ' ' + start.getFullYear();
        };
    } 

    vp = M.getPreset("weekAndDay");
    if (vp) {
        vp.displayDateFormat = 'm月d日';
        vp.headerConfig.bottom.dateFormat = 'm月d日';
        vp.headerConfig.middle.dateFormat = 'Y年m月d日';
    }
    
    vp = M.getPreset("weekAndDayLetter");
    if (vp) {
        vp.displayDateFormat = 'm月d日';
        vp.headerConfig.bottom.dateFormat = 'M月d日';
        vp.headerConfig.bottom.renderer = function(start, end, cfg) {
        	//将星期一变为一
        	var week = Ext.Date.dayNames[start.getDay()];
        	if(week.length > 1){
        		week = week.substr(2, 1);
        	}
        	return week;	
//        	return Ext.Date.dayNames[start.getDay()].substr(2, 1);
        };
        vp.headerConfig.middle.dateFormat = 'Y年m月d日';
    }

    vp = M.getPreset("weekAndMonth");
    if (vp) {
        vp.displayDateFormat = 'Y年m月d日';
        vp.headerConfig.middle.dateFormat = 'm月d日';
        vp.headerConfig.middle.renderer = function(start, end, cfg) {
        	cfg.align = 'left';
            return Ext.Date.format(start, 'm月d日');
        };
        vp.headerConfig.top.dateFormat = 'Y年m月d日';
        
    } 

    vp = M.getPreset("monthAndYear");
    if (vp) {
        vp.displayDateFormat = 'Y年m月d日';
        vp.headerConfig.middle.dateFormat = 'Y年m月';
        vp.headerConfig.top.dateFormat = 'Y年';
    } 

    vp = M.getPreset("year");
    if (vp.year) {
        vp.displayDateFormat = 'Y年m月d日';
        vp.headerConfig.bottom.renderer = function(start, end, cfg) {
            return Ext.String.format('{0}季度', Math.floor(start.getMonth() / 3) + 1);
        };
        vp.headerConfig.middle.dateFormat = 'Y年';
    } 

})();