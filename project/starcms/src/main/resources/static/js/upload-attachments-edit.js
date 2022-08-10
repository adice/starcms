$(document).ready(function () {
    // 上传附件
    Dropzone.autoDiscover = false;
    $("#dropzattachments").dropzone({
        url: "/backstage/uploadfile",
        paramName: "uploadfiles",
        maxFilesize: 20,
        createImageThumbnails: false,
        addRemoveLinks: true,
        acceptedFiles: ".pdf,.zip,.rar",
        autoProcessQueue: true,
        dictDefaultMessage: "点击或拖入需要上传的文件",
        dictMaxFilesExceeded: "只能上传一个文件，请先删除旧文件",
        dictFileTooBig: "文件超过20MB，不允许上传",
        dictInvalidInputType: "只能上传 pdf/zip/rar",
        dictRemoveFile: "删除",
        dictCancelUpload: "取消",
        init: function () {
            var as = $("#attachments").val().split(",");
            for(var i = 0; i < as.length; i++) {
                if (as[i] != "") {
                    let mockFile = {name: as[i], path: as[i]};
                    this.displayExistingFile(mockFile, "http://localhost/contents" + as[i], null, null, false);
                    this.files.push(mockFile);
                    $('.dz-image').html("<img data-dz-thumbnail=''>");
                    $('.dz-preview').addClass("dz-file-preview");
                }
            }
            this.on('success', function (files, response) {
                //文件上传成功之后的操作
                $("#attachments").val($("#attachments").val() + response[0].url + ",");
                files.path = response[0].url;
            });
            this.on('removedfile', function (files, response) {
                $("#attachments").val($("#attachments").val().replace(files.path + ',', ''));
            });
        }
    });
    
});