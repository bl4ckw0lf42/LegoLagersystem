$(document).ready(function () {

    var $mainDiv = $("#mainStartDiv");
    var $btnMainConnect = $("#btnMainConnect");
    var $iconRoboter1 = $("#iconRoboter1");
    var $iconRoboter2 = $("#iconRoboter2");
    var $iconRoboter3 = $("#iconRoboter3");
    var $inputRoboter1 = $("#input_robo_1");
    var $inputRoboter2 = $("#input_robo_2");
    var $inputRoboter3 = $("#input_robo_3");

    var $controlLDiv = $("#controllDiv");


    $btnMainConnect.click(function () {
        connectRoboter();
    });


    function connectRoboter() {
        var ipRobo1 = $inputRoboter1.val();
        var ipRobo2 = $inputRoboter2.val();
        var ipRobo3 = $inputRoboter3.val();


        $mainDiv.css("display", "none");
        $controlLDiv.css("display", "block");
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

    function createRequest(url, para) {
        return fetch("http://" + url + "/" + para)
            .then(function (response) {
                return response.text();
            });
    }
});

