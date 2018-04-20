<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <title>修改密码</title>
    <link type="text/css" href="css/common.css" rel="stylesheet">
    <script type="text/javascript" src="js/jquery/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="js/jquery/jquery.particleground.js"></script>
    <script type="text/javascript" src="js/layer/layer.js"></script>
</head>
<body>
<div class="common-layerCenter">
    <form id="Loginuser_modifypwdform">
        <input type="hidden" id="form_userid" name="userid">
        <ul class="commn-layerUl common-login-layerUl">
            <li><label>原密码：</label>
                <input type="password" name="Loginoldpwd" id="Loginoldpwd" placeholder="请输入原密码"
                       maxlength="50" onblur="pwdCheckIsNumOrLetterOld(this)">
                <div class="clear"></div>
            </li>
            <li><label>新密码：</label>
                <input type="password" name="Loginnewpwd" id="Loginnewpwd" placeholder="请输入新密码"
                       maxlength="50" onblur="pwdCheckIsNumOrLetterNew(this)">
                <div class="clear"></div>
            </li>
            <li><label>确认密码：</label>
                <input type="password" name="Loginnewpwd2" id="Loginnewpwd2" placeholder="请再次输入新密码"
                       maxlength="50">
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
        if (!pwdPassOld) {
            return false;
        }
        if (!pwdPassNew) {
            return false;
        }
        var oldpwd = $("#Loginoldpwd").val();
        if (!oldpwd || oldpwd.trim() == "") {
            layer.alert('请输入原密码！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        var newpwd = $("#Loginnewpwd").val();
        if (!newpwd || newpwd.trim() == "") {
            layer.alert('请输入新密码！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        var newpwd2 = $("#Loginnewpwd2").val();
        if (!newpwd2 || newpwd2.trim() == "") {
            layer.alert('请输入确认密码！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        if (newpwd != newpwd2) {
            layer.alert('两次输入的密码不一致！', {
                icon: 0,
                title: "提示"
            });
            return;
        }
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "<%=path%>/modifyPwd",
            data: $('#Loginuser_modifypwdform').serialize(),
            success: function (data) {
                if (data.msg == "1") {
                    layer.confirm("修改成功", {
                        title: '提示'
                    }, function (index) {
                        layer.close(index);
                        //修改密码成功后需要重新登录
                        layer.alert('修改密码后需要重新登录，页面即将跳转...', {icon: 0, title: '提示', btn: []});
                        setTimeout(function () {
                            //关闭添加节点的弹窗页面
                            var index = parent.layer.getFrameIndex(window.name);
                            parent.layer.close(index);
                            //跳转到登录界面
                            parent.location.href = "login.jsp";
                        }, 3000);
                    });
                } else {
                    layer.confirm("修改失败", {
                        title: '提示'
                    }, function (index) {
                        layer.close(index);
                    })
                }
            }
        });
    }

    var pwdPassOld = true;
    var pwdPassNew = true;

    //判断新建用户的密码是否仅为数字和大小写字母
    function pwdCheckIsNumOrLetterOld(object) {
        var str = $(object).val();
        if (str !== "") {
            var reg1 = new RegExp(/^[0-9A-Za-z]+$/);
            if (!reg1.test(str)) {
                $(object).css('border', '1px solid #ff4400');
                pwdPassOld = false;
                return false;
            } else {
                $(object).css('border', '');
                pwdPassOld = true;
            }
        } else {
            $(object).css('border', '1px solid #ff4400');
            pwdPassOld = false;
        }
    }//判断新建用户的密码是否仅为数字和大小写字母
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
