$(document).ready(function () {
    // 上传附件
    Dropzone.autoDiscover = false;
    $("#dropzpath").dropzone({
        url: "/backstage/uploadfile",
        paramName: "uploadfiles",
        maxFiles: 1,
        maxFilesize: 20,
        createImageThumbnails: false,
        addRemoveLinks: true,
        acceptedFiles: ".zip,.rar",
        autoProcessQueue: true,
        dictDefaultMessage: "点击或拖入需要上传的文件",
        dictFileTooBig: "文件超过20MB，不允许上传",
        dictInvalidInputType: "只能上传3D模型文件",
        dictRemoveFile: "删除",
        dictCancelUpload: "取消",
        init: function () {
            var as = $("#path").val().split(",");
            for(var i = 0; i < as.length; i++) {
                if (as[i] != "") {
                    let mockFile = {name: as[i], path: as[i]};
                    this.displayExistingFile(mockFile, "http://localhost/contents" + as[i], null, null, false);
                    this.files.push(mockFile);
                    $('.dz-image').html("<img data-dz-thumbnail=''>");
                    $('.dz-preview').addClass("dz-file-preview");
                    $('.dz-size').children("span").hide();
                }
            }
            this.on('success', function (files, response) {
                //文件上传成功之后的操作
                $("#path").val($("#path").val() + response[0].url + ",");
                files.path = response[0].url;
            });
            this.on('removedfile', function (files, response) {
                $("#path").val($("#path").val().replace(files.path + ',', ''));
            });
        }
    });
    
});