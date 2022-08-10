var Script = function () {

    $().ready(function () {
        // validate form on keyup and submit
        $("#userform").validate({
            debug: false,
            rules: {
                name: {
                    required: true,
                    minlength: 4
                },
                realName: {
                    required: true,
                    minlength: 2
                },
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
                name: {
                    required: "<font color='red'>请输入账号</font>",
                    minlength: "<font color='red'>账号不能少于4个字符</font>"
                },
                realName: {
                    required: "<font color='red'>请输入真实姓名</font>",
                    minlength: "<font color='red'>真实姓名不能少于2个字符</font>"
                },
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
