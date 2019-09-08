/**
 * textarea中输入Tab键
 * @type {class}
 */
tabIndent = {
	version: '0.1.8',
	config: {
		tab: '\t'
	},
	events: {
		keydown: function(e) {
			var tab = tabIndent.config.tab;
			var tabWidth = tab.length;
			if (e.keyCode === 9) {
				e.preventDefault();
				var	currentStart = this.selectionStart,
					currentEnd = this.selectionEnd;
				if (e.shiftKey === false) {
					// Normal Tab Behaviour
					if (!tabIndent.isMultiLine(this)) {
						// Add tab before selection, maintain highlighted text selection
						this.value = this.value.slice(0, currentStart) + tab + this.value.slice(currentStart);
						this.selectionStart = currentStart + tabWidth;
						this.selectionEnd = currentEnd + tabWidth;
					} else {
						// Iterating through the startIndices, if the index falls within selectionStart and selectionEnd, indent it there.
						var	startIndices = tabIndent.findStartIndices(this),
							l = startIndices.length,
							newStart = undefined,
							newEnd = undefined,
							affectedRows = 0;

						while(l--) {
							var lowerBound = startIndices[l];
							if (startIndices[l+1] && currentStart != startIndices[l+1]) lowerBound = startIndices[l+1];

							if (lowerBound >= currentStart && startIndices[l] < currentEnd) {
								this.value = this.value.slice(0, startIndices[l]) + tab + this.value.slice(startIndices[l]);

								newStart = startIndices[l];
								if (!newEnd) newEnd = (startIndices[l+1] ? startIndices[l+1] - 1 : 'end');
								affectedRows++;
							}
						}

						this.selectionStart = newStart;
						this.selectionEnd = (newEnd !== 'end' ? newEnd + (tabWidth * affectedRows) : this.value.length);
					}
				} else {
					// Shift-Tab Behaviour
					if (!tabIndent.isMultiLine(this)) {
						if (this.value.substr(currentStart - tabWidth, tabWidth) == tab) {
							// If there's a tab before the selectionStart, remove it
							this.value = this.value.substr(0, currentStart - tabWidth) + this.value.substr(currentStart);
							this.selectionStart = currentStart - tabWidth;
							this.selectionEnd = currentEnd - tabWidth;
						} else if (this.value.substr(currentStart - 1, 1) == "\n" && this.value.substr(currentStart, tabWidth) == tab) {
							// However, if the selection is at the start of the line, and the first character is a tab, remove it
							this.value = this.value.substring(0, currentStart) + this.value.substr(currentStart + tabWidth);
							this.selectionStart = currentStart;
							this.selectionEnd = currentEnd - tabWidth;
						}
					} else {
						// Iterating through the startIndices, if the index falls within selectionStart and selectionEnd, remove an indent from that row
						var	startIndices = tabIndent.findStartIndices(this),
							l = startIndices.length,
							newStart = undefined,
							newEnd = undefined,
							affectedRows = 0;

						while(l--) {
							var lowerBound = startIndices[l];
							if (startIndices[l+1] && currentStart != startIndices[l+1]) lowerBound = startIndices[l+1];

							if (lowerBound >= currentStart && startIndices[l] < currentEnd) {
								if (this.value.substr(startIndices[l], tabWidth) == tab) {
									// Remove a tab
									this.value = this.value.slice(0, startIndices[l]) + this.value.slice(startIndices[l] + tabWidth);
									affectedRows++;
								} else {}	// Do nothing

								newStart = startIndices[l];
								if (!newEnd) newEnd = (startIndices[l+1] ? startIndices[l+1] - 1 : 'end');
							}
						}

						this.selectionStart = newStart;
						this.selectionEnd = (newEnd !== 'end' ? newEnd - (affectedRows * tabWidth) : this.value.length);
					}
				}
			} else if (e.keyCode === 27) {	// Esc
				tabIndent.events.disable(e);
			} else if (e.keyCode === 13 && e.shiftKey === false) {	// Enter
				var	self = tabIndent,
					cursorPos = this.selectionStart,
					startIndices = self.findStartIndices(this),
					numStartIndices = startIndices.length,
					startIndex = 0,
					endIndex = 0,
					tabMatch = new RegExp(tab.replace('\t', '\\t').replace(/ /g, '\\s'), 'g'),
					lineText = '',
					indentText = '';
					tabs = null,
					numTabs = 0;

				for(var x=0;x<numStartIndices;x++) {
					if (startIndices[x+1] && (cursorPos >= startIndices[x]) && (cursorPos < startIndices[x+1])) {
						startIndex = startIndices[x];
						endIndex = startIndices[x+1] - 1;
						break;
					} else {
						startIndex = startIndices[numStartIndices-1];
						endIndex = this.value.length;
					}
				}

				// Find the number of tab characters following this line start index
				lineText = this.value.slice(startIndex, endIndex);
				tabs = lineText.match(tabMatch);
				if (tabs !== null) {
					e.preventDefault();
					numTabs = tabs.length;
					for(x=0;x<numTabs;x++) {
						indentText += tab;
					}

					this.value = this.value.slice(0, endIndex) + "\n" + indentText + this.value.slice(endIndex);
					this.selectionStart = endIndex + (tabWidth * numTabs) + 1;
					this.selectionEnd = endIndex + (tabWidth * numTabs) + 1;
				}
			}
		},
		disable: function(e) {
			var events = this;

			// Temporarily suspend the main tabIndent event
			tabIndent.remove(e.target);
		},
		focus: function() {
			var	self = tabIndent,
				el = this,
				delayedRefocus = setTimeout(function() {
					var classes = (el.getAttribute('class') || '').split(' '),
					contains = classes.indexOf('tabIndent');

					el.addEventListener('keydown', self.events.keydown);
					
					if (contains !== -1) classes.splice(contains, 1);
					classes.push('tabIndent-rendered');
					el.setAttribute('class', classes.join(' '));

					el.removeEventListener('focus', self.events.keydown);
				}, 500);

			// If they were just tabbing through the input, let them continue unimpeded
			el.addEventListener('blur', function b() {
				clearTimeout(delayedRefocus);
				el.removeEventListener('blur', b);
			});
		}
	},
	render: function(el) {
		var self = this;

		if (el.nodeName === 'TEXTAREA') {
			//IE,fireFox 无法使用暂时注释 云凤程
			if(el.addEventListener){
				el.addEventListener('focus', self.events.focus);
	
				el.addEventListener('blur', function b(e) {
					self.events.disable(e);
				});
			}
		}
	},
	renderAll: function() {
		// Find all elements with the tabIndent class
		var textareas = document.getElementsByTagName('textarea'),
			t = textareas.length,
			contains = -1,
			classes = [],
			el = undefined;

		while(t--) {
			classes = (textareas[t].getAttribute('class') || '').split(' ');
			contains = classes.indexOf('tabIndent');

			if (contains !== -1) {
				el = textareas[t];
				this.render(el);
			}
			contains = -1;
			classes = [];
			el = undefined;
		}
	},
	remove: function(el) {
		if (el.nodeName === 'TEXTAREA') {
			var classes = (el.getAttribute('class') || '').split(' '),
				contains = classes.indexOf('tabIndent-rendered');

			if (contains !== -1) {
				el.removeEventListener('keydown', this.events.keydown);
				el.style.backgroundImage = '';

				classes.splice(contains, 1);
				classes.push('tabIndent');
				el.setAttribute('class', (classes.length > 1 ? classes.join(' ') : classes[0]));
			}
		}
	},
	removeAll: function() {
		// Find all elements with the tabIndent class
		var textareas = document.getElementsByTagName('textarea'),
			t = textareas.length,
			contains = -1,
			classes = [],
			el = undefined;

		while(t--) {
			classes = (textareas[t].getAttribute('class') || '').split(' ');
			contains = classes.indexOf('tabIndent-rendered');

			if (contains !== -1) {
				el = textareas[t];
				this.remove(el);
			}
			contains = -1;
			classes = [];
			el = undefined;
		}
	},
	isMultiLine: function(el) {
		// Extract the selection
		var	snippet = el.value.slice(el.selectionStart, el.selectionEnd),
			nlRegex = new RegExp(/\n/);

		if (nlRegex.test(snippet)) return true;
		else return false;
	},
	findStartIndices: function(el) {
		var	text = el.value,
			startIndices = [],
			offset = 0;

		while(text.match(/\n/) && text.match(/\n/).length > 0) {
			offset = (startIndices.length > 0 ? startIndices[startIndices.length - 1] : 0);
			var lineEnd = text.search("\n");
			startIndices.push(lineEnd + offset + 1);
			text = text.substring(lineEnd + 1);
		}
		startIndices.unshift(0);

		return startIndices;
	}
}
