$(document).ready(function () {

    var $selection = {
        start: {
            startDiv: $("#mainStartDiv"),
            connectBtns: $("#btnMainConnect"),
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

    var allowConnection = true;


    $selection.start.connectBtns.click(function () {
        if (allowConnection) {
            $selection.start.server.connectionIcons
                .removeClass("fa-spinner fa-check-circle")
                .addClass("fa-times-circle");
            connectRoboter();
        }
    });


    /**
     * connects everything and Starts shit
     */
    function connectRoboter() {
        var ipServer = $selection.start.server.input.val();
        var ipRobo1 = $selection.start.robo1.input.val();
        var ipRobo2 = $selection.start.robo2.input.val();
        var ipRobo3 = $selection.start.robo3.input.val();

/*
        $selection.start.startDiv.css("display", "none");
        $selection.main.mainDiv.css("display", "block");

        setTimeout(function () {
            addOrderListItem("Red", 4);
        },2000)
        return;
*/
        if (ipServer == "" || ipRobo1 == "" || ipRobo2 == "" || ipRobo3 == "") return;

        $selection.start.connectBtns
            .removeClass("fa-times-circle")
            .addClass("fa-spinner");

        allowConnection = false;
        
        var reqServer = createRequest(ipServer, "connect");
        var req1 = createRequest(ipRobo1, "connect");
        var req2 = createRequest(ipRobo2, "connect");
        var req3 = createRequest(ipRobo3, "connect");
        var reqArray = [reqServer, req1, req2, req3];

        Promise.all(reqArray).then(function (beObj) {

            allowConnection = true;

            $selection.start.startDiv.css("display", "none");
            $selection.main.mainDiv.css("display", "block");

            createPostRequest(ipServer, "start", JSON.stringify([ipRobo1, ipRobo2, ipRobo3]));
        }, function (err) {
            allowConnection = true;
        });

        reqServer.then(function (beObj) {
            $selection.start.server.icon
                .removeClass("fa-spinner")
                .addClass("fa-check-circle");
        }, function (err) {
            $selection.start.server.icon
                .removeClass("fa-spinner")
                .addClass("fa-times-circle");
        });

        req1.then(function (beObj) {
            $selection.start.robo1.icon
                .removeClass("fa-spinner")
                .addClass("fa-check-circle");
        }, function (err) {
            $selection.start.robo1.icon
                .removeClass("fa-spinner")
                .addClass("fa-times-circle");
        });

        req2.then(function (beObj) {
            $selection.start.robo2.icon
                .removeClass("fa-spinner")
                .addClass("fa-check-circle");
        }, function (err) {
            $selection.start.robo2.icon
                .removeClass("fa-spinner")
                .addClass("fa-times-circle");
        });

        req3.then(function (beObj) {
            $selection.start.robo3.icon
                .removeClass("fa-spinner")
                .addClass("fa-check-circle");
        }, function (err) {
            $selection.start.robo3.icon
                .removeClass("fa-spinner")
                .addClass("fa-times-circle");
        });
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
     * @param amount
     * @param color
     */
    function addOrderListItem (itemText, amount, color) {
        var $collection = $("<li></li>")
            .addClass("collection-item avatar")
            .data("amount", amount);

        var $iconColor = $("<div></div>")
            .addClass("orderIconColor")
            .css("background-color", color || itemText.toLowerCase());

        var $amount = $("<span></span>")
            .addClass("title orderAmount")
            .html(amount + "x");

        var $text = $("<span></span>")
            .addClass("title")
            .html(itemText);

        var $iconOrder = $("<i></i>")
            .addClass("orderIcon fa fa-paper-plane");


        $collection.on("click", function (ev) {
            var $target = $(ev.currentTarget);
            var $amount = $target.find(".orderAmount");
            var amount = $target.data("amount");

            $target.data("amount", (amount - 1));

            // TODO sent order
            console.log("ORDER");

            if (amount < 2) {
                $target.remove();
            }
            else {
                $amount.html((amount - 1) + "x");
            }
        });

        $collection.append($iconColor).append($amount).append($text).append($iconOrder);
        $selection.main.order.table.append($collection);
    }
});

