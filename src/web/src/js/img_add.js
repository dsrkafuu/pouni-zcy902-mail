$(function () {
    $("#file").change(function () {

        var images = $("#file")[0].files;
        if(images.length == 0){
            alert("请选择图片上传");
            return;
        }
        var fileTypes = [".jpg", ".png"];  //支持的图片格式
        var filePath = $("#file")[0].value;
        if (filePath) {
            var filePic = images[0];            //选择的文件内容--图片
            var fileType = filePath.slice(filePath.indexOf("."));   //选择文件的格式
            var fileSize = filePic.size;            //选择文件的大
            if (fileTypes.indexOf(fileType) == -1) {  //判断文件格式是否符合要求
                alert("仅支持jpg与png图片！");
                return;
            }
            if (fileSize > 1024 * 1024) {
                alert("文件大小不能超过1M！");
                return;
            }
            var reader = new FileReader();
            reader.readAsDataURL(filePic);
            reader.onload = function (e) {
                var data = e.target.result;
                //加载图片获取图片真实宽度和高度
                var image = new Image();
                image.src = data;
                image.onload = function () {
                    var width = image.width;
                    var height = image.height;
                    if (width <= 500 && height <= 500) {  //判断文件像素
                        //上传图片
                        var params = new FormData();
                        params.append('file',images[0]);
                        $.ajax({
                            type:"POST",
                            url:host+"/item/single",
                            data:params,
                            xhrFields:{withCredentials:true},
                            cache: false,
                            processData: false,
                            contentType : false,
                            success:function (data) {
                                if(data.status == "success") {
                                    $('#imgUrl').val(data.data);
                                    $('#upload_img').attr("src",data.data);
                                }else {
                                    alert("上传失败，原因是"+data.data.errMsg);
                                }
                            },
                            error:function (data) {
                                alert("上传失败，原因是"+data.responseText);
                            }
                        });
                    } else {
                        alert("图片尺寸不大于：500*500！");
                    }
                };
            };
        }
    });
});