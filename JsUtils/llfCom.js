
/*************llf**************************/
//加上随级数
function addRandom(url){
	var preStr = '?';
	if(url.indexOf(preStr) > 0 )
		preStr='&';
	return url+= preStr + "fresh=" + Math.random(); 
}

 /* 
 * @param url
 * @param param
 * @returns
 */
//为url添加参数
function addParam(url, param){
	var preStr = '?';
	if(url.indexOf(preStr) > 0 )
		preStr='&';
	for(var key in param){
		url += preStr + key+ "="+param[key];
		preStr ='&';
	}	
	return url;
}



/**
 * 打开非分页页面,链接跳转页面时使用， button， a 直接的页面跳转可以用
 */
function openPage(url) {
	//
	if (typeof url === "undefined" || url == 'undefined' || url == '')
		return;

	url = addRandom(url);
	$.ajax({
		type: "get",
		url : url,
		cache : false,
		async : true,
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		success : function(data, textStatus, XMLHttpRequest) {
			var isLogin= XMLHttpRequest.getResponseHeader("login");
	        if(isLogin){
	        	window.top.location = "/login";	        	
	        }else{
	        	$("#frame").html(data);	
	        }
			
		},
		error : function() {
		}
	});
}

/*
 * 只提交表单不跳转
 */
function edit(formid, url){	
	var $form = $("#"+formid);
	if($form.valid()){		
		var params = getFormObj($form);
		submitform(url, params);
	}	
}

/*
 * 检查查询参数合理后再进行查询
 */
function validAndSearch(formid, url){
	var result = {};
	result.flag = true;
	$("#"+formid).find(":text,:password,select,radio,checkbox,textarea").each(function() {

		var validate = $(this).attr("validate");
		if(validate != undefined  && trim(validate) != ""){
			result = eval($(this).attr("validate"));
			if(result.flag == false){
				alertDialog(result.msg);
				return false;
			}
		}
	 });
	if(result.flag == true){
		search(formid, url);
	}
}
/*
 * formid : 数据查询的表单id，提交表单时用 ,
 * url 表单提交地址
 * 直接返回一封装好数据的html文本
 */
function searchListView(formid, url){

	var $form = $("#"+formid);
	var params = getSearchFormObj($form);
	if (typeof url === "undefined" || url == 'undefined' || url == '')
		return;
	url = addRandom(url);
	$.ajax({
		type: "POST",
		url : url,
		data:params,
		//dataType: 'html/text',
		cache : false,
		async : true,
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		success : function(data, textStatus, XMLHttpRequest) {
			var isLogin= XMLHttpRequest.getResponseHeader("login");
	        if(isLogin){
	        	window.top.location = "/login";	        	
	        }else{

	        	$("#frame").html(data);	
	        }
			
		},
		error : function() {
		}
	});
}



//提交表单， url：表单路径， params：表单数据
function submitform(url, params) {
	if (typeof url === "undefined" || url == 'undefined' || url == '')
		return;
	url = addRandom(url);
	$.ajax({
		type: "POST",
		url : url,
		cache : false,
		async : true,
		data:params,
		contentType : "application/x-www-form-urlencoded;charset=utf-8",
		success : function(data, textStatus, XMLHttpRequest) {
			var isLogin= XMLHttpRequest.getResponseHeader("login");
	        if(isLogin){
	        	window.top.location = "/login";	        	
	        }else{
	        	$("#frame").html(data);	
	        }
			
		},
		error : function() {
		}
	});
}

function saveEditData(formId, url, newPageUrl){
	if(!($('#'+formId).valid() )){
		return;
	}
	var obj = getFormObj($("#"+formId));
	var objJson = JSON.stringify(obj);

	//return;
	var operurl;
	if(isEmpty(obj.id)){
		operurl = url.replace("/save.htm", "/add.htm");
	}else{
		operurl = url.replace("/save.htm", "/update.htm");
	}
	$.ajax({
		type: "post",
		url : operurl,
		data: {"objJson":objJson},
		cache : false,
		async : true,
		dataType: "json",
		success : function(data, textStatus, XMLHttpRequest) {
			var isLogin= XMLHttpRequest.getResponseHeader("login");
	        if(isLogin){
	        	window.top.location = "/login";	        	
	        }else{
	        	alertDialog(data.msg);
	        	openPage(newPageUrl);
	        }			
		}
	});
}

/**
 * 提交表单并自动跳转到其他页面
 * @param formid
 * @param submitUrl 表单提交地址
 * @param openPageUrl 表单提交成功后的跳转地址（比如修改完成后可能需要调到列表页面）
 */
