function loadpage($pageDiv, page){

	if($pageDiv != undefined && $pageDiv.length > 0){
		//
		var $pageObj = $pageDiv.find("#pageObj");
		$pageObj.find("input[name='totalCounts']").val(page.totalElements);
		$pageObj.find("input[name='pageSize']").val(page.size);
		$pageObj.find("input[name='pageNo']").val(page.number+1);
		$pageObj.find("input[name='totalPages']").val(page.totalPages);
		var pageObj = getFormObj($pageDiv.find("#pageObj"));
		pageView2(pageObj);
	}
}


//重置分页视图
function pageView(obj, callback){
	var totalCounts = obj.totalElements;
	var pageSize = obj.size;
	var totalPages =  obj.totalPages;

	var pageNo =  1;
	var id = obj.pageId;
	var visiblePages = 5;
	$("#totalCounts").text(totalCounts);
	if (totalCounts > 0) {
		$("#pagination").jqPaginator({
			totalCounts: totalCounts,
			pageSize: pageSize,
			visiblePages: visiblePages,
			currentPage: pageNo,
			onPageChange: function (num, type) {
				callback(num - 1)
			}
		});
	}
}

//重置分页视图
function pageView2(obj){
	//
	var totalCounts = Number(obj.totalCounts);
    var pageSize = Number(obj.pageSize);
    var totalPages =  Number(obj.totalPages);
    var pageNo =  Number(obj.pageNo);
    var pageId = obj.pageId;
    var visiblePages = 5;
    var sreachBT = obj.sreachBT;
    var formId = obj.formId;
    $('#'+pageId).prevAll(".totalCounts").find("#totalCounts").text(totalCounts);
    //先统一隐藏
	$("#"+pageId).parent(".pageDiv,.pageDiv2").hide();
    if (totalCounts > 0) {
    	$('#'+pageId).jqPaginator({
            totalCounts: totalCounts,
            pageSize: pageSize,
            visiblePages: visiblePages,
            currentPage: pageNo,
            onPageChange: function (num, type) {

            	if("change"==type){
            		$("#"+formId).find("#pageNum").val(num-1);
            		$("#"+formId).find("#pageSize").val(pageSize);
            		$("#"+formId).find("#"+sreachBT).click();
            	}
            }
        });
    	$("#"+pageId).parent(".pageDiv,.pageDiv2").show();
    }
}

function changePage2(ele,  formId, sreachBT){
	var pageNum = parseInt($(ele).parent().find(".textForPage").val());
	var pageSize = parseInt($(ele).parents(".pageDiv").find("#pageObj input[name='pageSize']").val());
	var totalPage = parseInt($(ele).parents(".pageDiv").find("#pageObj input[name='totalPages']").val());
	if(pageNum > totalPage){
		alertDialog("请输入正确的页码！");
	}else{
		$("#"+formId).find("#pageNum").val(pageNum-1);
		$("#"+formId).find("#pageSize").val(pageSize);
		$("#"+formId).find("#"+sreachBT).click();
	}
}

function clearpageNumSize(formId){
	var $from;
	if(isEmpty(formId)){
		$from = $(".searchDiv");
	}else{
		$from = $("#"+formId);
	}
	$from.find("#pageNum").val("");
	$from.find("#pageSize").val("");
}