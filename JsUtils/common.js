//禁用Enter键表单自动提交  
document.onkeydown = function(event) {  
    var target, code, tag;  
    if (!event) {  
        event = window.event; //针对ie浏览器  
        target = event.srcElement;  
        code = event.keyCode;  
        if (code == 13) {  
            tag = target.tagName;  
            if (tag == "TEXTAREA") { 
            	return true; 
            } else { 
            	return false; 
            }
        }  
    }  
    else {  
        target = event.target; //针对遵循w3c标准的浏览器，如Firefox  
        code = event.keyCode;  
        if (code == 13) {  
            tag = target.tagName;  
            if (tag == "INPUT") { 
            	return false; 
            } else { 
            	return true;
            }   
        }  
    }  
};  

function openPage(url) {
	if (typeof url === "undefined" || url == 'undefined' || url == '')
		return;
	$.ajax({
		type: "get",
		url : url,
		cache : false,
		async : true,
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		success : function(data, textStatus, XMLHttpRequest) {
			$("#frame").html(data);	
		}	
	});
}

/**
 * 跳转页面
 * @param obj
 */
function changePage(obj,callback) {
	var pageNum = parseInt($(obj).parent().find(".textForPage").val());
	if (pageNum) {
		var totalPages = $(obj).closest(".pageDiv").find("#totalCounts").attr("allPages");
		if(totalPages && totalPages >= pageNum){
			$(obj).closest(".pageDiv").find(".pagination").jqPaginator('option', {
			    currentPage: pageNum
			});
			//加载当前分页数据
			callback(pageNum);
		}
	}
}

/**
 * 按钮权限过滤
 * @param obj
 */
function validate(obj) {
    var storage = window.sessionStorage;
    var json = JSON.parse(storage.data);

    var module = obj.attr("module");
    var add = obj.attr("operator");
    var key;

    if(module != "" && add != "") {
        key = module + "-" + add;
    }
    if(!json[key]) {
        obj.hide();
    }
}


/**
 * 展开下拉框
 * @param ele
 */ 
function showSelectDiv(ele) {
    $(ele).addClass("clicked").parent().find(".selectDiv").fadeIn(300).addClass("isVisible");
    $("body").bind("mousedown", onBodyDown);
}

/**
 * 綁定事件
 * @param event
 */
function onBodyDown(event) {
    var ev =  event || window.event;
    if (!($(ev.target).parents(".selectDiv").length>0)&&!$(ev.target).hasClass("selectDiv")) {
        hideMenu();
    }
}

/**
 * 隐藏选择列车列表
 */ 
function hideMenu(ele) {
	 //下拉选项消失时才进行验证
	var formValidFlag = $(".formDiv").parent("form").attr("novalidate");
	var seleName = $(".selectDiv.isVisible").prev(".textForSelect").attr("name");
	//seleName == undefined 表示进入了修改页面，正在重现下拉选项，此时不需要验证
	if(formValidFlag != undefined  && seleName != undefined){
		$(".selectDiv.isVisible").prev(".textForSelect").valid();
	}
	
	$(".selectDiv.isVisible").parent().find(".textForSelect").removeClass("clicked");
	$(".selectDiv.isVisible").addClass("hide");
	$(".selectDiv.isVisible").fadeOut(300).removeClass("isVisible");
    $("body").unbind("mousedown", onBodyDown);
   
   
}

/**
 * 隐藏选择列车列表
 * @param ele
 */ 
function hideSelectDiv(ele) {
    var selectText = $(ele).text();
    var selectId = $(ele).attr("value");
    $(ele).parents(".selectDiv").prev(".textForSelect").val(selectText);
    $(ele).parents(".selectDiv").prev(".textForSelect").attr("selectId", selectId);
    hideMenu(ele);
    removeErrorTip(ele);
    
    
}

/**
 * 隐藏选择列车列表-llf
 * @param ele
 */ 
function hideSelectDiv2(ele) {
	//
    var selectText = $(ele).text();
    var selectId = $(ele).attr("value");
    //
    $(ele).parents(".selectDiv").prev(".textForSelect").val(selectText);
    $(ele).parents(".selectDiv").prev(".textForSelect").attr("selectId", selectId);
    hideMenu(ele);
    removeErrorTip(ele);
}

/**
 * 移除validate 验证提示
 * @param ele
 */ 
function removeErrorTip(ele){
	//
	$(ele).parents(".selectDiv").prev("input.textForSelect").removeClass("error");
	$(ele).parents(".selectDiv").next(".error").remove();
}

