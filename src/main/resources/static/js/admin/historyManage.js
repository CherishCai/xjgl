	/**
	 * @author Cherish
	 * @date 2016年8月21日 下午9:10:05
	 */
	var oTable;
	$(document).ready(function() {
		oTable = $('#otable').DataTable(
			//拼接options参数
			$.extend(true,{},CONSTANT.DATA_TABLES.DEFAULT_OPTION, {
			//ajax配置为function,手动调用异步查询
			"ajax" : function(data, callback, settings) {
				//封装请求参数
				var param = historyManage.getQueryCondition(data);
				$.ajax({
					type : "GET",
					url : "/history/page",//TODO
					cache : false, //禁用缓存
					data : param, //传入已封装的参数
					dataType : "json",
					success : function(result) {
						//异常判断与处理
						if (!result.success) {
							myModalFail("查询失败，" + result.message);
							return;
						}
						var pageInfo = result.data;
						//封装返回数据，这里仅演示了修改属性名
						var returnData = {};
						returnData.draw = result.message;//这里直接自行返回了draw计数器,应该由后台返回
						returnData.recordsTotal = pageInfo.totalElements;
						returnData.recordsFiltered = pageInfo.totalElements;//后台不实现过滤功能，每次查询均视作全部结果
						returnData.data = pageInfo.content;
						//调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
						//此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
						callback(returnData);
					},
					error : function(XMLHttpRequest,textStatus,errorThrown) {
						myModalFail("查询失败");
					}
				});
			},
			"columns" : [
			    CONSTANT.DATA_TABLES.COLUMN.NO,
			    {
					"data" : 'operator'
				}, {
					"data" : 'type'
				}, {
					"data" : 'description'
                }, {
                    "data" : 'createdTime'
				}
				],
			"columnDefs" : [ {
					"searchable" : false,
					"orderable" : false,
					"targets" : "_all"
				}]
		}));//end $('#otable').DataTable($.extend({


        //查询
        $("#btn_search").click(function(){
            //reload效果与draw(true)或者draw()类似,
            //draw(false)则可在获取新数据的同时停留在当前页码,可自行试验
            //oTable.ajax.reload(); oTable.draw(false);
            oTable.draw();
        });
        //重置
        $("#btn_reset").click(function(){
            $("#type").val("");
            $("#description").val("");
            oTable.draw();
        });
        //刷新
        $("#btn_fresh").click(function(){
            oTable.draw(false);
        });

        // 回车键事件
        $("#type").keypress(function(e) {
            if(e.keyCode == 13) {
                $("#btn_search").click();
            }
            return;
        });
        $("#description").keypress(function(e) {
            if(e.keyCode == 13) {
                $("#btn_search").click();
            }
            return;
        });
	});

	//表格的管理机制
	var historyManage = {
		currentItem : null,//储存当前被选中的行
		fuzzySearch : false,//是否模糊查询
		getQueryCondition : function(data) {
			var param = {};
			//组装排序参数
			if (data.order && data.order.length && data.order[0]) {
				switch (data.order[0].column) {
				case 1:
					param.orderColumn = "type";
					break;
				default:
					param.orderColumn = "id";
					break;
				}
				param.orderDir = data.order[0].dir;
			}
			//组装查询参数
			param.fuzzySearch = historyManage.fuzzySearch;
			if (historyManage.fuzzySearch) {//模糊查询
				param.fuzzy = $("#fuzzy-search").val();
			} else {//非模糊查询
                param.type = $("#type").val();
                param.description = $("#description").val();
			}
			//组装分页参数
			param.startIndex = data.start;
			param.pageSize = data.length;

			param.draw = data.draw;

			return param;
		}
	};