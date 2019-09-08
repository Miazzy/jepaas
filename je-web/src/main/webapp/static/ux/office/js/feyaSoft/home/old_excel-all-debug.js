// 1号(原4)
// 2号(原6)
Ext.ns("feyaSoft.home");
feyaSoft.home.CONST = {
	BLANK_IMAGE_URL : "/static/ux/office/js/extjs/resources/images/default/s.gif",
	userLogo : "/static/ux/office/upload/user.gif",
	groupLogo : "/static/ux/office/upload/group.png",
	orgLogo : "/static/ux/office/upload/dep64.png",
	companyLogo : "/static/ux/office/images/icons/64px/company64.png"
};
// 3号(原7)
Ext.ns("feyaSoft.home");
feyaSoft.home.Login = function(config) {
	this.activeId = 0;
	this.normalLogin = new feyaSoft.home.NormalLogin(config);
	this.authenticateLogin = new feyaSoft.home.AuthenticateLogin(config);
	feyaSoft.home.Login.superclass.constructor.call(this, {
				title : feyaSoft.lang.profile.sessionExp,
				width : 550,
				height : 150,
				bodyStyle : "padding:10px;",
				buttonAlign : "center",
				closable : true,
				resizable : false,
				modal : true,
				layout : "card",
				border : true,
				activeItem : 0,
				layoutConfig : {
					animate : false,
					deferredRender : true
				},
				items : [this.normalLogin, this.authenticateLogin],
				keys : {
					key : [13],
					fn : this.loginFn,
					scope : this
				},
				buttons : [{
							text : "Cancel",
							handler : function() {
								this.close();
							},
							scope : this
						}, {
							text : "Login",
							handler : this.loginFn,
							scope : this
						}]
			});
	this.show();
	this.on("afterlayout", this.onAfterlayoutFn, this, {
				single : true
			});
};
Ext.extend(feyaSoft.home.Login, Ext.Window, {
			onAfterlayoutFn : function() {
				Ext.Ajax.request({
							url : "/static/ux/office/cubeDriveSetting/loadSetting",
							success : function(result, options) {
								var jsonData = Ext.util.JSON
										.decode(result.responseText);
								if (jsonData.success == "true"
										&& jsonData.data.isRadius) {
									this.activeId = 1;
									this
											.getLayout()
											.setActiveItem(this.authenticateLogin);
								}
								if (Ext.util.Common.readCookie("crb") != null
										&& Ext.util.Common.readCookie("crb") == "1") {
									this.username.setValue(Ext.util.Common
											.readCookie("cur"));
								}
							},
							failure : function(response, options) {
							},
							scope : this
						});
			},
			loginFn : function() {
				if (this.activeId == 0) {
					this.normalLogin.loginFn();
				} else {
					this.authenticateLogin.loginFn();
				}
			}
		});
feyaSoft.home.NormalLogin = function(config) {
	var hideValue = new Ext.form.Hidden({
				id : "spring-security-redirect",
				value : "/static/ux/office/login/ajaxSuccess"
			});
	var username = new Ext.form.TextField({
				name : "j_username",
				allowBlank : false,
				fieldLabel : "Username",
				blankText : "Please enter your email address",
				emptyText : "Please enter your email address",
				vtype : "email",
				minLength : 4,
				anchor : "90%"
			});
	var password = new Ext.form.TextField({
				fieldLabel : "Password",
				allowBlank : false,
				inputType : "password",
				name : "j_password",
				minLength : 2,
				anchor : "90%"
			});
	feyaSoft.home.NormalLogin.superclass.constructor.call(this, {
				baseCls : "x-plain",
				width : 450,
				frame : false,
				defaultType : "textfield",
				url : "j_spring_security_check",
				items : [hideValue, username, password]
			});
};
Ext.extend(feyaSoft.home.NormalLogin, Ext.form.FormPanel, {
	loginFn : function() {
		if (this.form.isValid()) {
			this.form.submit({
						waitMsg : "In processing",
						method : "POST",
						failure : function(form, action) {
							Ext.Msg.alert(feyaSoft.lang.common.error,
									feyaSoft.lang.profile.userPassNotMatch);
						},
						success : function(form, action) {
							if (action.result.success == "true") {
								Ext.Msg.alert(feyaSoft.lang.common.ok,
										feyaSoft.lang.profile.loginOkRetry);
								this.ownerCt.close();
							} else {
								Ext.Msg.alert(feyaSoft.lang.common.error,
										feyaSoft.lang.profile.userPassNotMatch);
							}
						},
						scope : this
					});
		} else {
			Ext.MessageBox.alert("Errors", "Please fix the errors noted.");
		}
	}
});
feyaSoft.home.AuthenticateLogin = function(config) {
	var hideValue = new Ext.form.Hidden({
				id : "returnType",
				value : "ajaxCall"
			});
	this.username = new Ext.form.TextField({
				name : "j_username",
				allowBlank : false,
				fieldLabel : "Username",
				blankText : "Please enter centralized authentication user name",
				emptyText : "Please enter centralized authentication user name",
				minLength : 2,
				anchor : "90%"
			});
	var password = new Ext.form.TextField({
				fieldLabel : "Password",
				allowBlank : false,
				inputType : "password",
				name : "j_password",
				minLength : 2,
				anchor : "90%"
			});
	feyaSoft.home.AuthenticateLogin.superclass.constructor.call(this, {
				baseCls : "x-plain",
				frame : false,
				defaultType : "textfield",
				url : "/static/ux/office/login/radiusAuthenticate",
				items : [hideValue, this.username, password]
			});
};
Ext.extend(feyaSoft.home.AuthenticateLogin, Ext.form.FormPanel, {
	loginFn : function() {
		if (this.form.isValid()) {
			this.form.submit({
						waitMsg : "In processing",
						method : "POST",
						failure : function(form, action) {
							Ext.Msg.alert(feyaSoft.lang.common.error,
									feyaSoft.lang.profile.userPassNotMatch);
						},
						success : function(form, action) {
							if (action.result.success == "true") {
								Ext.Msg.alert(feyaSoft.lang.common.ok,
										feyaSoft.lang.profile.loginOkRetry);
								this.ownerCt.close();
							} else {
								Ext.Msg.alert(feyaSoft.lang.common.error,
										feyaSoft.lang.profile.userPassNotMatch);
							}
						},
						scope : this
					});
		} else {
			Ext.MessageBox.alert("Errors", "Please fix the errors noted.");
		}
	}
});
feyaSoft.home.LoginCheck = function(callBackFn, scope) {
	Ext.Ajax.request({
				url : "/static/ux/office/spreadsheet/isAccess.json",
				success : function(result, request) {
					var jsonData = Ext.util.JSON.decode(result.responseText);
					if (jsonData.success == "true") {
						callBackFn.call(scope);
					} else {
						new feyaSoft.home.Login;
					}
				},
				failure : function(result, request) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.errorInternal);
				},
				scope : scope
			});
};
// 4号(原8)
Ext.ux.TabCloseMenu = Ext.extend(Object, {
	closeTabText : "Close Tab",
	closeOtherTabsText : "Close Other Tabs",
	showCloseAll : true,
	closeAllTabsText : "Close All Tabs",
	constructor : function(config) {
		Ext.apply(this, config || {});
	},
	init : function(tabs) {
		this.tabs = tabs;
		tabs.on({
					scope : this,
					contextmenu : this.onContextMenu,
					destroy : this.destroy
				});
	},
	destroy : function() {
		Ext.destroy(this.menu);
		delete this.menu;
		delete this.tabs;
		delete this.active;
	},
	onContextMenu : function(tabs, item, e) {
		this.active = item;
		var m = this.createMenu(), disableAll = true, disableOthers = true, closeAll = m
				.getComponent("closeall");
		m.getComponent("close").setDisabled(!item.closable);
		tabs.items.each(function() {
					if (this.closable) {
						disableAll = false;
						if (this != item) {
							disableOthers = false;
							return false;
						}
					}
				});
		m.getComponent("closeothers").setDisabled(disableOthers);
		if (closeAll) {
			closeAll.setDisabled(disableAll);
		}
		e.stopEvent();
		m.showAt(e.getPoint());
	},
	createMenu : function() {
		if (!this.menu) {
			var items = [{
						itemId : "close",
						text : this.closeTabText,
						scope : this,
						handler : this.onClose
					}];
			if (this.showCloseAll) {
				items.push("-");
			}
			items.push({
						itemId : "closeothers",
						text : this.closeOtherTabsText,
						scope : this,
						handler : this.onCloseOthers
					});
			if (this.showCloseAll) {
				items.push({
							itemId : "closeall",
							text : this.closeAllTabsText,
							scope : this,
							handler : this.onCloseAll
						});
			}
			this.menu = new Ext.menu.Menu({
						items : items
					});
		}
		return this.menu;
	},
	onClose : function() {
		this.tabs.remove(this.active);
	},
	onCloseOthers : function() {
		this.doClose(true);
	},
	onCloseAll : function() {
		this.doClose(false);
	},
	doClose : function(excludeActive) {
		var items = [];
		this.tabs.items.each(function(item) {
					if (item.closable) {
						if (!excludeActive || item != this.active) {
							items.push(item);
						}
					}
				}, this);
		Ext.each(items, function(item) {
					this.tabs.remove(item);
				}, this);
	}
});
Ext.preg("tabclosemenu", Ext.ux.TabCloseMenu);
// 5号(原9)
Ext.ux.GridKeyNav = function() {
};
Ext.ux.GridKeyNav.prototype = {
	bottomBar : true,
	toolbar : null,
	init : function(grid) {
		this.grid = grid;
		this.grid.on({
					render : this.onRender,
					destroy : this.onDestroy,
					scope : this
				});
	},
	onRender : function() {
		this.nav = new Ext.KeyNav(this.grid.getGridEl(), {
					left : this.pagingNav.createDelegate(this, ["prev"]),
					right : this.pagingNav.createDelegate(this, ["next"]),
					pageDown : this.pagingNav.createDelegate(this, ["next"]),
					pageUp : this.pagingNav.createDelegate(this, ["prev"]),
					home : this.pagingNav.createDelegate(this, ["first"]),
					end : this.pagingNav.createDelegate(this, ["last"])
				});
		this.pt = this.toolbar
				|| (this.bottomBar ? this.grid.getBottomToolbar() : this.grid
						.getTopToolbar());
		this.sm = this.grid.getSelectionModel();
		this.sm.selectNext = this.sm.selectNext.createInterceptor(
				this.beforeNext, this);
		this.sm.selectPrevious = this.sm.selectPrevious.createInterceptor(
				this.beforePrevious, this);
	},
	onDestroy : function() {
		this.nav.disable();
		delete this.nav;
	},
	pagingNav : function(page) {
		if (!this.pt[page].disabled) {
			this.pt.onClick(this.pt[page]);
			if (page === "last") {
				this.forceSelectLastRow();
			}
		} else {
			if (this.pt.pageSize >= this.grid.getStore().data.length) {
				if (page == "first") {
					this.sm.selectFirstRow();
				} else if (page == "last") {
					this.sm.selectLastRow();
				}
			}
		}
	},
	beforeNext : function() {
		if (!this.sm.hasNext()) {
			this.pagingNav("next");
			return false;
		}
	},
	beforePrevious : function() {
		if (!this.sm.hasPrevious()) {
			this.pagingNav("prev");
			this.forceSelectLastRow();
			return false;
		}
	},
	forceSelectLastRow : function() {
		this.grid.getStore().on("load", function() {
					this.sm.selectLastRow();
				}, this, {
					single : true
				});
	}
};
// 6号（原10）
Ext.Message = (function() {
	var msgCt;
	return {
		msgStay : function(title, message, time) {
			Ext.Msg.show({
						msg : message,
						title : title,
						closable : true,
						modal : true,
						icon : Ext.MessageBox.INFO
					});
			setTimeout(function() {
						Ext.Msg.hide();
					}, time);
		}
	};
})();
var Runner = (function() {
	var f = function(v, pbar, btn, count, cb) {
		return function() {
			if (v > count) {
				cb();
			} else {
				var i = v / count;
			}
		};
	};
	return {
		run : function(pbar, btn, count, cb) {
			var ms = 5000 / count;
			for (var i = 1; i < count + 2; i++) {
				setTimeout(f(i, pbar, btn, count, cb), i * ms);
			}
		}
	};
})();
// 7号（原11）
Ext.form.FileUploadField = Ext.extend(Ext.form.TextField, {
			buttonText : "Browse...",
			buttonOnly : false,
			buttonOffset : 3,
			readOnly : true,
			autoSize : Ext.emptyFn,
			initComponent : function() {
				Ext.form.FileUploadField.superclass.initComponent.call(this);
				this.addEvents("fileselected");
			},
			onRender : function(ct, position) {
				Ext.form.FileUploadField.superclass.onRender.call(this, ct,
						position);
				this.wrap = this.el.wrap({
							cls : "x-form-field-wrap x-form-file-wrap"
						});
				this.el.addClass("x-form-file-text");
				this.el.dom.removeAttribute("name");
				this.fileInput = this.wrap.createChild({
							id : this.getFileInputId(),
							name : this.name || this.getId(),
							cls : "x-form-file",
							tag : "input",
							type : "file",
							size : 1
						});
				var btnCfg = Ext.applyIf(this.buttonCfg || {}, {
							text : this.buttonText
						});
				this.button = new Ext.Button(Ext.apply(btnCfg, {
							renderTo : this.wrap,
							cls : "x-form-file-btn"
									+ (btnCfg.iconCls ? " x-btn-icon" : "")
						}));
				if (this.buttonOnly) {
					this.el.hide();
					this.wrap.setWidth(this.button.getEl().getWidth());
				}
				this.fileInput.on("change", function() {
							var v = this.fileInput.dom.value;
							this.setValue(v);
							this.fireEvent("fileselected", this, v);
						}, this);
			},
			getFileInputId : function() {
				return this.id + "-file";
			},
			onResize : function(w, h) {
				Ext.form.FileUploadField.superclass.onResize.call(this, w, h);
				this.wrap.setWidth(w);
				if (!this.buttonOnly) {
					var w = this.wrap.getWidth()
							- this.button.getEl().getWidth()
							- this.buttonOffset;
					this.el.setWidth(w);
				}
			},
			preFocus : Ext.emptyFn,
			getResizeEl : function() {
				return this.wrap;
			},
			getPositionEl : function() {
				return this.wrap;
			},
			alignErrorIcon : function() {
				this.errorIcon.alignTo(this.wrap, "tl-tr", [2, 0]);
			}
		});
Ext.reg("fileuploadfield", Ext.form.FileUploadField);
// 9号 13
Ext.ux.Spinner = Ext.extend(Ext.util.Observable, {
			incrementValue : 1,
			alternateIncrementValue : 5,
			triggerClass : "x-form-spinner-trigger",
			splitterClass : "x-form-spinner-splitter",
			alternateKey : Ext.EventObject.shiftKey,
			defaultValue : 0,
			accelerate : false,
			constructor : function(config) {
				Ext.ux.Spinner.superclass.constructor.call(this, config);
				Ext.apply(this, config);
				this.mimicing = false;
			},
			init : function(field) {
				this.field = field;
				field.afterMethod("onRender", this.doRender, this);
				field.afterMethod("onEnable", this.doEnable, this);
				field.afterMethod("onDisable", this.doDisable, this);
				field.afterMethod("afterRender", this.doAfterRender, this);
				field.afterMethod("onResize", this.doResize, this);
				field.afterMethod("onFocus", this.doFocus, this);
				field.beforeMethod("onDestroy", this.doDestroy, this);
			},
			doRender : function(ct, position) {
				var el = this.el = this.field.getEl();
				var f = this.field;
				if (!f.wrap) {
					f.wrap = this.wrap = el.wrap({
								cls : "x-form-field-wrap"
							});
				} else {
					this.wrap = f.wrap.addClass("x-form-field-wrap");
				}
				this.trigger = this.wrap.createChild({
							tag : "img",
							src : Ext.BLANK_IMAGE_URL,
							cls : "x-form-trigger " + this.triggerClass
						});
				if (!f.width) {
					this.wrap.setWidth(el.getWidth() + this.trigger.getWidth());
				}
				this.splitter = this.wrap.createChild({
							tag : "div",
							cls : this.splitterClass,
							style : "width:13px; height:2px;"
						});
				this.splitter.setRight(Ext.isIE ? 1 : 2).setTop(10).show();
				this.proxy = this.trigger.createProxy("", this.splitter, true);
				this.proxy.addClass("x-form-spinner-proxy");
				this.proxy.setStyle("left", "0px");
				this.proxy.setSize(14, 1);
				this.proxy.hide();
				this.dd = new Ext.dd.DDProxy(this.splitter.dom.id,
						"SpinnerDrag", {
							dragElId : this.proxy.id
						});
				this.initTrigger();
				this.initSpinner();
			},
			doAfterRender : function() {
				var y;
				if (Ext.isIE && this.el.getY() != (y = this.trigger.getY())) {
					this.el.position();
					this.el.setY(y);
				}
			},
			doEnable : function() {
				if (this.wrap) {
					this.wrap.removeClass(this.field.disabledClass);
				}
			},
			doDisable : function() {
				if (this.wrap) {
					this.wrap.addClass(this.field.disabledClass);
					this.el.removeClass(this.field.disabledClass);
				}
			},
			doResize : function(w, h) {
				if (typeof w == "number") {
					this.el.setWidth(w - this.trigger.getWidth());
				}
				this.wrap
						.setWidth(this.el.getWidth() + this.trigger.getWidth());
			},
			doFocus : function() {
				if (!this.mimicing) {
					this.wrap.addClass("x-trigger-wrap-focus");
					this.mimicing = true;
					Ext.get(Ext.isIE ? document.body : document).on(
							"mousedown", this.mimicBlur, this, {
								delay : 10
							});
					this.el.on("keydown", this.checkTab, this);
				}
			},
			checkTab : function(e) {
				if (e.getKey() == e.TAB) {
					this.triggerBlur();
				}
			},
			mimicBlur : function(e) {
				if (!this.wrap.contains(e.target) && this.field.validateBlur(e)) {
					this.triggerBlur();
				}
			},
			triggerBlur : function() {
				this.mimicing = false;
				Ext.get(Ext.isIE ? document.body : document).un("mousedown",
						this.mimicBlur, this);
				this.el.un("keydown", this.checkTab, this);
				this.field.beforeBlur();
				this.wrap.removeClass("x-trigger-wrap-focus");
				this.field.onBlur.call(this.field);
			},
			initTrigger : function() {
				this.trigger.addClassOnOver("x-form-trigger-over");
				this.trigger.addClassOnClick("x-form-trigger-click");
			},
			initSpinner : function() {
				this.field.addEvents({
							spin : true,
							spinup : true,
							spindown : true
						});
				this.keyNav = new Ext.KeyNav(this.el, {
							up : function(e) {
								e.preventDefault();
								this.onSpinUp();
							},
							down : function(e) {
								e.preventDefault();
								this.onSpinDown();
							},
							pageUp : function(e) {
								e.preventDefault();
								this.onSpinUpAlternate();
							},
							pageDown : function(e) {
								e.preventDefault();
								this.onSpinDownAlternate();
							},
							scope : this
						});
				this.repeater = new Ext.util.ClickRepeater(this.trigger, {
							accelerate : this.accelerate
						});
				this.field.mon(this.repeater, "click", this.onTriggerClick,
						this, {
							preventDefault : true
						});
				this.field.mon(this.trigger, {
							mouseover : this.onMouseOver,
							mouseout : this.onMouseOut,
							mousemove : this.onMouseMove,
							mousedown : this.onMouseDown,
							mouseup : this.onMouseUp,
							scope : this,
							preventDefault : true
						});
				this.field.mon(this.wrap, "mousewheel", this.handleMouseWheel,
						this);
				this.dd.setXConstraint(0, 0, 10);
				this.dd.setYConstraint(1500, 1500, 10);
				this.dd.endDrag = this.endDrag.createDelegate(this);
				this.dd.startDrag = this.startDrag.createDelegate(this);
				this.dd.onDrag = this.onDrag.createDelegate(this);
			},
			onMouseOver : function() {
				if (this.disabled) {
					return;
				}
				var middle = this.getMiddle();
				this.tmpHoverClass = Ext.EventObject.getPageY() < middle
						? "x-form-spinner-overup"
						: "x-form-spinner-overdown";
				this.trigger.addClass(this.tmpHoverClass);
			},
			onMouseOut : function() {
				this.trigger.removeClass(this.tmpHoverClass);
			},
			onMouseMove : function() {
				if (this.disabled) {
					return;
				}
				var middle = this.getMiddle();
				if (Ext.EventObject.getPageY() > middle
						&& this.tmpHoverClass == "x-form-spinner-overup"
						|| Ext.EventObject.getPageY() < middle
						&& this.tmpHoverClass == "x-form-spinner-overdown") {
				}
			},
			onMouseDown : function() {
				if (this.disabled) {
					return;
				}
				var middle = this.getMiddle();
				this.tmpClickClass = Ext.EventObject.getPageY() < middle
						? "x-form-spinner-clickup"
						: "x-form-spinner-clickdown";
				this.trigger.addClass(this.tmpClickClass);
			},
			onMouseUp : function() {
				this.trigger.removeClass(this.tmpClickClass);
			},
			onTriggerClick : function() {
				if (this.disabled || this.el.dom.readOnly) {
					return;
				}
				var middle = this.getMiddle();
				var ud = Ext.EventObject.getPageY() < middle ? "Up" : "Down";
				this["onSpin" + ud]();
			},
			getMiddle : function() {
				var t = this.trigger.getTop();
				var h = this.trigger.getHeight();
				var middle = t + h / 2;
				return middle;
			},
			isSpinnable : function() {
				if (this.disabled || this.el.dom.readOnly) {
					Ext.EventObject.preventDefault();
					return false;
				}
				return true;
			},
			handleMouseWheel : function(e) {
				if (this.wrap.hasClass("x-trigger-wrap-focus") == false) {
					return;
				}
				var delta = e.getWheelDelta();
				if (delta > 0) {
					this.onSpinUp();
					e.stopEvent();
				} else if (delta < 0) {
					this.onSpinDown();
					e.stopEvent();
				}
			},
			startDrag : function() {
				this.proxy.show();
				this._previousY = Ext.fly(this.dd.getDragEl()).getTop();
			},
			endDrag : function() {
				this.proxy.hide();
			},
			onDrag : function() {
				if (this.disabled) {
					return;
				}
				var y = Ext.fly(this.dd.getDragEl()).getTop();
				var ud = "";
				if (this._previousY > y) {
					ud = "Up";
				}
				if (this._previousY < y) {
					ud = "Down";
				}
				if (ud != "") {
					this["onSpin" + ud]();
				}
				this._previousY = y;
			},
			onSpinUp : function() {
				if (this.isSpinnable() == false) {
					return;
				}
				if (Ext.EventObject.shiftKey == true) {
					this.onSpinUpAlternate();
					return;
				} else {
					this.spin(false, false);
				}
				this.field.fireEvent("spin", this);
				this.field.fireEvent("spinup", this);
			},
			onSpinDown : function() {
				if (this.isSpinnable() == false) {
					return;
				}
				if (Ext.EventObject.shiftKey == true) {
					this.onSpinDownAlternate();
					return;
				} else {
					this.spin(true, false);
				}
				this.field.fireEvent("spin", this);
				this.field.fireEvent("spindown", this);
			},
			onSpinUpAlternate : function() {
				if (this.isSpinnable() == false) {
					return;
				}
				this.spin(false, true);
				this.field.fireEvent("spin", this);
				this.field.fireEvent("spinup", this);
			},
			onSpinDownAlternate : function() {
				if (this.isSpinnable() == false) {
					return;
				}
				this.spin(true, true);
				this.field.fireEvent("spin", this);
				this.field.fireEvent("spindown", this);
			},
			spin : function(down, alternate) {
				var v = parseFloat(this.field.getValue());
				var incr = alternate == true
						? this.alternateIncrementValue
						: this.incrementValue;
				down == true ? (v -= incr) : (v += incr);
				v = isNaN(v) ? this.defaultValue : v;
				v = this.fixBoundries(v);
				this.field.setRawValue(v);
			},
			fixBoundries : function(value) {
				var v = value;
				if (this.field.minValue != undefined && v < this.field.minValue) {
					v = this.field.minValue;
				}
				if (this.field.maxValue != undefined && v > this.field.maxValue) {
					v = this.field.maxValue;
				}
				return this.fixPrecision(v);
			},
			fixPrecision : function(value) {
				var nan = isNaN(value);
				if (!this.field.allowDecimals
						|| this.field.decimalPrecision == -1 || nan || !value) {
					return nan ? "" : value;
				}
				return parseFloat(parseFloat(value)
						.toFixed(this.field.decimalPrecision));
			},
			doDestroy : function() {
				if (this.trigger) {
					this.trigger.remove();
				}
				if (this.wrap) {
					this.wrap.remove();
					delete this.field.wrap;
				}
				if (this.splitter) {
					this.splitter.remove();
				}
				if (this.dd) {
					this.dd.unreg();
					this.dd = null;
				}
				if (this.proxy) {
					this.proxy.remove();
				}
				if (this.repeater) {
					this.repeater.purgeListeners();
				}
				if (this.mimicing) {
					Ext.get(Ext.isIE ? document.body : document).un(
							"mousedown", this.mimicBlur, this);
				}
			}
		});
Ext.form.Spinner = Ext.ux.Spinner;
// 10号
Ext.ns("Ext.ux.form");
Ext.ux.form.SpinnerField = Ext.extend(Ext.form.NumberField, {
	actionMode : "wrap",
	deferHeight : true,
	autoSize : Ext.emptyFn,
	onBlur : Ext.emptyFn,
	adjustSize : Ext.BoxComponent.prototype.adjustSize,
	constructor : function(config) {
		var spinnerConfig = Ext
				.copyTo(
						{},
						config,
						"incrementValue,alternateIncrementValue,accelerate,defaultValue,triggerClass,splitterClass");
		var spl = this.spinner = new Ext.ux.Spinner(spinnerConfig);
		var plugins = config.plugins ? Ext.isArray(config.plugins)
				? config.plugins.push(spl)
				: [config.plugins, spl] : spl;
		Ext.ux.form.SpinnerField.superclass.constructor.call(this, Ext.apply(
						config, {
							plugins : plugins
						}));
	},
	getResizeEl : function() {
		return this.wrap;
	},
	getPositionEl : function() {
		return this.wrap;
	},
	alignErrorIcon : function() {
		if (this.wrap) {
			this.errorIcon.alignTo(this.wrap, "tl-tr", [2, 0]);
		}
	},
	validateBlur : function() {
		return true;
	}
});
Ext.reg("spinnerfield", Ext.ux.form.SpinnerField);
Ext.form.SpinnerField = Ext.ux.form.SpinnerField;
// 11号 15
if ("function" !== typeof RegExp.escape) {
	RegExp.escape = function(s) {
		if ("string" !== typeof s) {
			return s;
		}
		return s.replace(/([.*+?^=!:${}()|[\]\/\\])/g, "\\$1");
	};
}
Ext.ns("Ext.ux.form");
Ext.ux.form.LovCombo = Ext.extend(Ext.form.ComboBox, {
			checkField : "checked",
			separator : ",",
			initComponent : function() {
				if (!this.tpl) {
					this.tpl = "<tpl for=\".\">"
							+ "<div class=\"x-combo-list-item\">"
							+ "<img src=\"" + Ext.BLANK_IMAGE_URL + "\" "
							+ "class=\"ux-lovcombo-icon ux-lovcombo-icon-"
							+ "{[values." + this.checkField
							+ "?\"checked\":\"unchecked\"" + "]}\">"
							+ "<div class=\"ux-lovcombo-item-text\">{"
							+ (this.displayField || "text") + "}</div>"
							+ "</div>" + "</tpl>";
				}
				Ext.ux.form.LovCombo.superclass.initComponent.apply(this,
						arguments);
				this.on({
							scope : this,
							beforequery : this.onBeforeQuery,
							blur : this.onRealBlur
						});
				this.onLoad = this.onLoad.createSequence(function() {
							if (this.el) {
								var v = this.el.dom.value;
								this.el.dom.value = "";
								this.el.dom.value = v;
							}
						});
			},
			initEvents : function() {
				Ext.ux.form.LovCombo.superclass.initEvents.apply(this,
						arguments);
				this.keyNav.tab = false;
			},
			onRender : function() {
				Ext.ux.form.LovCombo.superclass.onRender.apply(this, arguments);
			},
			clearValue : function() {
				this.value = "";
				this.setRawValue(this.value);
				this.store.clearFilter();
				this.store.each(function(r) {
							r.set(this.checkField, false);
						}, this);
				if (this.hiddenField) {
					this.hiddenField.value = "";
				}
				this.applyEmptyText();
			},
			getCheckedDisplay : function() {
				var re = new RegExp(this.separator, "g");
				return this.getCheckedValue(this.displayField).replace(re,
						this.separator + " ");
			},
			getCheckedValue : function(field) {
				field = field || this.valueField;
				var c = [];
				var snapshot = this.store.snapshot || this.store.data;
				snapshot.each(function(r) {
							if (r.get(this.checkField)) {
								c.push(r.get(field));
							}
						}, this);
				return c.join(this.separator);
			},
			onBeforeQuery : function(qe) {
				qe.query = qe.query.replace(new RegExp(RegExp.escape(this
								.getCheckedDisplay())
								+ "[ " + this.separator + "]*"), "");
			},
			onRealBlur : function() {
				this.list.hide();
				var rv = this.getRawValue();
				var rva = rv.split(new RegExp(RegExp.escape(this.separator)
						+ " *"));
				var va = [];
				var snapshot = this.store.snapshot || this.store.data;
				Ext.each(rva, function(v) {
							snapshot.each(function(r) {
										if (v === r.get(this.displayField)) {
											va.push(r.get(this.valueField));
										}
									}, this);
						}, this);
			},
			onSelect : function(record, index) {
				if (this.fireEvent("beforeselect", this, record, index) !== false) {
					record.set(this.checkField, !record.get(this.checkField));
					if (this.store.isFiltered()) {
						this.doQuery(this.allQuery);
					}
					this.setValue(this.getCheckedValue());
					this.fireEvent("select", this, record, index);
				}
			},
			setValue : function(v) {
				if (v) {
					v = "" + v;
					if (this.valueField) {
						this.store.clearFilter();
						this.store.each(function(r) {
									var checked = !(!v.match("(^|"
											+ this.separator
											+ ")"
											+ RegExp.escape(r
													.get(this.valueField))
											+ "(" + this.separator + "|$)"));
									r.set(this.checkField, checked);
								}, this);
						this.value = this.getCheckedValue();
						this.setRawValue(this.getCheckedDisplay());
						if (this.hiddenField) {
							this.hiddenField.value = this.value;
						}
					} else {
						this.value = v;
						this.setRawValue(v);
						if (this.hiddenField) {
							this.hiddenField.value = v;
						}
					}
					if (this.el) {
						this.el.removeClass(this.emptyClass);
					}
				} else {
					this.clearValue();
				}
			},
			selectAll : function() {
				this.store.each(function(record) {
							record.set(this.checkField, true);
						}, this);
				this.doQuery(this.allQuery);
				this.setValue(this.getCheckedValue());
			},
			deselectAll : function() {
				this.clearValue();
			},
			beforeBlur : Ext.emptyFn
		});
Ext.reg("lovcombo", Ext.ux.form.LovCombo);
// 12号 16
Ext.ns("Ext.ux.menu");
Ext.ux.menu.RangeMenu = Ext.extend(Ext.menu.Menu, {
	constructor : function(config) {
		Ext.ux.menu.RangeMenu.superclass.constructor.call(this, config);
		this.addEvents("update");
		this.updateTask = new Ext.util.DelayedTask(this.fireUpdate, this);
		var i, len, item, cfg, Cls;
		for (i = 0, len = this.menuItems.length; i < len; i++) {
			item = this.menuItems[i];
			if (item !== "-") {
				cfg = {
					itemId : "range-" + item,
					enableKeyEvents : true,
					iconCls : this.iconCls[item] || "no-icon",
					listeners : {
						scope : this,
						keyup : this.onInputKeyUp
					}
				};
				Ext.apply(cfg, Ext.applyIf(this.fields[item] || {},
								this.fieldCfg[item]), this.menuItemCfgs);
				Cls = cfg.fieldCls || this.fieldCls;
				item = this.fields[item] = new Cls(cfg);
			}
			this.add(item);
		}
	},
	fireUpdate : function() {
		this.fireEvent("update", this);
	},
	getValue : function() {
		var result = {}, key, field;
		for (key in this.fields) {
			field = this.fields[key];
			if (field.isValid() && String(field.getValue()).length > 0) {
				result[key] = field.getValue();
			}
		}
		return result;
	},
	setValue : function(data) {
		var key;
		for (key in this.fields) {
			this.fields[key].setValue(data[key] !== undefined ? data[key] : "");
		}
		this.fireEvent("update", this);
	},
	onInputKeyUp : function(field, e) {
		var k = e.getKey();
		if (k == e.RETURN && field.isValid()) {
			e.stopEvent();
			this.hide(true);
			return;
		}
		if (field == this.fields.eq) {
			if (this.fields.gt) {
				this.fields.gt.setValue(null);
			}
			if (this.fields.lt) {
				this.fields.lt.setValue(null);
			}
		} else {
			this.fields.eq.setValue(null);
		}
		this.updateTask.delay(this.updateBuffer);
	}
});
Ext.ns("Ext.ux.grid");
Ext.ux.grid.BufferView = Ext.extend(Ext.grid.GridView, {
	rowHeight : 19,
	borderHeight : 2,
	scrollDelay : 100,
	cacheSize : 20,
	cleanDelay : 500,
	initTemplates : function() {
		Ext.ux.grid.BufferView.superclass.initTemplates.call(this);
		var ts = this.templates;
		ts.rowHolder = new Ext.Template('<div class="x-grid3-row {alt}" style="{tstyle}"></div>');
		ts.rowHolder.disableFormats = true;
		ts.rowHolder.compile();
		ts.rowBody = new Ext.Template(
				'<table class="x-grid3-row-table" border="0" cellspacing="0" cellpadding="0" style="{tstyle}">',
				"<tbody><tr>{cells}</tr>",
				(this.enableRowBody
						? '<tr class="x-grid3-row-body-tr" style="{bodyStyle}"><td colspan="{cols}" class="x-grid3-body-cell" tabIndex="0" hidefocus="on"><div class="x-grid3-row-body">{body}</div></td></tr>'
						: ""), "</tbody></table>");
		ts.rowBody.disableFormats = true;
		ts.rowBody.compile()
	},
	getStyleRowHeight : function() {
		return Ext.isBorderBox
				? (this.rowHeight + this.borderHeight)
				: this.rowHeight
	},
	getCalculatedRowHeight : function() {
		return this.rowHeight + this.borderHeight
	},
	getVisibleRowCount : function() {
		var rh = this.getCalculatedRowHeight(), visibleHeight = this.scroller.dom.clientHeight;
		return (visibleHeight < 1) ? 0 : Math.ceil(visibleHeight / rh)
	},
	getVisibleRows : function() {
		var count = this.getVisibleRowCount(), sc = this.scroller.dom.scrollTop, start = (sc === 0
				? 0
				: Math.floor(sc / this.getCalculatedRowHeight()) - 1);
		return {
			first : Math.max(start, 0),
			last : Math.min(start + count + 2, this.ds.getCount() - 1)
		}
	},
	doRender : function(cs, rs, ds, startRow, colCount, stripe, onlyBody) {
		var ts = this.templates, ct = ts.cell, rt = ts.row, rb = ts.rowBody, last = colCount
				- 1, rh = this.getStyleRowHeight(), vr = this.getVisibleRows(), tstyle = "width:"
				+ this.getTotalWidth() + ";height:" + rh + "px;", buf = [], cb, c, p = {}, rp = {
			tstyle : tstyle
		}, r;
		for (var j = 0, len = rs.length; j < len; j++) {
			r = rs[j];
			cb = [];
			var rowIndex = (j + startRow), visible = rowIndex >= vr.first
					&& rowIndex <= vr.last;
			if (visible) {
				for (var i = 0; i < colCount; i++) {
					c = cs[i];
					p.id = c.id;
					p.css = i === 0 ? "x-grid3-cell-first " : (i == last
							? "x-grid3-cell-last "
							: "");
					p.attr = p.cellAttr = "";
					p.value = c.renderer(r.data[c.name], p, r, rowIndex, i, ds);
					p.style = c.style;
					if (p.value === undefined || p.value === "") {
						p.value = "&#160;"
					}
					if (r.dirty && typeof r.modified[c.name] !== "undefined") {
						p.css += " x-grid3-dirty-cell"
					}
					cb[cb.length] = ct.apply(p)
				}
			}
			var alt = [];
			if (stripe && ((rowIndex + 1) % 2 === 0)) {
				alt[0] = "x-grid3-row-alt"
			}
			if (r.dirty) {
				alt[1] = " x-grid3-dirty-row"
			}
			rp.cols = colCount;
			if (this.getRowClass) {
				alt[2] = this.getRowClass(r, rowIndex, rp, ds)
			}
			rp.alt = alt.join(" ");
			rp.cells = cb.join("");
			buf[buf.length] = !visible ? ts.rowHolder.apply(rp) : (onlyBody
					? rb.apply(rp)
					: rt.apply(rp))
		}
		return buf.join("")
	},
	isRowRendered : function(index) {
		var row = this.getRow(index);
		return row && row.childNodes.length > 0
	},
	syncScroll : function() {
		Ext.ux.grid.BufferView.superclass.syncScroll.apply(this, arguments);
		this.update()
	},
	update : function() {
		if (this.scrollDelay) {
			if (!this.renderTask) {
				this.renderTask = new Ext.util.DelayedTask(this.doUpdate, this)
			}
			this.renderTask.delay(this.scrollDelay)
		} else {
			this.doUpdate()
		}
	},
	onRemove : function(ds, record, index, isUpdate) {
		Ext.ux.grid.BufferView.superclass.onRemove.apply(this, arguments);
		if (isUpdate !== true) {
			this.update()
		}
	},
	doUpdate : function() {
		if (this.getVisibleRowCount() > 0) {
			var g = this.grid, cm = g.colModel, ds = g.store, cs = this
					.getColumnData(), vr = this.getVisibleRows(), row;
			for (var i = vr.first; i <= vr.last; i++) {
				if (!this.isRowRendered(i) && (row = this.getRow(i))) {
					var html = this.doRender(cs, [ds.getAt(i)], ds, i, cm
									.getColumnCount(), g.stripeRows, true);
					row.innerHTML = html
				}
			}
			this.clean()
		}
	},
	clean : function() {
		if (!this.cleanTask) {
			this.cleanTask = new Ext.util.DelayedTask(this.doClean, this)
		}
		this.cleanTask.delay(this.cleanDelay)
	},
	doClean : function() {
		if (this.getVisibleRowCount() > 0) {
			var vr = this.getVisibleRows();
			vr.first -= this.cacheSize;
			vr.last += this.cacheSize;
			var i = 0, rows = this.getRows();
			if (vr.first <= 0) {
				i = vr.last + 1
			}
			for (var len = this.ds.getCount(); i < len; i++) {
				if ((i < vr.first || i > vr.last) && rows[i].innerHTML) {
					rows[i].innerHTML = ""
				}
			}
		}
	},
	removeTask : function(name) {
		var task = this[name];
		if (task && task.cancel) {
			task.cancel();
			this[name] = null
		}
	},
	destroy : function() {
		this.removeTask("cleanTask");
		this.removeTask("renderTask");
		Ext.ux.grid.BufferView.superclass.destroy.call(this)
	},
	layout : function() {
		Ext.ux.grid.BufferView.superclass.layout.call(this);
		this.update()
	}
});
// 13号 17
Ext.ns("Ext.ux.form");
Ext.ux.form.SearchField = Ext.extend(Ext.form.TwinTriggerField, {
			initComponent : function() {
				Ext.ux.form.SearchField.superclass.initComponent.call(this);
				this.on("specialkey", function(f, e) {
							if (e.getKey() == e.ENTER) {
								this.onTrigger2Click();
							}
						}, this);
				this.addEvents("beforesearch", "search");
			},
			mode : "remote",
			validationEvent : false,
			validateOnBlur : false,
			trigger1Class : "x-form-clear-trigger",
			trigger2Class : "x-form-search-trigger",
			hideTrigger1 : true,
			width : 180,
			hasSearch : false,
			paramName : "query",
			onTrigger1Click : function() {
				if (this.hasSearch) {
					this.el.dom.value = "";
					if (false !== this.fireEvent("beforesearch", null,
							this.store, this)) {
						if ("local" != this.mode) {
							var o = {
								start : 0
							};
							if (this.limit) {
								o = {
									start : 0,
									limit : this.limit
								};
							}
							this.store.baseParams = this.store.baseParams || {};
							this.store.baseParams[this.paramName] = "";
							this.store.reload({
										params : o
									});
						} else {
							this.store.clearFilter();
						}
						this.fireEvent("search", null, this.store, this);
					}
					this.triggers[0].hide();
					this.hasSearch = false;
				}
			},
			onTrigger2Click : function() {
				var v = this.getRawValue();
				if (v.length < 1) {
					this.onTrigger1Click();
					return;
				}
				if (false !== this.fireEvent("beforesearch", v, this.store,
						this)) {
					if ("local" != this.mode) {
						var o = {
							start : 0
						};
						if (this.limit) {
							o = {
								start : 0,
								limit : this.limit
							};
						}
						this.store.baseParams = this.store.baseParams || {};
						this.store.baseParams[this.paramName] = v;
						this.store.reload({
									params : o
								});
					} else {
						var er = Ext.escapeRe;
						var reg = new RegExp("^" + er(String(v)), "i");
						this.store.filterBy(function(rd, id) {
									var fields = rd.fields;
									var match = false;
									fields.each(function(it) {
												if (!this.searchFields
														|| this.searchFields[it.name]) {
													if (reg
															.test(rd.data[it.name])) {
														match = true;
														return false;
													}
												}
											}, this);
									return match;
								}, this);
					}
					this.fireEvent("search", v, this.store, this);
				}
				this.hasSearch = true;
				this.triggers[0].show();
			}
		});
// 15号 19
Ext.ns("feyaSoft.home.common");
feyaSoft.home.common.InviteWindow = function(config) {
	this.config = config;
	this.url = "contact/invite";
	if (this.config && this.config.groupId != null) {
		this.url = "/static/ux/office/locationUserPending/invite?groupId=" + this.config.groupId;
	}
	this.email = new Ext.form.TextField({
				fieldLabel : feyaSoft.lang.profile.email,
				name : "email",
				allowBlank : true,
				plugins : [Ext.ux.FieldReplicator, Ext.ux.FieldLabeler]
			});
	var personalMessage = new Ext.form.TextArea({
				xtype : "textarea",
				name : "inviteMsg",
				hideLabel : true,
				flex : 1
			});
	this.formPanel = new Ext.FormPanel({
				labelWidth : 50,
				url : this.url,
				frame : true,
				border : false,
				bodyStyle : "padding:5px 5px 0 0",
				layout : {
					type : "vbox",
					align : "stretch"
				},
				items : [this.email, personalMessage]
			});
	feyaSoft.home.common.InviteWindow.superclass.constructor.call(this, {
				title : feyaSoft.lang.contact.inviteFriend,
				width : 500,
				height : 280,
				shim : false,
				constrainHeader : true,
				modal : true,
				border : false,
				layout : "fit",
				buttonAlign : "right",
				items : [this.formPanel],
				buttons : [{
							text : feyaSoft.lang.common.cancel,
							handler : this.cancelFn,
							scope : this
						}, {
							text : feyaSoft.lang.common.send,
							handler : this.sendFn,
							scope : this
						}]
			});
	this.show();
};
Ext.extend(feyaSoft.home.common.InviteWindow, Ext.Window, {
			sendFn : function() {
				if (this.formPanel.form.isValid()) {
					if (this.email.getValue() == null
							|| this.email.getValue() == "") {
						Ext.MessageBox.alert(feyaSoft.lang.common.error,
								"Please enter at least one email");
						return;
					}
					this.formPanel.form.submit({
								waitMsg : "Sending Invitations...",
								failure : function(form, action) {
									Ext.MessageBox.alert(
											feyaSoft.lang.common.error,
											feyaSoft.lang.common.errorInternal);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.close();
									} else {
										Ext.MessageBox.alert(
												feyaSoft.lang.common.error,
												action.result.info);
									}
								},
								scope : this
							});
				}
			},
			cancelFn : function() {
				this.close();
			}
		});
// 16号 20
Ext.ns("feyaSoft.home.common");
feyaSoft.home.common.AddContactWin = function(config) {
	var searchInvitePanel = new feyaSoft.home.common.SearchInvitePanel(config
			|| {});
	feyaSoft.home.common.AddContactWin.superclass.constructor.call(this, {
				title : feyaSoft.lang.file.addFriend,
				width : 600,
				height : 400,
				shim : false,
				constrainHeader : true,
				modal : true,
				border : true,
				buttonAlign : "right",
				layout : "fit",
				items : [searchInvitePanel],
				buttons : [{
							text : feyaSoft.lang.common.cancel,
							handler : this.cancelFn,
							scope : this
						}]
			});
	this.show();
};
Ext.extend(feyaSoft.home.common.AddContactWin, Ext.Window, {
			cancelFn : function() {
				this.close();
			}
		});
feyaSoft.home.common.SearchInvitePanel = Ext.extend(Ext.Panel, {
	border : false,
	autoScroll : true,
	initComponent : function() {
		var addFriendGroup = feyaSoft.lang.contact.addAsFriend;
		this.addUrl = "/static/ux/office/contact/inviteContact";
		if (this.groupId != null) {
			addFriendGroup = feyaSoft.lang.group.invite2group;
			this.addUrl = "/static/ux/office/locationUserPending/inviteFriend";
		}
		this.dataStore = new Ext.data.JsonStore({
					url : "/static/ux/office/userAccess/listForContact",
					fields : []
				});
		this.dataStore.baseParams = {
			excludeMe : "yes",
			start : 0,
			limit : 10,
			searchForFriend : true
		};
		this.inviteBtn = new Ext.Button({
					text : "<font color=\"blue\"><b>"
							+ feyaSoft.lang.file.invite + "</b></font>",
					iconCls : "email",
					handler : this.inviteFn,
					minWidth : 80,
					pressed : true,
					scope : this
				});
		this.dataView = new feyaSoft.home.common.ContactGroupDataView({
					store : this.dataStore,
					emptyText : "<div class=\"x-view-emtpytext\">"
							+ feyaSoft.lang.contact.inviteMsg + "</div>",
					applyText : addFriendGroup,
					applyCallback : this.addFriend,
					applyScope : this
				});
		this.tbar = [feyaSoft.lang.common.search, "",
				new Ext.ux.form.SearchField({
							autoShow : true,
							store : this.dataStore,
							width : 300,
							emptyText : feyaSoft.lang.common.atleast4char
						}), "->", this.inviteBtn];
		this.bbar = new Ext.PagingToolbar({
					pageSize : 10,
					store : this.dataStore,
					displayInfo : true,
					displayMsg : "{0} - {1} of {2}"
				});
		this.items = [this.dataView];
		feyaSoft.home.common.SearchInvitePanel.superclass.initComponent
				.call(this);
		this.reload();
	},
	addFriend : function(id) {
		var params = {
			userId : id
		};
		if (this.groupId != null) {
			params = {
				userId : id,
				groupId : this.groupId
			};
		}
		Ext.Ajax.request({
					url : this.addUrl,
					params : params,
					success : function(result, request) {
						var backObj = Ext.util.JSON.decode(result.responseText);
						if (backObj.success == "true") {
							Ext.Message.msgStay(feyaSoft.lang.common.confirm,
									backObj.info, 2000);
							this.reload();
						} else {
							Ext.MessageBox.alert(feyaSoft.lang.common.error,
									backObj.info);
						}
					},
					failure : function() {
						Ext.MessageBox.alert(feyaSoft.lang.common.error,
								"Fail to connect!");
					},
					scope : this
				});
	},
	reload : function() {
		this.dataStore.load({
					params : {
						start : 0,
						limit : 10
					}
				});
	},
	inviteFn : function() {
		new feyaSoft.home.common.InviteWindow(this.initialConfig);
	}
});
// 17号 21
Ext.ns("feyaSoft.home.common");
feyaSoft.home.common.ContactGroupDataView = Ext.extend(Ext.DataView, {
	cls : "x-publicgroup-list",
	applyText : "Apply",
	defaultLogo : feyaSoft.home.CONST.userLogo,
	overClass : "x-view-over",
	itemSelector : "div.x-group-preview",
	singleSelect : true,
	emptyText : "<div class=\"x-view-emtpytext\">"
			+ feyaSoft.lang.common.noResultDisplay + "</div>",
	initComponent : function() {
		var arr = [
				"<tpl for=\".\">",
				"<div class=\"x-group-preview\">",
				"<table><tbody><tr>",
				"<td class=\"x-group-logo-td\">",
				"<img class=\"x-group-logo\" src=\"{[values.logoPath?values.logoPath:(values.photo?values.photo:\"",
				this.defaultLogo,
				"\")]}\"></img>",
				"</td>",
				"<td>",
				"<div class=\"x-group-name\">{[values.firstname?values.firstname+\" \"+values.lastname:values.name]}</div>",
				"<div class=\"x-group-description\">{[values.description?values.description:\"<span>(",
				feyaSoft.lang.contact.noDescriptionForUser,
				")</span>\"]}</div>"];
		if (!this.hideApply) {
			arr = arr.concat(["<div class=\"x-group-foot\">",
					"<a name=\"apply\" href=\"#\">", this.applyText, "</a>",
					"</div>"]);
		}
		arr = arr.concat(["</td>", "</tr></tbody></table>", "</div>", "</tpl>",
				"<div class=\"x-clear\"></div>"]);
		this.tpl = (new Ext.XTemplate(arr.join(""))).compile();
		feyaSoft.home.common.ContactGroupDataView.superclass.initComponent
				.call(this);
		this.on("click", this.onClickFn, this);
	},
	onClickFn : function(dv, index, node, e) {
		var target = e.getTarget();
		if ("apply" == target.name) {
			if (this.applyCallback) {
				var rd = dv.getRecord(node);
				var id = rd.data.id || rd.data.contactUserId;
				this.applyCallback.call(this.applyScope || this, id, rd, node,
						dv);
			}
		}
	}
});
Ext.ns("feyaSoft.home.common");
feyaSoft.home.common.ListRevisionsWin = function(config) {
	var listRevision = new feyaSoft.home.common.ListRevisions(config);
	feyaSoft.home.common.ListRevisionsWin.superclass.constructor.call(this, {
				title : feyaSoft.lang.common.historyRevision + " - "
						+ config.name,
				width : 650,
				height : 380,
				shim : false,
				constrainHeader : true,
				modal : true,
				border : false,
				buttonAlign : "right",
				layout : "border",
				items : [listRevision],
				buttons : [{
							text : feyaSoft.lang.common.cancel,
							handler : this.cancelFn,
							scope : this
						}]
			});
	this.show();
};
Ext.extend(feyaSoft.home.common.ListRevisionsWin, Ext.Window, {
			cancelFn : function() {
				this.close();
			}
		});
feyaSoft.home.common.ListRevisions = function(config) {
	Ext.apply(this, config);
	this.myOwnerCt = config.myOwnerCt;
	this.compareBtn = new Ext.Button({
		text : feyaSoft.lang.common.compare,
		tooltip : "Highlight one or two items and click this button to compare",
		iconCls : "compare",
		disabled : true,
		documentId : config.fileId,
		handler : this.compareFn,
		scope : this
	});
	this.revisionBtn = new Ext.Button({
				text : feyaSoft.lang.common.revertToVersion,
				handler : this.revert,
				iconCls : "history",
				disabled : true,
				scope : this
			});
	this.refreshBtn = new Ext.Button({
				text : feyaSoft.lang.common.refresh,
				iconCls : "refresh",
				handler : function() {
					this.dataStore.reload();
				},
				scope : this
			});
	this.saveAsBtn = new Ext.Button({
				text : feyaSoft.lang.common.saveAs,
				iconCls : "save",
				disabled : true,
				handler : function() {
					this.saveAsNewFile();
				},
				scope : this
			});
	var tbar = [this.refreshBtn, "-", this.revisionBtn, "-", this.saveAsBtn];
	if (config.exname == "myDoc") {
		tbar = [this.refreshBtn, "-", this.revisionBtn, "-", this.compareBtn,
				"-", this.saveAsBtn];
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : false
			});
	var itemCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.name,
				dataIndex : "name",
				width : 100
			}, {
				header : feyaSoft.lang.common.author,
				dataIndex : "author",
				width : 100
			}, {
				header : feyaSoft.lang.common.dateModified,
				dataIndex : "createTime",
				width : 100
			}]);
	itemCM.defaultSortable = false;
	this.dataStore = new Ext.data.JsonStore({
				url : "documentHistory?id=" + config.fileId,
				remoteSort : true,
				fields : []
			});
	this.dataStore.load();
	feyaSoft.home.common.ListRevisions.superclass.constructor.call(this, {
				ds : this.dataStore,
				cm : itemCM,
				sm : selectBoxModel,
				tbar : tbar,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : "loading data ..."
				},
				layout : "fit",
				enableColumnHide : false,
				autoScroll : true,
				region : "center"
			});
	this.addListener("rowcontextmenu", this.onContextMenu, this);
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.ListRevisions, Ext.grid.GridPanel, {
	revertURL : "documentHistory/revert",
	onSelectionChangeFn : function(sm) {
		if (1 == sm.getSelections().length) {
			this.revisionBtn.enable();
			this.compareBtn.enable();
			this.saveAsBtn.enable();
		} else if (2 == sm.getSelections().length) {
			this.revisionBtn.disable();
			this.compareBtn.enable();
			this.saveAsBtn.disable();
		} else {
			this.revisionBtn.disable();
			this.compareBtn.disable();
			this.saveAsBtn.disable();
		}
	},
	onContextMenu : function(e) {
		if (!this.menu) {
			this.menu = new Ext.menu.Menu({
						id : "menus",
						items : [{
									id : "revert",
									iconCls : "history",
									text : feyaSoft.lang.common.revertToVersion,
									scope : this,
									handler : this.revert
								}]
					});
		}
		var record = this.getSelectionModel().getSelected();
		this.menu.items.get("revert")[record == null ? "disable" : "enable"]();
		e.stopEvent();
		this.menu.showAt(e.getXY());
	},
	revert : function() {
		var record = this.getSelectionModel().getSelected();
		if (record == null || record.data == null || record.data.id == null) {
			Ext.MessageBox.alert(feyaSoft.lang.common.error,
					feyaSoft.lang.common.pleaseSelectOne);
			return;
		}
		var revertInfo = feyaSoft.lang.common.revertToVersion + "?";
		Ext.MessageBox.confirm(revertInfo,
				feyaSoft.lang.common.revertToVersionDesc, function(btn) {
					if (btn == "yes") {
						Ext.Ajax.request({
									url : this.revertURL,
									params : {
										id : record.data.id
									},
									method : "GET",
									success : function(result, request) {
										var jsonData = Ext.util.JSON
												.decode(result.responseText);
										if (jsonData.success == "true") {
											this.ownerCt.cancelFn();
											if (this.myOwnerCt) {
												this.myOwnerCt.reload();
											}
										} else {
											Ext.MessageBox.alert(
													feyaSoft.lang.common.error,
													jsonData.info);
										}
									},
									failure : function(result, request) {
									},
									scope : this
								});
					}
				}, this);
	},
	compareFn : function(btn) {
		var selectedIds = [];
		var records = this.getSelectionModel().getSelections();
		for (var r = 0; r < records.length; r++) {
			if (r > 1) {
				break;
			}
			selectedIds.push(records[r].get("id"));
		}
		if (selectedIds.length == 0) {
			Ext.MessageBox.alert(feyaSoft.lang.common.error,
					feyaSoft.lang.common.pleaseSelectOne);
		} else {
			var param = {
				documentId : btn.documentId,
				selectedIds : selectedIds
			};
			new feyaSoft.home.common.CompareWindow(param);
		}
	},
	saveAsNewFile : function() {
		var record = this.getSelectionModel().getSelected();
		if (record == null || record.data == null || record.data.id == null) {
			Ext.MessageBox.alert(feyaSoft.lang.common.error,
					feyaSoft.lang.common.pleaseSelectOne);
			return;
		}
		Ext.Msg.show({
			title : feyaSoft.lang.common.saveAs,
			msg : feyaSoft.lang.file.enterNewName,
			width : 400,
			buttons : Ext.MessageBox.OKCANCEL,
			prompt : true,
			scope : this,
			animEl : Ext.getBody(),
			icon : Ext.MessageBox.INFO,
			fn : function(btn, text) {
				if (btn == "ok") {
					if (text.length > 0) {
						Ext.Ajax.request({
									url : "documentHistory/saveRevisionAs",
									params : {
										name : text,
										revisonId : record.data.id
									},
									method : "GET",
									success : function(result, request) {
										var jsonData = Ext.util.JSON
												.decode(result.responseText);
										if (jsonData.success == "true") {
											if (this.myOwnerCt) {
												this.myOwnerCt.reload();
											}
											Ext.MessageBox.alert(
													feyaSoft.lang.common.ok,
													jsonData.info);
										} else {
											Ext.MessageBox.alert(
													feyaSoft.lang.common.error,
													jsonData.info);
										}
									},
									failure : function(result, request) {
										Ext.MessageBox
												.alert(
														feyaSoft.lang.common.failed,
														feyaSoft.lang.common.errorInternal);
									},
									scope : this
								});
					} else {
						Ext.MessageBox.alert(feyaSoft.lang.common.failed,
								feyaSoft.lang.common.nameNoEmpty);
					}
				}
			}
		});
	}
});

Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.SharePanel = function(config) {
	this.myOwnerCt = null;
	this.listUsers = new feyaSoft.home.common.share.ListUsers(config);
	this.addUser = new feyaSoft.home.common.share.AddUser(config);
	feyaSoft.home.common.share.SharePanel.superclass.constructor.call(this, {
				title : feyaSoft.lang.file.peopleAccess,
				border : false,
				layout : "border",
				items : [this.listUsers, this.addUser]
			});
};
Ext.extend(feyaSoft.home.common.share.SharePanel, Ext.Panel, {
			initLoad : function(config) {
				this.myOwnerCt = config.myOwnerCt;
				this.listUsers.initLoad(config);
				this.addUser.initLoad(config);
			},
			existedUsers : function() {
				return this.listUsers.existedUsers();
			},
			reloadList : function() {
				this.listUsers.reload();
				this.myOwnerCt.reload();
			}
		});
feyaSoft.home.common.share.ListUsers = function(config) {
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "documentShareFile";
	if (config.componentClass) {
		this.componentClass = config.componentClass;
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	function viewEditPermission(val) {
		var result = val;
		if (val == 1) {
			result = feyaSoft.lang.common.canView;
		} else if (val == 2) {
			result = feyaSoft.lang.common.canEdit;
		} else if (val == 7) {
			result = feyaSoft.lang.common.canEdit;
		}
		return result;
	}
	function infoAction(val, p) {
		return "<img src=\"images/icons/information.png\" ext:qtitle=\"Note\" ext:qtip=\""
				+ val + "\"></img>";
	}
	var shareCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.user,
				width : 190,
				dataIndex : "shareUser"
			}, {
				header : feyaSoft.lang.common.permission,
				width : 110,
				dataIndex : "permission",
				scope : this,
				renderer : viewEditPermission
			}, {
				header : feyaSoft.lang.common.date,
				width : 140,
				dataIndex : "updateDate",
				renderer : Ext.util.Common.formatDateHour
			}, {
				header : "Info",
				width : 40,
				sortable : false,
				dataIndex : "note",
				renderer : infoAction
			}]);
	this.shareStore = new Ext.data.JsonStore({
				url : this.componentClass + "/list",
				remoteSort : true,
				fields : []
			});
	this.deleteBtn = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.remove,
		id : "delete-share-file-action",
		tooltip : "Highlight the item and click this button to remove this shared user",
		iconCls : "delete",
		handler : function() {
			var record = this.getSelectionModel().getSelected();
			if (record.data.id == 0) {
				Ext.MessageBox.alert(feyaSoft.lang.common.permissionDeny,
						"Not allowed to delete Owner share");
			} else {
				this.deleteItem(record);
			}
		},
		scope : this
	});
	this.changePermit = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.changePermission,
		tooltip : "Highlight the item and click this button to change this shared user permission",
		iconCls : "editItem",
		handler : function() {
			var record = this.getSelectionModel().getSelected();
			if (record.data.id == 0) {
				Ext.MessageBox.alert(feyaSoft.lang.common.permissionDeny,
						"Not allowed to change owner permission");
			} else {
				this.changeItem(record);
			}
		},
		scope : this
	});
	feyaSoft.home.common.share.ListUsers.superclass.constructor.call(this, {
				region : "center",
				border : true,
				store : this.shareStore,
				cm : shareCM,
				sm : selectBoxModel,
				width : 600,
				height : 300,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : feyaSoft.lang.common.loadingData
				},
				tbar : [this.deleteBtn, this.changePermit],
				autoScroll : true
			});
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.share.ListUsers, Ext.grid.GridPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.shareStore.baseParams = {
					fileId : this.fileId
				};
				this.shareStore.load();
			},
			existedUsers : function() {
				return this.shareStore.data.items;
			},
			onSelectionChangeFn : function(sm) {
				if (0 < sm.getSelections().length) {
					this.deleteBtn.enable();
					if (this.componentClass == "myPhotoShare"
							|| this.componentClass == "myPhotoFolderShare") {
						this.changePermit.disable();
					} else {
						this.changePermit.enable();
					}
				} else {
					this.deleteBtn.disable();
					this.changePermit.disable();
				}
			},
			deleteItem : function(rd) {
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirmDelete,
							msg : feyaSoft.lang.common.confirmDeleteDesc,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onDeleteConfirm,
							scope : this,
							record : rd
						});
			},
			onDeleteConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/delete",
								params : {
									id : options.record.data.id
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			changeItem : function(rd) {
				var changedPermit = feyaSoft.lang.common.canView;
				if (rd.data.permission == 1) {
					changedPermit = feyaSoft.lang.common.canEdit;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirm,
							msg : feyaSoft.lang.common.confirmChangePermit
									+ changedPermit,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onChangeConfirm,
							scope : this,
							record : rd
						});
			},
			onChangeConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/changePermit",
								params : {
									id : options.record.data.id
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			reload : function() {
				this.shareStore.reload();
			},
			disableDelete : function() {
				Ext.getCmp("delete-share-file-action").setDisabled(true);
			}
		});
feyaSoft.home.common.share.AddUser = function(config) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "documentShareFile";
	if (config.componentClass) {
		this.componentClass = config.componentClass;
	}
	var permissionStore = [["1", feyaSoft.lang.common.canView],
			["2", feyaSoft.lang.common.canEdit]];
	if (this.componentClass == "myPhotoShare"
			|| this.componentClass == "myPhotoFolderShare") {
		permissionStore = [["1", feyaSoft.lang.common.canView]];
	}
	var permission_data = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : permissionStore
			});
	var userStore = new Ext.data.JsonStore({
				url : "/static/ux/office/contact/list",
				remoteSort : true,
				fields : []
			});
	userStore.setDefaultSort("name", "ASC");
	this.contactName = new Ext.ux.form.LovCombo({
				fieldLabel : feyaSoft.lang.common.user,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				minChars : 2,
				triggerAction : "all",
				store : userStore,
				displayField : "name",
				hiddenName : "sharedUserId",
				valueField : "contactUserId",
				loadingText : feyaSoft.lang.common.loadingData,
				pageSize : 15,
				anchor : "93%",
				listeners : {
					select : this.onUserSelectFn,
					scope : this
				}
			});
	this.permission = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.permission,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				triggerAction : "all",
				store : permission_data,
				displayField : "name",
				hiddenName : "permission",
				mode : "local",
				valueField : "id",
				anchor : "93%",
				listeners : {
					select : this.onPermissionSelectFn,
					scope : this
				}
			});
	this.allowInvite = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.allowViewEdit,
				name : "allowInvite",
				disabled : true
			});
	this.note = new Ext.form.HtmlEditor({
				hideLabel : true,
				name : "note",
				height : 150,
				anchor : "93%"
			});
	var emailNotify = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.sendEmail,
				name : "emailNotify",
				checked : true
			});
	this.addMoreBtn = new Ext.Button({
				text : feyaSoft.lang.file.addFriend,
				handler : function() {
					new feyaSoft.home.common.AddContactWin;
				},
				minWidth : 90,
				iconCls : "addItem",
				scope : this
			});
	this.saveBtn = new Ext.Button({
				minWidth : 75,
				text : feyaSoft.lang.common.save,
				tooltip : "Save the result to the system",
				iconCls : "save",
				handler : this.doSaveFn,
				scope : this
			});
	feyaSoft.home.common.share.AddUser.superclass.constructor.call(this, {
				region : "east",
				width : 350,
				split : true,
				baseCls : "x-plain",
				labelWidth : 65,
				bodyStyle : "padding: 20px 10px 10px 10px",
				url : this.componentClass + "/create",
				items : [this.contactName, this.permission, this.allowInvite,
						this.note, emailNotify],
				buttons : [this.addMoreBtn, this.saveBtn]
			});
};
Ext.extend(feyaSoft.home.common.share.AddUser, Ext.form.FormPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.permission.setValue("1");
			},
			onUserSelectFn : function(obj) {
				var name = null;
				var existedUser = false;
				var existedUsers = this.ownerCt.existedUsers();
				for (var i = 0; i < existedUsers.length; i++) {
					if (obj.value == existedUsers[i].data.shareUserId) {
						existedUser = true;
						name = existedUsers[i].data.shareUser;
					}
				}
				if (existedUser) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.file.itemShared + ": " + name);
					this.contactName.setValue(null);
				}
			},
			onPermissionSelectFn : function(obj) {
				if (obj && obj.value == 2) {
					this.allowInvite.setDisabled(false);
				} else {
					this.allowInvite.setDisabled(true);
					this.allowInvite.setValue(false);
				}
			},
			doSaveFn : function() {
				if (this.form.isValid()) {
					this.form.submit({
								params : {
									fileId : this.fileId
								},
								waitMsg : "In processing",
								failure : function(form, action) {
									Ext.MessageBox.alert("Error Message",
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.ownerCt.reloadList();
										this.contactName.reset();
									} else {
										Ext.MessageBox.alert("Error Message",
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert("Errors",
							"Please fix the errors noted.");
				}
			}
		});
Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.GroupSharePanel = function(config) {
	this.listGroups = new feyaSoft.home.common.share.ListGroups(config);
	this.addGroup = new feyaSoft.home.common.share.AddGroup(config);
	feyaSoft.home.common.share.GroupSharePanel.superclass.constructor.call(
			this, {
				title : feyaSoft.lang.file.groupAccess,
				border : false,
				layout : "border",
				items : [this.listGroups, this.addGroup]
			});
};
Ext.extend(feyaSoft.home.common.share.GroupSharePanel, Ext.Panel, {
			initLoad : function(config) {
				this.listGroups.initLoad(config);
				this.addGroup.initLoad(config);
			},
			existedGroups : function() {
				return this.listGroups.existedGroups();
			},
			reloadListGroup : function() {
				this.listGroups.reload();
			}
		});
feyaSoft.home.common.share.ListGroups = function(config) {
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "locationFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "locationPhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "locationPhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "locationBookmark";
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	function viewEditPermission(val) {
		var result = val;
		if (val == 1) {
			result = feyaSoft.lang.common.canView;
		} else if (val == 2) {
			result = feyaSoft.lang.common.canEdit;
		} else if (val == 7) {
			result = feyaSoft.lang.common.canEdit;
		}
		return result;
	}
	var shareCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.name,
				width : 200,
				dataIndex : "name"
			}, {
				header : feyaSoft.lang.common.permission,
				width : 100,
				dataIndex : "permission",
				scope : this,
				renderer : viewEditPermission
			}, {
				header : feyaSoft.lang.common.date,
				width : 150,
				dataIndex : "shareDate",
				renderer : Ext.util.Common.formatDateHour
			}]);
	this.shareStore = new Ext.data.JsonStore({
				url : this.componentClass + "/sharedGroups",
				remoteSort : true,
				fields : []
			});
	this.deleteBtn = new Ext.Button({
		text : feyaSoft.lang.common.remove,
		tooltip : "Highlight the item and click this button to remove this shared user",
		iconCls : "delete",
		disabled : true,
		handler : this.deleteItem,
		scope : this
	});
	this.changePermit = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.changePermission,
		tooltip : "Highlight the item and click this button to change this shared user permission",
		iconCls : "editItem",
		handler : this.changeItem,
		scope : this
	});
	feyaSoft.home.common.share.ListGroups.superclass.constructor.call(this, {
				region : "center",
				store : this.shareStore,
				cm : shareCM,
				sm : selectBoxModel,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : feyaSoft.lang.common.loadingData
				},
				tbar : [this.deleteBtn, this.changePermit],
				autoScroll : true
			});
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.share.ListGroups, Ext.grid.GridPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.shareStore.baseParams = {
					fileId : this.fileId
				};
				this.shareStore.load();
			},
			existedGroups : function() {
				return this.shareStore.data.items;
			},
			onSelectionChangeFn : function(sm) {
				if (0 < sm.getSelections().length) {
					this.deleteBtn.enable();
					if (this.componentClass == "locationPhoto"
							|| this.componentClass == "locationPhotoFolder") {
						this.changePermit.disable();
					} else {
						this.changePermit.enable();
					}
				} else {
					this.deleteBtn.disable();
					this.changePermit.disable();
				}
			},
			deleteItem : function() {
				var record = this.getSelectionModel().getSelected();
				if (record == null || record.data == null
						|| record.data.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				var deleteInfo = feyaSoft.lang.common.confirmDelete + ": "
						+ record.data.name;
				Ext.MessageBox.confirm(deleteInfo,
						feyaSoft.lang.common.confirmDeleteDesc, function(btn) {
							if (btn == "yes") {
								Ext.Ajax.request({
											url : this.componentClass
													+ "/remove",
											params : {
												groupId : record.data.id,
												fileId : this.fileId
											},
											method : "GET",
											success : function(result, request) {
												var jsonData = Ext.util.JSON
														.decode(result.responseText);
												if (jsonData.success == "true") {
													this.reload();
													this.myOwnerCt.reload();
												} else {
													Ext.MessageBox
															.alert(
																	feyaSoft.lang.common.error,
																	jsonData.info);
												}
											},
											failure : function(result, request) {
											},
											scope : this
										});
							}
						}, this);
			},
			changeItem : function() {
				var record = this.getSelectionModel().getSelected();
				if (record == null || record.data == null
						|| record.data.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				var changedPermit = feyaSoft.lang.common.canView;
				if (record.data.permission == 1) {
					changedPermit = feyaSoft.lang.common.canEdit;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirm,
							msg : feyaSoft.lang.common.confirmChangePermit
									+ changedPermit,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onChangeConfirm,
							scope : this,
							record : record
						});
			},
			onChangeConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/changePermit",
								params : {
									groupId : options.record.data.id,
									fileId : this.fileId
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			reload : function() {
				this.shareStore.reload();
			},
			disableDelete : function() {
				Ext.getCmp("delete-share-file-action").setDisabled(true);
			}
		});
feyaSoft.home.common.share.AddGroup = function(config) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "locationFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "locationPhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "locationPhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "locationBookmark";
	}
	var groupStore = new Ext.data.JsonStore({
				url : "locationUser/list",
				remoteSort : true,
				fields : []
			});
	groupStore.setDefaultSort("name", "ASC");
	this.group = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.group.group,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				minChars : 2,
				triggerAction : "all",
				store : groupStore,
				displayField : "name",
				hiddenName : "groupId",
				valueField : "id",
				emptyText : feyaSoft.lang.common.pleaseSelectOne,
				loadingText : feyaSoft.lang.common.loadingData,
				pageSize : 15,
				anchor : "93%",
				listeners : {
					select : this.onGroupSelectFn,
					scope : this
				}
			});
	var permissionStore = [["1", feyaSoft.lang.common.canView],
			["2", feyaSoft.lang.common.canEdit]];
	if (config.componentClass == "myPhotoShare"
			|| config.componentClass == "myPhotoFolderShare") {
		permissionStore = [["1", feyaSoft.lang.common.canView]];
	}
	var permission_data = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : permissionStore
			});
	this.permission = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.permission,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				triggerAction : "all",
				store : permission_data,
				displayField : "name",
				hiddenName : "permission",
				mode : "local",
				valueField : "id",
				anchor : "93%"
			});
	this.note = new Ext.form.HtmlEditor({
				hideLabel : true,
				name : "note",
				height : 150,
				anchor : "93%"
			});
	this.posting = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.groupShareMsg,
				name : "doPosting",
				checked : true
			});
	this.saveBtn = new Ext.Button({
				minWidth : 75,
				text : feyaSoft.lang.common.save,
				tooltip : "Save the result to the system",
				iconCls : "save",
				handler : this.doSaveFn,
				scope : this
			});
	feyaSoft.home.common.share.AddGroup.superclass.constructor.call(this, {
				region : "east",
				width : 350,
				split : true,
				baseCls : "x-plain",
				labelWidth : 70,
				bodyStyle : "padding: 20px 10px 10px 10px",
				url : this.componentClass + "/createUpdate",
				items : [this.group, this.permission, this.note, this.posting],
				buttons : [this.saveBtn]
			});
};
Ext.extend(feyaSoft.home.common.share.AddGroup, Ext.form.FormPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.permission.setValue("1");
			},
			onGroupSelectFn : function(obj) {
				var allowPosting = true;
				var name = null;
				var items = obj.store.data.items;
				for (var i = 0; i < items.length; i++) {
					if (obj.value == items[i].data.id) {
						allowPosting = items[i].data.allowPosting;
						name = items[i].data.name;
					}
				}
				if (!allowPosting) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.group.notAllowPosting + ": " + name);
					this.group.setValue(null);
				}
				var existedGroup = false;
				var existedGroups = this.ownerCt.existedGroups();
				for (var i = 0; i < existedGroups.length; i++) {
					if (obj.value == existedGroups[i].data.id) {
						existedGroup = true;
						name = existedGroups[i].data.name;
					}
				}
				if (existedGroup) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.group.groupShared + ": " + name);
					this.group.setValue(null);
				}
			},
			doSaveFn : function() {
				if (this.form.isValid()) {
					this.form.submit({
								params : {
									fileId : this.fileId
								},
								waitMsg : "In processing",
								failure : function(form, action) {
									Ext.MessageBox.alert("Error Message",
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.ownerCt.reloadListGroup();
										this.group.reset();
									} else {
										Ext.MessageBox.alert(
												feyaSoft.lang.common.error,
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							"Please fix the errors noted.");
				}
			}
		});
Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.ContactSharePanel = function(config) {
	this.myOwnerCt = config.myOwnerCt;
	this.listContact = new feyaSoft.home.common.share.ListContacts(config);
	this.addContact = new feyaSoft.home.common.share.AddContact(config);
	feyaSoft.home.common.share.ContactSharePanel.superclass.constructor.call(
			this, {
				title : feyaSoft.lang.file.contactAccess,
				border : false,
				layout : "border",
				items : [this.listContact, this.addContact]
			});
};
Ext.extend(feyaSoft.home.common.share.ContactSharePanel, Ext.Panel, {
			initLoad : function(config) {
				this.listContact.initLoad(config);
				this.addContact.initLoad(config);
			},
			existedItems : function() {
				return this.listContact.existedItem();
			},
			reloadList : function() {
				this.listContact.reload();
			}
		});
feyaSoft.home.common.share.ListContacts = function(config) {
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "contactShareFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "contactSharePhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "contactSharePhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "contactShareBookmark";
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	function viewEditPermission(val) {
		var result = val;
		if (val == 1) {
			result = feyaSoft.lang.common.canView;
		} else if (val == 2) {
			result = feyaSoft.lang.common.canEdit;
		} else if (val == 7) {
			result = feyaSoft.lang.common.canEdit;
		}
		return result;
	}
	var shareCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.contactCategory,
				width : 200,
				dataIndex : "shareCategory"
			}, {
				header : feyaSoft.lang.common.permission,
				width : 90,
				dataIndex : "permission",
				scope : this,
				renderer : viewEditPermission
			}, {
				header : feyaSoft.lang.common.date,
				width : 140,
				dataIndex : "updateDate",
				renderer : Ext.util.Common.formatDateHour
			}]);
	this.shareStore = new Ext.data.JsonStore({
				url : this.componentClass + "/list",
				remoteSort : true,
				fields : []
			});
	this.deleteBtn = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.remove,
		id : "delete-contact-file-action",
		tooltip : "Highlight the item and click this button to remove this item",
		iconCls : "delete",
		handler : function() {
			var record = this.getSelectionModel().getSelected();
			if (record) {
				this.deleteItem(record);
			}
		},
		scope : this
	});
	this.changePermit = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.changePermission,
		tooltip : "Highlight the item and click this button to change this shared user permission",
		iconCls : "editItem",
		handler : function() {
			var record = this.getSelectionModel().getSelected();
			if (record) {
				this.changeItem(record);
			}
		},
		scope : this
	});
	feyaSoft.home.common.share.ListContacts.superclass.constructor.call(this, {
				region : "center",
				border : true,
				store : this.shareStore,
				cm : shareCM,
				sm : selectBoxModel,
				width : 600,
				height : 300,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : feyaSoft.lang.common.loadingData
				},
				tbar : [this.deleteBtn, this.changePermit],
				autoScroll : true
			});
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.share.ListContacts, Ext.grid.GridPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.shareStore.baseParams = {
					fileId : this.fileId
				};
				this.shareStore.load();
			},
			existedItem : function() {
				return this.shareStore.data.items;
			},
			onSelectionChangeFn : function(sm) {
				if (0 < sm.getSelections().length) {
					this.deleteBtn.enable();
					if (this.componentClass == "contactSharePhotoFolder"
							|| this.componentClass == "contactSharePhoto") {
						this.changePermit.disable();
					} else {
						this.changePermit.enable();
					}
				} else {
					this.deleteBtn.disable();
					this.changePermit.disable();
				}
			},
			deleteItem : function(rd) {
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirmDelete,
							msg : feyaSoft.lang.common.confirmDeleteDesc,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onDeleteConfirm,
							scope : this,
							record : rd
						});
			},
			onDeleteConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/delete",
								params : {
									id : options.record.data.id
								},
								success : function(result, request) {
									this.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			changeItem : function(rd) {
				var changedPermit = feyaSoft.lang.common.canView;
				if (rd.data.permission == 1) {
					changedPermit = feyaSoft.lang.common.canEdit;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirm,
							msg : feyaSoft.lang.common.confirmChangePermit
									+ changedPermit,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onChangeConfirm,
							scope : this,
							record : rd
						});
			},
			onChangeConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/changePermit",
								params : {
									id : options.record.data.id
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			reload : function() {
				this.shareStore.reload();
			},
			disableDelete : function() {
				Ext.getCmp("delete-contact-file-action").setDisabled(true);
			}
		});
feyaSoft.home.common.share.AddContact = function(config, myOwnerCt) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "contactShareFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "contactSharePhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "contactSharePhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "contactShareBookmark";
	}
	var permissionStore = [["1", feyaSoft.lang.common.canView],
			["2", feyaSoft.lang.common.canEdit]];
	if (config.componentClass == "myPhotoShare"
			|| config.componentClass == "myPhotoFolderShare") {
		permissionStore = [["1", feyaSoft.lang.common.canView]];
	}
	var permission_data = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : permissionStore
			});
	var categoryStore = new Ext.data.JsonStore({
				url : "contactCategory/list",
				remoteSort : true,
				fields : []
			});
	categoryStore.setDefaultSort("name", "ASC");
	this.categoryName = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.category,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				minChars : 2,
				triggerAction : "all",
				store : categoryStore,
				displayField : "name",
				hiddenName : "categoryId",
				valueField : "id",
				emptyText : feyaSoft.lang.common.pleaseSelectOne,
				loadingText : feyaSoft.lang.common.loadingData,
				pageSize : 15,
				anchor : "93%",
				listeners : {
					select : this.onCategorySelectFn,
					scope : this
				}
			});
	this.permission = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.permission,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				triggerAction : "all",
				store : permission_data,
				displayField : "name",
				hiddenName : "permission",
				mode : "local",
				valueField : "id",
				allowBlank : false,
				anchor : "93%"
			});
	this.note = new Ext.form.HtmlEditor({
				hideLabel : true,
				name : "note",
				height : 150,
				anchor : "93%"
			});
	this.posting = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.contactShareMsg,
				name : "doPosting",
				checked : true
			});
	this.saveBtn = new Ext.Button({
				minWidth : 75,
				text : feyaSoft.lang.common.save,
				tooltip : "Save the result to the system",
				iconCls : "save",
				handler : this.doSaveFn,
				scope : this
			});
	feyaSoft.home.common.share.AddContact.superclass.constructor.call(this, {
				region : "east",
				width : 350,
				split : true,
				baseCls : "x-plain",
				labelWidth : 65,
				bodyStyle : "padding: 20px 10px 10px 10px",
				url : this.componentClass + "/createUpdate",
				items : [this.categoryName, this.permission, this.note,
						this.posting],
				buttons : [this.saveBtn]
			});
};
Ext.extend(feyaSoft.home.common.share.AddContact, Ext.form.FormPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.permission.setValue("1");
			},
			onCategorySelectFn : function(obj) {
				var name = null;
				var existedItem = false;
				var existedItems = this.ownerCt.existedItems();
				for (var i = 0; i < existedItems.length; i++) {
					if (obj.value == existedItems[i].data.categoryId) {
						existedItem = true;
						name = existedItems[i].data.shareCategory;
					}
				}
				if (existedItem) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.file.itemShared + ": " + name);
					this.categoryName.setValue(null);
				}
			},
			doSaveFn : function() {
				if (this.form.isValid()) {
					this.form.submit({
								params : {
									fileId : this.fileId
								},
								waitMsg : "In processing",
								failure : function(form, action) {
									Ext.MessageBox.alert("Error Message",
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.ownerCt.reloadList();
										this.categoryName.reset();
										this.note.reset();
									} else {
										Ext.MessageBox.alert("Error Message",
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert("Errors",
							"Please fix the errors noted.");
				}
			}
		});
Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.OrganizationSharePanel = function(config) {
	this.listOrganizations = new feyaSoft.home.common.share.ListOrganizations(config);
	this.addOrganization = new feyaSoft.home.common.share.AddOrganization(config);
	feyaSoft.home.common.share.OrganizationSharePanel.superclass.constructor
			.call(this, {
						title : feyaSoft.lang.file.organizationAccess,
						border : false,
						layout : "border",
						items : [this.listOrganizations, this.addOrganization]
					});
};
Ext.extend(feyaSoft.home.common.share.OrganizationSharePanel, Ext.Panel, {
			initLoad : function(config) {
				this.listOrganizations.initLoad(config);
				this.addOrganization.initLoad(config);
			},
			existedOrganizations : function() {
				return this.listOrganizations.existedOrganizations();
			},
			reloadListOrganization : function() {
				this.listOrganizations.reload();
			}
		});
feyaSoft.home.common.share.ListOrganizations = function(config) {
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "organizationFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "organizationPhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "organizationPhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "organizationBookmark";
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	function viewEditPermission(val) {
		var result = val;
		if (val == 1) {
			result = feyaSoft.lang.common.canView;
		} else if (val == 2) {
			result = feyaSoft.lang.common.canEdit;
		} else if (val == 7) {
			result = feyaSoft.lang.common.canEdit;
		}
		return result;
	}
	var shareCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.name,
				width : 200,
				dataIndex : "name"
			}, {
				header : feyaSoft.lang.common.permission,
				width : 100,
				dataIndex : "permission",
				scope : this,
				renderer : viewEditPermission
			}, {
				header : feyaSoft.lang.common.date,
				width : 150,
				dataIndex : "shareDate",
				renderer : Ext.util.Common.formatDateHour
			}]);
	this.shareStore = new Ext.data.JsonStore({
				url : this.componentClass + "/sharedOrganizations",
				remoteSort : true,
				fields : []
			});
	this.deleteBtn = new Ext.Button({
		text : feyaSoft.lang.common.remove,
		tooltip : "Highlight the item and click this button to remove this shared user",
		iconCls : "delete",
		disabled : true,
		handler : function() {
			this.deleteItem();
		},
		scope : this
	});
	this.changePermit = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.changePermission,
		tooltip : "Highlight the item and click this button to change this shared organization permission",
		iconCls : "editItem",
		handler : this.changeItem,
		scope : this
	});
	feyaSoft.home.common.share.ListOrganizations.superclass.constructor.call(
			this, {
				region : "center",
				store : this.shareStore,
				cm : shareCM,
				sm : selectBoxModel,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : feyaSoft.lang.common.loadingData
				},
				tbar : [this.deleteBtn, this.changePermit],
				autoScroll : true
			});
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.share.ListOrganizations, Ext.grid.GridPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.shareStore.baseParams = {
					fileId : this.fileId
				};
				this.shareStore.load();
			},
			existedOrganizations : function() {
				return this.shareStore.data.items;
			},
			onSelectionChangeFn : function(sm) {
				if (0 < sm.getSelections().length) {
					this.deleteBtn.enable();
					if (this.componentClass == "organizationPhotoFolder"
							|| this.componentClass == "organizationPhoto") {
						this.changePermit.disable();
					} else {
						this.changePermit.enable();
					}
				} else {
					this.deleteBtn.disable();
					this.changePermit.disable();
				}
			},
			deleteItem : function(rd) {
				var record = this.getSelectionModel().getSelected();
				if (record == null || record.data == null
						|| record.data.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				var deleteInfo = feyaSoft.lang.common.confirmDelete + ": "
						+ record.data.name;
				Ext.MessageBox.confirm(deleteInfo,
						feyaSoft.lang.common.confirmDeleteDesc, function(btn) {
							if (btn == "yes") {
								Ext.Ajax.request({
											url : this.componentClass
													+ "/remove",
											params : {
												organizationId : record.data.id,
												fileId : this.fileId
											},
											method : "GET",
											success : function(result, request) {
												var jsonData = Ext.util.JSON
														.decode(result.responseText);
												if (jsonData.success == "true") {
													this.reload();
													this.myOwnerCt.reload();
												} else {
													Ext.MessageBox
															.alert(
																	feyaSoft.lang.common.error,
																	jsonData.info);
												}
											},
											failure : function(result, request) {
											},
											scope : this
										});
							}
						}, this);
			},
			changeItem : function() {
				var record = this.getSelectionModel().getSelected();
				if (record == null || record.data == null
						|| record.data.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				var changedPermit = feyaSoft.lang.common.canView;
				if (record.data.permission == 1) {
					changedPermit = feyaSoft.lang.common.canEdit;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirm,
							msg : feyaSoft.lang.common.confirmChangePermit
									+ changedPermit,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onChangeConfirm,
							scope : this,
							record : record
						});
			},
			onChangeConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/changePermit",
								params : {
									organizationId : options.record.data.id,
									fileId : this.fileId
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			reload : function() {
				this.shareStore.reload();
			},
			disableDelete : function() {
				Ext.getCmp("delete-share-file-action").setDisabled(true);
			}
		});
feyaSoft.home.common.share.AddOrganization = function(config) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "organizationFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "organizationPhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "organizationPhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "organizationBookmark";
	}
	var organizationStore = new Ext.data.JsonStore({
				url : "organizationUser/list",
				remoteSort : true,
				fields : []
			});
	organizationStore.setDefaultSort("name", "ASC");
	this.organization = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.organization.organization,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				minChars : 2,
				triggerAction : "all",
				store : organizationStore,
				displayField : "name",
				hiddenName : "itemId",
				valueField : "id",
				emptyText : feyaSoft.lang.common.pleaseSelectOne,
				loadingText : feyaSoft.lang.common.loadingData,
				pageSize : 15,
				anchor : "93%",
				listeners : {
					select : this.onOrganizationSelectFn,
					scope : this
				}
			});
	this.permissionStore = [["1", feyaSoft.lang.common.canView],
			["2", feyaSoft.lang.common.canEdit]];
	if (this.componentClass == "organizationPhotoFolder"
			|| this.componentClass == "organizationPhoto") {
		this.permissionStore = [["1", feyaSoft.lang.common.canView]];
	}
	this.permission_data = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : this.permissionStore
			});
	this.permission = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.permission,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				triggerAction : "all",
				store : this.permission_data,
				displayField : "name",
				hiddenName : "permission",
				mode : "local",
				valueField : "id",
				anchor : "93%"
			});
	this.note = new Ext.form.HtmlEditor({
				hideLabel : true,
				name : "note",
				height : 150,
				anchor : "93%"
			});
	this.posting = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.organizationShareMsg,
				name : "doPosting",
				checked : true
			});
	this.saveBtn = new Ext.Button({
				minWidth : 75,
				text : feyaSoft.lang.common.save,
				tooltip : "Save the result to the system",
				iconCls : "save",
				handler : this.doSaveFn,
				scope : this
			});
	feyaSoft.home.common.share.AddOrganization.superclass.constructor.call(
			this, {
				region : "east",
				width : 350,
				split : true,
				baseCls : "x-plain",
				labelWidth : 70,
				bodyStyle : "padding: 20px 10px 10px 10px",
				url : this.componentClass + "/createUpdate",
				items : [this.organization, this.permission, this.note,
						this.posting],
				buttons : [this.saveBtn]
			});
};
Ext.extend(feyaSoft.home.common.share.AddOrganization, Ext.form.FormPanel, {
			onOrgAfterRenderFn : function() {
				this.permission_data.on("load", function() {
							this.permission.setValue("1");
						}, this);
			},
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.permission.setValue("1");
			},
			onOrganizationSelectFn : function(obj) {
				var allowPosting = true;
				var name = null;
				var items = obj.store.data.items;
				for (var i = 0; i < items.length; i++) {
					if (obj.value == items[i].data.id) {
						allowPosting = items[i].data.allowPosting;
						name = items[i].data.name;
					}
				}
				if (!allowPosting) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.group.notAllowPosting + ": " + name);
					this.organization.setValue(null);
				}
				var existedOrganization = false;
				var existedOrganizations = this.ownerCt.existedOrganizations();
				for (var i = 0; i < existedOrganizations.length; i++) {
					if (obj.value == existedOrganizations[i].data.id) {
						existedOrganization = true;
						name = existedOrganizations[i].data.name;
					}
				}
				if (existedOrganization) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.group.groupShared + ": " + name);
					this.organization.setValue(null);
				}
			},
			doSaveFn : function() {
				if (this.form.isValid()) {
					this.form.submit({
								params : {
									fileId : this.fileId
								},
								waitMsg : "In processing",
								failure : function(form, action) {
									Ext.MessageBox.alert("Error Message",
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.ownerCt.reloadListOrganization();
										this.organization.reset();
									} else {
										Ext.MessageBox.alert(
												feyaSoft.lang.common.error,
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							"Please fix the errors noted.");
				}
			}
		});
// 23号 27
Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.ShareWin = function(config) {
	this.config = config;
	this.myOwnerCt = config.myOwnerCt;
	this.componentClass = "documentShareFile";
	if (config.componentClass) {
		this.componentClass = config.componentClass;
	}
	this.sharePanel = new feyaSoft.home.common.share.SharePanel(config);
	this.contactPanel = new feyaSoft.home.common.share.ContactSharePanel(config);
	this.groupSharePanel = new feyaSoft.home.common.share.GroupSharePanel(config);
	this.organizationPanel = new feyaSoft.home.common.share.OrganizationSharePanel(config);
	this.mainTabPanel = new Ext.TabPanel({
				loadMask : true,
				border : false,
				activeTab : 0,
				bodyStyle : "padding:5px;background:none;",
				deferredRender : false,
				enableTabScroll : true,
				monitorResize : true,
				resizeTabs : true,
				tabWidth : 200,
				minTabWidth : 120,
				plugins : new Ext.ux.TabCloseMenu,
				layoutOnTabChange : true,
				items : [this.sharePanel, this.contactPanel,
						this.groupSharePanel, this.organizationPanel]
			});
	feyaSoft.home.common.share.ShareWin.superclass.constructor.call(this, {
				title : feyaSoft.lang.common.share + " [ " + config.name + " ]",
				width : 800,
				height : 450,
				layout : "fit",
				shim : false,
				animCollapse : false,
				constrainHeader : true,
				modal : true,
				buttonAlign : "center",
				items : [this.mainTabPanel]
			});
	this.mainTabPanel.on("afterrender", this.onTabAfterRenderFn, this, {
				single : true
			});
	this.mainTabPanel.on("tabchange", this.onTabChangeFn, this);
	this.show();
};
Ext.extend(feyaSoft.home.common.share.ShareWin, Ext.Window, {
			onTabChangeFn : function(tabs, atab) {
				atab.initLoad(this.config);
			},
			onTabAfterRenderFn : function(tabs, atab) {
				Ext.Ajax.request({
							url : this.componentClass
									+ "/allowInviteAndPermission",
							params : {
								fileId : this.config.fileId
							},
							method : "GET",
							success : function(result, request) {
								var jsonData = Ext.util.JSON
										.decode(result.responseText);
								if (jsonData.allowInvite) {
									this.sharePanel.setDisabled(false);
								}
								if (jsonData.permission < 2) {
									this.sharePanel.setDisabled(true);
								}
								if (jsonData.permission < 7) {
									this.contactPanel.setDisabled(true);
									this.groupSharePanel.setDisabled(true);
									this.organizationPanel.setDisabled(true);
								}
							},
							failure : function(result, request) {
								Ext.MessageBox
										.alert("Failed", "Internal Error");
							},
							scope : this
						});
			},
			activeReloadTab : function() {
				this.sharePanel.reload();
				try {
					this.myOwnerCt.reload();
				} catch (e) {
				}
			}
		});
// 24号 28
Ext.ns("feyaSoft.home.common.document");
feyaSoft.home.common.document.EditingCommentList = Ext.extend(Ext.Panel, {});
// 26号 30
Ext.ns("Ext.ux.dialog");
Ext.ux.dialog.DirectorySelector = Ext.extend(Ext.form.TriggerField, {
			resizable : true,
			expandOnFocus : true,
			minListWidth : 0,
			maxHeight : 300,
			editable : false,
			listAlign : "tl-bl?",
			initComponent : function() {
				Ext.ux.dialog.DirectorySelector.superclass.initComponent
						.call(this);
				this.addEvents("changedirectory");
				this.on("expand", this._onExpandFn, this);
			},
			_onExpandFn : function() {
				var path = this.getValue();
				this.innerTree.selectPath(path, "text");
			},
			onRender : function(ct, position) {
				Ext.ux.dialog.DirectorySelector.superclass.onRender.call(this,
						ct, position);
				if (!this.lazyInit) {
					this.initList();
				} else {
					this.on("focus", this.initList, this, {
								single : true
							});
				}
			},
			onFocus : function() {
				Ext.ux.dialog.DirectorySelector.superclass.onFocus.call(this);
				if (true == this.expandOnFocus) {
					this.expand();
				}
			},
			initList : function() {
				if (!this.list) {
					var cls = "x-combo-list";
					this.list = new Ext.Layer({
								shadow : this.shadow,
								cls : [cls, this.listClass].join(" "),
								constrain : false
							});
					var lw = this.listWidth
							|| Math
									.max(this.wrap.getWidth(),
											this.minListWidth);
					this.list.setWidth(lw);
					this.list.swallowEvent("mousewheel");
					this.assetHeight = 0;
					this.innerList = this.list.createChild({
								cls : cls + "-inner"
							});
					this.innerList.setWidth(lw - this.list.getFrameWidth("lr"));
					this.innerTree = new Ext.tree.TreePanel({
								layout : "fit",
								useArrows : true,
								autoScroll : true,
								animate : true,
								rootVisible : this.rootVisible || false,
								border : false,
								root : this.root
										|| new Ext.tree.AsyncTreeNode({
													draggable : false,
													cls : "folder",
													leaf : false,
													expanded : true,
													expandable : true
												}),
								loader : this.urlLoader
										|| new Ext.tree.TreeLoader({
													dataUrl : this.url
												}),
								renderTo : this.innerList
							});
					this.innerTree.on("click", this.onNodeClickFn, this);
					if (this.resizable) {
						this.resizer = new Ext.Resizable(this.list, {
									pinned : true,
									handles : "se"
								});
						this.resizer.on("resize", function(r, w, h) {
									this.innerTree.setHeight(h);
									this.maxHeight = h - this.handleHeight
											- this.list.getFrameWidth("tb")
											- this.assetHeight;
									this.listWidth = w;
									this.innerList.setWidth(w
											- this.list.getFrameWidth("lr"));
									this.restrictHeight();
								}, this);
						this.innerList.setStyle("margin-bottom",
								this.handleHeight + "px");
					}
				}
			},
			onNodeClickFn : function(node, e) {
				this.collapse.defer(1, this);
				this.fireEvent("changedirectory", this, node, node.id);
			},
			restrictHeight : function() {
				this.innerList.dom.style.height = "";
				var inner = this.innerList.dom;
				var pad = this.list.getFrameWidth("tb")
						+ (this.resizable ? this.handleHeight : 0)
						+ this.assetHeight;
				var h = Math.max(inner.clientHeight, inner.offsetHeight,
						inner.scrollHeight);
				var ha = this.getPosition()[1] - Ext.getBody().getScroll().top;
				var hb = Ext.lib.Dom.getViewHeight() - ha
						- this.getSize().height;
				var space = Math.max(ha, hb, this.minHeight || 0)
						- this.list.shadowOffset - pad - 5;
				h = Math.min(h, space, this.maxHeight);
				this.innerList.setHeight(h);
				this.list.beginUpdate();
				this.list.setHeight(h + pad);
				this.list.alignTo(this.wrap, this.listAlign);
				this.list.endUpdate();
			},
			isExpanded : function() {
				return this.list && this.list.isVisible();
			},
			collapse : function() {
				if (!this.isExpanded()) {
					return;
				}
				this.list.hide();
				Ext.getDoc().un("mousewheel", this.collapseIf, this);
				Ext.getDoc().un("mousedown", this.collapseIf, this);
				this.fireEvent("collapse", this);
			},
			collapseIf : function(e) {
				if (!e.within(this.wrap) && !e.within(this.list)) {
					this.collapse();
				}
			},
			expand : function() {
				if (this.isExpanded() || !this.hasFocus) {
					return;
				}
				this.list.alignTo(this.wrap, this.listAlign);
				this.list.show();
				this.innerList.setOverflow("auto");
				Ext.getDoc().on("mousewheel", this.collapseIf, this);
				Ext.getDoc().on("mousedown", this.collapseIf, this);
				this.fireEvent("expand", this);
			},
			onTriggerClick : function() {
				if (this.disabled) {
					return;
				}
				if (this.isExpanded()) {
					this.collapse();
				} else {
					this.el.focus();
					this.expand();
				}
			}
		});
Ext.ns("Ext.ux.dialog");
Ext.ux.dialog.getFileTypeStore = function() {
	var store = new Ext.data.SimpleStore({
				fields : ["id", "text"],
				data : [["all", "All Types"], ["myXls", ".xls"],
						["myDoc", ".doc"], ["myPpt", ".ppt"],
						["myPMgr", ".pm"], ["myFlow", ".flow"]]
			});
	return store;
};
Ext.ux.dialog.FileDialog = Ext.extend(Ext.Window, {});
Ext.ux.dialog.OpenDialog = new Ext.ux.dialog.FileDialog({
			title : feyaSoft.lang.common.open,
			applyDisabled : true,
			popup : function(cb, scope, ename) {
				this.callback = cb.createDelegate(scope || this);
				this.typeCombo.setValue(ename);
				this.exname = ename;
				this.directSelector.setValue("/ROOT");
				this.show();
			},
			onRowSelectFn : function(sm, rowIndex, r) {
				var exname = r.data.exname;
				if ("File Folder" == exname || "myFolder" == exname) {
					this.applyBtn.disable();
				} else {
					this.nameField.setValue(r.data.name);
					this.applyBtn.enable();
				}
			},
			onApplyFn : function() {
				var sm = this.list.getSelectionModel();
				var srd = sm.getSelected();
				var fileId = srd.data.id;
				this.callback.defer(1, this, [fileId, srd.data]);
				sm.clearSelections();
				this.nameField.reset();
				this.hide();
			}
		});
Ext.ux.dialog.SaveDialog = new Ext.ux.dialog.FileDialog({
			title : feyaSoft.lang.common.saveAs,
			applyText : "Save",
			popup : function(cb, scope, ename) {
				this.callback = cb.createDelegate(scope || this);
				this.typeCombo.setValue(ename);
				this.exname = ename;
				this.directSelector.setValue("/ROOT");
				this.show();
			},
			onRowSelectFn : function(sm, rowIndex, r) {
				var exname = r.data.exname;
				if ("File Folder" != exname && "myFolder" != exname) {
					this.nameField.setValue(r.data.name);
					this.applyBtn.enable();
				}
			},
			onApplyFn : function() {
				if (this.nameField.isValid()) {
					var sm = this.list.getSelectionModel();
					var name = this.nameField.getValue();
					var st = this.list.getStore();
					var fileId;
					for (var i = 0; i < st.getCount(); i++) {
						var r = st.getAt(i);
						if (r.get("name") == name) {
							fileId = r.get("id");
						}
					}
					var folderId = this.list.getStore().lastOptions.params.folderId;
					if (fileId) {
						Ext.Msg.show({
									title : feyaSoft.lang.common.hint,
									msg : "["
											+ name
											+ "] "
											+ feyaSoft.lang.common.existNreplace,
									buttons : Ext.Msg.YESNO,
									fn : function(bid, text) {
										if ("yes" == bid) {
											this.callback.defer(1, this, [
															folderId, name,
															fileId]);
											this.nameField.reset();
											sm.clearSelections();
											this.hide();
										} else {
											return;
										}
									},
									scope : this
								});
					} else {
						this.callback.defer(1, this, [folderId, name]);
						this.nameField.reset();
						sm.clearSelections();
						this.hide();
					}
				}
			}
		});
// 28号 32
Ext.ns("Ext.ux.dialog");
Ext.ux.dialog.ConflictDialog = Ext.extend(Ext.Window, {
	title : "Conflict",
	width : 360,
	height : 240,
	modal : true,
	closiable : true,
	bodyStyle : "padding:10px;",
	resizable : false,
	constrain : true,
	constrainHeader : true,
	minimizable : false,
	maximizable : false,
	stateful : false,
	plain : true,
	initComponent : function() {
		var username = this.conflict.username;
		this.cls = "x-window-dlg";
		this.defaults = {
			style : "margin:5px;"
		};
		var html = [
				"<div class=\"ext-mb-icon ext-mb-question\"></div>",
				"<div class=\"ext-mb-content\"><span class=\"ext-mb-text\">",
				"There is another user [ <b>"
						+ username
						+ "</b> ] updated this file during your editing. <br><br>Please choose one of the actions below : ",
				"</span></div>"].join("");
		this.items = [{
					xtype : "label",
					style : "line-height:16px;",
					html : html
				}, {
					xtype : "container",
					layout : {
						type : "hbox",
						align : "stretchmax"
					},
					items : [{
								xtype : "button",
								scale : "large",
								text : feyaSoft.lang.conflict.overwrite,
								minWidth : 100,
								handler : this.onOverwrite,
								scope : this
							}, {
								xtype : "box",
								cls : "x-view-emtpytext",
								style : "padding:5px;margin:0;text-align:center;color:red;border-style:dashed;",
								margins : "0 0 0 5",
								flex : 1,
								html : feyaSoft.lang.conflict.overwrite_tip
							}]
				}, {
					xtype : "container",
					layout : {
						type : "hbox",
						align : "stretchmax"
					},
					items : [{
								xtype : "button",
								scale : "large",
								text : feyaSoft.lang.conflict.acceptChange,
								minWidth : 100,
								handler : this.onAcceptChange,
								scope : this
							}, {
								xtype : "box",
								cls : "x-view-emtpytext",
								style : "padding:5px;margin:0;text-align:center;color:red;border-style:dashed;",
								margins : "0 0 0 5",
								flex : 1,
								html : feyaSoft.lang.conflict.acceptChange_tip
							}]
				}, {
					xtype : "container",
					layout : {
						type : "hbox",
						align : "stretchmax"
					},
					items : [{
								xtype : "button",
								scale : "large",
								text : feyaSoft.lang.conflict.saveAs,
								minWidth : 100,
								handler : this.onSaveAs,
								scope : this
							}, {
								xtype : "box",
								cls : "x-view-emtpytext",
								style : "padding:5px;margin:0;text-align:center;color:blue;border-style:dashed;",
								margins : "0 0 0 5",
								flex : 1,
								html : feyaSoft.lang.conflict.saveAs_tip
							}]
				}];
		Ext.ux.dialog.ConflictDialog.superclass.initComponent.call(this);
	},
	onSaveAs : function() {
		if (this.saveAsCallback) {
			this.saveAsCallback.call(this.scope);
		}
		this.close();
	},
	onOverwrite : function() {
		if (this.overwriteCallback) {
			this.overwriteCallback.call(this.scope);
		}
		this.close();
	},
	onAcceptChange : function() {
		if (this.acceptChangeCallback) {
			this.acceptChangeCallback.call(this.scope);
		}
		this.close();
	}
});
// 29号 33
Ext.ns("Ext.ux.dialog");
Ext.ux.dialog.LookupWin = function(config) {
	var lan_common = feyaSoft.lang.common;
	this.searchpanel = new Ext.ux.dialog.SearchPanel(Ext.apply({
				title : lan_common.find
			}, config));
	this.replacepanel = new Ext.ux.dialog.SearchPanel(Ext.apply({
				title : lan_common.replace,
				replaceFlag : true
			}, config));
	this.tabpanel = new Ext.TabPanel({
				border : false,
				activeTab : 0,
				deferredRender : false,
				resizeTabs : true,
				tabWidth : 100,
				minTabWidth : 0,
				layoutOnTabChange : true,
				hideMode : "offsets",
				items : [this.searchpanel, this.replacepanel]
			});
	Ext.ux.dialog.LookupWin.superclass.constructor.call(this, {
				title : "Find and Replace",
				width : 400,
				height : 240,
				closeAction : "hide",
				layout : "fit",
				items : [this.tabpanel]
			});
};
Ext.extend(Ext.ux.dialog.LookupWin, Ext.Window, {
			manager : {
				register : Ext.emptyFn,
				unregister : Ext.emptyFn,
				bringToFront : Ext.emptyFn,
				sendToBack : Ext.emptyFn
			},
			popup : function(config) {
				this.show();
			}
		});
Ext.ux.dialog.SearchPanel = function(config) {
	Ext.apply(this, config);
	var lan_common = feyaSoft.lang.common;
	this.findCombo = new Ext.form.ComboBox({
				fieldLabel : lan_common.find_what,
				store : new Ext.data.SimpleStore({
							fields : ["text"],
							data : []
						}),
				displayField : "text",
				valueField : "text",
				typeAhead : true,
				mode : "local",
				triggerAction : "all",
				selectOnFocus : true,
				anchor : "95%",
				minChars : 1,
				allowBlank : false
			});
	if (this.replaceFlag) {
		this.replaceCombo = new Ext.form.ComboBox({
					fieldLabel : lan_common.replace_with,
					store : new Ext.data.SimpleStore({
								fields : ["text"],
								data : []
							}),
					displayField : "text",
					valueField : "text",
					typeAhead : true,
					mode : "local",
					triggerAction : "all",
					selectOnFocus : true,
					anchor : "95%",
					minChars : 1,
					allowBlank : false
				});
		this.replaceBtn = new Ext.Button({
					minWidth : 80,
					text : lan_common.replace,
					handler : this.onReplaceAllFn,
					scope : this
				});
		this.replaceAllBtn = new Ext.Button({
					minWidth : 80,
					text : lan_common.replace_all,
					handler : this.onReplaceAllFn,
					scope : this
				});
	}
	this.upRadio = new Ext.form.Radio({
				boxLabel : lan_common.up,
				name : "direction"
			});
	this.downRadio = new Ext.form.Radio({
				boxLabel : lan_common.down,
				checked : true,
				name : "direction"
			});
	this.caseCB = new Ext.form.Checkbox({
				boxLabel : lan_common.match_case
			});
	this.findNextBtn = new Ext.Button({
				minWidth : 80,
				text : lan_common.find_next,
				handler : this.onFindNextFn,
				scope : this
			});
	this.closeBtn = new Ext.Button({
				minWidth : 80,
				text : lan_common.close,
				handler : this.onCloseFn,
				scope : this
			});
	var items = [this.findCombo];
	var buttons = [this.findNextBtn];
	if (this.replaceFlag) {
		items.push(this.replaceCombo);
		buttons.push(this.replaceBtn);
		buttons.push(this.replaceAllBtn);
	}
	buttons.push(this.closeBtn);
	items.push({
				xtype : "radiogroup",
				fieldLabel : feyaSoft.lang.common.direction,
				items : [this.upRadio, this.downRadio]
			});
	items.push(this.caseCB);
	this.formpanel = new Ext.form.FormPanel({
				border : false,
				labelWidth : 100,
				hideMode : "offsets",
				style : "padding:10px",
				items : items,
				buttonAlign : "center",
				buttons : buttons
			});
	this.sbar = new Ext.ux.StatusBar({
				defaultText : "Ready",
				defaultIconCls : "x-status-valid",
				plugins : new Ext.ux.ValidationStatus({
							showText : "Please input a target to find",
							validText : "Please click to find target!",
							form : this.formpanel.getId()
						})
			});
	Ext.ux.dialog.SearchPanel.superclass.constructor.call(this, {
				border : false,
				layout : "border",
				items : [{
							border : false,
							region : "center",
							layout : "fit",
							items : [this.formpanel]
						}],
				bbar : this.sbar
			});
};
Ext.extend(Ext.ux.dialog.SearchPanel, Ext.Panel, {
			onReplaceAllFn : function() {
				if (this.formpanel.form.isValid()) {
					var target = this.findCombo.getValue();
					var replace = this.replaceCombo.getValue();
					var ret = this.replaceText(target, replace, {
								direction : this.upRadio.getValue(),
								matchcase : this.caseCB.getValue(),
								all : true
							});
					var lan_common = feyaSoft.lang.common;
					if (ret) {
						this.sbar.setStatus({
									iconCls : "x-status-valid",
									text : lan_common.all_replaced
								});
					} else {
						this.sbar.setStatus({
									iconCls : "x-status-error",
									text : lan_common.no_found
								});
					}
				}
			},
			onReplaceFn : function() {
				if (this.formpanel.form.isValid()) {
					var target = this.findCombo.getValue();
					var replace = this.replaceCombo.getValue();
					var ret = this.replaceText(target, replace, {
								direction : this.upRadio.getValue(),
								matchcase : this.caseCB.getValue()
							});
					var lan_common = feyaSoft.lang.common;
					if (ret) {
						this.sbar.setStatus({
									iconCls : "x-status-valid",
									text : lan_common.replaced
								});
					} else {
						this.sbar.setStatus({
									iconCls : "x-status-error",
									text : lan_common.no_found
								});
					}
				}
			},
			onFindNextFn : function() {
				if (this.formpanel.form.isValid()) {
					var target = this.findCombo.getValue();
					var ret = this.findText(target, {
								direction : this.upRadio.getValue(),
								matchcase : this.caseCB.getValue()
							});
					var lan_common = feyaSoft.lang.common;
					if (ret) {
						this.sbar.setStatus({
									iconCls : "x-status-valid",
									text : lan_common.found
								});
					} else {
						this.sbar.setStatus({
									iconCls : "x-status-error",
									text : lan_common.no_found
								});
					}
				}
			},
			onCloseFn : function() {
				this.ownerCt.ownerCt.hide();
			}
		});
Ext.ns("Ext.ux.dialog");
Ext.ns("feyaSoft.home.program.photo");
feyaSoft.home.program.photo.ImageDetailPanel = function(config) {
	this.config = config || {};
	Ext.apply(this, config);
	this.eastPanel = new feyaSoft.home.program.photo.ImageView;
	this.centerPanel = new feyaSoft.home.program.photo.ImageListPanel(config);
	if (config && config.eastPanelShow && config.eastPanelShow == "not") {
		feyaSoft.home.program.photo.ImageDetailPanel.superclass.constructor
				.call(this, {
							region : "center",
							style : "padding:5px 5px 5px 0px;",
							border : false,
							layout : "border",
							items : [this.centerPanel]
						});
	} else {
		feyaSoft.home.program.photo.ImageDetailPanel.superclass.constructor
				.call(this, {
							region : "center",
							style : "padding:5px 5px 5px 0px;",
							border : false,
							layout : "border",
							items : [this.eastPanel, this.centerPanel]
						});
	}
};
Ext.extend(feyaSoft.home.program.photo.ImageDetailPanel, Ext.Panel, {
			doSelectImage : function() {
				if (this.centerPanel.getSelectedImage) {
					var selNode = this.centerPanel.getSelectedImage();
					this.config.whoPopMe.doAssignImage(selNode.id);
					this.ownerCt.close();
				}
			}
		});
feyaSoft.home.program.photo.ImageListPanel = function(config) {
	config = config || {};
	Ext.apply(this, config);
	this.dataStore = new Ext.data.JsonStore({
				autoDestroy : true,
				url : "myPhoto/list",
				fields : []
			});
	var tpl = new Ext.XTemplate(
			"<tpl for=\".\">",
			"<div class=\"thumb-wrap\" id=\"{id}\" style=\"-moz-user-select: none;\">",
			"<div class=\"thumb\" style=\"-moz-user-select: none;\"><a href=\"imgAttach?id={id}\" title=\"{name}\"><img src=\"{imagepath}\" title=\"{name}\" style=\"-moz-user-select: none;\"></a></div>",
			"<span>{shortName}</span>", "</div>", "</tpl>",
			"<div class=\"x-clear\"></div>");
	this.shareBtn = new Ext.Toolbar.SplitButton({
				id : "tbar-share",
				iconCls : "share",
				text : feyaSoft.lang.common.share,
				scope : this,
				handler : this.shareFile,
				menu : {
					items : [{
								text : feyaSoft.lang.file.shareWithOther,
								tooltip : "Share images with my contract",
								iconCls : "share",
								handler : this.shareFile,
								scope : this
							}, {
								text : feyaSoft.lang.file.addFriend,
								tooltip : "Add or invite friend",
								iconCls : "addItem",
								handler : function() {
									new feyaSoft.home.common.AddContactWin;
								},
								scope : this
							}]
				}
			});
	this.uploadButton = new Ext.Button({
		text : feyaSoft.lang.common.upload,
		tooltip : "Upload a new image",
		iconCls : "addItem",
		handler : function() {
			var node = Ext.getCmp("image-win-west-folder-panel").fileTree.selModel.selNode;
			if (node == null) {
				Ext.MessageBox.alert(feyaSoft.lang.common.failed,
						"Please select one of folders to process.");
			} else {
				new feyaSoft.home.program.photo.CreateEditImage({
							myOwner : this
						});
			}
		},
		scope : this
	});
	this.deleteButton = new Ext.Button({
				text : feyaSoft.lang.common['delete'],
				tooltip : "Delete the selected image",
				iconCls : "delete",
				handler : function() {
					var selNode = this.view.getSelectedNodes()[0];
					if (selNode) {
						this.deleteImage(selNode);
					} else {
						Ext.MessageBox.alert(feyaSoft.lang.common.failed,
								feyaSoft.lang.common.pleaseSelectOne);
					}
				},
				scope : this
			});
	this.menubar = [this.uploadButton, {
		text : feyaSoft.lang.common.download,
		tooltip : "Download the selected image",
		iconCls : "download",
		handler : function() {
			var selNode = this.view.getSelectedNodes()[0];
			if (selNode) {
				this.downloadImage(selNode);
			} else {
				Ext.MessageBox.alert(feyaSoft.lang.common.failed,
						feyaSoft.lang.common.pleaseSelectOne);
			}
		},
		scope : this
	}, "-", this.deleteButton, "-", {
		text : feyaSoft.lang.common.open,
		tooltip : "Open the selected image",
		iconCls : "editItem",
		handler : this.onImageEditor,
		scope : this
	}, "-", this.shareBtn];
	this.view = new Ext.DataView({
				store : this.dataStore,
				tpl : tpl,
				autoHeight : true,
				singleSelect : true,
				overClass : "x-view-over-thumb",
				itemSelector : "div.thumb-wrap",
				emptyText : "<div style=\"padding:10px;\">"
						+ feyaSoft.lang.photo.notImage + "</div>",
				prepareData : function(data) {
					data.shortName = Ext.util.Format.ellipsis(data.name, 20);
					return data;
				}
			});
	feyaSoft.home.program.photo.ImageListPanel.superclass.constructor.call(
			this, {
				id : "image-win-list-images-panel",
				region : "center",
				border : true,
				layout : "fit",
				tbar : this.menubar,
				bbar : ["->", "-", feyaSoft.lang.common.search, "",
						new Ext.ux.form.SearchField({
									autoShow : true,
									store : this.dataStore
								}), "-"],
				autoScroll : true,
				cls : "feyasoft-myPhoto-view",
				items : [this.view]
			});
	this.view.on("click", this.onViewImageClickFn, this);
	this.view.on("dblclick", this.onImageDblClickFn, this);
	this.view.on("afterrender", this.onAfterrenderFn, this);
};
Ext.extend(feyaSoft.home.program.photo.ImageListPanel, Ext.Panel, {
			onAfterrenderFn : function() {
				new Ext.dd.DragZone(this.view.getEl(), {
							scroll : false,
							ddGroup : "folderTreeDD",
							parentCom : this,
							getDragData : function(e) {
								return this.parentCom.getDragData(e);
							},
							getRepairXY : function(e, data) {
								this.parentCom.getRepairXY(this);
							}
						});
			},
			getDragData : function(e) {
				var target = e.getTarget(".x-view-over-thumb");
				if (target) {
					var view = this.view;
					if (!view.isSelected(target)) {
						view.onClick(e);
					}
					var selNodes = view.getSelectedNodes();
					if (selNodes.length > 0) {
						var dragData = {
							selectelements : selNodes,
							selections : view.getSelectedRecords()
						};
						var divtmp = document.createElement("div");
						divtmp.appendChild(selNodes[0].firstChild
								.cloneNode(true));
						dragData.ddel = divtmp;
						dragData.single = true;
						return dragData;
					} else {
						return false;
					}
				}
				return false;
			},
			getRepairXY : function(dragzone) {
				if (!dragzone.dragData.multi) {
					var xy = Ext.Element.fly(dragzone.dragData.ddel).getXY();
					xy[0] += 3;
					xy[1] += 3;
					return xy;
				}
				return false;
			},
			getSelectedImage : function() {
				var selNode = this.view.getSelectedNodes()[0];
				return selNode;
			},
			reload : function() {
				this.dataStore.reload();
			},
			loadImages : function(folderId, isShared) {
				this.folderid = folderId;
				this.dataStore.baseParams = {
					folderId : folderId
				};
				this.dataStore.load();
				if (isShared) {
					this.uploadButton.setDisabled(true);
					if (folderId == "singleSharedImage") {
						this.deleteButton.setDisabled(false);
						this.shareBtn.setDisabled(false);
					} else {
						this.deleteButton.setDisabled(true);
						this.shareBtn.setDisabled(true);
					}
				} else {
					this.uploadButton.setDisabled(false);
					this.deleteButton.setDisabled(false);
					this.shareBtn.setDisabled(false);
				}
			},
			onImageEditor : function() {
				var selNode = this.view.getSelectedNodes()[0];
				var selIdx = this.view.getSelectedIndexes()[0];
				if (selNode) {
					var newWindow = window.open("myPhotoShow?folderId="
									+ this.folderid + "&index=" + selIdx,
							"_blank");
					newWindow.focus();
				} else {
					Ext.MessageBox.alert(feyaSoft.lang.common.failed,
							feyaSoft.lang.common.pleaseSelectOne);
				}
			},
			onViewImageClickFn : function(dv, index, node, e) {
				var rd = dv.store.getAt(index);
				var owner = this.ownerCt.ownerCt.ownerCt;
				if (owner) {
					try {
						if (typeof owner.selectImage == "function") {
							owner.selectImage("imgAttach?id=" + rd.data.id);
						} else {
							Ext.getCmp("image-win-view-detail-image-panel")
									.showDetailView(rd.data);
						}
					} catch (e) {
						Ext.getCmp("image-win-view-detail-image-panel")
								.showDetailView(rd.data);
					}
				} else {
					Ext.getCmp("image-win-view-detail-image-panel")
							.showDetailView(rd.data);
				}
			},
			onImageDblClickFn : function(dv, index, node, e) {
				var rd = dv.store.getAt(index);
				var newWindow = window.open("myPhotoShow?folderId="
								+ this.folderid + "&index=" + index, "_blank");
				newWindow.focus();
			},
			deleteImage : function(selNode) {
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirmDelete,
							msg : feyaSoft.lang.common.confirmDeleteDesc,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onDeleteConfirm,
							scope : this,
							record : selNode
						});
			},
			onDeleteConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : "myPhoto/delete",
								method : "POST",
								params : {
									id : options.record.id
								},
								success : function(result, request) {
									var jsonData = Ext.util.JSON
											.decode(result.responseText);
									if (jsonData.success == "true") {
										this.dataStore.load();
									} else {
										Ext.MessageBox.alert(
												feyaSoft.lang.common.failed,
												jsonData.info);
									}
								},
								failure : function(result, request) {
									Ext.MessageBox.alert(
											feyaSoft.lang.common.failed,
											feyaSoft.lang.common.errorInternal);
								},
								scope : this
							});
				}
			},
			downloadImage : function(selNode) {
				var url = "imgAttach?id=" + selNode.id;
				window.open(url, "Download");
			},
			shareFile : function() {
				var record = this.view.getSelectedNodes()[0];
				if (record == null || record.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				if (record.allowInvite == null || record.allowInvite == true) {
					new feyaSoft.home.common.share.ShareWin({
								fileId : record.id,
								name : "Photo",
								myOwnerCt : this,
								componentClass : "myPhotoShare"
							});
				} else {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.file.youNotAllowedShared);
				}
			}
		});
feyaSoft.home.program.photo.ImageView = function() {
	this.detailsTemplate = new Ext.XTemplate(
			"<div class=\"details\">",
			"<tpl for=\".\">",
			"<img class=\"feyasoft_photo_showImage\" src=\"imgAttach?id={id}\" ><div class=\"details-info\">",
			"<b>" + feyaSoft.lang.home.imageName + ":</b>",
			"<span>{name}</span>", "<b>" + feyaSoft.lang.common.size + ":</b>",
			"<span>{size} KB</span>", "<b>" + feyaSoft.lang.common.dateModified
					+ ":</b>", "<span>{createDate}</span>", "<b>"
					+ feyaSoft.lang.common.description + ":</b>",
			"<span>{description}</span></div>", "</tpl>", "</div>");
	this.detailsTemplate.compile();
	feyaSoft.home.program.photo.ImageView.superclass.constructor.call(this, {
				id : "image-win-view-detail-image-panel",
				layout : "fit",
				region : "east",
				autoScroll : true,
				split : true,
				width : 525,
				html : ""
			});
};
Ext.extend(feyaSoft.home.program.photo.ImageView, Ext.Panel, {
			showDetailView : function(data) {
				this.detailsTemplate.overwrite(this.body, data);
			},
			reload : function() {
				this.fileTree.root.reload({
							delay : 750
						});
			}
		});
feyaSoft.home.program.photo.CreateEditFolder = function(config) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.myOwner = config.myOwner;
	this.config = config;
	this.name = new Ext.form.TextField({
				fieldLabel : feyaSoft.lang.common.name,
				allowBlank : false,
				name : "name",
				anchor : "95%"
			});
	this.formPanel = new Ext.form.FormPanel({
				id : "formPanel",
				baseCls : "x-plain",
				labelWidth : 100,
				url : "myPhotoFolder/createUpdate",
				defaultType : "textfield",
				items : [this.name]
			});
	var title = feyaSoft.lang.common.addFolder;
	if (config.node) {
		title = feyaSoft.lang.common.editFolder;
	}
	feyaSoft.home.program.photo.CreateEditFolder.superclass.constructor.call(
			this, {
				title : title,
				width : 550,
				height : 120,
				minWidth : 400,
				minHeight : 100,
				layout : "fit",
				bodyStyle : "padding:10px;",
				buttonAlign : "center",
				shim : false,
				animCollapse : false,
				constrainHeader : true,
				modal : true,
				items : this.formPanel,
				resizable : false,
				keys : {
					key : [13],
					fn : function() {
						this.saveClose();
					},
					scope : this
				},
				buttons : [{
							text : feyaSoft.lang.common.cancel,
							handler : function() {
								this.close();
							},
							scope : this
						}, {
							text : feyaSoft.lang.common.saveClose,
							handler : function() {
								this.saveClose();
							},
							scope : this
						}]
			});
	this.on("afterlayout", function() {
				if (config.node) {
					this.name.setValue(config.node.text);
				}
			}, this, {
				single : true
			});
	this.show();
};
Ext.extend(feyaSoft.home.program.photo.CreateEditFolder, Ext.Window, {
			saveClose : function() {
				if (this.formPanel.form.isValid()) {
					var params = {};
					if (this.config.parentId) {
						params = {
							parentId : this.config.parentId
						};
					} else if (this.config.node) {
						params = {
							id : this.config.node.id
						};
					}
					this.formPanel.form.submit({
								waitMsg : "In processing",
								params : params,
								failure : function(form, action) {
									Ext.MessageBox.alert(
											feyaSoft.lang.common.error,
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										if (this.config.parentId) {
											var newNode = new Ext.tree.AsyncTreeNode(
													{
														id : action.result.id,
														text : this.name
																.getValue(),
														expandable : true,
														cls : "folder",
														parentId : this.config.parentId
													});
											this.myOwner.appendChild(
													this.config.parentId,
													newNode);
										} else if (this.config.node) {
											this.config.node.setText(this.name
													.getValue());
										}
										this.close();
									} else {
										Ext.MessageBox.alert(
												feyaSoft.lang.common.error,
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							"Please fix the errors noted.");
				}
			}
		});
Ext.ns("feyaSoft.home.program.photo");
feyaSoft.home.program.photo.CreateEditImage = function(config) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.myOwner = config.myOwner;
	this.name = new Ext.form.TextField({
				fieldLabel : feyaSoft.lang.photo.changeNewName,
				allowBlank : true,
				name : "name",
				anchor : "95%"
			});
	this.imagepath = new Ext.form.FileUploadField({
				allowBlank : false,
				emptyText : feyaSoft.lang.common.selectImage,
				fieldLabel : feyaSoft.lang.common.photo,
				name : "imagepath",
				anchor : "95%",
				buttonText : "",
				buttonCfg : {
					iconCls : "upload-icon"
				}
			});
	this.note = new Ext.form.TextArea({
				fieldLabel : feyaSoft.lang.common.description,
				name : "description",
				height : 100,
				anchor : "95%"
			});
	var formPanel = new Ext.form.FormPanel({
				id : "formPanel",
				fileUpload : true,
				baseCls : "x-plain",
				labelWidth : 80,
				url : "myPhoto/createUpdate",
				defaultType : "textfield",
				items : [this.imagepath, this.note]
			});
	feyaSoft.home.program.photo.CreateEditImage.superclass.constructor.call(
			this, {
				title : feyaSoft.lang.common.upload,
				width : 550,
				height : 220,
				minWidth : 400,
				minHeight : 100,
				layout : "fit",
				bodyStyle : "padding:10px;",
				buttonAlign : "center",
				shim : false,
				animCollapse : false,
				constrainHeader : true,
				modal : true,
				items : formPanel,
				resizable : false,
				buttons : [{
							text : feyaSoft.lang.common.close,
							handler : function() {
								this.close();
							},
							scope : this
						}, {
							text : feyaSoft.lang.common.saveClose,
							handler : function() {
								var node = Ext
										.getCmp("image-win-west-folder-panel").fileTree.selModel.selNode;
								if (formPanel.form.isValid()) {
									var params = {
										folderId : node.id
									};
									formPanel.form.submit({
										waitMsg : "In processing",
										params : params,
										failure : function(form, action) {
											Ext.MessageBox.alert(
													feyaSoft.lang.common.error,
													action.result.info);
										},
										success : function(form, action) {
											var jsonData = action.result;
											if (jsonData.success) {
												Ext.Message
														.msgStay(
																feyaSoft.lang.common.confirm,
																jsonData.info,
																2000);
												config.myOwner.reload();
												this.close();
											} else {
												Ext.MessageBox
														.alert(
																feyaSoft.lang.common.error,
																jsonData.info);
											}
										},
										scope : this
									});
								} else {
									Ext.MessageBox
											.alert(
													feyaSoft.lang.common.error,
													feyaSoft.lang.common.pleaseFixError);
								}
							},
							scope : this
						}]
			});
	this.on("afterlayout", function() {
				if (config.node) {
					this.name.setValue(config.node.text);
					this.isPublic.setValue(config.node.attributes.description);
				}
			}, this, {
				single : true
			});
	this.show();
};
Ext.extend(feyaSoft.home.program.photo.CreateEditImage, Ext.Window, {});
Ext.ns("feyaSoft.home.program.photo");
feyaSoft.home.program.photo.PhotoWin = Ext.extend(Ext.Window, {
			initComponent : function() {
				var westPanel = new feyaSoft.home.program.photo.WestPanel;
				var imageDetail = new feyaSoft.home.program.photo.ImageDetailPanel;
				feyaSoft.home.program.photo.PhotoWin.superclass.initComponent
						.call(Ext.apply(this, {
									layout : "border",
									autoScroll : true,
									items : [westPanel, imageDetail]
								}));
				this.on("show", this._onShowFn, this, {
							delay : 200,
							single : true
						});
			},
			_onShowFn : function(p) {
				p.setHeight(p.getSize().height + 1);
			}
		});
Ext.ns("feyaSoft.home.program.photo");
feyaSoft.home.program.photo.PrivateFolder = function() {
	var shareBtn = new Ext.Toolbar.SplitButton({
				iconCls : "share",
				text : feyaSoft.lang.common.shareFolder,
				scope : this,
				handler : this.shareFolder,
				menu : {
					items : [{
								text : feyaSoft.lang.file.shareWithOther,
								tooltip : "Share folder with friend",
								iconCls : "share",
								handler : this.shareFolder,
								scope : this
							}, {
								text : feyaSoft.lang.file.addFriend,
								tooltip : feyaSoft.lang.file.addFriend,
								iconCls : "addItem",
								handler : function() {
									new feyaSoft.home.common.AddContactWin;
								},
								scope : this
							}]
				}
			});
	this.menubar = [{
				text : feyaSoft.lang.common.add,
				iconCls : "folder_add",
				handler : this.createFolder,
				scope : this
			}, "-", shareBtn, "->", {
				tooltip : feyaSoft.lang.common.refresh,
				iconCls : "refresh",
				handler : function() {
					this.reloadTree();
				},
				scope : this
			}];
	this.urlLoader = new Ext.tree.TreeLoader({
				dataUrl : "myPhotoFolder/listTree",
				baseParams : {
					id : "0"
				}
			});
	this.fileTree = new Ext.tree.TreePanel({
				loader : this.urlLoader,
				rootVisible : false,
				lines : false,
				border : false,
				autoScroll : true,
				containerScroll : true,
				enableDD : true,
				root : new Ext.tree.AsyncTreeNode({
							text : "ROOT",
							expandable : true,
							draggable : false,
							cls : "folder",
							expanded : true,
							id : "0"
						}),
				ddGroup : "folderTreeDD",
				listeners : {
					contextmenu : {
						fn : this.onContextMenu,
						scope : this
					},
					click : {
						fn : this.onClickHandler,
						scope : this
					},
					nodedragover : {
						fn : this.onNodedragoverFn,
						scope : this
					},
					beforenodedrop : {
						fn : this.onBeforenodedropFn,
						scope : this
					}
				}
			});
	feyaSoft.home.program.photo.PrivateFolder.superclass.constructor.call(this,
			{
				id : "image-win-west-folder-panel",
				region : "center",
				layout : "fit",
				tbar : this.menubar,
				items : [this.fileTree]
			});
	this.fileTree.getLoader().on("load", function() {
				var root = this.fileTree.getRootNode();
				if (root.firstChild) {
					root.firstChild.select();
					var folderId = root.firstChild.id;
					Ext.getCmp("image-win-list-images-panel")
							.loadImages(folderId);
					root.firstChild.expand();
				}
			}, this, {
				single : true
			});
};
Ext.extend(feyaSoft.home.program.photo.PrivateFolder, Ext.Panel, {
			onNodedragoverFn : function(dropEvent) {
				var point = dropEvent.point;
				var target = dropEvent.target;
				var dd = dropEvent.source;
				var dropnode = dropEvent.dropNode;
				var root = this.fileTree.getRootNode();
				var firstFolder = root.firstChild;
				if (dd.tree) {
					if (point == "above"
							&& target.attributes.id == firstFolder.attributes.id) {
						dd.dragging = false;
						return false;
					} else if (point == "append"
							&& target.attributes.id == dropnode.attributes.id) {
						dd.dragging = false;
						return false;
					} else {
						dd.dragging = true;
						return true;
					}
				} else {
					if (point == "append") {
						return true;
					} else {
						dd.dragging = false;
						return false;
					}
				}
			},
			onBeforenodedropFn : function(dropEvent) {
				var target = dropEvent.target;
				var data = dropEvent.data;
				var point = dropEvent.point;
				var dropnode = dropEvent.dropNode;
				var dd = dropEvent.source;
				if (dd.tree) {
					var root = target.parentNode;
					if (point == "above") {
						var enod = target;
						root.insertBefore(dropnode, target);
						this.resetGroupSort(root);
					} else if (point == "below") {
						root.insertBefore(dropnode, target.nextSibling);
						this.resetGroupSort(root);
					} else if (point == "append") {
						var tid = target.attributes.id;
						var dpid = dropnode.attributes.parentId;
						if (tid != dpid) {
							Ext.Ajax.request({
										url : "myPhotoFolder/dragDrop",
										params : {
											targetFolderId : tid,
											dragNodeId : dropnode.attributes.id
										},
										method : "GET",
										success : function(result, request) {
											root.removeChild(dropnode);
											target.appendChild(dropnode);
											target.ui.expand();
										},
										failure : function(result, request) {
											dropEvent.cancel = true;
											Ext.MessageBox
													.alert(
															feyaSoft.lang.common.failed,
															"Internal Error");
										},
										scope : this
									});
						}
					}
				} else {
					var rd = data.selections[0];
					var tid = target.attributes.id;
					Ext.Ajax.request({
								url : "myPhoto/dragDrop",
								params : {
									folderId : tid,
									photoId : rd.data.id
								},
								method : "GET",
								success : function(result, request) {
									target.select();
									this.onClickHandler(target);
								},
								failure : function(result, request) {
									dropEvent.cancel = true;
									Ext.MessageBox.alert(
											feyaSoft.lang.common.failed,
											"Internal Error");
								},
								scope : this
							});
				}
			},
			resetGroupSort : function(root) {
				var cs = root.childNodes;
				var pid = root.attributes.id;
				var arys = [];
				for (var i = 0, len = cs.length; i < len; i++) {
					var node = cs[i];
					var fid = node.attributes.id;
					node.attributes.parentId = pid;
					arys.push({
								id : fid,
								orderIn : i,
								parentId : pid
							});
				}
				Ext.Ajax.request({
							url : "myPhotoFolder/resetOrderIn",
							params : {
								arys : Ext.encode(arys)
							},
							method : "POST",
							success : function(response, request) {
								var backdata = Ext.util.JSON
										.decode(response.responseText);
								if (backdata.success == "true") {
								}
							},
							scope : this
						});
			},
			shareFolder : function() {
				var node = this.fileTree.selModel.selNode;
				if (node == null || node.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				new feyaSoft.home.common.share.ShareWin({
							fileId : node.id,
							name : node.text,
							myOwnerCt : this,
							componentClass : "myPhotoFolderShare"
						});
			},
			createFolder : function() {
				var node = this.fileTree.selModel.selNode;
				var parentId = "0";
				if (null != node) {
					parentId = node.id;
				}
				new feyaSoft.home.program.photo.CreateEditFolder({
							myOwner : this,
							parentId : parentId
						});
			},
			onClickHandler : function(node, obj, options) {
				this.ownerCt.clearShareSelect();
				Ext.getCmp("image-win-list-images-panel").loadImages(node.id);
			},
			getTree : function() {
				return this.fileTree;
			},
			editFolder : function() {
				var node = this.fileTree.selModel.selNode;
				if (node == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.failed,
							feyaSoft.lang.common.pleaseSelectOne);
				} else {
					new feyaSoft.home.program.photo.CreateEditFolder({
								myOwner : this,
								node : node
							});
				}
			},
			deleteFolder : function() {
				var node = this.fileTree.selModel.selNode;
				if (node == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.failed,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirmDelete,
							msg : feyaSoft.lang.common.confirmDeleteDesc,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onDeleteConfirm,
							scope : this,
							node : node
						});
			},
			onDeleteConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : "myPhotoFolder/delete",
								method : "POST",
								params : {
									id : options.node.id
								},
								success : function(result, request) {
									var jsonData = Ext.util.JSON
											.decode(result.responseText);
									if (jsonData.success == "true") {
										var parentNode = options.node.parentNode;
										parentNode.removeChild(options.node);
									} else {
										Ext.MessageBox.alert("Error",
												jsonData.info);
									}
								},
								failure : function(result, request) {
									Ext.MessageBox.alert(
											feyaSoft.lang.common.failed,
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			onContextMenu : function(node, e) {
				if (!this.meun) {
					this.menu = new Ext.menu.Menu({
								items : [{
											iconCls : "folder_add",
											text : feyaSoft.lang.common.addFolder,
											scope : this,
											handler : this.createFolder
										}, {
											iconCls : "share",
											text : feyaSoft.lang.common.shareFolder,
											scope : this,
											handler : this.shareFolder
										}, {
											iconCls : "editItem",
											text : feyaSoft.lang.common.edit,
											scope : this,
											handler : this.editFolder
										}, {
											iconCls : "delete",
											text : feyaSoft.lang.common['delete'],
											scope : this,
											handler : this.deleteFolder
										}]
							});
				}
				node.select();
				var addBtn = this.menu.items.get(3);
				var selNode = this.fileTree.getSelectionModel()
						.getSelectedNode();
				var root = this.fileTree.getRootNode();
				var cs = root.childNodes;
				var firstFolder = root.firstChild;
				if (cs.length == 1
						&& selNode.attributes.id == firstFolder.attributes.id) {
					addBtn.setDisabled(true);
				} else {
					addBtn.setDisabled(false);
				}
				e.stopEvent();
				this.menu.showAt(e.getXY());
			},
			reload : function() {
				this.fileTree.root.reload({
							delay : 750
						});
				if (this.fileTree.getRootNode().firstChild) {
					this.fileTree.getRootNode().firstChild.select();
					var folderId = this.fileTree.getRootNode().firstChild.id;
					Ext.getCmp("image-win-list-images-panel")
							.loadImages(folderId);
					this.fileTree.getRootNode().firstChild.expand();
				}
			},
			reloadTree : function() {
				this.fileTree.root.reload({
							delay : 750
						});
			},
			reloadTreeFolder : function(folderId) {
				this.fileTree.root.reload({
							delay : 750
						});
				this.fileTree.getLoader().on("load", function() {
							if (this.fileTree.getNodeById(folderId) != null) {
								this.fileTree.getNodeById(folderId).select();
							}
						}, this, {
							single : true
						});
			},
			appendChild : function(parentId, newNode) {
				if (this.fileTree.getNodeById(parentId) != null) {
					this.fileTree.getNodeById(parentId).appendChild(newNode);
					this.fileTree.getNodeById(parentId).select();
				} else {
					this.reloadTreeFolder(parentId);
				}
			},
			cleanSelect : function() {
				this.fileTree.getRootNode().eachChild(function(child) {
							if (child.isSelected()) {
								child.unselect(true);
							}
						}, this);
			}
		});

Ext.ns("feyaSoft.home.program.photo");
feyaSoft.home.program.photo.SharedFolder = function() {
	this.urlLoader = new Ext.tree.TreeLoader({
				dataUrl : "myPhotoFolderShare/listTree",
				baseParams : {
					id : "init"
				}
			});
	this.fileTree = new Ext.tree.TreePanel({
				loader : this.urlLoader,
				rootVisible : false,
				lines : false,
				border : false,
				autoScroll : true,
				containerScroll : true,
				enableDD : true,
				dropConfig : {
					appendOnly : true
				},
				root : new Ext.tree.AsyncTreeNode({
							text : "ROOT",
							expandable : true,
							draggable : false,
							cls : "folder",
							expanded : true,
							id : "init"
						})
			});
	feyaSoft.home.program.photo.SharedFolder.superclass.constructor.call(this,
			{
				title : feyaSoft.lang.photo.imageSharedBy,
				height : 180,
				region : "south",
				split : true,
				layout : "fit",
				items : [this.fileTree]
			});
	this.fileTree.on("click", this.onClickHandler, this);
	this.fileTree.on("contextMenu", this.onContextMenu, this);
};
Ext.extend(feyaSoft.home.program.photo.SharedFolder, Ext.Panel, {
			onClickHandler : function(node, obj, options) {
				this.ownerCt.clearPrivateSelect();
				Ext.getCmp("image-win-list-images-panel").loadImages(node.id,
						true);
			},
			getTree : function() {
				return this.fileTree;
			},
			deleteFolder : function() {
				var node = this.fileTree.selModel.selNode;
				if (node == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.failed,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirmDelete,
							msg : feyaSoft.lang.common.confirmDeleteDesc,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onDeleteConfirm,
							scope : this,
							node : node
						});
			},
			onDeleteConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : "myPhotoFolderShare/remove",
								method : "POST",
								params : {
									id : options.node.id
								},
								success : function(result, request) {
									var jsonData = Ext.util.JSON
											.decode(result.responseText);
									if (jsonData.success == "true") {
										this.reloadTree();
									} else {
										Ext.MessageBox.alert("Error",
												jsonData.info);
									}
								},
								failure : function(result, request) {
									Ext.MessageBox.alert(
											feyaSoft.lang.common.failed,
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			onContextMenu : function(node, e) {
				if (!this.meun) {
					var menuDisable = false;
					if (node.id == "singleSharedImage"
							|| node.attributes.parentId != null) {
						menuDisable = true;
					}
					this.menu = new Ext.menu.Menu({
								items : [{
											iconCls : "delete",
											text : feyaSoft.lang.common['delete'],
											scope : this,
											disabled : menuDisable,
											handler : this.deleteFolder
										}]
							});
				}
				node.select();
				e.stopEvent();
				this.menu.showAt(e.getXY());
			},
			reloadTree : function() {
				this.fileTree.root.reload({
							delay : 750
						});
			},
			reloadTreeFolder : function(folderId) {
				this.fileTree.root.reload({
							delay : 750
						});
				this.fileTree.getLoader().on("load", function() {
							if (this.fileTree.getNodeById(folderId) != null) {
								this.fileTree.getNodeById(folderId).select();
							}
						}, this, {
							single : true
						});
			},
			appendChild : function(parentId, newNode) {
				if (this.fileTree.getNodeById(parentId) != null) {
					this.fileTree.getNodeById(parentId).appendChild(newNode);
					this.fileTree.getNodeById(parentId).select();
				} else {
					this.reloadTreeFolder(parentId);
				}
			},
			cleanSelect : function() {
				this.fileTree.getRootNode().eachChild(function(child) {
							if (child.isSelected()) {
								child.unselect(true);
							}
						}, this);
			}
		});

Ext.ns("feyaSoft.home.program.photo");
feyaSoft.home.program.photo.WestPanel = function(config) {
	config = config || {};
	this.privateFolder = new feyaSoft.home.program.photo.PrivateFolder;
	this.sharedFolder = new feyaSoft.home.program.photo.SharedFolder;
	feyaSoft.home.program.photo.WestPanel.superclass.constructor.call(this, {
				style : "padding:5px 0px 5px 5px;",
				layout : "border",
				region : "west",
				split : true,
				width : 220,
				minSize : 150,
				maxSize : 300,
				border : false,
				items : [this.privateFolder, this.sharedFolder]
			});
};
Ext.extend(feyaSoft.home.program.photo.WestPanel, Ext.Panel, {
			clearPrivateSelect : function() {
				this.privateFolder.cleanSelect();
			},
			clearShareSelect : function() {
				this.sharedFolder.cleanSelect();
			}
		});
Ext.ns("Ext.ss");
Ext.BLANK_IMAGE_URL = "/static/ux/office/js/extjs/resources/images/default/s.gif";
Ext.ss.CONST = {
	DEFAULT_DATE_FROMAT : "m/d/Y",
	DEFAULT_TIME_FROMAT : "H:i:s",
	MAIN_PATH : "/static/ux/office/js/feyaSoft/home/program/ss/",
	IMAGE_PATH : "/static/ux/office/js/feyaSoft/home/program/ss/resources/img",
	GADGETS_CONTAINER_PATH : "/static/ux/office/js/feyaSoft/home/program/ss/gadgets/container.html",
	posReg : /(([\'\"a-z0-9\s]+!)?[0-9]+:[0-9]+)|(([\'\"a-z0-9\s]+!)?\$[a-z]+\$[0-9]+(:\$[a-z]+\$[0-9]+)?)|(([\'\"a-z0-9\s]+!)?[a-z]+\$[0-9]+(:[a-z]+\$[0-9]+)?)|(([\'\"a-z0-9\s]+!)?\$[a-z]+[0-9]+(:\$[a-z]+[0-9]+)?)|(([\'\"a-z0-9\s]+!)?\$[a-z]+:\$[a-z]+)|(([\'\"a-z0-9\s]+!)?\$[0-9]+:\$[0-9]+)|(([\'\"a-z0-9\s]+!)?[a-z]+[0-9]+(:[a-z]+[0-9]+)?)|(([\'\"a-z0-9\s]+!)?[a-z]+:[a-z]+)/gi,
	ROW_AB : 1,
	COL_AB : 2,
	JS_RESERVED_WORD : ["do", "for", "while", "var", "extend", "Date", "true",
			"false", "const", "break", "int", "string", "x", "y", "ds",
			"sheetIndex"],
	cssHash : {
		3 : "x-spreadsheet-comment-cell",
		4 : "x-spreadsheet-hyperlink-cell"
	},
	copySourceID : "i4DA0mP5O1tGQ7H4hq",
	Comment : {
		ADD : 0,
		EDIT : 1
	},
	MAX_LEN_OF_HSTACK : 100,
	letters : {
		'0' : "A",
		'1' : "B",
		'2' : "C",
		'3' : "D",
		'4' : "E",
		'5' : "F",
		'6' : "G",
		'7' : "H",
		'8' : "I",
		'9' : "J",
		a : "K",
		b : "L",
		c : "M",
		d : "N",
		e : "O",
		f : "P",
		g : "Q",
		h : "R",
		i : "S",
		j : "T",
		k : "U",
		l : "V",
		m : "W",
		n : "X",
		o : "Y",
		p : "Z"
	},
	letters0 : {
		'1' : "A",
		'2' : "B",
		'3' : "C",
		'4' : "D",
		'5' : "E",
		'6' : "F",
		'7' : "G",
		'8' : "H",
		'9' : "I",
		a : "J",
		b : "K",
		c : "L",
		d : "M",
		e : "N",
		f : "O",
		g : "P",
		h : "Q",
		i : "R",
		j : "S",
		k : "T",
		l : "U",
		m : "V",
		n : "W",
		o : "X",
		p : "Y",
		q : "Z"
	},
	full2Short : {
		'font-size' : "fs",
		'font-family' : "ff",
		'text-align' : "ta",
		'word-wrap' : "ww",
		data : "d",
		color : "c",
		align : "a",
		display : "dp",
		tdcss : "td",
		format : "fm",
		'x-spreadsheet-bottom-silver' : "b-s",
		'x-spreadsheet-right-silver' : "r-s",
		'x-spreadsheet-bottom-black' : "b-b",
		'x-spreadsheet-right-black' : "r-b",
		'x-spreadsheet-bottom-none' : "b-n",
		'x-spreadsheet-right-none' : "r-n"
	},
	short2Full : {
		fs : "font-size",
		ff : "font-family",
		ta : "text-align",
		ww : "word-wrap",
		d : "data",
		c : "color",
		a : "align",
		dp : "display",
		td : "tdcss",
		fm : "format",
		'b-s' : "x-spreadsheet-bottom-silver",
		'r-s' : "x-spreadsheet-right-silver",
		'b-b' : "x-spreadsheet-bottom-black",
		'r-b' : "x-spreadsheet-right-black",
		'b-n' : "x-spreadsheet-bottom-none",
		'r-n' : "x-spreadsheet-right-none"
	}
};
Ext.ns("Ext.ss.common");
Ext.ss.common.Helper = {};
Ext.ns("Ext.ss.common");
Ext.ss.common.SpreadSheetHelper = {};
Ext.ns("Ext.ss.common");
Ext.ss.common.FunctionBoxHelper = {
	statisticalCommon : function(args, option) {
		var len = args.length;
		var ds, x, y, sheetIndex;
		ds = args[len - 1];
		y = args[len - 2];
		x = args[len - 3];
		sheetIndex = args[len - 4];
		len -= 4;
		if (len != 1) {
			throw "SS_ERROR_NA"
		}
		var val = args[0];
		if (Ext.isObject(val)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : val.ox,
						oy : val.oy
					}, {
						ox : val.oex,
						oy : val.oey
					});
			var minx = parseInt(val.ox) + x, maxx = parseInt(val.oex) + x, miny = parseInt(val.oy)
					+ y, maxy = parseInt(val.oey) + y;
			ds.checkSheetIndexValid(val.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(val.sheetIndex)) {
				curSheetIndex = val.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				val = ds.getCellValue(minx, miny, curSheetIndex);
				if (val === undefined || val === ""
						|| Ext.ss.common.Mask.isEmptyStr(val)) {
					if (option == "istext" || option == "isnumber"
							|| option == "islogical") {
						return false
					} else {
						if (option == "isblank") {
							return true
						} else {
							if (option == "n") {
								return 0
							}
						}
					}
				}
			} else {
				throw "SS_ERROR_VALUE"
			}
		} else {
			if (val === undefined || val === ""
					|| Ext.ss.common.Mask.isEmptyStr(val)) {
				if (option == "istext" || option == "isnumber"
						|| option == "islogical") {
					return false
				} else {
					if (option == "isblank") {
						return true
					} else {
						if (option == "n") {
							return 0
						}
					}
				}
			}
		}
		if (option == "isblank") {
			return false
		}
		if (Ext.ss.common.Helper.isBoolean(val)) {
			if (option == "islogical") {
				return true
			} else {
				if (option == "istext" || option == "isnumber") {
					return false
				} else {
					if (option == "n") {
						if (val.toLowerCase() == "false") {
							return 0
						} else {
							return 1
						}
					}
				}
			}
		}
		var checkDate = ds.prepareDate(val);
		if (checkDate) {
			val = Ext.ss.common.Helper.convertDateToNum(checkDate)
		}
		var num = Number(val);
		if (!Ext.isNumber(num)) {
			if (option == "n") {
				if (val == "#DIV/0!") {
					throw "SS_ERROR_INFINITY"
				} else {
					if (val == "#N/A") {
						throw "SS_ERROR_NA"
					} else {
						if (val == "#VALUE!") {
							throw "SS_ERROR_VALUE"
						} else {
							if (val == "#NUM!") {
								throw "SS_ERROR_NUM"
							} else {
								return 0
							}
						}
					}
				}
			}
			if (option == "istext") {
				return true
			}
		} else {
			if (option == "n") {
				return num
			}
			if (option == "isnumber") {
				return true
			}
		}
		return false
	},
	aMathCommon : function(args, option) {
		var len = args.length;
		var ds, x, y, sheetIndex;
		ds = args[len - 1];
		y = args[len - 2];
		x = args[len - 3];
		sheetIndex = args[len - 4];
		len -= 4;
		if (len != 1) {
			throw "SS_ERROR_NA"
		}
		var val = args[0];
		if (Ext.isObject(val)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : val.ox,
						oy : val.oy
					}, {
						ox : val.oex,
						oy : val.oey
					});
			var minx = parseInt(val.ox) + x, maxx = parseInt(val.oex) + x, miny = parseInt(val.oy)
					+ y, maxy = parseInt(val.oey) + y;
			ds.checkSheetIndexValid(val.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(val.sheetIndex)) {
				curSheetIndex = val.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				val = ds.getCellValue(minx, miny, curSheetIndex);
				if (val && val == "#N/A") {
					throw "SS_ERROR_VALUE"
				}
				if (val === undefined || Ext.ss.common.Mask.isEmptyStr(val)) {
					val = 0
				}
				var checkDate = ds.prepareDate(val);
				if (checkDate) {
					val = Ext.ss.common.Helper.convertDateToNum(checkDate)
				}
			} else {
				throw "SS_ERROR_VALUE"
			}
		} else {
			if (val === undefined || val === ""
					|| Ext.ss.common.Mask.isEmptyStr(val)) {
				val = 0
			}
		}
		var num = Number(val);
		if (!Ext.isNumber(num)) {
			throw "SS_ERROR_VALUE"
		}
		if (option == "acos") {
			num = Math.acos(num)
		} else {
			if (option == "asin") {
				num = Math.asin(num)
			} else {
				if (option == "atan") {
					num = Math.atan(num)
				} else {
					if (option == "sin") {
						num = Math.sin(num)
					} else {
						if (option == "cos") {
							num = Math.cos(num)
						} else {
							if (option == "tan") {
								num = Math.tan(num)
							}
						}
					}
				}
			}
		}
		return num
	},
	aMathCommon2 : function(args, option) {
		var len = args.length;
		var ds, x, y, sheetIndex;
		ds = args[len - 1];
		y = args[len - 2];
		x = args[len - 3];
		sheetIndex = args[len - 4];
		len -= 4;
		if (len != 1) {
			throw "SS_ERROR_NA"
		}
		var val = args[0];
		if (Ext.isObject(val)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : val.ox,
						oy : val.oy
					}, {
						ox : val.oex,
						oy : val.oey
					});
			var minx = parseInt(val.ox) + x, maxx = parseInt(val.oex) + x, miny = parseInt(val.oy)
					+ y, maxy = parseInt(val.oey) + y;
			ds.checkSheetIndexValid(val.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(val.sheetIndex)) {
				curSheetIndex = val.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				val = ds.getCellValue(minx, miny, curSheetIndex);
				if (val && val == "#N/A") {
					throw "SS_ERROR_VALUE"
				}
				var checkDate = ds.prepareDate(val);
				if (checkDate) {
					val = Ext.ss.common.Helper.convertDateToNum(checkDate)
				}
			} else {
				throw "SS_ERROR_VALUE"
			}
		} else {
			if (val === undefined || val === ""
					|| Ext.ss.common.Mask.isEmptyStr(val)) {
				throw "SS_ERROR_VALUE"
			}
		}
		var num = Number(val);
		if (!Ext.isNumber(num)) {
			throw "SS_ERROR_VALUE"
		}
		if (option == "even") {
			if (num < 0) {
				num = Math.ceil(Math.abs(num));
				if (num % 2 > 0) {
					num = num + (num2 - num % 2)
				}
				num = -num
			} else {
				num = Math.ceil(num);
				if (num % 2 > 0) {
					num = num + (num2 - num % 2)
				}
			}
		} else {
			if (option == "odd") {
				if (num < 0) {
					num = Math.ceil(Math.abs(num));
					if (num % 2 == 0) {
						num = num + 1
					}
					num = -num
				} else {
					num = Math.ceil(num);
					if (num % 2 == 0) {
						num = num + 1
					}
				}
			} else {
				if (option == "fact") {
					if (num < 0) {
						throw "SS_ERROR_VALUE"
					}
					var fact = 1;
					num = Math.floor(num);
					for (var i = 1; i <= num; i++) {
						fact = fact * i
					}
					num = fact
				} else {
					if (option == "iseven") {
						num = (num > 0) ? Math.floor(num) : Math.ceil(num);
						if (num % 2 == 0) {
							return true
						} else {
							return false
						}
					} else {
						if (option == "isodd") {
							num = (num > 0) ? Math.floor(num) : Math.ceil(num);
							if (num % 2 == 0) {
								return false
							} else {
								return true
							}
						} else {
							if (option == "sign") {
								if (num > 0) {
									num = 1
								} else {
									if (num < 0) {
										num = -1
									} else {
										num = 0
									}
								}
							} else {
								if (option == "sqrtpi") {
									if (num < 0) {
										throw "SS_ERROR_NUM"
									}
									num = Math.sqrt(num * Math.PI)
								} else {
									if (option == "sqrt") {
										if (num < 0) {
											throw "SS_ERROR_NUM"
										}
										num = Math.sqrt(num)
									} else {
										if (option == "degrees") {
											num = 180 * (num / Math.PI)
										} else {
											if (option == "exp") {
												num = Math.exp(num)
											} else {
												if (option == "abs") {
													num = Math.abs(num)
												} else {
													if (option == "int") {
														num = Math.floor(num)
													} else {
														if (option == "radians") {
															num = Math.PI
																	* (num / 180)
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return num
	},
	aMathCommon3 : function(args, option) {
		var len = args.length;
		var ds, x, y, sheetIndex;
		ds = args[len - 1];
		y = args[len - 2];
		x = args[len - 3];
		sheetIndex = args[len - 4];
		len -= 4;
		if (len == 0) {
			throw "SS_ERROR_NA"
		}
		var res = 0;
		for (var k = 0; k < len; k++) {
			var posOffset = args[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						try {
							var cell = ds.getCellValue(i, j, curSheetIndex);
							if (option == "counta") {
								if (cell != undefined && cell != "") {
									res++
								}
							} else {
								if (cell === undefined || cell === "") {
									if (option == "countblank") {
										res++
									}
								} else {
									if (Ext.ss.common.Mask.isNumber(cell)
											|| ds.prepareDate(cell)) {
										if (option == "count") {
											res++
										}
									}
								}
							}
						} catch (e) {
							alert("something wrong")
						}
					}
				}
			} else {
				if (option == "counta") {
					if (cell != undefined && cell != "") {
						res++
					}
				} else {
					if (posOffset === undefined || posOffset === "") {
						if (option == "countblank") {
							res++
						}
					} else {
						if (Ext.ss.common.Mask.isNumber(posOffset)) {
							if (option == "count") {
								res++
							}
						}
					}
				}
			}
		}
		return res
	},
	aMathCommon4 : function(args, option) {
		var len = args.length;
		var ds, x, y, sheetIndex;
		ds = args[len - 1];
		y = args[len - 2];
		x = args[len - 3];
		sheetIndex = args[len - 4];
		len -= 4;
		if (len != 2) {
			throw "SS_ERROR_NA"
		}
		var val = args[0];
		var val2 = args[1];
		if (Ext.isObject(val)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : val.ox,
						oy : val.oy
					}, {
						ox : val.oex,
						oy : val.oey
					});
			var minx = parseInt(val.ox) + x, maxx = parseInt(val.oex) + x, miny = parseInt(val.oy)
					+ y, maxy = parseInt(val.oey) + y;
			ds.checkSheetIndexValid(val.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(val.sheetIndex)) {
				curSheetIndex = val.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				val = ds.getCellValue(minx, miny, curSheetIndex);
				if (val && val == "#N/A") {
					throw "SS_ERROR_NA"
				}
			} else {
				throw "SS_ERROR_VALUE"
			}
		}
		if (Ext.isObject(val2)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : val2.ox,
						oy : val2.oy
					}, {
						ox : val2.oex,
						oy : val2.oey
					});
			var minx = parseInt(val2.ox) + x, maxx = parseInt(val2.oex) + x, miny = parseInt(val2.oy)
					+ y, maxy = parseInt(val2.oey) + y;
			ds.checkSheetIndexValid(val2.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(val2.sheetIndex)) {
				curSheetIndex = val2.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				val2 = ds.getCellValue(minx, miny, curSheetIndex);
				if (val2 && val2 == "#N/A") {
					throw "SS_ERROR_NA"
				}
			} else {
				throw "SS_ERROR_VALUE"
			}
		}
		var num = Number(val);
		var num2 = Number(val2);
		if (!Ext.isNumber(num)) {
			throw "SS_ERROR_VALUE"
		}
		if (!Ext.isNumber(num2)) {
			throw "SS_ERROR_VALUE"
		}
		if (option == "ceiling") {
			if (num2 == 0) {
				throw "SS_ERROR_INFINITY"
			}
			if (num * num2 < 0) {
				throw "SS_ERROR_NUM"
			}
			var snum2 = num2 + "";
			var times = 1;
			if (snum2.indexOf(".") > 0) {
				snum2 = snum2.substring(snum2.indexOf(".") + 1, snum2.length);
				times = Math.pow(10, snum2.length)
			}
			num = Math.ceil(num * times);
			num2 = num2 * times;
			if (num % num2 > 0) {
				num = num + (num2 - num % num2)
			}
		} else {
			if (option == "floor") {
				if (num2 == 0) {
					throw "SS_ERROR_INFINITY"
				}
				if (num * num2 < 0) {
					throw "SS_ERROR_NUM"
				}
				var snum2 = num2 + "";
				var times = 1;
				if (snum2.indexOf(".") > 0) {
					snum2 = snum2.substring(snum2.indexOf(".") + 1,
							snum2.length);
					times = Math.pow(10, snum2.length)
				}
				num = num * times;
				num2 = num % (num2 * times);
				num = (num - num2) / times
			} else {
				if (option == "mod") {
					if (num2 == 0) {
						throw "SS_ERROR_INFINITY"
					}
					num = num - num2 * Math.floor(num / num2)
				} else {
					if (option == "randbetween") {
						if (num > num2) {
							throw "SS_ERROR_NUM"
						}
						num = Math.floor(Math.random() * (num2 - num + 1))
								+ num
					} else {
						if (option == "randbetween") {
							if (num > num2) {
								throw "SS_ERROR_NUM"
							}
							num = Math.floor(Math.random() * (num2 - num + 1))
									+ num
						} else {
							if (option == "quotient") {
								if (num2 == 0) {
									throw "SS_ERROR_INFINITY"
								}
								num = num / num2;
								num = (num > 0) ? Math.floor(num) : Math
										.ceil(num)
							} else {
								if (option == "atan2") {
									if (num2 == 0 && num == 0) {
										throw "SS_ERROR_INFINITY"
									}
									num = Math.atan2(num2, num)
								}
							}
						}
					}
				}
			}
		}
		return num
	},
	aMathRound : function(args, option) {
		var len = args.length;
		var ds, x, y, sheetIndex;
		ds = args[len - 1];
		y = args[len - 2];
		x = args[len - 3];
		sheetIndex = args[len - 4];
		len -= 4;
		if (len != 2 && len != 1) {
			throw "SS_ERROR_NA"
		}
		var digital = 0;
		if (2 == len) {
			digital = args[1]
		}
		if (Ext.isObject(digital)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : digital.ox,
						oy : digital.oy
					}, {
						ox : digital.oex,
						oy : digital.oey
					});
			ds.checkSheetIndexValid(digital.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(digital.sheetIndex)) {
				curSheetIndex = digital.sheetIndex
			}
			var i = parseInt(digital.ox) + x, j = parseInt(digital.oy) + y;
			digital = ds.getCellValue(i, j, curSheetIndex)
		}
		if (digital === undefined || digital === "") {
			throw "SS_ERROR_VALUE"
		}
		digital = Number(digital);
		if (Ext.isNumber(digital)) {
			digital = Ext.ss.common.Mask.getCeilFloor(digital)
		} else {
			throw "SS_ERROR_VALUE"
		}
		var posOffset = args[0];
		if (Ext.isObject(posOffset)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : posOffset.ox,
						oy : posOffset.oy
					}, {
						ox : posOffset.oex,
						oy : posOffset.oey
					});
			ds.checkSheetIndexValid(posOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(posOffset.sheetIndex)) {
				curSheetIndex = posOffset.sheetIndex
			}
			var i = parseInt(posOffset.ox) + x, j = parseInt(posOffset.oy) + y;
			posOffset = ds.getCellValue(i, j, curSheetIndex)
		}
		if (posOffset === undefined || posOffset === "") {
			throw "SS_ERROR_VALUE"
		}
		var num = Number(posOffset);
		if (Ext.isNumber(num)) {
			var base = Math.pow(10, digital);
			var result = num;
			if (option == "round") {
				result = Math.round(num * base) / base
			} else {
				if (option == "rounddown") {
					result = Math.floor(num * base) / base
				} else {
					if (option == "roundup") {
						result = Math.ceil(num * base) / base
					}
				}
			}
			return result
		} else {
			throw "SS_ERROR_VALUE"
		}
	},
	stringComm1 : function(args, option) {
		var len = args.length;
		var ds, x, y, sheetIndex;
		ds = args[len - 1];
		y = args[len - 2];
		x = args[len - 3];
		sheetIndex = args[len - 4];
		len -= 4;
		if (len != 1) {
			throw "SS_ERROR_NA"
		}
		var val = args[0];
		if (Ext.isObject(val)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : val.ox,
						oy : val.oy
					}, {
						ox : val.oex,
						oy : val.oey
					});
			var minx = parseInt(val.ox) + x, maxx = parseInt(val.oex) + x, miny = parseInt(val.oy)
					+ y, maxy = parseInt(val.oey) + y;
			ds.checkSheetIndexValid(val.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(val.sheetIndex)) {
				curSheetIndex = val.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				val = ds.getCellValue(minx, miny, curSheetIndex)
			} else {
				throw "SS_ERROR_VALUE"
			}
		}
		if (option == "upper") {
			if (Ext.isBoolean(val) || Ext.isNumber(val)) {
				return val
			}
			return val.toUpperCase()
		} else {
			if (option == "lower") {
				if (Ext.isBoolean(val) || Ext.isNumber(val)) {
					return val
				}
				return val.toLowerCase()
			} else {
				if (option == "len") {
					return val.toString().length
				}
			}
		}
	},
	getNumObjValue : function(ds, x, y, sheetIndex, numberObj) {
		if (Ext.isObject(numberObj)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : numberObj.ox,
						oy : numberObj.oy
					}, {
						ox : numberObj.oex,
						oy : numberObj.oey
					});
			var minx = parseInt(numberObj.ox) + x, maxx = parseInt(numberObj.oex)
					+ x, miny = parseInt(numberObj.oy) + y, maxy = parseInt(numberObj.oey)
					+ y;
			ds.checkSheetIndexValid(numberObj.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(numberObj.sheetIndex)) {
				curSheetIndex = numberObj.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				numberObj = ds.getCellValue(minx, miny, curSheetIndex);
				if (numberObj === undefined
						|| Ext.ss.common.Mask.isEmptyStr(numberObj)) {
					throw "SS_ERROR_NUM"
				}
			} else {
				throw "SS_ERROR_VALUE"
			}
		}
		numberObj = Number(numberObj);
		if (!Ext.isNumber(numberObj)) {
			throw "SS_ERROR_VALUE"
		}
		return numberObj
	},
	getDateObjValue : function(ds, x, y, sheetIndex, dateObj) {
		if (Ext.isObject(dateObj)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : dateObj.ox,
						oy : dateObj.oy
					}, {
						ox : dateObj.oex,
						oy : dateObj.oey
					});
			var minx = parseInt(dateObj.ox) + x, maxx = parseInt(dateObj.oex)
					+ x, miny = parseInt(dateObj.oy) + y, maxy = parseInt(dateObj.oey)
					+ y;
			ds.checkSheetIndexValid(dateObj.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(dateObj.sheetIndex)) {
				curSheetIndex = dateObj.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				dateObj = ds.getCellValue(minx, miny, curSheetIndex);
				dateObj = ds.prepareDate(dateObj);
				if (!dateObj) {
					throw "SS_ERROR_VALUE"
				}
			} else {
				throw "SS_ERROR_VALUE"
			}
		} else {
			dateObj = ds.transfer2Date(dateObj)
		}
		return dateObj
	},
	getObjVal : function(ds, x, y, sheetIndex, passObj) {
		if (Ext.isObject(passObj)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : passObj.ox,
						oy : passObj.oy
					}, {
						ox : passObj.oex,
						oy : passObj.oey
					});
			var minx = parseInt(passObj.ox) + x, maxx = parseInt(passObj.oex)
					+ x, miny = parseInt(passObj.oy) + y, maxy = parseInt(passObj.oey)
					+ y;
			ds.checkSheetIndexValid(passObj.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(passObj.sheetIndex)) {
				curSheetIndex = passObj.sheetIndex
			}
			if (minx == maxx && miny == maxy) {
				passObj = ds.getCellValue(minx, miny, curSheetIndex)
			} else {
				throw "SS_ERROR_VALUE"
			}
		}
		return passObj
	},
	getRangeVal : function(ds, x, y, sheetIndex, passObj) {
		var arrObj = new Array();
		if (Ext.isObject(passObj)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : passObj.ox,
						oy : passObj.oy
					}, {
						ox : passObj.oex,
						oy : passObj.oey
					});
			var minx = parseInt(passObj.ox) + x, maxx = parseInt(passObj.oex)
					+ x, miny = parseInt(passObj.oy) + y, maxy = parseInt(passObj.oey)
					+ y;
			ds.checkSheetIndexValid(passObj.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(passObj.sheetIndex)) {
				curSheetIndex = passObj.sheetIndex
			}
			for (var i = minx; i <= maxx; i++) {
				for (var j = miny; j <= maxy; j++) {
					var pos = ds.getCellValue(i, j, curSheetIndex);
					arrObj.push(pos)
				}
			}
		} else {
			arrObj.push(passObj)
		}
		return arrObj
	},
	sumHelper : function(x, y, sheetIndex, ds, arrObj1, arrObj2, option) {
		var xArrary = [], yArrary = [];
		if (Ext.isObject(arrObj1)) {
			xArrary = [arrObj1];
			xArrary = Ext.ss.common.FunctionBoxHelper.restructureArray(xArrary,
					x, y, sheetIndex, ds)
		} else {
			if (Ext.isArray(arrObj1)) {
				xArrary = Ext.ss.common.FunctionBoxHelper.restructureArray(
						arrObj1, x, y, sheetIndex, ds)
			}
		}
		if (Ext.isObject(arrObj2)) {
			yArrary = [arrObj2];
			yArrary = Ext.ss.common.FunctionBoxHelper.restructureArray(yArrary,
					x, y, sheetIndex, ds)
		} else {
			if (Ext.isArray(arrObj2)) {
				yArrary = Ext.ss.common.FunctionBoxHelper.restructureArray(
						arrObj2, x, y, sheetIndex, ds)
			}
		}
		if (xArrary.length == yArrary.length && xArrary.length > 0) {
			var result = 0;
			for (var i = 0; i < xArrary.length; i++) {
				var xVal = Number(xArrary[i]);
				if (!Ext.isNumber(xVal)) {
					xVal = 0
				}
				var yVal = Number(yArrary[i]);
				if (!Ext.isNumber(yVal)) {
					yVal = 0
				}
				if (option == "sumxmy2") {
					result += Math.pow(xVal - yVal, 2)
				}
				if (option == "sumx2py2") {
					result += Math.pow(xVal, 2) + Math.pow(yVal, 2)
				}
				if (option == "sumx2my2") {
					result += Math.pow(xVal, 2) - Math.pow(yVal, 2)
				}
			}
			xArrary = [];
			yArrary = [];
			return result
		}
		xArrary = [];
		yArrary = [];
		throw "SS_ERROR_NA"
	},
	getnpv : function(rate, values, ds, x, y, sheetIndex) {
		var npv = 0;
		var len = values.length;
		for (var k = 0; k < len; k++) {
			var posOffset = values[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, posOffset, posOffset);
				var cx = posOffset.ox + x, cy = posOffset.oy + y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex
				}
				num = Number(ds.getCellValue(cx, cy, curSheetIndex))
			} else {
				var num = Number(posOffset)
			}
			if (Ext.isNumber(num)) {
				npv = npv + (num / (Math.pow((1 + rate), k + 1)))
			}
		}
		return npv
	},
	restructureArray : function(ary, x, y, sheetIndex, ds) {
		var tmpAry = new Array();
		for (var c = 0; c < ary.length; c++) {
			var posOffset = ary[c];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						tmpAry.push(ds.getCellValue(i, j, curSheetIndex))
					}
				}
			} else {
				tmpAry.push(posOffset)
			}
		}
		return tmpAry
	},
	__format : function(pattern, num, z) {
		if (z == 1 && num.length > 0) {
			pattern += "0"
		}
		var j = pattern.length >= num.length ? pattern.length : num.length;
		var p = pattern.split("");
		var n = num.split("");
		var bool = true, nn = "";
		for (var i = 0; i < j; i++) {
			var x = n[n.length - j + i];
			var y = p[p.length - j + i];
			if (z == 0) {
				if (bool) {
					if ((x && y && (x != "0" || y == "0"))
							|| (x && x != "0" && !y) || (y && y == "0" && !x)) {
						nn += x ? x : "0";
						bool = false
					}
				} else {
					nn += x ? x : "0"
				}
			} else {
				if (y && (y == "0" || (y == "#" && x))) {
					nn += x ? x : "0"
				}
			}
		}
		return nn
	},
	__formatNumber : function(numChar, pattern) {
		var patterns = pattern.split(".");
		var numChars = numChar.split(".");
		var z = patterns[0].indexOf(",") == -1 ? -1 : patterns[0].length
				- patterns[0].indexOf(",");
		var num1 = this.__format(patterns[0].replace(","), numChars[0], 0);
		var num2 = this.__format(patterns[1] ? patterns[1].split("").reverse()
						.join("") : "", numChars[1] ? numChars[1].split("")
						.reverse().join("") : "", 1);
		num1 = num1.split("").reverse().join("");
		var reCat = eval("/[0-9]{" + (z - 1) + "," + (z - 1) + "}/gi");
		var arrdata = z > -1 ? num1.match(reCat) : undefined;
		if (arrdata && arrdata.length > 0) {
			var w = num1.replace(arrdata.join(""), "");
			num1 = arrdata.join(",") + (w == "" ? "" : ",") + w
		}
		num1 = num1.split("").reverse().join("");
		var result = (num1 == "" ? "0" : num1)
				+ (num2 != "" ? "." + num2.split("").reverse().join("") : "");
		if (num1 && num2) {
			var pw = Math.pow(10, num2.length - 1);
			result = Math.round((Number(result) * pw)) / pw
		}
		return result
	},
	formatNumber : function(num, opt) {
		var reCat = /[0#?,.]{1,}/gi;
		var zeroExc = opt.zeroExc == undefined ? true : opt.zeroExc;
		var pattern = opt.match(reCat)[0];
		var numChar = num.toString();
		var fmun = this.__formatNumber(numChar, pattern);
		return !(zeroExc && numChar == 0) ? opt.replace(pattern, fmun) : opt
				.replace(pattern, "0")
	}
};
Ext.ns("Ext.ss.common");
Ext.ss.common.Mask = {};
Ext.ns("feyaSoft.ss.popup");
feyaSoft.ss.popup.ImagePopup = function(callBackFn, scope) {
	this.westPanel = new feyaSoft.home.program.photo.WestPanel;
	this.imageDetail = new feyaSoft.home.program.photo.ImageDetailPanel({
				eastPanelShow : "not"
			});
	this.callback = callBackFn.createDelegate(scope || this);
	feyaSoft.ss.popup.ImagePopup.superclass.constructor.call(this, {
				title : feyaSoft.lang.photo.listImages,
				layout : "fit",
				resizable : false,
				loadMask : true,
				width : 1000,
				height : 500,
				y : 20,
				closeAction : "close",
				items : [{
							layout : "border",
							items : [this.westPanel, this.imageDetail]
						}],
				modal : true,
				shim : false,
				animCollapse : false,
				border : false,
				constrainHeader : false,
				minimizable : false,
				maximizable : false
			});
	this.show();
};
Ext.extend(feyaSoft.ss.popup.ImagePopup, Ext.Window, {
			selectImage : function(imgpath) {
				if (imgpath) {
					this.callback.defer(1, this, [imgpath]);
					this.close();
				}
			}
		});
Ext.ns("Ext.ss.popup");
Ext.ss.popup.LookupWin = function(config) {
	this.searchpanel = new Ext.ss.SearchPanel(config);
	this.replacepanel = new Ext.ss.ReplacePanel(config);
	this.tabpanel = new Ext.TabPanel({
				border : false,
				activeTab : 0,
				deferredRender : false,
				resizeTabs : true,
				tabWidth : 200,
				minTabWidth : 0,
				layoutOnTabChange : true,
				hideMode : "offsets",
				items : [this.searchpanel, this.replacepanel]
			});
	Ext.ss.popup.LookupWin.superclass.constructor.call(this, {
				title : "Find and Replace",
				width : 400,
				height : 200,
				closeAction : "hide",
				layout : "fit",
				items : [this.tabpanel]
			});
	this.on("show", this._onShow, this);
};
Ext.extend(Ext.ss.popup.LookupWin, Ext.Window, {
			manager : {
				register : Ext.emptyFn,
				unregister : Ext.emptyFn,
				bringToFront : Ext.emptyFn,
				sendToBack : Ext.emptyFn
			},
			_onShow : function() {
				var tab = this.tabpanel.getActiveTab();
				if (tab) {
					tab.focus();
				}
			}
		});
Ext.ss.SearchPanel = function(config) {
	var lanss = feyaSoft.ss.lang;
	Ext.apply(this, config);
	this.targetCombo = new Ext.form.ComboBox({
				fieldLabel : lanss.target,
				store : Ext.ss.common.Mask.getTargetStore(config),
				displayField : "text",
				valueField : "text",
				typeAhead : true,
				mode : "local",
				triggerAction : "all",
				selectOnFocus : true,
				msgTarget : "qtip",
				anchor : "95%",
				minChars : 1,
				allowBlank : false
			});
	this.findAllBtn = new Ext.Button({
				iconCls : "icon_search",
				minWidth : 80,
				text : lanss.findAll,
				handler : this.onFindAllFn,
				scope : this
			});
	this.findLastBtn = new Ext.Button({
				iconCls : "icon_search",
				minWidth : 80,
				text : lanss.findLast,
				handler : this.onFindLastFn,
				scope : this
			});
	this.findNextBtn = new Ext.Button({
				iconCls : "icon_search",
				minWidth : 80,
				text : lanss.findNext,
				handler : this.onFindNextFn,
				scope : this
			});
	this.closeBtn = new Ext.Button({
				iconCls : "icon_close",
				minWidth : 80,
				text : "Close",
				handler : this.onCloseFn,
				scope : this
			});
	this.buttons = [this.findLastBtn, this.findNextBtn, this.closeBtn];
	Ext.ss.SearchPanel.superclass.constructor.call(this, {
				border : false,
				labelWidth : 50,
				bodyStyle : "padding:10px",
				title : lanss.find,
				layout : "form",
				items : [this.targetCombo]
			});
};
Ext.extend(Ext.ss.SearchPanel, Ext.Panel, {
			focus : function() {
				this.targetCombo.focus();
			},
			onFindAllFn : function() {
			},
			onFindLastFn : function() {
				if (this.targetCombo.isValid()) {
					var target = this.targetCombo.getRawValue();
					if (!this.target
							|| this.target !== target
							|| this.activeSheet != this.spreadsheet.ds.activeSheet) {
						this.target = target;
						this.targetLinks = this.findTargets(target);
						this.cursor = this.targetLinks.length;
					}
					if (0 === this.targetLinks.length) {
						this.setTitle(feyaSoft.ss.lang.find
								+ " <font style=\"color:red;\">["
								+ feyaSoft.word.lang.nomatch + "]</font>");
					} else {
						this.setTitle(feyaSoft.ss.lang.find);
						if (0 <= this.cursor) {
							this.cursor--;
							if (0 > this.cursor) {
								this.cursor = this.targetLinks.length - 1;
							}
							var link = this.targetLinks[this.cursor];
							this.spreadsheet.sm.setFocusCell(link.x, link.y);
						}
					}
				}
			},
			onFindNextFn : function() {
				if (this.targetCombo.isValid()) {
					var target = this.targetCombo.getRawValue();
					if (!this.target
							|| this.target !== target
							|| this.activeSheet != this.spreadsheet.ds.activeSheet) {
						this.target = target;
						this.cursor = -1;
						this.targetLinks = this.findTargets(target);
					}
					if (0 === this.targetLinks.length) {
						this.setTitle(feyaSoft.ss.lang.find
								+ " <font style=\"color:red;\">["
								+ feyaSoft.word.lang.nomatch + "]</font>");
					} else {
						this.setTitle(feyaSoft.ss.lang.find);
						if (this.cursor < this.targetLinks.length) {
							this.cursor++;
							if (this.cursor == this.targetLinks.length) {
								this.cursor = 0;
							}
							var link = this.targetLinks[this.cursor];
							this.spreadsheet.sm.setFocusCell(link.x, link.y);
						}
					}
				}
			},
			findTargets : function(target) {
				var ss = this.spreadsheet;
				var ds = ss.ds;
				this.activeSheet = ds.activeSheet;
				return ds.findTargets(target);
			},
			onCloseFn : function() {
				this.ownerCt.ownerCt.hide();
			}
		});
Ext.ss.ReplacePanel = function(config) {
	var lanss = feyaSoft.ss.lang;
	Ext.apply(this, config);
	this.targetCombo = new Ext.form.ComboBox({
				fieldLabel : lanss.target,
				store : Ext.ss.common.Mask.getTargetStore(config),
				displayField : "text",
				valueField : "text",
				typeAhead : true,
				mode : "local",
				triggerAction : "all",
				selectOnFocus : true,
				anchor : "95%",
				minChars : 1,
				allowBlank : false
			});
	this.replaceField = new Ext.form.TextField({
				fieldLabel : lanss.replaceWith,
				anchor : "95%"
			});
	this.replaceAllBtn = new Ext.Button({
				minWidth : 80,
				text : lanss.replaceAll,
				handler : this.onReplaceAllFn,
				scope : this
			});
	this.replaceBtn = new Ext.Button({
				minWidth : 80,
				text : lanss.replaceNext,
				handler : this.onReplaceNextFn,
				scope : this
			});
	this.closeBtn = new Ext.Button({
				iconCls : "icon_close",
				minWidth : 80,
				text : "Close",
				handler : this.onCloseFn,
				scope : this
			});
	this.buttons = [this.replaceAllBtn, this.replaceBtn, this.closeBtn];
	Ext.ss.ReplacePanel.superclass.constructor.call(this, {
				border : false,
				labelWidth : 100,
				bodyStyle : "padding:10px",
				title : lanss.replace,
				layout : "form",
				items : [this.targetCombo, this.replaceField]
			});
};
Ext.extend(Ext.ss.ReplacePanel, Ext.Panel, {
			focus : function() {
				this.targetCombo.focus();
			},
			onReplaceAllFn : function() {
				if (this.targetCombo.isValid()) {
					var lanss = feyaSoft.ss.lang;
					var retext = this.replaceField.getValue() || "";
					var ss = this.spreadsheet, ds = ss.ds;
					var target = this.targetCombo.getRawValue();
					if (!this.target || this.target !== target) {
						this.target = target;
						this.cursor = -1;
						this.targetLinks = this.findTargets(target);
					}
					var link;
					var len = this.targetLinks.length;
					while (this.cursor < len - 1) {
						this.cursor++;
						link = this.targetLinks[this.cursor];
						var data = link.data;
						var re = new RegExp(Ext.escapeRe(target), "gi");
						data = data.replace(re, retext);
						ss.setInnerText(data, link.x, link.y);
					}
					if (0 < len) {
						link = this.targetLinks[len - 1];
						ss.sm.setFocusCell(link.x, link.y);
						var sheet = ds.sheets[ds.activeSheet];
						ds.trace(lanss.replace + " " + lanss.all + " \""
								+ target + "\" " + lanss['with'] + " \""
								+ retext + "\" " + lanss['in'] + " \""
								+ sheet.name + "\"");
					}
				}
			},
			onReplaceNextFn : function() {
				if (this.targetCombo.isValid()) {
					var lanss = feyaSoft.ss.lang;
					var retext = this.replaceField.getValue() || "";
					var ss = this.spreadsheet, ds = ss.ds;
					var target = this.targetCombo.getRawValue();
					if (!this.target || this.target !== target) {
						this.target = target;
						this.cursor = -1;
						this.targetLinks = this.findTargets(target);
					}
					var len = this.targetLinks.length;
					if (0 < len && this.cursor < len) {
						this.cursor++;
						if (this.cursor == len) {
							this.cursor = 0;
						}
						var link = this.targetLinks[this.cursor];
						ss.sm.setFocusCell(link.x, link.y);
						var data = link.data;
						var re = new RegExp(Ext.escapeRe(target), "gi");
						data = data.replace(re, retext);
						ss.setInnerText(data, link.x, link.y);
						ds.trace(lanss.replace + " \"" + target + "\" "
								+ lanss['with'] + " \"" + retext + "\" "
								+ lanss.at + " " + ds.getNameFromPos(link));
					}
				}
			},
			findTargets : function(target) {
				var ss = this.spreadsheet;
				var ds = ss.ds;
				return ds.findTargets(target);
			},
			onCloseFn : function() {
				this.ownerCt.ownerCt.hide();
			}
		});
Ext.ns("Ext.ss.popup");
Ext.ns("Ext.ss.popup");
Ext.ss.popup.NameRangeWin = Ext.extend(Ext.Window, {
	layout : "border",
	initComponent : function() {
		this.nameField = new Ext.form.TextField({
					hideLabel : true,
					msgTarget : "qtip",
					allowBlank : false,
					anchor : "100%"
				});
		this.grid = new Ext.grid.GridPanel({
					hideHeaders : true,
					bodyStyle : "border-left:none;border-right:none;",
					region : "center",
					store : new Ext.data.ArrayStore({
								fields : ["name", "coord", "coordArr"],
								data : []
							}),
					columns : [{
								dataIndex : "name"
							}, {
								dataIndex : "coord"
							}],
					viewConfig : {
						forceFit : true
					}
				});
		this.rangeField = new Ext.form.TextField({
					hideLabel : true,
					labelSeparator : "",
					anchor : "100%",
					flex : 1,
					validator : Ext.ss.common.Mask.dataRangeValidator,
					allowBlank : false
				});
		this.items = [{
					xtype : "container",
					region : "north",
					autoHeight : true,
					style : "padding:10px 20px 15px 20px;",
					layout : "form",
					items : [{
								xtype : "box",
								html : feyaSoft.ss.lang.nameRange,
								height : 25
							}, this.nameField]
				}, this.grid, {
					xtype : "container",
					region : "south",
					autoHeight : true,
					style : "padding:10px 20px;",
					layout : "hbox",
					items : [this.rangeField, {
								xtype : "box",
								width : 5
							}, {
								xtype : "button",
								iconCls : "icon_window_popup",
								handler : this.popupDataRange,
								scope : this
							}]
				}];
		this.deleteBtn = new Ext.Button({
					text : feyaSoft.ss.lang['delete'],
					disabled : true,
					handler : this.onDelete,
					scope : this
				});
		this.buttons = [{
					text : feyaSoft.ss.lang.add,
					handler : this.onAdd,
					scope : this
				}, this.deleteBtn, {
					text : feyaSoft.ss.lang.close,
					handler : this._onClose,
					scope : this
				}];
		Ext.ss.popup.NameRangeWin.superclass.initComponent.call(this);
		var sm = this.grid.getSelectionModel();
		this.mon(sm, {
					scope : this,
					selectionchange : this.onSelectionChange
				});
	},
	parseStr2Coord : function(str) {
		var ds = this.spreadsheet.ds;
		var parts = str.split(",");
		var arr = [];
		for (var i = 0, len = parts.length; i < len; i++) {
			arr.push(ds.parseText2Pos(parts[i]));
		}
		return arr;
	},
	popupDataRange : function() {
		var me = this;
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var drSelector = ss.dataRangeSelector;
		me.hide();
		drSelector.popup(null, this.rangeField.getValue(), function(range) {
					this.rangeField.setValue(range);
					var arr = me.parseStr2Coord(range);
					me.initDataRange(arr);
					me.show();
				}, this);
	},
	initDataRange : function(rt) {
		var ss = this.spreadsheet;
		var sm = ss.sm;
		var ds = ss.ds;
		if (!rt) {
			var res = sm
					.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			rt = [{
						x : res.minPos.x,
						y : res.minPos.y,
						ex : res.maxPos.x,
						ey : res.maxPos.y,
						ab : true,
						sheetIndex : ds.activeSheet
					}];
		}
		for (var i = 0, len = rt.length; i < len; i++) {
			rt[i].ab = true;
		}
		this.dataRange = rt;
	},
	popup : function() {
		this.nameChanged = false;
		var ss = this.spreadsheet;
		var sm = ss.sm, ds = ss.ds;
		var nameRanges = ds.fileExtraInfo.nameRanges;
		this.initNameRanges(nameRanges);
		var rt = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
		this.initDataRange();
		var val = ss.encodeDataRange(rt, this.rangeFormat);
		this.nameField.reset();
		this.rangeField.setValue(val);
		this.show();
	},
	initNameRanges : function(nameRanges) {
		var store = this.grid.getStore();
		store.removeAll();
		if (nameRanges) {
			for (var i = 0, len = nameRanges.length; i < len; i++) {
				var it = nameRanges[i];
				var rec = new store.recordType(it);
				store.add(rec);
			}
		}
	},
	onAdd : function() {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		if (this.nameField.isValid() && this.rangeField.isValid()) {
			var name = this.nameField.getValue();
			if (!this.checkNameValid(name)) {
				return;
			}
			var coord = this.rangeField.getValue();
			var arr = this.parseStr2Coord(coord);
			this.initDataRange(arr);
			var store = this.grid.getStore();
			var rec = new store.recordType({
						name : name,
						coord : coord,
						coordArr : ds.deepClone(this.dataRange)
					});
			store.add([rec]);
			this.nameChanged = true;
			this.nameField.reset();
		}
	},
	checkNameValid : function(name) {
		var upper = name.toUpperCase();
		var ds = this.spreadsheet.ds;
		var fbox = Ext.ss.FunctionBox;
		if (!/[a-zA-Z0-9_]+/gi.test(name) || /^[0-9]/gi.test(name)
				|| /\s/gi.test(name)) {
			Ext.Msg.alert(feyaSoft.ss.lang.hint, feyaSoft.ss.lang.letterNumber);
			return false;
		}
		if (name.length > 250) {
			Ext.Msg.alert(feyaSoft.ss.lang.hint, feyaSoft.ss.lang.long250);
			return false;
		}
		if (fbox[name]) {
			Ext.Msg.alert(feyaSoft.ss.lang.hint, feyaSoft.ss.lang.reservedWord);
			return false;
		} else {
			var consts = Ext.ss.CONST.JS_RESERVED_WORD;
			var reservedFlag = false;
			for (var i = 0, len = consts.length; i < len; i++) {
				if (name == consts[i]) {
					reservedFlag = true;
					break;
				}
			}
			if (!reservedFlag) {
				try {
					reservedFlag = eval("var " + name + "=false;");
				} catch (e) {
					reservedFlag = true;
				}
			}
			if (reservedFlag) {
				Ext.Msg.alert(feyaSoft.ss.lang.hint,
						feyaSoft.ss.lang.reservedWord);
				return false;
			}
		}
		var reg = ds.posReg;
		var newName = name.replace(reg, function(w) {
					if (w == name) {
						try {
							var pos = ds.parseText2Pos(w);
							if (pos.x == pos.ex && pos.y == pos.ey) {
								if (pos.y <= 256) {
									return "";
								}
							} else {
								return "";
							}
						} catch (e) {
							return w;
						}
					}
					return w;
				});
		if (name != newName) {
			Ext.Msg.alert(feyaSoft.ss.lang.hint,
					feyaSoft.ss.lang.likeReferenceRange);
			return false;
		}
		var store = this.grid.getStore();
		var existed = false;
		store.each(function(rd) {
					var text = rd.data.name.toUpperCase();
					if (text == upper) {
						existed = true;
						return false;
					}
				});
		if (existed) {
			Ext.Msg.alert(feyaSoft.ss.lang.hint, feyaSoft.ss.lang.nameExisted);
			return false;
		}
		return true;
	},
	_onClose : function() {
		if (this.nameChanged) {
			var ss = this.spreadsheet, ds = ss.ds;
			var store = this.grid.getStore();
			var recs = store.getRange();
			var arr = [];
			for (var i = 0, len = recs.length; i < len; i++) {
				var rec = recs[i];
				arr.push(Ext.apply({}, rec.data));
			}
			ds.fileExtraInfo.nameRanges = arr;
			if (!ds.disableCallingBackEnd) {
				try {
					ds.saveFileExtraInfo(function() {
								delete ds.formulaRelatedMap;
								ds.onCheckFormulaFn();
							});
				} catch (err) {
					Ext.MessageBox.alert(feyaSoft.ss.lang.hint,
							"Save name to the backend server failed.");
				}
			}
		}
		this.hide();
	},
	onSelectionChange : function(sm) {
		var sels = sm.getSelections();
		if (0 < sels.length) {
			this.deleteBtn.enable();
		} else {
			this.deleteBtn.disable();
		}
	},
	onDelete : function() {
		var sm = this.grid.getSelectionModel();
		var store = this.grid.getStore();
		var sels = sm.getSelections();
		if (0 < sels.length) {
			store.remove(sels);
			this.nameChanged = true;
		}
	}
});
Ext.ns("Ext.ss.popup");
Ext.ss.popup.ConditionFormatWin = Ext.extend(Ext.Window, {
	width : 800,
	autoHeight : true,
	maxHeight : 300,
	bodyStyle : "min-height:60px;",
	autoScroll : true,
	closeAction : "hide",
	title : feyaSoft.ss.lang.conditionFormat,
	initComponent : function() {
		var lanss = feyaSoft.ss.lang;
		this.colorPalette = new Ext.ColorPalette({
					allowReselect : true
				});
		this.colorMenu = new Ext.menu.Menu({
					items : [this.colorPalette]
				});
		this.buttons = [{
					text : lanss.add,
					minWidth : 80,
					handler : this.onAddFn,
					scope : this
				}, {
					text : lanss.ok,
					minWidth : 80,
					handler : this.onApply,
					scope : this
				}, {
					text : lanss.cancel,
					minWidth : 80,
					handler : function() {
						this.hide();
					},
					scope : this
				}];
		this.items = [this.createConditionFormat(true)];
		Ext.ss.popup.ConditionFormatWin.superclass.initComponent.call(this);
		this.colorPalette.on("select", this.onColorSelectFn, this);
		this.on("hide", this.onHideFn, this);
	},
	createConditionFormat : function(hideDelete) {
		var rangeField = new Ext.form.TextField({
			hideLabel : true,
			labelSeparator : "",
			validator : this.validator || Ext.ss.common.Mask.dataRangeValidator,
			allowBlank : false
		});
		var combo = new Ext.form.ComboBox({
					typeAhead : true,
					triggerAction : "all",
					lazyRender : true,
					mode : "local",
					margins : "0 0 0 10",
					store : Ext.ss.common.Mask.getConditionStore(),
					valueField : "id",
					displayField : "text",
					value : "include"
				});
		this.mon(combo, {
					scope : this,
					select : this.onSelectOption
				});
		var numField = new Ext.form.NumberField({
					hidden : true,
					width : 110,
					margins : "0 0 0 10",
					allowBlank : false
				});
		var textField = new Ext.form.TextField({
					width : 110,
					margins : "0 0 0 10",
					allowBlank : false
				});
		var minField = new Ext.form.NumberField({
					hidden : true,
					width : 50,
					margins : "0 0 0 10",
					allowBlank : false
				});
		var maxField = new Ext.form.NumberField({
					hidden : true,
					width : 50,
					margins : "0 0 0 10",
					allowBlank : false
				});
		var textCb = new Ext.form.Checkbox({
					boxLabel : "Text",
					margins : "0 0 0 20",
					listeners : {
						check : {
							fn : function(cb, checked) {
								if (false === checked) {
									var ct = cb.ownerCt;
									var index = ct.items.indexOf(cb);
									var box = ct.items.get(index + 1);
									box.getEl()
											.setStyle("background-color", "");
								}
							},
							scope : this
						}
					}
				});
		var textBox = new Ext.BoxComponent({
					cls : "x-condition-text-box",
					margins : "0 0 0 10",
					listeners : {
						afterrender : {
							fn : function(box) {
								var el = box.getEl();
								el.on("click", function() {
											var ct = box.ownerCt;
											var index = ct.items.indexOf(box);
											var checkBox = ct.items.get(index
													- 1);
											this.colorPalette.sender = box;
											this.colorPalette.checkbox = checkBox;
											this.colorMenu.show(el, "tl-bl?");
										}, this);
							},
							scope : this
						}
					}
				});
		var backgroundCb = new Ext.form.Checkbox({
					boxLabel : "Background",
					margins : "0 0 0 10",
					listeners : {
						check : {
							fn : function(cb, checked) {
								if (false === checked) {
									var ct = cb.ownerCt;
									var index = ct.items.indexOf(cb);
									var box = ct.items.get(index + 1);
									box.getEl()
											.setStyle("background-color", "");
								}
							},
							scope : this
						}
					}
				});
		var backgroundBox = new Ext.BoxComponent({
					cls : "x-condition-text-box",
					margins : "0 0 0 10",
					listeners : {
						afterrender : {
							fn : function(box) {
								var el = box.getEl();
								el.on("click", function() {
											var ct = box.ownerCt;
											var index = ct.items.indexOf(box);
											var checkBox = ct.items.get(index
													- 1);
											this.colorPalette.sender = box;
											this.colorPalette.checkbox = checkBox;
											this.colorMenu.show(el, "tl-bl?");
										}, this);
							},
							scope : this
						}
					}
				});
		var deleteBtn = new Ext.Button({
					iconCls : "delete",
					hidden : hideDelete,
					handler : this.onDeleteConditionFormat,
					scope : this
				});
		var ct = new Ext.Container({
					style : "padding:10px;",
					layout : {
						type : "hbox"
					},
					items : [rangeField, {
								xtype : "button",
								margins : "0 0 0 5",
								iconCls : "icon_window_anchorin",
								handler : this.popupDataRange,
								scope : this
							}, combo, textField, numField, minField, maxField,
							textCb, textBox, backgroundCb, backgroundBox, {
								xtype : "box",
								flex : 1
							}, deleteBtn],
					isValid : function() {
						var flag = true;
						this.items.each(function(it) {
									if (it.isValid && it.isVisible()) {
										if (!it.isValid()) {
											flag = false;
											return false;
										}
									}
								});
						return flag;
					}
				});
		ct.rangeField = rangeField;
		ct.optionCombo = combo;
		ct.targetField = textField;
		ct.numField = numField;
		ct.minField = minField;
		ct.maxField = maxField;
		ct.textCb = textCb;
		ct.textBox = textBox;
		ct.backgroundCb = backgroundCb;
		ct.backgroundBox = backgroundBox;
		return ct;
	},
	onSelectOption : function(combo, rec) {
		var ct = combo.ownerCt;
		var id = rec.data.id;
		if ("empty" == id) {
			ct.targetField.disable();
			ct.numField.disable();
		} else {
			ct.targetField.enable();
			ct.numField.enable();
		}
		if ("bw" == id || "nbw" == id) {
			ct.minField.show();
			ct.maxField.show();
			ct.targetField.hide();
			ct.numField.hide();
		} else if ("gt" == id || "lt" == id || "eq" == id || "ne" == id) {
			ct.minField.hide();
			ct.maxField.hide();
			ct.targetField.hide();
			ct.numField.show();
		} else {
			ct.minField.hide();
			ct.maxField.hide();
			ct.targetField.show();
			ct.numField.hide();
		}
		ct.doLayout();
	},
	showDataRangeBorder : function(flag, saveflag) {
		if (this.dataRange) {
			this.spreadsheet.sm.showDataRangeBorder(this.dataRange, flag,
					saveflag);
		}
	},
	pushdown : function(stop) {
		this.stopHideDataRange = stop;
		this.hide();
	},
	onHideFn : function() {
		if (!this.stopHideDataRange) {
			this.showDataRangeBorder();
			this.spreadsheet.sm.fireEvent("renderborder");
		}
		delete this.stopHideDataRange;
	},
	popupDataRange : function(btn) {
		var rangeField = btn.ownerCt.items.first();
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var drSelector = ss.dataRangeSelector;
		var sender = this;
		sender.pushdown();
		drSelector.popup(null, rangeField.getValue(), function(range) {
					var parts = range.split(",");
					var arr = [];
					for (var i = 0, len = parts.length; i < len; i++) {
						arr.push(ds.parseText2Pos(parts[i]));
					}
					rangeField.setValue(range);
					sender.show();
					sender.initDataRange(arr);
					sender.showDataRangeBorder(true);
				}, this);
	},
	onColorSelectFn : function(cp, color) {
		var sender = this.colorPalette.sender;
		var checkBox = this.colorPalette.checkbox;
		var el = sender.getEl();
		checkBox.setValue(true);
		el.setStyle("background-color", "#" + color);
		this.colorMenu.hide();
	},
	addCondtionFormat : function() {
		var ct = this.createConditionFormat();
		var count = this.items.getCount();
		if (0 == count) {
			this.body.update("");
		}
		if (count) {
			var first = this.items.get(0);
		} else {
		}
		this.add(ct);
		this.doLayout();
		var rangeField = ct.items.get(0);
		rangeField.setValue(this.getDataRangeStr());
		this.syncShadow();
	},
	onDeleteConditionFormat : function(btn) {
		var ct = btn.ownerCt;
		var len = this.items.getCount();
		if (1 <= len) {
			this.remove(ct);
			if (2 == len) {
				var first = this.items.get(0);
			}
			this.doLayout();
			this.syncShadow();
			if (1 == len) {
				this.body.update("<div class=\"x-view-hinttext\">"
						+ feyaSoft.ss.lang.noConditionFormat + "</div>");
			}
		}
	},
	getConditionFormatInfo : function() {
		var json = [];
		var ss = this.spreadsheet;
		var ds = ss.ds;
		this.items.each(function(it) {
					var refStr = it.rangeField.getValue();
					var pos = ds.parseText2Pos(refStr);
					var o = {
						pos : pos,
						opt : it.optionCombo.getValue(),
						color : it.textBox.getEl().getStyle("background-color"),
						bgc : it.backgroundBox.getEl()
								.getStyle("background-color")
					};
					var opt = o.opt;
					if ("empty" != opt) {
						if ("bw" == opt || "nbw" == opt) {
							o.min = it.minField.getValue();
							o.max = it.maxField.getValue();
						} else if ("gt" == opt || "lt" == opt || "eq" == opt
								|| "ne" == opt) {
							o.tgt = it.numField.getValue();
						} else {
							o.tgt = it.targetField.getValue();
						}
					}
					json.push(o);
				}, this);
		return json;
	},
	onApply : function() {
		var valid = true;
		this.items.each(function(it) {
					if (!it.isValid()) {
						valid = false;
					}
				});
		if (valid) {
			var json = this.getConditionFormatInfo();
			this.spreadsheet.updateConditionFormat(json);
			this.hide();
		} else {
			Ext.Msg.alert(feyaSoft.ss.lang.hint, feyaSoft.ss.lang.fixInvalid);
		}
	},
	onAddFn : function() {
		this.addCondtionFormat();
	},
	initDataRange : function(rt) {
		var ss = this.spreadsheet;
		var setting = this.setting || {};
		var sm = ss.sm;
		var ds = ss.ds;
		if (!rt) {
			var res = sm
					.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			rt = [{
						x : res.minPos.x,
						y : res.minPos.y,
						ex : res.maxPos.x,
						ey : res.maxPos.y,
						ab : true,
						sheetIndex : ds.activeSheet
					}];
		}
		this.dataRange = rt;
		this.combineDir = "y";
		this.dataMatrix = ss.combineRanges(rt, this.combineDir);
	},
	getDataRangeStr : function() {
		var ss = this.spreadsheet;
		if (this.dataRange) {
			var arr = [];
			for (var i = 0, len = this.dataRange.length; i < len; i++) {
				var it = this.dataRange[i];
				arr.push(ss.encodeDataRange({
							minPos : {
								x : it.x,
								y : it.y
							},
							maxPos : {
								x : it.ex,
								y : it.ey
							}
						}));
			}
			return arr.join(",");
		} else {
			return "";
		}
	},
	popup : function(config) {
		config = config || {};
		var ss = this.spreadsheet;
		var cfm = config.cfm;
		if (cfm && 0 < cfm.length) {
			for (var i = 0, count = this.items.getCount(); i < count; i++) {
				var it = this.items.get(0);
				this.remove(it);
			}
			if (this.body) {
				this.body.update("");
			}
			for (var i = 0, len = cfm.length; i < len; i++) {
				var it = cfm[i], pos = it.pos;
				var ct = this.createConditionFormat();
				this.add(ct);
				var posStr = ss.encodeDataRange({
							minPos : {
								x : pos.x,
								y : pos.y
							},
							maxPos : {
								x : pos.ex,
								y : pos.ey
							}
						});
				ct.rangeField.setValue(posStr);
				ct.optionCombo.setValue(it.opt);
				if ("empty" == it.opt) {
					ct.targetField.disable();
				} else {
					ct.targetField.enable();
				}
				var opt = it.opt;
				if ("gt" == opt || "lt" == opt || "eq" == opt || "ne" == opt) {
					ct.numField.setValue(it.tgt);
				} else {
					ct.targetField.setValue(it.tgt);
				}
				ct.minField.setValue(it.min);
				ct.maxField.setValue(it.max);
				if ("bw" == opt || "nbw" == opt) {
					ct.minField.show();
					ct.maxField.show();
					ct.targetField.hide();
					ct.numField.hide();
				} else if ("gt" == opt || "lt" == opt || "eq" == opt
						|| "ne" == opt) {
					ct.minField.hide();
					ct.maxField.hide();
					ct.targetField.hide();
					ct.numField.show();
				} else {
					ct.minField.hide();
					ct.maxField.hide();
					ct.targetField.show();
					ct.numField.hide();
				}
				if (it.color) {
					ct.textCb.setValue("transparent" !== it.color);
					var el = ct.textBox.getEl();
					if (el) {
						el.setStyle("background-color", it.color);
					} else {
						ct.textBox.style = "background-color:" + it.color + ";";
					}
				} else {
					ct.textCb.setValue(false);
					var el = ct.textBox.getEl();
					if (el) {
						el.setStyle("background-color", "transparent");
					} else {
						ct.textBox.style = "background-color:transparent;";
					}
				}
				if (it.bgc) {
					ct.backgroundCb.setValue("transparent" !== it.bgc);
					var el = ct.backgroundBox.getEl();
					if (el) {
						el.setStyle("background-color", it.bgc);
					} else {
						ct.backgroundBox.style = "background-color:" + it.bgc
								+ ";";
					}
				} else {
					ct.backgroundCb.setValue(false);
					var el = ct.backgroundBox.getEl();
					if (el) {
						el.setStyle("background-color", "transparent");
					} else {
						ct.backgroundBox.style = "background-color:transparent;";
					}
				}
			}
			if (1 == this.items.getCount()) {
			}
			this.show();
		} else {
			var count = this.items.getCount();
			if (0 < count) {
				for (var i = 1; i < count; i++) {
					var it = this.items.get(1);
					this.remove(it);
				}
				if (this.body) {
					this.body.update("");
				}
			} else {
				if (this.body) {
					this.body.update("");
				}
				var ct = this.createConditionFormat();
				this.add(ct);
			}
			this.initDataRange();
			var str = this.getDataRangeStr();
			var ct = this.items.get(0);
			ct.rangeField.setValue(str);
			ct.optionCombo.setValue("include");
			ct.targetField.reset();
			this.show();
		}
	}
});
Ext.ns("Ext.ss.popup");
Ext.ss.popup.FormulaFunctionWin = Ext.extend(Ext.Window, {
	initComponent : function() {
		this.functionTypeCombo = new Ext.form.ComboBox({
					editable : false,
					store : new Ext.data.SimpleStore({
								fields : ["type", "text"]
							}),
					listeners : {
						select : {
							fn : this.__onFunctionTypeComboSelectFn,
							scope : this
						}
					},
					displayField : "text",
					valueField : "type",
					mode : "local",
					triggerAction : "all",
					width : 150,
					selectOnFocus : true
				});
		this.listView = new Ext.list.ListView({
			hideHeaders : true,
			region : "center",
			store : new Ext.data.ArrayStore({
						fields : ["name", "example", "description", "type"],
						sortInfo : {
							field : "name",
							direction : "ASC"
						},
						data : []
					}),
			multiSelect : true,
			emptyText : "No result to display",
			reserveScrollOffset : true,
			columns : [{
						dataIndex : "name",
						flex : 1
					}, {
						dataIndex : "type",
						flex : 2,
						tpl : new Ext.XTemplate(["{type:this.renderType()}"]
										.join(""), {
									renderType : function(v) {
										return "<font color=\"gray\">"
												+ (feyaSoft.ss.lang[v] || v)
												+ "</font>";
									}
								})
					}],
			tpl : new Ext.XTemplate(
					"<tpl for=\"rows\">",
					"<div style=\"background-color:<tpl if=\"xindex%2 == 0\"> #f2edf2</tpl>;\" ><dl> ",
					"<tpl for=\"parent.columns\">",
					"<dt style=\"width:{[values.width*100]}%;text-align:{align};\">",
					"<em unselectable=\"on\"<tpl if=\"cls\"> class=\"{cls}</tpl>\">",
					"&nbsp;&nbsp;&nbsp;{[values.tpl.apply(parent)]}",
					"</em></dt>", "</tpl>", "<div class=\"x-clear\"></div>",
					"</dl></div>", "</tpl>")
		});
		this.descriptionPanel = new Ext.Panel({
					autoScroll : true,
					frame : true
				});
		Ext.apply(this, {
			width : 350,
			height : 400,
			layout : "border",
			resizable : false,
			bodyStyle : "background:#ffffff;",
			tbar : [this.functionTypeCombo, "->", {
				text : feyaSoft.lang.common.help,
				handler : function() {
					var newWindow = window
							.open(
									"http://www.cubedrive.com/wordPublic?id=8XrMMXO3fXk_&viewStatus=publicView",
									"_blank");
					newWindow.focus();
				}
			}],
			items : [this.listView, {
						xtype : "container",
						region : "south",
						height : 80,
						style : "padding:1px;",
						items : [this.descriptionPanel],
						layout : "fit"
					}],
			buttonAlign : "right",
			buttons : [{
						text : feyaSoft.ss.lang.close,
						handler : this.__onClose,
						scope : this
					}]
		});
		Ext.ss.popup.FormulaFunctionWin.superclass.initComponent.call(this);
		this.listView.on("mouseenter", this.__onMouseenterFn, this);
		this.listView.on("click", this.__onSelectionchangeFn, this);
	},
	popup : function() {
		this.nameChanged = false;
		var ss = this.spreadsheet;
		var sm = ss.sm, ds = ss.ds;
		this.show();
		if (this.listView.store.getCount() == 0) {
			this.__initData();
		}
	},
	__initData : function() {
		var formulaData = Ext.ss.common.Mask.formulaData;
		var typeBoxData = [], tmpAry = [];
		for (var i = 0; i < formulaData.length; i++) {
			var typeValue = formulaData[i][3];
			if (!this.__arrayContain(tmpAry, typeValue)) {
				tmpAry.push(typeValue);
				typeBoxData.push([typeValue, feyaSoft.ss.lang[typeValue]]);
			}
		}
		typeBoxData = [["all", feyaSoft.ss.lang.all]].concat(typeBoxData);
		this.functionTypeCombo.getStore().loadData(typeBoxData);
		this.listView.store.loadData(Ext.ss.common.Mask.formulaData);
		this.functionTypeCombo.setValue("all");
	},
	__arrayContain : function(array, item) {
		var i, ln;
		for (i = 0, ln = array.length; i < ln; i++) {
			if (array[i] === item) {
				return true;
			}
		}
		return false;
	},
	__onFunctionTypeComboSelectFn : function(combo, rd, index) {
		var resultData = this.__filterFormulaData(combo.getValue());
		this.listView.store.loadData(resultData);
	},
	__filterFormulaData : function(type) {
		var formulaData = Ext.ss.common.Mask.formulaData;
		if (type == "all") {
			return formulaData;
		}
		var resultData = [];
		for (var i = 0; i < formulaData.length; i++) {
			var tmpDta = formulaData[i];
			if (tmpDta[3] == type) {
				resultData.push(tmpDta);
			}
		}
		return resultData;
	},
	__onMouseenterFn : function(grid, index, item, e) {
		var rd = this.listView.store.getAt(index);
		this.descriptionPanel.body.update(rd.data.description);
	},
	__onSelectionchangeFn : function(view, index, item, e) {
		var ss = this.spreadsheet;
		var editor = ss.editor;
		var rd = this.listView.store.getAt(index);
		editor.movingFormulaFlag = true;
		editor.startEditing(ss.sm.focusCell, "=" + rd.data.name + "(");
		this.__onClose();
	},
	__onClose : function() {
		this.hide();
	}
});
Ext.ns("Ext.ss.popup.about");
Ext.ss.popup.about.AboutSS = function(config) {
	Ext.apply(this, config);
	var myHtml = "<div style=\"float:left;width: 300px;padding:30px 15px 20px 20px;\"><img src=\"images/enterpriseLogo.png\"/></div>"
			+ "<div style=\"float:left;width: 200px;padding:20px 0 10px 0;\">"
			+ " <span style=\"font-size:14px;\"><b>EnterpriseSheet.com</b></span><br/><span style=\"font-size:12px;\">A Spreadsheet Solution</span><br/>"
			+ " <font color=\"grey\">Version "
			+ Ext.ss.common.Mask.version
			+ "</font>"
			+ " <br/><br/>"
			+ " Copyright &copy; 2012 Feyasoft Inc. All right reserved.<br/>"
			+ "</div>"
			+ "<div style=\"clear:left;padding:0 20px 20px;\">"
			+ " EnterpriseSheet and EnterpriseSheet logos are the trademarks of the Feyasoft Inc. All right reserved. <br/><br/>"
			+ " Want to run Spreadsheet in your server and maximize your Performance, Productivity and Security, "
			+ " please send the email to: <a href=\"mailto:info@EnterpriseSheet.com\">info@enterpriseSheet.com</a>. For more detail, please visit: <a href=\"http://www.EnterpriseSheet.com\" TARGET=_BLANK>www.enterpriseSheet.com</a>"
			+ " <br><br><hr>"
			+ " Warning: This computer program is protected by the copyright law and international treaties. Unauthorized "
			+ " reproduction or distribution this program, or any portion of it, maybe result in severe civil and "
			+ " criminal penalties, and will be prosecuted to the maximum extent possible under the law.";
	this.aboutPanel = new Ext.Panel({
				baseCls : "x-plain",
				html : myHtml
			});
	this.activeId = 0;
	this.releasePanel = new Ext.Panel({
				layout : "fit",
				autoScroll : true,
				border : false,
				autoLoad : {
					url : "enterprisesheetCommon/_release.gsp",
					scripts : true
				}
			});
	Ext.ss.popup.about.AboutSS.superclass.constructor.call(this, {
				iconCls : "icon_enterpriseSheet",
				title : "About EnterpriseSheet.com [ "
						+ Ext.ss.common.Mask.version + " ]",
				width : 600,
				height : 350,
				closable : true,
				resizable : false,
				modal : true,
				layout : "card",
				border : true,
				activeItem : 0,
				layoutConfig : {
					animate : false,
					deferredRender : true
				},
				items : [this.aboutPanel, this.releasePanel],
				buttons : [{
							text : "&nbsp;&nbsp;&nbsp;Release Notes&nbsp;&nbsp;&nbsp;",
							handler : function() {
								this.getLayout()
										.setActiveItem(this.releasePanel);
								this.activeId = 1;
							},
							scope : this
						}, {
							text : "Ok",
							handler : function() {
								if (this.activeId == 0) {
									this.close();
								} else {
									this.activeId = 0;
									this.getLayout()
											.setActiveItem(this.aboutPanel);
								}
							},
							scope : this
						}]
			});
	this.show();
};
Ext.extend(Ext.ss.popup.about.AboutSS, Ext.Window, {});
Ext.ns("Ext.ss.popup");
Ext.ss.popup.PrintSettingWin = function(config) {
	this.config = config || {};
	Ext.form.Field.prototype.msgTarget = "side";
	this.noGrid = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.ss.lang.notGridLine,
				name : "noGrid",
				checked : false
			});
	this.optionsSet = new Ext.form.FieldSet({
				title : feyaSoft.lang.common.options,
				collapsible : true,
				autoHeight : true,
				items : [this.noGrid]
			});
	this.landRadio = new Ext.form.Radio({
				columnWidth : 0.05,
				hideLabel : true,
				listeners : {
					check : {
						fn : this.onChangeFn,
						scope : this
					}
				},
				inputValue : "land",
				name : "printway"
			});
	this.portraitRadio = new Ext.form.Radio({
				columnWidth : 0.05,
				hideLabel : true,
				listeners : {
					check : {
						fn : this.onChangeFn,
						scope : this
					}
				},
				inputValue : "portrait",
				checked : true,
				name : "printway"
			});
	this.htmlWay = new Ext.form.Radio({
				columnWidth : 0.05,
				hideLabel : true,
				listeners : {
					check : {
						fn : this.onChangeFn,
						scope : this
					}
				},
				inputValue : "htmlway",
				name : "printway"
			});
	this.outPutSet = new Ext.form.FieldSet({
		title : feyaSoft.lang.common.outputWays,
		collapsible : true,
		autoHeight : true,
		layout : "column",
		items : [this.landRadio, {
			columnWidth : 0.26,
			html : "<table radiotype=\"x-radiocss-land\" class=\"x-radiocss\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" height=\"100%\" style=\"font-size:10px;\"><tr><td align=\"center\">"
					+ feyaSoft.lang.ss.landscape
					+ "</td></tr><tr><td align=\"center\"><div style=\"height:30px;border:1px solid #000000;width:50%;\"><div style=\"overflow:hidde;border-top: 1px #000000 dashed;height:1px;\"></div></div></td></tr><tr><td align=\"center\">"
					+ feyaSoft.lang.ss.landscape + "</td></tr></table>",
			style : "margin-right:30px;",
			height : 91
		}, this.portraitRadio, {
			columnWidth : 0.26,
			html : "<table radiotype=\"x-radiocss-portrait\" class=\"x-radiocss\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" height=\"100%\" style=\"font-size:10px;background:#8ace28;\"><tr><td align=\"center\">"
					+ feyaSoft.lang.ss.portrait
					+ "</td></tr><tr><td align=\"center\"><div style=\"height:50px;border:1px solid #000000;width:40%;\"><div style=\"overflow:hidde;border-bottom: 1px #000000 dashed;height:98%;\"></div></div></td></tr><tr><td align=\"center\">"
					+ feyaSoft.lang.ss.portrait + "</td></tr></table>",
			style : "margin-right:30px;",
			height : 91
		}, this.htmlWay, {
			columnWidth : 0.26,
			html : "<table radiotype=\"x-radiocss-htmlway\" class=\"x-radiocss\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" width=\"100%\" height=\"100%\" style=\"font-size:10px;\"><tr><td align=\"center\">"
					+ feyaSoft.lang.ss.HTMLformat
					+ "</td></tr><tr><td align=\"center\"><div style=\"height:50px;border:1px solid #000000;width:50%;\"><div style=\"overflow:hidde;border-top: 1px #000000 dashed;border-bottom: 1px #000000 dashed;height:40%;margin-top:18px;\"></div></div></td></tr><tr><td align=\"center\">"
					+ feyaSoft.lang.ss.HTMLformat + "</td></tr></table>",
			style : "margin-right:30px;",
			height : 91
		}]
	});
	this.formPanel = new Ext.form.FormPanel({
				bodyStyle : "padding:10px;",
				baseCls : "x-plain",
				labelWidth : 80,
				defaultType : "textfield",
				items : [this.optionsSet, this.outPutSet]
			});
	this.printerPanel = new Ext.Panel({
		layout : "border",
		autoScroll : true,
		border : false,
		items : [{
			region : "north",
			autoHeight : true,
			border : false,
			bodyStyle : "background:white;border-bottom:1px solid #bfbfbf;padding:5px;",
			html : feyaSoft.ss.lang.printDesc
		}, {
			region : "center",
			baseCls : "x-plain",
			border : false,
			items : [this.formPanel]
		}]
	});
	Ext.ss.popup.PrintSettingWin.superclass.constructor.call(this, {
				title : feyaSoft.ss.lang.printSheet,
				width : 550,
				height : 350,
				layout : "fit",
				buttonAlign : "right",
				shim : false,
				animCollapse : false,
				constrainHeader : true,
				modal : true,
				items : this.printerPanel,
				resizable : false,
				buttons : [{
							iconCls : "icon_no",
							text : feyaSoft.lang.common.cancel,
							handler : function() {
								this.close();
							},
							scope : this
						}, {
							text : feyaSoft.lang.common.print,
							handler : function() {
								var printWay = "portrait";
								if (this.landRadio.getValue()) {
									printWay = "landscape";
								} else if (this.htmlWay.getValue()) {
									printWay = "html";
								}
								var noGrid = this.noGrid.getValue();
								var url = "ssConvertPDF/exportPDF?noGrid="
										+ noGrid + "&printWay=" + printWay
										+ "&id=" + this.config.fileId;
								if (this.config.callback) {
									this.config.callback.call(
											this.config.scope, url, this);
								}
								this.close();
							},
							scope : this
						}]
			});
	this.show();
	this.outPutSet.body.on("click", this.outputClickFn, this);
};
Ext.extend(Ext.ss.popup.PrintSettingWin, Ext.Window, {
			onChangeFn : function(ro, nval, oval) {
				if (nval) {
					this.chanageTableCss(ro.inputValue);
				}
			},
			chanageTableCss : function(inputVal) {
				var doms = Ext.query("table[radiotype*=x-radiocss-]",
						this.outPutSet.body.dom);
				for (var i = 0; i < doms.length; i++) {
					var radiotype = doms[i].getAttribute("radiotype");
					var tmary = radiotype.split("x-radiocss-");
					if (tmary[1] == inputVal) {
						doms[i].style.background = "#8ace28";
					} else {
						doms[i].style.background = "none";
					}
				}
			},
			outputClickFn : function(e) {
				var target = e.getTarget();
				var tgEl = Ext.get(target);
				var evtEl;
				if (tgEl.hasClass("x-radiocss")) {
					evtEl = tgEl;
				} else {
					evtEl = tgEl.parent(".x-radiocss");
				}
				if (evtEl) {
					var radiotype = evtEl.getAttribute("radiotype");
					var tmary = radiotype.split("x-radiocss-");
					var inputVal = tmary[1];
					if (inputVal == "land") {
						this.landRadio.setValue(true);
					} else if (inputVal == "portrait") {
						this.portraitRadio.setValue(true);
					} else if (inputVal == "htmlway") {
						this.htmlWay.setValue(true);
					}
					this.chanageTableCss(inputVal);
				}
			}
		});
Ext.ns("Ext.ss.popup");
Ext.ss.popup.ImportFileWin = function(config) {
	Ext.apply(this, config);
	var maxExcelImportSize = Ext.get("maxExcelImportSize")
			.getAttribute("value");
	Ext.form.Field.prototype.msgTarget = "side";
	this.imagepath = new Ext.form.FileUploadField({
				emptyText : feyaSoft.lang.file.supportFormat,
				fieldLabel : feyaSoft.lang.file.fileName,
				name : "imagepath",
				allowBlank : false,
				anchor : "95%",
				buttonCfg : {
					text : " ",
					iconCls : "upload-icon"
				}
			});
	this.tipLabel = new Ext.form.DisplayField({
				hideLabel : true,
				labelSeparator : "",
				value : feyaSoft.lang.file.uploadHint,
				anchor : "95%"
			});
	this.formPanel = new Ext.form.FormPanel({
				bodyStyle : "padding:10px;",
				fileUpload : true,
				baseCls : "x-plain",
				labelWidth : 80,
				url : "ssImportExcel/upload",
				defaultType : "textfield",
				items : [this.imagepath, this.tipLabel]
			});
	this.importPanel = new Ext.Panel({
		layout : "border",
		autoScroll : true,
		border : false,
		items : [{
			region : "north",
			autoHeight : true,
			border : false,
			bodyStyle : "background:white;border-bottom:1px solid #bfbfbf;padding:5px;",
			html : "<span style=\"padding: 0 0 0 10px; font: bold 14px Arial, sans-serif; \">"
					+ feyaSoft.lang.file.importExcel
					+ "</span>"
					+ "<ul style=\"padding:8px 0 0 20px;\">"
					+ "<li>"
					+ feyaSoft.lang.file.ssFileUploadDesc
					+ maxExcelImportSize
					+ "k.</li>"
					+ "<li>"
					+ feyaSoft.lang.file.takeWhileToProcess + "</li>" + "</ul>"
		}, {
			region : "center",
			baseCls : "x-plain",
			border : false,
			layout : "fit",
			items : [this.formPanel]
		}]
	});
	Ext.ss.popup.ImportFileWin.superclass.constructor.call(this, {
				title : feyaSoft.lang.common.fileImport,
				width : 500,
				height : 250,
				minWidth : 500,
				layout : "fit",
				buttonAlign : "right",
				shim : false,
				animCollapse : false,
				constrainHeader : true,
				modal : true,
				items : this.importPanel,
				resizable : false,
				buttons : [{
							iconCls : "icon_no",
							text : feyaSoft.lang.common.cancel,
							handler : function() {
								this.close();
							},
							scope : this
						}, {
							text : feyaSoft.lang.common['import'],
							handler : function() {
								if (this.formPanel.form.isValid()) {
									this.formPanel.form.submit({
												waitMsg : "In processing",
												failure : function(form, action) {
													Ext.MessageBox.alert(
															"Error Message",
															action.result.info);
												},
												success : function(form, action) {
													var jsonData = action.result;
													if (jsonData.success) {
														var fileId = jsonData.id;
														config.parentView
																.openFile(fileId);
														this.close();
													} else {
														Ext.MessageBox.alert(
																"Failed",
																jsonData.info);
													}
												},
												scope : this
											});
								}
							},
							scope : this
						}]
			});
	this.show();
};
Ext.extend(Ext.ss.popup.ImportFileWin, Ext.Window, {});
Ext.ns("Ext.ss");
Ext.ns("Ext.ss");
Ext.ss.DoList = Ext.extend(Ext.menu.Menu, {
	minWidth : 200,
	maxHeight : 300,
	scrollerOffset : 18,
	defaultDisplay : "&nbsp;",
	plain : true,
	onRender : function(ct, position) {
		if (!ct) {
			ct = Ext.getBody();
		}
		var dh = {
			id : this.getId(),
			cls : "x-menu " + (this.floating ? "x-menu-floating x-layer " : "")
					+ (this.cls || "") + (this.plain ? " x-menu-plain" : "")
					+ (this.showSeparator ? "" : " x-menu-nosep"),
			style : this.style,
			cn : [{
						tag : "div",
						cls : "x-spreadsheet-dolist",
						cn : [{
									tag : "a",
									cls : "x-menu-focus",
									href : "#",
									onclick : "return false;",
									tabIndex : "-1"
								}, {
									tag : "ul",
									cls : "x-menu-list"
								}]
					}, {
						tag : "span",
						cls : "x-menu-sep",
						html : "&nbsp;"
					}, {
						tag : "div",
						cls : "x-spreadsheet-dolist-foot",
						html : this.defaultDisplay
					}]
		};
		if (this.floating) {
			this.el = new Ext.Layer({
						shadow : this.shadow,
						dh : dh,
						constrain : false,
						parentEl : ct,
						zindex : this.zIndex
					});
		} else {
			this.el = ct.createChild(dh);
		}
		Ext.menu.Menu.superclass.onRender.call(this, ct, position);
		this.focusEl = this.el.child("a.x-menu-focus");
		this.list = this.el.child("div.x-spreadsheet-dolist");
		this.ul = this.el.child("ul.x-menu-list");
		this.foot = this.el.child("div.x-spreadsheet-dolist-foot");
		this.mon(this.ul, {
					scope : this,
					click : this.onClick,
					mouseover : this.onMouseOver,
					mouseout : this.onMouseOut
				});
	},
	onMouseOver : function(e) {
		var t = this.findTargetItem(e);
		if (t) {
			var index = this.items.indexOf(t);
			var i;
			for (i = 0; i <= index; i++) {
				var it = this.items.get(i);
				if (it.canActivate && !it.disabled) {
					this.setActiveItem(it, true);
				}
			}
			for (var len = this.items.getCount(); i < len; i++) {
				var it = this.items.get(i);
				it.deactivate();
			}
			this.activeCount = index + 1;
			this.fireEvent("mouseover", this, e, t);
		} else {
			this.fireEvent("mouseover", this, e);
		}
		this.over = true;
	},
	deactiveAllItems : function() {
		for (var i = 0, len = this.items.getCount(); i < len; i++) {
			var it = this.items.get(i);
			it.deactivate();
		}
		this.foot.dom.innerHTML = this.defaultDisplay;
	},
	onMouseOut : function(e) {
		var t = this.findTargetItem(e);
		this.over = false;
		this.fireEvent("mouseout", this, e, t);
	},
	setActiveItem : function(item, autoExpand) {
		if (item != this.activeItem) {
			if ((this.activeItem = item).isFormField) {
				item.focus();
			} else {
				item.activate(autoExpand);
			}
		} else if (autoExpand) {
			item.expandMenu();
		}
	},
	show : function(el, pos, parentMenu) {
		if (this.floating) {
			this.parentMenu = parentMenu;
			if (!this.el) {
				this.render();
				this.doLayout(false, true);
			}
			this.showAt(this.el.getAlignToXY(el, pos || this.defaultAlign,
							this.defaultOffsets), parentMenu);
		} else {
			Ext.menu.Menu.superclass.show.call(this);
		}
		this.deactiveAllItems();
	},
	showAt : function(xy, parentMenu) {
		if (this.fireEvent("beforeshow", this) !== false) {
			this.parentMenu = parentMenu;
			if (!this.el) {
				this.render();
			}
			if (this.enableScrolling) {
				this.el.setXY(xy);
				this.constrainScroll(xy[1]);
				xy = [this.el.adjustForConstraints(xy)[0], xy[1]];
			} else {
				xy = this.el.adjustForConstraints(xy);
			}
			this.el.setStyle("width", "");
			if (this.el.getWidth() < this.minWidth) {
				this.el.setWidth(this.minWidth);
			}
			this.el.setXY(xy);
			this.el.show();
			Ext.menu.Menu.superclass.onShow.call(this);
			if (Ext.isIE) {
				this.fireEvent("autosize", this);
				if (!Ext.isIE8) {
					this.el.repaint();
				}
			}
			this.hidden = false;
			this.focus();
			this.fireEvent("show", this);
		}
	},
	constrainScroll : function(y) {
		var max, full = this.ul.setHeight("auto").getHeight(), returnY = y, normalY, parentEl, scrollTop, viewHeight;
		if (this.floating) {
			parentEl = Ext.fly(this.el.dom.parentNode);
			scrollTop = parentEl.getScroll().top;
			viewHeight = parentEl.getViewSize().height;
			normalY = y - scrollTop;
			max = this.maxHeight ? this.maxHeight : viewHeight - normalY;
			if (full > viewHeight) {
				max = viewHeight;
				returnY = y - normalY;
			} else if (max < full) {
				returnY = y - (full - max);
				max = full;
			}
		} else {
			max = this.getHeight();
		}
		if (this.maxHeight) {
			max = Math.min(this.maxHeight, max);
		}
		if (full > max && max > 0) {
			this.activeMax = max - this.scrollerHeight * 2
					- this.el.getFrameWidth("tb")
					- Ext.num(this.el.shadowOffset, 0);
			this.list.setHeight(this.activeMax);
			this.el.select(".x-menu-scroller").setDisplayed("none");
		} else {
			this.list.setHeight(full);
			this.el.select(".x-menu-scroller").setDisplayed("none");
		}
		this.ul.dom.scrollTop = 0;
		return returnY;
	}
});
Ext.ns("Ext.ss.menu");
Ext.ss.menu.FilterMenu = Ext.extend(Ext.menu.Menu, {});
Ext.ns("Ext.ss");
Ext.ss.FunctionBox = {
	precisionDigital : 3,
	acos : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon(arguments, "acos");
	},
	cos : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon(arguments, "cos");
	},
	asin : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon(arguments, "asin");
	},
	sin : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon(arguments, "sin");
	},
	atan : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon(arguments, "atan");
	},
	tan : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon(arguments, "tan");
	},
	isblank : function() {
		return Ext.ss.common.FunctionBoxHelper.statisticalCommon(arguments,
				"isblank");
	},
	isnumber : function() {
		return Ext.ss.common.FunctionBoxHelper.statisticalCommon(arguments,
				"isnumber");
	},
	istext : function() {
		return Ext.ss.common.FunctionBoxHelper.statisticalCommon(arguments,
				"istext");
	},
	islogical : function() {
		return Ext.ss.common.FunctionBoxHelper.statisticalCommon(arguments,
				"islogical");
	},
	n : function() {
		return Ext.ss.common.FunctionBoxHelper
				.statisticalCommon(arguments, "n");
	},
	odd : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "odd");
	},
	even : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "even");
	},
	abs : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "abs");
	},
	sqrt : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "sqrt");
	},
	sqrtpi : function() {
		return Ext.ss.common.FunctionBoxHelper
				.aMathCommon2(arguments, "sqrtpi");
	},
	degrees : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments,
				"degrees");
	},
	exp : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "exp");
	},
	fact : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "fact");
	},
	'int' : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "int");
	},
	radians : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments,
				"radians");
	},
	iseven : function() {
		return Ext.ss.common.FunctionBoxHelper
				.aMathCommon2(arguments, "iseven");
	},
	isodd : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "isodd");
	},
	sign : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon2(arguments, "sign");
	},
	count : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon3(arguments, "count");
	},
	counta : function() {
		return Ext.ss.common.FunctionBoxHelper
				.aMathCommon3(arguments, "counta");
	},
	countblank : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon3(arguments,
				"countblank");
	},
	floor : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon4(arguments, "floor");
	},
	ceiling : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon4(arguments,
				"ceiling");
	},
	mod : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon4(arguments, "mod");
	},
	randbetween : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon4(arguments,
				"randbetween");
	},
	quotient : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon4(arguments,
				"quotient");
	},
	atan2 : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathCommon4(arguments, "atan2");
	},
	round : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathRound(arguments, "round");
	},
	rounddown : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathRound(arguments,
				"rounddown");
	},
	roundup : function() {
		return Ext.ss.common.FunctionBoxHelper.aMathRound(arguments, "roundup");
	},
	upper : function() {
		return Ext.ss.common.FunctionBoxHelper.stringComm1(arguments, "upper");
	},
	lower : function() {
		return Ext.ss.common.FunctionBoxHelper.stringComm1(arguments, "lower");
	},
	len : function() {
		return Ext.ss.common.FunctionBoxHelper.stringComm1(arguments, "len");
	},
	'true' : function() {
		if (4 == arguments.length) {
			return "TRUE";
		}
		throw "SS_ERROR_NA";
	},
	'false' : function() {
		if (4 == arguments.length) {
			return "FALSE";
		}
		throw "SS_ERROR_NA";
	},
	pi : function() {
		if (arguments.length != 4) {
			throw "SS_ERROR_NA";
		}
		return Math.PI;
	},
	rand : function() {
		if (arguments.length != 4) {
			throw "SS_ERROR_NA";
		}
		return Math.random();
	},
	value : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (1 != len) {
			throw "SS_ERROR_NA";
		}
		var val = arguments[0];
		if (Ext.isObject(val)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : val.ox,
						oy : val.oy
					}, {
						ox : val.oex,
						oy : val.oey
					});
			var minx = parseInt(val.ox) + x, maxx = parseInt(val.oex) + x, miny = parseInt(val.oy)
					+ y, maxy = parseInt(val.oey) + y;
			ds.checkSheetIndexValid(val.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(val.sheetIndex)) {
				curSheetIndex = val.sheetIndex;
			}
			if (minx == maxx && miny == maxy) {
				val = ds.getCellValue(minx, miny, curSheetIndex);
				var checkDate = ds.prepareDate(val);
				if (checkDate) {
					val = Ext.ss.common.Helper.convertDateToNum(checkDate);
					return val;
				}
			} else {
				throw "SS_ERROR_VALUE";
			}
		} else if (val === undefined || Ext.ss.common.Mask.isEmptyStr(val)) {
			throw "SS_ERROR_VALUE";
		}
		var num = Number(val);
		if (!Ext.isNumber(num)) {
			throw "SS_ERROR_VALUE";
		}
		return num;
	},
	text : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 != len) {
			throw "SS_ERROR_NA";
		}
		var val = arguments[0], fm = arguments[1];
		val = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y, sheetIndex,
				val);
		fm = Ext.ss.common.FunctionBoxHelper
				.getObjVal(ds, x, y, sheetIndex, fm);
		var num = Number(val);
		if (!Ext.isNumber(num)) {
			if (fm && Ext.isString(fm) && fm.length > 3) {
				fm = fm.replace(/yyyy/i, "Y").replace(/dd/i, "d").replace(
						/mmm/gi, "M").replace(/mm/gi, "m").replace(/hh/i, "h");
				if (fm.indexOf(":m") != -1) {
					fm = fm.replace(/:m/i, ":i");
				}
			}
			var checkDate = ds.prepareDateStr(val, fm);
			if (checkDate && false != checkDate) {
				return checkDate + " ";
			} else {
				return val;
			}
		} else {
			var txt = Ext.ss.common.FunctionBoxHelper.formatNumber(val, fm);
			return txt;
		}
	},
	rept : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 != len) {
			throw "SS_ERROR_NA";
		}
		var txt = arguments[0], times = arguments[1];
		txt = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y, sheetIndex,
				txt);
		times = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y, sheetIndex,
				times);
		times = Number(times);
		if (Ext.isNumber(times)) {
			if (0 > times) {
				throw "SS_ERROR_VALUE";
			} else if (0 == times) {
				return "";
			}
		} else {
			throw "SS_ERROR_VALUE";
		}
		if (Ext.isNumber(txt) || Ext.isString(txt)) {
			txt = txt.toString();
			var arr = [];
			for (var i = 0; i < times; i++) {
				arr.push(txt);
			}
			return arr.join("");
		} else {
			throw "SS_ERROR_VALUE";
		}
	},
	find : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 > len || 3 < len) {
			throw "SS_ERROR_NA";
		}
		var source = arguments[0], target = arguments[1], start = 2 == len
				? 0
				: arguments[2];
		target = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, target);
		if (target === undefined || Ext.ss.common.Mask.isEmptyStr(target)) {
			throw "SS_ERROR_VALUE";
		}
		source = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, source);
		if (source === undefined || Ext.ss.common.Mask.isEmptyStr(source)) {
			throw "SS_ERROR_VALUE";
		}
		start = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, start);
		if ((Ext.isNumber(source) || Ext.isString(source))
				&& (Ext.isNumber(target) || Ext.isString(target))) {
			source = source.toString();
			target = target.toString();
			if (0 < start) {
				start = start - 1;
				target = target.slice(start, target.length);
			} else {
				start = 0;
			}
			var index = target.indexOf(source);
			if (-1 == index) {
				throw "SS_ERROR_VALUE";
			}
			return index + 1 + start;
		} else {
			throw "SS_ERROR_VALUE";
		}
	},
	mid : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 != len) {
			throw "SS_ERROR_NA";
		}
		var str = arguments[0], pos = arguments[1], leng = arguments[2];
		str = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y, sheetIndex,
				str);
		if (str === undefined || Ext.ss.common.Mask.isEmptyStr(str)) {
			throw "SS_ERROR_VALUE";
		}
		pos = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pos);
		leng = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, leng);
		if ((Ext.isNumber(str) || Ext.isString(str)) && Ext.isNumber(pos)
				&& Ext.isNumber(leng)) {
			str = str.toString();
			if (0 < pos && leng >= 0) {
				return str.substr(pos - 1, leng);
			} else {
				throw "SS_ERROR_VALUE";
			}
		} else {
			throw "SS_ERROR_VALUE";
		}
	},
	date : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 != len) {
			throw "SS_ERROR_NA";
		}
		var year = arguments[0], month = arguments[1], day = arguments[2];
		year = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, year);
		month = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, month);
		day = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, day);
		var date = new Date(year, month - 1, day);
		if (date) {
			return date.format("m/d/Y");
		} else {
			throw "SS_ERROR_VALUE";
		}
	},
	today : function() {
		if (4 == arguments.length) {
			var today = new Date;
			today = today.format("m/d/Y");
			return today;
		} else {
			throw "SS_ERROR_NA";
		}
	},
	weekday : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (1 != len && 2 != len) {
			throw "SS_ERROR_NA";
		}
		var enterDate = arguments[0];
		var retVal = 1;
		if (len == 2) {
			retVal = arguments[1];
			retVal = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
					sheetIndex, retVal);
		}
		if (retVal === undefined) {
			throw "SS_ERROR_NUM";
		}
		if (retVal != 1 && retVal != 2 && retVal != 3) {
			throw "SS_ERROR_NUM";
		}
		enterDate = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, enterDate);
		if (enterDate === undefined || Ext.ss.common.Mask.isEmptyStr(enterDate)) {
			throw "SS_ERROR_NUM";
		}
		var checkDate = ds.prepareDate(enterDate);
		if (checkDate) {
			var result = checkDate.getDay();
			if (retVal == 1) {
				result += 1;
			}
			if (retVal == 2 && result == 0) {
				result = 7;
			}
			if (retVal == 3) {
				result -= 1;
				if (result == -1) {
					result = 6;
				}
			}
			return result;
		} else {
			throw "SS_ERROR_VALUE";
		}
	},
	concatenate : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len) {
			throw "SS_ERROR_NA";
		}
		var res = [];
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						var str = num.toString();
						if (str) {
							res.push(str);
						}
					}
				}
			} else {
				if (Ext.isArray(posOffset)) {
					res = res.concat(posOffset);
				} else if (false != Ext.type(posOffset)) {
					var str = posOffset.toString();
					if (str) {
						res.push(str);
					}
				}
			}
		}
		return res.join("");
	},
	year : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (1 != len) {
			throw "SS_ERROR_NA";
		}
		var pos = arguments[0];
		var val = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, pos);
		var arr = Ext.ss.common.Mask.dateFormats;
		for (var i = 0, len = arr.length; i < len; i++) {
			var f = arr[i];
			try {
				var date = Date.parseDate(val, f);
				if (date.format(f) == val) {
					return date.getFullYear();
				}
			} catch (e) {
			}
		}
		if (Ext.isNumber(Number(val))) {
			var myDate = new Date;
			myDate.setFullYear(1900, 0, val);
			return myDate.getFullYear();
		} else {
			throw "SS_ERROR_NA";
		}
	},
	month : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (1 != len) {
			throw "SS_ERROR_NA";
		}
		var pos = arguments[0];
		var val = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, pos);
		var arr = Ext.ss.common.Mask.dateFormats;
		for (var i = 0, len = arr.length; i < len; i++) {
			var f = arr[i];
			try {
				var date = Date.parseDate(val, f);
				if (date.format(f) == val) {
					return date.getMonth() + 1;
				}
			} catch (e) {
			}
		}
		if (Ext.isNumber(Number(val))) {
			var myDate = new Date;
			myDate.setFullYear(1900, 0, val);
			return myDate.getMonth() + 1;
		}
		throw "SS_ERROR_NA";
	},
	combin : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 != len) {
			throw "SS_ERROR_NA";
		}
		var number = arguments[0], choose = arguments[1];
		var number = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, number);
		var choose = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, choose);
		var sum1 = 1, sum2 = 1;
		for (var i = Math.floor(number); i > Math.floor(number)
				- Math.floor(choose); i--) {
			sum1 = sum1 * i;
		}
		for (var i = 1; i <= Math.floor(choose); i++) {
			sum2 = sum2 * i;
		}
		return Math.floor(sum1 / sum2);
	},
	offset : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 <= len && len <= 5) {
			var originPos = arguments[0];
			if (Ext.isObject(originPos)) {
				originPos = Ext.apply({}, originPos);
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : originPos.ox,
							oy : originPos.oy
						}, {
							ox : originPos.oex,
							oy : originPos.oey
						});
				var rowOffset = arguments[1], colOffset = arguments[2], rowLen, colLen;
				if (4 <= len) {
					rowLen = arguments[3];
				}
				if (5 == len) {
					colLen = arguments[4];
				}
				if (Ext.isObject(rowOffset)) {
					ds.fireEvent("project", ds, {
								x : x,
								y : y
							}, {
								ox : rowOffset.ox,
								oy : rowOffset.oy
							}, {
								ox : rowOffset.oex,
								oy : rowOffset.oey
							});
					var roMinx = rowOffset.ox + x, roMiny = rowOffset.oy + y, roMaxx = rowOffset.oex
							+ x, roMaxy = rowOffset.oey + y;
					ds.checkSheetIndexValid(rowOffset.sheetIndex);
					var curSheetIndex = sheetIndex;
					if (false != Ext.type(rowOffset.sheetIndex)) {
						curSheetIndex = rowOffset.sheetIndex;
					}
					if (roMinx == roMaxx && roMiny == roMaxy) {
						rowOffset = ds.getCellValue(roMinx, roMiny,
								curSheetIndex);
					}
				}
				rowOffset = Number(rowOffset);
				if (!Ext.isNumber(rowOffset)) {
					throw "SS_ERROR_VALUE";
				}
				if (Ext.isObject(colOffset)) {
					ds.fireEvent("project", ds, {
								x : x,
								y : y
							}, {
								ox : colOffset.ox,
								oy : colOffset.oy
							}, {
								ox : colOffset.oex,
								oy : colOffset.oey
							});
					var coMinx = colOffset.ox + x, coMiny = colOffset.oy + y, coMaxx = colOffset.oex
							+ x, coMaxy = colOffset.oey + y;
					ds.checkSheetIndexValid(colOffset.sheetIndex);
					var curSheetIndex = sheetIndex;
					if (false != Ext.type(colOffset.sheetIndex)) {
						curSheetIndex = colOffset.sheetIndex;
					}
					if (coMinx == coMaxx && coMiny == coMaxy) {
						colOffset = ds.getCellValue(coMinx, coMiny,
								curSheetIndex);
					}
				}
				colOffset = Number(colOffset);
				if (!Ext.isNumber(colOffset)) {
					throw "SS_ERROR_VALUE";
				}
				if (Ext.isObject(rowLen)) {
					ds.fireEvent("project", ds, {
								x : x,
								y : y
							}, {
								ox : rowLen.ox,
								oy : rowLen.oy
							}, {
								ox : rowLen.oex,
								oy : rowLen.oey
							});
					var rlMinx = rowLen.ox + x, rlMiny = rowLen.oy + y, rlMaxx = rowLen.oex
							+ x, rlMaxy = rowLen.oey + y;
					ds.checkSheetIndexValid(rowLen.sheetIndex);
					var curSheetIndex = sheetIndex;
					if (false != Ext.type(rowLen.sheetIndex)) {
						curSheetIndex = rowLen.sheetIndex;
					}
					if (rlMinx == rlMaxx && rlMiny == rlMaxy) {
						rowLen = ds.getCellValue(rlMinx, rlMiny, curSheetIndex);
					}
				}
				if (Ext.isObject(colLen)) {
					ds.fireEvent("project", ds, {
								x : x,
								y : y
							}, {
								ox : colLen.ox,
								oy : colLen.oy
							}, {
								ox : colLen.oex,
								oy : colLen.oey
							});
					var clMinx = colLen.ox + x, clMiny = colLen.oy + y, clMaxx = colLen.oex
							+ x, clMaxy = colLen.oey + y;
					ds.checkSheetIndexValid(colLen.sheetIndex);
					var curSheetIndex = sheetIndex;
					if (false != Ext.type(colLen.sheetIndex)) {
						curSheetIndex = colLen.sheetIndex;
					}
					if (clMinx == clMaxx && clMiny == clMaxy) {
						colLen = ds.getCellValue(clMinx, clMiny, curSheetIndex);
					}
				}
				originPos.ox += rowOffset;
				originPos.oex += rowOffset;
				originPos.oy += colOffset;
				originPos.oey += colOffset;
				if (Ext.isDefined(rowLen)) {
					rowLen = Number(rowLen);
					if (Ext.isNumber(rowLen) && 0 < rowLen) {
						originPos.oex = originPos.ox + rowLen - 1;
					} else {
						throw "SS_ERROR_VALUE";
					}
				}
				if (Ext.isDefined(colLen)) {
					rowLen = Number(colLen);
					if (Ext.isNumber(colLen) && 0 < colLen) {
						originPos.oey = originPos.oy + colLen - 1;
					} else {
						throw "SS_ERROR_VALUE";
					}
				}
				return originPos;
			} else {
				throw "#PARAM_ERROR";
			}
		} else {
			throw "SS_ERROR_NA";
		}
	},
	indirect : function() {
		return Ext.ss.FunctionBox.getCellByOffset.apply(Ext.ss.FunctionBox,
				arguments);
	},
	areas : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		if (len - 4 == 0) {
			throw "SS_ERROR_NA";
		}
		var n = 0;
		var fn = function(args) {
			for (var i = 0, len = args.length; i < len; i++) {
				var arg = args[i];
				if (Ext.isObject(arg)) {
					if (Ext.isDefined(arg.ox)) {
						n++;
					}
				} else if (Ext.isArray(arg)) {
					fn(arg);
				}
			}
		};
		fn(arguments);
		return n;
	},
	index : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 < len && len <= 4) {
			var areas = arguments[0], rowIndex, colIndex, areaIndex;
			if (1 == len) {
				rowIndex = 1;
				colIndex = 1;
				areaIndex = 1;
			} else if (2 == len) {
				rowIndex = arguments[1];
				colIndex = 1;
				areaIndex = 1;
			} else if (3 == len) {
				rowIndex = arguments[1];
				colIndex = arguments[2];
				areaIndex = 1;
			} else if (4 == len) {
				rowIndex = arguments[1];
				colIndex = arguments[2];
				areaIndex = arguments[3];
			}
			if (Ext.isObject(rowIndex)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : rowIndex.ox,
							oy : rowIndex.oy
						}, {
							ox : rowIndex.oex,
							oy : rowIndex.oey
						});
				var minx = rowIndex.ox + x, miny = rowIndex.oy + y, maxx = rowIndex.oex
						+ x, maxy = rowIndex.oey + y;
				ds.checkSheetIndexValid(rowIndex.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(rowIndex.sheetIndex)) {
					curSheetIndex = rowIndex.sheetIndex;
				}
				if (minx == maxx && miny == maxy) {
					rowIndex = ds.getCellValue(minx, miny, curSheetIndex);
					rowIndex = Number(rowIndex);
					if (!Ext.isNumber(rowIndex) || 1 > rowIndex) {
						throw "SS_ERROR_VALUE";
					}
				} else {
					throw "SS_ERROR_VALUE";
				}
			} else {
				rowIndex = Number(rowIndex);
				if (!Ext.isNumber(rowIndex)) {
					throw "SS_ERROR_VALUE";
				}
			}
			if (Ext.isObject(colIndex)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : colIndex.ox,
							oy : colIndex.oy
						}, {
							ox : colIndex.oex,
							oy : colIndex.oey
						});
				var minx = colIndex.ox + x, miny = colIndex.oy + y, maxx = colIndex.oex
						+ x, maxy = colIndex.oey + y;
				ds.checkSheetIndexValid(colIndex.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(colIndex.sheetIndex)) {
					curSheetIndex = colIndex.sheetIndex;
				}
				if (minx == maxx && miny == maxy) {
					colIndex = ds.getCellValue(minx, miny, curSheetIndex);
					colIndex = Number(colIndex);
					if (!Ext.isNumber(colIndex) || 1 > colIndex) {
						throw "SS_ERROR_VALUE";
					}
				} else {
					throw "SS_ERROR_VALUE";
				}
			} else {
				colIndex = Number(colIndex);
				if (!Ext.isNumber(colIndex)) {
					throw "SS_ERROR_VALUE";
				}
			}
			if (Ext.isObject(areaIndex)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : areaIndex.ox,
							oy : areaIndex.oy
						}, {
							ox : areaIndex.oex,
							oy : areaIndex.oey
						});
				var minx = areaIndex.ox + x, miny = areaIndex.oy + y, maxx = areaIndex.oex
						+ x, maxy = areaIndex.oey + y;
				ds.checkSheetIndexValid(areaIndex.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(areaIndex.sheetIndex)) {
					curSheetIndex = areaIndex.sheetIndex;
				}
				if (minx == maxx && miny == maxy) {
					areaIndex = ds.getCellValue(minx, miny, curSheetIndex);
					areaIndex = Number(areaIndex);
					if (!Ext.isNumber(areaIndex) || 1 > areaIndex) {
						throw "SS_ERROR_VALUE";
					}
				} else {
					throw "SS_ERROR_VALUE";
				}
			} else {
				areaIndex = Number(areaIndex);
				if (!Ext.isNumber(areaIndex)) {
					throw "SS_ERROR_VALUE";
				}
			}
			if (Ext.isObject(areas) && 1 == areaIndex) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : areas.ox,
							oy : areas.oy
						}, {
							ox : areas.oex,
							oy : areas.oey
						});
				var minx = areas.ox + x, miny = areas.oy + y, maxx = areas.oex
						+ x, maxy = areas.oey + y;
				ds.checkSheetIndexValid(areas.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(areas.sheetIndex)) {
					curSheetIndex = areas.sheetIndex;
				}
				var row = minx + rowIndex - 1, col = miny + colIndex - 1;
				if (row <= maxx && col <= maxy) {
					var rest = ds.getCellValue(row, col, curSheetIndex);
					if (Ext.isNumber(Number(rest))) {
						return Number(rest);
					} else {
						return rest;
					}
				} else {
					throw "SS_ERROR_VALUE";
				}
			} else if (Ext.isArray(areas) && areaIndex <= areas.length) {
				var area;
				for (var i = 0, count = areas.length; i < count; i++) {
					area = areas[i];
					ds.fireEvent("project", ds, {
								x : x,
								y : y
							}, {
								ox : area.ox,
								oy : area.oy
							}, {
								ox : area.oex,
								oy : area.oey
							});
				}
				area = areas[areaIndex - 1];
				var minx = area.ox + x, miny = area.oy + y, maxx = area.oex + x, maxy = area.oey
						+ y;
				var row = minx + rowIndex - 1, col = miny + colIndex - 1;
				ds.checkSheetIndexValid(areas.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(areas.sheetIndex)) {
					curSheetIndex = areas.sheetIndex;
				}
				if (row <= maxx && col <= maxy) {
					var rest = ds.getCellValue(row, col, curSheetIndex);
					if (Ext.isNumber(Number(rest))) {
						return Number(rest);
					} else {
						return rest;
					}
				} else {
					throw "SS_ERROR_VALUE";
				}
			} else {
				throw "SS_ERROR_VALUE";
			}
		} else {
			throw "SS_ERROR_NA";
		}
	},
	trim : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (1 != len) {
			throw "SS_ERROR_NA";
		}
		var arr = [];
		for (var k = 0; k < len; k++) {
			var pos = arguments[k];
			if (Ext.isObject(pos)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : pos.ox,
							oy : pos.oy
						}, {
							ox : pos.oex,
							oy : pos.oey
						});
				var minx = pos.ox + x, miny = pos.oy + y, maxx = pos.oex + x, maxy = pos.oey
						+ y;
				ds.checkSheetIndexValid(pos.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(pos.sheetIndex)) {
					curSheetIndex = pos.sheetIndex;
				}
				var cell = ds.getCellValue(minx, miny, curSheetIndex);
				if (cell) {
					return cell.trim();
				}
			} else {
				if (Ext.isDefined(pos) && "" !== pos) {
					return pos.trim();
				}
			}
		}
	},
	sumproduct : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len) {
			throw "SS_ERROR_NA";
		}
		var varArrs = [];
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			var ary = [];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex) || "0";
						ary.push(Number(num));
					}
				}
				varArrs.push(ary);
			} else {
				if (posOffset.length != null) {
					var tmAry = Ext.ss.common.FunctionBoxHelper
							.restructureArray(posOffset, x, y, sheetIndex, ds);
					varArrs.push(tmAry);
				} else {
					var num = Number(posOffset);
					varArrs.push([num]);
				}
			}
		}
		if (varArrs.length > 0) {
			var basicLen = varArrs[0].length;
			for (var m = 0; m < varArrs.length; m++) {
				if (varArrs[m].length != basicLen) {
					varArrs = [];
					throw "SS_ERROR_VALUE";
				}
			}
			var total = 0;
			for (var t = 0; t < varArrs[0].length; t++) {
				var result = 1;
				for (var c = 0; c < varArrs.length; c++) {
					result *= varArrs[c][t];
				}
				total += result;
			}
			varArrs = [];
			return total;
		}
		varArrs = [];
		throw "SS_ERROR_VALUE";
	},
	gcd : function() {
		var gcd = 0, arr = [];
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len == 0) {
			throw "SS_ERROR_NA";
		}
		var res = 0;
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						num = Number(num);
						if (Ext.isNumber(num)) {
							if (num < 0) {
								throw "SS_ERROR_NUM";
							}
							arr.push(Math.floor(num));
						} else {
							throw "SS_ERROR_VALUE";
						}
					}
				}
			} else {
				var num = Number(posOffset);
				if (Ext.isNumber(num)) {
					if (num < 0) {
						throw "SS_ERROR_NUM";
					}
					arr.push(Math.floor(num));
				} else {
					throw "SS_ERROR_VALUE";
				}
			}
		}
		arr.sort(function(a, b) {
					return a - b;
				});
		res = arr[0];
		while (res > 0) {
			var counts = 0;
			for (i = 0; i < arr.length; i++) {
				if (arr[i] % res == 0) {
					counts++;
				}
			}
			if (counts == arr.length) {
				break;
			}
			res--;
		}
		return res;
	},
	lcm : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (1 <= len) {
			var arr = [];
			for (var k = 0; k < len; k++) {
				var pos = arguments[k];
				if (Ext.isObject(pos)) {
					ds.fireEvent("project", ds, {
								x : x,
								y : y
							}, {
								ox : pos.ox,
								oy : pos.oy
							}, {
								ox : pos.oex,
								oy : pos.oey
							});
					var minx = pos.ox + x, miny = pos.oy + y, maxx = pos.oex
							+ x, maxy = pos.oey + y;
					ds.checkSheetIndexValid(pos.sheetIndex);
					var curSheetIndex = sheetIndex;
					if (false != Ext.type(pos.sheetIndex)) {
						curSheetIndex = pos.sheetIndex;
					}
					for (var i = minx; i <= maxx; i++) {
						for (var j = miny; j <= maxy; j++) {
							pos = ds.getCellValue(i, j, curSheetIndex);
							if (pos === undefined
									|| Ext.ss.common.Mask.isEmptyStr(pos)) {
								throw "SS_ERROR_VALUE";
							}
							var num = Number(pos);
							if (Ext.isNumber(num)) {
								arr.push(Math.floor(pos));
							} else {
								throw "SS_ERROR_VALUE";
							}
						}
					}
				} else {
					if (pos === undefined || Ext.ss.common.Mask.isEmptyStr(pos)) {
						throw "SS_ERROR_VALUE";
					}
					var num = Number(pos);
					if (Ext.isNumber(num)) {
						arr.push(Math.floor(pos));
					} else {
						throw "SS_ERROR_VALUE";
					}
				}
			}
			var size = arr.length;
			if (size == 0) {
				throw "SS_ERROR_VALUE";
			}
			for (var i = 0; i < size; i++) {
				if (arr[i] < 0) {
					throw "SS_ERROR_NUM";
				}
			}
			if (1 == size) {
				return arr[0];
			} else if (1 < size) {
				var obj = {};
				for (var i = 0; i < size; i++) {
					if (arr[i] == 0 || arr[i] == "") {
						return 0;
					}
					obj[arr[i]] = true;
				}
				var fn = function(num) {
					var fcs = {};
					for (var f = 2; f <= num;) {
						if (0 == num % f) {
							fcs[f] = fcs[f] || 0;
							fcs[f]++;
							num = num / f;
						} else {
							f++;
						}
					}
					if (1 != num) {
						fcs[num] = fcs[num] || 0;
						fcs[num]++;
					}
					return fcs;
				};
				var all = {};
				for (var p in obj) {
					var num = Math.abs(p);
					if (num > 999999999999999) {
						throw "SS_ERROR_VALUE";
					}
					var parts = fn(num);
					for (var q in parts) {
						if (parts[q] > 999999999999999) {
							throw "SS_ERROR_VALUE";
						}
						if (!Ext.isDefined(all[q])) {
							all[q] = parts[q];
						} else if (all[q] < parts[q]) {
							all[q] = parts[q];
						}
					}
				}
				var ret = 1;
				for (var p in all) {
					ret *= Math.pow(p, all[p]);
				}
				return ret;
			}
		} else {
			throw "SS_ERROR_NA";
		}
	},
	mode : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (1 <= len) {
			var arr = [];
			for (var k = 0; k < len; k++) {
				var pos = arguments[k];
				if (Ext.isObject(pos)) {
					ds.fireEvent("project", ds, {
								x : x,
								y : y
							}, {
								ox : pos.ox,
								oy : pos.oy
							}, {
								ox : pos.oex,
								oy : pos.oey
							});
					var minx = pos.ox + x, miny = pos.oy + y, maxx = pos.oex
							+ x, maxy = pos.oey + y;
					ds.checkSheetIndexValid(pos.sheetIndex);
					var curSheetIndex = sheetIndex;
					if (false != Ext.type(pos.sheetIndex)) {
						curSheetIndex = pos.sheetIndex;
					}
					for (var i = minx; i <= maxx; i++) {
						for (var j = miny; j <= maxy; j++) {
							pos = ds.getCellValue(i, j, curSheetIndex);
							if (pos) {
								arr.push(pos);
							}
						}
					}
				} else {
					if (Ext.isDefined(pos) && "" !== pos) {
						arr.push(pos);
					}
				}
			}
			var size = arr.length;
			if (0 == size) {
				throw "SS_ERROR_VALUE";
			} else if (2 > size) {
				throw "SS_ERROR_NA";
			} else if (2 == size) {
				if (arr[0] == arr[1]) {
					return arr[0];
				}
				throw "SS_ERROR_NA";
			} else {
				var obj = {};
				for (var i = 0; i < size; i++) {
					obj[arr[i]] = obj[arr[i]] || 0;
					obj[arr[i]]++;
				}
				var max = -1, val;
				for (var p in obj) {
					if (max < obj[p]) {
						val = p;
						max = obj[p];
					}
				}
				if (1 >= max) {
					throw "SS_ERROR_NA";
				}
				return val;
			}
		} else {
			throw "SS_ERROR_NA";
		}
	},
	median : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len) {
			throw "SS_ERROR_NA";
		}
		var arr = [];
		for (var k = 0; k < len; k++) {
			var pos = arguments[k];
			if (Ext.isObject(pos)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : pos.ox,
							oy : pos.oy
						}, {
							ox : pos.oex,
							oy : pos.oey
						});
				var minx = pos.ox + x, miny = pos.oy + y, maxx = pos.oex + x, maxy = pos.oey
						+ y;
				ds.checkSheetIndexValid(pos.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(pos.sheetIndex)) {
					curSheetIndex = pos.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						pos = ds.getCellValue(i, j, curSheetIndex);
						pos = Number(pos);
						if (Ext.isNumber(pos)) {
							arr.push(pos);
						}
					}
				}
			} else {
				pos = Number(pos);
				if (Ext.isNumber(pos)) {
					arr.push(pos);
				}
			}
		}
		var size = arr.length;
		if (0 == size) {
			throw "SS_ERROR_VALUE";
		} else if (2 >= size) {
			var sum = 0;
			var haveNumber = false;
			for (var i = 0; i < size; i++) {
				sum += arr[i];
				if (!haveNumber) {
					haveNumber = Ext.ss.common.Mask.isNumber(arr[i]);
				}
			}
			var result = Math.round(sum / size * 100) / 100;
			if (haveNumber == false) {
				throw "SS_ERROR_VALUE";
			}
			return result;
		} else {
			arr.sort(function(a, b) {
						return a - b;
					});
			if (0 == size % 2) {
				var mid = Math.floor(size / 2);
				return (arr[mid] + arr[mid - 1]) / 2;
			} else {
				var mid = Math.floor(size / 2);
				return arr[mid];
			}
		}
	},
	countif : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (len < 2 && len > 3) {
			throw "SS_ERROR_NA";
		}
		var range = arguments[0], criteria = arguments[1], append = null, result = 0;
		criteria = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, criteria);
		if (3 == len) {
			append = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
					sheetIndex, arguments[2]);
			criteria = criteria + append;
		}
		range = Ext.ss.common.FunctionBoxHelper.getRangeVal(ds, x, y,
				sheetIndex, range);
		var compareFlag = /^\s*[!<>=]/gi.test(criteria.toString());
		for (var i = 0; i < range.length; i++) {
			var tempValue = range[i];
			var flag = false;
			if (compareFlag) {
				if (Ext.isNumber(Number(tempValue))) {
					flag = eval(tempValue + criteria);
				} else {
					flag = eval("\"" + tempValue + "\"" + criteria);
				}
			} else {
				if (tempValue == null) {
					tempValue = "";
				}
				if (1 == Ext.ss.common.Mask.shExpMatch_KMP(
						tempValue.toString(), criteria.toString())) {
					flag = true;
				}
			}
			if (flag) {
				result++;
			}
		}
		return result;
	},
	columns : function() {
		var len = arguments.length;
		var ds, x, y, index, sheetIndex;
		if (5 == len) {
			ds = arguments[len - 1];
			y = arguments[len - 2];
			x = arguments[len - 3];
			sheetIndex = arguments[len - 4];
			var posOffset = arguments[0];
			if (Ext.isObject(posOffset)) {
				var miny = parseInt(posOffset.oy), maxy = parseInt(posOffset.oey);
				return maxy - miny + 1;
			}
		}
		throw "SS_ERROR_NA";
	},
	column : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex, index;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		if (5 == len) {
			var posOffset = arguments[0];
			if (Ext.isObject(posOffset)) {
				var i = parseInt(posOffset.ox) + x, j = parseInt(posOffset.oy)
						+ y;
				return j;
			}
		} else if (4 == len) {
			var i = x, j = y;
			return j;
		}
		throw "SS_ERROR_NA";
	},
	rows : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex, index;
		if (5 == len) {
			ds = arguments[len - 1];
			y = arguments[len - 2];
			x = arguments[len - 3];
			sheetIndex = arguments[len - 4];
			var posOffset = arguments[0];
			if (Ext.isObject(posOffset)) {
				var minx = parseInt(posOffset.ox), maxx = parseInt(posOffset.oex);
				return maxx - minx + 1;
			}
		}
		throw "#PARAM_ERROR";
	},
	row : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex, index;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		if (5 == len) {
			var posOffset = arguments[0];
			if (Ext.isObject(posOffset)) {
				var i = parseInt(posOffset.ox) + x, j = parseInt(posOffset.oy)
						+ y;
				return i;
			}
		} else if (4 == len) {
			var i = x, j = y;
			return i;
		}
		throw "SS_ERROR_NA";
	},
	choose : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex, index;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len) {
			throw "SS_ERROR_NA";
		}
		var rows = ds.data.rows;
		var indexPos = arguments[0];
		indexPos = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, indexPos);
		if (0 < indexPos && indexPos < len) {
			var result = arguments[indexPos];
			result = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
					sheetIndex, result);
			return result;
		} else {
			throw "SS_ERROR_VALUE";
		}
	},
	trunc : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len || len > 2) {
			throw "SS_ERROR_NA";
		}
		var digital = arguments[1];
		digital = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, digital);
		var posOffset = arguments[0];
		posOffset = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, posOffset);
		if (posOffset === undefined) {
			return 0;
		}
		if (!digital || digital == undefined) {
			digital = 0;
			posOffset += "";
			var nDx = posOffset.indexOf(".");
			if (nDx == 0) {
				return 0;
			} else if (nDx > 0) {
				posOffset = posOffset.substring(0, nDx);
			}
		}
		var num = Number(posOffset);
		digital = Number(digital);
		if (!Ext.isNumber(digital) || !Ext.isNumber(num)
				|| Ext.ss.common.Mask.isEmptyStr(posOffset)) {
			throw "SS_ERROR_VALUE";
		} else if (num == 0 || digital < 0) {
			return 0;
		}
		if (digital > 0) {
			var maxIdx = 10;
			if (num < 0) {
				maxIdx = 11;
			}
			num += "";
			var base = Math.floor(digital);
			var idx = num.indexOf(".");
			if (idx > -1) {
				var leg = num.length;
				if (idx >= maxIdx) {
					num = Math.round(num);
				} else {
					if (leg - idx - 1 > base) {
						var a = "1";
						for (var k = 0; k < base - 1; k++) {
							a += "0";
						}
						a = Number(a);
						num = num.substring(0, idx + base + 1);
						if (maxIdx - 1 == idx) {
							num = parseFloat(Math.round(num * a)) / a;
						}
					} else {
						var a = "1";
						for (var k = 0; k < maxIdx - idx; k++) {
							a += "0";
						}
						a = Number(a);
						num = num.substring(0, maxIdx + 2);
						num = parseFloat(Math.round(num * a)) / a;
					}
				}
			}
		}
		return num;
	},
	product : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len) {
			throw "SS_ERROR_NA";
		}
		var res = 1;
		var allArgsNaN = true;
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						if (len == 1 && (num === undefined || num === "")) {
							return 0;
						}
						num = Number(num);
						if (Ext.isNumber(num)) {
							res *= num;
							allArgsNaN = false;
						}
					}
				}
			} else {
				var num = Number(posOffset);
				if (Ext.isNumber(num)) {
					res *= num;
					allArgsNaN = false;
				}
			}
		}
		if (allArgsNaN) {
			res = 0;
		}
		return res;
	},
	not : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (1 != len) {
			throw "SS_ERROR_NA";
		}
		var num = arguments[0];
		if (Ext.isObject(num)) {
			num = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
					sheetIndex, num);
			if (num == undefined || num == "true" || num == "false"
					|| !isNaN(num)) {
				return !num || "false" == num || 0 == num || "FALSE" == num
						? "TRUE"
						: "FALSE";
			} else {
				throw "SS_ERROR_VALUE";
			}
		}
		if (typeof num == "string" || num == "null" || num == undefined
				|| Ext.ss.common.Mask.isEmptyStr(num)) {
			throw "SS_ERROR_VALUE";
		}
		return !num || "false" == num || 0 == num || "FALSE" == num
				? "TRUE"
				: "FALSE";
	},
	or : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len == 0) {
			throw "SS_ERROR_NA";
		}
		var posOffset = arguments[0];
		if (Ext.isObject(posOffset)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : posOffset.ox,
						oy : posOffset.oy
					}, {
						ox : posOffset.oex,
						oy : posOffset.oey
					});
			var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
					+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
					+ y;
			ds.checkSheetIndexValid(posOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(posOffset.sheetIndex)) {
				curSheetIndex = posOffset.sheetIndex;
			}
			var tmAry = [];
			for (var i = minx; i <= maxx; i++) {
				for (var j = miny; j <= maxy; j++) {
					var num = ds.getCellValue(i, j, curSheetIndex);
					if (("FALSE" != num && "false" != num && num != "0" && !isNaN(num))
							&& num != undefined
							|| num == "true"
							|| num == "TRUE") {
						return "TRUE";
					}
					tmAry.push(num);
				}
			}
			for (var r = 0; r < tmAry.length; r++) {
				var itm = tmAry[r];
				if (itm == "false" || itm == "FALSE" || itm == "0") {
					return "FALSE";
				}
			}
			throw "SS_ERROR_VALUE";
		} else {
			var ary = [];
			for (var k = 0; k < len; k++) {
				var num = arguments[k];
				var type = typeof num;
				if (num == "0"
						&& type != "number"
						&& type != "boolean"
						|| num == undefined
						|| Ext.ss.common.Mask.isEmptyStr(num)
						|| (num != true && num != false && "FALSE" != num
								&& "false" != num && "TRUE" != num && "true" != num)
						&& type != "number") {
					throw "SS_ERROR_VALUE";
				}
				ary.push(num);
			}
			for (var p = 0; p < ary.length; p++) {
				var num = ary[p];
				if (false != null && "FALSE" != num && "false" != num
						&& "0" != num && 0 != Number(num)) {
					return "TRUE";
				}
			}
		}
		return "FALSE";
	},
	and : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len == 0) {
			throw "SS_ERROR_NA";
		}
		var allArgsAreNotNumber = null;
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						if (null == num || num === undefined || num === "") {
							if (allArgsAreNotNumber == null) {
								allArgsAreNotNumber = true;
							}
						} else if (0 == num || "0" == num || "false" == num
								|| "FALSE" == num) {
							return "FALSE";
						} else if (Ext.ss.common.Mask.isEmptyStr(num)) {
							if (allArgsAreNotNumber == null) {
								allArgsAreNotNumber = true;
							}
						} else if (Ext.isNumber(Number(num))) {
							allArgsAreNotNumber = false;
						} else {
							if (allArgsAreNotNumber == null) {
								allArgsAreNotNumber = true;
							}
						}
					}
				}
			} else {
				var num = posOffset;
				if (null == num || num === undefined || num === "") {
					if (allArgsAreNotNumber == null) {
						allArgsAreNotNumber = true;
					}
				} else if (0 == num || "0" == num || "false" == num
						|| "FALSE" == num) {
					return "FALSE";
				} else if (Ext.ss.common.Mask.isEmptyStr(num)) {
					if (allArgsAreNotNumber == null) {
						allArgsAreNotNumber = true;
					}
				} else if (Ext.isNumber(Number(num))) {
					allArgsAreNotNumber = false;
				} else {
					if (allArgsAreNotNumber == null) {
						allArgsAreNotNumber = true;
					}
				}
			}
		}
		if (allArgsAreNotNumber && allArgsAreNotNumber == true) {
			throw "SS_ERROR_VALUE";
		}
		return "TRUE";
	},
	power : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex, powNum, posOffset;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len != 2) {
			throw "SS_ERROR_NA";
		}
		powNum = arguments[1];
		if (powNum === undefined || powNum === "") {
			throw "SS_ERROR_VALUE";
		}
		powNum = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, powNum);
		var num = arguments[0];
		num = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, num);
		return Math.pow(num, powNum);
	},
	left : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex, leftPos = 1, posOffset;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len || len > 2) {
			throw "SS_ERROR_NA";
		}
		if (2 == len) {
			leftPos = arguments[1];
			posOffset = arguments[0];
			if (Ext.isObject(leftPos)) {
				leftPos = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
						sheetIndex, leftPos);
				leftPos = leftPos.toString();
				if (leftPos.toUpperCase() == "TRUE") {
					leftPos = 1;
				} else if (leftPos == "" || leftPos.toUpperCase() == "FALSE") {
					return "";
				}
				if (!/^[0-9]+$/i.test(leftPos)) {
					throw "SS_ERROR_VALUE";
				}
				leftPos = Ext.ss.FunctionBox.num(leftPos, 0);
			}
		} else {
			posOffset = arguments[0];
		}
		leftPos = leftPos.toString();
		if (leftPos.toUpperCase() == "TRUE") {
			leftPos = 1;
		} else if (leftPos.toUpperCase() == "FALSE") {
			return "";
		}
		if (!/^[0-9]+$/i.test(leftPos)) {
			throw "SS_ERROR_VALUE";
		}
		if (Ext.isString(posOffset) || Ext.isNumber(posOffset)
				|| Ext.isBoolean(posOffset)) {
			if (Ext.isBoolean(posOffset)) {
				posOffset = posOffset.toString().toUpperCase();
			} else {
				posOffset = posOffset.toString();
			}
			return posOffset.slice(0, leftPos);
		} else {
			var res = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
					sheetIndex, posOffset);
			if (res.toUpperCase() == "FALSE") {
				res = res.toUpperCase();
			}
			res = res.toString();
			res = res.slice(0, leftPos);
			return res;
		}
	},
	right : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex, rightPos = 1, posOffset;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len || len > 2) {
			throw "SS_ERROR_NA";
		}
		if (2 == len) {
			rightPos = arguments[1];
			posOffset = arguments[0];
			if (Ext.isObject(rightPos)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, rightPos, {
							ox : rightPos.oex,
							oy : rightPos.oey
						});
				var abPos = {
					sheetIndex : rightPos.sheetIndex,
					x : x + rightPos.ox,
					y : y + rightPos.oy
				};
				ds.checkSheetIndexValid(abPos.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(abPos.sheetIndex)) {
					curSheetIndex = abPos.sheetIndex;
				}
				var i = parseInt(abPos.x), j = parseInt(abPos.y);
				rightPos = ds.getCellValue(i, j, curSheetIndex) || "";
				rightPos = rightPos.toString();
				if (rightPos.toUpperCase() == "TRUE") {
					rightPos = 1;
				} else if (rightPos == "" || rightPos.toUpperCase() == "FALSE") {
					return "";
				}
				if (!/^[0-9]+$/i.test(rightPos)) {
					throw "SS_ERROR_VALUE";
				}
				rightPos = Ext.ss.FunctionBox.num(rightPos, 0);
			}
		} else {
			posOffset = arguments[0];
		}
		rightPos = rightPos.toString();
		if (rightPos.toUpperCase() == "TRUE") {
			rightPos = 1;
		} else if (rightPos.toUpperCase() == "FALSE") {
			return "";
		}
		if (!/^[0-9]+$/i.test(rightPos)) {
			throw "SS_ERROR_VALUE";
		}
		if (Ext.isString(posOffset) || Ext.isNumber(posOffset)
				|| Ext.isBoolean(posOffset)) {
			if (Ext.isBoolean(posOffset)) {
				posOffset = posOffset.toString().toUpperCase();
			} else {
				posOffset = posOffset.toString();
			}
			var strLen = posOffset.length;
			return posOffset.slice(strLen - rightPos, strLen);
		} else {
			var res = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
					sheetIndex, posOffset);
			res = res || "";
			res = res.toString();
			if (res.toUpperCase() == "FALSE") {
				res = res.toUpperCase();
			}
			var strLen = res.length;
			res = res.slice(strLen - rightPos, strLen);
			return res;
		}
	},
	num : function(v, dv) {
		if (Ext.isNumber(Number(v))) {
			return Number(v);
		}
		return dv;
	},
	sum : function() {
		var maxDigNum = 0;
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len) {
			throw "SS_ERROR_NA";
		}
		var res = 0;
		var argsIncludeNaN = false;
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						num = Number(num);
						if (Ext.isNumber(num)) {
							var dn = Ext.ss.common.Helper.getDigitalLen(num);
							if (dn > maxDigNum) {
								maxDigNum = dn;
							}
							res += num;
						}
					}
				}
			} else {
				if (posOffset.length != null) {
					var tmAry = Ext.ss.common.FunctionBoxHelper
							.restructureArray(posOffset, x, y, sheetIndex, ds);
					for (var z = 0; z < tmAry.length; z++) {
						var dn = Ext.ss.common.Helper.getDigitalLen(tmAry[z]
								|| "0");
						if (dn > maxDigNum) {
							maxDigNum = dn;
						}
						res += Number(tmAry[z] || "0");
					}
				} else {
					var num = Number(posOffset);
					if (Ext.isNumber(num)) {
						var dn = Ext.ss.common.Helper.getDigitalLen(num);
						if (dn > maxDigNum) {
							maxDigNum = dn;
						}
						res += num;
					} else {
						argsIncludeNaN = true;
					}
				}
			}
		}
		if (argsIncludeNaN) {
			throw "SS_ERROR_VALUE";
		}
		var dn = Ext.ss.common.Helper.getDigitalLen(res);
		if (dn > maxDigNum) {
			res = Ext.util.Format.round(res, maxDigNum);
		}
		return res;
	},
	sumsq : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len == 0) {
			throw "SS_ERROR_NA";
		}
		var res = 0;
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						num = Ext.ss.FunctionBox.num(num, 0);
						res += Math.pow(num, 2);
					}
				}
			} else {
				var num = Number(posOffset);
				if (Ext.isNumber(num)) {
					res += Math.pow(num);
				}
			}
		}
		return res;
	},
	sumxmy2 : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 != len) {
			throw "SS_ERROR_NA";
		}
		var xrng = arguments[0], yrng = arguments[1];
		return Ext.ss.common.FunctionBoxHelper.sumHelper(x, y, sheetIndex, ds,
				xrng, yrng, "sumxmy2");
	},
	sumx2py2 : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 != len) {
			throw "SS_ERROR_NA";
		}
		var xrng = arguments[0], yrng = arguments[1];
		return Ext.ss.common.FunctionBoxHelper.sumHelper(x, y, sheetIndex, ds,
				xrng, yrng, "sumx2py2");
	},
	sumx2my2 : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 != len) {
			throw "SS_ERROR_NA";
		}
		var xrng = arguments[0], yrng = arguments[1];
		return Ext.ss.common.FunctionBoxHelper.sumHelper(x, y, sheetIndex, ds,
				xrng, yrng, "sumx2my2");
	},
	sumif : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len < 2 || len > 5) {
			throw "SS_ERROR_NA";
		}
		var checkOffset = arguments[0], conditionOffset = arguments[1];
		var sumOffset = Ext.apply({}, checkOffset);
		if (len == 3) {
			sumOffset = arguments[2];
		}
		var minOffset = {
			sheetIndex : checkOffset.sheetIndex,
			ox : checkOffset.ox,
			oy : checkOffset.oy
		};
		var maxOffset = {
			sheetIndex : checkOffset.sheetIndex,
			ox : checkOffset.oex,
			oy : checkOffset.oey
		};
		var sumMinOffset = {
			sheetIndex : sumOffset.sheetIndex,
			ox : sumOffset.ox,
			oy : sumOffset.oy
		};
		var sumMaxOffset = {
			sheetIndex : sumOffset.sheetIndex,
			ox : sumOffset.oex,
			oy : sumOffset.oey
		};
		var minx = minOffset.ox + x, miny = minOffset.oy + y, maxx = maxOffset.ox
				+ x, maxy = maxOffset.oy + y;
		var sumMinx = sumMinOffset.ox + x, sumMiny = sumMinOffset.oy + y, sumMaxx = sumMaxOffset.ox
				+ x, sumMaxy = sumMaxOffset.oy + y;
		var rows = ds.data.rows;
		var r = 0, cond, compareFlag = false;
		if (Ext.isObject(conditionOffset)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, conditionOffset, conditionOffset);
			var cx = conditionOffset.ox + x, cy = conditionOffset.oy + y;
			ds.checkSheetIndexValid(conditionOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(conditionOffset.sheetIndex)) {
				curSheetIndex = conditionOffset.sheetIndex;
			}
			var cond = ds.getCellValue(cx, cy, curSheetIndex);
			if (Ext.isString(cond)) {
				cond = Ext.util.Format.htmlDecode(cond);
			}
		} else {
			cond = conditionOffset;
		}
		compareFlag = /^\s*[!<>=]/gi.test(cond);
		ds.checkSheetIndexValid(minOffset.sheetIndex);
		var curSheetIndex = sheetIndex;
		if (false != Ext.type(minOffset.sheetIndex)) {
			curSheetIndex = minOffset.sheetIndex;
		}
		ds.checkSheetIndexValid(sumMinOffset.sheetIndex);
		var sumSheetIndex = sheetIndex;
		if (false != Ext.type(sumMinOffset.sheetIndex)) {
			sumSheetIndex = sumMinOffset.sheetIndex;
		}
		var xoffset = sumMinx - minx, yoffset = sumMiny - miny;
		for (var i = minx; i <= maxx; i++) {
			for (var j = miny; j <= maxy; j++) {
				var data = ds.getCellValue(i, j, curSheetIndex);
				var flag = false;
				if (compareFlag) {
					if (Ext.isNumber(Number(data))) {
						flag = eval(data + cond);
					} else {
						flag = eval("\"" + data + "\"" + cond);
					}
				} else if (1 == Ext.ss.common.Mask.shExpMatch_KMP(data
								.toString(), cond.toString())) {
					flag = true;
				}
				if (flag) {
					var sumData = ds.getCellValue(xoffset + i, yoffset + j,
							sumSheetIndex);
					sumData = Number(sumData);
					if (Ext.isNumber(sumData)) {
						r += sumData;
					}
				}
			}
		}
		ds.fireEvent("project", ds, {
					x : x,
					y : y
				}, minOffset, maxOffset);
		ds.fireEvent("project", ds, {
					x : x,
					y : y
				}, sumMinOffset, sumMaxOffset);
		return r;
	},
	sumifs : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len < 3) {
			throw "SS_ERROR_NA";
		}
		var sumOffset = arguments[0];
		var sumMinOffset = {
			sheetIndex : sumOffset.sheetIndex,
			ox : sumOffset.ox,
			oy : sumOffset.oy
		};
		var sumMaxOffset = {
			sheetIndex : sumOffset.sheetIndex,
			ox : sumOffset.oex,
			oy : sumOffset.oey
		};
		var sumMinx = sumMinOffset.ox + x, sumMiny = sumMinOffset.oy + y, sumMaxx = sumMaxOffset.ox
				+ x, sumMaxy = sumMaxOffset.oy + y;
		var i = 1;
		var checkOffset = [];
		var conditionOffset = [];
		while (i < len) {
			checkOffset[checkOffset.length] = arguments[i];
			i = i + 1;
			conditionOffset[conditionOffset.length] = arguments[i];
			i = i + 1;
		}
		var minx = [], miny = [], maxx = [], maxy = [];
		var cond = [], compareFlag = [];
		var curSheetIndex = [];
		for (var i = 0; i < checkOffset.length; i++) {
			var minOffset = {
				sheetIndex : checkOffset[i].sheetIndex,
				ox : checkOffset[i].ox,
				oy : checkOffset[i].oy
			};
			var maxOffset = {
				sheetIndex : checkOffset[i].sheetIndex,
				ox : checkOffset[i].oex,
				oy : checkOffset[i].oey
			};
			minx[minx.length] = minOffset.ox + x, miny[miny.length] = minOffset.oy
					+ y, maxx[maxx.length] = maxOffset.ox + x, maxy[maxy.length] = maxOffset.oy
					+ y;
			var rows = ds.data.rows;
			if (Ext.isObject(conditionOffset[i])) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, conditionOffset, conditionOffset);
				var cx = conditionOffset.ox + x, cy = conditionOffset.oy + y;
				ds.checkSheetIndexValid(conditionOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(conditionOffset.sheetIndex)) {
					curSheetIndex = conditionOffset.sheetIndex;
				}
				var condi = ds.getCellValue(cx, cy, curSheetIndex);
				if (Ext.isString(condi)) {
					condi = Ext.util.Format.htmlDecode(condi);
				}
				cond[cond.length] = condi;
			} else {
				cond[cond.length] = conditionOffset[i];
			}
			compareFlag[compareFlag.length] = /^\s*[<>=]/gi.test(cond);
			ds.checkSheetIndexValid(minOffset.sheetIndex);
			if (false != Ext.type(minOffset.sheetIndex)) {
				curSheetIndex[curSheetIndex.length] = minOffset.sheetIndex;
			} else {
				curSheetIndex[curSheetIndex.length] = sheetIndex;
			}
		}
		ds.checkSheetIndexValid(sumMinOffset.sheetIndex);
		var sumSheetIndex = sheetIndex;
		if (false != Ext.type(sumMinOffset.sheetIndex)) {
			sumSheetIndex = sumMinOffset.sheetIndex;
		}
		var r = 0;
		for (var i = sumMinx; i <= sumMaxx; i++) {
			for (var j = sumMiny; j <= sumMaxy; j++) {
				var flag = true;
				for (var k = 0; k < checkOffset.length; k++) {
					var data = ds.getCellValue(minx[k] + (i - sumMinx), miny[k]
									+ (j - sumMiny), curSheetIndex[k]);
					var condflag = false;
					if (compareFlag[k]) {
						if (Ext.isNumber(Number(data))) {
							condflag = eval(data + cond[k]);
						} else {
							condflag = eval("\"" + data + "\"" + cond[k]);
						}
					} else if (1 == Ext.ss.common.Mask.shExpMatch_KMP(data
									.toString(), cond[k].toString())) {
						condflag = true;
					}
					flag = flag && condflag;
				}
				if (flag) {
					var sumData = ds.getCellValue(i, j, sumSheetIndex);
					sumData = Number(sumData);
					if (Ext.isNumber(sumData)) {
						r += sumData;
					}
				}
			}
		}
		return r;
	},
	lookup : function(conditionOffset, checkOffset, sumOffset, sheetIndex, x,
			y, ds) {
		if (6 == arguments.length) {
			ds = y;
			y = x;
			x = sheetIndex;
			sheetIndex = sumOffset;
			sumOffset = Ext.apply({}, checkOffset);
		}
		var cond;
		if (Ext.isObject(conditionOffset)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, conditionOffset, conditionOffset);
			var cx = conditionOffset.ox + x, cy = conditionOffset.oy + y;
			ds.checkSheetIndexValid(conditionOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(conditionOffset.sheetIndex)) {
				curSheetIndex = conditionOffset.sheetIndex;
			}
			cond = ds.getCellValue(cx, cy, curSheetIndex);
		} else {
			cond = conditionOffset;
		}
		if (Ext.isObject(checkOffset)) {
			var minOffset = {
				sheetIndex : checkOffset.sheetIndex,
				ox : checkOffset.ox,
				oy : checkOffset.oy
			};
			var maxOffset = {
				sheetIndex : checkOffset.sheetIndex,
				ox : checkOffset.oex,
				oy : checkOffset.oey
			};
			var minx = minOffset.ox + x, miny = minOffset.oy + y, maxx = maxOffset.ox
					+ x, maxy = maxOffset.oy + y;
			var sumMinOffset = {
				sheetIndex : sumOffset.sheetIndex,
				ox : sumOffset.ox,
				oy : sumOffset.oy
			};
			var sumMaxOffset = {
				sheetIndex : sumOffset.sheetIndex,
				ox : sumOffset.oex,
				oy : sumOffset.oey
			};
			var sumMinx = sumMinOffset.ox + x, sumMiny = sumMinOffset.oy + y, sumMaxx = sumMaxOffset.ox
					+ x, sumMaxy = sumMaxOffset.oy + y;
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, minOffset, maxOffset);
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, sumMinOffset, sumMaxOffset);
			if (!Ext.isDefined(cond)) {
				throw "SS_ERROR_NA";
			}
			ds.checkSheetIndexValid(minOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(minOffset.sheetIndex)) {
				curSheetIndex = minOffset.sheetIndex;
			}
			ds.checkSheetIndexValid(sumMinOffset.sheetIndex);
			var sumSheetIndex = sheetIndex;
			if (false != Ext.type(sumMinOffset.sheetIndex)) {
				sumSheetIndex = sumMinOffset.sheetIndex;
			}
			for (var i = minx; i <= maxx; i++) {
				for (var j = miny; j <= maxy; j++) {
					var data = ds.getCellValue(i, j, curSheetIndex);
					if (1 == Ext.ss.common.Mask.shExpMatch_KMP(data.toString(),
							cond.toString())) {
						var sum = ds.getCellValue(sumMinx + i - minx, sumMiny
										+ j - miny, sumSheetIndex);
						return sum;
					}
				}
			}
			throw "SS_ERROR_NA";
		} else if (Ext.isArray(checkOffset)) {
			if (!Ext.isDefined(cond)) {
				throw "SS_ERROR_NA";
			}
			for (var i = 0, len = checkOffset.length; i < len; i++) {
				var it = checkOffset[i];
				if (it == cond
						|| 1 == Ext.ss.common.Mask.shExpMatch_KMP(
								it.toString(), cond.toString())) {
					return it;
				}
			}
			throw "SS_ERROR_NA";
		}
	},
	iferror : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 != len) {
			throw "SS_ERROR_NA";
		}
		var val = arguments[0], txt = arguments[1];
		if (Ext.isObject(txt)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : txt.ox,
						oy : txt.oy
					}, {
						ox : txt.oex,
						oy : txt.oey
					});
			var minx = parseInt(txt.ox) + x, maxx = parseInt(txt.oex) + x, miny = parseInt(txt.oy)
					+ y, maxy = parseInt(txt.oey) + y;
			ds.checkSheetIndexValid(txt.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(txt.sheetIndex)) {
				curSheetIndex = txt.sheetIndex;
			}
			if (minx == maxx && miny == maxy) {
				txt = ds.getCellValue(minx, miny, curSheetIndex);
			} else {
				throw "SS_ERROR_VALUE";
			}
		}
		if (Ext.isObject(val)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : val.ox,
						oy : val.oy
					}, {
						ox : val.oex,
						oy : val.oey
					});
			var minx = parseInt(val.ox) + x, maxx = parseInt(val.oex) + x, miny = parseInt(val.oy)
					+ y, maxy = parseInt(val.oey) + y;
			ds.checkSheetIndexValid(val.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(val.sheetIndex)) {
				curSheetIndex = val.sheetIndex;
			}
			if (minx == maxx && miny == maxy) {
				try {
					val = ds.getCellValue(minx, miny, curSheetIndex);
					if (val == "Infinity" || val == "NaN" || val == "#NUM!"
							|| val == "#VALUE!" || val == "#ERROR!"
							|| val == "#REF!") {
						return txt;
					}
				} catch (e) {
					return txt;
				}
			} else {
				return txt;
			}
		}
		if (val === undefined || val === "") {
			return 0;
		}
		if (val == "Infinity" || val == "NaN") {
			return txt;
		}
		return val;
	},
	pmt : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 > len || len > 5) {
			throw "SS_ERROR_NA";
		}
		var rate = arguments[0], nper = arguments[1], pv = arguments[2], fv = 0, type = 0;
		if (4 == len) {
			fv = arguments[3];
		}
		if (5 == len) {
			fv = arguments[3];
			type = arguments[4];
		}
		rate = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, rate);
		nper = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, nper);
		pv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pv);
		fv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, fv);
		type = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, type);
		rate = parseFloat(rate);
		nper = parseFloat(nper);
		pv = parseFloat(pv);
		type = parseFloat(type);
		fv = parseFloat(fv);
		var pmt1 = Math.round(pv * (rate * Math.pow(1 + rate, nper - type))
				/ (Math.pow(1 + rate, nper) - 1) * 100)
				/ 100;
		var ir = 0;
		if (1 == type) {
			for (var i = 1; i <= nper; i++) {
				ir = ir + Math.pow(rate + 1, i);
			}
		} else {
			ir = 1;
			for (var i = 1; i < nper; i++) {
				ir = ir + Math.pow(rate + 1, i);
			}
		}
		var pmt2 = Math.round(fv / ir * 100) / 100;
		return -(pmt1 + pmt2);
	},
	rate : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 > len || len > 6) {
			throw "SS_ERROR_NA";
		}
		var nper = arguments[0], pmt = arguments[1], pv = arguments[2], fv = 0, type = 0, guess = 0;
		if (4 == len) {
			fv = arguments[3];
		}
		if (5 == len) {
			fv = arguments[3];
			type = arguments[4];
		}
		if (6 == len) {
			fv = arguments[3];
			type = arguments[4];
			guess = arguments[5];
		}
		nper = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, nper);
		pmt = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pmt);
		pv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pv);
		fv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, fv);
		type = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, type);
		guess = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, guess);
		nper = parseFloat(nper) ? parseFloat(nper) : 0;
		pmt = parseFloat(pmt) ? parseFloat(pmt) : 0;
		pv = parseFloat(pv) ? parseFloat(pv) : 0;
		type = parseFloat(type) ? parseFloat(type) : 0;
		fv = parseFloat(fv) ? parseFloat(fv) : 0;
		guess = parseFloat(guess) ? parseFloat(guess) : 0;
		var maxrate = 0;
		var minrate = Math.abs((pmt * nper + fv + pv) / pv / nper);
		if (Math.abs(minrate) > 1) {
			return "#NUM!";
		}
		if (Math.abs(pmt * nper + fv) < Math.abs(pv)) {
			maxrate = -Math.abs(minrate);
			minrate = -1;
		} else {
			maxrate = 1;
		}
		var rate = maxrate;
		for (var irup = 1; irup < 50; irup++) {
			var ir = 0;
			for (var i = 1; i <= nper; i++) {
				ir = ir + pmt * Math.pow(rate + 1, i);
			}
			var trypv = -(ir / Math.pow(rate + 1, nper + 1 - type) + fv
					/ Math.pow(rate + 1, nper));
			if (pmt > 0) {
				if (trypv < pv) {
					minrate = rate;
					rate = (rate + maxrate) / 2;
					if (Math.abs(rate) == 1) {
						return "#NUM!";
					}
				} else if (trypv > pv) {
					maxrate = rate;
					rate = (rate + minrate) / 2;
					if (Math.abs(rate) == 1) {
						return "#NUM!";
					}
				} else if (trypv = pv) {
					return Math.round(rate * 10000000) / 10000000;
				}
			} else {
				if (trypv > pv) {
					minrate = rate;
					rate = (rate + maxrate) / 2;
					if (Math.abs(rate) == 1) {
						return "#NUM!";
					}
				} else if (trypv < pv) {
					maxrate = rate;
					rate = (rate + minrate) / 2;
					if (Math.abs(rate) == 1) {
						return "#NUM!";
					}
				} else if (trypv = pv) {
					return Math.round(rate * 10000000) / 10000000;
				}
			}
			irup++;
		}
		return Math.round(rate * 10000000) / 10000000;
	},
	irr : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len > 2) {
			throw "SS_ERROR_NA";
		}
		var values = arguments[0];
		var guess = 0;
		var nums = [];
		if (2 == len) {
			guess = arguments[1];
		}
		if (Ext.isObject(values)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : values.ox,
						oy : values.oy
					}, {
						ox : values.oex,
						oy : values.oey
					});
			var minx = parseInt(values.ox) + x, maxx = parseInt(values.oex) + x, miny = parseInt(values.oy)
					+ y, maxy = parseInt(values.oey) + y;
			ds.checkSheetIndexValid(values.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(values.sheetIndex)) {
				curSheetIndex = values.sheetIndex;
			}
			for (var i = minx; i <= maxx; i++) {
				for (var j = miny; j <= maxy; j++) {
					var num = ds.getCellValue(i, j, curSheetIndex);
					num = Number(num);
					if (Ext.isNumber(num)) {
						nums[nums.length] = num;
					}
				}
			}
		} else if (Ext.isString(values)) {
			if (values.charAt(0) == "{"
					&& values.charAt(values.length - 1) == "}") {
				var str = values.slice(1, values.length - 1);
				nums = str.split(",");
			} else {
				throw "SS_ERROR_NA";
			}
		}
		guess = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, guess);
		var fsum = 0, zsum = 0;
		for (var ii = 0; ii < nums.length; ii++) {
			nums[ii] = parseFloat(nums[ii]);
			if (nums[ii] < 0) {
				fsum = fsum + nums[ii];
			} else {
				zsum = zsum + nums[ii];
			}
		}
		guess = parseFloat(guess) ? parseFloat(guess) : 0;
		var irr = 0;
		if (guess == 0) {
			guess = Math.abs((fsum + zsum) / Math.abs(fsum));
		} else {
			guess = Math.abs(guess);
		}
		var lawirr = 0, maxirr = 0;
		if (Math.abs(fsum) > Math.abs(zsum)) {
			lawirr = -guess;
		} else {
			maxirr = guess;
		}
		var irr = maxirr;
		for (var irup = 1; irup < 50; irup++) {
			var ir = 0;
			for (var i = 0; i < nums.length; i++) {
				ir = ir + nums[i] / Math.pow(irr + 1, i);
			}
			if (ir == 0) {
				return Math.round(irr * 10000000) / 10000000;
			} else if (ir > 0) {
				lawirr = irr;
				irr = (maxirr + irr) / 2;
			} else {
				maxirr = irr;
				irr = (irr + lawirr) / 2;
			}
			irup++;
		}
		return Math.round(irr * 10000000) / 10000000;
	},
	nper : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 > len || len > 5) {
			throw "SS_ERROR_NA";
		}
		var rate = arguments[0], pmt = arguments[1], pv = arguments[2], fv = 0, type = 0;
		if (4 == len) {
			fv = arguments[3];
			if (fv === undefined || Ext.ss.common.Mask.isEmptyStr(fv)) {
				fv = 0;
			}
		} else {
			fv = arguments[3];
			if (fv === undefined || Ext.ss.common.Mask.isEmptyStr(fv)) {
				fv = 0;
			}
			type = arguments[4];
			if (type === undefined || Ext.ss.common.Mask.isEmptyStr(type)) {
				type = 0;
			}
		}
		rate = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, rate);
		pmt = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pmt);
		pv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pv);
		fv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, fv);
		type = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, type);
		rate = parseFloat(rate) ? parseFloat(rate) : 0;
		pmt = parseFloat(pmt) ? parseFloat(pmt) : 0;
		pv = parseFloat(pv) ? parseFloat(pv) : 0;
		fv = parseFloat(fv) ? parseFloat(fv) : 0;
		type = parseFloat(type) ? parseFloat(type) : 0;
		var minnper = (-fv - pv) / pmt;
		minnper = (-fv / Math.pow(1 + rate, minnper - type) - pv) / pmt;
		var maxnper = (-fv - pv * Math.pow(1 + rate, minnper - type)) / pmt;
		var nper = maxnper;
		for (var irup = 1; irup < 50; irup++) {
			var calpmt = (-fv / Math.pow(1 + rate, nper) - pv)
					* (rate * Math.pow(1 + rate, nper - type))
					/ (Math.pow(1 + rate, nper) - 1);
			if (calpmt == pmt) {
				return Math.round(calpmt * 10000000) / 10000000;
			} else {
				if (nper < 0) {
					if (Math.abs(calpmt) < Math.abs(pmt)) {
						minnper = nper;
						nper = (maxnper + nper) / 2;
					} else {
						maxnper = nper;
						nper = (nper + minnper) / 2;
					}
				} else {
					if (Math.abs(calpmt) < Math.abs(pmt)) {
						maxnper = nper;
						nper = (nper + minnper) / 2;
					} else {
						minnper = nper;
						nper = (maxnper + nper) / 2;
					}
				}
				if (maxnper == minnper) {
					throw "SS_ERROR_NUM";
				}
			}
			irup++;
		}
		return Math.round(nper * 10000000) / 10000000;
	},
	subtotal : function() {
		var args = Array.prototype.slice.call(arguments);
		var fun_num = args.shift();
		switch (fun_num) {
			case 1 :
				return this.average.apply(null, args.concat());
				break;
			case 2 :
				return this.count.apply(null, args.concat());
				break;
			case 3 :
				return this.counta.apply(null, args.concat());
				break;
			case 4 :
				return this.max.apply(null, args.concat());
				break;
			case 5 :
				return this.min.apply(null, args.concat());
				break;
			case 6 :
				return this.product.apply(null, args.concat());
				break;
			case 7 :
				return this.sum.apply(null, args.concat());
				break;
			default :
				;
		}
	},
	pv : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 > len || len > 5) {
			throw "SS_ERROR_NA";
		}
		var rate = arguments[0], nper = arguments[1], payment = arguments[2], fv = 0, type = 0;
		if (4 == len) {
			fv = arguments[3];
			if (fv === undefined || Ext.ss.common.Mask.isEmptyStr(fv)) {
				fv = 0;
			}
		}
		if (5 == len) {
			fv = arguments[3];
			if (fv === undefined || Ext.ss.common.Mask.isEmptyStr(fv)) {
				fv = 0;
			}
			type = arguments[4];
			if (type === undefined || Ext.ss.common.Mask.isEmptyStr(type)) {
				type = 0;
			}
		}
		rate = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, rate);
		nper = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, nper);
		payment = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, payment);
		fv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, fv);
		type = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, type);
		rate = parseFloat(rate);
		nper = parseFloat(nper);
		payment = parseFloat(payment);
		type = parseFloat(type);
		fv = parseFloat(fv);
		var ir = 0;
		for (var i = 1; i <= nper; i++) {
			ir = ir + payment * Math.pow(rate + 1, i);
		}
		var pv = -(ir / Math.pow(rate + 1, nper + 1 - type) + fv
				/ Math.pow(rate + 1, nper));
		return Math.round(pv * 10000) / 10000;
	},
	npv : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len < 2) {
			throw "SS_ERROR_NA";
		}
		var npv = 0;
		var rate = arguments[0];
		if (Ext.isObject(rate)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : rate.ox,
						oy : rate.oy
					}, {
						ox : rate.oex,
						oy : rate.oey
					});
			var minx = parseInt(rate.ox) + x, maxx = parseInt(rate.oex) + x, miny = parseInt(rate.oy)
					+ y, maxy = parseInt(rate.oey) + y;
			ds.checkSheetIndexValid(rate.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(rate.sheetIndex)) {
				curSheetIndex = rate.sheetIndex;
			}
			if (minx == maxx && miny == maxy) {
				rate = ds.getCellValue(minx, miny, curSheetIndex);
				rate = Ext.ss.common.Helper.convertPercent2num(rate);
			} else {
				throw "SS_ERROR_VALUE";
			}
		}
		var rate = Number(rate);
		if (!Ext.isNumber(rate)) {
			throw "SS_ERROR_VALUE";
		}
		for (var k = 1; k < len; k++) {
			var posOffset = arguments[k];
			var num = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
					sheetIndex, posOffset);
			npv = npv + num / Math.pow(1 + rate, k);
		}
		return Math.round(npv * 10000) / 10000;
	},
	fv : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 > len || len > 5) {
			throw "SS_ERROR_NA";
		}
		var rate = arguments[0], nper = arguments[1], payment = arguments[2], pv = 0, type = 0;
		if (4 == len) {
			pv = arguments[3];
			if (pv === undefined || Ext.ss.common.Mask.isEmptyStr(pv)) {
				pv = 0;
			}
		}
		if (5 == len) {
			pv = arguments[3];
			if (pv === undefined || Ext.ss.common.Mask.isEmptyStr(pv)) {
				pv = 0;
			}
			type = arguments[4];
			if (type === undefined || Ext.ss.common.Mask.isEmptyStr(type)) {
				type = 0;
			}
		}
		rate = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, rate);
		nper = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, nper);
		payment = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, payment);
		pv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pv);
		type = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, type);
		rate = parseFloat(rate);
		nper = parseFloat(nper);
		payment = parseFloat(payment);
		type = parseFloat(type);
		pv = parseFloat(pv);
		var ir = 0;
		for (var i = 1; i <= nper; i++) {
			ir = ir + payment * Math.pow(rate + 1, i - 1 + type);
		}
		var fv = -(ir + pv * Math.pow(rate + 1, nper));
		return Math.round(fv * 100) / 100;
	},
	vlookup : function(conditionOffset, checkOffset, ix, presion, sheetIndex,
			x, y, ds) {
		if (7 == arguments.length) {
			ds = y;
			y = x;
			x = sheetIndex;
			sheetIndex = presion;
		}
		var cond;
		if (Ext.isObject(conditionOffset)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, conditionOffset, conditionOffset);
			var cx = conditionOffset.ox + x, cy = conditionOffset.oy + y;
			ds.checkSheetIndexValid(conditionOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(conditionOffset.sheetIndex)) {
				curSheetIndex = conditionOffset.sheetIndex;
			}
			cond = ds.getCellValue(cx, cy, curSheetIndex);
		} else {
			cond = conditionOffset;
		}
		if (Ext.isObject(checkOffset)) {
			var minOffset = {
				sheetIndex : checkOffset.sheetIndex,
				ox : checkOffset.ox,
				oy : checkOffset.oy
			};
			var maxOffset = {
				sheetIndex : checkOffset.sheetIndex,
				ox : checkOffset.oex,
				oy : checkOffset.oey
			};
			var minx = minOffset.ox + x, miny = minOffset.oy + y, maxx = maxOffset.ox
					+ x, maxy = maxOffset.oy + y;
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, minOffset, maxOffset);
			if (!Ext.isDefined(cond)) {
				throw "SS_ERROR_NA";
			}
			ds.checkSheetIndexValid(minOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(minOffset.sheetIndex)) {
				curSheetIndex = minOffset.sheetIndex;
			}
			for (var i = minx; i <= maxx; i++) {
				var data = ds.getCellValue(i, miny, curSheetIndex);
				if (1 == Ext.ss.common.Mask.shExpMatch_KMP(data.toString(),
						cond.toString())) {
					var sum = ds.getCellValue(i, miny + ix - 1, curSheetIndex);
					return sum;
				}
			}
			throw "SS_ERROR_NA";
		}
	},
	hlookup : function(conditionOffset, checkOffset, iy, presion, sheetIndex,
			x, y, ds) {
		if (7 == arguments.length) {
			ds = y;
			y = x;
			x = sheetIndex;
			sheetIndex = presion;
		}
		var cond;
		if (Ext.isObject(conditionOffset)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, conditionOffset, conditionOffset);
			var cx = conditionOffset.ox + x, cy = conditionOffset.oy + y;
			ds.checkSheetIndexValid(conditionOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(conditionOffset.sheetIndex)) {
				curSheetIndex = conditionOffset.sheetIndex;
			}
			cond = ds.getCellValue(cx, cy, curSheetIndex);
		} else {
			cond = conditionOffset;
		}
		if (Ext.isObject(checkOffset)) {
			var minOffset = {
				sheetIndex : checkOffset.sheetIndex,
				ox : checkOffset.ox,
				oy : checkOffset.oy
			};
			var maxOffset = {
				sheetIndex : checkOffset.sheetIndex,
				ox : checkOffset.oex,
				oy : checkOffset.oey
			};
			var minx = minOffset.ox + x, miny = minOffset.oy + y, maxx = maxOffset.ox
					+ x, maxy = maxOffset.oy + y;
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, minOffset, maxOffset);
			if (!Ext.isDefined(cond)) {
				throw "SS_ERROR_NA";
			}
			ds.checkSheetIndexValid(minOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(minOffset.sheetIndex)) {
				curSheetIndex = minOffset.sheetIndex;
			}
			for (var i = miny; i <= maxy; i++) {
				var data = ds.getCellValue(minx, i, curSheetIndex);
				if (1 == Ext.ss.common.Mask.shExpMatch_KMP(data.toString(),
						cond.toString())) {
					var sum = ds.getCellValue(miny + iy - 1, i, curSheetIndex);
					return sum;
				}
			}
			throw "SS_ERROR_NA";
		}
	},
	match : function(lookup_value, lookup_array, match_type, sheetIndex, x, y,
			ds) {
		if (6 == arguments.length) {
			ds = y;
			y = x;
			x = sheetIndex;
			sheetIndex = match_type;
			match_type = 1;
		}
		var cond;
		var lookarray = [];
		if (Ext.isObject(lookup_value)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, lookup_value, lookup_value);
			var cx = lookup_value.ox + x, cy = lookup_value.oy + y;
			ds.checkSheetIndexValid(lookup_value.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(lookup_value.sheetIndex)) {
				curSheetIndex = lookup_value.sheetIndex;
			}
			cond = ds.getCellValue(cx, cy, curSheetIndex);
		} else {
			cond = lookup_value;
		}
		if (Ext.isObject(lookup_array)) {
			var minOffset = {
				sheetIndex : lookup_array.sheetIndex,
				ox : lookup_array.ox,
				oy : lookup_array.oy
			};
			var maxOffset = {
				sheetIndex : lookup_array.sheetIndex,
				ox : lookup_array.oex,
				oy : lookup_array.oey
			};
			var minx = minOffset.ox + x, miny = minOffset.oy + y, maxx = maxOffset.ox
					+ x, maxy = maxOffset.oy + y;
			if (maxy - miny > 0 && maxx - minx > 0) {
				return "#N/A";
			}
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, minOffset, maxOffset);
			ds.checkSheetIndexValid(minOffset.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(minOffset.sheetIndex)) {
				curSheetIndex = minOffset.sheetIndex;
			}
			for (var i = minx; i <= maxx; i++) {
				for (var j = miny; j <= maxy; j++) {
					var data = ds.getCellValue(i, j, curSheetIndex);
					lookarray.push(data);
				}
			}
		} else {
			lookup_array = lookup_array.toString();
			lookup_array = lookup_array.substring(1, lookup_array.length - 1);
			lookarray = lookup_array.split(",");
		}
		var result = 0;
		for (i = 0; i < lookarray.length; i++) {
			if (match_type == "0") {
				if (lookarray[i].toString() == cond) {
					result = i + 1;
					break;
				}
			} else if (match_type == "1") {
				if (lookarray[i].toString() <= cond) {
					result = i + 1;
				} else {
					break;
				}
			} else if (match_type == "-1") {
				if (lookarray[i].toString() >= cond) {
					result = i + 1;
				} else {
					break;
				}
			}
		}
		if (result == 0) {
			throw "SS_ERROR_NA";
		} else {
			return result;
		}
	},
	address : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		var row_num, column_num, abs_num = 1, a1 = true, sheet_text = "";
		if (len < 2 || len > 5) {
			throw "SS_ERROR_NA";
		}
		row_num = arguments[0];
		column_num = arguments[1];
		if (len > 2) {
			abs_num = arguments[2];
		}
		if (len > 3) {
			a1 = arguments[3];
		}
		if (len > 4) {
			sheet_text = arguments[4];
		}
		if (row_num == true) {
			row_num = 1;
		}
		if (column_num == true) {
			column_num = 1;
		}
		if (abs_num == true) {
			abs_num = 1;
		}
		row_num = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, row_num);
		column_num = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, column_num);
		abs_num = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, abs_num);
		a1 = Ext.ss.common.FunctionBoxHelper
				.getObjVal(ds, x, y, sheetIndex, a1);
		if (undefined == a1 || a1 == "" || a1 == null) {
			a1 = true;
		} else if (typeof a1 == "string") {
			var lower = a1.toLowerCase();
			if (lower == "false" || lower == "true") {
				a1 = eval(lower);
			} else {
				throw "SS_ERROR_VALUE";
			}
		}
		var result = "";
		if (!/^[1-9]+$/.test(row_num) || !/^[1-9]+$/.test(column_num)
				|| !/^[1-9]+$/.test(abs_num)) {
			throw "SS_ERROR_VALUE";
		}
		var scolumn = "";
		if (column_num > 702 || column_num < 1) {
			throw "SS_ERROR_VALUE";
		} else if (column_num < 27) {
			scolumn = String.fromCharCode("A".charCodeAt(0) + column_num - 1);
		} else {
			scolumn = String.fromCharCode("A".charCodeAt(0)
					+ Math.floor(column_num / 26) - 1)
					+ String.fromCharCode("A".charCodeAt(0) + column_num % 26
							- 1);
		}
		if (sheet_text) {
			sheet_text = "\"" + sheet_text + "\"!";
		}
		if (a1) {
			if (abs_num == 1) {
				result = sheet_text + "$" + scolumn + "$" + row_num;
			} else if (abs_num == 2) {
				result = sheet_text + scolumn + "$" + row_num;
			} else if (abs_num == 3) {
				result = sheet_text + "$" + scolumn + row_num;
			} else if (abs_num == 4) {
				result = sheet_text + scolumn + "" + row_num;
			} else {
				throw "SS_ERROR_VALUE";
			}
		} else {
			result = sheet_text + "R" + row_num + "C" + column_num;
		}
		return result;
	},
	average : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len) {
			throw "SS_ERROR_NA";
		}
		var res = 0, count = 0;
		var argsAllNaN = true;
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				count += (maxx - minx + 1) * (maxy - miny + 1);
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						if (Ext.ss.common.Mask.isNumber(num)) {
							res += Ext.ss.FunctionBox.num(num, 0);
							argsAllNaN = false;
						} else {
							count = count - 1;
						}
					}
				}
			} else {
				var num = Number(posOffset);
				if (Ext.isNumber(num)) {
					res += num;
					count++;
					argsAllNaN = false;
				}
			}
		}
		if (argsAllNaN) {
			throw "SS_ERROR_INFINITY";
		}
		return res / count * 100 / 100;
	},
	min : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len == 0) {
			throw "SS_ERROR_NA";
		}
		var res = false;
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						if (Ext.isNumber(num)) {
							if (res == false || res > num) {
								res = num;
							}
						}
					}
				}
			} else {
				var num = Number(posOffset);
				if (Ext.isNumber(num)) {
					if (res == false || res > num) {
						res = num;
					}
				} else {
					throw "SS_ERROR_VALUE";
				}
			}
		}
		if (res == false) {
			res = 0;
		}
		return res;
	},
	max : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (0 == len) {
			throw "SS_ERROR_NA";
		}
		var res = false;
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						if (Ext.isNumber(num)) {
							if (false === res || res < num) {
								res = num;
							}
						}
					}
				}
			} else {
				var num = Number(posOffset);
				if (false === res || res < num) {
					res = num;
				} else {
					throw "SS_ERROR_VALUE";
				}
			}
		}
		if (res == false) {
			res = 0;
		}
		return res;
	},
	getCellByOffset : function(ox, oy, sheetIndex, x, y, ds) {
		if (5 == arguments.length) {
			ds = y;
			y = x;
			x = sheetIndex;
			sheetIndex = oy;
			if (ox.ox != ox.oex || ox.oy != ox.oey) {
				throw "SS_ERROR_VALUE";
				return;
			}
			oy = ox.oy;
			if (false !== Ext.type(ox.sheetIndex)) {
				sheetIndex = ox.sheetIndex;
			}
			ox = ox.ox;
			if (!(0 <= sheetIndex || undefined == sheetIndex
					|| null == sheetIndex || "" == sheetIndex)) {
				throw "#PARAM_ERROR";
			}
			if (!Ext.isNumber(sheetIndex)) {
				sheetIndex = ds.activeSheet;
			}
		}
		if (false == Ext.type(sheetIndex) || "" === sheetIndex) {
			sheetIndex = ds.activeSheet;
		}
		var pos = {
			x : x,
			y : y
		};
		x += ox;
		y += oy;
		var r = ds.getCellValue(x, y, sheetIndex);
		var date = ds.transfer2Date(r);
		var tmp = ds.getCellObj(x, y, sheetIndex);
		var sheetObj = ds.sheets[sheetIndex];
		var colcss = sheetObj.colSetting[y] || {};
		var rowcss = sheetObj.rowSetting[x] || {};
		var wholecss = sheetObj.wholeSetting || {};
		var fm = ds.getAttribute(tmp, rowcss, colcss, wholecss, "format");
		if (Ext.isDate(date)) {
			r = date.format(ds.defaultDateFormat);
		} else if (Ext.isString(r)) {
			if ("text" != fm) {
				var last = r.charAt(r.length - 1);
				if ("%" == last) {
					var rest = r.slice(0, r.length - 1);
					var rest = Number(rest);
					if (NaN != rest) {
						r = rest / 100;
					}
				}
			}
		}
		var num = Number(r);
		if (Ext.isNumber(num)) {
			r = num;
		} else if (false == Ext.type(r) || "" === r) {
			r = 0;
		}
		ds.fireEvent("project", ds, pos, {
					ox : ox,
					oy : oy
				}, {
					ox : ox,
					oy : oy
				});
		if ("true" == r || "TRUE" == r || "True" == r) {
			r = true;
		}
		if ("false" == r || "FALSE" == r || "False" == r) {
			r = false;
		}
		return r;
	},
	condition : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len != 3) {
			throw "SS_ERROR_NA";
		}
		var cond = arguments[0], trueVal = arguments[1], falseVal = arguments[2];
		if (cond && "FALSE" !== cond && "false" !== cond) {
			if ("true" == trueVal || "TRUE" == trueVal || "True" == trueVal) {
				return TRUE;
			}
			if ("false" == trueVal || "FALSE" == trueVal || "False" == trueVal) {
				return FALSE;
			} else {
				return trueVal;
			}
		} else {
			if ("true" == trueVal || "TRUE" == trueVal || "True" == trueVal) {
				return TRUE;
			}
			if ("false" == trueVal || "FALSE" == trueVal || "False" == trueVal) {
				return FALSE;
			} else {
				return falseVal;
			}
		}
	},
	day : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		if (len != 5) {
			throw "SS_ERROR_NA";
		}
		var posOffset = arguments[0];
		posOffset = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, posOffset);
		if (Ext.isNumber(Number(posOffset))) {
			var baseDate = new Date("1900-01-01");
			if (posOffset == "") {
				throw "SS_ERROR_NUM";
			}
			baseDate.setDate(baseDate.getDate() + Number(posOffset) - 2);
			return baseDate.getDate();
		} else if (Ext.isDate(new Date(posOffset))) {
			var baseDate = new Date(posOffset);
			return baseDate.getDate();
		} else {
			throw "SS_ERROR_VALUE";
		}
	},
	large : function() {
		var maxDigNum = 0;
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len != 2) {
			throw "SS_ERROR_NA";
		}
		var argsAry = [];
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				argsAry[k] = [];
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						num = Number(num);
						if (Ext.isNumber(num)) {
							argsAry[k].push(num);
						}
					}
				}
			} else {
				argsAry[k] = posOffset;
			}
		}
		if (Ext.isBoolean(argsAry[0]) && argsAry[1] > 0) {
			return Number(argsAry[0]);
		}
		if (!argsAry[0].length) {
			throw "SS_ERROR_NUM";
		}
		var arg2 = Number(argsAry[1]);
		if (!Ext.isNumber(arg2)) {
			throw "SS_ERROR_VALUE";
		}
		var tmp = [], ac = argsAry[0];
		for (var i = 0; i < ac.length; i++) {
			if (Ext.isNumber(ac[i])) {
				tmp.push(ac[i]);
			}
		}
		var len = tmp.length;
		for (var m = 0; m < len - 1; m++) {
			for (var n = m + 1; n < len; n++) {
				if (tmp[m] < tmp[n]) {
					var tp = tmp[m];
					tmp[m] = tmp[n];
					tmp[n] = tp;
				}
			}
		}
		return tmp[arg2 - 1];
	},
	small : function() {
		var maxDigNum = 0;
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len != 2) {
			throw "SS_ERROR_NA";
		}
		var argsAry = [];
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (Ext.isObject(posOffset)) {
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, {
							ox : posOffset.ox,
							oy : posOffset.oy
						}, {
							ox : posOffset.oex,
							oy : posOffset.oey
						});
				var minx = parseInt(posOffset.ox) + x, maxx = parseInt(posOffset.oex)
						+ x, miny = parseInt(posOffset.oy) + y, maxy = parseInt(posOffset.oey)
						+ y;
				ds.checkSheetIndexValid(posOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(posOffset.sheetIndex)) {
					curSheetIndex = posOffset.sheetIndex;
				}
				argsAry[k] = [];
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var num = ds.getCellValue(i, j, curSheetIndex);
						num = Number(num);
						if (Ext.isNumber(num)) {
							argsAry[k].push(num);
						}
					}
				}
			} else {
				argsAry[k] = posOffset;
			}
		}
		if (Ext.isBoolean(argsAry[0]) && argsAry[1] > 0) {
			return Number(argsAry[0]);
		}
		if (!argsAry[0].length) {
			throw "SS_ERROR_NUM";
		}
		var arg2 = Number(argsAry[1]);
		if (!Ext.isNumber(arg2)) {
			throw "SS_ERROR_VALUE";
		}
		var tmp = [], ac = argsAry[0];
		for (var i = 0; i < ac.length; i++) {
			if (Ext.isNumber(ac[i])) {
				tmp.push(ac[i]);
			}
		}
		var len = tmp.length;
		for (var m = 0; m < len - 1; m++) {
			for (var n = m + 1; n < len; n++) {
				if (tmp[m] > tmp[n]) {
					var tp = tmp[m];
					tmp[m] = tmp[n];
					tmp[n] = tp;
				}
			}
		}
		return tmp[arg2 - 1];
	},
	time : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len != 3) {
			throw "SS_ERROR_NA";
		}
		var argsAry = [];
		for (var k = 0; k < len; k++) {
			var posOffset = arguments[k];
			if (!posOffset) {
				posOffset = 0;
			}
			argsAry[k] = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
					sheetIndex, posOffset);
			if (argsAry[k] == true) {
				argsAry[k] = 1;
			}
			if (argsAry[k] == false) {
				argsAry[k] = 0;
			}
		}
		var hourPart = Number(argsAry[0]);
		var minutePart = Number(argsAry[1]);
		var secondPart = Number(argsAry[2]);
		if (Ext.isNumber(hourPart) && Ext.isNumber(minutePart)
				&& Ext.isNumber(secondPart) && hourPart < 32768
				&& minutePart < 32768 && secondPart < 32768) {
			minutePart += Math.floor(secondPart / 60);
			hourPart = Math.floor(hourPart);
			var sumMin = minutePart + hourPart * 60;
			var hsm = sumMin % 60;
			hourPart = (sumMin - hsm) / 60;
			minutePart = hsm % 60;
			var hm = hourPart % 24;
			var zone = "AM";
			if (hm > 11) {
				zone = "PM";
			}
			hourPart = hm % 12;
			if (hourPart == 0) {
				hourPart = 12;
			}
			minutePart = Math.floor(minutePart);
			if (minutePart < 10) {
				minutePart = "0" + minutePart;
			}
			secondPart = Math.floor(secondPart % 60);
			if (secondPart < 10) {
				secondPart = "0" + secondPart;
			}
			return hourPart + ":" + minutePart + ":" + secondPart + " " + zone;
		} else {
			throw "SS_ERROR_NUM";
		}
	},
	hour : function() {
		var maxDigNum = 0;
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len != 1) {
			throw "SS_ERROR_NA";
		}
		var posOffset = arguments[0];
		posOffset = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, posOffset);
		if (Ext.isBoolean(posOffset)) {
			return 0;
		}
		var posTmp = Number(posOffset);
		if (Ext.isNumber(posTmp) && posTmp > 0) {
			posTmp = posTmp.toString();
			var as = posTmp.match(/(\.\d+)$/);
			if (as) {
				var sumSecond = Number(as[1]) * 1440;
				return (sumSecond - sumSecond % 60) / 60;
			} else {
				return 0;
			}
		} else if (posOffset.indexOf(".") > -1) {
			throw "SS_ERROR_VALUE";
		} else {
			if (/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i
					.test(posOffset)) {
				var date = posOffset.replace(
						/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i, "");
				date = date.replace(/\s+/g, "");
				var pm = posOffset
						.match(/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i);
				if (!date) {
					if (!Ext.isDate(new Date(date))) {
						throw "SS_ERROR_VALUE";
					}
				}
				var h = Number(pm[1]);
				var m = Number(pm[2]);
				var s = Number(pm[3]);
				if (pm[4]) {
					if (m > -1 && m < 60 && s > -1 && s < 60 && h > -1
							&& h < 13) {
						if (/pm/i.test(pm[4])) {
							if (h == 12) {
								return 12;
							} else {
								return h + 12;
							}
						} else {
							if (h == 12) {
								return 0;
							} else {
								return h;
							}
						}
					} else {
						throw "SS_ERROR_VALUE";
					}
				} else {
					if (h > 24) {
						return h % 12;
					} else {
						return h;
					}
				}
			} else if (/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i.test(posOffset)) {
				var date = posOffset.replace(
						/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i, "");
				date = date.replace(/\s+/g, "");
				var pm = posOffset
						.match(/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i);
				if (!date) {
					if (!Ext.isDate(new Date(date))) {
						throw "SS_ERROR_VALUE";
					}
				}
				var h = Number(pm[1]);
				var m = Number(pm[2]);
				if (pm[3]) {
					if (m > -1 && m < 60 && h > -1 && h < 13) {
						if (/pm/i.test(pm[3])) {
							if (h == 12) {
								return 12;
							} else {
								return h + 12;
							}
						} else {
							if (h == 12) {
								return 0;
							} else {
								return h;
							}
						}
					} else {
						throw "SS_ERROR_VALUE";
					}
				} else {
					if (h > 24) {
						return h % 12;
					} else {
						return h;
					}
				}
			}
		}
	},
	minute : function() {
		var maxDigNum = 0;
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len != 1) {
			throw "SS_ERROR_NA";
		}
		var posOffset = arguments[0];
		posOffset = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, posOffset);
		if (Ext.isBoolean(posOffset)) {
			return 0;
		}
		var posTmp = Number(posOffset);
		if (Ext.isNumber(posTmp) && posTmp > 0) {
			posTmp = posTmp.toString();
			if (posTmp.indexOf(".") > -1) {
				return Math.floor(Number(posTmp) * 1440 % 60);
			} else {
				return 0;
			}
		} else if (posOffset.indexOf(".") > -1) {
			throw "SS_ERROR_VALUE";
		} else {
			if (/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i
					.test(posOffset)) {
				var date = posOffset.replace(
						/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i, "");
				date = date.replace(/\s+/g, "");
				var pm = posOffset
						.match(/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i);
				if (date) {
					if (!Ext.isDate(new Date(date))) {
						throw "SS_ERROR_VALUE";
					}
				}
				var h = Number(pm[1]);
				var m = Number(pm[2]);
				var s = Number(pm[3]);
				if (m > -1 && m < 60 && s > -1 && s < 60) {
					if (pm[4] && (h < 0 || h > 12)) {
						throw "SS_ERROR_VALUE";
					}
					return m;
				} else {
					throw "SS_ERROR_VALUE";
				}
			} else if (/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i.test(posOffset)) {
				var date = posOffset.replace(
						/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i, "");
				date = date.replace(/\s+/g, "");
				var pm = posOffset
						.match(/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i);
				if (date) {
					if (!Ext.isDate(new Date(date))) {
						throw "SS_ERROR_VALUE";
					}
				}
				var h = Number(pm[1]);
				var m = Number(pm[2]);
				if (m > -1 && m < 60) {
					if (pm[3] && (h < 0 || h > 12)) {
						throw "SS_ERROR_VALUE";
					}
					return m;
				} else {
					throw "SS_ERROR_VALUE";
				}
			}
		}
	},
	second : function() {
		var maxDigNum = 0;
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (len != 1) {
			throw "SS_ERROR_NA";
		}
		var posOffset = arguments[0];
		posOffset = Ext.ss.common.FunctionBoxHelper.getObjVal(ds, x, y,
				sheetIndex, posOffset);
		if (Ext.isBoolean(posOffset)) {
			return 0;
		}
		var posTmp = Number(posOffset);
		if (Ext.isNumber(posTmp) && posTmp > 0) {
			posTmp = posTmp.toString();
			if (posTmp.indexOf(".") > -1) {
				var mpos = Number(posTmp) * 1440 % 60;
				var s = Math.floor((mpos - Math.floor(mpos)) * 60);
				return s;
			} else {
				return 0;
			}
		} else if (posOffset.indexOf(".") > -1) {
			throw "SS_ERROR_VALUE";
		} else {
			if (/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i
					.test(posOffset)) {
				var date = posOffset.replace(
						/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i, "");
				date = date.replace(/\s+/g, "");
				var pm = posOffset
						.match(/(\d+)\s*:\s*(\d+)\s*:\s*(\d+)(\s+[P|A]M){0,1}/i);
				if (date) {
					if (!Ext.isDate(new Date(date))) {
						throw "SS_ERROR_VALUE";
					}
				}
				var h = Number(pm[1]);
				var m = Number(pm[2]);
				var s = Number(pm[3]);
				if (m > -1 && m < 60 && s > -1 && s < 60) {
					if (pm[4] && (h < 0 || h > 12)) {
						throw "SS_ERROR_VALUE";
					}
					return s;
				} else {
					throw "SS_ERROR_VALUE";
				}
			} else if (/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i.test(posOffset)) {
				var date = posOffset.replace(
						/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i, "");
				date = date.replace(/\s+/g, "");
				var pm = posOffset
						.match(/(\d+)\s*:\s*(\d+)\s*(\s+[P|A]M){0,1}$/i);
				if (date) {
					if (!Ext.isDate(new Date(date))) {
						throw "SS_ERROR_VALUE";
					}
				}
				var h = Number(pm[1]);
				var m = Number(pm[2]);
				if (m > -1 && m < 60) {
					if (pm[3] && (h < 0 || h > 12)) {
						throw "SS_ERROR_VALUE";
					}
					return 0;
				} else {
					throw "SS_ERROR_VALUE";
				}
			}
		}
	},
	days360 : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (2 == len) {
			var astart = arguments[0];
			var aend = arguments[1];
			var method = false;
		} else if (3 == len) {
			var astart = arguments[0];
			var aend = arguments[1];
			var method = arguments[2];
		} else {
			throw "SS_ERROR_NA";
		}
		var startdate = Ext.ss.common.FunctionBoxHelper.getDateObjValue(ds, x,
				y, sheetIndex, astart);
		var enddate = Ext.ss.common.FunctionBoxHelper.getDateObjValue(ds, x, y,
				sheetIndex, aend);
		var yeardiff = 0, monthdiff = 0, daydiff = 0, startday = 0, endday = 0;
		yeardiff = (enddate.getYear() - startdate.getYear()) * 360;
		monthdiff = (enddate.getMonth() - startdate.getMonth()) * 30;
		if (method) {
			if (startdate.getDate() == Ext.ss.common.Helper
					.getlastday(startdate)) {
				startday = 30;
			} else {
				startday = startdate.getDate();
			}
			if (enddate.getDate() == Ext.ss.common.Helper.getlastday(enddate)) {
				endday = 30;
			} else {
				endday = enddate.getDate();
			}
			daydiff = endday - startday;
		} else {
			if (startdate.getDate() == Ext.ss.common.Helper
					.getlastday(startdate)) {
				startday = 30;
			} else {
				startday = startdate.getDate();
			}
			if (enddate.getDate() == Ext.ss.common.Helper.getlastday(enddate)
					&& startday < 30) {
				monthdiff = monthdiff + 30;
				endday = 1;
			} else if (enddate.getDate() == Ext.ss.common.Helper
					.getlastday(enddate)
					&& startday >= 30) {
				endday = 30;
			} else {
				endday = enddate.getDate();
			}
			daydiff = endday - startday;
		}
		return yeardiff + monthdiff + daydiff;
	},
	seriessum : function(x, n, m, values, sheetIndex, x, y, ds) {
		if (8 == arguments.length) {
			x = Number(x);
			if (!Ext.isNumber(x)) {
				return "#VALUE";
			}
			n = Number(n);
			if (!Ext.isNumber(n)) {
				return "#VALUE";
			}
			m = Number(m);
			if (!Ext.isNumber(m)) {
				return "#VALUE";
			}
			var coefs = [];
			if (Ext.isObject(values)) {
				var minOffset = {
					sheetIndex : values.sheetIndex,
					ox : values.ox,
					oy : values.oy
				};
				var maxOffset = {
					sheetIndex : values.sheetIndex,
					ox : values.oex,
					oy : values.oey
				};
				var minx = minOffset.ox + x, miny = minOffset.oy + y, maxx = maxOffset.ox
						+ x, maxy = maxOffset.oy + y;
				if (maxy - miny > 0 && maxx - minx > 0) {
					return "#N/A";
				}
				ds.fireEvent("project", ds, {
							x : x,
							y : y
						}, minOffset, maxOffset);
				ds.checkSheetIndexValid(minOffset.sheetIndex);
				var curSheetIndex = sheetIndex;
				if (false != Ext.type(minOffset.sheetIndex)) {
					curSheetIndex = minOffset.sheetIndex;
				}
				for (var i = minx; i <= maxx; i++) {
					for (var j = miny; j <= maxy; j++) {
						var data = ds.getCellValue(i, j, curSheetIndex);
						coefs.push(Number(data));
					}
				}
			} else {
				coefs.push(Number(values));
			}
			var result = 0;
			for (i = 0; i < coefs.length; i++) {
				result = result + coefs[i] * Math.pow(x, n + (i - 1) * m);
			}
			return result;
		} else {
			throw "SS_ERROR_NA";
		}
	},
	db : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (4 > len || 5 < len) {
			throw "SS_ERROR_NA";
		}
		var cost, salvage, life, period, month;
		cost = arguments[0];
		salvage = arguments[1];
		life = arguments[2];
		period = arguments[3];
		if (4 == len) {
			month = 12;
		}
		if (5 == len) {
			month = arguments[4];
		}
		cost = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, cost);
		salvage = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, salvage);
		life = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, life);
		period = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, period);
		month = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, month);
		var rate = Math.round((1 - Math.pow(salvage / cost, 1 / life)) * 1000)
				/ 1000;
		var persum = 0, db = 0;
		for (var i = 1; i <= period; i++) {
			if (i == 1) {
				db = persum = cost * rate * month / 12;
			} else if (i == life) {
				db = (cost - persum) * rate * (12 - month) / 12;
			} else {
				db = (cost - persum) * rate;
				persum = persum + db;
			}
		}
		return Math.round(db * 10000) / 10000;
	},
	ddb : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (4 > len || 5 < len) {
			throw "SS_ERROR_NA";
		}
		var cost, salvage, life, period, factor;
		cost = arguments[0];
		salvage = arguments[1];
		life = arguments[2];
		period = arguments[3];
		if (4 == len) {
			factor = 2;
		}
		if (5 == len) {
			factor = arguments[4];
		}
		cost = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, cost);
		salvage = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, salvage);
		life = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, life);
		period = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, period);
		factor = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, factor);
		var persum = 0, db = 0;
		for (var i = 1; i <= period; i++) {
			db = Math.min((cost - persum) * factor / life, cost - salvage
							- persum);
			persum = persum + db;
		}
		return Math.round(db * 100) / 100;
	},
	vdb : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		len -= 4;
		if (5 > len || 7 < len) {
			throw "SS_ERROR_NA";
		}
		var cost, salvage, life, startperiod, endperiod, factor, noswitch = false;
		cost = arguments[0];
		salvage = arguments[1];
		life = arguments[2];
		startperiod = arguments[3];
		endperiod = arguments[4];
		if (5 == len) {
			factor = 2;
		} else if (6 == len) {
			factor = arguments[5];
		} else if (7 == len) {
			factor = arguments[5];
			noswitch = arguments[6];
		}
		cost = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, cost);
		salvage = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, salvage);
		life = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, life);
		startperiod = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, startperiod);
		endperiod = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, endperiod);
		factor = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, factor);
		var persum = 0, db = 0, persum2 = 0;
		for (var i = 1; i <= startperiod; i++) {
			db = Math.min((cost - persum) * factor / life, cost - salvage
							- persum);
			persum = persum + db;
		}
		for (var i = 1; i <= endperiod; i++) {
			db = Math.min((cost - persum2) * factor / life, cost - salvage
							- persum2);
			persum2 = persum2 + db;
		}
		var vdb = persum2 - persum;
		return Math.round(vdb * 10000) / 10000;
	},
	mirr : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 != len) {
			throw "SS_ERROR_NA";
		}
		var values = arguments[0];
		var frate = arguments[1];
		var rrate = arguments[2];
		var nums = [], positive = [], negative = [];
		if (Ext.isObject(values)) {
			ds.fireEvent("project", ds, {
						x : x,
						y : y
					}, {
						ox : values.ox,
						oy : values.oy
					}, {
						ox : values.oex,
						oy : values.oey
					});
			var minx = parseInt(values.ox) + x, maxx = parseInt(values.oex) + x, miny = parseInt(values.oy)
					+ y, maxy = parseInt(values.oey) + y;
			ds.checkSheetIndexValid(values.sheetIndex);
			var curSheetIndex = sheetIndex;
			if (false != Ext.type(values.sheetIndex)) {
				curSheetIndex = values.sheetIndex;
			}
			for (var i = minx; i <= maxx; i++) {
				for (var j = miny; j <= maxy; j++) {
					var num = ds.getCellValue(i, j, curSheetIndex);
					num = Number(num);
					if (Ext.isNumber(num)) {
						nums[nums.length] = num;
					}
				}
			}
		} else if (Ext.isString(values)) {
			if (values.charAt(0) == "{"
					&& values.charAt(values.length - 1) == "}") {
				var str = values.slice(1, values.length - 1);
				nums = str.split(",");
			} else {
				throw "SS_ERROR_NA";
			}
		}
		frate = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, frate);
		rrate = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, rrate);
		for (var ii = 0; ii < nums.length; ii++) {
			if (nums[ii] < 0) {
				positive[positive.length] = 0;
				negative[negative.length] = nums[ii];
			} else {
				negative[negative.length] = 0;
				positive[positive.length] = nums[ii];
			}
		}
		frate = parseFloat(frate) ? parseFloat(frate) : 0;
		rrate = parseFloat(rrate) ? parseFloat(rrate) : 0;
		var fnpv = Ext.ss.common.FunctionBoxHelper.getnpv(frate, negative, ds,
				x, y, sheetIndex);
		var rnpv = Ext.ss.common.FunctionBoxHelper.getnpv(rrate, positive, ds,
				x, y, sheetIndex);
		var mirr = Math.pow(-rnpv * Math.pow(1 + rrate, nums.length)
						/ (fnpv * (1 + frate)), 1 / (nums.length - 1))
				- 1;
		return Math.round(mirr * 10000000) / 10000000;
	},
	sln : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (3 != len) {
			throw "SS_ERROR_NA";
		}
		var cost = arguments[0];
		var salvage = arguments[1];
		var life = arguments[2];
		cost = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, cost);
		salvage = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, salvage);
		life = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, life);
		var sln = (cost - salvage) / life;
		return Math.round(sln * 10000) / 10000;
	},
	syd : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (4 != len) {
			throw "SS_ERROR_NA";
		}
		var cost = arguments[0];
		var salvage = arguments[1];
		var life = arguments[2];
		var per = arguments[3];
		cost = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, cost);
		salvage = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, salvage);
		life = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, life);
		per = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, per);
		var syd = (cost - salvage) * (life - per + 1) * 2 / (life * (life + 1));
		return Math.round(syd * 10000) / 10000;
	},
	ppmt : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (4 > len || len > 6) {
			throw "SS_ERROR_NA";
		}
		var rate = arguments[0], per = arguments[1], nper = arguments[2], pv = arguments[3], fv = 0, type = 0;
		if (5 == len) {
			fv = arguments[4];
		}
		if (6 == len) {
			fv = arguments[4];
			type = arguments[5];
		}
		rate = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, rate);
		per = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, per);
		nper = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, nper);
		pv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pv);
		fv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, fv);
		type = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, type);
		rate = parseFloat(rate);
		nper = parseFloat(nper);
		per = parseFloat(per);
		pv = parseFloat(pv);
		type = parseFloat(type);
		fv = parseFloat(fv);
		pv = pv + fv;
		var ppmt = Math.round(pv * (rate * Math.pow(1 + rate, per - 1 - type))
				/ (Math.pow(1 + rate, nper) - 1) * 10000)
				/ 10000;
		return -ppmt;
	},
	ipmt : function() {
		var len = arguments.length;
		var ds, x, y, sheetIndex;
		ds = arguments[len - 1];
		y = arguments[len - 2];
		x = arguments[len - 3];
		sheetIndex = arguments[len - 4];
		len -= 4;
		if (4 > len || len > 6) {
			throw "SS_ERROR_NA";
		}
		var rate = arguments[0], per = arguments[1], nper = arguments[2], pv = arguments[3], fv = 0, type = 0;
		if (5 == len) {
			fv = arguments[4];
		}
		if (6 == len) {
			fv = arguments[4];
			type = arguments[5];
		}
		rate = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, rate);
		per = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, per);
		nper = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, nper);
		pv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, pv);
		fv = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, fv);
		type = Ext.ss.common.FunctionBoxHelper.getNumObjValue(ds, x, y,
				sheetIndex, type);
		rate = parseFloat(rate);
		nper = parseFloat(nper);
		per = parseFloat(per);
		pv = parseFloat(pv);
		type = parseFloat(type);
		fv = parseFloat(fv);
		pv = pv + fv;
		var ppmt = -Math.round(pv * (rate * Math.pow(1 + rate, per - 1 - type))
				/ (Math.pow(1 + rate, nper) - 1) * 100)
				/ 100;
		var pmt1 = Math.round(pv * (rate * Math.pow(1 + rate, nper - type))
				/ (Math.pow(1 + rate, nper) - 1) * 100)
				/ 100;
		var ir = 0;
		if (1 == type) {
			for (var i = 1; i <= nper; i++) {
				ir = ir + Math.pow(rate + 1, i);
			}
		} else {
			ir = 1;
			for (var i = 1; i < nper; i++) {
				ir = ir + Math.pow(rate + 1, i);
			}
		}
		var pmt2 = Math.round(fv / ir);
		var pmt = -(pmt1 + pmt2);
		return (pmt - ppmt) * 10000 / 10000;
	},
	isif : function() {
		var len = arguments.length;
		var ds = arguments[len - 1];
		var y = arguments[len - 2];
		var x = arguments[len - 3];
		var sheetIndex = arguments[len - 4];
		var cell = ds.getCellObj(x, y, sheetIndex) || {};
		var iparams = cell.ISIParams, result = 0;
		if (iparams && iparams[ds.ISIFormulaIndex]) {
			result = iparams[ds.ISIFormulaIndex][2] || 0;
			ds.ISIFormulaIndex++;
		}
		if (ds.updteISFormulaFromServer) {
			len -= 4;
			var curSheetIndex = ds.activeSheet;
			var args = [];
			for (var i = 0; i < len; i++) {
				var it = arguments[i];
				if (Ext.isObject(it)) {
					var sheetIndex = curSheetIndex;
					if (false != Ext.type(it.sheetIndex)) {
						sheetIndex = it.sheetIndex;
					}
					var minx = it.ox + x, miny = it.oy + y, maxx = it.oex + x, maxy = it.oey
							+ y;
					var v = ds.getCellValue(minx, miny, sheetIndex) || 0;
					args.push(v);
				} else {
					args.push(it);
				}
			}
			ds.ISFCache.push(["isif", args]);
			return result;
		}
		return result;
	}
};

Ext.ns("Ext.ss");
Ext.ss.HotKey = function(config) {
	Ext.apply(this, config);
	Ext.ss.HotKey.superclass.constructor.call(this)
};
Ext.extend(Ext.ss.HotKey, Ext.util.Observable, {
	pendingInterval : 15,
	lookup : {
		37 : "left",
		39 : "right",
		38 : "up",
		40 : "down",
		46 : "del",
		33 : "pageUp",
		34 : "pageDown",
		36 : "home",
		35 : "end",
		27 : "esc",
		9 : "tab",
		13 : "enter",
		19 : "break"
	},
	onKeyupFn : function(e) {
		var ss = this.spreadsheet;
		var sm = ss.sm;
		var tg = e.getTarget();
		var k = e.getKey();
		if (k != 17 && !this.popupIsShow(tg)) {
			if (k != 16) {
				if ((9 == k) || (13 == k)) {
					e.stopEvent();
					sm.setFocusCell(sm.focusCell.x, sm.focusCell.y)
				}
			} else {
				delete this.shiftKeyConfig
			}
		}
		sm.fireEvent("renderborder")
	},
	onKeydownFn : function(e) {
		var tg = e.getTarget();
		if (!this.keydownPending && !this.popupIsShow(tg)) {
			this.keydownPending = true;
			var k = e.getKey();
			var ss = this.spreadsheet;
			var shift = e.shiftKey, ctrl = e.ctrlKey;
			var tgName = tg.tagName;
			if (shift && !this.shiftKeyConfig) {
				var focusCell = (ss.sm).focusCell;
				this.shiftKeyConfig = {
					x : focusCell.x,
					y : focusCell.y
				}
			}
			if (ctrl && shift) {
				if (37 == k) {
					this.onCtrlShiftNavigationFn("left")
				} else {
					if (39 == k) {
						this.onCtrlShiftNavigationFn("right")
					} else {
						if (38 == k) {
							this.onCtrlShiftNavigationFn("up")
						} else {
							if (40 == k) {
								this.onCtrlShiftNavigationFn("down")
							}
						}
					}
				}
			} else {
				if (true == ctrl) {
					if (!Ext.isIE && (118 != k && 86 != k)
							&& (99 != k && 67 != k) && (120 != k && 88 != k)) {
						e.stopEvent()
					}
					if (120 == k || 88 == k) {
						this.onCutFn()
					} else {
						if (99 == k || 67 == k) {
							this.onCopyFn()
						} else {
							if (118 == k || 86 == k) {
								this.onPasteFn()
							} else {
								if (122 == k || 90 == k) {
								} else {
									if (121 == k || 89 == k) {
									} else {
										if (102 == k || 70 == k) {
											if (Ext.isIE) {
												e.stopEvent()
											}
											this.onFindFn()
										} else {
											if (66 == k) {
												if (Ext.isIE) {
													e.stopEvent()
												}
												this.onBold()
											} else {
												if (85 == k) {
													if (Ext.isIE) {
														e.stopEvent()
													}
													this.onUndrline()
												} else {
													if (73 == k) {
														if (Ext.isIE) {
															e.stopEvent()
														}
														this.onItalic()
													} else {
														if (79 == k) {
															if (Ext.isIE) {
																e.stopEvent()
															}
															this.stopDefault(e);
															this.onOpenFile()
														} else {
															if (80 == k) {
																if (Ext.isIE) {
																	e
																			.stopEvent()
																}
																this
																		.stopDefault(e);
																this
																		.onPrintFile()
															} else {
																if (83 == k) {
																	if (Ext.isIE) {
																		e
																				.stopEvent()
																	}
																	this
																			.stopDefault(e);
																	this
																			.onSaveFile()
																} else {
																	if (k == 36) {
																		if (Ext.isIE) {
																			e
																					.stopEvent()
																		}
																		this
																				.stopDefault(e);
																		this
																				.onCtrlNavigationFn("home")
																	} else {
																		if (k == 35) {
																			if (Ext.isIE) {
																				e
																						.stopEvent()
																			}
																			this
																					.stopDefault(e);
																			this
																					.onCtrlEndFn()
																		} else {
																			if (37 == k) {
																				this
																						.onCtrlNavigationFn("left")
																			} else {
																				if (39 == k) {
																					this
																							.onCtrlNavigationFn("right")
																				} else {
																					if (38 == k) {
																						this
																								.onCtrlNavigationFn("up")
																					} else {
																						if (40 == k) {
																							this
																									.onCtrlNavigationFn("down")
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				} else {
					if ("TEXTAREA" == tgName) {
						var isPasteArea = Ext.get(tg)
								.hasClass("x-spreadsheet-paste-textarea");
						if (isPasteArea) {
							tgName = "DIV"
						}
					}
					if (46 == k && tgName != "TEXTAREA" && tgName != "INPUT") {
						this.spreadsheet.contextmenu.onClearAllFn()
					} else {
						if (37 == k && tgName != "INPUT"
								&& tgName != "TEXTAREA") {
							e.stopEvent();
							this.onLeftFn(shift)
						} else {
							if (38 == k && tgName != "INPUT"
									&& tgName != "TEXTAREA") {
								e.stopEvent();
								this.onUpFn(shift)
							} else {
								if ((39 == k || 9 == k) && tgName != "INPUT"
										&& tgName != "TEXTAREA") {
									e.stopEvent();
									this.onRightFn(shift)
								} else {
									if (40 == k && tgName != "TEXTAREA") {
										e.stopEvent();
										this.onDownFn(shift)
									} else {
										if (13 == k) {
											e.stopEvent();
											this.onDownFn(shift)
										} else {
											if (13 == k && false) {
												var ss = this.spreadsheet;
												var sm = ss.sm;
												var textCover = sm
														.getTextCover();
												if ("none" == textCover.style.display) {
													e.stopEvent();
													this.onEnterFn()
												}
											} else {
												if (k == 8) {
													if ((tgName != "TEXTAREA" && "INPUT" != tgName)
															|| tg.value == "") {
														this.stopDefault(e)
													}
												} else {
													if (k == 36) {
														this.onHomeFn()
													} else {
														if (k == 33) {
															this.onPageUpFn()
														} else {
															if (k == 34) {
																this
																		.onPageDownFn()
															} else {
																if (27 == k) {
																	ss
																			.resetCutStatus();
																	ss
																			.hidePasteOptions()
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
					if (!e.altKey) {
						if (!Ext.isIE && shift) {
							if (k == 16) {
								this.stopDefault(e)
							} else {
								this.onKeypressFn(k, tg)
							}
						} else {
							if (k != 16) {
								this.onKeypressFn(k, tg)
							}
						}
					}
				}
			}
(function	() {
				delete(this.keydownPending)
			}).defer(this.pendingInterval, this)
		}
	},
	onKeypressFn : function(k, tg) {
		var cName = tg.parentNode.className;
		if (!this.lookup[k]) {
			var editor = (this.spreadsheet).editor;
			if (k == 112) {
				window
						.open(
								"http://www.cubedrive.com/wordPublic?id=waGuAW04a-0_&viewStatus=publicView",
								"_blank")
			} else {
				if (k == 113) {
					if (cName == "x-spreadsheet-viewport") {
						this.onEnterFn(null, "db");
						editor.inEditMode = true
					}
				} else {
					if (k == 116) {
						window.location.reload()
					} else {
						if (k > 113 && k <= 124) {
						} else {
							if (cName == "x-spreadsheet-viewport") {
								this.onEnterFn(true);
								editor.inEditMode = false
							}
						}
					}
				}
			}
		}
		return false
	},
	onEnterFn : function(sv, where) {
		var ss = this.spreadsheet;
		var sm = ss.sm;
		var editor = ss.editor;
		if (sm.focusCell) {
			editor.startEditing(sm.focusCell, sv, where)
		}
	},
	onUpFn : function(shift) {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var rows = ds.data.rows;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		sm.transferFocus();
		var x, y;
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			var flag = (minx == x);
			if (shift) {
				if (flag) {
					x = maxx;
					y = maxy
				} else {
					x = minx;
					y = miny
				}
			}
			rows[x] = rows[x] || {};
			var cell = rows[x][y] || {};
			if (cell.wrap) {
				var wrap = cell.wrap;
				var span = ds.getVisiableSpan(wrap.minx, wrap.miny, wrap.maxx,
						wrap.maxy);
				y = span.miny
			}
			x--;
			var rt = ds.findVisiableCell(x, y, "row", "up");
			x = rt.x;
			y = rt.y;
			if ("showRange" === sm.posFlag || "left" === sm.posFlag) {
				if (0 == x) {
					return
				}
				if (showRange.row.start <= x) {
					if (shift) {
						if (flag) {
							sm.selectRange(null, {
										x : minx,
										y : miny
									}, {
										x : x,
										y : y
									})
						} else {
							sm.selectRange(null, {
										x : x,
										y : y
									}, {
										x : maxx,
										y : maxy
									})
						}
					} else {
						sm.setFocusCell(x, y, true)
					}
				} else {
					if (ss.isRowSplit()) {
						ss.scrollingRow(ss.autoOffset, 1);
						if (shift) {
							if (flag) {
								sm.selectRange(null, {
											x : minx,
											y : miny
										}, {
											x : x,
											y : y
										})
							} else {
								sm.selectRange(null, {
											x : x,
											y : y
										}, {
											x : maxx,
											y : maxy
										})
							}
						} else {
							sm.setFocusCell(x, y, true)
						}
					} else {
						if (showRange.row.start > freezeRange.row.end + 1) {
							ss.scrollingRow(ss.autoOffset, 1)
						}
						if (shift) {
							if (flag) {
								sm.selectRange(null, {
											x : minx,
											y : miny
										}, {
											x : x,
											y : y
										})
							} else {
								sm.selectRange(null, {
											x : x,
											y : y
										}, {
											x : maxx,
											y : maxy
										})
							}
						} else {
							sm.setFocusCell(x, y, true)
						}
					}
				}
			} else {
				if ("top" === sm.posFlag || "left-top" === sm.posFlag) {
					if (0 === x) {
						return
					}
					if (ss.isRowSplit() && x < freezeRange.row.start
							&& freezeRange.row.start > 1) {
						ss.scrollingURow(ss.autoOffset, 1)
					}
					if (shift) {
						if (flag) {
							sm.selectRange(null, {
										x : minx,
										y : miny
									}, {
										x : x,
										y : y
									})
						} else {
							sm.selectRange(null, {
										x : x,
										y : y
									}, {
										x : maxx,
										y : maxy
									})
						}
					} else {
						sm.setFocusCell(x, y, true)
					}
				}
			}
		}
		this.updateStatus()
	},
	onDownFn : function(shift) {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		var x, y;
		sm.transferFocus();
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			var rows = ds.data.rows;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			var flag = (minx == x);
			if (shift) {
				if (flag) {
					x = maxx;
					y = maxy
				} else {
					x = minx;
					y = miny
				}
			}
			rows[x] = rows[x] || {};
			var cell = rows[x][y] || {};
			if (cell.wrap) {
				var wrap = cell.wrap;
				var span = ds.getVisiableSpan(wrap.minx, wrap.miny, wrap.maxx,
						wrap.maxy);
				y = span.miny;
				x = wrap.maxx + 1
			} else {
				x++
			}
			var rt = ds.findVisiableCell(x, y, "row");
			x = rt.x;
			y = rt.y;
			if ("showRange" === sm.posFlag || "left" === sm.posFlag) {
				if (showRange.row.end <= x) {
					ss.scrollingRow(-ss.autoOffset, 1)
				}
				if (shift) {
					if (flag) {
						sm.selectRange(null, {
									x : minx,
									y : miny
								}, {
									x : x,
									y : y
								})
					} else {
						sm.selectRange(null, {
									x : x,
									y : y
								}, {
									x : maxx,
									y : maxy
								})
					}
				} else {
					sm.setFocusCell(x, y, true)
				}
			} else {
				if ("top" === sm.posFlag || "left-top" === sm.posFlag) {
					if (ss.isRowSplit()) {
						if (x > freezeRange.row.end) {
							ss.scrollingURow(-ss.autoOffset, 1)
						}
						if (shift) {
							if (flag) {
								sm.selectRange(null, {
											x : minx,
											y : miny
										}, {
											x : x,
											y : y
										})
							} else {
								sm.selectRange(null, {
											x : x,
											y : y
										}, {
											x : maxx,
											y : maxy
										})
							}
						} else {
							sm.setFocusCell(x, y, true)
						}
					} else {
						if (showRange.row.start > freezeRange.row.end + 1
								&& x > freezeRange.row.end) {
							ss.ylivescroller.dom.scrollTop = 0;
							if (shift) {
								if (flag) {
									sm.selectRange(null, {
												x : minx,
												y : miny
											}, {
												x : x,
												y : y
											})
								} else {
									sm.selectRange(null, {
												x : x,
												y : y
											}, {
												x : maxx,
												y : maxy
											})
								}
							} else {
								sm.focusCell = {
									x : x,
									y : y
								};
								sm.fireEvent("focuscell", sm, x, y);
								sm.selectedStart = Ext.apply({}, sm.focusCell);
								sm.selectedEnd = Ext.apply({}, sm.focusCell)
							}
						} else {
							if (shift) {
								if (flag) {
									sm.selectRange(null, {
												x : minx,
												y : miny
											}, {
												x : x,
												y : y
											})
								} else {
									sm.selectRange(null, {
												x : x,
												y : y
											}, {
												x : maxx,
												y : maxy
											})
								}
							} else {
								sm.setFocusCell(x, y, true)
							}
						}
					}
				}
			}
		}
		this.updateStatus()
	},
	onLeftFn : function(shift) {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var rows = ds.data.rows;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		sm.transferFocus();
		var x, y;
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			var flag = (miny == y);
			if (shift) {
				if (flag) {
					x = maxx;
					y = maxy
				} else {
					x = minx;
					y = miny
				}
			}
			rows[x] = rows[x] || {};
			var cell = rows[x][y] || {};
			if (cell.wrap) {
				var wrap = cell.wrap;
				var span = ds.getVisiableSpan(wrap.minx, wrap.miny, wrap.maxx,
						wrap.maxy);
				x = span.minx
			}
			y--;
			var rt = ds.findVisiableCell(x, y, "col", "left");
			x = rt.x;
			y = rt.y;
			if ("showRange" === sm.posFlag || "top" === sm.posFlag) {
				if (0 === y) {
					return
				}
				if (showRange.col.start > y) {
					if (ss.isColSplit()) {
						ss.scrollingCol(ss.autoOffset, 1)
					} else {
						if (showRange.col.start > freezeRange.col.end + 1) {
							ss.scrollingCol(ss.autoOffset, 1)
						}
					}
				}
				if (shift) {
					if (flag) {
						sm.selectRange(null, {
									x : minx,
									y : miny
								}, {
									x : x,
									y : y
								})
					} else {
						sm.selectRange(null, {
									x : x,
									y : y
								}, {
									x : maxx,
									y : maxy
								})
					}
				} else {
					sm.setFocusCell(x, y, true)
				}
			} else {
				if ("left" === sm.posFlag || "left-top" === sm.posFlag) {
					if (0 === y) {
						return
					}
					if (ss.isColSplit() && y < freezeRange.col.start
							&& freezeRange.col.start > 1) {
						ss.scrollingLCol(ss.autoOffset, 1)
					}
					if (shift) {
						if (flag) {
							sm.selectRange(null, {
										x : minx,
										y : miny
									}, {
										x : x,
										y : y
									})
						} else {
							sm.selectRange(null, {
										x : x,
										y : y
									}, {
										x : maxx,
										y : maxy
									})
						}
					} else {
						sm.setFocusCell(x, y, true)
					}
				}
			}
		}
		this.updateStatus()
	},
	onRightFn : function(shift) {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		sm.transferFocus();
		var x, y;
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			var rows = ds.data.rows;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			var flag = (miny == y);
			if (shift) {
				if (flag) {
					x = maxx;
					y = maxy
				} else {
					x = minx;
					y = miny
				}
			}
			rows[x] = rows[x] || {};
			var cell = rows[x][y] || {};
			if (cell.wrap) {
				var wrap = cell.wrap;
				var span = ds.getVisiableSpan(wrap.minx, wrap.miny, wrap.maxx,
						wrap.maxy);
				x = span.minx;
				y = wrap.maxy + 1
			} else {
				y++
			}
			var rt = ds.findVisiableCell(x, y, "col");
			x = rt.x;
			y = rt.y;
			if ("showRange" === sm.posFlag || "top" === sm.posFlag) {
				if (showRange.col.end <= y) {
					ss.scrollingCol(-ss.autoOffset, 1)
				}
				if (shift) {
					if (flag) {
						sm.selectRange(null, {
									x : minx,
									y : miny
								}, {
									x : x,
									y : y
								})
					} else {
						sm.selectRange(null, {
									x : x,
									y : y
								}, {
									x : maxx,
									y : maxy
								})
					}
				} else {
					sm.setFocusCell(x, y, true)
				}
			} else {
				if ("left" === sm.posFlag || "left-top" === sm.posFlag) {
					if (ss.isColSplit()) {
						if (y > freezeRange.col.end) {
							ss.scrollingLCol(-ss.autoOffset, 1)
						}
						if (shift) {
							if (flag) {
								sm.selectRange(null, {
											x : minx,
											y : miny
										}, {
											x : x,
											y : y
										})
							} else {
								sm.selectRange(null, {
											x : x,
											y : y
										}, {
											x : maxx,
											y : maxy
										})
							}
						} else {
							sm.setFocusCell(x, y, true)
						}
					} else {
						if (showRange.col.start > freezeRange.col.end + 1
								&& y > freezeRange.col.end) {
							ss.xlivescroller.dom.scrollLeft = 0;
							if (shift) {
								if (flag) {
									sm.selectRange(null, {
												x : minx,
												y : miny
											}, {
												x : x,
												y : y
											})
								} else {
									sm.selectRange(null, {
												x : x,
												y : y
											}, {
												x : maxx,
												y : maxy
											})
								}
							} else {
								sm.focusCell = {
									x : x,
									y : y
								};
								sm.fireEvent("focuscell", sm, x, y);
								sm.selectedStart = Ext.apply({}, sm.focusCell);
								sm.selectedEnd = Ext.apply({}, sm.focusCell)
							}
						} else {
							if (shift) {
								if (flag) {
									sm.selectRange(null, {
												x : minx,
												y : miny
											}, {
												x : x,
												y : y
											})
								} else {
									sm.selectRange(null, {
												x : x,
												y : y
											}, {
												x : maxx,
												y : maxy
											})
								}
							} else {
								sm.setFocusCell(x, y, true)
							}
						}
					}
				}
			}
		}
		this.updateStatus()
	},
	updateStatus : function() {
		var ss = this.spreadsheet;
		var sm = ss.sm;
		ss.fireEvent("updatestate", sm.selectedStart, sm.selectedEnd);
		ss.fireEvent("updatefx", sm.focusCell.x, sm.focusCell.y);
		ss.fireEvent("updatepress", ss, sm.focusCell.x, sm.focusCell.y)
	},
	onCutFn : function() {
		this.spreadsheet.contextmenu.onCutFn()
	},
	onCopyFn : function() {
		this.spreadsheet.contextmenu.onCopyFn()
	},
	onPasteFn : function() {
		this.spreadsheet.contextmenu.onPasteFn()
	},
	onUndoFn : function() {
		this.spreadsheet.toolbar.onUndoFn()
	},
	onRedoFn : function() {
		this.spreadsheet.toolbar.onRedoFn()
	},
	onFindFn : function() {
		if (false === Ext.type(this.lookupwin)) {
			this.lookupwin = new Ext.ss.popup.LookupWin({
						spreadsheet : this.spreadsheet
					})
		}
		this.lookupwin.tabpanel.setActiveTab(0);
		this.lookupwin.show()
	},
	onBold : function() {
		var ss = this.spreadsheet;
		var sm = ss.sm, ds = ss.ds;
		var focusCell = sm.focusCell;
		var cell = ds.getCell(focusCell.x, focusCell.y);
		if ("<b>" == cell.b) {
			ss.toolbar.applyAttribute([["b", ""]])
		} else {
			ss.toolbar.applyAttribute([["b", "<b>"]])
		}
		ss.updateStatus(focusCell.x, focusCell.y)
	},
	onUndrline : function() {
		var ss = this.spreadsheet;
		var sm = ss.sm, ds = ss.ds;
		var focusCell = sm.focusCell;
		var cell = ds.getCell(focusCell.x, focusCell.y);
		if ("<u>" == cell.u) {
			ss.toolbar.applyAttribute([["u", ""]])
		} else {
			ss.toolbar.applyAttribute([["u", "<u>"]])
		}
		ss.updateStatus(focusCell.x, focusCell.y)
	},
	onItalic : function() {
		var ss = this.spreadsheet;
		var sm = ss.sm, ds = ss.ds;
		var focusCell = sm.focusCell;
		var cell = ds.getCell(focusCell.x, focusCell.y);
		if ("<i>" == cell.i) {
			ss.toolbar.applyAttribute([["i", ""]])
		} else {
			ss.toolbar.applyAttribute([["i", "<i>"]])
		}
		ss.updateStatus(focusCell.x, focusCell.y)
	},
	onReplaceFn : function() {
		if (false === Ext.type(this.lookupwin)) {
			this.lookupwin = new Ext.ss.popup.LookupWin({
						spreadsheet : this.spreadsheet
					})
		}
		this.lookupwin.tabpanel.setActiveTab(1);
		this.lookupwin.show()
	},
	onOpenFile : function() {
		var ss = this.spreadsheet;
		var stb = ss.toolbar;
		feyaSoft.home.LoginCheck(function() {
					stb.onOpenFn()
				}, stb)
	},
	onPrintFile : function() {
		var ss = this.spreadsheet;
		var stb = ss.toolbar;
		feyaSoft.home.LoginCheck(function() {
					stb.onPrintFn()
				}, stb)
	},
	onSaveFile : function() {
		var ss = this.spreadsheet;
		var stb = ss.toolbar;
		feyaSoft.home.LoginCheck(function() {
					stb.onSaveFn()
				}, stb)
	},
	stopDefault : function(e) {
		e.keyCode = 0;
		if (e && e.preventDefault) {
			e.preventDefault()
		} else {
			window.event.returnValue = false
		}
		return false
	},
	onHomeFn : function() {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var rows = ds.data.rows;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		var x, y;
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			rows[x] = rows[x] || {};
			var cell = rows[x][y] || {};
			if (cell.wrap) {
				var wrap = cell.wrap;
				var span = ds.getVisiableSpan(wrap.minx, wrap.miny, wrap.maxx,
						wrap.maxy);
				y = span.miny
			}
			var rt = ds.findVisiableCell(x, y, "row", "up");
			x = rt.x;
			y = rt.y;
			sm.setFocusCell(x, 1, true)
		}
	},
	onPageUpFn : function() {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var rows = ds.data.rows;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		var x, y;
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			var flag = (minx == x);
			rows[x] = rows[x] || {};
			var cell = rows[x][y] || {};
			if (cell.wrap) {
				var wrap = cell.wrap;
				var span = ds.getVisiableSpan(wrap.minx, wrap.miny, wrap.maxx,
						wrap.maxy);
				y = span.miny
			}
			x--;
			var rt = ds.findVisiableCell(x, y, "row", "up");
			x = rt.x;
			y = rt.y;
			var vs = showRange.row.start;
			var ve = showRange.row.end;
			var va = ve - vs;
			if ("showRange" === sm.posFlag || "left" === sm.posFlag) {
				if (vs <= va) {
				} else {
					if (ss.isRowSplit()) {
						ss.scrollingRow(ss.autoOffset, va);
						sm.setFocusCell(x - va, y, true)
					} else {
						if (showRange.row.start > freezeRange.row.end + 1) {
							ss.scrollingRow(ss.autoOffset, va)
						}
					}
				}
			} else {
				if ("top" === sm.posFlag || "left-top" === sm.posFlag) {
					if (vs <= va) {
						ss.scrollingURow(ss.autoOffset, vs)
					}
					if (ss.isRowSplit() && x < freezeRange.row.start
							&& freezeRange.row.start > 1) {
						ss.scrollingURow(ss.autoOffset, va)
					}
				}
			}
		}
	},
	onPageDownFn : function() {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		var x, y;
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			var rows = ds.data.rows;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			var flag = (minx == x);
			rows[x] = rows[x] || {};
			var cell = rows[x][y] || {};
			if (cell.wrap) {
				var wrap = cell.wrap;
				var span = ds.getVisiableSpan(wrap.minx, wrap.miny, wrap.maxx,
						wrap.maxy);
				y = span.miny;
				x = wrap.maxx + 1
			} else {
			}
			var rt = ds.findVisiableCell(x, y, "row");
			x = rt.x;
			y = rt.y;
			var vs = showRange.row.start;
			var ve = showRange.row.end;
			var va = ve - vs;
			if ("showRange" === sm.posFlag || "left" === sm.posFlag) {
				ss.scrollingRow(-ss.autoOffset, va)
			} else {
				if ("top" === sm.posFlag || "left-top" === sm.posFlag) {
					if (ss.isRowSplit()) {
						if (x > freezeRange.row.end) {
							ss.scrollingURow(-ss.autoOffset, va)
						}
					} else {
						if (showRange.row.start > freezeRange.row.end + 1
								&& x > freezeRange.row.end) {
							ss.ylivescroller.dom.scrollTop = 0;
							sm.focusCell = {
								x : x,
								y : y
							};
							sm.fireEvent("focuscell", sm, x + va, y);
							sm.selectedStart = Ext.apply({}, sm.focusCell);
							sm.selectedEnd = Ext.apply({}, sm.focusCell)
						} else {
						}
					}
				}
			}
		}
	},
	onCtrlNavigationFn : function(nav) {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		var x, y;
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			var bound = ds.getReachBounds();
			var re = bound.row;
			var ce = bound.col;
			if ("left" === sm.posFlag || "left-top" === sm.posFlag) {
				ce = freezeRange.col.end
			}
			switch (nav) {
				case "home" :
					sm.setFocusCell(1, 1, true);
					break;
				case "up" :
					sm.setFocusCell(1, y, true);
					break;
				case "down" :
					sm.setFocusCell(re, y, true);
					break;
				case "left" :
					sm.setFocusCell(x, 1, true);
					break;
				case "right" :
					if (!Ext.isIE && ce == 14) {
						sm.setFocusCell(x, ce - 1, true)
					} else {
						sm.setFocusCell(x, ce, true)
					}
					break
			}
		}
		this.updateStatus()
	},
	onCtrlEndFn : function() {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var sm = ss.sm;
		var showRange = ss.showRange;
		var vs = showRange.row.start;
		var ve = showRange.row.end;
		var cs = showRange.col.start;
		var ce = showRange.col.end;
		var lastData;
		for (var i = 1; i <= (ve - vs); i++) {
			for (var j = 1; j <= (ce - cs); j++) {
				var cell = ds.getOrignCellObj(i, j);
				if (cell && cell.data) {
					lastData = {
						minx : i,
						miny : j
					}
				}
			}
		}
		if (lastData) {
			sm.setFocusCell(lastData.minx, lastData.miny, true)
		} else {
			sm.setFocusCell(vs, cs, true)
		}
	},
	onCtrlShiftNavigationFn : function(nav) {
		var ss = this.spreadsheet;
		var ds = ss.ds;
		var showRange = ss.showRange;
		var freezeRange = ss.freezeRange;
		var sm = ss.sm;
		sm.transferFocus();
		var x, y;
		if (sm.focusCell) {
			var rs = sm.getMinMaxFromStartEnd(sm.selectedStart, sm.selectedEnd);
			var sEndX = sm.selectedEnd.x, sEndY = sm.selectedEnd.y;
			var minx = rs.minPos.x, miny = rs.minPos.y, maxx = rs.maxPos.x, maxy = rs.maxPos.y;
			x = sm.focusCell.x;
			y = sm.focusCell.y;
			if ("showRange" === sm.posFlag || "top" === sm.posFlag) {
				switch (nav) {
					case "left" :
						sm.selectRange(null, {
									x : x,
									y : y
								}, {
									x : sEndX,
									y : 1
								});
						ss.scrollingCol(ss.autoOffset, showRange.col.end);
						break;
					case "right" :
						if (showRange.col.start > y) {
							ss.scrollingCol(-ss.autoOffset, showRange.col.start
											- y);
(function					() {
								sm.selectRange(null, {
											x : x,
											y : y
										}, {
											x : sEndX,
											y : showRange.col.end
										})
							}).defer(10)
						} else {
							sm.selectRange(null, {
										x : x,
										y : y
									}, {
										x : sEndX,
										y : showRange.col.end
									})
						}
						break;
					case "up" :
						sm.selectRange(null, {
									x : x,
									y : y
								}, {
									x : 1,
									y : sEndY
								});
						ss.scrollingRow(ss.autoOffset, showRange.row.end);
						break;
					case "down" :
						if (showRange.row.start > x) {
							ss.scrollingRow(-ss.autoOffset, showRange.row.start
											- x);
(function					() {
								sm.selectRange(null, {
											x : x,
											y : y
										}, {
											x : showRange.row.end,
											y : sEndY
										})
							}).defer(10)
						} else {
							sm.selectRange(null, {
										x : x,
										y : y
									}, {
										x : showRange.row.end,
										y : sEndY
									})
						}
						break
				}
			} else {
				if ("left" === sm.posFlag || "left-top" === sm.posFlag) {
					switch (nav) {
						case "left" :
							sm.selectRange(null, {
										x : x,
										y : y
									}, {
										x : sEndX,
										y : 1
									});
							ss
									.scrollingLCol(ss.autoOffset,
											freezeRange.col.end);
							break;
						case "right" :
							var bound = ds.getReachBounds();
							sm.setFocusCell(bound.row, bound.col, true);
							if (freezeRange.col.start > y) {
								ss.scrollingLCol(-ss.autoOffset,
										freezeRange.col.start - y);
(function						() {
									sm.selectRange(null, {
												x : x,
												y : y
											}, {
												x : sEndX,
												y : freezeRange.col.end
											})
								}).defer(10)
							} else {
								sm.selectRange(null, {
											x : x,
											y : y
										}, {
											x : sEndX,
											y : freezeRange.col.end
										})
							}
							break;
						case "up" :
							sm.selectRange(null, {
										x : x,
										y : y
									}, {
										x : 1,
										y : sEndY
									});
							ss.scrollingRow(ss.autoOffset, freezeRange.row.end);
							break;
						case "down" :
							if (showRange.row.start > x) {
								ss.scrollingRow(-ss.autoOffset,
										showRange.row.start - x);
(function						() {
									sm.selectRange(null, {
												x : x,
												y : y
											}, {
												x : showRange.row.end,
												y : sEndY
											})
								}).defer(10)
							} else {
								sm.selectRange(null, {
											x : x,
											y : y
										}, {
											x : showRange.row.end,
											y : sEndY
										})
							}
							break
					}
				}
			}
		}
		this.updateStatus()
	},
	popupIsShow : function(tg) {
		var pTagName = (tg.parentNode).tagName;
		if (pTagName == "LI") {
			return true
		}
		var tc = Ext.query("div[class=ext-el-mask]", document.body);
		for (var m = 0; m < tc.length; m++) {
			var it = tc[m];
			if (it.style.display == "block") {
				return true
			}
		}
	},
	mouseKeydown : function(e) {
		if (this.shiftKeyConfig && e.shiftKey) {
			var ss = this.spreadsheet;
			var ds = ss.ds;
			var sm = ss.sm;
			var firstFocusCell = this.shiftKeyConfig;
			var focusCell = sm.focusCell;
			sm.selectRange(null, {
						x : firstFocusCell.x,
						y : firstFocusCell.y
					}, {
						x : focusCell.x,
						y : focusCell.y
					})
		} else {
			delete this.shiftKeyConfig
		}
	}
});
window.onhelp = function() {
	return false
};

// Excel表格主视图
Ext.ns("Ext.ss");
var sc;
var isbrowserClose = true;
Ext.onReady(function() {
	document.body.parentNode.className += " ext-content-box ";
	var ua = navigator.userAgent.toLowerCase(), check = function(r) {
		return r.test(ua);
	}, docMode = document.documentMode;
	Ext.isIE9 = Ext.isIE
			&& (check(/msie 9/) && docMode != 7 && docMode != 8 || docMode == 9);
	Ext.isIE10 = Ext.isIE
			&& (check(/msie 10/) && docMode != 7 && docMode != 8
					&& docMode != 9 || docMode == 10);
	Ext.QuickTips.init();
	var wait = new Ext.LoadMask(document.body, {
				msg : "<b>Welcome to Enterprise "
						+ feyaSoft.lang.common.spreadsheet + "</b><br>"
						+ feyaSoft.lang.common.loadingData
			});
	wait.show();
	var fn = function(config) {
		sc = new Ext.ss.ViewerCon(config);
		new Ext.Viewport({
					border : false,
					layout : "fit",
					items : [sc]
				});
		wait.hide();
	};
	this.publicView = document.getElementById("publicView")
			.getAttribute("value");
	this.editFileId = document.getElementById("editFileId")
			.getAttribute("value");
	this.url = "/static/ux/office/spreadsheet/loadJson.json";
	if (this.publicView && this.publicView == "yes") {
		this.url = "ssPublic/loadPublicData";
	}
	if (this.editFileId) {
		var loadFn = function() {
			Ext.Ajax.request({
						timeout : 300000,
						url : this.url,
						params : {
							id : this.editFileId
						},
						success : function(response, options) {
							var jsonData = Ext.decode(response.responseText);
							if (jsonData.success == "true") {
								var file = jsonData.data[0];
								file.data = Ext.decode(file.description);
								delete file.description;
								fn.call(this, {
											file : file,
											title : FPROXY.prepareTitle(null,
													file)
										});
							} else {
								Ext.MessageBox.alert(
										feyaSoft.lang.common.error,
										jsonData.info);
								wait.hide();
							}
						},
						scope : this
					});
		};
		var me = this;
		Ext.Ajax.request({
					timeout : 300000,
					url : "secureDoc/isShield",
					params : {
						id : this.editFileId,
						publicView : this.publicView
					},
					success : function(response, options) {
						var json = Ext.decode(response.responseText);
						if ("no" == json.fileShield) {
							loadFn.call(me);
						} else {
							wait.hide();
							var secureWin = new feyaSoft.home.common.SecureAuthWin(
									{
										modal : true,
										authSuccessCallback : function() {
											loadFn.call(me);
										},
										scope : me
									});
							secureWin.show();
						}
					},
					failure : function(response, options) {
						wait.hide();
					},
					scope : me
				});
	} else {
		Ext.Ajax.request({
					timeout : 300000,
					url : this.url,
					params : {
						createDefault : true
					},
					success : function(response, options) {
						var jsonData = Ext.decode(response.responseText);
						if (jsonData.success == "true") {
							var file = jsonData.data[0];
							file.data = Ext.decode(file.description);
							file.isTemp = true;
							delete file.description;
							fn.call(this, {
										file : file,
										title : FPROXY.prepareTitle(null, file)
									});
						} else {
							Ext.MessageBox.alert(feyaSoft.lang.common.error,
									jsonData.info);
							wait.hide();
						}
					},
					scope : this
				});
	}
});
window.onload = function() {
	try {
		if (!window.onbeforeunload) {
			window.onbeforeunload = function() {
				if (sc) {
					return sc.browserClose();
				}
			};
		}
	} catch (e) {
	}
};
Ext.ss.ViewerCon = function(config) {
	Ext.apply(this, config);
	var file = {};
	if (config && config.file) {
		file = config.file;
		file.isPublic = true;
	}
	var flag = document.getElementById("flag").getAttribute("value");
	if (flag && (flag == "true" || flag == true)) {
		flag = true;
	} else {
		flag = this.readOnlyView || false;
	}
//	flag =true;
	// flag控制是只读模式还是编辑模式
	Ext.ss.ViewerCon.superclass.constructor.call(this, {
		id : file.id,
		title : this.title || file.name || feyaSoft.lang.common.spreadsheet,
		settings : {
			readOnlyView : flag
		},
		iconCls : "excel",
		tools : [{
					id : "gear",
					qtip : "Version " + Ext.ss.common.Mask.version,
					handler : function() {
						new Ext.ss.popup.about.AboutSS;
					},
					scope : this
				}, {
					id : "help",
					qtip : "Help",
					handler : function() {
						var newWindow = window
								.open(
										"http://www.cubedrive.com/wordPublic?id=waGuAW04a-0_&viewStatus=publicView",
										"_blank");
						newWindow.focus();
					},
					scope : this
				}],
		file : file
	});
	this.on("closewindow", this.onCloseWindowFn, this);
};
Ext.extend(Ext.ss.ViewerCon, Ext.ss.SheetContainer, {
			onBeforeCloseFn : function(p) {
				var ss = this.ss;
				var file = this.getFile() || {};
				if (ss.isChanged() || file.isTemp && file.onceChanged) {
					Ext.Msg.show({
								title : feyaSoft.lang.common.confirm,
								msg : feyaSoft.lang.common.fileChanged,
								buttons : Ext.Msg.YESNOCANCEL,
								fn : function(bid, text) {
									var toolbar = ss.toolbar;
									if ("yes" == bid) {
										toolbar.save(true);
									} else if ("no" == bid) {
										isbrowserClose = false;
										window.close();
									}
								},
								scope : this,
								icon : Ext.MessageBox.QUESTION
							});
					return false;
				} else {
					if (file.isTemp) {
						Ext.Ajax.request({
									timeout : 300000,
									url : "documentFile/delete",
									params : {
										id : file.id
									}
								});
					}
					isbrowserClose = false;
					window.close();
				}
			},
			browserClose : function() {
				var file = this.getFile() || {};
				if (isbrowserClose) {
					if (this.ss.isChanged() || file.isTemp && file.onceChanged) {
						return feyaSoft.lang.common.save_before_open;
					} else {
						if (file && file.isTemp) {
							if (file.onceChanged) {
								return feyaSoft.lang.common.save_before_open;
							}
							var ds = this.ss.ds;
							Ext.Ajax.request({
										timeout : 300000,
										url : "documentFile/delete",
										params : {
											id : file.id
										}
									});
						}
					}
				}
			}
		});
Ext.ns("feyaSoft.home.common");
feyaSoft.home.common.CompareWindow = Ext.extend(Ext.Window, {
	title : feyaSoft.lang.common.compare,
	width : document.body.clientWidth,
	height : document.body.clientHeight,
	shim : false,
	frame : true,
	modal : true,
	border : false,
	resizable : false,
	draggable : false,
	layout : "border",
	buttonAlign : "right",
	iconCls : "compare",
	linehg : 14,
	iframehg : document.body.clientHeight - 60,
	initComponent : function() {
		var wh = (document.body.clientWidth - 10) / 2;
		this.leftpanel = new feyaSoft.home.common.CompareLeftPanel({
					width : wh
				});
		this.centerpanel = new feyaSoft.home.common.CompareCenterPanel;
		this.items = [this.leftpanel, this.centerpanel];
		feyaSoft.home.common.CompareWindow.superclass.initComponent.call(this);
		this.show();
	},
	loadDataFn : function() {
		var param = {
			firstSelId : this.selectedIds[0],
			scondId : this.selectedIds[1]
		};
		Ext.Ajax.request({
					url : "documentHistory/getCompareDocInfo",
					params : param,
					method : "GET",
					success : function(result, request) {
						var jsonData = Ext.util.JSON
								.decode(result.responseText);
						var fsdoc1 = jsonData.firstDoc;
						var sedoc2 = jsonData.secondDoc;
						this.leftpanel.fillData(fsdoc1, "old");
						this.centerpanel.fillData(sedoc2, this.selectedIds[1]
										? "old"
										: "new");
					},
					failure : function(result, request) {
					},
					scope : this
				});
	},
	SaveFn : function(type, id, commitStr) {
		Ext.Ajax.request({
					url : "documentHistory/updateDoc",
					params : {
						documentId : id,
						description : commitStr
					},
					method : "POST",
					success : function(result, request) {
						var jsonData = Ext.util.JSON
								.decode(result.responseText);
						if (jsonData.success == "true") {
							Ext.Message.msgStay(feyaSoft.lang.common.confirm,
									jsonData.info, 2000);
						} else {
							Ext.MessageBox.alert(feyaSoft.lang.common.error,
									jsonData.info);
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert(feyaSoft.lang.common.error,
								"Internal error, please try again.");
					},
					scope : this
				});
	},
	scrollTopFn : function(pan, top) {
		var ebEl = pan.getEditorBodyEl();
		ebEl.scrollTo("top", top);
	},
	scrollLeftFn : function(pan, left) {
		var ebEl = pan.getEditorBodyEl();
		ebEl.scrollTo("left", left);
	},
	chanageNumFn : function(obj, top) {
		var minlin = top / this.linehg;
		var baslines = this.iframehg / this.linehg;
		var maxlin = minlin + baslines;
		var nstr = "";
		for (var n = minlin + 1; n < maxlin; n++) {
			nstr += "<div style=\"color:grey;overflow:hidden;width:100%;height:14px;font-size:12px;font-family:serif;border-right:1px dotted gray; \">"
					+ parseInt(n) + "</div>";
		}
		obj.innerHTML = nstr;
	}
});
feyaSoft.home.common.CompareLeftPanel = function(config) {
	this.infoLable = new Ext.form.Label({
				html : ""
			});
	feyaSoft.home.common.CompareLeftPanel.superclass.constructor.call(this, {
		region : "west",
		collapseMode : "mini",
		minWidth : 200,
		maxWidth : 1000,
		split : true,
		width : config.width,
		layout : "fit",
		autoScroll : false,
		sleft : 0,
		style : "padding-right:2px;",
		tbar : ["-", this.infoLable, "<div style=\"height:20px;\">&nbsp;</div>"]
	});
	this.on("afterRender", this.onAfterRenderFn, this);
};
Ext.extend(feyaSoft.home.common.CompareLeftPanel, Ext.Panel, {
	getDocMarkup : function() {
		return "<html><head><style type=\"text/css\">html{padding:0px;margin:0px} body{border:0;margin:0px;padding:0px;\"}</style></head><body  bgColor=\"transparent\" style=\"cursor: default;\">&nbsp;</body></html>";
	},
	onAfterRenderFn : function() {
		this.createIFrame();
	},
	createIFrame : function() {
		var numtb = document.createElement("table");
		numtb.cellspacing = "0";
		numtb.cellpadding = "0";
		numtb.border = 0;
		numtb.width = "100%";
		numtb.style.height = this.ownerCt.iframehg + "px";
		var numdiv = document.createElement("div");
		var lines = this.ownerCt.iframehg / this.ownerCt.linehg;
		this.ownerCt.chanageNumFn(numdiv, lines);
		var trobj = numtb.insertRow(0);
		var td0 = trobj.insertCell(0);
		td0.width = "20px";
		td0.appendChild(numdiv);
		numdiv.style.width = "100%";
		numdiv.style.height = "100%";
		this.numdivEl = Ext.get(numdiv);
		var td1 = trobj.insertCell(1);
		var iframe = document.createElement("iframe");
		iframe.width = "100%";
		iframe.height = "100%";
		iframe.scrolling = "yes";
		iframe.frameborder = "0";
		iframe.style.border = "none";
		td1.appendChild(iframe);
		this.body.dom.appendChild(numtb);
		this.iframe = iframe;
		this.iframeEl = Ext.get(iframe);
		if (Ext.isIE) {
			this.setDesignMode("on");
		}
		var doc = this.getDocument();
		doc.open();
		doc.write(this.getDocMarkup());
		doc.close();
		this.body.dom.style.overflow = "hidden";
		this.ownerCt.loadDataFn();
		Ext.EventManager.on(this.getWindow(), {
					scroll : this.onBodyScrollFn,
					buffer : 100,
					scope : this
				});
	},
	getDocument : function() {
		return this.getWindow().document;
	},
	getWindow : function() {
		return this.iframe.contentWindow || this.iframe.contentDocument;
	},
	setDesignMode : function(mode) {
		var doc = this.getDocument();
		if (doc) {
			doc.designMode = /on|true/i.test(String(mode).toLowerCase())
					? "on"
					: "off";
		}
	},
	getEditorBody : function() {
		var doc = this.getDocument();
		return doc.body || doc.documentElement;
	},
	getEditorBodyEl : function() {
		var body = this.getEditorBody();
		return Ext.fly(body);
	},
	fillData : function(json, type) {
		if (type == "new") {
			this.setDesignMode("on");
			this.getEditorBody().style.cursor = "text";
		} else {
			this.setDesignMode(Ext.isIE ? "on" : "off");
		}
		this.loadType = type;
		this.loadid = json.id;
		this.desObj = Ext.decode(json.description);
		if (this.desObj) {
			this.getEditorBody().innerHTML = Ext.util.Format
					.htmlDecode(this.desObj.json.html);
		} else {
			this.getEditorBody().innerHTML = "";
		}
		var sdate = Ext.util.Format.date(json.creationDate, "y-m-d H:i:s");
		if (this.desObj) {
			sdate = this.desObj.date;
			var style = this.desObj.json.style;
			var rg = /margin/gi;
			var rg2 = /padding/gi;
			for (var i in style) {
				if (rg.test(i) == false && rg2.test(i) == false) {
					this.setBodyStyleByName(i, style[i]);
				}
			}
		}
		var isNew = type == "new"
				? "<font color=red>Latest</font>"
				: "<font color=gray>History - older version</font>";
		this.infoLable.update(feyaSoft.lang.common.name + ": <u>" + json.name
				+ "</u>&nbsp;&nbsp;&nbsp;" + feyaSoft.lang.common.dateModified
				+ ": <u>" + sdate + "</u> (" + isNew + ")");
	},
	setBodyStyleByName : function(name, value) {
		var eb = this.getEditorBody();
		var ebEl = Ext.fly(eb);
		ebEl.setStyle(name, value);
	},
	onBodyScrollFn : function() {
		var centerpanel = this.ownerCt.centerpanel;
		var eb = this.getEditorBody();
		var ebEl = Ext.fly(eb);
		var top = ebEl.getScroll().top;
		var left = ebEl.getScroll().left;
		var type = "top";
		if (left != centerpanel.sleft) {
			type = "left";
		}
		centerpanel.stop = top;
		centerpanel.sleft = left;
		if (type == "top") {
			this.ownerCt.chanageNumFn(this.numdivEl.dom, top);
			this.ownerCt.chanageNumFn(centerpanel.numdivEl.dom, top);
			this.ownerCt.scrollTopFn(centerpanel, top);
		} else {
			this.ownerCt.scrollLeftFn(centerpanel, left);
		}
	}
});
feyaSoft.home.common.CompareCenterPanel = function(config) {
	this.infoLable = new Ext.form.Label({
				html : "xxxx"
			});
	this.saveBtn = new Ext.Button({
				text : feyaSoft.lang.common.save,
				iconCls : "icon_save",
				handler : this.onSaveFn,
				disabled : false,
				scope : this
			});
	feyaSoft.home.common.CompareCenterPanel.superclass.constructor.call(this, {
				region : "center",
				layout : "fit",
				autoScroll : false,
				sleft : 0,
				tbar : ["-", this.infoLable, "->", this.saveBtn]
			});
	this.on("afterRender", this.onAfterRenderFn, this);
};
Ext.extend(feyaSoft.home.common.CompareCenterPanel, Ext.Panel, {
	getDocMarkup : function() {
		return "<html><head><style type=\"text/css\">html{padding:0px;margin:0px} body{border:0;margin:0px;padding:0px;\"}</style></head><body  bgColor=\"transparent\" style=\"cursor: default;\">&nbsp;</body></html>";
	},
	onAfterRenderFn : function() {
		this.createIFrame();
	},
	createIFrame : function() {
		var numtb = document.createElement("table");
		numtb.cellspacing = "0";
		numtb.cellpadding = "0";
		numtb.border = 0;
		numtb.width = "100%";
		numtb.style.height = this.ownerCt.iframehg + "px";
		var numdiv = document.createElement("div");
		var lines = this.ownerCt.iframehg / this.ownerCt.linehg;
		this.ownerCt.chanageNumFn(numdiv, lines);
		var trobj = numtb.insertRow(0);
		var td0 = trobj.insertCell(0);
		td0.width = "20px";
		td0.appendChild(numdiv);
		numdiv.style.width = "100%";
		numdiv.style.height = "100%";
		this.numdivEl = Ext.get(numdiv);
		var td1 = trobj.insertCell(1);
		var iframe = document.createElement("iframe");
		iframe.width = "100%";
		iframe.height = "100%";
		iframe.scrolling = "yes";
		iframe.frameborder = "0";
		iframe.style.border = "none";
		td1.appendChild(iframe);
		this.body.dom.appendChild(numtb);
		this.iframe = iframe;
		this.iframeEl = Ext.get(iframe);
		if (Ext.isIE) {
			this.setDesignMode("on");
		}
		var doc = this.getDocument();
		doc.open();
		doc.write(this.getDocMarkup());
		doc.close();
		this.body.dom.style.overflow = "hidden";
		Ext.EventManager.on(this.getWindow(), {
					scroll : this.onBodyScrollFn,
					buffer : 100,
					scope : this
				});
	},
	getDocument : function() {
		return this.getWindow().document;
	},
	getWindow : function() {
		return this.iframe.contentWindow || this.iframe.contentDocument;
	},
	setDesignMode : function(mode) {
		var doc = this.getDocument();
		if (doc) {
			doc.designMode = /on|true/i.test(String(mode).toLowerCase())
					? "on"
					: "off";
		}
	},
	getEditorBody : function() {
		var doc = this.getDocument();
		return doc.body || doc.documentElement;
	},
	getEditorBodyEl : function() {
		var body = this.getEditorBody();
		return Ext.fly(body);
	},
	fillData : function(json, type) {
		if (type == "new") {
			this.saveBtn.enable();
			this.setDesignMode("on");
		} else {
			this.saveBtn.disable();
			this.setDesignMode(Ext.isIE ? "on" : "off");
		}
		this.loadType = type;
		this.loadid = json.id;
		this.desObj = Ext.decode(json.description);
		var ihtml = Ext.util.Format.htmlDecode(this.desObj.json.html);
		this.getEditorBody().innerHTML = ihtml;
		var isNew = type == "new"
				? "<font color=red>Latest version</font>"
				: "<font color=gray>History</font>";
		this.infoLable.update(feyaSoft.lang.common.name + ": <u>" + json.name
				+ "</u>&nbsp;&nbsp;&nbsp;" + feyaSoft.lang.common.dateModified
				+ ": <u>" + this.desObj.date + "</u> (" + isNew + ")");
		var style = this.desObj.json.style;
		var rg = /margin/gi;
		var rg2 = /padding/gi;
		for (var i in style) {
			if (rg.test(i) == false && rg2.test(i) == false) {
				this.setBodyStyleByName(i, style[i]);
			}
		}
	},
	setBodyStyleByName : function(name, value) {
		var eb = this.getEditorBody();
		var ebEl = Ext.fly(eb);
		ebEl.setStyle(name, value);
	},
	onSaveFn : function() {
		this.desObj.json.html = this.getEditorBody().innerHTML;
		this.desObj.date = (new Date).format("Y-m-d H:i:s");
		var comstr = Ext.encode(this.desObj);
		this.ownerCt.SaveFn(this.loadType, this.loadid, comstr);
	},
	onBodyScrollFn : function() {
		var leftpanel = this.ownerCt.leftpanel;
		var chg = document.body.clientHeight - 60;
		var baslines = chg / 14;
		var eb = this.getEditorBody();
		var ebEl = Ext.fly(eb);
		var top = ebEl.getScroll().top;
		var left = ebEl.getScroll().left;
		var type = "top";
		if (left != leftpanel.sleft) {
			type = "left";
		}
		leftpanel.stop = top;
		leftpanel.sleft = left;
		if (type == "top") {
			this.ownerCt.chanageNumFn(this.numdivEl.dom, top);
			this.ownerCt.chanageNumFn(leftpanel.numdivEl.dom, top);
			this.ownerCt.scrollTopFn(leftpanel, top);
		} else {
			this.ownerCt.scrollLeftFn(leftpanel, left);
		}
	}
});
Ext.ns("feyaSoft.home.common");
feyaSoft.home.common.AddContactWin = function(config) {
	var searchInvitePanel = new feyaSoft.home.common.SearchInvitePanel(config
			|| {});
	feyaSoft.home.common.AddContactWin.superclass.constructor.call(this, {
				title : feyaSoft.lang.file.addFriend,
				width : 600,
				height : 400,
				shim : false,
				constrainHeader : true,
				modal : true,
				border : true,
				buttonAlign : "right",
				layout : "fit",
				items : [searchInvitePanel],
				buttons : [{
							text : feyaSoft.lang.common.cancel,
							handler : this.cancelFn,
							scope : this
						}]
			});
	this.show();
};
Ext.extend(feyaSoft.home.common.AddContactWin, Ext.Window, {
			cancelFn : function() {
				this.close();
			}
		});
feyaSoft.home.common.SearchInvitePanel = Ext.extend(Ext.Panel, {
	border : false,
	autoScroll : true,
	initComponent : function() {
		var addFriendGroup = feyaSoft.lang.contact.addAsFriend;
		this.addUrl = "contact/inviteContact";
		if (this.groupId != null) {
			addFriendGroup = feyaSoft.lang.group.invite2group;
			this.addUrl = "locationUserPending/inviteFriend";
		}
		this.dataStore = new Ext.data.JsonStore({
					url : "userAccess/listForContact",
					fields : []
				});
		this.dataStore.baseParams = {
			excludeMe : "yes",
			start : 0,
			limit : 10,
			searchForFriend : true
		};
		this.inviteBtn = new Ext.Button({
					text : "<font color=\"blue\"><b>"
							+ feyaSoft.lang.file.invite + "</b></font>",
					iconCls : "email",
					handler : this.inviteFn,
					minWidth : 80,
					pressed : true,
					scope : this
				});
		this.dataView = new feyaSoft.home.common.ContactGroupDataView({
					store : this.dataStore,
					emptyText : "<div class=\"x-view-emtpytext\">"
							+ feyaSoft.lang.contact.inviteMsg + "</div>",
					applyText : addFriendGroup,
					applyCallback : this.addFriend,
					applyScope : this
				});
		this.tbar = [feyaSoft.lang.common.search, "",
				new Ext.ux.form.SearchField({
							autoShow : true,
							store : this.dataStore,
							width : 300,
							emptyText : feyaSoft.lang.common.atleast4char
						}), "->", this.inviteBtn];
		this.bbar = new Ext.PagingToolbar({
					pageSize : 10,
					store : this.dataStore,
					displayInfo : true,
					displayMsg : "{0} - {1} of {2}"
				});
		this.items = [this.dataView];
		feyaSoft.home.common.SearchInvitePanel.superclass.initComponent
				.call(this);
		this.reload();
	},
	addFriend : function(id) {
		var params = {
			userId : id
		};
		if (this.groupId != null) {
			params = {
				userId : id,
				groupId : this.groupId
			};
		}
		Ext.Ajax.request({
					url : this.addUrl,
					params : params,
					success : function(result, request) {
						var backObj = Ext.util.JSON.decode(result.responseText);
						if (backObj.success == "true") {
							Ext.Message.msgStay(feyaSoft.lang.common.confirm,
									backObj.info, 2000);
							this.reload();
						} else {
							Ext.MessageBox.alert(feyaSoft.lang.common.error,
									backObj.info);
						}
					},
					failure : function() {
						Ext.MessageBox.alert(feyaSoft.lang.common.error,
								"Fail to connect!");
					},
					scope : this
				});
	},
	reload : function() {
		this.dataStore.load({
					params : {
						start : 0,
						limit : 10
					}
				});
	},
	inviteFn : function() {
		new feyaSoft.home.common.InviteWindow(this.initialConfig);
	}
});
// 53号 85
Ext.ns("feyaSoft.home.common");
feyaSoft.home.common.ContactGroupDataView = Ext.extend(Ext.DataView, {
	cls : "x-publicgroup-list",
	applyText : "Apply",
	defaultLogo : feyaSoft.home.CONST.userLogo,
	overClass : "x-view-over",
	itemSelector : "div.x-group-preview",
	singleSelect : true,
	emptyText : "<div class=\"x-view-emtpytext\">"
			+ feyaSoft.lang.common.noResultDisplay + "</div>",
	initComponent : function() {
		var arr = [
				"<tpl for=\".\">",
				"<div class=\"x-group-preview\">",
				"<table><tbody><tr>",
				"<td class=\"x-group-logo-td\">",
				"<img class=\"x-group-logo\" src=\"{[values.logoPath?values.logoPath:(values.photo?values.photo:\"",
				this.defaultLogo,
				"\")]}\"></img>",
				"</td>",
				"<td>",
				"<div class=\"x-group-name\">{[values.firstname?values.firstname+\" \"+values.lastname:values.name]}</div>",
				"<div class=\"x-group-description\">{[values.description?values.description:\"<span>(",
				feyaSoft.lang.contact.noDescriptionForUser,
				")</span>\"]}</div>"];
		if (!this.hideApply) {
			arr = arr.concat(["<div class=\"x-group-foot\">",
					"<a name=\"apply\" href=\"#\">", this.applyText, "</a>",
					"</div>"]);
		}
		arr = arr.concat(["</td>", "</tr></tbody></table>", "</div>", "</tpl>",
				"<div class=\"x-clear\"></div>"]);
		this.tpl = (new Ext.XTemplate(arr.join(""))).compile();
		feyaSoft.home.common.ContactGroupDataView.superclass.initComponent
				.call(this);
		this.on("click", this.onClickFn, this);
	},
	onClickFn : function(dv, index, node, e) {
		var target = e.getTarget();
		if ("apply" == target.name) {
			if (this.applyCallback) {
				var rd = dv.getRecord(node);
				var id = rd.data.id || rd.data.contactUserId;
				this.applyCallback.call(this.applyScope || this, id, rd, node,
						dv);
			}
		}
	}
});
// 54号 86
Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.SharePanel = function(config) {
	this.myOwnerCt = null;
	this.listUsers = new feyaSoft.home.common.share.ListUsers(config);
	this.addUser = new feyaSoft.home.common.share.AddUser(config);
	feyaSoft.home.common.share.SharePanel.superclass.constructor.call(this, {
				title : feyaSoft.lang.file.peopleAccess,
				border : false,
				layout : "border",
				items : [this.listUsers, this.addUser]
			});
};
Ext.extend(feyaSoft.home.common.share.SharePanel, Ext.Panel, {
			initLoad : function(config) {
				this.myOwnerCt = config.myOwnerCt;
				this.listUsers.initLoad(config);
				this.addUser.initLoad(config);
			},
			existedUsers : function() {
				return this.listUsers.existedUsers();
			},
			reloadList : function() {
				this.listUsers.reload();
				this.myOwnerCt.reload();
			}
		});
feyaSoft.home.common.share.ListUsers = function(config) {
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "documentShareFile";
	if (config.componentClass) {
		this.componentClass = config.componentClass;
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	function viewEditPermission(val) {
		var result = val;
		if (val == 1) {
			result = feyaSoft.lang.common.canView;
		} else if (val == 2) {
			result = feyaSoft.lang.common.canEdit;
		} else if (val == 7) {
			result = feyaSoft.lang.common.canEdit;
		}
		return result;
	}
	function infoAction(val, p) {
		return "<img src=\"images/icons/information.png\" ext:qtitle=\"Note\" ext:qtip=\""
				+ val + "\"></img>";
	}
	var shareCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.user,
				width : 190,
				dataIndex : "shareUser"
			}, {
				header : feyaSoft.lang.common.permission,
				width : 110,
				dataIndex : "permission",
				scope : this,
				renderer : viewEditPermission
			}, {
				header : feyaSoft.lang.common.date,
				width : 140,
				dataIndex : "updateDate",
				renderer : Ext.util.Common.formatDateHour
			}, {
				header : "Info",
				width : 40,
				sortable : false,
				dataIndex : "note",
				renderer : infoAction
			}]);
	this.shareStore = new Ext.data.JsonStore({
				url : this.componentClass + "/list",
				remoteSort : true,
				fields : []
			});
	this.deleteBtn = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.remove,
		id : "delete-share-file-action",
		tooltip : "Highlight the item and click this button to remove this shared user",
		iconCls : "delete",
		handler : function() {
			var record = this.getSelectionModel().getSelected();
			if (record.data.id == 0) {
				Ext.MessageBox.alert(feyaSoft.lang.common.permissionDeny,
						"Not allowed to delete Owner share");
			} else {
				this.deleteItem(record);
			}
		},
		scope : this
	});
	this.changePermit = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.changePermission,
		tooltip : "Highlight the item and click this button to change this shared user permission",
		iconCls : "editItem",
		handler : function() {
			var record = this.getSelectionModel().getSelected();
			if (record.data.id == 0) {
				Ext.MessageBox.alert(feyaSoft.lang.common.permissionDeny,
						"Not allowed to change owner permission");
			} else {
				this.changeItem(record);
			}
		},
		scope : this
	});
	feyaSoft.home.common.share.ListUsers.superclass.constructor.call(this, {
				region : "center",
				border : true,
				store : this.shareStore,
				cm : shareCM,
				sm : selectBoxModel,
				width : 600,
				height : 300,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : feyaSoft.lang.common.loadingData
				},
				tbar : [this.deleteBtn, this.changePermit],
				autoScroll : true
			});
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.share.ListUsers, Ext.grid.GridPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.shareStore.baseParams = {
					fileId : this.fileId
				};
				this.shareStore.load();
			},
			existedUsers : function() {
				return this.shareStore.data.items;
			},
			onSelectionChangeFn : function(sm) {
				if (0 < sm.getSelections().length) {
					this.deleteBtn.enable();
					if (this.componentClass == "myPhotoShare"
							|| this.componentClass == "myPhotoFolderShare") {
						this.changePermit.disable();
					} else {
						this.changePermit.enable();
					}
				} else {
					this.deleteBtn.disable();
					this.changePermit.disable();
				}
			},
			deleteItem : function(rd) {
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirmDelete,
							msg : feyaSoft.lang.common.confirmDeleteDesc,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onDeleteConfirm,
							scope : this,
							record : rd
						});
			},
			onDeleteConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/delete",
								params : {
									id : options.record.data.id
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			changeItem : function(rd) {
				var changedPermit = feyaSoft.lang.common.canView;
				if (rd.data.permission == 1) {
					changedPermit = feyaSoft.lang.common.canEdit;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirm,
							msg : feyaSoft.lang.common.confirmChangePermit
									+ changedPermit,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onChangeConfirm,
							scope : this,
							record : rd
						});
			},
			onChangeConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/changePermit",
								params : {
									id : options.record.data.id
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			reload : function() {
				this.shareStore.reload();
			},
			disableDelete : function() {
				Ext.getCmp("delete-share-file-action").setDisabled(true);
			}
		});
feyaSoft.home.common.share.AddUser = function(config) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "documentShareFile";
	if (config.componentClass) {
		this.componentClass = config.componentClass;
	}
	var permissionStore = [["1", feyaSoft.lang.common.canView],
			["2", feyaSoft.lang.common.canEdit]];
	if (this.componentClass == "myPhotoShare"
			|| this.componentClass == "myPhotoFolderShare") {
		permissionStore = [["1", feyaSoft.lang.common.canView]];
	}
	var permission_data = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : permissionStore
			});
	var userStore = new Ext.data.JsonStore({
				url : "contact/list",
				remoteSort : true,
				fields : []
			});
	userStore.setDefaultSort("name", "ASC");
	this.contactName = new Ext.ux.form.LovCombo({
				fieldLabel : feyaSoft.lang.common.user,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				minChars : 2,
				triggerAction : "all",
				store : userStore,
				displayField : "name",
				hiddenName : "sharedUserId",
				valueField : "contactUserId",
				loadingText : feyaSoft.lang.common.loadingData,
				pageSize : 15,
				anchor : "93%",
				listeners : {
					select : this.onUserSelectFn,
					scope : this
				}
			});
	this.permission = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.permission,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				triggerAction : "all",
				store : permission_data,
				displayField : "name",
				hiddenName : "permission",
				mode : "local",
				valueField : "id",
				anchor : "93%",
				listeners : {
					select : this.onPermissionSelectFn,
					scope : this
				}
			});
	this.allowInvite = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.allowViewEdit,
				name : "allowInvite",
				disabled : true
			});
	this.note = new Ext.form.HtmlEditor({
				hideLabel : true,
				name : "note",
				height : 150,
				anchor : "93%"
			});
	var emailNotify = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.sendEmail,
				name : "emailNotify",
				checked : true
			});
	this.addMoreBtn = new Ext.Button({
				text : feyaSoft.lang.file.addFriend,
				handler : function() {
					new feyaSoft.home.common.AddContactWin;
				},
				minWidth : 90,
				iconCls : "addItem",
				scope : this
			});
	this.saveBtn = new Ext.Button({
				minWidth : 75,
				text : feyaSoft.lang.common.save,
				tooltip : "Save the result to the system",
				iconCls : "save",
				handler : this.doSaveFn,
				scope : this
			});
	feyaSoft.home.common.share.AddUser.superclass.constructor.call(this, {
				region : "east",
				width : 350,
				split : true,
				baseCls : "x-plain",
				labelWidth : 65,
				bodyStyle : "padding: 20px 10px 10px 10px",
				url : this.componentClass + "/create",
				items : [this.contactName, this.permission, this.allowInvite,
						this.note, emailNotify],
				buttons : [this.addMoreBtn, this.saveBtn]
			});
};
Ext.extend(feyaSoft.home.common.share.AddUser, Ext.form.FormPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.permission.setValue("1");
			},
			onUserSelectFn : function(obj) {
				var name = null;
				var existedUser = false;
				var existedUsers = this.ownerCt.existedUsers();
				for (var i = 0; i < existedUsers.length; i++) {
					if (obj.value == existedUsers[i].data.shareUserId) {
						existedUser = true;
						name = existedUsers[i].data.shareUser;
					}
				}
				if (existedUser) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.file.itemShared + ": " + name);
					this.contactName.setValue(null);
				}
			},
			onPermissionSelectFn : function(obj) {
				if (obj && obj.value == 2) {
					this.allowInvite.setDisabled(false);
				} else {
					this.allowInvite.setDisabled(true);
					this.allowInvite.setValue(false);
				}
			},
			doSaveFn : function() {
				if (this.form.isValid()) {
					this.form.submit({
								params : {
									fileId : this.fileId
								},
								waitMsg : "In processing",
								failure : function(form, action) {
									Ext.MessageBox.alert("Error Message",
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.ownerCt.reloadList();
										this.contactName.reset();
									} else {
										Ext.MessageBox.alert("Error Message",
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert("Errors",
							"Please fix the errors noted.");
				}
			}
		});
// 55号 87
Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.GroupSharePanel = function(config) {
	this.listGroups = new feyaSoft.home.common.share.ListGroups(config);
	this.addGroup = new feyaSoft.home.common.share.AddGroup(config);
	feyaSoft.home.common.share.GroupSharePanel.superclass.constructor.call(
			this, {
				title : feyaSoft.lang.file.groupAccess,
				border : false,
				layout : "border",
				items : [this.listGroups, this.addGroup]
			});
};
Ext.extend(feyaSoft.home.common.share.GroupSharePanel, Ext.Panel, {
			initLoad : function(config) {
				this.listGroups.initLoad(config);
				this.addGroup.initLoad(config);
			},
			existedGroups : function() {
				return this.listGroups.existedGroups();
			},
			reloadListGroup : function() {
				this.listGroups.reload();
			}
		});
feyaSoft.home.common.share.ListGroups = function(config) {
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "locationFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "locationPhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "locationPhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "locationBookmark";
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	function viewEditPermission(val) {
		var result = val;
		if (val == 1) {
			result = feyaSoft.lang.common.canView;
		} else if (val == 2) {
			result = feyaSoft.lang.common.canEdit;
		} else if (val == 7) {
			result = feyaSoft.lang.common.canEdit;
		}
		return result;
	}
	var shareCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.name,
				width : 200,
				dataIndex : "name"
			}, {
				header : feyaSoft.lang.common.permission,
				width : 100,
				dataIndex : "permission",
				scope : this,
				renderer : viewEditPermission
			}, {
				header : feyaSoft.lang.common.date,
				width : 150,
				dataIndex : "shareDate",
				renderer : Ext.util.Common.formatDateHour
			}]);
	this.shareStore = new Ext.data.JsonStore({
				url : this.componentClass + "/sharedGroups",
				remoteSort : true,
				fields : []
			});
	this.deleteBtn = new Ext.Button({
		text : feyaSoft.lang.common.remove,
		tooltip : "Highlight the item and click this button to remove this shared user",
		iconCls : "delete",
		disabled : true,
		handler : this.deleteItem,
		scope : this
	});
	this.changePermit = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.changePermission,
		tooltip : "Highlight the item and click this button to change this shared user permission",
		iconCls : "editItem",
		handler : this.changeItem,
		scope : this
	});
	feyaSoft.home.common.share.ListGroups.superclass.constructor.call(this, {
				region : "center",
				store : this.shareStore,
				cm : shareCM,
				sm : selectBoxModel,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : feyaSoft.lang.common.loadingData
				},
				tbar : [this.deleteBtn, this.changePermit],
				autoScroll : true
			});
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.share.ListGroups, Ext.grid.GridPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.shareStore.baseParams = {
					fileId : this.fileId
				};
				this.shareStore.load();
			},
			existedGroups : function() {
				return this.shareStore.data.items;
			},
			onSelectionChangeFn : function(sm) {
				if (0 < sm.getSelections().length) {
					this.deleteBtn.enable();
					if (this.componentClass == "locationPhoto"
							|| this.componentClass == "locationPhotoFolder") {
						this.changePermit.disable();
					} else {
						this.changePermit.enable();
					}
				} else {
					this.deleteBtn.disable();
					this.changePermit.disable();
				}
			},
			deleteItem : function() {
				var record = this.getSelectionModel().getSelected();
				if (record == null || record.data == null
						|| record.data.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				var deleteInfo = feyaSoft.lang.common.confirmDelete + ": "
						+ record.data.name;
				Ext.MessageBox.confirm(deleteInfo,
						feyaSoft.lang.common.confirmDeleteDesc, function(btn) {
							if (btn == "yes") {
								Ext.Ajax.request({
											url : this.componentClass
													+ "/remove",
											params : {
												groupId : record.data.id,
												fileId : this.fileId
											},
											method : "GET",
											success : function(result, request) {
												var jsonData = Ext.util.JSON
														.decode(result.responseText);
												if (jsonData.success == "true") {
													this.reload();
													this.myOwnerCt.reload();
												} else {
													Ext.MessageBox
															.alert(
																	feyaSoft.lang.common.error,
																	jsonData.info);
												}
											},
											failure : function(result, request) {
											},
											scope : this
										});
							}
						}, this);
			},
			changeItem : function() {
				var record = this.getSelectionModel().getSelected();
				if (record == null || record.data == null
						|| record.data.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				var changedPermit = feyaSoft.lang.common.canView;
				if (record.data.permission == 1) {
					changedPermit = feyaSoft.lang.common.canEdit;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirm,
							msg : feyaSoft.lang.common.confirmChangePermit
									+ changedPermit,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onChangeConfirm,
							scope : this,
							record : record
						});
			},
			onChangeConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/changePermit",
								params : {
									groupId : options.record.data.id,
									fileId : this.fileId
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			reload : function() {
				this.shareStore.reload();
			},
			disableDelete : function() {
				Ext.getCmp("delete-share-file-action").setDisabled(true);
			}
		});
feyaSoft.home.common.share.AddGroup = function(config) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "locationFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "locationPhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "locationPhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "locationBookmark";
	}
	var groupStore = new Ext.data.JsonStore({
				url : "locationUser/list",
				remoteSort : true,
				fields : []
			});
	groupStore.setDefaultSort("name", "ASC");
	this.group = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.group.group,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				minChars : 2,
				triggerAction : "all",
				store : groupStore,
				displayField : "name",
				hiddenName : "groupId",
				valueField : "id",
				emptyText : feyaSoft.lang.common.pleaseSelectOne,
				loadingText : feyaSoft.lang.common.loadingData,
				pageSize : 15,
				anchor : "93%",
				listeners : {
					select : this.onGroupSelectFn,
					scope : this
				}
			});
	var permissionStore = [["1", feyaSoft.lang.common.canView],
			["2", feyaSoft.lang.common.canEdit]];
	if (config.componentClass == "myPhotoShare"
			|| config.componentClass == "myPhotoFolderShare") {
		permissionStore = [["1", feyaSoft.lang.common.canView]];
	}
	var permission_data = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : permissionStore
			});
	this.permission = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.permission,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				triggerAction : "all",
				store : permission_data,
				displayField : "name",
				hiddenName : "permission",
				mode : "local",
				valueField : "id",
				anchor : "93%"
			});
	this.note = new Ext.form.HtmlEditor({
				hideLabel : true,
				name : "note",
				height : 150,
				anchor : "93%"
			});
	this.posting = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.groupShareMsg,
				name : "doPosting",
				checked : true
			});
	this.saveBtn = new Ext.Button({
				minWidth : 75,
				text : feyaSoft.lang.common.save,
				tooltip : "Save the result to the system",
				iconCls : "save",
				handler : this.doSaveFn,
				scope : this
			});
	feyaSoft.home.common.share.AddGroup.superclass.constructor.call(this, {
				region : "east",
				width : 350,
				split : true,
				baseCls : "x-plain",
				labelWidth : 70,
				bodyStyle : "padding: 20px 10px 10px 10px",
				url : this.componentClass + "/createUpdate",
				items : [this.group, this.permission, this.note, this.posting],
				buttons : [this.saveBtn]
			});
};
Ext.extend(feyaSoft.home.common.share.AddGroup, Ext.form.FormPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.permission.setValue("1");
			},
			onGroupSelectFn : function(obj) {
				var allowPosting = true;
				var name = null;
				var items = obj.store.data.items;
				for (var i = 0; i < items.length; i++) {
					if (obj.value == items[i].data.id) {
						allowPosting = items[i].data.allowPosting;
						name = items[i].data.name;
					}
				}
				if (!allowPosting) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.group.notAllowPosting + ": " + name);
					this.group.setValue(null);
				}
				var existedGroup = false;
				var existedGroups = this.ownerCt.existedGroups();
				for (var i = 0; i < existedGroups.length; i++) {
					if (obj.value == existedGroups[i].data.id) {
						existedGroup = true;
						name = existedGroups[i].data.name;
					}
				}
				if (existedGroup) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.group.groupShared + ": " + name);
					this.group.setValue(null);
				}
			},
			doSaveFn : function() {
				if (this.form.isValid()) {
					this.form.submit({
								params : {
									fileId : this.fileId
								},
								waitMsg : "In processing",
								failure : function(form, action) {
									Ext.MessageBox.alert("Error Message",
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.ownerCt.reloadListGroup();
										this.group.reset();
									} else {
										Ext.MessageBox.alert(
												feyaSoft.lang.common.error,
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							"Please fix the errors noted.");
				}
			}
		});
// 56号 88
Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.ContactSharePanel = function(config) {
	this.myOwnerCt = config.myOwnerCt;
	this.listContact = new feyaSoft.home.common.share.ListContacts(config);
	this.addContact = new feyaSoft.home.common.share.AddContact(config);
	feyaSoft.home.common.share.ContactSharePanel.superclass.constructor.call(
			this, {
				title : feyaSoft.lang.file.contactAccess,
				border : false,
				layout : "border",
				items : [this.listContact, this.addContact]
			});
};
Ext.extend(feyaSoft.home.common.share.ContactSharePanel, Ext.Panel, {
			initLoad : function(config) {
				this.listContact.initLoad(config);
				this.addContact.initLoad(config);
			},
			existedItems : function() {
				return this.listContact.existedItem();
			},
			reloadList : function() {
				this.listContact.reload();
			}
		});
feyaSoft.home.common.share.ListContacts = function(config) {
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "contactShareFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "contactSharePhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "contactSharePhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "contactShareBookmark";
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	function viewEditPermission(val) {
		var result = val;
		if (val == 1) {
			result = feyaSoft.lang.common.canView;
		} else if (val == 2) {
			result = feyaSoft.lang.common.canEdit;
		} else if (val == 7) {
			result = feyaSoft.lang.common.canEdit;
		}
		return result;
	}
	var shareCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.contactCategory,
				width : 200,
				dataIndex : "shareCategory"
			}, {
				header : feyaSoft.lang.common.permission,
				width : 90,
				dataIndex : "permission",
				scope : this,
				renderer : viewEditPermission
			}, {
				header : feyaSoft.lang.common.date,
				width : 140,
				dataIndex : "updateDate",
				renderer : Ext.util.Common.formatDateHour
			}]);
	this.shareStore = new Ext.data.JsonStore({
				url : this.componentClass + "/list",
				remoteSort : true,
				fields : []
			});
	this.deleteBtn = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.remove,
		id : "delete-contact-file-action",
		tooltip : "Highlight the item and click this button to remove this item",
		iconCls : "delete",
		handler : function() {
			var record = this.getSelectionModel().getSelected();
			if (record) {
				this.deleteItem(record);
			}
		},
		scope : this
	});
	this.changePermit = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.changePermission,
		tooltip : "Highlight the item and click this button to change this shared user permission",
		iconCls : "editItem",
		handler : function() {
			var record = this.getSelectionModel().getSelected();
			if (record) {
				this.changeItem(record);
			}
		},
		scope : this
	});
	feyaSoft.home.common.share.ListContacts.superclass.constructor.call(this, {
				region : "center",
				border : true,
				store : this.shareStore,
				cm : shareCM,
				sm : selectBoxModel,
				width : 600,
				height : 300,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : feyaSoft.lang.common.loadingData
				},
				tbar : [this.deleteBtn, this.changePermit],
				autoScroll : true
			});
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.share.ListContacts, Ext.grid.GridPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.shareStore.baseParams = {
					fileId : this.fileId
				};
				this.shareStore.load();
			},
			existedItem : function() {
				return this.shareStore.data.items;
			},
			onSelectionChangeFn : function(sm) {
				if (0 < sm.getSelections().length) {
					this.deleteBtn.enable();
					if (this.componentClass == "contactSharePhotoFolder"
							|| this.componentClass == "contactSharePhoto") {
						this.changePermit.disable();
					} else {
						this.changePermit.enable();
					}
				} else {
					this.deleteBtn.disable();
					this.changePermit.disable();
				}
			},
			deleteItem : function(rd) {
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirmDelete,
							msg : feyaSoft.lang.common.confirmDeleteDesc,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onDeleteConfirm,
							scope : this,
							record : rd
						});
			},
			onDeleteConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/delete",
								params : {
									id : options.record.data.id
								},
								success : function(result, request) {
									this.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			changeItem : function(rd) {
				var changedPermit = feyaSoft.lang.common.canView;
				if (rd.data.permission == 1) {
					changedPermit = feyaSoft.lang.common.canEdit;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirm,
							msg : feyaSoft.lang.common.confirmChangePermit
									+ changedPermit,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onChangeConfirm,
							scope : this,
							record : rd
						});
			},
			onChangeConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/changePermit",
								params : {
									id : options.record.data.id
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			reload : function() {
				this.shareStore.reload();
			},
			disableDelete : function() {
				Ext.getCmp("delete-contact-file-action").setDisabled(true);
			}
		});
feyaSoft.home.common.share.AddContact = function(config, myOwnerCt) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "contactShareFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "contactSharePhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "contactSharePhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "contactShareBookmark";
	}
	var permissionStore = [["1", feyaSoft.lang.common.canView],
			["2", feyaSoft.lang.common.canEdit]];
	if (config.componentClass == "myPhotoShare"
			|| config.componentClass == "myPhotoFolderShare") {
		permissionStore = [["1", feyaSoft.lang.common.canView]];
	}
	var permission_data = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : permissionStore
			});
	var categoryStore = new Ext.data.JsonStore({
				url : "contactCategory/list",
				remoteSort : true,
				fields : []
			});
	categoryStore.setDefaultSort("name", "ASC");
	this.categoryName = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.category,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				minChars : 2,
				triggerAction : "all",
				store : categoryStore,
				displayField : "name",
				hiddenName : "categoryId",
				valueField : "id",
				emptyText : feyaSoft.lang.common.pleaseSelectOne,
				loadingText : feyaSoft.lang.common.loadingData,
				pageSize : 15,
				anchor : "93%",
				listeners : {
					select : this.onCategorySelectFn,
					scope : this
				}
			});
	this.permission = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.permission,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				triggerAction : "all",
				store : permission_data,
				displayField : "name",
				hiddenName : "permission",
				mode : "local",
				valueField : "id",
				allowBlank : false,
				anchor : "93%"
			});
	this.note = new Ext.form.HtmlEditor({
				hideLabel : true,
				name : "note",
				height : 150,
				anchor : "93%"
			});
	this.posting = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.contactShareMsg,
				name : "doPosting",
				checked : true
			});
	this.saveBtn = new Ext.Button({
				minWidth : 75,
				text : feyaSoft.lang.common.save,
				tooltip : "Save the result to the system",
				iconCls : "save",
				handler : this.doSaveFn,
				scope : this
			});
	feyaSoft.home.common.share.AddContact.superclass.constructor.call(this, {
				region : "east",
				width : 350,
				split : true,
				baseCls : "x-plain",
				labelWidth : 65,
				bodyStyle : "padding: 20px 10px 10px 10px",
				url : this.componentClass + "/createUpdate",
				items : [this.categoryName, this.permission, this.note,
						this.posting],
				buttons : [this.saveBtn]
			});
};
Ext.extend(feyaSoft.home.common.share.AddContact, Ext.form.FormPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.permission.setValue("1");
			},
			onCategorySelectFn : function(obj) {
				var name = null;
				var existedItem = false;
				var existedItems = this.ownerCt.existedItems();
				for (var i = 0; i < existedItems.length; i++) {
					if (obj.value == existedItems[i].data.categoryId) {
						existedItem = true;
						name = existedItems[i].data.shareCategory;
					}
				}
				if (existedItem) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.file.itemShared + ": " + name);
					this.categoryName.setValue(null);
				}
			},
			doSaveFn : function() {
				if (this.form.isValid()) {
					this.form.submit({
								params : {
									fileId : this.fileId
								},
								waitMsg : "In processing",
								failure : function(form, action) {
									Ext.MessageBox.alert("Error Message",
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.ownerCt.reloadList();
										this.categoryName.reset();
										this.note.reset();
									} else {
										Ext.MessageBox.alert("Error Message",
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert("Errors",
							"Please fix the errors noted.");
				}
			}
		});
// 57号 89
Ext.ns("feyaSoft.home.common.share");
feyaSoft.home.common.share.OrganizationSharePanel = function(config) {
	this.listOrganizations = new feyaSoft.home.common.share.ListOrganizations(config);
	this.addOrganization = new feyaSoft.home.common.share.AddOrganization(config);
	feyaSoft.home.common.share.OrganizationSharePanel.superclass.constructor
			.call(this, {
						title : feyaSoft.lang.file.organizationAccess,
						border : false,
						layout : "border",
						items : [this.listOrganizations, this.addOrganization]
					});
};
Ext.extend(feyaSoft.home.common.share.OrganizationSharePanel, Ext.Panel, {
			initLoad : function(config) {
				this.listOrganizations.initLoad(config);
				this.addOrganization.initLoad(config);
			},
			existedOrganizations : function() {
				return this.listOrganizations.existedOrganizations();
			},
			reloadListOrganization : function() {
				this.listOrganizations.reload();
			}
		});
feyaSoft.home.common.share.ListOrganizations = function(config) {
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "organizationFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "organizationPhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "organizationPhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "organizationBookmark";
	}
	var selectBoxModel = new Ext.grid.CheckboxSelectionModel({
				singleSelect : true
			});
	function viewEditPermission(val) {
		var result = val;
		if (val == 1) {
			result = feyaSoft.lang.common.canView;
		} else if (val == 2) {
			result = feyaSoft.lang.common.canEdit;
		} else if (val == 7) {
			result = feyaSoft.lang.common.canEdit;
		}
		return result;
	}
	var shareCM = new Ext.grid.ColumnModel([selectBoxModel, {
				id : "id",
				header : "Identify",
				dataIndex : "id",
				width : 100,
				hidden : true
			}, {
				header : feyaSoft.lang.common.name,
				width : 200,
				dataIndex : "name"
			}, {
				header : feyaSoft.lang.common.permission,
				width : 100,
				dataIndex : "permission",
				scope : this,
				renderer : viewEditPermission
			}, {
				header : feyaSoft.lang.common.date,
				width : 150,
				dataIndex : "shareDate",
				renderer : Ext.util.Common.formatDateHour
			}]);
	this.shareStore = new Ext.data.JsonStore({
				url : this.componentClass + "/sharedOrganizations",
				remoteSort : true,
				fields : []
			});
	this.deleteBtn = new Ext.Button({
		text : feyaSoft.lang.common.remove,
		tooltip : "Highlight the item and click this button to remove this shared user",
		iconCls : "delete",
		disabled : true,
		handler : function() {
			this.deleteItem();
		},
		scope : this
	});
	this.changePermit = new Ext.Button({
		disabled : true,
		text : feyaSoft.lang.common.changePermission,
		tooltip : "Highlight the item and click this button to change this shared organization permission",
		iconCls : "editItem",
		handler : this.changeItem,
		scope : this
	});
	feyaSoft.home.common.share.ListOrganizations.superclass.constructor.call(
			this, {
				region : "center",
				store : this.shareStore,
				cm : shareCM,
				sm : selectBoxModel,
				viewConfig : {
					forceFit : true
				},
				loadMask : {
					msg : feyaSoft.lang.common.loadingData
				},
				tbar : [this.deleteBtn, this.changePermit],
				autoScroll : true
			});
	selectBoxModel.on("selectionchange", this.onSelectionChangeFn, this);
};
Ext.extend(feyaSoft.home.common.share.ListOrganizations, Ext.grid.GridPanel, {
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.shareStore.baseParams = {
					fileId : this.fileId
				};
				this.shareStore.load();
			},
			existedOrganizations : function() {
				return this.shareStore.data.items;
			},
			onSelectionChangeFn : function(sm) {
				if (0 < sm.getSelections().length) {
					this.deleteBtn.enable();
					if (this.componentClass == "organizationPhotoFolder"
							|| this.componentClass == "organizationPhoto") {
						this.changePermit.disable();
					} else {
						this.changePermit.enable();
					}
				} else {
					this.deleteBtn.disable();
					this.changePermit.disable();
				}
			},
			deleteItem : function(rd) {
				var record = this.getSelectionModel().getSelected();
				if (record == null || record.data == null
						|| record.data.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				var deleteInfo = feyaSoft.lang.common.confirmDelete + ": "
						+ record.data.name;
				Ext.MessageBox.confirm(deleteInfo,
						feyaSoft.lang.common.confirmDeleteDesc, function(btn) {
							if (btn == "yes") {
								Ext.Ajax.request({
											url : this.componentClass
													+ "/remove",
											params : {
												organizationId : record.data.id,
												fileId : this.fileId
											},
											method : "GET",
											success : function(result, request) {
												var jsonData = Ext.util.JSON
														.decode(result.responseText);
												if (jsonData.success == "true") {
													this.reload();
													this.myOwnerCt.reload();
												} else {
													Ext.MessageBox
															.alert(
																	feyaSoft.lang.common.error,
																	jsonData.info);
												}
											},
											failure : function(result, request) {
											},
											scope : this
										});
							}
						}, this);
			},
			changeItem : function() {
				var record = this.getSelectionModel().getSelected();
				if (record == null || record.data == null
						|| record.data.id == null) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.common.pleaseSelectOne);
					return;
				}
				var changedPermit = feyaSoft.lang.common.canView;
				if (record.data.permission == 1) {
					changedPermit = feyaSoft.lang.common.canEdit;
				}
				Ext.Msg.show({
							title : feyaSoft.lang.common.confirm,
							msg : feyaSoft.lang.common.confirmChangePermit
									+ changedPermit,
							icon : Ext.Msg.QUESTION,
							buttons : Ext.Msg.YESNO,
							fn : this.onChangeConfirm,
							scope : this,
							record : record
						});
			},
			onChangeConfirm : function(button_id, text, options) {
				if (button_id == "yes") {
					Ext.Ajax.request({
								url : this.componentClass + "/changePermit",
								params : {
									organizationId : options.record.data.id,
									fileId : this.fileId
								},
								success : function(result, request) {
									this.reload();
									this.myOwnerCt.reload();
								},
								failure : function(result, request) {
									Ext.MessageBox.alert("Failed",
											"Internal Error, please try again");
								},
								scope : this
							});
				}
			},
			reload : function() {
				this.shareStore.reload();
			},
			disableDelete : function() {
				Ext.getCmp("delete-share-file-action").setDisabled(true);
			}
		});
feyaSoft.home.common.share.AddOrganization = function(config) {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = "side";
	this.fileId = null;
	this.myOwnerCt = null;
	this.componentClass = "organizationFile";
	if (config.componentClass == "myPhotoFolderShare") {
		this.componentClass = "organizationPhotoFolder";
	} else if (config.componentClass == "myPhotoShare") {
		this.componentClass = "organizationPhoto";
	} else if (config.componentClass == "browserBookmarkShare") {
		this.componentClass = "organizationBookmark";
	}
	var organizationStore = new Ext.data.JsonStore({
				url : "organizationUser/list",
				remoteSort : true,
				fields : []
			});
	organizationStore.setDefaultSort("name", "ASC");
	this.organization = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.organization.organization,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				minChars : 2,
				triggerAction : "all",
				store : organizationStore,
				displayField : "name",
				hiddenName : "itemId",
				valueField : "id",
				emptyText : feyaSoft.lang.common.pleaseSelectOne,
				loadingText : feyaSoft.lang.common.loadingData,
				pageSize : 15,
				anchor : "93%",
				listeners : {
					select : this.onOrganizationSelectFn,
					scope : this
				}
			});
	this.permissionStore = [["1", feyaSoft.lang.common.canView],
			["2", feyaSoft.lang.common.canEdit]];
	if (this.componentClass == "organizationPhotoFolder"
			|| this.componentClass == "organizationPhoto") {
		this.permissionStore = [["1", feyaSoft.lang.common.canView]];
	}
	this.permission_data = new Ext.data.SimpleStore({
				fields : ["id", "name"],
				data : this.permissionStore
			});
	this.permission = new Ext.form.ComboBox({
				fieldLabel : feyaSoft.lang.common.permission,
				forceSelection : true,
				allowBlank : false,
				typeAhead : true,
				triggerAction : "all",
				store : this.permission_data,
				displayField : "name",
				hiddenName : "permission",
				mode : "local",
				valueField : "id",
				anchor : "93%"
			});
	this.note = new Ext.form.HtmlEditor({
				hideLabel : true,
				name : "note",
				height : 150,
				anchor : "93%"
			});
	this.posting = new Ext.form.Checkbox({
				hideLabel : true,
				boxLabel : feyaSoft.lang.file.organizationShareMsg,
				name : "doPosting",
				checked : true
			});
	this.saveBtn = new Ext.Button({
				minWidth : 75,
				text : feyaSoft.lang.common.save,
				tooltip : "Save the result to the system",
				iconCls : "save",
				handler : this.doSaveFn,
				scope : this
			});
	feyaSoft.home.common.share.AddOrganization.superclass.constructor.call(
			this, {
				region : "east",
				width : 350,
				split : true,
				baseCls : "x-plain",
				labelWidth : 70,
				bodyStyle : "padding: 20px 10px 10px 10px",
				url : this.componentClass + "/createUpdate",
				items : [this.organization, this.permission, this.note,
						this.posting],
				buttons : [this.saveBtn]
			});
};
Ext.extend(feyaSoft.home.common.share.AddOrganization, Ext.form.FormPanel, {
			onOrgAfterRenderFn : function() {
				this.permission_data.on("load", function() {
							this.permission.setValue("1");
						}, this);
			},
			initLoad : function(config) {
				this.fileId = config.fileId;
				this.myOwnerCt = config.myOwnerCt;
				this.permission.setValue("1");
			},
			onOrganizationSelectFn : function(obj) {
				var allowPosting = true;
				var name = null;
				var items = obj.store.data.items;
				for (var i = 0; i < items.length; i++) {
					if (obj.value == items[i].data.id) {
						allowPosting = items[i].data.allowPosting;
						name = items[i].data.name;
					}
				}
				if (!allowPosting) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.group.notAllowPosting + ": " + name);
					this.organization.setValue(null);
				}
				var existedOrganization = false;
				var existedOrganizations = this.ownerCt.existedOrganizations();
				for (var i = 0; i < existedOrganizations.length; i++) {
					if (obj.value == existedOrganizations[i].data.id) {
						existedOrganization = true;
						name = existedOrganizations[i].data.name;
					}
				}
				if (existedOrganization) {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							feyaSoft.lang.group.groupShared + ": " + name);
					this.organization.setValue(null);
				}
			},
			doSaveFn : function() {
				if (this.form.isValid()) {
					this.form.submit({
								params : {
									fileId : this.fileId
								},
								waitMsg : "In processing",
								failure : function(form, action) {
									Ext.MessageBox.alert("Error Message",
											action.result.errorInfo);
								},
								success : function(form, action) {
									if (action.result.success == "true") {
										Ext.Message.msgStay(
												feyaSoft.lang.common.confirm,
												action.result.info, 2000);
										this.ownerCt.reloadListOrganization();
										this.organization.reset();
									} else {
										Ext.MessageBox.alert(
												feyaSoft.lang.common.error,
												action.result.info);
									}
								},
								scope : this
							});
				} else {
					Ext.MessageBox.alert(feyaSoft.lang.common.error,
							"Please fix the errors noted.");
				}
			}
		});

Ext.namespace("Ext.ux.designHelper");
Ext.ux.designHelper.selection = function(win, doc) {
	this.sel = null;
	this.range = null;
	this.sdRange = null;
	this.init = function() {
		var sel = win.getSelection ? win.getSelection() : doc.selection;
		var range;
		try {
			if (sel.rangeCount > 0) {
				range = sel.getRangeAt(0);
			} else {
				range = sel.createRange();
			}
		} catch (e) {
		}
		if (!range) {
			range = Ext.ux.designHelper.util.createRange(doc);
		}
		this.sel = sel;
		this.range = range;
		var startNode, startPos, endNode, endPos;
		if (Ext.isIE) {
			if (range.item) {
				var el = range.item(0);
				startNode = endNode = el;
				startPos = endPos = 0;
			} else {
				var getStartEnd = function(isStart) {
					var pointRange = range.duplicate();
					pointRange.collapse(isStart);
					var parentNode = pointRange.parentElement();
					var nodes = parentNode.childNodes;
					if (nodes.length == 0) {
						return {
							node : parentNode,
							pos : 0
						};
					}
					var startNode;
					var endElement;
					var startPos = 0;
					var isEnd = false;
					var testRange = range.duplicate();
					testRange.moveToElementText(parentNode);
					for (var i = 0, len = nodes.length; i < len; i++) {
						var node = nodes[i];
						var cmp = testRange.compareEndPoints("StartToStart",
								pointRange);
						if (cmp > 0) {
							isEnd = true;
						} else if (cmp == 0) {
							if (node.nodeType == 1) {
								var sdRange = new Ext.ux.designHelper.range(doc);
								sdRange.selectTextNode(node);
								return {
									node : sdRange.startNode,
									pos : 0
								};
							} else {
								return {
									node : node,
									pos : 0
								};
							}
						}
						if (node.nodeType == 1) {
							var nodeRange = range.duplicate();
							nodeRange.moveToElementText(node);
							testRange.setEndPoint("StartToEnd", nodeRange);
							if (isEnd) {
								startPos += nodeRange.text.length;
							} else {
								startPos = 0;
							}
						} else if (node.nodeType == 3) {
							testRange.moveStart("character",
									node.nodeValue.length);
							startPos += node.nodeValue.length;
						}
						if (!isEnd) {
							startNode = node;
						}
					}
					if (!isEnd && startNode.nodeType == 1) {
						var startNode = parentNode.lastChild;
						return {
							node : startNode,
							pos : startNode.nodeType == 1
									? 1
									: startNode.nodeValue.length
						};
					}
					testRange = range.duplicate();
					testRange.moveToElementText(parentNode);
					testRange.setEndPoint("StartToEnd", pointRange);
					startPos -= testRange.text.replace(/\r\n|\n|\r/g, "").length;
					return {
						node : startNode,
						pos : startPos
					};
				};
				var start = getStartEnd(true);
				var end = getStartEnd(false);
				startNode = start.node;
				startPos = start.pos;
				endNode = end.node;
				endPos = end.pos;
			}
		} else {
			startNode = range.startContainer;
			startPos = range.startOffset;
			endNode = range.endContainer;
			endPos = range.endOffset;
			if (startNode.nodeType == 1
					&& typeof startNode.childNodes[startPos] != "undefined") {
				startNode = startNode.childNodes[startPos];
				startPos = 0;
			}
			if (endNode.nodeType == 1) {
				endPos = endPos == 0 ? 1 : endPos;
				if (typeof endNode.childNodes[endPos - 1] != "undefined") {
					endNode = endNode.childNodes[endPos - 1];
					endPos = endNode.nodeType == 1
							? 0
							: endNode.nodeValue.length;
				}
			}
			if (startNode.nodeType == 1 && endNode.nodeType == 3 && endPos == 0
					&& endNode.previousSibling) {
				var node = endNode.previousSibling;
				while (node) {
					if (node === startNode) {
						endNode = startNode;
						break;
					}
					if (node.childNodes.length != 1) {
						break;
					}
					node = node.childNodes[0];
				}
			}
		}
		var sdRange = new Ext.ux.designHelper.range(doc);
		sdRange.setTextStart(startNode, startPos);
		sdRange.setTextEnd(endNode, endPos);
		this.sdRange = sdRange;
	};
	this.init();
	this.addRange = function(sdRange) {
		this.sdRange = sdRange;
		if (Ext.isIE) {
			var getEndRange = function(isStart) {
				var range = Ext.ux.designHelper.util.createRange(doc);
				var node = isStart ? sdRange.startNode : sdRange.endNode;
				if (node.nodeType == 1) {
					range.moveToElementText(node);
					range.collapse(isStart);
				} else if (node.nodeType == 3) {
					range = Ext.ux.designHelper.util.getNodeStartRange(doc,
							node);
					var pos = isStart ? sdRange.startPos : sdRange.endPos;
					range.moveStart("character", pos);
				}
				return range;
			};
			if (!this.range.item) {
				var node = sdRange.startNode;
				if (node == sdRange.endNode
						&& Ext.ux.designHelper.util.getNodeType(node) == 1
						&& Ext.ux.designHelper.util.getNodeTextLength(node) == 0) {
					var temp = doc.createTextNode(" ");
					node.appendChild(temp);
					this.range.moveToElementText(node);
					this.range.collapse(false);
					this.range.select();
					node.removeChild(temp);
				} else {
					this.range.setEndPoint("StartToStart", getEndRange(true));
					this.range.setEndPoint("EndToStart", getEndRange(false));
					this.range.select();
				}
			}
		} else {
			var getNodePos = function(node) {
				var pos = 0;
				while (node) {
					node = node.previousSibling;
					pos++;
				}
				return --pos;
			};
			var range = new Ext.ux.designHelper.range(doc);
			range.setTextStart(sdRange.startNode, sdRange.startPos);
			range.setTextEnd(sdRange.endNode, sdRange.endPos);
			if (Ext.ux.designHelper.util.getNodeType(range.startNode) == 88) {
				this.range.setStart(range.startNode.parentNode,
						getNodePos(range.startNode));
			} else {
				this.range.setStart(range.startNode, range.startPos);
			}
			if (Ext.ux.designHelper.util.getNodeType(range.endNode) == 88) {
				this.range.setEnd(range.endNode.parentNode,
						getNodePos(range.endNode) + 1);
			} else {
				this.range.setEnd(range.endNode, range.endPos);
			}
			this.sel.removeAllRanges();
			this.sel.addRange(this.range);
		}
	};
	this.focus = function() {
		if (Ext.isIE && this.range != null) {
			this.range.select();
		}
	};
};
Ext.ux.designHelper.range = function(doc) {
	this.startNode = null;
	this.startPos = null;
	this.endNode = null;
	this.endPos = null;
	this.getParentElement = function() {
		var scanParent = function(node, func) {
			while (node
					&& (!node.tagName || node.tagName.toLowerCase() != "body")) {
				node = node.parentNode;
				if (func(node)) {
					return;
				}
			}
		};
		var nodeList = [];
		scanParent(this.startNode, function(node) {
					nodeList.push(node);
				});
		var parentNode;
		scanParent(this.endNode, function(node) {
					if (Ext.ux.designHelper.util.inArray(node, nodeList)) {
						parentNode = node;
						return true;
					}
				});
		return parentNode ? parentNode : doc.body;
	};
	this.getNodeList = function() {
		var self = this;
		var parentNode = this.getParentElement();
		var nodeList = [];
		var isStarted = false;
		if (parentNode == self.startNode) {
			isStarted = true;
		}
		if (isStarted) {
			nodeList.push(parentNode);
		}
		Ext.ux.designHelper.util.eachNode(parentNode, function(node) {
					if (node == self.startNode) {
						isStarted = true;
					}
					var range = new Ext.ux.designHelper.range(doc);
					range.selectTextNode(node);
					if (range.comparePoints("START_TO_END", self) >= 0) {
						return false;
					}
					if (isStarted) {
						nodeList.push(node);
					}
					return true;
				});
		return nodeList;
	};
	this.comparePoints = function(how, range) {
		var compareNodes = function(nodeA, posA, nodeB, posB) {
			var cmp;
			if (Ext.isIE) {
				var getStartRange = function(node, pos, isStart) {
					var range = Ext.ux.designHelper.util.createRange(doc);
					var type = Ext.ux.designHelper.util.getNodeType(node);
					if (type == 1) {
						range.moveToElementText(node);
						range.collapse(isStart);
					} else if (type == 3) {
						range = Ext.ux.designHelper.util.getNodeStartRange(doc,
								node);
						range.moveStart("character", pos);
						range.collapse(true);
					}
					return range;
				};
				var rangeA, rangeB;
				if (how == "START_TO_START" || how == "START_TO_END") {
					rangeA = getStartRange(nodeA, posA, true);
				} else {
					rangeA = getStartRange(nodeA, posA, false);
				}
				if (how == "START_TO_START" || how == "END_TO_START") {
					rangeB = getStartRange(nodeB, posB, true);
				} else {
					rangeB = getStartRange(nodeB, posB, false);
				}
				return rangeA.compareEndPoints("StartToStart", rangeB);
			} else {
				var rangeA = Ext.ux.designHelper.util.createRange(doc);
				rangeA.selectNode(nodeA);
				if (how == "START_TO_START" || how == "START_TO_END") {
					rangeA.collapse(true);
				} else {
					rangeA.collapse(false);
				}
				var rangeB = Ext.ux.designHelper.util.createRange(doc);
				rangeB.selectNode(nodeB);
				if (how == "START_TO_START" || how == "END_TO_START") {
					rangeB.collapse(true);
				} else {
					rangeB.collapse(false);
				}
				if (rangeA.compareBoundaryPoints(Range.START_TO_START, rangeB) > 0) {
					cmp = 1;
				} else if (rangeA.compareBoundaryPoints(Range.START_TO_START,
						rangeB) == 0) {
					if (posA > posB) {
						cmp = 1;
					} else if (posA == posB) {
						cmp = 0;
					} else {
						cmp = -1;
					}
				} else {
					cmp = -1;
				}
			}
			return cmp;
		};
		if (how == "START_TO_START") {
			return compareNodes(this.startNode, this.startPos, range.startNode,
					range.startPos);
		}
		if (how == "START_TO_END") {
			return compareNodes(this.startNode, this.startPos, range.endNode,
					range.endPos);
		}
		if (how == "END_TO_START") {
			return compareNodes(this.endNode, this.endPos, range.startNode,
					range.startPos);
		}
		if (how == "END_TO_END") {
			return compareNodes(this.endNode, this.endPos, range.endNode,
					range.endPos);
		}
	};
	this.setTextStart = function(node, pos) {
		var textNode = node;
		Ext.ux.designHelper.util.eachNode(node, function(n) {
					if (Ext.ux.designHelper.util.getNodeType(n) == 3
							&& n.nodeValue.length > 0
							|| Ext.ux.designHelper.util.getNodeType(n) == 88) {
						textNode = n;
						pos = 0;
						return false;
					}
					return true;
				});
		this.setStart(textNode, pos);
	};
	this.setStart = function(node, pos) {
		this.startNode = node;
		this.startPos = pos;
		if (this.endNode === null) {
			this.endNode = node;
			this.endPos = pos;
		}
	};
	this.setTextEnd = function(node, pos) {
		var textNode = node;
		Ext.ux.designHelper.util.eachNode(node, function(n) {
					if (Ext.ux.designHelper.util.getNodeType(n) == 3
							&& n.nodeValue.length > 0
							|| Ext.ux.designHelper.util.getNodeType(n) == 88) {
						textNode = n;
						pos = Ext.ux.designHelper.util.getNodeType(n) == 3
								? n.nodeValue.length
								: 0;
					}
					return true;
				});
		this.setEnd(textNode, pos);
	};
	this.setEnd = function(node, pos) {
		this.endNode = node;
		this.endPos = pos;
		if (this.startNode === null) {
			this.startNode = node;
			this.startPos = pos;
		}
	};
	this.selectNode = function(node) {
		this.setStart(node, 0);
		this.setEnd(node, node.nodeType == 1 ? 0 : node.nodeValue.length);
	};
	this.selectTextNode = function(node) {
		this.setTextStart(node, 0);
		this.setTextEnd(node, node.nodeType == 1 ? 0 : node.nodeValue.length);
	};
	this.extractContents = function(isDelete) {
		isDelete = isDelete === false ? false : true;
		var thisRange = this;
		var startNode = this.startNode;
		var startPos = this.startPos;
		var endNode = this.endNode;
		var endPos = this.endPos;
		var extractTextNode = function(node, startPos, endPos) {
			var length = node.nodeValue.length;
			var cloneNode = node.cloneNode(true);
			var centerNode = cloneNode.splitText(startPos);
			centerNode.splitText(endPos - startPos);
			if (isDelete) {
				var center = node;
				if (startPos > 0) {
					center = node.splitText(startPos);
				}
				if (endPos < length) {
					center.splitText(endPos - startPos);
				}
				center.parentNode.removeChild(center);
			}
			return centerNode;
		};
		var isStarted = false;
		var isEnd = false;
		var extractNodes = function(parent, frag) {
			if (Ext.ux.designHelper.util.getNodeType(parent) != 1) {
				return true;
			}
			var node = parent.firstChild;
			while (node) {
				if (node == startNode) {
					isStarted = true;
				}
				if (node == endNode) {
					isEnd = true;
				}
				var nextNode = node.nextSibling;
				var type = node.nodeType;
				if (type == 1) {
					var range = new Ext.ux.designHelper.range(doc);
					range.selectNode(node);
					if (isStarted
							&& range.comparePoints("END_TO_END", thisRange) < 0) {
						var cloneNode = node.cloneNode(true);
						frag.appendChild(cloneNode);
						if (isDelete) {
							node.parentNode.removeChild(node);
						}
					} else {
						var childFlag = node.cloneNode(false);
						frag.appendChild(childFlag);
						if (!extractNodes(node, childFlag)) {
							return false;
						}
					}
				}
				node = nextNode;
				if (isEnd) {
					return false;
				}
			}
			return true;
		};
		var parentNode = this.getParentElement();
		var docFrag = parentNode.cloneNode(false);
		extractNodes(parentNode, docFrag);
		return docFrag;
	};
};
Ext.ux.designHelper.cmd = function(iframeWin, iframeDoc) {
	this.doc = iframeDoc;
	this.sdSel = new Ext.ux.designHelper.selection(iframeWin, iframeDoc);
	this.sdRange = this.sdSel.sdRange;
	this.mergeAttributes = function(el, attr) {
		for (var i = 0, len = attr.length; i < len; i++) {
			Ext.ux.designHelper.util.each(attr[i], function(key, value) {
						if (key.charAt(0) == ".") {
							var jsKey = Ext.ux.designHelper.util.getJsKey(key
									.substr(1));
							eval("el.style." + jsKey + " = value;");
						} else {
							el.setAttribute(key, value);
						}
					});
		}
		return el;
	};
	this.wrapTextNode = function(node, startPos, endPos, element, attributes) {
		var length = node.nodeValue.length;
		var isFull = startPos == 0 && endPos == length;
		var range = new Ext.ux.designHelper.range(this.doc);
		range.selectTextNode(node.parentNode);
		if (isFull && node.parentNode.tagName == element.tagName
				&& range.comparePoints("END_TO_END", this.sdRange) <= 0
				&& range.comparePoints("START_TO_START", this.sdRange) >= 0) {
			this.mergeAttributes(node.parentNode, attributes);
			return node;
		} else {
			var el = element.cloneNode(true);
			if (isFull) {
				var cloneNode = node.cloneNode(true);
				el.appendChild(cloneNode);
				node.parentNode.replaceChild(el, node);
				return cloneNode;
			} else {
				var centerNode = node;
				if (startPos < endPos) {
					if (startPos > 0) {
						centerNode = node.splitText(startPos);
					}
					if (endPos < length) {
						centerNode.splitText(endPos - startPos);
					}
					var cloneNode = centerNode.cloneNode(true);
					el.appendChild(cloneNode);
					centerNode.parentNode.replaceChild(el, centerNode);
					return cloneNode;
				} else {
					if (startPos < length) {
						centerNode = node.splitText(startPos);
						centerNode.parentNode.insertBefore(el, centerNode);
					} else {
						if (centerNode.nextSibling) {
							centerNode.parentNode.insertBefore(el,
									centerNode.nextSibling);
						} else {
							centerNode.parentNode.appendChild(el);
						}
					}
					return el;
				}
			}
		}
	};
	this.wrap = function(tagName, attributes) {
		var self = this;
		this.sdSel.focus();
		var element = this.doc.createElement(tagName);
		this.mergeAttributes(element, attributes);
		var sdRange = this.sdRange;
		var startNode = sdRange.startNode;
		var startPos = sdRange.startPos;
		var endNode = sdRange.endNode;
		var endPos = sdRange.endPos;
		var parentNode = sdRange.getParentElement();
		var isStarted = false;
		Ext.ux.designHelper.util.eachNode(parentNode, function(node) {
					if (node == startNode) {
						isStarted = true;
					}
					if (node.nodeType == 1) {
						if (node == startNode && node == endNode) {
							if (Ext.ux.designHelper.util.inArray(node.tagName
											.toLowerCase(), ["br", "hr", "img",
											"area", "col", "embed", "input",
											"param"])) {
								if (startPos > 0) {
									node.parentNode.appendChild(element);
								} else {
									node.parentNode.insertBefore(element, node);
								}
							} else {
								node.appendChild(element);
							}
							sdRange.selectNode(element);
							return false;
						} else if (node == startNode) {
							sdRange.setStart(node, 0);
						} else if (node == endNode) {
							sdRange.setEnd(node, 0);
							return false;
						}
					} else if (node.nodeType == 3) {
						if (isStarted) {
							if (node == startNode && node == endNode) {
								var rangeNode = self.wrapTextNode(node,
										startPos, endPos, element, attributes);
								sdRange.selectNode(rangeNode);
								return false;
							} else if (node == startNode) {
								var rangeNode = self.wrapTextNode(node,
										startPos, node.nodeValue.length,
										element, attributes);
								sdRange.setStart(rangeNode, 0);
							} else if (node == endNode) {
								var rangeNode = self.wrapTextNode(node, 0,
										endPos, element, attributes);
								sdRange.setEnd(rangeNode,
										rangeNode.nodeType == 1
												? 0
												: rangeNode.nodeValue.length);
								return false;
							} else {
								self.wrapTextNode(node, 0,
										node.nodeValue.length, element,
										attributes);
							}
						}
					}
					return true;
				});
		this.sdSel.addRange(sdRange);
	};
	this.execLink = function(url, txt, stype) {
		var startNode = this.sdRange.startNode;
		var endNode = this.sdRange.endNode;
		var node = this.sdRange.getParentElement();
		while (node) {
			if (node.tagName.toLowerCase() == "a"
					|| node.tagName.toLowerCase() == "body") {
				break;
			}
			node = node.parentNode;
		}
		node = node.parentNode;
		if (startNode.nodeType == 1 && startNode === endNode) {
			return;
		}
		if (Ext.isIE) {
			var html = "<a href=\"#\"";
			html += "target=\"_blank\" ext:qtip=" + url + " tmpurl=\""
					+ (stype == "web" ? url : "mailto:" + url) + "\">" + txt
					+ "</a>";
			this.sdSel.range.pasteHTML(html);
		} else {
			var arr = node.getElementsByTagName("a");
			for (var i = 0, l = arr.length; i < l; i++) {
				if (arr[i].href.match(/\/?_tmp_link_url_$/)) {
					arr[i].href = "#";
				}
			}
			this.doc.execCommand("createlink", false, "_tmp_link_url_");
			for (var i = 0, l = arr.length; i < l; i++) {
				if (arr[i].href.match(/\/?_tmp_link_url_$/)) {
					arr[i].setAttribute("tmpurl", stype == "web"
									? url
									: "mailto:" + url);
					arr[i].setAttribute("target", "_blank");
				}
			}
		}
	};
	this.execWordLink = function(url, txt, stype) {
		var startNode = this.sdRange.startNode;
		var endNode = this.sdRange.endNode;
		var node = this.sdRange.getParentElement();
		while (node) {
			if (node.tagName.toLowerCase() == "a"
					|| node.tagName.toLowerCase() == "body") {
				break;
			}
			node = node.parentNode;
		}
		node = node.parentNode;
		if (startNode.nodeType == 1 && startNode === endNode) {
			return;
		}
		if (Ext.isIE) {
			var html = "<a href=\"" + (stype == "web" ? url : "mailto:" + url)
					+ "\"";
			html += "target=\"_blank\" ext:qtip=" + url + ">" + txt + "</a>";
			this.sdSel.range.pasteHTML(html);
		} else {
			var arr = node.getElementsByTagName("a");
			for (var i = 0, l = arr.length; i < l; i++) {
				if (arr[i].href.match(/\/?_tmp_link_url_$/)) {
					arr[i].href = "#";
				}
			}
			this.doc.execCommand("createlink", false, "_tmp_link_url_");
			for (var i = 0, l = arr.length; i < l; i++) {
				if (arr[i].href.match(/\/?_tmp_link_url_$/)) {
					arr[i].href = url;
				}
			}
		}
	};
	this.fontShadow = function() {
		var startNode = this.sdRange.startNode;
		var endNode = this.sdRange.endNode;
		var node = this.sdRange.getParentElement();
		node = node.parentNode;
		if (Ext.isIE) {
			var html = "<span  style=\"position:absolute;filter: Shadow(color=\"#666666\", Direction=135, Strength=2);\">"
					+ this.sdSel.range.text + "</span>";
			this.sdSel.range.pasteHTML(html);
		}
	};
};
Ext.ux.designHelper.util = {
	inArray : function(str, arr) {
		for (var i = 0; i < arr.length; i++) {
			if (str == arr[i]) {
				return true;
			}
		}
		return false;
	},
	getJsKey : function(key) {
		var arr = key.split("-");
		key = "";
		for (var i = 0, len = arr.length; i < len; i++) {
			key += i > 0
					? arr[i].charAt(0).toUpperCase() + arr[i].substr(1)
					: arr[i];
		}
		return key;
	},
	createRange : function(doc) {
		return doc.createRange ? doc.createRange() : doc.body.createTextRange();
	},
	getNodeType : function(node) {
		return node.nodeType == 1
				&& Ext.ux.designHelper.util.inArray(node.tagName.toLowerCase(),
						["br", "hr", "img", "area", "col", "embed", "input",
								"param"]) ? 88 : node.nodeType;
	},
	getNodeTextLength : function(node) {
		var type = Ext.ux.designHelper.util.getNodeType(node);
		if (type == 1) {
			var html = node.innerHTML;
			return html.replace(/<.*?>/gi, "").length;
		} else if (type == 3) {
			return node.nodeValue.length;
		}
	},
	getNodeStartRange : function(doc, node) {
		var range = Ext.ux.designHelper.util.createRange(doc);
		var type = node.nodeType;
		if (type == 1) {
			range.moveToElementText(node);
			return range;
		} else if (type == 3) {
			var offset = 0;
			var sibling = node.previousSibling;
			while (sibling) {
				if (sibling.nodeType == 1) {
					var nodeRange = Ext.ux.designHelper.util.createRange(doc);
					nodeRange.moveToElementText(sibling);
					range.setEndPoint("StartToEnd", nodeRange);
					range.moveStart("character", offset);
					return range;
				} else if (sibling.nodeType == 3) {
					offset += sibling.nodeValue.length;
				}
				sibling = sibling.previousSibling;
			}
			range.moveToElementText(node.parentNode);
			range.moveStart("character", offset);
			return range;
		}
	},
	each : function(obj, func) {
		for (var key in obj) {
			if (obj.hasOwnProperty(key)) {
				func(key, obj[key]);
			}
		}
	},
	eachNode : function(node, func) {
		var walkNodes = function(parent) {
			if (Ext.ux.designHelper.util.getNodeType(parent) != 1) {
				return true;
			}
			var n = parent.firstChild;
			while (n) {
				var next = n.nextSibling;
				if (!func(n)) {
					return false;
				}
				if (!walkNodes(n)) {
					return false;
				}
				n = next;
			}
			return true;
		};
		walkNodes(node);
	},
	getStyleOfSelection : function(win, doc, style) {
		var selection = new Ext.ux.designHelper.selection(win, doc);
		var range = selection.sdRange;
		if (range) {
			var pn = range.startNode;
			if (pn) {
				if (pn.tagName) {
					pn = Ext.fly(pn);
				} else {
					pn = Ext.fly(range.getParentElement());
				}
				if (pn) {
					return pn.getStyle(style);
				}
			}
		}
	},
	getStartNodeOfSelection : function(win, doc) {
		var selection = new Ext.ux.designHelper.selection(win, doc);
		var range = selection.sdRange;
		if (range) {
			var pn = range.startNode;
			if (pn) {
				if (pn.tagName) {
					pn = Ext.fly(pn);
				} else {
					pn = Ext.fly(range.getParentElement());
				}
				if (pn) {
					return pn;
				}
			}
		}
	},
	getBackgroundColor : function(El, doc) {
		doc = doc || document;
		var bg = El.getStyle("background-color");
		while (("transparent" == bg || !bg) && El && El.dom.parentNode != doc) {
			El = Ext.fly(El.dom.parentNode);
			if (El) {
				bg = El.getStyle("background-color");
			}
		}
		return bg;
	},
	hasClass : function(El, doc, className) {
		doc = doc || document;
		while (El && El.dom.parentNode != doc) {
			if (El.hasClass(className)) {
				return El;
			}
			El = Ext.fly(El.dom.parentNode);
		}
	},
	selectRange : function(doc, win) {
		var selection = new Ext.ux.designHelper.selection(win, doc);
		var r = selection.sdRange;
		doc.execCommand("SelectAll", false, null);
		selection.addRange(r);
	},
	getSelectedText : function(win, doc) {
		if (win.getSelection) {
			return doc.getSelection().toString();
		} else if (doc.getSelection) {
			return doc.getSelection();
		} else if (doc.selection) {
			return doc.selection.createRange().text;
		}
	}
};
Ext.ns("Ext.ux");
Ext.ux.MessagePusher = Ext.extend(Ext.Window, {
	iconCls : "cubedrive_icon",
	title : "News",
	width : 380,
	height : 180,
	closable : true,
	plain : false,
	resizable : false,
	animCollapse : false,
	manager : {
		register : Ext.emptyFn,
		unregister : Ext.emptyFn,
		bringToFront : Ext.emptyFn,
		sendToBack : Ext.emptyFn
	},
	initComponent : function() {
		this.layout = "fit";
		var p;
		if (Ext.isIE) {
			p = 96;
		} else {
			p = 100;
		}
		this.tpl = new Ext.XTemplate("<div style=\"font-size:14px;text-align:center;color:rgb(21,66,139);padding:5px;\"><b>Cubedrive Notification</b></div>"
				+ "<tpl for=\".\">"
				+ "<div style=\"background-color:{color};padding:5px;\">"
				+ "<div><table width=\""
				+ p
				+ "%\"><tbody><tr>"
				+ "<td style=\"font-size:11px;\"><b>{title}</b></td>"
				+ "</tr></tbody></table></div>"
				+ "{content}"
				+ "</div>"
				+ "</tpl>");
		this.tpl.compile();
		this.viewpanel = new Ext.Panel({
					autoScroll : true,
					bodyStyle : "background:transparent;",
					border : false
				});
		this.items = [this.viewpanel];
		this.tools = [{
					id : "down",
					handler : this.onDownFn,
					scope : this
				}, {
					id : "up",
					hidden : true,
					handler : this.onUpFn,
					scope : this
				}];
		Ext.ux.MessagePusher.superclass.initComponent.call(this);
		this.on("show", this.onShowFn, this);
		this.on("render", function() {
					this.body.on("click", function(e) {
								var tgEl = Ext.get(e.getTarget());
								if (tgEl.hasClass("invitation")) {
									var vid = e.getTarget()
											.getAttribute("name");
									this.onAcceptInvitation(vid);
								} else if (tgEl.hasClass("reject")) {
									var vid = e.getTarget()
											.getAttribute("name");
									this.onRejectInvitation(vid);
								} else if (tgEl.hasClass("groupInvitation")) {
									var vid = e.getTarget()
											.getAttribute("name");
									this.onGroupInvitation(vid);
								} else if (tgEl.hasClass("groupReject")) {
									var vid = e.getTarget()
											.getAttribute("name");
									this.onGroupReject(vid);
								}
							}, this);
				});
	},
	changeTool : function(id) {
		if ("up" == id) {
			this.tools.down.hide();
			this.tools.up.show();
		} else {
			this.tools.up.hide();
			this.tools.down.show();
		}
	},
	onUpFn : function(event, tEl, p) {
		this.changeTool("down");
		var ch = this.getSize().height;
		this.expand();
		var eh = this.getSize().height;
		var pos = this.getPosition(true);
		this.setPosition(pos[0], pos[1] + ch - eh);
	},
	onDownFn : function(event, tEl, p) {
		this.changeTool("up");
		var eh = this.getSize().height;
		this.collapse();
		var ch = this.getSize().height;
		var pos = this.getPosition(true);
		this.setPosition(pos[0], pos[1] + eh - ch);
	},
	popup : function(messages) {
		this.messages = messages;
		if (this.isVisible()) {
			if (this.collapsed) {
				this.onUpFn();
			}
			this.onShowFn();
		} else {
			this.show();
			if (true != this.mini) {
				this.expand();
				this.changeTool("down");
			} else {
				this.collapse();
				this.changeTool("up");
			}
			var taskBar = Ext.get("ux-taskbar");
			if (taskBar) {
				this.el.alignTo(taskBar, "br-tr", [-1, -1]);
			} else {
				this.el.alignTo(Ext.getBody(), "br-br", [-10, -10]);
			}
			this.el.slideIn("b", {
						duration : 0.7
					});
		}
	},
	onShowFn : function(p) {
		var len = this.messages.length;
		if (0 < len) {
			this.setTitle("Notification(" + len + ")");
			var html = this.tpl.apply(this.messages);
			this.viewpanel.body.update(html);
			this.viewpanel.body.highlight("#c3daf9", {
						block : true
					});
		} else {
			this.hide();
		}
	},
	onAcceptInvitation : function(id) {
		Ext.Ajax.request({
					url : "contactPending/accept",
					params : {
						id : id
					},
					method : "GET",
					success : function(result, request) {
						var jsonData = Ext.util.JSON
								.decode(result.responseText);
						if (jsonData.success == "true") {
							Ext.Message.msgStay(feyaSoft.lang.common.confirm,
									jsonData.info, 2000);
						} else {
							Ext.MessageBox.alert(feyaSoft.lang.common.error,
									jsonData.info);
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert(feyaSoft.lang.common.error,
								"Internal Error");
					},
					scope : this
				});
	},
	onRejectInvitation : function(id) {
		Ext.Ajax.request({
					url : "contactPending/reject",
					params : {
						id : id
					},
					method : "GET",
					success : function(result, request) {
						var jsonData = Ext.util.JSON
								.decode(result.responseText);
						if (jsonData.success == "true") {
							Ext.Message.msgStay(feyaSoft.lang.common.confirm,
									jsonData.info, 2000);
						} else {
							Ext.MessageBox.alert(feyaSoft.lang.common.error,
									jsonData.info);
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert(feyaSoft.lang.common.error,
								"Internal Error");
					},
					scope : this
				});
	},
	onGroupInvitation : function(id) {
		Ext.Ajax.request({
					url : "locationUserPending/accept",
					params : {
						id : id
					},
					method : "GET",
					success : function(result, request) {
						var jsonData = Ext.util.JSON
								.decode(result.responseText);
						if (jsonData.success == "true") {
							Ext.Message.msgStay(feyaSoft.lang.common.confirm,
									jsonData.info, 2000);
						} else {
							Ext.MessageBox.alert(feyaSoft.lang.common.error,
									jsonData.info);
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert(feyaSoft.lang.common.error,
								"Internal Error");
					},
					scope : this
				});
	},
	onGroupReject : function(id) {
		Ext.Ajax.request({
					url : "locationUserPending/reject",
					params : {
						id : id
					},
					method : "GET",
					success : function(result, request) {
						var jsonData = Ext.util.JSON
								.decode(result.responseText);
						if (jsonData.success == "true") {
							Ext.Message.msgStay(feyaSoft.lang.common.confirm,
									jsonData.info, 2000);
						} else {
							Ext.MessageBox.alert(feyaSoft.lang.common.error,
									jsonData.info);
						}
					},
					failure : function(result, request) {
						Ext.MessageBox.alert(feyaSoft.lang.common.error,
								"Internal Error");
					},
					scope : this
				});
	}
});
Ext.ux.SlidingPager = Ext.extend(Object, {
			init : function(pbar) {
				Ext.each(pbar.items.getRange(2, 6), function(c) {
							c.hide();
						});
				var slider = new Ext.Slider({
							width : 114,
							minValue : 1,
							maxValue : 1,
							plugins : new Ext.ux.SliderTip({
										getText : function(s) {
											return String
													.format(
															"Page <b>{0}</b> of <b>{1}</b>",
															s.value, s.maxValue);
										}
									}),
							listeners : {
								changecomplete : function(s, v) {
									pbar.changePage(v);
								}
							}
						});
				pbar.insert(5, slider);
				pbar.on({
							change : function(pb, data) {
								slider.maxValue = data.pages;
								slider.setValue(data.activePage);
							},
							beforedestroy : function() {
								slider.destroy();
							}
						});
			}
		});
// 哈哈

