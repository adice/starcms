var Script = function () {

    $().ready(function () {
        // validate form on keyup and submit
        $("#userform").validate({
            debug: false,
            rules: {
                password: {
                    required: true,
                    minlength: 6
                },
                password1: {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                }
            },
            messages: {
                password: {
                    required: "<font color='red'>请输入密码</font>",
                    minlength: "<font color='red'>密码不能少于6位</font>"
                },
                password1: {
                    required: "<font color='red'>请输入确认密码</font>",
                    minlength: "<font color='red'>确认密码不能少于6位</font>",
                    equalTo: "<font color='red'>密码和确认密码不一致</font>"
                }
            }
        });
    });
}();