function editAndOpenPage(formid, submitUrl, openPageUrl){
	var $form = $("#"+formid);
	if($form.valid()){		
		var params = getFormObj($form);
		$.ajax({
			type: "POST",
			url : submitUrl,
			data: params,
			dataType:'json',
			success : function(data, textStatus, XMLHttpRequest) {
				var isLogin= XMLHttpRequest.getResponseHeader("login");
		        if(isLogin){
		        	window.top.location = "/login";	        	
		        }else{
		        	if(isNotEmpty(data.msg)){
		        		alertDialog(data.msg);
		        	}
					if(data.success == 1){
						openPage(openPageUrl);
					}
		        }
								
			}
		});
	}	
}

/**在当前页面删除一条记录 并刷新页面
 * @param operUrl 数据提交地址
 * @param formid 提交form表单
 * @param newPageUrl 提交成功后的跳转地址
 * @param isSure 跳转前是否需要跳出确认弹框 ，boolean 类型
 */
function deleteAndSearch(deleteUrl, earchBT, needComfirm){
	if(needComfirm == undefined || isEmpty(needComfirm)){
		needComfirm = true;
	}
	if(needComfirm == true){
		confirmDialog({
    	    content: '确定要删除该条记录 ？',
    	    btns: function (item) {
    	    	$.ajax({
    				type: "POST",
    				url : deleteUrl,
    				dataType:'json',
    				success : function(data, textStatus, XMLHttpRequest) {
    					var isLogin= XMLHttpRequest.getResponseHeader("login");
    			        if(isLogin){
    			        	window.top.location = "/login";	        	
    			        }else{
    			        	if(isNotEmpty(data.msg)){
    			        		alertDialog(data.msg);
    			        	}
    						if(data.success == 1){
    							$("#"+earchBT).click();
    						}
    			        }
    				}
    			});
    	    	$("#confirmDialog").remove();
    	    }
		});
	}else{
		$.ajax({
			type: "POST",
			url : deleteUrl,
			dataType:'json',
			success : function(data, textStatus, XMLHttpRequest) {
				var isLogin= XMLHttpRequest.getResponseHeader("login");
		        if(isLogin){
		        	window.top.location = "/login";	        	
		        }else{
		        	if(isNotEmpty(data.msg)){
		        		alertDialog(data.msg);
		        	}
					if(data.success == 1){
						$("#"+earchBT).click();
					}
		        }
			}
		});
	}
	
}
/**
 * 操作成功后再跳转到其他页面
 * @param operUrl 数据提交地址
 * @param params 提交的数据，json格式
 * @param newPageUrl 提交成功后的跳转地址
 */
function operationAndOpenPage(operUrl, params, newPageUrl){
	$.ajax({
		type: "POST",
		url : operUrl,
		data: params,
		dataType:'json',
		success : function(data, textStatus, XMLHttpRequest) {
			var isLogin= XMLHttpRequest.getResponseHeader("login");
	        if(isLogin){
	        	window.top.location = "/login";	        	
	        }else{
	        	if(isNotEmpty(data.msg)){
	        		alertDialog(data.msg);
	        	}
				if(data.success == 1){
					openPage(newPageUrl);
				}
	        }
		}
	});
}

/**在当前页面操作 并刷新页面
 * @param operUrl 数据提交地址
 * @param formid 提交form表单
 * @param newPageUrl 提交成功后的跳转地址
 * @param isSure 跳转前是否需要跳出确认弹框 ，boolean 类型
 */
function operationAndSearch(operUrl, formid, newPageUrl){
	var $form = $("#"+formid);
	var params = getSearchFormObj($form);
	operationAndOpenPage(operUrl, params, newPageUrl);
}

/*
 * 先确认，再进入新页面
 */
function leaveToNewPage(newPageUrl){
	if(confirm('确定要离开当前页面 ？')){
		openPage(newPageUrl);
   	}
}


//去左右空格; 
String.prototype.trimAll = function()  {
	return this.replace(/(^\s*)|(\s*$)/g, "").replace(/[\r\n]/g,"");
}

/*
 * 将form表单（或其他的元素）中的含有name属性的数据组装成Object对象，
 * name="developUser.name" 或  name="name" 
 * @Param $form一个页面元素封装成的$对象,
 * 使用方法： getFormObj($("#productDiv"))
 */
