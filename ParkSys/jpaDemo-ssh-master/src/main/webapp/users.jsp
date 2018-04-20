<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <title>主页</title>
    <link rel="stylesheet" href="layui-v2.2.45/layui/css/layui.css">
</head>
<body class="layui-layout-body" style="padding: 15px 15px">

<div class="demoTable">
    <div class="layui-inline">
        <input class="layui-input" name="id" id="demoReload" placeholder="输入用户名查询" autocomplete="off">
    </div>
    <button class="layui-btn " data-type="reload"><i class="layui-icon">&#xe615;</i> 搜索</button>
    <button class="layui-btn layui-btn-normal" id="creatUser"><i class="layui-icon">&#xe608;</i> 新建</button>
</div>

<table id="users" lay-filter="user"></table>

<script src="layui-v2.2.45/layui/layui.js"></script>
<script>
    layui.use(['table', 'element', 'layer', 'laypage'], function () {
        var table = layui.table,
            $ = layui.jquery,
            element = layui.element,
            layer = layui.layer,
            laypage = layui.laypage;

        table.render({
            elem: '#users'
            , url: "<%=path%>/getAll" //数据接口
            , even: true
            , size: 'lg'
            , cols: [[ //表头
                {field: 'id', title: 'ID', width: 80, fixed: 'left', sort: true}
                , {field: 'userName', title: '用户名', sort: true}
                , {field: 'nickName', title: '昵称'}
                , {field: 'phoneNo', title: '联系方式'}
                , {field: 'memo', title: '备注'}
                , {
                    fixed: 'right',
                    title: '操作',
                    align: 'center',
                    toolbar: '#barDemo'
                }
            ]]
            , page: true //开启分页
            ,id: 'users'
        });

        var $ = layui.$, active = {
            reload: function(){
                var demoReload = $('#demoReload');

                //执行重载
                table.reload('users', {
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

        //监听工具条
        table.on('tool(user)', function (obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var data = obj.data //获得当前行数据
                , layEvent = obj.event; //获得 lay-event 对应的值
            // if(layEvent === 'detail'){
            //     layer.msg('查看操作');
            // } else
            if (layEvent === 'del') {
                layer.confirm('确定删除该用户？', function (index) {
                    // layer.close(index);
                    //向服务端发送删除指令
                    $.ajax({
                        type: "POST",
                        dataType: "text",
                        url: "<%=path%>/del",
                        data: data,
                        success: function (data) {
                            if (data == 1) {
                                data = '删除成功!';
                            } else {
                                data = '删除失败!';
                            }
                            layer.confirm(data, {
                                title: '提示'
                            }, function (index) {
                                layer.close(index);
                                if (data == '删除成功!') {
                                    obj.del(); //删除对应行（tr）的DOM结构
                                    //parent.location.reload();
                                    self.location.reload();
                                    var index = parent.layer.getFrameIndex(window.name);
                                    parent.layer.close(index);
                                }
                            });
                        }

                    })
                });
            } else if (layEvent === 'edit') {
                layer.open({
                    type: 2,
                    title: ['修改用户',
                        'background-color:#f2f5f9;font-size:16px;color:#2d83ff'],
                    shadeClose: false,
                    shade: 0.5,
                    area: ['480px', '480px'],
                    content: ["editUser.jsp"],
                    success: function () {
                        $("iframe").contents().find("#form_userid").val(data.id);
                        $("iframe").contents().find("#userName").val(data.userName);
                        $("iframe").contents().find("#nickName").val(data.nickName);
                        $("iframe").contents().find("#phoneNo").val(data.phoneNo);
                        $("iframe").contents().find("#memo").val(data.memo);
                    }
                });
            }
        });

        $(function () {
            $("#creatUser").click(function () {
                layer.open({
                    type: 2,
                    title: ['新建用户',
                        'background-color:#f2f5f9;font-size:16px;color:#2d83ff'],
                    shadeClose: false,
                    shade: 0.5,
                    area: ['480px', '480px'],
                    content: ["creatUser.jsp"]
                });
            });
        });
    });
</script>
<script type="text/html" id="barDemo">
    <%--<a class="layui-btn layui-btn-mini" lay-event="detail">查看</a>--%>
    <a class="layui-btn layui-btn-mini" lay-event="edit"><i class="layui-icon">&#xe642;</i> 编辑</a>
    <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="del"><i class="layui-icon">&#xe640;</i> 删除</a>
</script>
</div>

</body>
</html>