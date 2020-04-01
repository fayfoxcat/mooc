$(function(){
    //点击input选择框
    $("#input-school").click(function(){
        $('#school-box').css({
            'top':getPosition($(this)).top + getPosition($(this)).height + "px",
            'left':getPosition($(this)).left + "px",
            'display':'block'
        });
        addProvince();
    })

    //添加城市列表
    function addProvince(){
        var province = $("#province")[0];
        var items = province.getElementsByTagName("a");
        // var items = $("a");

        $(province).html("");    //清空城市列表
        // province.innerHTML = "";    //清空城市列表
        for(var i=0; i<schoolList.length; i++){
            var a = document.createElement("a");
            a.id = schoolList[i].id;
            a.innerHTML = schoolList[i].name;
            if(i==0){    //给第一个城市添加样式
                a.className = "active";
                addSchool(1);
            }
            $("#province").append(a);
        }

        //给城市列表添加onclick事件
        for(var i=0; i<items.length; i++) {
            items[i].onclick = function(){

                //清除同级别a链接的active样式
                var as = this.parentNode.childNodes;
                for(var a=0; a < as.length; a++){
                    as[a].className = "";
                }
                this.className = "active";
                addSchool(this.id);
            }
        }
    }

    //添加学校列表
    function addSchool(id){
        var school = $("#school")[0];
        var items = school.getElementsByTagName("a");
        var schools = schoolList[id-1].school;
        $(school).html("");

        for(var i=0; i<schools.length; i++) {
            var a = document.createElement("a");
            a.id = schools.id;
            a.innerHTML = schools[i].name;
            school.appendChild(a);
        }

        //给学校列表添加onclick事件 修改input内容
        for(var i=0; i<items.length; i++) {
            items[i].onclick = function(){
                $('#school-box').css({
                    'display':' none'
                });
                $("#input-school").val(this.innerHTML);
            }
        }
    }

    /*真实姓名*/
    $("#realname").blur(function(){
        if(util.cnValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });
    /*学历*/
    $("#education").blur(function(){
        if(!util.isEmpty($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });
    /*毕业院校*/
    $("#input-school").click(function(){
        $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
    });
    /*提交申请按钮*/
    $("#apply").click(function(){
        //生成表单对象，用于接收参数并传递给后台
        var formData = new FormData();
        //获取表单数据
        var realname = $("#realname").val();
        var education = $("#education option:selected").val();
        var college = $("#input-school").val();
        var file = $('#file')[0].files[0];
        var title = $("#title").val();
        var verifyCodeActual = $("#apply-code").val();

        if(!util.cnValid(realname)){
            $("#realname").css({ "border":"solid red 1px" });
            toast("id","表单提示","填写有误，必须是2-5个中文汉字");
            return;
        }
        if(util.isEmpty(education)){
            $("#education").css({ "border":"solid red 1px" });
            toast("id","表单提示","请选择学历，不能为空");
            return;
        }
        if(util.isEmpty(college)){
            $("#input-school").css({ "border":"solid red 1px" });
            toast("id","表单提示","请选择毕业院校");
            return;
        }
        if(file===undefined){
            toast("id","表单提示","凭证不能为空！");
            return;
        }
        if (!util.photoValid(file.name)||file.size>1024*1024){
            toast("id","表单提示","图片格式或大小不正确，仅支持大小不得超过1M的png、jpg、jepg图片");
            return;
        }
        if(!verifyCodeActual){
            $("#apply-code").css({ "border":"solid red 1px" });
            toast("id","系统通知","验证码不能为空");
            $("#cpatcha-img").click();
            return;
        }
        formData.append('realname', realname);
        formData.append('education', education);
        formData.append('college', college);
        formData.append('file', file);
        formData.append('title', title);
        formData.append("verifyCodeActual",verifyCodeActual);
        $.ajax({
            url : '/mooc/teacher/applyinfo',
            type : 'POST',
            data : formData,
            contentType : false,
            processData : false,
            cache : false,
            success : function(data) {
                if (data.success) {
                    toast("id","系统通知",data.message);
                } else {
                    toast("id","系统通知",data.message);
                }

            }
        });
    });
});

//获取元素在页面里的位置和宽高
function getPosition(obj) {
    var top = 0;
    var left = 0;
    var width = obj.width;
    var height = obj.height;
    while(obj.parent){
        top += obj.top;
        left += obj.left;
        obj = obj.parent;
    }
    return {"top":top,"left":left,"width":width,"height":height};
}