function getFormObj($form){
	//
	var o = {};
	var $inputEle = $form.find("input.formInp");
	var $textareaEle = $form.find("textarea.formTextarea");
	$inputEle.each(function(i, ele){
		//
		var $ele = $(ele);
		if($ele.hasClass("textForSelect")){
			if(isNotEmpty(trim($ele.attr("selectId")))){
				o[$ele.attr("name")] = trim($ele.attr("selectId"));
			}
		}else{
			if(isNotEmpty(trim($ele.val()))){
				o[$ele.attr("name")] = trim($ele.val());
			}
		}
	});
	$textareaEle.each(function(i, ele){
		var $ele = $(ele);
		o[$ele.attr("name")] = trim($ele.val());
	});
	//
	var o2 = {};
	//
	for(var ele in o){//用javascript的for/in循环遍历对象的属性 
		//
		
		var nameArry = ele.split(".");
		if(nameArry.length > 1){
			if(isEmpty(o2[nameArry[0]])){
				var nativeobj2 = {};
				nativeobj2[nameArry[1]] = o[ele];
				o2[nameArry[0]] = nativeobj2;
			}else{
				o2[nameArry[0]][nameArry[1]] = o[ele];
			}
			
		}else{
			o2[ele] =  o[ele];
		}
	} 
	
	return o2;
}

function getSearchFormObj($form){
	//
	var o = {};
	var $inputEle = $form.find("input.inp");
	var $textareaEle = $form.find("textarea.formTextarea");
	$inputEle.each(function(i, ele){
		var $ele = $(ele);
		if($ele.hasClass("textForSelect")){
			o[$ele.attr("name")] = trim($ele.attr("selectId"));
		}else{
			o[$ele.attr("name")] = trim($ele.val());
		}
	});
	$textareaEle.each(function(i, ele){
		var $ele = $(ele);
		o[$ele.attr("name")] = trim($ele.val());
	});
	
	return o;
}

/*
 * 将table tr行（或其他的元素）中的含有name属性的数据组装成Object对象，
 * name="developUser.name" 或  name="name" 
 * @Param $form一个页面元素封装成的$对象,
 * 使用方法： getFormObj($("#productDiv"))
 */
function getTrObj($tr){
	var o = {};
	var $inputEle = $tr.find("input.tableInp");
	var $textareaEle = $tr.find("textarea.tbTextarea");
	$inputEle.each(function(i, ele){
		var $ele = $(ele);
		if($ele.hasClass("textForSelect")){
			if(isNotEmpty(trim($ele.attr("selectId")))){
				o[$ele.attr("name")] = trim($ele.attr("selectId"));
			}
		}else{
			if(isNotEmpty(trim($ele.val()))){
				o[$ele.attr("name")] = trim($ele.val());
			}
		}
	});
	$textareaEle.each(function(i, ele){
		var $ele = $(ele);
		o[$ele.attr("name")] = trim($ele.val());
	});
	//
	var o2 = {};
	//
	for(var ele in o){//用javascript的for/in循环遍历对象的属性 
		//
		
		var nameArry = ele.split(".");
		if(nameArry.length > 1){
			if(isEmpty(o2[nameArry[0]])){
				var nativeobj2 = {};
				nativeobj2[nameArry[1]] = o[ele];
				o2[nameArry[0]] = nativeobj2;
			}else{
				o2[nameArry[0]][nameArry[1]] = o[ele];
			}
			
		}else{
			o2[ele] =  o[ele];
		}
	} 
	
	return o2;
}

/**
 * 判断选择的时间是否大于当前系统时间
 * @param dateStr
 * @returns {Boolean}
 */
function isBeforeDate(dateStr){
	dateStr = dateStr.replace(/-/g, '/'); // "2010/08/01";
	var Ddate = new Date(dateStr);
	var nowDate = new Date();
	if(Ddate > nowDate){
		return false;
	}else{
		return true;
	}
}




/*
 * 初始化下拉框Option选项，
 * selectedValue:初始化被选中的option
 */
function getLis(optionJsonEleId, propname, jsonData){
	var jsonOption;
	if(isNotEmpty(jsonData)){
		jsonOption = jsonData;
	}else{
		jsonOption = eval('('+$("#"+optionJsonEleId).val()+')'); 
	}
	var lis = "";
	if(propname == null){
		propname = "name";
	}
	$(jsonOption).each(function(i, obj){
		lis += '<li onclick="hideSelectDiv(this);" value="'+obj.id+'">'+obj[propname+""]+'</li>';
	});
	return lis;
}

function setLisByExam(datalist, $ulele){
	var lis="";
	var liExample = $ulele.next("ul.example").html();
	$(datalist).each(function(i, obj){
		var li = liExample;
		if(isEmpty(liExample)){
			$ulele.html("");
			return false;
		}else{
			//
			var replaceKeys = $ulele.attr("replaceKeys").split(";");
			
			for(var j=0; j<replaceKeys.length; j++){
				var replaceStr = "${"+replaceKeys[j]+"}";
				//
				li = li.replace(replaceStr, obj[replaceKeys[j]+'']);
			}
			lis += li;
		}
	});
	$ulele.html(lis);
}
/*
 * 初始化枚举类型下拉框Option选项，
 * selectedValue:初始化被选中的option
 */
