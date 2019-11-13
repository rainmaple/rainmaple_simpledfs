<%@ page import="cn.edu.ruc.adcourse.data.GroupInfo" %>
<%@ page import="cn.edu.ruc.adcourse.data.FileStorageInfo" %>
<%@ page import="cn.edu.ruc.adcourse.util.Tool" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%!
    List<GroupInfo> list = null;
%>

<%
    list = (List<GroupInfo>) request.getAttribute("data");
%>
<script language="JavaScript">

    function myRefresh() {
        history.go(0);
    }
    var j = false;
    t = setTimeout("myRefresh()", 1000);
    function changeState() {
        if (j) {
            myRefresh();
        }
        else {
            j = true;
            clearTimeout(t);
            var btn = document.getElementById("autoFresh");
            btn.innerHTML = "开启自动刷新";
        }
    }

</script>

<html>
<head>
    <%--<meta http-equiv="refresh" content="1">--%>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
          integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

    <script src="https://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
    <!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <title>$Title$</title>
</head>

<h1 style="margin: 10px auto; text-align: center">rainmaple分布式文件系统监控面板</h1>
<table class="table table-hover" style="width: 80%;margin: 30px auto;">
    <tr>
        <th>节点数量</th>
        <th>IP</th>
        <th>端口</th>
        <th>总容量</th>
        <th>剩余容量</th>
        <th>连接数量</th>
        <th>组别</th>
        <th>状态</th>
    </tr>

    <%
        for (GroupInfo groupInfo : list) {
            for (FileStorageInfo fileStorageInfo : groupInfo.getNodes()) {
                out.write("<tr>");
                out.print("<td>" + fileStorageInfo.getName() + "</td>");
                out.print("<td>" + fileStorageInfo.getIp() + "</td>");
                out.print("<td>" + fileStorageInfo.getPort() + "</td>");
                out.print("<td>" + Tool.convertToSize(fileStorageInfo.getCapacity()) + " </td>");
                out.print("<td>" + Tool.convertToSize(fileStorageInfo.getLeftCapacity()) + "</td>");
                out.print("<td>" + fileStorageInfo.getConnectNum() + "</td>");
                out.print("<td>" + fileStorageInfo.getGroupId() + "</td>");
                if (fileStorageInfo.isUseful()) {
                    out.print("<td>UP</td>");
                } else {
                    out.print("<td>DOWN</td>");
                }
                out.write("</tr>");
            }
            out.flush();
        }
    %>
</table>
<div style="width: 100%; text-align: center; margin: 0 auto">
    <button class="btn btn-primary" onclick="changeState()" id="autoFresh" style="">
        关闭自动刷新
    </button>
    <a href="FileInfo.jsp">
        <button class="btn btn-primary">
            文件详情
        </button>
    </a>
</div>
</div>
</body>
</html>