/**
 * 隐藏选择列车列表
 * @param ele
 */ 
function toggleSelect(ele, texttOrValue) {
	$(ele).toggleClass("clicked");
    var selectValue = "";
    var selectIds = "";
    $(ele).parent().find("li.clicked").each(function(j, li) {

    	if (selectValue=="") {
            selectValue += $(li).text();
            selectIds += $(li).val();
        } else {
            selectValue += ";" + $(li).text();
            selectIds +=  ";" +$(li).val();
        }
    });
    var selectInput = $(ele).parents(".selectDiv").prev(".textForSelect");
    selectInput.val(selectValue).attr("title", selectValue);
    if(isEmpty(texttOrValue) || texttOrValue == 'value'){
    	selectInput.attr("selectId", selectIds );
    }else if( texttOrValue == 'text'){
    	selectInput.attr("selectId", selectValue );
    }
    removeErrorTip(ele);
}

/**
 * 初始化下拉选项的值
 */
function initValueSelectDiv(){
	$("input.textForSelect").each(function(i, ele){
		var $inputSelect = $(ele);
		var selecIdStr = $inputSelect.attr("selectId");
		 
		if(isNotEmpty(selecIdStr)){
			var selecIdArr = [];
			if(selecIdStr.indexOf(";") > -1){
				selecIdArr = selecIdStr.split(";");
			}else{
				selecIdArr = selecIdStr.split(",");
			}
			//可能是多选框
			$(selecIdArr).each(function(j, selecId){
				var selectedLi = $(ele).next("div.selectDiv").find("li[value='"+selecId+"']");
				if(selectedLi.length == 0){
					selectedLi =getLiByLihtml($(ele).next("div.selectDiv").find("ul"), selecId);
				}
				if(isNotEmpty(selectedLi) && selectedLi.length> 0 ){
					selectedLi.click();
				}
			});
		}
	});
	
}

function getLiByLihtml($ul, Lihtml){
	//
	var $li = null;
	$ul.find("li").each(function(i, li){
		if(trim($(li).html()) == Lihtml){
			$li = $(li);
			return false;
		}
	});
	return $li;
}
/**
 *  判断相同行，循环列
 *  @param colIdx
 */
jQuery.fn.rowspan = function(colIdx) {
    var that;		// 上一个td
    var tdLength;	// 一行td个数
    var cnt = 0;	// tr合并个数
    var className;	// 可合并tr的样式名
    
	$('tr', this).each(function(row) {
		tdLength = $(this).children('td').length;
	    $('td:eq('+colIdx+')', this).filter(':visible').each(function(col) {
	        if (that!=null && $(this).html() == $(that).html()) {
	            var data = $(that).attr("data");
	            if (data == undefined) {
	                className = 'sameTr' + cnt++;
	                $(that).parents('tbody').find('tr').eq(row-1).addClass(className);
	            }
	            $(that).attr("data", 0).parents('tbody').find('tr').eq(row).addClass(className);
	        } else {
	            that = this;
	        }
	    });
	});
	
	for (var i=0; i<cnt; i++) {
		for (var j=0; j<tdLength; j++) {
			this.tableEach(i, j);
		}
	}
    return this;
}

/**
 *  循环合并单元格
 *  @param teamNum
 *  @param colI
 */
jQuery.fn.tableEach = function(teamNum, colI) {
	return this.each(function() {
		var that;
		$('tr.sameTr'+teamNum, this).each(function(row) {
		    $('td:eq('+colI+')', this).filter(':visible').each(function(col) {
		        if (that!=null && $(this).html() == $(that).html()) {
		            var rowspan = $(that).attr("rowSpan");
		            if (rowspan == undefined) {
		                $(that).attr("rowSpan", 1);
		                rowspan = $(that).attr("rowSpan");
		            }
		            rowspan = Number(rowspan)+1;
		            $(that).attr("rowSpan", rowspan);
		            $(this).hide();
		        } else {
		            that = this;
		        }
		    });
		});
	});
}


/**
 * 初始化弹框位置
 */
function dialogPosition() {
    $(".dialog").each(function() {
        if ($(this).is(":visible")) {
            var dialogH = $(this).outerHeight();
            var dialogDivH = $(this).find(".dialogDiv").outerHeight();
            $(this).find(".dialogDiv").css("margin-top", (dialogH-dialogDivH)/2);
        }
    });
}

/**
 * 关闭弹框页面
 * @param ele
 */
