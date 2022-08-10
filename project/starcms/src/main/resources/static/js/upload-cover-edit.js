$(document).ready(function () {
    // 上传图片
    Dropzone.autoDiscover = false;
    $("#dropzcover").dropzone({
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
        dictFileTooBig: "文件超过2MB，不允许上传",
        dictInvalidInputType: "只能上传图片，jpg/jpeg/gif/png/bmp",
        dictRemoveFile: "删除",
        dictCancelUpload: "取消",
        init: function () {
            let mockFile = {name: $("#cover").val(), path: $("#cover").val()};
            this.displayExistingFile(mockFile, "http://localhost/contents" + $("#cover").val(), null, null, true);
            this.files.push(mockFile);
            this.options.maxFiles = this.options.maxFiles - 1;
            this.on('success', function (files, response) {
                //文件上传成功之后的操作
                $("#cover").val(response[0].url);
                files.path = response[0].url;
            });
            this.on('removedfile', function (files, response) {
                $("#cover").val($("#cover").val().replace(files.path, ''));
                if($("#cover").val() == "")
                    this.options.maxFiles = 1;
            });
        }
    });
});