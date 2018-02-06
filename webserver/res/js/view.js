$(document).ready(function () {

    var iconClass = {
        connected: "fa-check-circle",
        error: "fa-times-circle",
        load: "fa-spinner"
    };

    var $selection = {
        start: {
            startDiv: $("#mainStartDiv"),
            connectBtn: $("#btnMainConnect"),
            connectionIcons: $(".connectionIcons"),
            server: {
                icon: $("#iconServerConnection"),
                input: $("#inputServerConnection")
            },
            robo1: {
                icon: $("#iconRoboter1"),
                input: $("#input_robo_1")
            },
            robo2: {
                icon: $("#iconRoboter2"),
                input: $("#input_robo_2")
            },
            robo3: {
                icon: $("#iconRoboter3"),
                input: $("#input_robo_3")
            }
        },
        main: {
            mainDiv: $("#controllDiv"),
            roboterHub: {
                div: $("#roboterHubDiv"),
                robo1: {
                    action: $("#roboter1Hub")
                },
                robo2: {
                    action: $("#roboter2Hub")
                },
                robo3: {
                    action: $("#roboter3Hub")
                }
            },
            order: {
                div: $("#orderDIv"),
                table: $("#orderTable")
            }
        }
    };

    var SERVERIP;
    var ROBO1;
    var ROBO2;
    var ROBO3;
    var allowConnection = true;


    $selection.start.connectBtn.click(function () {
        if (allowConnection) {
            addIconClass($selection.start.connectionIcons, iconClass.error);
            startConnectTab();
        }
    });


    /**
     * init function for Start Tab
     * connects everything and Starts shit
     */
    function startConnectTab() {
        SERVERIP = $selection.start.server.input.val();
        ROBO1 = $selection.start.robo1.input.val();
        ROBO2 = $selection.start.robo2.input.val();
        ROBO3 = $selection.start.robo3.input.val();

/*
        showMainTab();
        setTimeout(function () {
            addOrderListItem("Red", 4);
        },2000);
        return;
*/

        if (SERVERIP === "" || ROBO1 === "" || ROBO2 === "" || ROBO3 === "") return;

        addIconClass($selection.start.connectionIcons, iconClass.load);

        allowConnect(false);
        
        var reqServer = createRequest(SERVERIP, "connect");
        var req1 = createRequest(ROBO1, "connect");
        var req2 = createRequest(ROBO2, "connect");
        var req3 = createRequest(ROBO3, "connect");
        var reqArray = [reqServer, req1, req2, req3];

        Promise.all(reqArray).then(function (beObj) {
            createPostRequest(SERVERIP, "start", JSON.stringify([ROBO1, ROBO2, ROBO3]));
            allowConnect(true);
            startMainTab();
        }, function (err) {
            allowConnect(true);
        });

        reqServer.then(function (beObj) {
            addIconClass($selection.start.server.icon, iconClass.connected);
        }, function (err) {
            addIconClass($selection.start.server.icon, iconClass.error);
        });

        req1.then(function (beObj) {
            addIconClass($selection.start.robo1.icon, iconClass.connected);
        }, function (err) {
            addIconClass($selection.start.robo1.icon, iconClass.error);
        });

        req2.then(function (beObj) {
            addIconClass($selection.start.robo2.icon, iconClass.connected);
        }, function (err) {
            addIconClass($selection.start.robo2.icon, iconClass.error);
        });

        req3.then(function (beObj) {
            addIconClass($selection.start.robo3.icon, iconClass.connected);
        }, function (err) {
            addIconClass($selection.start.robo3.icon, iconClass.error);
        });
    }

    /**
     * init function for Main Tab
     */
    function startMainTab() {
        showMainTab();
        startAutoDbRefresh();
    }

    /**
     * starts the automatic Database Table refresh
     */
    function startAutoDbRefresh() {
        setInterval(refreshOrderList, 5000);
    }

    /**
     * refreshes Order List
     */
    function refreshOrderList () {
        createRequest(SERVERIP, "getStock").then(function (beObj) {
            var table = $("#orderTable");
            table.find(".slot").remove();
            var artikels = JSON.parse(beObj);
            for (var key in artikels) {
                table.append($("<div>").addClass("slot").text("Slot "+key+": " + artikels[key]));
            }
        }, function (err) {
            console.log(err);
        });
    }

    /**
     * disable / enable Connection Btn
     * @param allow
     */
    function allowConnect (allow) {
        if (allow) {
            allowConnection = true;
            $selection.start.connectBtn.removeClass("cursorNotAllowed");
        }
        else {
            allowConnection = false;
            $selection.start.connectBtn.addClass("cursorNotAllowed");
        }
    }

    /**
     * removes all Icon Class and adds new one
     * @param $obj
     * @param iClass
     */
    function addIconClass ($obj, iClass) {
        $obj
            .removeClass(iconClass.connected + " " + iconClass.load + " " + iconClass.error)
            .addClass(iClass);
    }

    /**
     * shows Main Tab
     */
    function showConnectTab () {
        $selection.start.startDiv.css("display", "block");
        $selection.main.mainDiv.css("display", "none");
    }

    /**
     * shows Connect Tab
     */
    function showMainTab () {
        $selection.start.startDiv.css("display", "none");
        $selection.main.mainDiv.css("display", "block");
    }

    /**
     * creates and sends Request
     * @param url
     * @param para
     * @returns {*}
     */
    function createRequest(url, para) {
        return fetch("http://" + url + "/" + para)
            .then(function (response) {
                return response.text();
            });
    }

    /**
     * creates and sends Post Request
     * @param url
     * @param para
     * @param body
     * @returns {*}
     */
    function createPostRequest(url, para, body) {
        return fetch("http://" + url + "/" + para, {
            method: "POST",
            body: body
        });
    }

    /**
     * adds an Item to the Order List
     * @param itemText
     * @param slot
     * @param color
     */
    function addOrderListItem (itemText, slot, color) {
        var $collection = $("<li></li>")
            .addClass("collection-item avatar")
            .data("Slot", slot);

        var $iconColor = $("<div></div>")
            .addClass("orderIconColor")
            .css("background-color", color || itemText.toLowerCase());

        var $amount = $("<span></span>")
            .addClass("title orderAmount")
            .html("Slot: " + slot + " - ");

        var $text = $("<span></span>")
            .addClass("title")
            .html(itemText);

        var $iconOrder = $("<i></i>")
            .addClass("orderIcon fa fa-paper-plane");


        $collection.on("click", function (ev) {
            var $target = $(ev.currentTarget);

            // TODO send order
            console.log("ORDER");

            $target.remove();
        });

        $collection.append($iconColor).append($amount).append($text).append($iconOrder);
        $selection.main.order.table.append($collection);
    }

    /**
     * clears Order List
     */
    function clearOrderList () {
        $(".collection-item").remove();
    }
});

