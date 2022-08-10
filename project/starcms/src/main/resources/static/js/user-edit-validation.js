var Script = function () {

    $().ready(function () {
        // validate form on keyup and submit
        $("#userform").validate({
            debug: false,
            rules: {
                realName: {
                    required: true,
                    minlength: 2
                }
            },
            messages: {
                realName: {
                    required: "<font color='red'>请输入真实姓名</font>",
                    minlength: "<font color='red'>真实姓名不能少于2个字符</font>"
                }
            }
        });
    });
}();
