<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <meta content="" name="description"/>
    <meta content="webthemez" name="author"/>
    <title>任务目录</title>
    <!-- Bootstrap Styles-->
    <link href="/assets/css/bootstrap.css" rel="stylesheet"/>
    <!-- FontAwesome Styles-->
    <link href="/assets/css/font-awesome.css" rel="stylesheet"/>
    <!-- Morris Chart Styles-->
    <link href="/assets/js/morris/morris-0.4.3.min.css" rel="stylesheet"/>
    <!-- Custom Styles-->
    <link href="/assets/css/custom-styles.css" rel="stylesheet"/>
    <!-- Google Fonts-->
    <link rel="stylesheet" href="/assets/js/Lightweight-Chart/cssCharts.css">
</head>
<style>
    .modal.in .modal-dialog {
        -webkit-transform: translate(0, -50%);
        -ms-transform: translate(0, -50%);
        -o-transform: translate(0, -50%);
        transform: translate(0, -50%)
    }

    .modal-dialog {
        position: absolute;
        width: auto;
        margin: 10px auto;
        left: 0;
        right: 0;
        top: 50%
    }

    @media (min-width: 768px) {
        .modal-dialog {
            width: 600px
        }
    }
</style>
<body>
<div id="wrapper">
    <jsp:include page="/common/header.jsp"/>
    <div id="page-wrapper">
        <div class="header">
            <h1 class="page-header">
                定时任务
            </h1>
            <ol class="breadcrumb">
                <li><a href="#">Home</a></li>
                <li><a href="#">任务目录</a></li>
            </ol>
        </div>
        <div id="page-inner">
            <div class="row">
                <div class="col-md-12">
                    <div class="panel panel-default">
                        <button id='newJob' class='btn btn-primary' onclick="newJob()" type='button'>新增任务</button>
                        &nbsp;&nbsp;&nbsp;
                        <button class='btn btn-success' onclick="newCron()" type='button'>生成时间表达式</button>
                    </div>
                    <div class="panel-body">
                        <div class="table-responsive">
                            <table class="table table-striped table-bordered table-hover" id="dataTables"
                                   style="width: 100%">
                                <thead>
                                <tr>
                                    <th>id</th>
                                    <th>任务</th>
                                    <th>时间表达式</th>
                                    <th>运行状态</th>
                                    <th>操作</th>
                                </tr>
                                </thead>
                                <tbody></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="editSpace" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                                aria-hidden="true">&times;</span>
                        </button>
                        <h4 class="modal-title" id="myModalLabel">任务详情</h4>
                    </div>
                    <div class="modal-body">
                        <form class="form-horizontal">
                            <div class="form-group">
                                <label for="cronExpression" class="col-xs-3 control-label">时间表达式</label>
                                <button class='btn btn-success' onclick="newCron()" type='button'>生成时间表达式</button>
                                <div class="col-sm-4">
                                    <input class="form-control" id="cronExpression" placeholder="cronExpression">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="jobDesc" class="col-xs-3 control-label">任务别名</label>
                                <div class="col-sm-6">
                                    <input class="form-control" id="jobDesc" placeholder="jobDesc">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="className" class="col-xs-3 control-label">执行类</label>
                                <div class="col-sm-8">
                                    <input class="form-control" id="className" placeholder="className">
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                        <button type="button" class="btn btn-primary" onclick="update()">保存</button>
                    </div>
                </div>
            </div>
        </div>
        <input type="hidden" id="hid_id">
        <div class="row">
            <div class="col-md-12">

            </div>
        </div>
    </div>
