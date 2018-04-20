<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="echarts-master/dist/echarts.min.js"></script>
    <script type="text/javascript" src="js/jquery/jquery-1.11.3.min.js"></script>
    <script type="text/javascript" src="js/jquery/jquery.particleground.js"></script>
</head>
<body style="margin: 0px">
<div id="main" style="width:100%;height: 840px;"></div>
<script type="text/javascript">
    function GetDateStr(AddDayCount) {
        var dd = new Date();
        dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
        var y = dd.getFullYear();
        var m = dd.getMonth() + 1;//获取当前月份的日期
        var d = dd.getDate();
        return y + "-" + m + "-" + d;
    }

    $.ajax({
        type: "POST",
        dataType: "json",
        url: "<%=path%>/getDatas",
        success: function (data) {
            var myChart = echarts.init(document.getElementById('main'));
            var option = {
                backgroundColor: "#404A59",
                color: ['#ffd285', '#ff733f', '#ec4863'],

                title: [{
                    text: '停车场每日进出车辆数',
                    left: '1%',
                    top: '6%',
                    textStyle: {
                        color: '#fff'
                    }
                }, {
                    text: '停车场剩余车位数',
                    left: '83%',
                    top: '6%',
                    textAlign: 'center',
                    textStyle: {
                        color: '#fff'
                    }
                }],
                tooltip: {
                    trigger: 'axis'
                },
                legend: {
                    x: 300,
                    top: '7%',
                    textStyle: {
                        color: '#ffd285',
                    },
                    data: ['入库', '出库']
                },
                grid: {
                    left: '1%',
                    right: '35%',
                    top: '16%',
                    bottom: '6%',
                    containLabel: true
                },
                toolbox: {
                    "show": false,
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    "axisLine": {
                        lineStyle: {
                            color: '#FF4500'
                        }
                    },
                    "axisTick": {
                        "show": false
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    boundaryGap: false,
                    data: [GetDateStr(-6), GetDateStr(-5), GetDateStr(-4), GetDateStr(-3), GetDateStr(-2), GetDateStr(-1), GetDateStr(0)]
                },
                yAxis: {
                    "axisLine": {
                        lineStyle: {
                            color: '#fff'
                        }
                    },
                    splitLine: {
                        show: true,
                        lineStyle: {
                            color: '#fff'
                        }
                    },
                    "axisTick": {
                        "show": false
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    type: 'value'
                },
                series: [{
                    name: '入库',
                    smooth: true,
                    type: 'line',
                    symbolSize: 8,
                    symbol: 'circle',
                    data: [data.ins[6], data.ins[5], data.ins[4], data.ins[3], data.ins[2], data.ins[1], data.ins[0]]
                }, {
                    name: '出库',
                    smooth: true,
                    type: 'line',
                    symbolSize: 8,
                    symbol: 'circle',
                    data: [data.outs[6], data.outs[5], data.outs[4], data.outs[3], data.outs[2], data.outs[1], data.outs[0]]
                },
                    {
                        name:'数量',
                        type: 'pie',
                        center: ['83%', '33%'],
                        radius: ['25%', '35%'],
                        label: {
                            normal: {
                                position: 'center'
                            }
                        },
                        data: [{
                            value: 50 - data.usedCarPlace,
                            name: '剩余车位',
                            tooltip: {
                                trigger: 'item',
                                formatter: "{a} <br/>{b}: {c} ({d}%)"
                            },
                            itemStyle: {
                                normal: {
                                    color: '#ffd285'
                                }
                            },
                            label: {
                                normal: {
                                    formatter: function (params) {
                                        return params.value
                                    },
                                    textStyle: {
                                        color: '#ffd285',
                                        fontSize: 35

                                    }
                                }
                            }
                        }, {
                            value: data.usedCarPlace,
                            name: '已用车位',
                            tooltip: {
                                trigger: 'item',
                                formatter: "{a} <br/>{b}: {c} ({d}%)"
                            },
                            itemStyle: {
                                normal: {
                                    color: '#87CEFA'
                                }
                            },
                            label: {
                                normal: {
                                    textStyle: {
                                        color: '#ffd285',
                                        fontSize: 20
                                    },
                                    formatter: '\n剩余车位数'
                                }
                            }
                        }]
                    }]
            };
            myChart.setOption(option);
        }
    });
</script>
</body>
</html>