function closeCurrent(ele) {
	$(ele).closest(".dialog").hide();
	//clearDialogContent('', $(ele).closest(".dialog"));
}

/**
 * 移除提示弹窗
 */
function closeTip(ele) {
    $(ele).closest(".dialog").remove();
}

/**
 * alert提示弹窗
 * @param content
 */
function alertDialog(content) {
    var html = '';
    html += '<div class="dialog" id="alertDialog">' +
        '<div class="dialogDiv tip">' +
        '<div class="dialogHeader"><span>提示</span></div>' +
        '<div class="dialogBody">' +
        '<div class="dialogContent textCenter">' + content + '</div>' +
        '</div>' +
        '<div class="dialogFooter">' +
        '<button type="button" class="sureBtn" onclick="closeTip(this);">确认</button>' +
        '</div>' +
        '</div>' +
        '</div>';
    $("body").append(html);
    $("#alertDialog").find(".sureBtn").focus();
    dialogPosition();
}
/**
 * confirm提示弹窗
 * @param options
 */
function confirmDialog(options) {
    // 默认参数
    var defaults = {
        content: '',
        btns: function (item) {
            // TODO 处理返回结果
        },
        cancel: function () {
            // TODO 处理返回结果
        }
    };
    // 参数初始化
    var settings = $.extend({}, defaults, options);

    var html = '';
    html += '<div class="dialog" id="confirmDialog">' +
        '<div class="dialogDiv tip">' +
        '<div class="dialogHeader"><span>提示</span></div>' +
        '<div class="dialogBody">' +
        '<div class="dialogContent textCenter">' + settings.content + '</div>' +
        '</div>' +
        '<div class="dialogFooter">' +
        '<button type="button" class="sureBtn confirmSureBtn">确认</button>' +
        '<button type="button" class="cancelBtn confirmCancelBtn" onclick="closeTip(this);">取消</button>' +
        '</div>' +
        '</div>' +
        '</div>';
    $("body").append(html);
    $("#confirmDialog").find(".confirmCancelBtn").focus();
    $(".confirmSureBtn").bind("click", settings.btns);
    $(".confirmCancelBtn").bind("mouseup", settings.cancel);
    dialogPosition();
}

/**
 * 重置表格序号
 * @param id 表格id
 */
function resetCount(id) {
	//debugger;
	$("#"+id).find("tbody tr:not('.first')").each(function(i, ele) {
		$(ele).find("td").eq(0).text(i+1);
	});
}


/**
 * 分页控件
 * @param data 返回的列表数据
 * @param tableId 显示数据的表格ID
 * @param callback 回调函数
 * @returns
 */
function page(data,tableId,callback) {
	var totalCounts = data.totalElements;
    var pageSize = data.size;
    var totalPages = data.totalPages;
    var visiblePages = 5;
    var pageNo = data.number + 1;
    $("#"+tableId).next(".pageDiv").find('#totalCounts').text(totalCounts);
    $("#"+tableId).next(".pageDiv").find('#totalCounts').attr("allPages",totalPages);
    if (totalCounts > 0) {
    	$("#"+tableId).next(".pageDiv").find('#pagination').closest('.pageDiv').show();
    	$("#"+tableId).next(".pageDiv").find('#pagination').jqPaginator({
            totalCounts: totalCounts,
            pageSize: pageSize,
            visiblePages: visiblePages,
            currentPage: pageNo,
            onPageChange: function (num, type) {
                //console.log('当前第' + num + '页');
            	//加载当前分页数据
            	if("change"==type){
            		callback(num);
            	}
            }
        });
    }else{
    	$("#"+tableId).next(".pageDiv").find('#pagination').closest('.pageDiv').hide();
    }
}


/**
 * form表单提交时转为json数据格式
 * 使用：JSON.stringify($("#formId").serializeObject());
 */
$.fn.serializeObject = function() {
    var o = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (o[this.name] !== undefined) {
            if (!o[this.name].push) {
                o[this.name] = [o[this.name]];
            }
            o[this.name].push(this.value || '');
        } else {
            o[this.name] = this.value || '';
        }
    });
    return o;
};

/**
 * 获取url的参数
 * @param name 参数名
 */
function getQueryString(name) {
    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return null;
}

function saveFrontpageUrl (urlstr) {
	var storage = window.sessionStorage;
	storage["FRONT_PAGE_URL"] = urlstr;
}

function getFrontpageUrl() {
	var storage = window.sessionStorage;
	openPage(storage["FRONT_PAGE_URL"]);
}
