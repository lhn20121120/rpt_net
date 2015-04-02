//http://www.blogjava.net/alexwan/archive/2008/07/30/218580.html
Ext.ux.ComparingNumberField = function(config) {
  this.topField = config.topField;
  Ext.ux.ComparingNumberField.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ux.ComparingNumberField, Ext.form.NumberField, {
  clearInvalid : function() {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.removeClass(this.invalidClass);
    this.topField.clearInvalid();
  },
  markInvalid : function(msg) {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.addClass(this.invalidClass);
    msg = msg || this.invalidText;
    this.topField.markInvalid(msg);
  }
});
//----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
Ext.ux.ComparingDateField = function(config) {
  this.topField = config.topField;
  this.format = config.format || 'Y-m-d';
  Ext.ux.ComparingDateField.superclass.constructor.call(this, Ext.apply({
    format : this.format
  }, config));
};

Ext.extend(Ext.ux.ComparingDateField, Ext.form.DateField, {
  clearInvalid : function() {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.removeClass(this.invalidClass);
    this.topField.clearInvalid();
  },
  markInvalid : function(msg) {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.addClass(this.invalidClass);
    msg = msg || this.invalidText;
    this.topField.markInvalid(msg);
  }
});
//----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
Ext.ux.ComparingTimeField = function(config) {
  this.topField = config.topField;
  this.format = config.format || 'H:i:s';
  Ext.ux.ComparingTimeField.superclass.constructor.call(this, Ext.apply({
    format : this.format
  }, config));
};

Ext.extend(Ext.ux.ComparingTimeField, Ext.form.TimeField, {
  clearInvalid : function() {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.removeClass(this.invalidClass);
    this.topField.clearInvalid();
  },
  markInvalid : function(msg) {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.addClass(this.invalidClass);
    msg = msg || this.invalidText;
    this.topField.markInvalid(msg);
  }
});
//----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
Ext.ux.ComparingDateTimeField = function(config) {
  this.topField = config.topField;
  this.timeWidth = config.timeWidth || 80;
  this.dateFormat = config.dateFormat || 'Y-m-d';
  this.timeFormat = config.timeFormat || 'H:i:s';
  Ext.ux.ComparingDateTimeField.superclass.constructor.call(this, Ext.apply({
    dateFormat : this.dateFormat,
    timeFormat : this.timeFormat,
    timeWidth : this.timeWidth
  }, config));
};

