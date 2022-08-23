$(document).ready(function () {
    // 上传内容
    Dropzone.autoDiscover = false;
    $("#dropzpath").dropzone({
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
        dictInvalidInputType: "只能上传图片，jpg/jpeg/gif/png/bmp",
        dictRemoveFile: "删除",
        dictCancelUpload: "取消",
        init: function () {
            this.on('success', function (files, response) {
                //文件上传成功之后的操作
                $("#path").val($("#path").val() + response[0].url);
                files.path = response[0].url;
                $("#pathpreview").attr("src", "/contents" + response[0].url);
                $("html").niceScroll().resize();
            });
            this.on('removedfile', function (files, response) {
                $("#path").val($("#path").val().replace(files.path, ''));
                $("html").niceScroll().resize();
            });
        }
    });
    
});