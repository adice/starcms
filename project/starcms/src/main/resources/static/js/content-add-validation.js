var Script = function () {

    $().ready(function () {
        // validate form on keyup and submit
        $("#contentform").validate({
            debug: false,
            rules: {
                channelId: {
                    required: true
                },
                title: {
                    required: true
                },
                pictures: {
                    required: true
                },
                attachments: {
                    required: true
                }
            },
            messages: {
                channelId: {
                    required: "<font color='red'>请选择栏目</font>"
                },
                title: {
                    required: "<font color='red'>请填写书籍名称</font>"
                },
                pictures: {
                    required: "<font color='red'>请上传书影</font>"
                },
                attachments: {
                    required: "<font color='red'>请上传书籍</font>"
                }
            }
        });
    });
}();
