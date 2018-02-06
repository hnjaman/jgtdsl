/**
 *
 * Author : MD. Sheik Mohideen K.H
 *
 *
 *
 */
;
(function ($, window, undefined) {


    $.nsWindow = {

        name: 'nsWindow',

        version: '1.0',

        window: null,

        options: null,

        bind: function (type, callback) {
            $(document).bind(type, callback);
            return this;
        },

        unbind: function (type) {
            $(document).unbind(type);
            return this;
        },

        open: function (options) {

            this.options = $.extend(true, {
                title: 'Untitled',
                top: 0,
                left: 0,
                width: 300,
                height: 300,
                useOverlay: true,
                animate: true,
                showControlBar: true,
                titleBarHeight: 0,
                closable: true,
                movable: false,
                resizable: false,
                overlay: {
                    background: '#000',
                    opacity: .60,
                    zIndex: 999999,
                    clickToClose: true,
                    destroyOnClose: false
                },
                dataUrl: null,
                data: null,
                theme: 'yellow',
                loadingMessage: 'Loading...'
            }, options);

            var opt = this.options, self = this;

            this.bind($.nsWindowEvent.onOpen, _openAndCloseHandler).bind($.nsWindowEvent.onClose, _openAndCloseHandler);

            var body = $(document.body);

            if (body.find('> .nswindowOverlay, > .nswindowContainer').length > 0) {
                body.find('> .nswindowOverlay, > .nswindowContainer').show();
                this.window = $('.nswindowContainer #nswindow').get(0);
            } else {
                var pg = _createOverlay(opt, body);
                this.window = $('<div>').attr('id', 'nswindow').get(0);
                pg.append(this.window);
            }

            var t = '';
            if (opt.showControlBar == true) {
                t = '<div id="ctrlbar" class="controlbar"><div class="title">' + opt.title + '</div>';
                if (_isEmpty(opt.closable) || opt.closable == true) {
                    t += '<a class="close" title="close" href="javascript:void(0)">Close</a>';
                }
                t += '</div>';
            }
            t += '<div id="nswindowContent">&nbsp;</div>';

            $(this.window).addClass(opt.theme).html(t);

            var w = parseInt($(this.window).css('width')), h = parseInt($(this.window).css('height'))

            // Get width & height from css.
            this.options.width = (_isEmpty(options.width) && w > 0) ? w : this.options.width;
            this.options.height = (_isEmpty(options.height) && h > 0) ? h : this.options.height;
            //alert(w);

            if (opt.showControlBar == true) {
                var ctrlbar = $('.controlbar', this.window);
                if (opt.titleBarHeight == 0) {
                    opt.titleBarHeight = parseInt(ctrlbar.css('height'));
                } else {
                    ctrlbar.css('height', opt.titleBarHeight + 'px');
                }

                $('#nswindowContent', this.window).css('top', (opt.titleBarHeight + 1) + 'px');

                $('.title', this.window).css({
                    marginTop: (opt.titleBarHeight / 2 - 11) + 'px'
                })
            }

            $('.close', this.window).click(function () {
                self.close();
                return false;
            });
                        
            _trigger($.nsWindowEvent.onOpen, this);
            return this;
        },

        close: function (option) {
            var me = this;
            var o = $.extend({
                delay: 0
            }, option);

            if (o.delay > 0) {
                setTimeout(_t, o.delay);
            } else {
                _t();
            }

            function _t() {
                _trigger($.nsWindowEvent.onClose, me);
            }

        },

        showLoading: function (msg, clearContainer) {
            msg = _isEmpty(msg) ? this.options.loadingMessage : msg;
            clearContainer = _isEmpty(clearContainer) ? false : clearContainer;
            _showLoading(this.window, msg, clearContainer);
        }
    }

    //-------------------------------------------------------------------------------------//

    $.nsWindowEvent = {
        onOpen: $.nsWindow.name + 'OnOpen',
        onClose: $.nsWindow.name + 'OnClose'
    }

    //-------------------------------------------------------------------------------------//

    function _trigger(type, context) {
        $(document).trigger(jQuery.Event(type, {
            context: context,
            window: context.window,
            dataContainer: $('#nswindowContent', context.window).get(0),
            options: context.options
        }));
    }

    //-------------------------------------------------------------------------------------//

    function _createOverlay(opt, body) {
        var overlay = opt.overlay;
        if (opt.useOverlay == true) {
            $('<div>').addClass('nswindowOverlay').html('&nbsp;').css({
                opacity: overlay.opacity,
                filter: 'alpha(opacity=' + (overlay.opacity * 100) + ')',
                background: overlay.background,
                zIndex: overlay.zIndex
            }).appendTo(body);
        }

        var pg = $('<div>');
        if (overlay.clickToClose) {
            pg.click(function (event) {
                if (!event.isDefaultPrevented() && event.target == this) {
                    $.nsWindow.close();
                }
            })
        }

        pg.addClass('nswindowContainer').css({
            zIndex: overlay.zIndex + 10
        });

        body.append(pg);

        return pg;
    }

    //-------------------------------------------------------------------------------------//

    function _closeWindow(event) {
        var body = $(document.body);

        if (event.options.overlay.destroyOnClose) {
            body.find('.nswindowOverlay,.nswindowContainer').remove();
        } else {
            $(event.window).removeAttr('style').removeAttr('class').html('');
            body.find('.nswindowOverlay,.nswindowContainer').hide();
        }
    }

    //-------------------------------------------------------------------------------------//

    function _showLoading(win, msg, clearContainer) {
        var w = $('#nswindowContent', win);
        $('.loader', win).remove();
        if (clearContainer == true) {
            w.html('')
        }
        w.append('<div class="loader" align="center">' + msg + '</div>');
    }

    //-------------------------------------------------------------------------------------//

    function _openAndCloseHandler(event) {
        if (typeof event != 'undefined' && event.isDefaultPrevented()) {
            return;
        }

        var opt = event.options, win = $(event.window);

        var tw = opt.width / 8, th = opt.height / 8;

        opt.left = opt.left == 0 ? $(window).width() / 2 - opt.width / 2 : opt.left;
        opt.top = opt.top == 0 ? $(window).height() / 2 - opt.height / 2 : opt.top;

        var css1 = {
            width: tw + 'px',
            height: th + 'px',
            left: (opt.left + tw * 3.5) + 'px',
            top: (opt.top + th * 3.5) + 'px',
            opacity: 0.0
        }, css2 = {
            left: opt.left + 'px',
            top: opt.top + 'px',
            width: opt.width + 'px',
            height: opt.height + 'px',
            opacity: 1.0
        }

        if (event.type == $.nsWindowEvent.onOpen) {
            if (opt.animate == true) {
                win.css(css1).stop().animate(css2, {
                    duration: 300,
                    easing: 'easeOutBack',
                    complete: function (e) {
                        _configWindow(event);
                    }
                });
            } else {
                win.css(css2);
                _configWindow(event);
            }

        } else {
            if (opt.animate == true) {
                win.stop().animate(css1, {
                    duration: 300,
                    easing: 'easeOutBack',
                    complete: function () {
                        opt.left = 0;
                        opt.top = 0;
                        _closeWindow(event);
                    }
                });
            } else {
                _closeWindow(event);
            }
            $.nsWindow.unbind($.nsWindowEvent.onOpen).unbind($.nsWindowEvent.onClose);
        }
    }

    //-------------------------------------------------------------------------------------//

    function _configWindow(event) {
        var opt = event.options, win = $(event.window);

        event.context.showLoading(opt.loadingMessage);

        /*
         if ($.browser.msie) {
         $(event.dataContainer).css({
         width: (opt.width - 12) + 'px',
         height: (opt.height - opt.titleBarHeight - 7) + 'px'
         })
         }
         */

        if (opt.movable == true) {
            win.draggable({
                handle: ".controlbar"
            });
        }
        if (opt.resizable == true) {
            win.resizable();
        }

        if (_isEmpty(opt.dataUrl)) {
            return;
        }
        
        /*
         * Commentd By Ifti
        $.ajax({
            url: opt.dataUrl,
            dataType: 'html',
            type: "POST",
			cache: false,
            success: function (data) {
                event.dataContainer.innerHTML = data;
            }
        })
        */
        $("#nswindowContent").load(opt.dataUrl);
        
    }

    //-------------------------------------------------------------------------------------//

    function _isEmpty(value) {
        return typeof value == 'undefined' || value == null || value == 'null' || $.trim(value) == ''
    }

    //-------------------------------------------------------------------------------------//

    function _log(msg) {
        //console.log(msg);
    }

})(jQuery, window);


