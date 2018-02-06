/**************************************************************************
*	@name		    Zozo UI Accordion
*	@descripton	    Create awesome accordion
*	@version	    4.4
*   @requires       jQuery v1.7 or later
*	@copyright      Copyright (c) 2013 Zozo UI
*   @author         Zozo UI
*   @URL:           http://www.zozoui.com
**************************************************************************/

/**
 * jQuery.browser.mobile (http://detectmobilebrowser.com/)
 **/
; (function (a) { (jQuery.browser = jQuery.browser || {}).mobile = /(android|ipad|playbook|silk|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4)) })(navigator.userAgent || navigator.vendor || window.opera);

; (function ($, window, document, undefined) {
    if (!window.console) window.console = {};
    if (!window.console.log) window.console.log = function () { };

    $.fn.extend({
        hasClasses: function (selectors) {
            var _base = this;
            for (i in selectors) {
                if ($(_base).hasClass(selectors[i]))
                    return true;
            }
            return false;
        }
    });

    $.zozo = {};
    $.zozo.core = {};
    $.zozo.core.console = {
        debug: false,
        log: function (message) {
            if ($("#zozo-console").length != 0) {
                $("<div/>")
                .css({ marginTop: -24 })
                .html(message)
                .prependTo("#zozo-console")
                .animate({ marginTop: 0 }, 300)
                .animate({ backgroundColor: "#ffffff" }, 800);
            }
            else {
                if (console && this.debug === true) {
                    console.log(message);
                }
            }
        }
    };


    $.zozo.core.content = {
        debug: false,
        video: function (_content) {
            if (_content) {
                _content.find("iframe").each(function () {
                    var _iframeSrc = $(this).attr('src');
                    var wmode = "wmode=transparent";
                    if (_iframeSrc && _iframeSrc.indexOf(wmode) === -1) {
                        if (_iframeSrc.indexOf('?') != -1) $(this).attr('src', _iframeSrc + '&' + wmode);
                        else $(this).attr('src', _iframeSrc + '?' + wmode);
                    }
                });
            }
        },
        check: function (_content) {
            this.video(_content);
        }
    };

    $.zozo.core.keyCodes = {
        tab: 9,
        enter: 13,
        esc: 27,

        space: 32,
        pageup: 33,
        pagedown: 34,
        end: 35,
        home: 36,

        left: 37,
        up: 38,
        right: 39,
        down: 40
    };

    $.zozo.core.debug = {
        startTime: new Date(),
        log: function (msg) {
            if (console) {
                console.log(msg);
            }
        },
        start: function () {
            this.startTime = +new Date();
            this.log("start: " + this.startTime);
        },
        stop: function () {
            var _end = +new Date();
            var _diff = _end - this.startTime;

            this.log("end: " + _end);
            this.log("diff: " + _diff);

            var Seconds_from_T1_to_T2 = _diff / 1000;
            var Seconds_Between_Dates = Math.abs(Seconds_from_T1_to_T2);

            //this.log("diff s: " + Seconds_Between_Dates);
        }
    };

    $.zozo.core.support = {
        is_mouse_present: function () {
            return (('onmousedown' in window) && ('onmouseup' in window) && ('onmousemove' in window) && ('onclick' in window) && ('ondblclick' in window) && ('onmousemove' in window) && ('onmouseover' in window) && ('onmouseout' in window) && ('oncontextmenu' in window));
        },
        is_touch_device: function () {
            return (('ontouchstart' in window) ||   // html5 browsers
             (navigator.maxTouchPoints > 0) ||      // future IE
             (navigator.msMaxTouchPoints > 0)) &&   // current IE10
             (jQuery.browser.mobile);               // mobile browser
        },
        html5_storage: function () {
            try {//https://github.com/artberri/jquery-html5storage/blob/master/jquery.html5storage.js
                return 'localStorage' in window && window['localStorage'] !== null;
            } catch (e) {
                return false;
            }
        },
        supportsCss: (function () {
            var div = document.createElement('div'), vendors = 'khtml ms o moz webkit'.split(' '), cssPre = false;
            return function (prop) {
                (prop in div.style) && (cssPre = prop)
                var propUp = prop.replace(/^[a-z]/, function (val) { return val.toUpperCase(); });
                $.each(vendors, function (index, value) {
                    (value + propUp in div.style) && (cssPre = '-' + value + '-' + prop);
                });
                return cssPre;
            };
        })(),
        css: {
            transition: false
        }
    };


    $.zozo.core.utils = {
        toArray: function (_object) {
            return $.map(_object, function (value, key) {
                return value;
            });
        },
        createHeader: function (_t, _c) {
            var _tab = $("<li><a>" + _t + "</a></li>");
            var _content = $("<div>" + _c + "</div>");

            return { tab: _tab, content: _content };
        },
        isEmpty: function (_str) {
            return (!_str || 0 === _str.length);
        },
        isNumber: function (_input) {
            return typeof _input === 'number' && isFinite(_input);
        },
        isEven: function (_number) {
            return _number % 2 === 0;
        },
        isOdd: function (_input) {
            return !(_number % 2 === 0);
        },
        animate: function (_base, _elem, _pre, _prop, _post, _hidePre) {
            var _duration = (_base.settings.animation.effects === "none") ? 0 : _base.settings.animation.duration;
            var _easing = _base.settings.animation.easing;
            var _transition = $.zozo.core.support.css.transition;

            if (_elem && _prop) {
                if (_pre) {
                    _elem.css(_pre);
                }

                /* moz transitions css transition fix*/
                var _prLeft = _elem.css("left");
                var _preTop = _elem.css("top");

                if (_base.settings.animation.type === "css") {
                    //pre animation
                    _prop[_transition] = "all " + _duration + "ms ease-in-out"

                    //animation
                    setTimeout(function () {
                        _elem.css(_prop);
                    });

                    //post animation                   
                    setTimeout(function () {
                        // _base.settings.animating = false;
                        if (_post) {
                            _elem.css(_post);
                        }
                        _elem.css(_transition, "");
                        //_container.removeClass(ANIMATINGCLASS);
                    }, _duration);
                }
                else {
                    //lem.show().animate(_prop, {
                    _elem.animate(_prop, {
                        duration: _duration,
                        easing: _easing,
                        complete: function () {
                            // _base.settings.animating = false;
                            if (_post) {
                                _elem.css(_post);
                            }
                            // _container.removeClass(ANIMATINGCLASS);
                            if (_hidePre) {
                                _elem.hide();
                            }
                        }
                    });
                }
            }

            return _base;
        }
    };

    $.zozo.core.plugins = {
        easing: function (_base) {
            var _exist = false;
            if (_base) {
                if (_base.settings) {
                    //set up a default value for easing
                    var _defEasing = 'swing';

                    // check for the existence of the easing plugin
                    if ($.easing.def) {
                        _exist = true;
                    }
                    else {
                        if (_base.settings.animation.easing != 'swing' && _base.settings.animation.easing != 'linear') {
                            _base.settings.animation.easing = _defEasing;
                        }
                    }
                }
            }
            return _exist;
        }
    };

    $.zozo.core.browser = {
        init: function () {
            this.browser = this.searchString(this.dataBrowser) || "An unknown browser";
            this.version = this.searchVersion(navigator.userAgent)
                           || this.searchVersion(navigator.appVersion)
                           || "an unknown version";

            $.zozo.core.console.log("init: " + this.browser + " : " + this.version);


            if (this.browser === "Explorer") {

                var _el = $("html");
                var version = parseInt(this.version);

                if (version === 6) {
                    _el.addClass("ie ie7");
                }
                else if (version === 7) {
                    _el.addClass("ie ie7");
                }
                else if (version === 8) {
                    _el.addClass("ie ie8");
                }
                else if (version === 9) {
                    _el.addClass("ie ie9");
                }
            }
        },
        isIE: function (_version) {
            if ($.zozo.core.utils.isNumber(_version)) {
                return (this.browser === "Explorer" && this.version <= _version)
            }
            else {
                return (this.browser === "Explorer")
            }
        },
        isChrome: function (_version) {
            if ($.zozo.core.utils.isNumber(_version)) {
                return (this.browser === "Chrome" && this.version <= _version)
            }
            else {
                return (this.browser === "Chrome")
            }
        },
        searchString: function (data) {
            for (var i = 0; i < data.length; i++) {
                var dataString = data[i].string;
                var dataProp = data[i].prop;
                this.versionSearchString = data[i].versionSearch || data[i].identity;
                if (dataString) {
                    if (dataString.indexOf(data[i].subString) != -1)
                        return data[i].identity;
                }
                else if (dataProp)
                    return data[i].identity;
            }
        },
        searchVersion: function (dataString) {
            var index = dataString.indexOf(this.versionSearchString);
            if (index == -1)
                return;
            return parseFloat(dataString.substring(index + this.versionSearchString.length + 1));
        },
        dataBrowser: [
            {
                string: navigator.userAgent,
                subString: "Chrome",
                identity: "Chrome"
            }, {
                string: navigator.vendor,
                subString: "Apple",
                identity: "Safari",
                versionSearch: "Version"
            }, {
                prop: window.opera,
                identity: "Opera"
            }, {
                string: navigator.userAgent,
                subString: "Firefox",
                identity: "Firefox"
            }, {
                string: navigator.userAgent,
                subString: "MSIE",
                identity: "Explorer",
                versionSearch: "MSIE"
            }
        ]
    };

    $.zozo.core.hashHelper = {
        mode: "single",
        separator: null,
        all: function (_sep) {
            var hashArray = [];
            var hash = document.location.hash;

            if (!this.hasHash()) {
                return hashArray;
            }

            if (this.isSimple(_sep)) {
                return hash.substring(1);
            } else {
                hash = hash.substring(1).split('&');
                for (var i = 0; i < hash.length; i++) {
                    var match = hash[i].split(_sep);
                    //if (match.length != 2 || match[0] in hashArray) return undefined;
                    if (match.length != 2 || match[0] in hashArray) {
                        match[1] = "none";
                    }
                    hashArray[match[0]] = match[1];
                }
            }

            return hashArray;
        },
        get: function (key, _sep) {
            var all = this.all(_sep);
            if (this.isSimple(_sep)) {
                return all;
            } else {
                if (typeof all === 'undefined' || typeof all.length < 0) {
                    //self.log("get: undefined or null all");
                    return null;
                }
                else {
                    if (typeof all[key] !== 'undefined' && all[key] !== null) {
                        //self.log("get: exist key");
                        return all[key];
                    }
                    else {
                        //self.log("get: undefined or null key" + key);
                        return null;
                    }
                }
            }

        },
        set: function (key, value, _sep, _mod) {
            if (this.isSimple(_sep)) {
                document.location.hash = value;
            } else {
                if (_mod === "multiple") {
                    var all = this.all(_sep);
                    var hash = [];
                    all[key] = value;
                    for (var key in all) {
                        hash.push(key + _sep + all[key]);
                    }
                    document.location.hash = hash.join('&');
                } else {
                    document.location.hash = key + _sep + value;
                }
            }
        },
        isSimple: function (_sep) {
            if (!_sep || _sep === "none") {
                return true;
            }
            else {
                return false;
            }
        },
        hasHash: function () {
            var hash = document.location.hash;
            if (hash.length > 0) {
                return true;
            }
            else {
                return false;
            }
        }
    };


    $.zozo.core.support.css.transition = $.zozo.core.support.supportsCss("transition");
    $.zozo.core.browser.init();

})(jQuery, window, document);


