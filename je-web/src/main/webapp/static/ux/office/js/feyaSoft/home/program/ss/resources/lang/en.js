Ext.ns('feyaSoft.ss');

feyaSoft.ss.lang = {
	'greaterThan' : 'Greater than',
	'lessThan': 'Less than',
	'isEqualTo': 'Is equal to',
	'isNotEqualTo': 'Is not equal to',
	'isBetween': 'Is between',
	'isNotBetween': 'Is not between',
	'setConditionFormat': 'Set conditional format',
	'textContains': 'Text contains',	
	'textNotContains': 'Text not contains',
	'textExactly': 'Text is exactly',
	'cellEmpty': 'Cell is empty',
	'noConditionFormat': 'no conditional format in current tab',
	'fixInvalid': 'Please fix the invalid items first',
	'text': 'Text',
	'background': 'Background',
	'condition': 'Condition',	
	'conditionFormat': 'Conditional format',
	'copySheet': 'Copying sheet',
	'loadSheet': 'Loading sheet',
    'insertFormula': 'Insert Formula',
    'cancelFilter': 'Cancel filter',
    'hideRow' : 'Hide row(s)',
    'unhideRow' : 'Unhide row(s)',
        'formula' : 'Formula',
        'nameExisted': 'This name is already existed.',
        'likeReferenceRange': 'It can\'t be evaluated as a range in either A1 or R1C1 syntax.',
        'reservedWord': 'This word is a reserve formula',
        'long250': 'It must be greater than zero characters, but less than 250 characters.',
        'letterNumber': 'A range name can only contain letters, numbers, and underscores. It can\'t contain any spaces and can not start with number.',
        'hint': 'Hint',
        'names': 'Names',
	'ok': 'OK',
	'add': 'Add',
	'close': 'Close',
	'cancel': 'Cancel',
	'delete': 'Delete',
	'nameRange': 'Input a name for the range',	
	'nameManager': 'Range Name Manager',
	'markRange': 'Name the selection',
	'internetError': 'Internet error',
	'clearAll': 'Clear all',
	'merge_cell': 'Merge cell',
	'cancel_merge': 'Cancel merge',
    'can_not_change_combine': 'Can not change the combined cell!',
     'paste': 'Paste',
    'moneyFormat':'Money Format',
    'percentFormat':'Percent Format',
    'commaFormat':'Format with Comma',
    'moveLeftFormat': 'Increase Decimal',
    'moveRightFormat': 'Decrease Decimal',
    'dateFormat':'Date Format',

	'canNotChangePartMergedCell':'To process this action, the selection need included the whole merged cells',	
	'horizontally': 'Horizontally',
	'vertically': 'Vertically',
	'combineRanges': 'Combine ranges',
	'deletePicture': 'Delete picture',
	'insertPicture': 'Insert picture',
	'removeFilter': 'Remove filter',
	'expand_row_group': 'Expand row group',
	'collapse_row_group': 'Collapse row group',
	'expand_col_group': 'Expand column group',
	'collapse_col_group': 'Collapse column group',
    'change_dot_len' : 'Change dot digital number',
    'cancelGroup': 'Cancel group',
    'dataRange': 'Data Range: Area',
    'unfreezeGrid': 'Unfreeze',
	'addGroup': 'Add group',
	'nameInvalid': 'There is invalid char in the name, please check out, no empty space or any one of "\,+-*/%()[]{}" should included.',
	'nameDuplicated': 'The name of sheet can not be duplicated.',	
    'chartWizard': 'Chart Wizard',
    'bar': 'Bar',
    'pie':'Pie',
    'bubble':'Bubble',
    'xyscatter':'XY(Scatter)',
    'line':'Line',
    'area':'Area',
    'title':'Title',
    'subtitle':'SubTitle',
    'xAixs':'X axis',
    'yAixs':'Y axis',
    'zAixs':'Z axis',
    'displayLegend': 'Display legend',
    'chartElement': 'Chart Elements',
    'chartTitle': 'Change titles, legend, and grid settings',
    'displayGrid': 'Display grids',
    'left':'Left',
    'right':'Right',
    'top':'Top',
    'bottom':'Bottom',
    'normal': 'Normal',
    'percentStacked': 'Percent Stacked',
    'stacked': 'Stacked',
    'exploded':'Exploded',
    'donut':'Donut',
    'explodedDonut': 'Exploded Donut',
    'lineOnly': 'Line Only',
    'pointOnly':'Point Only',
    'pointLine':'Points and Lines',
    '3D':'3D',
    '3DLook':'3D Look',
    'dataSeries':'Data series',
    'dataRanges':'Data Ranges',
    'customizeDataForseries' : 'Customize data ranges for individual data series',
    'dataSeriesRows' :'Data series in rows',
    'dataSeriesCols' :'Data series in columns',
    'firstRowLabel':'First row as label',
    'firstColumnLabel':'First column as label',
    'chooseDataRange':'Choose a data range',
    'smoothlines':'Smooth lines',
    'chartType':'Chart Type',
    'chooseChartType':'Choose a chart type',
    'dataRange':'Data Range',
    'categories':'Categories',
    'rangeforName':'Range for Name',

        'useRightTimeFormat': 'Please use right time format, such as:',
        'noHeadFoot2Show': 'There is no Head and Foot for this Tab',
        'editHeadFoot': 'Edit Head & Foot',
        'showHeadFoot': 'Show Head & Foot',
        'hideHeadFoot': 'Hide Head & Foot',
        'header_footer': 'Header & Footer',
        'notGridLine': 'Not show gridlines in the output',       
        'printDesc': '<span style="padding: 0 0 0 10px; font: bold 14px Arial, sans-serif; ">Print Settings</span><ul style="padding:8px 0 0 20px;"><li>The result will be opened in PDF or HTML format in the new browser tab.</li><li><b>Note</b>: Only current sheet in this spreadsheet will be processed.</li></ul>',
        'printSheet': 'Print this sheet',
        'noPermissionAction' : 'You do not have permission to execute this action or this file is locked',
    	'saveChanges' : 'Save changed',
    	'saved' : 'Saved',
    	'fail2Save' : 'Fail to save',
        'columnWidth': 'Column Width',
        'rowHeight': 'Row Height',
        'clearSelection': 'Clear Selection',
    	'incell':'In-cell',
    	'paste_value_style':'Paste Value and Style',
    	'only_paste_value':'Only Paste Value',
    	'only_paste_style':'Only Paste Style',
    	'only_paste_content':'Only Paste Content',
    	'transpose_paste':'Transpose Paste',
    	'findFilter':'Find & Filter',
    	'filtering':'Filtering',
    	'createFilter':'Create filter',
    	'wholeSheet':'whole sheet',
    	'row':'Row',
    	'column':'Column',
    	'for':'for',
    	'changeFormat':'Change format setting',
    	'asc':'Asc',
    	'desc':'Desc',
    	'clearFilter':'Clear filter',
    	'canNotSortMerge':'Can not sort range contains combined cell(s)!',
    	'selectAll':'Select all',
    	'no_data_in_selection':'Please select the cell fields before apply filter.',
    	'filters':'Filters',
    	'filter_tip':'Create auto filter for selection',
    	'asc_tip':'Sort Ascending',
    	'desc_tip':'Sort Descending',
    	'copyCell':'Copy cells',
    	'fillStyleContent':'Fill with Style and Content',
    	'fillSequence': 'Fill Sequence',
    	'fillOnlyStyle':'Only fill Style',
    	'fillOnlyContent':'Fill without Style',
    	'target':'Target',
    	'all':'All',
    	'with':'with',
    	'at':'at',
    	'in':'in',
    	'replaceWith':'Replace With',
    	'find':'Find',
    	'replace':'Replace',
    	'replaceNext':'Replace Next',
    	'replaceAll':'Replace All',
    	'findAll':'Find All',
    	'findLast':'Find Last',
    	'findNext':'Find Next',
        'formulaError':'Formula Error',
    	'bracketError':'The brackets in this formula are not match, do you want to change to ',
        'deleteRows': 'Delete Row(s)',
        'deleteColumns': 'Delete Column(s)',
        'exportExcel2007': 'Export MS Excel 2007 (.xlsx)',
        'exportExcel': 'Export MS Excel 2003 (.xls)',
        'exportCSV': 'Export CSV (.csv)',
        'exportPDF': 'Export PDF (.pdf)',
        'exportHTML': 'Export HTML (.html)',
        'freezeGrid': 'Freeze grid',
        'cancelFreeze': 'Cancel freeze',
        'freeze_first_row': 'Freeze first row',
        'freeze_first_col': 'Freeze first column',
        'grid': 'Grid',
        'hideGridLine': 'Hide grid line',
        'showGridLine': 'Show grid line',
        'insertRowAbove': 'Insert Row(s) Above',
        'insertRowBelow': 'Insert Row(s) Below',
        'insertColBefore': 'Insert Col(s) Before',
        'insertColAfter': 'Insert Col(s) After',
        'inCellImage': 'In-cell Image',
        'importExcel': 'Import File',
        'linkImage': 'Link an Image',
        'rowColumn': 'Row & Column',
        'remove_format' : 'Remove format',
        'selectImage': 'Select an Image',
        'splitGrid': 'Split grid',
        'splitCell': 'Split Cell',
        'combineCells': 'Combine Cells',
        'cancelSplit': 'Cancel Split',
        'formatBrushAction': 'Format Brush',
        'commentAction': 'Comment',
        'freezeAction':'Freeze grid',
        'unfreezeAction':'Cancel freeze grid',
        'undo':'Undo',
        'redo':'Redo',
        'no_action':'No action',
        'step':'step',
        'steps':'steps',
        'regular':'Regular',
        'number':'Number',
        'percent':'Percent',
        'money':'Money',
        'date':'Date',
        'time':'Time',
        'science':'Science',
        'text':'Text',
        'rmb':'RMB (Chinese)',
        'usdollar':'Dollar (US)',
        'euro':'Euro (Europe)',
        'pound':'Pound (English)',
        'insertCellImage': 'Insert In-cell Image',
        'hyperlink':'Hyperlink',
        'displayText':'Display Text',
        'webPage':'Web Page',
        'documentLocation':'Document Location',
        "insertFunction":"Insert Function",
         //formula function type
        "spreadSheet":"Spreadsheet",
        "lookup":"Lookup",
        "statistical":"Statistical",
        "string":"String",
        "logic":"Logical",
        "numeric":"Mathematical",
        "finicial":"Financial",
		"info":"Information",
        "cannotAutoFill":"One time the max autofill rows/columns is 50.",
        'action_comment':function(cell){
            return 'Comment at '+cell;
        },
        'action_delete_comment':function(cell){
            return 'Delete Comment at '+cell;
        },
        'action_clear':function(sc, ec){
            return 'Clear '+sc+':'+ec;
        },
        'action_clear_format':function(sc, ec){
            return 'Clear format '+sc+':'+ec;
        },
        'action_input_cell':function(data, cell){
            return 'Input "'+data+'" at '+cell;
        },
        'action_freeze_change':function(cell){
            return 'Freeze at '+cell;
        },
        'action_row_resize':function(minx, maxx){
            if(minx == maxx){
                return 'Resize Row '+minx;
            }else{
                return 'Resize Row '+minx+':'+'Row '+maxx;
            }
        },
        'action_col_resize':function(miny, maxy){
            if(miny == maxy){
                return 'Resize Column '+miny;
            }else{
                return 'Resize Column '+miny+':'+'Column '+maxy;
            }
        },
        'action_row_hide':function(minx, maxx){
            if(minx == maxx){
                return 'Hide Row '+minx;
            }else{
                return 'Hide Row '+minx+':'+'Row '+maxx;
            }
        },
        'action_col_hide':function(miny, maxy){
            if(miny == maxy){
                return 'Hide Column '+miny;
            }else{
                return 'Hide Column '+miny+':'+'Column '+maxy;
            }
        },
        'action_row_show':function(minx, maxx){
            if(minx == maxx){
                return 'Show Row '+minx;
            }else{
                return 'Show Row '+minx+':'+'Row '+maxx;
            }
        },
        'action_col_show':function(miny, maxy){
            if(miny == maxy){
                return 'Show Column '+miny;
            }else{
                return 'Show Column '+miny+':'+'Column '+maxy;
            }
        },
        'action_fit_image_cell':function(cell){
            return 'Change '+cell+' to fit in-cell image';
        },
        'action_split_grid':'Split grid',
        'action_fromat_brush':function(s, e){
            return 'Format brush on '+s+':'+e;
        },
        'action_paste':'Paste',
        'action_incell_image':function(cell){
            return 'Insert in-cell image at '+cell;
        },
        'action_apply_function':function(cell){
            return 'Calculate at '+cell;
        },
        'action_change_background':'Change background',
        'action_change_attribute':'Change attribute',
        'action_sort_asc':'Sort ascending',
        'action_sort_desc':'Sort descending',
        'action_border_all':'Border all',
        'action_border_out':'Border outside',
        'action_border_in':'Border inside',
        'action_border_none':'Border none',
        'action_border_left':'Border left',
        'action_border_right':'Border right',
        'action_border_top':'Border top',
        'action_border_bottom':'Border bottom',
        'action_insert_row_above':function(x){
            return 'Insert row above Row '+x;
        },
        'action_insert_row_below':function(x){
            return 'Insert row below Row '+x;
        },
        'action_insert_col_before':function(y){
            return 'Insert column before Column '+y;
        },
        'action_insert_col_after':function(y){
            return 'Insert column after Column '+y;
        },
        'action_delete_row':function(x){
            return 'Delete Row '+x;
        },
        'action_delete_col':function(y){
            return 'Delete Column '+y;
        },
        'action_hyperlink':function(cell){
            return 'Edit Hyperlink '+cell;
        },
        'action_delete_hyperlink':function(cell){
            return 'Remove Hyperlink '+cell;
        }
};
