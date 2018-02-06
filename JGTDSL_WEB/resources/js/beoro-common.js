$(document).ready(function() {
	//* boxes actions

    beoro_boxes = {
        show_hide: function() {
            $('.w-box.hideable').each(function() {
                $this = $(this)
                if( !$this.children('.w-box-header').children('.icon-plus,.icon-minus').length ) {
                    if($this.children('.w-box-content').hasClass('content-hide')) {
                        $this.children('.w-box-header').prepend('<i class="icon-plus icon-white" />')
                    } else {
                        $this.children('.w-box-header').prepend('<i class="icon-minus icon-white" />')
                    }
                }
            });
            $('.w-box-header .icon-plus,.w-box-header .icon-minus').click(function() {

                var this_box_content = $(this).closest('.w-box').find('.w-box-content')
                var box_height = this_box_content.actual('height');
                this_box_content.height(box_height-20);
                $(this).toggleClass('icon-plus icon-minus');
                this_box_content.slideToggle(400, 'easeOutCubic',  function() { this_box_content.css('height','') });
                
            });
        }
    };
    
    beoro_boxes.show_hide();
    
});