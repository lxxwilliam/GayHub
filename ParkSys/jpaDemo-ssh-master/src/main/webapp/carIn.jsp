<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>入库登记</title>
    <link rel="stylesheet" href="layui-v2.2.45/layui/css/layui.css">
</head>
<body style="padding: 15px">
<form style="width: 300px;height: 300px;margin: 50px;" class="layui-form" id="form_carIn">
    <!-- 提示：如果你不想用form，你可以换成div等任何一个普通元素 -->
    <div class="layui-form-item">
        <label class="layui-form-label">车牌号</label>
        <div class="layui-input-block">
            <input type="text" name="carNo" id="carNo" placeholder="请输入车牌号" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <label class="layui-form-label">车型</label>
        <div class="layui-input-block">
            <select name="carType" lay-filter="aihao">
                <option value="0">小型车</option>
                <option value="1">中型车</option>
                <option value="2">大型车</option>
            </select>
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="*" id="submit" type="button">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>
    <!-- 更多表单结构排版请移步文档左侧【页面元素-表单】一项阅览 -->
</form>
<script src="layui-v2.2.45/layui/layui.js"></script>
<script>
    //JavaScript代码区域
    layui.use(['element', 'layer', 'form'], function () {
        var element = layui.element
            , $ = layui.jquery
            , form = layui.form
            , layer = layui.layer;

        function isVehicleNumber(vehicleNumber) {
            var result = false;
            if (vehicleNumber.length == 7) {
                var express = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
                result = express.test(vehicleNumber);
            }
            return result;
        }

        $(function () {
            $("#submit").click(function () {
                var carNO = $("#carNo").val();
                if (!isVehicleNumber(carNO)) {
                    layer.confirm("车牌号格式错误！")
                } else {
                    $.ajax({
                        type: "POST",
                        dataType: "json",
                        url: "<%=path%>/carIn",
                        data: $('#form_carIn').serialize(),
                        success: function (data) {
                            if (data.msg == "1") {
                                data = '登记成功!';
                            } else {
                                data = '登记失败!';
                            }
                            layer.confirm(data, {
                                title: '提示'
                            }, function (index) {
                                layer.close(index);
                            });
                        },
                        error: function (data) {
                            console.log(data);
                            alert(data);
                        }
                    });
                }
            });
        })
    });
</script>
</body>
</html>