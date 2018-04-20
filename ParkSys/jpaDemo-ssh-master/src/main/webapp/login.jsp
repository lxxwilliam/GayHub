<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<html>
<head>
    <title>Login</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link type="text/css" rel="stylesheet" href="css/common.css">
    <%--<link type="text/css" rel="stylesheet" href="css/login.css">--%>
    <script type="text/javascript" src="js/jquery/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="js/jquery/jquery.particleground.js"></script>
    <script type="text/javascript" src="js/layer/layer.js"></script>
    <%--<script type="text/javascript" src="js/login.js"></script>--%>
    <script>
        function ajaxlogin() {
            $.ajax({
                type: "POST",
                dataType: "json",
                url: "<%=path%>/login",
                data: $('#login-form').serialize(),
                success: function (data) {
                    sessionStorage.setItem("nickName",data.nickName);
                    sessionStorage.setItem("userId",data.userId);
                    if (data.msg == "success") {
                        window.location.href = "main.html";
                    } else {
                        layer.alert("用户名或密码错误！", {icon: 2, title: "提示"});
                    }
                },
                error: function () {
                    alert("失败")
                }
            });
        }

        $(function () {
            $(".login-btn").click(function () {
                ajaxlogin();
            });

            $(".login-containner").bind("keypress", function(e) {
                var theEvent = e || window.event;
                var code = theEvent.keyCode || theEvent.which || theEvent.charCode;
                if (code == 13) {
                    e.preventDefault();
                    ajaxlogin();
                }
            });
        });

    </script>
</head>
<body>
<div class="login-containner">
    <div class="login-canvas" id="particles"></div>
    <form method="post" id="login-form">
        <div class="login-center">
            <div class="login-centerForm">
                <div class="login-logo">
                    <img src="<%=path%>/images/park-logo.png"/>
                </div>
                <div class="login-inputDiv">
                    <i class="icon login-icon-user"></i>
                    <input type="text" name="j_username" id="username" placeholder="请输入用户名"
                           onfocus="focusIn(this)" onblur="focusOut(this)"/>
                </div>
                <div class="login-inputDiv">
                    <i class="icon login-icon-pwd"></i>
                    <input class="pwd-input" type="password" name="j_password" id="password" placeholder="请输入密码"
                           onfocus="focusIn(this)" onblur="focusOut(this)"/>
                    <i class="eyeIcon login-icon-eye" onclick="showPwd(this);"></i>
                </div>
                <div class="clear"></div>
                <button class="login-btn" type="button">登陆</button>
            </div>
        </div>
    </form>
</div>
</body>
</html>