function getEnumLis(optionJsonEleId){
	var jsonOption = eval('('+$("#"+optionJsonEleId).val()+')'); 
	var lis = "";
	
	$(jsonOption).each(function(i, obj){
		lis += '<li onclick="hideSelectDiv(this);" value="'+obj.name+'">'+obj.title+'</li>';
	});
	return lis;
}

/*
 * 给页面上的table行添加一行，使用用例 可见 新增产品数据中 的新增产品功能，新增产品的竞品
 * @Param ele “添加” 按钮所在元素，用于定位所操作的table
 */
function addRow(ele){

	var tbody =  $(ele).parents("li.tableLi").find("tbody");
	var tdhtml = tbody.find("tr.first").html();
	tbody.append("<tr>"+tdhtml+"</tr>");
}

function removeRow(ele){

	var tbody =  $(ele).parents("tr"). remove();
	
}

/**
 * 将选择的项赋值到 textForSelect
 * 隐藏选择列表
 * @param ele
 */ 
function hideSelectDiv2(ele) {
	//
    var selectText = $(ele).text();
    var selectId = $(ele).attr("value");
    //
    $(ele).parents(".selectDiv").prev(".textForSelect").val(selectText);
    $(ele).parents(".selectDiv").prev(".textForSelect").attr("selectId", selectId);
    hideMenu();
    removeErrorTip(ele);
   
}

function resetNextUl(ele, url, queryNme, nextEleUlId){

	var selectId =  trim($(ele).attr("value"));
	var oldSelectId = $(ele).parents("div.selectDiv").prev(".textForSelect").attr("selectId");
	if(isNotEmpty(selectId) ){
		var nextUl =null;
		var nextInput = null;
		//如果有指定的nextUl，就直接用
		if(isNotEmpty(nextEleUlId)){
			nextUl = $("#"+nextEleUlId);
			nextInput = nextUl.parent(".selectDiv").prev("input.textForSelect");
		}else{//否则就默认是当前节点的临近下一节点
			nextUl = $(ele).parents("td").next("td").find("ul") || $(ele).parents("li").next("li").find("ul");
			nextInput = $(ele).parents("td").next("td").find("input.textForSelect") || $(ele).parents("li").next("li").find("input.textForSelect");
		}
		if(isEmpty(nextUl) || isEmpty(nextInput)){
			return;
		}
		var propname = nextUl.attr("val");
		var param={};
		param.Q_E_deleteStatus_NEQ = "DISABLE";
		param[queryNme+""] = selectId;
		$.ajax({
			type: "post",
			url : url,
			data: param,
			cache : false,
			async : true,
			dataType: "json",
			success : function(data, textStatus, XMLHttpRequest) {
				var isLogin= XMLHttpRequest.getResponseHeader("login");
		        if(isLogin){
		        	window.top.location = "/login";	        	
		        }else{
		        	if(data.list.length > 0){//隐藏option，这里用移除
		        		var lihtml = getLis(null, propname, data.list);
		        		nextUl.html(lihtml);
		        		var nextElectId = nextInput.attr("selectId");
		        		var nextEelectedLi = nextUl.find("li[value='"+nextElectId+"']");
		        		if(isEmpty(nextElectId) || nextEelectedLi.length == 0){
		        			nextInput.val("");
			        		nextInput.attr("selectId", "");
		        		}else{
		        			nextEelectedLi.click();
		        		}
		        	}else{//显示option，这里用添加option
		        		nextUl.html("");
		        		nextInput.val("");
		        		nextInput.attr("selectId", "");
		        	}
		        	
		        }			
			}
		});

	}
}

