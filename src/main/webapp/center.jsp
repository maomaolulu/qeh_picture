<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

%>

<!DOCTYPE html>
<html>
<head>
    <base href="<%=basePath%>"/>
    <title>生成二维码实例</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
    <link rel="stylesheet" href="<%=basePath%>css/index.css?v=<%=new Date().getTime()%>">
</head>
<body>
    <div>
        <input datatype="*" type="text" value="嘉和兴" name="name" placeholder="企业名" id="companyName">
        <button type="submit" onclick="createQrcodeList(this)">批量生成二维码</button>
    </div>
    <div class="imageUrl"></div>
    <div><a href="/uploadController/download2.php">下载到本地</a></div>
    <div><a href="/wx/upload.php">回到上传文件界面</a></div>
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
       var name = $("#companyName").val();
       $.ajax({
           async: true,
           url : "<%=basePath%>wx/createEncodeList2.php",
           data:{"name":name},
           type:"POST",
           dataType:"json",
           success:function (data) {
               if(data.success){
                   $(".imageUrl").html(data.msg);
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