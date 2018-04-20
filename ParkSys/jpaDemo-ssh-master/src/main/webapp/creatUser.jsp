<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/12/18
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>新建用户</title>
    <link type="text/css" href="css/common.css" rel="stylesheet">
    <script type="text/javascript" src="js/jquery/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="js/jquery/jquery.particleground.js"></script>
    <script type="text/javascript" src="js/layer/layer.js"></script>
</head>
<body>
<div class="common-layerCenter">
    <form id="creatUser_form">
        <ul class="commn-layerUl common-login-layerUl">
            <li>
                <label>用户名：</label>
                <input type="text" name="userName" id="userName" placeholder="请输入用户名"
                       maxlength="50">
                <div class="clear"></div>
            </li>
            <li>
                <label>密码：</label>
                <input type="password" name="passWd" id="passWd" placeholder="请输入密码"
                       maxlength="50" onblur="pwdCheckIsNumOrLetterNew(this)">
                <div class="clear"></div>
            </li>
            <li>
                <label>昵称：</label>
                <input type="text" name="nickName" id="nickName" placeholder="请输入昵称"
                       maxlength="50">
                <div class="clear"></div>
            </li>
            <li>
                <label>联系方式：</label>
                <input name="phoneNo" id="phoneNo" type="text" placeholder="请输入联系方式" maxlength="20">
                <div class="clear"></div>
            </li>
            <li>
                <label>备注：</label>
                <textarea name="memo" id="memo" rows="5" cols="47" placeholder="备注信息" maxlength="150"></textarea>
                <div class="clear"></div>
            </li>
        </ul>
    </form>
</div>
<!--common-bottomDiv Start-->
<div class="common-bottomDiv">
    <button class="btn-blue" onclick="saveModifyUserPwd()">确定</button>
    <button class="btn-gray" onclick="cancel()">取消</button>
</div>
<!--common-bottomDiv End-->
</body>
<script type="text/javascript">
    function cancel() {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    }

    function saveModifyUserPwd() {
        if (!pwdPassNew) {
            return false;
        }
        var userName = $("#userName").val();
        if (!userName || userName.trim() == "") {
            layer.alert('请输入用户名！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        var nickName = $("#nickName").val();
        if (!nickName || nickName.trim() == "") {
            layer.alert('请输入昵称！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        var passWd = $("#passWd").val();
        if (!passWd || passWd.trim() == "") {
            layer.alert('请输入密码！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        var phoneNo = $("#phoneNo").val();
        if (!phoneNo || phoneNo.trim() == "") {
            layer.alert('请再次输入密码！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        var memo = $("#memo").val();
        if (!memo || memo.trim() == "") {
            layer.alert('请再次输入密码！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        $.ajax({
            type: "POST",
            dataType: "text",
            url: "<%=path%>/save",
            data: $('#creatUser_form').serialize(),
            success: function (data) {
                if (data == 1) {
                    data = '新建成功!';
                } else if (data == 2) {
                    data = '用户名已存在!';
                } else {
                    data = '新建失败!';
                }
                layer.confirm(data, {
                    title: '提示'
                }, function (index) {
                    layer.close(index);
                    if (data == '新建成功!') {
                        parent.location.reload();
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(index);
                    }
                });
            }
        });
    }

    var pwdPassNew = true;
    //判断新建用户的密码是否仅为数字和大小写字母
    function pwdCheckIsNumOrLetterNew(object) {
        var str = $(object).val();
        if (str !== "") {
            var reg1 = new RegExp(/^[0-9A-Za-z]+$/);
            if (!reg1.test(str)) {
                $(object).css('border', '1px solid #ff4400');
                pwdPassNew = false;
                return false;
            } else {
                $(object).css('border', '');
                pwdPassNew = true;
            }
        } else {
            $(object).css('border', '1px solid #ff4400');
            pwdPassNew = false;
        }
    }
</script>
</html>

