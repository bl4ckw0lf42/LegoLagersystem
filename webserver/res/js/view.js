$(document).ready(function () {

    var $selection = {
        start: {
            startDiv: $("#mainStartDiv"),
            connectBtn: $("#btnMainConnect"),
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


    $selection.start.connectBtn.click(function () {
        connectRoboter();
    });


    /**
     * connects the Roboter and Starts shit
     */
    function connectRoboter() {
        var ipServer = $selection.start.server.input.val();
        var ipRobo1 = $selection.start.robo1.input.val();
        var ipRobo2 = $selection.start.robo2.input.val();
        var ipRobo3 = $selection.start.robo3.input.val();


        $selection.start.startDiv.css("display", "none");
        $selection.main.mainDiv.css("display", "block");

        setTimeout(function () {
            addOrderListItem("Red", 4);
        },2000)
        return;

        if (ipServer == "" || ipRobo1 == "" || ipRobo2 == "" || ipRobo3 == "") return;

        var reqServer = createPostRequest(ipServer, "", JSON.stringify([ipRobo1, ipRobo2, ipRobo3]));
        var req1 = createRequest(ipRobo1, "connect");
        var req2 = createRequest(ipRobo2, "connect");
        var req3 = createRequest(ipRobo3, "connect");


        req.then(function (ev) {
            console.log("test");
        });

        req.done(function (ev) {
            console.log('Yes! Success!');
        });

        req.catch(function (ev) {
            console.log('Oh noes!');
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
     */
    function addOrderListItem (itemText, amount) {
        var $collection = $("<li></li>")
            .addClass("collection-item avatar")
            .data("amount", amount);

        var $iconColor = $("<div></div>")
            .addClass("orderIconColor")
            .css("background-color", itemText.toLowerCase());

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

