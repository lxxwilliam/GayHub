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
        <label class="layui-form-label">车牌号：</label>
        <div class="layui-input-block">
            <input type="text" name="carNo" id="carNo" placeholder="请输入车牌号" autocomplete="off" class="layui-input">
        </div>
    </div>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit lay-filter="*" id="submit" type="button">立即提交</button>
            <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
    </div>

</form>
<table id="carOut" lay-filter="carOut"></table>
<script src="layui-v2.2.45/layui/layui.js"></script>
<script>

</script>
<script>
    //JavaScript代码区域
    layui.use(['element', 'layer', 'table', 'form'], function () {
        var element = layui.element
            , $ = layui.jquery
            , form = layui.form
            , layer = layui.layer
            , table = layui.table;

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
                        url: "<%=path%>/carOut",
                        dataType: "json",
                        data: $('#form_carIn').serialize(),
                        async: false,
                        success: function (data) {
                            if (data.msg == "1") {
                                table.render({
                                    elem: '#carOut'
                                    , even: true
                                    , size: 'lg'
                                    , cols: [[ //表头
                                        {field: 'id', title: 'ID', width: 80, fixed: 'left', sort: true}
                                        , {field: 'carNo', title: '车牌号',}
                                        , {field: 'type', title: '车型'}
                                        , {field: 'startDate', title: '开始时间'}
                                        , {field: 'endDate', title: '结束时间'}
                                        , {field: 'price', title: '单价'}
                                        , {field: 'cost', title: '总价'}
                                    ]]
                                    , data: [{
                                        "id": data.data[0].id,
                                        "carNo": data.data[0].carNo,
                                        "type": data.data[0].carType,
                                        "startDate": data.data[0].startDateStr,
                                        "endDate": data.data[0].endDateStr,
                                        "price": data.data[0].price,
                                        "cost": data.data[0].cost
                                    }]
                                });
                                layer.confirm('出库成功!', {
                                    title: '提示'
                                }, function (index) {
                                    layer.close(index);
                                });
                            } else {
                                layer.confirm('出库失败!', {
                                    title: '提示'
                                }, function (index) {
                                    self.location.reload();
                                    layer.close(index);
                                });
                            }
                        },
                        error: function () {
                            alert('fail');
                        }
                    });
                }
                //return false;
            });
        });
    });
</script>
</body>
</html>