
/*mxStencilRegistry.packages = [];

// Extends the default stencil registry to add dynamic loading
mxStencilRegistry.getStencil = function(name)
{
	var result = mxStencilRegistry.stencils[name];
	
	if (result == null)
	{
		var basename = mxStencilRegistry.getBasenameForStencil(name);
		
		// Loads stencil files and tries again
		if (basename != null)
		{
			mxStencilRegistry.loadStencilSet(STENCIL_PATH + '/' + basename + '.xml', null);
			result = mxStencilRegistry.stencils[name];
		}
	}
	
	return result;
};

// Returns the basename for the given stencil or null if no file must be
// loaded to render the given stencil.
mxStencilRegistry.getBasenameForStencil = function(name)
{
	var parts = name.split('.');
	var tmp = null;
	
	if (parts.length > 0 && parts[0] == 'mxgraph')
	{
		tmp = parts[1];
		
		for (var i = 2; i < parts.length - 1; i++)
		{
			tmp += '/' + parts[i];
		}
	}

	return tmp;
};

// Loads the given stencil set
mxStencilRegistry.loadStencilSet = function(stencilFile, postStencilLoad, force)
{
	force = (force != null) ? force : false;
	
	// Uses additional cache for detecting previous load attempts
	var installed = mxStencilRegistry.packages[stencilFile] != null;
	
	if (force || !installed)
	{
		mxStencilRegistry.packages[stencilFile] = 1;
		var req = mxUtils.load(stencilFile);
		mxStencilRegistry.parseStencilSet(req.getXml(), postStencilLoad, !installed);
	}
};

// Parses the given stencil set
mxStencilRegistry.parseStencilSet = function(xmlDocument, postStencilLoad, install)
{
	install = (install != null) ? install : true;
	var root = xmlDocument.documentElement;
	var shape = root.firstChild;
	var packageName = '';
	var name = root.getAttribute('name');
	
	if (name != null)
	{
		packageName = name + '.';
	}
	
	while (shape != null)
	{
		if (shape.nodeType == mxConstants.NODETYPE_ELEMENT)
		{
			name = shape.getAttribute('name');
			
			if (name != null)
			{
				var w = shape.getAttribute('w');
				var h = shape.getAttribute('h');
				
				w = (w == null) ? 80 : parseInt(w, 10);
				h = (h == null) ? 80 : parseInt(h, 10);
				
				packageName = packageName.toLowerCase();
				var stencilName = name.replace(/ /g,"_");
					
				if (install)
				{
					mxStencilRegistry.addStencil(packageName + stencilName.toLowerCase(), new mxStencil(shape));
				}

				if (postStencilLoad != null)
				{
					postStencilLoad(packageName, stencilName, name, w, h);
				}
			}
		}
		
		shape = shape.nextSibling;
	}
};
mxGraph.prototype.validationAlert = function(message)
{
	if(this.errorAlert != false){
		mxUtils.alert(message);
	}
};

//粘贴方法
mxClipboard.paste = function(graph)
	{
		if (mxClipboard.cells != null)
		{
			var cells = graph.getImportableCells(mxClipboard.cells);
			var delta = mxClipboard.insertCount * mxClipboard.STEPSIZE;
			var parent = graph.getDefaultParent();
			cells = graph.importCells(cells, delta, delta, parent);
			if(mxClipboard.insertCount == 0){//如果是剪切，清空cells
				mxClipboard.cells=null;
			}
			// Increments the counter and selects the inserted cells
			mxClipboard.insertCount++;
			//自定义的粘贴方法
			if(graph.doPaste){
				graph.doPaste(cells);
			}
			graph.setSelectionCells(cells);
		}
	}

mxGraph.prototype.convertValueToString = function(cell)
{
		return cell.getAttribute('label');
};
mxGraph.prototype.cellLabelChanged = function(cell, newValue, autoSize)
{
	if (mxUtils.isNode(cell.value)){
	    // Clones the value for correct undo/redo
	    var elt = cell.value.cloneNode(true);
	    elt.setAttribute('label', newValue);
	    newValue = elt;
	}

	this.model.beginUpdate();
	try
	{
		this.model.setValue(cell, newValue);
		
		if (autoSize)
		{
			this.cellSizeUpdated(cell, false);
		}
	}
	finally
	{
		this.model.endUpdate();
	}
};*/

