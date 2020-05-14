<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>"/>
    <title>Bootstrap 实例</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="<%=basePath%>css/index.css?v=<%=new Date().getTime()%>">
</head>
<body>
    <%--<div>--%>
        <%--<input datatype="*" type="text" value="" name="qrCode" id="qrCode">--%>
        <%--<button type="submit" onclick="createQrcode(this)">生成二维码</button>--%>
    <%--</div>--%>

    <form action="/uploadController/upload.php" enctype="multipart/form-data" method="post">
        <table>
            <tr>
                <td>请选择文件:</td>
                <td><input type="file" name="file"></td>
            </tr>
            <tr>
                <td><input type="submit" value="上传"></td>
            </tr>
        </table>
    </form>

    <div>
        <div>图片地址：<p id="urlName">${url}</p></div>
        <input datatype="*" type="text" value="嘉和兴" name="name" placeholder="企业名" id="companyName">
        <input datatype="*" type="hidden" value="50" name="textSize" placeholder="字体大小" id="textSize">
        <input datatype="*" type="number" value="" name="qrCodeList" placeholder="开始值" id="befor">
        <input datatype="*" type="number" value="" name="qrCodeList" placeholder="结束值" id="after">
        <button type="submit" onclick="createQrcodeList(this)">批量生成二维码</button>
    </div>
    <div class="imageUrl"></div>
    <div><a href="/uploadController/download.php?fileName=${url}">下载到本地</a></div>
    <div class="s-pic-content">
</div>
</body>
<script>

    $(function () {
        $(".s-pic-content").css("background-image",'url(${url})');
    })
   function createQrcode(e) {
       var params = $("#qrCode").val();
       $.ajax({
           async: false,
           url : "<%=basePath%>wx/testEncode.php",
           data:{"qrCode":params},
           type:"POST",
           dataType:"json",
           success:function (data) {
               var html='<img style="position: absolute;left: 460px;top:137px" src="'+data.msg+'?imageMogr2/thumbnail/108x108!"/>'
               <%--var html='        <li class="item">\n' +--%>
                   <%--'            <div class="qrcode">\n' +--%>
                   <%--'                <img alt="zhonggen1967_网易摄影" class="pic" src="<%=basePath%>'+data.msg+'">\n' +--%>
                   <%--'                <img alt="zhonggen1967_网易摄影" class="pic" src="<%=basePath%>'+data.msg+'">\n' +--%>
                   <%--'            </div>\n' +--%>
                   <%--'            <i>ID:'+params+'</i>\n' +--%>
                   <%--'        </li>'--%>
               $(".s-pic-content").append(html);
           },
           error:function () {
               alert("失败");
           }
       })
   }

   function createQrcodeList(e) {
       var url = $("#urlName").html();
       var size = $("#textSize").val();
       var name = $("#companyName").val();
       var befor = $("#befor").val();
       var after = $("#after").val();
       $.ajax({
           async: true,
           url : "<%=basePath%>wx/testEncodeList.php",
           data:{"url":url,"size":size,"name":name,"befor":befor,"after":after},
           type:"POST",
           dataType:"json",
           success:function (data) {
               if(data.success){
                   var img = "<%=basePath%>"+data.msg+"?v=<%=new Date().getTime()%>";
                   $(".s-pic-content").css("background-image",'url(' + img + ')');
                   //alert(""+data.msg);
                   $(".imageUrl").html(img);
               }
           },
           error:function () {
               alert("失败");
           }
       })
   }

   function downLoad(url) {
       windows.location.href=url;
       windows.location.href="upload.jsp";
   }
</script>
</html>