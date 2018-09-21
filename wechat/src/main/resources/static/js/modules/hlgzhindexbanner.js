$(function () {
    $("#jqGrid").jqGrid({
        url: baseURL + 'hlgzhindexbanner/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true },
			{ label: '标题', name: 'title', index: 'title', width: 50 },
			{ label: 'banner图片', name: 'url', index: 'url', width: 80,formatter:function (value) {
				var url="/hlgzh/"+value;
				return '<img src="'+url+'" title="pic" class="figureShow" height="40px" width="90px">';
			}}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });

	new AjaxUpload('#upload', {
		action: baseURL + 'upload/imgfile?token='+token,
		name: 'file',
		autoSubmit:true,
		responseType:"json",
		data:{},
		onSubmit:function(file, extension){
			if (!(extension && /^(jpg|jpeg|png|gif)$/.test(extension.toLowerCase()))){
				alert('只支持jpg、png、gif格式的图片！');
				return false;
			}
		},
		onComplete : function(file, r){
			console.info(r);
			if (r.code == 0) {
				callBack(r.filename);
			} else {
				alert("上传文件失败");
			}
		}
	});


});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		hlgzhIndexBanner: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.hlgzhIndexBanner = {};
			$("#banner").remove();
			$("#upload").before("<img id='banner' width='500px' height='200px' />");
            //清空富文本信息
            editor.txt.clear();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			vm.hlgzhIndexBanner.content = escape(editor.txt.html().replace(/(")/g,"'"));
			var url = vm.hlgzhIndexBanner.id == null ? "hlgzhindexbanner/save" : "hlgzhindexbanner/update";
			$.ajax({
				type: "POST",
			    url: baseURL + url,
                contentType: "application/json",
			    data: JSON.stringify(vm.hlgzhIndexBanner),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
                            //清空富文本信息
                            editor.txt.clear();
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "hlgzhindexbanner/delete",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(id){
			$.get(baseURL + "hlgzhindexbanner/info/"+id, function(r){
                vm.hlgzhIndexBanner = r.hlgzhIndexBanner;
				$("#banner").attr("src","/hlgzh/"+r.hlgzhIndexBanner.url);
                editor.txt.html(unescape(r.hlgzhIndexBanner.content));
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		}
	}
});