</div>
</div>
<script src="/assets/js/dataTables/jquery.dataTables.js"></script>
<script src="/assets/js/dataTables/dataTables.bootstrap.js"></script>
<script src="/assets/js/common.js"></script>
<script type="text/javascript">
    var table;
    $(function () {
        $("#capitalflow").addClass("active-menu");
        table = $('#dataTables').DataTable({
            language: {
                "sProcessing": "处理中...",
                "sLengthMenu": "每页 _MENU_ 项",
                "sZeroRecords": "没有匹配结果",
                "sInfo": "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",
                "sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
                "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
                "sInfoPostFix": "",
                "sSearch": "搜索:",
                "sUrl": "",
                "sEmptyTable": "表中数据为空",
                "sLoadingRecords": "载入中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首页",
                    "sPrevious": "上页",
                    "sNext": "下页",
                    "sLast": "末页",
                    "sJump": "跳转"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            "aoColumns": [
                {data: 'id'},
                {data: 'jobDesc'},
                {data: 'cronExpression'},
                {data: 'statusName'},
                {data: ''}
            ],
            "ajax": {
                "url": '/schedule/selectByCondition'
            },
            "columnDefs": [{
                "targets": -1, //最后一列添加删除按键
                "data": null,
                "defaultContent": "" +
                "<button id='pause' class='btn btn-warning' type='button'>暂停</button>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<button id='resume' class='btn btn-primary' type='button'>恢复</button>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<button id='runOnce' class='btn btn-success' type='button'>运行一次</button>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<button id='edit' class='btn btn-info' type='button'>编辑</button>" +
                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" +
                "<button id='del' class='btn btn-danger' type='button'>删除</button>"
            }, {
                "targets": 0, //第一列隐藏
                "data": "id",
                "visible": false,//不可见
                "searchable": false
            }]
        });
        $('#dataTables tbody').on('click', 'button#pause', function () {
            var data = table.row($(this).parents('tr')).data();//所选择的行的数据
            Ewin.confirm({message: "确定要暂停当前任务吗？"}).on(function (e) {
                if (!e) {
                    return;
                }
                $.ajax({
                    url: "/schedule/pauseJob",
                    type: "post",
                    dataType: "json",
                    data: {
                        "id": data.id
                    },
                    success: function (data) {
                        if (data.flag == 1) {
                            table.ajax.reload();
                        }
                    },
                    error: function () {
                        Ewin.alert({message: "暂停失败"});
                    }
                });
            });
        });
        $('#dataTables tbody').on('click', 'button#resume', function () {
            var data = table.row($(this).parents('tr')).data();
            Ewin.confirm({message: "确定要恢复当前任务吗？"}).on(function (e) {
                if (!e) {
                    return;
                }
                $.ajax({
                    url: "/schedule/resumeJob",
                    type: "post",
                    dataType: "json",
                    data: {
                        "id": data.id
                    },
                    success: function (data) {
                        if (data.flag == 1) {
                            Ewin.alert({message: "恢复成功"});
                            table.ajax.reload();
                        }
                    },
                    error: function () {
                        Ewin.alert({message: "恢复失败"});
                    }
                });
            });
        });
        $('#dataTables tbody').on('click', 'button#runOnce', function () {
            var data = table.row($(this).parents('tr')).data();
            Ewin.confirm({message: "确定要运行一次吗？"}).on(function (e) {
                if (!e) {
                    return;
                }
                $.ajax({
                    url: "/schedule/runOnce",
                    type: "post",
                    dataType: "json",
                    data: {
                        "id": data.id
                    },
                    success: function (data) {
                        if (data.flag === 1) {
                            Ewin.alert({message: "运行完毕"});
                        }
                    },
                    error: function () {
                        Ewin.alert({message: "运行失败"});
                    }
                });
            });
        });
        $('#dataTables tbody').on('click', 'button#edit', function () {
            var data = table.row($(this).parents('tr')).data();
            $.ajax({
                url: "/schedule/selectJob?a=" + new Date(),
                type: "get",
                dataType: "json",
                data: {
                    "id": data.id
                },
                success: function (data) {
                    var flag = data.flag;
                    if (flag === 0) {
                        Ewin.alert({message: "查询失败"});
                    } else {
                        var job = data.job;
                        $("#cronExpression").val(job.cronExpression);
                        $("#jobDesc").val(job.jobDesc);
                        $("#className").val(job.className);
                        $("#hid_id").val(job.id);
                        $("#editSpace").modal('show');
                    }
                },
                error: function () {
                    Ewin.alert({message: "查询失败"});
                }
            });
        });
        $('#dataTables tbody').on('click', 'button#del', function () {
            var data = table.row($(this).parents('tr')).data();
            Ewin.confirm({message: "确定要删除吗，删除了就找不回来了?"}).on(function (e) {
                if (!e) {
                    return;
                }
                $.ajax({
                    url: "/schedule/deleteJob",
                    type: "post",
                    dataType: "json",
                    data: {
                        "id": data.id
                    },
                    success: function (data) {
                        var flag = data.flag;
                        if (flag === 1) {
                            Ewin.alert({message: "删除成功"});
                            table.ajax.reload();
                        }
                    },
                    error: function () {
                        Ewin.alert({message: "删除失败"});
                    }
                });
            });
        });
    });

    function update() {
        var cronExpression = $("#cronExpression").val();
        var jobDesc = $("#jobDesc").val();
        var id = $("#hid_id").val();
        var className = $("#className").val();
        if (cronExpression === null || cronExpression === "") {
            Ewin.alert({message: "时间表达式不能为空"});
            return;
        }
        if (jobDesc === null || jobDesc === "") {
            Ewin.alert({message: "任务别名不能为空"});
            return;
        }
        if (className === null || className === "") {
            Ewin.alert({message: "执行类不能为空"});
            return;
        }
        $.ajax({
            url: "/schedule/saveOrupdateJob",
            type: "post",
            dataType: "json",
            data: {
                "id": id,
                "cronExpression": cronExpression,
                "jobDesc": jobDesc,
                "className": className
            },
            success: function (data) {
                var flag = data.flag;
                if (flag === 1) {
                    Ewin.alert({message: "保存成功"});
                    $("#editSpace").modal('hide');
                    table.ajax.reload();
                }
            },
            error: function (data) {
                Ewin.alert({message: "保存失败"});
            }
        });
    }

    function newCron() {
        window.open("/cron", "_blank", "top=300,left=300,width=900,height=600,menubar=yes,scrollbars=no,toolbar=yes,status=yes");
    }

    function newJob() {
        $("#cronExpression").val("");
        $("#jobDesc").val("");
        $("#hid_id").val("");
        $("#className").val("");
        $("#editSpace").modal('show');
    }
</script>
</body>
</html>