//粘贴方法
mxClipboard.paste = function(graph){
	var cells = null;
	
	if (!mxClipboard.isEmpty())
	{
		cells = graph.getImportableCells(mxClipboard.getCells());
		var delta = mxClipboard.insertCount * mxClipboard.STEPSIZE;
		var parent = graph.getDefaultParent();
		cells = graph.importCells(cells, delta, delta, parent);
		if(mxClipboard.insertCount == 0){//如果是剪切，清空cells
			mxClipboard.cells=null;
		}
		// Increments the counter and selects the inserted cells
		mxClipboard.insertCount++;
			//自定义的粘贴方法
			if(graph.doPaste){
				graph.doPaste(cells);
			}
		graph.setSelectionCells(cells);
	}
	
	return cells;
}
/**
 * 获得所有元素（显示的节点）
 * @param {Boolean} vertices 节点
 * @param {Boolean} edges 线
 * @param {Object} parent 父节点
 * @param {Boolean} all 全部元素，包括隐藏的;默认只获取显示的节点
 * @return {Array}
 */
mxGraph.prototype.getAllCells = function(vertices, edges, parent,all)
{
	parent = parent || this.getDefaultParent();
	
	var filter = mxUtils.bind(this, function(cell)
	{
		all = all || false;
		var flag = this.model.getChildCount(cell) == 0 &&
			((this.model.isVertex(cell) && vertices) ||
			(this.model.isEdge(cell) && edges));
		//只去显示的节点
		if(!all){
			flag = flag && this.view.getState(cell) != null;
		}
		return flag;
//		return this.view.getState(cell) != null &&
//			this.model.getChildCount(cell) == 0 &&
//			((this.model.isVertex(cell) && vertices) ||
//			(this.model.isEdge(cell) && edges));
	});
	var cells = this.model.filterDescendants(filter, parent);
	return cells;
};
/**
 * 设置节点的显隐
 * @param {Object/Array} cells
 * @param {Boolean} visible
 */
mxGraph.prototype.setCellsVisible = function(cells,visible){
	var graph = this,model = graph.model;
	if(cells == undefined || cells == null){return;}
	model.beginUpdate();
	try
	{
		if(toString.call(cells) !== '[object Array]'){
			cells = [cells];		
		}
		for(var i=0;i<cells.length;i++){
			model.setVisible(cells[i], visible);
		}
		graph.view.invalidate();
	}
	finally
	{
		model.endUpdate();
	}
}
mxUtils.findNode= function(node, attr, value)
	{
		var tmp = node.getAttribute(attr);
		if (tmp != null && tmp.replaceAll('\n','') == value)
		{
			return node;
		}
		
		node = node.firstChild;
		
		while (node != null)
		{
			var result = mxUtils.findNode(node, attr, value);
			
			if (result != null)
			{
				return result;
			}
			
			node = node.nextSibling;
		}
		
		return null;
	};
//获得样式值
mxUtils.getStyleObj = function(style){
	if(!style || !style.split)return {};
	var sts = style.split(';');
	var obj = {};
	for( var i =0; i<sts.length;i++){
		if(sts[i] != ''){
			var kv = sts[i].split('=');
			obj[kv[0]] = kv[1] || true;
		}
	}
	return obj;
}
mxStylesheet.prototype.getCellStyle = function(name, defaultStyle)
{
	var style = defaultStyle;
	
	if (name != null && name.length > 0)
	{
		var pairs = name.split(';');

		if (style != null &&
			name.charAt(0) != ';')
		{
			style = mxUtils.clone(style);
		}
		else
		{
			style = new Object();
		}

		// Parses each key, value pair into the existing style
	 	for (var i = 0; i < pairs.length; i++)
	 	{
	 		var tmp = pairs[i];
	 		var pos = tmp.indexOf('=');
	 		
	 		if (pos >= 0)
	 		{
		 		var key = tmp.substring(0, pos);
		 		var value = tmp.substring(pos + 1);

		 		if (value == mxConstants.NONE)
		 		{
		 			delete style[key];
		 		}
		 		else if (mxUtils.isNumeric(value))
		 		{
		 			style[key] = parseFloat(value);
		 		}
		 		else
		 		{
					//处理图片
					if(value.indexOf('data:image') == 0){
						i++;
						value += ';'+pairs[i];
					}
			 		style[key] = value;
		 		}
			}
	 		else
	 		{
	 			// Merges the entries from a named style
				var tmpStyle = this.styles[tmp];
				
				if (tmpStyle != null)
				{
					for (var key in tmpStyle)
					{
						style[key] = tmpStyle[key];
					}
				}
	 		}
		}
	}
	
	return style;
};