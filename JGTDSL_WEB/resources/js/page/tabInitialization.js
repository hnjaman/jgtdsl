 $(document).ready(function () {
        $("#tabbed-nav").zozoTabs({
            theme: "crystal",
            orientation: "horizontal",
            position: "top-left",
            size: "medium",
            animation: {
                easing: "easeInOutExpo",
                duration: 400,
                effects: "slideH"
            },
            defaultTab: "tab1"
        });
    });