function resetNextUlByExamLi(ele, url, queryNme, nextEleUlId){
	//
	var selectId =  trim($(ele).attr("value"));
	var oldSelectId = $(ele).parents("div.selectDiv").prev(".textForSelect").attr("selectId");
	if(isNotEmpty(selectId) ){
		var nextUl =null;
		var nextInput = null;
		//如果有指定的nextUl，就直接用
		if(isNotEmpty(nextEleUlId)){
			nextUl = $("#"+nextEleUlId);
			nextInput = nextUl.parent(".selectDiv").prev("input.textForSelect");
		}else{//否则就默认是当前节点的临近下一节点
			nextUl = $(ele).parents("td").next("td").find("ul") || $(ele).parents("li").next("li").find("ul");
			nextInput = $(ele).parents("td").next("td").find("input.textForSelect") || $(ele).parents("li").next("li").find("input.textForSelect");
		}
		if(isEmpty(nextUl) || isEmpty(nextInput)){
			return;
		}
		var param={};
		param.Q_E_deleteStatus_NEQ = "DISABLE";
		param[queryNme+""] = selectId;
		$.ajax({
			type: "post",
			url : url,
			data: param,
			cache : false,
			async : true,
			dataType: "json",
			success : function(data, textStatus, XMLHttpRequest) {
				var isLogin= XMLHttpRequest.getResponseHeader("login");
		        if(isLogin){
		        	window.top.location = "/login";	        	
		        }else{
		        	if(data.list.length > 0){//隐藏option，这里用移除
		        		var lihtml = "";
		        		setLisByExam(data.list, nextUl);
		        		var nextSelectedIdStr = nextInput.attr("selectId");
		        		var nextSelectedIdArr = [];
		        		//
		        		nextInput.val("");
		        		nextInput.attr("selectId", "");
		        		if(isNotEmpty(nextSelectedIdStr)){
		        			nextSelectedIdArr = nextSelectedIdStr.split(";");
		        			$(nextSelectedIdArr).each(function(j, nextSelectedId){

		        				var selLi = nextUl.find("li[value='"+nextSelectedId+"']");
		        				nextUl.find("li[value='"+nextSelectedId+"']").click();
		        			});
		        		}else{
		        			nextInput.val("");
			        		nextInput.attr("selectId", "");
		        		}
		        	}else{//显示option，这里用添加option
		        		nextUl.html("");
		        		nextInput.val("");
		        		nextInput.attr("selectId", "");
		        	}
		        	
		        }			
			}
		});

	}
}

//formid : 数据查询的表单id，提交表单时用 
function search(formid, url, tableId, pageDivClass, nativLoadDataFunc){
	if($("#"+formid).find("button.searchBtn").hasClass("hide")){
		return;
	}
	var $form = $("#"+formid);
	var params = getSearchFormObj($form);
	searchListData(url, params, tableId, pageDivClass, nativLoadDataFunc);
}

/*发送请求到url，查询数据并将数据通过loadDataFuncName （函数名）加载到tableId所在的table上
 * @Param  url 向后台发送请求的地址
 * @Param  params 向后台发送的请求参数
 * @Param  loadDataFuncName 自定义的数据加载函数
 * @Param  tableId 数据要加载到的table的id
 */
function searchListData(url, params, tableId, pageDivClass, nativLoadDataFunc) {
	if (typeof url === "undefined" || url == 'undefined' || url == '')
		return;
	url = addRandom(url);
	$.ajax({
		type: "POST",
		url : url,
		cache : false,
		async : true,
		data:params,
		dataType: 'json',
		success : function(data, textStatus, XMLHttpRequest) {
			var isLogin= XMLHttpRequest.getResponseHeader("login");
	        if(isLogin){
	        	window.top.location = "/login";	        	
	        }else{
	        	//
	        	if(isEmpty(nativLoadDataFunc)){
	        		loadData(data.page.content, tableId);
	        		clearpageNumSize(null);
	        	}else{
	        		eval(nativLoadDataFunc).call(null, data.page.content, tableId);
	        		clearpageNumSize(null);
	        	}
	        	
	        	//重置分页视图
	        	if(isNotEmpty(pageDivClass)){
	        		loadpage($("div."+pageDivClass), data.page);
	        	}
	        	
	        	if(isNotEmpty($("#initFunc").val())){
	        		eval($("#initFunc").val()).call(null);
	        	}
	        	
	        }
			
		},
		error : function() {
		}
	});
}


