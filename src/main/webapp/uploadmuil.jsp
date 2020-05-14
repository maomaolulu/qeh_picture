<%--
  Created by IntelliJ IDEA.User: xujianbin-Date: 2018/2/6
  To change this template use File | Settings | File Templates.
--%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    request.setAttribute("base", basePath);
%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <base href="${base}"/>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>easyUpload.js</title>
    <link rel="stylesheet" href="css/easy-upload.css">
    <style type="text/css">
        button{
            height: 30px;
            width: 200px;
        }
    </style>
</head>
<body style="">

<div>
    <button  onclick="javascript:window.location.href='wx/center.php'";>上传成功后进入生成二维码界面</button>
</div>

<div id="easyContainer">
    <div class="easy_upload-container">
        <div class="easy_upload-head">
            <input type="file" multiple="" class="fileInput" data-count="0"style="display:none;">
            <span class="easy_upload_select noselect">选择文件</span>
            <span class="easy_upload_head_btn1 noselect">上传</span>
            <span class="easy_upload_head_btn2 noselect">删除</span>
            <i class="easyUploadIcon noselect head_check" data-checked="no"></i>
            <span class="easy_upload_note">提示：最多上传10000000个文件，支持格式为doc、pdf、jpg</span>
        </div>
        <ul class="easy_upload_queue"></ul>
    </div>
</div>

<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript" src="js/jquery.qrcode.min.js"></script>
<!-- 视实际需要决定是否引入jquery.cookie-1.4.1.min.js-->
<script type="text/javascript" src="js/jquery.cookie-1.4.1.min.js"></script>
<script type="text/javascript" src="js/easyUpload.js"></script>
<script type="text/javascript">
    $('#easyContainer').easyUpload({
        allowFileTypes: '*.jpg;*.doc;*.pdf',//允许上传文件类型，格式';*.doc;*.pdf'
        allowFileSize: 100000,//允许上传文件大小(KB)
        selectText: '选择文件',//选择文件按钮文案
        multi: true,//是否允许多文件上传
        multiNum: 100000000,//多文件上传时允许的文件数
        showNote: true,//是否展示文件上传说明
        note: '提示：最多上传1000000个文件，支持格式为doc、pdf、jpg',//文件上传说明
        showPreview: true,//是否显示文件预览
        url: '/uploadController/uploadMulyi.php',//上传文件地址
        fileName: 'file',//文件filename配置参数
        formParam: {
            token: $.cookie('token_cookie')//不需要验证token时可以去掉
        },//文件filename以外的配置参数，格式：{key1:value1,key2:value2}
        timeout: 30000,//请求超时时间
        successFunc: function (res) {
            console.log('成功回调', res);
        },//上传成功回调函数
        errorFunc: function (res) {
            console.log('失败回调', res[res.length-1].easyFileIndex);
        },//上传失败回调函数
        deleteFunc: function (res) {
            console.log('删除回调', res);
        }//删除文件回调函数
    });
</script>


</body>
</html>
