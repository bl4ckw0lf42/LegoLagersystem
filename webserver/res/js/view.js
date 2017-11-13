$(document).ready(function () {

    var $selection = {
        start: {
            startDiv: $("#mainStartDiv"),
            connectBtn: $("#btnMainConnect"),
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
            mainDiv: $("#controllDiv")
        }
    };


    $btnMainConnect.click(function () {
        connectRoboter();
    });


    function connectRoboter () {
        var ipRobo1 = $selection.start.robo1.input.val();
        var ipRobo2 = $selection.start.robo2.input.val();
        var ipRobo3 = $selection.start.robo3.input.val();


        $selection.start.startDiv.css("display", "none");
        $selection.main.mainDiv.css("display", "block");
        return;

        if (ipRobo1 == "" || ipRobo2 == "" || ipRobo3 == "") return;

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

    function createRequest (url, para) {
        return $.ajax({
            url : "http://" + url + "/" + para,
            dataType : "jsonp",
            timeout : 10000
        })
    }
});

