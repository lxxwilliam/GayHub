<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>历史记录</title>
    <link rel="stylesheet" href="layui-v2.2.45/layui/css/layui.css" media="all">
</head>
<body style="padding: 15px">

<div class="demoTable">
    <div class="layui-inline">
        <input class="layui-input" name="id" id="demoReload" placeholder="输入车牌号查询" autocomplete="off">
    </div>
    <button class="layui-btn" data-type="reload"><i class="layui-icon">&#xe615;</i> 搜索</button>
</div>

<table class="layui-hide" id="records" lay-filter="user"></table>

<script src="layui-v2.2.45/layui/layui.js"></script>
<script>
    layui.use('table', function(){
        var table = layui.table;
        table.render({
            elem: '#records'
            ,url: "<%=path%>/getRecords" //数据接口
            ,page: true //开启分页
            , even: true
            , size: 'lg'
            ,cols: [[ //表头
                {field: 'id', title: 'ID', width:80 , fixed: 'left', sort: true}
                ,{field: 'carNo', title: '车牌号', sort: true}
                ,{field: 'carType', title: '车型', sort: true}
                ,{field: 'startDateStr', title: '开始时间', sort: true}
                ,{field: 'endDateStr', title: '结束时间', sort: true}
                ,{field: 'price', title: '单价', sort: true}
                ,{field: 'cost', title: '总价', sort: true}
            ]]
            ,id: 'testReload'
        });

        var $ = layui.$, active = {
            reload: function(){
                var demoReload = $('#demoReload');

                //执行重载
                table.reload('testReload', {
                    page: {
                        curr: 1 //重新从第 1 页开始
                    }
                    ,where: {
                        keyword: demoReload.val()
                    }
                });
            }
        };

        $('.demoTable .layui-btn').on('click', function(){
            var type = $(this).data('type');
            active[type] ? active[type].call(this) : '';
        });
    });
</script>
</body>
</html>