; (function ($, window, document, undefined) {
    var ZozoAccordion = function (elem, options) {
        this.elem = elem;
        this.$elem = $(elem);
        this.options = options;
        this.metadata = (this.$elem.data("options")) ? this.$elem.data("options") : {};
        this.attrdata = (this.$elem.data()) ? this.$elem.data() : {};
        this.elemID;
        this.$sections;
        this.sectionCount;
        this.$container;
        this.$contents;
        this.autoplayIntervalId;
        this.resizeWindowIntervalId;
        this.currentsection;
        this.browser = $.zozo.core.browser;
        this.responsive;
        this.lastWindowHeight;
        this.lastWindowWidth;
    };

    if (window.zozo == null) {
        window.zozo = {};
    }
    var zozo = {
        pluginName: "zozoAccordion",
        elementSpacer: "<span class='z-tab-spacer' style='clear: both;display: block;'></span>",
        commaRegExp: /,/g,
        headerTitle: "<span class='z-title'>Illustrations</span>",
        headerArrow: "<span class='z-arrow'><i class='icon-chevron-down'></i></span>",
        space: " ",
        responsive: {
            largeDesktop: 1200,
            desktop: 960,
            tablet: 720,
            phone: "auto"
        },
        animation: {
            effects: {
                fade: "fade",
                none: "none"
            },
            types: {
                css: "css",
                jquery: "jquery"
            }
        },
        expandModes: {
            single: "single",
            multiple: "multiple"
        },
        events: {
            click: "click",
            mousover: "mouseover",
            mouseenter: "mouseenter",
            mouseleave: "mouseleave",
            touchend: "touchend",
            touchstart: "touchstart",
            touchmove: "touchmove"
        },
        classes: {
            prefix: "z-",
            wrapper: "z-accordion",
            section: "z-section",
            first: "z-first",
            last: "z-last",
            active: "z-active",
            link: "z-header",
            focus: "z-focus",
            container: "z-container",
            content: "z-content",
            shadows: "z-shadows",
            bordered: "z-bordered",
            rounded: "z-rounded",
            scrollable: "z-scrollable",
            autoClass: "z-auto-g",
            themes: {
                gray: "gray",
                black: "black",
                blue: "blue",
                white: "white",
                lightblue: "lightblue",
                deepblue: "deepblue",
                crystal: "crystal",
                green: "green",
                yellow: "yellow",
                purple: "purple",
                silver: "silver",
                red: "red",
                orange: "orange",
                clean: "clean2"
            },
            orientations: {
                vertical: "vertical",
                horizontal: "horizontal"
            },
            groups: {
                grouped: "z-grouped",
                ungrouped: "z-ungrouped"
            }
        }
    },
    PX = "px",
    LINK = ".z-header",
    ARROW = ".z-arrow",
    ERROR = "error",
    DOTNAV = ".z-dot-nav",
    SELECT = "select",
    CONTENT = ".z-content",
    EXPAND = "expand",
    ACTIVATE = "activate",    
    //SECTION = "section",
    CONTENTS = "> " + CONTENT,
    SECTIONS = "> *",
    HEADERTAG = "> *:first-child",
    CONTENTTAG = "> *:last-child",
    CONTENTURL = "contentUrl",
    CONTENTLOAD = "contentLoad",
    DOTNAVCLASS = "z-dot-nav",
    MOBILECLASS = "z-mobile",
    ACTIVECLASS = "z-active",
    DISABLED = "disabled",
    DISABLEDCLASS = "z-disabled",
    LOADINGCLASS = "z-loading",
    AJAXSPINNERCLASS = "z-spinner",
    //ACTIVESECTION = SECTIONS + "." + ACTIVECLASS,
    DOTNAVITEMCLASS = "z-dot-nav-item",
    SLIDERWRAPPERCLASS = "z-slider-wrapper",
    SUBNAVCLASS = "z-sub-nav",
    SUBNAV = "> ." + SUBNAVCLASS,
    CONTENTNAVCLASS = "z-content-nav",
    DOTNAVITEM = DOTNAV + " span." + DOTNAVITEMCLASS,
    DOTNAVACTIVEITEM = DOTNAV + " ." + ACTIVECLASS;
    
    ZozoAccordion.prototype = {
        defaults: {
            animation: { duration: 400, effects: "fadeIn", easing: "easeOutQuart", type: zozo.animation.types.jquery },
            autoplay: { interval: 0, smart: true },
            active: false,
            activate: function () { },
            bordered: true,
            cacheAjax: true,
            contentHeight: 0,
            contentLoad: function () { },
            contentSpacing: 0,
            contentUrls: null,
            contentWidth: 715,
            dotNav: false,
            contentNav: false,
            headerFontSize: 1.5,
            event: zozo.events.click,
            error: function () { },
            expand: function () { },
            expandAfter: false,
            expandMode: zozo.expandModes.single,
            grouped: true,
            headerSize: 40,
            headerTag: "h3",
            height: 320,
            hideHeaders: false,
            iconStateClose:null,
            iconStateOpen:null,
            keyboard: false,
            minContentWidth: 0,
            minWidth: 480,
            minWindowWidth: 720,
            orientation: zozo.classes.orientations.vertical,
            original: { width: 0, height: 0, headerSize: 0, headerFontSize: 0, sectionSpacing: 0, orientation: null },
            responsive: false,
            responsiveDelay: 100,
            rounded: false,
            rememberState:false,
            sectionTag: "section",
            scrollable: false,
            shadows: true,
            showIcons: true,
            slider: false,
            sectionSpacing: 0,
            theme: zozo.classes.themes.silver,
            urlBased: false,
            horizontal: {
                headerSize: 40,
                headerFontSize: 1.1,
                sectionSpacing: 8
            },
            vertical: {
                headerSize: 38,
                headerFontSize: 1.1,
                sectionSpacing: 0
            },
            select: function () { },
            width: 960
        },
        init: function () {
            var _base = this;
            //setTimeout( function(){
            _base.settings = $.extend(true, {}, _base.defaults, _base.options, _base.metadata, _base.attrdata);

             methods.hideLoading(_base);

            _base.currentsection = _base.settings.active;
            _base.settings.original.width = _base.settings.width;
            _base.settings.original.height = _base.settings.height;
            _base.settings.original.headerSize = _base.settings.headerSize;
            _base.settings.original.orientation = _base.settings.orientation
            _base.settings.original.headerFontSize = _base.settings.headerFontSize;
            _base.settings.original.sectionSpacing = _base.settings.sectionSpacing;


            if (_base.settings.original.orientation === zozo.classes.orientations.vertical) {
                _base.settings.vertical.headerSize = _base.settings.original.headerSize;
            }

            if ((_base.settings.animation.type === zozo.animation.types.css && $.zozo.core.support.transition) || jQuery.browser.mobile) {
                //_base.settings.animation.duration = 0;
            }

            if (_base.settings.slider === true) {
                if (!_base.$elem.parent().hasClass(SLIDERWRAPPERCLASS)) {
                    _base.$elem.wrap("<div class='" + SLIDERWRAPPERCLASS + "'></div>");
                }
                if (_base.settings.dotNav === true && _base.settings.slider === true) {
                    _base.$sections = _base.$elem.find(SECTIONS);

                    var _dotNav = $("<div class='" + DOTNAVCLASS + "'></div>");
                    _base.$sections.each(function (index, item) {
                        _dotNav.append($("<span class='" + DOTNAVITEMCLASS + "'></span>"));
                    });

                    _base.$elem.parent().append(_dotNav);
                }
            }

            $.zozo.core.plugins.easing(_base);

            methods.updateClasses(_base);
            methods.bindEvents(_base);

            if (_base.settings.contentUrls != null) {
                _base.$sections.each(function (index, item) {
                    $(item).find("." + zozo.classes.link).data(CONTENTURL, _base.settings.contentUrls[index]);
                });
            }

            if (_base.settings.responsive === true && _base.settings.original.orientation === zozo.classes.orientations.horizontal) {
                methods.checkWidth(_base);
            }
            else {
                methods.loadState(_base);                
                var active = _base.settings.active;
                if (_base.settings.orientation === zozo.classes.orientations.vertical) {                    
                    if ($.isArray(active)) {
                        //_base.settings.expandMode = zozo.expandModes.multiple; //commented by ifti
                        var len = active.length;
                        for (var i = 0; i < len; i++) {
                            methods.showSection(_base, active[i]);
                            _base.settings.active = active[i];
                        }
                    } else {
                        methods.showSection(_base, active);
                    }
                }
                else {
                    methods.showSection(_base, active);
                }
            }

            methods.initAutoPlay(_base);
            return _base;
         // }, 2000); 
        },
        setOptions: function (_option, _refresh) {
            var _base = this;

            // methods.resetClasses(_base);

            _base.settings.active = _base.currentsection;
            _base.settings = $.extend(true, _base.settings, _option);

            methods.updateClasses(_base, true);

            //(_refresh === true) &&
            methods.showSection(_base, _base.settings.active);
            methods.initAutoPlay(_base);
            return _base;
        },
        add: function (_t, _c, _d) {
            var _base = this;
            var _section = methods.create(_base, _t, _c);

            _section.appendTo(_base.$elem);
            methods.updateClasses(_base);

            methods.bindEvent(_base, _section.find(HEADERTAG));

            return _base;
        },
        remove: function (_i) {
            var _base = this;
            var _section = _base.$sections.eq(_i);

            //remove events
            //var _events = $.zozo.core.utils.toArray(zozo.events);                
            //_section.find(HEADERTAG).off(_events.join(" "));

            _section.fadeOut(300, function () {
                $(this).remove();
                methods.updateClasses(_base);
            });
            return _base;
        },
        select: function (_i) {
            var _base = this;
            methods.showSection(_base, _i);
            return _base;
        },
        enable: function (_i) {
            var _base = this;
            var _tabToEnable = _base.$sections.eq(_i);

            if (_tabToEnable.length) {
                _tabToEnable.removeClass(DISABLEDCLASS);
                _tabToEnable.data(DISABLED, false);
            }
            return _base;
        },
        disable: function (_i) {
            var _base = this;
            var _tabToDisable = _base.$sections.eq(_i);

            if (_tabToDisable.length) {
                _tabToDisable.addClass(DISABLEDCLASS);
                _tabToDisable.data(DISABLED, true);
            }
            return _base;
        },
        destroy:function () {
            var _base = this;
            methods.resetClasses(_base);
        },
        first: function () {
            var _base = this;
            _base.select(methods.getFirst(_base));
            return _base;
        },
        prev: function () {
            var _base = this;
            var _currentIndex = parseInt(_base.currentsection);

            if (_currentIndex <= methods.getFirst(_base)) {
                _base.select(methods.getLast(_base));
            }
            else {
                _base.select(_currentIndex - 1);
            }
            return _base;
        },
        next: function (_base) {
            _base = (_base) ? _base : this;
            var _currentIndex = parseInt(_base.currentsection);
            var _count = methods.getLast(_base);

            if (_currentIndex >= _count) {
                _base.select(methods.getFirst(_base));
            }
            else {
                _base.select(_currentIndex + 1);
            }
            return _base;
        },
        last: function () {
            var _base = this;
            _base.select(methods.getLast(_base));
            return _base;
        },
        play: function (interval) {
            var _base = this;
            if (interval == null || interval < 0) {
                interval = 2000;
            }
            _base.settings.autoplay.interval = interval;
            _base.stop();
            _base.autoplayIntervalId = setInterval(function () { _base.next(_base); }, _base.settings.autoplay.interval);

            return _base;
        },
        stop: function (_base) {
            _base = (_base) ? _base : this;
            clearInterval(_base.autoplayIntervalId);
            return _base;
        },
        expandAll: function (_base) {
            _base = (_base) ? _base : this;
            return _base;
        },
        collapseAll: function (_base) {
            _base = (_base) ? _base : this;
            return _base;
        },
        refresh: function () {
            var _base = this;
            methods.checkWidth(_base);
            return _base;
        }
    };

    var methods = {
        resetClasses: function (_base) {
            // stop all animations
            _base.$elem.find('*').stop(true, true);
            _base.elemID = _base.$elem.attr("id");

            _base.$sections = _base.$elem.find(SECTIONS);
            _base.sectionCount = _base.$sections.length;
            //_base.settings.contentWidth = _base.settings.width - (_base.sectionCount * (_base.settings.headerSize + 1));
            _base.settings.contentWidth = _base.settings.width - (_base.sectionCount * (_base.settings.headerSize + _base.settings.sectionSpacing)); // adding lShift

            _base.$elem
                .attr("role", "tablist")
                //.attr("accesskey", "W")
                //.attr("tabindex", "0")
                .removeClass(zozo.classes.wrapper).addClass(zozo.classes.wrapper)
                .removeClass(zozo.classes.orientations.vertical)
                .removeClass(zozo.classes.orientations.horizontal)
                .removeClass(zozo.classes.groups.grouped)
                .removeClass(zozo.classes.groups.ungrouped)
                .addClass(_base.settings.orientation)
                .removeClass(zozo.classes.rounded)
                .removeClass(zozo.classes.shadows)
                .removeClass(zozo.classes.bordered)
                .parents("." + SLIDERWRAPPERCLASS).css({ 'width': "", padding: "" });

            _base.$elem.css({
                width: "",
                height: ""
            });

            _base.$sections.each(function (index, item) {
                var _section = $(item);

                _section.removeClass(zozo.classes.first)
                    .removeClass(zozo.classes.last)
                    .removeClass(zozo.classes.active)
                    .addClass(zozo.classes.section)
                    .css({ margin: 'none' });

                _section.css({
                    left: "",
                    width: "",
                    margin: ""
                });

                _section.find(HEADERTAG).css({
                    width: "",
                    height: "",
                    lineHeight: ""
                }).find("span").css({
                    width: "",
                    height: "",
                    lineHeight: ""
                });

                _section.find(CONTENTTAG).css({
                    height: "",
                    maxHeight: "",
                    width: "",
                    left: "",
                    display: "",
                    margin: "",
                    padding: ""
                }).find("> div").css({
                    height: "",
                    width: "",
                    left: "",
                    display: "",
                    margin: "",
                    padding: ""
                });
            });

            return _base;
        },
        updateClasses: function (_base, _active) {
            methods.resetClasses(_base, _active);

            _base.$sections.each(function (index, item) {
                var _section = $(item);
                var _head = _section.find(HEADERTAG);
                var _headText = _head.html();
                var _cont = _section.find(CONTENTTAG);
                var _icon = (_base.settings.showIcons === true) ? "<span class='z-arrow' style='top:none'></span>" : "";

                if (!_head.find("> span.z-title").length) {
                    _head.text("").append("<span class='z-title'>" + $.trim(_headText) + "</span>" + _icon).addClass(zozo.classes.link);
                }

                if (methods.isTabDisabled(_section)) {
                    _base.disable(index);
                }
                _cont.addClass(zozo.classes.content);
            });

            //set content height change css
            methods.setContentSize(_base);

            //update first and last
            _base.$sections.filter(zozo.classes.first + ":not(:first-child)").removeClass(zozo.classes.first);
            _base.$sections.filter(zozo.classes.last + ":not(:last-child)").removeClass(zozo.classes.last);
            _base.$sections.filter(":first-child").addClass(zozo.classes.first).find(HEADERTAG).attr("tabindex", "0").attr("accesskey", "w");
            _base.$sections.filter(":last-child").addClass(zozo.classes.last);

            var _themes = $.zozo.core.utils.toArray(zozo.classes.themes);
            if (!$.zozo.core.utils.isEmpty(_base.settings.theme)) {
                _base.$elem
                .removeClass(_themes.join().replace(zozo.commaRegExp, zozo.space))
                .addClass(_base.settings.theme);
            }
            else {
                if (!_base.$elem.hasClasses(_themes)) {
                    _base.$elem.addClass(zozo.classes.themes.silver);
                }
            }

            if ((_base.settings.animation.type === "css" && $.zozo.core.support.transition) || jQuery.browser.mobile) {
                //_base.settings.animation.duration = 0;

            }

            _base.$elem.addClass("transition");

            (_base.settings.contentNav === true) && _base.$elem.addClass(CONTENTNAVCLASS);
            (jQuery.browser.mobile === true) && (_base.$elem.addClass(MOBILECLASS));
            //($.zozo.core.support.transition) && _base.$elem.addClass("transition");
            (_base.settings.rounded === true) && _base.$elem.addClass(zozo.classes.rounded).parent("." + SLIDERWRAPPERCLASS).addClass(zozo.classes.rounded);
            (_base.settings.scrollable === true) && _base.$elem.addClass(zozo.classes.scrollable);
            (_base.settings.grouped === true) ? _base.$elem.addClass(zozo.classes.groups.grouped) : _base.$elem.addClass(zozo.classes.groups.ungrouped);
            (_base.settings.bordered === true) && _base.$elem.addClass(zozo.classes.bordered);
            (_base.settings.shadows === true) && _base.$elem.addClass(zozo.classes.shadows).parent("." + SLIDERWRAPPERCLASS).addClass(zozo.classes.shadows);




            //add aria
            methods.addAria(_base, { index: _base.currentsection });
        },
        setContentSize: function (_base) {
            var _slider = _base.settings.slider;
            var _contentNav = _base.settings.contentNav;
            var _orientation = _base.settings.orientation;           
            var _spacing = (_orientation === zozo.classes.orientations.vertical && _base.settings.responsive === true) ? _base.settings.vertical.sectionSpacing : _base.settings.sectionSpacing;
            var _headerSize = _orientation === zozo.classes.orientations.vertical ? _base.settings.vertical.headerSize : _base.settings.headerSize;
            var _width = _base.settings.contentWidth - ((_base.settings.contentSpacing * 2));
            var _height = _base.settings.height - (_base.settings.contentSpacing * 2);
            var _sliderWrapper = _base.$elem.parents("." + SLIDERWRAPPERCLASS);
            var _isIe8 = $.zozo.core.browser.isIE(8);
            var _isIe9 = $.zozo.core.browser.isIE(9);

            if (_orientation === zozo.classes.orientations.horizontal) {
                if (_spacing > 0) {
                    _base.settings.contentWidth = parseInt(_base.settings.width - (_base.sectionCount * (_base.settings.headerSize + _spacing - 1)));
                }
                else {
                    _base.settings.contentWidth = _base.settings.width - (_base.sectionCount * (_base.settings.headerSize));
                }

                _base.$elem.css({ width: (_spacing > 0) ? _base.settings.width - 1 : _base.settings.width, height: _base.settings.height });
                _sliderWrapper.css({ width: _base.settings.width });
            }
            else {
                (_spacing > 0) ? _base.settings.grouped = false : _base.settings.grouped = true;
            }


            if (_slider == true && _spacing > 0) {
                _sliderWrapper.css({ padding: _spacing + PX });
                (_orientation === zozo.classes.orientations.horizontal) ? _sliderWrapper.css({ paddingRight: 1, paddingBottom: (_spacing - 1) + PX }) : _sliderWrapper.css({ paddingTop: "1px", paddingBottom: "1px" })
            }

            _base.$sections.each(function (index, item) {
                var _$section = $(item);
                var _head = $(item).find(HEADERTAG).css({ outline: "none", height: (_headerSize + 1) + PX, lineHeight: (_headerSize + 2) + PX });//+2 verrsion 4
                var _cont = $(item).find(CONTENTTAG);


                (_isIe8) && (_head.css({ height: (_headerSize + 3) + PX }).find("> span.z-title").css({ height: (_base.settings.height) + PX }));


                //wrap content in inner content div
                if (!_$section.find("> * >." + zozo.classes.autoClass).length) {
                    var _contentContainer = $("<div class='" + zozo.classes.autoClass + "'></div>");
                    var _contentHtml = _cont.html();
                    _cont.html("");
                    _contentContainer.append(_contentHtml);
                    _contentContainer.appendTo(_cont);

                    (_base.settings.contentNav === true) && _contentContainer.find("> ul").addClass(SUBNAVCLASS);
                }

                if (_orientation === zozo.classes.orientations.horizontal) {
                    _head.css({ width: _base.settings.height });
                    if (_slider === true) {
                        _cont.css({ height: "100%", margin: 0 }).find("img").css({ margin: _base.settings.contentSpacing });

                        //_cont.css({ height: "100%", margin: 0 }).find("iframe").css({ width: "100%", height: "100%" });
                    }

                    if (_base.settings.responsive === true && _base.settings.original.headerFontSize > 0) {
                        _head.css({ fontSize: _base.settings.headerFontSize + "em" });
                    }
                }
                else {

                    //(_base.settings.contentHeight > 0) && (_cont.css({ height: _base.settings.contentHeight, overflow: "auto" }));                                            
                    _$section.css({ overflow: "", width: "100%", left: "", display: "block" });
                    if (_base.settings.grouped === false || _spacing > 0) {
                        _$section.css({
                            marginTop: _spacing + PX,
                            marginBottom: _spacing + PX
                        });
                    }

                    if (_base.settings.responsive === true && _base.settings.vertical.headerFontSize > 0) {
                        _head.css({ fontSize: _base.settings.vertical.headerFontSize + "em" });
                    }
                }

                if (_slider === true || _contentNav === true) {
                    if (_orientation === zozo.classes.orientations.horizontal) {
                        _cont.find(">." + zozo.classes.autoClass).css({ paddingLeft: (_spacing - 2) + PX });

                    } else {
                        _cont.find(">." + zozo.classes.autoClass).css({ paddingTop: _spacing + PX });
                    }
                }


                (_contentNav === true) && _cont.find(">." + zozo.classes.autoClass).find(SUBNAV + " > li:not(:first)").css({ marginTop: _spacing + PX });
            });
        },
        bindEvents: function (_base) {
            var _windowFocus = false;

            $(window).blur(function () {
                _windowFocus = false;
                //$.zozo.core.console.log("blur: " + _windowFocus);
            }).focus(function () {
                _windowFocus = true;
                //$.zozo.core.console.log("focus: " + _windowFocus);
            });

            _base.$elem.focus(function (e) {
                var _this = $(this);
                var _mdown = _this.data('mdown');

                _this.removeData('mdown');

                if (!_mdown) {
                    _this.addClass(zozo.classes.focus);
                }
            }).blur(function (e) {
                $(this).removeClass(zozo.classes.focus);
            });

            _base.$sections.each(function () {
                var $this = $(this);
                var _section = $this.find(HEADERTAG);
                var _content = $this.find("> .z-content");

                //css transition end event    
                _content.on("transitionend webkitTransitionEnd oTransitionEnd otransitionend MSTransitionEnd", function () {
                    _base.settings.animating = false;
                });

                if (_base.settings.hideHeaders === true) {
                    _section = $(this).find(">div>div>img");
                }

                if (!_section.find("a").length) {
                    methods.bindEvent(_base, _section);

                } else {
                    _section.on(_base.settings.event, function (e) {
                        var link = _section.find("a");
                        var target = link.attr("target");
                        if ($.trim(target).length) {
                            window.open(link.attr("href"), target);
                        }
                        else {
                            window.location = link.attr("href");
                        }
                        e.preventDefault();
                    });
                }
            });

            _base.$elem.bind(SELECT, _base.settings.select);
            _base.$elem.bind(EXPAND, _base.settings.expand);
            _base.$elem.bind(ACTIVATE, _base.settings.activate);
            _base.$elem.bind(ERROR, _base.settings.error);
            _base.$elem.bind(CONTENTLOAD, _base.settings.contentLoad);

            if (_base.settings.slider === true && _base.settings.dotNav === true) {
                $(DOTNAVITEM).each(function () {
                    $(this).on("click", function (_event) {
                        _event.preventDefault();
                        methods.showSection(_base, $(this).index());
                    });
                });


                $(".z-nav a.z-next").click(function (_event) {
                    _event.preventDefault();
                    methods.showSection(_base, _base.currentsection + 1);

                });

                $(".z-nav a.z-prev").click(function (_event) {
                    _event.preventDefault();
                    methods.showSection(_base, _base.currentsection - 1);
                });
            }

            if (_base.settings.responsive === true && _base.settings.original.orientation === zozo.classes.orientations.horizontal) {
                //responsive window resize
                $(window).resize(function () {
                    if (_base.lastWindowHeight !== $(window).height() || _base.lastWindowWidth !== $(window).width()) {
                        clearInterval(_base.resizeWindowIntervalId);
                        _base.resizeWindowIntervalId = setTimeout(function () {

                            _base.lastWindowHeight = $(window).height();
                            _base.lastWindowWidth = $(window).width();

                            methods.checkWidth(_base);
                        }, _base.settings.responsiveDelay);
                    }
                });

            }
        },
        bindEvent: function (_base, _section) {
            //jquery 1.5 _section.bind(_base.settings.event, function (_event) {


            if ($.zozo.core.support.is_touch_device()) {
                _section.on(zozo.events.touchstart, function (_event) {
                    $(this).on(zozo.events.touchend, function (_event) {

                        _event.preventDefault();
                        var _index = $(this).parent().index();
                        _base.currentsection = _index;

                        //stop autoplay if smart is true
                        if (_base.settings.autoplay !== false && _base.settings.autoplay != null) {
                            if (_base.settings.autoplay.smart === true) {
                                _base.stop();
                            }
                        }

                        methods.showSection(_base, _index);


                        $(this).off(zozo.events.touchend);
                    });
                    $(this).on(zozo.events.touchmove, function (_event) { $(this).off(zozo.events.touchend); });
                });
            } else {
                _section.on(_base.settings.event, function (_event) {
                    _event.preventDefault();
                    var _index = $(this).parent().index();
                    _base.currentsection = _index;

                    //stop autoplay if smart is true
                    if (_base.settings.autoplay !== false && _base.settings.autoplay != null) {
                        if (_base.settings.autoplay.smart === true) {
                            _base.stop();
                        }
                    }

                    methods.showSection(_base, _index);
                });
            }




            if (_base.settings.keyboard === true) {
                _section.on('keyup', function (e) {
                    e.preventDefault();
                    var _this = $(this);
                    var _keyCode = e.keyCode || e.which;
                    var _indexOriginal = _this.parent().index();
                    var _index = _this.parent().index();
                    var _total = _base.sectionCount;
                    if (_keyCode == $.zozo.core.keyCodes.space || _keyCode == $.zozo.core.keyCodes.enter) {
                        methods.showSection(_base, _index);
                    }
                    else if (_keyCode >= $.zozo.core.keyCodes.end || _keyCode <= $.zozo.core.keyCodes.down) {
                        if (_keyCode === $.zozo.core.keyCodes.home) {
                            _index = 0;
                        }
                        else if (_keyCode === $.zozo.core.keyCodes.end) {
                            _index = _total - 1;
                        }
                        else if (_keyCode === $.zozo.core.keyCodes.up || _keyCode === $.zozo.core.keyCodes.left) {
                            _index--;
                        }
                        else if (_keyCode === $.zozo.core.keyCodes.down || _keyCode === $.zozo.core.keyCodes.right) {
                            _index++;
                        }

                        if (_index != _indexOriginal) {
                            if (_index === -1) {
                                _index = _total - 1;
                            }
                            if (_index === _total && _keyCode != $.zozo.core.keyCodes.end) {
                                _index = 0;
                            }
                            _base.$sections.find(HEADERTAG).eq(_index).focus();
                        }
                    }
                }).mousedown(function (_event) {
                    var _this = $(this);
                    if (!_this.is(':focus')) {
                        _this.data('mdown', true);
                    }
                }).focus(function (e) {
                    var _this = $(this);
                    var _mdown = _this.data('mdown');

                    _this.removeData('mdown');

                    if (!_mdown) {
                        _this.addClass(zozo.classes.focus);
                    }
                }).blur(function (e) {
                    $(this).removeClass(zozo.classes.focus);
                });
            }

        },
        checkWidth: function (_base) {
            var _windowSize = $(window).width();
            var _orientation = _base.settings.orientation;
            var _minContentWidth = _base.settings.minContentWidth;
            var _minWidth = _base.settings.minWidth;
            var _minWindowWidth = _base.settings.minWindowWidth;
            var wrapper = _base.$elem.parents("." + SLIDERWRAPPERCLASS);
            var elem = _base.$elem;

            if (wrapper.length)
                elem = wrapper;

            _base.settings.width = elem.hide().parent().outerWidth() - 2;
            if ($(document).height() > $(window).height())
                //_base.settings.width = elem.hide().parent().outerWidth() - 17;


                if (_base.settings.slider === true && _base.settings.sectionSpacing > 0) {
                    _base.settings.width = _base.settings.width - ((_base.settings.width / _base.settings.original.width) * _base.settings.sectionSpacing);
                }

            elem.show();

            if (_base.settings.width > _base.settings.original.width) {
                _base.settings.width = _base.settings.original.width;
                _base.settings.height = _base.settings.original.height;
                _base.settings.headerSize = _base.settings.original.headerSize;
                _base.settings.headerFontSize = _base.settings.original.headerFontSize;
                _base.settings.sectionSpacing = _base.settings.original.sectionSpacing;

            } else {
                var _ratio = (_base.settings.width / _base.settings.original.width);

                _base.settings.height = parseInt(_ratio * _base.settings.original.height);
                _base.settings.headerSize = _ratio * _base.settings.original.headerSize
                _base.settings.headerFontSize = _ratio * _base.settings.original.headerFontSize;
                _base.settings.sectionSpacing = _ratio * _base.settings.original.sectionSpacing;

                //console.log("radio "+  _ratio );
            }

            /* _base.settings.contentWidth < _minContentWidth
            if (_orientation === zozo.classes.orientations.horizontal) {
                var _spacing = _base.settings.sectionSpacing;
                if (_spacing > 0) {
                    _base.settings.contentWidth = parseInt(_base.settings.width - (_base.sectionCount * (_base.settings.headerSize + _spacing - 1)));
                }
                else {
                    _base.settings.contentWidth = _base.settings.width - (_base.sectionCount * (_base.settings.headerSize));
                }
            }
            */
            //console.log("width: " + _base.settings.width + " / " + _minWidth + " _windowSize: " + _windowSize +" / " +_minWindowWidth + " contentWidth " + _base.settings.contentWidth + " / " + _minContentWidth);            
            console.log("accordionWidth: " + _base.settings.width + " / " + _minWidth + " windowWidth: " + _windowSize + " / " + _minWindowWidth);

            if (_windowSize <= _minWindowWidth || _base.settings.width <= _minWidth) {
                _base.settings.width = _base.settings.original.width;
                _base.settings.height = _base.settings.original.height;
                _base.settings.headerSize = _base.settings.original.headerSize;
                _base.settings.headerFontSize = _base.settings.original.headerFontSize;
                _base.settings.sectionSpacing = _base.settings.vertical.sectionSpacing;
                methods.changeOrientation(_base, zozo.classes.orientations.vertical);
            } else {
                methods.changeOrientation(_base, zozo.classes.orientations.horizontal)
            }
        },
        changeOrientation: function (_base, _orientation) {
            methods.setContentSize(_base);
            if (_orientation != _base.settings.orientation) {
                _base.settings.orientation = _orientation;
                _base.setOptions({ orientation: _orientation });
            }
            else {
                methods.showSection(_base, _base.currentsection, true);
            }
        },
        showSection: function (_base, _index, _noAnimation) {
            if ($.zozo.core.utils.isNumber(_index)) {
                //_base.$elem.find('*').stop(true, true);
                var _$section = _base.$elem.find(SECTIONS).eq(_index);
                var _duration = _base.settings.animation.duration;
                var $item = {
                    index: $.zozo.core.utils.isNumber(_index) ? _index : 0,
                    section: _$section,
                    enabled: methods.isTabDisabled(_$section) === false,
                    head: _$section.find(HEADERTAG),
                    link: _$section.find(LINK),
                    content: _$section.find("> .z-content"),
                    contentInner: _$section.find("> .z-content > .z-auto-g"),
                    contentUrl: _$section.find(LINK).data(CONTENTURL),
                    noAnimation: _noAnimation
                };

                if ($item.enabled) {
                    setTimeout(function () {
                        //_base.$container.find(".z-tabs").each(function (index, item) { $(item).data('zozoTabs').refresh(); });
                        (_base.settings.select && typeof (_base.settings.select) == typeof (Function)) && _base.$elem.trigger(SELECT, { header: $item.link[0], content: $item.contentInner[0], index: $item.index });
                        (_base.settings.activate && typeof (_base.settings.activate) == typeof (Function)) && _base.$elem.trigger(ACTIVATE, { header: $item.link[0], content: $item.contentInner[0], index: $item.index });
                        methods.saveState(_base);
                    }, (_duration + 20));


                    if ($item.contentUrl) {
                        (_base.settings.orientation === zozo.classes.orientations.vertical) ? methods.ajaxRequest(_base, $item, methods.showVertical) : methods.ajaxRequest(_base, $item, methods.showHorizontal);
                    } else {
                        (_base.settings.orientation === zozo.classes.orientations.vertical) ? methods.showVertical(_base, $item) : methods.showHorizontal(_base, $item);
                    }

                    methods.updateDotNav(_base, $item);
                    methods.addAria(_base, $item);

                    _base.currentsection = $item.index;

                    //(_base.settings.expand && typeof (_base.settings.expand) == typeof (Function)) && _base.$elem.trigger(EXPAND, { header: $item.link[0], content: $item.contentInner[0] });                
                }
            }
            return _base;
        },
        showHorizontal: function (_base, _item) {
            var _spacing = _base.settings.orientation === zozo.classes.orientations.vertical ? _base.settings.vertical.sectionSpacing : _base.settings.sectionSpacing;

            var _headerSize = _base.settings.headerSize;
            var _contentWidth = _base.settings.contentWidth;
            var _sectionSpacing = (_spacing > 0) ? _spacing - 1 : _spacing;
            var _left = 0;
            var _index = _item.index;
            var _isIe8 = $.zozo.core.browser.isIE(8);
            //if (_sectionSpacing === 0)
            //(_headerSize = _headerSize - 1)

            _base.$sections.each(function (index, item) {
                var _currentLeft;
                var _$section = $(item);
                var _$head = _$section.find(HEADERTAG);
                var _$cont = _$section.find("> .z-content");

                if (index > 0) {
                    //horizontal grouped _left = _left + _headerSize + _sectionSpacing;
                    //_left = _left + _headerSize
                    _left = _left + _headerSize + _sectionSpacing // _shift added
                }

                _currentLeft = _left;

                if (index === _index) {
                    _left = _left + _contentWidth;
                }
                
                _base.$elem.find("> *." + ACTIVECLASS + " > .z-content").parent().removeClass(zozo.classes.active);
                _base.$elem.find("> * > .z-content").eq(_index).parent().toggleClass(zozo.classes.active);

                //removed +1 from height of head
                _$head
                    .css({ "outline": "none", "height": _headerSize + 1, "line-height": (_headerSize) + PX })
                    .find("> span.z-title").css({ "height": _headerSize, "line-height": (_headerSize) + PX });
                //.find("> span.z-title").css({ "height": _headerSize, "line-height": (_headerSize - 1) + PX });

                if (_sectionSpacing > 0) {
                    _$head.find("> span.z-title").css({ "height": _headerSize + 2 });
                }

                if (_isIe8) {
                    _$head.find("> span.z-title").css({ height: (_base.settings.height) + PX })
                }


                var _setSectionWidth = _contentWidth + _headerSize + 2;
                var _setContentLeft = (_sectionSpacing === 0) ? _headerSize : _headerSize + 3;//was 2 , 3 for opera

                //ie8 +2    
                if (_isIe8 == true && _sectionSpacing > 0) {
                    _setContentLeft = _setContentLeft + 1;
                }

                if (_isIe8 == true && _sectionSpacing === 0) {
                    _setContentLeft = _setContentLeft + 1;
                }

                if (_item.noAnimation === true) {
                    _$section.stop().css({ "left": _currentLeft, "width": _setSectionWidth });
                    _$cont.css({ "left": _setContentLeft, "width": "auto", "overflow": "", display: "" });
                }
                else {
                    methods.animate(_base, _$section.stop(), null, { "left": _currentLeft, "width": _setSectionWidth });
                    methods.animate(_base, _$cont.stop(), { "left": _setContentLeft, display: "" }, { "width": "auto" }, { "overflow": "" });
                }
                // methods.addAria(_base, _$section, _$head, _$cont, index);
            });

            return _base;
        },
        showVertical: function (_base, _item) {
            if (typeof _item.noAnimation === 'undefined' || _item.noAnimation == null) {

                var _contentHeight = _base.settings.contentHeight;
                var _duration = _base.settings.animation.duration;
                var _transition = $.zozo.core.support.css.transition;

                if (_item.section.hasClass(zozo.classes.active)) {
                    setTimeout(function () { _item.section.removeClass(zozo.classes.active); }, _duration);
                    methods.animate(_base, _item.content, null, { height: "0", overflow: "" });
                }
                else {

                    if (_base.settings.expandMode === zozo.expandModes.single) {
                        _base.$sections.each(function () {
                            methods.animate(_base, $(this).removeClass(zozo.classes.active).find("> .z-content").stop(), null, { height: "0", overflow: "" });
                        });
                    }

                    /*if (_base.settings.expandMode === zozo.expandModes.single) {
                        methods.animate(_base, _base.$elem.find("> " + _base.settings.sectionTag + "." + ACTIVECLASS).removeClass(zozo.classes.active).find(CONTENTS).stop(), null, { height: "0", overflow: "" });
                    } */

                    var _maxHeight = (_contentHeight <= 0) ? methods.getElementSize(_item.content).height : _contentHeight;
                    var _post = { height: "auto" };
                    if (_contentHeight > 0) {
                        setTimeout(function () { _item.content.css({ overflow: "auto" }); }, _duration);
                        _post = null;
                    }

                    methods.animate(_base, _item.content.stop(), null, { height: _maxHeight }, _post);
                    _item.section.addClass(zozo.classes.active);


                }
            }

            return _base;
        },
        updateDotNav: function (_base, _item) {
            if (_base.settings.slider === true) {
                var _parent = _base.$elem.parent();
                _parent.find(DOTNAVACTIVEITEM).removeClass(zozo.classes.active);
                _parent.find(DOTNAVITEM).eq(_item.index).toggleClass(zozo.classes.active);
            }
        },
        addAria: function (_base, _item) {
            _base.$sections.each(function (index, item) {
                var _$section = $(item);
                var _$head = _$section.find(HEADERTAG);
                var _$cont = _$section.find("> div");
                var _expanded = (_$section.hasClass(zozo.classes.active));

                //$.zozo.core.console.log("currentsection: " + _base.currentsection + " index: " + _item.index + " expanded: " + _expanded);

                _$section.attr({
                    "aria-hidden": (!_expanded).toString(),
                    "aria-expanded": (_expanded).toString(),
                    "aria-selected": (_expanded).toString()
                });

                _$head.attr({
                    "aria-controls": _base.elemID + "-" + (index + 1),
                    "role": "tab",
                    "tabindex": "-1"
                });

                _$cont.attr({
                    "id": _base.elemID + "-" + (index + 1),
                    "role": "tabpanel",
                    "aria-hidden": (!_expanded).toString(),
                    "aria-expanded": (_expanded).toString()
                });
            });

            return _base;
        },
        ajaxRequest: function (_base, _item, _func) {
            if (!_item.section.hasClass(zozo.classes.active)) {
                var loadingIconTimeout = setTimeout(function () {
                    _item.link.find(ARROW).addClass(LOADINGCLASS);
                },0);

                var data = {};

                $.ajax({
                    type: "GET",
                    cache: (_base.settings.cacheAjax === true),
                    url: _item.contentUrl,
                    dataType: "html",
                    data: data,
                    beforeSend: function (xhr, settings) {
                        //return fire(element, 'ajax:beforeSend', [xhr, settings]);
                    },
                    error: function (xhr, status, error) {
                        if (xhr.status == 404) {
                            _item.contentInner.html("<h4 style='color:red;'>Sorry, error: 404 - the requested content could not be found.</h4>");
                        }
                        else {
                            _item.contentInner.html("<h4 style='color:red;'>An error occurred: " + status + "\nError: " + xhr + " code: " + xhr.status + "</h4>");
                        }
                        (_base.settings.error && typeof (_base.settings.error) == typeof (Function)) && _base.$elem.trigger(ERROR, xhr);
                    },
                    complete: function (xhr, status) {                       
                        //_base.$elem.trigger('ajax:complete', [xhr, status]);
                                            
                        setTimeout(function () {
                            clearTimeout(loadingIconTimeout);
                            _item.link.find(ARROW).removeClass(LOADINGCLASS);
                        }, 0);

                        (_func && typeof (_func) == typeof (Function)) && _func(_base, _item);
                    },
                    success: function (data, status, xhr) {
                        _item.contentInner.html(data);
                        (_base.settings.contentLoad && typeof (_base.settings.contentLoad) == typeof (Function)) && _base.$elem.trigger(CONTENTLOAD, { header: _item.link[0], content: _item.contentInner[0], index: _item.index });
                    }
                });
            } else {
                (_func && typeof (_func) == typeof (Function)) && _func(_base, _item);
            }
            return _base;
        },
        getFirst: function (_base) {
            return 0;
        },
        getLast: function (_base) {
            return parseInt(_base.$sections.size()) - 1;
        },
        initAutoPlay: function (_base) {
            if (_base.settings.autoplay !== false && _base.settings.autoplay != null) {
                if (_base.settings.autoplay.interval > 0) {
                    _base.stop();
                    _base.autoplayIntervalId = setInterval(function () { _base.next(_base); }, _base.settings.autoplay.interval);
                } else {
                    _base.stop();
                }
            }
            else {
                _base.stop();
            }
        },
        animate: function (_base, _elem, _pre, _prop, _post, _hidePre) {
            $.zozo.core.utils.animate(_base, _elem, _pre, _prop, _post, _hidePre);
        },
        getElementSize: function (_content) {
            var _size = { width: 0, height: 0 };
            if (_content == null || _content.length == 0) {
                return _size;
            }

            if (_content.css("height") === 0 || _content.css("height") === "0px") {

                _content.css({ "height": "auto" });

                _size.height = _content.innerHeight();
                _size.width = _content.outerWidth();

                _content.css("height", "0px");

            } else {

                var _oHeight = _content.css("height");

                //_content.css({ "height": "auto" });


                _size.height = _content.innerHeight();
                _size.width = _content.outerWidth();

                // _content.css("height", _oHeight);
            }

            return _size;
        },
        isTabDisabled: function (_section) {
            return (_section.hasClass(DISABLEDCLASS) || _section.data(DISABLED) === true);
        },
        create: function (_base, _h, _c) {
            var _header = _base.settings.headerTag;
            var _section = _base.settings.sectionTag;
            return $("<" + _section + "><" + _header + ">" + _h + "</" + _header + "><div>" + _c + "</div></" + _section + ">");
        },
        saveState: function (_base) {
            if (_base.settings.rememberState === true && $.zozo.core.support.html5_storage()) {
                var activeSections = [];
                _base.$sections.each(function (index, item) {
                    if ($(item).hasClass(ACTIVECLASS)) {
                        activeSections.push(index)
                    }
                });

                localStorage[_base.elemID + "_active"] = JSON.stringify(activeSections);
                //console.log("saveState sectons: " + JSON.parse(localStorage[_base.elemID + "_active"]).join());
            }
            return _base;
        },
        loadState: function (_base) {
            if (_base.settings.rememberState === true && $.zozo.core.support.html5_storage()) {
                var storage = localStorage[_base.elemID + "_active"];
                if (_base.settings.rememberState === true && storage) {
                    var activeSections = JSON.parse(storage);
                    //console.log("loadState sectons: " + activeSections.join());
                    if ($.isArray(activeSections) || $.zozo.core.utils.isNumber(activeSections)) {
                        {
                            _base.settings.active = activeSections
                        }
                    }
                }
            }
            return _base;
        },
        showLoading: function (_base) {
            _base.$elem.append('<span class="' + AJAXSPINNERCLASS + '"></span>');
            return _base;
        },
        hideLoading: function (_base) {
            _base.$elem.find(">." + AJAXSPINNERCLASS).remove();
            _base.$elem.removeClass("z-accordion-loading");
            return _base;
        }
    };

    ZozoAccordion.defaults = ZozoAccordion.prototype.defaults;

    $.fn.zozoAccordion = function (options) {
        return this.each(function () {
            if (undefined == $(this).data(zozo.pluginName)) {
                var zozoAccordion = new ZozoAccordion(this, options).init();
                $(this).data(zozo.pluginName, zozoAccordion);
            }
        });
    };

    window.zozo.accordion = ZozoAccordion;

    $(document).ready(function () {
        $("[data-role='z-accordion']").each(function (index, item) {
            if (!$(item).zozoAccordion())
                $(item).zozoAccordion();

            $(item).find("[data-role='z-accordion']").each(function (index, nested2) {
                if (!$(nested2).zozoAccordion())
                    $(nested2).zozoAccordion();

                $(nested2).find("[data-role='z-accordion']").each(function (index, nested3) {
                    if (!$(nested3).zozoAccordion())
                        $(nested3).zozoAccordion();

                    $(nested3).find("[data-role='z-accordion']").each(function (index, nested4) {
                        if (!$(nested4).zozoAccordion())
                            $(nested4).zozoAccordion();
                    });
                });
            });
        });
    });



})(jQuery, window, document);