/**
 *  JQuery Easing Function
 *
 */

//-------------------------------------------------------------------------------------//

jQuery.easing.jswing=jQuery.easing.swing;jQuery.extend(jQuery.easing,{def:"easeOutQuad",swing:function(e,f,a,h,g){return jQuery.easing[jQuery.easing.def](e,f,a,h,g)},easeInQuad:function(e,f,a,h,g){return h*(f/=g)*f+a},easeOutQuad:function(e,f,a,h,g){return -h*(f/=g)*(f-2)+a},easeInOutQuad:function(e,f,a,h,g){if((f/=g/2)<1){return h/2*f*f+a}return -h/2*((--f)*(f-2)-1)+a},easeInCubic:function(e,f,a,h,g){return h*(f/=g)*f*f+a},easeOutCubic:function(e,f,a,h,g){return h*((f=f/g-1)*f*f+1)+a},easeInOutCubic:function(e,f,a,h,g){if((f/=g/2)<1){return h/2*f*f*f+a}return h/2*((f-=2)*f*f+2)+a},easeInQuart:function(e,f,a,h,g){return h*(f/=g)*f*f*f+a},easeOutQuart:function(e,f,a,h,g){return -h*((f=f/g-1)*f*f*f-1)+a},easeInOutQuart:function(e,f,a,h,g){if((f/=g/2)<1){return h/2*f*f*f*f+a}return -h/2*((f-=2)*f*f*f-2)+a},easeInQuint:function(e,f,a,h,g){return h*(f/=g)*f*f*f*f+a},easeOutQuint:function(e,f,a,h,g){return h*((f=f/g-1)*f*f*f*f+1)+a},easeInOutQuint:function(e,f,a,h,g){if((f/=g/2)<1){return h/2*f*f*f*f*f+a}return h/2*((f-=2)*f*f*f*f+2)+a},easeInSine:function(e,f,a,h,g){return -h*Math.cos(f/g*(Math.PI/2))+h+a},easeOutSine:function(e,f,a,h,g){return h*Math.sin(f/g*(Math.PI/2))+a},easeInOutSine:function(e,f,a,h,g){return -h/2*(Math.cos(Math.PI*f/g)-1)+a},easeInExpo:function(e,f,a,h,g){return(f==0)?a:h*Math.pow(2,10*(f/g-1))+a},easeOutExpo:function(e,f,a,h,g){return(f==g)?a+h:h*(-Math.pow(2,-10*f/g)+1)+a},easeInOutExpo:function(e,f,a,h,g){if(f==0){return a}if(f==g){return a+h}if((f/=g/2)<1){return h/2*Math.pow(2,10*(f-1))+a}return h/2*(-Math.pow(2,-10*--f)+2)+a},easeInCirc:function(e,f,a,h,g){return -h*(Math.sqrt(1-(f/=g)*f)-1)+a},easeOutCirc:function(e,f,a,h,g){return h*Math.sqrt(1-(f=f/g-1)*f)+a},easeInOutCirc:function(e,f,a,h,g){if((f/=g/2)<1){return -h/2*(Math.sqrt(1-f*f)-1)+a}return h/2*(Math.sqrt(1-(f-=2)*f)+1)+a},easeInElastic:function(f,h,e,l,k){var i=1.70158;var j=0;var g=l;if(h==0){return e}if((h/=k)==1){return e+l}if(!j){j=k*0.3}if(g<Math.abs(l)){g=l;var i=j/4}else{var i=j/(2*Math.PI)*Math.asin(l/g)}return -(g*Math.pow(2,10*(h-=1))*Math.sin((h*k-i)*(2*Math.PI)/j))+e},easeOutElastic:function(f,h,e,l,k){var i=1.70158;var j=0;var g=l;if(h==0){return e}if((h/=k)==1){return e+l}if(!j){j=k*0.3}if(g<Math.abs(l)){g=l;var i=j/4}else{var i=j/(2*Math.PI)*Math.asin(l/g)}return g*Math.pow(2,-10*h)*Math.sin((h*k-i)*(2*Math.PI)/j)+l+e},easeInOutElastic:function(f,h,e,l,k){var i=1.70158;var j=0;var g=l;if(h==0){return e}if((h/=k/2)==2){return e+l}if(!j){j=k*(0.3*1.5)}if(g<Math.abs(l)){g=l;var i=j/4}else{var i=j/(2*Math.PI)*Math.asin(l/g)}if(h<1){return -0.5*(g*Math.pow(2,10*(h-=1))*Math.sin((h*k-i)*(2*Math.PI)/j))+e}return g*Math.pow(2,-10*(h-=1))*Math.sin((h*k-i)*(2*Math.PI)/j)*0.5+l+e},easeInBack:function(e,f,a,i,h,g){if(g==undefined){g=1.70158}return i*(f/=h)*f*((g+1)*f-g)+a},easeOutBack:function(e,f,a,i,h,g){if(g==undefined){g=1.70158}return i*((f=f/h-1)*f*((g+1)*f+g)+1)+a},easeInOutBack:function(e,f,a,i,h,g){if(g==undefined){g=1.70158}if((f/=h/2)<1){return i/2*(f*f*(((g*=(1.525))+1)*f-g))+a}return i/2*((f-=2)*f*(((g*=(1.525))+1)*f+g)+2)+a},easeInBounce:function(e,f,a,h,g){return h-jQuery.easing.easeOutBounce(e,g-f,0,h,g)+a},easeOutBounce:function(e,f,a,h,g){if((f/=g)<(1/2.75)){return h*(7.5625*f*f)+a}else{if(f<(2/2.75)){return h*(7.5625*(f-=(1.5/2.75))*f+0.75)+a}else{if(f<(2.5/2.75)){return h*(7.5625*(f-=(2.25/2.75))*f+0.9375)+a}else{return h*(7.5625*(f-=(2.625/2.75))*f+0.984375)+a}}}},easeInOutBounce:function(e,f,a,h,g){if(f<g/2){return jQuery.easing.easeInBounce(e,f*2,0,h,g)*0.5+a}return jQuery.easing.easeOutBounce(e,f*2-g,0,h,g)*0.5+h*0.5+a}});

//-------------------------------------------------------------------------------------//