Ext.extend(Ext.ux.ComparingDateTimeField, Ext.ux.DateTime, {
  clearInvalid : function() {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.removeClass(this.invalidClass);
    this.topField.clearInvalid();
  },
  markInvalid : function(msg) {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.addClass(this.invalidClass);
    msg = msg || this.invalidText;
    this.topField.markInvalid(msg);
  }
});
//----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
Ext.ux.SymbolCombobox = function(config) {
  this.topField = config.topField;
  this.hiddenName = config.name;
  this.type = config.type || 'number';
  this.lessThan = -2;
  this.notMoreThan = -1;
  this.equal = 0;
  this.notLessThan = 1;
  this.moreThan = 2;
  this.symbols = [[this.lessThan, '<'], [this.notMoreThan, '=<'],
      [this.equal, '='], [this.notLessThan, '>='],
      [this.notMoreThan, '>']];
  this.store = new Ext.data.SimpleStore({
    fields : ['value', 'displayValue'],
    data : this.symbols
  });
  Ext.ux.SymbolCombobox.superclass.constructor.call(this, Ext.apply({
    hiddenName : this.hiddenName,
    store : this.store,
    hiddenField : true,
    displayField : 'displayValue',
    valueField : 'value',
    typeAhead : true,
    mode : 'local',
    triggerAction : 'all',
    selectOnFocus : true,
    value : 0
  }, config));
}
Ext.extend(Ext.ux.SymbolCombobox, Ext.form.ComboBox, {
  clearInvalid : function() {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.removeClass(this.invalidClass);
    this.topField.clearInvalid();
  },
  markInvalid : function(msg) {
    if (!this.rendered || this.preventMark) { // not rendered
      return;
    }
    this.el.addClass(this.invalidClass);
    msg = msg || this.invalidText;
    this.topField.markInvalid(msg);
  }
});
//----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
Ext.ux.ComparingField = function(config) {
  this.messages = new Array();
  this.type = config.type;
  var symbolConfig = Ext.apply({}, {
    name : config.name + 'Symbol',

    topField : this,
    listeners : {
      blur : {
        scope : this,
        fn : this.onBlur
      },
      focus : {
        scope : this,
        fn : this.onFocus
      }
    }
  }, config.symbolConfig);
  this.symbolField = new Ext.ux.SymbolCombobox(symbolConfig);

  var valueConfig = Ext.apply({}, {
    name : config.name + 'Value',
    topField : this,
    listeners : {
      blur : {
        scope : this,
        fn : this.onBlur
      },
      focus : {
        scope : this,
        fn : this.onFocus
      }
    }
  }, config.valueConfig);
  switch (this.type) {
    case 'date' :
      this.valueField = new Ext.ux.ComparingDateField(valueConfig);
      break;
    case 'time' :
      this.valueField = new Ext.ux.ComparingTimeField(valueConfig);
      break;
    case 'dateTime' :
      this.valueField = new Ext.ux.ComparingDateTimeField(valueConfig);
      break;
    default :
      this.valueField = new Ext.ux.ComparingNumberField(valueConfig);
      break;
  }

  Ext.ux.ComparingField.superclass.constructor.call(this, config);

};
Ext.extend(Ext.ux.ComparingField, Ext.form.Field, {
  defaultAutoCreate : {
    tag : 'input',
    type : 'hidden'
  },
  symbolWidth : 50,
  initComponent : function() {
    Ext.ux.ComparingField.superclass.initComponent.call(this);
    this.relayEvents(this.symbolField, ['focus', 'specialkey', 'invalid',
        'valid']);
    this.relayEvents(this.valueField, ['focus', 'specialkey', 'invalid',
        'valid']);

  },
  onRender : function(ct, position) {
    if (this.isRendered) {
      return;
    }
    Ext.ux.ComparingField.superclass.onRender.call(this, ct, position);

    var t = Ext.DomHelper.append(ct, {
      tag : 'table',
      style : 'border-collapse:collapse',
      children : [{
        tag : 'tr',
        children : [{
          tag : 'td',
          style : 'padding-right:4px',
          cls : 'ux-comparing-symbol'
        }, {
          tag : 'td',
          cls : 'ux-comparing-value'
        }]
      }]
    }, true);

    this.tableEl = t;
    this.wrap = t.wrap({
      cls : 'x-form-field-wrap'
    });
    this.wrap.on("mousedown", this.onMouseDown, this, {
      delay : 10
    });

    // render symbolField & valueField
    this.symbolField.render(t.child('td.ux-comparing-symbol'));
    this.valueField.render(t.child('td.ux-comparing-value'));

    if (Ext.isIE && Ext.isStrict) {
      t.select('input').applyStyles({
        top : 0
      });
    }

    this.on('specialkey', this.onSpecialKey, this);
    this.symbolField.el.swallowEvent(['keydown', 'keypress']);
    this.valueField.el.swallowEvent(['keydown', 'keypress']);

    // create icon for side invalid errorIcon
    if ('side' === this.msgTarget) {
      var elp = this.el.findParent('.x-form-element', 10, true);
      this.errorIcon = elp.createChild({
        cls : 'x-form-invalid-icon'
      });

      this.symbolField.errorIcon = this.errorIcon;
      this.valueField.errorIcon = this.errorIcon;
    }

    this.isRendered = true;

  },
  adjustSize : Ext.BoxComponent.prototype.adjustSize

  ,
  alignErrorIcon : function() {
    this.errorIcon.alignTo(this.tableEl, 'tl-tr', [2, 0]);
  }

  ,
  disable : function() {
    if (this.isRendered) {
      this.symbolField.disabled = this.disabled;
      this.symbolField.onDisable();
      this.valueField.onDisable();
    }
    this.disabled = true;
    this.symbolField.disabled = true;
    this.valueField.disabled = true;
    this.fireEvent("disable", this);
    return this;
  },
  enable : function() {
    if (this.rendered) {
      this.symbolField.onEnable();
      this.valueField.onEnable();
    }
    this.disabled = false;
    this.symbolField.disabled = false;
    this.valueField.disabled = false;
    this.fireEvent("enable", this);
    return this;
  },
  focus : function() {
    this.symbolField.focus();
  },
  getPositionEl : function() {
    return this.wrap;
  }

  ,
  getResizeEl : function() {
    return this.wrap;
  }

  ,
  getValue : function() {

    return '';
  },
  isValid : function() {
    return this.symbolField.isValid() && this.valueField.isValid();
  },
  isVisible : function() {
    return this.symbolField.rendered
        && this.symbolField.getActionEl().isVisible();
  },
  onBlur : function(f) {
    // called by both DateField and TimeField blur events

    // revert focus to previous field if clicked in between
    if (this.wrapClick) {
      f.focus();
      this.wrapClick = false;
    }

    (function() {
      if (!this.symbolField.hasFocus && !this.valueField.hasFocus) {
        var v = this.getValue();
        if (String(v) !== String(this.startValue)) {
          this.fireEvent("change", this, v, this.startValue);
        }
        this.hasFocus = false;
        this.fireEvent('blur', this);
      }
    }).defer(100, this);

  },
  onFocus : function() {
    if (!this.hasFocus) {
      this.hasFocus = true;
      this.startValue = this.getValue();
      this.fireEvent("focus", this);
    }
  }

  ,
  onMouseDown : function(e) {
    this.wrapClick = 'td' === e.target.nodeName.toLowerCase();
  }

  ,
  onSpecialKey : function(t, e) {
    var key = e.getKey();
    if (key == e.TAB) {
      if (t === this.df && !e.shiftKey) {
        e.stopEvent();
        this.valueField.focus();
      }
      if (t === this.tf && e.shiftKey) {
        e.stopEvent();
        this.symbolField.focus();
      }
    }
    // otherwise it misbehaves in editor grid
    if (key == e.ENTER) {
      this.updateValue();
    }

  }

  ,
  setSize : function(w, h) {
    if (!w) {
      return;
    }
    this.symbolField.setSize(this.symbolWidth, h);
    this.valueField.setSize(w - this.symbolWidth - 4, h);

    if (Ext.isIE) {
      this.symbolField.el.up('td').setWidth(this.symbolWidth);
      this.valueField.el.up('td').setWidth(w - this.symbolWidth - 4);
    }
  },
  setValue : function(val) {

    this.updateValue();
  },
  setVisible : function(visible) {
    if (visible) {

      this.symbolField.show();
      this.valueField.show();

    } else {
      this.symbolField.hide();
      this.valueField.hide();
    }
    return this;
  },
  reset : function() {
    this.symbolField.reset();
    this.valueField.reset();
    this.clearInvalid();
  },
  show : function() {

    return this.setVisible(true);
  },
  hide : function() {

    return this.setVisible(false);
  },
  updateValue : function() {
    return;
  },
  validate : function() {
    return this.symbolField.validate() && this.valueField.validate();
  },
  renderer : function(field) {
  },
  markInvalid : function(msg) {
    this.messages.push(msg);
    Ext.ux.ComparingField.superclass.markInvalid.call(this, msg);
  },
  clearInvalid : function() {
    this.messages.pop();
    if (this.messages.length == 0) {
      Ext.ux.ComparingField.superclass.clearInvalid.call(this);
    } else {
      var msg = this.messages.pop();
      this.markInvalid(msg);
    }
  }
}

);
//----------------------------------------------------------------------------------
//----------------------------------------------------------------------------------
Ext.ux.IntervalField = function(config) {
  this.messages = new Array();
  this.type = config.type;
  var startConfig = Ext.apply({}, {
    name : config.name + 'Start',

    topField : this,
    listeners : {
      blur : {
        scope : this,
        fn : this.onBlur
      },
      focus : {
        scope : this,
        fn : this.onFocus
      }
    }
  }, config.startConfig);

  var endConfig = Ext.apply({}, {
    name : config.name + 'End',
    topField : this,
    listeners : {
      blur : {
        scope : this,
        fn : this.onBlur
      },
      focus : {
        scope : this,
        fn : this.onFocus
      }
    }
  }, config.endConfig);
  switch (this.type) {
    case 'date' :
      this.endField = new Ext.ux.ComparingDateField(endConfig);
      this.startField = new Ext.ux.ComparingDateField(startConfig);
      break;
    case 'time' :
      this.endField = new Ext.ux.ComparingTimeField(endConfig);
      this.startField = new Ext.ux.ComparingTimeField(startConfig);
      break;
    case 'dateTime' :
      this.endField = new Ext.ux.ComparingDateTimeField(endConfig);
      this.startField = new Ext.ux.ComparingDateTimeField(startConfig);
      break;
    default :
      this.endField = new Ext.ux.ComparingNumberField(endConfig);
      this.startField = new Ext.ux.ComparingNumberField(startConfig);
      break;
  }

  Ext.ux.IntervalField.superclass.constructor.call(this, config);
}
Ext.extend(Ext.ux.IntervalField, Ext.form.Field, {
  defaultAutoCreate : {
    tag : 'input',
    type : 'hidden'
  },
  startFieldWidth : 75,
  baseWidth : 12,
  leftText : '',
  centerText : '-',
  errorText : '开始的值不能大于结束的值',
  //rightText:'之间',
  initComponent : function() {
    Ext.ux.IntervalField.superclass.initComponent.call(this);
    this.relayEvents(this.startField, ['focus', 'specialkey', 'invalid',
        'valid']);
    this.relayEvents(this.endField, ['focus', 'specialkey', 'invalid',
        'valid']);

  },
  onRender : function(ct, position) {
    if (this.isRendered) {
      return;
    }
    Ext.ux.IntervalField.superclass.onRender.call(this, ct, position);
    var t;
    if ('dateTime' === this.type) {
      var upChildren = new Array();
      var belowChildren = new Array();
      if (this.leftText)
        upChildren[upChildren.length] = {
          tag : 'td',
          html : this.leftText
        };
      upChildren[upChildren.length] = {
        tag : 'td',
        style : 'padding-bottom:1px',
        cls : 'ux-interval-start'
      };
      if (this.centerText)
        belowChildren[belowChildren.length] = {
          tag : 'td',
          html : this.centerText
        };
      belowChildren[belowChildren.length] = {
        tag : 'td',
        cls : 'ux-interval-end'
      };
      if (this.rightText)
        belowChildren[belowChildren.length] = {
          tag : 'td',
          html : this.rightText
        };
      t = Ext.DomHelper.append(ct, {
        tag : 'table',
        style : 'border-collapse:collapse',
        children : [{
          tag : 'tr',
          children : upChildren
        }, {
          tag : 'tr',
          children : belowChildren
        }]
      }, true);
    } else {

      var children = new Array();
      if (this.leftText)
        children[children.length] = {
          tag : 'td',
          html : this.leftText
        };
      children[children.length] = {
        tag : 'td',
        style : 'padding-right:4px',
        cls : 'ux-interval-start'
      };
      if (this.centerText)
        children[children.length] = {
          tag : 'td',
          html : this.centerText
        };
      children[children.length] = {
        tag : 'td',
        cls : 'ux-interval-end'
      };
      if (this.rightText)
        children[children.length] = {
          tag : 'td',
          html : this.rightText
        };
      t = Ext.DomHelper.append(ct, {
        tag : 'table',
        style : 'border-collapse:collapse',
        children : [{
          tag : 'tr',
          children : children
        }]
      }, true);
    }

    this.tableEl = t;
    this.wrap = t.wrap({
      cls : 'x-form-field-wrap'
    });
    this.wrap.on("mousedown", this.onMouseDown, this, {
      delay : 10
    });

    // render startField & endField
    this.startField.render(t.child('td.ux-interval-start'));
    this.endField.render(t.child('td.ux-interval-end'));

    if (Ext.isIE && Ext.isStrict) {
      t.select('input').applyStyles({
        top : 0
      });
    }

    this.on('specialkey', this.onSpecialKey, this);
    this.startField.el.swallowEvent(['keydown', 'keypress']);
    this.endField.el.swallowEvent(['keydown', 'keypress']);

    // create icon for side invalid errorIcon
    if ('side' === this.msgTarget) {
      var elp = this.el.findParent('.x-form-element', 10, true);
      this.errorIcon = elp.createChild({
        cls : 'x-form-invalid-icon'
      });

      this.startField.errorIcon = this.errorIcon;
      this.endField.errorIcon = this.errorIcon;
    }

    this.isRendered = true;

  },
  adjustSize : Ext.BoxComponent.prototype.adjustSize

  ,
  alignErrorIcon : function() {
    this.errorIcon.alignTo(this.tableEl, 'tl-tr', [2, 0]);
  }

  ,
  disable : function() {
    if (this.isRendered) {
      this.startField.disabled = this.disabled;
      this.startField.onDisable();
      this.endField.onDisable();
    }
    this.disabled = true;
    this.startField.disabled = true;
    this.endField.disabled = true;
    this.fireEvent("disable", this);
    return this;
  },
  enable : function() {
    if (this.rendered) {
      this.startField.onEnable();
      this.endField.onEnable();
    }
    this.disabled = false;
    this.startField.disabled = false;
    this.endField.disabled = false;
    this.fireEvent("enable", this);
    return this;
  },
  focus : function() {
    this.startField.focus();
  },
  getPositionEl : function() {
    return this.wrap;
  }

  ,
  getResizeEl : function() {
    return this.wrap;
  }

  ,
  getValue : function() {

    return '';
  },
  isValid : function() {
    return this.startField.isValid() && this.endField.isValid();
  },
  isVisible : function() {
    return this.startField.rendered
        && this.startField.getActionEl().isVisible();
  },
  onBlur : function(f) {
    // called by both DateField and TimeField blur events

    // revert focus to previous field if clicked in between
    if (this.wrapClick) {
      f.focus();
      this.wrapClick = false;
    }

    (function() {
      if (!this.startField.hasFocus && !this.endField.hasFocus) {
        var v = this.getValue();
        if (String(v) !== String(this.startValue)) {
          this.fireEvent("change", this, v, this.startValue);
        }
        this.hasFocus = false;
        this.fireEvent('blur', this);
      }
    }).defer(100, this);

  },
  onFocus : function() {
    if (!this.hasFocus) {
      this.hasFocus = true;
      this.startValue = this.getValue();
      this.fireEvent("focus", this);
    }
  }

  ,
  onMouseDown : function(e) {
    this.wrapClick = 'td' === e.target.nodeName.toLowerCase();
  }

  ,
  onSpecialKey : function(t, e) {
    var key = e.getKey();
    if (key == e.TAB) {
      if (t === this.df && !e.shiftKey) {
        e.stopEvent();
        this.endField.focus();
      }
      if (t === this.tf && e.shiftKey) {
        e.stopEvent();
        this.startField.focus();
      }
    }
    // otherwise it misbehaves in editor grid
    if (key == e.ENTER) {
      this.updateValue();
    }

  }

  ,
  setSize : function(w, h) {
    if (!w) {
      return;
    }

    if ('dateTime' == this.type) {
      var upTextLength = 0;
      if (this.leftText)
        upTextLength += this.leftText.length;
      var belowTextLength = 0;
      if (this.centerText)
        belowTextLength += this.centerText.length;
      if (this.rightText)
        belowTextLength += this.rightText.length;

      this.startField.setSize(w - upTextLength * this.baseWidth, h);
      this.endField.setSize(w - belowTextLength * this.baseWidth, h);
      if (Ext.isIE) {
        this.startField.el.up('td').setWidth(w - upTextLength
            * this.baseWidth);
        this.endField.el.up('td').setWidth(w - belowTextLength
            * this.baseWidth);
      }
    } else {
      var textLength = 0;
      if (this.leftText)
        textLength += this.leftText.length;
      if (this.centerText)
        textLength += this.centerText.length;
      if (this.rightText)
        textLength += this.rightText.length;
      this.startField.setSize(this.startFieldWidth, h);
      this.endField.setSize(w - this.startFieldWidth - 4 - textLength
          * this.baseWidth, h);

      if (Ext.isIE) {
        this.startField.el.up('td').setWidth(this.startFieldWidth);
        this.endField.el.up('td').setWidth(w - this.startFieldWidth - 4
            - textLength * this.baseWidth);
      }
    }
  },
  setValue : function(val) {

    this.updateValue();
  },
  setVisible : function(visible) {
    if (visible) {

      this.startField.show();
      this.endField.show();

    } else {
      this.startField.hide();
      this.endField.hide();
    }
    return this;
  },
  reset : function() {
    this.startField.reset();
    this.endField.reset();
    this.clearInvalid();
  },
  show : function() {

    return this.setVisible(true);
  },
  hide : function() {

    return this.setVisible(false);
  },
  updateValue : function() {
    return;
  },
  validate : function() {
    if (this.startField.getValue() > this.endField.getValue()) {
      this.startField.markInvalid(this.errorText);
      return false;
    }
    return this.startField.validate() && this.endField.validate();
  },
  renderer : function(field) {
  },
  markInvalid : function(msg) {
    this.messages.push(msg);
    Ext.ux.IntervalField.superclass.markInvalid.call(this, msg);
  },
  clearInvalid : function() {
    this.messages.pop();
    if (this.messages.length == 0) {
      Ext.ux.IntervalField.superclass.clearInvalid.call(this);
    } else {
      var msg = this.messages.pop();
      this.markInvalid(msg);
    }
  }

}

);
