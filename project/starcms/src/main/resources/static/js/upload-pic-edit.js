$(document).ready(function () {
    // 上传图片
    Dropzone.autoDiscover = false;
    $("#dropzpic").dropzone({
        url: "/backstage/uploadpic",
        paramName: "uploadfiles",
        maxFiles: 1,
        maxFilesize: 20,
        createImageThumbnails: true,
        thumbnailWidth: 120,
        addRemoveLinks: true,
        acceptedFiles: ".jpg,.jpeg,.gif,.png,.bmp",
        autoProcessQueue: true,
        dictDefaultMessage: "点击或拖入需要上传的文件",
        dictMaxFilesExceeded: "只能上传一个文件，请先删除旧文件",
        dictFileTooBig: "文件超过20MB，不允许上传",
        dictInvalidFileType: "只能上传图片，jpg/jpeg/gif/png/bmp",
        dictRemoveFile: "删除",
        dictCancelUpload: "取消",
        init: function () {
            let mockFile = {name: $("#pic").val(), path: $("#pic").val()};
            this.displayExistingFile(mockFile, "http://localhost/contents" + $("#pic").val(), null, null, true);
            this.files.push(mockFile);
            this.options.maxFiles = this.options.maxFiles - 1;
            this.on('success', function (files, response) {
                if (response[0].code == 1) {
                    //文件上传成功之后的操作
                    $("#pic").val(response[0].url);
                    files.path = response[0].url;
                    $("html").niceScroll().resize();
                } else {
                    this.find('.dz-error-message').children().text(response[0].msg);
                    this.find('.dz-error-message').css("display", "block");
                    this.find('.dz-error-message').css("opacity", "1");
                }
            });
            this.on('removedfile', function (files, response) {
                $("#pic").val($("#pic").val().replace(files.path, ''));
                $("html").niceScroll().resize();
                if($("#pic").val() == "")
                    this.options.maxFiles = 1;
            });
        }
    });
});