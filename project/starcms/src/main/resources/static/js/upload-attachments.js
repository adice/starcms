$(document).ready(function () {
    // 上传附件
    Dropzone.autoDiscover = false;
    $("#dropzattachments").dropzone({
        url: "/backstage/uploadpdf",
        paramName: "uploadfiles",
        maxFilesize: 20,
        createImageThumbnails: false,
        addRemoveLinks: true,
        acceptedFiles: ".pdf",
        autoProcessQueue: true,
        dictDefaultMessage: "点击或拖入需要上传的文件",
        dictFileTooBig: "文件超过20MB，不允许上传",
        dictInvalidFileType: "只能上传 pdf",
        dictRemoveFile: "删除",
        dictCancelUpload: "取消",
        init: function () {
            this.on('success', function (files, response) {
                //文件上传成功之后的操作
                if (response[0].code == 1) {
                    $("#attachments").val($("#attachments").val() + response[0].url + ",");
                    files.path = response[0].url;
                    $("html").niceScroll().resize();
                } else {
                    this.find('.dz-error-message').children().text(response[0].msg);
                    this.find('.dz-error-message').css("display", "block");
                    this.find('.dz-error-message').css("opacity", "1");
                }
            });
            this.on('removedfile', function (files, response) {
                $("#attachments").val($("#attachments").val().replace(files.path + ',', ''));
                $("html").niceScroll().resize();
            });
        }
    });
    
});