function loadData(content, tbId){
	//
	var tbody = '';
	
	if(content != undefined && content.length>0){
		var thNames = getTableThNames($("#"+tbId));
		var combinedata = $("#"+tbId+" thead").attr("combinedata");
		var detailUrl = $("#"+tbId).find("thead tr").attr("detailUrl");
		var ExampleoperTd = $("#"+tbId).find("thead td.operateTd");
		var operTd = "";
		if(ExampleoperTd.length > 0){
			ExampleoperTd = $("#"+tbId).find("thead td.operateTd").html();
		}else{
			ExampleoperTd = "";
		}
		$(content).each(function(index,obj){
			//
			var trclickHtml = 'getDetailView(\''+detailUrl+'\', \''+obj.id+'\');';
			if(isEmpty(combinedata)){// 不需要合并行
				tbody+= '<tr objid="'+obj.id+'"><td onclick="'+trclickHtml+'"><a title="点击查看详情">'+(index+1)+'</a></td>';
				$(thNames).each(function(i, ele){
					tbody += '<td >'+getProp(obj, ele, "--")+'</td>';
				});

				if(ExampleoperTd.length > 0){
					operTd = ExampleoperTd.replace(new RegExp(/(\*\*\*\*)/g), obj.id);
					tbody+= '<td class="operateTd">'+operTd+'</td></tr>';
				}else{
					tbody+= '</tr>';
				}
			}else{// 需要合并行，将有list
				var obj2 = eval(obj[combinedata+""]);
				if(obj2.length == 0){//行组无数据
					tbody+= '<tr objid="'+obj.id+'" ><td onclick="'+trclickHtml+'"><a title="点击查看详情">'+(index+1)+'</a></td>';
					$(thNames).each(function(i, ele){
						if(isEmpty(ele.listKey)){//非行组列
							tbody += '<td >'+getProp(obj, ele, "--")+'</td>';
						}else{//行组列
							tbody += '<td ></td>';
						}
					});
					if(ExampleoperTd.length > 0){
						operTd = ExampleoperTd.replace(new RegExp(/(\*\*\*\*)/g), obj.id);
						tbody+= '<td class="operateTd">'+operTd+'</td></tr>';
					}else{
						tbody+= '</tr>';
					}
				}else{//行组有数据-需要构造多个行
					$(obj2).each(function(i, ele2){
						tbody+= '<tr objid="'+obj.id+'" ><td onclick="'+trclickHtml+'"><a title="点击查看详情">'+(index+1)+'</a></td>';
						$(thNames).each(function(i, ele){
							if(isEmpty(ele.listKey)){//非行组列
								tbody += '<td >'+getProp(obj, ele, "--")+'</td>';
							}else{//行组列
								tbody += '<td >'+getProp(ele2, ele, "--")+'</td>';
							}
						});
						if(ExampleoperTd.length > 0){
							operTd = ExampleoperTd.replace(new RegExp(/(\*\*\*\*)/g), obj.id);
							tbody+= '<td class="operateTd">'+operTd+'</td></tr>';
						}else{
							tbody+= '</tr>';
						}
					});
				}
			}
			
		});
	}else{
		//debugger;
		var thnums = $("#"+tbId).find("thead tr th").length;
		tbody = '<tr><td colspan="'+thnums+'">没有记录</td></tr>';
	}
	$("#"+tbId+" tbody").html(tbody);
	//// 传入的参数是对应的列数从0开始，当前以index=1的竞品名称作为合并依据
	if(isNotEmpty(combinedata)){
		$("#"+tbId+" tbody").rowspan(1); 
	}
	setAuth();
	//阻止编辑、删除按钮的冒泡事件
	/*$("#"+tbId+" tbody tr td a").each(function(i, a){
		a.addEventListener('click', function(e){e.stopPropagation()});
	});*/
}

function getTableThNames($tb){
	var thNames = [];
	$tb.find("thead th").each(function(i, ele){
		var key = $(ele).attr("key");
		if(isNotEmpty(key)){
			var obj = {};
			obj['key'] = key;
			obj['listKey'] = $(ele).attr("listKey");
			thNames.push(obj);
		}
	});
	return thNames;
}

//为编辑或详情页面 设置数据
function setFormData(mapData, formId){
	//
	var itemKey = $("#"+formId).attr("item");
	var obj = mapData[itemKey+""];
	if(obj == null){
		return;
	}
	var nameobj = {};
	//
	$("#"+formId).find("input.formInp").each(function(i, ele){
		//
		nameobj.key = $(ele).attr("name");
		if(isNotEmpty(nameobj.key)){
			if($(ele).hasClass("textForSelect")){
				$(ele).attr("selectId", getProp(obj, nameobj))
			}else{
				$(ele).val(getProp(obj, nameobj));
			}
		}
	});
	$("#"+formId).find("textarea.formTextarea").each(function(i, ele){
		nameobj.key = $(ele).attr("name");
		if(isNotEmpty(nameobj.key)){
			var dd = getProp(obj, nameobj)
			$(ele).val(dd).html(dd);
		}
	});
}

//为编辑或详情页面 设置数据
function setFormSpanData(mapData, formId){
	//
	var itemKey = $("#"+formId).attr("item");
	var obj = mapData[itemKey+""];
	if(obj == null){
		return;
	}
	var nameobj = {};
	//
	$("#"+formId).find("li>span").each(function(i, ele){
		//
		nameobj.key = $(ele).attr("name");
		if(isNotEmpty(nameobj.key)){
			$(ele).html(getProp(obj, nameobj));
		}
	});
	//将无数据的span 设置为无
	$("#"+formId).find("li>span").each(function(i, ele){
		//
		if(isNotEmpty($(ele).attr("name")) &&isEmpty(trim($(ele).html()))){
			$(ele).html("无");
		}
	});
}

