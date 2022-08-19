var Script = function () {

    $().ready(function () {
        $('#cover').on("focusin", function() {
            $(this).prop('readonly', true);
        });

        $('#cover').on("focusout", function() {
            $(this).prop('readonly', false);
        });
        // validate form on keyup and submit
        $("#journalform").validate({
            debug: false,
            rules: {
                title: {
                    required: true
                },
                cover: {
                    required: true
                }
            },
            messages: {
                title: {
                    required: "<font color='red'>请填写报刊名称</font>"
                },
                cover: {
                    required: "<font color='red'>请上传内容</font>"
                }
            }
        });
    });
}();
