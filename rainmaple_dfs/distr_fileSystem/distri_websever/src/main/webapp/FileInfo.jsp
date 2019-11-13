<%@ page import="cn.edu.ruc.adcourse.data.FileInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="cn.edu.ruc.adcourse.util.Tool" %>
<%@ page import="cn.edu.ruc.adcourse.data.FileStorageInfo" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%!
    List<FileInfo> fileInfos = null;
    Map<String, FileStorageInfo> groupInfos = null;
    String[] keys = null;
%>
<%
    fileInfos = (List<FileInfo>) request.getAttribute("fileInfos");
    groupInfos = (Map<String, FileStorageInfo>) request.getAttribute("groupInfos");
    Object[] src = groupInfos.keySet().toArray();
    keys = new String[groupInfos.size()];
    for (int i = 0; i < keys.length; i++) {
        keys[i] = (String) src[i];
    }
%>
<html>
<head>
    <title>Title</title>
    <%--<meta http-equiv="refresh" content="1">--%>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
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
    <meta charset="utf-8">
</head>
<body>

<ul id="myTabs" class="nav nav-tabs" style="margin: 0 auto">
    <li class="active">
        <a href="#all" data-toggle="tab">所有文件信息</a>
    </li>

    <%
        for (int i = 0; i < keys.length; i++) {
            out.print("<li>" +
                    "<a href='#" + keys[i] + "' data-toggle=\"tab\">" + keys[i] + "</a>" +
                    "</li>");
        }
    %>
</ul>

<div id="myTabsContent" class="tab-content">
    <div class="tab-pane fade in active" id="all">
        <table class="table table-hover" style="width: 80%;margin: 30px auto;">
            <tr>
                <th>FileName</th>
                <th>UUID</th>
                <th>OriginalSize</th>
                <th>SaveSize</th>
                <%--<th>GroupID</th>--%>
            </tr>
            <%
                if (fileInfos != null) {
                    for (FileInfo fi : fileInfos) {
                        out.write("<tr>");
                        out.print("<td>" + fi.getOriginalName() + "</td>");
                        out.print("<td>" + fi.getUid() + "</td>");
                        out.print("<td>" + Tool.convertToSize(fi.getOriginalSize()) + "</td>");
                        out.print("<td>" + Tool.convertToSize(fi.getSaveSize()) + "</td>");
//                        out.print("<td>" + fi.getGroupNumber() + "</td>");
                        out.write("</tr>");
                    }
                }
            %>
        </table>
    </div>

    <%
        for (int i = 0; i < keys.length; i++) {
            out.print("<div class=\"tab-pane fade\" id=\"" + keys[i] + "\">");
    %>
    <table class="table table-hover" style="width: 80%;margin: 30px auto;">
        <tr>
            <th>FileName</th>
            <th>UUID</th>
            <th>OriginalSize</th>
            <th>SaveSize</th>
            <%--<th>GroupID</th>--%>
        </tr>
            <%
            if(groupInfos.get(keys[i]).getFileInfos() != null){
                for (FileInfo fi : groupInfos.get(keys[i]).getFileInfos()) {
                out.write("<tr>");
                out.print("<td>" + fi.getOriginalName() + "</td>");
                out.print("<td>" + fi.getUid() + "</td>");
                out.print("<td>" + Tool.convertToSize(fi.getOriginalSize()) + "</td>");
                out.print("<td>" + Tool.convertToSize(fi.getSaveSize()) + "</td>");
//                out.print("<td>" + groupInfos.get(keys[i]).getGroupId() + "</td>");
                out.write("</tr>");
                }
            }

            %>
            <%
            out.print("</table></div>");
        }
    %>
</div>
</body>
</html>