//设置ul下拉列表的html
function setUlLis(mapData){
	//设置ul下拉列表的html
	var tduls = $("div.selectDiv ul");
	if(tduls.length > 0){
		tduls.each(function(i, ul){
			var itemKey = $(ul).attr("items");
			if(isNotEmpty(itemKey)){
				//
				setSelectInputUL(eval(mapData[itemKey+""]), $(ul));
			}
		});
	}
}

//为详情页面 的table 设置数据
function setTableDetailData(mapData, tbId){
	var $tb = $("#"+tbId);
	//
	//构建table中的旧数据
	var objListKey =  $("#"+tbId).attr("items");
	var trObjList = eval(mapData[objListKey+""]);
	var tbody = $tb.find("tbody");
	var tdhtml = tbody.find("tr.first").html();
	//添加行
	if(trObjList == null || trObjList.length == 0){
		var tdlength = tbody.find("tr.first td").length;
		tbody.html("<tr><td colspan=\""+tdlength+"\">没有记录</td></tr>");
	}
	if(trObjList.length > 0 ){
		//addRow 添加空行
		$(trObjList).each(function(){
			tbody.append("<tr>"+tdhtml+"</tr>");
		});
		//为空行设置值
		tbody.find("tr").each(function(i, tr){
			if(i == 0){
				return;
			}else{
				setTrDetailData($(tr), trObjList[i-1], i);
			}
		});
	}
}
//为详情页面 的tr行设置数据
function setTrDetailData($tr, obj, trIndex){
	//
	var nameobj = {};
	$tr.find("td:first").html(trIndex);
	$tr.find("td").not(":first").each(function(i, ele){
		nameobj.key = $(ele).attr("name");
		if(isNotEmpty(nameobj.key)){
			$(ele).html(getProp(obj, nameobj));
		}
	});
}

//为编辑页面 的table 设置数据
function setTableData(mapData, tbId){
	var $tb = $("#"+tbId);
	//
	//构建table中的旧数据
	var objListKey =  $("#"+tbId).attr("items");
	var trObjList = eval(mapData[objListKey+""]);
	var tbody = $tb.find("tbody");
	var tdhtml = tbody.find("tr.first").html();
	//添加行
	if(trObjList == null){
		return;
	}
	if(trObjList.length > 0 ){
		//addRow 添加空行
		$(trObjList).each(function(){
			tbody.append("<tr>"+tdhtml+"</tr>");
		});
		//为空行设置值
		tbody.find("tr").each(function(i, tr){
			if(i == 0){
				return;
			}else{
				setTrData($(tr), trObjList[i-1], i);
			}
		});
	}
}
//为编辑页面 的tr行设置数据
function setTrData($tr, obj, trIndex){
	//
	var nameobj = {};
	$tr.find("td:first").html(trIndex);
	$tr.find("input.tableInp").each(function(i, ele){
		nameobj.key = $(ele).attr("name");
		if(isNotEmpty(nameobj.key)){
			if($(ele).hasClass("textForSelect")){
				$(ele).attr("selectId", getProp(obj, nameobj))
			}else{
				$(ele).val(getProp(obj, nameobj));
			}
		}
	});
	$tr.find("textarea.tbTextarea").each(function(i, ele){
		nameobj.key = $(ele).attr("name");
		if(isNotEmpty(nameobj.key)){
			var dd = getProp(obj, nameobj)
			$(ele).val(dd).html(dd);
		}
	});
	
}


//为编辑或详情页面 的下拉列表设置数据
function setSelectInputUL(selectList, $ulele){
	var lis = '';
	var key = $ulele.attr("key");
	var valkey = $ulele.attr("val");
	var selectFuncHtml= $ulele.attr("selectFuncHtml");
	var liExample = trim($ulele.html());
	
	$(selectList).each(function(i, obj){
		var li = liExample;
		if(liExample.length > 0){
			//
			var replaceKeys = $ulele.attr("replaceKeys").split(";");
			
			for(var j=0; j<replaceKeys.length; j++){
				var replaceStr = "${"+replaceKeys[j]+"}";
				//
				li = li.replace(replaceStr, obj[replaceKeys[j]+'']);
			}
			lis += li;
		}else{
			lis += '<li onclick="'+selectFuncHtml+';hideSelectDiv2(this);" value="'+obj[key+'']+'">'+obj[valkey+'']+'</li>';
		}
		
	});
	$ulele.html(lis);
}

