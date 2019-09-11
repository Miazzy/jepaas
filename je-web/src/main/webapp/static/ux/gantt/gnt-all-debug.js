/*

Ext Gantt 2.2.21
Copyright(c) 2009-2014 Bryntum AB
http://bryntum.com/contact
http://bryntum.com/license

*/

Ext.define("Sch.locale.Locale", {
	l10n : null,
	legacyMode : true,
	localeName : null,
	namespaceId : null,
	constructor : function () {
		if (!Sch.locale.Active) {
			Sch.locale.Active = {};
			this.bindRequire()
		}
		var b = this.self.getName().split(".");
		var a = this.localeName = b.pop();
		this.namespaceId = b.join(".");
		var c = Sch.locale.Active[this.namespaceId];
		if (!(a == "En" && c && c.localeName != "En")) {
			this.apply()
		}
	},
	bindRequire : function () {
		var a = Ext.ClassManager.triggerCreated;
		Ext.ClassManager.triggerCreated = function (d) {
			a.apply(this, arguments);
			var c = Ext.ClassManager.get(d);
			for (var b in Sch.locale.Active) {
				Sch.locale.Active[b].apply(c)
			}
		}
	},
	apply : function (a) {
		if (this.l10n) {
			var h = this,
			f,
			e;
			var g = this.self.getName();
			var d = function (l, k) {
				k = k || Ext.ClassManager.get(l);
				if (k && (k.activeLocaleId !== g)) {
					var i = h.l10n[l];
					if (typeof i === "function") {
						i(l)
					} else {
						if (k.singleton) {
							k.l10n = Ext.apply(k.l10n || {}, i)
						} else {
							Ext.override(k, {
								l10n : i
							})
						}
					}
					if (h.legacyMode) {
						var n;
						if (k.prototype) {
							n = k.prototype
						} else {
							if (k.singleton) {
								n = k
							}
						}
						if (n) {
							if (n.legacyHolderProp) {
								if (!n[n.legacyHolderProp]) {
									n[n.legacyHolderProp] = {}

								}
								n = n[n.legacyHolderProp]
							}
							for (var m in i) {
								if (typeof n[m] !== "function") {
									n[m] = i[m]
								}
							}
						}
					}
					k.activeLocaleId = g;
					if (k.onLocalized) {
						k.onLocalized()
					}
				}
			};
			if (a) {
				if (!Ext.isArray(a)) {
					a = [a]
				}
				var b,
				j;
				for (f = 0, e = a.length; f < e; f++) {
					if (Ext.isObject(a[f])) {
						if (a[f].singleton) {
							j = a[f];
							b = Ext.getClassName(Ext.getClass(j))
						} else {
							j = Ext.getClass(a[f]);
							b = Ext.getClassName(j)
						}
					} else {
						j = null;
						b = "string" === typeof a[f] ? a[f] : Ext.getClassName(a[f])
					}
					if (b && b in this.l10n) {
						d(b, j)
					}
				}
			} else {
				Sch.locale.Active[this.namespaceId] = this;
				for (var c in this.l10n) {
					d(c)
				}
			}
		}
	}
});
Ext.define("Sch.locale.En", {
	extend : "Sch.locale.Locale",
	singleton : true,
	l10n : {
		"Sch.util.Date" : {
			unitNames : {
				YEAR : {
					single : "year",
					plural : "years",
					abbrev : "yr"
				},
				QUARTER : {
					single : "quarter",
					plural : "quarters",
					abbrev : "q"
				},
				MONTH : {
					single : "month",
					plural : "months",
					abbrev : "mon"
				},
				WEEK : {
					single : "week",
					plural : "weeks",
					abbrev : "w"
				},
				DAY : {
					single : "day",
					plural : "days",
					abbrev : "d"
				},
				HOUR : {
					single : "hour",
					plural : "hours",
					abbrev : "h"
				},
				MINUTE : {
					single : "minute",
					plural : "minutes",
					abbrev : "min"
				},
				SECOND : {
					single : "second",
					plural : "seconds",
					abbrev : "s"
				},
				MILLI : {
					single : "ms",
					plural : "ms",
					abbrev : "ms"
				}
			}
		},
		"Sch.view.SchedulerGridView" : {
			loadingText : "Loading events..."
		},
		"Sch.plugin.CurrentTimeLine" : {
			tooltipText : "Current time"
		},
		"Sch.plugin.EventEditor" : {
			saveText : "Save",
			deleteText : "Delete",
			cancelText : "Cancel"
		},
		"Sch.plugin.SimpleEditor" : {
			newEventText : "New booking..."
		},
		"Sch.widget.ExportDialog" : {
			generalError : "An error occured, try again.",
			title : "Export Settings",
			formatFieldLabel : "Paper format",
			orientationFieldLabel : "Orientation",
			rangeFieldLabel : "Export range",
			showHeaderLabel : "Add page number",
			orientationPortraitText : "Portrait",
			orientationLandscapeText : "Landscape",
			completeViewText : "Complete schedule",
			currentViewText : "Current view",
			dateRangeText : "Date range",
			dateRangeFromText : "Export from",
			pickerText : "Resize column/rows to desired value",
			dateRangeToText : "Export to",
			exportButtonText : "Export",
			cancelButtonText : "Cancel",
			progressBarText : "Exporting...",
			exportToSingleLabel : "Export as single page",
			adjustCols : "Adjust column width",
			adjustColsAndRows : "Adjust column width and row height",
			specifyDateRange : "Specify date range"
		},
		"Sch.preset.Manager" : function () {
			var b = Sch.preset.Manager,
			a = b.getPreset("hourAndDay");
			if (a) {
				a.displayDateFormat = "G:i";
				a.headerConfig.middle.dateFormat = "G:i";
				a.headerConfig.top.dateFormat = "D d/m"
			}
			a = b.getPreset("secondAndMinute");
			if (a) {
				a.displayDateFormat = "g:i:s";
				a.headerConfig.top.dateFormat = "D, d g:iA"
			}
			a = b.getPreset("dayAndWeek");
			if (a) {
				a.displayDateFormat = "m/d h:i A";
				a.headerConfig.middle.dateFormat = "D d M"
			}
			a = b.getPreset("weekAndDay");
			if (a) {
				a.displayDateFormat = "m/d";
				a.headerConfig.bottom.dateFormat = "d M";
				a.headerConfig.middle.dateFormat = "Y F d"
			}
			a = b.getPreset("weekAndMonth");
			if (a) {
				a.displayDateFormat = "m/d/Y";
				a.headerConfig.middle.dateFormat = "m/d";
				a.headerConfig.top.dateFormat = "m/d/Y"
			}
			a = b.getPreset("weekAndDayLetter");
			if (a) {
				a.displayDateFormat = "m/d/Y";
				a.headerConfig.middle.dateFormat = "D d M Y"
			}
			a = b.getPreset("weekDateAndMonth");
			if (a) {
				a.displayDateFormat = "m/d/Y";
				a.headerConfig.middle.dateFormat = "d";
				a.headerConfig.top.dateFormat = "Y F"
			}
			a = b.getPreset("monthAndYear");
			if (a) {
				a.displayDateFormat = "m/d/Y";
				a.headerConfig.middle.dateFormat = "M Y";
				a.headerConfig.top.dateFormat = "Y"
			}
			a = b.getPreset("year");
			if (a) {
				a.displayDateFormat = "m/d/Y";
				a.headerConfig.middle.dateFormat = "Y"
			}
			a = b.getPreset("manyYears");
			if (a) {
				a.displayDateFormat = "m/d/Y";
				a.headerConfig.middle.dateFormat = "Y"
			}
		}
	}
});
Ext.define("Sch.util.Patch", {
	target : null,
	minVersion : null,
	maxVersion : null,
	reportUrl : null,
	description : null,
	applyFn : null,
	ieOnly : false,
	overrides : null,
	onClassExtended : function (a, b) {
		if (Sch.disableOverrides) {
			return
		}
		if (b.ieOnly && !Ext.isIE) {
			return
		}
		if ((!b.minVersion || Ext.versions.extjs.equals(b.minVersion) || Ext.versions.extjs.isGreaterThan(b.minVersion)) && (!b.maxVersion || Ext.versions.extjs.equals(b.maxVersion) || Ext.versions.extjs.isLessThan(b.maxVersion))) {
			if (b.applyFn) {
				b.applyFn()
			} else {
				Ext.ClassManager.get(b.target).override(b.overrides)
			}
		}
	}
});
Ext.define("Sch.patches.ColumnResize", {
	override : "Sch.panel.TimelineGridPanel",
	afterRender : function () {
		this.callParent(arguments);
		var a = this.lockedGrid.headerCt.findPlugin("gridheaderresizer");
		if (a) {
			a.getConstrainRegion = function () {
				var d = this,
				b = d.dragHd.el,
				c;
				if (d.headerCt.forceFit) {
					c = d.dragHd.nextNode("gridcolumn:not([hidden]):not([isGroupHeader])");
					if (!d.headerInSameGrid(c)) {
						c = null
					}
				}
				return d.adjustConstrainRegion(Ext.util.Region.getRegion(b), 0, d.headerCt.forceFit ? (c ? c.getWidth() - d.minColWidth : 0) : d.maxColWidth - b.getWidth(), 0, d.minColWidth)
			}
		}
	}
});
Ext.define("Sch.patches.ColumnResizeTree", {
	override : "Sch.panel.TimelineTreePanel",
	afterRender : function () {
		this.callParent(arguments);
		var a = this.lockedGrid.headerCt.findPlugin("gridheaderresizer");
		if (a) {
			a.getConstrainRegion = function () {
				var d = this,
				b = d.dragHd.el,
				c;
				if (d.headerCt.forceFit) {
					c = d.dragHd.nextNode("gridcolumn:not([hidden]):not([isGroupHeader])");
					if (!d.headerInSameGrid(c)) {
						c = null
					}
				}
				return d.adjustConstrainRegion(Ext.util.Region.getRegion(b), 0, d.headerCt.forceFit ? (c ? c.getWidth() - d.minColWidth : 0) : d.maxColWidth - b.getWidth(), 0, d.minColWidth)
			}
		}
	}
});
Ext.define("Sch.mixin.Localizable", {
	requires : ["Sch.locale.En"],
	legacyMode : true,
	activeLocaleId : "",
	l10n : null,
	isLocaleApplied : function () {
		var b = (this.singleton && this.activeLocaleId) || this.self.activeLocaleId;
		if (!b) {
			return false
		}
		for (var a in Sch.locale.Active) {
			if (b === Sch.locale.Active[a].self.getName()) {
				return true
			}
		}
		return false
	},
	applyLocale : function () {
		for (var a in Sch.locale.Active) {
			Sch.locale.Active[a].apply(this.singleton ? this : this.self.getName())
		}
	},
	L : function () {
		return this.localize.apply(this, arguments)
	},
	localize : function (b, d, g) {
		if (!this.isLocaleApplied() && !g) {
			this.applyLocale()
		}
		if (this.hasOwnProperty("l10n") && this.l10n.hasOwnProperty(b) && "function" != typeof this.l10n[b]) {
			return this.l10n[b]
		}
		var c = this.self && this.self.prototype;
		if (this.legacyMode) {
			var a = d || this.legacyHolderProp;
			var h = a ? this[a] : this;
			if (h && h.hasOwnProperty(b) && "function" != typeof h[b]) {
				return h[b]
			}
			if (c) {
				var e = a ? c[a] : c;
				if (e && e.hasOwnProperty(b) && "function" != typeof e[b]) {
					return e[b]
				}
			}
		}
		var i = c.l10n[b];
		if (i === null || i === undefined) {
			var f = c && c.superclass;
			if (f && f.localize) {
				i = f.localize(b, d, g)
			}
			if (i === null || i === undefined) {
				throw "Cannot find locale: " + b + " [" + this.self.getName() + "]"
			}
		}
		return i
	}
});
Ext.define("Sch.util.Date", {
	requires : "Ext.Date",
	mixins : ["Sch.mixin.Localizable"],
	singleton : true,
	stripEscapeRe : /(\\.)/g,
	hourInfoRe : /([gGhHisucUOPZ]|MS)/,
	unitHash : null,
	unitsByName : {},
	constructor : function () {
		var a = Ext.Date;
		var c = this.unitHash = {
			MILLI : a.MILLI,
			SECOND : a.SECOND,
			MINUTE : a.MINUTE,
			HOUR : a.HOUR,
			DAY : a.DAY,
			WEEK : "w",
			MONTH : a.MONTH,
			QUARTER : "q",
			YEAR : a.YEAR
		};
		Ext.apply(this, c);
		var b = this;
		this.units = [b.MILLI, b.SECOND, b.MINUTE, b.HOUR, b.DAY, b.WEEK, b.MONTH, b.QUARTER, b.YEAR]
	},
	onLocalized : function () {
		this.setUnitNames(this.L("unitNames"))
	},
	setUnitNames : function (f, b) {
		var e = this.unitsByName = {};
		this.l10n.unitNames = f;
		this._unitNames = Ext.apply({}, f);
		var c = this.unitHash;
		for (var a in c) {
			if (c.hasOwnProperty(a)) {
				var d = c[a];
				this._unitNames[d] = this._unitNames[a];
				e[a] = d;
				e[d] = d
			}
		}
	},
	betweenLesser : function (b, d, a) {
		var c = b.getTime();
		return d.getTime() <= c && c < a.getTime()
	},
	constrain : function (b, c, a) {
		return this.min(this.max(b, c), a)
	},
	compareUnits : function (c, b) {
		var a = Ext.Array.indexOf(this.units, c),
		d = Ext.Array.indexOf(this.units, b);
		return a > d ? 1 : (a < d ? -1 : 0)
	},
	isUnitGreater : function (b, a) {
		return this.compareUnits(b, a) > 0
	},
	copyTimeValues : function (b, a) {
		b.setHours(a.getHours());
		b.setMinutes(a.getMinutes());
		b.setSeconds(a.getSeconds());
		b.setMilliseconds(a.getMilliseconds())
	},
	add : function (b, c, e) {
		var f = Ext.Date.clone(b);
		if (!c || e === 0) {
			return f
		}
		switch (c.toLowerCase()) {
		case this.MILLI:
			f = new Date(b.getTime() + e);
			break;
		case this.SECOND:
			f = new Date(b.getTime() + (e * 1000));
			break;
		case this.MINUTE:
			f = new Date(b.getTime() + (e * 60000));
			break;
		case this.HOUR:
			f = new Date(b.getTime() + (e * 3600000));
			break;
		case this.DAY:
			f.setDate(b.getDate() + e);
			break;
		case this.WEEK:
			f.setDate(b.getDate() + e * 7);
			break;
		case this.MONTH:
			var a = b.getDate();
			if (a > 28) {
				a = Math.min(a, Ext.Date.getLastDateOfMonth(this.add(Ext.Date.getFirstDateOfMonth(b), this.MONTH, e)).getDate())
			}
			f.setDate(a);
			f.setMonth(f.getMonth() + e);
			break;
		case this.QUARTER:
			f = this.add(b, this.MONTH, e * 3);
			break;
		case this.YEAR:
			f.setFullYear(b.getFullYear() + e);
			break
		}
		return f
	},
	getUnitDurationInMs : function (a) {
		return this.add(new Date(1, 0, 1), a, 1) - new Date(1, 0, 1)
	},
	getMeasuringUnit : function (a) {
		if (a === this.WEEK) {
			return this.DAY
		}
		return a
	},
	getDurationInUnit : function (e, a, c, d) {
		var b;
		switch (c) {
		case this.YEAR:
			b = this.getDurationInYears(e, a);
			break;
		case this.QUARTER:
			b = this.getDurationInMonths(e, a) / 3;
			break;
		case this.MONTH:
			b = this.getDurationInMonths(e, a);
			break;
		case this.WEEK:
			b = this.getDurationInDays(e, a) / 7;
			break;
		case this.DAY:
			b = this.getDurationInDays(e, a);
			break;
		case this.HOUR:
			b = this.getDurationInHours(e, a);
			break;
		case this.MINUTE:
			b = this.getDurationInMinutes(e, a);
			break;
		case this.SECOND:
			b = this.getDurationInSeconds(e, a);
			break;
		case this.MILLI:
			b = this.getDurationInMilliseconds(e, a);
			break
		}
		return d ? b : Math.round(b)
	},
	getUnitToBaseUnitRatio : function (b, a) {
		if (b === a) {
			return 1
		}
		switch (b) {
		case this.YEAR:
			switch (a) {
			case this.QUARTER:
				return 1 / 4;
			case this.MONTH:
				return 1 / 12
			}
			break;
		case this.QUARTER:
			switch (a) {
			case this.YEAR:
				return 4;
			case this.MONTH:
				return 1 / 3
			}
			break;
		case this.MONTH:
			switch (a) {
			case this.YEAR:
				return 12;
			case this.QUARTER:
				return 3
			}
			break;
		case this.WEEK:
			switch (a) {
			case this.DAY:
				return 1 / 7;
			case this.HOUR:
				return 1 / 168
			}
			break;
		case this.DAY:
			switch (a) {
			case this.WEEK:
				return 7;
			case this.HOUR:
				return 1 / 24;
			case this.MINUTE:
				return 1 / 1440
			}
			break;
		case this.HOUR:
			switch (a) {
			case this.DAY:
				return 24;
			case this.MINUTE:
				return 1 / 60
			}
			break;
		case this.MINUTE:
			switch (a) {
			case this.HOUR:
				return 60;
			case this.SECOND:
				return 1 / 60;
			case this.MILLI:
				return 1 / 60000
			}
			break;
		case this.SECOND:
			switch (a) {
			case this.MILLI:
				return 1 / 1000
			}
			break;
		case this.MILLI:
			switch (a) {
			case this.SECOND:
				return 1000
			}
			break
		}
		return -1
	},
	getDurationInMilliseconds : function (b, a) {
		return (a - b)
	},
	getDurationInSeconds : function (b, a) {
		return (a - b) / 1000
	},
	getDurationInMinutes : function (b, a) {
		return (a - b) / 60000
	},
	getDurationInHours : function (b, a) {
		return (a - b) / 3600000
	},
	getDurationInDays : function (c, b) {
		var a = c.getTimezoneOffset() - b.getTimezoneOffset();
		return (b - c + a * 60 * 1000) / 86400000
	},
	getDurationInBusinessDays : function (g, b) {
		var c = Math.round((b - g) / 86400000),
		a = 0,
		f;
		for (var e = 0; e < c; e++) {
			f = this.add(g, this.DAY, e).getDay();
			if (f !== 6 && f !== 0) {
				a++
			}
		}
		return a
	},
	getDurationInMonths : function (b, a) {
		return ((a.getFullYear() - b.getFullYear()) * 12) + (a.getMonth() - b.getMonth())
	},
	getDurationInYears : function (b, a) {
		return this.getDurationInMonths(b, a) / 12
	},
	min : function (b, a) {
		return b < a ? b : a
	},
	max : function (b, a) {
		return b > a ? b : a
	},
	intersectSpans : function (c, d, b, a) {
		return this.betweenLesser(c, b, a) || this.betweenLesser(b, c, d)
	},
	getNameOfUnit : function (a) {
		a = this.getUnitByName(a);
		switch (a.toLowerCase()) {
		case this.YEAR:
			return "YEAR";
		case this.QUARTER:
			return "QUARTER";
		case this.MONTH:
			return "MONTH";
		case this.WEEK:
			return "WEEK";
		case this.DAY:
			return "DAY";
		case this.HOUR:
			return "HOUR";
		case this.MINUTE:
			return "MINUTE";
		case this.SECOND:
			return "SECOND";
		case this.MILLI:
			return "MILLI"
		}
		throw "Incorrect UnitName"
	},
	getReadableNameOfUnit : function (b, a) {
		if (!this.isLocaleApplied()) {
			this.applyLocale()
		}
		return this._unitNames[b][a ? "plural" : "single"]
	},
	getShortNameOfUnit : function (a) {
		if (!this.isLocaleApplied()) {
			this.applyLocale()
		}
		return this._unitNames[a].abbrev
	},
	getUnitByName : function (a) {
		if (!this.isLocaleApplied()) {
			this.applyLocale()
		}
		if (!this.unitsByName[a]) {
			Ext.Error.raise("Unknown unit name: " + a)
		}
		return this.unitsByName[a]
	},
	getNext : function (c, g, a, f) {
		var e = Ext.Date.clone(c);
		f = arguments.length < 4 ? 1 : f;
		a = a == null ? 1 : a;
		switch (g) {
		case this.MILLI:
			e = this.add(c, g, a);
			break;
		case this.SECOND:
			e = this.add(c, g, a);
			if (e.getMilliseconds() > 0) {
				e.setMilliseconds(0)
			}
			break;
		case this.MINUTE:
			e = this.add(c, g, a);
			if (e.getSeconds() > 0) {
				e.setSeconds(0)
			}
			if (e.getMilliseconds() > 0) {
				e.setMilliseconds(0)
			}
			break;
		case this.HOUR:
			e = this.add(c, g, a);
			if (e.getMinutes() > 0) {
				e.setMinutes(0)
			}
			if (e.getSeconds() > 0) {
				e.setSeconds(0)
			}
			if (e.getMilliseconds() > 0) {
				e.setMilliseconds(0)
			}
			break;
		case this.DAY:
			var d = c.getHours() === 23 && this.add(e, this.HOUR, 1).getHours() === 1;
			if (d) {
				e = this.add(e, this.DAY, 2);
				Ext.Date.clearTime(e);
				return e
			}
			Ext.Date.clearTime(e);
			e = this.add(e, this.DAY, a);
			break;
		case this.WEEK:
			Ext.Date.clearTime(e);
			var b = e.getDay();
			e = this.add(e, this.DAY, f - b + 7 * (a - (f <= b ? 0 : 1)));
			if (e.getDay() !== f) {
				e = this.add(e, this.HOUR, 1)
			} else {
				Ext.Date.clearTime(e)
			}
			break;
		case this.MONTH:
			e = this.add(e, this.MONTH, a);
			e.setDate(1);
			Ext.Date.clearTime(e);
			break;
		case this.QUARTER:
			e = this.add(e, this.MONTH, ((a - 1) * 3) + (3 - (e.getMonth() % 3)));
			Ext.Date.clearTime(e);
			e.setDate(1);
			break;
		case this.YEAR:
			e = new Date(e.getFullYear() + a, 0, 1);
			break;
		default:
			throw "Invalid date unit"
		}
		return e
	},
	getNumberOfMsFromTheStartOfDay : function (a) {
		return a - Ext.Date.clearTime(a, true) || 86400000
	},
	getNumberOfMsTillTheEndOfDay : function (a) {
		return this.getStartOfNextDay(a, true) - a
	},
	getStartOfNextDay : function (b, f, e) {
		var d = this.add(e ? b : Ext.Date.clearTime(b, f), this.DAY, 1);
		if (d.getDate() == b.getDate()) {
			var c = this.add(Ext.Date.clearTime(b, f), this.DAY, 2).getTimezoneOffset();
			var a = b.getTimezoneOffset();
			d = this.add(d, this.MINUTE, a - c)
		}
		return d
	},
	getEndOfPreviousDay : function (b, c) {
		var a = c ? b : Ext.Date.clearTime(b, true);
		if (a - b) {
			return a
		} else {
			return this.add(a, this.DAY, -1)
		}
	},
	timeSpanContains : function (c, b, d, a) {
		return (d - c) >= 0 && (b - a) >= 0
	}
});
Ext.define("Sch.util.DragTracker", {
	extend : "Ext.dd.DragTracker",
	xStep : 1,
	yStep : 1,
	constructor : function () {
		this.callParent(arguments);
		this.on("dragstart", function () {
			var a = this.el;
			a.on("scroll", this.onMouseMove, this);
			this.on("dragend", function () {
				a.un("scroll", this.onMouseMove, this)
			}, this, {
				single : true
			})
		})
	},
	setXStep : function (a) {
		this.xStep = a
	},
	startScroll : null,
	setYStep : function (a) {
		this.yStep = a
	},
	getRegion : function () {
		var j = this.startXY,
		f = this.el.getScroll(),
		l = this.getXY(),
		c = l[0],
		b = l[1],
		h = f.left - this.startScroll.left,
		m = f.top - this.startScroll.top,
		i = j[0] - h,
		g = j[1] - m,
		e = Math.min(i, c),
		d = Math.min(g, b),
		a = Math.abs(i - c),
		k = Math.abs(g - b);
		return new Ext.util.Region(d, e + a, d + k, e)
	},
	onMouseDown : function (b, a) {
		this.callParent(arguments);
		this.lastXY = this.startXY;
		this.startScroll = this.el.getScroll()
	},
	onMouseMove : function (g, f) {
		if (this.active && g.type === "mousemove" && Ext.isIE9m && !g.browserEvent.button) {
			g.preventDefault();
			this.onMouseUp(g);
			return
		}
		g.preventDefault();
		var d = g.type === "scroll" ? this.lastXY : g.getXY(),
		b = this.startXY;
		if (!this.active) {
			if (Math.max(Math.abs(b[0] - d[0]), Math.abs(b[1] - d[1])) > this.tolerance) {
				this.triggerStart(g)
			} else {
				return
			}
		}
		var a = d[0],
		h = d[1];
		if (this.xStep > 1) {
			a -= this.startXY[0];
			a = Math.round(a / this.xStep) * this.xStep;
			a += this.startXY[0]
		}
		if (this.yStep > 1) {
			h -= this.startXY[1];
			h = Math.round(h / this.yStep) * this.yStep;
			h += this.startXY[1]
		}
		var c = this.xStep > 1 || this.yStep > 1;
		if (!c || a !== d[0] || h !== d[1]) {
			this.lastXY = [a, h];
			if (this.fireEvent("mousemove", this, g) === false) {
				this.onMouseUp(g)
			} else {
				this.onDrag(g);
				this.fireEvent("drag", this, g)
			}
		}
	}
});
Ext.define("Sch.util.ScrollManager", {
	singleton : true,
	vthresh : 25,
	hthresh : 25,
	increment : 100,
	frequency : 500,
	animate : true,
	animDuration : 200,
	activeEl : null,
	scrollElRegion : null,
	scrollProcess : {},
	pt : null,
	scrollWidth : null,
	scrollHeight : null,
	direction : "both",
	constructor : function () {
		this.doScroll = Ext.Function.bind(this.doScroll, this)
	},
	triggerRefresh : function () {
		if (this.activeEl) {
			this.refreshElRegion();
			this.clearScrollInterval();
			this.onMouseMove()
		}
	},
	doScroll : function () {
		var c = this.scrollProcess,
		d = c.el,
		b = c.dir[0],
		a = this.increment;
		if (b === "r") {
			a = Math.min(a, this.scrollWidth - this.activeEl.dom.scrollLeft - this.activeEl.dom.clientWidth)
		} else {
			if (b === "d") {
				a = Math.min(a, this.scrollHeight - this.activeEl.dom.scrollTop - this.activeEl.dom.clientHeight)
			}
		}
		d.scroll(b, Math.max(a, 0), {
			duration : this.animDuration,
			callback : this.triggerRefresh,
			scope : this
		})
	},
	clearScrollInterval : function () {
		var a = this.scrollProcess;
		if (a.id) {
			clearTimeout(a.id)
		}
		a.id = 0;
		a.el = null;
		a.dir = ""
	},
	isScrollAllowed : function (a) {
		switch (this.direction) {
		case "both":
			return true;
		case "horizontal":
			return a === "right" || a === "left";
		case "vertical":
			return a === "up" || a === "down";
		default:
			throw "Invalid direction: " + this.direction
		}
	},
	startScrollInterval : function (b, a) {
		if (!this.isScrollAllowed(a)) {
			return
		}
		if (Ext.versions.extjs.isLessThan("4.2.2")) {
			if (a[0] === "r") {
				a = "left"
			} else {
				if (a[0] === "l") {
					a = "right"
				}
			}
		}
		this.clearScrollInterval();
		this.scrollProcess.el = b;
		this.scrollProcess.dir = a;
		this.scrollProcess.id = setTimeout(this.doScroll, this.frequency)
	},
	onMouseMove : function (d) {
		var k = d ? d.getPoint() : this.pt,
		j = k.x,
		h = k.y,
		f = this.scrollProcess,
		a,
		b = this.activeEl,
		i = this.scrollElRegion,
		c = b.dom,
		g = this;
		this.pt = k;
		if (i && i.contains(k) && b.isScrollable()) {
			if (i.bottom - h <= g.vthresh && (this.scrollHeight - c.scrollTop - c.clientHeight > 0)) {
				if (f.el != b) {
					this.startScrollInterval(b, "down")
				}
				return
			} else {
				if (i.right - j <= g.hthresh && (this.scrollWidth - c.scrollLeft - c.clientWidth > 0)) {
					if (f.el != b) {
						this.startScrollInterval(b, "right")
					}
					return
				} else {
					if (h - i.top <= g.vthresh && b.dom.scrollTop > 0) {
						if (f.el != b) {
							this.startScrollInterval(b, "up")
						}
						return
					} else {
						if (j - i.left <= g.hthresh && b.dom.scrollLeft > 0) {
							if (f.el != b) {
								this.startScrollInterval(b, "left")
							}
							return
						}
					}
				}
			}
		}
		this.clearScrollInterval()
	},
	refreshElRegion : function () {
		this.scrollElRegion = this.activeEl.getRegion()
	},
	activate : function (a, b) {
		this.direction = b || "both";
		this.activeEl = Ext.get(a);
		this.scrollWidth = this.activeEl.dom.scrollWidth;
		this.scrollHeight = this.activeEl.dom.scrollHeight;
		this.refreshElRegion();
		this.activeEl.on("mousemove", this.onMouseMove, this)
	},
	deactivate : function () {
		this.clearScrollInterval();
		this.activeEl.un("mousemove", this.onMouseMove, this);
		this.activeEl = this.scrollElRegion = this.scrollWidth = this.scrollHeight = null;
		this.direction = "both"
	}
});
if (!Ext.ClassManager.get("Sch.model.Customizable")) {
	Ext.define("Sch.model.Customizable", {
		extend : "Ext.data.Model",
		idProperty : null,
		customizableFields : null,
		previous : null,
		constructor : function () {
			var a = this.callParent(arguments);
			this.modified = this.modified || {};
			return a
		},
		onClassExtended : function (b, d, a) {
			var c = a.onBeforeCreated;
			a.onBeforeCreated = function (o, k) {
				c.apply(this, arguments);
				var l = o.prototype;
				var n = Ext.versions.extjs && Ext.versions.extjs.isGreaterThanOrEqual("5.0");
				if (!l.customizableFields) {
					return
				}
				l.customizableFields = (o.superclass.customizableFields || []).concat(l.customizableFields);
				var g = l.customizableFields;
				var h = {};
				Ext.Array.each(g, function (i) {
					if (typeof i == "string") {
						i = {
							name : i
						}
					}
					h[i.name] = i
				});
				var m = l.fields;
				if (Ext.isArray(m)) {
					var e = new Ext.util.MixedCollection();
					for (var j = 0; j < m.length; j++) {
						e.add(m[j].name, m[j])
					}
					m = e
				}
				var f = [];
				m.each(function (i) {
					if (i.isCustomizableField) {
						f.push(i)
					}
				});
				m.removeAll(f);
				Ext.Object.each(h, function (i, r) {
					r.isCustomizableField = true;
					var s = r.name || r.getName();
					var x = s === "Id" ? "idProperty" : s.charAt(0).toLowerCase() + s.substr(1) + "Field";
					var t = l[x];
					var w = t || s;
					var v;
					if (m.containsKey(w)) {
						v = Ext.applyIf({
								name : s,
								isCustomizableField : true
							}, m.getByKey(w));
						m.getByKey(w).isCustomizableField = true;
						if (n) {
							v = Ext.create("data.field." + (v.type || "auto"), v)
						} else {
							v = new Ext.data.Field(v)
						}
						g.push(v)
					} else {
						v = Ext.applyIf({
								name : w,
								isCustomizableField : true
							}, r);
						if (n) {
							v = Ext.create("data.field." + (v.type || "auto"), v)
						} else {
							v = new Ext.data.Field(v)
						}
						m.add(w, v)
					}
					var q = Ext.String.capitalize(s);
					if (q != "Id") {
						var u = "get" + q;
						var p = "set" + q;
						if (!l[u] || l[u].__getterFor__ && l[u].__getterFor__ != w) {
							l[u] = function () {
								return this.data[w]
							};
							l[u].__getterFor__ = w
						}
						if (!l[p] || l[p].__setterFor__ && l[p].__setterFor__ != w) {
							l[p] = function (y) {
								return this.set(w, y)
							};
							l[p].__setterFor__ = w
						}
					}
				});
				if (n) {
					l.fields.splice(0, l.fields.length);
					l.fields.push.apply(l.fields, m.items)
				}
			}
		},
		set : function (d, b) {
			var a;
			this.previous = this.previous || {};
			if (arguments.length > 1) {
				a = this.get(d);
				if (a !== b) {
					this.previous[d] = a
				}
			} else {
				for (var c in d) {
					a = this.get(c);
					if (a !== d[c]) {
						this.previous[c] = a
					}
				}
			}
			this.callParent(arguments);
			if (!this.__editing) {
				delete this.previous
			}
		},
		beginEdit : function () {
			this.__editing = true;
			this.callParent(arguments)
		},
		cancelEdit : function () {
			this.callParent(arguments);
			this.__editing = false;
			delete this.previous
		},
		endEdit : function () {
			this.callParent(arguments);
			this.__editing = false;
			delete this.previous
		},
		reject : function () {
			var b = this,
			a = b.modified,
			c;
			b.previous = b.previous || {};
			for (c in a) {
				if (a.hasOwnProperty(c)) {
					if (typeof a[c] != "function") {
						b.previous[c] = b.get(c)
					}
				}
			}
			b.callParent(arguments);
			delete b.previous
		}
	})
}
Ext.define("Sch.data.mixin.EventStore", {
	model : "Sch.model.Event",
	config : {
		model : "Sch.model.Event"
	},
	requires : ["Sch.util.Date"],
	isEventStore : true,
	setResourceStore : function (a) {
		if (this.resourceStore) {
			this.resourceStore.un({
				beforesync : this.onResourceStoreBeforeSync,
				write : this.onResourceStoreWrite,
				scope : this
			})
		}
		this.resourceStore = a;
		if (a) {
			a.on({
				beforesync : this.onResourceStoreBeforeSync,
				write : this.onResourceStoreWrite,
				scope : this
			})
		}
	},
	onResourceStoreBeforeSync : function (b, c) {
		var a = b.create;
		if (a) {
			for (var e, d = a.length - 1; d >= 0; d--) {
				e = a[d];
				e._phantomId = e.internalId
			}
		}
	},
	onResourceStoreWrite : function (c, b) {
		if (b.wasSuccessful()) {
			var d = this,
			a = b.getRecords();
			Ext.each(a, function (e) {
				if (e._phantomId && !e.phantom) {
					d.each(function (f) {
						if (f.getResourceId() === e._phantomId) {
							f.assign(e)
						}
					})
				}
			})
		}
	},
	isDateRangeAvailable : function (f, a, b, d) {
		var c = true,
		e = Sch.util.Date;
		this.forEachScheduledEvent(function (h, g, i) {
			if (e.intersectSpans(f, a, g, i) && d === h.getResource() && (!b || b !== h)) {
				c = false;
				return false
			}
		});
		return c
	},
	getEventsInTimeSpan : function (d, b, a) {
		if (a !== false) {
			var c = Sch.util.Date;
			return this.queryBy(function (g) {
				var f = g.getStartDate(),
				e = g.getEndDate();
				return f && e && c.intersectSpans(f, e, d, b)
			})
		} else {
			return this.queryBy(function (g) {
				var f = g.getStartDate(),
				e = g.getEndDate();
				return f && e && (f - d >= 0) && (b - e >= 0)
			})
		}
	},
	forEachScheduledEvent : function (b, a) {
		this.each(function (e) {
			var d = e.getStartDate(),
			c = e.getEndDate();
			if (d && c) {
				return b.call(a || this, e, d, c)
			}
		}, this)
	},
	getTotalTimeSpan : function () {
		var a = new Date(9999, 0, 1),
		b = new Date(0),
		c = Sch.util.Date;
		this.each(function (d) {
			if (d.getStartDate()) {
				a = c.min(d.getStartDate(), a)
			}
			if (d.getEndDate()) {
				b = c.max(d.getEndDate(), b)
			}
		});
		a = a < new Date(9999, 0, 1) ? a : null;
		b = b > new Date(0) ? b : null;
		return {
			start : a || null,
			end : b || a || null
		}
	},
	getEventsForResource : function (e) {
		var c = [],
		d,
		f = e.getId() || e.internalId;
		for (var b = 0, a = this.getCount(); b < a; b++) {
			d = this.getAt(b);
			if (d.data[d.resourceIdField] == f) {
				c.push(d)
			}
		}
		return c
	},
	append : function (a) {
		throw "Must be implemented by consuming class"
	},
	getModel : function () {
		return this.model
	}
});
Ext.define("Sch.model.Range", {
	extend : "Sch.model.Customizable",
	requires : ["Sch.util.Date"],
	idProperty : "Id",
	config : Ext.versions.touch ? {
		idProperty : "Id"
	}
	 : null,
	startDateField : "StartDate",
	endDateField : "EndDate",
	nameField : "Name",
	clsField : "Cls",
	customizableFields : [{
			name : "StartDate",
			type : "date",
			dateFormat : "c"
		}, {
			name : "EndDate",
			type : "date",
			dateFormat : "c"
		}, {
			name : "Cls",
			type : "string"
		}, {
			name : "Name",
			type : "string"
		}
	],
	setStartDate : function (a, d) {
		var c = this.getEndDate();
		var b = this.getStartDate();
		this.set(this.startDateField, a);
		if (d === true && c && b) {
			this.setEndDate(Sch.util.Date.add(a, Sch.util.Date.MILLI, c - b))
		}
	},
	setEndDate : function (b, d) {
		var a = this.getStartDate();
		var c = this.getEndDate();
		this.set(this.endDateField, b);
		if (d === true && a && c) {
			this.setStartDate(Sch.util.Date.add(b, Sch.util.Date.MILLI,  - (c - a)))
		}
	},
	setStartEndDate : function (c, a) {
		var b = !this.editing;
		b && this.beginEdit();
		this.set(this.startDateField, c);
		this.set(this.endDateField, a);
		b && this.endEdit()
	},
	getDates : function () {
		var c = [],
		b = this.getEndDate();
		for (var a = Ext.Date.clearTime(this.getStartDate(), true); a < b; a = Sch.util.Date.add(a, Sch.util.Date.DAY, 1)) {
			c.push(a)
		}
		return c
	},
	forEachDate : function (b, a) {
		return Ext.each(this.getDates(), b, a)
	},
	isValid : function () {
		var b = this.callParent(arguments);
		if (b) {
			var c = this.getStartDate(),
			a = this.getEndDate();
			b = !c || !a || (a - c >= 0)
		}
		return b
	},
	shift : function (b, a) {
		this.setStartEndDate(Sch.util.Date.add(this.getStartDate(), b, a), Sch.util.Date.add(this.getEndDate(), b, a))
	}
});
Ext.define("Sch.model.TimeAxisTick", {
	extend : "Sch.model.Range",
	startDateField : "start",
	endDateField : "end"
});
if (!Ext.ClassManager.get("Sch.model.Resource")) {
	Ext.define("Sch.model.Resource", {
		extend : "Sch.model.Customizable",
		idProperty : "Id",
		config : Ext.versions.touch ? {
			idProperty : "Id"
		}
		 : null,
		nameField : "Name",
		customizableFields : ["Id", {
				name : "Name",
				type : "string"
			}
		],
		getEventStore : function () {
			return this.stores[0] && this.stores[0].eventStore || this.parentNode && this.parentNode.getEventStore()
		},
		getEvents : function (d) {
			var c = [],
			e,
			f = this.getId() || this.internalId;
			d = d || this.getEventStore();
			for (var b = 0, a = d.getCount(); b < a; b++) {
				e = d.getAt(b);
				if (e.data[e.resourceIdField] === f) {
					c.push(e)
				}
			}
			return c
		}
	})
}
Ext.define("Sch.data.mixin.ResourceStore", {
	getModel : function () {
		return this.model
	}
});
Ext.define("Sch.data.FilterableNodeStore", {
	extend : "Ext.data.NodeStore",
	onNodeExpand : function (d, c, b) {
		var e = this.treeStore;
		var a = e.isTreeFiltered(true);
		if (a && d == this.node) {
			e.reApplyFilter()
		} else {
			return this.callParent(arguments)
		}
	},
	handleNodeExpand : function (h, b, j) {
		var f = [];
		var a = this.treeStore;
		var c = a.isTreeFiltered();
		var g = a.currentFilterGeneration;
		for (var d = 0; d < b.length; d++) {
			var e = b[d];
			if (!(c && e.__filterGen != g || e.hidden)) {
				f[f.length] = e
			}
		}
		return this.callParent([h, f, j])
	},
	onNodeCollapse : function (g, b, i, h, j) {
		var f = this;
		var d = this.data;
		var k = d.contains;
		var a = this.treeStore;
		var c = a.isTreeFiltered();
		var e = a.currentFilterGeneration;
		d.contains = function () {
			var o,
			n,
			q;
			var m = f.indexOf(g) + 1;
			var p = false;
			for (var l = 0; l < b.length; l++) {
				if (!(b[l].hidden || c && b[l].__filterGen != e) && k.call(this, b[l])) {
					o = g;
					while (o.parentNode) {
						n = o;
						do {
							n = n.nextSibling
						} while (n && (n.hidden || c && n.__filterGen != e));
						if (n) {
							p = true;
							q = f.indexOf(n);
							break
						} else {
							o = o.parentNode
						}
					}
					if (!p) {
						q = f.getCount()
					}
					f.removeAt(m, q - m);
					break
				}
			}
			return false
		};
		this.callParent(arguments);
		d.contains = k
	},
	onNodeAppend : function (h, b, d) {
		var g = this,
		e,
		i;
		var a = this.treeStore;
		var c = a.isTreeFiltered();
		var f = a.currentFilterGeneration;
		if (c) {
			b.__filterGen = f
		}
		if (g.isVisible(b)) {
			if (d === 0) {
				e = h
			} else {
				i = b;
				do {
					i = i.previousSibling
				} while (i && (i.hidden || c && i.__filterGen != f));
				if (!i) {
					e = h
				} else {
					while (i.isExpanded() && i.lastChild) {
						i = i.lastChild
					}
					e = i
				}
			}
			g.insert(g.indexOf(e) + 1, b);
			if (!b.isLeaf() && b.isExpanded()) {
				if (b.isLoaded()) {
					g.onNodeExpand(b, b.childNodes, true)
				} else {
					if (!g.treeStore.fillCount) {
						b.set("expanded", false);
						b.expand()
					}
				}
			}
		}
	}
});
Ext.define("Sch.data.mixin.FilterableTreeStore", {
	requires : ["Sch.data.FilterableNodeStore"],
	nodeStoreClassName : "Sch.data.FilterableNodeStore",
	nodeStore : null,
	isFilteredFlag : false,
	isHiddenFlag : false,
	lastTreeFilter : null,
	lastTreeHiding : null,
	allowExpandCollapseWhileFiltered : true,
	reApplyFilterOnDataChange : true,
	suspendIncrementalFilterRefresh : 0,
	filterGeneration : 0,
	currentFilterGeneration : null,
	dataChangeListeners : null,
	monitoringDataChange : false,
	initTreeFiltering : function () {
		if (!this.nodeStore) {
			this.nodeStore = this.createNodeStore(this)
		}
		this.dataChangeListeners = {
			append : this.onNeedToUpdateFilter,
			insert : this.onNeedToUpdateFilter,
			scope : this
		}
	},
	startDataChangeMonitoring : function () {
		if (this.monitoringDataChange) {
			return
		}
		this.monitoringDataChange = true;
		this.on(this.dataChangeListeners)
	},
	stopDataChangeMonitoring : function () {
		if (!this.monitoringDataChange) {
			return
		}
		this.monitoringDataChange = false;
		this.un(this.dataChangeListeners)
	},
	onNeedToUpdateFilter : function () {
		if (this.reApplyFilterOnDataChange && !this.suspendIncrementalFilterRefresh) {
			this.reApplyFilter()
		}
	},
	createNodeStore : function (a) {
		return Ext.create(this.nodeStoreClassName, {
			treeStore : a,
			recursive : true,
			rootVisible : this.rootVisible
		})
	},
	clearTreeFilter : function () {
		if (!this.isTreeFiltered()) {
			return
		}
		this.currentFilterGeneration = null;
		this.isFilteredFlag = false;
		this.lastTreeFilter = null;
		if (!this.isTreeFiltered(true)) {
			this.stopDataChangeMonitoring()
		}
		this.refreshNodeStoreContent();
		this.fireEvent("filter-clear", this)
	},
	reApplyFilter : function () {
		if (this.isHiddenFlag) {
			this.hideNodesBy.apply(this, this.lastTreeHiding.concat(this.isFilteredFlag))
		}
		if (this.isFilteredFlag) {
			this.filterTreeBy(this.lastTreeFilter)
		}
	},
	refreshNodeStoreContent : function (a) {
		var g = this.getRootNode(),
		i = [];
		var h = this.rootVisible;
		var b = this.isTreeFiltered();
		var e = this;
		var f = this.currentFilterGeneration;
		var d = function (m) {
			if (b && m.__filterGen != f || m.hidden) {
				return
			}
			if (h || m != g) {
				i[i.length] = m
			}
			if (!m.data.leaf && m.isExpanded()) {
				var n = m.childNodes,
				l = n.length;
				for (var j = 0; j < l; j++) {
					d(n[j])
				}
			}
		};
		d(g);
		this.fireEvent("nodestore-datachange-start", this);
		var c = this.nodeStore;
		if (!this.loadDataInNodeStore || !this.loadDataInNodeStore(i)) {
			c.loadRecords(i)
		}
		if (!a) {
			c.fireEvent("clear", c)
		}
		this.fireEvent("nodestore-datachange-end", this)
	},
	getIndexInTotalDataset : function (d) {
		var c = this.getRootNode(),
		f = -1;
		var g = this.rootVisible;
		if (!g && d == c) {
			return -1
		}
		var b = this.isTreeFiltered();
		var h = this;
		var a = this.currentFilterGeneration;
		var e = function (l) {
			if (b && l.__filterGen != a || l.hidden) {
				if (l == d) {
					return false
				}
			}
			if (g || l != c) {
				f++
			}
			if (l == d) {
				return false
			}
			if (!l.data.leaf && l.isExpanded()) {
				var m = l.childNodes,
				j = m.length;
				for (var i = 0; i < j; i++) {
					if (e(m[i]) === false) {
						return false
					}
				}
			}
		};
		e(c);
		return f
	},
	isTreeFiltered : function (a) {
		return this.isFilteredFlag || a && this.isHiddenFlag
	},
	collectFilteredNodes : function (k, t) {
		var s = this.currentFilterGeneration;
		var o = {};
		var n = this.getRootNode(),
		e = this.rootVisible,
		d = [];
		var a = function (u) {
			var i = u.parentNode;
			while (i && !o[i.internalId]) {
				o[i.internalId] = true;
				i = i.parentNode
			}
		};
		var g = t.filter;
		var b = t.scope || this;
		var j = t.shallow;
		var r = t.checkParents || j;
		var h = t.fullMathchingParents;
		var c = t.onlyParents || h;
		if (c && r) {
			throw new Error("Can't combine `onlyParents` and `checkParents` options")
		}
		var m = function (w) {
			if (w.hidden) {
				return
			}
			var u,
			x,
			v,
			i;
			if (w.data.leaf) {
				if (g.call(b, w, o)) {
					d[d.length] = w;
					a(w)
				}
			} else {
				if (e || w != n) {
					d[d.length] = w
				}
				if (c) {
					u = g.call(b, w);
					x = w.childNodes;
					v = x.length;
					if (u) {
						o[w.internalId] = true;
						a(w);
						if (h) {
							w.cascadeBy(function (y) {
								if (y != w) {
									d[d.length] = y;
									if (!y.data.leaf) {
										o[y.internalId] = true
									}
								}
							});
							return
						}
					}
					for (i = 0; i < v; i++) {
						if (u && x[i].data.leaf) {
							d[d.length] = x[i]
						} else {
							if (!x[i].data.leaf) {
								m(x[i])
							}
						}
					}
				} else {
					if (r) {
						u = g.call(b, w, o);
						if (u) {
							o[w.internalId] = true;
							a(w)
						}
					}
					if (!r || !j || j && (u || w == n && !e)) {
						x = w.childNodes;
						v = x.length;
						for (i = 0; i < v; i++) {
							m(x[i])
						}
					}
				}
			}
		};
		m(k);
		var f = [];
		for (var p = 0, q = d.length; p < q; p++) {
			var l = d[p];
			if (l.data.leaf || o[l.internalId]) {
				f[f.length] = l;
				l.__filterGen = s;
				if (this.allowExpandCollapseWhileFiltered && !l.data.leaf) {
					l.data.expanded = true
				}
			}
		}
		return f
	},
	filterTreeBy : function (e, c) {
		this.currentFilterGeneration = this.filterGeneration++;
		var b;
		if (arguments.length == 1 && Ext.isObject(arguments[0])) {
			c = e.scope;
			b = e.filter
		} else {
			b = e;
			e = {
				filter : b,
				scope : c
			}
		}
		this.fireEvent("nodestore-datachange-start", this);
		e = e || {};
		var a = this.collectFilteredNodes(this.getRootNode(), e);
		var d = this.nodeStore;
		if (!this.loadDataInNodeStore || !this.loadDataInNodeStore(a)) {
			d.loadRecords(a, false);
			d.fireEvent("clear", d)
		}
		this.startDataChangeMonitoring();
		this.isFilteredFlag = true;
		this.lastTreeFilter = e;
		this.fireEvent("nodestore-datachange-end", this);
		this.fireEvent("filter-set", this)
	},
	hideNodesBy : function (b, a, d) {
		if (this.isFiltered()) {
			throw new Error("Can't hide nodes of the filtered tree store")
		}
		var c = this;
		a = a || this;
		this.getRootNode().cascadeBy(function (e) {
			e.hidden = b.call(a, e, c)
		});
		this.startDataChangeMonitoring();
		this.isHiddenFlag = true;
		this.lastTreeHiding = [b, a];
		if (!d) {
			this.refreshNodeStoreContent()
		}
	},
	showAllNodes : function (a) {
		this.getRootNode().cascadeBy(function (b) {
			b.hidden = false
		});
		this.isHiddenFlag = false;
		this.lastTreeHiding = null;
		if (!this.isTreeFiltered(true)) {
			this.stopDataChangeMonitoring()
		}
		if (!a) {
			this.refreshNodeStoreContent()
		}
	},
	inheritables : function () {
		return {
			load : function (l) {
				var i = this.getRootNode();
				if (i) {
					var e = this.nodeStore;
					var b = i.removeAll;
					i.removeAll = function () {
						b.apply(this, arguments);
						e && e.fireEvent("clear", e);
						delete i.removeAll
					}
				}
				var a = Ext.getVersion("extjs").isLessThan("4.2.2.1144");
				if (a) {
					l = l || {};
					var f = false;
					var c;
					this.on("beforeload", function (n, m) {
						c = m.node;
						f = c.data.expanded;
						c.data.expanded = false
					}, this, {
						single : true
					});
					var j = l.callback;
					var k = l.scope;
					l.callback = function () {
						if (f) {
							c.expand()
						}
						Ext.callback(j, k, arguments)
					}
				}
				var g = this;
				l = l || {};
				var d = l.callback;
				var h = l.scope;
				l.callback = function () {
					g.suspendIncrementalFilterRefresh--;
					Ext.callback(d, h, arguments)
				};
				this.suspendIncrementalFilterRefresh++;
				this.callParent([l]);
				if (i) {
					delete i.removeAll
				}
			}
		}
	}
});
Ext.define("Sch.data.ResourceStore", {
	extend : "Ext.data.Store",
	model : "Sch.model.Resource",
	config : {
		model : "Sch.model.Resource"
	},
	mixins : ["Sch.data.mixin.ResourceStore"],
	constructor : function () {
		this.callParent(arguments);
		if (this.getModel() !== Sch.model.Resource && !(this.getModel().prototype instanceof Sch.model.Resource)) {
			throw "The model for the ResourceStore must subclass Sch.model.Resource"
		}
	}
});
Ext.define("Sch.data.TimeAxis", {
	extend : "Ext.data.JsonStore",
	requires : ["Sch.util.Date", "Sch.model.TimeAxisTick"],
	model : "Sch.model.TimeAxisTick",
	continuous : true,
	originalContinuous : null,
	autoAdjust : true,
	unit : null,
	increment : null,
	resolutionUnit : null,
	resolutionIncrement : null,
	weekStartDay : null,
	mainUnit : null,
	shiftUnit : null,
	shiftIncrement : 1,
	defaultSpan : 1,
	isConfigured : false,
	adjustedStart : null,
	adjustedEnd : null,
	visibleTickStart : null,
	visibleTickEnd : null,
	constructor : function (a) {
		var b = this;
		if (b.setModel) {
			b.setModel(b.model)
		}
		b.originalContinuous = b.continuous;
		b.callParent(arguments);
		b.on(Ext.versions.touch ? "refresh" : "datachanged", function (c, d, e) {
			b.fireEvent("reconfigure", b, d, e)
		});
		if (a && b.start) {
			b.reconfigure(a)
		}
	},
	reconfigure : function (e, a) {
		this.isConfigured = true;
		Ext.apply(this, e);
		var m = this.getAdjustedDates(e.start, e.end, true);
		var l = this.getAdjustedDates(e.start, e.end);
		var b = l.start;
		var f = l.end;
		if (this.fireEvent("beforereconfigure", this, b, f) !== false) {
			this.fireEvent("beginreconfigure", this);
			var j = this.unit;
			var k = this.increment || 1;
			var i = this.generateTicks(b, f, j, k, this.mainUnit);
			var d = Ext.Object.getKeys(e).length;
			var g = (d === 1 && "start" in e) || (d === 2 && "start" in e && "end" in e);
			this.removeAll(true);
			this.suspendEvents();
			this.add(i);
			if (this.getCount() === 0) {
				Ext.Error.raise("Invalid time axis configuration or filter, please check your input data.")
			}
			this.resumeEvents();
			var c = Sch.util.Date;
			var h = i.length;
			if (this.isContinuous()) {
				this.adjustedStart = m.start;
				this.adjustedEnd = this.getNext(h > 1 ? i[h - 1].start : m.start, j, k)
			} else {
				this.adjustedStart = this.getStart();
				this.adjustedEnd = this.getEnd()
			}
			do {
				this.visibleTickStart = (this.getStart() - this.adjustedStart) / (c.getUnitDurationInMs(j) * k);
				if (this.visibleTickStart >= 1) {
					this.adjustedStart = c.getNext(this.adjustedStart, j, 1)
				}
			} while (this.visibleTickStart >= 1);
			do {
				this.visibleTickEnd = h - (this.adjustedEnd - this.getEnd()) / (c.getUnitDurationInMs(j) * k);
				if (h - this.visibleTickEnd >= 1) {
					this.adjustedEnd = c.getNext(this.adjustedEnd, j, -1)
				}
			} while (h - this.visibleTickEnd >= 1);
			this.fireEvent("datachanged", this, !g, a);
			this.fireEvent("refresh", this, !g, a);
			this.fireEvent("endreconfigure", this)
		}
	},
	setTimeSpan : function (c, a) {
		var b = this.getAdjustedDates(c, a);
		c = b.start;
		a = b.end;
		if (this.getStart() - c !== 0 || this.getEnd() - a !== 0) {
			this.reconfigure({
				start : c,
				end : a
			})
		}
	},
	filterBy : function (b, a) {
		this.continuous = false;
		a = a || this;
		this.clearFilter(true);
		this.suspendEvents(true);
		this.filter([{
					filterFn : function (d, c) {
						return b.call(a, d.data, c)
					}
				}
			]);
		if (this.getCount() === 0) {
			this.clearFilter();
			this.resumeEvents();
			Ext.Error.raise("Invalid time axis filter - no ticks passed through the filter. Please check your filter method.")
		}
		this.resumeEvents()
	},
	isContinuous : function () {
		return this.continuous && !this.isFiltered()
	},
	clearFilter : function () {
		this.continuous = this.originalContinuous;
		this.callParent(arguments)
	},
	generateTicks : function (a, d, g, i) {
		var h = [],
		f,
		b = Sch.util.Date,
		e = 0;
		g = g || this.unit;
		i = i || this.increment;
		var j = this.getAdjustedDates(a, d);
		a = j.start;
		d = j.end;
		while (a < d) {
			f = this.getNext(a, g, i);
			if (!this.autoAdjust && f > d) {
				f = d
			}
			if (g === b.HOUR && i > 1 && h.length > 0 && e === 0) {
				var c = h[h.length - 1];
				e = ((c.start.getHours() + i) % 24) - c.end.getHours();
				if (e !== 0) {
					f = b.add(f, b.HOUR, e)
				}
			}
			h.push({
				start : a,
				end : f
			});
			a = f
		}
		return h
	},
	getVisibleTickTimeSpan : function () {
		return this.isContinuous() ? this.visibleTickEnd - this.visibleTickStart : this.getCount()
	},
	getAdjustedDates : function (c, b, a) {
		c = c || this.getStart();
		b = b || Sch.util.Date.add(c, this.mainUnit, this.defaultSpan);
		return this.autoAdjust || a ? {
			start : this.floorDate(c, false, this.mainUnit, 1),
			end : this.ceilDate(b, false, this.mainUnit, 1)
		}
		 : {
			start : c,
			end : b
		}
	},
	getTickFromDate : function (d) {
		var j = this.data.items;
		var h = j.length - 1;
		if (d < j[0].data.start || d > j[h].data.end) {
			return -1
		}
		var f,
		g,
		b;
		if (this.isContinuous()) {
			if (d - j[0].data.start === 0) {
				return this.visibleTickStart
			}
			if (d - j[h].data.end === 0) {
				return this.visibleTickEnd
			}
			var k = this.adjustedStart;
			var a = this.adjustedEnd;
			var c = Math.floor(j.length * (d - k) / (a - k));
			if (c > h) {
				c = h
			}
			g = c === 0 ? k : j[c].data.start;
			b = c == h ? a : j[c].data.end;
			f = c + (d - g) / (b - g);
			if (f < this.visibleTickStart || f > this.visibleTickEnd) {
				return -1
			}
			return f
		} else {
			for (var e = 0; e <= h; e++) {
				b = j[e].data.end;
				if (d <= b) {
					g = j[e].data.start;
					f = e + (d > g ? (d - g) / (b - g) : 0);
					return f
				}
			}
		}
		return -1
	},
	getDateFromTick : function (e, i) {
		if (e === this.visibleTickEnd) {
			return this.getEnd()
		}
		var b = Math.floor(e),
		g = e - b,
		h = this.getAt(b);
		if (!h) {
			return null
		}
		var f = h.data;
		var a = b === 0 ? this.adjustedStart : f.start;
		var d = (b == this.getCount() - 1) && this.isContinuous() ? this.adjustedEnd : f.end;
		var c = Sch.util.Date.add(a, Sch.util.Date.MILLI, g * (d - a));
		if (i) {
			c = this[i + "Date"](c)
		}
		return c
	},
	getTicks : function () {
		var a = [];
		this.each(function (b) {
			a.push(b.data)
		});
		return a
	},
	getStart : function () {
		var a = this.first();
		if (a) {
			return new Date(a.data.start)
		}
		return null
	},
	getEnd : function () {
		var a = this.last();
		if (a) {
			return new Date(a.data.end)
		}
		return null
	},
	floorDate : function (e, g, h, a) {
		g = g !== false;
		var c = Ext.Date.clone(e),
		d = g ? this.getStart() : null,
		l = a || this.resolutionIncrement,
		k;
		if (h) {
			k = h
		} else {
			k = g ? this.resolutionUnit : this.mainUnit
		}
		var b = Sch.util.Date;
		var f = function (n, m) {
			return Math.floor(n / m) * m
		};
		switch (k) {
		case b.MILLI:
			if (g) {
				c = b.add(d, b.MILLI, f(b.getDurationInMilliseconds(d, c), l))
			}
			break;
		case b.SECOND:
			if (g) {
				c = b.add(d, b.MILLI, f(b.getDurationInSeconds(d, c), l) * 1000)
			} else {
				c.setMilliseconds(0);
				c.setSeconds(f(c.getSeconds(), l))
			}
			break;
		case b.MINUTE:
			if (g) {
				c = b.add(d, b.SECOND, f(b.getDurationInMinutes(d, c), l) * 60)
			} else {
				c.setMinutes(f(c.getMinutes(), l));
				c.setSeconds(0);
				c.setMilliseconds(0)
			}
			break;
		case b.HOUR:
			if (g) {
				c = b.add(d, b.MINUTE, f(b.getDurationInHours(this.getStart(), c), l) * 60)
			} else {
				c.setMinutes(0);
				c.setSeconds(0);
				c.setMilliseconds(0);
				c.setHours(f(c.getHours(), l))
			}
			break;
		case b.DAY:
			if (g) {
				c = b.add(d, b.DAY, f(b.getDurationInDays(d, c), l))
			} else {
				Ext.Date.clearTime(c);
				c.setDate(f(c.getDate() - 1, l) + 1)
			}
			break;
		case b.WEEK:
			var j = c.getDay() || 7;
			var i = this.weekStartDay || 7;
			Ext.Date.clearTime(c);
			c = b.add(c, b.DAY, j >= i ? i - j :  - (7 - i + j));
			break;
		case b.MONTH:
			if (g) {
				c = b.add(d, b.MONTH, f(b.getDurationInMonths(d, c), l))
			} else {
				Ext.Date.clearTime(c);
				c.setDate(1);
				c.setMonth(f(c.getMonth(), l))
			}
			break;
		case b.QUARTER:
			Ext.Date.clearTime(c);
			c.setDate(1);
			c = b.add(c, b.MONTH,  - (c.getMonth() % 3));
			break;
		case b.YEAR:
			if (g) {
				c = b.add(d, b.YEAR, f(b.getDurationInYears(d, c), l))
			} else {
				c = new Date(f(e.getFullYear() - 1, l) + 1, 0, 1)
			}
			break
		}
		return c
	},
	roundDate : function (r, b) {
		var l = Ext.Date.clone(r),
		s = this.resolutionIncrement;
		b = b || this.getStart();
		switch (this.resolutionUnit) {
		case Sch.util.Date.MILLI:
			var e = Sch.util.Date.getDurationInMilliseconds(b, l),
			d = Math.round(e / s) * s;
			l = Sch.util.Date.add(b, Sch.util.Date.MILLI, d);
			break;
		case Sch.util.Date.SECOND:
			var i = Sch.util.Date.getDurationInSeconds(b, l),
			q = Math.round(i / s) * s;
			l = Sch.util.Date.add(b, Sch.util.Date.MILLI, q * 1000);
			break;
		case Sch.util.Date.MINUTE:
			var n = Sch.util.Date.getDurationInMinutes(b, l),
			a = Math.round(n / s) * s;
			l = Sch.util.Date.add(b, Sch.util.Date.SECOND, a * 60);
			break;
		case Sch.util.Date.HOUR:
			var m = Sch.util.Date.getDurationInHours(b, l),
			j = Math.round(m / s) * s;
			l = Sch.util.Date.add(b, Sch.util.Date.MINUTE, j * 60);
			break;
		case Sch.util.Date.DAY:
			var c = Sch.util.Date.getDurationInDays(b, l),
			f = Math.round(c / s) * s;
			l = Sch.util.Date.add(b, Sch.util.Date.DAY, f);
			break;
		case Sch.util.Date.WEEK:
			Ext.Date.clearTime(l);
			var o = l.getDay() - this.weekStartDay,
			t;
			if (o < 0) {
				o = 7 + o
			}
			if (Math.round(o / 7) === 1) {
				t = 7 - o
			} else {
				t = -o
			}
			l = Sch.util.Date.add(l, Sch.util.Date.DAY, t);
			break;
		case Sch.util.Date.MONTH:
			var p = Sch.util.Date.getDurationInMonths(b, l) + (l.getDate() / Ext.Date.getDaysInMonth(l)),
			h = Math.round(p / s) * s;
			l = Sch.util.Date.add(b, Sch.util.Date.MONTH, h);
			break;
		case Sch.util.Date.QUARTER:
			Ext.Date.clearTime(l);
			l.setDate(1);
			l = Sch.util.Date.add(l, Sch.util.Date.MONTH, 3 - (l.getMonth() % 3));
			break;
		case Sch.util.Date.YEAR:
			var k = Sch.util.Date.getDurationInYears(b, l),
			g = Math.round(k / s) * s;
			l = Sch.util.Date.add(b, Sch.util.Date.YEAR, g);
			break
		}
		return l
	},
	ceilDate : function (c, b, f) {
		var e = Ext.Date.clone(c);
		b = b !== false;
		var a = b ? this.resolutionIncrement : 1,
		g = false,
		d;
		if (f) {
			d = f
		} else {
			d = b ? this.resolutionUnit : this.mainUnit
		}
		switch (d) {
		case Sch.util.Date.HOUR:
			if (e.getMinutes() > 0 || e.getSeconds() > 0 || e.getMilliseconds() > 0) {
				g = true
			}
			break;
		case Sch.util.Date.DAY:
			if (e.getHours() > 0 || e.getMinutes() > 0 || e.getSeconds() > 0 || e.getMilliseconds() > 0) {
				g = true
			}
			break;
		case Sch.util.Date.WEEK:
			Ext.Date.clearTime(e);
			if (e.getDay() !== this.weekStartDay) {
				g = true
			}
			break;
		case Sch.util.Date.MONTH:
			Ext.Date.clearTime(e);
			if (e.getDate() !== 1) {
				g = true
			}
			break;
		case Sch.util.Date.QUARTER:
			Ext.Date.clearTime(e);
			if (e.getMonth() % 3 !== 0 || (e.getMonth() % 3 === 0 && e.getDate() !== 1)) {
				g = true
			}
			break;
		case Sch.util.Date.YEAR:
			Ext.Date.clearTime(e);
			if (e.getMonth() !== 0 || e.getDate() !== 1) {
				g = true
			}
			break;
		default:
			break
		}
		if (g) {
			return this.getNext(e, d, a)
		} else {
			return e
		}
	},
	getNext : function (b, c, a) {
		return Sch.util.Date.getNext(b, c, a, this.weekStartDay)
	},
	getResolution : function () {
		return {
			unit : this.resolutionUnit,
			increment : this.resolutionIncrement
		}
	},
	setResolution : function (b, a) {
		this.resolutionUnit = b;
		this.resolutionIncrement = a || 1
	},
	shift : function (a, b) {
		this.setTimeSpan(Sch.util.Date.add(this.getStart(), b, a), Sch.util.Date.add(this.getEnd(), b, a))
	},
	shiftNext : function (a) {
		a = a || this.getShiftIncrement();
		var b = this.getShiftUnit();
		this.setTimeSpan(Sch.util.Date.add(this.getStart(), b, a), Sch.util.Date.add(this.getEnd(), b, a))
	},
	shiftPrevious : function (a) {
		a =  - (a || this.getShiftIncrement());
		var b = this.getShiftUnit();
		this.setTimeSpan(Sch.util.Date.add(this.getStart(), b, a), Sch.util.Date.add(this.getEnd(), b, a))
	},
	getShiftUnit : function () {
		return this.shiftUnit || this.mainUnit
	},
	getShiftIncrement : function () {
		return this.shiftIncrement || 1
	},
	getUnit : function () {
		return this.unit
	},
	getIncrement : function () {
		return this.increment
	},
	dateInAxis : function (a) {
		return Sch.util.Date.betweenLesser(a, this.getStart(), this.getEnd())
	},
	timeSpanInAxis : function (b, a) {
		if (this.isContinuous()) {
			return Sch.util.Date.intersectSpans(b, a, this.getStart(), this.getEnd())
		} else {
			return (b < this.getStart() && a > this.getEnd()) || this.getTickFromDate(b) !== this.getTickFromDate(a)
		}
	},
	forEachAuxInterval : function (h, b, a, f) {
		f = f || this;
		var c = this.getEnd(),
		g = this.getStart(),
		e = 0,
		d;
		if (g > c) {
			throw "Invalid time axis configuration"
		}
		while (g < c) {
			d = Sch.util.Date.min(this.getNext(g, h, b || 1), c);
			a.call(f, g, d, e);
			g = d;
			e++
		}
	},
	consumeViewPreset : function (a) {
		Ext.apply(this, {
			unit : a.getBottomHeader().unit,
			increment : a.getBottomHeader().increment || 1,
			resolutionUnit : a.timeResolution.unit,
			resolutionIncrement : a.timeResolution.increment,
			mainUnit : a.getMainHeader().unit,
			shiftUnit : a.shiftUnit,
			shiftIncrement : a.shiftIncrement || 1,
			defaultSpan : a.defaultSpan || 1
		})
	}
});
Ext.define("Sch.preset.ViewPreset", {
	name : null,
	rowHeight : null,
	timeColumnWidth : 50,
	timeRowHeight : null,
	timeAxisColumnWidth : null,
	displayDateFormat : "G:i",
	shiftUnit : "HOUR",
	shiftIncrement : 1,
	defaultSpan : 12,
	timeResolution : null,
	headerConfig : null,
	columnLinesFor : "middle",
	headers : null,
	mainHeader : 0,
	constructor : function (a) {
		Ext.apply(this, a)
	},
	getHeaders : function () {
		if (this.headers) {
			return this.headers
		}
		var a = this.headerConfig;
		this.mainHeader = a.top ? 1 : 0;
		return this.headers = [].concat(a.top || [], a.middle || [], a.bottom || [])
	},
	getMainHeader : function () {
		return this.getHeaders()[this.mainHeader]
	},
	getBottomHeader : function () {
		var a = this.getHeaders();
		return a[a.length - 1]
	},
	clone : function () {
		var a = {};
		var b = this;
		Ext.each(["rowHeight", "timeColumnWidth", "timeRowHeight", "timeAxisColumnWidth", "displayDateFormat", "shiftUnit", "shiftIncrement", "defaultSpan", "timeResolution", "headerConfig"], function (c) {
			a[c] = b[c]
		});
		return new this.self(Ext.clone(a))
	}
});
Ext.define("Sch.preset.Manager", {
	extend : "Ext.util.MixedCollection",
	requires : ["Sch.util.Date", "Sch.preset.ViewPreset"],
	mixins : ["Sch.mixin.Localizable"],
	singleton : true,
	constructor : function () {
		this.callParent(arguments);
		this.registerDefaults()
	},
	registerPreset : function (b, a) {
		if (a) {
			var c = a.headerConfig;
			var f = Sch.util.Date;
			for (var g in c) {
				if (c.hasOwnProperty(g)) {
					if (f[c[g].unit]) {
						c[g].unit = f[c[g].unit.toUpperCase()]
					}
				}
			}
			if (!a.timeColumnWidth) {
				a.timeColumnWidth = 50
			}
			if (!a.rowHeight) {
				a.rowHeight = 24
			}
			var d = a.timeResolution;
			if (d && f[d.unit]) {
				d.unit = f[d.unit.toUpperCase()]
			}
			var e = a.shiftUnit;
			if (e && f[e]) {
				a.shiftUnit = f[e.toUpperCase()]
			}
		}
		if (this.isValidPreset(a)) {
			if (this.containsKey(b)) {
				this.removeAtKey(b)
			}
			a.name = b;
			this.add(b, new Sch.preset.ViewPreset(a))
		} else {
			throw "Invalid preset, please check your configuration"
		}
	},
	isValidPreset : function (a) {
		var e = Sch.util.Date,
		c = true,
		d = Sch.util.Date.units,
		b = {};
		for (var f in a.headerConfig) {
			if (a.headerConfig.hasOwnProperty(f)) {
				b[f] = true;
				c = c && Ext.Array.indexOf(d, a.headerConfig[f].unit) >= 0
			}
		}
		if (!(a.columnLinesFor in b)) {
			a.columnLinesFor = "middle"
		}
		if (a.timeResolution) {
			c = c && Ext.Array.indexOf(d, a.timeResolution.unit) >= 0
		}
		if (a.shiftUnit) {
			c = c && Ext.Array.indexOf(d, a.shiftUnit) >= 0
		}
		return c
	},
	getPreset : function (a) {
		return this.get(a)
	},
	deletePreset : function (a) {
		this.removeAtKey(a)
	},
	registerDefaults : function () {
		var b = this,
		a = this.defaultPresets;
		for (var c in a) {
			b.registerPreset(c, a[c])
		}
	},
	defaultPresets : {
		secondAndMinute : {
			timeColumnWidth : 30,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "G:i:s",
			shiftIncrement : 10,
			shiftUnit : "MINUTE",
			defaultSpan : 24,
			timeResolution : {
				unit : "SECOND",
				increment : 5
			},
			headerConfig : {
				middle : {
					unit : "SECOND",
					increment : 10,
					align : "center",
					dateFormat : "s"
				},
				top : {
					unit : "MINUTE",
					align : "center",
					dateFormat : "D, d g:iA"
				}
			}
		},
		minuteAndHour : {
			timeColumnWidth : 100,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "G:i",
			shiftIncrement : 1,
			shiftUnit : "HOUR",
			defaultSpan : 24,
			timeResolution : {
				unit : "MINUTE",
				increment : 30
			},
			headerConfig : {
				middle : {
					unit : "MINUTE",
					increment : "30",
					align : "center",
					dateFormat : "i"
				},
				top : {
					unit : "HOUR",
					align : "center",
					dateFormat : "D, gA/d"
				}
			}
		},
		hourAndDay : {
			timeColumnWidth : 60,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "G:i",
			shiftIncrement : 1,
			shiftUnit : "DAY",
			defaultSpan : 24,
			timeResolution : {
				unit : "MINUTE",
				increment : 30
			},
			headerConfig : {
				middle : {
					unit : "HOUR",
					align : "center",
					dateFormat : "G:i"
				},
				top : {
					unit : "DAY",
					align : "center",
					dateFormat : "D d/m"
				}
			}
		},
		dayAndWeek : {
			timeColumnWidth : 100,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "Y-m-d G:i",
			shiftUnit : "DAY",
			shiftIncrement : 1,
			defaultSpan : 5,
			timeResolution : {
				unit : "HOUR",
				increment : 1
			},
			headerConfig : {
				middle : {
					unit : "DAY",
					align : "center",
					dateFormat : "D d M"
				},
				top : {
					unit : "WEEK",
					align : "center",
					renderer : function (c, b, a) {
						return Sch.util.Date.getShortNameOfUnit("WEEK") + "." + Ext.Date.format(c, "W M Y")
					}
				}
			}
		},
		weekAndDay : {
			timeColumnWidth : 100,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "Y-m-d",
			shiftUnit : "WEEK",
			shiftIncrement : 1,
			defaultSpan : 1,
			timeResolution : {
				unit : "DAY",
				increment : 1
			},
			headerConfig : {
				bottom : {
					unit : "DAY",
					align : "center",
					increment : 1,
					dateFormat : "d/m"
				},
				middle : {
					unit : "WEEK",
					dateFormat : "D d M"
				}
			}
		},
		weekAndMonth : {
			timeColumnWidth : 100,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "Y-m-d",
			shiftUnit : "WEEK",
			shiftIncrement : 5,
			defaultSpan : 6,
			timeResolution : {
				unit : "DAY",
				increment : 1
			},
			headerConfig : {
				middle : {
					unit : "WEEK",
					renderer : function (c, b, a) {
						return Ext.Date.format(c, "d M")
					}
				},
				top : {
					unit : "MONTH",
					align : "center",
					dateFormat : "M Y"
				}
			}
		},
		monthAndYear : {
			timeColumnWidth : 110,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "Y-m-d",
			shiftIncrement : 3,
			shiftUnit : "MONTH",
			defaultSpan : 12,
			timeResolution : {
				unit : "DAY",
				increment : 1
			},
			headerConfig : {
				middle : {
					unit : "MONTH",
					align : "center",
					dateFormat : "M Y"
				},
				top : {
					unit : "YEAR",
					align : "center",
					dateFormat : "Y"
				}
			}
		},
		year : {
			timeColumnWidth : 100,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "Y-m-d",
			shiftUnit : "YEAR",
			shiftIncrement : 1,
			defaultSpan : 1,
			timeResolution : {
				unit : "MONTH",
				increment : 1
			},
			headerConfig : {
				middle : {
					unit : "QUARTER",
					align : "center",
					renderer : function (c, b, a) {
						return Ext.String.format(Sch.util.Date.getShortNameOfUnit("QUARTER").toUpperCase() + "{0}", Math.floor(c.getMonth() / 3) + 1)
					}
				},
				top : {
					unit : "YEAR",
					align : "center",
					dateFormat : "Y"
				}
			}
		},
		manyYears : {
			timeColumnWidth : 50,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "Y-m-d",
			shiftUnit : "YEAR",
			shiftIncrement : 1,
			defaultSpan : 1,
			timeResolution : {
				unit : "YEAR",
				increment : 1
			},
			headerConfig : {
				middle : {
					unit : "YEAR",
					align : "center",
					dateFormat : "Y",
					increment : 5
				},
				bottom : {
					unit : "YEAR",
					align : "center",
					increment : 1,
					renderer : Ext.emptyFn
				}
			}
		},
		weekAndDayLetter : {
			timeColumnWidth : 20,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "Y-m-d",
			shiftUnit : "WEEK",
			shiftIncrement : 1,
			defaultSpan : 10,
			timeResolution : {
				unit : "DAY",
				increment : 1
			},
			headerConfig : {
				bottom : {
					unit : "DAY",
					align : "center",
					renderer : function (a) {
						return Ext.Date.dayNames[a.getDay()].substring(0, 1)
					}
				},
				middle : {
					unit : "WEEK",
					dateFormat : "D d M Y"
				}
			}
		},
		weekDateAndMonth : {
			timeColumnWidth : 30,
			rowHeight : 24,
			resourceColumnWidth : 100,
			displayDateFormat : "Y-m-d",
			shiftUnit : "WEEK",
			shiftIncrement : 1,
			defaultSpan : 10,
			timeResolution : {
				unit : "DAY",
				increment : 1
			},
			headerConfig : {
				middle : {
					unit : "WEEK",
					align : "center",
					dateFormat : "d"
				},
				top : {
					unit : "MONTH",
					dateFormat : "Y F"
				}
			}
		}
	}
});
if (!Ext.ClassManager.get("Sch.feature.AbstractTimeSpan")) {
	Ext.define("Sch.feature.AbstractTimeSpan", {
		extend : "Ext.AbstractPlugin",
		mixins : {
			observable : "Ext.util.Observable"
		},
		lockableScope : "top",
		schedulerView : null,
		timeAxis : null,
		containerEl : null,
		expandToFitView : false,
		disabled : false,
		cls : null,
		clsField : "Cls",
		template : null,
		store : null,
		renderElementsBuffered : false,
		renderDelay : 15,
		refreshSizeOnItemUpdate : true,
		_resizeTimer : null,
		_renderTimer : null,
		showHeaderElements : false,
		headerTemplate : null,
		innerHeaderTpl : null,
		headerContainerCls : "sch-header-secondary-canvas",
		headerContainerEl : null,
		renderingDoneEvent : null,
		constructor : function (a) {
			this.uniqueCls = this.uniqueCls || ("sch-timespangroup-" + Ext.id());
			Ext.apply(this, a);
			this.mixins.observable.constructor.call(this);
			this.callParent(arguments)
		},
		setDisabled : function (a) {
			if (a) {
				this.removeElements()
			}
			this.disabled = a
		},
		removeElements : function () {
			this.removeBodyElements();
			if (this.showHeaderElements) {
				this.removeHeaderElements()
			}
		},
		getBodyElements : function () {
			if (this.containerEl) {
				return this.containerEl.select("." + this.uniqueCls)
			}
			return null
		},
		getHeaderContainerEl : function () {
			var c = this.headerContainerEl,
			b = Ext.baseCSSPrefix,
			a;
			if (!c || !c.dom) {
				if (this.schedulerView.isHorizontal()) {
					a = this.panel.getTimeAxisColumn().headerView.containerEl
				} else {
					a = this.panel.el.down("." + b + "grid-inner-locked ." + b + "panel-body ." + b + "grid-view")
				}
				if (a) {
					c = a.down("." + this.headerContainerCls);
					if (!c) {
						c = a.appendChild({
								cls : this.headerContainerCls
							})
					}
					this.headerContainerEl = c
				}
			}
			return c
		},
		getHeaderElements : function () {
			var a = this.getHeaderContainerEl();
			if (a) {
				return a.select("." + this.uniqueCls)
			}
			return null
		},
		removeBodyElements : function () {
			var a = this.getBodyElements();
			if (a) {
				a.each(function (b) {
					b.destroy()
				})
			}
		},
		removeHeaderElements : function () {
			var a = this.getHeaderElements();
			if (a) {
				a.each(function (b) {
					b.destroy()
				})
			}
		},
		getElementId : function (a) {
			return this.uniqueCls + "-" + a.internalId
		},
		getHeaderElementId : function (a) {
			return this.uniqueCls + "-header-" + a.internalId
		},
		getTemplateData : function (a) {
			return this.prepareTemplateData ? this.prepareTemplateData(a) : a.data
		},
		getElementCls : function (a, c) {
			var b = a.clsField || this.clsField;
			if (!c) {
				c = this.getTemplateData(a)
			}
			return this.cls + " " + this.uniqueCls + " " + (c[b] || "")
		},
		getHeaderElementCls : function (a, c) {
			var b = a.clsField || this.clsField;
			if (!c) {
				c = this.getTemplateData(a)
			}
			return "sch-header-indicator " + this.uniqueCls + " " + (c[b] || "")
		},
		init : function (a) {
			if (Ext.versions.touch && !a.isReady()) {
				a.on("viewready", function () {
					this.init(a)
				}, this);
				return
			}
			if (Ext.isString(this.innerHeaderTpl)) {
				this.innerHeaderTpl = new Ext.XTemplate(this.innerHeaderTpl)
			}
			var b = this.innerHeaderTpl;
			if (!this.headerTemplate) {
				this.headerTemplate = new Ext.XTemplate('<tpl for=".">', '<div id="{id}" class="{cls}" style="{side}:{position}px;">' + (b ? "{[this.renderInner(values)]}" : "") + "</div>", "</tpl>", {
						renderInner : function (c) {
							return b.apply(c)
						}
					})
			}
			this.schedulerView = a.getSchedulingView();
			this.panel = a;
			this.timeAxis = a.getTimeAxis();
			this.store = Ext.StoreManager.lookup(this.store);
			if (!this.store) {
				Ext.Error.raise("Error: You must define a store for this plugin")
			}
			if (!this.schedulerView.getEl()) {
				this.schedulerView.on({
					afterrender : this.onAfterRender,
					scope : this
				})
			} else {
				this.onAfterRender()
			}
			this.schedulerView.on({
				destroy : this.onDestroy,
				scope : this
			})
		},
		onAfterRender : function (c) {
			var a = this.schedulerView;
			this.containerEl = a.getSecondaryCanvasEl();
			this.storeListeners = {
				load : this.renderElements,
				datachanged : this.renderElements,
				clear : this.renderElements,
				add : this.refreshSingle,
				remove : this.renderElements,
				update : this.refreshSingle,
				addrecords : this.refreshSingle,
				removerecords : this.renderElements,
				updaterecord : this.refreshSingle,
				scope : this
			};
			this.store.on(this.storeListeners);
			if (Ext.data.NodeStore && a.store instanceof Ext.data.NodeStore) {
				if (a.animate) {}
				else {
					a.mon(a.store, {
						expand : this.renderElements,
						collapse : this.renderElements,
						scope : this
					})
				}
			}
			a.on({
				bufferedrefresh : this.renderElements,
				refresh : this.renderElements,
				itemadd : this.refreshSizeOnItemUpdate ? this.refreshSizes : this.renderElements,
				itemremove : this.refreshSizeOnItemUpdate ? this.refreshSizes : this.renderElements,
				itemupdate : this.refreshSizeOnItemUpdate ? this.refreshSizes : this.renderElements,
				groupexpand : this.renderElements,
				groupcollapse : this.renderElements,
				columnwidthchange : this.renderElements,
				resize : this.renderElements,
				scope : this
			});
			if (a.headerCt) {
				a.headerCt.on({
					add : this.renderElements,
					remove : this.renderElements,
					scope : this
				})
			}
			this.panel.on({
				viewchange : this.renderElements,
				show : this.refreshSizes,
				orientationchange : this.forceNewRenderingTimeout,
				scope : this
			});
			var b = a.getRowContainerEl();
			if (b && b.down(".sch-timetd")) {
				this.renderElements()
			}
		},
		forceNewRenderingTimeout : function () {
			this.renderElementsBuffered = false;
			clearTimeout(this._renderTimer);
			clearTimeout(this._resizeTimer);
			this.renderElements()
		},
		refreshSizesInternal : function () {
			if (!this.schedulerView.isDestroyed && this.schedulerView.isHorizontal()) {
				var a = this.schedulerView.getTimeSpanRegion(new Date(), null, this.expandToFitView);
				this.getBodyElements().setHeight(a.bottom - a.top)
			}
		},
		refreshSizes : function () {
			clearTimeout(this._resizeTimer);
			this._resizeTimer = Ext.Function.defer(this.refreshSizesInternal, this.renderDelay, this)
		},
		renderElements : function () {
			if (this.renderElementsBuffered || this.disabled) {
				return
			}
			this.renderElementsBuffered = true;
			clearTimeout(this._renderTimer);
			this._renderTimer = Ext.Function.defer(this.renderElementsInternal, this.renderDelay, this)
		},
		setElementX : function (b, a) {
			if (this.panel.rtl) {
				b.setRight(a)
			} else {
				b.setLeft(a)
			}
		},
		getHeaderElementPosition : function (b) {
			var a = this.schedulerView.getTimeAxisViewModel();
			return Math.round(a.getPositionFromDate(b))
		},
		renderBodyElementsInternal : function (a) {
			Ext.DomHelper.append(this.containerEl, this.generateMarkup(false, a))
		},
		getHeaderElementData : function (a, b) {
			throw "Abstract method call"
		},
		renderHeaderElementsInternal : function (a) {
			var b = this.getHeaderContainerEl();
			if (b) {
				Ext.DomHelper.append(b, this.generateHeaderMarkup(false, a))
			}
		},
		renderElementsInternal : function () {
			this.renderElementsBuffered = false;
			if (this.disabled || this.schedulerView.isDestroyed) {
				return
			}
			if (Ext.versions.extjs && !this.schedulerView.el.down("table")) {
				return
			}
			this.removeElements();
			this.renderBodyElementsInternal();
			if (this.showHeaderElements) {
				this.headerContainerEl = null;
				this.renderHeaderElementsInternal()
			}
			if (this.renderingDoneEvent) {
				this.fireEvent(this.renderingDoneEvent, this)
			}
		},
		generateMarkup : function (c, b) {
			var e = this.timeAxis.getStart(),
			a = this.timeAxis.getEnd(),
			d = this.getElementData(e, a, b, c);
			return this.template.apply(d)
		},
		generateHeaderMarkup : function (b, a) {
			var c = this.getHeaderElementData(a, b);
			return this.headerTemplate.apply(c)
		},
		getElementData : function (d, c, a, b) {
			throw "Abstract method call"
		},
		updateBodyElement : function (b) {
			var c = Ext.get(this.getElementId(b));
			if (c) {
				var e = this.timeAxis.getStart(),
				a = this.timeAxis.getEnd(),
				d = this.getElementData(e, a, [b])[0];
				if (d) {
					c.dom.className = d.$cls;
					c.setTop(d.top);
					this.setElementX(c, d.left);
					c.setSize(d.width, d.height)
				} else {
					Ext.destroy(c)
				}
			} else {
				this.renderBodyElementsInternal([b])
			}
		},
		updateHeaderElement : function (a) {
			var b = Ext.get(this.getHeaderElementId(a));
			if (b) {
				var c = this.getHeaderElementData([a])[0];
				if (c) {
					b.dom.className = c.cls;
					if (this.schedulerView.isHorizontal()) {
						this.setElementX(b, c.position);
						b.setWidth(c.size)
					} else {
						b.setTop(c.position);
						b.setHeight(c.size)
					}
				} else {
					Ext.destroy(b)
				}
			} else {
				this.renderHeaderElementsInternal([a])
			}
		},
		onDestroy : function () {
			clearTimeout(this._renderTimer);
			clearTimeout(this._resizeTimer);
			if (this.store.autoDestroy) {
				this.store.destroy()
			}
			this.store.un(this.storeListeners)
		},
		refreshSingle : function (b, a) {
			Ext.each(a, this.updateBodyElement, this);
			if (this.showHeaderElements) {
				Ext.each(a, this.updateHeaderElement, this)
			}
		}
	})
}
Ext.define("Sch.plugin.Lines", {
	extend : "Sch.feature.AbstractTimeSpan",
	alias : "plugin.scheduler_lines",
	cls : "sch-timeline",
	showTip : true,
	innerTpl : null,
	prepareTemplateData : null,
	side : null,
	init : function (a) {
		if (Ext.isString(this.innerTpl)) {
			this.innerTpl = new Ext.XTemplate(this.innerTpl)
		}
		this.side = a.rtl ? "right" : "left";
		var b = this.innerTpl;
		if (!this.template) {
			this.template = new Ext.XTemplate('<tpl for=".">', '<div id="{id}" ' + (this.showTip ? 'title="{[this.getTipText(values)]}" ' : "") + 'class="{$cls}" style="' + this.side + ':{left}px;top:{top}px;height:{height}px;width:{width}px">' + (b ? "{[this.renderInner(values)]}" : "") + "</div>", "</tpl>", {
					getTipText : function (c) {
						return a.getSchedulingView().getFormattedDate(c.Date) + " " + (c.Text || "")
					},
					renderInner : function (c) {
						return b.apply(c)
					}
				})
		}
		this.callParent(arguments)
	},
	getElementData : function (m, q, c) {
		var t = this.store,
		j = this.schedulerView,
		p = j.isHorizontal(),
		f = c || t.getRange(),
		h = [],
		r,
		a,
		o = j.getTimeSpanRegion(m, null, this.expandToFitView),
		k,
		b,
		e;
		if (Ext.versions.touch) {
			r = "100%"
		} else {
			r = p ? o.bottom - o.top : 1
		}
		a = p ? 1 : o.right - o.left;
		for (var g = 0, d = f.length; g < d; g++) {
			k = f[g];
			b = k.get("Date");
			if (b && Sch.util.Date.betweenLesser(b, m, q)) {
				var n = j.getCoordinateFromDate(b);
				e = Ext.apply({}, this.getTemplateData(k));
				e.id = this.getElementId(k);
				e.$cls = this.getElementCls(k, e);
				e.width = a;
				e.height = r;
				if (p) {
					e.left = n
				} else {
					e.top = n
				}
				h.push(e)
			}
		}
		return h
	},
	getHeaderElementData : function (c) {
		var a = this.timeAxis.getStart(),
		k = this.timeAxis.getEnd(),
		m = this.schedulerView.isHorizontal(),
		g = [],
		h,
		b,
		j,
		e;
		c = c || this.store.getRange();
		for (var f = 0, d = c.length; f < d; f++) {
			h = c[f];
			b = h.get("Date");
			if (b && Sch.util.Date.betweenLesser(b, a, k)) {
				j = this.getHeaderElementPosition(b);
				e = this.getTemplateData(h);
				g.push(Ext.apply({
						id : this.getHeaderElementId(h),
						side : m ? this.side : "top",
						cls : this.getHeaderElementCls(h, e),
						position : j
					}, e))
			}
		}
		return g
	}
});
Ext.define("Sch.plugin.Zones", {
	extend : "Sch.feature.AbstractTimeSpan",
	alias : "plugin.scheduler_zones",
	requires : ["Sch.model.Range"],
	innerTpl : null,
	cls : "sch-zone",
	side : null,
	init : function (a) {
		if (Ext.isString(this.innerTpl)) {
			this.innerTpl = new Ext.XTemplate(this.innerTpl)
		}
		this.side = a.rtl ? "right" : "left";
		var b = this.innerTpl;
		if (!this.template) {
			this.template = new Ext.XTemplate('<tpl for="."><div id="{id}" class="{$cls}" style="' + this.side + ':{left}px;top:{top}px;height:{height}px;width:{width}px;{style}">' + (b ? "{[this.renderInner(values)]}" : "") + "</div></tpl>", {
					renderInner : function (c) {
						return b.apply(c)
					}
				})
		}
		if (Ext.isString(this.innerHeaderTpl)) {
			this.innerHeaderTpl = new Ext.XTemplate(this.innerHeaderTpl)
		}
		this.callParent(arguments)
	},
	getElementData : function (h, d, r, f) {
		var g = this.schedulerView,
		s = [],
		c = g.getTimeSpanRegion(h, d, this.expandToFitView),
		k = this.schedulerView.isHorizontal(),
		b,
		m,
		a,
		j,
		n,
		e;
		r = r || this.store.getRange();
		for (var q = 0, p = r.length; q < p; q++) {
			b = r[q];
			m = b.getStartDate();
			a = b.getEndDate();
			e = this.getTemplateData(b);
			if (m && a && Sch.util.Date.intersectSpans(m, a, h, d)) {
				var t = g.getCoordinateFromDate(Sch.util.Date.max(m, h));
				var o = g.getCoordinateFromDate(Sch.util.Date.min(a, d));
				j = Ext.apply({}, e);
				j.id = this.getElementId(b);
				j.$cls = this.getElementCls(b, e);
				if (k) {
					j.left = t;
					j.top = c.top;
					j.width = f ? 0 : o - t;
					j.height = c.bottom - c.top;
					j.style = f ? ("border-left-width:" + (o - t) + "px") : ""
				} else {
					j.left = c.left;
					j.top = t;
					j.height = f ? 0 : o - t;
					j.width = c.right - c.left;
					j.style = f ? ("border-top-width:" + (o - t) + "px") : ""
				}
				s.push(j)
			}
		}
		return s
	},
	getHeaderElementId : function (b, a) {
		return this.callParent([b]) + (a ? "-start" : "-end")
	},
	getHeaderElementCls : function (b, d, a) {
		var c = b.clsField || this.clsField;
		if (!d) {
			d = this.getTemplateData(b)
		}
		return "sch-header-indicator sch-header-indicator-" + (a ? "start " : "end ") + this.uniqueCls + " " + (d[c] || "")
	},
	getZoneHeaderElementData : function (b, h, f, a) {
		var c = a ? f.getStartDate() : f.getEndDate(),
		e = null,
		g,
		i,
		d;
		if (c && Sch.util.Date.betweenLesser(c, b, h)) {
			g = this.getHeaderElementPosition(c);
			i = this.schedulerView.isHorizontal();
			d = this.getTemplateData(f);
			e = Ext.apply({
					id : this.getHeaderElementId(f, a),
					cls : this.getHeaderElementCls(f, d, a),
					isStart : a,
					side : i ? this.side : "top",
					position : g
				}, d)
		}
		return e
	},
	getHeaderElementData : function (b) {
		var a = this.timeAxis.getStart(),
		h = this.timeAxis.getEnd(),
		e = [],
		g,
		d,
		j;
		b = b || this.store.getRange();
		for (var f = 0, c = b.length; f < c; f++) {
			g = b[f];
			d = this.getZoneHeaderElementData(a, h, g, true);
			if (d) {
				e.push(d)
			}
			j = this.getZoneHeaderElementData(a, h, g, false);
			if (j) {
				e.push(j)
			}
		}
		return e
	},
	updateZoneHeaderElement : function (a, b) {
		a.dom.className = b.cls;
		if (this.schedulerView.isHorizontal()) {
			this.setElementX(a, b.position)
		} else {
			a.setTop(b.position)
		}
	},
	updateHeaderElement : function (c) {
		var a = this.timeAxis.getStart(),
		g = this.timeAxis.getEnd(),
		f = Ext.get(this.getHeaderElementId(c, true)),
		e = Ext.get(this.getHeaderElementId(c, false)),
		d = this.getZoneHeaderElementData(a, g, c, true),
		b = this.getZoneHeaderElementData(a, g, c, false);
		if (!(f && b) || !(e && b)) {
			Ext.destroy(f, e);
			this.renderHeaderElementsInternal([c])
		} else {
			if (f) {
				if (!d) {
					Ext.destroy(f)
				} else {
					this.updateZoneHeaderElement(f, d)
				}
			}
			if (e) {
				if (!b) {
					Ext.destroy(e)
				} else {
					this.updateZoneHeaderElement(e, b)
				}
			}
		}
	}
});
Ext.define("Sch.plugin.Pan", {
	extend : "Ext.AbstractPlugin",
	alias : "plugin.scheduler_pan",
	lockableScope : "top",
	enableVerticalPan : true,
	statics : {
		KEY_SHIFT : 1,
		KEY_CTRL : 2,
		KEY_ALT : 4,
		KEY_ALL : 7
	},
	disableOnKey : 0,
	panel : null,
	constructor : function (a) {
		Ext.apply(this, a)
	},
	init : function (a) {
		this.panel = a.normalGrid || a;
		this.view = a.getSchedulingView();
		this.view.on("afterrender", this.onRender, this)
	},
	onRender : function (a) {
		this.view.el.on("mousedown", this.onMouseDown, this)
	},
	onMouseDown : function (d, c) {
		var b = this.self,
		a = this.disableOnKey;
		if ((d.shiftKey && (a & b.KEY_SHIFT)) || (d.ctrlKey && (a & b.KEY_CTRL)) || (d.altKey && (a & b.KEY_ALT))) {
			return
		}
		if (d.getTarget("." + this.view.timeCellCls, 10) && !d.getTarget(this.view.eventSelector)) {
			this.mouseX = d.getPageX();
			this.mouseY = d.getPageY();
			Ext.getBody().on("mousemove", this.onMouseMove, this);
			Ext.getDoc().on("mouseup", this.onMouseUp, this);
			if (Ext.isIE || Ext.isGecko) {
				Ext.getBody().on("mouseenter", this.onMouseUp, this)
			}
			d.stopEvent()
		}
	},
	onMouseMove : function (d) {
		d.stopEvent();
		var a = d.getPageX(),
		f = d.getPageY(),
		c = a - this.mouseX,
		b = f - this.mouseY;
		this.panel.scrollByDeltaX(-c);
		this.mouseX = a;
		this.mouseY = f;
		if (this.enableVerticalPan) {
			this.panel.scrollByDeltaY(-b)
		}
	},
	onMouseUp : function (a) {
		Ext.getBody().un("mousemove", this.onMouseMove, this);
		Ext.getDoc().un("mouseup", this.onMouseUp, this);
		if (Ext.isIE || Ext.isGecko) {
			Ext.getBody().un("mouseenter", this.onMouseUp, this)
		}
	}
});
Ext.define("Sch.tooltip.ClockTemplate", {
	extend : "Ext.XTemplate",
	constructor : function () {
		var i = Math.PI / 180,
		l = Math.cos,
		j = Math.sin,
		m = 7,
		c = 2,
		d = 10,
		k = 6,
		f = 3,
		a = 10,
		e = Ext.isIE && (Ext.isIE8m || Ext.isIEQuirks);
		function b(n) {
			var q = n * i,
			o = l(q),
			t = j(q),
			r = k * j((90 - n) * i),
			s = k * l((90 - n) * i),
			u = Math.min(k, k - r),
			p = n > 180 ? s : 0,
			v = "progid:DXImageTransform.Microsoft.Matrix(sizingMethod='auto expand', M11 = " + o + ", M12 = " + (-t) + ", M21 = " + t + ", M22 = " + o + ")";
			return Ext.String.format("filter:{0};-ms-filter:{0};top:{1}px;left:{2}px;", v, u + f, p + a)
		}
		function h(n) {
			var q = n * i,
			o = l(q),
			t = j(q),
			r = m * j((90 - n) * i),
			s = m * l((90 - n) * i),
			u = Math.min(m, m - r),
			p = n > 180 ? s : 0,
			v = "progid:DXImageTransform.Microsoft.Matrix(sizingMethod='auto expand', M11 = " + o + ", M12 = " + (-t) + ", M21 = " + t + ", M22 = " + o + ")";
			return Ext.String.format("filter:{0};-ms-filter:{0};top:{1}px;left:{2}px;", v, u + c, p + d)
		}
		function g(n) {
			return Ext.String.format("transform:rotate({0}deg);-ms-transform:rotate({0}deg);-moz-transform: rotate({0}deg);-webkit-transform: rotate({0}deg);-o-transform:rotate({0}deg);", n)
		}
		this.callParent(['<div class="sch-clockwrap {cls}"><div class="sch-clock"><div class="sch-hourIndicator" style="{[this.getHourStyle((values.date.getHours()%12) * 30)]}">{[Ext.Date.monthNames[values.date.getMonth()].substr(0,3)]}</div><div class="sch-minuteIndicator" style="{[this.getMinuteStyle(values.date.getMinutes() * 6)]}">{[values.date.getDate()]}</div></div><span class="sch-clock-text">{text}</span></div>', {
					compiled : true,
					disableFormats : true,
					getMinuteStyle : e ? h : g,
					getHourStyle : e ? b : g
				}
			])
	}
});
Ext.define("Sch.tooltip.Tooltip", {
	extend : "Ext.tip.ToolTip",
	requires : ["Sch.tooltip.ClockTemplate"],
	autoHide : false,
	anchor : "b",
	padding : "0 3 0 0",
	showDelay : 0,
	hideDelay : 0,
	quickShowInterval : 0,
	dismissDelay : 0,
	trackMouse : false,
	valid : true,
	anchorOffset : 5,
	shadow : false,
	frame : false,
	constructor : function (b) {
		var a = new Sch.tooltip.ClockTemplate();
		this.renderTo = document.body;
		this.startDate = this.endDate = new Date();
		if (!this.template) {
			this.template = Ext.create("Ext.XTemplate", '<div class="{[values.valid ? "sch-tip-ok" : "sch-tip-notok"]}">', '{[this.renderClock(values.startDate, values.startText, "sch-tooltip-startdate")]}', '{[this.renderClock(values.endDate, values.endText, "sch-tooltip-enddate")]}', "</div>", {
					compiled : true,
					disableFormats : true,
					renderClock : function (d, e, c) {
						return a.apply({
							date : d,
							text : e,
							cls : c
						})
					}
				})
		}
		this.callParent(arguments)
	},
	update : function (a, e, d, f) {
		if (this.startDate - a !== 0 || this.endDate - e !== 0 || this.valid !== d || f) {
			this.startDate = a;
			this.endDate = e;
			this.valid = d;
			var c = this.schedulerView.getFormattedDate(a),
			b = this.schedulerView.getFormattedEndDate(e, a);
			if (this.mode === "calendar" && e.getHours() === 0 && e.getMinutes() === 0 && !(e.getYear() === a.getYear() && e.getMonth() === a.getMonth() && e.getDate() === a.getDate())) {
				e = Sch.util.Date.add(e, Sch.util.Date.DAY, -1)
			}
			this.callParent([this.template.apply({
						valid : d,
						startDate : a,
						endDate : e,
						startText : c,
						endText : b
					})])
		}
	},
	show : function (b, a) {
		if (!b) {
			return
		}
		a = a || 18;
		if (Sch.util.Date.compareUnits(this.schedulerView.getTimeResolution().unit, Sch.util.Date.DAY) >= 0) {
			this.mode = "calendar";
			this.addCls("sch-day-resolution")
		} else {
			this.mode = "clock";
			this.removeCls("sch-day-resolution")
		}
		this.mouseOffsets = [a - 18, -7];
		this.setTarget(b);
		this.callParent();
		this.alignTo(b, "bl-tl", this.mouseOffsets);
		this.mon(Ext.getDoc(), "mousemove", this.onMyMouseMove, this);
		this.mon(Ext.getDoc(), "mouseup", this.onMyMouseUp, this, {
			single : true
		})
	},
	onMyMouseMove : function () {
		this.el.alignTo(this.target, "bl-tl", this.mouseOffsets)
	},
	onMyMouseUp : function () {
		this.mun(Ext.getDoc(), "mousemove", this.onMyMouseMove, this)
	},
	afterRender : function () {
		this.callParent(arguments);
		this.el.on("mouseenter", this.onElMouseEnter, this)
	},
	onElMouseEnter : function () {
		this.alignTo(this.target, "bl-tl", this.mouseOffsets)
	}
});
if (!Ext.ClassManager.get("Sch.view.model.TimeAxis")) {
	Ext.define("Sch.view.model.TimeAxis", {
		extend : "Ext.util.Observable",
		requires : ["Ext.Date", "Sch.util.Date", "Sch.preset.Manager"],
		timeAxis : null,
		availableWidth : 0,
		tickWidth : 100,
		snapToIncrement : false,
		forceFit : false,
		headerConfig : null,
		headers : null,
		mainHeader : 0,
		timeAxisColumnWidth : null,
		resourceColumnWidth : null,
		timeColumnWidth : null,
		rowHeightHorizontal : null,
		rowHeightVertical : null,
		orientation : "horizontal",
		suppressFit : false,
		refCount : 0,
		columnConfig : {},
		viewPreset : null,
		columnLinesFor : "middle",
		eventStore : null,
		constructor : function (a) {
			var c = this;
			Ext.apply(this, a);
			if (this.viewPreset) {
				var b = Sch.preset.Manager.getPreset(this.viewPreset);
				b && this.consumeViewPreset(b)
			}
			c.timeAxis.on("reconfigure", c.onTimeAxisReconfigure, c);
			this.callParent(arguments)
		},
		destroy : function () {
			this.timeAxis.un("reconfigure", this.onTimeAxisReconfigure, this)
		},
		onTimeAxisReconfigure : function (b, a, c) {
			if (!c) {
				this.update()
			}
		},
		reconfigure : function (a) {
			this.headers = null;
			Ext.apply(this, a);
			if (this.orientation == "horizontal") {
				this.setTickWidth(this.timeColumnWidth)
			} else {
				this.setTickWidth(this.rowHeightVertical)
			}
			this.fireEvent("reconfigure", this)
		},
		getColumnConfig : function () {
			return this.columnConfig
		},
		update : function (d, b) {
			var e = this.timeAxis,
			c = this.headerConfig;
			this.availableWidth = Math.max(d || this.availableWidth, 0);
			if (!Ext.isNumber(this.availableWidth)) {
				throw "Invalid available width provided to Sch.view.model.TimeAxis"
			}
			if (this.forceFit && this.availableWidth <= 0) {
				return
			}
			this.columnConfig = {};
			for (var f in c) {
				if (c[f].cellGenerator) {
					this.columnConfig[f] = c[f].cellGenerator.call(this, e.getStart(), e.getEnd())
				} else {
					this.columnConfig[f] = this.createHeaderRow(f, c[f])
				}
			}
			var a = this.calculateTickWidth(this.getTickWidth());
			if (!Ext.isNumber(a) || a <= 0) {
				throw "Invalid column width calculated in Sch.view.model.TimeAxis"
			}
			this.updateTickWidth(a);
			if (!b) {
				this.fireEvent("update", this)
			}
		},
		createHeaderRow : function (a, d) {
			var c = [],
			e = this,
			f = d.align,
			b = Ext.Date.clearTime(new Date());
			e.forEachInterval(a, function (k, g, h) {
				var j = {
					align : f,
					start : k,
					end : g,
					headerCls : ""
				};
				if (d.renderer) {
					j.header = d.renderer.call(d.scope || e, k, g, j, h, e.eventStore)
				} else {
					j.header = Ext.Date.format(k, d.dateFormat)
				}
				if (d.unit === Sch.util.Date.DAY && (!d.increment || d.increment === 1)) {
					j.headerCls += " sch-dayheadercell-" + k.getDay();
					if (Ext.Date.clearTime(k, true) - b === 0) {
						j.headerCls += " sch-dayheadercell-today"
					}
				}
				c.push(j)
			});
			return c
		},
		getDistanceBetweenDates : function (b, a) {
			return Math.round(this.getPositionFromDate(a) - this.getPositionFromDate(b))
		},
		getPositionFromDate : function (a) {
			var c = -1,
			b = this.timeAxis.getTickFromDate(a);
			if (b >= 0) {
				c = Math.round(this.tickWidth * (b - this.timeAxis.visibleTickStart))
			}
			return c
		},
		getDateFromPosition : function (a, d) {
			var c = a / this.getTickWidth() + this.timeAxis.visibleTickStart,
			b = this.timeAxis.getCount();
			if (c < 0 || c > b) {
				return null
			}
			return this.timeAxis.getDateFromTick(c, d)
		},
		getSingleUnitInPixels : function (a) {
			return Sch.util.Date.getUnitToBaseUnitRatio(this.timeAxis.getUnit(), a) * this.tickWidth / this.timeAxis.increment
		},
		getSnapPixelAmount : function () {
			if (this.snapToIncrement) {
				var a = this.timeAxis.getResolution();
				return (a.increment || 1) * this.getSingleUnitInPixels(a.unit)
			} else {
				return 1
			}
		},
		getTickWidth : function () {
			return this.tickWidth
		},
		setTickWidth : function (b, a) {
			this.updateTickWidth(b);
			this.update(null, a)
		},
		updateTickWidth : function (a) {
			this.tickWidth = a;
			if (this.orientation == "horizontal") {
				this.timeColumnWidth = a
			} else {
				this.rowHeightVertical = a
			}
		},
		getTotalWidth : function () {
			return Math.round(this.tickWidth * this.timeAxis.getVisibleTickTimeSpan())
		},
		calculateTickWidth : function (d) {
			var j = this.forceFit;
			var g = this.timeAxis;
			var b = 0,
			f = g.getUnit(),
			i = Number.MAX_VALUE,
			c = Sch.util.Date;
			if (this.snapToIncrement) {
				var e = g.getResolution();
				i = c.getUnitToBaseUnitRatio(f, e.unit) * e.increment
			} else {
				var h = c.getMeasuringUnit(f);
				i = Math.min(i, c.getUnitToBaseUnitRatio(f, h))
			}
			var a = Math[j ? "floor" : "round"](this.getAvailableWidth() / g.getVisibleTickTimeSpan());
			if (!this.suppressFit) {
				b = (j || d < a) ? a : d;
				if (i > 0 && (!j || i < 1)) {
					b = Math.round(Math.max(1, Math[j ? "floor" : "round"](i * b)) / i)
				}
			} else {
				b = d
			}
			return b
		},
		getAvailableWidth : function () {
			return this.availableWidth
		},
		setAvailableWidth : function (a) {
			this.availableWidth = Math.max(0, a);
			var b = this.calculateTickWidth(this.tickWidth);
			if (b !== this.tickWidth) {
				this.setTickWidth(b)
			}
		},
		fitToAvailableWidth : function (a) {
			var b = Math.floor(this.availableWidth / this.timeAxis.getVisibleTickTimeSpan());
			this.setTickWidth(b, a)
		},
		setForceFit : function (a) {
			if (a !== this.forceFit) {
				this.forceFit = a;
				this.update()
			}
		},
		setSnapToIncrement : function (a) {
			if (a !== this.snapToIncrement) {
				this.snapToIncrement = a;
				this.update()
			}
		},
		getViewRowHeight : function () {
			var a = this.orientation == "horizontal" ? this.rowHeightHorizontal : this.rowHeightVertical;
			if (!a) {
				throw "rowHeight info not available"
			}
			return a
		},
		setViewRowHeight : function (c, a) {
			var d = this.orientation === "horizontal";
			var b = "rowHeight" + Ext.String.capitalize(this.orientation);
			if (this[b] != c) {
				this[b] = c;
				if (d) {
					if (!a) {
						this.fireEvent("update", this)
					}
				} else {
					this.setTickWidth(c, a)
				}
			}
		},
		setViewColumnWidth : function (b, a) {
			if (this.orientation === "horizontal") {
				this.setTickWidth(b, a)
			} else {
				this.resourceColumnWidth = b
			}
			if (!a) {
				this.fireEvent("columnwidthchange", this, b)
			}
		},
		getHeaders : function () {
			if (this.headers) {
				return this.headers
			}
			var a = this.headerConfig;
			this.mainHeader = a.top ? 1 : 0;
			return this.headers = [].concat(a.top || [], a.middle || [], a.bottom || [])
		},
		getMainHeader : function () {
			return this.getHeaders()[this.mainHeader]
		},
		getBottomHeader : function () {
			var a = this.getHeaders();
			return a[a.length - 1]
		},
		forEachInterval : function (b, a, d) {
			d = d || this;
			var c = this.headerConfig;
			if (!c) {
				return
			}
			if (b === "top" || (b === "middle" && c.bottom)) {
				var e = c[b];
				this.timeAxis.forEachAuxInterval(e.unit, e.increment, a, d)
			} else {
				this.timeAxis.each(function (g, f) {
					return a.call(d, g.data.start, g.data.end, f)
				})
			}
		},
		forEachMainInterval : function (a, b) {
			this.forEachInterval("middle", a, b)
		},
		consumeViewPreset : function (a) {
			this.headers = null;
			var b = this.orientation == "horizontal";
			Ext.apply(this, {
				headerConfig : a.headerConfig,
				columnLinesFor : a.columnLinesFor || "middle",
				rowHeightHorizontal : a.rowHeight,
				tickWidth : b ? a.timeColumnWidth : a.timeRowHeight || a.timeColumnWidth || 60,
				timeColumnWidth : a.timeColumnWidth,
				rowHeightVertical : a.timeRowHeight || a.timeColumnWidth || 60,
				timeAxisColumnWidth : a.timeAxisColumnWidth,
				resourceColumnWidth : a.resourceColumnWidth || 100
			})
		}
	})
}
Ext.define("Sch.view.HorizontalTimeAxis", {
	extend : "Ext.util.Observable",
	requires : ["Ext.XTemplate"],
	trackHeaderOver : true,
	compactCellWidthThreshold : 15,
	baseCls : "sch-column-header",
	tableCls : "sch-header-row",
	headerHtmlRowTpl : '<table border="0" cellspacing="0" cellpadding="0" style="width: {totalWidth}px; {tstyle}" class="{{tableCls}} sch-header-row-{position} {cls}"><thead><tr><tpl for="cells"><td class="{{baseCls}} {headerCls}" style="position : static; text-align: {align}; width: {width}px; {style}" tabIndex="0"headerPosition="{parent.position}" headerIndex="{[xindex-1]}"><div class="sch-simple-timeheader">{header}</div></td></tpl></tr></thead></table>',
	model : null,
	hoverCls : "",
	containerEl : null,
	height : null,
	constructor : function (d) {
		var e = this;
		var b = !!Ext.versions.touch;
		var a = b ? "tap" : "click";
		Ext.apply(this, d);
		e.callParent(arguments);
		e.model.on("update", e.onModelUpdate, this, {
			priority : 5
		});
		e.containerEl = Ext.get(e.containerEl);
		if (!(e.headerHtmlRowTpl instanceof Ext.Template)) {
			e.headerHtmlRowTpl = e.headerHtmlRowTpl.replace("{{baseCls}}", this.baseCls).replace("{{tableCls}}", this.tableCls);
			e.headerHtmlRowTpl = new Ext.XTemplate(e.headerHtmlRowTpl)
		}
		if (e.trackHeaderOver && e.hoverCls) {
			e.containerEl.on({
				mousemove : e.highlightCell,
				delegate : ".sch-column-header",
				scope : e
			});
			e.containerEl.on({
				mouseleave : e.clearHighlight,
				scope : e
			})
		}
		var c = {
			scope : this,
			delegate : ".sch-column-header"
		};
		if (b) {
			c.tap = this.onElClick("tap");
			c.doubletap = this.onElClick("doubletap")
		} else {
			c.click = this.onElClick("click");
			c.dblclick = this.onElClick("dblclick");
			c.contextmenu = this.onElClick("contextmenu")
		}
		e._listenerCfg = c;
		if (e.containerEl) {
			e.containerEl.on(c)
		}
	},
	destroy : function () {
		var a = this;
		if (a.containerEl) {
			a.containerEl.un(a._listenerCfg);
			a.containerEl.un({
				mousemove : a.highlightCell,
				delegate : ".sch-simple-timeheader",
				scope : a
			});
			a.containerEl.un({
				mouseleave : a.clearHighlight,
				scope : a
			})
		}
		a.model.un({
			update : a.onModelUpdate,
			scope : a
		})
	},
	onModelUpdate : function () {
		this.render()
	},
	getHTML : function (e, h, d) {
		var i = this.model.getColumnConfig();
		var g = this.model.getTotalWidth();
		var c = Ext.Object.getKeys(i).length;
		var b = this.height ? this.height / c : 0;
		var f = "";
		var a;
		if (i.top) {
			this.embedCellWidths(i.top);
			f += this.headerHtmlRowTpl.apply({
				totalWidth : g,
				cells : i.top,
				position : "top",
				tstyle : "border-top : 0;" + (b ? "height:" + b + "px" : "")
			})
		}
		if (i.middle) {
			this.embedCellWidths(i.middle);
			f += this.headerHtmlRowTpl.apply({
				totalWidth : g,
				cells : i.middle,
				position : "middle",
				tstyle : (i.top ? "" : "border-top : 0;") + (b ? "height:" + b + "px" : ""),
				cls : !i.bottom && this.model.getTickWidth() <= this.compactCellWidthThreshold ? "sch-header-row-compact" : ""
			})
		}
		if (i.bottom) {
			this.embedCellWidths(i.bottom);
			f += this.headerHtmlRowTpl.apply({
				totalWidth : g,
				cells : i.bottom,
				position : "bottom",
				tstyle : (b ? "height:" + b + "px" : ""),
				cls : this.model.getTickWidth() <= this.compactCellWidthThreshold ? "sch-header-row-compact" : ""
			})
		}
		return f + '<div class="sch-header-secondary-canvas"></div>'
	},
	render : function () {
		if (!this.containerEl) {
			return
		}
		var e = this.containerEl,
		f = e.dom,
		d = f.style.display,
		a = this.model.getColumnConfig(),
		b = f.parentNode;
		f.style.display = "none";
		b.removeChild(f);
		var c = this.getHTML();
		f.innerHTML = c;
		if (!a.top && !a.middle) {
			this.containerEl.addCls("sch-header-single-row")
		} else {
			this.containerEl.removeCls("sch-header-single-row")
		}
		b && b.appendChild(f);
		f.style.display = d;
		this.fireEvent("refresh", this)
	},
	embedCellWidths : function (b) {
		var e = (Ext.isIE7 || Ext.isSafari) ? 1 : 0;
		for (var c = 0; c < b.length; c++) {
			var a = b[c];
			var d = this.model.getDistanceBetweenDates(a.start, a.end);
			if (d) {
				a.width = d - (c ? e : 0)
			} else {
				a.width = 0;
				a.style = "display: none"
			}
		}
	},
	onElClick : function (a) {
		return function (e, f) {
			f = e.delegatedTarget || f;
			var b = Ext.fly(f).getAttribute("headerPosition"),
			c = Ext.fly(f).getAttribute("headerIndex"),
			d = this.model.getColumnConfig()[b][c];
			this.fireEvent("timeheader" + a, this, d.start, d.end, e)
		}
	},
	highlightCell : function (c, a) {
		var b = this;
		if (a !== b.highlightedCell) {
			b.clearHighlight();
			b.highlightedCell = a;
			Ext.fly(a).addCls(b.hoverCls)
		}
	},
	clearHighlight : function () {
		var b = this,
		a = b.highlightedCell;
		if (a) {
			Ext.fly(a).removeCls(b.hoverCls);
			delete b.highlightedCell
		}
	}
});
Ext.define("Sch.column.timeAxis.Horizontal", {
	extend : "Ext.grid.column.Column",
	alias : "widget.timeaxiscolumn",
	draggable : false,
	groupable : false,
	hideable : false,
	sortable : false,
	fixed : true,
	menuDisabled : true,
	cls : "sch-simple-timeaxis",
	tdCls : "sch-timetd",
	enableLocking : false,
	requires : ["Sch.view.HorizontalTimeAxis"],
	timeAxisViewModel : null,
	headerView : null,
	hoverCls : "",
	ownHoverCls : "sch-column-header-over",
	trackHeaderOver : true,
	compactCellWidthThreshold : 20,
	initComponent : function () {
		this.callParent(arguments)
	},
	afterRender : function () {
		var a = this;
		a.headerView = new Sch.view.HorizontalTimeAxis({
				model : a.timeAxisViewModel,
				containerEl : a.titleEl,
				hoverCls : a.ownHoverCls,
				trackHeaderOver : a.trackHeaderOver,
				compactCellWidthThreshold : a.compactCellWidthThreshold
			});
		a.headerView.on("refresh", a.onTimeAxisViewRefresh, a);
		a.ownerCt.on("afterlayout", function () {
			a.mon(a.ownerCt, "resize", a.onHeaderContainerResize, a);
			if (this.getWidth() > 0) {
				if (a.getAvailableWidthForSchedule() === a.timeAxisViewModel.getAvailableWidth()) {
					a.headerView.render()
				} else {
					a.timeAxisViewModel.update(a.getAvailableWidthForSchedule())
				}
				a.setWidth(a.timeAxisViewModel.getTotalWidth())
			}
		}, null, {
			single : true
		});
		this.enableBubble("timeheaderclick", "timeheaderdblclick", "timeheadercontextmenu");
		a.relayEvents(a.headerView, ["timeheaderclick", "timeheaderdblclick", "timeheadercontextmenu"]);
		a.callParent(arguments)
	},
	initRenderData : function () {
		var a = this;
		a.renderData.headerCls = a.renderData.headerCls || a.headerCls;
		return a.callParent(arguments)
	},
	destroy : function () {
		if (this.headerView) {
			this.headerView.destroy()
		}
		this.callParent(arguments)
	},
	onTimeAxisViewRefresh : function () {
		this.headerView.un("refresh", this.onTimeAxisViewRefresh, this);
		this.setWidth(this.timeAxisViewModel.getTotalWidth());
		this.headerView.on("refresh", this.onTimeAxisViewRefresh, this)
	},
	getAvailableWidthForSchedule : function () {
		var c = this.ownerCt.getWidth();
		var a = this.ownerCt.items;
		for (var b = 1; b < a.length; b++) {
			c -= a.get(b).getWidth()
		}
		return c - Ext.getScrollbarSize().width - 1
	},
	onResize : function () {
		this.callParent(arguments);
		this.timeAxisViewModel.setAvailableWidth(this.getAvailableWidthForSchedule())
	},
	onHeaderContainerResize : function () {
		this.timeAxisViewModel.setAvailableWidth(this.getAvailableWidthForSchedule());
		this.headerView.render()
	},
	refresh : function () {
		this.timeAxisViewModel.update(null, true);
		this.headerView.render()
	}
});
Ext.define("Sch.mixin.Lockable", {
	extend : "Ext.grid.locking.Lockable",
	useSpacer : true,
	syncRowHeight : false,
	horizontalScrollForced : false,
	injectLockable : function () {
		var j = this;
		var h = Ext.data.TreeStore && j.store instanceof Ext.data.TreeStore;
		var c = j.getEventSelectionModel ? j.getEventSelectionModel() : j.getSelectionModel();
		j.lockedGridConfig = Ext.apply({}, j.lockedGridConfig || {});
		j.normalGridConfig = Ext.apply({}, j.schedulerConfig || j.normalGridConfig || {});
		if (j.lockedXType) {
			j.lockedGridConfig.xtype = j.lockedXType
		}
		if (j.normalXType) {
			j.normalGridConfig.xtype = j.normalXType
		}
		var a = j.lockedGridConfig,
		i = j.normalGridConfig;
		Ext.applyIf(j.lockedGridConfig, {
			useArrows : true,
			trackMouseOver : false,
			split : true,
			animCollapse : false,
			collapseDirection : "left",
			region : "west"
		});
		Ext.applyIf(j.normalGridConfig, {
			viewType : j.viewType,
			layout : "fit",
			sortableColumns : false,
			enableColumnMove : false,
			enableColumnResize : false,
			enableColumnHide : false,
			getSchedulingView : function () {
				var m = typeof console !== "undefined" ? console : false;
				if (m && m.log) {
					m.log('getSchedulingView is deprecated on the inner grid panel. Instead use getView on the "normal" subgrid.')
				}
				return this.getView()
			},
			selModel : c,
			collapseDirection : "right",
			animCollapse : false,
			region : "center"
		});
		if (j.orientation === "vertical") {
			a.store = i.store = j.timeAxis
		}
		if (a.width) {
			j.syncLockedWidth = Ext.emptyFn;
			a.scroll = "horizontal";
			a.scrollerOwner = true
		}
		var e = j.lockedViewConfig = j.lockedViewConfig || {};
		var k = j.normalViewConfig = j.normalViewConfig || {};
		if (h) {
			var g = Ext.tree.View.prototype.onUpdate;
			e.onUpdate = function () {
				this.refreshSize = function () {
					var n = this,
					m = n.getBodySelector();
					if (m) {
						n.body.attach(n.el.child(m, true))
					}
				};
				Ext.suspendLayouts();
				g.apply(this, arguments);
				Ext.resumeLayouts();
				this.refreshSize = Ext.tree.View.prototype.refreshSize
			};
			if (Ext.versions.extjs.isLessThan("5.0")) {
				e.store = k.store = j.store.nodeStore
			}
		}
		var f = j.layout;
		var d = a.width;
		this.callParent(arguments);
		this.on("afterrender", function () {
			var m = this.lockedGrid.headerCt.showMenuBy;
			this.lockedGrid.headerCt.showMenuBy = function () {
				m.apply(this, arguments);
				j.showMenuBy.apply(this, arguments)
			}
		});
		var l = j.lockedGrid.getView();
		var b = j.normalGrid.getView();
		this.patchViews();
		if (d || f === "border") {
			if (d) {
				j.lockedGrid.setWidth(d)
			}
			b.addCls("sch-timeline-horizontal-scroll");
			l.addCls("sch-locked-horizontal-scroll");
			j.horizontalScrollForced = true
		}
		if (j.normalGrid.collapsed) {
			j.normalGrid.collapsed = false;
			b.on("boxready", function () {
				j.normalGrid.collapse()
			}, j, {
				delay : 10
			})
		}
		if (j.lockedGrid.collapsed) {
			if (l.bufferedRenderer) {
				l.bufferedRenderer.disabled = true
			}
		}
		if (Ext.getScrollbarSize().width === 0) {
			l.addCls("sch-ganttpanel-force-locked-scroll")
		}
		if (h) {
			this.setupLockableTree()
		}
		if (j.useSpacer) {
			b.on("refresh", j.updateSpacer, j);
			l.on("refresh", j.updateSpacer, j)
		}
		if (f !== "fit") {
			j.layout = f
		}
		if (b.bufferedRenderer) {
			this.lockedGrid.on("expand", function () {
				l.el.dom.scrollTop = b.el.dom.scrollTop
			});
			this.patchSubGrid(this.lockedGrid, true);
			this.patchSubGrid(this.normalGrid, false);
			this.patchBufferedRenderingPlugin(b.bufferedRenderer);
			this.patchBufferedRenderingPlugin(l.bufferedRenderer)
		}
		this.patchSyncHorizontalScroll(this.lockedGrid);
		this.patchSyncHorizontalScroll(this.normalGrid);
		this.delayReordererPlugin(this.lockedGrid);
		this.delayReordererPlugin(this.normalGrid);
		this.fixHeaderResizer(this.lockedGrid);
		this.fixHeaderResizer(this.normalGrid)
	},
	setupLockableTree : function () {
		var c = this;
		var b = c.lockedGrid.getView();
		var a = Sch.mixin.FilterableTreeView.prototype;
		b.initTreeFiltering = a.initTreeFiltering;
		b.onFilterChangeStart = a.onFilterChangeStart;
		b.onFilterChangeEnd = a.onFilterChangeEnd;
		b.onFilterCleared = a.onFilterCleared;
		b.onFilterSet = a.onFilterSet;
		b.initTreeFiltering()
	},
	patchSyncHorizontalScroll : function (a) {
		a.scrollTask = new Ext.util.DelayedTask(function (d, b) {
				var c = this.getScrollTarget().el;
				if (c) {
					this.syncHorizontalScroll(c.dom.scrollLeft, b)
				}
			}, a)
	},
	delayReordererPlugin : function (b) {
		var c = b.headerCt;
		var a = c.reorderer;
		if (a) {
			c.un("render", a.onHeaderCtRender, a);
			c.on("render", function () {
				if (!c.isDestroyed) {
					a.onHeaderCtRender()
				}
			}, a, {
				single : true,
				delay : 10
			})
		}
	},
	fixHeaderResizer : function (a) {
		var c = a.headerCt;
		var d = c.resizer;
		if (d) {
			var b = d.onBeforeStart;
			d.onBeforeStart = function () {
				if (this.activeHd && this.activeHd.isDestroyed) {
					return false
				}
				return b.apply(this, arguments)
			}
		}
	},
	updateSpacer : function () {
		var g = this.lockedGrid.getView();
		var e = this.normalGrid.getView();
		if (g.rendered && e.rendered && g.el.child("table")) {
			var f = this,
			c = g.el,
			d = e.el.dom,
			b = c.dom.id + "-spacer",
			h = (d.offsetHeight - d.clientHeight) + "px";
			f.spacerEl = Ext.getDom(b);
			if (Ext.isIE6 || Ext.isIE7 || (Ext.isIEQuirks && Ext.isIE8) && f.spacerEl) {
				Ext.removeNode(f.spacerEl);
				f.spacerEl = null
			}
			if (f.spacerEl) {
				f.spacerEl.style.height = h
			} else {
				var a = c;
				Ext.core.DomHelper.append(a, {
					id : b,
					style : "height: " + h
				})
			}
		}
	},
	onLockedViewScroll : function () {
		this.callParent(arguments);
		var a = this.lockedGrid.getView().bufferedRenderer;
		if (a) {
			a.onViewScroll()
		}
	},
	onNormalViewScroll : function () {
		this.callParent(arguments);
		var a = this.normalGrid.getView().bufferedRenderer;
		if (a) {
			a.onViewScroll()
		}
	},
	patchSubGrid : function (f, h) {
		var d = f.getView();
		var g = d.bufferedRenderer;
		f.on({
			collapse : function () {
				g.disabled = true
			},
			expand : function () {
				g.disabled = false
			}
		});
		var e = d.collectData;
		d.collectData = function () {
			var j = e.apply(this, arguments);
			var i = j.tableStyle;
			if (i && i[i.length - 1] != "x") {
				j.tableStyle += "px"
			}
			return j
		};
		var c = Ext.data.TreeStore && this.store instanceof Ext.data.TreeStore;
		if (!h && c) {
			var b = d.onRemove;
			d.onRemove = function () {
				var i = this;
				if (i.rendered && i.bufferedRenderer) {
					i.refreshView()
				} else {
					b.apply(this, arguments)
				}
			}
		}
		var a = d.onAdd;
		d.onAdd = function () {
			var i = this;
			if (i.rendered && i.bufferedRenderer) {
				i.refreshView()
			} else {
				a.apply(this, arguments)
			}
		};
		d.bindStore(null);
		d.bindStore(c ? this.store.nodeStore : this.resourceStore)
	},
	afterLockedViewLayout : function () {
		if (!this.horizontalScrollForced) {
			return this.callParent(arguments)
		}
	},
	patchBufferedRenderingPlugin : function (c) {
		c.variableRowHeight = true;
		if (Ext.getVersion("extjs").isLessThan("4.2.1.883")) {
			c.view.on("afterrender", function () {
				c.view.el.un("scroll", c.onViewScroll, c)
			}, this, {
				single : true,
				delay : 1
			});
			var b = c.stretchView;
			c.stretchView = function (e, d) {
				var g = this,
				f = (g.store.buffered ? g.store.getTotalCount() : g.store.getCount());
				if (f && (g.view.all.endIndex === f - 1)) {
					d = g.bodyTop + e.body.dom.offsetHeight
				}
				b.apply(this, [e, d])
			}
		} else {
			var a = c.enable;
			c.enable = function () {
				if (c.grid.collapsed) {
					return
				}
				return a.apply(this, arguments)
			}
		}
	},
	showMenuBy : function (b, f) {
		var e = this.getMenu(),
		c = e.down("#unlockItem"),
		d = e.down("#lockItem"),
		a = c.prev();
		a.hide();
		c.hide();
		d.hide()
	},
	patchViews : function () {
		if (Ext.isIE) {
			var e = this.getSelectionModel();
			var h = this;
			var g = h.lockedGrid.view;
			var f = h.normalGrid.view;
			var a = e.processSelection;
			var d = Ext.getVersion("extjs").isLessThan("4.2.2.1144") ? "mousedown" : "click";
			var c = g.doFocus ? "doFocus" : "focus";
			e.processSelection = function (k, j, m, l, o) {
				var i,
				n;
				if (o.type == d) {
					i = g.scrollRowIntoView;
					n = g[c];
					g.scrollRowIntoView = f.scrollRowIntoView = Ext.emptyFn;
					g[c] = f[c] = Ext.emptyFn
				}
				a.apply(this, arguments);
				if (o.type == d) {
					g.scrollRowIntoView = f.scrollRowIntoView = i;
					g[c] = f[c] = n;
					g.el.focus()
				}
			};
			var b = f.onRowFocus;
			f.onRowFocus = function (j, i, k) {
				b.call(this, j, i, true)
			};
			if (Ext.tree && Ext.tree.plugin && Ext.tree.plugin.TreeViewDragDrop) {
				g.on("afterrender", function () {
					Ext.each(g.plugins, function (i) {
						if (i instanceof Ext.tree.plugin.TreeViewDragDrop) {
							var j = g[c];
							i.dragZone.view.un("itemmousedown", i.dragZone.onItemMouseDown, i.dragZone);
							i.dragZone.view.on("itemmousedown", function () {
								g[c] = Ext.emptyFn;
								if (g.editingPlugin) {
									g.editingPlugin.completeEdit()
								}
								i.dragZone.onItemMouseDown.apply(i.dragZone, arguments);
								g[c] = j
							});
							return false
						}
					})
				}, null, {
					delay : 100
				})
			}
		}
	}
});
Ext.define("Sch.plugin.TreeCellEditing", {
	extend : "Ext.grid.plugin.CellEditing",
	alias : "plugin.scheduler_treecellediting",
	lockableScope : "locked",
	editorsStarted : 0,
	init : function (a) {
		this._grid = a;
		this.on("beforeedit", this.checkReadOnly, this);
		this.callParent(arguments)
	},
	bindPositionFixer : function () {
		Ext.on({
			afterlayout : this.fixEditorPosition,
			scope : this
		})
	},
	unbindPositionFixer : function () {
		Ext.un({
			afterlayout : this.fixEditorPosition,
			scope : this
		})
	},
	fixEditorPosition : function () {
		var a = this.getActiveEditor();
		if (a && a.getEl()) {
			var c = this.getEditingContext(this.context.record, this.context.column);
			if (c) {
				this.context.row = c.row;
				this.context.rowIdx = c.rowIdx;
				a.boundEl = this.getCell(c.record, c.column);
				a.realign();
				this.scroll = this.view.el.getScroll();
				var b = this._grid.getView();
				b.focusedRow = b.getNode(c.rowIdx)
			}
		}
	},
	checkReadOnly : function () {
		var a = this._grid;
		if (!(a instanceof Sch.panel.TimelineTreePanel)) {
			a = a.up("tablepanel")
		}
		return !a.isReadOnly()
	},
	startEdit : function (a, c, b) {
		this._grid.suspendLayouts();
		var d = this.callParent(arguments);
		this._grid.resumeLayouts();
		return d
	},
	onEditComplete : function (c, f, b) {
		var e = this,
		a,
		d;
		if (c.field.applyChanges) {
			a = c.field.task || e.context.record;
			d = true;
			a.set = function () {
				delete a.set;
				d = false;
				c.field.applyChanges(a)
			}
		}
		this.callParent(arguments);
		if (d) {
			delete a.set
		}
		this.unbindPositionFixer()
	},
	showEditor : function (d, a, i) {
		var b = this.grid.getSelectionModel();
		var g;
		var f = this;
		this.editorsStarted++;
		if (!d.hideEditOverridden) {
			var c = d.hideEdit;
			d.hideEdit = function () {
				f.editorsStarted--;
				if (!f.editorsStarted) {
					c.apply(this, arguments)
				}
			};
			d.hideEditOverridden = true
		}
		if (Ext.isIE && Ext.getVersion("extjs").isLessThan("4.2.2.1144")) {
			g = b.selectByPosition;
			b.selectByPosition = Ext.emptyFn;
			this.view.focusedRow = this.view.getNode(a.record)
		}
		var h = d.field;
		if (h && h.setSuppressTaskUpdate) {
			h.setSuppressTaskUpdate(true);
			if (!d.startEditOverridden) {
				d.startEditOverridden = true;
				var e = d.startEdit;
				d.startEdit = function () {
					e.apply(this, arguments);
					h.setSuppressTaskUpdate(false)
				}
			}
		}
		if (h) {
			if (h.setTask) {
				h.setTask(a.record);
				i = a.value = a.originalValue = h.getValue()
			} else {
				if (!a.column.dataIndex && a.value === undefined) {
					i = a.value = h.getDisplayValue(a.record)
				}
			}
		}
		if (Ext.isIE8m && Ext.getVersion("extjs").toString() === "4.2.2.1144") {
			Ext.EventObject.type = "click"
		}
		this.callParent([d, a, i]);
		if (g) {
			b.selectByPosition = g
		}
		this.bindPositionFixer()
	},
	cancelEdit : function () {
		this.callParent(arguments);
		this.unbindPositionFixer()
	}
});
Ext.define("Sch.feature.ResizeZone", {
	extend : "Ext.util.Observable",
	requires : ["Ext.resizer.Resizer", "Sch.tooltip.Tooltip", "Sch.util.ScrollManager"],
	showTooltip : true,
	showExactResizePosition : false,
	validatorFn : Ext.emptyFn,
	validatorFnScope : null,
	schedulerView : null,
	origEl : null,
	handlePos : null,
	eventRec : null,
	tip : null,
	startScroll : null,
	constructor : function (a) {
		Ext.apply(this, a);
		var b = this.schedulerView;
		b.on({
			destroy : this.cleanUp,
			scope : this
		});
		b.mon(b.el, {
			mousedown : this.onMouseDown,
			mouseup : this.onMouseUp,
			scope : this,
			delegate : ".sch-resizable-handle"
		});
		this.callParent(arguments)
	},
	onMouseDown : function (f, a) {
		var b = this.schedulerView;
		var d = this.eventRec = b.resolveEventRecord(a);
		var c = d.isResizable();
		if (f.button !== 0 || (c === false || typeof c === "string" && !a.className.match(c))) {
			return
		}
		this.eventRec = d;
		this.handlePos = this.getHandlePosition(a);
		this.origEl = Ext.get(f.getTarget(".sch-event"));
		b.el.on({
			mousemove : this.onMouseMove,
			scope : this,
			single : true
		})
	},
	onMouseUp : function (c, a) {
		var b = this.schedulerView;
		b.el.un({
			mousemove : this.onMouseMove,
			scope : this,
			single : true
		})
	},
	onMouseMove : function (g, a) {
		var b = this.schedulerView;
		var f = this.eventRec;
		var d = this.handlePos;
		if (!f || b.fireEvent("beforeeventresize", b, f, g) === false) {
			return
		}
		delete this.eventRec;
		g.stopEvent();
		this.resizer = this.createResizer(this.origEl, f, d, g, a);
		var c = this.resizer.resizeTracker;
		if (this.showTooltip) {
			if (!this.tip) {
				this.tip = Ext.create("Sch.tooltip.Tooltip", {
						rtl : this.rtl,
						schedulerView : b,
						renderTo : b.up("[lockable=true]").el,
						cls : "sch-resize-tip"
					})
			}
			this.tip.update(f.getStartDate(), f.getEndDate(), true);
			this.tip.show(this.origEl)
		}
		c.onMouseDown(g, this.resizer[d].dom);
		c.onMouseMove(g, this.resizer[d].dom);
		b.fireEvent("eventresizestart", b, f);
		b.el.on("scroll", this.onViewElScroll, this)
	},
	getHandlePosition : function (b) {
		var a = b.className.match("start");
		if (this.schedulerView.getOrientation() === "horizontal") {
			if (this.schedulerView.rtl) {
				return a ? "east" : "west"
			}
			return a ? "west" : "east"
		} else {
			return a ? "north" : "south"
		}
	},
	createResizer : function (c, f, p) {
		var m = this.schedulerView,
		t = this,
		b = m.getElementFromEventRecord(f),
		g = m.resolveResource(c),
		r = m.getSnapPixelAmount(),
		o = m.getScheduleRegion(g, f),
		q = m.getDateConstraints(g, f),
		n = c.getHeight,
		h = (m.rtl && p[0] === "e") || (!m.rtl && p[0] === "w") || p[0] === "n",
		i = m.getOrientation() === "vertical",
		e = {
			otherEdgeX : h ? b.getRight() : b.getLeft(),
			otherEdgeY : h ? b.getBottom() : b.getTop(),
			target : b,
			isStart : h,
			startYOffset : b.getY() - b.parent().getY(),
			startXOffset : b.getX() - b.parent().getX(),
			dateConstraints : q,
			resourceRecord : g,
			eventRecord : f,
			handles : p[0],
			minHeight : n,
			constrainTo : o,
			listeners : {
				resizedrag : this.partialResize,
				resize : this.afterResize,
				scope : this
			}
		};
		var d = c.id;
		var k = "_" + d;
		c.id = c.dom.id = k;
		Ext.cache[k] = Ext.cache[d];
		if (i) {
			if (r > 0) {
				var j = c.getWidth();
				Ext.apply(e, {
					minHeight : r,
					minWidth : j,
					maxWidth : j,
					heightIncrement : r
				})
			}
		} else {
			if (r > 0) {
				Ext.apply(e, {
					minWidth : r,
					maxHeight : n,
					widthIncrement : r
				})
			}
		}
		var l = new Ext.resizer.Resizer(e);
		if (l.resizeTracker) {
			l.resizeTracker.tolerance = -1;
			var a = l.resizeTracker.updateDimensions;
			l.resizeTracker.updateDimensions = function (u) {
				if (!Ext.isWebKit || u.getTarget(".sch-timelineview")) {
					var s;
					if (i) {
						s = m.el.getScroll().top - t.startScroll.top;
						l.resizeTracker.minHeight = e.minHeight - Math.abs(s)
					} else {
						s = m.el.getScroll().left - t.startScroll.left;
						l.resizeTracker.minWidth = e.minWidth - Math.abs(s)
					}
					a.apply(this, arguments)
				}
			};
			l.resizeTracker.resize = function (s) {
				var u;
				if (i) {
					u = m.el.getScroll().top - t.startScroll.top;
					if (p[0] === "s") {
						s.y -= u
					}
					s.height += Math.abs(u)
				} else {
					u = m.el.getScroll().left - t.startScroll.left;
					if (p[0] === "e") {
						s.x -= u
					}
					s.width += Math.abs(u)
				}
				Ext.resizer.ResizeTracker.prototype.resize.apply(this, arguments)
			}
		}
		c.setStyle("z-index", parseInt(c.getStyle("z-index"), 10) + 1);
		Sch.util.ScrollManager.activate(m.el, m.getOrientation());
		this.startScroll = m.el.getScroll();
		return l
	},
	getStartEndDates : function () {
		var e = this.resizer,
		c = e.el,
		d = this.schedulerView,
		b = e.isStart,
		g,
		a,
		f;
		if (b) {
			f = [d.rtl ? c.getRight() : c.getLeft() + 1, c.getTop()];
			a = e.eventRecord.getEndDate();
			if (d.snapRelativeToEventStartDate) {
				g = d.getDateFromXY(f);
				g = d.timeAxis.roundDate(g, e.eventRecord.getStartDate())
			} else {
				g = d.getDateFromXY(f, "round")
			}
		} else {
			f = [d.rtl ? c.getLeft() : c.getRight(), c.getBottom()];
			g = e.eventRecord.getStartDate();
			if (d.snapRelativeToEventStartDate) {
				a = d.getDateFromXY(f);
				a = d.timeAxis.roundDate(a, e.eventRecord.getEndDate())
			} else {
				a = d.getDateFromXY(f, "round")
			}
		}
		g = g || e.start;
		a = a || e.end;
		if (e.dateConstraints) {
			g = Sch.util.Date.constrain(g, e.dateConstraints.start, e.dateConstraints.end);
			a = Sch.util.Date.constrain(a, e.dateConstraints.start, e.dateConstraints.end)
		}
		return {
			start : g,
			end : a
		}
	},
	partialResize : function (b, g, n, l) {
		var q = this.schedulerView,
		p = l.type === "scroll" ? this.resizer.resizeTracker.lastXY : l.getXY(),
		o = this.getStartEndDates(p),
		f = o.start,
		h = o.end,
		j = b.eventRecord,
		m = q.isHorizontal();
		if (m) {
			b.target.el.setY(b.target.parent().getY() + b.startYOffset)
		} else {
			b.target.el.setX(b.target.parent().getX() + b.startXOffset)
		}
		if (this.showTooltip) {
			var a = this.validatorFn.call(this.validatorFnScope || this, b.resourceRecord, j, f, h) !== false;
			this.tip.update(f, h, a)
		}
		if (this.showExactResizePosition) {
			var k = b.target.el,
			d,
			c,
			i;
			if (b.isStart) {
				d = q.timeAxisViewModel.getDistanceBetweenDates(f, j.getEndDate());
				if (m) {
					c = q.getDateFromCoordinate(b.otherEdgeX - Math.min(g, b.maxWidth)) || f;
					i = q.timeAxisViewModel.getDistanceBetweenDates(c, f);
					k.setWidth(d);
					k.setX(k.getX() + i)
				} else {
					c = q.getDateFromCoordinate(b.otherEdgeY - Math.min(g, b.maxHeight)) || f;
					i = q.timeAxisViewModel.getDistanceBetweenDates(c, f);
					k.setHeight(d);
					k.setY(k.getY() + i)
				}
			} else {
				d = q.timeAxisViewModel.getDistanceBetweenDates(j.getStartDate(), h);
				if (m) {
					k.setWidth(d)
				} else {
					k.setHeight(d)
				}
			}
		} else {
			if (!f || !h || ((b.start - f === 0) && (b.end - h === 0))) {
				return
			}
		}
		b.end = h;
		b.start = f;
		q.fireEvent("eventpartialresize", q, j, f, h, b.el)
	},
	onViewElScroll : function (b, a) {
		this.resizer.resizeTracker.onDrag.apply(this.resizer.resizeTracker, arguments);
		this.partialResize(this.resizer, 0, 0, b)
	},
	afterResize : function (a, m, f, g) {
		var j = this,
		i = a.resourceRecord,
		k = a.eventRecord,
		d = k.getStartDate(),
		p = k.getEndDate(),
		b = a.start || d,
		c = a.end || p,
		o = j.schedulerView,
		n = false,
		l = true;
		Sch.util.ScrollManager.deactivate();
		o.el.un("scroll", this.onViewElScroll, this);
		if (this.showTooltip) {
			this.tip.hide()
		}
		delete Ext.cache[a.el.id];
		a.el.id = a.el.dom.id = a.el.id.substr(1);
		j.resizeContext = {
			resourceRecord : a.resourceRecord,
			eventRecord : k,
			start : b,
			end : c,
			finalize : function () {
				j.finalize.apply(j, arguments)
			}
		};
		if (b && c && (c - b > 0) && ((b - d !== 0) || (c - p !== 0)) && j.validatorFn.call(j.validatorFnScope || j, i, k, b, c, g) !== false) {
			l = o.fireEvent("beforeeventresizefinalize", j, j.resizeContext, g) !== false;
			n = true
		} else {
			o.repaintEventsForResource(i)
		}
		if (l) {
			j.finalize(n)
		}
	},
	finalize : function (a) {
		var b = this.schedulerView;
		var d = this.resizeContext;
		var c = false;
		d.eventRecord.store.on("update", function () {
			c = true
		}, null, {
			single : true
		});
		if (a) {
			if (this.resizer.isStart) {
				d.eventRecord.setStartDate(d.start, false, b.eventStore.skipWeekendsDuringDragDrop)
			} else {
				d.eventRecord.setEndDate(d.end, false, b.eventStore.skipWeekendsDuringDragDrop)
			}
			if (!c) {
				b.repaintEventsForResource(d.resourceRecord)
			}
		} else {
			b.repaintEventsForResource(d.resourceRecord)
		}
		this.resizer.destroy();
		b.fireEvent("eventresizeend", b, d.eventRecord);
		this.resizeContext = null
	},
	cleanUp : function () {
		if (this.tip) {
			this.tip.destroy()
		}
	}
});
Ext.define("Sch.feature.ColumnLines", {
	extend : "Sch.plugin.Lines",
	requires : ["Ext.data.JsonStore"],
	cls : "sch-column-line",
	showTip : false,
	timeAxisViewModel : null,
	renderingDoneEvent : "columnlinessynced",
	init : function (a) {
		this.timeAxis = a.getTimeAxis();
		this.timeAxisViewModel = a.timeAxisViewModel;
		this.panel = a;
		this.store = new Ext.data.JsonStore({
				fields : ["Date"]
			});
		this.store.loadData = this.store.loadData || this.store.setData;
		this.callParent(arguments);
		a.on({
			orientationchange : this.populate,
			destroy : this.onHostDestroy,
			scope : this
		});
		this.timeAxisViewModel.on("update", this.populate, this);
		this.populate()
	},
	onHostDestroy : function () {
		this.timeAxisViewModel.un("update", this.populate, this)
	},
	populate : function () {
		this.store.loadData(this.getData())
	},
	getElementData : function () {
		var a = this.schedulerView;
		if (a.isHorizontal() && a.store.getCount() > 0) {
			return this.callParent(arguments)
		}
		return []
	},
	getData : function () {
		var b = this.panel,
		g = [];
		if (b.isHorizontal()) {
			var h = this.timeAxisViewModel;
			var e = h.columnLinesFor;
			var f = !!(h.headerConfig && h.headerConfig[e].cellGenerator);
			if (f) {
				var c = h.getColumnConfig()[e];
				for (var d = 1, a = c.length; d < a; d++) {
					g.push({
						Date : c[d].start
					})
				}
			} else {
				h.forEachInterval(e, function (l, j, k) {
					if (k > 0) {
						g.push({
							Date : l
						})
					}
				})
			}
		}
		return g
	}
});
Ext.define("Sch.plugin.CurrentTimeLine", {
	extend : "Sch.plugin.Lines",
	alias : "plugin.scheduler_currenttimeline",
	mixins : ["Sch.mixin.Localizable"],
	requires : ["Ext.data.JsonStore"],
	updateInterval : 60000,
	showHeaderElements : true,
	autoUpdate : true,
	expandToFitView : true,
	timer : null,
	init : function (c) {
		if (Ext.getVersion("touch")) {
			this.showHeaderElements = false
		}
		var b = new Ext.data.JsonStore({
				fields : ["Date", "Cls", "Text"],
				data : [{
						Date : new Date(),
						Cls : "sch-todayLine",
						Text : this.L("tooltipText")
					}
				]
			});
		var a = b.first();
		if (this.autoUpdate) {
			this.timer = setInterval(function () {
					a.set("Date", new Date())
				}, this.updateInterval)
		}
		c.on("destroy", this.onHostDestroy, this);
		this.store = b;
		this.callParent(arguments)
	},
	onHostDestroy : function () {
		if (this.timer) {
			clearInterval(this.timer);
			this.timer = null
		}
		if (this.store.autoDestroy) {
			this.store.destroy()
		}
	}
});
Ext.define("Sch.view.Horizontal", {
	requires : ["Ext.util.Region", "Ext.Element", "Sch.util.Date"],
	view : null,
	constructor : function (a) {
		Ext.apply(this, a)
	},
	translateToScheduleCoordinate : function (a) {
		var b = this.view;
		if (b.rtl) {
			return b.getTimeAxisColumn().getEl().getRight() - a
		}
		return a - b.getEl().getX() + b.getScroll().left
	},
	translateToPageCoordinate : function (a) {
		var b = this.view;
		return a + b.getEl().getX() - b.getScroll().left
	},
	getEventRenderData : function (a, b, c) {
		var h = b || a.getStartDate(),
		g = c || a.getEndDate() || h,
		j = this.view,
		f = j.timeAxis.getStart(),
		k = j.timeAxis.getEnd(),
		i = Math,
		e = j.getXFromDate(Sch.util.Date.max(h, f)),
		l = j.getXFromDate(Sch.util.Date.min(g, k)),
		d = {};
		if (this.view.rtl) {
			d.right = i.min(e, l)
		} else {
			d.left = i.min(e, l)
		}
		d.width = i.max(1, i.abs(l - e)) - j.eventBorderWidth;
		if (j.managedEventSizing) {
			d.top = i.max(0, (j.barMargin - ((Ext.isIE && !Ext.isStrict) ? 0 : j.eventBorderWidth - j.cellTopBorderWidth)));
			d.height = j.timeAxisViewModel.rowHeightHorizontal - (2 * j.barMargin) - j.eventBorderWidth
		}
		d.start = h;
		d.end = g;
		d.startsOutsideView = h < f;
		d.endsOutsideView = g > k;
		return d
	},
	getScheduleRegion : function (e, g) {
		var c = Ext.Element.prototype.getRegion ? "getRegion" : "getPageBox",
		j = this.view,
		i = e ? Ext.fly(j.getRowNode(e))[c]() : j.getTableRegion(),
		f = j.timeAxis.getStart(),
		l = j.timeAxis.getEnd(),
		b = j.getDateConstraints(e, g) || {
			start : f,
			end : l
		},
		d = this.translateToPageCoordinate(j.getXFromDate(Sch.util.Date.max(f, b.start))),
		k = this.translateToPageCoordinate(j.getXFromDate(Sch.util.Date.min(l, b.end))),
		h = i.top + j.barMargin,
		a = i.bottom - j.barMargin - j.eventBorderWidth;
		return new Ext.util.Region(h, Math.max(d, k), a, Math.min(d, k))
	},
	getResourceRegion : function (j, e, i) {
		var m = this.view,
		d = m.getRowNode(j),
		f = Ext.fly(d).getOffsetsTo(m.getEl()),
		k = m.timeAxis.getStart(),
		o = m.timeAxis.getEnd(),
		c = e ? Sch.util.Date.max(k, e) : k,
		g = i ? Sch.util.Date.min(o, i) : o,
		h = m.getXFromDate(c),
		n = m.getXFromDate(g),
		l = f[1] + m.cellTopBorderWidth,
		a = f[1] + Ext.fly(d).getHeight() - m.cellBottomBorderWidth;
		if (!Ext.versions.touch) {
			var b = m.getScroll();
			l += b.top;
			a += b.top
		}
		return new Ext.util.Region(l, Math.max(h, n), a, Math.min(h, n))
	},
	columnRenderer : function (d, q, k, n, p) {
		var o = this.view;
		var b = o.eventStore.getEventsForResource(k);
		if (b.length === 0) {
			return
		}
		var h = o.timeAxis,
		m = [],
		g,
		e;
		for (g = 0, e = b.length; g < e; g++) {
			var a = b[g],
			c = a.getStartDate(),
			f = a.getEndDate();
			if (c && f && h.timeSpanInAxis(c, f)) {
				m[m.length] = o.generateTplData(a, k, n)
			}
		}
		if (o.dynamicRowHeight) {
			var j = o.eventLayout.horizontal;
			j.applyLayout(m, k);
			q.rowHeight = j.getRowHeight(k, b)
		}
		return o.eventTpl.apply(m)
	},
	resolveResource : function (b) {
		var a = this.view;
		var c = a.findRowByChild(b);
		if (c) {
			return a.getRecordForRowNode(c)
		}
		return null
	},
	getTimeSpanRegion : function (b, h, g) {
		var d = this.view,
		c = d.getXFromDate(b),
		e = h ? d.getXFromDate(h) : c,
		a,
		f;
		f = d.getTableRegion();
		if (g) {
			a = Math.max(f ? f.bottom - f.top : 0, d.getEl().dom.clientHeight)
		} else {
			a = f ? f.bottom - f.top : 0
		}
		return new Ext.util.Region(0, Math.max(c, e), a, Math.min(c, e))
	},
	getStartEndDatesFromRegion : function (g, d, c) {
		var b = this.view;
		var f = b.rtl;
		var a = b.getDateFromCoordinate(f ? g.right : g.left, d),
		e = b.getDateFromCoordinate(f ? g.left : g.right, d);
		if (a && e || c && (a || e)) {
			return {
				start : a,
				end : e
			}
		}
		return null
	},
	onEventAdd : function (n, m) {
		var h = this.view;
		var e = {};
		for (var g = 0, c = m.length; g < c; g++) {
			var a = m[g].getResources(h.eventStore);
			for (var f = 0, d = a.length; f < d; f++) {
				var b = a[f];
				e[b.getId()] = b
			}
		}
		Ext.Object.each(e, function (j, i) {
			h.repaintEventsForResource(i)
		})
	},
	onEventRemove : function (k, e) {
		var h = this.view;
		var j = this.resourceStore;
		var f = Ext.tree && Ext.tree.View && h instanceof Ext.tree.View;
		if (!Ext.isArray(e)) {
			e = [e]
		}
		var g = function (i) {
			if (h.store.indexOf(i) >= 0) {
				h.repaintEventsForResource(i)
			}
		};
		for (var d = 0; d < e.length; d++) {
			var a = e[d].getResources(h.eventStore);
			if (a.length > 1) {
				Ext.each(a, g, this)
			} else {
				var b = h.getEventNodeByRecord(e[d]);
				if (b) {
					var c = h.resolveResource(b);
					if (Ext.Element.prototype.fadeOut) {
						Ext.get(b).fadeOut({
							callback : function () {
								g(c)
							}
						})
					} else {
						Ext.Anim.run(Ext.get(b), "fade", {
							out : true,
							duration : 500,
							after : function () {
								g(c)
							},
							autoClear : false
						})
					}
				}
			}
		}
	},
	onEventUpdate : function (c, d, b) {
		var e = d.previous;
		var a = this.view;
		if (e && e[d.resourceIdField]) {
			var f = d.getResource(e[d.resourceIdField], a.eventStore);
			if (f) {
				a.repaintEventsForResource(f, true)
			}
		}
		var g = d.getResources(a.eventStore);
		Ext.each(g, function (h) {
			a.repaintEventsForResource(h, true)
		})
	},
	setColumnWidth : function (c, b) {
		var a = this.view;
		a.getTimeAxisViewModel().setViewColumnWidth(c, b)
	},
	getVisibleDateRange : function () {
		var d = this.view;
		if (!d.getEl()) {
			return null
		}
		var c = d.getTableRegion(),
		b = d.timeAxis.getStart(),
		f = d.timeAxis.getEnd(),
		e = d.getWidth();
		if ((c.right - c.left) < e) {
			return {
				startDate : b,
				endDate : f
			}
		}
		var a = d.getScroll();
		return {
			startDate : d.getDateFromCoordinate(a.left, null, true),
			endDate : d.getDateFromCoordinate(a.left + e, null, true)
		}
	}
});
Ext.define("Sch.mixin.AbstractTimelineView", {
	requires : ["Sch.data.TimeAxis", "Sch.view.Horizontal"],
	selectedEventCls : "sch-event-selected",
	readOnly : false,
	horizontalViewClass : "Sch.view.Horizontal",
	timeCellCls : "sch-timetd",
	timeCellSelector : ".sch-timetd",
	eventBorderWidth : 1,
	timeAxis : null,
	timeAxisViewModel : null,
	eventPrefix : null,
	rowHeight : null,
	orientation : "horizontal",
	horizontal : null,
	vertical : null,
	secondaryCanvasEl : null,
	panel : null,
	displayDateFormat : null,
	el : null,
	_initializeTimelineView : function () {
		if (this.horizontalViewClass) {
			this.horizontal = Ext.create(this.horizontalViewClass, {
					view : this
				})
		}
		if (this.verticalViewClass) {
			this.vertical = Ext.create(this.verticalViewClass, {
					view : this
				})
		}
		this.eventPrefix = (this.eventPrefix || this.getId()) + "-"
	},
	getTimeAxisViewModel : function () {
		return this.timeAxisViewModel
	},
	getFormattedDate : function (a) {
		return Ext.Date.format(a, this.getDisplayDateFormat())
	},
	getFormattedEndDate : function (c, a) {
		var b = this.getDisplayDateFormat();
		if (c.getHours() === 0 && c.getMinutes() === 0 && !(c.getYear() === a.getYear() && c.getMonth() === a.getMonth() && c.getDate() === a.getDate()) && !Sch.util.Date.hourInfoRe.test(b.replace(Sch.util.Date.stripEscapeRe, ""))) {
			c = Sch.util.Date.add(c, Sch.util.Date.DAY, -1)
		}
		return Ext.Date.format(c, b)
	},
	getDisplayDateFormat : function () {
		return this.displayDateFormat
	},
	setDisplayDateFormat : function (a) {
		this.displayDateFormat = a
	},
	fitColumns : function (b) {
		if (this.orientation === "horizontal") {
			this.getTimeAxisViewModel().fitToAvailableWidth(b)
		} else {
			var a = Math.floor((this.panel.getWidth() - Ext.getScrollbarSize().width - 1) / this.headerCt.getColumnCount());
			this.setColumnWidth(a, b)
		}
	},
	getElementFromEventRecord : function (a) {
		return Ext.get(this.eventPrefix + a.internalId)
	},
	getEventNodeByRecord : function (a) {
		return document.getElementById(this.eventPrefix + a.internalId)
	},
	getEventNodesByRecord : function (a) {
		return this.el.select("[id=" + this.eventPrefix + a.internalId + "]")
	},
	getStartEndDatesFromRegion : function (c, b, a) {
		return this[this.orientation].getStartEndDatesFromRegion(c, b, a)
	},
	getTimeResolution : function () {
		return this.timeAxis.getResolution()
	},
	setTimeResolution : function (b, a) {
		this.timeAxis.setResolution(b, a);
		if (this.getTimeAxisViewModel().snapToIncrement) {
			this.refreshKeepingScroll()
		}
	},
	getEventIdFromDomNodeId : function (a) {
		return a.substring(this.eventPrefix.length)
	},
	getDateFromDomEvent : function (b, a) {
		return this.getDateFromXY(b.getXY(), a)
	},
	getSnapPixelAmount : function () {
		return this.getTimeAxisViewModel().getSnapPixelAmount()
	},
	getTimeColumnWidth : function () {
		return this.getTimeAxisViewModel().getTickWidth()
	},
	setSnapEnabled : function (a) {
		this.getTimeAxisViewModel().setSnapToIncrement(a)
	},
	setReadOnly : function (a) {
		this.readOnly = a;
		this[a ? "addCls" : "removeCls"](this._cmpCls + "-readonly")
	},
	isReadOnly : function () {
		return this.readOnly
	},
	setOrientation : function (a) {
		this.orientation = a;
		this.timeAxisViewModel.orientation = a
	},
	getOrientation : function () {
		return this.orientation
	},
	isHorizontal : function () {
		return this.getOrientation() === "horizontal"
	},
	isVertical : function () {
		return !this.isHorizontal()
	},
	getDateFromXY : function (c, b, a) {
		return this.getDateFromCoordinate(this.orientation === "horizontal" ? c[0] : c[1], b, a)
	},
	getDateFromCoordinate : function (c, b, a) {
		if (!a) {
			c = this[this.orientation].translateToScheduleCoordinate(c)
		}
		return this.timeAxisViewModel.getDateFromPosition(c, b)
	},
	getDateFromX : function (a, b) {
		return this.getDateFromCoordinate(a, b)
	},
	getDateFromY : function (b, a) {
		return this.getDateFromCoordinate(b, a)
	},
	getCoordinateFromDate : function (a, b) {
		var c = this.timeAxisViewModel.getPositionFromDate(a);
		if (b === false) {
			c = this[this.orientation].translateToPageCoordinate(c)
		}
		return Math.round(c)
	},
	getXFromDate : function (a, b) {
		return this.getCoordinateFromDate(a, b)
	},
	getYFromDate : function (a, b) {
		return this.getCoordinateFromDate(a, b)
	},
	getTimeSpanDistance : function (a, b) {
		return this.timeAxisViewModel.getDistanceBetweenDates(a, b)
	},
	getTimeSpanRegion : function (a, b) {
		return this[this.orientation].getTimeSpanRegion(a, b)
	},
	getScheduleRegion : function (b, a) {
		return this[this.orientation].getScheduleRegion(b, a)
	},
	getTableRegion : function () {
		throw "Abstract method call"
	},
	getRowNode : function (a) {
		throw "Abstract method call"
	},
	getRecordForRowNode : function (a) {
		throw "Abstract method call"
	},
	getVisibleDateRange : function () {
		return this[this.orientation].getVisibleDateRange()
	},
	setColumnWidth : function (b, a) {
		this[this.orientation].setColumnWidth(b, a)
	},
	findRowByChild : function (a) {
		throw "Abstract method call"
	},
	setBarMargin : function (b, a) {
		this.barMargin = b;
		if (!a) {
			this.refreshKeepingScroll()
		}
	},
	getRowHeight : function () {
		return this.timeAxisViewModel.getViewRowHeight()
	},
	setRowHeight : function (a, b) {
		this.timeAxisViewModel.setViewRowHeight(a, b)
	},
	refreshKeepingScroll : function () {
		throw "Abstract method call"
	},
	scrollVerticallyTo : function (b, a) {
		throw "Abstract method call"
	},
	scrollHorizontallyTo : function (a, b) {
		throw "Abstract method call"
	},
	getVerticalScroll : function () {
		throw "Abstract method call"
	},
	getHorizontalScroll : function () {
		throw "Abstract method call"
	},
	getEl : Ext.emptyFn,
	getSecondaryCanvasEl : function () {
		if (!this.rendered) {
			throw "Calling this method too early"
		}
		if (!this.secondaryCanvasEl) {
			this.secondaryCanvasEl = this.getEl().createChild({
					cls : "sch-secondary-canvas"
				})
		}
		return this.secondaryCanvasEl
	},
	getScroll : function () {
		throw "Abstract method call"
	},
	getOuterEl : function () {
		return this.getEl()
	},
	getRowContainerEl : function () {
		return this.getEl()
	},
	getScheduleCell : function (b, a) {
		return this.getCellByPosition({
			row : b,
			column : a
		})
	},
	getScrollEventSource : function () {
		return this.getEl()
	},
	getViewportHeight : function () {
		return this.getEl().getHeight()
	},
	getViewportWidth : function () {
		return this.getEl().getWidth()
	},
	getDateConstraints : Ext.emptyFn
});
Ext.apply(Sch, {
	VERSION : "2.2.21"
});
Ext.define("Sch.mixin.TimelineView", {
	extend : "Sch.mixin.AbstractTimelineView",
	requires : ["Ext.tip.ToolTip"],
	overScheduledEventClass : "sch-event-hover",
	ScheduleEventMap : {
		click : "Click",
		mousedown : "MouseDown",
		mouseup : "MouseUp",
		dblclick : "DblClick",
		contextmenu : "ContextMenu",
		keydown : "KeyDown",
		keyup : "KeyUp"
	},
	preventOverCls : false,
	_initializeTimelineView : function () {
		this.callParent(arguments);
		this.on("destroy", this._onDestroy, this);
		this.on("afterrender", this._onAfterRender, this);
		this.setOrientation(this.orientation);
		this.enableBubble("columnwidthchange");
		this.addCls("sch-timelineview");
		if (this.readOnly) {
			this.addCls(this._cmpCls + "-readonly")
		}
		this.addCls(this._cmpCls);
		if (this.eventAnimations) {
			this.addCls("sch-animations-enabled")
		}
	},
	inheritables : function () {
		return {
			processUIEvent : function (d) {
				var a = d.getTarget(this.eventSelector),
				c = this.ScheduleEventMap,
				b = d.type,
				f = false;
				if (a && b in c) {
					this.fireEvent(this.scheduledEventName + b, this, this.resolveEventRecord(a), d);
					f = !(this.getSelectionModel()instanceof Ext.selection.RowModel)
				}
				if (!f) {
					return this.callParent(arguments)
				}
			}
		}
	},
	_onDestroy : function () {
		if (this.tip) {
			this.tip.destroy()
		}
	},
	_onAfterRender : function () {
		if (this.overScheduledEventClass) {
			this.setMouseOverEnabled(true)
		}
		if (this.tooltipTpl) {
			this.el.on("mousemove", this.setupTooltip, this, {
				single : true
			})
		}
		var c = this.bufferedRenderer;
		if (c) {
			this.patchBufferedRenderingPlugin(c);
			this.patchBufferedRenderingPlugin(this.lockingPartner.bufferedRenderer)
		}
		this.on("bufferedrefresh", this.onBufferedRefresh, this, {
			buffer : 10
		});
		this.setupTimeCellEvents();
		var b = this.getSecondaryCanvasEl();
		if (b.getStyle("position").toLowerCase() !== "absolute") {
			var a = Ext.Msg || window;
//			a.alert("ERROR: The CSS file for the Bryntum component has not been loaded.")
		}
	},
	patchBufferedRenderingPlugin : function (c) {
		var b = this;
		var a = c.setBodyTop;
		c.setBodyTop = function (d, e) {
			if (d < 0) {
				d = 0
			}
			var f = a.apply(this, arguments);
			b.fireEvent("bufferedrefresh", this);
			return f
		}
	},
	onBufferedRefresh : function () {
		this.getSecondaryCanvasEl().dom.style.top = this.body.dom.style.top
	},
	setMouseOverEnabled : function (a) {
		this[a ? "mon" : "mun"](this.el, {
			mouseover : this.onEventMouseOver,
			mouseout : this.onEventMouseOut,
			delegate : this.eventSelector,
			scope : this
		})
	},
	onEventMouseOver : function (c, a) {
		if (a !== this.lastItem && !this.preventOverCls) {
			this.lastItem = a;
			Ext.fly(a).addCls(this.overScheduledEventClass);
			var b = this.resolveEventRecord(a);
			if (b) {
				this.fireEvent("eventmouseenter", this, b, c)
			}
		}
	},
	onEventMouseOut : function (b, a) {
		if (this.lastItem) {
			if (!b.within(this.lastItem, true, true)) {
				Ext.fly(this.lastItem).removeCls(this.overScheduledEventClass);
				this.fireEvent("eventmouseleave", this, this.resolveEventRecord(this.lastItem), b);
				delete this.lastItem
			}
		}
	},
	highlightItem : function (b) {
		if (b) {
			var a = this;
			a.clearHighlight();
			a.highlightedItem = b;
			Ext.fly(b).addCls(a.overItemCls)
		}
	},
	setupTooltip : function () {
		var b = this,
		a = Ext.apply({
				renderTo : Ext.getBody(),
				delegate : b.eventSelector,
				target : b.el,
				anchor : "b",
				rtl : b.rtl,
				show : function () {
					Ext.ToolTip.prototype.show.apply(this, arguments);
					if (this.triggerElement && b.getOrientation() === "horizontal") {
						this.setX(this.targetXY[0] - 10);
						this.setY(Ext.fly(this.triggerElement).getY() - this.getHeight() - 10)
					}
				}
			}, b.tipCfg);
		b.tip = new Ext.ToolTip(a);
		b.tip.on({
			beforeshow : function (d) {
				if (!d.triggerElement || !d.triggerElement.id) {
					return false
				}
				var c = this.resolveEventRecord(d.triggerElement);
				if (!c || this.fireEvent("beforetooltipshow", this, c) === false) {
					return false
				}
				d.update(this.tooltipTpl.apply(this.getDataForTooltipTpl(c)))
			},
			scope : this
		})
	},
	getTimeAxisColumn : function () {
		if (!this.timeAxisColumn) {
			this.timeAxisColumn = this.headerCt.down("timeaxiscolumn")
		}
		return this.timeAxisColumn
	},
	getDataForTooltipTpl : function (a) {
		return Ext.apply({
			_record : a
		}, a.data)
	},
	refreshKeepingScroll : function () {
		Ext.suspendLayouts();
		this.saveScrollState();
		this.refresh();
		if (this.up("tablepanel[lockable=true]").lockedGridDependsOnSchedule) {
			this.lockingPartner.refresh()
		}
		Ext.resumeLayouts(true);
		if (this.scrollState.left !== 0 || this.scrollState.top !== 0 || this.infiniteScroll) {
			this.restoreScrollState()
		}
	},
	setupTimeCellEvents : function () {
		this.mon(this.el, {
			click : this.handleScheduleEvent,
			dblclick : this.handleScheduleEvent,
			contextmenu : this.handleScheduleEvent,
			scope : this
		})
	},
	getTableRegion : function () {
		var a = this.el.down("." + Ext.baseCSSPrefix + (Ext.versions.extjs.isLessThan("5.0") ? "grid-table" : "grid-item-container"));
		return (a || this.el).getRegion()
	},
	getRowNode : function (a) {
		return this.getNodeByRecord(a)
	},
	findRowByChild : function (a) {
		return this.findItemByChild(a)
	},
	getRecordForRowNode : function (a) {
		return this.getRecord(a)
	},
	refreshKeepingResourceScroll : function () {
		var a = this.getScroll();
		this.refresh();
		if (this.getOrientation() === "horizontal") {
			this.scrollVerticallyTo(a.top)
		} else {
			this.scrollHorizontallyTo(a.left)
		}
	},
	scrollHorizontallyTo : function (a, b) {
		var c = this.getEl();
		if (c) {
			c.scrollTo("left", Math.max(0, a), b)
		}
	},
	scrollVerticallyTo : function (c, a) {
		var b = this.getEl();
		if (b) {
			b.scrollTo("top", Math.max(0, c), a)
		}
	},
	getVerticalScroll : function () {
		var a = this.getEl();
		return a.getScroll().top
	},
	getHorizontalScroll : function () {
		var a = this.getEl();
		return a.getScroll().left
	},
	getScroll : function () {
		var a = this.getEl().getScroll();
		return {
			top : a.top,
			left : a.left
		}
	},
	getXYFromDate : function () {
		var a = this.getCoordinateFromDate.apply(this, arguments);
		return this.orientation === "horizontal" ? [a, 0] : [0, a]
	},
	handleScheduleEvent : function (a) {},
	scrollElementIntoView : function (b, k, p, f, e) {
		var a = 20,
		o = b.dom,
		h = b.getOffsetsTo(k = Ext.getDom(k) || Ext.getBody().dom),
		d = h[0] + k.scrollLeft,
		l = h[1] + k.scrollTop,
		i = l + o.offsetHeight,
		q = d + o.offsetWidth,
		m = k.clientHeight,
		g = parseInt(k.scrollTop, 10),
		r = parseInt(k.scrollLeft, 10),
		n = g + m,
		j = r + k.clientWidth,
		c;
		if (e) {
			if (f) {
				f = Ext.apply({
						listeners : {
							afteranimate : function () {
								Ext.fly(o).highlight()
							}
						}
					}, f)
			} else {
				Ext.fly(o).highlight()
			}
		}
		if (o.offsetHeight > m || l < g) {
			c = l - a
		} else {
			if (i > n) {
				c = i - m + a
			}
		}
		if (c != null) {
			Ext.fly(k).scrollTo("top", c, f)
		}
		if (p !== false) {
			c = null;
			if (o.offsetWidth > k.clientWidth || d < r) {
				c = d - a
			} else {
				if (q > j) {
					c = q - k.clientWidth + a
				}
			}
			if (c != null) {
				Ext.fly(k).scrollTo("left", c, f)
			}
		}
		return b
	}
});
if (!Ext.ClassManager.get("Sch.patches.ElementScroll")) {
	Ext.define("Sch.patches.ElementScroll", {
		override : "Sch.mixin.TimelineView",
		_onAfterRender : function () {
			this.callParent(arguments);
			if (Ext.versions.extjs.isLessThan("4.2.1") || Ext.versions.extjs.isGreaterThan("4.2.2")) {
				return
			}
			this.el.scroll = function (i, a, c) {
				if (!this.isScrollable()) {
					return false
				}
				i = i.substr(0, 1);
				var h = this,
				e = h.dom,
				g = i === "r" || i === "l" ? "left" : "top",
				b = false,
				d,
				f;
				if (i === "r" || i === "t" || i === "u") {
					a = -a
				}
				if (g === "left") {
					d = e.scrollLeft;
					f = h.constrainScrollLeft(d + a)
				} else {
					d = e.scrollTop;
					f = h.constrainScrollTop(d + a)
				}
				if (f !== d) {
					this.scrollTo(g, f, c);
					b = true
				}
				return b
			}
		}
	})
}
Ext.define("Sch.view.TimelineGridView", {
	extend : "Ext.grid.View",
	mixins : ["Sch.mixin.TimelineView"],
	infiniteScroll : false,
	bufferCoef : 5,
	bufferThreshold : 0.2,
	cachedScrollLeftDate : null,
	boxIsReady : false,
	ignoreNextHorizontalScroll : false,
	constructor : function (a) {
		this.callParent(arguments);
		if (this.infiniteScroll) {
			this.on("afterrender", this.setupInfiniteScroll, this, {
				single : true
			})
		}
		if (this.timeAxisViewModel) {
			this.relayEvents(this.timeAxisViewModel, ["columnwidthchange"])
		}
	},
	setupInfiniteScroll : function () {
		var b = this.panel.ownerCt;
		this.cachedScrollLeftDate = b.startDate || this.timeAxis.getStart();
		var a = this;
		b.calculateOptimalDateRange = function (d, c, g, e) {
			if (e) {
				return e
			}
			var f = Sch.preset.Manager.getPreset(g.preset);
			return a.calculateInfiniteScrollingDateRange(d, f.getBottomHeader().unit, g.increment, g.width)
		};
		this.el.on("scroll", this.onHorizontalScroll, this);
		this.on("resize", this.onSelfResize, this)
	},
	onHorizontalScroll : function () {
		if (this.ignoreNextHorizontalScroll || this.cachedScrollLeftDate) {
			this.ignoreNextHorizontalScroll = false;
			return
		}
		var c = this.el.dom,
		b = this.getWidth(),
		a = b * this.bufferThreshold * this.bufferCoef;
		if ((c.scrollWidth - c.scrollLeft - b < a) || c.scrollLeft < a) {
			this.shiftToDate(this.getDateFromCoordinate(c.scrollLeft, null, true));
			this.el.stopAnimation()
		}
	},
	refresh : function () {
		this.callParent(arguments);
		if (this.infiniteScroll && !this.scrollStateSaved && this.boxIsReady) {
			this.restoreScrollLeftDate()
		}
	},
	onSelfResize : function (c, d, a, b, e) {
		this.boxIsReady = true;
		if (d != b) {
			this.shiftToDate(this.cachedScrollLeftDate || this.timeAxis.getStart(), this.cachedScrollCentered)
		}
	},
	restoreScrollLeftDate : function () {
		if (this.cachedScrollLeftDate && this.boxIsReady) {
			this.ignoreNextHorizontalScroll = true;
			this.scrollToDate(this.cachedScrollLeftDate);
			this.cachedScrollLeftDate = null
		}
	},
	scrollToDate : function (a) {
		this.cachedScrollLeftDate = a;
		if (this.cachedScrollCentered) {
			this.panel.ownerCt.scrollToDateCentered(a)
		} else {
			this.panel.ownerCt.scrollToDate(a)
		}
		var b = this.el.dom.scrollLeft;
		this.panel.scrollLeftPos = b;
		this.headerCt.el.dom.scrollLeft = b
	},
	saveScrollState : function () {
		this.scrollStateSaved = this.boxIsReady;
		this.callParent(arguments)
	},
	restoreScrollState : function () {
		this.scrollStateSaved = false;
		if (this.infiniteScroll && this.cachedScrollLeftDate) {
			this.restoreScrollLeftDate();
			this.el.dom.scrollTop = this.scrollState.top;
			return
		}
		this.callParent(arguments)
	},
	calculateInfiniteScrollingDateRange : function (e, f, b, a) {
		var g = this.timeAxis;
		var d = this.getWidth();
		a = a || this.timeAxisViewModel.getTickWidth();
		b = b || g.increment || 1;
		f = f || g.unit;
		var h = Sch.util.Date;
		var c = Math.ceil(d * this.bufferCoef / a);
		return {
			start : g.floorDate(h.add(e, f, -c * b), false, f, b),
			end : g.ceilDate(h.add(e, f, Math.ceil((d / a + c) * b)), false, f, b)
		}
	},
	shiftToDate : function (b, c) {
		var a = this.calculateInfiniteScrollingDateRange(b);
		this.cachedScrollLeftDate = b;
		this.cachedScrollCentered = c;
		this.timeAxis.setTimeSpan(a.start, a.end)
	},
	destroy : function () {
		if (this.infiniteScroll && this.rendered) {
			this.el.un("scroll", this.onHorizontalScroll, this)
		}
		this.callParent(arguments)
	}
}, function () {
	this.override(Sch.mixin.TimelineView.prototype.inheritables() || {})
});
Ext.define("Sch.mixin.FilterableTreeView", {
	prevBlockRefresh : null,
	initTreeFiltering : function () {
		var a = function () {
			var b = this.store.treeStore;
			this.mon(b, "nodestore-datachange-start", this.onFilterChangeStart, this);
			this.mon(b, "nodestore-datachange-end", this.onFilterChangeEnd, this);
			if (!b.allowExpandCollapseWhileFiltered) {
				this.mon(b, "filter-clear", this.onFilterCleared, this);
				this.mon(b, "filter-set", this.onFilterSet, this)
			}
		};
		if (this.rendered) {
			a.call(this)
		} else {
			this.on("beforerender", a, this, {
				single : true
			})
		}
	},
	onFilterChangeStart : function () {
		this.prevBlockRefresh = this.blockRefresh;
		this.blockRefresh = true;
		Ext.suspendLayouts()
	},
	onFilterChangeEnd : function () {
		Ext.resumeLayouts(true);
		this.blockRefresh = this.prevBlockRefresh
	},
	onFilterCleared : function () {
		delete this.toggle;
		var a = this.getEl();
		if (a) {
			a.removeCls("sch-tree-filtered")
		}
	},
	onFilterSet : function () {
		this.toggle = function () {};
		var a = this.getEl();
		if (a) {
			a.addCls("sch-tree-filtered")
		}
	}
});
Ext.define("Sch.mixin.Zoomable", {
	zoomLevels : [{
			width : 40,
			increment : 1,
			resolution : 1,
			preset : "manyYears",
			resolutionUnit : "YEAR"
		}, {
			width : 80,
			increment : 1,
			resolution : 1,
			preset : "manyYears",
			resolutionUnit : "YEAR"
		}, {
			width : 30,
			increment : 1,
			resolution : 1,
			preset : "year",
			resolutionUnit : "MONTH"
		}, {
			width : 50,
			increment : 1,
			resolution : 1,
			preset : "year",
			resolutionUnit : "MONTH"
		}, {
			width : 100,
			increment : 1,
			resolution : 1,
			preset : "year",
			resolutionUnit : "MONTH"
		}, {
			width : 200,
			increment : 1,
			resolution : 1,
			preset : "year",
			resolutionUnit : "MONTH"
		}, {
			width : 100,
			increment : 1,
			resolution : 7,
			preset : "monthAndYear",
			resolutionUnit : "DAY"
		}, {
			width : 30,
			increment : 1,
			resolution : 1,
			preset : "weekDateAndMonth",
			resolutionUnit : "DAY"
		}, {
			width : 35,
			increment : 1,
			resolution : 1,
			preset : "weekAndMonth",
			resolutionUnit : "DAY"
		}, {
			width : 50,
			increment : 1,
			resolution : 1,
			preset : "weekAndMonth",
			resolutionUnit : "DAY"
		}, {
			width : 20,
			increment : 1,
			resolution : 1,
			preset : "weekAndDayLetter"
		}, {
			width : 50,
			increment : 1,
			resolution : 1,
			preset : "weekAndDay",
			resolutionUnit : "HOUR"
		}, {
			width : 100,
			increment : 1,
			resolution : 1,
			preset : "weekAndDay",
			resolutionUnit : "HOUR"
		}, {
			width : 50,
			increment : 6,
			resolution : 30,
			preset : "hourAndDay",
			resolutionUnit : "MINUTE"
		}, {
			width : 100,
			increment : 6,
			resolution : 30,
			preset : "hourAndDay",
			resolutionUnit : "MINUTE"
		}, {
			width : 60,
			increment : 2,
			resolution : 30,
			preset : "hourAndDay",
			resolutionUnit : "MINUTE"
		}, {
			width : 60,
			increment : 1,
			resolution : 30,
			preset : "hourAndDay",
			resolutionUnit : "MINUTE"
		}, {
			width : 30,
			increment : 15,
			resolution : 5,
			preset : "minuteAndHour"
		}, {
			width : 60,
			increment : 15,
			resolution : 5,
			preset : "minuteAndHour"
		}, {
			width : 130,
			increment : 15,
			resolution : 5,
			preset : "minuteAndHour"
		}, {
			width : 60,
			increment : 5,
			resolution : 5,
			preset : "minuteAndHour"
		}, {
			width : 100,
			increment : 5,
			resolution : 5,
			preset : "minuteAndHour"
		}, {
			width : 50,
			increment : 2,
			resolution : 1,
			preset : "minuteAndHour"
		}, {
			width : 30,
			increment : 10,
			resolution : 5,
			preset : "secondAndMinute"
		}, {
			width : 60,
			increment : 10,
			resolution : 5,
			preset : "secondAndMinute"
		}, {
			width : 130,
			increment : 5,
			resolution : 5,
			preset : "secondAndMinute"
		}
	],
	minZoomLevel : null,
	maxZoomLevel : null,
	visibleZoomFactor : 5,
	zoomKeepsOriginalTimespan : false,
	cachedCenterDate : null,
	isFirstZoom : true,
	isZooming : false,
	initializeZooming : function () {
		this.zoomLevels = this.zoomLevels.slice();
		this.setMinZoomLevel(this.minZoomLevel || 0);
		this.setMaxZoomLevel(this.maxZoomLevel !== null ? this.maxZoomLevel : this.zoomLevels.length - 1);
		this.on("viewchange", this.clearCenterDateCache, this)
	},
	getZoomLevelUnit : function (a) {
		return Sch.preset.Manager.getPreset(a.preset).getBottomHeader().unit
	},
	getMilliSecondsPerPixelForZoomLevel : function (c, a) {
		var b = Sch.util.Date;
		return Math.round((b.add(new Date(1, 0, 1), this.getZoomLevelUnit(c), c.increment) - new Date(1, 0, 1)) / (a ? c.width : c.actualWidth || c.width))
	},
	presetToZoomLevel : function (b) {
		var a = Sch.preset.Manager.getPreset(b);
		return {
			preset : b,
			increment : a.getBottomHeader().increment || 1,
			resolution : a.timeResolution.increment,
			resolutionUnit : a.timeResolution.unit,
			width : a.timeColumnWidth
		}
	},
	zoomLevelToPreset : function (c) {
		var b = Sch.preset.Manager.getPreset(c.preset).clone();
		var a = b.getBottomHeader();
		a.increment = c.increment;
		b.timeColumnWidth = c.width;
		if (c.resolutionUnit || c.resolution) {
			b.timeResolution = {
				unit : c.resolutionUnit || b.timeResolution.unit || a.unit,
				increment : c.resolution || b.timeResolution.increment || 1
			}
		}
		return b
	},
	calculateCurrentZoomLevel : function () {
		var a = this.presetToZoomLevel(this.viewPreset);
		a.width = this.timeAxisViewModel.timeColumnWidth;
		a.increment = this.timeAxisViewModel.getBottomHeader().increment || 1;
		return a
	},
	getCurrentZoomLevelIndex : function () {
		var f = this.calculateCurrentZoomLevel();
		var b = this.getMilliSecondsPerPixelForZoomLevel(f);
		var e = this.zoomLevels;
		for (var c = 0; c < e.length; c++) {
			var d = this.getMilliSecondsPerPixelForZoomLevel(e[c]);
			if (d == b) {
				return c
			}
			if (c === 0 && b > d) {
				return -0.5
			}
			if (c == e.length - 1 && b < d) {
				return e.length - 1 + 0.5
			}
			var a = this.getMilliSecondsPerPixelForZoomLevel(e[c + 1]);
			if (d > b && b > a) {
				return c + 0.5
			}
		}
		throw "Can't find current zoom level index"
	},
	setMaxZoomLevel : function (a) {
		if (a < 0 || a >= this.zoomLevels.length) {
			throw new Error("Invalid range for `setMinZoomLevel`")
		}
		this.maxZoomLevel = a
	},
	setMinZoomLevel : function (a) {
		if (a < 0 || a >= this.zoomLevels.length) {
			throw new Error("Invalid range for `setMinZoomLevel`")
		}
		this.minZoomLevel = a
	},
	getViewportCenterDateCached : function () {
		if (this.cachedCenterDate) {
			return this.cachedCenterDate
		}
		return this.cachedCenterDate = this.getViewportCenterDate()
	},
	clearCenterDateCache : function () {
		this.cachedCenterDate = null
	},
	zoomToLevel : function (b, r, e) {
		b = Ext.Number.constrain(b, this.minZoomLevel, this.maxZoomLevel);
		e = e || {};
		var q = this.calculateCurrentZoomLevel();
		var d = this.getMilliSecondsPerPixelForZoomLevel(q);
		var l = this.zoomLevels[b];
		var a = this.getMilliSecondsPerPixelForZoomLevel(l);
		if (d == a && !r) {
			return null
		}
		var t = this;
		var m = this.getSchedulingView();
		var h = m.getOuterEl();
		var s = m.getScrollEventSource();
		if (this.isFirstZoom) {
			this.isFirstZoom = false;
			s.on("scroll", this.clearCenterDateCache, this)
		}
		var i = this.orientation == "vertical";
		var g = r ? new Date((r.start.getTime() + r.end.getTime()) / 2) : this.getViewportCenterDateCached();
		var n = i ? h.getHeight() : h.getWidth();
		var o = Sch.preset.Manager.getPreset(l.preset).clone();
		var p = o.getBottomHeader();
		var f = Boolean(r);
		r = this.calculateOptimalDateRange(g, n, l, r);
		o[i ? "timeRowHeight" : "timeColumnWidth"] = e.customWidth || l.width;
		p.increment = l.increment;
		this.isZooming = true;
		this.viewPreset = l.preset;
		var c = this.timeAxis;
		o.increment = l.increment;
		o.resolutionUnit = Sch.util.Date.getUnitByName(l.resolutionUnit || p.unit);
		o.resolutionIncrement = l.resolution;
		this.switchViewPreset(o, r.start || this.getStart(), r.end || this.getEnd(), false, true);
		l.actualWidth = this.timeAxisViewModel.getTickWidth();
		if (f) {
			g = e.centerDate || new Date((c.getStart().getTime() + c.getEnd().getTime()) / 2)
		}
		s.on("scroll", function () {
			t.cachedCenterDate = g
		}, this, {
			single : true
		});
		if (i) {
			var j = m.getYFromDate(g, true);
			m.scrollVerticallyTo(j - n / 2)
		} else {
			var k = m.getXFromDate(g, true);
			m.scrollHorizontallyTo(k - n / 2)
		}
		t.isZooming = false;
		this.fireEvent("zoomchange", this, b);
		return b
	},
	zoomToSpan : function (r, u) {
		if (r.start && r.end && r.start < r.end) {
			var g = r.start,
			d = r.end,
			e = u && u.adjustStart >= 0 && u.adjustEnd >= 0;
			if (e) {
				g = Sch.util.Date.add(g, this.timeAxis.mainUnit, -u.adjustStart);
				d = Sch.util.Date.add(d, this.timeAxis.mainUnit, u.adjustEnd)
			}
			var a = this.getSchedulingView().getTimeAxisViewModel().getAvailableWidth();
			var m = Math.floor(this.getCurrentZoomLevelIndex());
			if (m == -1) {
				m = 0
			}
			var v = this.zoomLevels;
			var o,
			b = d - g,
			j = this.getMilliSecondsPerPixelForZoomLevel(v[m], true),
			l = b / j > a ? -1 : 1,
			f = m + l;
			var p,
			q,
			h = null;
			while (f >= 0 && f <= v.length - 1) {
				p = v[f];
				var s = b / this.getMilliSecondsPerPixelForZoomLevel(p, true);
				if (l == -1) {
					if (s <= a) {
						h = f;
						break
					}
				} else {
					if (s <= a) {
						if (m !== f - l) {
							h = f
						}
					} else {
						break
					}
				}
				f += l
			}
			h = h !== null ? h : f - l;
			p = v[h];
			var c = Sch.preset.Manager.getPreset(p.preset).getBottomHeader().unit;
			var t = Sch.util.Date.getDurationInUnit(g, d, c) / p.increment;
			if (t === 0) {
				return
			}
			var i = Math.floor(a / t);
			var k = new Date((g.getTime() + d.getTime()) / 2);
			var n;
			if (e) {
				n = {
					start : g,
					end : d
				}
			} else {
				n = this.calculateOptimalDateRange(k, a, p)
			}
			return this.zoomToLevel(h, n, {
				customWidth : i,
				centerDate : k
			})
		}
		return null
	},
	zoomIn : function (a) {
		a = a || 1;
		var b = this.getCurrentZoomLevelIndex();
		if (b >= this.zoomLevels.length - 1) {
			return null
		}
		return this.zoomToLevel(Math.floor(b) + a)
	},
	zoomOut : function (a) {
		a = a || 1;
		var b = this.getCurrentZoomLevelIndex();
		if (b <= 0) {
			return null
		}
		return this.zoomToLevel(Math.ceil(b) - a)
	},
	zoomInFull : function () {
		return this.zoomToLevel(this.maxZoomLevel)
	},
	zoomOutFull : function () {
		return this.zoomToLevel(this.minZoomLevel)
	},
	calculateOptimalDateRange : function (c, i, e, l) {
		if (l) {
			return l
		}
		var h = this.timeAxis;
		if (this.zoomKeepsOriginalTimespan) {
			return {
				start : h.getStart(),
				end : h.getEnd()
			}
		}
		var b = Sch.util.Date;
		var j = Sch.preset.Manager.getPreset(e.preset).headerConfig;
		var f = j.top ? j.top.unit : j.middle.unit;
		var k = this.getZoomLevelUnit(e);
		var d = Math.ceil(i / e.width * e.increment * this.visibleZoomFactor / 2);
		var a = b.add(c, k, -d);
		var g = b.add(c, k, d);
		return {
			start : h.floorDate(a, false, k, e.increment),
			end : h.ceilDate(g, false, k, e.increment)
		}
	}
});
Ext.define("Sch.mixin.AbstractTimelinePanel", {
	requires : ["Sch.data.TimeAxis", "Sch.view.model.TimeAxis", "Sch.feature.ColumnLines", "Sch.preset.Manager"],
	mixins : ["Sch.mixin.Zoomable"],
	orientation : "horizontal",
	weekStartDay : 1,
	snapToIncrement : false,
	readOnly : false,
	forceFit : false,
	eventResizeHandles : "both",
	timeAxis : null,
	autoAdjustTimeAxis : true,
	timeAxisViewModel : null,
	viewPreset : "weekAndDay",
	trackHeaderOver : true,
	startDate : null,
	endDate : null,
	columnLines : true,
	getDateConstraints : Ext.emptyFn,
	snapRelativeToEventStartDate : false,
	trackMouseOver : false,
	readRowHeightFromPreset : true,
	eventBorderWidth : 1,
	getOrientation : function () {
		return this.orientation
	},
	isHorizontal : function () {
		return this.getOrientation() === "horizontal"
	},
	isVertical : function () {
		return !this.isHorizontal()
	},
	cellBorderWidth : 1,
	cellTopBorderWidth : 1,
	cellBottomBorderWidth : 1,
	renderers : null,
	_initializeTimelinePanel : function () {
		var b = this.viewPreset && Sch.preset.Manager.getPreset(this.viewPreset);
		if (!b) {
			throw "You must define a valid view preset object. See Sch.preset.Manager class for reference"
		}
		this.initializeZooming();
		this.renderers = [];
		this.readRowHeightFromPreset = !this.rowHeight;
		if (!this.timeAxis) {
			this.timeAxis = new Sch.data.TimeAxis({
					autoAdjust : this.autoAdjustTimeAxis
				})
		}
		if (!this.timeAxisViewModel || !(this.timeAxisViewModel instanceof Sch.view.model.TimeAxis)) {
			var a = Ext.apply({
					orientation : this.orientation,
					snapToIncrement : this.snapToIncrement,
					forceFit : this.forceFit,
					timeAxis : this.timeAxis,
					eventStore : this.getEventStore()
				}, this.timeAxisViewModel || {});
			this.timeAxisViewModel = new Sch.view.model.TimeAxis(a)
		}
		this.timeAxisViewModel.on("update", this.onTimeAxisViewModelUpdate, this);
		this.timeAxisViewModel.refCount++;
		this.on("destroy", this.onPanelDestroyed, this);
		this.addCls(["sch-timelinepanel", "sch-" + this.orientation])
	},
	onTimeAxisViewModelUpdate : function () {
		var a = this.getSchedulingView();
		if (a && a.viewReady) {
			a.refreshKeepingScroll();
			this.fireEvent("viewchange", this)
		}
	},
	onPanelDestroyed : function () {
		var a = this.timeAxisViewModel;
		a.un("update", this.onTimeAxisViewModelUpdate, this);
		a.refCount--;
		if (a.refCount <= 0) {
			a.destroy()
		}
	},
	getSchedulingView : function () {
		throw "Abstract method call"
	},
	setReadOnly : function (a) {
		this.getSchedulingView().setReadOnly(a)
	},
	isReadOnly : function () {
		return this.getSchedulingView().isReadOnly()
	},
	switchViewPreset : function (i, a, d, f, b) {
		var e = this.timeAxis;
		if (this.fireEvent("beforeviewchange", this, i, a, d) !== false) {
			var h = this.getOrientation() === "horizontal";
			if (Ext.isString(i)) {
				this.viewPreset = i;
				i = Sch.preset.Manager.getPreset(i)
			}
			if (!i) {
				throw "View preset not found"
			}
			if (!(f && e.isConfigured)) {
				var c = {
					weekStartDay : this.weekStartDay
				};
				if (f) {
					if (e.getCount() === 0 || a) {
						c.start = a || new Date()
					}
				} else {
					c.start = a || e.getStart()
				}
				c.end = d;
				e.consumeViewPreset(i);
				e.reconfigure(c, true);
				this.timeAxisViewModel.reconfigure({
					headerConfig : i.headerConfig,
					columnLinesFor : i.columnLinesFor || "middle",
					rowHeightHorizontal : this.readRowHeightFromPreset ? i.rowHeight : this.rowHeight,
					tickWidth : h ? i.timeColumnWidth : i.timeRowHeight || i.timeColumnWidth || 60,
					timeColumnWidth : i.timeColumnWidth,
					rowHeightVertical : i.timeRowHeight || i.timeColumnWidth || 60,
					timeAxisColumnWidth : i.timeAxisColumnWidth,
					resourceColumnWidth : this.resourceColumnWidth || i.resourceColumnWidth || 100
				})
			}
			var g = this.getSchedulingView();
			g.setDisplayDateFormat(i.displayDateFormat);
			if (!h) {
				g.setColumnWidth(this.resourceColumnWidth || i.resourceColumnWidth || 100, true)
			}
			if (!b) {
				if (h) {
					g.scrollHorizontallyTo(0)
				} else {
					g.scrollVerticallyTo(0)
				}
			}
		}
	},
	getStart : function () {
		return this.getStartDate()
	},
	getStartDate : function () {
		return this.timeAxis.getStart()
	},
	getEnd : function () {
		return this.getEndDate()
	},
	getEndDate : function () {
		return this.timeAxis.getEnd()
	},
	setTimeColumnWidth : function (b, a) {
		this.timeAxisViewModel.setTickWidth(b, a)
	},
	getTimeColumnWidth : function () {
		return this.timeAxisViewModel.getTickWidth()
	},
	getRowHeight : function () {
		return this.timeAxisViewModel.getViewRowHeight()
	},
	shiftNext : function (a) {
		this.suspendLayouts && this.suspendLayouts();
		this.timeAxis.shiftNext(a);
		this.suspendLayouts && this.resumeLayouts(true)
	},
	shiftPrevious : function (a) {
		this.suspendLayouts && this.suspendLayouts();
		this.timeAxis.shiftPrevious(a);
		this.suspendLayouts && this.resumeLayouts(true)
	},
	goToNow : function () {
		this.setTimeSpan(new Date())
	},
	setTimeSpan : function (b, a) {
		if (this.timeAxis) {
			this.timeAxis.setTimeSpan(b, a)
		}
	},
	setStart : function (a) {
		this.setTimeSpan(a)
	},
	setEnd : function (a) {
		this.setTimeSpan(null, a)
	},
	getTimeAxis : function () {
		return this.timeAxis
	},
	scrollToDate : function (c, b) {
		var a = this.getSchedulingView();
		var d = a.getCoordinateFromDate(c, true);
		this.scrollToCoordinate(d, c, b, false)
	},
	scrollToDateCentered : function (c, b) {
		var a = this.getSchedulingView();
		var e = 0;
		if (this.orientation === "horizontal") {
			e = a.getBox().width / 2
		} else {
			e = a.getBox().height / 2
		}
		var d = Math.round(a.getCoordinateFromDate(c, true) - e);
		this.scrollToCoordinate(d, c, b, true)
	},
	scrollToCoordinate : function (g, e, d, c) {
		var b = this.getSchedulingView();
		var f = this;
		if (g < 0) {
			if (this.infiniteScroll) {
				b.shiftToDate(e, c)
			} else {
				var a = (this.timeAxis.getEnd() - this.timeAxis.getStart()) / 2;
				this.setTimeSpan(new Date(e.getTime() - a), new Date(e.getTime() + a));
				if (c) {
					f.scrollToDateCentered(e, d)
				} else {
					f.scrollToDate(e, d)
				}
			}
			return
		}
		if (this.orientation === "horizontal") {
			b.scrollHorizontallyTo(g, d)
		} else {
			b.scrollVerticallyTo(g, d)
		}
		b.fireEvent("scroll", this, g)
	},
	getViewportCenterDate : function () {
		var b = this.getSchedulingView(),
		a = b.getScroll(),
		c;
		if (this.getOrientation() === "vertical") {
			c = [0, a.top + b.getViewportHeight() / 2]
		} else {
			c = [a.left + b.getViewportWidth() / 2, 0]
		}
		return b.getDateFromXY(c, null, true)
	},
	addCls : function () {
		throw "Abstract method call"
	},
	removeCls : function () {
		throw "Abstract method call"
	},
	registerRenderer : function (b, a) {
		this.renderers.push({
			fn : b,
			scope : a
		})
	},
	deregisterRenderer : function (b, a) {
		Ext.each(this.renderers, function (c, d) {
			if (b === c) {
				Ext.Array.removeAt(this.renderers, d);
				return false
			}
		})
	}
});
if (!Ext.ClassManager.get("Sch.mixin.TimelinePanel")) {
	Ext.define("Sch.mixin.TimelinePanel", {
		extend : "Sch.mixin.AbstractTimelinePanel",
		requires : ["Sch.util.Patch", "Sch.patches.ElementScroll", "Sch.column.timeAxis.Horizontal", "Sch.preset.Manager"],
		mixins : ["Sch.mixin.Zoomable", "Sch.mixin.Lockable"],
		bufferCoef : 5,
		bufferThreshold : 0.2,
		infiniteScroll : false,
		waitingForAutoTimeSpan : false,
		columnLinesFeature : null,
		tipCfg : {
			cls : "sch-tip",
			showDelay : 1000,
			hideDelay : 0,
			autoHide : true,
			anchor : "b"
		},
		inheritables : function () {
			return {
				columnLines : true,
				enableLocking : true,
				lockable : true,
				initComponent : function () {
					if (this.partnerTimelinePanel) {
						this.timeAxisViewModel = this.partnerTimelinePanel.timeAxisViewModel;
						this.timeAxis = this.partnerTimelinePanel.getTimeAxis();
						this.startDate = this.timeAxis.getStart();
						this.endDate = this.timeAxis.getEnd()
					}
					if (this.viewConfig && this.viewConfig.forceFit) {
						this.forceFit = true
					}
					if (Ext.versions.extjs.isGreaterThanOrEqual("4.2.1")) {
						this.cellTopBorderWidth = 0
					}
					this._initializeTimelinePanel();
					this.configureColumns();
					var c = this.normalViewConfig = this.normalViewConfig || {};
					var e = this.getId();
					Ext.apply(this.normalViewConfig, {
						id : e + "-timelineview",
						eventPrefix : this.autoGenId ? null : e,
						timeAxisViewModel : this.timeAxisViewModel,
						eventBorderWidth : this.eventBorderWidth,
						timeAxis : this.timeAxis,
						readOnly : this.readOnly,
						orientation : this.orientation,
						rtl : this.rtl,
						cellBorderWidth : this.cellBorderWidth,
						cellTopBorderWidth : this.cellTopBorderWidth,
						cellBottomBorderWidth : this.cellBottomBorderWidth,
						infiniteScroll : this.infiniteScroll,
						bufferCoef : this.bufferCoef,
						bufferThreshold : this.bufferThreshold
					});
					Ext.Array.forEach(["eventRendererScope", "eventRenderer", "dndValidatorFn", "resizeValidatorFn", "createValidatorFn", "tooltipTpl", "validatorFnScope", "eventResizeHandles", "enableEventDragDrop", "enableDragCreation", "resizeConfig", "createConfig", "tipCfg", "getDateConstraints"], function (f) {
						if (f in this) {
							c[f] = this[f]
						}
					}, this);
					this.mon(this.timeAxis, "reconfigure", this.onMyTimeAxisReconfigure, this);
					this.callParent(arguments);
					this.switchViewPreset(this.viewPreset, this.startDate || this.timeAxis.getStart(), this.endDate || this.timeAxis.getEnd(), true);
					if (!this.startDate) {
						var a = this.getTimeSpanDefiningStore();
						if (Ext.data.TreeStore && a instanceof Ext.data.TreeStore ? a.getRootNode().childNodes.length : a.getCount()) {
							var d = a.getTotalTimeSpan();
							this.setTimeSpan(d.start || new Date(), d.end)
						} else {
							this.bindAutoTimeSpanListeners()
						}
					}
					var b = this.columnLines;
					if (b) {
						this.columnLinesFeature = new Sch.feature.ColumnLines(Ext.isObject(b) ? b : undefined);
						this.columnLinesFeature.init(this);
						this.columnLines = true
					}
					this.relayEvents(this.getSchedulingView(), ["beforetooltipshow"]);
					this.on("afterrender", this.__onAfterRender, this);
					this.on("zoomchange", function () {
						this.normalGrid.scrollTask.cancel()
					})
				},
				getState : function () {
					var a = this,
					b = a.callParent(arguments);
					Ext.apply(b, {
						viewPreset : a.viewPreset,
						startDate : a.getStart(),
						endDate : a.getEnd(),
						zoomMinLevel : a.zoomMinLevel,
						zoomMaxLevel : a.zoomMaxLevel,
						currentZoomLevel : a.currentZoomLevel
					});
					return b
				},
				applyState : function (b) {
					var a = this;
					a.callParent(arguments);
					if (b && b.viewPreset) {
						a.switchViewPreset(b.viewPreset, b.startDate, b.endDate)
					}
					if (b && b.currentZoomLevel) {
						a.zoomToLevel(b.currentZoomLevel)
					}
				},
				setTimeSpan : function () {
					if (this.waitingForAutoTimeSpan) {
						this.unbindAutoTimeSpanListeners()
					}
					this.callParent(arguments);
					if (!this.normalGrid.getView().viewReady) {
						this.getView().refresh()
					}
				}
			}
		},
		bindAutoTimeSpanListeners : function () {
			var a = this.getTimeSpanDefiningStore();
			this.waitingForAutoTimeSpan = true;
			this.normalGrid.getView().on("beforerefresh", this.refreshStopper, this);
			this.lockedGrid.getView().on("beforerefresh", this.refreshStopper, this);
			this.mon(a, "load", this.applyStartEndDatesFromStore, this);
			if (Ext.data.TreeStore && a instanceof Ext.data.TreeStore) {
				this.mon(a, "rootchange", this.applyStartEndDatesFromStore, this);
				this.mon(a.tree, "append", this.applyStartEndDatesAfterTreeAppend, this)
			} else {
				this.mon(a, "add", this.applyStartEndDatesFromStore, this)
			}
		},
		refreshStopper : function (a) {
			return a.store.getCount() === 0
		},
		getTimeSpanDefiningStore : function () {
			throw "Abstract method called"
		},
		unbindAutoTimeSpanListeners : function () {
			this.waitingForAutoTimeSpan = false;
			var a = this.getTimeSpanDefiningStore();
			this.normalGrid.getView().un("beforerefresh", this.refreshStopper, this);
			this.lockedGrid.getView().un("beforerefresh", this.refreshStopper, this);
			a.un("load", this.applyStartEndDatesFromStore, this);
			if (Ext.data.TreeStore && a instanceof Ext.data.TreeStore) {
				a.un("rootchange", this.applyStartEndDatesFromStore, this);
				a.tree.un("append", this.applyStartEndDatesAfterTreeAppend, this)
			} else {
				a.un("add", this.applyStartEndDatesFromStore, this)
			}
		},
		applyStartEndDatesAfterTreeAppend : function () {
			var a = this.getTimeSpanDefiningStore();
			if (!a.isSettingRoot) {
				this.applyStartEndDatesFromStore()
			}
		},
		applyStartEndDatesFromStore : function () {
			var a = this.getTimeSpanDefiningStore();
			var b = a.getTotalTimeSpan();
			var c = this.lockedGridDependsOnSchedule;
			this.lockedGridDependsOnSchedule = true;
			this.setTimeSpan(b.start || new Date(), b.end);
			this.lockedGridDependsOnSchedule = c
		},
		onMyTimeAxisReconfigure : function (a) {
			if (this.stateful && this.rendered) {
				this.saveState()
			}
		},
		onLockedGridItemDblClick : function (b, a, c, e, d) {
			if (this.orientation === "vertical" && a) {
				this.fireEvent("timeheaderdblclick", this, a.get("start"), a.get("end"), e, d)
			}
		},
		getSchedulingView : function () {
			return this.normalGrid.getView()
		},
		getTimeAxisColumn : function () {
			if (!this.timeAxisColumn) {
				this.timeAxisColumn = this.down("timeaxiscolumn")
			}
			return this.timeAxisColumn
		},
		configureColumns : function () {
			var a = this.columns || [];
			if (a.items) {
				a = a.items
			} else {
				a = this.columns = a.slice()
			}
			var c = [];
			var b = [];
			Ext.Array.each(a, function (d) {
				if (d.position === "right") {
					if (!Ext.isNumber(d.width)) {
						Ext.Error.raise('"Right" columns must have a fixed width')
					}
					d.locked = false;
					b.push(d)
				} else {
					d.locked = true;
					c.push(d)
				}
				d.lockable = false
			});
			Ext.Array.erase(a, 0, a.length);
			Ext.Array.insert(a, 0, c.concat({
					xtype : "timeaxiscolumn",
					timeAxisViewModel : this.timeAxisViewModel,
					trackHeaderOver : this.trackHeaderOver,
					renderer : this.mainRenderer,
					scope : this
				}).concat(b));
			this.horizontalColumns = Ext.Array.clone(a);
			this.verticalColumns = [Ext.apply({
					xtype : "verticaltimeaxis",
					width : 100,
					timeAxis : this.timeAxis,
					timeAxisViewModel : this.timeAxisViewModel,
					cellTopBorderWidth : this.cellTopBorderWidth,
					cellBottomBorderWidth : this.cellBottomBorderWidth
				}, this.timeAxisColumnCfg || {})];
			if (this.orientation === "vertical") {
				this.columns = this.verticalColumns;
				this.store = this.timeAxis;
				this.on("beforerender", this.refreshResourceColumns, this)
			}
		},
		mainRenderer : function (b, m, g, j, l) {
			var c = this.renderers,
			k = this.orientation === "horizontal",
			d = k ? g : this.resourceStore.getAt(l),
			a = "&nbsp;";
			m.rowHeight = null;
			for (var e = 0; e < c.length; e++) {
				a += c[e].fn.call(c[e].scope || this, b, m, d, j, l) || ""
			}
			if (this.variableRowHeight) {
				var h = this.getSchedulingView();
				var f = this.timeAxisViewModel.getViewRowHeight();
				m.style = "height:" + ((m.rowHeight || f) - h.cellTopBorderWidth - h.cellBottomBorderWidth) + "px"
			}
			return a
		},
		__onAfterRender : function () {
			var a = this;
			a.normalGrid.on({
				collapse : a.onNormalGridCollapse,
				expand : a.onNormalGridExpand,
				scope : a
			});
			a.lockedGrid.on({
				collapse : a.onLockedGridCollapse,
				itemdblclick : a.onLockedGridItemDblClick,
				scope : a
			});
			if (a.lockedGridDependsOnSchedule) {
				a.normalGrid.getView().on("itemupdate", a.onNormalViewItemUpdate, a)
			}
			if (this.partnerTimelinePanel) {
				if (this.partnerTimelinePanel.rendered) {
					this.setupPartnerTimelinePanel()
				} else {
					this.partnerTimelinePanel.on("afterrender", this.setupPartnerTimelinePanel, this)
				}
			}
		},
		onLockedGridCollapse : function () {
			if (this.normalGrid.collapsed) {
				this.normalGrid.expand()
			}
		},
		onNormalGridCollapse : function () {
			var a = this;
			if (!a.normalGrid.reExpander) {
				a.normalGrid.reExpander = a.normalGrid.placeholder
			}
			if (!a.lockedGrid.rendered) {
				a.lockedGrid.on("render", a.onNormalGridCollapse, a, {
					delay : 1
				})
			} else {
				a.lockedGrid.flex = 1;
				a.lockedGrid.doLayout();
				if (a.lockedGrid.collapsed) {
					a.lockedGrid.expand()
				}
				a.addCls("sch-normalgrid-collapsed")
			}
		},
		onNormalGridExpand : function () {
			this.removeCls("sch-normalgrid-collapsed");
			delete this.lockedGrid.flex;
			this.lockedGrid.doLayout()
		},
		onNormalViewItemUpdate : function (a, b, d) {
			if (this.lockedGridDependsOnSchedule) {
				var c = this.lockedGrid.getView();
				c.suspendEvents();
				c.refreshNode(b);
				c.resumeEvents()
			}
		},
		setupPartnerTimelinePanel : function () {
			var f = this.partnerTimelinePanel;
			var d = f.down("splitter");
			var c = this.down("splitter");
			if (d) {
				d.on("dragend", function () {
					this.lockedGrid.setWidth(f.lockedGrid.getWidth())
				}, this)
			}
			if (c) {
				c.on("dragend", function () {
					f.lockedGrid.setWidth(this.lockedGrid.getWidth())
				}, this)
			}
			var b = f.isVisible() ? f.lockedGrid.getWidth() : f.lockedGrid.width;
			this.lockedGrid.setWidth(b);
			var a = f.getSchedulingView().getEl(),
			e = this.getSchedulingView().getEl();
			f.mon(e, "scroll", function (h, g) {
				a.scrollTo("left", g.scrollLeft)
			});
			this.mon(a, "scroll", function (h, g) {
				e.scrollTo("left", g.scrollLeft)
			});
			this.on("viewchange", function () {
				f.viewPreset = this.viewPreset
			}, this);
			f.on("viewchange", function () {
				this.viewPreset = f.viewPreset
			}, this)
		}
	}, function () {
		var a = "4.2.1";
		Ext.apply(Sch, {
			VERSION : "2.2.21"
		});
		if (Ext.versions.extjs.isLessThan(a)) {
			alert("The Ext JS version you are using needs to be updated to at least " + a)
		}
	})
}
Ext.define("Sch.panel.TimelineGridPanel", {
	extend : "Ext.grid.Panel",
	mixins : ["Sch.mixin.TimelinePanel"],
	subGridXType : "gridpanel",
	requires : ["Sch.patches.ColumnResize"],
	initComponent : function () {
		this.callParent(arguments);
		this.getSchedulingView()._initializeTimelineView()
	}
}, function () {
	this.override(Sch.mixin.TimelinePanel.prototype.inheritables() || {})
});
if (!Ext.ClassManager.get("Sch.panel.TimelineTreePanel")) {
	Ext.define("Sch.panel.TimelineTreePanel", {
		extend : "Ext.tree.Panel",
		requires : ["Ext.grid.Panel", "Ext.data.TreeStore", "Sch.mixin.FilterableTreeView", "Sch.patches.ColumnResizeTree"],
		mixins : ["Sch.mixin.TimelinePanel"],
		useArrows : true,
		rootVisible : false,
		lockedXType : "treepanel",
		initComponent : function () {
			this.callParent(arguments);
			this.getSchedulingView()._initializeTimelineView()
		}
	}, function () {
		this.override(Sch.mixin.TimelinePanel.prototype.inheritables() || {})
	})
}
Ext.define("Sch.plugin.Printable", {
	extend : "Ext.AbstractPlugin",
	alias : "plugin.scheduler_printable",
	requires : ["Ext.XTemplate"],
	lockableScope : "top",
	docType : "<!DOCTYPE HTML>",
	beforePrint : Ext.emptyFn,
	afterPrint : Ext.emptyFn,
	autoPrintAndClose : true,
	fakeBackgroundColor : true,
	scheduler : null,
	constructor : function (a) {
		Ext.apply(this, a)
	},
	init : function (a) {
		this.scheduler = a;
		a.print = Ext.Function.bind(this.print, this)
	},
	mainTpl : new Ext.XTemplate('{docType}<html class="' + Ext.baseCSSPrefix + 'border-box {htmlClasses}"><head><meta content="text/html; charset=UTF-8" http-equiv="Content-Type" /><title>{title}</title>{styles}</head><body class="sch-print-body {bodyClasses}"><div class="sch-print-ct {componentClasses}" style="width:{totalWidth}px"><div class="sch-print-headerbg" style="border-left-width:{totalWidth}px;height:{headerHeight}px;"></div><div class="sch-print-header-wrap">{[this.printLockedHeader(values)]}{[this.printNormalHeader(values)]}</div>{[this.printLockedGrid(values)]}{[this.printNormalGrid(values)]}</div><script type="text/javascript">{setupScript}<\/script></body></html>', {
		printLockedHeader : function (a) {
			var b = "";
			if (a.lockedGrid) {
				b += '<div style="left:-' + a.lockedScroll + "px;margin-right:-" + a.lockedScroll + "px;width:" + (a.lockedWidth + a.lockedScroll) + 'px"';
				b += 'class="sch-print-lockedheader ' + a.lockedGrid.headerCt.el.dom.className + '">';
				b += a.lockedHeader;
				b += "</div>"
			}
			return b
		},
		printNormalHeader : function (a) {
			var b = "";
			if (a.normalGrid) {
				b += '<div style="left:' + (a.lockedGrid ? a.lockedWidth : "0") + "px;width:" + a.normalWidth + 'px;" class="sch-print-normalheader ' + a.normalGrid.headerCt.el.dom.className + '">';
				b += '<div style="margin-left:-' + a.normalScroll + 'px">' + a.normalHeader + "</div>";
				b += "</div>"
			}
			return b
		},
		printLockedGrid : function (a) {
			var b = "";
			if (a.lockedGrid) {
				b += '<div id="lockedRowsCt" style="left:-' + a.lockedScroll + "px;margin-right:-" + a.lockedScroll + "px;width:" + (a.lockedWidth + a.lockedScroll) + "px;top:" + a.headerHeight + 'px;" class="sch-print-locked-rows-ct ' + a.innerLockedClasses + " " + Ext.baseCSSPrefix + 'grid-inner-locked">';
				b += a.lockedRows;
				b += "</div>"
			}
			return b
		},
		printNormalGrid : function (a) {
			var b = "";
			if (a.normalGrid) {
				b += '<div id="normalRowsCt" style="left:' + (a.lockedGrid ? a.lockedWidth : "0") + "px;top:" + a.headerHeight + "px;width:" + a.normalWidth + 'px" class="sch-print-normal-rows-ct ' + a.innerNormalClasses + '">';
				b += '<div style="position:relative;overflow:visible;margin-left:-' + a.normalScroll + 'px">' + a.normalRows + "</div>";
				b += "</div>"
			}
			return b
		}
	}),
	getGridContent : function (n) {
		var m = n.normalGrid,
		e = n.lockedGrid,
		o = e.getView(),
		g = m.getView(),
		j,
		d,
		l,
		i,
		k,
		b,
		h;
		this.beforePrint(n);
		if (e.collapsed && !m.collapsed) {
			b = e.getWidth() + m.getWidth()
		} else {
			b = m.getWidth();
			h = e.getWidth()
		}
		var c = o.store.getRange();
		d = o.tpl.apply(o.collectData(c, 0));
		l = g.tpl.apply(g.collectData(c, 0));
		i = o.el.getScroll().left;
		k = g.el.getScroll().left;
		var a = document.createElement("div");
		a.innerHTML = d;
		a.firstChild.style.width = o.el.dom.style.width;
		if (Ext.versions.extjs.isLessThan("4.2.1")) {
			e.headerCt.items.each(function (q, p) {
				if (q.isHidden()) {
					Ext.fly(a).down("colgroup:nth-child(" + (p + 1) + ") col").setWidth(0)
				}
			})
		}
		d = a.innerHTML;
		if (Sch.feature && Sch.feature.AbstractTimeSpan) {
			var f = (n.plugins || []).concat(n.normalGrid.plugins || []).concat(n.columnLinesFeature || []);
			Ext.each(f, function (p) {
				if (p instanceof Sch.feature.AbstractTimeSpan && p.generateMarkup) {
					l = p.generateMarkup(true) + l
				}
			})
		}
		this.afterPrint(n);
		return {
			normalHeader : m.headerCt.el.dom.innerHTML,
			lockedHeader : e.headerCt.el.dom.innerHTML,
			lockedGrid : e.collapsed ? false : e,
			normalGrid : m.collapsed ? false : m,
			lockedRows : d,
			normalRows : l,
			lockedScroll : i,
			normalScroll : k,
			lockedWidth : h - (Ext.isWebKit ? 1 : 0),
			normalWidth : b,
			headerHeight : m.headerCt.getHeight(),
			innerLockedClasses : e.view.el.dom.className,
			innerNormalClasses : m.view.el.dom.className + (this.fakeBackgroundColor ? " sch-print-fake-background" : ""),
			width : n.getWidth()
		}
	},
	getStylesheets : function () {
		return Ext.getDoc().select('link[rel="stylesheet"]')
	},
	print : function () {
		var g = this.scheduler;
		if (!(this.mainTpl instanceof Ext.Template)) {
			var a = 22;
			this.mainTpl = new Ext.XTemplate(this.mainTpl, {
					compiled : true,
					disableFormats : true
				})
		}
		var h = g.getView(),
		i = this.getStylesheets(),
		e = Ext.get(Ext.core.DomHelper.createDom({
					tag : "div"
				})),
		b;
		i.each(function (j) {
			e.appendChild(j.dom.cloneNode(true))
		});
		b = e.dom.innerHTML + "";
		var f = this.getGridContent(g),
		c = this.mainTpl.apply(Ext.apply({
					waitText : this.waitText,
					docType : this.docType,
					htmlClasses : Ext.getBody().parent().dom.className,
					bodyClasses : Ext.getBody().dom.className,
					componentClasses : g.el.dom.className,
					title : (g.title || ""),
					styles : b,
					totalWidth : g.getWidth(),
					setupScript : ("window.onload = function(){ (" + this.setupScript.toString() + ")(" + g.syncRowHeight + ", " + this.autoPrintAndClose + ", " + Ext.isChrome + ", " + Ext.isIE + "); };")
				}, f));
		var d = window.open("", "printgrid");
		if (!d || !d.document) {
			return false
		}
		this.printWindow = d;
		d.document.write(c);
		d.document.close()
	},
	setupScript : function (e, a, d, b) {
		var c = function () {
			if (e) {
				var f = document.getElementById("lockedRowsCt"),
				o = document.getElementById("normalRowsCt"),
				g = f && f.getElementsByTagName("tr"),
				m = o && o.getElementsByTagName("tr"),
				k = m && g ? m.length : 0;
				for (var j = 0; j < k; j++) {
					var h = m[j].clientHeight;
					var l = g[j].clientHeight;
					var n = Math.max(h, l) + "px";
					g[j].style.height = m[j].style.height = n
				}
			}
			document._loaded = true;
			if (a) {
				window.print();
				if (!d) {
					window.close()
				}
			}
		};
		if (b) {
			setTimeout(c, 0)
		} else {
			c()
		}
	}
});
Ext.define("Sch.plugin.Export", {
	extend : "Ext.util.Observable",
	alternateClassName : "Sch.plugin.PdfExport",
	alias : "plugin.scheduler_export",
	mixins : ["Ext.AbstractPlugin"],
	requires : ["Ext.XTemplate"],
	lockableScope : "top",
	printServer : undefined,
	tpl : null,
	exportDialogClassName : "Sch.widget.ExportDialog",
	exportDialogConfig : {},
	defaultConfig : {
		format : "A4",
		orientation : "portrait",
		range : "complete",
		showHeader : true,
		singlePageExport : false
	},
	expandAllBeforeExport : false,
	pageSizes : {
		A5 : {
			width : 5.8,
			height : 8.3
		},
		A4 : {
			width : 8.3,
			height : 11.7
		},
		A3 : {
			width : 11.7,
			height : 16.5
		},
		Letter : {
			width : 8.5,
			height : 11
		},
		Legal : {
			width : 8.5,
			height : 14
		}
	},
	openAfterExport : true,
	beforeExport : Ext.emptyFn,
	afterExport : Ext.emptyFn,
	fileFormat : "pdf",
	DPI : 72,
	constructor : function (a) {
		a = a || {};
		if (a.exportDialogConfig) {
			Ext.Object.each(this.defaultConfig, function (c, b, e) {
				var d = a.exportDialogConfig[c];
				if (d) {
					e[c] = d
				}
			})
		}
		this.callParent([a]);
		if (!this.tpl) {
			this.tpl = new Ext.XTemplate('<!DOCTYPE html><html class="' + Ext.baseCSSPrefix + 'border-box {htmlClasses}"><head><meta content="text/html; charset=UTF-8" http-equiv="Content-Type" /><title>{column}/{row}</title>{styles}</head><body class="' + Ext.baseCSSPrefix + 'webkit sch-export {bodyClasses}"><tpl if="showHeader"><div class="sch-export-header" style="width:{totalWidth}px"><h2>{column}/{row}</h2></div></tpl><div class="{componentClasses}" style="height:{bodyHeight}px; width:{totalWidth}px; position: relative !important">{HTML}</div></body></html>', {
					disableFormats : true
				})
		}
		this.setFileFormat(this.fileFormat)
	},
	init : function (a) {
		this.scheduler = a;
		a.showExportDialog = Ext.Function.bind(this.showExportDialog, this);
		a.doExport = Ext.Function.bind(this.doExport, this)
	},
	setFileFormat : function (a) {
		if (typeof a !== "string") {
			this.fileFormat = "pdf"
		} else {
			a = a.toLowerCase();
			if (a === "png") {
				this.fileFormat = a
			} else {
				this.fileFormat = "pdf"
			}
		}
	},
	showExportDialog : function () {
		var b = this,
		a = b.scheduler.getSchedulingView();
		if (b.win) {
			b.win.destroy();
			b.win = null
		}
		b.win = Ext.create(b.exportDialogClassName, {
				plugin : b,
				exportDialogConfig : Ext.apply({
					startDate : b.scheduler.getStart(),
					endDate : b.scheduler.getEnd(),
					rowHeight : a.timeAxisViewModel.getViewRowHeight(),
					columnWidth : a.timeAxisViewModel.getTickWidth(),
					defaultConfig : b.defaultConfig
				}, b.exportDialogConfig)
			});
		b.saveRestoreData();
		b.win.show()
	},
	saveRestoreData : function () {
		var b = this.scheduler,
		a = b.getSchedulingView(),
		c = b.normalGrid,
		d = b.lockedGrid;
		this.restoreSettings = {
			width : b.getWidth(),
			height : b.getHeight(),
			rowHeight : a.timeAxisViewModel.getViewRowHeight(),
			columnWidth : a.timeAxisViewModel.getTickWidth(),
			startDate : b.getStart(),
			endDate : b.getEnd(),
			normalWidth : c.getWidth(),
			normalLeft : c.getEl().getStyle("left"),
			lockedWidth : d.getWidth(),
			lockedCollapse : d.collapsed,
			normalCollapse : c.collapsed
		}
	},
	getStylesheets : function () {
		var c = Ext.getDoc().select('link[rel="stylesheet"]'),
		a = Ext.get(Ext.core.DomHelper.createDom({
					tag : "div"
				})),
		b;
		c.each(function (d) {
			a.appendChild(d.dom.cloneNode(true))
		});
		b = a.dom.innerHTML + "";
		return b
	},
	doExport : function (n, j, q) {
		this.mask();
		var K = this,
		p = K.scheduler,
		r = p.getSchedulingView(),
		m = K.getStylesheets(),
		I = n || K.defaultConfig,
		s = p.normalGrid,
		F = p.lockedGrid,
		A = s.headerCt.getHeight();
		K.saveRestoreData();
		s.expand();
		F.expand();
		K.fireEvent("updateprogressbar", 0.1);
		if (this.expandAllBeforeExport && p.expandAll) {
			p.expandAll()
		}
		var J = p.timeAxis.getTicks(),
		t = r.timeAxisViewModel.getTickWidth(),
		D,
		e,
		g;
		if (!I.singlePageExport) {
			if (I.orientation === "landscape") {
				D = K.pageSizes[I.format].height * K.DPI;
				g = K.pageSizes[I.format].width * K.DPI
			} else {
				D = K.pageSizes[I.format].width * K.DPI;
				g = K.pageSizes[I.format].height * K.DPI
			}
			var H = 41;
			e = Math.floor(g) - A - (I.showHeader ? H : 0)
		}
		r.timeAxisViewModel.suppressFit = true;
		var E = 0;
		var k = 0;
		if (I.range !== "complete") {
			var d,
			b;
			switch (I.range) {
			case "date":
				d = new Date(I.dateFrom);
				b = new Date(I.dateTo);
				if (Sch.util.Date.getDurationInDays(d, b) < 1) {
					b = Sch.util.Date.add(b, Sch.util.Date.DAY, 1)
				}
				d = Sch.util.Date.constrain(d, p.getStart(), p.getEnd());
				b = Sch.util.Date.constrain(b, p.getStart(), p.getEnd());
				break;
			case "current":
				var L = r.getVisibleDateRange();
				d = L.startDate;
				b = L.endDate || r.timeAxis.getEnd();
				if (I.cellSize) {
					t = I.cellSize[0];
					if (I.cellSize.length > 1) {
						r.setRowHeight(I.cellSize[1])
					}
				}
				break
			}
			p.setTimeSpan(d, b);
			var c = Math.floor(r.timeAxis.getTickFromDate(d));
			var x = Math.floor(r.timeAxis.getTickFromDate(b));
			J = p.timeAxis.getTicks();
			J = Ext.Array.filter(J, function (i, a) {
					if (a < c) {
						E++;
						return false
					} else {
						if (a > x) {
							k++;
							return false
						}
					}
					return true
				})
		}
		this.beforeExport(p, J);
		var C,
		z,
		h;
		if (!I.singlePageExport) {
			p.setWidth(D);
			p.setTimeColumnWidth(t);
			r.timeAxisViewModel.setTickWidth(t);
			h = K.calculatePages(I, J, t, D, e);
			z = K.getExportJsonHtml(h, {
					styles : m,
					config : I,
					ticks : J,
					skippedColsBefore : E,
					skippedColsAfter : k,
					printHeight : e,
					paperWidth : D,
					headerHeight : A
				});
			C = I.format
		} else {
			z = K.getExportJsonHtml(null, {
					styles : m,
					config : I,
					ticks : J,
					skippedColsBefore : E,
					skippedColsAfter : k,
					timeColumnWidth : t
				});
			var f = K.getRealSize(),
			v = Ext.Number.toFixed(f.width / K.DPI, 1),
			u = Ext.Number.toFixed(f.height / K.DPI, 1);
			C = v + "in*" + u + "in"
		}
		K.fireEvent("updateprogressbar", 0.4);
		if (K.printServer) {
			if (!K.debug && !K.test) {
				Ext.Ajax.request({
					type : "POST",
					url : K.printServer,
					timeout : 60000,
					params : Ext.apply({
						html : {
							array : z
						},
						startDate : p.getStartDate(),
						endDate : p.getEndDate(),
						format : C,
						orientation : I.orientation,
						range : I.range,
						fileFormat : K.fileFormat
					}, this.getParameters()),
					success : function (a) {
						K.onSuccess(a, j, q)
					},
					failure : function (a) {
						K.onFailure(a, q)
					},
					scope : K
				})
			} else {
				if (K.debug) {
					var o,
					G = Ext.JSON.decode(z);
					for (var B = 0, y = G.length; B < y; B++) {
						o = window.open();
						o.document.write(G[B].html);
						o.document.close()
					}
				}
			}
		} else {
			throw "Print server URL is not defined, please specify printServer config"
		}
		r.timeAxisViewModel.suppressFit = false;
		K.restorePanel();
		this.afterExport(p);
		if (K.test) {
			return {
				htmlArray : Ext.JSON.decode(z),
				calculatedPages : h
			}
		}
	},
	getParameters : function () {
		return {}

	},
	getRealSize : function () {
		var c = this.scheduler,
		b = c.normalGrid.headerCt.getHeight(),
		e = "." + Ext.baseCSSPrefix + (Ext.versions.extjs.isLessThan("5.0") ? "grid-table" : "grid-item-container"),
		a = (b + c.lockedGrid.getView().getEl().down(e).getHeight()),
		d = (c.lockedGrid.headerCt.getEl().first().getWidth() + c.normalGrid.body.down(e).getWidth());
		return {
			width : d,
			height : a
		}
	},
	calculatePages : function (r, s, j, p, b) {
		var t = this,
		i = t.scheduler,
		q = i.lockedGrid,
		c = i.getSchedulingView().timeAxisViewModel.getViewRowHeight(),
		u = q.headerCt,
		o = u.getEl().first().getWidth(),
		h = null,
		k = 0;
		if (o > q.getWidth()) {
			var g = 0,
			d = 0,
			m = 0,
			n = false,
			e;
			h = [];
			q.headerCt.items.each(function (y, w, v) {
				e = y.width;
				if (!m || m + e < p) {
					m += e;
					if (w === v - 1) {
						n = true;
						var x = p - m;
						k = Math.floor(x / j)
					}
				} else {
					n = true
				}
				if (n) {
					d = w;
					h.push({
						firstColumnIdx : g,
						lastColumnIdx : d,
						totalColumnsWidth : m || e
					});
					g = d + 1;
					m = 0
				}
			})
		} else {
			k = Math.floor((p - o) / j)
		}
		var l = Math.floor(p / j),
		a = Math.ceil((s.length - k) / l),
		f = Math.floor(b / c);
		if (!h || a === 0) {
			a += 1
		}
		return {
			columnsAmountLocked : k,
			columnsAmountNormal : l,
			lockedColumnPages : h,
			rowsAmount : f,
			rowPages : Math.ceil(i.getSchedulingView().store.getCount() / f),
			columnPages : a,
			timeColumnWidth : j,
			lockedGridWidth : o,
			rowHeight : c,
			panelHTML : {}

		}
	},
	getExportJsonHtml : function (f, E) {
		var H = this,
		n = H.scheduler,
		y = [],
		v = new RegExp(Ext.baseCSSPrefix + "ie\\d?|" + Ext.baseCSSPrefix + "gecko", "g"),
		B = Ext.getBody().dom.className.replace(v, ""),
		q = n.el.dom.className,
		m = E.styles,
		F = E.config,
		G = E.ticks,
		o,
		d,
		e,
		p,
		r;
		if (Ext.isIE) {
			B += " sch-ie-export"
		}
		n.timeAxis.autoAdjust = false;
		if (!F.singlePageExport) {
			var s = f.columnsAmountLocked,
			u = f.columnsAmountNormal,
			l = f.lockedColumnPages,
			h = f.rowsAmount,
			t = f.rowPages,
			a = f.columnPages,
			C = E.paperWidth,
			c = E.printHeight,
			z = E.headerHeight,
			j = null,
			b,
			g;
			r = f.timeColumnWidth;
			o = f.panelHTML;
			o.skippedColsBefore = E.skippedColsBefore;
			o.skippedColsAfter = E.skippedColsAfter;
			if (l) {
				g = l.length;
				a += g
			}
			for (var A = 0; A < a; A++) {
				if (l && A < g) {
					if (A === g - 1 && s !== 0) {
						n.normalGrid.show();
						j = Ext.Number.constrain((s - 1), 0, (G.length - 1));
						n.setTimeSpan(G[0].start, G[j].end)
					} else {
						n.normalGrid.hide()
					}
					var D = l[A];
					this.showLockedColumns();
					this.hideLockedColumns(D.firstColumnIdx, D.lastColumnIdx);
					n.lockedGrid.setWidth(D.totalColumnsWidth + 1)
				} else {
					if (A === 0) {
						this.showLockedColumns();
						if (s !== 0) {
							n.normalGrid.show()
						}
						j = Ext.Number.constrain(s - 1, 0, G.length - 1);
						n.setTimeSpan(G[0].start, G[j].end)
					} else {
						n.lockedGrid.hide();
						n.normalGrid.show();
						if (j === null) {
							j = -1
						}
						if (G[j + u]) {
							n.setTimeSpan(G[j + 1].start, G[j + u].end);
							j = j + u
						} else {
							n.setTimeSpan(G[j + 1].start, G[G.length - 1].end)
						}
					}
				}
				n.setTimeColumnWidth(r, true);
				n.getSchedulingView().timeAxisViewModel.setTickWidth(r);
				for (var x = 0; x < t; x += 1) {
					H.hideRows(h, x);
					o.dom = n.body.dom.innerHTML;
					o.k = x;
					o.i = A;
					d = H.resizePanelHTML(o);
					p = H.tpl.apply(Ext.apply({
								bodyClasses : B,
								bodyHeight : c + z,
								componentClasses : q,
								styles : m,
								showHeader : F.showHeader,
								HTML : d.dom.innerHTML,
								totalWidth : C,
								headerHeight : z,
								column : A + 1,
								row : x + 1
							}));
					e = {
						html : p
					};
					y.push(e);
					H.showRows()
				}
			}
		} else {
			r = E.timeColumnWidth;
			o = f ? f.panelHTML : {};
			n.setTimeSpan(G[0].start, G[G.length - 1].end);
			n.lockedGrid.setWidth(n.lockedGrid.headerCt.getEl().first().getWidth());
			n.setTimeColumnWidth(r);
			n.getSchedulingView().timeAxisViewModel.setTickWidth(r);
			var w = H.getRealSize();
			Ext.apply(o, {
				dom : n.body.dom.innerHTML,
				column : 1,
				row : 1,
				timeColumnWidth : E.timeColumnWidth,
				skippedColsBefore : E.skippedColsBefore,
				skippedColsAfter : E.skippedColsAfter
			});
			d = H.resizePanelHTML(o);
			p = H.tpl.apply(Ext.apply({
						bodyClasses : B,
						bodyHeight : w.height,
						componentClasses : q,
						styles : m,
						showHeader : false,
						HTML : d.dom.innerHTML,
						totalWidth : w.width
					}));
			e = {
				html : p
			};
			y.push(e)
		}
		n.timeAxis.autoAdjust = true;
		return Ext.JSON.encode(y)
	},
	resizePanelHTML : function (f) {
		var k = Ext.get(Ext.core.DomHelper.createDom({
					tag : "div",
					html : f.dom
				})),
		j = this.scheduler,
		d = j.lockedGrid,
		i = j.normalGrid,
		g,
		e,
		b;
		if (Ext.isIE6 || Ext.isIE7 || Ext.isIEQuirks) {
			var h = document.createDocumentFragment(),
			a,
			c;
			if (h.getElementById) {
				a = "getElementById";
				c = ""
			} else {
				a = "querySelector";
				c = "#"
			}
			h.appendChild(k.dom);
			g = d.view.el;
			e = [h[a](c + j.id + "-targetEl"), h[a](c + j.id + "-innerCt"), h[a](c + d.id), h[a](c + d.body.id), h[a](c + g.id)];
			b = [h[a](c + i.id), h[a](c + i.headerCt.id), h[a](c + i.body.id), h[a](c + i.getView().id)];
			Ext.Array.each(e, function (l) {
				if (l !== null) {
					l.style.height = "100%";
					l.style.width = "100%"
				}
			});
			Ext.Array.each(b, function (m, l) {
				if (m !== null) {
					if (l === 1) {
						m.style.width = "100%"
					} else {
						m.style.height = "100%";
						m.style.width = "100%"
					}
				}
			});
			k.dom.innerHTML = h.firstChild.innerHTML
		} else {
			g = d.view.el;
			e = [k.select("#" + j.id + "-targetEl").first(), k.select("#" + j.id + "-innerCt").first(), k.select("#" + d.id).first(), k.select("#" + d.body.id).first(), k.select("#" + g.id)];
			b = [k.select("#" + i.id).first(), k.select("#" + i.headerCt.id).first(), k.select("#" + i.body.id).first(), k.select("#" + i.getView().id).first()];
			Ext.Array.each(e, function (m, l) {
				if (m) {
					m.setHeight("100%");
					if (l !== 3 && l !== 2) {
						m.setWidth("100%")
					}
				}
			});
			Ext.Array.each(b, function (m, l) {
				if (l === 1) {
					m.setWidth("100%")
				} else {
					m.applyStyles({
						height : "100%",
						width : "100%"
					})
				}
			})
		}
		return k
	},
	getWin : function () {
		return this.win || null
	},
	hideDialogWindow : function (a) {
		var b = this;
		b.fireEvent("hidedialogwindow", a);
		b.unmask();
		if (b.openAfterExport) {
			window.open(a.url, "ExportedPanel")
		}
	},
	onSuccess : function (c, h, b) {
		var d = this,
		g = d.getWin(),
		a;
		try {
			a = Ext.JSON.decode(c.responseText)
		} catch (f) {
			this.onFailure(c, b);
			return
		}
		d.fireEvent("updateprogressbar", 1, a);
		if (a.success) {
			setTimeout(function () {
				d.hideDialogWindow(a)
			}, g ? g.hideTime : 3000)
		} else {
			d.fireEvent("showdialogerror", g, a.msg, a)
		}
		d.unmask();
		if (h) {
			h.call(this, c)
		}
	},
	onFailure : function (b, a) {
		var c = this.getWin(),
		d = b.status === 200 ? b.responseText : b.statusText;
		this.fireEvent("showdialogerror", c, d);
		this.unmask();
		if (a) {
			a.call(this, b)
		}
	},
	hideRows : function (e, g) {
		var d = this.scheduler.lockedGrid.view.getNodes(),
		a = this.scheduler.normalGrid.view.getNodes(),
		h = e * g,
		c = h + e;
		for (var f = 0, b = a.length; f < b; f++) {
			if (f < h || f >= c) {
				d[f].className += " sch-none";
				a[f].className += " sch-none"
			}
		}
	},
	showRows : function () {
		this.scheduler.getEl().select(this.scheduler.getSchedulingView().getItemSelector()).each(function (a) {
			a.removeCls("sch-none")
		})
	},
	hideLockedColumns : function (c, e) {
		var d = this.scheduler.lockedGrid.headerCt.items.items;
		for (var b = 0, a = d.length; b < a; b++) {
			if (b < c || b > e) {
				d[b].hide()
			}
		}
	},
	showLockedColumns : function () {
		this.scheduler.lockedGrid.headerCt.items.each(function (a) {
			a.show()
		})
	},
	mask : function () {
		var a = Ext.getBody().mask();
		a.addCls("sch-export-mask")
	},
	unmask : function () {
		Ext.getBody().unmask()
	},
	restorePanel : function () {
		var b = this.scheduler,
		a = this.restoreSettings;
		b.setWidth(a.width);
		b.setHeight(a.height);
		b.setTimeSpan(a.startDate, a.endDate);
		b.setTimeColumnWidth(a.columnWidth, true);
		b.getSchedulingView().setRowHeight(a.rowHeight);
		b.lockedGrid.show();
		b.normalGrid.setWidth(a.normalWidth);
		b.normalGrid.getEl().setStyle("left", a.normalLeft);
		b.lockedGrid.setWidth(a.lockedWidth);
		if (a.lockedCollapse) {
			b.lockedGrid.collapse()
		}
		if (a.normalCollapse) {
			b.normalGrid.collapse()
		}
		b.getSchedulingView().timeAxisViewModel.update()
	},
	destroy : function () {
		if (this.win) {
			this.win.destroy()
		}
	}
});
Ext.define("Sch.plugin.HeaderZoom", {
	extend : "Sch.util.DragTracker",
	mixins : ["Ext.AbstractPlugin"],
	alias : "plugin.scheduler_headerzoom",
	lockableScope : "top",
	scheduler : null,
	proxy : null,
	headerRegion : null,
	init : function (a) {
		a.on({
			destroy : this.onSchedulerDestroy,
			scope : this
		});
		this.scheduler = a;
		this.onOrientationChange();
		a.on("orientationchange", this.onOrientationChange, this)
	},
	onOrientationChange : function () {
		var a = this.scheduler.down("timeaxiscolumn");
		if (a) {
			if (a.rendered) {
				this.onTimeAxisColumnRender(a)
			} else {
				a.on({
					afterrender : this.onTimeAxisColumnRender,
					scope : this
				})
			}
		}
	},
	onTimeAxisColumnRender : function (a) {
		this.proxy = a.el.createChild({
				cls : "sch-drag-selector"
			});
		this.initEl(a.el)
	},
	onStart : function (a) {
		this.proxy.show();
		this.headerRegion = this.scheduler.normalGrid.headerCt.getRegion()
	},
	onDrag : function (b) {
		var c = this.headerRegion;
		var a = this.getRegion().constrainTo(c);
		a.top = c.top;
		a.bottom = c.bottom;
		this.proxy.setRegion(a)
	},
	onEnd : function (g) {
		if (this.proxy) {
			this.proxy.setDisplayed(false);
			var b = this.scheduler;
			var d = b.timeAxis;
			var f = this.getRegion();
			var c = b.getSchedulingView().timeAxisViewModel.getBottomHeader().unit;
			var a = b.getSchedulingView().getStartEndDatesFromRegion(f);
			b.zoomToSpan({
				start : d.floorDate(a.start, false, c, 1),
				end : d.ceilDate(a.end, false, c, 1)
			})
		}
	},
	onSchedulerDestroy : function () {
		if (this.proxy) {
			Ext.destroy(this.proxy);
			this.proxy = null
		}
		this.destroy()
	}
});
Ext.define("Sch.widget.ResizePicker", {
	extend : "Ext.Panel",
	alias : "widget.dualrangepicker",
	width : 200,
	height : 200,
	border : true,
	collapsible : false,
	bodyStyle : "position:absolute; margin:5px",
	verticalCfg : {
		height : 120,
		value : 24,
		increment : 2,
		minValue : 20,
		maxValue : 80,
		reverse : true,
		disabled : true
	},
	horizontalCfg : {
		width : 120,
		value : 100,
		minValue : 25,
		increment : 5,
		maxValue : 200,
		disable : true
	},
	initComponent : function () {
		var a = this;
		a.horizontalCfg.value = a.dialogConfig.columnWidth;
		a.verticalCfg.value = a.dialogConfig.rowHeight;
		a.verticalCfg.disabled = a.dialogConfig.scrollerDisabled || false;
		a.dockedItems = [a.vertical = new Ext.slider.Single(Ext.apply({
						dock : "left",
						style : "margin-top:10px",
						vertical : true,
						listeners : {
							change : a.onSliderChange,
							changecomplete : a.onSliderChangeComplete,
							scope : a
						}
					}, a.verticalCfg)), a.horizontal = new Ext.slider.Single(Ext.apply({
						dock : "top",
						style : "margin-left:28px",
						listeners : {
							change : a.onSliderChange,
							changecomplete : a.onSliderChangeComplete,
							scope : a
						}
					}, a.horizontalCfg))];
		a.callParent(arguments)
	},
	afterRender : function () {
		var b = this;
		b.addCls("sch-ux-range-picker");
		b.valueHandle = this.body.createChild({
				cls : "sch-ux-range-value",
				cn : {
					tag : "span"
				}
			});
		b.valueSpan = this.valueHandle.down("span");
		var a = new Ext.dd.DD(this.valueHandle);
		Ext.apply(a, {
			startDrag : function () {
				b.dragging = true;
				this.constrainTo(b.body)
			},
			onDrag : function () {
				b.onHandleDrag.apply(b, arguments)
			},
			endDrag : function () {
				b.onHandleEndDrag.apply(b, arguments);
				b.dragging = false
			},
			scope : this
		});
		this.setValues(this.getValues());
		this.callParent(arguments);
		this.body.on("click", this.onBodyClick, this)
	},
	onBodyClick : function (c, a) {
		var b = [c.getXY()[0] - 8 - this.body.getX(), c.getXY()[1] - 8 - this.body.getY()];
		this.valueHandle.setLeft(Ext.Number.constrain(b[0], 0, this.getAvailableWidth()));
		this.valueHandle.setTop(Ext.Number.constrain(b[1], 0, this.getAvailableHeight()));
		this.setValues(this.getValuesFromXY([this.valueHandle.getLeft(true), this.valueHandle.getTop(true)]));
		this.onSliderChangeComplete()
	},
	getAvailableWidth : function () {
		return this.body.getWidth() - 18
	},
	getAvailableHeight : function () {
		return this.body.getHeight() - 18
	},
	onHandleDrag : function () {
		this.setValues(this.getValuesFromXY([this.valueHandle.getLeft(true), this.valueHandle.getTop(true)]))
	},
	onHandleEndDrag : function () {
		this.setValues(this.getValuesFromXY([this.valueHandle.getLeft(true), this.valueHandle.getTop(true)]))
	},
	getValuesFromXY : function (d) {
		var c = d[0] / this.getAvailableWidth();
		var a = d[1] / this.getAvailableHeight();
		var e = Math.round((this.horizontalCfg.maxValue - this.horizontalCfg.minValue) * c);
		var b = Math.round((this.verticalCfg.maxValue - this.verticalCfg.minValue) * a) + this.verticalCfg.minValue;
		return [e + this.horizontalCfg.minValue, b]
	},
	getXYFromValues : function (d) {
		var b = this.horizontalCfg.maxValue - this.horizontalCfg.minValue;
		var f = this.verticalCfg.maxValue - this.verticalCfg.minValue;
		var a = Math.round((d[0] - this.horizontalCfg.minValue) * this.getAvailableWidth() / b);
		var c = d[1] - this.verticalCfg.minValue;
		var e = Math.round(c * this.getAvailableHeight() / f);
		return [a, e]
	},
	updatePosition : function () {
		var a = this.getValues();
		var b = this.getXYFromValues(a);
		this.valueHandle.setLeft(Ext.Number.constrain(b[0], 0, this.getAvailableWidth()));
		if (this.verticalCfg.disabled) {
			this.valueHandle.setTop(this.dialogConfig.rowHeight)
		} else {
			this.valueHandle.setTop(Ext.Number.constrain(b[1], 0, this.getAvailableHeight()))
		}
		this.positionValueText();
		this.setValueText(a)
	},
	positionValueText : function () {
		var a = this.valueHandle.getTop(true);
		var b = this.valueHandle.getLeft(true);
		this.valueSpan.setLeft(b > 30 ? -30 : 10);
		this.valueSpan.setTop(a > 10 ? -20 : 20)
	},
	setValueText : function (a) {
		if (this.verticalCfg.disabled) {
			a[1] = this.dialogConfig.rowHeight
		}
		this.valueSpan.update("[" + a.toString() + "]")
	},
	setValues : function (a) {
		this.horizontal.setValue(a[0]);
		if (this.verticalCfg.reverse) {
			if (!this.verticalCfg.disabled) {
				this.vertical.setValue(this.verticalCfg.maxValue + this.verticalCfg.minValue - a[1])
			}
		} else {
			if (!this.verticalCfg.disabled) {
				this.vertical.setValue(a[1])
			}
		}
		if (!this.dragging) {
			this.updatePosition()
		}
		this.positionValueText();
		this.setValueText(a)
	},
	getValues : function () {
		if (!this.verticalCfg.disabled) {
			var a = this.vertical.getValue();
			if (this.verticalCfg.reverse) {
				a = this.verticalCfg.maxValue - a + this.verticalCfg.minValue
			}
			return [this.horizontal.getValue(), a]
		}
		return [this.horizontal.getValue()]
	},
	onSliderChange : function () {
		this.fireEvent("change", this, this.getValues());
		if (!this.dragging) {
			this.updatePosition()
		}
	},
	onSliderChangeComplete : function () {
		this.fireEvent("changecomplete", this, this.getValues())
	},
	afterLayout : function () {
		this.callParent(arguments);
		this.updatePosition()
	}
});
Ext.define("Sch.widget.ExportDialogForm", {
	extend : "Ext.form.Panel",
	requires : ["Ext.data.Store", "Ext.ProgressBar", "Ext.form.field.ComboBox", "Ext.form.field.Date", "Ext.form.FieldContainer", "Ext.form.field.Checkbox", "Sch.widget.ResizePicker"],
	border : false,
	bodyPadding : "10 10 0 10",
	autoHeight : true,
	initComponent : function () {
		var a = this;
		if (Ext.getVersion("extjs").isLessThan("4.2.1")) {
			if (typeof Ext.tip !== "undefined" && Ext.tip.Tip && Ext.tip.Tip.prototype.minWidth != "auto") {
				Ext.tip.Tip.prototype.minWidth = "auto"
			}
		}
		a.createFields();
		Ext.apply(this, {
			fieldDefaults : {
				labelAlign : "left",
				labelWidth : 120,
				anchor : "99%"
			},
			items : [a.rangeField, a.resizerHolder, a.datesHolder, a.showHeaderField, a.exportToSingleField, a.formatField, a.orientationField, a.progressBar || a.createProgressBar()]
		});
		a.callParent(arguments);
		a.onRangeChange(null, a.dialogConfig.defaultConfig.range);
		a.on({
			hideprogressbar : a.hideProgressBar,
			showprogressbar : a.showProgressBar,
			updateprogressbar : a.updateProgressBar,
			scope : a
		})
	},
	isValid : function () {
		var a = this;
		if (a.rangeField.getValue() === "date") {
			return a.dateFromField.isValid() && a.dateToField.isValid()
		}
		return true
	},
	getValues : function (e, c, d, b) {
		var a = this.callParent(arguments);
		var f = this.resizePicker.getValues();
		if (!e) {
			a.cellSize = f
		} else {
			a += "&cellSize[0]=" + f[0] + "&cellSize[1]=" + f[1]
		}
		return a
	},
	createFields : function () {
		var d = this,
		a = d.dialogConfig,
		f = '<table class="sch-fieldcontainer-label-wrap"><td width="1" class="sch-fieldcontainer-label">',
		e = '<td><div class="sch-fieldcontainer-separator"></div></table>';
		d.rangeField = new Ext.form.field.ComboBox({
				value : a.defaultConfig.range,
				triggerAction : "all",
				cls : "sch-export-dialog-range",
				forceSelection : true,
				editable : false,
				fieldLabel : a.rangeFieldLabel,
				name : "range",
				queryMode : "local",
				displayField : "name",
				valueField : "value",
				store : new Ext.data.Store({
					fields : ["name", "value"],
					data : [{
							name : a.completeViewText,
							value : "complete"
						}, {
							name : a.dateRangeText,
							value : "date"
						}, {
							name : a.currentViewText,
							value : "current"
						}
					]
				}),
				listeners : {
					change : d.onRangeChange,
					scope : d
				}
			});
		d.resizePicker = new Sch.widget.ResizePicker({
				dialogConfig : a,
				margin : "10 20"
			});
		d.resizerHolder = new Ext.form.FieldContainer({
				fieldLabel : a.scrollerDisabled ? a.adjustCols : a.adjustColsAndRows,
				labelAlign : "top",
				hidden : true,
				labelSeparator : "",
				beforeLabelTextTpl : f,
				afterLabelTextTpl : e,
				layout : "vbox",
				defaults : {
					flex : 1,
					allowBlank : false
				},
				items : [d.resizePicker]
			});
		d.dateFromField = new Ext.form.field.Date({
				fieldLabel : a.dateRangeFromText,
				baseBodyCls : "sch-exportdialogform-date",
				name : "dateFrom",
				format : a.dateRangeFormat || Ext.Date.defaultFormat,
				allowBlank : false,
				maxValue : a.endDate,
				minValue : a.startDate,
				value : a.startDate
			});
		d.dateToField = new Ext.form.field.Date({
				fieldLabel : a.dateRangeToText,
				name : "dateTo",
				format : a.dateRangeFormat || Ext.Date.defaultFormat,
				baseBodyCls : "sch-exportdialogform-date",
				allowBlank : false,
				maxValue : a.endDate,
				minValue : a.startDate,
				value : a.endDate
			});
		d.datesHolder = new Ext.form.FieldContainer({
				fieldLabel : a.specifyDateRange,
				labelAlign : "top",
				hidden : true,
				labelSeparator : "",
				beforeLabelTextTpl : f,
				afterLabelTextTpl : e,
				layout : "vbox",
				defaults : {
					flex : 1,
					allowBlank : false
				},
				items : [d.dateFromField, d.dateToField]
			});
		d.showHeaderField = new Ext.form.field.Checkbox({
				xtype : "checkboxfield",
				boxLabel : d.dialogConfig.showHeaderLabel,
				name : "showHeader",
				checked : !!a.defaultConfig.showHeaderLabel
			});
		d.exportToSingleField = new Ext.form.field.Checkbox({
				xtype : "checkboxfield",
				boxLabel : d.dialogConfig.exportToSingleLabel,
				name : "singlePageExport",
				checked : !!a.defaultConfig.singlePageExport
			});
		d.formatField = new Ext.form.field.ComboBox({
				value : a.defaultConfig.format,
				triggerAction : "all",
				forceSelection : true,
				editable : false,
				fieldLabel : a.formatFieldLabel,
				name : "format",
				queryMode : "local",
				store : ["A5", "A4", "A3", "Letter", "Legal"]
			});
		var c = a.defaultConfig.orientation === "portrait" ? 'class="sch-none"' : "",
		b = a.defaultConfig.orientation === "landscape" ? 'class="sch-none"' : "";
		d.orientationField = new Ext.form.field.ComboBox({
				value : a.defaultConfig.orientation,
				triggerAction : "all",
				baseBodyCls : "sch-exportdialogform-orientation",
				forceSelection : true,
				editable : false,
				fieldLabel : d.dialogConfig.orientationFieldLabel,
				afterSubTpl : new Ext.XTemplate('<span id="sch-exportdialog-imagePortrait" ' + b + '></span><span id="sch-exportdialog-imageLandscape" ' + c + "></span>"),
				name : "orientation",
				displayField : "name",
				valueField : "value",
				queryMode : "local",
				store : new Ext.data.Store({
					fields : ["name", "value"],
					data : [{
							name : a.orientationPortraitText,
							value : "portrait"
						}, {
							name : a.orientationLandscapeText,
							value : "landscape"
						}
					]
				}),
				listeners : {
					change : function (h, g) {
						switch (g) {
						case "landscape":
							Ext.fly("sch-exportdialog-imagePortrait").toggleCls("sch-none");
							Ext.fly("sch-exportdialog-imageLandscape").toggleCls("sch-none");
							break;
						case "portrait":
							Ext.fly("sch-exportdialog-imagePortrait").toggleCls("sch-none");
							Ext.fly("sch-exportdialog-imageLandscape").toggleCls("sch-none");
							break
						}
					}
				}
			})
	},
	createProgressBar : function () {
		return this.progressBar = new Ext.ProgressBar({
				text : this.config.progressBarText,
				animate : true,
				hidden : true,
				margin : "4px 0 10px 0"
			})
	},
	onRangeChange : function (b, a) {
		switch (a) {
		case "complete":
			this.datesHolder.hide();
			this.resizerHolder.hide();
			break;
		case "date":
			this.datesHolder.show();
			this.resizerHolder.hide();
			break;
		case "current":
			this.datesHolder.hide();
			this.resizerHolder.show();
			this.resizePicker.expand(true);
			break
		}
	},
	showProgressBar : function () {
		if (this.progressBar) {
			this.progressBar.show()
		}
	},
	hideProgressBar : function () {
		if (this.progressBar) {
			this.progressBar.hide()
		}
	},
	updateProgressBar : function (a) {
		if (this.progressBar) {
			this.progressBar.updateProgress(a)
		}
	}
});
Ext.define("Sch.widget.ExportDialog", {
	alternateClassName : "Sch.widget.PdfExportDialog",
	extend : "Ext.window.Window",
	requires : ["Sch.widget.ExportDialogForm"],
	mixins : ["Sch.mixin.Localizable"],
	alias : "widget.exportdialog",
	modal : false,
	width : 350,
	cls : "sch-exportdialog",
	frame : false,
	layout : "fit",
	draggable : true,
	padding : 0,
	plugin : null,
	buttonsPanel : null,
	buttonsPanelScope : null,
	progressBar : null,
	dateRangeFormat : "",
	constructor : function (a) {
		Ext.apply(this, a.exportDialogConfig);
		Ext.Array.forEach(["generalError", "title", "formatFieldLabel", "orientationFieldLabel", "rangeFieldLabel", "showHeaderLabel", "orientationPortraitText", "orientationLandscapeText", "completeViewText", "currentViewText", "dateRangeText", "dateRangeFromText", "pickerText", "dateRangeToText", "exportButtonText", "cancelButtonText", "progressBarText", "exportToSingleLabel"], function (b) {
			if (b in a) {
				this[b] = a[b]
			}
		}, this);
		this.title = this.L("title");
		this.config = Ext.apply({
				progressBarText : this.L("progressBarText"),
				dateRangeToText : this.L("dateRangeToText"),
				pickerText : this.L("pickerText"),
				dateRangeFromText : this.L("dateRangeFromText"),
				dateRangeText : this.L("dateRangeText"),
				currentViewText : this.L("currentViewText"),
				formatFieldLabel : this.L("formatFieldLabel"),
				orientationFieldLabel : this.L("orientationFieldLabel"),
				rangeFieldLabel : this.L("rangeFieldLabel"),
				showHeaderLabel : this.L("showHeaderLabel"),
				exportToSingleLabel : this.L("exportToSingleLabel"),
				orientationPortraitText : this.L("orientationPortraitText"),
				orientationLandscapeText : this.L("orientationLandscapeText"),
				completeViewText : this.L("completeViewText"),
				adjustCols : this.L("adjustCols"),
				adjustColsAndRows : this.L("adjustColsAndRows"),
				specifyDateRange : this.L("specifyDateRange"),
				dateRangeFormat : this.dateRangeFormat,
				defaultConfig : this.defaultConfig
			}, a.exportDialogConfig);
		this.callParent(arguments)
	},
	initComponent : function () {
		var b = this,
		a = {
			hidedialogwindow : b.destroy,
			showdialogerror : b.showError,
			updateprogressbar : function (c) {
				b.fireEvent("updateprogressbar", c)
			},
			scope : this
		};
		b.form = b.buildForm(b.config);
		Ext.apply(this, {
			items : b.form,
			fbar : b.buildButtons(b.buttonsPanelScope || b)
		});
		b.callParent(arguments);
		b.plugin.on(a)
	},
	afterRender : function () {
		var a = this;
		a.relayEvents(a.form.resizePicker, ["change", "changecomplete", "select"]);
		a.form.relayEvents(a, ["updateprogressbar", "hideprogressbar", "showprogressbar"]);
		a.callParent(arguments)
	},
	buildButtons : function (a) {
		return [{
				xtype : "button",
				scale : "medium",
				text : this.L("exportButtonText"),
				handler : function () {
					if (this.form.isValid()) {
						this.fireEvent("showprogressbar");
						this.plugin.doExport(this.form.getValues())
					}
				},
				scope : a
			}, {
				xtype : "button",
				scale : "medium",
				text : this.L("cancelButtonText"),
				handler : function () {
					this.destroy()
				},
				scope : a
			}
		]
	},
	buildForm : function (a) {
		return new Sch.widget.ExportDialogForm({
			progressBar : this.progressBar,
			dialogConfig : a
		})
	},
	showError : function (b, a) {
		var c = b,
		d = a || c.L("generalError");
		c.fireEvent("hideprogressbar");
		Ext.Msg.alert("", d)
	}
});
Ext.define("Gnt.locale.En", {
	extend : "Sch.locale.Locale",
	requires : "Sch.locale.En",
	singleton : true,
	l10n : {
		"Gnt.util.DurationParser" : {
			unitsRegex : {
				MILLI : /^ms$|^mil/i,
				SECOND : /^s$|^sec/i,
				MINUTE : /^m$|^min/i,
				HOUR : /^h$|^hr$|^hour/i,
				DAY : /^d$|^day/i,
				WEEK : /^w$|^wk|^week/i,
				MONTH : /^mo|^mnt/i,
				QUARTER : /^q$|^quar|^qrt/i,
				YEAR : /^y$|^yr|^year/i
			}
		},
		"Gnt.field.Duration" : {
			invalidText : "Invalid duration value"
		},
		"Gnt.feature.DependencyDragDrop" : {
			fromText : "From",
			toText : "To",
			startText : "Start",
			endText : "End"
		},
		"Gnt.Tooltip" : {
			startText : "Starts: ",
			endText : "Ends: ",
			durationText : "Duration: "
		},
		"Gnt.plugin.TaskContextMenu" : {
			taskInformation : "Task information...",
			newTaskText : "New task",
			deleteTask : "Delete task(s)",
			editLeftLabel : "Edit left label",
			editRightLabel : "Edit right label",
			add : "Add...",
			deleteDependency : "Delete dependency...",
			addTaskAbove : "Task above",
			addTaskBelow : "Task below",
			addMilestone : "Milestone",
			addSubtask : "Sub-task",
			addSuccessor : "Successor",
			addPredecessor : "Predecessor",
			convertToMilestone : "Convert to milestone",
			convertToRegular : "Convert to regular task"
		},
		"Gnt.plugin.DependencyEditor" : {
			fromText : "From",
			toText : "To",
			typeText : "Type",
			lagText : "Lag",
			endToStartText : "Finish-To-Start",
			startToStartText : "Start-To-Start",
			endToEndText : "Finish-To-Finish",
			startToEndText : "Start-To-Finish"
		},
		"Gnt.widget.calendar.Calendar" : {
			dayOverrideNameHeaderText : "Name",
			overrideName : "Name",
			startDate : "Start Date",
			endDate : "End Date",
			error : "Error",
			dateText : "Date",
			addText : "Add",
			editText : "Edit",
			removeText : "Remove",
			workingDayText : "Working day",
			weekendsText : "Weekends",
			overriddenDayText : "Overridden day",
			overriddenWeekText : "Overridden week",
			workingTimeText : "Working time",
			nonworkingTimeText : "Non-working time",
			dayOverridesText : "Day overrides",
			weekOverridesText : "Week overrides",
			okText : "OK",
			cancelText : "Cancel",
			parentCalendarText : "Parent calendar",
			noParentText : "No parent",
			selectParentText : "Select parent",
			newDayName : "[Without name]",
			calendarNameText : "Calendar name",
			tplTexts : {
				tplWorkingHours : "Working hours for",
				tplIsNonWorking : "is non-working",
				tplOverride : "override",
				tplInCalendar : "in calendar",
				tplDayInCalendar : "standard day in calendar",
				tplBasedOn : "Based on"
			},
			overrideErrorText : "There is already an override for this day",
			overrideDateError : "There is already a week override on this date: {0}",
			startAfterEndError : "Start date should be less than end date",
			weeksIntersectError : "Week overrides should not intersect"
		},
		"Gnt.widget.calendar.AvailabilityGrid" : {
			startText : "Start",
			endText : "End",
			addText : "Add",
			removeText : "Remove",
			error : "Error"
		},
		"Gnt.widget.calendar.DayEditor" : {
			workingTimeText : "Working time",
			nonworkingTimeText : "Non-working time"
		},
		"Gnt.widget.calendar.WeekEditor" : {
			defaultTimeText : "Default time",
			workingTimeText : "Working time",
			nonworkingTimeText : "Non-working time",
			error : "Error",
			noOverrideError : "Week override contains only 'default' days - can't save it"
		},
		"Gnt.widget.calendar.ResourceCalendarGrid" : {
			name : "Name",
			calendar : "Calendar"
		},
		"Gnt.widget.calendar.CalendarWindow" : {
			ok : "Ok",
			cancel : "Cancel"
		},
		"Gnt.field.Assignment" : {
			cancelText : "Cancel",
			closeText : "Save and Close"
		},
		"Gnt.column.AssignmentUnits" : {
			text : "Units"
		},
		"Gnt.column.Duration" : {
			text : "Duration"
		},
		"Gnt.column.Effort" : {
			text : "Effort"
		},
		"Gnt.column.EndDate" : {
			text : "Finish"
		},
		"Gnt.column.PercentDone" : {
			text : "% Done"
		},
		"Gnt.column.ResourceAssignment" : {
			text : "Assigned Resources"
		},
		"Gnt.column.ResourceName" : {
			text : "Resource Name"
		},
		"Gnt.column.SchedulingMode" : {
			text : "Mode"
		},
		"Gnt.column.Predecessor" : {
			text : "Predecessors"
		},
		"Gnt.column.Successor" : {
			text : "Successors"
		},
		"Gnt.column.StartDate" : {
			text : "Start"
		},
		"Gnt.column.WBS" : {
			text : "WBS"
		},
		"Gnt.column.Sequence" : {
			text : "#"
		},
		"Gnt.widget.taskeditor.TaskForm" : {
			taskNameText : "Name",
			durationText : "Duration",
			datesText : "Dates",
			baselineText : "Baseline",
			startText : "Start",
			finishText : "Finish",
			percentDoneText : "Percent Complete",
			baselineStartText : "Start",
			baselineFinishText : "Finish",
			baselinePercentDoneText : "Percent Complete",
			effortText : "Effort",
			invalidEffortText : "Invalid effort value",
			calendarText : "Calendar",
			schedulingModeText : "Scheduling Mode"
		},
		"Gnt.widget.DependencyGrid" : {
			idText : "ID",
			snText : "SN",
			taskText : "Task Name",
			blankTaskText : "Please select task",
			invalidDependencyText : "Invalid dependency",
			parentChildDependencyText : "Dependency between child and parent found",
			duplicatingDependencyText : "Duplicate dependency found",
			transitiveDependencyText : "Transitive dependency",
			cyclicDependencyText : "Cyclic dependency",
			typeText : "Type",
			lagText : "Lag",
			clsText : "CSS class",
			endToStartText : "Finish-To-Start",
			startToStartText : "Start-To-Start",
			endToEndText : "Finish-To-Finish",
			startToEndText : "Start-To-Finish"
		},
		"Gnt.widget.AssignmentEditGrid" : {
			confirmAddResourceTitle : "Confirm",
			confirmAddResourceText : "Resource &quot;{0}&quot; not found in list. Would you like to add it?",
			noValueText : "Please select resource to assign",
			noResourceText : "No resource &quot;{0}&quot; found in the list"
		},
		"Gnt.widget.taskeditor.TaskEditor" : {
			generalText : "General",
			resourcesText : "Resources",
			dependencyText : "Predecessors",
			addDependencyText : "Add new",
			dropDependencyText : "Remove",
			notesText : "Notes",
			advancedText : "Advanced",
			wbsCodeText : "WBS code",
			addAssignmentText : "Add new",
			dropAssignmentText : "Remove"
		},
		"Gnt.plugin.TaskEditor" : {
			title : "Task Information",
			alertCaption : "Information",
			alertText : "Please correct marked errors to save changes",
			okText : "Ok",
			cancelText : "Cancel"
		},
		"Gnt.field.EndDate" : {
			endBeforeStartText : "End date is before start date"
		},
		"Gnt.column.Note" : {
			text : "Note"
		},
		"Gnt.column.AddNew" : {
			text : "Add new column..."
		},
		"Gnt.column.EarlyStartDate" : {
			text : "Early Start"
		},
		"Gnt.column.EarlyEndDate" : {
			text : "Early Finish"
		},
		"Gnt.column.LateStartDate" : {
			text : "Late Start"
		},
		"Gnt.column.LateEndDate" : {
			text : "Late Finish"
		},
		"Gnt.field.Calendar" : {
			calendarNotApplicable : "Task calendar has no overlapping with assigned resources calendars"
		},
		"Gnt.column.Slack" : {
			text : "Slack"
		},
		"Gnt.column.Name" : {
			text : "Task Name"
		},
		"Gnt.column.BaselineStartDate" : {
			text : "Baseline Start Date"
		},
		"Gnt.column.BaselineEndDate" : {
			text : "Baseline End Date"
		},
		"Gnt.column.Milestone" : {
			text : "Milestone"
		},
		"Gnt.field.Milestone" : {
			yes : "Yes",
			no : "No"
		},
		"Gnt.field.Dependency" : {
			invalidFormatText : "Invalid dependency format",
			invalidDependencyText : "Invalid dependency found, please make sure you have no cyclic paths between your tasks",
			invalidDependencyType : "Invalid dependency type {0}. Allowed values are: {1}."
		}
	}
});
Ext.define("Gnt.mixin.Localizable", {
	extend : "Sch.mixin.Localizable",
	requires : ["Gnt.locale.En"]
});
Ext.define("Gnt.model.CalendarDay", {
	requires : ["Ext.data.Types"],
	extend : "Sch.model.Customizable",
	idProperty : "Id",
	customizableFields : [{
			name : "Date",
			type : "date",
			dateFormat : "c",
			convert : function (b, a) {
				if (!b) {
					return
				}
				var c = Ext.data.Types.DATE.convert.call(this, b);
				if (c) {
					Ext.Date.clearTime(c)
				}
				return c
			}
		}, {
			name : "Weekday",
			type : "int"
		}, {
			name : "OverrideStartDate",
			type : "date",
			dateFormat : "c"
		}, {
			name : "OverrideEndDate",
			type : "date",
			dateFormat : "c"
		}, {
			name : "Type",
			defaultValue : "DAY"
		}, {
			name : "Id"
		}, {
			name : "IsWorkingDay",
			type : "boolean",
			defaultValue : false
		}, {
			name : "Cls",
			defaultValue : "gnt-holiday"
		}, "Name", {
			name : "Availability",
			convert : function (b, a) {
				if (b) {
					return Ext.typeOf(b) === "string" ? [b] : b
				} else {
					return []
				}
			}
		}
	],
	availabilityCache : null,
	weekDayField : "Weekday",
	overrideStartDateField : "OverrideStartDate",
	overrideEndDateField : "OverrideEndDate",
	typeField : "Type",
	dateField : "Date",
	isWorkingDayField : "IsWorkingDay",
	clsField : "Cls",
	nameField : "Name",
	availabilityField : "Availability",
	setDate : function (a) {
		if (a) {
			a = Ext.Date.clearTime(a, true)
		}
		this.set(this.dateField, a)
	},
	clearDate : function () {
		this.set(this.dateField, null)
	},
	getAvailability : function (b) {
		var c = this;
		if (b) {
			return this.get(this.availabilityField)
		}
		if (this.availabilityCache) {
			return this.availabilityCache
		}
		var a = [];
		Ext.Array.each(this.get(this.availabilityField), function (d) {
			a.push(Ext.typeOf(d) === "string" ? c.parseInterval(d) : d)
		});
		this.verifyAvailability(a);
		return this.availabilityCache = a
	},
	setAvailability : function (a) {
		this.availabilityCache = null;
		this.set(this.availabilityField, this.stringifyIntervals(a));
		this.getAvailability()
	},
	verifyAvailability : function (b) {
		b.sort(function (f, e) {
			return f.startTime - e.startTime
		});
		Ext.Array.each(b, function (e) {
			if (e.startTime > e.endTime) {
				throw new Error("Start time " + Ext.Date.format(e.startTime, "H:i") + " is greater than end time " + Ext.Date.format(e.endTime, "H:i"))
			}
		});
		for (var a = 1; a < b.length; a++) {
			var c = b[a - 1];
			var d = b[a];
			if (c.endTime > d.startTime) {
				throw new Error("Availability intervals should not intersect: [" + this.stringifyInterval(c) + "] and [" + this.stringifyInterval(d) + "]")
			}
		}
	},
	prependZero : function (a) {
		return a < 10 ? "0" + a : a
	},
	stringifyInterval : function (b) {
		var c = b.startTime;
		var a = b.endTime;
		return this.prependZero(c.getHours()) + ":" + this.prependZero(c.getMinutes()) + "-" + (a.getDate() == 1 ? 24 : this.prependZero(a.getHours())) + ":" + this.prependZero(a.getMinutes())
	},
	stringifyIntervals : function (b) {
		var c = this;
		var a = [];
		Ext.Array.each(b, function (d) {
			if (Ext.typeOf(d) === "string") {
				a.push(d)
			} else {
				a.push(c.stringifyInterval(d))
			}
		});
		return a
	},
	parseInterval : function (b) {
		var a = /(\d\d):(\d\d)-(\d\d):(\d\d)/.exec(b);
		if (!a) {
			throw "Invalid format for availability string: " + b + ". It should have exact format: hh:mm-hh:mm"
		}
		return {
			startTime : new Date(0, 0, 0, a[1], a[2]),
			endTime : new Date(0, 0, 0, a[3], a[4])
		}
	},
	getTotalHours : function () {
		return this.getTotalMS() / 1000 / 60 / 60
	},
	getTotalMS : function () {
		var a = 0;
		Ext.Array.each(this.getAvailability(), function (b) {
			a += b.endTime - b.startTime
		});
		return a
	},
	addAvailabilityInterval : function (d, b) {
		var a;
		if (d instanceof Date) {
			a = {
				startTime : d,
				endTime : b
			}
		} else {
			a = this.parseInterval(d + (b ? "-" + b : ""))
		}
		var c = this.getAvailability().concat(a);
		this.verifyAvailability(c);
		this.setAvailability(c)
	},
	removeAvailbilityInterval : function (a) {
		var b = this.getAvailability();
		b.splice(a, 1);
		this.setAvailability(b)
	},
	getAvailabilityIntervalsFor : function (d) {
		d = typeof d == "number" ? new Date(d) : d;
		var c = d.getFullYear();
		var e = d.getMonth();
		var b = d.getDate();
		var a = [];
		Ext.Array.each(this.getAvailability(), function (f) {
			var g = f.endTime.getDate();
			a.push({
				startDate : new Date(c, e, b, f.startTime.getHours(), f.startTime.getMinutes()),
				endDate : new Date(c, e, b + (g == 1 ? 1 : 0), f.endTime.getHours(), f.endTime.getMinutes())
			})
		});
		return a
	},
	getAvailabilityStartFor : function (b) {
		var a = this.getAvailabilityIntervalsFor(b);
		if (!a.length) {
			return null
		}
		return a[0].startDate
	},
	getAvailabilityEndFor : function (b) {
		var a = this.getAvailabilityIntervalsFor(b);
		if (!a.length) {
			return null
		}
		return a[a.length - 1].endDate
	}
});
Ext.define("Gnt.model.Assignment", {
	extend : "Sch.model.Customizable",
	idProperty : "Id",
	customizableFields : [{
			name : "Id"
		}, {
			name : "ResourceId"
		}, {
			name : "TaskId"
		}, {
			name : "Units",
			type : "float",
			defaultValue : 100
		}
	],
	resourceIdField : "ResourceId",
	taskIdField : "TaskId",
	unitsField : "Units",
	isPersistable : function () {
		var a = this.getTask(),
		b = this.getResource();
		return a && !a.phantom && b && !b.phantom
	},
	getUnits : function () {
		return Math.max(0, this.get(this.unitsField))
	},
	setUnits : function (a) {
		if (a < 0) {
			throw "`Units` value for an assignment can't be less than 0"
		}
		this.set(this.unitsField, a)
	},
	getResourceName : function () {
		var a = this.getResource();
		if (a) {
			return a.getName()
		}
		return ""
	},
	getTask : function (a) {
		a = a || this.stores[0].getTaskStore();
		return a && a.getByInternalId(this.getTaskId())
	},
	getResource : function () {
		return this.stores[0] && this.stores[0].getResourceStore().getByInternalId(this.getResourceId())
	},
	getInternalId : function () {
		return this.getId() || this.internalId
	},
	getEffort : function (b) {
		var a = this.getTask();
		var c = 0;
		a.forEachAvailabilityIntervalWithResources({
			startDate : a.getStartDate(),
			endDate : a.getEndDate(),
			resources : [this.getResource()]
		}, function (g, f, e) {
			var h;
			for (var d in e) {
				h = e[d].units
			}
			c += (f - g) * h / 100
		});
		return a.getProjectCalendar().convertMSDurationToUnit(c, b || a.getEffortUnit())
	}
});
Ext.define("Gnt.model.Dependency", {
	extend : "Sch.model.Customizable",
	inheritableStatics : {
		Type : {
			StartToStart : 0,
			StartToEnd : 1,
			EndToStart : 2,
			EndToEnd : 3
		}
	},
	idProperty : "Id",
	customizableFields : [{
			name : "Id"
		}, {
			name : "From"
		}, {
			name : "To"
		}, {
			name : "Type",
			type : "int",
			defaultValue : 2
		}, {
			name : "Lag",
			type : "number",
			defaultValue : 0
		}, {
			name : "LagUnit",
			type : "string",
			defaultValue : "d",
			convert : function (a) {
				return a || "d"
			}
		}, {
			name : "Cls"
		}
	],
	fromField : "From",
	toField : "To",
	typeField : "Type",
	lagField : "Lag",
	lagUnitField : "LagUnit",
	clsField : "Cls",
	isHighlighted : false,
	constructor : function (a) {
		this.callParent(arguments);
		if (a) {
			if (a[this.fromField] && a[this.fromField]instanceof Gnt.model.Task) {
				this.setSourceTask(a[this.fromField]);
				delete a.fromField
			}
			if (a[this.toField] && a[this.toField]instanceof Gnt.model.Task) {
				this.setTargetTask(a[this.toField]);
				delete a.toField
			}
		}
	},
	getTaskStore : function () {
		return this.stores[0].taskStore
	},
	getSourceTask : function (a) {
		return (a || this.getTaskStore()).getById(this.getSourceId())
	},
	setSourceTask : function (a) {
		this.setSourceId(a.getId() || a.internalId)
	},
	getTargetTask : function (a) {
		return (a || this.getTaskStore()).getById(this.getTargetId())
	},
	setTargetTask : function (a) {
		this.setTargetId(a.getId() || a.internalId)
	},
	getSourceId : function () {
		return this.get(this.fromField)
	},
	setSourceId : function (a) {
		this.set(this.fromField, a)
	},
	getTargetId : function () {
		return this.get(this.toField)
	},
	setTargetId : function (a) {
		this.set(this.toField, a)
	},
	getLagUnit : function () {
		return this.get(this.lagUnitField) || "d"
	},
	isPersistable : function () {
		var a = this.getSourceTask(),
		b = this.getTargetTask();
		return a && !a.phantom && b && !b.phantom
	},
	isValid : function (b) {
		var d = this.callParent(arguments),
		e = this.getSourceId(),
		a = this.getTargetId(),
		c = this.getType();
		if (d) {
			d = Ext.isNumber(c) && !Ext.isEmpty(e) && !Ext.isEmpty(a) && e != a
		}
		if (d && b !== false && this.stores[0]) {
			d = this.stores[0].isValidDependency(e, a, c, null, null, this)
		}
		return d
	},
	getInternalId : function () {
		return this.getId() || this.internalId
	}
});
Ext.define("Gnt.model.Resource", {
	extend : "Sch.model.Resource",
	customizableFields : ["CalendarId"],
	calendarIdField : "CalendarId",
	normalized : false,
	calendarWaitingListener : null,
	getTaskStore : function () {
		return this.stores[0].getTaskStore()
	},
	getEventStore : function () {
		return this.getTaskStore()
	},
	getEvents : function () {
		return this.getTasks()
	},
	getTasks : function () {
		var a = [];
		this.forEachAssignment(function (b) {
			var c = b.getTask();
			if (c) {
				a.push(c)
			}
		});
		return a
	},
	getCalendar : function (a) {
		return a ? this.getOwnCalendar() : this.getOwnCalendar() || this.getProjectCalendar()
	},
	getOwnCalendar : function () {
		var a = this.getCalendarId();
		return a ? Gnt.data.Calendar.getCalendar(a) : null
	},
	getProjectCalendar : function () {
		return this.stores[0].getTaskStore().getCalendar()
	},
	setCalendar : function (b) {
		var a = b instanceof Gnt.data.Calendar;
		if (a && !b.calendarId) {
			throw new Error("Can't set calendar w/o `calendarId` property")
		}
		this.setCalendarId(a ? b.calendarId : b)
	},
	setCalendarId : function (c, d) {
		if (c instanceof Gnt.data.Calendar) {
			c = c.calendarId
		}
		var b = this.getCalendarId();
		if (b != c || d) {
			if (this.calendarWaitingListener) {
				this.calendarWaitingListener.destroy();
				this.calendarWaitingListener = null
			}
			var a = {
				calendarchange : this.adjustToCalendar,
				scope : this
			};
			var f = this.calendar || Gnt.data.Calendar.getCalendar(b);
			this.calendar = null;
			f && f.un(a);
			this.set(this.calendarIdField, c);
			var e = Gnt.data.Calendar.getCalendar(c);
			if (e) {
				e.on(a);
				if (!d) {
					this.adjustToCalendar()
				}
			} else {
				this.calendarWaitingListener = Ext.data.StoreManager.on("add", function (g, i, h) {
						e = Gnt.data.Calendar.getCalendar(c);
						if (e) {
							this.calendarWaitingListener.destroy();
							this.calendarWaitingListener = null;
							e.on(a);
							this.adjustToCalendar()
						}
					}, this, {
						destroyable : true
					})
			}
		}
	},
	adjustToCalendar : function () {
		this.forEachTask(function (a) {
			a.adjustToCalendar()
		})
	},
	getInternalId : function () {
		return this.getId() || this.internalId
	},
	assignTo : function (a, c) {
		var b = a instanceof Gnt.model.Task ? a : this.getTaskStore().getById(a);
		return b.assign(this, c)
	},
	unassignFrom : function () {
		return this.unAssignFrom.apply(this, arguments)
	},
	unAssignFrom : function (a) {
		var b = a instanceof Gnt.model.Task ? a : this.getTaskStore().getById(a);
		b.unAssign(this)
	},
	forEachAssignment : function (b, a) {
		a = a || this;
		var c = this.getInternalId();
		this.getTaskStore().getAssignmentStore().each(function (d) {
			if (d.getResourceId() == c) {
				return b.call(a, d)
			}
		})
	},
	forEachTask : function (b, a) {
		a = a || this;
		var c = this.getInternalId();
		this.getTaskStore().getAssignmentStore().each(function (e) {
			if (e.getResourceId() == c) {
				var d = e.getTask();
				if (d) {
					return b.call(a, d)
				}
			}
		})
	},
	collectAvailabilityIntervalPoints : function (h, i, b, m, e) {
		var d = Ext.isFunction(i) ? function (k) {
			m[k].push(i(k))
		}
		 : function (k) {
			m[k].push(i)
		};
		var g = Ext.isFunction(b) ? function (k) {
			m[k].push(b(k))
		}
		 : function (k) {
			m[k].push(b)
		};
		for (var f = 0, c = h.length; f < c; f++) {
			var a = h[f];
			var j = a.startDate - 0;
			var n = a.endDate - 0;
			if (!m[j]) {
				m[j] = [];
				e.push(j)
			}
			d(j);
			if (!m[n]) {
				m[n] = [];
				e.push(n)
			}
			g(n)
		}
	},
	forEachAvailabilityIntervalWithTasks : function (f, h, b) {
		b = b || this;
		var e = f.startDate;
		var B = f.endDate;
		if (!e || !B) {
			throw "Both `startDate` and `endDate` are required for `forEachAvailabilityIntervalWithTasks`"
		}
		var m = new Date(e);
		var D = f.includeAllIntervals;
		var C = f.includeResCalIntervals;
		var t = this.getCalendar();
		var q = [];
		var r = [];
		var c = [];
		var H = [e - 0, B - 0];
		var o = {};
		o[e - 0] = [{
				type : "00-intervalStart"
			}
		];
		o[B - 0] = [{
				type : "00-intervalEnd"
			}
		];
		this.forEachAssignment(function (I) {
			var j = I.getTask();
			if (!j) {
				return
			}
			var i = j.getStartDate();
			var l = j.getEndDate();
			var k = j.getInternalId();
			if (i > B || l < e) {
				return
			}
			r.push(j);
			c.push(j.getCalendar());
			this.collectAvailabilityIntervalPoints([{
						startDate : i,
						endDate : l
					}
				], {
				type : "05-taskStart",
				assignment : I,
				taskId : k,
				units : I.getUnits()
			}, {
				type : "04-taskEnd",
				taskId : k
			}, o, H);
			q.push(I)
		});
		if (!r.length && !D && !C) {
			return
		}
		var g = Sch.util.Date;
		var A,
		u,
		d;
		while (m < B) {
			this.collectAvailabilityIntervalPoints(t.getAvailabilityIntervalsFor(m), {
				type : "00-resourceAvailabilityStart"
			}, {
				type : "01-resourceAvailabilityEnd"
			}, o, H);
			for (A = 0, u = c.length; A < u; A++) {
				d = r[A].getInternalId();
				this.collectAvailabilityIntervalPoints(c[A].getAvailabilityIntervalsFor(m), {
					type : "02-taskAvailabilityStart",
					taskId : d
				}, {
					type : "03-taskAvailabilityEnd",
					taskId : d
				}, o, H)
			}
			m = g.getStartOfNextDay(m)
		}
		H.sort(function (j, i) {
			return j - i
		});
		var x = false,
		F = false,
		E = {},
		a = 0,
		z = 0;
		for (A = 0, u = H.length - 1; A < u; A++) {
			var y = o[H[A]];
			y.sort(function (j, i) {
				return j.type < i.type ? 1 : -1
			});
			for (var v = 0, w = y.length; v < w; v++) {
				var s = y[v];
				switch (s.type) {
				case "00-resourceAvailabilityStart":
					F = true;
					break;
				case "01-resourceAvailabilityEnd":
					F = false;
					break;
				case "02-taskAvailabilityStart":
					a++;
					break;
				case "03-taskAvailabilityEnd":
					a--;
					break;
				case "05-taskStart":
					E[s.taskId] = s;
					z++;
					break;
				case "04-taskEnd":
					delete E[s.taskId];
					z--;
					break;
				case "00-intervalStart":
					x = true;
					break;
				case "00-intervalEnd":
					return
				}
			}
			if (x && (D || C && F || F && a && z)) {
				var p = {
					inResourceCalendar : !!F,
					inTasksCalendar : !!a,
					inTask : z
				};
				var G = H[A];
				var n = H[A + 1];
				if (G > B || n < e) {
					continue
				}
				if (G < e) {
					G = e - 0
				}
				if (n > B) {
					n = B - 0
				}
				if (h.call(b, G, n, E, p) === false) {
					return false
				}
			}
		}
	},
	getAllocationInfo : function (a) {
		var b = [];
		this.forEachAvailabilityIntervalWithTasks(a, function (j, h, g, k) {
			var f = 0,
			d = [],
			c = {};
			if (k.inResourceCalendar && k.inTasksCalendar && k.inTask) {
				for (var e in g) {
					f += g[e].units;
					d.push(g[e].assignment);
					c[e] = g[e].assignment
				}
			}
			b.push(Ext.apply({
					startDate : new Date(j),
					endDate : new Date(h),
					totalAllocation : f,
					assignments : d,
					assignmentsHash : c
				}, k))
		});
		return b
	}
});
Ext.define("Gnt.model.task.More", {
	indent : function () {
		var c = this.previousSibling;
		if (c) {
			var a = {
				parentNode : this.parentNode,
				previousSibling : this.previousSibling,
				nextSibling : this.nextSibling
			};
			var b = this.getTaskStore();
			b.suspendEvents(true);
			c.appendChild(this);
			this.removeContext = a;
			c.removeInvalidDependencies();
			b.resumeEvents();
			c.set("leaf", false);
			c.expand()
		}
	},
	outdent : function () {
		var b = this.parentNode;
		if (b && !b.isRoot()) {
			var a = {
				parentNode : this.parentNode,
				previousSibling : this.previousSibling,
				nextSibling : this.nextSibling
			};
			if (this.convertEmptyParentToLeaf) {
				b.set("leaf", b.childNodes.length === 1)
			}
			var c = this.getTaskStore();
			c.suspendEvents(true);
			if (b.nextSibling) {
				b.parentNode.insertBefore(this, b.nextSibling)
			} else {
				b.parentNode.appendChild(this)
			}
			this.removeContext = a;
			c.resumeEvents();
			if (c.recalculateParents && b.childNodes.length) {
				b.childNodes[0].recalculateParents()
			}
		}
	},
	removeInvalidDependencies : function () {
		var a = this.getDependencyStore(),
		c = this.getAllDependencies();
		for (var b = 0; b < c.length; b++) {
			if (!c[b].isValid(true)) {
				a.remove(c[b])
			}
		}
	},
	getAllDependencies : function () {
		return this.predecessors.concat(this.successors)
	},
	hasIncomingDependencies : function () {
		return this.predecessors.length > 0
	},
	hasOutgoingDependencies : function () {
		return this.successors.length > 0
	},
	getIncomingDependencies : function (a) {
		return a ? this.predecessors : this.predecessors.slice()
	},
	getOutgoingDependencies : function (a) {
		return a ? this.successors : this.successors.slice()
	},
	constrain : function (c) {
		if (this.isManuallyScheduled()) {
			return false
		}
		var e = false;
		c = c || this.getTaskStore();
		var b = this.getConstrainContext(c);
		if (b) {
			var a = b.startDate;
			var d = b.endDate;
			if (a && a - this.getStartDate() !== 0) {
				this.setStartDate(a, true, c.skipWeekendsDuringDragDrop);
				e = true
			} else {
				if (d && d - this.getEndDate() !== 0) {
					this.setEndDate(d, true, c.skipWeekendsDuringDragDrop);
					e = true
				}
			}
		}
		return e
	},
	getConstrainContext : function (f) {
		var g = this.getIncomingDependencies(true);
		if (!g.length) {
			return null
		}
		var a = Gnt.model.Dependency.Type,
		c = new Date(0),
		b = new Date(0),
		i = this.getProjectCalendar(),
		h = this.getCalendar(),
		d;
		var e = (f || this.getTaskStore()).dependenciesCalendar;
		Ext.each(g, function (l) {
			var k = l.getSourceTask();
			if (k) {
				var n;
				if (e == "project") {
					n = i
				} else {
					if (e == "source") {
						n = k.getCalendar()
					} else {
						if (e == "target") {
							n = h
						} else {
							throw "Unsupported value for `dependenciesCalendar` config option"
						}
					}
				}
				var p = l.getLag() || 0,
				m = l.getLagUnit(),
				o = k.getStartDate(),
				j = k.getEndDate();
				switch (l.getType()) {
				case a.StartToEnd:
					o = n.skipWorkingTime(o, p, m);
					if (b < o) {
						b = o;
						d = k
					}
					break;
				case a.StartToStart:
					o = n.skipWorkingTime(o, p, m);
					if (c < o) {
						c = o;
						d = k
					}
					break;
				case a.EndToStart:
					j = n.skipWorkingTime(j, p, m);
					if (c < j) {
						c = j;
						d = k
					}
					break;
				case a.EndToEnd:
					j = n.skipWorkingTime(j, p, m);
					if (b < j) {
						b = j;
						d = k
					}
					break;
				default:
					throw "Invalid dependency type: " + l.getType()
				}
			}
		});
		return {
			startDate : c > 0 ? c : null,
			endDate : b > 0 ? b : null,
			constrainingTask : d
		}
	},
	getCriticalPaths : function () {
		var b = [this],
		a = this.getConstrainContext();
		while (a) {
			b.push(a.constrainingTask);
			a = a.constrainingTask.getConstrainContext()
		}
		return b
	},
	cascadeChanges : function (b, c, d) {
		c = c || {
			nbrAffected : 0,
			affected : {}

		};
		b = b || this.getTaskStore();
		var a = b.currentCascadeBatch;
		if (a) {
			if (a.visitedCounters[this.internalId] > this.predecessors.length) {
				return
			}
			a.addVisited(this)
		}
		if (this.isLeaf() || b.enableDependenciesForParentTasks) {
			var e = this.constrain(b);
			if (e) {
				c.nbrAffected++;
				c.affected[this.internalId] = this;
				if (a) {
					a.addAffected(this)
				}
				Ext.each(this.getOutgoingDependencies(true), function (f) {
					var g = f.getTargetTask();
					if (g && !g.isManuallyScheduled()) {
						g.cascadeChanges(b, c, f)
					}
				})
			}
		}
		return c
	},
	addSubtask : function (a) {
		this.set("leaf", false);
		this.appendChild(a);
		this.expand();
		return a
	},
	addSuccessor : function (b) {
		var d = this.getTaskStore(),
		c = this.getDependencyStore();
		b = b || new this.self();
		b.calendar = b.calendar || this.getCalendar();
		b.taskStore = d;
		if (this.getEndDate()) {
			b.setStartDate(this.getEndDate(), true, d.skipWeekendsDuringDragDrop);
			b.setDuration(1, Sch.util.Date.DAY)
		}
		this.addTaskBelow(b);
		var a = new c.model();
		a.setSourceTask(this);
		a.setTargetTask(b);
		a.setType(c.model.Type.EndToStart);
		c.add(a);
		return b
	},
	addMilestone : function (c) {
		var b = this.getTaskStore();
		c = c || new this.self();
		var a = this.getEndDate();
		if (a) {
			c.calendar = c.calendar || this.getCalendar();
			c.setStartEndDate(a, a, b.skipWeekendsDuringDragDrop)
		}
		return this.addTaskBelow(c)
	},
	addPredecessor : function (c) {
		var b = this.getDependencyStore();
		c = c || new this.self();
		c.calendar = c.calendar || this.getCalendar();
		c.beginEdit();
		if (this.getStartDate()) {
			c.set(this.startDateField, c.calculateStartDate(this.getStartDate(), 1, Sch.util.Date.DAY));
			c.set(this.endDateField, this.getStartDate());
			c.set(this.durationField, 1);
			c.set(this.durationUnitField, Sch.util.Date.DAY)
		}
		c.endEdit();
		this.addTaskAbove(c);
		var a = new b.model();
		a.setSourceTask(c);
		a.setTargetTask(this);
		a.setType(b.model.Type.EndToStart);
		b.add(a);
		return c
	},
	getSuccessors : function () {
		var e = this.successors,
		d = [];
		for (var c = 0, a = e.length; c < a; c++) {
			var b = e[c].getTargetTask();
			if (b) {
				d.push(b)
			}
		}
		return d
	},
	getPredecessors : function () {
		var e = this.predecessors,
		d = [];
		for (var c = 0, a = e.length; c < a; c++) {
			var b = e[c].getSourceTask();
			if (b) {
				d.push(b)
			}
		}
		return d
	},
	addTaskAbove : function (a) {
		a = a || new this.self();
		return this.parentNode.insertBefore(a, this)
	},
	addTaskBelow : function (a) {
		a = a || new this.self();
		if (this.nextSibling) {
			return this.parentNode.insertBefore(a, this.nextSibling)
		} else {
			return this.parentNode.appendChild(a)
		}
	},
	isAbove : function (a) {
		var b = this,
		d = Math.min(b.data.depth, a.data.depth);
		var c = this;
		while (c.data.depth > d) {
			c = c.parentNode;
			if (c == a) {
				return false
			}
		}
		while (a.data.depth > d) {
			a = a.parentNode;
			if (a == b) {
				return true
			}
		}
		while (a.parentNode !== c.parentNode) {
			a = a.parentNode;
			c = c.parentNode
		}
		return a.data.index > c.data.index
	},
	cascadeChildren : function (d, c) {
		var e = this;
		if (e.isLeaf()) {
			return
		}
		var f = this.childNodes;
		for (var b = 0, a = f.length; b < a; b++) {
			f[b].cascadeBy(d, c)
		}
	},
	getViolatedConstraints : function () {
		if (!this.get("leaf") || this.isManuallyScheduled()) {
			return false
		}
		var a = this.getEarlyStartDate();
		if (this.getStartDate() < a) {
			return [{
					task : this,
					startDate : a
				}
			]
		}
		return null
	},
	resolveViolatedConstraints : function (e) {
		e = e || this.getViolatedConstraints();
		if (!e) {
			return
		}
		if (!Ext.isArray(e)) {
			e = [e]
		}
		var b = this.getTaskStore();
		for (var c, d = 0, a = e.length; d < a; d++) {
			c = e[d];
			if (c.startDate) {
				c.task.setStartDate(c.startDate, true, b.skipWeekendsDuringDragDrop)
			} else {
				if (c.endDate) {
					c.task.setEndDate(c.endDate, true, b.skipWeekendsDuringDragDrop)
				}
			}
		}
	},
	getSlack : function (b) {
		b = b || Sch.util.Date.DAY;
		var c = this.getEarlyStartDate(),
		a = this.getLateStartDate();
		if (!c || !a) {
			return null
		}
		return this.getCalendar().calculateDuration(this.getEarlyStartDate(), this.getLateStartDate(), b)
	},
	getEarlyStartDate : function () {
		var k = this.getTaskStore();
		if (!k) {
			return this.getEndDate()
		}
		var h = this.internalId;
		if (k.earlyStartDates[h]) {
			return k.earlyStartDates[h]
		}
		var b,
		n = 0,
		f,
		e;
		if (this.childNodes.length) {
			for (f = 0, e = this.childNodes.length; f < e; f++) {
				b = this.childNodes[f].getEarlyStartDate();
				if (b < n || !n) {
					n = b
				}
			}
			k.earlyStartDates[h] = n;
			return n
		}
		if (this.isManuallyScheduled()) {
			n = k.earlyStartDates[h] = this.getStartDate();
			return n
		}
		var m = this.getIncomingDependencies(true),
		j;
		if (!m.length) {
			n = k.earlyStartDates[h] = this.getStartDate();
			return n
		}
		var g = Gnt.model.Dependency.Type,
		a = this.getCalendar(),
		d = this.getProjectCalendar(),
		c;
		for (f = 0, e = m.length; f < e; f++) {
			j = m[f].getSourceTask();
			if (j) {
				switch (m[f].getType()) {
				case g.StartToStart:
					b = j.getEarlyStartDate();
					break;
				case g.StartToEnd:
					b = j.getEarlyStartDate();
					b = a.calculateStartDate(b, this.getDuration(), this.getDurationUnit());
					break;
				case g.EndToStart:
					b = j.getEarlyEndDate();
					break;
				case g.EndToEnd:
					b = j.getEarlyEndDate();
					b = a.calculateStartDate(b, this.getDuration(), this.getDurationUnit());
					break
				}
				c = m[f].getLag();
				if (c) {
					b = d.skipWorkingTime(b, c, m[f].getLagUnit())
				}
				b = d.skipNonWorkingTime(b, true)
			}
			if (b > n) {
				n = b
			}
		}
		k.earlyStartDates[h] = n;
		return k.earlyStartDates[h]
	},
	getEarlyEndDate : function () {
		var d = this.getTaskStore();
		if (!d) {
			return this.getEndDate()
		}
		var c = this.internalId;
		if (d.earlyEndDates[c]) {
			return d.earlyEndDates[c]
		}
		var a = 0;
		if (this.childNodes.length) {
			var f,
			e,
			b;
			for (e = 0, b = this.childNodes.length; e < b; e++) {
				f = this.childNodes[e].getEarlyEndDate();
				if (f > a) {
					a = f
				}
			}
			d.earlyEndDates[c] = a;
			return a
		}
		if (this.isManuallyScheduled()) {
			a = d.earlyEndDates[c] = this.getEndDate();
			return a
		}
		var g = this.getEarlyStartDate();
		if (!g) {
			return null
		}
		a = d.earlyEndDates[c] = this.getCalendar().calculateEndDate(g, this.getDuration(), this.getDurationUnit());
		return a
	},
	getLateEndDate : function () {
		var k = this.getTaskStore();
		if (!k) {
			return this.getEndDate()
		}
		var j = this.internalId;
		if (k.lateEndDates[j]) {
			return k.lateEndDates[j]
		}
		var b,
		n = 0,
		g,
		e;
		if (this.childNodes.length) {
			for (g = 0, e = this.childNodes.length; g < e; g++) {
				b = this.childNodes[g].getLateEndDate();
				if (b > n) {
					n = b
				}
			}
			k.lateEndDates[j] = n;
			return n
		}
		if (this.isManuallyScheduled()) {
			n = k.lateEndDates[j] = this.getEndDate();
			return n
		}
		var m = this.getOutgoingDependencies(true);
		if (!m.length) {
			n = k.lateEndDates[j] = k.getProjectEndDate();
			return n
		}
		var h = Gnt.model.Dependency.Type,
		a = this.getCalendar(),
		d = this.getProjectCalendar(),
		f,
		c;
		for (g = 0, e = m.length; g < e; g++) {
			f = m[g].getTargetTask();
			if (f) {
				switch (m[g].getType()) {
				case h.StartToStart:
					b = f.getLateStartDate();
					b = a.calculateEndDate(b, this.getDuration(), this.getDurationUnit());
					break;
				case h.StartToEnd:
					b = f.getLateEndDate();
					b = a.calculateEndDate(b, this.getDuration(), this.getDurationUnit());
					break;
				case h.EndToStart:
					b = f.getLateStartDate();
					break;
				case h.EndToEnd:
					b = f.getLateEndDate();
					break
				}
				c = m[g].getLag();
				if (c) {
					b = d.skipWorkingTime(b, -c, m[g].getLagUnit())
				}
				b = d.skipNonWorkingTime(b, false);
				if (b < n || !n) {
					n = b
				}
			}
		}
		k.lateEndDates[j] = n || k.getProjectEndDate();
		return k.lateEndDates[j]
	},
	getLateStartDate : function () {
		var d = this.getTaskStore();
		if (!d) {
			return this.getStartDate()
		}
		var c = this.internalId;
		if (d.lateStartDates[c]) {
			return d.lateStartDates[c]
		}
		var a;
		if (this.childNodes.length) {
			var f,
			e,
			b;
			for (e = 0, b = this.childNodes.length; e < b; e++) {
				f = this.childNodes[e].getLateStartDate();
				if (f < a || !a) {
					a = f
				}
			}
			d.lateStartDates[c] = a;
			return a
		}
		if (this.isManuallyScheduled()) {
			a = d.lateStartDates[c] = this.getStartDate();
			return a
		}
		var g = this.getLateEndDate();
		if (!g) {
			return null
		}
		a = d.lateStartDates[c] = this.getCalendar().calculateStartDate(g, this.getDuration(), this.getDurationUnit());
		return a
	},
	resetEarlyDates : function () {
		var b = this.getTaskStore();
		if (!b) {
			return
		}
		var a = this.internalId;
		b.earlyStartDates[a] = null;
		b.earlyEndDates[a] = null
	},
	resetLateDates : function () {
		var b = this.getTaskStore();
		if (!b) {
			return
		}
		var a = this.internalId;
		b.lateStartDates[a] = null;
		b.lateEndDates[a] = null
	},
	getTopParent : function (c) {
		var b = this.getTaskStore().getRootNode(),
		e = this,
		d = [this],
		a;
		while (e) {
			if (e === b) {
				return c ? d : a
			}
			d.push(e);
			a = e;
			e = e.parentNode
		}
	}
});
Ext.define("Gnt.model.Task", {
	extend : "Sch.model.Range",
	requires : ["Sch.util.Date", "Ext.data.NodeInterface"],
	mixins : ["Gnt.model.task.More"],
	idProperty : "Id",
	customizableFields : [{
			name : "Id"
		}, {
			name : "Duration",
			type : "number",
			useNull : true
		}, {
			name : "Effort",
			type : "number",
			useNull : true
		}, {
			name : "EffortUnit",
			type : "string",
			defaultValue : "h"
		}, {
			name : "CalendarId",
			type : "string"
		}, {
			name : "Note",
			type : "string"
		}, {
			name : "DurationUnit",
			type : "string",
			defaultValue : "d",
			convert : function (a) {
				return a || "d"
			}
		}, {
			name : "PercentDone",
			type : "number",
			defaultValue : 0
		}, {
			name : "ManuallyScheduled",
			type : "boolean",
			defaultValue : false
		}, {
			name : "SchedulingMode",
			type : "string",
			defaultValue : "Normal"
		}, {
			name : "BaselineStartDate",
			type : "date",
			dateFormat : "c"
		}, {
			name : "BaselineEndDate",
			type : "date",
			dateFormat : "c"
		}, {
			name : "BaselinePercentDone",
			type : "int",
			defaultValue : 0
		}, {
			name : "Draggable",
			type : "boolean",
			persist : false,
			defaultValue : true
		}, {
			name : "Resizable",
			persist : false
		}, {
			name : "PhantomId",
			type : "string"
		}, {
			name : "PhantomParentId",
			type : "string"
		}, {
			name : "index",
			type : "int",
			persist : true
		}
	],
	draggableField : "Draggable",
	resizableField : "Resizable",
	nameField : "Name",
	durationField : "Duration",
	durationUnitField : "DurationUnit",
	effortField : "Effort",
	effortUnitField : "EffortUnit",
	percentDoneField : "PercentDone",
	manuallyScheduledField : "ManuallyScheduled",
	schedulingModeField : "SchedulingMode",
	calendarIdField : "CalendarId",
	baselineStartDateField : "BaselineStartDate",
	baselineEndDateField : "BaselineEndDate",
	baselinePercentDoneField : "BaselinePercentDone",
	noteField : "Note",
	calendar : null,
	dependencyStore : null,
	taskStore : null,
	phantomIdField : "PhantomId",
	phantomParentIdField : "PhantomParentId",
	normalized : false,
	recognizedSchedulingModes : ["Normal", "Manual", "FixedDuration", "EffortDriven", "DynamicAssignment"],
	convertEmptyParentToLeaf : true,
	autoCalculateEffortForParentTask : true,
	autoCalculatePercentDoneForParentTask : true,
	isHighlighted : false,
	calendarWaitingListener : null,
	childTasksDuration : null,
	completedChildTasksDuration : null,
	totalCount : null,
	predecessors : null,
	successors : null,
	assignments : null,
	removeChildIsCalledFromReplaceChild : false,
	savedDirty : null,
	constructor : function () {
		this.callParent(arguments);
		if (this.idgen.type != "default") {
			this.internalId = this.getId()
		}
		if (this.phantom) {
			this.data[this.phantomIdField] = this.internalId;
			this._phantomId = this.internalId
		}
		this.predecessors = [];
		this.successors = [];
		this.assignments = []
	},
	normalize : function () {
		var d = this.getDuration(),
		h = this.getDurationUnit(),
		c = this.getStartDate(),
		g = this.getEndDate(),
		f = this.getSchedulingMode(),
		e = this.data,
		j = this.getTaskStore(true);
		this.assignments = j && j.cachedAssignments && j.cachedAssignments[this.getId()] || [];
		var b = this.endDateField;
		var k = this.effortField;
		if (g && this.inclusiveEndDate) {
			var l = this.fields.getByKey(b).dateFormat;
			var a = (l && !Ext.Date.formatContainsHourInfo(l)) || (g.getHours() === 0 && g.getMinutes() === 0 && g.getSeconds() === 0 && g.getMilliseconds() === 0);
			if (a) {
				if (Ext.isNumber(d)) {
					g = e[b] = this.calculateEndDate(c, d, h)
				} else {
					g = e[b] = Ext.Date.add(g, Ext.Date.DAY, 1)
				}
			}
		}
		if (d == null && c && g) {
			d = e[this.durationField] = this.calculateDuration(c, g, h)
		}
		if ((f == "Normal" || this.isManuallyScheduled()) && g == null && c && Ext.isNumber(d)) {
			g = e[b] = this.calculateEndDate(c, d, h)
		}
		var n = this.get(k),
		i = this.getEffortUnit();
		if (f == "FixedDuration") {
			if (g == null && c && Ext.isNumber(d)) {
				g = e[b] = this.calculateEndDate(c, d, h)
			}
			if (n == null && c && g) {
				e[k] = this.calculateEffort(c, g, i)
			}
		} else {
			if (f == "EffortDriven") {
				if (n == null && c && g) {
					e[k] = this.calculateEffort(c, g, i)
				}
				if (g == null && c && n) {
					e[b] = this.calculateEffortDrivenEndDate(c, n, i);
					if (d == null) {
						e[this.durationField] = this.calculateDuration(c, e[b], h)
					}
				}
			} else {
				if (g == null && c && Ext.isNumber(d)) {
					g = e[b] = this.calculateEndDate(c, d, h)
				}
			}
		}
		var m = this.getCalendarId();
		if (m) {
			this.setCalendarId(m, true)
		}
		this.normalized = true
	},
	normalizeParent : function () {
		var l = this.childNodes;
		var a = 0;
		var d = 0;
		var h = 0;
		var j = this.autoCalculatePercentDoneForParentTask;
		var f = this.autoCalculateEffortForParentTask;
		for (var e = 0; e < l.length; e++) {
			var c = l[e];
			var b = c.isLeaf();
			if (!b) {
				c.normalizeParent()
			}
			if (f) {
				a += c.getEffort("MILLI")
			}
			if (j) {
				var k = b ? c.getDuration("MILLI") || 0 : c.childTasksDuration;
				d += k;
				h += b ? k * (c.getPercentDone() || 0) : c.completedChildTasksDuration
			}
		}
		if (j) {
			this.childTasksDuration = d;
			this.completedChildTasksDuration = h;
			var g = d ? h / d : 0;
			if (this.getPercentDone() != g) {
				this.data[this.percentDoneField] = g
			}
		}
		if (f) {
			if (this.getEffort("MILLI") != a) {
				this.data[this.effortField] = this.getProjectCalendar().convertMSDurationToUnit(a, this.getEffortUnit())
			}
		}
	},
	getInternalId : function () {
		return this.getId() || this.internalId
	},
	getCalendar : function (a) {
		return a ? this.getOwnCalendar() : this.getOwnCalendar() || this.parentNode && this.parentNode.getCalendar() || this.getProjectCalendar()
	},
	getOwnCalendar : function () {
		var a = this.get(this.calendarIdField);
		return a ? Gnt.data.Calendar.getCalendar(a) : this.calendar
	},
	getProjectCalendar : function () {
		var a = this.getTaskStore(true);
		var b = a && a.getCalendar() || this.parentNode && this.parentNode.getProjectCalendar() || this.isRoot() && this.calendar;
		if (!b) {
			Ext.Error.raise("Can't find a project calendar in `getProjectCalendar`")
		}
		return b
	},
	setCalendar : function (b) {
		var a = b instanceof Gnt.data.Calendar;
		if (a && !b.calendarId) {
			throw new Error("Can't set calendar w/o `calendarId` property")
		}
		this.setCalendarId(a ? b.calendarId : b)
	},
	setCalendarId : function (c, d) {
		if (c instanceof Gnt.data.Calendar) {
			c = c.calendarId
		}
		var b = this.getCalendarId();
		if (b != c || d) {
			if (this.calendarWaitingListener) {
				this.calendarWaitingListener.destroy();
				this.calendarWaitingListener = null
			}
			var a = {
				calendarchange : this.adjustToCalendar,
				scope : this
			};
			var f = this.calendar || Gnt.data.Calendar.getCalendar(b);
			this.calendar = null;
			f && f.un(a);
			this.set(this.calendarIdField, c);
			var e = Gnt.data.Calendar.getCalendar(c);
			if (e) {
				e.on(a);
				if (!d) {
					this.adjustToCalendar()
				}
			} else {
				this.calendarWaitingListener = Ext.data.StoreManager.on("add", function (g, i, h) {
						e = Gnt.data.Calendar.getCalendar(c);
						if (e) {
							this.calendarWaitingListener.destroy();
							this.calendarWaitingListener = null;
							e.on(a);
							this.adjustToCalendar()
						}
					}, this, {
						destroyable : true
					})
			}
		}
	},
	getDependencyStore : function () {
		var a = this.dependencyStore || this.getTaskStore().dependencyStore;
		if (!a) {
			Ext.Error.raise("Can't find a dependencyStore in `getDependencyStore`")
		}
		return a
	},
	getResourceStore : function () {
		return this.getTaskStore().getResourceStore()
	},
	getAssignmentStore : function () {
		return this.getTaskStore().getAssignmentStore()
	},
	getTaskStore : function (b) {
		if (this.taskStore) {
			return this.taskStore
		}
		var a = (this.stores[0] && this.stores[0].treeStore) || this.parentNode && this.parentNode.getTaskStore(b);
		if (!a && !b) {
			Ext.Error.raise("Can't find a taskStore in `getTaskStore`")
		}
		this.taskStore = a;
		return a
	},
	setTaskStore : function (a) {
		this.taskStore = a
	},
	isManuallyScheduled : function () {
		return this.get(this.schedulingModeField) === "Manual" || this.get(this.manuallyScheduledField)
	},
	setManuallyScheduled : function (a) {
		if (a) {
			this.set(this.schedulingModeField, "Manual")
		} else {
			if (this.get(this.schedulingModeField) == "Manual") {
				this.set(this.schedulingModeField, "Normal")
			}
		}
		return this.set(this.manuallyScheduledField, a)
	},
	setSchedulingMode : function (a) {
		if (!Ext.Array.contains(this.recognizedSchedulingModes, a)) {
			throw "Unrecognized scheduling mode: " + a
		}
		this.beginEdit();
		this.set(this.schedulingModeField, a);
		switch (a) {
		case "FixedDuration":
			this.updateEffortBasedOnDuration();
			break;
		case "EffortDriven":
			this.updateSpanBasedOnEffort();
			break
		}
		this.endEdit()
	},
	skipNonWorkingTime : function (b, c) {
		var a = false;
		this.forEachAvailabilityIntervalWithResources(c ? {
			startDate : b
		}
			 : {
			endDate : b,
			isForward : false
		}, function (f, e, d) {
			if (f !== e) {
				b = c ? f : e;
				a = true;
				return false
			}
		});
		return a ? new Date(b) : this.getCalendar().skipNonWorkingTime(b, c)
	},
	setStartDate : function (c, a, b, h) {
		h = h !== false;
		var d,
		g;
		this.beginEdit();
		var k = this.getStartDate();
		if (!c) {
			this.set(this.durationField, null);
			this.set(this.startDateField, null)
		} else {
			var f = this.getCalendar();
			if (b && !this.isManuallyScheduled()) {
				c = this.skipNonWorkingTime(c, !this.isMilestone())
			}
			var j = this.getTaskStore(true);
			var e = this.getSchedulingMode();
			this.set(this.startDateField, c);
			if (this.isLeaf() || !j || !j.moveParentAsGroup || !h || !this.childNodes.length || this.isManuallyScheduled()) {
				if (a !== false) {
					this.set(this.endDateField, this.recalculateEndDate(c))
				} else {
					g = this.getEndDate();
					if (g) {
						this.set(this.durationField, this.calculateDuration(c, g, this.getDurationUnit()))
					}
				}
			} else {
				if (c != k) {
					var l = {};
					j.startBatchCascade();
					j.suspendAutoRecalculateParents++;
					j.suspendAutoCascade++;
					var i = j.getParentsContext();
					this.cascadeChildren(function (n) {
						var m = n.isLeaf();
						if (m || !n.childNodes.length) {
							if (!l[n.internalId]) {
								var o = n.calculateDuration(k, n.getStartDate());
								j.currentCascadeBatch.addAffected(n);
								n.setStartDate(n.calculateEndDate(c, o), true, b, false);
								if (j.cascadeChanges) {
									var p = j.cascadeChangesForTask(n, true);
									Ext.apply(l, p.affected)
								}
							}
						}
						if (!m) {
							j.addTaskToParentsContext(i, n)
						}
					});
					if (j.recalculateParents || j.cascadeChanges) {
						j.addTaskToParentsContext(i, this);
						j.recalculateAffectedParents(l, i)
					}
					j.suspendAutoRecalculateParents--;
					j.suspendAutoCascade--;
					j.endBatchCascade()
				}
			}
		}
		d = this.getDuration();
		g = this.getEndDate();
		if (c && g && (d === undefined || d === null)) {
			this.set(this.durationField, this.calculateDuration(c, g, this.getDurationUnit()))
		}
		this.onPotentialEffortChange();
		this.endEdit()
	},
	setEndDate : function (e, c, d, j) {
		j = j !== false;
		var g,
		b;
		this.beginEdit();
		var i = this.getEndDate();
		if (!e) {
			this.set(this.durationField, null);
			this.set(this.endDateField, null)
		} else {
			var h = this.getCalendar();
			b = this.getStartDate();
			if (e < b && c === false) {
				e = b
			}
			if (d && !this.isManuallyScheduled()) {
				e = this.skipNonWorkingTime(e, false)
			}
			var l = this.getTaskStore(true);
			if (this.isLeaf() || !l || !l.moveParentAsGroup || !j || !this.childNodes.length) {
				if (c !== false) {
					g = this.getDuration();
					if (Ext.isNumber(g)) {
						this.set(this.startDateField, this.calculateStartDate(e, g, this.getDurationUnit()));
						this.set(this.endDateField, e)
					} else {
						this.set(this.endDateField, e)
					}
				} else {
					var f = this.isMilestone();
					if (e < b) {
						this.set(this.startDateField, e)
					}
					this.set(this.endDateField, e);
					if (b) {
						this.set(this.durationField, this.calculateDuration(b, e, this.getDurationUnit()));
						if (f && !this.isMilestone()) {
							var a = this.skipNonWorkingTime(b, true);
							if (a - b !== 0) {
								this.set(this.startDateField, a)
							}
						}
					}
				}
			} else {
				if (e != i) {
					var m = {};
					l.startBatchCascade();
					l.suspendAutoRecalculateParents++;
					l.suspendAutoCascade++;
					var k = l.getParentsContext();
					this.cascadeChildren(function (o) {
						var n = o.isLeaf();
						if (n || !o.childNodes.length) {
							if (!m[o.internalId]) {
								var p = o.calculateDuration(o.getEndDate(), i);
								l.currentCascadeBatch.addAffected(o);
								o.setEndDate(o.calculateStartDate(e, p), true, d, false);
								if (l.cascadeChanges) {
									var q = l.cascadeChangesForTask(o, true);
									Ext.apply(m, q.affected)
								}
							}
						}
						if (!n) {
							l.addTaskToParentsContext(k, o)
						}
					});
					if (l.recalculateParents || l.cascadeChanges) {
						l.addTaskToParentsContext(k, this);
						l.recalculateAffectedParents(m, k)
					}
					l.suspendAutoRecalculateParents--;
					l.suspendAutoCascade--;
					l.endBatchCascade()
				}
			}
		}
		g = this.getDuration();
		b = this.getStartDate();
		if (e && b && (g === undefined || g === null)) {
			this.set(this.durationField, this.calculateDuration(b, e, this.getDurationUnit()))
		}
		this.onPotentialEffortChange();
		this.endEdit()
	},
	setStartEndDate : function (a, b, c) {
		this.beginEdit();
		if (c && !this.isManuallyScheduled()) {
			a = a && this.skipNonWorkingTime(a, true);
			b = b && this.skipNonWorkingTime(b, false);
			if (b < a) {
				a = b
			}
		}
		this.set(this.startDateField, a);
		this.set(this.endDateField, b);
		this.set(this.durationField, this.calculateDuration(a, b, this.getDurationUnit()));
		this.onPotentialEffortChange();
		this.endEdit()
	},
	getDuration : function (a) {
		if (!a) {
			return this.get(this.durationField)
		}
		var b = this.getProjectCalendar(),
		c = b.convertDurationToMs(this.get(this.durationField), this.get(this.durationUnitField));
		return b.convertMSDurationToUnit(c, a)
	},
	getEffort : function (a) {
		var b = this.get(this.effortField) || 0;
		if (!a) {
			return b
		}
		var c = this.getProjectCalendar(),
		d = c.convertDurationToMs(b, this.getEffortUnit());
		return c.convertMSDurationToUnit(d, a)
	},
	setEffort : function (b, a) {
		a = a || this.get(this.effortUnitField);
		this.beginEdit();
		this.set(this.effortField, b);
		this.set(this.effortUnitField, a);
		switch (this.getSchedulingMode()) {
		case "EffortDriven":
			this.updateSpanBasedOnEffort();
			break;
		case "DynamicAssignment":
			this.updateAssignments();
			break
		}
		this.endEdit()
	},
	getCalendarDuration : function (a) {
		return this.getProjectCalendar().convertMSDurationToUnit(this.getEndDate() - this.getStartDate(), a || this.get(this.durationUnitField))
	},
	setDuration : function (d, i) {
		i = i || this.get(this.durationUnitField);
		var e = this.isMilestone();
		this.beginEdit();
		if (Ext.isNumber(d) && !this.getStartDate()) {
			var h = this.getTaskStore(true);
			var a = (h && h.getProjectStartDate()) || Ext.Date.clearTime(new Date());
			this.setStartDate(a)
		}
		var f = null;
		if (Ext.isNumber(d)) {
			f = this.calculateEndDate(this.getStartDate(), d, i)
		}
		this.set(this.endDateField, f);
		this.set(this.durationField, d);
		this.set(this.durationUnitField, i);
		if (this.isMilestone() != e) {
			if (e) {
				var c = this.getStartDate();
				if (c) {
					var b = this.skipNonWorkingTime(c, true);
					if (b - c !== 0) {
						this.set(this.startDateField, b)
					}
				}
			} else {
				if (f) {
					var g = this.skipNonWorkingTime(f, false);
					if (g - f !== 0) {
						this.set(this.startDateField, g);
						this.set(this.endDateField, g)
					}
				}
			}
		}
		this.onPotentialEffortChange();
		this.endEdit()
	},
	calculateStartDate : function (e, d, c) {
		c = c || this.getDurationUnit();
		if (!d) {
			return e
		}
		if (this.isManuallyScheduled()) {
			return Sch.util.Date.add(e, c, -d)
		} else {
			if (this.getTaskStore(true) && this.hasResources()) {
				var b = this.getProjectCalendar().convertDurationToMs(d, c || this.getDurationUnit());
				var a;
				this.forEachAvailabilityIntervalWithResources({
					endDate : e,
					isForward : false
				}, function (i, h, g) {
					var f = h - i;
					if (f >= b) {
						a = new Date(h - b);
						return false
					} else {
						b -= f
					}
				});
				return a
			} else {
				return this.getCalendar().calculateStartDate(e, d, c)
			}
		}
	},
	endEdit : function (a, b) {
		this.savedDirty = this.dirty;
		this.dirty = false;
		this.callParent(arguments);
		if (this.savedDirty != null) {
			this.dirty = this.savedDirty;
			this.savedDirty = null
		}
	},
	recalculateEndDate : function (a) {
		a = a || this.getStartDate();
		if (this.getSchedulingMode() == "EffortDriven") {
			return this.calculateEffortDrivenEndDate(a, this.getEffort())
		} else {
			var b = this.getDuration();
			if (Ext.isNumber(b)) {
				return this.calculateEndDate(a, b, this.getDurationUnit())
			} else {
				return this.getEndDate()
			}
		}
	},
	calculateEndDate : function (a, f, d) {
		d = d || this.getDurationUnit();
		if (!f) {
			return a
		}
		if (this.isManuallyScheduled()) {
			return Sch.util.Date.add(a, d, f)
		} else {
			var c = this.getSchedulingMode();
			if (this.getTaskStore(true) && this.hasResources() && c != "FixedDuration" && c != "DynamicAssignment" && c != "EffortDriven") {
				var b = this.getProjectCalendar().convertDurationToMs(f, d || this.getDurationUnit());
				var e;
				this.forEachAvailabilityIntervalWithResources({
					startDate : a
				}, function (j, i, h) {
					var g = i - j;
					if (g >= b) {
						e = new Date(j + b);
						return false
					} else {
						b -= g
					}
				});
				return e
			} else {
				return this.getCalendar().calculateEndDate(a, f, d)
			}
		}
	},
	calculateDuration : function (a, c, b) {
		b = b || this.getDurationUnit();
		if (!a || !c) {
			return 0
		}
		if (this.isManuallyScheduled()) {
			return this.getProjectCalendar().convertMSDurationToUnit(c - a, b)
		} else {
			if (this.getTaskStore(true) && this.hasResources()) {
				var d = 0;
				this.forEachAvailabilityIntervalWithResources({
					startDate : a,
					endDate : c
				}, function (g, f, e) {
					d += f - g
				});
				return this.getProjectCalendar().convertMSDurationToUnit(d, b)
			} else {
				return this.getCalendar().calculateDuration(a, c, b)
			}
		}
	},
	isCalendarApplicable : function (j) {
		var b = this.getStartDate();
		if (!b) {
			return true
		}
		var h = this.getTaskStore(true);
		if (!h) {
			return true
		}
		var g = Sch.util.Date.add(b, "d", (h && h.availabilitySearchLimit) || 5 * 365);
		var a = this.getAssignments();
		var c = [];
		Ext.each(a, function (k) {
			var i = k.getResource();
			if (i) {
				c.push(i.getCalendar())
			}
		});
		if (!c.length) {
			return true
		}
		var e = Gnt.data.Calendar.getCalendar(j);
		for (var f = 0, d = c.length; f < d; f++) {
			if (e.isAvailabilityIntersected(c[f], b, g)) {
				return true
			}
		}
		return false
	},
	forEachAvailabilityIntervalWithResources : function (f, h, a) {
		a = a || this;
		var C = this;
		var d = f.startDate;
		var z = f.endDate;
		var q = f.isForward !== false;
		if (q ? !d : !z) {
			throw new Error("At least `startDate` or `endDate` is required, depending from the `isForward` option")
		}
		var l = new Date(q ? d : z);
		var b = f.includeEmptyIntervals;
		var c = this.getOwnCalendar();
		var G = Boolean(c);
		var E = this.getProjectCalendar();
		var A,
		v,
		s;
		if (f.resources) {
			A = f.resources;
			s = [];
			v = [];
			Ext.each(A, function (i) {
				v.push(i.getCalendar());
				s.push(C.getAssignmentFor(i))
			})
		} else {
			s = this.getAssignments();
			A = [];
			v = [];
			Ext.each(s, function (k) {
				var i = k.getResource();
				if (i) {
					A.push(i);
					v.push(i.getCalendar())
				}
			})
		}
		if (!A.length) {
			return
		}
		var g = Sch.util.Date;
		var y,
		u,
		B,
		D,
		o;
		var j = this.getTaskStore(true);
		if (q) {
			if (!z) {
				z = g.add(d, "d", f.availabilitySearchLimit || j.availabilitySearchLimit || 5 * 365)
			}
		} else {
			if (!d) {
				d = g.add(z, "d",  - (f.availabilitySearchLimit || j.availabilitySearchLimit || 5 * 365))
			}
		}
		while (q ? l < z : l > d) {
			var r = {};
			var F = [];
			if (G) {
				var m = c.getAvailabilityIntervalsFor(l - (q ? 0 : 1));
				for (u = 0; u < m.length; u++) {
					B = m[u];
					D = B.startDate - 0;
					o = B.endDate - 0;
					if (!r[D]) {
						r[D] = [];
						F.push(D)
					}
					r[D].push({
						type : "00-taskAvailailabilityStart",
						typeBackward : "01-taskAvailailabilityStart"
					});
					F.push(o);
					r[o] = r[o] || [];
					r[o].push({
						type : "01-taskAvailailabilityEnd",
						typeBackward : "00-taskAvailailabilityEnd"
					})
				}
			}
			for (y = 0; y < v.length; y++) {
				var e = v[y].getAvailabilityIntervalsFor(l - (q ? 0 : 1));
				for (u = 0; u < e.length; u++) {
					B = e[u];
					D = B.startDate - 0;
					o = B.endDate - 0;
					if (!r[D]) {
						r[D] = [];
						F.push(D)
					}
					r[D].push({
						type : "02-resourceAvailailabilityStart",
						typeBackward : "03-resourceAvailailabilityStart",
						assignment : s[y],
						resourceId : A[y].getInternalId(),
						units : s[y].getUnits()
					});
					if (!r[o]) {
						r[o] = [];
						F.push(o)
					}
					r[o].push({
						type : "03-resourceAvailailabilityEnd",
						typeBackward : "02-resourceAvailailabilityEnd",
						assignment : s[y],
						resourceId : A[y].getInternalId(),
						units : s[y].getUnits()
					})
				}
			}
			F.sort();
			var x = false;
			var p = {};
			var n = 0;
			var w,
			t;
			if (q) {
				for (y = 0; y < F.length; y++) {
					w = r[F[y]];
					w.sort(function (k, i) {
						return k.type < i.type ? 1 : -1
					});
					for (u = 0; u < w.length; u++) {
						t = w[u];
						switch (t.type) {
						case "00-taskAvailailabilityStart":
							x = true;
							break;
						case "01-taskAvailailabilityEnd":
							x = false;
							break;
						case "02-resourceAvailailabilityStart":
							p[t.resourceId] = t;
							n++;
							break;
						case "03-resourceAvailailabilityEnd":
							delete p[t.resourceId];
							n--;
							break
						}
					}
					if ((x || !G) && (n || b)) {
						D = F[y];
						o = F[y + 1];
						if (D >= z || o <= d) {
							continue
						}
						if (D < d) {
							D = d - 0
						}
						if (o > z) {
							o = z - 0
						}
						if (h.call(a, D, o, p) === false) {
							return false
						}
					}
				}
			} else {
				for (y = F.length - 1; y >= 0; y--) {
					w = r[F[y]];
					w.sort(function (k, i) {
						return k.typeBackward < i.typeBackward ? 1 : -1
					});
					for (u = 0; u < w.length; u++) {
						t = w[u];
						switch (t.typeBackward) {
						case "00-taskAvailailabilityEnd":
							x = true;
							break;
						case "01-taskAvailailabilityStart":
							x = false;
							break;
						case "02-resourceAvailailabilityEnd":
							p[t.resourceId] = t;
							n++;
							break;
						case "03-resourceAvailailabilityStart":
							delete p[t.resourceId];
							n--;
							break
						}
					}
					if ((x || !G) && (n || b)) {
						D = F[y - 1];
						o = F[y];
						if (D > z || o <= d) {
							continue
						}
						if (D < d) {
							D = d - 0
						}
						if (o > z) {
							o = z - 0
						}
						if (h.call(a, D, o, p) === false) {
							return false
						}
					}
				}
			}
			l = q ? g.getStartOfNextDay(l) : g.getEndOfPreviousDay(l)
		}
	},
	calculateEffortDrivenEndDate : function (a, c, b) {
		var e = this.getProjectCalendar().convertDurationToMs(c, b || this.getEffortUnit());
		var d = new Date(a);
		this.forEachAvailabilityIntervalWithResources({
			startDate : a
		}, function (l, k, j) {
			var m = 0;
			for (var h in j) {
				m += j[h].units
			}
			var g = k - l;
			var f = m * g / 100;
			if (f >= e) {
				d = new Date(l + e / f * g);
				return false
			} else {
				e -= f
			}
		});
		return d
	},
	refreshCalculatedParentNodeData : function () {
		var j = new Date(9999, 0, 0),
		v = new Date(0);
		var t = this.autoCalculatePercentDoneForParentTask;
		var h = this.autoCalculateEffortForParentTask;
		var a = this.childNodes;
		var d = a.length;
		var f = {};
		if (d > 0 && (h || t)) {
			var g = 0;
			var n = 0;
			var p = 0;
			for (var q = 0; q < d; q++) {
				var b = a[q];
				var u = b.isLeaf();
				if (h) {
					g += b.getEffort("MILLI")
				}
				if (t) {
					var c = u ? b.getDuration("MILLI") || 0 : b.childTasksDuration;
					n += c;
					p += u ? c * (b.getPercentDone() || 0) : b.completedChildTasksDuration
				}
			}
			if (h && this.getEffort("MILLI") != g) {
				f.Effort = true;
				this.setEffort(this.getProjectCalendar().convertMSDurationToUnit(g, this.getEffortUnit()))
			}
			if (t) {
				this.childTasksDuration = n;
				this.completedChildTasksDuration = p;
				var o = n ? p / n : 0;
				if (this.getPercentDone() != o) {
					f.PercentDone = true;
					this.setPercentDone(o)
				}
			}
		}
		var e,
		l;
		if (!this.isRoot() && d > 0) {
			if (!this.isManuallyScheduled()) {
				for (var s = 0; s < d; s++) {
					var m = a[s];
					j = Sch.util.Date.min(j, m.getStartDate() || j);
					v = Sch.util.Date.max(v, m.getEndDate() || v)
				}
				e = f.StartDate = j - new Date(9999, 0, 0) !== 0 && this.getStartDate() - j !== 0;
				l = f.EndDate = v - new Date(0) !== 0 && this.getEndDate() - v !== 0;
				if (e && l) {
					this.setStartEndDate(j, v, false)
				} else {
					if (e) {
						this.setStartDate(j, false, false, false)
					} else {
						if (l) {
							this.setEndDate(v, false, false, false)
						}
					}
				}
			}
		}
		return f
	},
	recalculateParents : function () {
		var a = this.parentNode;
		if (a) {
			var b = a.refreshCalculatedParentNodeData();
			var d = b.StartDate;
			var c = b.EndDate;
			if ((this.getTaskStore().cascading && (d || c)) || (!d && !c)) {
				if (!a.isRoot()) {
					a.recalculateParents()
				}
			}
		}
	},
	isMilestone : function (a) {
		return a ? this.isBaselineMilestone() : this.getDuration() === 0
	},
	convertToMilestone : function () {
		if (!this.isMilestone()) {
			this.setStartDate(this.getEndDate(), false);
			this.setDuration(0)
		}
	},
	convertToRegular : function () {
		if (this.isMilestone()) {
			var b = this.get(this.durationUnitField);
			this.setDuration(1, b);
			var a = this.calculateStartDate(this.getStartDate(), 1, b);
			this.setStartDate(a, true, false, false)
		}
	},
	isBaselineMilestone : function () {
		var b = this.getBaselineStartDate(),
		a = this.getBaselineEndDate();
		if (b && a) {
			return a - b === 0
		}
		return false
	},
	afterEdit : function (b) {
		if (this.savedDirty != null) {
			this.dirty = this.savedDirty;
			this.savedDirty = null
		}
		if (this.stores.length > 0 || !this.normalized) {
			this.callParent(arguments)
		} else {
			var a = this.taskStore || this.getTaskStore(true);
			if (a && !a.isFillingRoot) {
				a.afterEdit(this, b)
			}
			this.callParent(arguments)
		}
	},
	afterCommit : function () {
		this.callParent(arguments);
		if (this.stores.length > 0 || !this.normalized) {
			return
		}
		var a = this.taskStore || this.getTaskStore(true);
		if (a && !a.isFillingRoot) {
			a.afterCommit(this)
		}
	},
	afterReject : function () {
		if (this.stores.length > 0) {
			this.callParent(arguments)
		} else {
			var a = this.getTaskStore(true);
			if (a && !a.isFillingRoot) {
				a.afterReject(this)
			}
			this.callParent(arguments)
		}
	},
	getDurationUnit : function () {
		return this.get(this.durationUnitField) || "d"
	},
	getEffortUnit : function () {
		return this.get(this.effortUnitField) || "h"
	},
	getBaselinePercentDone : function () {
		return this.get(this.baselinePercentDoneField) || 0
	},
	isPersistable : function () {
		var a = this.parentNode;
		return !a.phantom
	},
	getResources : function () {
		var a = [];
		Ext.each(this.assignments, function (b) {
			a.push(b.getResource())
		});
		return a
	},
	getAssignments : function () {
		return this.assignments
	},
	hasAssignments : function () {
		return this.assignments.length > 0
	},
	hasResources : function () {
		var a = false;
		Ext.each(this.assignments, function (b) {
			if (b.getResource()) {
				a = true;
				return false
			}
		});
		return a
	},
	getAssignmentFor : function (b) {
		var a = null,
		c = b instanceof Gnt.model.Resource ? b.getInternalId() : b;
		Ext.each(this.assignments, function (d) {
			if (d.getResourceId() == c) {
				a = d;
				return false
			}
		});
		return a
	},
	isAssignedTo : function (a) {
		return !!this.getAssignmentFor(a)
	},
	unassign : function () {
		return this.unAssign.apply(this, arguments)
	},
	unAssign : function (c) {
		var d = this.getAssignmentStore();
		var b = this.getInternalId();
		var e = c instanceof Gnt.model.Resource ? c.getInternalId() : c;
		var a = d.findBy(function (f) {
				return f.getResourceId() == e && f.getTaskId() == b
			});
		if (a >= 0) {
			d.removeAt(a)
		}
	},
	assign : function (e, a) {
		var b = this.getTaskStore(),
		h = this.getInternalId(),
		f = b.getAssignmentStore(),
		d = b.getResourceStore();
		var g = e instanceof Gnt.model.Resource ? e.getInternalId() : e;
		f.each(function (i) {
			if (i.getTaskId() == h && i.getResourceId() == g) {
				throw "Resource can't be assigned twice to the same task"
			}
		});
		if (e instanceof Gnt.model.Resource && d.indexOf(e) == -1) {
			d.add(e)
		}
		var c = new Gnt.model.Assignment({
				TaskId : h,
				ResourceId : g,
				Units : a
			});
		f.add(c);
		return c
	},
	calculateEffort : function (a, c, b) {
		if (!a || !c) {
			return 0
		}
		var d = 0;
		this.forEachAvailabilityIntervalWithResources({
			startDate : a,
			endDate : c
		}, function (h, g, f) {
			var j = 0;
			for (var e in f) {
				j += f[e].units
			}
			d += (g - h) * j / 100
		});
		return this.getProjectCalendar().convertMSDurationToUnit(d, b || this.getEffortUnit())
	},
	updateAssignments : function () {
		var b = {};
		var a = this.getStartDate();
		var d = this.getEndDate();
		if (!a || !d) {
			return
		}
		var c = 0;
		this.forEachAvailabilityIntervalWithResources({
			startDate : a,
			endDate : d
		}, function (h, g, f) {
			for (var i in f) {
				c += g - h
			}
		});
		if (!c) {
			return
		}
		var e = this.getEffort(Sch.util.Date.MILLI);
		Ext.Array.each(this.getAssignments(), function (f) {
			f.setUnits(e / c * 100)
		})
	},
	updateEffortBasedOnDuration : function () {
		this.setEffort(this.calculateEffort(this.getStartDate(), this.getEndDate()))
	},
	updateEffortBasedOnSpan : function () {
		this.updateEffortBasedOnDuration()
	},
	updateSpanBasedOnEffort : function () {
		this.setStartEndDate(this.getStartDate(), this.recalculateEndDate(), this.getTaskStore().skipWeekendsDuringDragDrop)
	},
	onPotentialEffortChange : function () {
		switch (this.getSchedulingMode()) {
		case "FixedDuration":
			this.updateEffortBasedOnDuration();
			break;
		case "DynamicAssignment":
			this.updateAssignments();
			break
		}
	},
	onAssignmentMutation : function () {
		switch (this.getSchedulingMode()) {
		case "FixedDuration":
			this.updateEffortBasedOnDuration();
			break;
		case "EffortDriven":
			this.updateSpanBasedOnEffort();
			break
		}
	},
	onAssignmentStructureMutation : function () {
		switch (this.getSchedulingMode()) {
		case "FixedDuration":
			this.updateEffortBasedOnDuration();
			break;
		case "EffortDriven":
			this.updateSpanBasedOnEffort();
			break;
		case "DynamicAssignment":
			this.updateAssignments();
			break
		}
	},
	adjustToCalendar : function () {
		if (this.get("leaf") && !this.isManuallyScheduled()) {
			this.setStartDate(this.getStartDate(), true, this.getTaskStore().skipWeekendsDuringDragDrop);
			var c = this.getTaskStore(true);
			if (c && c.cascadeChanges) {
				var e = this.getViolatedConstraints();
				if (e) {
					for (var d = 0, a = e.length, b; d < a; d++) {
						b = e[d];
						this.resolveViolatedConstraints(!d ? b : null)
					}
				}
			}
		}
	},
	isEditable : function (a) {
		if (!this.isLeaf()) {
			if (a === this.effortField && this.autoCalculateEffortForParentTask) {
				return false
			}
			if (a === this.percentDoneField && this.autoCalculatePercentDoneForParentTask) {
				return false
			}
		}
		if ((a === this.durationField || a === this.endDateField) && this.getSchedulingMode() === "EffortDriven") {
			return false
		}
		if (a === this.effortField && this.getSchedulingMode() === "FixedDuration") {
			return false
		}
		return true
	},
	isDraggable : function () {
		return this.getDraggable()
	},
	isResizable : function () {
		return this.getResizable()
	},
	getWBSCode : function () {
		var b = [],
		a = this;
		while (a.parentNode) {
			b.push(a.data.index + 1);
			a = a.parentNode
		}
		return b.reverse().join(".")
	},
	resetTotalCount : function (b) {
		var a = this;
		while (a) {
			a.totalCount = b ? -1 : null;
			a = a.parentNode
		}
	},
	getTotalCount : function () {
		var b = this.totalCount;
		var c = b == -1;
		if (b == null || c) {
			var e = this.childNodes;
			b = e.length;
			for (var d = 0, a = e.length; d < a; d++) {
				b += e[d].getTotalCount()
			}
			if (c) {
				return b
			} else {
				this.totalCount = b
			}
		}
		return b
	},
	getPredecessorsCount : function () {
		var a = this.previousSibling,
		b = this.data.index;
		while (a) {
			b += a.getTotalCount();
			a = a.previousSibling
		}
		return b
	},
	getSequenceNumber : function () {
		var b = 0,
		a = this;
		while (a.parentNode) {
			b += a.getPredecessorsCount() + 1;
			a = a.parentNode
		}
		return b
	},
	getBySequenceNumber : function (f) {
		var c = null,
		e,
		b;
		if (f === 0) {
			c = this
		} else {
			if (f > 0 && f <= this.getTotalCount()) {
				f--;
				for (var d = 0, a = this.childNodes.length; d < a; d++) {
					e = this.childNodes[d];
					b = e.getTotalCount();
					if (f > b) {
						f -= b + 1
					} else {
						e = this.childNodes[d];
						c = e.getBySequenceNumber(f);
						break
					}
				}
			}
		}
		return c
	},
	getDisplayStartDate : function (g, d, e, b, c) {
		var f = this.getEndDate(),
		a = this.getStartDate();
		if (arguments.length < 3) {
			e = this.getStartDate();
			if (arguments.length < 2) {
				d = true
			}
		}
		if (e && d && this.isMilestone(c) && e - Ext.Date.clearTime(e, true) === 0 && !Ext.Date.formatContainsHourInfo(g)) {
			e = Sch.util.Date.add(e, Sch.util.Date.MILLI, -1)
		}
		return b ? e : (e ? Ext.util.Format.date(e, g) : "")
	},
	getDisplayEndDate : function (e, c, d, a, b) {
		if (arguments.length < 3) {
			d = this.getEndDate();
			if (arguments.length < 2) {
				c = true
			}
		}
		if (d && (!this.isMilestone(b) || c) && d - Ext.Date.clearTime(d, true) === 0 && !Ext.Date.formatContainsHourInfo(e)) {
			d = Sch.util.Date.add(d, Sch.util.Date.MILLI, -1)
		}
		return a ? d : (d ? Ext.util.Format.date(d, e) : "")
	},
	getId : function () {
		var a = this.data[this.idProperty];
		return a && a !== "root" ? a : null
	}
}, function () {
	Ext.data.NodeInterface.decorate(this);
	if (Ext.getVersion("extjs").isGreaterThan("4.2.0.663")) {
		var a = {
			idchanged : true,
			append : true,
			remove : true,
			move : true,
			insert : true,
			beforeappend : true,
			beforeremove : true,
			beforemove : true,
			beforeinsert : true,
			expand : true,
			collapse : true,
			beforeexpand : true,
			beforecollapse : true,
			sort : true,
			rootchange : true
		};
		this.override({
			fireEventArgs : function (d, e) {
				var g = Ext.data.Model.prototype.fireEventArgs,
				b,
				f,
				c;
				if (a[d]) {
					for (f = this; b !== false && f; f = (c = f).parentNode) {
						if (f.hasListeners[d]) {
							b = g.call(f, d, e)
						}
					}
					f = c.rootOf;
					if (b !== false && f) {
						if (f.hasListeners[d]) {
							b = f.fireEventArgs.call(f, d, e)
						}
						f = f.treeStore;
						if (b !== false && f) {
							if (f.hasListeners[d]) {
								b = f.fireEventArgs.call(f, d, e)
							}
						}
					}
					return b
				} else {
					return g.apply(this, arguments)
				}
			}
		})
	}
	this.override({
		remove : function () {
			var c = this.parentNode;
			var b = this.getTaskStore();
			var d = this.callParent(arguments);
			if (b.recalculateParents) {
				if (c.convertEmptyParentToLeaf && c.childNodes.length === 0) {
					c.set("leaf", true)
				} else {
					if (!c.isRoot() && c.childNodes.length > 0) {
						c.childNodes[0].recalculateParents()
					}
				}
			}
			return d
		},
		insertBefore : function (d) {
			d = this.createNode(d);
			if (this.phantom) {
				this.data[this.phantomIdField] = d.data[this.phantomParentIdField] = this.internalId
			}
			var c = d.parentNode;
			this.resetTotalCount(c);
			var b = this.callParent(arguments);
			if (c) {
				this.resetTotalCount()
			}
			return b
		},
		appendChild : function (b, f, g) {
			var e = false;
			b = b instanceof Array ? b : [b];
			for (var d = 0; d < b.length; d++) {
				b[d] = this.createNode(b[d]);
				if (b[d].parentNode) {
					e = true
				}
				if (this.phantom) {
					b[d].data[this.phantomParentIdField] = this.internalId
				}
			}
			if (this.phantom) {
				this.data[this.phantomIdField] = this.internalId
			}
			this.resetTotalCount(e);
			var c = this.callParent([b.length > 1 ? b : b[0], f, g]);
			if (e) {
				this.resetTotalCount()
			}
			return c
		},
		removeChild : function (f, d, e, b) {
			this.resetTotalCount();
			var h = this.childNodes;
			var c = !this.removeChildIsCalledFromReplaceChild && this.convertEmptyParentToLeaf && h.length == 1;
			this.removeChildIsCalledFromReplaceChild = false;
			var g = this.callParent(arguments);
			if (b) {
				this.resetTotalCount()
			}
			if (this.getTaskStore().recalculateParents) {
				if (c) {
					this.set("leaf", true)
				} else {
					if (!this.isRoot() && h.length > 0) {
						h[0].recalculateParents()
					}
				}
			}
			return g
		},
		replaceChild : function () {
			this.removeChildIsCalledFromReplaceChild = true;
			this.callParent(arguments)
		},
		removeAll : function () {
			this.resetTotalCount();
			this.callParent(arguments)
		},
		createNode : function (c) {
			c = this.callParent(arguments);
			if (!c.normalized && !c.normalizeScheduled) {
				var b = c.updateInfo;
				c.updateInfo = function () {
					b.apply(this, arguments);
					delete c.updateInfo;
					c.normalize()
				};
				c.normalizeScheduled = true
			}
			return c
		}
	})
});
Ext.define("Gnt.util.DurationParser", {
	requires : ["Sch.util.Date"],
	mixins : ["Gnt.mixin.Localizable"],
	parseNumberFn : null,
	durationRegex : null,
	allowDecimals : true,
	constructor : function (a) {
		Ext.apply(this, a);
		if (this.unitsRegex) {
			Ext.apply(this.l10n.unitsRegex, this.unitsRegex)
		}
		if (!this.durationRegex) {
			this.durationRegex = this.allowDecimals ? /^\s*([\-+]?\d+(?:[.,]\d+)?)\s*(\w+)?/i : /^\s*([\-+]?\d+)(?![.,])\s*(\w+)?/i
		}
	},
	parse : function (c) {
		var a = this.durationRegex.exec(c);
		if (c == null || !a) {
			return null
		}
		var e = this.parseNumberFn(a[1]);
		var b = a[2];
		var d;
		if (b) {
			Ext.iterate(this.L("unitsRegex"), function (f, g) {
				if (g.test(b)) {
					d = Sch.util.Date.getUnitByName(f);
					return false
				}
			});
			if (!d) {
				return null
			}
		}
		return {
			value : e,
			unit : d
		}
	}
});
Ext.define("Gnt.util.DependencyParser", {
	requires : ["Gnt.util.DurationParser"],
	separator : /\s*;\s*/,
	parseNumberFn : null,
	dependencyRegex : /(-?\d+)(SS|SF|FS|FF)?([\+\-].*)?/i,
	types : ["SS", "SF", "FS", "FF"],
	constructor : function (a) {
		this.durationParser = new Gnt.util.DurationParser(a);
		Ext.apply(this, a)
	},
	parse : function (j) {
		if (!j) {
			return []
		}
		var d = j.split(this.separator);
		var k = [];
		var c = this.dependencyRegex;
		for (var f = 0; f < d.length; f++) {
			var a = d[f];
			if (!a && f == d.length - 1) {
				continue
			}
			var g = c.exec(a);
			var e = {};
			if (!g) {
				return null
			}
			e.taskId = parseInt(g[1], 10);
			e.type = Ext.Array.indexOf(this.types, (g[2] || "FS").toUpperCase());
			var h = g[3];
			if (h) {
				var b = this.durationParser.parse(h);
				if (!b) {
					return null
				}
				e.lag = b.value;
				e.lagUnit = b.unit || "d"
			}
			k.push(e)
		}
		return k
	}
});
Ext.define("Gnt.util.Data", {
	requires : ["Ext.data.Model"],
	singleton : true,
	cloneModelSet : function (b, d, c) {
		var e = [],
		a;
		var f = function (g) {
			a = g.copy();
			Ext.data.Model.id(a);
			a.phantom = false;
			a.originalRecord = g;
			if (d) {
				if (d.call(c || b, a, g) === false) {
					return
				}
			}
			e.push(a)
		};
		if (b.each) {
			b.each(f)
		} else {
			Ext.Array.each(b, f)
		}
		return e
	},
	applyCloneChanges : function (g, m, j, p) {
		var a = [];
		var c = m.autoSyncSuspended;
		if (m.autoSync && !c) {
			m.suspendAutoSync()
		}
		var f = g.getRemovedRecords();
		for (var e = 0, b = f.length; e < b; e++) {
			if (f[e].originalRecord) {
				a.push(f[e].originalRecord)
			}
		}
		if (a.length) {
			m.remove(a);
			g.removed.length = 0
		}
		var n = g.getModifiedRecords(),
		q,
		d,
		o,
		h;
		for (e = 0, b = n.length; e < b; e++) {
			q = n[e].originalRecord;
			d = n[e].getData();
			delete d[n[e].idProperty];
			if (q) {
				q.beginEdit();
				for (var k in d) {
					q.set(k, d[k])
				}
				if (j) {
					j.call(p || n[e], d, n[e])
				}
				q.endEdit()
			} else {
				if (j) {
					j.call(p || n[e], d, n[e])
				}
				h = m.add(d);
				n[e].originalRecord = h && h[0]
			}
			n[e].commit(true)
		}
		if (m.autoSync && !c) {
			m.resumeAutoSync();
			m.sync()
		}
	}
});
Ext.define("Gnt.patches.Tree", {
	override : "Ext.data.Tree",
	onNodeRemove : function (b, c, a) {
		if (!a) {
			this.unregisterNode(c, true)
		}
	}
});
Ext.define("Gnt.data.Calendar", {
	extend : "Ext.data.Store",
	requires : ["Ext.Date", "Gnt.model.CalendarDay", "Sch.model.Range", "Sch.util.Date"],
	model : "Gnt.model.CalendarDay",
	daysPerMonth : 30,
	daysPerWeek : 7,
	hoursPerDay : 24,
	unitsInMs : null,
	defaultNonWorkingTimeCssCls : "gnt-holiday",
	weekendsAreWorkdays : false,
	weekendFirstDay : 6,
	weekendSecondDay : 0,
	holidaysCache : null,
	availabilityIntervalsCache : null,
	daysIndex : null,
	weekAvailability : null,
	defaultWeekAvailability : null,
	nonStandardWeeksByStartDate : null,
	nonStandardWeeksStartDates : null,
	calendarId : null,
	parent : null,
	defaultAvailability : ["00:00-24:00"],
	name : null,
	suspendCacheUpdate : 0,
	availabilitySearchLimit : 1825,
	statics : {
		getCalendar : function (a) {
			if (a instanceof Gnt.data.Calendar) {
				return a
			}
			return Ext.data.StoreManager.lookup("GNT_CALENDAR:" + a)
		},
		getAllCalendars : function () {
			var a = [];
			Ext.data.StoreManager.each(function (b) {
				if (b instanceof Gnt.data.Calendar) {
					a.push(b)
				}
			});
			return a
		}
	},
	constructor : function (a) {
		a = a || {};
		var b = a.parent;
		delete a.parent;
		var c = a.calendarId;
		delete a.calendarId;
		this.callParent(arguments);
		this.setParent(b);
		this.setCalendarId(c);
		this.unitsInMs = {
			MILLI : 1,
			SECOND : 1000,
			MINUTE : 60 * 1000,
			HOUR : 60 * 60 * 1000,
			DAY : this.hoursPerDay * 60 * 60 * 1000,
			WEEK : this.daysPerWeek * this.hoursPerDay * 60 * 60 * 1000,
			MONTH : this.daysPerMonth * this.hoursPerDay * 60 * 60 * 1000,
			QUARTER : 3 * this.daysPerMonth * 24 * 60 * 60 * 1000,
			YEAR : 4 * 3 * this.daysPerMonth * 24 * 60 * 60 * 1000
		};
		this.defaultWeekAvailability = this.getDefaultWeekAvailability();
		this.on({
			update : this.clearCache,
			datachanged : this.clearCache,
			clear : this.clearCache,
			load : this.clearCache,
			scope : this
		});
		this.clearCache()
	},
	getCalendarId : function () {
		return this.calendarId
	},
	setCalendarId : function (b) {
		if (this.calendarId != null) {
			Ext.data.StoreManager.unregister(this)
		}
		this.calendarId = b;
		if (b != null) {
			this.storeId = "GNT_CALENDAR:" + b;
			Ext.data.StoreManager.register(this)
		} else {
			this.storeId = null
		}
		var a = this.proxy;
		if (a && a.extraParams) {
			a.extraParams.calendarId = b
		}
	},
	getDefaultWeekAvailability : function () {
		var e = this.defaultAvailability;
		var d = this.weekendFirstDay;
		var a = this.weekendSecondDay;
		var c = [];
		for (var b = 0; b < 7; b++) {
			c.push(this.weekendsAreWorkdays || b != d && b != a ? new Gnt.model.CalendarDay({
					Type : "WEEKDAY",
					Weekday : b,
					Availability : Ext.Array.clone(e),
					IsWorkingDay : true
				}) : new Gnt.model.CalendarDay({
					Type : "WEEKDAY",
					Weekday : b,
					Availability : []
				}))
		}
		return c
	},
	clearCache : function () {
		if (this.suspendCacheUpdate > 0) {
			return
		}
		this.holidaysCache = {};
		this.availabilityIntervalsCache = {};
		var c = this.daysIndex = {};
		var a = this.weekAvailability = [];
		var d = this.nonStandardWeeksStartDates = [];
		var b = this.nonStandardWeeksByStartDate = {};
		this.each(function (k) {
			var e = k.getId();
			var n = /^(\d)-(\d\d\d\d\/\d\d\/\d\d)-(\d\d\d\d\/\d\d\/\d\d)$/.exec(e);
			var j = /^WEEKDAY:(\d+)$/.exec(e);
			var m = k.getType();
			var l = k.getWeekday();
			if (m == "WEEKDAYOVERRIDE" || n) {
				var f,
				i;
				if (m == "WEEKDAYOVERRIDE") {
					f = k.getOverrideStartDate();
					i = k.getOverrideEndDate()
				}
				if (n) {
					f = Ext.Date.parse(n[2], "Y/m/d");
					i = Ext.Date.parse(n[3], "Y/m/d");
					l = n[1]
				}
				if (f && i && l != null) {
					var h = f - 0;
					if (!b[h]) {
						b[h] = {
							startDate : new Date(f),
							endDate : new Date(i),
							name : k.getName(),
							weekAvailability : [],
							mainDay : null
						};
						d.push(h)
					}
					if (l >= 0) {
						b[h].weekAvailability[l] = k
					} else {
						b[h].mainDay = k
					}
				}
			} else {
				if (m == "WEEKDAY" || j) {
					if (j) {
						l = j[1]
					}
					if (l != null) {
						if (l < 0 || l > 6) {
							throw new Error("Incorrect week day index")
						}
						a[l] = k
					}
				} else {
					var g = k.getDate();
					if (g) {
						c[g - 0] = k
					}
				}
			}
		});
		d.sort();
		this.fireEvent("calendarchange", this)
	},
	intersectsWithCurrentWeeks : function (b, c) {
		var a = false;
		this.forEachNonStandardWeek(function (f) {
			var d = f.startDate;
			var e = f.endDate;
			if (d <= b && b < e || d < c && c <= e) {
				a = true;
				return false
			}
		});
		return a
	},
	addNonStandardWeek : function (b, f, a, c) {
		b = Ext.Date.clearTime(new Date(b));
		f = Ext.Date.clearTime(new Date(f));
		if (this.intersectsWithCurrentWeeks(b, f)) {
			throw new Error("Can not add intersecting week")
		}
		var e = this.model;
		var g = [];
		Ext.Array.each(a, function (h, i) {
			if (h instanceof Gnt.model.CalendarDay) {
				h.setType("WEEKDAYOVERRIDE");
				h.setOverrideStartDate(b);
				h.setOverrideEndDate(f);
				h.setWeekday(i);
				h.setName(c || "Week override");
				g.push(h)
			} else {
				if (Ext.isArray(h)) {
					var j = new e();
					j.setType("WEEKDAYOVERRIDE");
					j.setOverrideStartDate(b);
					j.setOverrideEndDate(f);
					j.setWeekday(i);
					j.setName(c || "Week override");
					j.setAvailability(h);
					g.push(j)
				}
			}
		});
		var d = new e();
		d.setType("WEEKDAYOVERRIDE");
		d.setOverrideStartDate(b);
		d.setOverrideEndDate(f);
		d.setWeekday(-1);
		d.setName(c || "Week override");
		g.push(d);
		this.add(g)
	},
	getNonStandardWeekByStartDate : function (a) {
		return this.nonStandardWeeksByStartDate[Ext.Date.clearTime(new Date(a)) - 0] || null
	},
	getNonStandardWeekByDate : function (d) {
		d = Ext.Date.clearTime(new Date(d)) - 0;
		var e = this.nonStandardWeeksStartDates;
		var a = this.nonStandardWeeksByStartDate;
		for (var c = 0; c < e.length; c++) {
			var b = a[e[c]];
			if (b.startDate > d) {
				break
			}
			if (b.startDate <= d && d <= b.endDate) {
				return b
			}
		}
		return null
	},
	removeNonStandardWeek : function (a) {
		a = Ext.Date.clearTime(new Date(a)) - 0;
		var b = this.getNonStandardWeekByStartDate(a);
		if (!b) {
			return
		}
		this.remove(Ext.Array.clean(b.weekAvailability).concat(b.mainDay))
	},
	forEachNonStandardWeek : function (e, c) {
		var d = this;
		var f = this.nonStandardWeeksStartDates;
		var a = this.nonStandardWeeksByStartDate;
		for (var b = 0; b < f.length; b++) {
			if (e.call(c || d, a[f[b]]) === false) {
				return false
			}
		}
	},
	setWeekendsAreWorkDays : function (a) {
		if (a !== this.weekendsAreWorkdays) {
			this.weekendsAreWorkdays = a;
			this.defaultWeekAvailability = this.getDefaultWeekAvailability();
			this.clearCache()
		}
	},
	areWeekendsWorkDays : function () {
		return this.weekendsAreWorkdays
	},
	getCalendarDay : function (a) {
		a = typeof a == "number" ? new Date(a) : a;
		return this.getOverrideDay(a) || this.getWeekDay(a.getDay(), a) || this.getDefaultCalendarDay(a.getDay())
	},
	getOverrideDay : function (a) {
		return this.getOwnCalendarDay(a) || this.parent && this.parent.getOverrideDay(a) || null
	},
	getOwnCalendarDay : function (a) {
		a = typeof a == "number" ? new Date(a) : a;
		return this.daysIndex[Ext.Date.clearTime(a, true) - 0]
	},
	getWeekDay : function (c, b) {
		if (b) {
			var a = this.getNonStandardWeekByDate(b);
			if (a && a.weekAvailability[c]) {
				return a.weekAvailability[c]
			}
		}
		return this.weekAvailability[c] || this.parent && this.parent.getWeekDay(c, b) || null
	},
	getDefaultCalendarDay : function (a) {
		if (!this.hasOwnProperty("defaultAvailability") && !this.hasOwnProperty("weekendsAreWorkdays") && this.parent) {
			return this.parent.getDefaultCalendarDay(a)
		}
		return this.defaultWeekAvailability[a]
	},
	isHoliday : function (c) {
		var b = c - 0;
		var d = this.holidaysCache;
		if (d[b] != null) {
			return d[b]
		}
		c = typeof c == "number" ? new Date(c) : c;
		var a = this.getCalendarDay(c);
		if (!a) {
			throw "Can't find day for " + c
		}
		return d[b] = !a.getIsWorkingDay()
	},
	isWeekend : function (b) {
		var a = b.getDay();
		return a === this.weekendFirstDay || a === this.weekendSecondDay
	},
	isWorkingDay : function (a) {
		return !this.isHoliday(a)
	},
	convertMSDurationToUnit : function (a, b) {
		return a / this.unitsInMs[Sch.util.Date.getNameOfUnit(b)]
	},
	convertDurationToMs : function (b, a) {
		return b * this.unitsInMs[Sch.util.Date.getNameOfUnit(a)]
	},
	getHolidaysRanges : function (d, g, a) {
		if (d > g) {
			Ext.Error.raise("startDate can't be bigger than endDate")
		}
		d = Ext.Date.clearTime(d, true);
		g = Ext.Date.clearTime(g, true);
		var c = [],
		h,
		e;
		for (e = d; e < g; e = Sch.util.Date.getNext(e, Sch.util.Date.DAY, 1)) {
			if (this.isHoliday(e) || (this.weekendsAreWorkdays && a && this.isWeekend(e))) {
				var i = this.getCalendarDay(e);
				var j = i && i.getCls() || this.defaultNonWorkingTimeCssCls;
				var f = Sch.util.Date.getNext(e, Sch.util.Date.DAY, 1);
				if (!h) {
					h = {
						StartDate : e,
						EndDate : f,
						Cls : j
					}
				} else {
					if (h.Cls == j) {
						h.EndDate = f
					} else {
						c.push(h);
						h = {
							StartDate : e,
							EndDate : f,
							Cls : j
						}
					}
				}
			} else {
				if (h) {
					c.push(h);
					h = null
				}
			}
		}
		if (h) {
			c.push(h)
		}
		var b = [];
		Ext.each(c, function (k) {
			b.push(Ext.create("Sch.model.Range", {
					StartDate : k.StartDate,
					EndDate : k.EndDate,
					Cls : k.Cls
				}))
		});
		return b
	},
	forEachAvailabilityInterval : function (r, f, q) {
		q = q || this;
		var m = this;
		var d = r.startDate;
		var k = r.endDate;
		var p = r.isForward !== false;
		if (p ? !d : !k) {
			throw new Error("At least `startDate` or `endDate` is required, depending from the `isForward` option")
		}
		var a = new Date(p ? d : k);
		var c = Sch.util.Date;
		if (p) {
			if (!k) {
				k = c.add(d, "d", r.availabilitySearchLimit || this.availabilitySearchLimit || 5 * 365)
			}
		} else {
			if (!d) {
				d = c.add(k, "d",  - (r.availabilitySearchLimit || this.availabilitySearchLimit || 5 * 365))
			}
		}
		var g = false;
		while (p ? a < k : a > d) {
			var j = this.getAvailabilityIntervalsFor(a - (p ? 0 : 1), p ? g : false);
			for (var h = p ? 0 : j.length - 1; p ? h < j.length : h >= 0; p ? h++ : h--) {
				var b = j[h];
				var l = b.startDate;
				var o = b.endDate;
				if (l >= k || o <= d) {
					continue
				}
				var e = l < d ? d : l;
				var n = o > k ? k : o;
				if (f.call(q, e, n) === false) {
					return false
				}
			}
			a = p ? c.getStartOfNextDay(a, false, g) : c.getEndOfPreviousDay(a, g);
			g = true
		}
	},
	calculateDuration : function (a, d, b) {
		var c = 0;
		this.forEachAvailabilityInterval({
			startDate : a,
			endDate : d
		}, function (g, f) {
			var e = g.getTimezoneOffset() - f.getTimezoneOffset();
			c += f - g + e * 60 * 1000
		});
		return this.convertMSDurationToUnit(c, b)
	},
	calculateEndDate : function (a, f, b) {
		if (!f) {
			return new Date(a)
		}
		var e = Sch.util.Date,
		d;
		f = this.convertDurationToMs(f, b);
		var c = f === 0 && Ext.Date.clearTime(a, true) - a === 0 ? e.add(a, Sch.util.Date.DAY, -1) : a;
		this.forEachAvailabilityInterval({
			startDate : c
		}, function (i, h) {
			var j = h - i;
			var g = i.getTimezoneOffset() - h.getTimezoneOffset();
			if (j >= f) {
				d = new Date(i - 0 + f);
				return false
			} else {
				f -= j + g * 60 * 1000
			}
		});
		return d
	},
	calculateStartDate : function (d, c, b) {
		if (!c) {
			return new Date(d)
		}
		var a;
		c = this.convertDurationToMs(c, b);
		this.forEachAvailabilityInterval({
			endDate : d,
			isForward : false
		}, function (f, e) {
			var g = e - f;
			if (g >= c) {
				a = new Date(e - c);
				return false
			} else {
				c -= g
			}
		});
		return a
	},
	skipNonWorkingTime : function (a, c) {
		var b = false;
		this.forEachAvailabilityInterval(c ? {
			startDate : a
		}
			 : {
			endDate : a,
			isForward : false
		}, function (e, d) {
			a = c ? e : d;
			b = true;
			return false
		});
		if (!b) {
			throw "skipNonWorkingTime: Cannot skip non-working time, please ensure that this calendar has any working period of time specified"
		}
		return new Date(a)
	},
	skipWorkingTime : function (a, c, b) {
		return c >= 0 ? this.calculateEndDate(a, c, b) : this.calculateStartDate(a, -c, b)
	},
	getAvailabilityIntervalsFor : function (a, b) {
		a = b ? a - 0 : Ext.Date.clearTime(new Date(a)) - 0;
		if (this.availabilityIntervalsCache[a]) {
			return this.availabilityIntervalsCache[a]
		}
		return this.availabilityIntervalsCache[a] = this.getCalendarDay(a).getAvailabilityIntervalsFor(a)
	},
	getByInternalId : function (a) {
		return this.data.map[a]
	},
	getParentableCalendars : function () {
		var c = this,
		a = [],
		d = Gnt.data.Calendar.getAllCalendars();
		var b = function (e) {
			if (!e.parent) {
				return false
			}
			if (e.parent == c) {
				return true
			}
			return b(e.parent)
		};
		Ext.Array.each(d, function (e) {
			if (e === c) {
				return
			}
			if (!b(e)) {
				a.push({
					Id : e.calendarId,
					Name : e.name || e.calendarId
				})
			}
		});
		return a
	},
	setParent : function (e) {
		var d = Gnt.data.Calendar.getCalendar(e);
		if (e && !d) {
			throw new Error("Invalid parent specified for the calendar")
		}
		if (this.parent != d) {
			var b = this.proxy;
			var c = {
				calendarchange : this.clearCache,
				scope : this
			};
			var a = this.parent;
			if (a) {
				a.un(c)
			}
			this.parent = d;
			if (d) {
				d.on(c)
			}
			if (b && b.extraParams) {
				b.extraParams.parentId = d ? d.calendarId : null
			}
			this.clearCache();
			this.fireEvent("parentchange", this, d, a)
		}
	},
	isAvailabilityIntersected : function (p, b, m) {
		var n,
		a,
		e,
		h;
		for (var g = 0; g < 7; g++) {
			n = this.getWeekDay(g) || this.getDefaultCalendarDay(g);
			e = p.getWeekDay(g) || p.getDefaultCalendarDay(g);
			if (!n || !e) {
				continue
			}
			a = n.getAvailability();
			h = e.getAvailability();
			for (var f = 0, c = a.length; f < c; f++) {
				for (var d = 0, o = h.length; d < o; d++) {
					if (h[d].startTime < a[f].endTime && h[d].endTime > a[f].startTime) {
						return true
					}
				}
			}
		}
		var q = false;
		this.forEachNonStandardWeek(function (i) {
			if (i.startDate >= m) {
				return false
			}
			if (b < i.endDate) {
				q = true;
				return false
			}
		});
		return q
	}
});
Ext.define("Gnt.data.calendar.BusinessTime", {
	extend : "Gnt.data.Calendar",
	daysPerMonth : 20,
	daysPerWeek : 5,
	hoursPerDay : 8,
	defaultAvailability : ["08:00-12:00", "13:00-17:00"]
});
Ext.define("Gnt.data.DependencyStore", {
	extend : "Ext.data.Store",
	model : "Gnt.model.Dependency",
	taskStore : null,
	methodsCache : null,
	strictDependencyValidation : false,
	ignoreInitial : true,
	isLoadingRecords : false,
	allowedDependencyTypes : null,
	constructor : function () {
		this.mixins.observable.constructor.apply(this, arguments);
		this.init();
		this.callParent(arguments);
		this.ignoreInitial = false
	},
	init : function () {
		this.methodsCache = {};
		this.on({
			add : this.onDependencyAdd,
			update : this.onDependencyUpdate,
			load : this.onDependencyLoad,
			datachanged : this.onDependencyDataChanged,
			remove : this.onDependecyRemove,
			clear : this.onDependencyStoreClear,
			beforesync : this.onBeforeSyncOperation,
			scope : this
		})
	},
	onDependencyLoad : function () {
		var a = this.getTaskStore();
		a && a.fillTasksWithDepInfo()
	},
	onDependencyDataChanged : function () {
		var a = this.getTaskStore();
		if (this.isLoadingRecords && a) {
			a.fillTasksWithDepInfo()
		}
	},
	loadRecords : function () {
		this.isLoadingRecords = true;
		this.callParent(arguments);
		this.isLoadingRecords = false
	},
	scheduleTask : function (a) {
		var b = this.getTaskStore();
		a.beginEdit();
		if (!a.getStartDate()) {
			a.setStartDate(b.getProjectStartDate(), undefined !== a.getDuration(), b.skipWeekendsDuringDragDrop)
		}
		if (!a.getEndDate()) {
			a.setDuration(1)
		}
		a.endEdit()
	},
	scheduleLinkedTasks : function (b, a) {
		this.scheduleTask(b);
		if (!a.getStartDate() && !b.getTaskStore().cascadeChanges) {
			a.constrain()
		}
		this.scheduleTask(a)
	},
	onDependencyAdd : function (c, d) {
		if (this.ignoreInitial) {
			return
		}
		for (var b = 0; b < d.length; b++) {
			var a = d[b];
			if (!this.isValidDependencyType(a.getType())) {
				throw "This dependency type is invalid. Check Gnt.data.DependencyStore#allowedDependencyTypes value"
			}
			var f = a.getSourceTask(),
			e = a.getTargetTask();
			if (f && e) {
				f.successors.push(a);
				e.predecessors.push(a);
				this.scheduleLinkedTasks(f, e)
			}
		}
		c.resetMethodsCache()
	},
	onDependecyRemove : function (c, b) {
		var a = this.getTaskStore();
		var e = b.getSourceTask(a),
		d = b.getTargetTask(a);
		if (e) {
			Ext.Array.remove(e.successors, b)
		}
		if (d) {
			Ext.Array.remove(d.predecessors, b)
		}
		c.resetMethodsCache()
	},
	onDependencyUpdate : function (i, b, c) {
		if (c != Ext.data.Model.COMMIT) {
			var j = this.getTaskStore();
			var e = b.previous;
			var h = b.getSourceTask();
			var f = b.getTargetTask();
			var g = b.fromField in e;
			var a = b.toField in e;
			if (g) {
				var k = j.getById(e[b.fromField]);
				k && Ext.Array.remove(k.successors, b);
				h && h.successors.push(b)
			}
			if (a) {
				var d = j.getById(e[b.toField]);
				d && Ext.Array.remove(d.predecessors, b);
				f && f.predecessors.push(b)
			}
			if ((g || a) && h && f) {
				this.scheduleLinkedTasks(h, f)
			}
			this.resetMethodsCache()
		}
	},
	onDependencyStoreClear : function (b) {
		var a = b.getTaskStore();
		a && a.fillTasksWithDepInfo()
	},
	onBeforeSyncOperation : function (a) {
		if (a.create) {
			for (var c, b = a.create.length - 1; b >= 0; b--) {
				c = a.create[b];
				if (!c.isPersistable()) {
					Ext.Array.remove(a.create, c)
				}
			}
			if (a.create.length === 0) {
				delete a.create
			}
		}
		return Boolean((a.create && a.create.length > 0) || (a.update && a.update.length > 0) || (a.destroy && a.destroy.length > 0))
	},
	getDependenciesForTask : function (a) {
		return a.successors.concat(a.predecessors)
	},
	getIncomingDependenciesForTask : function (a, b) {
		return b ? a.predecessors : a.predecessors.slice()
	},
	getOutgoingDependenciesForTask : function (a, b) {
		return b ? a.successors : a.successors.slice()
	},
	getKeyByDeps : function (e, g, b) {
		if (!e || !e.length) {
			return ""
		}
		var d = "";
		for (var c = 0, a = e.length; c < a; c++) {
			var f = e[c];
			d += (f.getSourceId && f.getSourceId() || f[g]) + ":" + (f.getTargetId && f.getTargetId() || f[b]) + ","
		}
		return d
	},
	buildCacheKey : function (f, g, c, e, a) {
		var i = a.fromField || (a.fromField = this.model.prototype.fromField),
		d = a.toField || (a.toField = this.model.prototype.toField),
		b = a.ignoreDepKey,
		h = a.addDepKey;
		if (!a.hasOwnProperty("ignoreDepKey")) {
			a.ignoreDepKey = b = c && this.getKeyByDeps(c, i, d) || "";
			a.addDepKey = h = e && this.getKeyByDeps(e, i, d) || ""
		}
		return f + "-" + g + "-" + b + "-" + h
	},
	hasTransitiveDependency : function (f, b, c, o, a) {
		a = a || {
			visitedTasks : {}

		};
		var d = this.buildCacheKey(f, b, c, o, a);
		var r = a.visitedTasks,
		p = a.extraSuccessors;
		if (this.isCachedResultAvailable("hasTransitiveDependency", d)) {
			return this.methodsCache.hasTransitiveDependency[d]
		}
		var q = this,
		u = a.fromField,
		g = a.toField,
		s = this.getTaskById(f),
		n,
		k;
		if (r[f]) {
			return false
		}
		r[f] = true;
		if (s) {
			if (o && !p) {
				p = a.extraSuccessors = {};
				for (n = 0, k = o.length; n < k; n++) {
					var e = o[n];
					var m = e.getSourceId && e.getSourceId() || e[u];
					p[m] = p[m] || [];
					p[m].push(e)
				}
			}
			var j,
			h = s.successors;
			if (p && p[f]) {
				h = h.concat(p[f])
			}
			for (n = 0, k = h.length; n < k; n++) {
				j = h[n];
				var t = j.getTargetId && j.getTargetId() || j[g];
				if ((!c || Ext.Array.indexOf(c, j) == -1) && (t === b || q.hasTransitiveDependency(t, b, c, o, a))) {
					return this.setCachedResult("hasTransitiveDependency", d, true)
				}
			}
		}
		return this.setCachedResult("hasTransitiveDependency", d, false)
	},
	successorsHaveTransitiveDependency : function (j, k, e, h, b) {
		b = b || {};
		var g = this.buildCacheKey(j, k, e, h, b);
		var c = k instanceof Gnt.model.Task ? k : this.getTaskById(k);
		if (this.isCachedResultAvailable("successorsHaveTransitiveDependency", g)) {
			return this.methodsCache.successorsHaveTransitiveDependency[g]
		}
		for (var f = 0, d = c.successors.length; f < d; f++) {
			var a = c.successors[f].getTargetId();
			if (this.hasTransitiveDependency(j, a, e, h) || this.predecessorsHaveTransitiveDependency(j, a, e, h) || this.successorsHaveTransitiveDependency(j, a, e, h, b)) {
				return this.setCachedResult("successorsHaveTransitiveDependency", g, true)
			}
		}
		return this.setCachedResult("successorsHaveTransitiveDependency", g, false)
	},
	predecessorsHaveTransitiveDependency : function (j, k, d, h, a) {
		a = a || {};
		var g = this.buildCacheKey(j, k, d, h, a);
		var b = j instanceof Gnt.model.Task ? j : this.getTaskById(j);
		if (this.isCachedResultAvailable("predecessorsHaveTransitiveDependency", g)) {
			return this.methodsCache.predecessorsHaveTransitiveDependency[g]
		}
		for (var f = 0, c = b.predecessors.length; f < c; f++) {
			var e = b.predecessors[f].getSourceId();
			if (this.hasTransitiveDependency(e, k, d, h) || this.successorsHaveTransitiveDependency(e, k, d, h) || this.predecessorsHaveTransitiveDependency(e, k, d, h, a)) {
				return this.setCachedResult("predecessorsHaveTransitiveDependency", g, true)
			}
		}
		return this.setCachedResult("predecessorsHaveTransitiveDependency", g, false)
	},
	isPartOfTransitiveDependency : function (e, c, d, a) {
		var b = e instanceof Gnt.model.Task ? e : this.getTaskById(e);
		if (!b.predecessors.length && !b.successors.length) {
			return false
		}
		if (b.predecessors.length) {
			return this.predecessorsHaveTransitiveDependency.apply(this, arguments)
		} else {
			return this.successorsHaveTransitiveDependency.apply(this, arguments)
		}
	},
	getCycle : function (a) {
		a = a || {};
		Ext.applyIf(a, {
			ignoreTasks : {},
			visitedTasks : {},
			path : [],
			task : this.getAt(0).getSourceTask()
		});
		var f = a.visitedTasks,
		g = a.ignoreTasks,
		k = a.path,
		b = a.task,
		j = b.getInternalId();
		if (g[j]) {
			return
		}
		k.push(b);
		if (f[j]) {
			return k
		}
		f[j] = true;
		var c = b.successors;
		for (var h = 0, d = c.length; h < d; h++) {
			a.task = c[h].getTargetTask();
			var e = this.getCycle(a);
			if (e) {
				return e
			}
		}
		k.pop();
		delete f[j]
	},
	getCycles : function () {
		var c = this,
		a = [],
		b = {};
		this.each(function (g) {
			var f = c.getCycle({
					task : g.getSourceTask(),
					ignoreTasks : b
				});
			if (f) {
				for (var e = 0, d = f.length; e < d; e++) {
					b[f[e]] = true
				}
				a.push(f)
			}
		});
		return a
	},
	resetMethodsCache : function () {
		this.methodsCache = {}

	},
	isCachedResultAvailable : function (b, a) {
		return this.methodsCache[b] && this.methodsCache[b].hasOwnProperty(a)
	},
	getCachedResult : function (b, a) {
		return this.methodsCache[b][a]
	},
	setCachedResult : function (c, a, b) {
		this.methodsCache[c] = this.methodsCache[c] || {};
		this.methodsCache[c][a] = b;
		return b
	},
	getGroupTopTasks : function (g, h) {
		var b = g.length,
		f = h.length,
		d = b,
		a = f,
		e,
		c;
		do {
			e = g[d];
			c = h[a];
			d--;
			a--
		} while (e == c && d >= 0 && a >= 0);
		return [e, c]
	},
	groupsHasTransitiveDependency : function (h, c, d, u, b) {
		var q = b || {
			targets : null,
			visitedTasks : {}

		};
		var r = this.getTaskStore().getRootNode(),
		j = false,
		y = this,
		o = this.getTaskById(h),
		A = this.getTaskById(c),
		z = q.visitedTasks,
		x = q.targets;
		if (!q.targetGroup) {
			q.targetGroup = A.getTopParent(true)
		}
		var B = q.fromField || (q.fromField = this.model.prototype.fromField),
		k = q.toField || (q.toField = this.model.prototype.toField),
		n = q.ignoreDepKey,
		a = q.addDepKey;
		var v = this.getGroupTopTasks(o.getTopParent(true), q.targetGroup),
		m = v[0],
		f = v[1];
		if (m === o && f === A && o.isLeaf() && A.isLeaf()) {
			return this.hasTransitiveDependency(h, c, d)
		}
		if (!q.hasOwnProperty("ignoreDepKey")) {
			q.ignoreDepKey = n = d && this.getKeyByDeps(d, B, k) || "";
			q.addDepKey = a = u && this.getKeyByDeps(u, B, k) || ""
		}
		var e = m.getInternalId() + "-" + f.getInternalId() + "-" + n + "-" + a;
		if (this.isCachedResultAvailable("groupsHasTransitiveDependency", e)) {
			return this.methodsCache.groupsHasTransitiveDependency[e]
		}
		if (f !== q.targetTopParent) {
			q.targetTopParent = f;
			x = q.targets = {};
			f.cascadeBy(function (i) {
				x[i.getInternalId()] = true
			})
		}
		var w = q.extraSuccessors;
		if (u && !w) {
			w = q.extraSuccessors = {};
			for (var t = 0, p = u.length; t < p; t++) {
				var g = u[t];
				var s = g.getSourceId && g.getSourceId() || g[B];
				w[s] = w[s] || [];
				w[s].push(g)
			}
		}
		m.cascadeBy(function (E) {
			if (E !== r) {
				var H = E.getInternalId();
				if (z[H]) {
					return false
				}
				z[H] = true;
				var I = E.successors;
				if (w && w[H]) {
					I = I.concat(w[H])
				}
				for (var G = 0, D = I.length; G < D; G++) {
					var F = I[G],
					C = F.getTargetId && F.getTargetId() || F[k];
					if ((!d || Ext.Array.indexOf(d, F) == -1) && (x[C] || y.groupsHasTransitiveDependency(C, c, d, u, q))) {
						j = true;
						return false
					}
				}
			}
		});
		return this.setCachedResult("groupsHasTransitiveDependency", e, j)
	},
	getDependencyError : function (c, a, j, h, l, f) {
		var d,
		i,
		e;
		var b = c instanceof Gnt.model.Dependency;
		if (b) {
			d = c.getSourceId();
			i = this.getTaskById(d);
			h = a;
			l = j;
			if (h && Ext.Array.contains(h, c)) {
				h = Ext.Array.slice(h, 0);
				Ext.Array.remove(h, c)
			}
			j = c.getType();
			a = c.getTargetId();
			e = this.getTaskById(a);
			if (c.stores.length) {
				f = c
			}
		} else {
			d = c;
			i = this.getTaskById(d);
			e = this.getTaskById(a);
			if (j === undefined) {
				var g = this.model.prototype.fields.getByKey("Type").defaultValue;
				j = g !== undefined ? g : Gnt.model.Dependency.Type.EndToStart
			}
		}
		if (!f && b && !c.isValid()) {
			return -1
		} else {
			if (!d || !a || d == a) {
				return -1
			}
		}
		if (!i || !e) {
			return -2
		}
		if (!this.isValidDependencyType(j)) {
			return -10
		}
		if (i.contains(e) || e.contains(i)) {
			return -9
		}
		var k;
		if (l || f) {
			k = [];
			if (f) {
				k.push(f)
			}
			if (l) {
				k = k.concat(l)
			}
		}
		if (this.hasTransitiveDependency(d, a, k, h)) {
			return -3
		}
		if (this.hasTransitiveDependency(a, d, k, h)) {
			return -4
		}
		if (this.isPartOfTransitiveDependency(d, a, k, h)) {
			return -5
		}
		if (this.strictDependencyValidation) {
			if (this.groupsHasTransitiveDependency(e.getInternalId(), i.getInternalId(), k, h)) {
				return -7
			}
			if (this.groupsHasTransitiveDependency(i.getInternalId(), e.getInternalId(), k, h)) {
				return -8
			}
		}
		return 0
	},
	isValidDependencyType : function (b) {
		if (this.allowedDependencyTypes) {
			var a = false;
			Ext.each(this.allowedDependencyTypes, function (c) {
				if (Gnt.model.Dependency.Type[c] == b) {
					a = true;
					return false
				}
			});
			return a
		}
		return true
	},
	isValidDependency : function (f, b, d, a, c, e) {
		return !this.getDependencyError(f, b, d, a, c, e)
	},
	areTasksLinked : function (a, d) {
		var h = a instanceof Gnt.model.Task ? a : this.getTaskById(a);
		var j = d instanceof Gnt.model.Task ? d : this.getTaskById(d);
		if (!h || !j) {
			return false
		}
		var e = h.getInternalId() + "-" + j.getInternalId();
		if (this.isCachedResultAvailable("areTasksLinked", e)) {
			return this.methodsCache.areTasksLinked[e]
		}
		var g = {};
		var f = {};
		for (var c = 0, b = j.predecessors.length; c < b; c++) {
			g[j.predecessors[c].id] = true
		}
		for (c = 0, b = j.successors.length; c < b; c++) {
			f[j.successors[c].id] = true
		}
		for (c = 0, b = h.successors.length; c < b; c++) {
			if (g[h.successors[c].id]) {
				return this.setCachedResult("areTasksLinked", e, true)
			}
		}
		for (c = 0, b = h.predecessors.length; c < b; c++) {
			if (f[h.predecessors[c].id]) {
				return this.setCachedResult("areTasksLinked", e, true)
			}
		}
		return this.setCachedResult("areTasksLinked", e, false)
	},
	getByTaskIds : function (c, b) {
		var a = this.findBy(function (f) {
				var d = f.getTargetId(),
				e = f.getSourceId();
				if ((e === c && d === b) || (e === b && d === c)) {
					return true
				}
			});
		return this.getAt(a)
	},
	getTaskById : function (c) {
		var b = this.getTaskStore().getById(c);
		if (!b) {
			var a = this.getTaskStore().getRootNode();
			a.cascadeBy(function (d) {
				if (d !== a && d._phantomId == c) {
					b = d;
					return false
				}
			})
		}
		return b
	},
	getSourceTask : function (a) {
		var b = a instanceof Gnt.model.Dependency ? a.getSourceId() : a;
		return this.getTaskById(b)
	},
	getTargetTask : function (a) {
		var b = a instanceof Gnt.model.Dependency ? a.getTargetId() : a;
		return this.getTaskById(b)
	},
	getTaskStore : function () {
		return this.taskStore
	}
});
Ext.define("Gnt.data.TaskStore", {
	extend : "Ext.data.TreeStore",
	requires : ["Gnt.model.Task", "Gnt.data.Calendar", "Gnt.data.DependencyStore", "Gnt.patches.Tree"],
	mixins : ["Sch.data.mixin.FilterableTreeStore", "Sch.data.mixin.EventStore"],
	model : "Gnt.model.Task",
	calendar : null,
	dependencyStore : null,
	resourceStore : null,
	assignmentStore : null,
	weekendsAreWorkdays : false,
	cascadeChanges : false,
	batchSync : true,
	recalculateParents : true,
	skipWeekendsDuringDragDrop : true,
	cascadeDelay : 0,
	moveParentAsGroup : true,
	enableDependenciesForParentTasks : true,
	availabilitySearchLimit : 1825,
	cascading : false,
	isFillingRoot : false,
	isSettingRoot : false,
	earlyStartDates : null,
	earlyEndDates : null,
	lateStartDates : null,
	lateEndDates : null,
	lastTotalTimeSpan : null,
	suspendAutoRecalculateParents : 0,
	suspendAutoCascade : 0,
	currentCascadeBatch : null,
	batchCascadeLevel : 0,
	fillTasksWithDepInfoCounter : 0,
	fillTasksWithAssignmentInfoCounter : 0,
	dependenciesCalendar : "project",
	cachedAssignments : null,
	constructor : function (c) {
		this.addEvents("filter", "clearfilter", "beforecascade", "cascade");
		c = c || {};
		if (!c.calendar) {
			var a = {};
			if (c.hasOwnProperty("weekendsAreWorkdays")) {
				a.weekendsAreWorkdays = c.weekendsAreWorkdays
			} else {
				if (this.self.prototype.hasOwnProperty("weekendsAreWorkdays") && this.self != Gnt.data.TaskStore) {
					a.weekendsAreWorkdays = this.weekendsAreWorkdays
				}
			}
			c.calendar = new Gnt.data.Calendar(a)
		}
		var b = c.dependencyStore || this.dependencyStore || Ext.create("Gnt.data.DependencyStore");
		delete c.dependencyStore;
		this.setDependencyStore(b);
		var d = c.resourceStore || this.resourceStore || Ext.create("Gnt.data.ResourceStore");
		delete c.resourceStore;
		this.setResourceStore(d);
		var f = c.assignmentStore || this.assignmentStore || Ext.create("Gnt.data.AssignmentStore", {
				resourceStore : d
			});
		delete c.assignmentStore;
		this.setAssignmentStore(f);
		var e = c.calendar;
		if (e) {
			delete c.calendar;
			this.setCalendar(e, true)
		}
		this.resetEarlyDates();
		this.resetLateDates();
		this.mixins.observable.constructor.call(this);
		this.on({
			beforefill : this.onRootBeforeFill,
			fillcomplete : this.onRootFillEnd,
			remove : this.onTaskDeleted,
			write : this.onTaskStoreWrite,
			sort : this.onSorted,
			load : this.onTasksLoaded,
			rootchange : this.onTasksLoaded,
			scope : this
		});
		this.callParent([c]);
		this.fillTasksWithDepInfo();
		if (this.autoSync) {
			if (this.batchSync) {
				this.sync = Ext.Function.createBuffered(this.sync, 500)
			} else {
				this.on("beforesync", this.onTaskStoreBeforeSync, this)
			}
		}
		this.initTreeFiltering();
		this.treeStore = this
	},
	onTasksLoaded : function () {
		this.fillTasksWithDepInfoCounter = 1;
		this.fillTasksWithAssignmentInfoCounter = 1;
		this.fillTasksWithDepInfo()
	},
	load : function (a) {
		this.un("remove", this.onTaskDeleted, this);
		this.callParent(arguments);
		this.on("remove", this.onTaskDeleted, this)
	},
	loadData : function (E, y) {
		var C = this,
		t = C.getRootNode(),
		c = y ? y.addRecords : false,
		v = y ? y.syncStore : false;
		C.suspendAutoSync();
		C.suspendEvents();
		if (!c && t) {
			t.removeAll()
		}
		if (!C.getRootNode()) {
			t = C.setRootNode()
		}
		if (E.length) {
			var f = E.length,
			e = C.model,
			o = [],
			w = (typeof E[0].get === "function"),
			b,
			r,
			p,
			z,
			B,
			D,
			x,
			n,
			h,
			q;
			var A = C.sortNewNodesByIndex(E);
			for (var u = 0; u < f; u++) {
				r = C.getById(E[u].getId ? E[u].getId() : E[u].Id);
				q = false;
				b = 0;
				if (r) {
					z = w ? E[u].get("parentId") : E[u].parentId;
					B = r.parentNode.getId();
					D = w ? E[u].get("index") : E[u].index;
					x = r.get("index");
					if (((typeof z !== "undefined" || z === null) ? (z !== B) : false) || (typeof D !== "undefined" ? (D !== x) : false)) {
						n = z === null ? t : C.getById(z);
						h = B === null ? t : C.getById(B);
						if (n && (n.get("parentId") === r.getId()) && C.selfChildInRecordsData(r.getId(), z, A)) {
							q = true
						}
					} else {
						b = 1
					}
				} else {
					r = w ? new e(E[u].data) : new e(E[u]);
					B = r.get("parentId");
					if (B) {
						n = C.getById(B)
					} else {
						if (B === null) {
							n = t
						}
					}
				}
				if (!q) {
					if (w) {
						r.set(E[u].data)
					} else {
						r.set(E[u])
					}
				} else {
					continue
				}
				if (n && !b) {
					C.moveChildren(r, n, h, A);
					C.fixNodeDates(r)
				} else {
					if (typeof n === "undefined" && !b) {
						p = {
							node : r,
							index : r.get("index") || 0,
							parentId : r.get("parentId")
						};
						o.push(p)
					} else {
						C.fixNodeDates(r)
					}
				}
				if (n && !v) {
					n.commit();
					r.commit();
					if (h) {
						h.commit()
					}
				}
			}
			var g = 0,
			m = 0,
			j = o.length,
			d,
			k;
			while (o.length) {
				if (g > o.length - 1) {
					g = 0;
					m = 1
				}
				d = o[g];
				k = d.parentId === null ? t : C.getById(d.parentId);
				if (k) {
					var a = C.nodeIsChild(d.node, n);
					if (a) {
						k.insertChild(d.index, d.node);
						C.fixNodeDates(d.node);
						o.splice(g, 1);
						if (!v) {
							k.commit();
							d.node.commit()
						}
						g -= 1
					}
				}
				g += 1;
				if (m && g === j - 1 && o.length === j) {
					throw "Invalid data, possible infinite loop."
				}
			}
			if (C.nodesToExpand) {
				u = 0;
				for (var s = C.nodesToExpand.length; u < s; u += 1) {
					r = C.nodesToExpand[u];
					if (r.childNodes && r.childNodes.length) {
						r.expand()
					}
				}
				delete C.nodesToExpand
			}
		}
		C.resumeAutoSync();
		C.resumeEvents();
		this.fireEvent("datachanged");
		this.fireEvent("refresh");
		if (v) {
			C.sync()
		}
		if (this.buffered) {}

	},
	selfChildInRecordsData : function (d, c, b) {
		var a = false;
		a = typeof b[c] === "undefined" ? true : b[c] === d;
		return a
	},
	sortNewNodesByIndex : function (c) {
		var b = {},
		a = function (d, e) {
			if (typeof d.get === "function") {
				return d.get(e)
			}
			return d[e]
		};
		Ext.Array.each(c, function (d) {
			b[a(d, "Id")] = a(d, "parentId")
		});
		Ext.Array.sort(c, function (e, i) {
			var h = a(e, "index"),
			g = a(i, "index"),
			f = a(e, "parentId"),
			d = a(i, "parentId");
			if (typeof h !== "undefined" && typeof g !== "undefined") {
				if (f === d) {
					return (h < g) ? -1 : (h > g) ? 1 : 0
				} else {
					if (f === null) {
						return 1
					} else {
						if (d === null) {
							return -1
						} else {
							return (f < d) ? -1 : 1
						}
					}
				}
			}
			return 0
		});
		return b
	},
	fixNodeDates : function (b) {
		var c = b.calculateDuration(b.getStartDate(), b.getEndDate(), b.getDurationUnit()),
		a;
		b.set({
			Duration : c
		});
		if (this.recalculateParents) {
			if (b.childNodes.length) {
				a = b.getChildAt(0);
				a.recalculateParents()
			} else {
				b.recalculateParents()
			}
		}
	},
	nodeIsChild : function (c, b) {
		var d = b.getId(),
		a = true;
		if (c.childNodes.length) {
			c.cascadeBy(function (e) {
				if (e.getId() === d) {
					a = false;
					return false
				}
			})
		}
		return a
	},
	moveChildren : function (e, d, c, f) {
		if (e.get("expanded")) {
			if (!this.nodesToExpand) {
				this.nodesToExpand = []
			}
			this.nodesToExpand.push(e);
			e.set("expanded", false)
		}
		var b,
		h = this.nodeIsChild(e, d),
		g = f ? !this.selfChildInRecordsData(e.getId(), d.getId(), f) : true,
		a = c || this.getById(e.get("parentId"));
		if (!h && g) {
			d.set("parentId", null);
			this.moveChildren(d, this.getRootNode(), e)
		}
		if (h || g) {
			if (e.childNodes.length) {
				b = e.copy(null, true);
				e.removeAll()
			}
			if (a && a.getId() !== d.getId()) {
				a.removeChild(e)
			}
			typeof e.get("index") !== "undefined" ? d.insertChild(e.get("index"), e) : d.appendChild(e);
			if (b) {
				b.cascadeBy(function (j) {
					if (j !== b) {
						var i = j.copy(null);
						i.get("index") ? e.insertChild(i.get("index"), i) : e.appendChild(i)
					}
				})
			}
			this.fixNodeDates(e)
		}
	},
	setRootNode : function () {
		var b = this;
		this.isSettingRoot = true;
		this.tree.setRootNode = Ext.Function.createInterceptor(this.tree.setRootNode, function (c) {
				Ext.apply(c, {
					calendar : b.calendar,
					taskStore : b,
					dependencyStore : b.dependencyStore,
					phantom : false,
					dirty : false
				})
			});
		var a = this.callParent(arguments);
		this.isSettingRoot = false;
		delete this.tree.setRootNode;
		return a
	},
	onRootBeforeFill : function (b, a) {
		if (a.isRoot()) {
			this.cachedAssignments = this.fillAssignmentsCache()
		}
		this.isFillingRoot = true;
		this.un({
			append : this.onNodeUpdated,
			insert : this.onNodeUpdated,
			update : this.onTaskUpdated,
			scope : this
		})
	},
	onRootFillEnd : function (b, a) {
		if (a.isRoot()) {
			this.cachedAssignments = null
		}
		a.normalizeParent();
		this.on({
			append : this.onNodeUpdated,
			insert : this.onNodeUpdated,
			update : this.onTaskUpdated,
			scope : this
		});
		this.isFillingRoot = false;
		if (Ext.data.reader.Xml && this.proxy.reader instanceof Ext.data.reader.Xml) {
			this.lazyFill = true
		}
	},
	getDependencyStore : function () {
		return this.dependencyStore
	},
	fillTasksWithDepInfo : function () {
		if (!this.tree || !this.tree.nodeHash) {
			return
		}
		var a = this.getDependencyStore();
		if (this.fillTasksWithDepInfoCounter++ > 0) {
			this.forEachTaskUnOrdered(function (b) {
				b.successors = [];
				b.predecessors = []
			})
		}
		if (a) {
			a.each(function (b) {
				var d = b.getSourceTask(),
				c = b.getTargetTask();
				if (d && c) {
					d.successors.push(b);
					c.predecessors.push(b)
				}
			})
		}
	},
	setDependencyStore : function (a) {
		var b = {
			add : this.onDependencyAddOrUpdate,
			update : this.onDependencyAddOrUpdate,
			remove : this.onDependencyDelete,
			scope : this
		};
		if (this.dependencyStore) {
			this.dependencyStore.un(b)
		}
		if (a) {
			this.dependencyStore = Ext.StoreMgr.lookup(a);
			if (a) {
				a.taskStore = this;
				a.on(b);
				this.fillTasksWithDepInfo()
			}
		} else {
			this.dependencyStore = null
		}
	},
	setResourceStore : function (a) {
		if (a) {
			this.resourceStore = Ext.StoreMgr.lookup(a);
			a.taskStore = this;
			a.normalizeResources()
		} else {
			this.resourceStore = null
		}
	},
	getResourceStore : function () {
		return this.resourceStore || null
	},
	fillAssignmentsCache : function () {
		var b = this.getAssignmentStore(),
		a = {};
		if (this.fillTasksWithAssignmentInfoCounter++ > 0) {
			this.forEachTaskUnOrdered(function (c) {
				c.assignments = []
			})
		}
		if (b) {
			b.each(function (c) {
				var d = c.getTaskId();
				a[d] ? a[d].push(c) : a[d] = [c]
			})
		}
		return a
	},
	fillTasksWithAssignmentInfo : function () {
		if (!this.tree || !this.tree.nodeHash) {
			return
		}
		var a = this.getAssignmentStore();
		if (this.fillTasksWithAssignmentInfoCounter++ > 0) {
			this.forEachTaskUnOrdered(function (b) {
				b.assignments = []
			})
		}
		if (a) {
			a.each(function (c) {
				var b = c.getTask();
				b && b.assignments.push(c)
			})
		}
	},
	setAssignmentStore : function (b) {
		var a = {
			add : this.onAssignmentStructureMutation,
			update : this.onAssignmentMutation,
			remove : this.onAssignmentStructureMutation,
			scope : this
		};
		if (this.assignmentStore) {
			this.assignmentStore.un(a)
		}
		if (b) {
			this.assignmentStore = Ext.StoreMgr.lookup(b);
			b.taskStore = this;
			b.on(a);
			this.fillTasksWithAssignmentInfo()
		} else {
			this.assignmentStore = null
		}
	},
	getAssignmentStore : function () {
		return this.assignmentStore || null
	},
	renormalizeTasks : function (c, b) {
		this.resetEarlyDates();
		this.resetLateDates();
		if (b instanceof Gnt.model.Task) {
			b.adjustToCalendar()
		} else {
			var a = this.getRootNode();
			if (a) {
				a.cascadeBy(function (d) {
					d.adjustToCalendar()
				})
			}
		}
	},
	getCalendar : function () {
		return this.calendar || null
	},
	setCalendar : function (d, b) {
		var c = {
			calendarchange : this.renormalizeTasks,
			scope : this
		};
		if (this.calendar) {
			this.calendar.un(c)
		}
		this.calendar = d;
		if (d) {
			d.on(c);
			var a = this.tree && this.getRootNode();
			if (a) {
				a.calendar = d
			}
			if (!b) {
				this.renormalizeTasks()
			}
		}
	},
	getCriticalPaths : function () {
		var b = this.getRootNode(),
		a = [],
		d = new Date(0);
		b.cascadeBy(function (e) {
			d = Sch.util.Date.max(e.getEndDate(), d)
		});
		b.cascadeBy(function (e) {
			if (d - e.getEndDate() === 0 && !e.isRoot() && !(!e.isLeaf() && e.childNodes.length)) {
				a.push(e)
			}
		});
		var c = [];
		Ext.each(a, function (e) {
			c.push(e.getCriticalPaths())
		});
		return c
	},
	onNodeUpdated : function (b, c) {
		if (!c.isRoot()) {
			if (this.lastTotalTimeSpan) {
				var a = this.getTotalTimeSpan();
				if (c.getEndDate() > a.end || c.getStartDate() < a.start) {
					this.lastTotalTimeSpan = null
				}
			}
			if (c.getEndDate() - this.getProjectEndDate() === 0) {
				this.resetLateDates()
			}
			if (!this.cascading && this.recalculateParents) {
				c.recalculateParents()
			}
		}
	},
	getViolatedConstraints : function (a) {
		var c = this,
		b = 0,
		d = [];
		this.dependencyStore.each(function (f) {
			var h = f.getSourceTask();
			var g = f.getTargetTask();
			if (h && g) {
				var e = g.getViolatedConstraints();
				if (e) {
					b++;
					d.push(e)
				}
				if (a && (b >= a)) {
					return false
				}
			}
		});
		return d
	},
	onTaskUpdated : function (c, b, a) {
		var f = b.previous;
		if (this.lastTotalTimeSpan) {
			var d = this.getTotalTimeSpan();
			if (f && (f[b.endDateField] - d.end === 0 || f[b.startDateField] - d.start === 0) || (b.getEndDate() > d.end || b.getStartDate() < d.start)) {
				this.lastTotalTimeSpan = null
			}
		}
		if (!this.cascading && a !== Ext.data.Model.COMMIT && f) {
			var g = b.percentDoneField in f;
			if (b.startDateField in f || b.endDateField in f || "parentId" in f || b.effortField in f || f[b.schedulingModeField] === "Manual") {
				var e = b;
				if (this.cascadeChanges && !this.suspendAutoCascade) {
					if (f[e.schedulingModeField] == "Manual") {
						var h = e.getIncomingDependencies(true);
						if (h.length) {
							e = h[0].getSourceTask()
						}
					}
					Ext.Function.defer(this.cascadeChangesForTask, this.cascadeDelay, this, [e])
				} else {
					this.resetEarlyDates();
					this.resetLateDates()
				}
				g = true
			} else {
				if (f[b.schedulingModeField] && b.isManuallyScheduled()) {
					this.resetEarlyDates();
					this.resetLateDates()
				}
			}
			if (g && this.recalculateParents && !this.suspendAutoRecalculateParents) {
				b.recalculateParents()
			}
		}
	},
	startBatchCascade : function () {
		if (!this.batchCascadeLevel) {
			this.currentCascadeBatch = {
				nbrAffected : 0,
				affected : {},
				visitedCounters : {},
				addVisited : function (b) {
					var a = b.internalId;
					if (!this.visitedCounters[a]) {
						this.visitedCounters[a] = 1
					} else {
						this.visitedCounters[a]++
					}
				},
				addAffected : function (b) {
					var a = b.internalId;
					if (!this.affected[a]) {
						this.affected[a] = b;
						this.nbrAffected++
					}
				}
			}
		}
		this.batchCascadeLevel++;
		return this.currentCascadeBatch
	},
	endBatchCascade : function () {
		this.batchCascadeLevel--;
		if (!this.batchCascadeLevel) {
			var a = this.currentCascadeBatch;
			this.currentCascadeBatch = null;
			if (a.nbrAffected > 0) {
				this.resetEarlyDates();
				this.resetLateDates()
			}
			if (this.cascading) {
				this.cascading = false;
				this.fireEvent("cascade", this, a)
			}
		}
	},
	cascadeChangesForTask : function (a, e) {
		var b = this.currentCascadeBatch;
		if (b && b.visitedCounters[a.internalId] > a.predecessors.length) {
			return {
				nbrAffected : 0,
				affected : {}

			}
		}
		this.startBatchCascade();
		var d = this,
		c = {
			nbrAffected : 0,
			affected : {}

		};
		Ext.each(a.getOutgoingDependencies(true), function (f) {
			var g = f.getTargetTask();
			if (g) {
				if (!d.cascading) {
					d.fireEvent("beforecascade", d);
					d.cascading = true
				}
				g.cascadeChanges(d, c, f)
			}
		});
		if (d.cascading) {
			if (!e && d.recalculateParents) {
				d.recalculateAffectedParents(c.affected)
			}
		}
		this.endBatchCascade();
		return c
	},
	getParentsContext : function () {
		return {
			array : [],
			byInternalId : {}

		}
	},
	addTaskToParentsContext : function (e, a) {
		var b = e.byInternalId;
		var d = e.array;
		var c = a.isLeaf() ? a.parentNode : a;
		while (c) {
			if (b[c.internalId]) {
				break
			}
			b[c.internalId] = c;
			d.push(c);
			c = c.parentNode
		}
	},
	recalculateAffectedParents : function (c, e) {
		e = e || this.getParentsContext();
		this.suspendAutoCascade++;
		this.suspendAutoRecalculateParents++;
		var b = this;
		Ext.Object.each(c, function (g, f) {
			b.addTaskToParentsContext(e, f)
		});
		var d = e.array;
		d.sort(function (g, f) {
			return g.data.depth - f.data.depth
		});
		var a;
		if (this.recalculateParents) {
			for (a = d.length - 1; a >= 0; a--) {
				d[a].refreshCalculatedParentNodeData()
			}
		}
		if (this.cascadeChanges) {
			for (a = 0; a < d.length; a++) {
				this.cascadeChangesForTask(d[a])
			}
		}
		this.suspendAutoRecalculateParents--;
		this.suspendAutoCascade--
	},
	removeTaskDependencies : function (a) {
		var b = this.dependencyStore,
		c = a.getAllDependencies(b);
		if (c.length) {
			b.remove(c)
		}
	},
	removeTaskAssignments : function (b) {
		var c = this.getAssignmentStore(),
		a = b.getAssignments();
		if (a.length) {
			c.remove(a)
		}
	},
	onTaskDeleted : function (f, e, b) {
		var c = this.dependencyStore;
		if (c && !e.isReplace && !b) {
			e.cascadeBy(this.removeTaskDependencies, this)
		}
		var h = this.getAssignmentStore();
		if (h && !e.isReplace && !b) {
			h.fireEvent("beforetaskassignmentschange", h, e.getInternalId(), []);
			e.cascadeBy(this.removeTaskAssignments, this);
			h.fireEvent("taskassignmentschanged", h, e.getInternalId(), [])
		}
		var d = this.getTotalTimeSpan();
		var a = e.getStartDate();
		var g = e.getEndDate();
		if (g - d.end === 0 || a - d.start === 0) {
			this.lastTotalTimeSpan = null
		}
		this.resetEarlyDates();
		this.resetLateDates()
	},
	onAssignmentMutation : function (c, a) {
		var b = this;
		Ext.each(a, function (e) {
			var d = e.getTask(b);
			if (d) {
				d.onAssignmentMutation(e)
			}
		})
	},
	onAssignmentStructureMutation : function (c, a) {
		var b = this;
		Ext.each(a, function (e) {
			var d = e.getTask(b);
			if (d) {
				d.onAssignmentStructureMutation(e)
			}
		})
	},
	onDependencyAddOrUpdate : function (b, d) {
		this.resetEarlyDates();
		this.resetLateDates();
		if (this.cascadeChanges) {
			var c = this,
			a;
			Ext.each(d, function (e) {
				a = e.getTargetTask();
				if (a) {
					a.constrain(c)
				}
			})
		}
	},
	onDependencyDelete : function (a, b) {
		this.resetEarlyDates();
		this.resetLateDates()
	},
	getNewRecords : function () {
		return Ext.Array.filter(this.tree.flatten(), this.filterNew, this)
	},
	getUpdatedRecords : function () {
		return Ext.Array.filter(this.tree.flatten(), this.filterUpdated, this)
	},
	filterNew : function (a) {
		return a.phantom && a.isValid() && a != this.tree.root
	},
	filterUpdated : function (a) {
		return a.dirty && !a.phantom && a.isValid() && a != this.tree.root
	},
	onTaskStoreBeforeSync : function (b, c) {
		var a = b.create;
		if (a) {
			for (var e, d = a.length - 1; d >= 0; d--) {
				e = a[d];
				if (!e.isPersistable()) {
					Ext.Array.remove(a, e)
				}
			}
			if (a.length === 0) {
				delete b.create
			}
		}
		return Boolean((b.create && b.create.length > 0) || (b.update && b.update.length > 0) || (b.destroy && b.destroy.length > 0))
	},
	onTaskStoreWrite : function (c, b) {
		var d = this.dependencyStore;
		if (!d || b.action !== "create") {
			return
		}
		var a = b.getRecords(),
		e;
		Ext.each(a, function (f) {
			e = f.getId();
			if (!f.phantom && e !== f._phantomId) {
				Ext.each(d.getNewRecords(), function (g) {
					var i = g.getSourceId();
					var h = g.getTargetId();
					if (i === f._phantomId) {
						g.setSourceId(e)
					} else {
						if (h === f._phantomId) {
							g.setTargetId(e)
						}
					}
				});
				Ext.each(f.childNodes, function (g) {
					if (g.phantom) {
						g.set("parentId", e)
					}
				});
				delete f._phantomId
			}
		})
	},
	forEachTaskUnOrdered : function (c, b) {
		var e = this.tree.nodeHash;
		var a = this.getRootNode();
		for (var d in e) {
			if (e[d] !== a) {
				if (c.call(b || this, e[d]) === false) {
					return false
				}
			}
		}
	},
	getTasksTimeSpan : function (d) {
		var a = new Date(9999, 0, 1),
		b = new Date(0);
		var c = function (f) {
			var e = f.getStartDate();
			var g = f.getEndDate();
			if (e && e < a) {
				a = e
			}
			if (e && g && g > b) {
				b = g
			}
		};
		if (d) {
			if (!Ext.isArray(d)) {
				d = [d]
			}
			Ext.Array.each(d, c)
		} else {
			this.forEachTaskUnOrdered(c)
		}
		a = a < new Date(9999, 0, 1) ? a : null;
		b = b > new Date(0) ? b : null;
		return {
			start : a,
			end : b || (a && Ext.Date.add(a, Ext.Date.DAY, 1)) || null
		}
	},
	getTotalTimeSpan : function () {
		if (this.lastTotalTimeSpan) {
			return this.lastTotalTimeSpan
		}
		this.lastTotalTimeSpan = this.getTasksTimeSpan();
		return this.lastTotalTimeSpan
	},
	getProjectStartDate : function () {
		return this.getTotalTimeSpan().start
	},
	getProjectEndDate : function () {
		return this.getTotalTimeSpan().end
	},
	getCount : function (b) {
		var a = b === false ? 0 : -1;
		this.getRootNode().cascadeBy(function () {
			a++
		});
		return a
	},
	toArray : function () {
		var a = [];
		this.getRootNode().cascadeBy(function (b) {
			a.push(b)
		});
		return a
	},
	remove : function (a) {
		Ext.each(a, function (b) {
			b.remove()
		})
	},
	indent : function (a) {
		this.fireEvent("beforeindentationchange", this, a);
		a = Ext.isArray(a) ? a.slice() : [a];
		a.sort(function (d, c) {
			return d.data.index - c.data.index
		});
		this.suspendEvents(true);
		Ext.each(a, function (b) {
			b.indent()
		});
		this.resumeEvents();
		this.fireEvent("indentationchange", this, a)
	},
	outdent : function (a) {
		this.fireEvent("beforeindentationchange", this, a);
		a = Ext.isArray(a) ? a.slice() : [a];
		a.sort(function (d, c) {
			return c.data.index - d.data.index
		});
		this.suspendEvents(true);
		Ext.each(a, function (b) {
			b.outdent()
		});
		this.resumeEvents();
		this.fireEvent("indentationchange", this, a)
	},
	getTasksForResource : function (a) {
		return a.getTasks()
	},
	getEventsForResource : function (a) {
		return this.getTasksForResource(a)
	},
	indexOf : function (a) {
		return a && this.tree.getNodeById(a.internalId) ? 0 : -1
	},
	getByInternalId : function (a) {
		return this.tree.getNodeById(a)
	},
	queryBy : function (b, a) {
		var d = [];
		var c = this;
		this.getRootNode().cascadeBy(function (e) {
			if (b.call(a || c, e)) {
				d.push(e)
			}
		});
		return d
	},
	onSorted : function () {
		if (this.lastTreeFilter) {
			this.filterTreeBy(this.lastTreeFilter)
		}
	},
	append : function (a) {
		this.getRootNode().appendChild(a)
	},
	resetEarlyDates : function () {
		this.earlyStartDates = {};
		this.earlyEndDates = {};
		this.fireEvent("resetearlydates")
	},
	resetLateDates : function () {
		this.lateStartDates = {};
		this.lateEndDates = {};
		this.fireEvent("resetlatedates")
	},
	getBySequenceNumber : function (a) {
		return this.getRootNode().getBySequenceNumber(a)
	},
	destroy : function () {
		this.setCalendar(null);
		this.setAssignmentStore(null);
		this.setDependencyStore(null);
		this.setResourceStore(null);
		this.callParent(arguments)
	}
}, function () {
	this.override(Sch.data.mixin.FilterableTreeStore.prototype.inheritables() || {})
});
Ext.define("Gnt.data.ResourceStore", {
	requires : ["Gnt.model.Resource"],
	extend : "Sch.data.ResourceStore",
	model : "Gnt.model.Resource",
	taskStore : null,
	constructor : function () {
		this.mixins.observable.constructor.call(this);
		this.on({
			load : this.normalizeResources,
			remove : this.onResourceRemoved,
			scope : this
		});
		this.callParent(arguments)
	},
	normalizeResources : function () {
		this.each(function (b) {
			if (!b.normalized) {
				var a = b.getCalendarId();
				if (a) {
					b.setCalendarId(a, true)
				}
				b.normalized = true
			}
		})
	},
	onResourceRemoved : function (a, b) {
		var c = this.getAssignmentStore();
		c.removeAssignmentsForResource(b)
	},
	getTaskStore : function () {
		return this.taskStore || null
	},
	getAssignmentStore : function () {
		return this.assignmentStore = (this.assignmentStore || this.getTaskStore().getAssignmentStore())
	},
	getByInternalId : function (a) {
		return this.data.getByKey(a) || this.getById(a)
	}
});
Ext.define("Gnt.data.AssignmentStore", {
	extend : "Ext.data.Store",
	requires : ["Gnt.model.Assignment"],
	model : "Gnt.model.Assignment",
	ignoreInitial : true,
	isLoadingRecords : false,
	isRemovingAll : false,
	taskStore : null,
	constructor : function () {
		this.mixins.observable.constructor.apply(this, arguments);
		this.init();
		this.callParent(arguments);
		this.ignoreInitial = false
	},
	init : function () {
		this.on({
			add : this.onAssignmentAdd,
			update : this.onAssignmentUpdate,
			load : this.onAssignmentsLoad,
			datachanged : this.onAssignmentDataChanged,
			remove : this.onAssignmentRemove,
			scope : this
		})
	},
	onAssignmentsLoad : function () {
		var a = this.getTaskStore();
		a && a.fillTasksWithAssignmentInfo()
	},
	onAssignmentDataChanged : function () {
		var a = this.getTaskStore();
		if (a && (this.isLoadingRecords || this.isRemovingAll)) {
			a.fillTasksWithAssignmentInfo()
		}
	},
	removeAll : function () {
		this.isRemovingAll = true;
		this.callParent(arguments);
		this.isRemovingAll = false
	},
	loadRecords : function () {
		this.isLoadingRecords = true;
		this.callParent(arguments);
		this.isLoadingRecords = false
	},
	onAssignmentAdd : function (d, b) {
		if (this.ignoreInitial) {
			return
		}
		for (var c = 0; c < b.length; c++) {
			var e = b[c];
			var a = e.getTask();
			a && a.assignments.push(e)
		}
	},
	onAssignmentRemove : function (c, d) {
		var b = this.getTaskStore();
		if (!b) {
			return
		}
		var a = d.getTask(b);
		a && Ext.Array.remove(a.assignments, d)
	},
	onAssignmentUpdate : function (f, g, b) {
		if (b != Ext.data.Model.COMMIT) {
			var c = this.getTaskStore();
			if (!c) {
				return
			}
			var e = g.previous;
			var a = g.getTask();
			if (e && g.taskIdField in e) {
				var d = c.getById(e[g.taskIdField]);
				d && Ext.Array.remove(d.assignments, g);
				a && a.assignments.push(g)
			}
		}
	},
	getTaskStore : function () {
		return this.taskStore
	},
	getResourceStore : function () {
		return this.getTaskStore().resourceStore
	},
	getByInternalId : function (a) {
		return this.data.getByKey(a) || this.getById(a)
	},
	removeAssignmentsForResource : function (b) {
		var c = b.getId();
		if (c) {
			var a = this.queryBy(function (d) {
					return d.getResourceId() === c
				}).items;
			this.remove(a)
		}
	}
});
Ext.define("Gnt.patches.IETreeStore", {
	extend : "Sch.util.Patch",
	requires : ["Gnt.data.TaskStore"],
	target : "Gnt.data.TaskStore",
	ieOnly : true,
	overrides : {
		onNodeAdded : function (c, e) {
			var d = this,
			b = d.getProxy(),
			a = b.getReader(),
			f = e.raw || e[e.persistenceProperty],
			g;
			Ext.Array.remove(d.removed, e);
			e.join(d);
			if (!e.isLeaf()) {
				g = a.getRoot(f);
				if (g) {
					d.fillNode(e, a.extractData(g));
					if (f[a.root]) {
						delete f[a.root]
					}
				}
			}
			if (d.autoSync && !d.autoSyncSuspended && (e.phantom || e.dirty)) {
				d.sync()
			}
		}
	}
});
Ext.define("Gnt.template.Template", {
	extend : "Ext.XTemplate",
	isLegacyIE : Ext.isIE8m || Ext.isIEQuirks,
	getInnerTpl : Ext.emptyFn,
	innerTpl : null,
	dependencyTerminalMarkup : '<div class="sch-gantt-terminal sch-gantt-terminal-start"></div><div class="sch-gantt-terminal sch-gantt-terminal-end"></div>',
	constructor : function (a) {
		Ext.apply(this, a);
		var c = a.rtl ? "right" : "left";
		var b = this.getInnerTpl(a) || "";
		this.callParent(['<div class="sch-event-wrap {ctcls} ' + Ext.baseCSSPrefix + 'unselectable" style="' + c + ':{offset}px">' + (a.leftLabel ? '<div class="sch-gantt-labelct sch-gantt-labelct-left"><label class="sch-gantt-label sch-gantt-label-left">{leftLabel}</label></div>' : "") + (a.rightLabel ? '<div class="sch-gantt-labelct sch-gantt-labelct-right" style="left:{width}px"><label class="sch-gantt-label sch-gantt-label-right">{rightLabel}</label></div>' : "") + (a.topLabel ? '<div class="sch-gantt-labelct sch-gantt-labelct-top"><label class="sch-gantt-label sch-gantt-label-top">{topLabel}</label></div>' : "") + b + (a.bottomLabel ? '<div class="sch-gantt-labelct sch-gantt-labelct-bottom"><label class="sch-gantt-label sch-gantt-label-bottom">{bottomLabel}</label></div>' : "") + "</div>", {
					compiled : true,
					disableFormats : true
				}
			])
	}
});
Ext.define("Gnt.template.Task", {
	extend : "Gnt.template.Template",
	innerTpl : '<div class="sch-gantt-progress-bar" style="width:{percentDone}%;{progressBarStyle}" unselectable="on">&#160;</div>',
	getInnerTpl : function (a) {
		var b = a.rtl ? "right" : "left";
		return '<div id="' + a.prefix + '{id}" class="sch-gantt-item sch-gantt-task-bar {cls}" unselectable="on" style="width:{width}px;{style}">' + ((a.resizeHandles === "both" || a.resizeHandles === "left") ? '<div class="sch-resizable-handle sch-gantt-task-handle sch-resizable-handle-start sch-resizable-handle-west"></div>' : "") + this.innerTpl + ((a.resizeHandles === "both" || a.resizeHandles === "right") ? '<div class="sch-resizable-handle sch-gantt-task-handle sch-resizable-handle-end sch-resizable-handle-east"></div>' : "") + (a.enableProgressBarResize ? '<div style="' + b + ':{percentDone}%" class="sch-gantt-progressbar-handle"></div>' : "") + (a.enableDependencyDragDrop ? this.dependencyTerminalMarkup : "") + "</div>"
	}
});
Ext.define("Gnt.template.Milestone", {
	extend : "Gnt.template.Template",
	innerTpl : (Ext.isIE8m || Ext.isIEQuirks ? ('<div style="border-width:{[Math.floor(values.side*0.7)]}px" class="sch-gantt-milestone-diamond-top {cls}" unselectable="on" style="{style}"></div><div style="border-width:{[Math.floor(values.side*0.7)]}px" class="sch-gantt-milestone-diamond-bottom {cls}" unselectable="on" style="{style}"></div>') : ('<img style="{[values.print ? "height:" + values.side + "px;border-left-width:" + values.side + "px" : ""]};{style}" src="' + Ext.BLANK_IMAGE_URL + '" class="sch-gantt-milestone-diamond {cls}" unselectable="on"/>')),
	getInnerTpl : function (a) {
		return "<div " + (this.isLegacyIE ? 'style="width:{[Math.floor(values.side*0.7)]}px"' : "") + ' id="' + a.prefix + '{id}" class="sch-gantt-item sch-gantt-milestone-diamond-ct">' + this.innerTpl + (a.enableDependencyDragDrop ? this.dependencyTerminalMarkup : "") + "</div>"
	}
});
Ext.define("Gnt.template.ParentTask", {
	extend : "Gnt.template.Template",
	innerTpl : '<div class="sch-gantt-progress-bar" style="width:{percentDone}%;{progressBarStyle}">&#160;</div><div class="sch-gantt-parenttask-arrow sch-gantt-parenttask-leftarrow"></div><div class="sch-gantt-parenttask-arrow sch-gantt-parenttask-rightarrow"></div>',
	getInnerTpl : function (a) {
		return '<div id="' + a.prefix + '{id}" class="sch-gantt-item sch-gantt-parenttask-bar {cls}" style="width:{width}px; {style}">' + this.innerTpl + (a.enableDependencyDragDrop ? this.dependencyTerminalMarkup : "") + "</div>"
	}
});
Ext.define("Gnt.Tooltip", {
	extend : "Ext.ToolTip",
	alias : "widget.gantt_task_tooltip",
	requires : ["Ext.Template"],
	mixins : ["Gnt.mixin.Localizable"],
	mode : "startend",
	cls : "sch-tip",
	height : 40,
	autoHide : false,
	anchor : "b-tl",
	maskOnDisable : false,
	startEndTemplate : null,
	durationTemplate : null,
	initComponent : function () {
		this.rtl = this.gantt.rtl;
		if (this.mode === "startend" && !this.startEndTemplate) {
			this.startEndTemplate = new Ext.Template('<div class="sch-timetipwrap {cls}"><div>' + this.L("startText") + "{startText}</div><div>" + this.L("endText") + "{endText}</div></div>").compile()
		}
		if (this.mode === "duration" && !this.durationTemplate) {
			this.durationTemplate = new Ext.Template('<div class="sch-timetipwrap {cls}">', "<div>" + this.L("startText") + " {startText}</div>", "<div>" + this.L("durationText") + " {duration} {unit}</div>", "</div>").compile()
		}
		this.callParent(arguments)
	},
	update : function (e, b, d, a) {
		var c;
		if (this.mode === "duration") {
			c = this.getDurationContent(e, b, d, a)
		} else {
			c = this.getStartEndContent(e, b, d, a)
		}
		this.callParent([c])
	},
	getStartEndContent : function (h, b, f, a) {
		var c = this.gantt,
		e = c.getFormattedDate(h),
		d = e;
		if (b - h > 0) {
			d = c.getFormattedEndDate(b, h)
		}
		var g = {
			cls : f ? "sch-tip-ok" : "sch-tip-notok",
			startText : e,
			endText : d,
			task : a
		};
		return this.startEndTemplate.apply(g)
	},
	getDurationContent : function (f, b, d, a) {
		var c = a.getDurationUnit() || Sch.util.Date.DAY;
		var e = a.calculateDuration(f, b, c);
		return this.durationTemplate.apply({
			cls : d ? "sch-tip-ok" : "sch-tip-notok",
			startText : this.gantt.getFormattedDate(f),
			duration : parseFloat(Ext.Number.toFixed(e, 1)),
			unit : Sch.util.Date.getReadableNameOfUnit(c, e > 1),
			task : a
		})
	},
	show : function (a, b) {
		if (a) {
			this.setTarget(a)
		}
		this.callParent([]);
		if (b !== undefined) {
			this.setX(b)
		}
	}
});
Ext.define("Gnt.feature.TaskDragDrop", {
	extend : "Ext.dd.DragZone",
	requires : ["Gnt.Tooltip", "Ext.dd.StatusProxy"],
	useTooltip : true,
	tooltipConfig : null,
	validatorFn : function (a, b, d, c) {
		return true
	},
	validatorFnScope : null,
	showExactDropPosition : false,
	containerScroll : false,
	dropAllowed : "sch-gantt-dragproxy",
	dropNotAllowed : "sch-gantt-dragproxy",
	valid : false,
	gantt : null,
	onDragEnter : Ext.emptyFn,
	onDragOut : Ext.emptyFn,
	tip : null,
	constructor : function (c, b) {
		b = b || {};
		Ext.apply(this, b);
		if (Ext.isIE && (Ext.isIE8 || Ext.isIE7 || Ext.ieVersion < 9) && window.top !== window) {
			Ext.dd.DragDropManager.notifyOccluded = true
		}
		this.proxy = this.proxy || new Ext.dd.StatusProxy({
				shadow : false,
				dropAllowed : "sch-gantt-dragproxy",
				dropNotAllowed : "sch-gantt-dragproxy",
				ensureAttachedToBody : Ext.emptyFn
			});
		var d = this,
		a = d.gantt;
		if (d.useTooltip) {
			d.tip = new Gnt.Tooltip(Ext.apply({
						cls : "gnt-dragdrop-tip",
						gantt : a
					}, d.tooltipConfig))
		}
		d.callParent([c, Ext.apply(b, {
					ddGroup : a.id + "-task-dd"
				})]);
		d.scroll = false;
		d.isTarget = true;
		d.ignoreSelf = false;
		d.addInvalidHandleClass("sch-resizable-handle");
		d.addInvalidHandleClass(Ext.baseCSSPrefix + "resizable-handle");
		d.addInvalidHandleClass("sch-gantt-terminal");
		d.addInvalidHandleClass("sch-gantt-progressbar-handle");
		a.ownerCt.el.appendChild(this.proxy.el);
		a.on({
			destroy : d.destroy,
			scope : d
		})
	},
	destroy : function () {
		if (this.tip) {
			this.tip.destroy()
		}
		this.callParent(arguments)
	},
	autoOffset : function (a, b) {
		this.setDelta(0, 0)
	},
	setXConstraint : function (c, b, a) {
		this.leftConstraint = c;
		this.rightConstraint = b;
		this.minX = c;
		this.maxX = b;
		if (a) {
			this.setXTicks(this.initPageX, a)
		}
		this.constrainX = true
	},
	setYConstraint : function (a, c, b) {
		this.topConstraint = a;
		this.bottomConstraint = c;
		this.minY = a;
		this.maxY = c;
		if (b) {
			this.setYTicks(this.initPageY, b)
		}
		this.constrainY = true
	},
	constrainTo : function (b, c, a, d) {
		this.resetConstraints();
		this.initPageX = b.left + a;
		this.initPageY = c.top + d;
		this.setXConstraint(b.left, b.right, this.xTickSize);
		this.setYConstraint(c.top - 1, c.top - 1, this.yTickSize)
	},
	onDragOver : function (m) {
		var i = this.dragData,
		c = i.record,
		d = this.gantt;
		if (!i.hidden) {
			Ext.fly(i.sourceNode).hide();
			i.hidden = true
		}
		var a = d.getDateFromCoordinate(m.getXY()[0]) - i.sourceDate;
		var o = new Date(i.origStart - 0 + a);
		var g = this.proxy.el;
		var j;
		if (d.timeAxis.isContinuous()) {
			j = d.timeAxis.roundDate(o, d.snapRelativeToEventStartDate ? i.origStart : false)
		} else {
			var n = g.getX() + (d.rtl ? g.getWidth() : 0) + d.getXOffset(c) - i.offsets[0];
			j = d.getDateFromXY([n, 0], "round")
		}
		if (this.showExactDropPosition && d.taskStore.skipWeekendsDuringDragDrop && !c.isManuallyScheduled()) {
			var b = Ext.fly(i.ddel.id);
			var k = 0;
			var l = c.skipNonWorkingTime(j, !c.isMilestone());
			if (o.getTime() != l.getTime()) {
				k = d.timeAxisViewModel.getDistanceBetweenDates(o, l)
			}
			var f = c.recalculateEndDate(l);
			if (o > d.timeAxis.getStart()) {
				b.setWidth(d.timeAxisViewModel.getDistanceBetweenDates(l, Sch.util.Date.min(f, d.timeAxis.getEnd())));
				if (k) {
					g.setX(g.getX() + k)
				}
			}
		}
		if (!j || j - i.start === 0) {
			return
		}
		i.start = j;
		this.valid = this.validatorFn.call(this.validatorFnScope || d, c, j, i.duration, m) !== false;
		if (this.tip) {
			var h = c.calculateEndDate(j, c.getDuration(), c.getDurationUnit());
			this.updateTip(c, j, h, this.valid)
		}
	},
	startDrag : function () {
		var a = Ext.dd.ScrollManager;
		this.gantt.el.ddScrollConfig = {
			increment : a.increment,
			hthresh : a.hthresh,
			vthresh : -1
		};
		return this.callParent(arguments)
	},
	endDrag : function () {
		delete this.gantt.el.ddScrollConfig;
		return this.callParent(arguments)
	},
	onStartDrag : function () {
		var a = this.dragData.record;
		if (this.tip) {
			Ext.suspendLayouts();
			this.tip.enable();
			this.tip.show(this.dragData.ddel);
			this.updateTip(a, a.getStartDate(), a.getEndDate());
			Ext.resumeLayouts()
		}
		this.gantt.fireEvent("taskdragstart", this.gantt, a)
	},
	updateTip : function (b, d, a, c) {
		c = c !== false;
		if (b.isMilestone() && d - Ext.Date.clearTime(d, true) === 0) {
			d = Sch.util.Date.add(d, Sch.util.Date.MILLI, -1);
			a = Sch.util.Date.add(a, Sch.util.Date.MILLI, -1)
		}
		this.tip.update(d, a, c, b)
	},
	getDragData : function (i) {
		var h = this.gantt,
		f = i.getTarget(h.eventSelector);
		if (f && !i.getTarget(".sch-gantt-baseline-item")) {
			var c = h.resolveTaskRecord(f),
			b = c.isMilestone();
			if (h.fireEvent("beforetaskdrag", h, c, i) === false) {
				return null
			}
			var m = i.getXY();
			var a = f.cloneNode(true),
			k = this.showExactDropPosition ? 0 : h.getSnapPixelAmount(),
			j = Ext.fly(f).getXY();
			var d = [m[0] - j[0], m[1] - j[1]];
			a.id = Ext.id();
			var l = Ext.fly(f).getHeight();
			Ext.fly(a).setHeight(l - (Ext.isIE7 && !b ? 2 : 0));
			if (Ext.isIE8m && b) {
				Ext.fly(a).setSize(l + 5, l + 5)
			}
			a.style.left = -d[0] + "px";
			this.constrainTo(Ext.fly(h.findItemByChild(f)).getRegion(), Ext.fly(f).getRegion(), d[0], d[1]);
			this.valid = false;
			if (k >= 1) {
				this.setXConstraint(this.leftConstraint, this.rightConstraint, k)
			}
			return {
				sourceNode : f,
				repairXY : j,
				offsets : d,
				ddel : a,
				record : c,
				duration : Sch.util.Date.getDurationInMinutes(c.getStartDate(), c.getEndDate()),
				sourceDate : h.getDateFromCoordinate(m[0]),
				origStart : c.getStartDate(),
				start : null
			}
		}
		return null
	},
	afterRepair : function () {
		Ext.fly(this.dragData.sourceNode).show();
		if (this.tip) {
			this.tip.hide()
		}
		this.dragging = false
	},
	getRepairXY : function () {
		this.gantt.fireEvent("aftertaskdrop", this.gantt);
		return this.dragData.repairXY
	},
	onDragDrop : function (g, a) {
		var i = this,
		h = i.cachedTarget || Ext.dd.DragDropMgr.getDDById(a),
		f = i.dragData,
		d = i.gantt,
		c = f.record,
		b = f.start,
		j = true;
		f.ddCallbackArgs = [h, g, a];
		if (this.tip) {
			this.tip.disable()
		}
		if (this.valid && b && c.getStartDate() - b !== 0) {
			f.finalize = function () {
				i.finalize.apply(i, arguments)
			};
			j = d.fireEvent("beforetaskdropfinalize", i, f, g) !== false
		}
		if (j) {
			this.finalize(this.valid)
		}
	},
	finalize : function (c) {
		var e = this.dragData,
		b = this.gantt,
		a = e.record,
		f = e.start,
		d = false;
		if (c) {
			b.taskStore.on("update", function () {
				d = true
			}, null, {
				single : true
			});
			a.setStartDate(f, true, b.taskStore.skipWeekendsDuringDragDrop);
			if (d) {
				b.fireEvent("taskdrop", b, a);
				if (Ext.isIE9) {
					this.proxy.el.setStyle("visibility", "hidden");
					Ext.Function.defer(this.onValidDrop, 10, this, e.ddCallbackArgs)
				} else {
					this.onValidDrop.apply(this, e.ddCallbackArgs)
				}
			} else {
				this.onInvalidDrop.apply(this, e.ddCallbackArgs)
			}
		} else {
			this.onInvalidDrop.apply(this, e.ddCallbackArgs)
		}
		b.fireEvent("aftertaskdrop", b, a)
	},
	onInvalidDrop : function (b, a, c) {
		if (Ext.isIE && !a) {
			a = b;
			b = b.getTarget() || document.body
		}
		return this.callParent([b, a, c])
	}
});
Ext.define("Gnt.feature.DependencyDragZone", {
	extend : "Ext.dd.DragZone",
	mixins : {
		observable : "Ext.util.Observable"
	},
	rtl : null,
	useLineProxy : null,
	terminalSelector : null,
	ganttView : null,
	fromText : null,
	toText : null,
	startText : null,
	endText : null,
	toolTipTpl : null,
	constructor : function (b, a) {
		this.mixins.observable.constructor.call(this, a);
		this.callParent(arguments)
	},
	initLineProxy : function (b, a) {
		var d = this.lineProxyEl = this.lineProxyEl || this.el.createChild({
				cls : "sch-gantt-connector-proxy"
			});
		var e = (Ext.isIE9m || Ext.isIEQuirks) ? 0 : 4;
		var c = this.rtl ? (a ? "r" : "l") : (a ? "l" : "r");
		d.alignTo(b, c, [a ? -e : e, 0]);
		Ext.apply(this, {
			containerTop : this.el.getTop(),
			containerLeft : this.el.getLeft(),
			startXY : d.getXY(),
			startScrollLeft : this.el.dom.scrollLeft,
			startScrollTop : this.el.dom.scrollTop
		})
	},
	onDrag : function (b, a) {
		if (this.useLineProxy) {
			this.updateLineProxy(b.getXY())
		}
	},
	updateLineProxy : function (m) {
		var a = this.lineProxyEl,
		j = m[0] - this.startXY[0] + this.el.dom.scrollLeft - this.startScrollLeft,
		i = m[1] - this.startXY[1] + this.el.dom.scrollTop - this.startScrollTop,
		b = Math.max(1, Math.sqrt(Math.pow(j, 2) + Math.pow(i, 2)) - 2),
		h = Math.atan2(i, j) - (Math.PI / 2),
		e;
		if ((Ext.isIE9m || Ext.isIEQuirks)) {
			var k = Math.cos(h),
			g = Math.sin(h),
			l = 'progid:DXImageTransform.Microsoft.Matrix(sizingMethod="auto expand", M11 = ' + k + ", M12 = " + (-g) + ", M21 = " + g + ", M22 = " + k + ")",
			d,
			f;
			if (this.el.dom.scrollTop !== this.startScrollTop) {
				d = this.startScrollTop - this.containerTop
			} else {
				d = this.el.dom.scrollTop - this.containerTop
			}
			if (this.el.dom.scrollLeft !== this.startScrollLeft) {
				f = this.startScrollLeft - this.containerLeft
			} else {
				f = this.el.dom.scrollLeft - this.containerLeft
			}
			e = {
				height : b + "px",
				top : Math.min(0, i) + this.startXY[1] + d + (i < 0 ? 2 : 0) + "px",
				left : Math.min(0, j) + this.startXY[0] + f + (j < 0 ? 2 : 0) + "px",
				filter : l,
				"-ms-filter" : l
			}
		} else {
			var c = "rotate(" + h + "rad)";
			e = {
				height : b + "px",
				"-o-transform" : c,
				"-webkit-transform" : c,
				"-ms-transform" : c,
				"-moz-transform" : c,
				transform : c
			}
		}
		a.setStyle(e)
	},
	onStartDrag : function () {
		this.el.addCls("sch-gantt-dep-dd-dragging");
		this.proxy.el.addCls("sch-dd-dependency-proxy");
		this.fireEvent("dndstart", this);
		if (this.useLineProxy) {
			var a = this.dragData;
			this.initLineProxy(a.sourceNode, a.isStart);
			this.lineProxyEl.show()
		}
	},
	getDragData : function (f) {
		var d = f.getTarget(this.terminalSelector);
		if (d) {
			var c = this.ganttView.resolveTaskRecord(d);
			if (this.fireEvent("beforednd", this, c) === false) {
				return null
			}
			var b = !!d.className.match("sch-gantt-terminal-start");
			var a = {
				fromLabel : this.fromText,
				fromTaskName : Ext.String.htmlEncode(c.getName()),
				fromSide : b ? this.startText : this.endText,
				toLabel : this.toText,
				toTaskName : "",
				toSide : ""
			};
			var g = Ext.core.DomHelper.createDom({
					html : this.toolTipTpl.apply(a)
				}).firstChild;
			return {
				fromId : c.getId() || c.internalId,
				tplData : a,
				isStart : b,
				repairXY : Ext.fly(d).getXY(),
				ddel : g,
				sourceNode : Ext.fly(d).up(this.ganttView.eventSelector)
			}
		}
		return false
	},
	afterRepair : function () {
		this.el.removeCls("sch-gantt-dep-dd-dragging");
		this.dragging = false;
		this.fireEvent("afterdnd", this)
	},
	onMouseUp : function () {
		this.el.removeCls("sch-gantt-dep-dd-dragging");
		if (this.lineProxyEl) {
			var b = (Ext.isIE9m || Ext.isIEQuirks) ? 0 : 400;
			var a = this.lineProxyEl;
			a.animate({
				to : {
					height : 0
				},
				duration : b,
				callback : function () {
					Ext.destroy(a)
				}
			});
			this.lineProxyEl = null
		}
	},
	getRepairXY : function () {
		return this.dragData.repairXY
	},
	destroy : function () {
		Ext.destroy(this.lineProxyEl);
		this.callParent(arguments)
	}
});
Ext.define("Gnt.feature.DependencyDropZone", {
	extend : "Ext.dd.DropZone",
	mixins : {
		observable : "Ext.util.Observable"
	},
	terminalSelector : null,
	dependencyStore : null,
	toText : null,
	startText : null,
	endText : null,
	ganttView : null,
	constructor : function (b, a) {
		this.mixins.observable.constructor.call(this, a);
		this.callParent(arguments)
	},
	getTargetFromEvent : function (a) {
		return a.getTarget(this.terminalSelector)
	},
	onNodeEnter : function (d, a, c, b) {
		Ext.fly(d).addCls("sch-gantt-terminal-drophover")
	},
	onNodeOut : function (d, a, c, b) {
		Ext.fly(d).removeCls("sch-gantt-terminal-drophover");
		this.toolTipTpl.overwrite(a.proxy.el.down(".sch-dd-dependency"), b.tplData)
	},
	onNodeOver : function (d, j, f, c) {
		var a = this.ganttView.resolveTaskRecord(d),
		i = a.getId() || a.internalId,
		b = d.className.match("sch-gantt-terminal-start");
		var h = {};
		Ext.apply(h, {
			toLabel : this.toText,
			toTaskName : Ext.String.htmlEncode(a.getName()),
			toSide : b ? this.startText : this.endText
		}, c.tplData);
		this.toolTipTpl.overwrite(j.proxy.el.down(".sch-dd-dependency"), h);
		var g = this.resolveType(c.isStart, d);
		if (this.dependencyStore.isValidDependency(c.fromId, i, g)) {
			return this.dropAllowed
		} else {
			return this.dropNotAllowed
		}
	},
	onNodeDrop : function (i, a, h, f) {
		var d = this.resolveType(f.isStart, i),
		g,
		c = this.ganttView.resolveTaskRecord(i),
		b = c.getId() || c.internalId;
		this.el.removeCls("sch-gantt-dep-dd-dragging");
		g = this.dependencyStore.isValidDependency(f.fromId, b, d);
		if (g) {
			this.fireEvent("drop", this, f.fromId, b, d)
		}
		this.fireEvent("afterdnd", this);
		return g
	},
	resolveType : function (a, d) {
		var c = Gnt.model.Dependency.Type,
		b;
		if (a) {
			if (d.className.match("sch-gantt-terminal-start")) {
				b = c.StartToStart
			} else {
				b = c.StartToEnd
			}
		} else {
			if (d.className.match("sch-gantt-terminal-start")) {
				b = c.EndToStart
			} else {
				b = c.EndToEnd
			}
		}
		return b
	}
});
Ext.define("Gnt.feature.DependencyDragDrop", {
	extend : "Ext.util.Observable",
	mixins : {
		localizable : "Gnt.mixin.Localizable"
	},
	requires : ["Gnt.feature.DependencyDragZone", "Gnt.feature.DependencyDropZone", "Ext.XTemplate"],
	useLineProxy : true,
	dragZoneConfig : null,
	dropZoneConfig : null,
	toolTipTpl : ['<div class="sch-dd-dependency">', "<table><tbody>", "<tr>", '<td><span class="sch-dd-dependency-from">{fromLabel}:</span></td>', '<td><span class="sch-dd-dependency-from-name">{fromTaskName}</span> - {fromSide}</td>', "</tr>", "<tr>", '<td><span class="sch-dd-dependency-to">{toLabel}:</span></td>', '<td><span class="sch-dd-dependency-to-name">{toTaskName}</span> - {toSide}</td>', "</tr>", "</tbody></table>", "</div>"],
	terminalSelector : ".sch-gantt-terminal",
	el : null,
	rtl : null,
	ddGroup : null,
	ganttView : null,
	dependencyStore : null,
	constructor : function (b) {
		this.addEvents("beforednd", "dndstart", "drop", "afterdnd");
		var a = b.ganttView;
		Ext.apply(this, b);
		this.ddGroup = a.id + "-sch-dependency-dd";
		this.el.on("mousemove", this.doSetup, this, {
			single : true
		});
		this.callParent(arguments)
	},
	doSetup : function () {
		var a = this;
		this.dragZone = new Gnt.feature.DependencyDragZone(this.el, Ext.apply({
					rtl : this.rtl,
					terminalSelector : this.terminalSelector,
					useLineProxy : this.useLineProxy,
					ddGroup : this.ddGroup,
					ganttView : this.ganttView,
					startText : this.L("startText"),
					endText : this.L("endText"),
					fromText : this.L("fromText"),
					toText : this.L("toText"),
					toolTipTpl : Ext.XTemplate.getTpl(this, "toolTipTpl")
				}, this.dragZoneConfig));
		this.relayEvents(this.dragZone, ["beforednd", "dndstart", "afterdnd"]);
		this.dropZone = Ext.create("Gnt.feature.DependencyDropZone", this.el, Ext.apply({
					rtl : this.rtl,
					terminalSelector : this.terminalSelector,
					ddGroup : this.ddGroup,
					ganttView : this.ganttView,
					dependencyStore : this.dependencyStore,
					startText : this.L("startText"),
					endText : this.L("endText"),
					toText : this.L("toText"),
					toolTipTpl : Ext.XTemplate.getTpl(this, "toolTipTpl")
				}, this.dropZoneConfig));
		this.relayEvents(this.dropZone, ["drop", "afterdnd"]);
		this.configureAllowedSourceTerminals();
		if (this.dependencyStore.allowedDependencyTypes) {
			this.dragZone.on("dndstart", this.configureAllowedTargetTerminals, this)
		} else {
			this.el.addCls(["sch-gantt-terminal-allow-target-start", "sch-gantt-terminal-allow-target-end"])
		}
	},
	configureAllowedSourceTerminals : function () {
		var b = this.dependencyStore.allowedDependencyTypes;
		var a = ["sch-gantt-terminal-allow-source-start", "sch-gantt-terminal-allow-source-end"];
		if (b) {
			a = [];
			if (Ext.Array.indexOf(b, "EndToEnd") > -1 || Ext.Array.indexOf(b, "EndToStart") > -1) {
				a.push("sch-gantt-terminal-allow-source-end")
			}
			if (Ext.Array.indexOf(b, "StartToStart") > -1 || Ext.Array.indexOf(b, "StartToEnd") > -1) {
				a.push("sch-gantt-terminal-allow-source-start")
			}
		}
		this.el.addCls(a)
	},
	configureAllowedTargetTerminals : function () {
		var b = this.dependencyStore.allowedDependencyTypes;
		var a = [];
		this.el.removeCls(["sch-gantt-terminal-allow-target-start", "sch-gantt-terminal-allow-target-end"]);
		if (Ext.Array.contains(b, "EndToEnd") || Ext.Array.contains(b, "StartToEnd")) {
			a.push("sch-gantt-terminal-allow-target-end")
		}
		if (Ext.Array.contains(b, "StartToStart") || Ext.Array.contains(b, "EndToStart")) {
			a.push("sch-gantt-terminal-allow-target-start")
		}
		this.el.addCls(a)
	},
	destroy : function () {
		if (this.dragZone) {
			this.dragZone.destroy()
		}
		if (this.dropZone) {
			this.dropZone.destroy()
		}
	}
});
Ext.define("Gnt.feature.DragCreator", {
	requires : ["Ext.Template", "Sch.util.DragTracker", "Gnt.Tooltip"],
	constructor : function (a) {
		Ext.apply(this, a || {});
		this.init()
	},
	disabled : false,
	showDragTip : true,
	tooltipConfig : null,
	dragTolerance : 2,
	validatorFn : Ext.emptyFn,
	validatorFnScope : null,
	setDisabled : function (a) {
		this.disabled = a;
		if (this.dragTip) {
			this.dragTip.setDisabled(a)
		}
	},
	getProxy : function () {
		if (!this.proxy) {
			this.proxy = this.template.append(this.ganttView.ownerCt.el, {}, true)
		}
		return this.proxy
	},
	onBeforeDragStart : function (f) {
		var c = this.ganttView,
		b = f.getTarget("." + c.timeCellCls, 2);
		if (b) {
			var a = c.resolveTaskRecord(b);
			var d = c.getDateFromDomEvent(f);
			if (!this.disabled && b && !a.getStartDate() && !a.getEndDate() && c.fireEvent("beforedragcreate", c, a, d, f) !== false) {
				f.stopEvent();
				this.taskRecord = a;
				this.originalStart = d;
				this.rowRegion = c.getScheduleRegion(this.taskRecord, this.originalStart);
				this.dateConstraints = c.getDateConstraints(this.resourceRecord, this.originalStart);
				return true
			}
		}
		return false
	},
	onDragStart : function () {
		var c = this,
		a = c.ganttView,
		b = c.getProxy();
		c.start = c.originalStart;
		c.end = c.start;
		c.rowBoundaries = {
			top : c.rowRegion.top,
			bottom : c.rowRegion.bottom
		};
		b.setRegion({
			top : c.rowBoundaries.top,
			right : c.tracker.startXY[0],
			bottom : c.rowBoundaries.bottom,
			left : c.tracker.startXY[0]
		});
		b.show();
		c.ganttView.fireEvent("dragcreatestart", c.ganttView);
		if (c.showDragTip) {
			c.dragTip.update(c.start, c.end, true, this.taskRecord);
			c.dragTip.enable();
			c.dragTip.show(b)
		}
	},
	onDrag : function (g) {
		var d = this,
		c = d.ganttView,
		b = d.tracker.getRegion().constrainTo(d.rowRegion),
		f = c.getStartEndDatesFromRegion(b, "round");
		if (!f) {
			return
		}
		d.start = f.start || d.start;
		d.end = f.end || d.end;
		var a = d.dateConstraints;
		if (a) {
			d.end = Sch.util.Date.constrain(d.end, a.start, a.end);
			d.start = Sch.util.Date.constrain(d.start, a.start, a.end)
		}
		d.valid = this.validatorFn.call(d.validatorFnScope || d, this.taskRecord, d.start, d.end, g) !== false;
		if (d.showDragTip) {
			d.dragTip.update(d.start, d.end, d.valid, this.taskRecord)
		}
		Ext.apply(b, d.rowBoundaries);
		this.getProxy().setRegion(b)
	},
	onDragEnd : function (a) {
		var b = this.ganttView;
		if (this.showDragTip) {
			this.dragTip.disable()
		}
		if (!this.start || !this.end || (this.end < this.start)) {
			this.valid = false
		}
		if (this.valid) {
			this.taskRecord.setStartEndDate(this.start, this.end, this.taskRecord.getTaskStore().skipWeekendsDuringDragDrop);
			b.fireEvent("dragcreateend", b, this.taskRecord, a)
		}
		this.proxy.hide();
		b.fireEvent("afterdragcreate", b)
	},
	init : function () {
		var c = this.ganttView,
		a = c.el,
		b = Ext.Function.bind;
		this.lastTime = new Date();
		this.template = this.template || Ext.create("Ext.Template", '<div class="sch-gantt-dragcreator-proxy"></div>', {
				compiled : true,
				disableFormats : true
			});
		c.on({
			destroy : this.onGanttDestroy,
			scope : this
		});
		this.tracker = new Sch.util.DragTracker({
				el : a,
				tolerance : this.dragTolerance,
				onBeforeStart : b(this.onBeforeDragStart, this),
				onStart : b(this.onDragStart, this),
				onDrag : b(this.onDrag, this),
				onEnd : b(this.onDragEnd, this)
			});
		if (this.showDragTip) {
			this.dragTip = new Gnt.Tooltip(Ext.apply({
						mode : "duration",
						cls : "sch-gantt-dragcreate-tip",
						gantt : c
					}, this.tooltipConfig))
		}
	},
	onGanttDestroy : function () {
		if (this.dragTip) {
			this.dragTip.destroy()
		}
		if (this.tracker) {
			this.tracker.destroy()
		}
		if (this.proxy) {
			Ext.destroy(this.proxy);
			this.proxy = null
		}
	}
});
Ext.define("Gnt.feature.LabelEditor", {
	extend : "Ext.Editor",
	labelPosition : "",
	constructor : function (b, a) {
		this.ganttView = b;
		this.ganttView.on("afterrender", this.onGanttRender, this);
		Ext.apply(this, a);
		if (this.labelPosition === "left") {
			this.alignment = "r-r"
		} else {
			if (this.labelPosition === "right") {
				this.alignment = "l-l"
			}
		}
		this.delegate = ".sch-gantt-label-" + this.labelPosition;
		this.callParent([a])
	},
	edit : function (a) {
		var b = this.ganttView.getElementFromEventRecord(a).up(this.ganttView.eventWrapSelector);
		this.record = a;
		if (!this.rendered) {
			this.render(this.ganttView.getSecondaryCanvasEl())
		}
		this.startEdit(b.down(this.delegate), this.dataIndex ? a.get(this.dataIndex) : "")
	},
	triggerEvent : "dblclick",
	delegate : null,
	dataIndex : null,
	shadow : false,
	completeOnEnter : true,
	cancelOnEsc : true,
	ignoreNoChange : true,
	onGanttRender : function (a) {
		if (!this.field.width) {
			this.autoSize = "width"
		}
		this.on({
			beforestartedit : function (c, b, d) {
				return a.fireEvent("labeledit_beforestartedit", a, this.record, d, c)
			},
			beforecomplete : function (c, d, b) {
				return a.fireEvent("labeledit_beforecomplete", a, d, b, this.record, c)
			},
			complete : function (c, d, b) {
				this.record.set(this.dataIndex, d);
				a.fireEvent("labeledit_complete", a, d, b, this.record, c)
			},
			scope : this
		});
		a.el.on(this.triggerEvent, function (c, b) {
			this.edit(a.resolveTaskRecord(b))
		}, this, {
			delegate : this.delegate
		})
	}
});
Ext.define("Gnt.feature.ProgressBarResize", {
	requires : ["Ext.ToolTip", "Ext.resizer.Resizer"],
	constructor : function (a) {
		Ext.apply(this, a || {});
		var b = this.ganttView;
		b.on({
			destroy : this.cleanUp,
			scope : this
		});
		b.el.on("mousedown", this.onMouseDown, this, {
			delegate : ".sch-gantt-progressbar-handle"
		});
		this.callParent(arguments)
	},
	useTooltip : true,
	increment : 10,
	tip : null,
	resizable : null,
	ganttView : null,
	onMouseDown : function (d, b) {
		var c = this.ganttView,
		f = c.resolveTaskRecord(b);
		if (c.fireEvent("beforeprogressbarresize", c, f) !== false) {
			var a = Ext.fly(b).prev(".sch-gantt-progress-bar");
			d.stopEvent();
			this.resizable = this.createResizable(a, f, d);
			c.fireEvent("progressbarresizestart", c, f);
			Ext.getBody().on("mouseup", this.onBodyMouseUp, this, {
				single : true,
				delay : 1
			})
		}
	},
	createResizable : function (b, g, d) {
		var i = d.getTarget(),
		h = this.ganttView.rtl,
		f = b.up(this.ganttView.eventSelector),
		j = f.getWidth() - 2,
		c = j * this.increment / 100;
		var a = Ext.create("Ext.resizer.Resizer", {
				target : b,
				taskRecord : g,
				handles : h ? "w" : "e",
				minWidth : 0,
				maxWidth : j,
				minHeight : 1,
				widthIncrement : c,
				listeners : {
					resizedrag : this.partialResize,
					resize : this.afterResize,
					scope : this
				}
			});
		a.resizeTracker.onMouseDown(d, a[h ? "west" : "east"].dom);
		f.select("." + Ext.baseCSSPrefix + "resizable-handle, .sch-gantt-terminal, .sch-gantt-progressbar-handle").hide();
		if (this.useTooltip) {
			this.tip = Ext.create("Ext.ToolTip", {
					autoHide : false,
					anchor : "b",
					html : "%"
				});
			this.tip.setTarget(b);
			this.tip.update(g.getPercentDone() + "%");
			this.tip.show()
		}
		return a
	},
	partialResize : function (c, b) {
		var a = Math.round(b * 100 / (c.maxWidth * this.increment)) * this.increment;
		if (this.tip) {
			this.tip.body.update(a + "%")
		}
	},
	afterResize : function (f, b, c, g) {
		var i = f.taskRecord;
		if (this.tip) {
			this.tip.destroy();
			this.tip = null
		}
		var a = f.taskRecord.getPercentDone();
		if (Ext.isNumber(b)) {
			var d = Math.round(b * 100 / (f.maxWidth * this.increment)) * this.increment;
			f.taskRecord.setPercentDone(d)
		}
		if (a === f.taskRecord.getPercentDone()) {
			this.ganttView.refreshNode(this.ganttView.indexOf(f.taskRecord))
		}
		f.destroy();
		this.resizable = null;
		this.ganttView.fireEvent("afterprogressbarresize", this.ganttView, i)
	},
	onBodyMouseUp : function () {
		if (this.resizable) {
			this.afterResize(this.resizable)
		}
	},
	cleanUp : function () {
		if (this.tip) {
			this.tip.destroy()
		}
	}
});
Ext.define("Gnt.feature.TaskResize", {
	requires : ["Gnt.Tooltip"],
	constructor : function (a) {
		Ext.apply(this, a);
		var b = this.ganttView;
		b.on({
			destroy : this.cleanUp,
			scope : this
		});
		b.mon(b.el, "mousedown", this.onMouseDown, this, {
			delegate : ".sch-resizable-handle"
		});
		this.callParent(arguments)
	},
	showDuration : true,
	showExactResizePosition : false,
	useTooltip : true,
	tooltipConfig : null,
	validatorFn : Ext.emptyFn,
	validatorFnScope : null,
	taskRec : null,
	isStart : null,
	ganttView : null,
	resizable : null,
	onMouseDown : function (f, c) {
		var b = this.ganttView,
		a = f.getTarget(b.eventSelector),
		g = b.resolveTaskRecord(a);
		var d = g.isResizable();
		if (f.button !== 0 || d === false || typeof d === "string" && !a.className.match(d)) {
			return
		}
		if (b.fireEvent("beforetaskresize", b, g, f) === false) {
			return
		}
		f.stopEvent();
		this.taskRec = g;
		this.isStart = !!c.className.match("sch-resizable-handle-start");
		b.el.on({
			mousemove : this.onMouseMove,
			mouseup : this.onMouseUp,
			scope : this,
			single : true
		});
		b.fireEvent("taskresizestart", b, g)
	},
	onMouseMove : function (h, m) {
		var f = this.ganttView,
		j = this.taskRec,
		i = f.getElementFromEventRecord(j),
		k = f.rtl,
		a = this.isStart,
		d = (k && !a) || (!k && a),
		c = f.getSnapPixelAmount(),
		b = i.getWidth(),
		l = i.up(f.getItemSelector()).getRegion();
		this.resizable = Ext.create("Ext.resizer.Resizer", {
				otherEdgeX : d ? i.getRight() : i.getLeft(),
				target : i,
				taskRecord : j,
				isStart : a,
				isWest : d,
				handles : d ? "w" : "e",
				constrainTo : l,
				minHeight : 1,
				minWidth : c,
				widthIncrement : c,
				listeners : {
					resizedrag : this.partialResize,
					resize : this.afterResize,
					scope : this
				}
			});
		this.resizable.resizeTracker.onMouseDown(h, this.resizable[d ? "west" : "east"].dom);
		if (this.useTooltip) {
			if (!this.tip) {
				this.tip = Ext.create("Gnt.Tooltip", Ext.apply({
							mode : this.showDuration ? "duration" : "startend",
							gantt : this.ganttView
						}, this.tooltipConfig))
			}
			this.tip.show(i, h.getX() - 15);
			this.tip.update(j.getStartDate(), j.getEndDate(), true, j);
			Ext.getBody().on("mouseup", function () {
				this.tip.hide()
			}, this, {
				single : true
			})
		}
	},
	onMouseUp : function (c, a) {
		var b = this.ganttView;
		b.el.un({
			mousemove : this.onMouseMove,
			scope : this,
			single : true
		})
	},
	partialResize : function (k, j, l, p) {
		var h = this.ganttView,
		f = k.isWest,
		o = k.taskRecord,
		g;
		if (f) {
			g = h.getDateFromCoordinate(k.otherEdgeX - Math.min(j, this.resizable.maxWidth), !this.showExactResizePosition ? "round" : null)
		} else {
			g = h.getDateFromCoordinate(k.otherEdgeX + Math.min(j, this.resizable.maxWidth), !this.showExactResizePosition ? "round" : null)
		}
		if (!g || k.date - g === 0) {
			return
		}
		var b,
		a,
		d;
		if (this.showExactResizePosition) {
			var n = h.timeAxis.roundDate(g, h.snapRelativeToEventStartDate ? o.getStartDate() : false);
			n = o.skipNonWorkingTime(n, !o.isMilestone());
			var s = k.target.el,
			c;
			if (f) {
				b = o.skipNonWorkingTime(n, !o.isMilestone());
				d = b;
				c = h.timeAxisViewModel.getDistanceBetweenDates(b, o.getEndDate());
				s.setWidth(c);
				var q = h.timeAxisViewModel.getDistanceBetweenDates(g, b);
				s.setX(s.getX() + q)
			} else {
				var r = Gnt.util.Data.cloneModelSet([o])[0];
				var i = o.getTaskStore();
				r.setTaskStore(i);
				r.setCalendar(o.getCalendar());
				r.setEndDate(n, false, i.skipWeekendsDuringDragDrop);
				a = r.getEndDate();
				d = a;
				c = h.timeAxisViewModel.getDistanceBetweenDates(o.getStartDate(), a);
				s.setWidth(c)
			}
		} else {
			b = k.isStart ? g : k.taskRecord.getStartDate();
			a = k.isStart ? k.taskRecord.getEndDate() : g;
			d = g
		}
		k.date = d;
		h.fireEvent("partialtaskresize", h, o, b, a, k.el, p);
		if (this.useTooltip) {
			var m = this.validatorFn.call(this.validatorFnScope || this, o, b, a) !== false;
			this.tip.update(b, a, m, o)
		}
	},
	afterResize : function (n, m, i, k) {
		if (this.useTooltip) {
			this.tip.hide()
		}
		var l = n.taskRecord,
		d = l.getStartDate(),
		o = l.getEndDate(),
		b = n.isStart ? n.date : d,
		c = n.isStart ? o : n.date,
		j = this.ganttView;
		n.destroy();
		if (b && c && (b - d || c - o) && this.validatorFn.call(this.validatorFnScope || this, l, b, c, k) !== false) {
			var g,
			f = function () {
				g = true
			};
			j.on("itemupdate", f, null, {
				single : true
			});
			var a = j.taskStore.skipWeekendsDuringDragDrop;
			if (b - d !== 0) {
				l.setStartDate(b <= c ? b : c, false, a)
			} else {
				l.setEndDate(b <= c ? c : b, false, a)
			}
			j.un("itemupdate", f, null, {
				single : true
			});
			if (!g) {
				j.refreshNode(j.store.indexOf(l))
			}
		} else {
			j.refreshKeepingScroll()
		}
		j.fireEvent("aftertaskresize", j, l)
	},
	cleanUp : function () {
		if (this.tip) {
			this.tip.destroy()
		}
	}
});
Ext.define("Gnt.feature.WorkingTime", {
	extend : "Sch.plugin.Zones",
	requires : ["Ext.data.Store", "Sch.model.Range"],
	expandToFitView : true,
	calendar : null,
	init : function (a) {
		if (!this.calendar) {
			Ext.Error.raise("Required attribute 'calendar' missed during initialization of 'Gnt.feature.WorkingTime'")
		}
		this.bindCalendar(this.calendar);
		Ext.apply(this, {
			store : new Ext.data.Store({
				model : "Sch.model.Range"
			})
		});
		this.callParent(arguments);
		a.on("viewchange", this.onViewChange, this);
		this.onViewChange()
	},
	bindCalendar : function (b) {
		var a = {
			datachanged : this.refresh,
			update : this.refresh,
			scope : this,
			delay : 1
		};
		if (this.calendar) {
			this.calendar.un(a)
		}
		if (b) {
			b.on(a)
		}
		this.calendar = b
	},
	onViewChange : function () {
		var a = Sch.util.Date;
		if (a.compareUnits(this.timeAxis.unit, a.WEEK) > 0) {
			this.setDisabled(true)
		} else {
			this.setDisabled(false);
			this.refresh()
		}
	},
	refresh : function () {
		var a = this.schedulerView;
		this.store.removeAll(true);
		this.store.add(this.calendar.getHolidaysRanges(a.timeAxis.getStart(), a.timeAxis.getEnd(), true))
	},
	destroy : function () {
		this.bindCalendar(null);
		this.callParent(arguments)
	}
});
Ext.define("Gnt.plugin.DependencyEditor", {
	extend : "Ext.form.Panel",
	alias : "plugin.gantt_dependencyeditor",
	mixins : ["Ext.AbstractPlugin", "Gnt.mixin.Localizable"],
	lockableScope : "top",
	header : false,
	requires : ["Ext.util.Filter", "Ext.form.field.Display", "Ext.form.field.ComboBox", "Ext.form.field.Number", "Gnt.model.Dependency", "Ext.data.ArrayStore"],
	hideOnBlur : true,
	showLag : false,
	border : false,
	height : 150,
	width : 260,
	frame : true,
	labelWidth : 60,
	triggerEvent : "dependencydblclick",
	constrain : false,
	initComponent : function () {
		Ext.apply(this, {
			items : this.buildFields(),
			defaults : {
				width : 240
			},
			floating : true,
			hideMode : "offsets"
		});
		this.callParent(arguments);
		this.addCls("sch-gantt-dependencyeditor")
	},
	init : function (a) {
		a.on(this.triggerEvent, this.onDependencyDblClick, this);
		a.on("destroy", this.destroy, this);
		a.on("afterrender", this.onGanttRender, this, {
			delay : 50
		});
		this.gantt = a;
		this.taskStore = a.getTaskStore()
	},
	onGanttRender : function () {
		this.render(Ext.getBody());
		this.collapse(Ext.Component.DIRECTION_TOP, true);
		this.hide();
		if (this.hideOnBlur) {
			this.on({
				show : function () {
					this.mon(Ext.getBody(), {
						click : this.onMouseClick,
						scope : this
					})
				},
				hide : function () {
					this.mun(Ext.getBody(), {
						click : this.onMouseClick,
						scope : this
					})
				},
				delay : 50
			})
		}
	},
	show : function (b, c) {
		this.dependencyRecord = b;
		if (this.lagField) {
			this.lagField.name = b.lagField
		}
		if (this.typeField) {
			this.typeField.name = b.typeField
		}
		this.getForm().loadRecord(b);
		this.fromLabel.setValue(Ext.String.htmlEncode(this.dependencyRecord.getSourceTask().getName()));
		this.toLabel.setValue(Ext.String.htmlEncode(this.dependencyRecord.getTargetTask().getName()));
		if (this.typeField) {
			var a = this.taskStore && this.taskStore.getDependencyStore(),
			d = a && a.allowedDependencyTypes;
			this.typeField.store.filter();
			this.typeField.setReadOnly(d && d.length < 2)
		}
		this.callParent([]);
		this.el.setXY(c);
		this.expand(!this.constrain);
		if (this.constrain) {
			this.doConstrain(Ext.util.Region.getRegion(Ext.getBody()))
		}
	},
	buildFields : function () {
		var b = this,
		c = Gnt.model.Dependency;
		var a = [this.fromLabel = new Ext.form.DisplayField({
					fieldLabel : this.L("fromText")
				}), this.toLabel = new Ext.form.DisplayField({
					fieldLabel : this.L("toText")
				}), this.typeField = this.buildTypeField()];
		if (this.showLag) {
			a.push(this.lagField = new Ext.form.NumberField({
						name : c.prototype.lagField,
						fieldLabel : this.L("lagText")
					}))
		}
		return a
	},
	onDependencyDblClick : function (c, a, d, b) {
		if (a != this.dependencyRecord) {
			this.show(a, d.getXY())
		}
	},
	filterAllowedTypes : function (b) {
		var c = this.taskStore && this.taskStore.getDependencyStore();
		if (!c || !c.allowedDependencyTypes) {
			return true
		}
		var f = c.allowedDependencyTypes;
		var g = c.model.Type;
		for (var d = 0, a = f.length; d < a; d++) {
			var e = g[f[d]];
			if (b.getId() == e) {
				return true
			}
		}
		return false
	},
	buildTypeField : function () {
		var b = Gnt.model.Dependency;
		var c = b.Type;
		this.typesFilter = new Ext.util.Filter({
				filterFn : this.filterAllowedTypes,
				scope : this
			});
		var a = new Ext.data.ArrayStore({
				fields : [{
						name : "id",
						type : "int"
					}, "text"],
				data : [[c.EndToStart, this.L("endToStartText")], [c.StartToStart, this.L("startToStartText")], [c.EndToEnd, this.L("endToEndText")], [c.StartToEnd, this.L("startToEndText")]]
			});
		a.filter(this.typesFilter);
		return new Ext.form.field.ComboBox({
			name : b.prototype.nameField,
			fieldLabel : this.L("typeText"),
			triggerAction : "all",
			queryMode : "local",
			editable : false,
			valueField : "id",
			displayField : "text",
			store : a
		})
	},
	onMouseClick : function (a) {
		if (this.collapsed || a.within(this.getEl()) || a.getTarget("." + Ext.baseCSSPrefix + "layer") || a.getTarget(".sch-ignore-click")) {
			return
		}
		this.collapse()
	},
	afterCollapse : function () {
		delete this.dependencyRecord;
		this.hide();
		this.callParent(arguments);
		if (this.hideOnBlur) {
			this.mun(Ext.getBody(), "click", this.onMouseClick, this)
		}
	}
});
Ext.define("Gnt.plugin.TaskContextMenu", {
	extend : "Ext.menu.Menu",
	alias : "plugin.gantt_taskcontextmenu",
	mixins : ["Ext.AbstractPlugin", "Gnt.mixin.Localizable"],
	lockableScope : "top",
	requires : ["Gnt.model.Dependency"],
	legacyHolderProp : "texts",
	plain : true,
	triggerEvent : "itemcontextmenu",
	grid : null,
	rec : null,
	lastHighlightedItem : null,
	taskEditorInjected : false,
	createMenuItems : function () {
		return [{
				handler : this.deleteTask,
				requiresTask : true,
				itemId : "deleteTask",
				text : this.L("deleteTask")
			}, {
				handler : this.editLeftLabel,
				requiresTask : true,
				itemId : "editLeftLabel",
				text : this.L("editLeftLabel")
			}, {
				handler : this.editRightLabel,
				requiresTask : true,
				itemId : "editRightLabel",
				text : this.L("editRightLabel")
			}, {
				handler : this.toggleMilestone,
				requiresTask : true,
				itemId : "toggleMilestone",
				text : this.L("convertToMilestone")
			}, {
				text : this.L("add"),
				itemId : "addTaskMenu",
				menu : {
					plain : true,
					defaults : {
						scope : this
					},
					items : [{
							handler : this.addTaskAboveAction,
							requiresTask : true,
							itemId : "addTaskAbove",
							text : this.L("addTaskAbove")
						}, {
							handler : this.addTaskBelowAction,
							itemId : "addTaskBelow",
							text : this.L("addTaskBelow")
						}, {
							handler : this.addMilestone,
							itemId : "addMilestone",
							requiresTask : true,
							text : this.L("addMilestone")
						}, {
							handler : this.addSubtask,
							requiresTask : true,
							itemId : "addSubtask",
							text : this.L("addSubtask")
						}, {
							handler : this.addSuccessor,
							requiresTask : true,
							itemId : "addSuccessor",
							text : this.L("addSuccessor")
						}, {
							handler : this.addPredecessor,
							requiresTask : true,
							itemId : "addPredecessor",
							text : this.L("addPredecessor")
						}
					]
				}
			}, {
				text : this.L("deleteDependency"),
				requiresTask : true,
				itemId : "deleteDependencyMenu",
				isDependenciesMenu : true,
				menu : {
					plain : true,
					listeners : {
						beforeshow : this.populateDependencyMenu,
						mouseover : this.onDependencyMouseOver,
						mouseleave : this.onDependencyMouseOut,
						scope : this
					}
				}
			}
		]
	},
	buildMenuItems : function () {
		this.items = this.createMenuItems()
	},
	initComponent : function () {
		this.defaults = this.defaults || {};
		this.defaults.scope = this;
		this.buildMenuItems();
		this.callParent(arguments)
	},
	init : function (b) {
		b.on("destroy", this.cleanUp, this);
		var a = b.getSchedulingView(),
		c = b.lockedGrid.getView();
		if (this.triggerEvent === "itemcontextmenu") {
			c.on("itemcontextmenu", this.onItemContextMenu, this);
			a.on("itemcontextmenu", this.onItemContextMenu, this)
		}
		a.on("taskcontextmenu", this.onTaskContextMenu, this);
		a.on("containercontextmenu", this.onContainerContextMenu, this);
		c.on("containercontextmenu", this.onContainerContextMenu, this);
		this.grid = b
	},
	populateDependencyMenu : function (f) {
		var d = this.grid,
		b = d.getTaskStore(),
		e = this.rec.getAllDependencies(),
		a = d.dependencyStore;
		f.removeAll();
		if (e.length === 0) {
			return false
		}
		var c = this.rec.getId() || this.rec.internalId;
		Ext.each(e, function (i) {
			var h = i.getSourceId(),
			g = b.getById(h == c ? i.getTargetId() : h);
			if (g) {
				f.add({
					depId : i.internalId,
					text : Ext.util.Format.ellipsis(Ext.String.htmlEncode(g.getName()), 30),
					scope : this,
					handler : function (k) {
						var j;
						a.each(function (l) {
							if (l.internalId == k.depId) {
								j = l;
								return false
							}
						});
						a.remove(j)
					}
				})
			}
		}, this)
	},
	onDependencyMouseOver : function (d, a, b) {
		if (a) {
			var c = this.grid.getSchedulingView();
			if (this.lastHighlightedItem) {
				c.unhighlightDependency(this.lastHighlightedItem.depId)
			}
			this.lastHighlightedItem = a;
			c.highlightDependency(a.depId)
		}
	},
	onDependencyMouseOut : function (b, a) {
		if (this.lastHighlightedItem) {
			this.grid.getSchedulingView().unhighlightDependency(this.lastHighlightedItem.depId)
		}
	},
	cleanUp : function () {
		this.destroy()
	},
	onTaskContextMenu : function (b, a, c) {
		this.activateMenu(a, c)
	},
	onItemContextMenu : function (b, a, d, c, f) {
		this.activateMenu(a, f)
	},
	onContainerContextMenu : function (a, b) {
		this.activateMenu(null, b)
	},
	activateMenu : function (b, a) {
		if (this.grid.isReadOnly() || this.grid.taskStore.getRootNode() === b) {
			return
		}
		a.stopEvent();
		this.rec = b;
		this.configureMenuItems();
		this.showAt(a.getXY())
	},
	configureMenuItems : function () {
		if (this.grid.taskEditor && !this.taskEditorInjected) {
			this.insert(0, {
				text : this.L("taskInformation"),
				requiresTask : true,
				handler : function () {
					this.grid.taskEditor.showTask(this.rec)
				},
				scope : this
			});
			this.taskEditorInjected = true
		}
		var b = this.query("[requiresTask]");
		var c = this.rec;
		Ext.each(b, function (e) {
			e.setDisabled(!c)
		});
		var a = this.query("[isDependenciesMenu]")[0];
		if (c && a) {
			a.setDisabled(!c.getAllDependencies().length)
		}
		var d = this.down("#toggleMilestone");
		if (c && d) {
			d.setText(c.isMilestone() ? this.L("convertToRegular") : this.L("convertToMilestone"))
		}
	},
	copyTask : function (c) {
		var b = this.grid.getTaskStore().model;
		var a = new b({
				leaf : true
			});
		a.setPercentDone(0);
		a.setName(this.L("newTaskText", this.texts));
		a.set(a.startDateField, (c && c.getStartDate()) || null);
		a.set(a.endDateField, (c && c.getEndDate()) || null);
		a.set(a.durationField, (c && c.getDuration()) || null);
		a.set(a.durationUnitField, (c && c.getDurationUnit()) || "d");
		return a
	},
	addTaskAbove : function (a) {
		var b = this.rec;
		if (b) {
			b.addTaskAbove(a)
		} else {
			this.grid.taskStore.getRootNode().appendChild(a)
		}
	},
	addTaskBelow : function (a) {
		var b = this.rec;
		if (b) {
			b.addTaskBelow(a)
		} else {
			this.grid.taskStore.getRootNode().appendChild(a)
		}
	},
	deleteTask : function () {
		var a = this.grid.getSelectionModel().selected;
		this.grid.taskStore.remove(a.getRange())
	},
	editLeftLabel : function () {
		this.grid.getSchedulingView().editLeftLabel(this.rec)
	},
	editRightLabel : function () {
		this.grid.getSchedulingView().editRightLabel(this.rec)
	},
	addTaskAboveAction : function () {
		this.addTaskAbove(this.copyTask(this.rec))
	},
	addTaskBelowAction : function () {
		this.addTaskBelow(this.copyTask(this.rec))
	},
	addSubtask : function () {
		var a = this.rec;
		a.addSubtask(this.copyTask(a))
	},
	addSuccessor : function () {
		var a = this.rec;
		a.addSuccessor(this.copyTask(a))
	},
	addPredecessor : function () {
		var a = this.rec;
		a.addPredecessor(this.copyTask(a))
	},
	addMilestone : function () {
		var b = this.rec,
		a = this.copyTask(b);
		b.addTaskBelow(a);
		a.setStartEndDate(b.getEndDate(), b.getEndDate())
	},
	toggleMilestone : function () {
		if (this.rec.isMilestone()) {
			this.rec.convertToRegular()
		} else {
			this.rec.convertToMilestone()
		}
	}
});
Ext.define("Gnt.plugin.Export", {
	extend : "Sch.plugin.Export",
	alias : "plugin.gantt_export",
	alternateClassName : "Gnt.plugin.PdfExport",
	showExportDialog : function () {
		this.exportDialogConfig.scrollerDisabled = true;
		this.callParent(arguments)
	},
	getExportJsonHtml : function (e, h) {
		var c = this.scheduler.getSchedulingView(),
		d = c.dependencyView,
		a = d.painter.getDependencyTplData(c.dependencyStore.getRange()),
		f = d.lineTpl.apply(a),
		b = h.config,
		g;
		if (!b.singlePageExport) {
			g = {
				dependencies : f,
				rowsAmount : e.rowsAmount,
				columnsAmountNormal : e.columnsAmountNormal,
				columnsAmountLocked : e.columnsAmountLocked,
				timeColumnWidth : e.timeColumnWidth,
				lockedGridWidth : e.lockedGridWidth,
				rowHeight : e.rowHeight
			}
		} else {
			e = {};
			g = {
				dependencies : f,
				singlePageExport : true
			}
		}
		g.lockedColumnPages = e.lockedColumnPages;
		e.panelHTML = g;
		return this.callParent(arguments)
	},
	getRealSize : function () {
		var a = this.callParent(arguments);
		a.width += this.scheduler.down("splitter").getWidth();
		return a
	},
	resizePanelHTML : function (c) {
		var h = this.callParent(arguments),
		e = h.select(".sch-dependencyview-ct").first(),
		g = h.select("." + Ext.baseCSSPrefix + "splitter").first(),
		b = 0,
		f = 0,
		a,
		d;
		b = c.skippedColsBefore * c.timeColumnWidth;
		if (!c.singlePageExport) {
			f = c.k * c.rowsAmount * c.rowHeight;
			a = c.lockedColumnPages ? c.lockedColumnPages.length : 0;
			d = c.i;
			if (a) {
				if (d >= a - 1) {
					var j = d - a + 1;
					b += (j === a - 1) ? c.timeColumnWidth * c.columnsAmountLocked : c.timeColumnWidth * c.columnsAmountLocked + (j - 1) * c.timeColumnWidth * c.columnsAmountNormal
				} else {
					g && g.hide()
				}
			} else {
				if (d) {
					b += (d - 1) * c.timeColumnWidth * c.columnsAmountNormal + c.timeColumnWidth * c.columnsAmountLocked
				}
			}
		}
		e.dom.innerHTML = c.dependencies;
		e.applyStyles({
			top : -f + "px",
			left : -b + "px"
		});
		g && g.setHeight("100%");
		return h
	}
});
Ext.define("Gnt.plugin.Printable", {
	extend : "Sch.plugin.Printable",
	alias : "plugin.gantt_printable",
	getGridContent : function (e) {
		var j = e.getSchedulingView();
		j._print = true;
		var a = this.callParent(arguments),
		h = j.dependencyView,
		n = h.painter.getDependencyTplData(j.dependencyStore.getRange()),
		d = '<div class="' + h.containerEl.dom.className + '">' + h.lineTpl.apply(n) + "</div>",
		m = a.normalRows;
		if (Ext.select(".sch-gantt-critical-chain").first()) {
			var b = Ext.DomHelper.createDom({
					tag : "div",
					html : d
				});
			b = Ext.get(b);
			var p = Ext.DomHelper.createDom({
					tag : "div",
					html : m
				});
			p = Ext.get(p);
			var q = j.getCriticalPaths(),
			c = j.dependencyStore,
			o,
			g,
			f,
			k;
			Ext.each(q, function (i) {
				for (g = 0, f = i.length; g < f; g++) {
					o = i[g];
					this.highlightTask(o, e, p);
					if (g < (f - 1)) {
						k = c.getAt(c.findBy(function (l) {
									return l.getTargetId() === (o.getId() || o.internalId) && l.getSourceId() === (i[g + 1].getId() || i[g + 1].internalId)
								}));
						this.highlightDependency(k, b, h)
					}
				}
			}, this);
			m = p.getHTML();
			d = b.getHTML()
		}
		a.normalRows = d + m;
		delete j._print;
		return a
	},
	highlightTask : function (b, a, e) {
		var d = a.getSchedulingView().getElementFromEventRecord(b),
		c = d.id;
		if (d) {
			e.select("#" + c).first().parent("tr").addCls("sch-gantt-task-highlighted")
		}
	},
	highlightDependency : function (c, b, a) {
		var d = c instanceof Ext.data.Model ? c.internalId : c;
		return b.select(".sch-dep-" + d).addCls(a.selectedCls)
	}
});
Ext.define("Gnt.view.DependencyPainter", {
	ganttView : null,
	rowHeight : null,
	topArrowOffset : 8,
	arrowOffset : 8,
	lineWidth : 2,
	xOffset : 6,
	constructor : function (a) {
		a = a || {};
		Ext.apply(this, a)
	},
	setRowHeight : function (a) {
		this.rowHeight = a
	},
	getTaskBox : function (s) {
		var i = Sch.util.Date,
		k = s.getStartDate(),
		r = s.getEndDate(),
		o = this.ganttView,
		n = o.bufferedRenderer,
		f = o.timeAxis.getStart(),
		d = o.timeAxis.getEnd();
		if (!s.isVisible() || !k || !r || !i.intersectSpans(k, r, f, d)) {
			return null
		}
		if (o.store.indexOf(s) < 0) {
			var m = o.taskStore;
			if (!n) {
				return null
			}
			if (m.isTreeFiltered() && !m.lastTreeFilter.filter.call(m.lastTreeFilter.scope || m, s)) {
				return null
			}
		}
		var g,
		c = o.getXFromDate(i.max(k, f)),
		b = o.getXFromDate(i.min(r, d)),
		a = o.getNodeByRecord(s);
		if (a || n) {
			var v = o.getXOffset(s),
			p,
			j,
			w = s.isMilestone(),
			u = true;
			if (c > v) {
				c -= v
			}
			b += v;
			if (!w && Ext.isIE) {
				if ((Ext.isIE6 || Ext.isIE7 || Ext.isIE8) && Ext.isIEQuirks) {
					b += 1;
					c -= 2
				}
			}
			var h = o.el;
			var l = h.getScroll().top;
			if (a) {
				var t = o.getEventNodeByRecord(s);
				if (!t) {
					return null
				}
				g = Ext.fly(t).getOffsetsTo(h);
				p = g[1] + l + (w && Ext.isIE8 ? 3 : 0);
				j = p + Ext.fly(t).getHeight();
				if (w) {
					b += 1
				}
			} else {
				var q = o.all.elements;
				var e = o.store.getAt(o.all.startIndex);
				if (s.isAbove(e)) {
					a = q[o.all.startIndex];
					g = Ext.fly(a).getOffsetsTo(h);
					g[1] -= o.getRowHeight()
				} else {
					a = q[o.all.endIndex];
					g = Ext.fly(a).getOffsetsTo(h);
					g[1] += o.getRowHeight()
				}
				p = g[1] + l;
				j = p + this.rowHeight;
				u = false
			}
			return {
				top : p,
				end : b,
				bottom : j,
				start : c,
				rendered : u
			}
		}
	},
	getRenderData : function (g) {
		var f = g.getSourceTask(),
		d = g.getTargetTask();
		if (!f || f.stores.length === 0 || !d || d.stores.length === 0) {
			return null
		}
		var a = this.getTaskBox(f);
		var e = this.getTaskBox(d);
		var c = this.ganttView;
		if (c.bufferRender && a && !a.rendered && e && !e.rendered) {
			var h = c.store.getAt(c.all.startIndex);
			var b = c.store.getAt(c.all.endIndex);
			if ((f.isAbove(h) && d.isAbove(h)) || (b.isAbove(f) && b.isAbove(d))) {
				return null
			}
		}
		return {
			fromBox : a,
			toBox : e
		}
	},
	getDependencyTplData : function (o) {
		var j = this,
		m = j.ganttView;
		if (o instanceof Ext.data.Model) {
			o = [o]
		}
		if (o.length === 0 || m.store.getCount() === 0) {
			return
		}
		var a = [],
		n,
		h,
		f,
		k,
		g,
		b;
		for (var e = 0, c = o.length; e < c; e++) {
			b = o[e];
			var d = this.getRenderData(b);
			if (d) {
				k = d.fromBox;
				g = d.toBox;
				if (k && g) {
					n = j.getLineCoordinates(k, g, b);
					if (n) {
						a.push({
							dependency : b,
							id : b.internalId,
							cls : b.getCls(),
							lineCoordinates : n
						})
					}
				}
			}
		}
		return a
	},
	getLineCoordinates : function (q, o, j) {
		var f,
		r,
		p = [0, q.top - 1 + ((q.bottom - q.top) / 2)],
		a = [0, o.top - 1 + ((o.bottom - o.top) / 2)],
		b = a[1] > p[1],
		g = Gnt.model.Dependency.Type,
		d = this.arrowOffset + this.xOffset,
		c = j.getType(),
		m = [],
		s = j.getTargetTask().isMilestone(),
		k,
		h,
		t;
		switch (c) {
		case g.StartToEnd:
			p[0] = q.start;
			a[0] = o.end + d;
			f = "l";
			r = "r";
			break;
		case g.StartToStart:
			p[0] = q.start;
			a[0] = o.start - d;
			f = "l";
			r = "l";
			break;
		case g.EndToStart:
			p[0] = q.end;
			a[0] = o.start - d;
			f = "r";
			r = "l";
			break;
		case g.EndToEnd:
			p[0] = q.end;
			a[0] = o.end + d;
			f = "r";
			r = "r";
			break;
		default:
			throw "Invalid dependency type: " + j.getType()
		}
		m.push(p);
		var n = p[0] + (f === "r" ? this.xOffset : -this.xOffset);
		if (b && c === g.EndToStart && q.end < (o.start + 5)) {
			k = Math.min(o.start, o.end) + this.xOffset;
			m.push([k, p[1]]);
			m.push([k, o.top - this.arrowOffset - (s ? 2 : 0)])
		} else {
			if (f !== r && ((f === "r" && n > a[0]) || (f === "l" && n < a[0]))) {
				h = o[r === "l" ? "start" : "end"];
				t = a[1] + (b ? -1 : 1) * (this.rowHeight / 2);
				m.push([n, p[1]]);
				m.push([n, t]);
				m.push([a[0], t]);
				m.push(a);
				m.push([h + (a[0] < h ? -this.arrowOffset : this.arrowOffset) - (s && r === "l" ? 2 : 0), a[1]])
			} else {
				h = o[r === "l" ? "start" : "end"];
				if (f === "r") {
					k = Math.max(n, a[0])
				} else {
					k = Math.min(n, a[0])
				}
				m.push([k, p[1]]);
				m.push([k, a[1]]);
				m.push([h + (k < h ? -this.arrowOffset : this.arrowOffset) - (s && r === "l" ? 2 : 0), a[1]])
			}
		}
		var e = [];
		for (var l = 0; l < m.length - 1; l++) {
			e.push({
				x1 : m[l][0],
				y1 : m[l][1],
				x2 : m[l + 1][0],
				y2 : m[l + 1][1]
			})
		}
		return e
	}
});
Ext.define("Gnt.view.Dependency", {
	extend : "Ext.util.Observable",
	requires : ["Gnt.feature.DependencyDragDrop", "Gnt.view.DependencyPainter"],
	lineWidth : 1,
	dragZoneConfig : null,
	dropZoneConfig : null,
	dependencyPainterClass : "Gnt.view.DependencyPainter",
	containerEl : null,
	ganttView : null,
	painter : null,
	taskStore : null,
	store : null,
	dnd : null,
	lineTpl : null,
	renderTimer : null,
	enableDependencyDragDrop : true,
	renderAllDepsBuffered : false,
	dependencyCls : "sch-dependency",
	selectedCls : "sch-dependency-selected",
	constructor : function (a) {
		this.callParent(arguments);
		var c = this.ganttView;
		c.on({
			refresh : this.renderAllDependenciesBuffered,
			bufferedrefresh : this.renderAllDependenciesBuffered,
			itemupdate : this.onTaskUpdated,
			scope : this
		});
		this.bindTaskStore(c.getTaskStore());
		this.bindDependencyStore(a.store);
		if (!this.lineTpl) {
			var d = this.rtl;
			var b = d ? "right" : "left";
			this.lineTpl = Ext.create("Ext.XTemplate", '<tpl for=".">' + Ext.String.format('<tpl for="lineCoordinates"><div class="{0} {[ parent.dependency.isHighlighted ? "{1}" : "" ]} {[values.x1==values.x2 ? "sch-dependency-line-v" : "sch-dependency-line-h"]} {lineCls} sch-dep-{parent.id} {0}-line {[this.getSuffixedCls(parent.cls, "-line")]}" style="' + b + ":{[Math.min(values.x1, values.x2)]}px;top:{[Math.min(values.y1, values.y2)]}px;width:{[Math.abs(values.x1-values.x2)+" + this.lineWidth + "]}px;height:{[Math.abs(values.y1-values.y2)+" + this.lineWidth + ']}px"></div></tpl><div style="' + b + ':{[values.lineCoordinates[values.lineCoordinates.length - 1].x2]}px;top:{[values.lineCoordinates[values.lineCoordinates.length - 1].y2]}px"    class="{0}-arrow-ct {0} {[ values.dependency.isHighlighted ? "{1}" : "" ]} sch-dep-{id} {[this.getSuffixedCls(values.cls, "-arrow-ct")]}"><img src="' + Ext.BLANK_IMAGE_URL + '" class="{0}-arrow {0}-arrow-{[this.getArrowDirection(values.lineCoordinates)]} {[this.getSuffixedCls(values.cls, "-arrow")]}" /></div>', this.dependencyCls, this.selectedCls) + "</tpl>", {
					compiled : true,
					disableFormats : true,
					getArrowDirection : function (f) {
						var e = f[f.length - 1];
						if (e.y2 < e.y1) {
							return "up"
						}
						if (e.x1 === e.x2) {
							return "down"
						} else {
							if ((!d && e.x1 > e.x2) || (d && e.x1 < e.x2)) {
								return "left"
							} else {
								return "right"
							}
						}
					},
					getSuffixedCls : function (e, f) {
						if (e && e.indexOf(" ") != -1) {
							return e.replace(/^\s*(.*)\s*$/, "$1").split(/\s+/).join(f + " ") + f
						} else {
							return e + f
						}
					}
				})
		}
		this.painter = Ext.create(this.dependencyPainterClass, Ext.apply({
					rowHeight : c.getRowHeight(),
					ganttView : c
				}, a));
		this.addEvents("beforednd", "dndstart", "drop", "afterdnd", "dependencyclick", "dependencycontextmenu", "dependencydblclick", "refresh");
		if (this.enableDependencyDragDrop) {
			this.dnd = Ext.create("Gnt.feature.DependencyDragDrop", {
					el : c.getEl(),
					rtl : c.rtl,
					ganttView : c,
					dragZoneConfig : this.dragZoneConfig,
					dropZoneConfig : this.dropZoneConfig,
					dependencyStore : this.store
				});
			this.dnd.on("drop", this.onDependencyDrop, this);
			this.relayEvents(this.dnd, ["beforednd", "dndstart", "afterdnd", "drop"])
		}
		this.containerEl = this.containerEl.createChild({
				cls : "sch-dependencyview-ct " + (this.lineWidth === 1 ? " sch-dependencyview-thin " : "")
			});
		this.ganttView.mon(this.containerEl, {
			dblclick : this.onDependencyClick,
			click : this.onDependencyClick,
			contextmenu : this.onDependencyClick,
			scope : this,
			delegate : "." + this.dependencyCls
		});
		if (c.rendered) {
			this.renderAllDependenciesBuffered()
		}
	},
	bindDependencyStore : function (a) {
		this.depStoreListeners = {
			refresh : this.renderAllDependenciesBuffered,
			clear : this.renderAllDependenciesBuffered,
			load : this.renderAllDependenciesBuffered,
			add : this.onDependencyAdd,
			update : this.onDependencyUpdate,
			remove : this.onDependencyDelete,
			scope : this
		};
		a.on(this.depStoreListeners);
		this.store = a
	},
	unBindDependencyStore : function () {
		if (this.depStoreListeners) {
			this.store.un(this.depStoreListeners)
		}
	},
	bindTaskStore : function (a) {
		var b = this.ganttView;
		this.taskStoreListeners = {
			cascade : this.onTaskStoreCascade,
			beforefill : this.onRootFillStart,
			remove : this.renderAllDependenciesBuffered,
			insert : this.renderAllDependenciesBuffered,
			append : this.renderAllDependenciesBuffered,
			move : this.renderAllDependenciesBuffered,
			sort : this.renderAllDependenciesBuffered,
			scope : this
		};
		Ext.apply(this.taskStoreListeners, {
			expand : this.renderAllDependenciesBuffered,
			collapse : this.renderAllDependenciesBuffered
		});
		a.on(this.taskStoreListeners);
		this.taskStore = a
	},
	onTaskStoreCascade : function (a, b) {
		if (b && b.nbrAffected > 0) {
			this.renderAllDependenciesBuffered()
		}
	},
	unBindTaskStore : function (a) {
		a = a || this.taskStore;
		if (!a) {
			return
		}
		if (this.ganttViewListeners) {
			this.ganttView.un(this.ganttViewListeners)
		}
		a.un(this.taskStoreListeners)
	},
	onRootFillStart : function () {
		var a = this.taskStore;
		this.unBindTaskStore(a);
		this.mon(this.taskStore, "fillcomplete", function () {
			this.bindTaskStore(a)
		}, this, {
			single : true
		})
	},
	onDependencyClick : function (b, a) {
		var c = this.getRecordForDependencyEl(a);
		this.fireEvent("dependency" + b.type, this, c, b, a)
	},
	highlightDependency : function (a) {
		if (!(a instanceof Ext.data.Model)) {
			a = this.getDependencyRecordByInternalId(a)
		}
		if (a) {
			a.isHighlighted = true;
			this.getElementsForDependency(a).addCls(this.selectedCls)
		}
	},
	unhighlightDependency : function (a) {
		if (!(a instanceof Ext.data.Model)) {
			a = this.getDependencyRecordByInternalId(a)
		}
		if (a) {
			a.isHighlighted = false;
			this.getElementsForDependency(a).removeCls(this.selectedCls)
		}
	},
	getElementsForDependency : function (a) {
		var b = a instanceof Ext.data.Model ? a.internalId : a;
		return this.containerEl.select(".sch-dep-" + b)
	},
	depRe : new RegExp("sch-dep-([^\\s]+)"),
	getDependencyRecordByInternalId : function (d) {
		var c,
		b,
		a;
		for (b = 0, a = this.store.getCount(); b < a; b++) {
			c = this.store.getAt(b);
			if (c.internalId == d) {
				return c
			}
		}
		return null
	},
	getRecordForDependencyEl : function (c) {
		var a = c.className.match(this.depRe),
		d = null;
		if (a && a[1]) {
			var b = a[1];
			d = this.getDependencyRecordByInternalId(b)
		}
		return d
	},
	renderAllDependenciesBuffered : function () {
		var b = this;
		var a = this.ganttView.up("{isHidden()}");
		if (a) {
			clearTimeout(b.renderTimer);
			b.renderTimer = null;
			a.on("show", this.renderAllDependenciesBuffered, this, {
				single : true
			});
			return
		}
		if (b.renderTimer) {
			return
		}
		this.containerEl.update("");
		this.renderTimer = setTimeout(function () {
				b.renderTimer = null;
				if (!b.ganttView.isDestroyed) {
					b.renderAllDependencies()
				}
			}, 0)
	},
	renderAllDependencies : function () {
		if (!this.containerEl.dom) {
			return
		}
		this.containerEl.update("");
		this.renderDependencies(this.store.data.items);
		this.fireEvent("refresh", this)
	},
	getDependencyElements : function () {
		return this.containerEl.select("." + this.dependencyCls)
	},
	renderDependencies : function (b) {
		if (b) {
			var a = this.painter.getDependencyTplData(b);
			this.lineTpl[Ext.isIE ? "insertFirst" : "append"](this.containerEl, a)
		}
	},
	renderTaskDependencies : function (d) {
		var c = [];
		if (d instanceof Ext.data.Model) {
			d = [d]
		}
		for (var a = 0, b = d.length; a < b; a++) {
			c = c.concat(d[a].getAllDependencies())
		}
		this.renderDependencies(c)
	},
	onDependencyUpdate : function (b, a) {
		this.removeDependencyElements(a, false);
		this.renderDependencies(a)
	},
	onDependencyAdd : function (a, b) {
		this.renderDependencies(b)
	},
	removeDependencyElements : function (a, b) {
		if (b !== false) {
			this.getElementsForDependency(a).fadeOut({
				remove : true
			})
		} else {
			this.getElementsForDependency(a).remove()
		}
	},
	onDependencyDelete : function (b, a) {
		this.removeDependencyElements(a)
	},
	dimEventDependencies : function (a) {
		this.containerEl.select(this.depRe + a).setOpacity(0.2)
	},
	clearSelectedDependencies : function () {
		this.containerEl.select("." + this.selectedCls).removeCls(this.selectedCls);
		this.store.each(function (a) {
			a.isHighlighted = false
		})
	},
	onTaskUpdated : function (a) {
		if (!this.taskStore.cascading && (!a.previous || a.startDateField in a.previous || a.endDateField in a.previous)) {
			this.updateDependencies(a)
		}
	},
	updateDependencies : function (b) {
		if (b instanceof Ext.data.Model) {
			b = [b]
		}
		var a = this;
		Ext.each(b, function (c) {
			Ext.each(c.getAllDependencies(), function (d) {
				a.removeDependencyElements(d, false)
			})
		});
		this.renderTaskDependencies(b)
	},
	onNewDependencyCreated : function () {},
	onDependencyDrop : function (f, d, b, e) {
		var c = this.store;
		var a = new c.model();
		a.setSourceId(d);
		a.setTargetId(b);
		a.setType(e);
		if (c.isValidDependency(a) && this.onNewDependencyCreated(a) !== false) {
			c.add(a)
		}
	},
	destroy : function () {
		if (this.dnd) {
			this.dnd.destroy()
		}
		this.unBindTaskStore();
		this.unBindDependencyStore()
	},
	setRowHeight : function (a, b) {
		this.rowHeight = a;
		this.painter.setRowHeight(a);
		if (!b) {
			this.renderAllDependencies()
		}
	}
});
Ext.define("Gnt.view.Gantt", {
	extend : "Sch.view.TimelineGridView",
	alias : ["widget.ganttview"],
	requires : ["Ext.dd.ScrollManager", "Gnt.view.Dependency", "Gnt.model.Task", "Gnt.template.Task", "Gnt.template.ParentTask", "Gnt.template.Milestone", "Gnt.feature.TaskDragDrop", "Gnt.feature.ProgressBarResize", "Gnt.feature.TaskResize", "Sch.view.Horizontal"],
	uses : ["Gnt.feature.LabelEditor", "Gnt.feature.DragCreator"],
	mixins : ["Sch.mixin.FilterableTreeView"],
	_cmpCls : "sch-ganttview",
	barMargin : 4,
	scheduledEventName : "task",
	trackOver : false,
	toggleOnDblClick : false,
	parentTaskOffset : 6,
	eventSelector : ".sch-gantt-item",
	eventWrapSelector : ".sch-event-wrap",
	progressBarResizer : null,
	taskResizer : null,
	taskDragDrop : null,
	dragCreator : null,
	dependencyView : null,
	resizeConfig : null,
	createConfig : null,
	dragDropConfig : null,
	progressBarResizeConfig : null,
	dependencyViewConfig : null,
	externalGetRowClass : null,
	constructor : function (a) {
		a = a || {};
		if (a) {
			this.externalGetRowClass = a.getRowClass;
			delete a.getRowClass
		}
		this.addEvents("taskclick", "taskdblclick", "taskcontextmenu", "beforetaskresize", "taskresizestart", "partialtaskresize", "aftertaskresize", "beforeprogressbarresize", "progressbarresizestart", "afterprogressbarresize", "beforetaskdrag", "taskdragstart", "beforetaskdropfinalize", "taskdrop", "aftertaskdrop", "labeledit_beforestartedit", "labeledit_beforecomplete", "labeledit_complete", "beforedependencydrag", "dependencydragstart", "dependencydrop", "afterdependencydragdrop", "beforedragcreate", "dragcreatestart", "dragcreateend", "afterdragcreate", "scheduleclick", "scheduledblclick", "schedulecontextmenu");
		this.callParent(arguments);
		this.initTreeFiltering();
		this.addCls("sch-ganttview")
	},
	onRender : function () {
		this.configureLabels();
		this.setupGanttEvents();
		this.setupTemplates();
		this.callParent(arguments)
	},
	getDependencyStore : function () {
		return this.dependencyStore
	},
	configureFeatures : function () {
		if (this.enableProgressBarResize !== false) {
			this.progressBarResizer = Ext.create("Gnt.feature.ProgressBarResize", Ext.apply({
						ganttView : this
					}, this.progressBarResizeConfig || {}));
			this.on({
				beforeprogressbarresize : this.onBeforeTaskProgressBarResize,
				progressbarresizestart : this.onTaskProgressBarResizeStart,
				afterprogressbarresize : this.onTaskProgressBarResizeEnd,
				scope : this
			})
		}
		if (this.resizeHandles !== "none") {
			this.taskResizer = Ext.create("Gnt.feature.TaskResize", Ext.apply({
						ganttView : this,
						validatorFn : this.resizeValidatorFn || Ext.emptyFn,
						validatorFnScope : this
					}, this.resizeConfig || {}));
			this.on({
				beforedragcreate : this.onBeforeDragCreate,
				beforetaskresize : this.onBeforeTaskResize,
				taskresizestart : this.onTaskResizeStart,
				aftertaskresize : this.onTaskResizeEnd,
				scope : this
			})
		}
		if (this.enableTaskDragDrop) {
			this.taskDragDrop = Ext.create("Gnt.feature.TaskDragDrop", this.ownerCt.el, Ext.apply({
						gantt : this,
						validatorFn : this.dndValidatorFn || Ext.emptyFn,
						validatorFnScope : this
					}, this.dragDropConfig));
			this.on({
				beforetaskdrag : this.onBeforeTaskDrag,
				taskdragstart : this.onDragDropStart,
				aftertaskdrop : this.onDragDropEnd,
				scope : this
			})
		}
		if (this.enableDragCreation) {
			this.dragCreator = Ext.create("Gnt.feature.DragCreator", Ext.apply({
						ganttView : this,
						validatorFn : this.createValidatorFn || Ext.emptyFn,
						validatorFnScope : this
					}, this.createConfig))
		}
	},
	getTemplateForTask : function (b, a) {
		if (b.isMilestone(a)) {
			return this.milestoneTemplate
		}
		if (b.isLeaf()) {
			return this.eventTemplate
		}
		return this.parentEventTemplate
	},
	columnRenderer : function (z, u, l) {
		var m = l.getStartDate(),
		p = this.timeAxis,
		v = Sch.util.Date,
		b = {},
		G = "",
		r = "",
		h = p.getStart(),
		g = p.getEnd(),
		J = l.isMilestone(),
		C = l.isLeaf(),
		q,
		s,
		y;
		if (m) {
			var A = l.getEndDate() || Sch.util.Date.add(m, l.getDurationUnit() || Sch.util.Date.DAY, 1),
			e = Sch.util.Date.intersectSpans(m, A, h, g);
			if (e) {
				y = A > g;
				s = v.betweenLesser(m, h, g);
				var F = Math.floor(this.getXFromDate(s ? m : h)),
				d = Math.floor(this.getXFromDate(y ? g : A)),
				f = J ? 0 : d - F,
				w = this.leftLabelField,
				k = this.rightLabelField,
				x = this.topLabelField,
				j = this.bottomLabelField,
				I = this.getTemplateForTask(l);
				if (!J && !C) {
					if (y) {
						f += this.parentTaskOffset
					} else {
						f += 2 * this.parentTaskOffset
					}
				}
				b = Ext.apply({}, {
						id : l.internalId,
						offset : J ? (d || F) - this.getXOffset(l) : F,
						width : Math.max(1, f),
						ctcls : "",
						cls : "",
						print : this._print,
						record : l,
						percentDone : Math.min(l.getPercentDone() || 0, 100)
					}, l.data);
				q = this.eventRenderer.call(this.eventRendererScope || this, l, b, l.store) || {};
				if (w) {
					b.leftLabel = Ext.util.Format.htmlEncode(w.renderer.call(w.scope || this, l.data[w.dataIndex], l))
				}
				if (k) {
					b.rightLabel = Ext.util.Format.htmlEncode(k.renderer.call(k.scope || this, l.data[k.dataIndex], l))
				}
				if (x) {
					b.topLabel = Ext.util.Format.htmlEncode(x.renderer.call(x.scope || this, l.data[x.dataIndex], l))
				}
				if (j) {
					b.bottomLabel = Ext.util.Format.htmlEncode(j.renderer.call(j.scope || this, l.data[j.dataIndex], l))
				}
				Ext.apply(b, q);
				var i = " sch-event-resizable-" + l.getResizable();
				if (J) {
					b.side = Math.round((this.enableBaseline ? 0.4 : 0.5) * this.getRowHeight());
					r += " sch-gantt-milestone"
				} else {
					b.width = Math.max(1, f);
					if (y) {
						r += " sch-event-endsoutside "
					}
					if (!s) {
						r += " sch-event-startsoutside "
					}
					if (C) {
						r += " sch-gantt-task"
					} else {
						r += " sch-gantt-parent-task"
					}
				}
				if (l.dirty) {
					i += " sch-dirty "
				}
				if (l.isDraggable() === false) {
					i += " sch-event-fixed "
				}
				b.cls = (b.cls || "") + (l.getCls() || "") + i;
				b.ctcls += " " + r;
				G += I.apply(b)
			}
		}
		if (this.enableBaseline) {
			var t = l.getBaselineStartDate(),
			a = l.getBaselineEndDate();
			if (!q) {
				q = this.eventRenderer.call(this, l, b, l.store) || {}

			}
			if (t && a && Sch.util.Date.intersectSpans(t, a, h, g)) {
				y = a > g;
				s = v.betweenLesser(t, h, g);
				var c = l.isBaselineMilestone(),
				o = Math.floor(this.getXFromDate(s ? t : h)),
				n = Math.floor(this.getXFromDate(y ? g : a)),
				E = c ? 0 : n - o,
				B = this.getTemplateForTask(l, true),
				H = {
					progressBarStyle : q.baseProgressBarStyle || "",
					id : l.internalId + "-base",
					percentDone : l.getBaselinePercentDone(),
					offset : c ? (n || o) - this.getXOffset(l, true) : o,
					print : this._print,
					width : Math.max(1, E),
					baseline : true
				};
				r = "";
				if (c) {
					H.side = Math.round(0.4 * this.getRowHeight());
					r = "sch-gantt-milestone-baseline sch-gantt-baseline-item"
				} else {
					if (l.isLeaf()) {
						r = "sch-gantt-task-baseline sch-gantt-baseline-item"
					} else {
						r = "sch-gantt-parenttask-baseline sch-gantt-baseline-item"
					}
				}
				if (y) {
					r += " sch-event-endsoutside "
				}
				if (!s) {
					r += " sch-event-startsoutside "
				}
				H.ctcls = r + " " + (q.basecls || "");
				G += B.apply(H)
			}
		}
		return G
	},
	setupTemplates : function () {
		var a = {
			leftLabel : this.leftLabelField,
			rightLabel : this.rightLabelField,
			topLabel : this.topLabelField,
			bottomLabel : this.bottomLabelField,
			prefix : this.eventPrefix,
			resizeHandles : this.resizeHandles,
			enableDependencyDragDrop : this.enableDependencyDragDrop !== false,
			enableProgressBarResize : this.enableProgressBarResize,
			rtl : this.rtl
		};
		var b;
		if (!this.eventTemplate) {
			b = this.taskBodyTemplate ? Ext.apply({
					innerTpl : this.taskBodyTemplate
				}, a) : a;
			this.eventTemplate = Ext.create("Gnt.template.Task", b)
		}
		if (!this.parentEventTemplate) {
			b = this.parentTaskBodyTemplate ? Ext.apply({
					innerTpl : this.parentTaskBodyTemplate
				}, a) : a;
			this.parentEventTemplate = Ext.create("Gnt.template.ParentTask", b)
		}
		if (!this.milestoneTemplate) {
			b = this.milestoneBodyTemplate ? Ext.apply({
					innerTpl : this.milestoneBodyTemplate
				}, a) : a;
			this.milestoneTemplate = Ext.create("Gnt.template.Milestone", b)
		}
	},
	getDependencyView : function () {
		return this.dependencyView
	},
	getTaskStore : function () {
		return this.taskStore
	},
	initDependencies : function () {
		if (this.dependencyStore) {
			var b = this,
			a = Ext.create("Gnt.view.Dependency", Ext.apply({
						containerEl : b.el,
						ganttView : b,
						enableDependencyDragDrop : b.enableDependencyDragDrop,
						store : b.dependencyStore,
						rtl : b.rtl
					}, this.dependencyViewConfig));
			a.on({
				beforednd : b.onBeforeDependencyDrag,
				dndstart : b.onDependencyDragStart,
				drop : b.onDependencyDrop,
				afterdnd : b.onAfterDependencyDragDrop,
				scope : b
			});
			b.dependencyView = a;
			b.relayEvents(a, ["dependencyclick", "dependencycontextmenu", "dependencydblclick"])
		}
	},
	updateBufferedView : function () {
		this.bufferedRenderer.scrollTo(this.bufferedRenderer.getFirstVisibleRowIndex())
	},
	setupGanttEvents : function () {
		var b = this.getSelectionModel();
		var a = this.taskStore;
		if (this.toggleParentTasksOnClick) {
			this.on({
				taskclick : function (c, d) {
					if (!d.isLeaf() && (!a.isTreeFiltered() || a.allowExpandCollapseWhileFiltered)) {
						d.isExpanded() ? d.collapse() : d.expand()
					}
				}
			})
		}
		if (this.bufferedRenderer) {
			this.getStore().on({
				collapse : this.updateBufferedView,
				expand : this.updateBufferedView,
				scope : this
			})
		}
	},
	configureLabels : function () {
		var a = {
			renderer : function (b) {
				return b
			},
			dataIndex : undefined
		};
		Ext.Array.forEach(["left", "right", "top", "bottom"], function (c) {
			var b = this[c + "LabelField"];
			if (b) {
				if (Ext.isString(b)) {
					b = this[c + "LabelField"] = {
						dataIndex : b
					}
				}
				Ext.applyIf(b, a);
				if (b.editor) {
					b.editor = Ext.create("Gnt.feature.LabelEditor", this, {
							labelPosition : c,
							field : b.editor,
							dataIndex : b.dataIndex
						})
				}
			}
		}, this);
		this.on("labeledit_beforestartedit", this.onBeforeLabelEdit, this)
	},
	onBeforeTaskDrag : function (b, a) {
		return !this.readOnly && a.isDraggable() !== false && (this.allowParentTaskMove || a.isLeaf())
	},
	onDragDropStart : function () {
		if (this.tip) {
			this.tip.disable()
		}
	},
	onDragDropEnd : function () {
		if (this.tip) {
			this.tip.enable()
		}
	},
	onTaskProgressBarResizeStart : function () {
		if (this.tip) {
			this.tip.hide();
			this.tip.disable()
		}
	},
	onTaskProgressBarResizeEnd : function () {
		if (this.tip) {
			this.tip.enable()
		}
	},
	onTaskResizeStart : function () {
		if (this.tip) {
			this.tip.hide();
			this.tip.disable()
		}
	},
	onTaskResizeEnd : function () {
		if (this.tip) {
			this.tip.enable()
		}
	},
	onBeforeDragCreate : function () {
		return !this.readOnly
	},
	onBeforeTaskResize : function (a, b) {
		return !this.readOnly && b.getSchedulingMode() !== "EffortDriven"
	},
	onBeforeTaskProgressBarResize : function () {
		return !this.readOnly
	},
	onBeforeLabelEdit : function () {
		return !this.readOnly
	},
	onBeforeEdit : function () {
		return !this.readOnly
	},
	afterRender : function () {
		this.initDependencies();
		this.callParent(arguments);
		this.el.on("mousemove", this.configureFeatures, this, {
			single : true
		});
		Ext.dd.ScrollManager.register(this.el)
	},
	resolveTaskRecord : function (a) {
		var b = this.findItemByChild(a);
		if (b) {
			return this.getRecord(this.findItemByChild(a))
		}
		return null
	},
	resolveEventRecord : function (a) {
		return this.resolveTaskRecord(a)
	},
	highlightTask : function (c, b) {
		if (!(c instanceof Ext.data.Model)) {
			c = this.taskStore.getById(c)
		}
		if (c) {
			c.isHighlighted = true;
			var e = this.getNode(c);
			if (e) {
				Ext.fly(e).addCls("sch-gantt-task-highlighted")
			}
			if (b !== false) {
				for (var d = 0, a = c.successors.length; d < a; d++) {
					var f = c.successors[d];
					this.highlightDependency(f);
					this.highlightTask(f.getTargetTask(), b)
				}
			}
		}
	},
	unhighlightTask : function (b, e) {
		if (!(b instanceof Ext.data.Model)) {
			b = this.taskStore.getById(b)
		}
		if (b) {
			b.isHighlighted = false;
			Ext.fly(this.getNode(b)).removeCls("sch-gantt-task-highlighted");
			if (e !== false) {
				for (var c = 0, a = b.successors.length; c < a; c++) {
					var d = b.successors[c];
					this.unhighlightDependency(d);
					this.unhighlightTask(d.getTargetTask(), e)
				}
			}
		}
	},
	getRowClass : function (b) {
		var a = "";
		if (b.isHighlighted) {
			a = "sch-gantt-task-highlighted"
		}
		if (this.externalGetRowClass) {
			a += " " + (this.externalGetRowClass.apply(this, arguments) || "")
		}
		return a
	},
	clearSelectedTasksAndDependencies : function () {
		this.getDependencyView().clearSelectedDependencies();
		this.el.select("tr.sch-gantt-task-highlighted").removeCls("sch-gantt-task-highlighted");
		this.taskStore.getRootNode().cascadeBy(function (a) {
			a.isHighlighted = false
		})
	},
	getCriticalPaths : function () {
		return this.taskStore.getCriticalPaths()
	},
	highlightCriticalPaths : function () {
		this.clearSelectedTasksAndDependencies();
		var g = this.getCriticalPaths(),
		c = this.getDependencyView(),
		f = this.dependencyStore,
		e,
		d,
		b,
		a;
		Ext.each(g, function (k) {
			for (d = 0, b = k.length; d < b; d++) {
				e = k[d];
				this.highlightTask(e, false);
				if (d < b - 1) {
					for (var i = 0, h = e.predecessors.length; i < h; i++) {
						if (e.predecessors[i].getSourceId() == k[d + 1].getInternalId()) {
							a = e.predecessors[i];
							break
						}
					}
					c.highlightDependency(a)
				}
			}
		}, this);
		this.addCls("sch-gantt-critical-chain")
	},
	unhighlightCriticalPaths : function () {
		this.el.removeCls("sch-gantt-critical-chain");
		this.clearSelectedTasksAndDependencies()
	},
	getXOffset : function (b, a) {
		var c = 0;
		if (b.isMilestone(a)) {
			c = Math.floor(this.getRowHeight() * Math.sqrt(2) / 4) - 2
		} else {
			if (!b.isLeaf() && !a) {
				c = this.parentTaskOffset
			}
		}
		return c
	},
	onDestroy : function () {
		if (this.dependencyView) {
			this.dependencyView.destroy()
		}
		if (this.rendered) {
			Ext.dd.ScrollManager.unregister(this.el)
		}
		this.callParent(arguments)
	},
	highlightDependency : function (a) {
		this.dependencyView.highlightDependency(a)
	},
	unhighlightDependency : function (a) {
		this.dependencyView.unhighlightDependency(a)
	},
	onBeforeDependencyDrag : function (b, a) {
		return this.fireEvent("beforedependencydrag", this, a)
	},
	onDependencyDragStart : function (a) {
		this.fireEvent("dependencydragstart", this);
		if (this.tip) {
			this.tip.disable()
		}
		this.preventOverCls = true
	},
	onDependencyDrop : function (b, c, a, d) {
		this.fireEvent("dependencydrop", this, this.taskStore.getById(c), this.taskStore.getById(a), d)
	},
	onAfterDependencyDragDrop : function () {
		this.fireEvent("afterdependencydragdrop", this);
		if (this.tip) {
			this.tip.enable()
		}
		this.preventOverCls = false
	},
	getLeftEditor : function () {
		return this.leftLabelField.editor
	},
	getRightEditor : function () {
		return this.rightLabelField.editor
	},
	getTopEditor : function () {
		return this.topLabelField.editor
	},
	getBottomEditor : function () {
		return this.bottomLabelField.editor
	},
	editLeftLabel : function (a) {
		var b = this.leftLabelField && this.leftLabelField.editor;
		if (b) {
			b.edit(a)
		}
	},
	editRightLabel : function (a) {
		var b = this.rightLabelField && this.rightLabelField.editor;
		if (b) {
			b.edit(a)
		}
	},
	editTopLabel : function (a) {
		var b = this.topLabelField && this.topLabelField.editor;
		if (b) {
			b.edit(a)
		}
	},
	editBottomLabel : function (a) {
		var b = this.bottomLabelField && this.bottomLabelField.editor;
		if (b) {
			b.edit(a)
		}
	},
	getOuterElementFromEventRecord : function (a) {
		var b = this.callParent([a]);
		return b && b.up(this.eventWrapSelector) || null
	},
	getDependenciesForTask : function (a) {
		console.warn("`ganttPanel.getDependenciesForTask()` is deprecated, use `task.getAllDependencies()` instead");
		return a.getAllDependencies()
	},
	onAdd : function () {
		Ext.suspendLayouts();
		this.callParent(arguments);
		Ext.resumeLayouts()
	},
	onRemove : function () {
		Ext.suspendLayouts();
		this.callParent(arguments);
		Ext.resumeLayouts()
	},
	onUpdate : function (c, a, b, d) {
		Ext.suspendLayouts();
		this.callParent(arguments);
		Ext.resumeLayouts()
	},
	handleScheduleEvent : function (c) {
		var a = c.getTarget("." + this.timeCellCls, 3);
		if (a) {
			var b = this.findRowByChild(a);
			this.fireEvent("schedule" + c.type, this, this.getDateFromDomEvent(c, "floor"), this.indexOf(b), c)
		}
	},
	scrollEventIntoView : function (d, e, b, m, n) {
		n = n || this;
		var i = this;
		var j = this.taskStore;
		var k = function (p, o) {
			i.up("panel").scrollTask.cancel();
			i.scrollElementIntoView(p, i.el, o, b);
			if (e) {
				if (typeof e === "boolean") {
					p.highlight()
				} else {
					p.highlight(null, e)
				}
			}
			m && m.call(n)
		};
		if (!d.isVisible()) {
			d.bubble(function (o) {
				o.expand()
			})
		}
		var l;
		var c = d.getStartDate();
		var h = d.getEndDate();
		var a = Boolean(c && h);
		if (a) {
			var g = this.timeAxis;
			if (!g.dateInAxis(c) || !g.dateInAxis(h)) {
				var f = g.getEnd() - g.getStart();
				g.setTimeSpan(new Date(c.getTime() - f / 2), new Date(h.getTime() + f / 2))
			}
			l = this.getElementFromEventRecord(d)
		} else {
			l = this.getNode(d);
			if (l) {
				l = Ext.fly(l).down(this.getCellSelector())
			}
		}
		if (l) {
			k(l, a)
		} else {
			if (this.bufferedRenderer) {
				Ext.Function.defer(function () {
					i.bufferedRenderer.scrollTo(j.getIndexInTotalDataset(d), false, function () {
						var o = i.getElementFromEventRecord(d);
						if (o) {
							k(o, true)
						} else {
							m && m.call(n)
						}
					})
				}, 10)
			}
		}
	}
});
Ext.define("Gnt.view.ResourceHistogram", {
	extend : "Sch.view.TimelineGridView",
	alias : "widget.resourcehistogramview",
	requires : ["Ext.XTemplate", "Ext.util.Format", "Sch.util.Date", "Gnt.model.Resource"],
	_cmpCls : "gnt-resourcehistogramview",
	scheduledEventName : "bar",
	eventSelector : ".gnt-resourcehistogram-bar",
	barTpl : null,
	barRenderer : Ext.emptyFn,
	barCls : "gnt-resourcehistogram-bar",
	lineTpl : null,
	lineCls : "gnt-resourcehistogram-line",
	limitLineTpl : null,
	limitLineCls : "gnt-resourcehistogram-limitline",
	limitLineWidth : 1,
	rowHeight : 60,
	labelMode : false,
	labelPercentFormat : "0",
	labelUnitsFormat : "0.0",
	unitHeight : null,
	availableRowHeight : null,
	initComponent : function (a) {
		if (this.barCls) {
			this.eventSelector = "." + this.barCls
		}
		if (!this.barTpl) {
			this.barTpl = new Ext.XTemplate('<tpl for=".">', '<div id="{id}" class="' + this.barCls + ' {cls}" gnt-bar-index="{index}" style="left:{left}px;top:{top}px;height:{height}px;width:{width}px"></div>', "<tpl if=\"text !== ''\">", '<span class="' + this.barCls + '-text" style="left:{left}px;">{text}</span>', "</tpl>", "</tpl>")
		}
		if (!this.lineTpl) {
			this.lineTpl = new Ext.XTemplate('<tpl for=".">', '<div class="' + this.lineCls + ' {cls}" style="top:{top}px;"></div>', "</tpl>")
		}
		if (!this.limitLineTpl) {
			this.limitLineTpl = new Ext.XTemplate('<tpl for=".">', '<div class="' + this.limitLineCls + ' {cls}" style="left:{left}px;top:{top}px;width:{width}px;height:{height}px"></div>', "</tpl>")
		}
		this.addEvents("barclick", "bardblclick", "barcontextmenu");
		this.callParent(arguments);
		this.unitHeight = this.getAvailableRowHeight() / (this.scaleMax - this.scaleMin + this.scaleStep)
	},
	onUpdate : function (b, d, a, c) {
		if (Ext.Array.indexOf(Gnt.model.Resource.prototype.calendarIdField, c) > -1) {
			this.histogram.loadAllocationData(d, true);
			this.histogram.unbindResourceCalendarListeners(d);
			var e = d.getOwnCalendar();
			if (e && e !== this.histogram.calendar) {
				this.histogram.bindResourceCalendarListeners(d, e)
			}
		}
		this.callParent(arguments)
	},
	onDataRefresh : function () {
		this.histogram.loadAllocationData(null, true);
		this.histogram.bindCalendarListeners();
		this.callParent(arguments)
	},
	renderLines : function (a) {
		return this.lineTpl.apply(this.prepareLines(a))
	},
	prepareLines : function (g) {
		var h = g.scaleMin,
		d = g.scaleLabelStep,
		a = this.getAvailableRowHeight(),
		e = [],
		m = {},
		k = this.lineCls,
		j = k + "min";
		if (g.scalePoints) {
			var f;
			for (var c = 0, b = g.scalePoints.length; c < b; c++) {
				f = g.scalePoints[c];
				e.push({
					value : f.value,
					top : f.top || Math.round(a - this.unitHeight * (f.value - g.scaleMin)),
					cls : f.cls + (f.label ? " " + k + "-label" : "") + (c === 0 ? " " + k + "-min" : (c == b ? " " + k + "-max" : ""))
				})
			}
		} else {
			while (h <= g.scaleMax) {
				e.push({
					value : h,
					top : Math.round(a - this.unitHeight * (h - g.scaleMin)),
					cls : j
				});
				h += g.scaleStep;
				j = h % d ? "" : k + "-label";
				if (h == g.scaleMax) {
					j += " " + k + "-max"
				}
			}
			if (e.length && e[e.length - 1].value !== g.scaleMax) {
				e.push({
					value : g.scaleMax,
					top : Math.round(a - this.unitHeight * (g.scaleMax - g.scaleMin)),
					cls : (g.scaleMax % d ? "" : k + "-label") + " " + k + "-max"
				})
			}
		}
		return e
	},
	renderLimitLines : function (b, a) {
		return this.limitLineTpl.apply(this.prepareLimitLines(b, a))
	},
	prepareLimitLines : function (m, g) {
		var k = [],
		a = this.getAvailableRowHeight(),
		n = this.limitLineCls,
		b;
		for (var h = 0, f = g.length; h < f; h++) {
			var e = this.getXFromDate(g[h].startDate || m.getStart(), true);
			var c = {
				left : e,
				width : this.getXFromDate(g[h].endDate || m.getEnd(), true) - e,
				top : "",
				height : 0,
				cls : ""
			};
			var j = m.calendar.convertMSDurationToUnit(g[h].allocationMS, m.scaleUnit);
			var d = true;
			if (j * this.unitHeight > a) {
				j = m.scaleMax + m.scaleStep;
				d = false
			} else {
				if (j < m.scaleMin) {
					j = m.scaleMin;
					d = false
				}
			}
			c.top = Math.round(a - (j - m.scaleMin) * this.unitHeight);
			if (d) {
				c.cls += " " + n + "-top"
			}
			if (k[0]) {
				k.push({
					left : e,
					width : 1,
					top : Math.min(c.top, k[k.length - 1].top),
					height : Math.round(Math.abs(k[k.length - 1].top - c.top) + this.limitLineWidth),
					cls : n + "-top"
				})
			}
			b = j;
			k.push(c)
		}
		return k
	},
	renderBars : function (b, a, c) {
		return this.barTpl.apply(this.prepareBars(b, a, c))
	},
	prepareBars : function (j, e, f) {
		var h = [],
		a = this.getAvailableRowHeight(),
		k = this.barCls,
		b,
		g;
		for (var d = 0, c = e.length; d < c; d++) {
			if (e[d].totalAllocation) {
				g = j.calendar.convertMSDurationToUnit(e[d].allocationMS, j.scaleUnit);
				b = Ext.apply({
						id : f + "-" + d,
						index : d,
						left : this.getXFromDate(e[d].startDate, true),
						width : this.getXFromDate(e[d].endDate, true) - this.getXFromDate(e[d].startDate, true),
						height : a,
						top : 0,
						text : "",
						cls : ""
					}, this.barRenderer(f, e[d]));
				if (this.labelMode) {
					switch (this.labelMode) {
					case "percent":
						b.text = Ext.util.Format.number(e[d].totalAllocation, this.labelPercentFormat) + "%";
						break;
					case "units":
						b.text = Ext.util.Format.number(g, this.labelUnitsFormat) + Sch.util.Date.getShortNameOfUnit(j.scaleUnit);
						break;
					default:
						b.text = this.labelMode.apply({
								allocation : g,
								percent : e[d].totalAllocation
							})
					}
				}
				if (g <= j.scaleMax + j.scaleStep) {
					b.height = g >= j.scaleMin ? Math.round((g - j.scaleMin) * this.unitHeight) : 0;
					b.top = a - b.height
				} else {
					b.cls = k + "-partofbar"
				}
				if (e[d].totalAllocation > 100) {
					b.cls = k + "-overwork"
				}
				h.push(b)
			}
		}
		return h
	},
	columnRenderer : function (e, d, c, g, b) {
		var f = c.getInternalId(),
		a = this.normalGrid.getView();
		return (this.showScaleLines ? a.renderLines(this) : "") + a.renderBars(this, this.allocationData[f].bars, f) + (this.showLimitLines ? a.renderLimitLines(this, this.allocationData[f].maxBars) : "")
	},
	getAvailableRowHeight : function () {
		if (this.availableRowHeight) {
			return this.availableRowHeight
		}
		this.availableRowHeight = this.rowHeight - this.cellTopBorderWidth - this.cellBottomBorderWidth;
		return this.availableRowHeight
	},
	resolveEventRecord : function (c) {
		var e = this.findItemByChild(c);
		if (e) {
			var g = this.getRecord(e);
			if (g) {
				var a = {
					resource : g
				};
				var f = this.histogram.allocationData[g.getInternalId()];
				var b = c.getAttribute("gnt-bar-index");
				var d = f.bars[b];
				a.startDate = d.startDate;
				a.endDate = d.endDate;
				a.assignments = d.assignments;
				a.allocationMS = d.allocationMS;
				a.totalAllocation = d.totalAllocation;
				return a
			}
		}
		return null
	},
	getDataForTooltipTpl : function (a) {
		return a
	}
});
Ext.define("Gnt.column.Scale", {
	extend : "Ext.grid.column.Template",
	alias : "widget.scalecolumn",
	tpl : null,
	sortable : false,
	scalePoints : null,
	scaleStep : 2,
	scaleLabelStep : 4,
	scaleMin : 0,
	scaleMax : 24,
	width : 40,
	availableHeight : 48,
	scaleCellCls : "gnt-scalecolumn",
	_isGanttColumn : false,
	initComponent : function () {
		this.tdCls = (this.tdCls || "") + " " + this.scaleCellCls;
		if (!this.tpl) {
			this.tpl = new Ext.XTemplate('<div class="' + this.scaleCellCls + '-wrap" style="height:{scaleHeight}px;">', '<tpl for="scalePoints">', "<tpl if=\"label !== ''\">", '<span class="' + this.scaleCellCls + '-label-line {cls}" style="top:{top}px"><span class="' + this.scaleCellCls + '-label">{label}</span></span>', "<tpl else>", '<span class="' + this.scaleCellCls + '-line {cls}" style="top:{top}px"></span>', "</tpl>", "</tpl>", "</div>")
		}
		this.setAvailableHeight(this.availableHeight, true);
		this.callParent(arguments)
	},
	setAvailableHeight : function (a, b) {
		this.availableHeight = a;
		if (!this.scalePoints) {
			this.scaleStepHeight = this.availableHeight / (this.scaleMax - this.scaleMin + this.scaleStep);
			this.scalePoints = this.buildScalePoints()
		} else {
			if (b) {
				this.scalePoints.sort(function (d, c) {
					return d.value > c.value ? 1 : -1
				});
				this.scaleMin = this.scalePoints[0].value;
				this.scaleMax = this.scalePoints[this.scalePoints.length - 1].value;
				this.scaleStep = (this.scaleMax - this.scaleMin) / 10
			}
			this.scaleStepHeight = this.availableHeight / (this.scaleMax - this.scaleMin + this.scaleStep);
			this.updateScalePointsTops()
		}
	},
	defaultRenderer : function (c, d, a) {
		var b = {
			record : Ext.apply({}, a.data, a.getAssociatedData()),
			scaleHeight : this.availableHeight,
			scalePoints : this.scalePoints
		};
		return this.tpl.apply(b)
	},
	buildScalePoints : function () {
		var g = this.scaleMin,
		h = g,
		c = this.scaleStep,
		f = this.scaleLabelStep,
		d = this.scaleStepHeight,
		b = this.availableHeight,
		a = this.scaleCellCls,
		i = a + "-min",
		j = [];
		var e = function (m, l, k) {
			return {
				top : Math.round(b - (m - g) * d),
				value : m,
				label : l != "undefined" ? l : "",
				cls : k || ""
			}
		};
		while (h < this.scaleMax) {
			j.push(e(h, h % f || h === g ? "" : h, i));
			i = "";
			h += c
		}
		j.push(e(this.scaleMax, this.scaleMax, a + "-max"));
		return j
	},
	updateScalePointsTops : function () {
		var d = this.scaleStepHeight,
		e = this.availableHeight,
		a;
		for (var c = 0, b = this.scalePoints.length; c < b; c++) {
			a = this.scalePoints[c];
			a.top = Math.round(e - a.value * d)
		}
	}
});
Ext.define("Gnt.panel.Gantt", {
	extend : "Sch.panel.TimelineTreePanel",
	alias : ["widget.ganttpanel"],
	alternateClassName : ["Sch.gantt.GanttPanel"],
	requires : ["Ext.layout.container.Border", "Gnt.model.Dependency", "Gnt.data.ResourceStore", "Gnt.data.AssignmentStore", "Gnt.feature.WorkingTime", "Gnt.data.Calendar", "Gnt.data.TaskStore", "Gnt.data.DependencyStore", "Gnt.view.Gantt"],
	uses : ["Sch.plugin.CurrentTimeLine"],
	viewType : "ganttview",
	layout : "border",
	rowLines : true,
	syncRowHeight : false,
	useSpacer : false,
	rowHeight : 24,
	topLabelField : null,
	leftLabelField : null,
	bottomLabelField : null,
	rightLabelField : null,
	highlightWeekends : true,
	weekendsAreWorkdays : false,
	skipWeekendsDuringDragDrop : true,
	enableTaskDragDrop : true,
	enableDependencyDragDrop : true,
	enableProgressBarResize : false,
	toggleParentTasksOnClick : true,
	addRowOnTab : true,
	recalculateParents : true,
	cascadeChanges : false,
	showTodayLine : false,
	enableBaseline : false,
	baselineVisible : false,
	enableAnimations : false,
	animate : false,
	workingTimePlugin : null,
	todayLinePlugin : null,
	allowParentTaskMove : true,
	enableDragCreation : true,
	eventRenderer : Ext.emptyFn,
	eventRendererScope : null,
	eventTemplate : null,
	parentEventTemplate : null,
	milestoneTemplate : null,
	taskBodyTemplate : null,
	parentTaskBodyTemplate : null,
	milestoneBodyTemplate : null,
	autoHeight : null,
	calendar : null,
	taskStore : null,
	dependencyStore : null,
	resourceStore : null,
	assignmentStore : null,
	columnLines : false,
	dndValidatorFn : Ext.emptyFn,
	createValidatorFn : Ext.emptyFn,
	resizeHandles : "both",
	resizeValidatorFn : Ext.emptyFn,
	resizeConfig : null,
	progressBarResizeConfig : null,
	dragDropConfig : null,
	createConfig : null,
	autoFitOnLoad : false,
	refreshLockedTreeOnDependencyUpdate : false,
	_lockedDependencyListeners : null,
	earlyStartColumn : null,
	earlyEndColumn : null,
	lateStartColumn : null,
	lateEndColumn : null,
	earlyDatesListeners : null,
	lateDatesListeners : null,
	slackListeners : null,
	refreshTimeout : 100,
	lastFocusedRecord : null,
	lastFocusedRecordFrom : null,
	simpleCascadeThreshold : 30,
	getEventSelectionModel : function () {
		return this.getSelectionModel()
	},
	initStores : function () {
		if (!this.taskStore) {
			Ext.Error.raise("You must specify a taskStore config.")
		}
		var a = Ext.StoreMgr.lookup(this.taskStore);
		if (!a) {
			Ext.Error.raise("You have provided an incorrect taskStore identifier")
		}
		if (!(a instanceof Gnt.data.TaskStore)) {
			Ext.Error.raise("A `taskStore` should be an instance of `Gnt.data.TaskStore` (or of a subclass)")
		}
		Ext.apply(this, {
			store : a,
			taskStore : a
		});
		var d = this.calendar = a.calendar;
		if (this.dependencyStore) {
			this.dependencyStore = Ext.StoreMgr.lookup(this.dependencyStore);
			a.setDependencyStore(this.dependencyStore)
		} else {
			this.dependencyStore = a.dependencyStore
		}
		if (!(this.dependencyStore instanceof Gnt.data.DependencyStore)) {
			Ext.Error.raise("The Gantt dependency store should be a Gnt.data.DependencyStore, or a subclass thereof.")
		}
		var b = this.resourceStore ? Ext.StoreMgr.lookup(this.resourceStore) : a.getResourceStore();
		if (!(b instanceof Gnt.data.ResourceStore)) {
			Ext.Error.raise("A `ResourceStore` should be an instance of `Gnt.data.ResourceStore` (or of a subclass)")
		}
		var c = this.assignmentStore ? Ext.StoreMgr.lookup(this.assignmentStore) : a.getAssignmentStore();
		if (!(c instanceof Gnt.data.AssignmentStore)) {
			Ext.Error.raise("An `assignmentStore` should be an instance of `Gnt.data.AssignmentStore` (or of a subclass)")
		}
		this.bindAssignmentStore(c, true);
		this.bindResourceStore(b, true);
		if (this.needToTranslateOption("weekendsAreWorkdays")) {
			d.setWeekendsAreWorkDays(this.weekendsAreWorkdays)
		}
	},
	initComponent : function () {
		var e = this;
		if (Ext.isBoolean(this.showBaseline)) {
			this.enableBaseline = this.baselineVisible = this.showBaseline;
			this.showBaseline = Gnt.panel.Gantt.prototype.showBaseline
		}
		this.autoHeight = false;
		this.initStores();
		if (this.needToTranslateOption("cascadeChanges")) {
			this.setCascadeChanges(this.cascadeChanges)
		}
		if (this.needToTranslateOption("recalculateParents")) {
			this.setRecalculateParents(this.recalculateParents)
		}
		if (this.needToTranslateOption("skipWeekendsDuringDragDrop")) {
			this.setSkipWeekendsDuringDragDrop(this.skipWeekendsDuringDragDrop)
		}
		var d = this.normalViewConfig = this.normalViewConfig || {};
		Ext.apply(this.normalViewConfig, {
			taskStore : this.taskStore,
			dependencyStore : this.dependencyStore,
			snapRelativeToEventStartDate : this.snapRelativeToEventStartDate,
			enableDependencyDragDrop : this.enableDependencyDragDrop,
			enableTaskDragDrop : this.enableTaskDragDrop,
			enableProgressBarResize : this.enableProgressBarResize,
			enableDragCreation : this.enableDragCreation,
			allowParentTaskMove : this.allowParentTaskMove,
			toggleParentTasksOnClick : this.toggleParentTasksOnClick,
			resizeHandles : this.resizeHandles,
			enableBaseline : this.baselineVisible || this.enableBaseline,
			leftLabelField : this.leftLabelField,
			rightLabelField : this.rightLabelField,
			topLabelField : this.topLabelField,
			bottomLabelField : this.bottomLabelField,
			eventTemplate : this.eventTemplate,
			parentEventTemplate : this.parentEventTemplate,
			milestoneTemplate : this.milestoneTemplate,
			taskBodyTemplate : this.taskBodyTemplate,
			parentTaskBodyTemplate : this.parentTaskBodyTemplate,
			milestoneBodyTemplate : this.milestoneBodyTemplate,
			resizeConfig : this.resizeConfig,
			dragDropConfig : this.dragDropConfig
		});
		if (this.topLabelField || this.bottomLabelField) {
			this.addCls("sch-gantt-topbottom-labels " + (this.topLabelField ? "sch-gantt-top-label" : ""));
			this.normalViewConfig.rowHeight = 52
		}
		this.configureFunctionality();
		this.mon(this.taskStore, {
			beforecascade : this.onBeforeCascade,
			cascade : this.onAfterCascade,
			beforeindentationchange : this.onBeforeIndentChange,
			indentationchange : this.onIndentChange,
			scope : this
		});
		this.callParent(arguments);
		if (this.autoFitOnLoad) {
			if (this.store.getCount()) {
				this.zoomToFit()
			}
			this.mon(this.store, "load", function () {
				this.zoomToFit()
			}, this)
		}
		this.bodyCls = (this.bodyCls || "") + " sch-ganttpanel-container-body";
		var c = this.getSchedulingView();
		c.store.calendar = this.calendar;
		this.relayEvents(c, ["taskclick", "taskdblclick", "taskcontextmenu", "beforetaskresize", "taskresizestart", "partialtaskresize", "aftertaskresize", "beforeprogressbarresize", "progressbarresizestart", "afterprogressbarresize", "beforetaskdrag", "taskdragstart", "beforetaskdropfinalize", "taskdrop", "aftertaskdrop", "labeledit_beforestartedit", "labeledit_beforecomplete", "labeledit_complete", "beforedependencydrag", "dependencydragstart", "dependencydrop", "afterdependencydragdrop", "dependencyclick", "dependencycontextmenu", "dependencydblclick", "scheduleclick", "scheduledblclick", "schedulecontextmenu"]);
		if (this.addRowOnTab) {
			var f = this.getSelectionModel();
			f.onEditorTab = Ext.Function.createInterceptor(f.onEditorTab, this.onEditorTabPress, this)
		}
		var b = this.getSchedulingView();
		this.registerRenderer(b.columnRenderer, b);
		var a = " sch-ganttpanel sch-horizontal ";
		if (this.highlightWeekends) {
			a += " sch-ganttpanel-highlightweekends "
		}
		if (!this.rtl) {
			a += " sch-ltr "
		}
		this.addCls(a);
		if (this.baselineVisible) {
			this.showBaseline()
		}
		this.on("add", function (h, g) {
			if (g instanceof Ext.Editor) {
				h.lockedGrid.suspendLayouts();
				Ext.suspendLayouts();
				h.lockedGrid.add(g);
				Ext.resumeLayouts();
				h.lockedGrid.resumeLayouts()
			}
		})
	},
	getTimeSpanDefiningStore : function () {
		return this.taskStore
	},
	bindAutoTimeSpanListeners : function () {
		if (!this.autoFitOnLoad) {
			this.callParent(arguments)
		}
	},
	onBeforeCascade : function () {
		var a = this.normalGrid.getView().store;
		a.suspendEvents();
		Ext.suspendLayouts()
	},
	onAfterCascade : function (n, b) {
		var r = this;
		var q = this.normalGrid.getView().store;
		q.resumeEvents();
		Ext.resumeLayouts();
		if (b.nbrAffected > 0) {
			var j = this.lockedGrid.getView();
			if (b.nbrAffected < r.simpleCascadeThreshold) {
				var m = this.getView();
				var l = this.getSchedulingView();
				var i = {};
				l.suspendEvents(true);
				for (var o in b.affected) {
					var p = b.affected[o];
					var k = p.parentNode;
					var h = j.store.indexOf(p);
					if (h >= 0) {
						m.refreshNode(j.store.indexOf(p))
					}
					if (k && !k.data.root && !i[k.id]) {
						var c = j.store.indexOf(k);
						i[k.id] = true;
						if (c >= 0) {
							m.refreshNode(c)
						}
					}
				}
				l.resumeEvents();
				return
			}
			var s = this.normalGrid.getView();
			var g = this.lockedGrid.plugins[0] && this.lockedGrid.plugins[0].activeEditor;
			var a,
			f,
			e,
			d;
			if (g) {
				a = g.realign;
				f = g.alignTo;
				e = g.setPosition;
				d = j.preserveScrollOnRefresh;
				g.realign = Ext.emptyFn;
				g.alignTo = Ext.emptyFn;
				g.setPosition = Ext.emptyFn;
				j.preserveScrollOnRefresh = true
			}
			s.refreshKeepingScroll(true);
			Ext.suspendLayouts();
			j.refresh();
			Ext.resumeLayouts(true);
			if (g) {
				g.realign = a;
				g.alignTo = f;
				g.setPosition = e;
				j.preserveScrollOnRefresh = d
			}
		}
	},
	onBeforeIndentChange : function (a) {
		var e = this.lockedGrid.view;
		var d = document.activeElement;
		Ext.suspendLayouts();
		e.blockRefresh = true;
		e.saveScrollState();
		if (!e.rendered || !d || !d.tagName) {
			return
		}
		var b = Ext.fly(d).findParent(e.itemSelector);
		if (b) {
			var c = e.getRecord(b);
			if (c) {
				this.lastFocusedRecordFrom = e.el.contains(b) ? "lockedGrid" : "normalGrid"
			}
			this.lastFocusedRecord = c
		}
	},
	onIndentChange : function (a) {
		var b = this.lastFocusedRecord;
		var c = this.lockedGrid.view;
		if (b) {
			this[this.lastFocusedRecordFrom].view.focusRow(b);
			this.lastFocusedRecord = null
		}
		c.blockRefresh = false;
		Ext.resumeLayouts();
		c.restoreScrollState();
		if (c.bufferedRenderer) {
			c.refresh()
		}
	},
	bindFullRefreshListeners : function (a) {
		var c = this;
		var d;
		var b = function () {
			if (d) {
				return
			}
			d = setTimeout(function () {
					d = null;
					c.redrawColumns([a])
				}, c.refreshTimeout)
		};
		this.mon(this.taskStore, {
			append : b,
			insert : b,
			remove : b,
			scope : this
		})
	},
	bindSequentialDataListeners : function (b) {
		var c = this.lockedGrid.view;
		var a = this.taskStore;
		if (c.bufferedRenderer) {
			return
		}
		this.mon(a, {
			append : function (e, d) {
				if (!a.fillCount) {
					this.updateAutoGeneratedCells(b, c.store.indexOf(d))
				}
			},
			insert : function (f, e, d) {
				this.updateAutoGeneratedCells(b, c.store.indexOf(d))
			},
			move : function (g, e, f) {
				if (g.__recordBelow) {
					var h = c.store;
					var d = Math.min(h.indexOf(g), h.indexOf(g.__recordBelow));
					this.updateAutoGeneratedCells(b, d)
				}
			},
			remove : function (g, e, f) {
				var d = e.removeContext;
				var i = d.nextSibling || (d.parentNode && d.parentNode.nextSibling);
				var h = i ? c.store.indexOf(i) : -1;
				if (f) {
					e.__recordBelow = i
				} else {
					if (h >= 0) {
						this.updateAutoGeneratedCells(b, h)
					}
				}
			},
			scope : this
		})
	},
	bindSlackListeners : function () {
		var a = Ext.Function.createBuffered(this.updateSlackColumns, this.refreshTimeout, this, []);
		this.slackListeners = this.mon(this.taskStore, {
				resetearlydates : a,
				resetlatedates : a,
				scope : this,
				destroyable : true
			})
	},
	bindEarlyDatesListeners : function () {
		var a = Ext.Function.createBuffered(this.updateEarlyDateColumns, this.refreshTimeout, this, []);
		this.earlyDatesListeners = this.mon(this.taskStore, {
				resetearlydates : a,
				scope : this,
				destroyable : true
			})
	},
	bindLateDatesListeners : function () {
		var a = Ext.Function.createBuffered(this.updateLateDateColumns, this.refreshTimeout, this, []);
		this.lateDatesListeners = this.mon(this.taskStore, {
				resetlatedates : a,
				scope : this,
				destroyable : true
			})
	},
	onEditorTabPress : function (j, i) {
		var k = this.lockedGrid.view,
		g = j.getActiveRecord(),
		d = j.getActiveColumn(),
		h = k.getPosition(g, d),
		a = this.lockedGrid.headerCt,
		f = h.row === this.lockedGrid.view.store.getCount() - 1,
		c = function (e) {
			return a.items.indexOf(e) > h.column && e.isVisible() && e.getEditor()
		};
		if (f && a.items.findIndexBy(c) < 0) {
			var b = g.addTaskBelow({
					leaf : true
				});
			j.on("beforeedit", function (e, n) {
				var m = n.column;
				var l = j.getEditor(b, m);
				l.on("startedit", function () {
					k.scrollCellIntoView(k.getCell(b, m))
				}, null, {
					single : true
				})
			}, this, {
				single : true
			})
		}
	},
	needToTranslateOption : function (a) {
		return this.hasOwnProperty(a) || this.self.prototype.hasOwnProperty(a) && this.self != Gnt.panel.Gantt
	},
	getDependencyView : function () {
		return this.getSchedulingView().getDependencyView()
	},
	disableWeekendHighlighting : function (a) {
		this.workingTimePlugin.setDisabled(a)
	},
	resolveTaskRecord : function (a) {
		return this.getSchedulingView().resolveTaskRecord(a)
	},
	fitTimeColumns : function () {
		this.getSchedulingView().fitColumns()
	},
	getResourceStore : function () {
		return this.getTaskStore().getResourceStore()
	},
	getAssignmentStore : function () {
		return this.getTaskStore().getAssignmentStore()
	},
	getTaskStore : function () {
		return this.taskStore
	},
	getEventStore : function () {
		return this.taskStore
	},
	getDependencyStore : function () {
		return this.dependencyStore
	},
	onDragDropStart : function () {
		if (this.tip) {
			this.tip.hide();
			this.tip.disable()
		}
	},
	onDragDropEnd : function () {
		if (this.tip) {
			this.tip.enable()
		}
	},
	configureFunctionality : function () {
		var a = this.plugins = [].concat(this.plugins || []);
		if (this.highlightWeekends) {
			this.workingTimePlugin = Ext.create("Gnt.feature.WorkingTime", {
					calendar : this.calendar
				});
			a.push(this.workingTimePlugin)
		}
		if (this.showTodayLine) {
			this.todayLinePlugin = new Sch.plugin.CurrentTimeLine();
			a.push(this.todayLinePlugin)
		}
	},
	getWorkingTimePlugin : function () {
		return this.workingTimePlugin
	},
	registerLockedDependencyListeners : function () {
		var b = this;
		var a = this.getDependencyStore();
		this._lockedDependencyListeners = this._lockedDependencyListeners || {
			load : function () {
				var c = b.getTaskStore();
				c.resetEarlyDates();
				c.resetLateDates();
				b.lockedGrid.getView().refresh()
			},
			clear : function () {
				var c = b.getTaskStore();
				c.resetEarlyDates();
				c.resetLateDates();
				b.lockedGrid.getView().refresh()
			},
			add : function (d, c) {
				for (var e = 0; e < c.length; e++) {
					b.updateDependencyTasks(c[e])
				}
			},
			update : function (h, f) {
				var e = b.lockedGrid.view;
				var g = e.store;
				if (f.previous[f.fromField]) {
					var d = b.taskStore.getByInternalId(f.previous[f.fromField]);
					if (d) {
						e.refreshNode(g.indexOf(d))
					}
				}
				if (f.previous[f.toField]) {
					var c = b.taskStore.getByInternalId(f.previous[f.toField]);
					if (c) {
						e.refreshNode(g.indexOf(c))
					}
				}
				b.updateDependencyTasks(f)
			},
			remove : function (d, c) {
				b.updateDependencyTasks(c)
			}
		};
		this.mun(a, this._lockedDependencyListeners);
		this.mon(a, this._lockedDependencyListeners)
	},
	updateDependencyTasks : function (c) {
		var b = c.getSourceTask(this.taskStore);
		var e = c.getTargetTask(this.taskStore);
		var f = this.lockedGrid.getView();
		var a = f.store.indexOf(b);
		var d = f.store.indexOf(e);
		if (b && a >= 0) {
			f.refreshNode(a)
		}
		if (e && d >= 0) {
			f.refreshNode(d)
		}
	},
	showBaseline : function () {
		this.addCls("sch-ganttpanel-showbaseline")
	},
	hideBaseline : function () {
		this.removeCls("sch-ganttpanel-showbaseline")
	},
	toggleBaseline : function () {
		this.toggleCls("sch-ganttpanel-showbaseline")
	},
	zoomToFit : function (b) {
		var a = b ? this.taskStore.getTasksTimeSpan(b) : this.taskStore.getTotalTimeSpan();
		if (this.zoomToSpan(a, {
				adjustStart : 1,
				adjustEnd : 1
			}) === null) {
			if (!b) {
				this.fitTimeColumns()
			}
		}
	},
	getCascadeChanges : function () {
		return this.taskStore.cascadeChanges
	},
	setCascadeChanges : function (a) {
		this.taskStore.cascadeChanges = a
	},
	getRecalculateParents : function () {
		return this.taskStore.recalculateParents
	},
	setRecalculateParents : function (a) {
		this.taskStore.recalculateParents = a
	},
	setSkipWeekendsDuringDragDrop : function (a) {
		this.taskStore.skipWeekendsDuringDragDrop = this.skipWeekendsDuringDragDrop = a
	},
	getSkipWeekendsDuringDragDrop : function () {
		return this.taskStore.skipWeekendsDuringDragDrop
	},
	bindResourceStore : function (d, a) {
		var c = this;
		var b = {
			scope : c,
			update : c.onResourceStoreUpdate,
			datachanged : c.onResourceStoreDataChanged
		};
		if (!a && c.resourceStore) {
			if (d !== c.resourceStore && c.resourceStore.autoDestroy) {
				c.resourceStore.destroy()
			} else {
				c.mun(c.resourceStore, b)
			}
			if (!d) {
				c.resourceStore = null
			}
		}
		if (d) {
			d = Ext.data.StoreManager.lookup(d);
			c.mon(d, b);
			this.taskStore.setResourceStore(d)
		}
		c.resourceStore = d;
		if (d && !a) {
			c.refreshViews()
		}
	},
	refreshViews : function () {
		if (!this.rendered) {
			return
		}
		var b = this.lockedGrid.getView(),
		c = b.el.dom.scrollLeft,
		a = b.el.dom.scrollTop;
		b.refresh();
		this.getSchedulingView().refreshKeepingScroll();
		b.el.dom.scrollLeft = c;
		b.el.dom.scrollTop = a
	},
	bindAssignmentStore : function (d, a) {
		var c = this;
		var b = {
			scope : c,
			beforetaskassignmentschange : c.onBeforeSingleTaskAssignmentChange,
			taskassignmentschanged : c.onSingleTaskAssignmentChange,
			update : c.onAssignmentStoreUpdate,
			datachanged : c.onAssignmentStoreDataChanged
		};
		if (!a && c.assignmentStore) {
			if (d !== c.assignmentStore && c.assignmentStore.autoDestroy) {
				c.assignmentStore.destroy()
			} else {
				c.mun(c.assignmentStore, b)
			}
			if (!d) {
				c.assignmentStore = null
			}
		}
		if (d) {
			d = Ext.data.StoreManager.lookup(d);
			c.mon(d, b);
			this.taskStore.setAssignmentStore(d)
		}
		c.assignmentStore = d;
		if (d && !a) {
			c.refreshViews()
		}
	},
	onResourceStoreUpdate : function (a, b) {
		var c = b.getTasks();
		Ext.Array.each(c, function (d) {
			var e = this.lockedGrid.view.store.indexOf(d);
			if (e >= 0) {
				this.getView().refreshNode(e)
			}
		}, this)
	},
	onResourceStoreDataChanged : function () {
		if (this.taskStore.getRootNode().childNodes.length > 0) {
			this.refreshViews()
		}
	},
	onAssignmentStoreDataChanged : function () {
		if (this.taskStore.getRootNode().childNodes.length > 0) {
			this.refreshViews()
		}
	},
	onAssignmentStoreUpdate : function (b, d) {
		var a = d.getTask();
		if (a) {
			var c = this.lockedGrid.view.store.indexOf(a);
			if (c >= 0) {
				this.getView().refreshNode(c)
			}
		}
	},
	onBeforeSingleTaskAssignmentChange : function () {
		this.assignmentStore.un("datachanged", this.onAssignmentStoreDataChanged, this)
	},
	onSingleTaskAssignmentChange : function (e, d, c) {
		this.assignmentStore.on("datachanged", this.onAssignmentStoreDataChanged, this);
		var a = this.taskStore.getById(d);
		if (a) {
			var b = this.lockedGrid.view.store.indexOf(a);
			if (b >= 0) {
				this.getView().refreshNode(b)
			}
		}
	},
	updateAutoGeneratedCells : function (e, c) {
		var b = this.lockedGrid.view;
		var h = b.all.startIndex;
		var f = b.all.endIndex;
		if (c < 0 || c > f) {
			return
		}
		for (var d = Math.max(h, c); d <= f; d++) {
			var g = b.store.getAt(d);
			var a = this.getCellDom(b, g, e);
			if (a) {
				a.firstChild.innerHTML = e.renderer(null, null, g)
			}
		}
	},
	getCellDom : function (b, a, c) {
		var d = b.getNode(a, true);
		return Ext.fly(d).down(c.getCellSelector(), true)
	},
	redrawColumns : function (f) {
		if (f.length && !this.isDestroyed) {
			var h = this.lockedGrid.view;
			var a = Ext.getVersion("extjs").isLessThan("4.2.2.1144");
			for (var e = h.all.startIndex; e <= h.all.endIndex; e++) {
				var b = h.store.getAt(e);
				for (var d = 0, g = f.length; d < g; d++) {
					var k = this.getCellDom(h, b, f[d]);
					var c = [];
					if (a) {
						h.renderCell(f[d], b, e, f[d].getIndex(), c)
					} else {
						h.renderCell(f[d], b, e, f[d].getIndex(), e, c)
					}
					k.innerHTML = c.join("")
				}
			}
		}
	},
	updateSlackColumns : function () {
		var a = this.lockedGrid.view;
		if (this.slackColumn) {
			this.redrawColumns([this.slackColumn])
		}
	},
	updateEarlyDateColumns : function () {
		var a = this.lockedGrid.view;
		var b = [];
		if (this.earlyStartColumn) {
			b.push(this.earlyStartColumn)
		}
		if (this.earlyEndColumn) {
			b.push(this.earlyEndColumn)
		}
		if (b.length) {
			this.redrawColumns(b)
		}
	},
	updateLateDateColumns : function () {
		var a = this.lockedGrid.view;
		var b = [];
		if (this.lateStartColumn) {
			b.push(this.lateStartColumn)
		}
		if (this.lateEndColumn) {
			b.push(this.lateEndColumn)
		}
		if (b.length) {
			this.redrawColumns(b)
		}
	},
	afterRender : function () {
		this.callParent(arguments);
		this.getSelectionModel().view = this.lockedGrid.getView();
		this.on("beforeedit", function (b, c) {
			return !this.isReadOnly() && c.record.isEditable(c.field)
		}, this);
		this.setupColumnListeners();
		var a = this.getDependencyView();
		this.getView().on({
			expandbody : a.renderAllDependencies,
			collapsebody : a.renderAllDependencies,
			scope : a
		})
	},
	setupColumnListeners : function () {
		var a = this;
		var b = this.lockedGrid.headerCt;
		b.on("add", this.onLockedColumnAdded, this);
		b.items.each(function (c) {
			a.onLockedColumnAdded(b, c)
		})
	},
	onLockedColumnAdded : function (b, a) {
		var c = Gnt.column;
		if ((c.WBS && a instanceof c.WBS) || (c.Sequence && a instanceof c.Sequence)) {
			this.bindSequentialDataListeners(a)
		} else {
			if (c.Dependency && a instanceof c.Dependency && a.useSequenceNumber) {
				this.bindFullRefreshListeners(a)
			} else {
				if (c.EarlyStartDate && a instanceof c.EarlyStartDate) {
					this.earlyStartColumn = a
				} else {
					if (c.EarlyEndDate && a instanceof c.EarlyEndDate) {
						this.earlyEndColumn = a
					} else {
						if (c.LateStartDate && a instanceof c.LateStartDate) {
							this.lateStartColumn = a
						} else {
							if (c.LateEndDate && a instanceof c.LateEndDate) {
								this.lateEndColumn = a
							} else {
								if (c.Slack && a instanceof c.Slack) {
									this.slackColumn = a
								}
							}
						}
					}
				}
			}
		}
		if (!this.slackListeners && this.slackColumn) {
			this.bindSlackListeners()
		}
		if (!this.earlyDatesListeners && (this.earlyStartColumn || this.earlyEndColumn)) {
			this.bindEarlyDatesListeners()
		}
		if (!this.lateDatesListeners && (this.lateStartColumn || this.lateEndColumn)) {
			this.bindLateDatesListeners()
		}
	},
	getState : function () {
		var a = this,
		b = a.callParent(arguments);
		b.lockedWidth = a.lockedGrid.getWidth();
		return b
	},
	applyState : function (b) {
		var a = this;
		a.callParent(arguments);
		if (b && b.lockedWidth) {
			a.lockedGrid.setWidth(b.lockedWidth)
		}
	},
	expandAll : function () {
		var a = this.taskStore.nodeStore;
		a.suspendEvents();
		this.callParent(arguments);
		a.resumeEvents();
		this.view.refresh()
	},
	collapseAll : function () {
		var a = this.taskStore.nodeStore;
		a.suspendEvents();
		this.callParent(arguments);
		a.resumeEvents();
		this.view.refresh()
	}
});
Ext.define("Gnt.panel.ResourceHistogram", {
	extend : "Sch.panel.TimelineGridPanel",
	requires : ["Ext.XTemplate", "Sch.util.Date", "Gnt.feature.WorkingTime", "Gnt.column.Scale", "Gnt.view.ResourceHistogram"],
	alias : "widget.resourcehistogram",
	viewType : "resourcehistogramview",
	layout : "border",
	preserveScrollOnRefresh : true,
	showScaleLines : false,
	showLimitLines : true,
	calendarListeners : null,
	calendarListenersHash : null,
	calendar : null,
	taskStore : null,
	resourceStore : null,
	assignmentStore : null,
	startDate : null,
	endDate : null,
	timelinePanel : null,
	highlightWeekends : true,
	allocationData : null,
	scaleUnit : "HOUR",
	scaleMin : 0,
	scaleMax : 24,
	scaleLabelStep : 4,
	scaleStep : 2,
	rowHeight : 50,
	resourceText : "Resource",
	initComponent : function () {
		this.partnerTimelinePanel = this.partnerTimelinePanel || this.timelinePanel;
		this.lockedViewConfig = this.lockedViewConfig || {};
		this.normalViewConfig = this.normalViewConfig || {};
		this.normalViewConfig.histogram = this;
		this.normalViewConfig.trackOver = false;
		this.lockedGridConfig = this.lockedGridConfig || {};
		Ext.applyIf(this.lockedGridConfig, {
			width : 300,
			forceFit : true
		});
		this.lockedViewConfig.rowHeight = this.normalViewConfig.rowHeight = this.rowHeight;
		this.lockedViewConfig.preserveScrollOnRefresh = this.normalViewConfig.preserveScrollOnRefresh = this.preserveScrollOnRefresh;
		if (this.scalePoints) {
			this.scalePoints.sort(function (m, i) {
				return m.value > i.value ? 1 : -1
			});
			this.scaleMin = this.scalePoints[0].value;
			this.scaleMax = this.scalePoints[this.scalePoints.length - 1].value;
			this.scaleStep = (this.scaleMax - this.scaleMin) / 10
		}
		if (!this.columns) {
			var c,
			f;
			this.columns = [];
			c = this.resourceNameCol = new Ext.grid.column.Column({
					header : this.resourceText,
					dataIndex : this.resourceStore.model.prototype.nameField
				});
			this.columns.push(c);
			f = {
				width : 40,
				fixed : true
			};
			Ext.Array.forEach(["scalePoints", "scaleStep", "scaleLabelStep", "scaleMin", "scaleMax", "scaleLabelStep", "scaleStep"], function (i) {
				f[i] = this[i]
			}, this);
			f = this.scaleCol = new Gnt.column.Scale(f);
			this.mon(f, {
				beforerender : function () {
					f.setAvailableHeight(this.getSchedulingView().getAvailableRowHeight());
					if (this.scalePoints) {
						this.scalePoints = f.scalePoints
					}
				},
				scope : this,
				single : true
			});
			if (this.scalePoints) {
				this.scaleMin = f.scaleMin;
				this.scaleMax = f.scaleMax;
				this.scaleStep = f.scaleStep
			}
			this.columns.push(f)
		} else {
			var e = !Ext.isArray(this.columns) ? [this.columns] : this.columns,
			d;
			for (var g = 0; g < e.length; g++) {
				d = e[g];
				if (d instanceof Gnt.column.Scale || d.xtype == "scalecolumn") {
					Ext.Array.forEach(["scalePoints", "scaleStep", "scaleLabelStep", "scaleMin", "scaleMax", "scaleLabelStep", "scaleStep"], function (i) {
						if (!(i in d)) {
							d[i] = this[i]
						}
					}, this);
					if (!(d instanceof Gnt.column.Scale)) {
						d = e[g] = Ext.ComponentManager.create(d, "scalecolumn")
					}
					this.mon(d, {
						beforerender : function () {
							d.setAvailableHeight(this.getSchedulingView().getAvailableRowHeight())
						},
						scope : this,
						single : true
					})
				}
			}
		}
		Ext.Array.forEach(["barCls", "barTpl", "barRenderer", "lineTpl", "lineCls", "limitLineTpl", "limitLineCls", "limitLineWidth", "labelMode", "labelPercentFormat", "labelUnitsFormat", "scaleMin", "scaleMax", "scaleStep", "loadMask"], function (i) {
			if (i in this) {
				this.normalViewConfig[i] = this[i]
			}
		}, this);
		this.store = this.resourceStore;
		this.taskStore = this.taskStore || this.store.getTaskStore();
		if (this.taskStore) {
			this.mon(this.taskStore, {
				refresh : this.onTaskStoreRefresh,
				update : this.onTaskUpdate,
				append : this.onTaskUpdate,
				scope : this
			})
		}
		this.calendar = this.calendar || this.taskStore && this.taskStore.getCalendar();
		if (!this.calendar) {
			throw 'Cannot get project calendar instance: please specify either "calendar" or "taskStore" option'
		}
		this.mon(this.calendar, {
			calendarchange : this.onProjectCalendarChange,
			scope : this
		});
		this.bindCalendarListeners();
		this.assignmentStore = this.assignmentStore || this.store.getAssignmentStore() || this.taskStore && this.taskStore.getAssignmentStore();
		if (this.assignmentStore) {
			this.mon(this.assignmentStore, {
				refresh : this.onAssignmentsRefresh,
				remove : this.onAssignmentsChange,
				update : this.onAssignmentsChange,
				add : this.onAssignmentsChange,
				scope : this
			})
		}
		this.plugins = [].concat(this.plugins || []);
		if (this.highlightWeekends) {
			this.workingTimePlugin = new Gnt.feature.WorkingTime({
					calendar : this.calendar
				});
			this.plugins.push(this.workingTimePlugin)
		}
		this.callParent(arguments);
		var k = "gnt-resourcehistogram sch-horizontal ";
		if (this.highlightWeekends) {
			k += " gnt-resourcehistogram-highlightweekends "
		}
		this.addCls(k);
		var h = this.getSchedulingView();
		this.registerRenderer(h.columnRenderer, this);
		this.relayEvents(h, ["barclick", "bardblclick", "barcontextmenu"]);
		if (!this.syncRowHeight) {
			this.enableRowHeightInjection(this.lockedGrid.getView(), this.normalGrid.getView())
		}
		this.loadAllocationData(null, true);
		this.mon(this.timeAxis, "reconfigure", this.onTimeAxisReconfigure, this);
		var b = this.lockedGrid.headerCt.layout;
		var j = b.getContainerSize;
		var a = Ext.getScrollbarSize;
		var l = Ext.getScrollbarSize();
		b.scrollbarWidth = 0;
		b.getContainerSize = function (i) {
			Ext.getScrollbarSize = function () {
				return {
					width : 0,
					height : l.height
				}
			};
			var m = j.apply(this, arguments);
			Ext.getScrollbarSize = a;
			return m
		}
	},
	destroy : function () {
		this.unbindCalendarListeners();
		if (this.assignmentStore) {
			this.mun(this.assignmentStore, {
				refresh : this.onAssignmentsRefresh,
				remove : this.onAssignmentsChange,
				update : this.onAssignmentsChange,
				add : this.onAssignmentsChange,
				scope : this
			})
		}
		if (this.taskStore) {
			this.mun(this.taskStore, {
				refresh : this.onTaskStoreRefresh,
				update : this.onTaskUpdate,
				append : this.onTaskUpdate,
				scope : this
			})
		}
		this.mun(this.calendar, {
			calendarchange : this.onProjectCalendarChange,
			scope : this
		});
		this.mun(this.timeAxis, "reconfigure", this.onTimeAxisReconfigure, this)
	},
	getEventStore : function () {
		return this.taskStore
	},
	onTaskStoreRefresh : function () {
		this.loadAllocationData()
	},
	onProjectCalendarChange : function () {
		this.loadAllocationData()
	},
	onTimeAxisReconfigure : function () {
		this.loadAllocationData()
	},
	unbindResourceCalendarListeners : function (b) {
		var a = this.calendarListenersHash && this.calendarListenersHash[b.getInternalId()];
		if (a) {
			Ext.Array.remove(this.calendarListeners, a);
			Ext.destroy(a)
		}
	},
	bindResourceCalendarListeners : function (d, e) {
		var c = this;
		e = e || d.getOwnCalendar();
		var b = function () {
			c.loadAllocationData(d)
		};
		var a = c.mon(e, {
				load : b,
				calendarchange : b,
				scope : c,
				destroyable : true
			});
		c.calendarListenersHash[d.getInternalId()] = a;
		c.calendarListeners.push(a)
	},
	bindCalendarListeners : function () {
		this.unbindCalendarListeners();
		var a = this;
		this.store.each(function (b) {
			var c = b.getOwnCalendar();
			if (c && c !== a.calendar) {
				a.bindResourceCalendarListeners(b, c)
			}
		})
	},
	unbindCalendarListeners : function () {
		if (this.calendarListeners && this.calendarListeners.length) {
			Ext.destroy.apply(Ext, this.calendarListeners)
		}
		this.calendarListeners = [];
		this.calendarListenersHash = {}

	},
	onResourcesLoad : function () {
		this.loadAllocationData();
		this.bindCalendarListeners()
	},
	onTaskUpdate : function (c, b) {
		var a;
		if (this.assignmentStore) {
			a = this.assignmentStore.queryBy(function (d) {
					return d.getTaskId() == b.getInternalId()
				});
			a = a.getRange()
		} else {
			a = b.getAssignments()
		}
		this.onAssignmentsChange(this.assignmentStore, a)
	},
	onAssignmentsRefresh : function (a) {
		this.onAssignmentsChange(a, a.getRange())
	},
	onAssignmentsChange : function (e, b) {
		var d;
		if (!Ext.isArray(b)) {
			b = [b]
		}
		for (var c = 0, a = b.length; c < a; c++) {
			d = this.resourceStore.getByInternalId(b[c].getResourceId());
			if (d) {
				this.loadAllocationData(d)
			}
		}
	},
	enableRowHeightInjection : function (c, e) {
		var b = c.renderRow;
		var a = c.renderCell;
		var d = new Ext.XTemplate("{%", "this.processCellValues(values);", "this.nextTpl.applyOut(values, out, parent);", "%}", {
				priority : 1,
				processCellValues : function (g) {
					if (e.orientation == "horizontal") {
						var f = e.getAvailableRowHeight();
						g.style = (g.style || "") + ";height:" + f + "px;"
					}
				}
			});
		c.addCellTpl(d);
		e.addCellTpl(d)
	},
	loadAllocationData : function (d, b) {
		if (this.resourceStore) {
			if (!d) {
				this.allocationData = {};
				var c = this;
				var e = this.getStartDate();
				var a = this.getEndDate();
				this.store.each(function (f) {
					c.allocationData[f.getInternalId()] = c.processAllocationData(f.getAllocationInfo({
								startDate : e,
								endDate : a,
								includeResCalIntervals : true
							}))
				});
				if (!b && this.rendered) {
					this.getView().refresh()
				}
			} else {
				this.allocationData = this.allocationData || {};
				this.allocationData[d.getInternalId()] = this.processAllocationData(d.getAllocationInfo({
							startDate : this.getStartDate(),
							endDate : this.getEndDate(),
							includeResCalIntervals : true
						}));
				if (!b && this.rendered) {
					this.getView().refreshNode(this.store.indexOf(d))
				}
			}
		}
	},
	processAllocationData : function (z) {
		var w,
		x,
		h,
		u,
		o,
		c,
		b,
		m,
		d,
		A,
		r = [],
		g = [],
		a = false,
		y = this;
		var p = function () {
			if (!x.assignments || !w.inResourceCalendar || !w.totalAllocation || !w.inTasksCalendar) {
				return false
			}
			for (var B = 0, j = x.assignments.length; B < j; B++) {
				if (w.assignmentsHash[x.assignments[B].getTaskId()]) {
					return false
				}
			}
			return true
		};
		var n = function (i) {
			x = {
				startDate : i,
				totalAllocation : w.totalAllocation,
				allocationMS : b * w.totalAllocation / 100,
				assignments : w.assignments
			};
			a = true
		};
		var f = function (i) {
			if (!a) {
				return false
			}
			if (i) {
				x.endDate = i
			}
			r.push(x);
			a = false
		};
		var k = function (j, i) {
			if (!j) {
				return false
			}
			var B = Sch.util.Date.getDurationInDays(j, i);
			if (B < 2) {
				return false
			}
			var l = true;
			if (h) {
				if (!h.allocationMS) {
					l = false
				} else {
					h.endDate = Sch.util.Date.getStartOfNextDay(j, true);
					g.push(h)
				}
			}
			if (l) {
				h = {
					startDate : h && h.endDate || y.getStart(),
					allocationMS : 0
				}
			}
			d = 0;
			return true
		};
		var q;
		for (var v = 0, s = z.length; v < s; v++) {
			w = z[v];
			q = Ext.Date.clearTime(w.startDate, true);
			if (q - u !== 0) {
				this.showLimitLines && k(u, q);
				u = q;
				m = b;
				A = d;
				b = 0;
				d = 0;
				var t = v;
				while (z[t] && Ext.Date.clearTime(z[t].startDate, true) - q === 0) {
					if (z[t].inResourceCalendar) {
						d += z[t].endDate - z[t].startDate;
						if (z[t].totalAllocation && z[t].inTasksCalendar) {
							b += z[t].endDate - z[t].startDate
						}
					}
					t++
				}
			} else {
				q = false
			}
			if (this.showLimitLines) {
				if (q && d != A) {
					if (h) {
						h.endDate = q;
						g.push(h)
					}
					h = {
						startDate : q,
						allocationMS : d
					}
				}
				h.endDate = w.endDate
			}
			if (!a) {
				if (w.inTask) {
					n(new Date(w.startDate))
				}
			} else {
				if (!w.inTask) {
					f()
				} else {
					var e = false;
					if (q && x.endDate <= Sch.util.Date.add(q, Sch.util.Date.DAY, -1)) {
						o = Ext.Date.clearTime(x.endDate, true);
						if (o < x.endDate) {
							o = Sch.util.Date.add(o, Sch.util.Date.DAY, 1)
						}
						c = Ext.Date.clearTime(w.startDate, true);
						e = true
					} else {
						if (q && b !== m) {
							o = c = w.startDate;
							e = true
						} else {
							if (w.totalAllocation && p()) {
								o = x.endDate;
								c = new Date(w.startDate);
								e = true
							} else {
								if (w.totalAllocation && w.totalAllocation != x.totalAllocation) {
									o = c = w.totalAllocation > x.totalAllocation ? new Date(w.startDate) : x.endDate;
									e = true
								}
							}
						}
					}
					if (e) {
						f(o);
						n(c)
					}
				}
			}
			if (a) {
				x.endDate = w.endDate
			}
		}
		f();
		if (this.showLimitLines) {
			k(u || this.getStart(), this.getEnd());
			if (h) {
				g.push(h)
			}
			if (g.length) {
				g[0].startDate = null;
				g[g.length - 1].endDate = null
			}
		}
		return {
			bars : r,
			maxBars : g
		}
	}
});
Ext.define("Gnt.field.mixin.TaskField", {
	taskField : "",
	task : null,
	taskStore : null,
	suppressTaskUpdate : 0,
	highlightTaskUpdates : true,
	highlightColor : "#009900",
	lastHighlight : 0,
	instantUpdate : true,
	setTask : function (a) {
		if (!a) {
			return
		}
		this.destroyTaskListener();
		this.updateReadOnly(a);
		this.task = a;
		a.on("taskupdated", this.onTaskUpdateProcess, this);
		if (!a.getCalendar(true) && !a.getTaskStore(true)) {
			a.taskStore = a.getTaskStore(true) || this.taskStore;
			if (!a.taskStore) {
				throw "Configuration issue: Gnt.data.taskStore instance should be provided."
			}
			if (!a.getCalendar(true) && !a.taskStore.getCalendar()) {
				throw "Configuration issue: Gnt.data.Calendar instance should be provided."
			}
		}
		this.setSuppressTaskUpdate(true);
		if (this.onSetTask) {
			this.onSetTask(a)
		}
		this.setSuppressTaskUpdate(false)
	},
	setSuppressTaskUpdate : function (a) {
		a ? this.suppressTaskUpdate++ : this.suppressTaskUpdate--
	},
	getSuppressTaskUpdate : function () {
		return this.suppressTaskUpdate
	},
	updateReadOnly : function (a) {
		if (!this.disabled) {
			if (this.editable === false) {
				if (!a.isEditable(a[this.taskField])) {
					this.setReadOnly(true)
				} else {
					if (this.inputEl) {
						this.setReadOnly(false);
						this.inputEl.dom.readOnly = true
					}
				}
			} else {
				this.setReadOnly(!a.isEditable(a[this.taskField]))
			}
		}
	},
	onTaskUpdateProcess : function (a, d) {
		if (d !== this) {
			var c = this.getValue();
			this.updateReadOnly(a);
			this.setSuppressTaskUpdate(true);
			if (this.onTaskUpdate) {
				this.onTaskUpdate(a, d)
			} else {
				if (this.onSetTask) {
					this.onSetTask()
				}
			}
			this.setSuppressTaskUpdate(false);
			if (this.highlightTaskUpdates) {
				var e = this.getValue(),
				b = Ext.isDate(c);
				if (b && (c - e !== 0) || (!b && String(c) !== String(e))) {
					this.highlightField()
				}
			}
		}
	},
	highlightField : function (a, b) {
		if (this.rendered && (new Date() - this.lastHighlight > 1000)) {
			this.lastHighlight = new Date();
			this.inputEl.highlight(a || this.highlightColor, b || {
				attr : "color"
			})
		}
	},
	destroyTaskListener : function () {
		if (this.task) {
			this.task.un("taskupdated", this.onTaskUpdateProcess, this)
		}
	}
});
Ext.define("Gnt.field.EndDate", {
	extend : "Ext.form.field.Date",
	requires : ["Sch.util.Date"],
	mixins : ["Gnt.field.mixin.TaskField", "Gnt.mixin.Localizable"],
	alias : "widget.enddatefield",
	adjustMilestones : true,
	keepDuration : false,
	taskField : "endDateField",
	validateStartDate : true,
	constructor : function (a) {
		a = a || {};
		Ext.apply(this, a);
		if (a.task && !a.value) {
			a.value = a.task.getEndDate()
		}
		this.setSuppressTaskUpdate(true);
		this.callParent([a]);
		this.setSuppressTaskUpdate(false);
		if (this.task) {
			this.setTask(this.task)
		}
	},
	destroy : function () {
		this.destroyTaskListener();
		this.callParent()
	},
	onSetTask : function () {
		this.setValue(this.task.getEndDate())
	},
	rawToValue : function (a) {
		if (!a) {
			return null
		}
		return this.visibleToValue(this.parseDate(a))
	},
	valueToRaw : function (a) {
		if (!a) {
			return a
		}
		return Ext.Date.format(this.valueToVisible(a), this.format)
	},
	valueToVisible : function (b, a) {
		a = a || this.task;
		return a.getDisplayEndDate(this.format, this.adjustMilestones, b, true)
	},
	visibleToValue : function (a) {
		if (a && this.task) {
			if (!Ext.Date.formatContainsHourInfo(this.format) && a - Ext.Date.clearTime(a, true) === 0) {
				a = this.task.getCalendar().getCalendarDay(a).getAvailabilityEndFor(a) || Sch.util.Date.add(a, Sch.util.Date.DAY, 1)
			}
		} else {
			a = null
		}
		return a
	},
	getErrors : function (a) {
		var b = this.callParent([a]);
		if (b && b.length) {
			return b
		}
		if (this.validateStartDate) {
			a = this.rawToValue(a);
			if (this.task && a) {
				if (a < this.task.getStartDate()) {
					return [this.L("endBeforeStartText")]
				}
			}
		}
	},
	onExpand : function () {
		var a = this.valueToVisible(this.getValue());
		if (!this.isValid()) {
			a = this.getRawValue();
			if (a) {
				a = Ext.Date.parse(a, this.format)
			}
		}
		this.picker.setValue(Ext.isDate(a) ? a : new Date())
	},
	onSelect : function (d, a) {
		var c = this.task.getEndDate();
		if (Ext.Date.formatContainsHourInfo(this.format) && c) {
			a.setHours(c.getHours());
			a.setMinutes(c.getMinutes())
		}
		var e = this;
		var b = e.getValue();
		var g = this.visibleToValue(a);
		var f = Ext.Date.format(a, this.format);
		if (b != g) {
			if (this.getErrors(f)) {
				e.setRawValue(f);
				e.collapse();
				e.validate()
			} else {
				e.setValue(g, true);
				e.fireEvent("select", e, g);
				e.collapse()
			}
		}
	},
	applyChanges : function (a) {
		a = a || this.task;
		var b = a.getTaskStore(true) || this.taskStore;
		if (this.value) {
			a.setEndDate(this.value, this.keepDuration, b.skipWeekendsDuringDragDrop)
		} else {
			a.setEndDate(null)
		}
	},
	setVisibleValue : function (a) {
		this.setValue(this.rawToValue(Ext.Date.format(a, this.format)))
	},
	getVisibleValue : function () {
		if (!this.getValue()) {
			return null
		}
		return Ext.Date.parse(this.valueToRaw(this.getValue()), this.format)
	},
	setValue : function (b, a) {
		this.callParent([b]);
		if ((a || this.instantUpdate) && !this.getSuppressTaskUpdate() && this.task) {
			this.applyChanges();
			var c = this.task.getEndDate();
			if (c - this.getValue() !== 0) {
				this.callParent([c])
			}
			this.task.fireEvent("taskupdated", this.task, this)
		}
	},
	getValue : function () {
		return this.value
	},
	assertValue : function () {
		var c = this,
		d = c.rawValue,
		f = c.getRawValue(),
		a = c.getValue(),
		e = c.rawToValue(f),
		b = c.focusTask;
		if (b) {
			b.cancel()
		}
		if ((d != f) && (e - a !== 0)) {
			if (!c.validateOnBlur || c.isValid()) {
				c.setValue(e, true)
			}
		}
	},
	beforeBlur : function () {
		this.assertValue()
	}
});
Ext.define("Gnt.field.StartDate", {
	extend : "Ext.form.field.Date",
	requires : ["Sch.util.Date"],
	mixins : ["Gnt.field.mixin.TaskField"],
	alias : "widget.startdatefield",
	adjustMilestones : true,
	keepDuration : true,
	taskField : "startDateField",
	constructor : function (a) {
		a = a || {};
		if (a.task && !a.value) {
			a.value = a.task.getStartDate()
		}
		this.setSuppressTaskUpdate(true);
		this.callParent([a]);
		this.setSuppressTaskUpdate(false);
		if (this.task) {
			this.setTask(this.task)
		}
	},
	destroy : function () {
		this.destroyTaskListener();
		this.callParent()
	},
	onSetTask : function () {
		this.setValue(this.task.getStartDate())
	},
	rawToValue : function (a) {
		if (!a) {
			return null
		}
		return this.visibleToValue(this.parseDate(a))
	},
	valueToRaw : function (a) {
		if (!a) {
			return a
		}
		return Ext.Date.format(this.valueToVisible(a), this.format)
	},
	valueToVisible : function (b, a) {
		a = a || this.task;
		return a.getDisplayStartDate(this.format, this.adjustMilestones, b, true)
	},
	visibleToValue : function (c) {
		var b = this.task;
		if (b && c) {
			var d = b.getEndDate();
			var a = !this.lastValue || this.lastValue - Ext.Date.clearTime(this.lastValue, true) === 0;
			if (this.adjustMilestones && b.isMilestone() && c - Ext.Date.clearTime(c, true) === 0 && a) {
				c = b.getCalendar().getCalendarDay(c).getAvailabilityEndFor(c) || c
			}
		}
		return c
	},
	onExpand : function () {
		var a = this.valueToVisible(this.getValue());
		if (!this.isValid()) {
			a = this.getRawValue();
			if (a) {
				a = Ext.Date.parse(a, this.format)
			}
		}
		this.picker.setValue(Ext.isDate(a) ? a : new Date())
	},
	onSelect : function (d, a) {
		var c = this.task.getStartDate();
		if (Ext.Date.formatContainsHourInfo(this.format) && c) {
			a.setHours(c.getHours());
			a.setMinutes(c.getMinutes())
		}
		var e = this,
		f = Ext.Date.format(a, this.format),
		b = e.getValue(),
		g = this.visibleToValue(a),
		h = this.getErrors(f);
		if (b != g) {
			if (h && h.length) {
				e.setRawValue(f);
				e.collapse();
				e.validate()
			} else {
				e.setValue(g);
				e.fireEvent("select", e, g);
				e.collapse()
			}
		}
	},
	applyChanges : function (a) {
		a = a || this.task;
		var b = a.getTaskStore(true) || this.taskStore;
		a.setStartDate(this.value, this.keepDuration, b.skipWeekendsDuringDragDrop)
	},
	setVisibleValue : function (a) {
		this.setValue(this.rawToValue(Ext.Date.format(a, this.format)))
	},
	getVisibleValue : function () {
		if (!this.getValue()) {
			return null
		}
		return Ext.Date.parse(this.valueToRaw(this.getValue()), this.format)
	},
	setValue : function (d, c) {
		this.callParent([d]);
		var b = this.task;
		if ((c || this.instantUpdate) && !this.getSuppressTaskUpdate() && b && b.taskStore && d) {
			this.applyChanges();
			var a = b.getStartDate();
			if (a - this.getValue() !== 0) {
				this.callParent([a])
			}
			b.fireEvent("taskupdated", b, this)
		}
	},
	getValue : function () {
		return this.value
	},
	assertValue : function () {
		var c = this,
		d = c.rawValue,
		f = c.getRawValue(),
		a = c.getValue(),
		e = c.rawToValue(f),
		b = c.focusTask;
		if (b) {
			b.cancel()
		}
		if ((d != f) && (e - a !== 0)) {
			if (!c.validateOnBlur || c.isValid()) {
				c.setValue(e, true)
			}
		}
	},
	beforeBlur : function () {
		this.assertValue()
	}
});
Ext.define("Gnt.field.Duration", {
	extend : "Ext.form.field.Number",
	requires : ["Gnt.util.DurationParser"],
	mixins : ["Gnt.field.mixin.TaskField", "Gnt.mixin.Localizable"],
	alias : "widget.durationfield",
	alternateClassName : ["Gnt.column.duration.Field", "Gnt.widget.DurationField"],
	disableKeyFilter : true,
	allowExponential : false,
	minValue : 0,
	durationUnit : "h",
	useAbbreviation : false,
	getDurationUnitMethod : "getDurationUnit",
	setDurationMethod : "setDuration",
	getDurationMethod : "getDuration",
	taskField : "durationField",
	durationParser : null,
	durationParserConfig : null,
	constructor : function (a) {
		var b = this;
		Ext.apply(this, a);
		this.durationParser = new Gnt.util.DurationParser(Ext.apply({
					parseNumberFn : function () {
						return b.parseValue.apply(b, arguments)
					},
					allowDecimals : this.decimalPrecision > 0
				}, this.durationParserConfig));
		this.setSuppressTaskUpdate(true);
		this.callParent(arguments);
		this.setSuppressTaskUpdate(false);
		this.invalidText = this.L("invalidText");
		if (this.task) {
			this.setTask(this.task)
		}
	},
	destroy : function () {
		this.destroyTaskListener();
		this.callParent()
	},
	onSetTask : function () {
		this.durationUnit = this.task[this.getDurationUnitMethod]();
		var a = this.getDurationMethod ? this.task[this.getDurationMethod]() : this.task.get(this.task[this.taskField]);
		this.setValue(a);
		this.setSpinUpEnabled(a == null || a < this.maxValue, true);
		this.setSpinDownEnabled(a > this.minValue, true)
	},
	rawToValue : function (b) {
		var a = this.parseDuration(b);
		if (!a) {
			return null
		}
		this.durationUnit = a.unit;
		return a.value != null ? a.value : null
	},
	valueToVisible : function (c, d) {
		if (Ext.isNumber(c)) {
			var b = parseInt(c, 10),
			a = Ext.Number.toFixed(c, this.decimalPrecision);
			return String(b == a ? b : a).replace(".", this.decimalSeparator) + " " + Sch.util.Date[this.useAbbreviation ? "getShortNameOfUnit" : "getReadableNameOfUnit"](d || this.durationUnit, c !== 1)
		}
		return ""
	},
	valueToRaw : function (a) {
		return this.valueToVisible(a, this.durationUnit, this.decimalPrecision, this.useAbbreviation)
	},
	parseDuration : function (b) {
		if (b == null) {
			return null
		}
		var a = this;
		var c = this.durationParser.parse(b);
		if (!c) {
			return null
		}
		c.unit = c.unit || this.durationUnit;
		return c
	},
	getDurationValue : function () {
		return this.parseDuration(this.getRawValue())
	},
	getErrors : function (b) {
		var a;
		if (b) {
			a = this.parseDuration(b);
			if (!a) {
				return [this.L("invalidText")]
			}
			b = a.value
		}
		return this.callParent([b])
	},
	checkChange : function () {
		if (!this.suspendCheckChange) {
			var d = this,
			c = d.getDurationValue(),
			a = d.lastValue;
			var b = c && !a || !c && a || c && a && (c.value != a.value || c.unit != a.unit);
			if (b && !d.isDestroyed) {
				d.lastValue = c;
				d.fireEvent("change", d, c, a);
				d.onChange(c, a)
			}
		}
	},
	getValue : function () {
		return this.value
	},
	applyChanges : function (a) {
		a = a || this.task;
		a[this.setDurationMethod](this.getValue(), this.durationUnit)
	},
	setValue : function (b, a) {
		var c = b;
		if (Ext.isObject(b)) {
			this.durationUnit = b.unit;
			c = b.value
		}
		this.callParent([c]);
		if ((a || this.instantUpdate) && !this.getSuppressTaskUpdate() && this.task) {
			this.applyChanges();
			this.task.fireEvent("taskupdated", this.task, this)
		}
	},
	assertValue : function () {
		var d = this,
		a = d.getValue(),
		e = d.durationUnit,
		c = d.getDurationValue();
		if (this.isValid()) {
			var b = c && !a || !c && a || c && (c.value != a || c.unit != e);
			if (b) {
				d.setValue(c, true)
			}
		}
	},
	beforeBlur : function () {
		this.assertValue()
	},
	onSpinUp : function () {
		var a = this;
		if (!a.readOnly) {
			var b = a.getValue() || 0;
			a.setSpinValue(Ext.Number.constrain(b + a.step, a.minValue, a.maxValue))
		}
	},
	onSpinDown : function () {
		var a = this;
		if (!a.readOnly) {
			var b = a.getValue() || 0;
			a.setSpinValue(Ext.Number.constrain(b - a.step, a.minValue, a.maxValue))
		}
	}
});
Ext.define("Gnt.field.Effort", {
	extend : "Gnt.field.Duration",
	requires : ["Gnt.util.DurationParser"],
	alias : "widget.effortfield",
	alternateClassName : ["Gnt.column.effort.Field", "Gnt.widget.EffortField"],
	invalidText : "Invalid effort value",
	taskField : "effortField",
	getDurationUnitMethod : "getEffortUnit",
	setDurationMethod : "setEffort",
	getDurationMethod : "getEffort"
});
Ext.define("Gnt.field.SchedulingMode", {
	extend : "Ext.form.field.ComboBox",
	mixins : ["Gnt.field.mixin.TaskField"],
	alias : "widget.schedulingmodefield",
	alternateClassName : ["Gnt.column.schedulingmode.Field"],
	taskField : "schedulingModeField",
	store : [["Normal", "Normal"], ["Manual", "Manual"], ["FixedDuration", "Fixed duration"], ["EffortDriven", "Effort driven"], ["DynamicAssignment", "Dynamic assignment"]],
	pickerAlign : "tl-bl?",
	matchFieldWidth : true,
	editable : false,
	forceSelection : true,
	triggerAction : "all",
	constructor : function (a) {
		var b = this;
		Ext.apply(this, a);
		this.setSuppressTaskUpdate(true);
		this.callParent(arguments);
		this.setSuppressTaskUpdate(false);
		if (this.task) {
			this.setTask(this.task)
		}
	},
	destroy : function () {
		this.destroyTaskListener();
		this.callParent()
	},
	onSetTask : function () {
		this.setValue(this.task.getSchedulingMode())
	},
	valueToVisible : function (e, b) {
		var c = this,
		d = [];
		var a = this.findRecordByValue(e);
		if (a) {
			d.push(a.data)
		} else {
			if (Ext.isDefined(c.valueNotFoundText)) {
				d.push(c.valueNotFoundText)
			}
		}
		return c.displayTpl.apply(d)
	},
	applyChanges : function (a) {
		a = a || this.task;
		a.setSchedulingMode(this.getValue())
	},
	getValue : function () {
		return this.value
	},
	setValue : function (a) {
		this.callParent([a]);
		if (this.instantUpdate && !this.getSuppressTaskUpdate() && this.task && this.value) {
			this.applyChanges();
			this.task.fireEvent("taskupdated", this.task, this)
		}
	}
});
Ext.define("Gnt.field.Calendar", {
	extend : "Ext.form.field.ComboBox",
	requires : ["Gnt.data.Calendar"],
	mixins : ["Gnt.field.mixin.TaskField", "Gnt.mixin.Localizable"],
	alias : "widget.calendarfield",
	alternateClassName : ["Gnt.column.calendar.Field"],
	taskField : "calendarIdField",
	pickerAlign : "tl-bl?",
	matchFieldWidth : true,
	editable : true,
	triggerAction : "all",
	valueField : "Id",
	displayField : "Name",
	queryMode : "local",
	forceSelection : true,
	allowBlank : true,
	constructor : function (a) {
		var b = this;
		Ext.apply(this, a);
		this.store = this.store || Ext.create("Ext.data.Store", {
				fields : ["Id", "Name"]
			});
		this.setSuppressTaskUpdate(true);
		this.callParent(arguments);
		this.setSuppressTaskUpdate(false);
		if (this.rendered) {
			this.store.loadData(this.getCalendarData())
		} else {
			this.on({
				render : function () {
					this.store.loadData(this.getCalendarData())
				},
				show : this.setReadOnlyIfEmpty,
				scope : this
			})
		}
		if (this.task) {
			this.setTask(this.task)
		}
	},
	destroy : function () {
		this.destroyTaskListener();
		this.callParent()
	},
	setReadOnlyIfEmpty : function () {
		var a = Gnt.data.Calendar.getAllCalendars();
		if (!a || !a.length) {
			this.setReadOnly(true)
		}
	},
	getCalendarData : function () {
		var a = [];
		Ext.Array.each(Gnt.data.Calendar.getAllCalendars(), function (b) {
			a.push({
				Id : b.calendarId,
				Name : b.name || b.calendarId
			})
		});
		return a
	},
	onSetTask : function () {
		this.setReadOnlyIfEmpty();
		this.setValue(this.task.getCalendarId())
	},
	onTaskUpdate : function (a, b) {
		this.setReadOnlyIfEmpty();
		this.setValue(this.task.getCalendarId())
	},
	valueToVisible : function (e, b) {
		var c = this,
		d = [];
		var a = this.findRecordByValue(e);
		if (a) {
			d.push(a.data)
		} else {
			if (Ext.isDefined(c.valueNotFoundText)) {
				d.push(c.valueNotFoundText)
			}
		}
		return c.displayTpl.apply(d)
	},
	getValue : function () {
		return this.value
	},
	applyChanges : function (a) {
		a = a || this.task;
		a.setCalendarId(this.value)
	},
	getErrors : function (b) {
		if (b) {
			var a = this.findRecordByDisplay(b);
			if (a) {
				if (this.task && !this.task.isCalendarApplicable(a.data.Id)) {
					return [this.L("calendarNotApplicable")]
				}
			}
		}
		return this.callParent(arguments)
	},
	setValue : function (a) {
		this.callParent([a]);
		if (undefined === a || null === a || "" === a) {
			this.value = ""
		}
		if (!this.getSuppressTaskUpdate() && this.task) {
			if (this.task.getCalendarId() != this.value) {
				this.applyChanges();
				this.task.fireEvent("taskupdated", this.task, this)
			}
		}
	},
	assertValue : function () {
		var a = this.getRawValue();
		if (!a && this.value) {
			this.setValue("")
		} else {
			this.callParent(arguments)
		}
	}
});
Ext.define("Gnt.field.Percent", {
	extend : "Ext.form.field.Number",
	alias : "widget.percentfield",
	alternateClassName : ["Gnt.widget.PercentField"],
	disableKeyFilter : false,
	minValue : 0,
	maxValue : 100,
	allowExponential : false,
	invalidText : "Invalid percent value",
	baseChars : "0123456789%",
	valueToRaw : function (a) {
		if (Ext.isNumber(a)) {
			return parseFloat(Ext.Number.toFixed(a, this.decimalPrecision)) + "%"
		}
		return ""
	},
	getErrors : function (b) {
		var a = this.parseValue(b);
		if (a === null) {
			if (b !== null && b !== "") {
				return [this.invalidText]
			} else {
				a = ""
			}
		}
		return this.callParent([a])
	}
});
Ext.define("Gnt.field.Dependency", {
	extend : "Ext.form.field.Text",
	alternateClassName : "Gnt.widget.DependencyField",
	alias : "widget.dependencyfield",
	requires : ["Gnt.util.DependencyParser"],
	mixins : ["Gnt.mixin.Localizable"],
	type : "predecessors",
	separator : ";",
	task : null,
	dependencyParser : null,
	useSequenceNumber : false,
	constructor : function (a) {
		var b = this;
		Ext.apply(this, a);
		this.dependencyParser = new Gnt.util.DependencyParser({
				parseNumberFn : function () {
					return Gnt.widget.DurationField.prototype.parseValue.apply(b, arguments)
				}
			});
		this.callParent(arguments);
		this.addCls("gnt-field-dependency")
	},
	isPredecessor : function () {
		return this.type === "predecessors"
	},
	setTask : function (a) {
		this.task = a;
		this.setRawValue(this.getDisplayValue(a))
	},
	getDependencies : function () {
		return this.dependencyParser.parse(this.getRawValue())
	},
	getTaskIdFromDependency : function (c) {
		var b = this.task.getTaskStore(),
		d = c.taskId,
		a;
		if (this.useSequenceNumber) {
			a = b.getBySequenceNumber(d);
			d = a && a.getInternalId()
		}
		return d
	},
	getErrors : function (n) {
		if (!n) {
			return
		}
		var e = this.dependencyParser.parse(n);
		if (!e) {
			return [this.L("invalidFormatText")]
		}
		var g = this.getDependencies(),
		m = this.isPredecessor(),
		s = this.task,
		u = s.getTaskStore().dependencyStore,
		l = s[m ? "predecessors" : "successors"],
		a;
		var t = [];
		for (var r = 0; r < g.length; r++) {
			var p = g[r];
			a = this.getTaskIdFromDependency(p);
			if (!a) {
				return [this.L("invalidDependencyText")]
			}
			if (u.allowedDependencyTypes && !u.isValidDependencyType(p.type)) {
				var h = this.dependencyParser.types,
				d = u.allowedDependencyTypes,
				f = u.model.Type,
				b = "";
				for (var q = 0, o = d.length; q < o; q++) {
					b += h[f[d[q]]] + ","
				}
				return [Ext.String.format(this.L("invalidDependencyType"), h[p.type], b.substring(0, b.length - 1))]
			}
			var c = new u.model();
			c.setSourceId(m ? a : s.getInternalId());
			c.setTargetId(m ? s.getInternalId() : a);
			c.setType(p.type);
			c.setLag(p.lag);
			c.setLagUnit(p.lagUnit);
			t.push(c)
		}
		for (r = 0; r < t.length; r++) {
			if (!u.isValidDependency(t[r], t, l)) {
				return [this.invalidDependencyText]
			}
		}
		return this.callParent([e.value])
	},
	getDisplayValue : function (a) {
		var h = this.isPredecessor(),
		l = h ? a.getIncomingDependencies(true) : a.getOutgoingDependencies(true),
		j = Gnt.util.DependencyParser.prototype.types,
		e = Gnt.model.Dependency.Type.EndToStart,
		k = [],
		m;
		for (var d = 0; d < l.length; d++) {
			var g = l[d];
			m = h ? g.getSourceTask() : g.getTargetTask();
			if (m && g.isValid(false)) {
				var f = g.getType(),
				b = g.getLag(),
				c = g.getLagUnit();
				k.push(Ext.String.format("{0}{1}{2}{3}{4}", this.useSequenceNumber ? m.getSequenceNumber() : m.getInternalId(), b || f !== e ? j[f] : "", b > 0 ? "+" : "", b || "", b && c !== "d" ? c : ""))
			}
		}
		return k.join(this.separator)
	},
	isDirty : function (c) {
		c = c || this.task;
		if (!c) {
			return false
		}
		var a = this.isPredecessor(),
		d = c.getTaskStore().dependencyStore,
		h = a ? c.getIncomingDependencies() : c.getOutgoingDependencies(),
		f = c.getInternalId();
		for (var e = 0, b = h.length; e < b; e++) {
			if (h[e].dirty || h[e].phantom) {
				return true
			}
		}
		if (d) {
			var g = a ? "getTargetId" : "getSourceId";
			h = d.getRemovedRecords();
			for (e = 0, b = h.length; e < b; e++) {
				if (h[e][g]() == f) {
					return true
				}
			}
		}
		return false
	},
	applyChanges : function (d) {
		d = d || this.task;
		var n = d.getTaskStore().dependencyStore,
		h = this.getDependencies(),
		a = [],
		k = this.isPredecessor(),
		m = k ? d.getIncomingDependencies(true) : d.getOutgoingDependencies(true),
		c = [];
		for (var g = 0, f = h.length; g < f; g++) {
			a.push(this.getTaskIdFromDependency(h[g]))
		}
		for (g = 0; g < m.length; g++) {
			if (!Ext.Array.contains(a, m[g][k ? "getSourceId" : "getTargetId"]())) {
				c.push(m[g])
			}
		}
		if (c.length > 0) {
			n.remove(c)
		}
		var o = [];
		for (g = 0; g < h.length; g++) {
			var e = h[g];
			var b = this.getTaskIdFromDependency(e);
			var j = n.getByTaskIds(b, d.getInternalId());
			if (j) {
				j.beginEdit();
				j.setType(e.type);
				j.setLag(e.lag);
				j.setLagUnit(e.lagUnit);
				j.endEdit()
			} else {
				j = new n.model();
				j.setSourceId(k ? b : d.getInternalId());
				j.setTargetId(k ? d.getInternalId() : b);
				j.setType(e.type);
				j.setLag(e.lag);
				j.setLagUnit(e.lagUnit);
				o.push(j)
			}
		}
		if (o.length > 0) {
			n.add(o)
		}
		if (o.length || c.length) {
			d.triggerUIUpdate()
		}
	}
});
Ext.define("Gnt.field.Milestone", {
	extend : "Ext.form.field.ComboBox",
	requires : "Ext.data.JsonStore",
	mixins : ["Gnt.field.mixin.TaskField", "Gnt.mixin.Localizable"],
	alias : "widget.milestonefield",
	instantUpdate : false,
	allowBlank : false,
	forceSelection : true,
	displayField : "text",
	valueField : "value",
	queryMode : "local",
	constructor : function (a) {
		Ext.apply(this, a);
		this.store = new Ext.data.JsonStore({
				fields : ["value", "text"],
				data : [{
						value : 0,
						text : this.L("no")
					}, {
						value : 1,
						text : this.L("yes")
					}
				]
			});
		this.setSuppressTaskUpdate(true);
		this.callParent(arguments);
		this.setSuppressTaskUpdate(false);
		if (this.task) {
			this.setTask(this.task)
		}
	},
	destroy : function () {
		this.destroyTaskListener();
		this.callParent()
	},
	onSetTask : function () {
		this.setValue(this.task.isMilestone() ? 1 : 0)
	},
	valueToVisible : function (a) {
		return a ? this.L("yes") : this.L("no")
	},
	setValue : function (a) {
		this.callParent([a]);
		if (this.instantUpdate && !this.getSuppressTaskUpdate() && this.task) {
			if (this.task.isMilestone() != Boolean(this.value)) {
				this.applyChanges();
				this.task.fireEvent("taskupdated", this.task, this)
			}
		}
	},
	getValue : function () {
		return this.value
	},
	applyChanges : function (a) {
		a = a || this.task;
		if (this.getValue()) {
			a.convertToMilestone()
		} else {
			a.convertToRegular()
		}
	}
});
Ext.define("Gnt.column.ResourceName", {
	extend : "Ext.grid.column.Column",
	alias : "widget.resourcenamecolumn",
	mixins : ["Gnt.mixin.Localizable"],
	dataIndex : "ResourceName",
	flex : 1,
	align : "left",
	_isGanttColumn : false,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		Ext.apply(this, a);
		this.callParent(arguments)
	}
});
Ext.define("Gnt.column.AssignmentUnits", {
	extend : "Ext.grid.column.Number",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.assignmentunitscolumn",
	dataIndex : "Units",
	format : "0 %",
	align : "left",
	_isGanttColumn : false,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments)
	}
});
Ext.define("Gnt.widget.AssignmentGrid", {
	extend : "Ext.grid.Panel",
	alias : "widget.assignmentgrid",
	requires : ["Gnt.model.Resource", "Gnt.model.Assignment", "Gnt.column.ResourceName", "Gnt.column.AssignmentUnits", "Ext.grid.plugin.CellEditing"],
	assignmentStore : null,
	resourceStore : null,
	readOnly : false,
	cls : "gnt-assignmentgrid",
	defaultAssignedUnits : 100,
	taskId : null,
	cellEditing : null,
	sorter : {
		sorterFn : function (b, a) {
			var d = b.getUnits(),
			c = a.getUnits();
			if ((!d && !c) || (d && c)) {
				return b.get("ResourceName") < a.get("ResourceName") ? -1 : 1
			}
			return d ? -1 : 1
		}
	},
	constructor : function (a) {
		this.store = Ext.create("Ext.data.JsonStore", {
				model : Ext.define("Gnt.model.AssignmentEditing", {
					extend : "Gnt.model.Assignment",
					fields : ["ResourceName"]
				})
			});
		this.columns = this.buildColumns();
		if (!this.readOnly) {
			this.plugins = this.buildPlugins()
		}
		Ext.apply(this, {
			selModel : {
				selType : "checkboxmodel",
				mode : "MULTI",
				checkOnly : true,
				selectByPosition : function (b) {
					var c = this.store.getAt(b.row);
					this.select(c, true)
				}
			}
		});
		this.callParent(arguments)
	},
	initComponent : function () {
		this.loadResources();
		this.mon(this.resourceStore, {
			datachanged : this.loadResources,
			scope : this
		});
		this.getSelectionModel().on("select", this.onSelect, this, {
			delay : 50
		});
		this.callParent(arguments)
	},
	onSelect : function (b, a) {
		if ((!this.cellEditing || !this.cellEditing.getActiveEditor()) && !a.getUnits()) {
			a.setUnits(this.defaultAssignedUnits)
		}
	},
	loadResources : function () {
		var e = [],
		b = this.resourceStore,
		f;
		for (var c = 0, a = b.getCount(); c < a; c++) {
			var d = b.getAt(c);
			f = d.getId();
			e.push({
				ResourceId : f,
				ResourceName : d.getName(),
				Units : ""
			})
		}
		this.store.loadData(e)
	},
	buildPlugins : function () {
		var a = this.cellEditing = Ext.create("Ext.grid.plugin.CellEditing", {
				clicksToEdit : 1
			});
		a.on("edit", this.onEditingDone, this);
		return [a]
	},
	hide : function () {
		this.cellEditing.cancelEdit();
		this.callParent(arguments)
	},
	onEditingDone : function (a, b) {
		if (b.value) {
			this.getSelectionModel().select(b.record, true)
		} else {
			this.getSelectionModel().deselect(b.record);
			b.record.reject()
		}
	},
	buildColumns : function () {
		return [{
				xtype : "resourcenamecolumn"
			}, {
				xtype : "assignmentunitscolumn",
				assignmentStore : this.assignmentStore,
				editor : {
					xtype : "numberfield",
					minValue : 0,
					step : 10
				}
			}
		]
	},
	loadTaskAssignments : function (d) {
		var b = this.store,
		f = this.getSelectionModel();
		this.taskId = d;
		f.deselectAll(true);
		for (var c = 0, a = b.getCount(); c < a; c++) {
			b.getAt(c).data.Units = "";
			b.getAt(c).data.Id = null;
			delete b.getAt(c).__id__
		}
		b.suspendEvents();
		var e = this.assignmentStore.queryBy(function (g) {
				return g.getTaskId() == d
			});
		e.each(function (h) {
			var g = b.findRecord("ResourceId", h.getResourceId(), 0, false, true, true);
			if (g) {
				g.setUnits(h.getUnits());
				g.__id__ = h.getId();
				f.select(g, true, true)
			}
		});
		b.resumeEvents();
		b.sort(this.sorter);
		this.getView().refresh()
	},
	saveTaskAssignments : function () {
		var a = this.assignmentStore,
		e = this.taskId;
		var d = {};
		var c = [];
		this.getSelectionModel().selected.each(function (g) {
			var f = g.getUnits();
			if (f > 0) {
				var i = g.__id__;
				if (i) {
					d[i] = true;
					a.getById(i).setUnits(f)
				} else {
					var h = Ext.create(a.model);
					h.setTaskId(e);
					h.setResourceId(g.getResourceId());
					h.setUnits(f);
					d[h.internalId] = true;
					c.push(h)
				}
			}
		});
		var b = [];
		a.each(function (f) {
			if (f.getTaskId() == e && !d[f.getId() || f.internalId]) {
				b.push(f)
			}
		});
		a.fireEvent("beforetaskassignmentschange", a, e, c);
		a.suspendAutoSync();
		a.remove(b);
		a.add(c);
		a.resumeAutoSync();
		a.fireEvent("taskassignmentschanged", a, e, c);
		if (a.autoSync) {
			a.sync()
		}
	}
});
Ext.define("Gnt.field.Assignment", {
	extend : "Ext.form.field.Picker",
	alias : ["widget.assignmentfield", "widget.assignmenteditor"],
	alternateClassName : "Gnt.widget.AssignmentField",
	requires : ["Gnt.widget.AssignmentGrid"],
	mixins : ["Gnt.mixin.Localizable"],
	matchFieldWidth : false,
	editable : false,
	task : null,
	assignmentStore : null,
	resourceStore : null,
	gridConfig : null,
	formatString : "{0} [{1}%]",
	expandPickerOnFocus : false,
	afterRender : function () {
		this.callParent(arguments);
		this.on("expand", this.onPickerExpand, this);
		if (this.expandPickerOnFocus) {
			this.on("focus", function () {
				this.expand()
			}, this)
		}
	},
	createPicker : function () {
		var a = new Gnt.widget.AssignmentGrid(Ext.apply({
					ownerCt : this.ownerCt,
					renderTo : document.body,
					frame : true,
					floating : true,
					hidden : true,
					height : 200,
					width : 300,
					resourceStore : this.task.getResourceStore(),
					assignmentStore : this.task.getAssignmentStore(),
					fbar : this.buildButtons()
				}, this.gridConfig || {}));
		return a
	},
	buildButtons : function () {
		return ["->", {
				text : this.L("closeText"),
				handler : function () {
					Ext.Function.defer(this.onSaveClick, Ext.isIE && !Ext.isIE9 ? 60 : 30, this)
				},
				scope : this
			}, {
				text : this.L("cancelText"),
				handler : function () {
					this.collapse();
					this.fireEvent("blur", this)
				},
				scope : this
			}
		]
	},
	setTask : function (a) {
		this.task = a;
		this.setRawValue(this.getDisplayValue(a))
	},
	onPickerExpand : function () {
		this.picker.loadTaskAssignments(this.task.getInternalId())
	},
	onSaveClick : function () {
		var b = this.picker.getSelectionModel(),
		a = b.selected;
		this.collapse();
		this.fireEvent("blur", this);
		this.fireEvent("select", this, a);
		Ext.Function.defer(this.picker.saveTaskAssignments, 1, this.picker)
	},
	collapseIf : function (b) {
		var a = this;
		if (this.picker && !b.getTarget("." + Ext.baseCSSPrefix + "editor") && !b.getTarget("." + Ext.baseCSSPrefix + "menu-item")) {
			a.callParent(arguments)
		}
	},
	mimicBlur : function (b) {
		var a = this;
		if (!b.getTarget("." + Ext.baseCSSPrefix + "editor") && !b.getTarget("." + Ext.baseCSSPrefix + "menu-item")) {
			a.callParent(arguments)
		}
	},
	isDirty : function (c) {
		c = c || this.task;
		if (!c) {
			return false
		}
		var e = this.picker && this.picker.assignmentStore || c.getAssignmentStore(),
		b = c.getAssignments(),
		f;
		for (var d = 0, a = b.length; d < a; d++) {
			if (b[d].dirty || b[d].phantom) {
				return true
			}
		}
		if (e) {
			b = e.getRemovedRecords();
			for (d = 0, a = b.length; d < a; d++) {
				if (b[d].getTaskId() == c.getInternalId()) {
					return true
				}
			}
		}
		return false
	},
	getDisplayValue : function (c) {
		c = c || this.task;
		var g = [],
		f = this.assignmentStore,
		h,
		e = c.getInternalId(),
		b = c.getAssignments();
		for (var d = 0, a = b.length; d < a; d++) {
			h = b[d];
			if (h.data.Units > 0) {
				g.push(Ext.String.format(this.formatString, h.getResourceName(), h.getUnits()))
			}
		}
		return g.join(", ")
	}
}, function () {
	Gnt.widget.AssignmentCellEditor = function () {
		var a = console;
		if (a && a.log) {
			a.log("Gnt.widget.AssignmentCellEditor is deprecated and should no longer be used. Instead simply use Gnt.field.Assignment.")
		}
	}
});
Ext.define("Gnt.column.ResourceAssignment", {
	extend : "Ext.grid.column.Column",
	alias : "widget.resourceassignmentcolumn",
	requires : ["Gnt.field.Assignment"],
	mixins : ["Gnt.mixin.Localizable"],
	tdCls : "sch-assignment-cell",
	showUnits : true,
	field : null,
	constructor : function (b) {
		b = b || {};
		this.text = b.text || this.L("text");
		var c = b.field || b.editor;
		var a = b.showUnits || this.showUnits;
		delete b.field;
		delete b.editor;
		b.editor = c || {};
		if (!(b.editor instanceof Ext.form.Field)) {
			b.editor = Ext.ComponentManager.create(Ext.applyIf(b.editor, {
						expandPickerOnFocus : true,
						formatString : "{0}" + (a ? " [{1}%]" : "")
					}), "assignmentfield")
		}
		b.field = b.editor;
		this.callParent([b]);
		this.scope = this
	},
	getContainingPanel : function () {
		if (!this.panel) {
			this.panel = this.up("tablepanel")
		}
		return this.panel
	},
	setDirtyClass : function (c, b) {
		var a = this.getContainingPanel().getView();
		if (a.markDirty && this.field.isDirty(b)) {
			c.tdCls = a.dirtyCls
		}
	},
	renderer : function (b, c, a) {
		this.setDirtyClass(c, a);
		return this.field.getDisplayValue(a)
	}
});
Ext.define("Gnt.column.Name", {
	extend : "Ext.tree.Column",
	alias : "widget.namecolumn",
	mixins : ["Gnt.mixin.Localizable"],
	draggable : true,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		var b = a.field || a.editor;
		delete a.field;
		delete a.editor;
		Ext.apply(this, a);
		a.editor = b || {
			xtype : "textfield",
			allowBlank : false
		};
		this.scope = this;
		this.callParent([a])
	},
	afterRender : function () {
		var b = this,
		a = this.up("treepanel");
		if (!this.dataIndex) {
			this.dataIndex = a.store.model.prototype.nameField
		}
		this.callParent(arguments)
	},
	renderer : function (b, c, a) {
		if (!a.isEditable(this.dataIndex)) {
			c.tdCls = (c.tdCls || "") + " sch-column-readonly"
		}
		return Ext.util.Format.htmlEncode(b)
	}
});
Ext.define("Gnt.column.Note", {
	extend : "Ext.grid.column.Column",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.notecolumn",
	field : {
		xtype : "textfield"
	},
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.scope = this
	},
	afterRender : function () {
		var a = this.up("treepanel");
		if (!this.dataIndex) {
			this.dataIndex = a.store.model.prototype.noteField
		}
		this.callParent(arguments)
	},
	renderer : function (b, c, a) {
		if (!a.isEditable(this.dataIndex)) {
			c.tdCls = (c.tdCls || "") + " sch-column-readonly"
		}
		return b
	}
});
Ext.define("Gnt.column.EndDate", {
	extend : "Ext.grid.column.Date",
	alias : "widget.enddatecolumn",
	requires : ["Ext.grid.CellEditor", "Gnt.field.EndDate"],
	mixins : ["Gnt.mixin.Localizable"],
	width : 100,
	align : "left",
	editorFormat : null,
	adjustMilestones : true,
	validateStartDate : true,
	instantUpdate : false,
	keepDuration : false,
	field : null,
	constructor : function (b) {
		b = b || {};
		this.text = b.text || this.L("text");
		var c = b.field || b.editor;
		delete b.field;
		var a = {
			format : b.editorFormat || b.format || this.format || Ext.Date.defaultFormat,
			instantUpdate : this.instantUpdate,
			adjustMilestones : this.adjustMilestones,
			keepDuration : this.keepDuration,
			validateStartDate : this.validateStartDate
		};
		Ext.Array.forEach(["instantUpdate", "adjustMilestones", "keepDuration", "validateStartDate"], function (d) {
			if (d in b) {
				a[d] = b[d]
			}
		}, this);
		b.editor = c || a;
		if (!(b.editor instanceof Gnt.field.EndDate)) {
			Ext.applyIf(b.editor, a);
			b.editor = Ext.ComponentManager.create(b.editor, "enddatefield")
		}
		b.field = b.editor;
		this.hasCustomRenderer = true;
		this.callParent([b]);
		this.scope = this;
		this.renderer = b.renderer || this.rendererFunc;
		this.editorFormat = this.editorFormat || this.format
	},
	rendererFunc : function (b, c, a) {
		if (!b) {
			return
		}
		if (!a.isEditable(this.dataIndex)) {
			c.tdCls = (c.tdCls || "") + " sch-column-readonly"
		}
		b = this.field.valueToVisible(b, a);
		return Ext.util.Format.date(b, this.format)
	},
	afterRender : function () {
		var a = this.ownerCt.up("treepanel");
		if (!this.dataIndex) {
			this.dataIndex = a.store.model.prototype.endDateField
		}
		this.callParent(arguments)
	}
});
Ext.define("Gnt.column.PercentDone", {
	extend : "Ext.grid.column.Number",
	alias : "widget.percentdonecolumn",
	mixins : ["Gnt.mixin.Localizable"],
	width : 50,
	format : "0",
	align : "center",
	field : {
		xtype : "percentfield",
		decimalPrecision : 0,
		minValue : 0,
		maxValue : 100
	},
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.scope = this
	},
	afterRender : function () {
		var a = this.up("treepanel");
		if (!this.dataIndex) {
			this.dataIndex = a.store.model.prototype.percentDoneField
		}
		this.callParent(arguments)
	},
	renderer : function (b, c, a) {
		if (!a.isEditable(this.dataIndex)) {
			c.tdCls = (c.tdCls || "") + " sch-column-readonly"
		}
		return this.defaultRenderer(b, c, a)
	}
});
Ext.define("Gnt.column.StartDate", {
	extend : "Ext.grid.column.Date",
	alias : "widget.startdatecolumn",
	requires : ["Gnt.field.StartDate"],
	mixins : ["Gnt.mixin.Localizable"],
	width : 100,
	align : "left",
	editorFormat : null,
	adjustMilestones : true,
	instantUpdate : false,
	keepDuration : true,
	field : null,
	constructor : function (b) {
		b = b || {};
		this.text = b.text || this.L("text");
		var c = b.field || b.editor;
		delete b.field;
		var a = {
			format : b.editorFormat || b.format || this.format || Ext.Date.defaultFormat,
			instantUpdate : this.instantUpdate,
			adjustMilestones : this.adjustMilestones,
			keepDuration : this.keepDuration
		};
		Ext.Array.forEach(["instantUpdate", "adjustMilestones", "keepDuration"], function (d) {
			if (d in b) {
				a[d] = b[d]
			}
		}, this);
		b.editor = c || a;
		if (!(b.editor instanceof Gnt.field.StartDate)) {
			Ext.applyIf(b.editor, a);
			b.editor = Ext.ComponentManager.create(b.editor, "startdatefield")
		}
		b.field = b.editor;
		this.hasCustomRenderer = true;
		this.callParent([b]);
		this.renderer = b.renderer || this.rendererFunc;
		this.editorFormat = this.editorFormat || this.format
	},
	afterRender : function () {
		var a = this.up("treepanel");
		var b = a.store;
		if (!this.dataIndex) {
			this.dataIndex = b.model.prototype.startDateField
		}
		this.callParent(arguments)
	},
	rendererFunc : function (b, c, a) {
		if (!b) {
			return
		}
		if (!a.isEditable(this.dataIndex)) {
			c.tdCls = (c.tdCls || "") + " sch-column-readonly"
		}
		b = this.field.valueToVisible(b, a);
		return Ext.util.Format.date(b, this.format)
	}
});
Ext.define("Gnt.column.WBS", {
	extend : "Ext.grid.column.Column",
	alias : "widget.wbscolumn",
	mixins : ["Gnt.mixin.Localizable"],
	width : 40,
	align : "left",
	sortable : false,
	dataIndex : "index",
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments)
	},
	renderer : function (b, c, a) {
		return a.getWBSCode()
	}
});
Ext.define("Gnt.column.Sequence", {
	extend : "Ext.grid.column.Column",
	alias : "widget.sequencecolumn",
	mixins : ["Gnt.mixin.Localizable"],
	width : 40,
	align : "right",
	sortable : false,
	dataIndex : "index",
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments)
	},
	renderer : function (b, c, a) {
		return a.getSequenceNumber()
	}
});
Ext.define("Gnt.column.SchedulingMode", {
	extend : "Ext.grid.column.Column",
	requires : ["Gnt.field.SchedulingMode"],
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.schedulingmodecolumn",
	width : 100,
	align : "left",
	data : null,
	instantUpdate : false,
	field : null,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		var b = a.field || a.editor || new Gnt.field.SchedulingMode({
				store : a.data || Gnt.field.SchedulingMode.prototype.store,
				instantUpdate : this.instantUpdate
			});
		delete a.field;
		delete a.editor;
		if (!(b instanceof Gnt.field.SchedulingMode)) {
			Ext.applyIf(b, {
				instantUpdate : this.instantUpdate
			});
			b = Ext.ComponentManager.create(b, "schedulingmodefield")
		}
		a.field = a.editor = b;
		this.scope = this;
		this.callParent([a])
	},
	renderer : function (b, c, a) {
		return this.field.valueToVisible(b, a)
	},
	afterRender : function () {
		this.callParent(arguments);
		var a = this.up("treepanel");
		this.mon(a, "beforeedit", function (b, c) {
			if (this.field.setTask) {
				this.field.setTask(c.record)
			}
		}, this);
		if (!this.dataIndex) {
			this.dataIndex = a.store.model.prototype.schedulingModeField
		}
	}
});
Ext.define("Gnt.column.AddNew", {
	extend : "Ext.grid.column.Column",
	alias : "widget.addnewcolumn",
	requires : ["Ext.form.field.ComboBox", "Ext.Editor"],
	mixins : ["Gnt.mixin.Localizable"],
	width : 100,
	resizable : false,
	sortable : false,
	draggable : false,
	colEditor : null,
	columnList : null,
	initComponent : function () {
		if (!this.text) {
			this.text = this.L("text")
		}
		this.addCls("gnt-addnewcolumn");
		this.on({
			headerclick : this.myOnHeaderClick,
			headertriggerclick : this.myOnHeaderClick,
			scope : this
		});
		this.callParent(arguments)
	},
	getGantt : function () {
		if (!this.gantt) {
			this.gantt = this.up("ganttpanel")
		}
		return this.gantt
	},
	getContainingPanel : function () {
		if (!this.panel) {
			this.panel = this.up("tablepanel")
		}
		return this.panel
	},
	myOnHeaderClick : function () {
		if (!this.combo) {
			var a = this.getContainingPanel();
			var e,
			d = this;
			if (this.columnList) {
				e = this.columnList
			} else {
				e = Ext.Array.map(Ext.ClassManager.getNamesByExpression("Gnt.column.*"), function (g) {
						var f = Ext.ClassManager.get(g);
						if (f.prototype._isGanttColumn === false || d instanceof f) {
							return null
						}
						return {
							clsName : g,
							text : f.prototype.localize ? f.prototype.localize("text") : f.prototype.text
						}
					});
				e = Ext.Array.clean(e).sort(function (g, f) {
						return g.text > f.text ? 1 : -1
					})
			}
			var c = this.colEditor = new Ext.Editor({
					shadow : false,
					updateEl : false,
					itemId : "addNewEditor",
					renderTo : this.el,
					offsets : [20, 0],
					field : new Ext.form.field.ComboBox({
						displayField : "text",
						valueField : "clsName",
						hideTrigger : true,
						queryMode : "local",
						listConfig : {
							itemId : "addNewEditorComboList",
							minWidth : 150
						},
						store : new Ext.data.Store({
							fields : ["text", "clsName"],
							data : e
						}),
						listeners : {
							render : function () {
								this.on("blur", function () {
									c.cancelEdit()
								})
							},
							select : this.onSelect,
							scope : this
						}
					})
				})
		}
		var b = this.el.down("." + Ext.baseCSSPrefix + "column-header-text");
		this.colEditor.startEdit(b, "");
		this.colEditor.field.setWidth(this.getWidth() - 20);
		this.colEditor.field.expand();
		return false
	},
	onSelect : function (g, c) {
		var h = c[0];
		var a = this.ownerCt;
		var f = h.get("clsName");
		var b = this.getContainingPanel().getView();
		var e,
		d = function () {
			e = true
		};
		b.on("refresh", d);
		this.colEditor.cancelEdit();
		Ext.require(f, function () {
			var i = Ext.create(f);
			a.insert(a.items.indexOf(this), i);
			if (!e) {
				b.refresh()
			}
			b.un("refresh", d)
		}, this)
	}
});
Ext.define("Gnt.column.EarlyStartDate", {
	extend : "Ext.grid.column.Date",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.earlystartdatecolumn",
	width : 100,
	align : "left",
	adjustMilestones : true,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.renderer = a.renderer || this.rendererFunc;
		this.scope = a.scope || this;
		this.hasCustomRenderer = true
	},
	afterRender : function () {
		var a = this.up("ganttpanel");
		a.registerLockedDependencyListeners();
		this.callParent(arguments)
	},
	rendererFunc : function (b, c, a) {
		c.tdCls = (c.tdCls || "") + " sch-column-readonly";
		return a.getDisplayStartDate(this.format, this.adjustMilestones, a.getEarlyStartDate())
	}
});
Ext.define("Gnt.column.EarlyEndDate", {
	extend : "Ext.grid.column.Date",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.earlyenddatecolumn",
	width : 100,
	align : "left",
	adjustMilestones : true,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.renderer = a.renderer || this.rendererFunc;
		this.scope = a.scope || this;
		this.hasCustomRenderer = true
	},
	afterRender : function () {
		var a = this.up("ganttpanel");
		a.registerLockedDependencyListeners();
		this.callParent(arguments)
	},
	rendererFunc : function (b, c, a) {
		c.tdCls = (c.tdCls || "") + " sch-column-readonly";
		return a.getDisplayEndDate(this.format, this.adjustMilestones, a.getEarlyEndDate())
	}
});
Ext.define("Gnt.column.LateStartDate", {
	extend : "Ext.grid.column.Date",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.latestartdatecolumn",
	width : 100,
	align : "left",
	adjustMilestones : true,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.renderer = a.renderer || this.rendererFunc;
		this.scope = a.scope || this;
		this.hasCustomRenderer = true
	},
	afterRender : function () {
		var a = this.up("ganttpanel");
		a.registerLockedDependencyListeners();
		this.callParent(arguments)
	},
	rendererFunc : function (b, c, a) {
		c.tdCls = (c.tdCls || "") + " sch-column-readonly";
		return a.getDisplayStartDate(this.format, this.adjustMilestones, a.getLateStartDate())
	}
});
Ext.define("Gnt.column.LateEndDate", {
	extend : "Ext.grid.column.Date",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.lateenddatecolumn",
	width : 100,
	align : "left",
	adjustMilestones : true,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.renderer = a.renderer || this.rendererFunc;
		this.scope = a.scope || this;
		this.hasCustomRenderer = true
	},
	afterRender : function () {
		var a = this.up("ganttpanel");
		a.registerLockedDependencyListeners();
		this.callParent(arguments)
	},
	rendererFunc : function (b, c, a) {
		c.tdCls = (c.tdCls || "") + " sch-column-readonly";
		return a.getDisplayEndDate(this.format, this.adjustMilestones, a.getLateEndDate())
	}
});
Ext.define("Gnt.column.Slack", {
	extend : "Ext.grid.column.Column",
	requires : ["Ext.Number", "Sch.util.Date"],
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.slackcolumn",
	decimalPrecision : 2,
	useAbbreviation : false,
	slackUnit : "d",
	width : 100,
	align : "left",
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.renderer = a.renderer || this.rendererFunc;
		this.scope = a.scope || this;
		this.hasCustomRenderer = true
	},
	afterRender : function () {
		var a = this.up("ganttpanel");
		a.registerLockedDependencyListeners();
		this.callParent(arguments)
	},
	rendererFunc : function (b, c, a) {
		c.tdCls = (c.tdCls || "") + " sch-column-readonly";
		b = a.getSlack();
		if (Ext.isNumber(b)) {
			return parseFloat(Ext.Number.toFixed(b, this.decimalPrecision)) + " " + Sch.util.Date[this.useAbbreviation ? "getShortNameOfUnit" : "getReadableNameOfUnit"](this.slackUnit, b !== 1)
		}
		return ""
	}
});
Ext.define("Gnt.column.BaselineStartDate", {
	extend : "Ext.grid.column.Date",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.baselinestartdatecolumn",
	width : 100,
	adjustMilestones : true,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.renderer = a.renderer || this.rendererFunc;
		this.scope = a.scope || this;
		this.hasCustomRenderer = true
	},
	rendererFunc : function (b, c, a) {
		c.tdCls = (c.tdCls || "") + " sch-column-readonly";
		return a.getDisplayStartDate(this.format, this.adjustMilestones, a.getBaselineStartDate(), false, true)
	}
});
Ext.define("Gnt.column.BaselineEndDate", {
	extend : "Ext.grid.column.Date",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.baselineenddatecolumn",
	width : 100,
	adjustMilestones : true,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments);
		this.renderer = a.renderer || this.rendererFunc;
		this.scope = a.scope || this;
		this.hasCustomRenderer = true
	},
	rendererFunc : function (b, c, a) {
		c.tdCls = (c.tdCls || "") + " sch-column-readonly";
		return a.getDisplayEndDate(this.format, this.adjustMilestones, a.getBaselineEndDate(), false, true)
	}
});
Ext.define("Gnt.column.Milestone", {
	extend : "Ext.grid.column.Column",
	alias : "widget.milestonecolumn",
	mixins : ["Gnt.mixin.Localizable"],
	width : 50,
	align : "center",
	constructor : function (a) {
		a = a || {};
		a.editor = a.editor || new Gnt.field.Milestone();
		this.text = a.text || this.L("text");
		this.field = a.editor;
		this.callParent(arguments);
		this.scope = this
	},
	renderer : function (b, c, a) {
		return this.field.valueToVisible(a.isMilestone())
	}
});
Ext.define("Gnt.widget.AssignmentEditGrid", {
	extend : "Ext.grid.Panel",
	alias : "widget.assignmenteditgrid",
	requires : ["Ext.data.JsonStore", "Ext.window.MessageBox", "Ext.form.field.ComboBox", "Ext.grid.plugin.CellEditing", "Gnt.util.Data", "Gnt.data.AssignmentStore", "Gnt.data.ResourceStore", "Gnt.model.Resource", "Gnt.model.Assignment", "Gnt.column.ResourceName", "Gnt.column.AssignmentUnits"],
	mixins : ["Gnt.mixin.Localizable"],
	assignmentStore : null,
	resourceStore : null,
	readOnly : false,
	cls : "gnt-assignmentgrid",
	defaultAssignedUnits : 100,
	confirmAddResource : true,
	addResources : true,
	taskId : null,
	refreshTimeout : 100,
	resourceDupStore : null,
	resourceComboStore : null,
	constructor : function (a) {
		Ext.apply(this, a);
		this.confirmAddResource = this.confirmAddResourceText !== false;
		this.store = a.store || new Gnt.data.AssignmentStore({
				taskStore : a.taskStore || a.assignmentStore.getTaskStore()
			});
		this.resourceDupStore = a.resourceDupStore || new Gnt.data.ResourceStore({
				taskStore : a.taskStore || a.assignmentStore.getTaskStore()
			});
		this.resourceComboStore = new Ext.data.JsonStore({
				model : Gnt.model.Resource
			});
		if (a.addResources !== undefined) {
			this.addResources = a.addResources
		}
		this.columns = this.buildColumns();
		if (!this.readOnly) {
			this.plugins = this.buildPlugins()
		}
		this.callParent(arguments)
	},
	initComponent : function () {
		this.loadResources();
		var a = Ext.Function.createBuffered(this.loadResources, this.refreshTimeout, this, []);
		this.mon(this.resourceStore, {
			add : a,
			remove : a,
			load : a,
			clear : a
		});
		this.loadTaskAssignments();
		var b = Ext.Function.createBuffered(this.loadTaskAssignments, this.refreshTimeout, this, []);
		this.mon(this.assignmentStore, {
			add : b,
			remove : b,
			load : b,
			clear : b
		});
		this.callParent(arguments)
	},
	loadResources : function (b) {
		if (!this.resourceStore) {
			return false
		}
		var a = Gnt.util.Data.cloneModelSet(this.resourceStore, function (c, d) {
				if (!c.getId()) {
					c.setId(d.getInternalId())
				}
			});
		this.resourceDupStore.loadData(a);
		this.resourceComboStore.loadData(a);
		if (!b) {
			this.loadTaskAssignments()
		}
		return true
	},
	loadTaskAssignments : function (d) {
		d = d || this.taskId;
		if (!d) {
			return false
		}
		var c = this.taskStore || this.assignmentStore.getTaskStore(),
		b = c && c.getByInternalId(d),
		g;
		if (b) {
			g = b.assignments
		} else {
			if (!this.assignmentStore) {
				return false
			}
			g = this.assignmentStore.queryBy(function (h) {
					return h.getTaskId() == d
				})
		}
		this.taskId = d;
		var a = this.store,
		e = this.resourceDupStore,
		f = Gnt.util.Data.cloneModelSet(g, function (i, h) {
				var k = h.getResourceId();
				var j = e.queryBy(function (m) {
						var l = m.originalRecord;
						return (l.getId() || l.internalId) == k
					});
				if (j.getCount()) {
					j = j.first();
					i.setResourceId(j.getId() || j.internalId)
				}
			});
		a.loadData(f);
		return true
	},
	insertAssignment : function (b, g) {
		if (!this.store) {
			return
		}
		var d = this.store.model.prototype,
		c = {};
		if (b) {
			c = b
		} else {
			c[d.unitsField] = this.defaultAssignedUnits
		}
		c[d.taskIdField] = this.taskId;
		var e = this.store.insert(0, c);
		var f = this,
		a = e[0].isValid;
		e[0].isValid = function () {
			return a.apply(this, arguments) && f.isValidAssignment(this)
		};
		if (!g) {
			this.cellEditing.startEditByPosition({
				row : 0,
				column : 0
			})
		}
		return e
	},
	isValid : function () {
		var a = true;
		this.store.each(function (b) {
			if (!b.isValid()) {
				a = false;
				return false
			}
		});
		return a
	},
	getAssignmentErrors : function (a) {
		var b = a.getResourceId();
		if (!b) {
			return [this.L("noValueText")]
		}
		if (!this.resourceDupStore.getByInternalId(b)) {
			return [Ext.String.format(this.L("noResourceText"), b)]
		}
	},
	isValidAssignment : function (a) {
		return !this.getAssignmentErrors(a)
	},
	buildPlugins : function () {
		var a = this.cellEditing = Ext.create("Ext.grid.plugin.CellEditing", {
				clicksToEdit : 1
			});
		var b = a.startEdit;
		a.startEdit = function () {
			this.completeEdit();
			return b.apply(this, arguments)
		};
		a.on({
			beforeedit : this.onEditingStart,
			scope : this
		});
		return [a]
	},
	hide : function () {
		this.cellEditing.cancelEdit();
		return this.callParent(arguments)
	},
	onEditingStart : function (a, c) {
		var b = this.store.model.prototype;
		if (c.field == b.resourceIdField) {
			this.assignment = c.record;
			this.resourceId = c.record.getResourceId();
			this.resourceComboStore.loadData(this.resourceDupStore.getRange());
			this.resourceComboStore.filter(this.resourcesFilter)
		}
	},
	resourceRender : function (b, c, d) {
		var e = this.getAssignmentErrors(d);
		if (e && e.length) {
			c.tdCls = Ext.baseCSSPrefix + "form-invalid";
			c.tdAttr = 'data-errorqtip="' + e.join("<br>") + '"'
		} else {
			c.tdCls = "";
			c.tdAttr = 'data-errorqtip=""'
		}
		var a = this.resourceDupStore.getByInternalId(b);
		return Ext.String.htmlEncode((a && a.getName()) || b)
	},
	filterResources : function (c) {
		var d = c.getInternalId(),
		b = Gnt.model.Assignment.prototype.resourceIdField,
		a = true;
		if (d !== this.resourceId) {
			this.store.each(function (e) {
				if (d == e.get(b)) {
					a = false;
					return false
				}
			})
		}
		return a
	},
	onResourceComboAssert : function (f) {
		var e = f.getRawValue();
		if (e) {
			var a = this.resourceDupStore.findExact(f.displayField, e);
			var b = a !== -1 ? this.resourceDupStore.getAt(a) : false;
			if (!b) {
				var h = this.assignment;
				var d = this;
				var c = function (j) {
					var k = Gnt.model.Resource.prototype,
					i = {};
					i[k.nameField] = f.rawValue;
					i = Ext.ModelManager.create(i, Gnt.model.Resource);
					i.setId(i.internalId);
					var l = d.resourceDupStore.add(i);
					if (l && l.length) {
						if (!j) {
							f.getStore().add(i);
							f.setValue(l[0].getId())
						} else {
							h.setResourceId(l[0].getId())
						}
					}
				};
				if (this.confirmAddResource) {
					var g = Ext.String.format(this.L("confirmAddResourceText"), Ext.String.htmlEncode(e));
					Ext.Msg.confirm(this.L("confirmAddResourceTitle"), g, function (i) {
						if (i == "yes") {
							c(true)
						}
					})
				} else {
					c()
				}
			} else {
				f.select(b, true)
			}
		}
	},
	buildColumns : function () {
		var a = this;
		this.resourceCombo = new Ext.form.field.ComboBox({
				queryMode : "local",
				store : this.resourceComboStore,
				allowBlank : false,
				editing : this.addResources,
				validateOnChange : false,
				autoSelect : false,
				forceSelection : !this.addResources,
				valueField : Gnt.model.Resource.prototype.idProperty,
				displayField : Gnt.model.Resource.prototype.nameField,
				queryCaching : false,
				listConfig : {
					getInnerTpl : function () {
						return "{" + this.displayField + ":htmlEncode}"
					}
				}
			});
		if (this.addResources) {
			this.resourcesFilter = Ext.create("Ext.util.Filter", {
					filterFn : this.filterResources,
					scope : this
				});
			Ext.Function.interceptBefore(this.resourceCombo, "assertValue", function () {
				a.onResourceComboAssert(this)
			})
		}
		return [{
				xtype : "resourcenamecolumn",
				editor : this.resourceCombo,
				dataIndex : Gnt.model.Assignment.prototype.resourceIdField,
				renderer : this.resourceRender,
				scope : this
			}, {
				xtype : "assignmentunitscolumn",
				assignmentStore : this.assignmentStore,
				editor : {
					xtype : "percentfield",
					step : 10
				}
			}
		]
	},
	saveResources : function () {
		Gnt.util.Data.applyCloneChanges(this.resourceDupStore, this.resourceStore)
	},
	saveTaskAssignments : function () {
		this.resourceStore.suspendEvents(true);
		this.assignmentStore.suspendEvents(true);
		this.saveResources();
		var b = this.store.model,
		c = this.resourceDupStore,
		a = true;
		Gnt.util.Data.applyCloneChanges(this.store, this.assignmentStore, function (f) {
			var e = c.getByInternalId(this.getResourceId());
			if (!e || !e.originalRecord) {
				a = false;
				return
			}
			var d = e.originalRecord;
			f[b.prototype.resourceIdField] = d.getId() || d.internalId
		});
		this.resourceStore.resumeEvents();
		this.assignmentStore.resumeEvents();
		return a
	}
});
Ext.define("Gnt.widget.DependencyGrid", {
	extend : "Ext.grid.Panel",
	alias : "widget.dependencygrid",
	requires : ["Ext.data.JsonStore", "Ext.grid.plugin.CellEditing", "Ext.form.field.ComboBox", "Ext.util.Filter", "Gnt.model.Dependency", "Gnt.util.Data", "Gnt.field.Duration"],
	mixins : ["Gnt.mixin.Localizable"],
	readOnly : false,
	showCls : false,
	cls : "gnt-dependencygrid",
	task : null,
	dependencyStore : null,
	taskModel : null,
	direction : "predecessors",
	oppositeStore : null,
	taskStoreListeners : null,
	refreshTimeout : 100,
	allowParentTaskDependencies : false,
	useSequenceNumber : false,
	constructor : function (b) {
		b = b || {};
		Ext.Array.each(["idText", "taskText", "blankTaskText", "invalidDependencyText", "parentChildDependencyText", "duplicatingDependencyText", "transitiveDependencyText", "cyclicDependencyText", "typeText", "lagText", "clsText", "endToStartText", "startToStartText", "endToEndText", "startToEndText"], function (c) {
			if (c in b) {
				this[c] = b[c]
			}
		}, this);
		this.store = b.store || new Ext.data.JsonStore({
				model : "Gnt.model.Dependency"
			});
		if (!this.readOnly) {
			this.plugins = this.buildPlugins()
		}
		this.direction = b.direction || this.direction;
		if (!b.taskModel) {
			b.taskModel = Ext.ClassManager.get("Gnt.model.Task");
			if (b.dependencyStore) {
				var a = b.dependencyStore.getTaskStore();
				if (a) {
					b.taskModel = a.model
				}
			}
		}
		if (b.oppositeStore) {
			this.setOppositeStore(b.oppositeStore)
		}
		if (b.task) {
			this.loadDependencies(b.task)
		}
		this.callParent([b])
	},
	initComponent : function () {
		this.columns = this.buildColumns();
		this.callParent(arguments)
	},
	destroy : function () {
		this.cellEditing.destroy();
		if (this.deferredStoreBind) {
			this.tasksCombo.un("render", this.bindTaskStore, this)
		}
		this.tasksCombo.destroy();
		this.lagEditor.destroy();
		this.callParent(arguments)
	},
	setTask : function (a) {
		if (!a) {
			return
		}
		this.task = a;
		var b = a.dependencyStore || a.getTaskStore().dependencyStore;
		if (b && b !== this.dependencyStore) {
			this.dependencyStore = b;
			if (this.typesCombo) {
				this.typesCombo.store.filter()
			}
			this.mon(this.dependencyStore, {
				datachanged : function () {
					this.loadDependencies()
				},
				scope : this
			})
		}
	},
	buildPlugins : function () {
		var a = this.cellEditing = new Ext.grid.plugin.CellEditing({
				clicksToEdit : 1
			});
		a.on({
			beforeedit : this.onEditingStart,
			edit : this.onEditingDone,
			scope : this
		});
		return [a]
	},
	hide : function () {
		this.cellEditing.cancelEdit();
		this.callParent(arguments)
	},
	onEditingStart : function (a, c) {
		var b = this.store.model.prototype;
		switch (c.field) {
		case b.lagField:
			this.lagEditor.durationUnit = c.record.getLagUnit();
			break;
		case b.typeField:
			this.typesCombo.store.filter();
			if (this.typesCombo.store.count() < 2) {
				return false
			}
			break
		}
	},
	onEditingDone : function (a, c) {
		var b = this.store.model.prototype;
		if (c.field == b.lagField) {
			c.record.setLagUnit(this.lagEditor.durationUnit)
		}
		this.getView().refresh()
	},
	dependencyTypeRender : function (b) {
		var a = this.store.model.Type;
		switch (b) {
		case a.EndToStart:
			return this.L("endToStartText");
		case a.StartToStart:
			return this.L("startToStartText");
		case a.EndToEnd:
			return this.L("endToEndText");
		case a.StartToEnd:
			return this.L("startToEndText")
		}
		return b
	},
	taskValidate : function (b, a) {
		if (!b) {
			return [this.L("blankTaskText")]
		}
		if (!a.isValid()) {
			var c = this.getDependencyErrors(a);
			if (c && c.length) {
				return c
			}
			return [this.L("invalidDependencyText")]
		}
	},
	taskRender : function (d, e, b) {
		var f = this.taskValidate(d, b),
		a;
		if (f && f.length) {
			e.tdCls = Ext.baseCSSPrefix + "form-invalid";
			e.tdAttr = 'data-errorqtip="' + f.join("<br>") + '"'
		} else {
			e.tdCls = "";
			e.tdAttr = 'data-errorqtip=""'
		}
		var c = this.dependencyStore && this.dependencyStore.getTaskStore();
		if (c) {
			a = c.getById(d) || c.getByInternalId(d);
			return (a && Ext.String.htmlEncode(a.getName())) || ""
		}
		return ""
	},
	filterTasks : function (a) {
		var c = this,
		b = a.getInternalId(),
		d;
		if (this.direction === "predecessors") {
			d = "getSourceId"
		} else {
			d = "getTargetId"
		}
		return b != this.task.getInternalId() && !this.task.contains(a) && !a.contains(this.task) && (this.allowParentTaskDependencies || a.isLeaf())
	},
	bindTaskStore : function () {
		var c = this.dependencyStore && this.dependencyStore.getTaskStore();
		if (c) {
			if (!this.taskStoreListeners) {
				var d = Ext.Function.createBuffered(this.bindTaskStore, this.refreshTimeout, this, []);
				this.taskStoreListeners = this.mon(c, {
						append : d,
						insert : d,
						update : d,
						remove : d,
						refresh : d,
						clear : d,
						"nodestore-datachange-end" : d,
						scope : this,
						destroyable : true
					})
			}
			var b = new Ext.data.JsonStore({
					model : c.model,
					sorters : c.model.prototype.nameField
				});
			var a = c.tree.getRootNode();
			b.loadData(Gnt.util.Data.cloneModelSet(c.tree.flatten(), function (f, e) {
					if (e === a || e.hidden) {
						return false
					}
					if (!e.getId()) {
						f.setId(e.getPhantomId())
					}
				}));
			this.tasksFilter = new Ext.util.Filter({
					filterFn : this.filterTasks,
					scope : this
				});
			b.filter(this.tasksFilter);
			this.tasksCombo.bindStore(b)
		}
	},
	buildTasksCombo : function () {
		var a = this;
		return new Ext.form.field.ComboBox({
			queryMode : "local",
			alowBlank : false,
			editing : false,
			forceSelection : true,
			valueField : this.taskModel.prototype.idProperty,
			displayField : this.taskModel.prototype.nameField,
			queryCaching : false,
			listConfig : {
				getInnerTpl : function () {
					return "{" + this.displayField + ":htmlEncode}"
				}
			},
			validator : function (b) {
				if (!b) {
					return a.L("blankTaskText")
				}
				return true
			}
		})
	},
	filterAllowedTypes : function (b) {
		if (!this.dependencyStore || !this.dependencyStore.allowedDependencyTypes) {
			return true
		}
		var e = this.dependencyStore.allowedDependencyTypes;
		var f = this.store.model.Type;
		for (var c = 0, a = e.length; c < a; c++) {
			var d = f[e[c]];
			if (b.getId() == d) {
				return true
			}
		}
		return false
	},
	buildTypesCombo : function () {
		var b = this.store.model.Type;
		this.typesFilter = new Ext.util.Filter({
				filterFn : this.filterAllowedTypes,
				scope : this
			});
		var a = new Ext.data.ArrayStore({
				fields : [{
						name : "id",
						type : "int"
					}, "text"],
				data : [[b.EndToStart, this.L("endToStartText")], [b.StartToStart, this.L("startToStartText")], [b.EndToEnd, this.L("endToEndText")], [b.StartToEnd, this.L("startToEndText")]]
			});
		a.filter(this.typesFilter);
		return new Ext.form.field.ComboBox({
			triggerAction : "all",
			queryMode : "local",
			editable : false,
			valueField : "id",
			displayField : "text",
			store : a
		})
	},
	buildColumns : function () {
		var d = this,
		b = this.store.model.prototype,
		f = this.store.model.Type,
		e = [],
		a = this.dependencyStore && this.dependencyStore.getTaskStore();
		this.tasksCombo = this.buildTasksCombo();
		if (!a) {
			this.deferredStoreBind = true;
			this.tasksCombo.on("render", this.bindTaskStore, this)
		} else {
			this.bindTaskStore()
		}
		var c = {};
		if (this.useSequenceNumber) {
			c = {
				text : this.L("snText"),
				dataIndex : b.fromField,
				renderer : function (j, k, g) {
					var h = d.dependencyStore && d.dependencyStore.getTaskStore(),
					i = h && h.getNodeById(g.get("From"));
					return i ? i.getSequenceNumber() : ""
				},
				width : 50
			}
		} else {
			c = {
				text : this.L("idText"),
				dataIndex : b.fromField,
				width : 50
			}
		}
		if (this.direction === "predecessors") {
			e.push(c, {
				text : this.L("taskText"),
				dataIndex : b.fromField,
				flex : 1,
				editor : this.tasksCombo,
				renderer : function (h, i, g) {
					return d.taskRender(h, i, g)
				}
			})
		} else {
			e.push(c, {
				text : this.L("taskText"),
				dataIndex : b.toField,
				flex : 1,
				editor : this.tasksCombo,
				renderer : function (h, i, g) {
					return d.taskRender(h, i, g)
				}
			})
		}
		this.lagEditor = new Gnt.field.Duration({
				minValue : Number.NEGATIVE_INFINITY
			});
		this.typesCombo = this.buildTypesCombo();
		e.push({
			text : this.L("typeText"),
			dataIndex : b.typeField,
			width : 120,
			renderer : function (g) {
				return d.dependencyTypeRender(g)
			},
			editor : this.typesCombo
		}, {
			text : this.L("lagText"),
			dataIndex : b.lagField,
			width : 100,
			editor : this.lagEditor,
			renderer : function (h, i, g) {
				return d.lagEditor.valueToVisible(h, g.get(b.lagUnitField), 2)
			}
		}, {
			text : this.L("clsText"),
			dataIndex : b.clsField,
			hidden : !this.showCls,
			width : 100
		});
		return e
	},
	insertDependency : function (b, h) {
		if (!this.dependencyStore) {
			return
		}
		var e = this.task.getInternalId(),
		d = this.store.model.prototype,
		c = {},
		g = this;
		if (b) {
			c = b
		} else {
			c[d.typeField] = this.typesCombo.store.getAt(0).getId();
			c[d.lagField] = 0;
			c[d.lagUnitField] = "d"
		}
		if (this.direction === "predecessors") {
			c[d.toField] = e
		} else {
			c[d.fromField] = e
		}
		var f = this.store.insert(0, c);
		if (f.length) {
			var a = f[0].isValid;
			f[0].isValid = function () {
				return a.call(this, false) && g.isValidDependency(this)
			}
		}
		if (!h) {
			this.cellEditing.startEditByPosition({
				row : 0,
				column : 1
			})
		}
		return f
	},
	onOppositeStoreChange : function () {
		this.getView().refresh()
	},
	setOppositeStore : function (a) {
		var b = {
			update : this.onOppositeStoreChange,
			datachanged : this.onOppositeStoreChange,
			scope : this
		};
		if (this.oppositeStore) {
			this.mun(this.oppositeStore, b)
		}
		this.oppositeStore = a;
		this.mon(this.oppositeStore, b)
	},
	loadDependencies : function (b) {
		var c = this;
		b = b || this.task;
		if (!b) {
			return
		}
		if (this.task !== b) {
			this.setTask(b)
		}
		var d;
		if (this.direction === "predecessors") {
			d = b.getIncomingDependencies(true);
			if (!this.oppositeStore) {
				this.oppositeData = b.getOutgoingDependencies(true)
			}
		} else {
			d = b.getOutgoingDependencies(true);
			if (!this.oppositeStore) {
				this.oppositeData = b.getIncomingDependencies(true)
			}
		}
		var a = Gnt.util.Data.cloneModelSet(d, function (f) {
				var e = f.isValid;
				f.isValid = function () {
					return e.call(this, false) && c.isValidDependency(this)
				}
			});
		this.store.loadData(a);
		this.fireEvent("loaddependencies", this, this.store, a, b)
	},
	getDependencyErrors : function (c) {
		var h = this,
		j = h.dependencyStore,
		d = h.task.getInternalId(),
		b = d,
		i = [];
		if (h.direction === "predecessors") {
			d = c.getSourceId()
		} else {
			b = c.getTargetId()
		}
		var f = j.getSourceTask(d);
		var e = j.getTargetTask(b);
		h.store.each(function (n) {
			var m = n.getSourceId(),
			l = n.getTargetId();
			if ((d == m) && (b == l) && (n !== c)) {
				i.push(h.L("duplicatingDependencyText"));
				return false
			}
		});
		if (i.length) {
			return i
		}
		var k = h.store.getRange();
		k.splice(Ext.Array.indexOf(k, c), 1);
		var a = h.task[h.direction];
		var g = j.getDependencyError(c, k, a);
		if (g) {
			switch (g) {
			case -3:
			case -8:
			case -5:
			case -6:
				return [h.L("transitiveDependencyText")];
			case -4:
			case -7:
				return [h.L("cyclicDependencyText")];
			case -9:
				return [h.L("parentChildDependencyText")]
			}
			return [this.L("invalidDependencyText")]
		}
		return i
	},
	isValidDependency : function (a) {
		var b = this.getDependencyErrors(a);
		return !b || !b.length
	},
	isValid : function () {
		var a = true;
		this.store.each(function (b) {
			if (!b.isValid()) {
				a = false;
				return false
			}
		});
		return a
	},
	saveDependencies : function () {
		if (!this.dependencyStore || !this.isValid()) {
			return
		}
		Gnt.util.Data.applyCloneChanges(this.store, this.dependencyStore)
	}
});
Ext.define("Gnt.widget.taskeditor.TaskForm", {
	extend : "Ext.form.Panel",
	alias : "widget.taskform",
	requires : ["Gnt.model.Task", "Ext.form.FieldContainer", "Ext.form.field.Text", "Ext.form.field.Date", "Gnt.field.Percent", "Gnt.field.StartDate", "Gnt.field.EndDate", "Gnt.field.Duration", "Gnt.field.SchedulingMode", "Gnt.field.Effort"],
	mixins : ["Gnt.mixin.Localizable"],
	alternateClassName : ["Gnt.widget.TaskForm"],
	task : null,
	taskBuffer : null,
	taskStore : null,
	highlightTaskUpdates : true,
	showBaseline : true,
	editBaseline : false,
	showCalendar : false,
	showSchedulingMode : false,
	taskNameConfig : null,
	durationConfig : null,
	startConfig : null,
	finishConfig : null,
	percentDoneConfig : null,
	baselineStartConfig : null,
	baselineFinishConfig : null,
	baselinePercentDoneConfig : null,
	effortConfig : null,
	calendarConfig : null,
	schedulingModeConfig : null,
	constructor : function (b) {
		b = b || {};
		this.showBaseline = b.showBaseline;
		this.editBaseline = b.editBaseline;
		var a = Gnt.model.Task.prototype;
		this.fieldNames = {
			baselineEndDateField : a.baselineEndDateField,
			baselinePercentDoneField : a.baselinePercentDoneField,
			baselineStartDateField : a.baselineStartDateField,
			calendarIdField : a.calendarIdField,
			clsField : a.clsField,
			draggableField : a.draggableField,
			durationField : a.durationField,
			durationUnitField : a.durationUnitField,
			effortField : a.effortField,
			effortUnitField : a.effortUnitField,
			endDateField : a.endDateField,
			manuallyScheduledField : a.manuallyScheduledField,
			nameField : a.nameField,
			percentDoneField : a.percentDoneField,
			resizableField : a.resizableField,
			schedulingModeField : a.schedulingModeField,
			startDateField : a.startDateField,
			noteField : a.noteField
		};
		Ext.apply(this, b, {
			border : false,
			layout : "anchor",
			defaultType : "textfield"
		});
		if (this.task) {
			this.fieldNames = this.getFieldNames(this.task)
		}
		if (!this.items) {
			this.buildFields()
		}
		this.callParent(arguments);
		if (this.task) {
			this.loadRecord(this.task, this.taskBuffer)
		}
	},
	getFieldNames : function (b) {
		if (!b) {
			return
		}
		var a = {};
		for (var c in this.fieldNames) {
			a[c] = b[c]
		}
		return a
	},
	renameFields : function (b) {
		var a = this.getFieldNames(b);
		if (!a) {
			return
		}
		var d = this.getForm(),
		f = false,
		e;
		for (var c in this.fieldNames) {
			e = d.findField(this.fieldNames[c]);
			if (e && a[c] && a[c] != e.name) {
				f = true;
				e.name = a[c]
			}
		}
		if (f) {
			this.fieldNames = a
		}
	},
	buildFields : function () {
		var d = this,
		e = this.fieldNames,
		c = this.task,
		b = this.taskStore,
		i = '<table class="gnt-fieldcontainer-label-wrap"><td width="1" class="gnt-fieldcontainer-label">',
		h = '<td><div class="gnt-fieldcontainer-separator"></div></table>';
		var g = function (f) {
			return c ? c.get(e[f]) : ""
		};
		var a = function (k, f) {
			var j = {
				taskStore : d.taskStore,
				task : d.task,
				highlightTaskUpdates : d.highlightTaskUpdates
			};
			if (!k.readOnly && d.task) {
				j.readOnly = !d.task.isEditable(k.name)
			}
			return Ext.apply(k, j, f)
		};
		this.items = this.items || [];
		this.items.push.call(this.items, {
			xtype : "fieldcontainer",
			layout : "hbox",
			defaults : {
				allowBlank : false
			},
			items : [a({
					xtype : "textfield",
					fieldLabel : this.L("taskNameText"),
					name : e.nameField,
					labelWidth : 110,
					flex : 1,
					value : g(e.nameField)
				}, this.taskNameConfig), a({
					xtype : "durationfield",
					fieldLabel : this.L("durationText"),
					name : e.durationField,
					margins : "0 0 0 6",
					labelWidth : 90,
					width : 170,
					value : g(e.durationField)
				}, this.durationConfig)]
		}, a({
				xtype : "percentfield",
				fieldLabel : this.L("percentDoneText"),
				name : e.percentDoneField,
				labelWidth : 110,
				width : 200,
				allowBlank : false,
				value : g(e.percentDoneField)
			}, this.percentDoneConfig), {
			xtype : "fieldcontainer",
			fieldLabel : this.L("datesText"),
			labelAlign : "top",
			labelSeparator : "",
			beforeLabelTextTpl : i,
			afterLabelTextTpl : h,
			layout : "hbox",
			defaults : {
				labelWidth : 110,
				flex : 1,
				allowBlank : false
			},
			items : [a({
					xtype : "startdatefield",
					fieldLabel : this.L("startText"),
					name : e.startDateField,
					value : g(e.startDateField)
				}, this.startConfig), a({
					xtype : "enddatefield",
					fieldLabel : this.L("finishText"),
					name : e.endDateField,
					margins : "0 0 0 6",
					value : g(e.endDateField)
				}, this.finishConfig)]
		}, a({
				xtype : "effortfield",
				fieldLabel : this.L("effortText"),
				name : e.effortField,
				invalidText : this.L("invalidEffortText"),
				labelWidth : 110,
				width : 200,
				margins : "0 0 0 6",
				allowBlank : true,
				value : g(e.effortField)
			}, this.effortConfig));
		if (this.showBaseline) {
			this.items.push.call(this.items, {
				xtype : "fieldcontainer",
				fieldLabel : this.L("baselineText"),
				labelAlign : "top",
				labelSeparator : "",
				beforeLabelTextTpl : i,
				afterLabelTextTpl : h,
				layout : "hbox",
				defaultType : "datefield",
				defaults : {
					labelWidth : 110,
					flex : 1,
					cls : "gnt-baselinefield"
				},
				items : [a({
						fieldLabel : this.L("baselineStartText"),
						name : e.baselineStartDateField,
						value : g(e.baselineStartDateField),
						readOnly : !this.editBaseline
					}, this.baselineStartConfig), a({
						fieldLabel : this.L("baselineFinishText"),
						name : e.baselineEndDateField,
						margins : "0 0 0 6",
						value : g(e.baselineEndDateField),
						readOnly : !this.editBaseline
					}, this.baselineFinishConfig)]
			}, a({
					xtype : "percentfield",
					fieldLabel : this.L("baselinePercentDoneText"),
					name : e.baselinePercentDoneField,
					labelWidth : 110,
					width : 200,
					cls : "gnt-baselinefield",
					value : g(e.baselinePercentDoneField),
					readOnly : !this.editBaseline
				}, this.baselinePercentDoneConfig))
		}
		if (this.showCalendar) {
			this.items.push(a({
					xtype : "calendarfield",
					fieldLabel : this.L("calendarText"),
					name : e.calendarIdField,
					value : g(e.calendarIdField)
				}, this.calendarConfig))
		}
		if (this.showSchedulingMode) {
			this.items.push(a({
					xtype : "schedulingmodefield",
					fieldLabel : this.L("schedulingModeText"),
					name : e.schedulingModeField,
					value : g(e.schedulingModeField),
					allowBlank : false
				}, this.schedulingModeConfig))
		}
	},
	setSuppressTaskUpdate : function (b) {
		var a = this.getForm().getFields();
		a.each(function (c) {
			c.setSuppressTaskUpdate && c.setSuppressTaskUpdate(b)
		})
	},
	loadRecord : function (b, a) {
		if (b && b !== this.task) {
			this.renameFields(b)
		}
		this.task = b;
		this.taskBuffer = a;
		if (!this.taskBuffer) {
			this.taskBuffer = b.copy();
			this.taskBuffer.taskStore = b.taskStore
		}
		var d = this,
		c = d.getForm();
		c._record = b;
		this.suspendLayouts();
		Ext.iterate(b.getData(), function (e, g) {
			var f = c.findField(e);
			if (f) {
				if (f.setTask) {
					f.setSuppressTaskUpdate(true);
					f.setTask(d.taskBuffer);
					f.setSuppressTaskUpdate(false)
				} else {
					f.setValue(g);
					if (!f.disabled) {
						if (f.editable === false) {
							if (!d.taskBuffer.isEditable(f.name)) {
								f.setReadOnly(true)
							} else {
								if (f.inputEl) {
									f.setReadOnly(false);
									f.inputEl.dom.readOnly = true
								}
							}
						} else {
							f.setReadOnly(!d.taskBuffer.isEditable(f.name))
						}
					}
				}
				if (c.trackResetOnLoad) {
					f.resetOriginalValue()
				}
			}
		});
		this.resumeLayouts(true);
		this.fireEvent("afterloadrecord", this, b)
	},
	updateRecord : function (b) {
		b = b || this.task;
		var a = Ext.Function.bind(function () {
				this.setSuppressTaskUpdate(true);
				var c = this.getForm().getFields();
				b.beginEdit();
				c.each(function (e) {
					if (e.applyChanges) {
						e.applyChanges(b)
					} else {
						var d = b.fields.getByKey(e.name);
						if (d && d.persist) {
							b.set(e.name, e.getValue())
						}
					}
				});
				b.endEdit();
				this.setSuppressTaskUpdate(false);
				this.fireEvent("afterupdaterecord", this, b)
			}, this);
		if (b && this.fireEvent("beforeupdaterecord", this, b, a) !== false) {
			a();
			return true
		}
		return false
	}
});
Ext.define("Gnt.widget.taskeditor.TaskEditor", {
	extend : "Ext.tab.Panel",
	alias : "widget.taskeditor",
	requires : ["Ext.form.Panel", "Gnt.widget.taskeditor.TaskForm", "Gnt.widget.AssignmentEditGrid", "Gnt.widget.DependencyGrid", "Gnt.field.Calendar", "Gnt.field.SchedulingMode", "Ext.form.field.HtmlEditor", "Gnt.util.Data"],
	mixins : ["Gnt.mixin.Localizable"],
	margin : "5 0 0 0",
	alternateClassName : ["Gnt.widget.TaskEditor"],
	border : false,
	plain : true,
	defaults : {
		margin : 5,
		border : false
	},
	task : null,
	taskStore : null,
	assignmentStore : null,
	resourceStore : null,
	taskFormClass : "Gnt.widget.taskeditor.TaskForm",
	advancedFormClass : "Gnt.widget.taskeditor.TaskForm",
	showAssignmentGrid : true,
	showDependencyGrid : true,
	allowParentTaskDependencies : false,
	showNotes : true,
	showStyle : true,
	showAdvancedForm : true,
	showBaseline : true,
	tabsConfig : null,
	taskFormConfig : null,
	dependencyGridClass : "Gnt.widget.DependencyGrid",
	dependencyGridConfig : null,
	assignmentGridClass : "Gnt.widget.AssignmentEditGrid",
	assignmentGridConfig : null,
	styleFormConfig : null,
	advancedFormConfig : null,
	notesConfig : null,
	height : 340,
	width : 600,
	layout : "fit",
	tabs : null,
	taskForm : null,
	assignmentGrid : null,
	dependencyGrid : null,
	advancedForm : null,
	stylingText : "Styling",
	clsText : "CSS Class",
	backgroundText : "Background",
	doneBackgroundText : "Progress Background",
	clonedStores : null,
	constructor : function (b) {
		var f = this,
		e = Gnt.model.Task.prototype;
		b = b || {};
		Ext.apply(this, b);
		this.taskFormConfig = this.taskFormConfig || {};
		Ext.applyIf(this.taskFormConfig, {
			showBaseline : this.showBaseline
		});
		var d = this.clonedStores = (this.task || this.taskStore) ? this.cloneStores() : {};
		var a = [];
		this.taskForm = Ext.create(this.taskFormClass || "Gnt.widget.taskeditor.TaskForm", Ext.applyIf(this.taskFormConfig, {
					task : this.task,
					taskStore : this.taskStore
				}));
		a.push(this.taskForm);
		if (this.showDependencyGrid) {
			this.dependencyGrid = Ext.create(this.dependencyGridClass, Ext.apply({
						allowParentTaskDependencies : this.allowParentTaskDependencies,
						taskModel : this.taskStore.model,
						task : this.task,
						margin : 5,
						tbar : {
							layout : "auto",
							items : [{
									xtype : "button",
									iconCls : "gnt-action-add",
									text : this.L("addDependencyText"),
									handler : function () {
										f.dependencyGrid.insertDependency()
									}
								}, {
									xtype : "button",
									iconCls : "gnt-action-remove",
									text : this.L("dropDependencyText"),
									itemId : "drop-dependency-btn",
									disabled : true,
									handler : function () {
										var h = f.dependencyGrid.getSelectionModel().getSelection();
										if (h && h.length) {
											f.dependencyGrid.store.remove(h)
										}
									}
								}
							]
						},
						listeners : {
							selectionchange : function (j, i) {
								var h = f.dependencyGrid;
								if (!h.dropDepBtn) {
									h.dropDepBtn = h.down("#drop-dependency-btn")
								}
								h.dropDepBtn && h.dropDepBtn.setDisabled(!i.length)
							}
						}
					}, this.dependencyGridConfig));
			a.push(this.dependencyGrid)
		}
		if (this.showAssignmentGrid && this.assignmentStore && this.resourceStore) {
			if (!d.assignmentStore) {
				d.assignmentStore = this.cloneAssignmentStore(this.task)
			}
			if (!d.resourceStore) {
				d.resourceStore = this.cloneResourceStore(this.task)
			}
			this.assignmentGrid = Ext.create(this.assignmentGridClass, Ext.apply({
						assignmentStore : this.assignmentStore,
						resourceStore : this.resourceStore,
						store : d.assignmentStore,
						resourceDupStore : d.resourceStore,
						tbar : {
							layout : "auto",
							items : [{
									xtype : "button",
									iconCls : "gnt-action-add",
									text : this.L("addAssignmentText"),
									handler : function () {
										f.assignmentGrid.insertAssignment()
									}
								}, {
									xtype : "button",
									iconCls : "gnt-action-remove",
									text : this.L("dropAssignmentText"),
									itemId : "drop-assignment-btn",
									disabled : true,
									handler : function () {
										var h = f.assignmentGrid.getSelectionModel().getSelection();
										if (h && h.length) {
											f.assignmentGrid.store.remove(h)
										}
									}
								}
							]
						},
						listeners : {
							afterrender : {
								fn : function (h) {
									h.loadTaskAssignments(f.task.get(f.task.idProperty))
								},
								single : true
							},
							selectionchange : function (j, i) {
								var h = f.assignmentGrid;
								if (!h.dropBtn) {
									h.dropBtn = h.down("#drop-assignment-btn")
								}
								h.dropBtn && h.dropBtn.setDisabled(!i.length)
							}
						}
					}, this.assignmentGridConfig));
			a.push(this.assignmentGrid)
		}
		if (this.showAdvancedForm) {
			var g = Ext.ClassManager.get(this.advancedFormClass || "Gnt.widget.taskeditor.TaskForm").prototype;
			this.advancedFormConfig = this.advancedFormConfig || {};
			this.advancedForm = Ext.create(this.advancedFormClass || "Gnt.widget.taskeditor.TaskForm", Ext.applyIf(this.advancedFormConfig, {
						items : [{
								xtype : "calendarfield",
								fieldLabel : g.L("calendarText"),
								name : this.task ? this.task.calendarIdField : e.calendarIdField,
								value : this.task && this.task.getCalendarId(true),
								taskStore : this.taskStore,
								task : this.task
							}, {
								xtype : "schedulingmodefield",
								fieldLabel : g.L("schedulingModeText"),
								name : this.task ? this.task.schedulingModeField : e.schedulingModeField,
								value : this.task && this.task.getSchedulingMode(),
								allowBlank : false,
								taskStore : this.taskStore,
								task : this.task
							}, {
								xtype : "displayfield",
								fieldLabel : this.L("wbsCodeText"),
								name : "wbsCode",
								value : this.task && this.task.getWBSCode()
							}
						],
						task : this.task,
						taskStore : this.taskStore
					}));
			a.push(this.advancedForm)
		}
		if (this.showNotes) {
			this.notesEditor = Ext.create("Ext.form.field.HtmlEditor", Ext.apply({
						listeners : {
							afterrender : function (h) {
								f.notesEditor.setValue(f.task.get(f.task.noteField))
							}
						}
					}, this.notesConfig));
			this.notesPanel = Ext.create("Ext.panel.Panel", {
					border : false,
					layout : "fit",
					items : this.notesEditor
				});
			a.push(this.notesPanel)
		}
		if (!this.taskForm.title) {
			this.taskForm.title = this.L("generalText")
		}
		if (this.dependencyGrid && !this.dependencyGrid.title) {
			this.dependencyGrid.title = this.L("dependencyText")
		}
		if (this.assignmentGrid && !this.assignmentGrid.title) {
			this.assignmentGrid.title = this.L("resourcesText")
		}
		if (this.advancedForm && !this.advancedForm.title) {
			this.advancedForm.title = this.L("advancedText")
		}
		if (this.notesPanel && !this.notesPanel.title) {
			this.notesPanel.title = this.L("notesText")
		}
		if (this.styleForm && !this.styleForm.title) {
			this.styleForm.title = this.stylingText
		}
		var c = this.items || (this.tabsConfig && this.tabsConfig.items);
		if (c) {
			a.push.apply(a, Ext.isArray(c) ? c : [c]);
			delete b.items;
			if (this.tabsConfig && this.tabsConfig.items) {
				delete this.tabsConfig.items
			}
		}
		this.items = a;
		Ext.apply(this, this.tabsConfig);
		if (a.length <= 1) {
			b.tabBar = b.tabBar || {};
			Ext.applyIf(b.tabBar, {
				hidden : true
			})
		}
		this.tabs = this;
		this.callParent(arguments)
	},
	getTaskStore : function () {
		return this.taskStore || this.task && this.task.getTaskStore()
	},
	cloneTaskStore : function (b, c) {
		var a = this.getTaskStore();
		if (!a) {
			return null
		}
		return new(Ext.getClass(a))(Ext.apply({
				isCloned : true,
				calendar : a.getCalendar(),
				model : a.model,
				weekendsAreWorkdays : a.weekendsAreWorkdays,
				cascadeChanges : a.cascadeChanges,
				batchSync : false,
				recalculateParents : false,
				skipWeekendsDuringDragDrop : a.skipWeekendsDuringDragDrop,
				moveParentAsGroup : a.moveParentAsGroup,
				enableDependenciesForParentTasks : a.enableDependenciesForParentTasks,
				availabilitySearchLimit : a.availabilitySearchLimit,
				dependenciesCalendar : "project",
				proxy : {
					type : "memory",
					reader : {
						type : "json"
					}
				}
			}, c))
	},
	cloneDependencyStore : function (b, d) {
		var c = this.getTaskStore();
		var a = this.dependencyStore || c && c.getDependencyStore();
		if (!a) {
			return null
		}
		return new(Ext.getClass(a))(Ext.apply({
				isCloned : true,
				model : a.model,
				strictDependencyValidation : a.strictDependencyValidation,
				allowedDependencyTypes : a.allowedDependencyTypes,
				proxy : {
					type : "memory",
					reader : {
						type : "json"
					}
				}
			}, d))
	},
	cloneAssignmentStore : function (b, d) {
		var c = this.getTaskStore();
		var a = this.assignmentStore || c && c.getAssignmentStore();
		if (!a) {
			return null
		}
		return new(Ext.getClass(a))(Ext.apply({
				isCloned : true,
				model : a.model,
				proxy : {
					type : "memory",
					reader : {
						type : "json"
					}
				}
			}, d))
	},
	cloneResourceStore : function (b, d) {
		var c = this.getTaskStore();
		var a = this.resourceStore || c && c.getResourceStore();
		if (!a) {
			return null
		}
		return new(Ext.getClass(a))(Ext.apply({
				isCloned : true,
				model : a.model,
				proxy : {
					type : "memory",
					reader : {
						type : "json"
					}
				}
			}, d))
	},
	cloneStores : function (d) {
		var a = this.task,
		e = this.cloneResourceStore(a, d && d.resourceStore),
		f = this.cloneAssignmentStore(a, d && d.assignmentStore),
		c = this.cloneDependencyStore(a, d && d.dependencyStore);
		var b = this.cloneTaskStore(a, Ext.apply({
					assignmentStore : f,
					resourceStore : e,
					dependencyStore : c
				}, d && d.taskStore));
		return {
			resourceStore : e,
			assignmentStore : f,
			dependencyStore : c,
			taskStore : b
		}
	},
	cloneTask : function (a) {
		return a.copy(a.getInternalId(), false)
	},
	bindDependencyGrid : function () {
		var a = this.clonedStores.dependencyStore;
		var b = this.dependencyGrid;
		b.store.taskStore = this.clonedStores.taskStore;
		if (a) {
			this.mon(b, {
				loaddependencies : function (d, c) {
					a.loadData(c.getRange().concat(Gnt.util.Data.cloneModelSet(d.oppositeData)))
				}
			});
			this.mon(b.store, {
				add : function (d, c) {
					a.add(c)
				},
				remove : function (d, c) {
					a.remove(c)
				}
			});
			this.dependencyGridBound = true
		}
	},
	loadTask : function (d) {
		if (!d) {
			return
		}
		var h = this;
		this.task = d;
		var c = this.taskForm;
		c.setSuppressTaskUpdate(true);
		c.getForm().reset();
		var l = this.clonedStores;
		var i = this.getTaskStore();
		var g = this.cloneTask(d);
		var e = [g];
		Ext.Array.each(d.predecessors, function (n) {
			var m = h.cloneTask(n.getSourceTask());
			m.taskStore = l.taskStore;
			e.push(m)
		});
		Ext.Array.each(d.successors, function (n) {
			var m = h.cloneTask(n.getTargetTask());
			m.taskStore = l.taskStore;
			e.push(m)
		});
		if (!l.dependencyStore) {
			l.dependencyStore = this.cloneDependencyStore(d)
		}
		if (!l.assignmentStore) {
			l.assignmentStore = this.cloneAssignmentStore(d)
		}
		if (!l.resourceStore) {
			l.resourceStore = this.cloneResourceStore(d)
		}
		if (!l.taskStore) {
			l.taskStore = this.cloneTaskStore(d, {
					assignmentStore : l.assignmentStore,
					resourceStore : l.resourceStore,
					dependencyStore : l.dependencyStore
				})
		}
		g.taskStore = l.taskStore;
		var a = function (n, m) {
			n.setId(m.getId())
		};
		var f = this.assignmentGrid;
		if (f) {
			if (l.assignmentStore !== f.getStore()) {
				f.reconfigure(l.assignmentStore)
			}
			if (f.resourceDupStore !== l.resourceStore) {
				f.resourceDupStore = l.resourceStore
			}
			f.loadResources();
			f.loadTaskAssignments(d.getId() || d.getPhantomId())
		} else {
			if (l.resourceStore) {
				l.resourceStore.loadData(Gnt.util.Data.cloneModelSet(this.resourceStore || this.getTaskStore().getResourceStore(), a))
			}
			if (l.assignmentStore) {
				l.assignmentStore.loadData(Gnt.util.Data.cloneModelSet(d.assignments, a))
			}
		}
		var k = this.dependencyGrid;
		if (k) {
			if (!this.dependencyGridBound) {
				this.bindDependencyGrid()
			}
			k.loadDependencies(d);
			if (this.allowParentTaskDependencies || d.isLeaf()) {
				k.tab.show()
			} else {
				k.tab.hide()
			}
		} else {
			if (l.dependencyStore) {
				l.dependencyStore.loadData(Gnt.util.Data.cloneModelSet(d.getAllDependencies(), a))
			}
		}
		l.taskStore.setRootNode({
			expanded : true,
			children : e
		});
		c.loadRecord(d, g);
		if (this.advancedForm) {
			this.advancedForm.setSuppressTaskUpdate(true);
			var b = this.advancedForm.getForm();
			b.reset();
			this.advancedForm.loadRecord(d, c.taskBuffer);
			var j = b.findField("wbsCode");
			if (j) {
				j.setValue(d.getWBSCode())
			}
			this.advancedForm.setSuppressTaskUpdate(false)
		}
		c.setSuppressTaskUpdate(false);
		if (this.styleForm) {
			this.styleForm.loadRecord(d)
		}
		if (this.notesEditor) {
			this.notesEditor.setValue(d.get(d.noteField))
		}
		this.fireEvent("loadtask", this, d)
	},
	getTabByComponent : function (b) {
		var a;
		this.items.each(function (c) {
			if (b === c || b.isDescendantOf(c)) {
				a = c;
				return false
			}
		}, this);
		return a
	},
	validate : function () {
		var b = this.getActiveTab(),
		a = true,
		c;
		if (b) {
			if (!this.taskForm.isValid()) {
				if (this.taskForm === b || this.taskForm.isDescendantOf(b)) {
					return false
				}
				a = false;
				c = this.getTabByComponent(this.taskForm)
			}
			if (this.dependencyGrid && !this.dependencyGrid.isValid()) {
				if (this.dependencyGrid === b || this.dependencyGrid.isDescendantOf(b)) {
					return false
				}
				a = false;
				c = c || this.getTabByComponent(this.dependencyGrid)
			}
			if (this.assignmentGrid && !this.assignmentGrid.isValid()) {
				if (this.assignmentGrid === b || this.assignmentGrid.isDescendantOf(b)) {
					return false
				}
				a = false;
				c = c || this.getTabByComponent(this.assignmentGrid)
			}
			if (this.advancedForm && !this.advancedForm.isValid()) {
				if (this.advancedForm === b || this.advancedForm.isDescendantOf(b)) {
					return false
				}
				a = false;
				c = c || this.getTabByComponent(this.advancedForm)
			}
		}
		if (c) {
			this.setActiveTab(c)
		}
		return (this.fireEvent("validate", this, c) !== false) && a
	},
	updateTask : function () {
		var a = Ext.Function.bind(function () {
				this.taskForm.updateRecord();
				if (this.advancedForm) {
					this.advancedForm.updateRecord()
				}
				if (this.notesEditor) {
					this.task.set(this.task.noteField, this.notesEditor.getValue())
				}
				if (this.styleForm) {
					this.styleForm.getForm().updateRecord()
				}
				if (this.assignmentGrid) {
					this.assignmentGrid.saveTaskAssignments()
				}
				if (this.dependencyGrid) {
					this.dependencyGrid.saveDependencies()
				}
				this.fireEvent("afterupdatetask", this)
			}, this);
		if (this.fireEvent("beforeupdatetask", this, a) !== false) {
			a();
			return true
		}
		return false
	},
	onDestroy : function () {
		if (this.clonedStores.taskStore) {
			this.clonedStores.taskStore.destroy()
		}
		this.callParent(arguments)
	}
});
Ext.define("Gnt.plugin.TaskEditor", {
	extend : "Ext.window.Window",
	requires : ["Ext.window.MessageBox", "Gnt.widget.taskeditor.TaskEditor"],
	alias : "plugin.gantt_taskeditor",
	mixins : ["Ext.AbstractPlugin", "Gnt.mixin.Localizable"],
	lockableScope : "top",
	taskEditor : null,
	panelConfig : null,
	height : 340,
	width : 600,
	layout : "fit",
	triggerEvent : "taskdblclick",
	closeAction : "hide",
	modal : true,
	gantt : null,
	assignmentStore : null,
	resourceStore : null,
	taskStore : null,
	constructor : function (a) {
		a = a || {};
		Ext.apply(this, a);
		this.title = this.L("title");
		if (!a.buttons) {
			this.buttons = ["->", {
					text : this.L("okText"),
					handler : function () {
						this.completeEditing() || Ext.Msg.alert(this.L("alertCaption"), this.L("alertText"))
					},
					scope : this
				}, {
					text : this.L("cancelText"),
					handler : this.close,
					scope : this
				}
			]
		}
		this.callParent(arguments);
		this.addCls("gnt-taskeditor-window")
	},
	init : function (d) {
		this.assignmentStore = this.assignmentStore || d.getAssignmentStore();
		this.resourceStore = this.resourceStore || d.getResourceStore();
		this.taskStore = this.taskStore || d.getTaskStore();
		var b = {
			width : null,
			height : null,
			border : false
		},
		e = ["l10n", "task", "taskStore", "assignmentStore", "resourceStore", "generalText", "resourcesText", "dependencyText", "addDependencyText", "dropDependencyText", "notesText", "advancedText", "wbsCodeText", "addAssignmentText", "dropAssignmentText", "showAssignmentGrid", "showDependencyGrid", "allowParentTaskDependencies", "showNotes", "showStyle", "showAdvancedForm", "taskFormClass", "advancedFormClass", "tabsConfig", "taskFormConfig", "dependencyGridConfig", "assignmentGridConfig", "advancedFormConfig", "styleFormConfig", "dependencyGridClass", "assignmentGridClass"];
		for (var c = 0, a = e.length; c < a; c++) {
			if (this[e[c]] !== undefined) {
				b[e[c]] = this[e[c]]
			}
		}
		b.showBaseline = d.enableBaseline;
		Ext.apply(b, this.panelConfig);
		this.buildTaskEditor(b);
		this.add(this.taskEditor);
		this.relayEvents(this.taskEditor, ["validate", "beforeupdatetask", "afterupdatetask", "loadtask"]);
		this.mon(d, this.triggerEvent, this.onTriggerEvent, this);
		this.gantt = d;
		d.taskEditor = this
	},
	buildTaskEditor : function (a) {
		this.taskEditor = new Gnt.widget.taskeditor.TaskEditor(a)
	},
	onTriggerEvent : function (b, a) {
		this.showTask(a)
	},
	showTask : function (a) {
		if (this.taskEditor && a) {
			this.taskEditor.loadTask(a);
			this.show()
		}
	},
	validate : function () {
		if (this.taskEditor) {
			return this.taskEditor.validate()
		}
	},
	completeEditing : function () {
		if (this.taskEditor) {
			var a = this.taskEditor.getActiveTab();
			if (a.editingPlugin && a.editingPlugin.completeEdit) {
				a.editingPlugin.completeEdit()
			}
			if (!this.taskEditor.validate()) {
				return false
			}
			if (this.taskEditor.updateTask()) {
				this.hide();
				return true
			}
			return false
		}
	},
	updateTask : function () {
		if (this.taskEditor) {
			return this.taskEditor.updateTask()
		}
	}
});
Ext.define("Gnt.column.Dependency", {
	extend : "Ext.grid.column.Column",
	requires : ["Gnt.field.Dependency"],
	separator : ";",
	type : "predecessors",
	field : null,
	useSequenceNumber : false,
	constructor : function (a) {
		a = a || {};
		var b = a.field || a.editor;
		delete a.field;
		delete a.editor;
		Ext.apply(this, a);
		a.editor = b || Ext.create("Gnt.field.Dependency", {
				type : this.type,
				separator : this.separator,
				useSequenceNumber : this.useSequenceNumber
			});
		if (!(a.editor instanceof Gnt.widget.DependencyField)) {
			a.editor = Ext.ComponentManager.create(a.editor, "dependencyfield")
		}
		a.field = a.editor;
		this.scope = this;
		this.callParent([a])
	},
	afterRender : function () {
		var a = this.up("ganttpanel");
		a.registerLockedDependencyListeners();
		this.callParent(arguments)
	},
	getContainingPanel : function () {
		if (!this.panel) {
			this.panel = this.up("tablepanel")
		}
		return this.panel
	},
	setDirtyClass : function (c, b) {
		var a = this.getContainingPanel().getView();
		if (a.markDirty && this.field.isDirty(b)) {
			c.tdCls = a.dirtyCls
		}
	},
	renderer : function (b, c, a) {
		if (!a.isEditable(this.dataIndex)) {
			c.tdCls = (c.tdCls || "") + " sch-column-readonly"
		} else {
			this.setDirtyClass(c, a)
		}
		return this.field.getDisplayValue(a)
	}
});
Ext.define("Gnt.column.Successor", {
	extend : "Gnt.column.Dependency",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.successorcolumn",
	type : "successors",
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments)
	}
});
Ext.define("Gnt.column.Predecessor", {
	extend : "Gnt.column.Dependency",
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.predecessorcolumn",
	type : "predecessors",
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		this.callParent(arguments)
	}
});
Ext.define("Gnt.column.Duration", {
	extend : "Ext.grid.column.Column",
	alias : "widget.durationcolumn",
	requires : ["Gnt.field.Duration"],
	mixins : ["Gnt.mixin.Localizable"],
	width : 80,
	align : "left",
	decimalPrecision : 2,
	useAbbreviation : false,
	instantUpdate : true,
	field : null,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		var b = a.field || a.editor;
		delete a.field;
		delete a.editor;
		Ext.apply(this, a);
		a.editor = b || Ext.create("Gnt.field.Duration", {
				useAbbreviation : this.useAbbreviation,
				decimalPrecision : this.decimalPrecision,
				instantUpdate : this.instantUpdate
			});
		if (!(a.editor instanceof Gnt.field.Duration)) {
			Ext.applyIf(a.editor, {
				instantUpdate : this.instantUpdate
			});
			a.editor = Ext.ComponentManager.create(a.editor, "durationfield")
		}
		this.field = a.editor;
		this.scope = this;
		this.hasCustomRenderer = true;
		this.callParent([a])
	},
	afterRender : function () {
		var a = this.up("treepanel");
		if (!this.dataIndex) {
			this.dataIndex = a.store.model.prototype[this.field.taskField]
		}
		this.callParent(arguments)
	},
	renderer : function (b, d, a) {
		if (!Ext.isNumber(b)) {
			return ""
		}
		if (!a.isEditable(this.dataIndex)) {
			d.tdCls = (d.tdCls || "") + " sch-column-readonly"
		}
		var c = a.getDurationUnit();
		return this.field.valueToVisible(b, c)
	}
});
Ext.define("Gnt.column.Effort", {
	extend : "Gnt.column.Duration",
	alias : "widget.effortcolumn",
	requires : ["Gnt.field.Effort"],
	width : 80,
	align : "left",
	decimalPrecision : 2,
	field : null,
	constructor : function (a) {
		a = a || {};
		this.text = a.text || this.L("text");
		var b = a.field || a.editor;
		delete a.field;
		delete a.editor;
		Ext.apply(this, a);
		a.editor = b || Ext.create("Gnt.field.Effort", {
				useAbbreviation : this.useAbbreviation,
				decimalPrecision : this.decimalPrecision,
				getDurationMethod : null,
				instantUpdate : this.instantUpdate
			});
		if (!(a.editor instanceof Gnt.field.Effort)) {
			Ext.applyIf(a.editor, {
				useAbbreviation : this.useAbbreviation,
				decimalPrecision : this.decimalPrecision,
				getDurationMethod : null,
				instantUpdate : this.instantUpdate
			});
			a.editor = Ext.ComponentManager.create(a.editor, "effortfield")
		}
		this.field = a.editor;
		this.scope = this;
		this.hasCustomRenderer = true;
		this.callParent([a])
	},
	afterRender : function () {
		var a = this.up("treepanel");
		if (!this.dataIndex) {
			this.dataIndex = a.store.model.prototype[this.field.taskField]
		}
		this.callParent(arguments)
	},
	renderer : function (c, d, a) {
		if (!Ext.isNumber(c)) {
			return ""
		}
		if (!a.isEditable(this.dataIndex)) {
			d.tdCls = (d.tdCls || "") + " sch-column-readonly"
		}
		var b = a.getEffortUnit();
		return this.field.valueToVisible(c, b)
	}
});
Ext.define("Gnt.widget.Calendar", {
	extend : "Ext.picker.Date",
	alias : "widget.ganttcalendar",
	requires : ["Gnt.data.Calendar", "Sch.util.Date"],
	mixins : ["Gnt.mixin.Localizable"],
	calendar : null,
	startDate : null,
	endDate : null,
	initComponent : function () {
		if (!this.calendar) {
			Ext.Error.raise('Required attribute "calendar" missing during initialization of `Gnt.widget.Calendar`')
		}
		if (!this.startDate) {
			Ext.Error.raise('Required attribute "startDate" missing during initialization of `Gnt.widget.Calendar`')
		}
		if (!this.endDate) {
			this.endDate = Sch.util.Date.add(this.startDate, Sch.util.Date.MONTH, 1)
		}
		this.setCalendar(this.calendar);
		this.minDate = this.value = this.startDate;
		this.callParent(arguments);
		this.injectDates()
	},
	injectDates : function () {
		var a = this;
		var b = a.disabledDates = [];
		Ext.each(a.calendar.getHolidaysRanges(a.startDate, a.endDate), function (c) {
			c.forEachDate(function (d) {
				b.push(Ext.Date.format(d, a.format))
			})
		});
		a.setDisabledDates(b)
	},
	setCalendar : function (b) {
		var a = {
			update : this.injectDates,
			remove : this.injectDates,
			add : this.injectDates,
			load : this.injectDates,
			clear : this.injectDates,
			scope : this
		};
		if (this.calendar) {
			this.mun(b, a)
		}
		this.calendar = b;
		if (b) {
			this.mon(b, a)
		}
	}
});
Ext.define("Gnt.widget.calendar.ResourceCalendarGrid", {
	extend : "Ext.grid.Panel",
	requires : ["Gnt.data.Calendar", "Sch.util.Date"],
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.resourcecalendargrid",
	resourceStore : null,
	calendarStore : null,
	initComponent : function () {
		var a = this;
		this.calendarStore = this.calendarStore || Ext.create("Ext.data.Store", {
				fields : ["Id", "Name"]
			});
		Ext.apply(a, {
			store : a.resourceStore,
			columns : [{
					header : this.L("name"),
					dataIndex : "Name",
					flex : 1
				}, {
					header : this.L("calendar"),
					dataIndex : "CalendarId",
					flex : 1,
					renderer : function (f, h, b, e, d, c) {
						if (!f) {
							var g = b.getCalendar();
							f = g ? g.calendarId : ""
						}
						var i = a.calendarStore.getById(f);
						return i ? i.get("Name") : f
					},
					editor : {
						xtype : "combobox",
						store : a.calendarStore,
						queryMode : "local",
						displayField : "Name",
						valueField : "Id",
						editable : false,
						allowBlank : false
					}
				}
			],
			border : true,
			height : 180,
			plugins : Ext.create("Ext.grid.plugin.CellEditing", {
				clicksToEdit : 2
			})
		});
		this.calendarStore.loadData(this.getCalendarData());
		this.callParent(arguments)
	},
	getCalendarData : function () {
		var a = [];
		Ext.Array.each(Gnt.data.Calendar.getAllCalendars(), function (b) {
			a.push({
				Id : b.calendarId,
				Name : b.name || b.calendarId
			})
		});
		return a
	}
});
Ext.define("Gnt.widget.calendar.AvailabilityGrid", {
	extend : "Ext.grid.Panel",
	requires : ["Ext.Button", "Ext.data.Store", "Ext.grid.plugin.CellEditing", "Ext.window.MessageBox", "Gnt.data.Calendar", "Sch.util.Date"],
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.calendaravailabilitygrid",
	calendarDay : null,
	height : 160,
	addButton : null,
	removeButton : null,
	maxIntervalsNum : 5,
	initComponent : function () {
		Ext.applyIf(this, {
			store : new Ext.data.Store({
				fields : ["startTime", "endTime"],
				data : this.calendarDay.getAvailability()
			}),
			plugins : [new Ext.grid.plugin.CellEditing({
					clicksToEdit : 2
				})],
			tbar : this.buildToolbar(),
			columns : [{
					xtype : "datecolumn",
					header : this.L("startText"),
					format : "g:i a",
					dataIndex : "startTime",
					flex : 1,
					editor : {
						xtype : "timefield",
						allowBlank : false,
						initDate : "31/12/1899"
					}
				}, {
					xtype : "datecolumn",
					header : this.L("endText"),
					format : "g:i a",
					dataIndex : "endTime",
					flex : 1,
					editor : {
						xtype : "timefield",
						allowBlank : false,
						initDate : "31/12/1899"
					}
				}
			],
			listeners : {
				selectionchange : this.onAvailabilityGridSelectionChange,
				scope : this
			}
		});
		this.callParent(arguments)
	},
	buildToolbar : function () {
		this.addButton = new Ext.Button({
				text : this.L("addText"),
				iconCls : "gnt-action-add",
				handler : this.addAvailability,
				scope : this
			});
		this.removeButton = new Ext.Button({
				text : this.L("removeText"),
				iconCls : "gnt-action-remove",
				handler : this.removeAvailability,
				scope : this,
				disabled : true
			});
		return [this.addButton, this.removeButton]
	},
	onAvailabilityGridSelectionChange : function (a) {
		this.removeButton.setDisabled(!a || a.getSelection().length === 0)
	},
	setAvailability : function (a) {
		this.store.loadData(a);
		this.addButton.setDisabled(this.store.getCount() >= this.maxIntervalsNum)
	},
	addAvailability : function () {
		var a = this.getStore(),
		b = a.count();
		if (b >= this.maxIntervalsNum) {
			return
		}
		a.add({
			startTime : new Date(0, 0, 0, 12, 0),
			endTime : new Date(0, 0, 0, 13, 0)
		});
		if (b + 1 >= this.maxIntervalsNum && this.addButton) {
			this.addButton.disable()
		}
	},
	removeAvailability : function () {
		var a = this.getStore(),
		c = a.getCount(),
		b = this.getSelectionModel().getSelection();
		if (b.length === 0) {
			return
		}
		a.remove(b[0]);
		if (c < this.maxIntervalsNum && this.addButton) {
			this.addButton.enable()
		}
	},
	isValid : function (b) {
		try {
			this.calendarDay.verifyAvailability(this.getIntervals())
		} catch (a) {
			if (!b) {
				Ext.MessageBox.show({
					title : this.L("error"),
					msg : a,
					modal : true,
					icon : Ext.MessageBox.ERROR,
					buttons : Ext.MessageBox.OK
				})
			}
			return false
		}
		return true
	},
	extractTimeFromDate : function (a) {
		return new Date(0, 0, 0, a.getHours(), a.getMinutes(), a.getSeconds())
	},
	getIntervals : function () {
		var a = [];
		var b = this;
		this.getStore().each(function (d) {
			var c = b.extractTimeFromDate(d.get("endTime"));
			if (c - new Date(0, 0, 0, 0, 0, 0) === 0) {
				c = new Date(0, 0, 1, 0, 0)
			}
			a.push({
				startTime : b.extractTimeFromDate(d.get("startTime")),
				endTime : c
			})
		});
		return a
	}
});
Ext.define("Gnt.widget.calendar.DayEditor", {
	extend : "Gnt.widget.calendar.AvailabilityGrid",
	requires : ["Ext.grid.plugin.CellEditing", "Gnt.data.Calendar", "Sch.util.Date"],
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.calendardayeditor",
	height : 160,
	initComponent : function () {
		var a = this.calendarDay.getIsWorkingDay();
		Ext.applyIf(this, {
			dockedItems : [{
					xtype : "radiogroup",
					dock : "top",
					name : "dayType",
					padding : "0 5px",
					margin : 0,
					items : [{
							boxLabel : this.L("workingTimeText"),
							name : "IsWorkingDay",
							inputValue : true,
							checked : a
						}, {
							boxLabel : this.L("nonworkingTimeText"),
							name : "IsWorkingDay",
							inputValue : false,
							checked : !a
						}
					],
					listeners : {
						change : this.onDayTypeChanged,
						scope : this
					}
				}
			]
		});
		this.on("viewready", this.applyState, this);
		this.callParent(arguments)
	},
	getDayTypeRadioGroup : function () {
		return this.down('radiogroup[name="dayType"]')
	},
	applyState : function () {
		if (!this.isWorkingDay()) {
			this.getView().disable();
			this.addButton.disable()
		}
	},
	onDayTypeChanged : function (a) {
		var b = a.getValue();
		if (Ext.isArray(b.IsWorkingDay)) {
			return
		}
		this.getView().setDisabled(!b.IsWorkingDay);
		this.addButton.setDisabled(!b.IsWorkingDay || this.getStore().getCount() >= this.maxIntervalsNum)
	},
	isWorkingDay : function () {
		return this.getDayTypeRadioGroup().getValue().IsWorkingDay
	},
	isValid : function () {
		if (this.isWorkingDay()) {
			return this.callParent()
		}
		return true
	},
	getIntervals : function () {
		if (!this.isWorkingDay()) {
			return []
		}
		return this.callParent()
	}
});
Ext.define("Gnt.widget.calendar.WeekEditor", {
	extend : "Ext.form.Panel",
	requires : ["Ext.grid.Panel", "Gnt.data.Calendar", "Sch.util.Date", "Gnt.widget.calendar.AvailabilityGrid"],
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.calendarweekeditor",
	weekName : null,
	startDate : null,
	endDate : null,
	weekAvailability : null,
	calendarWeekAvailability : null,
	defaultWeekAvailability : null,
	backupWeekAvailability : null,
	layout : "anchor",
	defaults : {
		border : false,
		anchor : "100%"
	},
	currentDayIndex : null,
	_weekDaysGrid : null,
	_availabilityGrid : null,
	initComponent : function () {
		this.backupWeekAvailability = [];
		this.items = [{
				xtype : "radiogroup",
				padding : "0 5px",
				name : "dayType",
				items : [{
						boxLabel : this.L("defaultTimeText"),
						name : "IsWorkingDay",
						inputValue : 0
					}, {
						boxLabel : this.L("workingTimeText"),
						name : "IsWorkingDay",
						inputValue : 1
					}, {
						boxLabel : this.L("nonworkingTimeText"),
						name : "IsWorkingDay",
						inputValue : 2
					}
				],
				listeners : {
					change : this.onDayTypeChanged,
					scope : this
				}
			}, {
				layout : "column",
				padding : "0 0 5px 0",
				defaults : {
					border : false
				},
				items : [{
						margin : "0 10px 0 5px",
						columnWidth : 0.5,
						items : this.getWeekDaysGrid()
					}, {
						columnWidth : 0.5,
						margin : "0 5px 0 0",
						items : this.getAvailabilityGrid()
					}
				]
			}
		];
		this.callParent(arguments)
	},
	getWeekDaysGrid : function(){
		if (this._weekDaysGrid != null) {
			return this._weekDaysGrid
		}
		var a = Ext.Date.dayNames;
		return this._weekDaysGrid = new Ext.grid.Panel({
				hideHeaders : true,
				height : 160,
				columns : [{
						header : "",
						dataIndex : "name",
						flex : 1
					}
				],
				store : new Ext.data.Store({
					fields : ["id", "name"],
					idProperty : "id",
					data : [{
							id : 1,
							name : a[1]
						}, {
							id : 2,
							name : a[2]
						}, {
							id : 3,
							name : a[3]
						}, {
							id : 4,
							name : a[4]
						}]
					})
			})
		},
	lityGrid : function () {
		if (!this._availabilityGrid) {
			this._availabilityGrid = new Gnt.widget.calendar.AvailabilityGrid({
					calendarDay : new Gnt.model.CalendarDay()
				})
		}
		return this._availabilityGrid
	},
	getDayTypeRadioGroup : function () {
		if (!this.dayTypeRadioGroup) {
			this.dayTypeRadioGroup = this.down('radiogroup[name="dayType"]')
		}
		return this.dayTypeRadioGroup
	},
	getWeekAvailability : function () {
		return this.weekAvailability
	},
	onWeekDaysListViewReady : function () {
		var b = this.getWeekDaysGrid(),
		a = b.getStore().getAt(0);
		this.currentDayIndex = a.getId();
		this.readFromData();
		b.getSelectionModel().select(a, false, true)
	},
	onWeekDaysListBeforeSelect : function () {
		if (!this.saveToData()) {
			return false
		}
	},
	applyChanges : function (e) {
		if (!this.saveToData()) {
			return false
		}
		var b = this.weekAvailability;
		var d = false;
		for (var c = 0; c < 7; c++) {
			var a = b[c];
			if (a) {
				d = true
			}
			if (!a) {
				e[c] = null
			}
			if (a && !e[c]) {
				e[c] = a
			}
			if (a && e[c]) {
				e[c].setIsWorkingDay(a.getIsWorkingDay());
				e[c].setAvailability(a.getAvailability())
			}
		}
		if (!d) {
			Ext.MessageBox.show({
				title : this.L("error"),
				msg : this.L("noOverrideError"),
				modal : true,
				icon : Ext.MessageBox.ERROR,
				buttons : Ext.MessageBox.OK
			});
			return false
		}
		return true
	},
	onWeekDaysListSelectionChange : function (a, b) {
		this.currentDayIndex = b[0].getId();
		this.readFromData()
	},
	getCurrentTypeOfWeekDay : function (a) {
		return this.weekAvailability[a] ? (this.weekAvailability[a].getIsWorkingDay() ? 1 : 2) : 0
	},
	getCurrentWeekDay : function (a) {
		return this.weekAvailability[a] || this.calendarWeekAvailability[a] || this.defaultWeekAvailability[a]
	},
	saveToData : function () {
		var c = this.currentDayIndex;
		var d = this.getDayTypeRadioGroup().getValue().IsWorkingDay;
		var a = this.weekAvailability;
		if (d === 0) {
			a[c] = null;
			return true
		}
		var b = this.getAvailabilityGrid();
		if (d == 1) {
			if (!b.isValid()) {
				return false
			}
			if (!a[c]) {
				a[c] = this.copyDefaultWeekDay(c)
			}
			a[c].setIsWorkingDay(true);
			a[c].setAvailability(b.getIntervals());
			this.backupWeekAvailability[c] = null;
			return true
		}
		if (!a[c]) {
			a[c] = this.copyDefaultWeekDay(c)
		}
		a[c].setIsWorkingDay(false);
		a[c].setAvailability([]);
		return true
	},
	copyDefaultWeekDay : function (a) {
		var b = (this.calendarWeekAvailability[a] || this.defaultWeekAvailability[a]).copy();
		b.setType("WEEKDAYOVERRIDE");
		b.setOverrideStartDate(this.startDate);
		b.setOverrideEndDate(this.endDate);
		b.setName(this.weekName);
		return b
	},
	readFromData : function (b) {
		var a = this.getCurrentWeekDay(this.currentDayIndex);
		var d = this.getCurrentTypeOfWeekDay(this.currentDayIndex);
		var c = this.getAvailabilityGrid();
		c.setAvailability(b || a.getAvailability());
		var e = this.getDayTypeRadioGroup();
		e.suspendEvents();
		e.setValue({
			IsWorkingDay : [d]
		});
		e.resumeEvents();
		c.setDisabled(d != 1)
	},
	onDayTypeChanged : function (d, b, a) {
			var g = d.getValue();
			if (g.IsWorkingDay == null || Ext.isArray(g.IsWorkingDay)) {
				return
			}
			var e = this.weekAvailability;
			var f = this.backupWeekAvailability;
			var h = this.currentDayIndex;
			var c = this.getAvailabilityGrid();
			var i;
			if (a.IsWorkingDay == 1) {
				f[h] = c.getIntervals()
			}
			switch (g.IsWorkingDay) {
			case 0:
				e[h] = null;
				break;
			case 1:
				if (!e[h]) {
					e[h] = this.copyDefaultWeekDay(h)
				}
				i = f[h];
				e[h].setIsWorkingDay(true);
				break;
			case 2:
				if (!e[h]) {
					e[h] = this.copyDefaultWeekDay(h)
				}
				e[h].setAvailability([]);
				e[h].setIsWorkingDay(false);
				break;
			default:
				throw "Unrecognized day type"
			}
			this.readFromData(i)
		}
});
Ext.define("Gnt.widget.calendar.DatePicker", {
	extend : "Ext.picker.Date",
	alias : "widget.gntdatepicker",
	workingDayCls : "gnt-datepicker-workingday",
	nonWorkingDayCls : "gnt-datepicker-nonworkingday",
	overriddenDayCls : "gnt-datepicker-overriddenday",
	overriddenWeekDayCls : "gnt-datepicker-overriddenweekday",
	weekOverridesStore : null,
	dayOverridesCalendar : null,
	update : function () {
		this.callParent(arguments);
		this.refreshCssClasses()
	},
	refreshCssClasses : function () {
		var d = this,
		b = d.cells.elements;
		this.removeCustomCls();
		for (var c = 0; c < d.numDays; c++) {
			var a = b[c].firstChild.dateValue;
			b[c].className += " " + this.getDateCls(new Date(a))
		}
	},
	getDateCls : function (e) {
		var b = "";
		if (e.getMonth() !== this.getActive().getMonth()) {
			return
		}
		var c = this.dayOverridesCalendar;
		if (c.getOwnCalendarDay(e)) {
			b += " " + this.overriddenDayCls;
			if (!c.isWorkingDay(e)) {
				b += " " + this.nonWorkingDayCls
			}
		} else {
			var f = null;
			this.weekOverridesStore.each(function (g) {
				if (Ext.Date.between(e, g.get("startDate"), g.get("endDate"))) {
					f = g;
					return false
				}
			});
			if (f) {
				b += " " + this.overriddenWeekDayCls;
				var d = e.getDay(),
				a = f.get("weekAvailability");
				if (a && a[d] && !a[d].getIsWorkingDay()) {
					b += " " + this.nonWorkingDayCls
				}
			} else {
				if (!c.isWorkingDay(e)) {
					b += " " + this.nonWorkingDayCls
				}
			}
		}
		return b || this.workingDayCls
	},
	removeCustomCls : function () {
		this.cells.removeCls([this.overriddenDayCls, this.nonWorkingDayCls, this.workingDayCls, this.overriddenWeekDayCls])
	}
});
Ext.define("Gnt.widget.calendar.Calendar", {
	extend : "Ext.form.Panel",
	requires : ["Ext.XTemplate", "Ext.data.Store", "Ext.grid.Panel", "Ext.grid.plugin.CellEditing", "Gnt.data.Calendar", "Gnt.model.CalendarDay", "Gnt.widget.calendar.DayEditor", "Gnt.widget.calendar.WeekEditor", "Gnt.widget.calendar.DatePicker"],
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.calendar",
	defaults : {
		padding : 10,
		border : false
	},
	workingDayCls : "gnt-datepicker-workingday",
	nonWorkingDayCls : "gnt-datepicker-nonworkingday",
	overriddenDayCls : "gnt-datepicker-overriddenday",
	overriddenWeekDayCls : "gnt-datepicker-overriddenweekday",
	calendar : null,
	dayGridConfig : null,
	weekGridConfig : null,
	datePickerConfig : null,
	dayGrid : null,
	weekGrid : null,
	datePicker : null,
	legendTpl : '<ul class="gnt-calendar-legend"><li class="gnt-calendar-legend-item"><div class="gnt-calendar-legend-itemstyle {workingDayCls}"></div><span class="gnt-calendar-legend-itemname">{workingDayText}</span><div style="clear: both"></div></li><li><div class="gnt-calendar-legend-itemstyle {nonWorkingDayCls}"></div><span class="gnt-calendar-legend-itemname">{weekendsText}</span><div style="clear: both"></div></li><li class="gnt-calendar-legend-override"><div class="gnt-calendar-legend-itemstyle {overriddenDayCls}">31</div><span class="gnt-calendar-legend-itemname">{overriddenDayText}</span><div style="clear: both"></div></li><li class="gnt-calendar-legend-override"><div class="gnt-calendar-legend-itemstyle {overriddenWeekDayCls}">31</div><span class="gnt-calendar-legend-itemname">{overriddenWeekText}</span><div style="clear: both"></div></li></ul>',
	dateInfoTpl : null,
	dayOverridesCalendar : null,
	weekOverridesStore : null,
	copiesIndexByOriginalId : null,
	currentDayOverrideEditor : null,
	getDayGrid : function () {
		if (!this.dayGrid) {
			var a = this.calendar.model.prototype;
			this.dayGrid = new Ext.grid.Panel(Ext.apply({
						title : this.L("dayOverridesText"),
						tbar : [{
								text : this.L("addText"),
								action : "add",
								iconCls : "gnt-action-add",
								handler : this.addDay,
								scope : this
							}, {
								text : this.L("editText"),
								action : "edit",
								iconCls : "gnt-action-edit",
								handler : this.editDay,
								scope : this
							}, {
								text : this.L("removeText"),
								action : "remove",
								iconCls : "gnt-action-remove",
								handler : this.removeDay,
								scope : this
							}
						],
						store : new Gnt.data.Calendar(),
						plugins : [new Ext.grid.plugin.CellEditing({
								clicksToEdit : 2
							})],
						columns : [{
								header : this.L("dayOverrideNameHeaderText"),
								dataIndex : a.nameField,
								flex : 1,
								editor : {
									allowBlank : false
								}
							}, {
								header : this.L("dateText"),
								dataIndex : a.dateField,
								width : 100,
								xtype : "datecolumn",
								editor : {
									xtype : "datefield"
								}
							}
						]
					}, this.dayGridConfig || {}));
			this.dayOverridesCalendar = this.dayGrid.store
		}
		return this.dayGrid
	},
	getWeekGrid : function () {
		if (!this.weekGrid) {
			this.weekGrid = new Ext.grid.Panel(Ext.apply({
						title : this.L("weekOverridesText"),
						border : true,
						plugins : [new Ext.grid.plugin.CellEditing({
								clicksToEdit : 2
							})],
						store : new Ext.data.Store({
							fields : ["name", "startDate", "endDate", "weekAvailability", "mainDay"]
						}),
						tbar : [{
								text : this.L("addText"),
								action : "add",
								iconCls : "gnt-action-add",
								handler : this.addWeek,
								scope : this
							}, {
								text : this.L("editText"),
								action : "edit",
								iconCls : "gnt-action-edit",
								handler : this.editWeek,
								scope : this
							}, {
								text : this.L("removeText"),
								action : "remove",
								iconCls : "gnt-action-remove",
								handler : this.removeWeek,
								scope : this
							}
						],
						columns : [{
								header : this.L("overrideName"),
								dataIndex : "name",
								flex : 1,
								editor : {
									allowBlank : false
								}
							}, {
								xtype : "datecolumn",
								header : this.L("startDate"),
								dataIndex : "startDate",
								width : 100,
								editor : {
									xtype : "datefield"
								}
							}, {
								xtype : "datecolumn",
								header : this.L("endDate"),
								dataIndex : "endDate",
								width : 100,
								editor : {
									xtype : "datefield"
								}
							}
						]
					}, this.weekGridConfig || {}));
			this.weekOverridesStore = this.weekGrid.store
		}
		return this.weekGrid
	},
	getDatePicker : function () {
		if (!this.datePicker) {
			this.datePicker = new Gnt.widget.calendar.DatePicker(Ext.apply({
						dayOverridesCalendar : this.getDayGrid().store,
						weekOverridesStore : this.getWeekGrid().store
					}, this.datePickerConfig))
		}
		return this.datePicker
	},
	initComponent : function () {
		this.copiesIndexByOriginalId = {};
		var d = this;
		d.setupTemplates();
		if (!(this.legendTpl instanceof Ext.Template)) {
			this.legendTpl = new Ext.XTemplate(this.legendTpl)
		}
		if (!(this.dateInfoTpl instanceof Ext.Template)) {
			this.dateInfoTpl = new Ext.XTemplate(this.dateInfoTpl)
		}
		var e = this.calendar;
		if (!e) {
			Ext.Error.raise('Required attribute "calendar" is missed during initialization of `Gnt.widget.Calendar`')
		}
		var b = this.getWeekGrid(),
		a = this.getDayGrid(),
		c = this.getDatePicker();
		a.on({
			selectionchange : this.onDayGridSelectionChange,
			validateedit : this.onDayGridValidateEdit,
			edit : this.onDayGridEdit,
			scope : this
		});
		a.store.on({
			update : this.refreshView,
			remove : this.refreshView,
			add : this.refreshView,
			scope : this
		});
		b.on({
			selectionchange : this.onWeekGridSelectionChange,
			validateedit : this.onWeekGridValidateEdit,
			edit : this.onWeekGridEdit,
			scope : this
		});
		b.store.on({
			update : this.refreshView,
			remove : this.refreshView,
			add : this.refreshView,
			scope : this
		});
		c.on({
			select : this.onDateSelect,
			scope : this
		});
		this.fillDaysStore();
		this.fillWeeksStore();
		this.mon(e, {
			load : this.onCalendarChange,
			add : this.onCalendarChange,
			remove : this.onCalendarChange,
			update : this.onCalendarChange,
			scope : this
		});
		this.dateInfoPanel = new Ext.Panel({
				cls : "gnt-calendar-dateinfo",
				columnWidth : 0.33,
				border : false,
				height : 200
			});
		this.items = [{
				xtype : "container",
				layout : "hbox",
				pack : "start",
				align : "stretch",
				items : [{
						html : Ext.String.format('{0}: "{1}"', this.L("calendarNameText"), e.name),
						border : false,
						flex : 1
					}, {
						xtype : "combobox",
						name : "cmb_parentCalendar",
						fieldLabel : this.L("parentCalendarText"),
						store : new Ext.data.Store({
							fields : ["Id", "Name"],
							data : [{
									Id : -1,
									Name : this.L("noParentText")
								}
							].concat(e.getParentableCalendars())
						}),
						queryMode : "local",
						displayField : "Name",
						valueField : "Id",
						editable : false,
						emptyText : this.L("selectParentText"),
						value : e.parent ? e.parent.calendarId : -1,
						flex : 1
					}
				]
			}, {
				layout : "column",
				defaults : {
					border : false
				},
				items : [{
						margin : "0 15px 0 0",
						columnWidth : 0.3,
						html : this.legendTpl.apply({
							workingDayText : this.L("workingDayText"),
							weekendsText : this.L("weekendsText"),
							overriddenDayText : this.L("overriddenDayText"),
							overriddenWeekText : this.L("overriddenWeekText"),
							workingDayCls : this.workingDayCls,
							nonWorkingDayCls : this.nonWorkingDayCls,
							overriddenDayCls : this.overriddenDayCls,
							overriddenWeekDayCls : this.overriddenWeekDayCls
						})
					}, {
						columnWidth : 0.37,
						margin : "0 5px 0 0",
						items : [c]
					}, this.dateInfoPanel]
			}, {
				xtype : "tabpanel",
				height : 220,
				items : [a, b]
			}
		];
		this.callParent(arguments)
	},
	onCalendarChange : function () {
		this.fillDaysStore();
		this.fillWeeksStore();
		this.refreshView()
	},
	setupTemplates : function () {
		var a = this.L("tplTexts");
		this.dateInfoTpl = this.dateInfoTpl || Ext.String.format('<tpl if="isWorkingDay == true"><div>{0} {date}:</div></tpl><tpl if="isWorkingDay == false"><div>{date} {1}</div></tpl><ul class="gnt-calendar-availabilities"><tpl for="availability"><li>{.}</li></tpl></ul><span>{5}: <tpl if="override == true">{2} "{name}" {3} "{calendarName}"</tpl><tpl if="override == false">{4} "{calendarName}"</tpl></span>', a.tplWorkingHours, a.tplIsNonWorking, a.tplOverride, a.tplInCalendar, a.tplDayInCalendar, a.tplBasedOn)
	},
	afterRender : function () {
		this.callParent(arguments);
		this.onDateSelect(this.getDatePicker(), new Date())
	},
	fillDaysStore : function () {
		var a = Gnt.util.Data.cloneModelSet(this.calendar, function (b) {
				return (b.getType() == "DAY" && b.getDate())
			});
		this.dayOverridesCalendar.loadData(a)
	},
	copyCalendarDay : function (a) {
		var b = a.copy();
		b.__COPYOF__ = a.internalId;
		this.copiesIndexByOriginalId[a.internalId] = b.internalId;
		return b
	},
	fillWeeksStore : function () {
		var a = this;
		var b = [];
		this.calendar.forEachNonStandardWeek(function (c) {
			var d = Ext.apply({}, c);
			d.weekAvailability = Ext.Array.map(d.weekAvailability, function (e) {
					return e && a.copyCalendarDay(e) || null
				});
			d.mainDay = a.copyCalendarDay(d.mainDay);
			b.push(d)
		});
		this.weekOverridesStore.loadData(b)
	},
	addDay : function () {
		var a = this.getDatePicker().getValue();
		if (this.dayOverridesCalendar.getOwnCalendarDay(a)) {
			this.alert({
				msg : this.L("overrideErrorText")
			});
			return
		}
		var b = Ext.create("Gnt.model.CalendarDay", {
				Name : this.L("newDayName"),
				Type : "DAY",
				Date : a,
				IsWorkingDay : false
			});
		this.dayOverridesCalendar.insert(0, b);
		this.getDayGrid().getSelectionModel().select([b], false, false)
	},
	editDay : function () {
		var e = this,
		c = this.getDayGrid().getSelectionModel().getSelection();
		if (c.length === 0) {
			return
		}
		var a = c[0];
		var b = this.currentDayOverrideEditor = new Gnt.widget.calendar.DayEditor({
				addText : this.L("addText"),
				removeText : this.L("removeText"),
				workingTimeText : this.L("workingTimeText"),
				nonworkingTimeText : this.L("nonworkingTimeText"),
				calendarDay : a
			});
		var d = Ext.create("Ext.window.Window", {
				title : this.L("dayOverridesText"),
				modal : true,
				width : 280,
				height : 260,
				layout : "fit",
				items : b,
				buttons : [{
						text : this.L("okText"),
						handler : function () {
							if (b.isValid()) {
								var f = b.calendarDay;
								f.setIsWorkingDay(b.isWorkingDay());
								f.setAvailability(b.getIntervals());
								e.applyCalendarDay(f, a);
								e.refreshView();
								d.close()
							}
						}
					}, {
						text : this.L("cancelText"),
						handler : function () {
							d.close()
						}
					}
				]
			});
		d.show()
	},
	removeDay : function () {
		var a = this.getDayGrid(),
		b = a.getSelectionModel().getSelection();
		if (!b.length) {
			return
		}
		a.getStore().remove(b[0]);
		this.refreshView()
	},
	refreshView : function () {
		var f = this.getDatePicker().getValue(),
		b = this.getCalendarDay(f),
		e = this.getWeekGrid(),
		a = this.getDayGrid(),
		d = this.dayOverridesCalendar.getOwnCalendarDay(f),
		h;
		var c;
		if (d) {
			a.getSelectionModel().select([d], false, true);
			c = d.getName()
		} else {
			h = this.getWeekOverrideByDate(f);
			if (h) {
				e.getSelectionModel().select([h], false, true);
				c = h.get("name")
			}
		}
		var g = {
			name : c || b.getName(),
			date : Ext.Date.format(f, "M j, Y"),
			calendarName : this.calendar.name || this.calendar.calendarId,
			availability : b.getAvailability(true),
			override : Boolean(d || h),
			isWorkingDay : b.getIsWorkingDay()
		};
		this.dateInfoPanel.update(this.dateInfoTpl.apply(g));
		this.datePicker.refreshCssClasses()
	},
	onDayGridSelectionChange : function (b) {
		if (b.getSelection().length === 0) {
			return
		}
		var a = b.getSelection()[0];
		this.getDatePicker().setValue(a.getDate());
		this.refreshView()
	},
	onDayGridEdit : function (b, a) {
		if (a.field === "Date") {
			a.grid.getStore().clearCache();
			this.getDatePicker().setValue(a.value)
		}
		this.refreshView()
	},
	onDayGridValidateEdit : function (b, a) {
		var c = this.getDayGrid().store;
		if (a.field === c.model.prototype.dateField && c.getOwnCalendarDay(a.value) && a.value !== a.originalValue) {
			this.alert({
				msg : this.L("overrideErrorText")
			});
			return false
		}
	},
	onDateSelect : function (b, a) {
		this.refreshView()
	},
	getCalendarDay : function (b) {
		var a = this.dayOverridesCalendar.getOwnCalendarDay(b);
		if (a) {
			return a
		}
		a = this.getWeekOverrideDay(b);
		if (a) {
			return a
		}
		return this.calendar.weekAvailability[b.getDay()] || this.calendar.defaultWeekAvailability[b.getDay()]
	},
	getWeekOverrideDay : function (d) {
		var e = new Date(d),
		b = this.getWeekOverrideByDate(d),
		c = e.getDay();
		if (b == null) {
			return null
		}
		var a = b.get("weekAvailability");
		if (!a) {
			return null
		}
		return a[c]
	},
	getWeekOverrideByDate : function (a) {
		var b = null;
		this.weekOverridesStore.each(function (c) {
			if (Ext.Date.between(a, c.get("startDate"), c.get("endDate"))) {
				b = c;
				return false
			}
		});
		return b
	},
	intersectsWithCurrentWeeks : function (b, d, c) {
		var a = false;
		this.weekOverridesStore.each(function (f) {
			if (f == c) {
				return
			}
			var e = f.get("startDate");
			var g = f.get("endDate");
			if (e <= b && b < g || e < d && d <= g) {
				a = true;
				return false
			}
		});
		return a
	},
	addWeek : function () {
		var c = this.weekOverridesStore;
		var a = this.getDatePicker().getValue();
		var f;
		for (var e = 7; e > 0; e--) {
			f = Sch.util.Date.add(a, Sch.util.Date.DAY, e);
			if (!this.intersectsWithCurrentWeeks(a, f)) {
				break
			}
		}
		if (!e) {
			this.alert({
				msg : Ext.String.format(this.L("overrideDateError"), Ext.Date.format(a, "Y/m/d"))
			});
			return
		}
		var d = new this.calendar.model();
		d.setType("WEEKDAYOVERRIDE");
		d.setName(this.L("newDayName"));
		d.setOverrideStartDate(a);
		d.setOverrideEndDate(f);
		d.setWeekday(-1);
		var b = c.insert(0, {
				name : this.L("newDayName"),
				startDate : a,
				endDate : f,
				weekAvailability : [],
				mainDay : d
			})[0];
		this.getWeekGrid().getSelectionModel().select([b], false, false)
	},
	editWeek : function () {
		var c = this.getWeekGrid().getSelectionModel().getSelection(),
		e = this;
		if (c.length === 0) {
			return
		}
		var b = c[0];
		var a = new Gnt.widget.calendar.WeekEditor({
				startDate : b.get("startDate"),
				endDate : b.get("endDate"),
				weekName : b.get("name"),
				weekAvailability : b.get("weekAvailability"),
				calendarWeekAvailability : this.calendar.weekAvailability,
				defaultWeekAvailability : this.calendar.defaultWeekAvailability
			});
		var d = Ext.create("Ext.window.Window", {
				title : this.L("weekOverridesText"),
				modal : true,
				width : 370,
				defaults : {
					border : false
				},
				layout : "fit",
				items : a,
				buttons : [{
						action : "ok",
						text : this.L("okText"),
						handler : function () {
							if (a.applyChanges(b.get("weekAvailability"))) {
								e.refreshView();
								d.close()
							}
						}
					}, {
						text : this.L("cancelText"),
						handler : function () {
							d.close()
						}
					}
				]
			});
		d.show()
	},
	removeWeek : function () {
		var a = this.getWeekGrid().getSelectionModel().getSelection(),
		b = this;
		if (a.length === 0) {
			return
		}
		this.weekOverridesStore.remove(a[0]);
		this.refreshView()
	},
	onWeekGridSelectionChange : function (a) {
		var b = a.getSelection();
		if (b.length === 0) {
			return
		}
		this.getDatePicker().setValue(b[0].get("startDate"))
	},
	onWeekGridEdit : function (d, b) {
		var c = b.record,
		a = c.get("startDate"),
		e = c.get("endDate");
		if (b.field == "startDate" || b.field == "endDate") {
			Ext.Array.each(c.get("weekAvailability").concat(c.get("mainDay")), function (f) {
				if (f) {
					f.setOverrideStartDate(a);
					f.setOverrideEndDate(e)
				}
			});
			this.getDatePicker().setValue(a)
		}
		if (b.field == "name") {
			Ext.Array.each(c.get("weekAvailability").concat(c.get("mainDay")), function (f) {
				if (f) {
					f.setName(c.get("name"))
				}
			})
		}
		this.refreshView()
	},
	alert : function (a) {
		a = a || {};
		Ext.MessageBox.show(Ext.applyIf(a, {
				title : this.L("error"),
				icon : Ext.MessageBox.WARNING,
				buttons : Ext.MessageBox.OK
			}))
	},
	onWeekGridValidateEdit : function (d, b) {
		var c = b.record,
		a = b.field == "startDate" ? b.value : c.get("startDate"),
		e = b.field == "endDate" ? b.value : c.get("endDate");
		if (a > e) {
			this.alert({
				msg : this.L("startAfterEndError")
			});
			return false
		}
		if (this.intersectsWithCurrentWeeks(a, e, c)) {
			this.alert({
				msg : this.L("weeksIntersectError")
			});
			return false
		}
	},
	applyCalendarDay : function (d, c) {
		c.beginEdit();
		c.setId(d.getId());
		c.setName(d.getName());
		c.setIsWorkingDay(d.getIsWorkingDay());
		c.setDate(d.getDate());
		c.setOverrideStartDate(d.getOverrideStartDate());
		c.setOverrideEndDate(d.getOverrideEndDate());
		var b = d.getAvailability(true);
		var a = c.getAvailability(true);
		if (b + "" != a + "") {
			c.setAvailability(d.getAvailability())
		}
		c.endEdit()
	},
	applySingleDay : function (b, a) {
		if (b.__COPYOF__) {
			this.applyCalendarDay(b, this.calendar.getByInternalId(b.__COPYOF__))
		} else {
			b.unjoin(b.stores[0]);
			a.push(b)
		}
	},
	applyChanges : function () {
		var e = this;
		var f = this.calendar;
		var d = this.down('combobox[name="cmb_parentCalendar"]').getValue();
		f.suspendEvents(true);
		f.suspendCacheUpdate++;
		f.setParent(d ? Gnt.data.Calendar.getCalendar(d) : null);
		f.proxy.extraParams.calendarId = f.calendarId;
		Gnt.util.Data.applyCloneChanges(this.dayOverridesCalendar, f);
		var b = [];
		var a = [];
		var c = {};
		this.weekOverridesStore.each(function (g) {
			Ext.Array.each(g.get("weekAvailability").concat(g.get("mainDay")), function (h) {
				if (h) {
					if (h.__COPYOF__) {
						c[h.__COPYOF__] = true
					}
					e.applySingleDay(h, b)
				}
			})
		});
		f.forEachNonStandardWeek(function (g) {
			Ext.Array.each(g.weekAvailability.concat(g.mainDay), function (h) {
				if (h && !c[h.internalId]) {
					a.push(h)
				}
			})
		});
		f.add(b);
		f.remove(a);
		f.suspendCacheUpdate--;
		f.resumeEvents();
		f.clearCache()
	}
});
Ext.define("Gnt.widget.calendar.CalendarWindow", {
	extend : "Ext.window.Window",
	requires : ["Gnt.widget.calendar.Calendar"],
	mixins : ["Gnt.mixin.Localizable"],
	alias : "widget.calendarwindow",
	calendarConfig : null,
	calendar : null,
	calendarWidget : null,
	initComponent : function () {
		Ext.apply(this, {
			width : 600,
			layout : "fit",
			items : this.calendarWidget = new Gnt.widget.calendar.Calendar(Ext.apply({
						calendar : this.calendar
					}, this.calendarConfig)),
			buttons : [{
					text : this.L("ok"),
					handler : function () {
						this.applyChanges();
						this.close()
					},
					scope : this
				}, {
					text : this.L("cancel"),
					handler : this.close,
					scope : this
				}
			]
		});
		this.callParent(arguments)
	},
	applyChanges : function () {
		this.calendarWidget.applyChanges()
	}
});
Ext.data.Connection.override({
	parseStatus : function (b) {
		var a = this.callOverridden(arguments);
		if (b === 0) {
			a.success = true
		}
		return a
	}
});

//去掉注册信息
if(false){
//if (!window.location.href.match("bryntum.com|ext-scheduler.com")) {
	var log = function (a) {
		if (window.console) {
			console.log(a)
		}
	};
	log("BRYNTUM TRIAL LICENSE, for purchasing and licensing options please visit: www.bryntum.com/store (btw, we're hiring: www.bryntum.com/company/careers )");
	function newRefresh() {
		this.callOverridden(arguments);
		if (this.__injected || !this.rendered) {
			return
		}
		this.__injected = true;
		Ext.Function.defer(function () {
			this.el && this.el.select(this.eventSelector).setOpacity(0.15);
			log("TRIAL VERSION: PRODUCT DEACTIVATED")
		}, 10 * 60 * 1000, this);
		var a = this.el.parent().createChild({
				tag : "a",
				cls : "bryntum-trial",
				href : "http://www.bryntum.com/store",
				title : "Click here to purchase a license",
				style : "display:block;height:54px;width:230px;background: #fff url(http://www.bryntum.com/site-images/bryntum-trial.png) no-repeat;z-index:10000;border:1px solid #ddd;-webkit-box-shadow: 2px 2px 2px rgba(100, 100, 100, 0.5);-moz-box-shadow: 2px 2px 2px rgba(100, 100, 100, 0.5);-moz-border-radius:5px;-webkit-border-radius:5px;position:absolute;bottom:10px;right:15px;"
			});
		try {
			if (!Ext.util.Cookies.get("bmeval")) {
				Ext.util.Cookies.set("bmeval", new Date().getTime(), Ext.Date.add(new Date(), Ext.Date.YEAR, 2))
			} else {
				var d = Ext.util.Cookies.get("bmeval"),
				b = new Date(parseInt(d, 10));
				if (Ext.Date.add(b, Ext.Date.DAY, 45) < new Date()) {
					this.el.select(this.eventSelector).hide();
					this.el.mask("Trial Period Expired!").setStyle("z-index", 10000);
					log("TRIAL PERIOD EXPIRED, PURCHASE A LICENSE HERE http://bryntum.com/");
					this.refresh = Ext.emptyFn
				}
			}
		} catch (c) {}

	}
	if (Sch && Sch.view && Sch.view.TimelineGridView) {
		Sch.view.TimelineGridView.override({
			refresh : Ext.Function.clone(newRefresh)
		})
	}
	if (Sch && Sch.view && Sch.view.TimelineTreeView) {
		Sch.view.TimelineTreeView.override({
			refresh : Ext.Function.clone(newRefresh)
		})
	}
}
