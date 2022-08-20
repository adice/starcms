var Script = function () {

    $().ready(function () {
        $('#cover').on("focusin", function() {
            $(this).prop('readonly', true);
        });

        $('#cover').on("focusout", function() {
            $(this).prop('readonly', false);
        });
        $('#attachments').on("focusin", function() {
            $(this).prop('readonly', true);
        });

        $('#attachments').on("focusout", function() {
            $(this).prop('readonly', false);
        });
        $('#pic').on("focusin", function() {
            $(this).prop('readonly', true);
        });

        $('#pic').on("focusout", function() {
            $(this).prop('readonly', false);
        });
        $('#path').on("focusin", function() {
            $(this).prop('readonly', true);
        });

        $('#path').on("focusout", function() {
            $(this).prop('readonly', false);
        });
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
                cover: {
                    required: true
                },
                attachments: {
                    required: true
                },
                pic: {
                    required: true
                },
                path: {
                    required: true
                },
                journalId: {
                    required: true
                },
                newsTime: {
                    required: true
                },
                section: {
                    required: true
                },
                position: {
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
                cover: {
                    required: "<font color='red'>请上传封面</font>"
                },
                attachments: {
                    required: "<font color='red'>请上传书籍</font>"
                },
                pic: {
                    required: "<font color='red'>请上传图片</font>"
                },
                path: {
                    required: "<font color='red'>请上传内容</font>"
                },
                journalId: {
                    required: "<font color='red'>请选择报刊</font>"
                },
                newsTime: {
                    required: "<font color='red'>请填写时间</font>"
                },
                section: {
                    required: "<font color='red'>请填写板块</font>"
                },
                position: {
                    required: "<font color='red'>请设置新闻位置</font>"
                }
            }
        });
    });
}();