//跳转到编辑页面
function getEditView(url, id, ele){
	var $this = $(ele);
	url = addRandom(url);
	$.ajax({
        type: "get",
        url: url,
		async : false,
        dataType: "html",
        success: function (data) {
        	//放置空的html到frame
        	$("#frame").html(data);
           if(isNotEmpty(id)){
        	   $(".formDiv input[name='id']").val(id);
        	   //为frame 添加数据
           }
           searchEditData(id);
           return false;
        }
    });
	
}

function searchEditData(objid) {
	var url = $("#initUrl").val();
	if (typeof url === "undefined" || url == 'undefined' || url == '')
		return;
	var formId =  $("#formId").val();
	var tableIdStr =  $("#tableIds").val();
	var tableIds = [];
	if(isNotEmpty(tableIdStr)){
		tableIds = tableIdStr.split(";");
	}
	url = addRandom(url);
	$.ajax({
		type: "POST",
		url : url,
		cache : false,
		async : true,
		data:{"id" : objid},
		dataType: 'json',
		success : function(data, textStatus, XMLHttpRequest) {
			var isLogin= XMLHttpRequest.getResponseHeader("login");
	        if(isLogin){
	        	window.top.location = "/login";	        	
	        }else{
	        	setUlLis(data);
	        	setFormData(data, formId);
	        	$(tableIds).each(function(i, tableId){
	        		setTableData(data, tableId);
	        	});
	        	//最后将下拉框初始化
	        	initValueSelectDiv();
	        	//执行页面初始化函数
	        	var funcname = $("#onloadFunc").val();
	        	if(isNotEmpty(funcname)){
	        		var funcs = funcname.split(";");
	        		$(funcs).each(function(r, func){
	        			eval(func+"").call(null, data);
	        		});
	        	}
	        }
		}
	});
}

//跳转到编辑页面
function getDetailView(url, id, ele){
	url = addRandom(url);
	$.ajax({
        type: "get",
        url: url,
		async : false,
        dataType: "html",
        success: function (data) {
        	//放置空的html到frame
        	$("#frame").html(data);
           if(isNotEmpty(id)){
        	   $(".formDiv input[name='id']").val(id);
        	   //为frame 添加数据
           }
           searchDetailData(id);
        }
    });
}
function searchDetailData(objid) {

	var url = $("#initUrl").val();
	if (typeof url === "undefined" || url == 'undefined' || url == '')
		return;
	var formId =  $("#formId").val();
	var tableIdStr =  $("#tableIds").val();
	var tableIds = [];
	if(isNotEmpty(tableIdStr)){
		tableIds = tableIdStr.split(";");
	}
	url = addRandom(url);
	$.ajax({
		type: "POST",
		url : url,
		cache : false,
		async : true,
		data:{"id" : objid},
		dataType: 'json',
		success : function(data, textStatus, XMLHttpRequest) {
			var isLogin= XMLHttpRequest.getResponseHeader("login");
	        if(isLogin){
	        	window.top.location = "/login";	        	
	        }else{
	        	setFormSpanData(data, formId);
	        	$(tableIds).each(function(i, tableId){
	        		setTableDetailData(data, tableId);
	        	});
	        }
		}
	});
}


function getEnums(eunmType){
	$.ajax({
		type: "POST",
		url : "enum/getEnums.htm",
		cache : false,
		async : true,
		dataType: 'json',
		data:{'classType': eunmType },
		success : function(data, textStatus, XMLHttpRequest) {
			setUlLis(data);
		}	
	});
}

/**
 * 按钮权限过滤
 * @param obj
 */
function setAuth() {
	 //
    var storage = window.sessionStorage;
    var json = JSON.parse(storage.data);
   
    $("button,a").each(function(i, button){
    	var module = $(button).attr("module");
        var oper = $(button).attr("operator");
        if(isEmpty(module) || isEmpty(oper)){
        	return;
        }
        var valid = false;
        var orOperArr = [];
        var andOperArr = [];
        if(isNotEmpty(module)){
        	orOperArr = oper.split("||");
        	$(orOperArr).each(function(j, oroperOne){
        		andOperArr = oroperOne.split("&&");
        		
        		var andvalid = true;
        		$(andOperArr).each(function(k, andoperOne){
        			 var key = module + "-" + andoperOne;
        			 if(isEmpty(json[key])) {
                         andvalid = false;
                         return false;//跳出循环
                     }
        		});
        		
        		if(andvalid == true){
        			valid = true;
        			return false;//跳出循环
        		}
        	});
        	
        	if(valid == false){
    			$(button).addClass("hide");
    		}
        }
    });
    
}