$(function () {
    userState();
    ThreeData();
    userGraph();

    /*用户模糊查询*/
    $('#search-user').click(function () {
        AuthUserList(1);
        setTimeout("$('.user-manage').click()","100");
    });

    /*点击一级分类只加载对应二级分类*/
    $(document).on('click','.btn-top',function (e) {
        $('#sub-classify').find('tr').each(function (i,item) {
            if($(item).attr('parentCode') ===e.currentTarget.dataset.code){
                $(item).show();
            }else {
                $(item).hide();
            }
        });
        return false;
    });

    /*加载顶级分类列表*/
    function TopClassify() {
        var topfUrl = '/mooc/homeshow/querytopclassify';
        $.getJSON(topfUrl,function(data) {
            if(data.success){
                var topClassify = '';
                data.classifylist.map(function(item, index) {
                    topClassify +='<tr class="btn-top" data-code="'
                        +item.code+'" classify-id="'
                        +item.id+'"><td>'
                        +item.name+'</td><td>'
                        +item.code+'</td><td><button class="classify-modify">'
                        +"修改"+'</button><button class="classify-delete">'
                        +"删除"+'</button></td></tr>';
                });
                $('#top-classify').html(topClassify);
            }
        });
    }

    /*加载二级分类列表*/
    function SubClassify(parentCode){
        var subyUrl = '/mooc/homeshow/subclassifylist';
        $.getJSON(subyUrl,function(data) {
            if(data.success){
                var subClassify = '';
                data.subclassifylist.map(function(item, index) {
                    subClassify +='<tr parentCode="'
                        +item.parentCode+'" classify-id="'
                        +item.id+'"><td>'
                        +item.name+'</td><td>'
                        +item.code+'</td><td>'
                        +item.parentCode+'</td><td><button class="classify-modify">'
                        +"修改"+'</button><button class="classify-delete">'
                        +"删除"+'</button></td></tr>';
                });
                $('#sub-classify').html(subClassify);
            }
        });
    }

    /*添加分类*/
    $("#add-classify").click(function () {
        $("#course-classify").modal("show");
    });
    /*切换至添加一级分类*/
    $("#btn-top").click(function () {
        $("#top-sheet").css({"display":"block"});
        $("#sub-sheet").css({"display":"none"})
    });
    /*切换至添加二级分类*/
    $("#btn-sub").click(function () {
        //给select赋值
        selectValue(".top-name")
        $("#top-sheet").css({"display":"none"});
        $("#sub-sheet").css({"display":"block"})
    });

    /*给一级分类select赋值*/
    function selectValue(element){
        $.getJSON("/mooc/homeshow/querytopclassify",function(data) {
            if(data.success){
                var topClassify ='';
                data.classifylist.map(function(item, index) {
                    topClassify +='<option value="'+item.code+'">'+item.name+'</option>';
                });
                $(element).html('<option value="0">请选择分类</option>'+topClassify);
            }
        });
    }

    /*给二级分类select赋值*/
    function selectSubValue(element){
        var parentCode = $('#top-name option:selected').attr('value');
        var classifyUrl = '/mooc/homeshow/querysubclassify?code='+parentCode;
        $.getJSON(classifyUrl,function(data) {
            if(data.success){
                var subClassify = '';
                data.subcalssifylist.map(function(item, index) {
                    subClassify +='<option value="'
                        +item.code+'">'
                        +item.name+'</option>';
                });
                $(element).html(subClassify);
            }
        });
    }

    /*添加分类：一级分类名*/
    $("#name-classify").blur(function() {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*添加分类：一级分类编码*/
    $("#code-classify").blur(function() {
        if(util.enNumValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*添加分类：上级分类名*/
    $(".top-name").blur(function() {
        if($(".top-name").val()!=='0'){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*添加分类：二级分类名*/
    $(".sub-name").blur(function() {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*添加分类：二级分类编码*/
    $(".sub-code").blur(function() {
        if(util.enNumValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*添加分类函数*/
    $("#submit-classify").click(function () {
        var formData = new FormData();
        var parentCode = $(".top-code").val();
        var classifyName = $("#name-classify").val();
        var classifyCode = $("#code-classify").val();
        var subName = $(".sub-name").val();
        var subCode = $(".sub-code").val();
        console.log($(".top-name").val());
        if(parentCode===''&&$(".top-name").val()!=='0'){//添加一级分类
            /*一级分类名*/
            if(!util.zcValid($("#name-classify").val())){
                $("#name-classify").css({ "border":"solid red 1px"});
                toast("id","表单提示","分类名必须是中文、字母及-、_组合，2-12位！")
                return;
            }
            /*一级分类编码*/
            if(!util.enNumValid($("#code-classify").val())){
                $("#code-classify").css({ "border":"solid red 1px"});
                toast("id","表单提示","分类编码必须是字母或数字及组合！")
                return;
            }
            formData.append("classifyName",classifyName);
            formData.append("classifyCode",classifyCode);

        }else{//添加二级分类
            if($(".top-name").val()==='0'){
                $(".top-name").css({ "border":"solid red 1px"});
                toast("id","表单提示","请选择上级分类！")
                return;
            }
            /*一级分类名*/
            if(!util.zcValid($(".sub-name").val())){
                $(".sub-name").css({ "border":"solid red 1px"});
                toast("id","表单提示","分类名必须是中文、字母及-、_组合，2-12位！")
                return;
            }
            /*一级分类编码*/
            if(!util.enNumValid($(".sub-code").val())){
                $(".sub-code").css({ "border":"solid red 1px"});
                toast("id","表单提示","分类编码必须是字母或数字及组合！")
                return;
            }
            formData.append("parentCode",parentCode);
            formData.append("classifyName", subName);
            formData.append("classifyCode", subCode);

        }
        $.ajax({
            url:("/mooc/admin/addclassify"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                    $("#course-classify").modal("hide");
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*修改分类*/
    $(document).on('click','.classify-modify',function () {
        var $t = $(this).parent().parent();
        $("#classify-name").val($t.find("td").eq(0).text()).attr("cid",$t.attr("classify-id"));
        $("#classify-code").val($t.find("td").eq(1).text());
        $("#modify-classify").modal("show");
        return false;
    });

    /*分类名*/
    $("#classify-name").blur(function() {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }
    });

    /*修改分类函数*/
    $("#save-classify").click(function () {
        var formData = new FormData();
        var $c = $("#classify-name");

        /*分类名*/
        if(!util.zcValid($c.val())){
            $("#classify-name").css({ "border":"solid red 1px"});
            toast("id","表单提示","分类名必须是中文、字母及-、_组合，2-12位！")
            return;
        }

        formData.append("id",$c.attr('cid'));
        formData.append("classifyName",$c.val());
        $.ajax({
            url:("/mooc/admin/modifyclassify"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                    $("#modify-classify").modal("hide");
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*删除指定id的分类*/
    $(document).on('click','.classify-delete',function () {
        var formData = new FormData();
        formData.append("id",$(this).parent().parent().attr("classify-id"));
        $.ajax({
            url:("/mooc/admin/deleteclassify"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*用户登录状态*/
    function userState(){
        var UserStateUrl = '/mooc/useroperator/loginstate';
        $.post(UserStateUrl,function(data) {
            if(data.success){
                var userinfo ='<img src="'
                    +data.userinfo.header
                    +'" alt="个人中心" class="rounded-circle personal-img"/>'
                    +'<span class="user-name">'
                    +data.userinfo.username+'</span>';
                $('.userinfo').html(userinfo);
                $('.img').attr('src',data.userinfo.header);
                $('.user-name').html(data.userinfo.username);
            }else{
                return window.location.href='/mooc/login';
            }
        });
    }

    /*分页获取用户列表*/
    function AuthUserList(pageIndex) {
        var formData = new FormData();
        var role = $("#role option:selected").val();
        var keyword = $('.search').val();
        formData.append('pageIndex',pageIndex);
        formData.append('pageSize',10);
        formData.append('keyWord',keyword);
        formData.append('role',role);
        $.post({
            url:("/mooc/admin/authuserlist"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data) {
                if (data.success && data.count>0) {
                    sessionStorage.setItem('auth-count',data.count);
                    sessionStorage.setItem('auth-page',Math.ceil(data.count/10));
                    var authUserList = '';
                    data.authuserlist.map(function (item, index) {
                        authUserList += '<tr><td user-id="'
                            + item.id+'">'
                            + item.account + '</td><td>'
                            + item.username + '</td><td>'
                            + item.realname + '</td><td gender="'
                            + item.gender+'">'
                            + (item.gender===1?'女':'男') + '</td><td>'
                            + item.education + '</td><td sign="'
                            + item.sign+'">'
                            + (item.title==null?'':item.title) + '</td><td>'
                            + (item.collegeName==null?'':item.collegeName)+ '</td><td>'
                            + item.mobile + '</td><td role="'
                            + item.role+'">'
                            + (item.role>0?(item.role===1?'普通用户':'讲师'):"管理员") + '</td><td status="'
                            + item.status+'">'
                            + (item.status===1?'正常':'无效') + '</td><td><button class="user-modify">'
                            + '修改' + '</button><button class="user-delete">'
                            + '删除' + '</button></td></tr>';
                    });
                }else{
                    authUserList = '<h5 class="null-tip">未查询到相关数据!</h5>';
                }
                $('#user-list').html(authUserList);
            }
        });
    }

    /*分页获取当前课程学习用户*/
    function studyUserList(pageIndex) {
        const formData = new FormData();
        formData.append('pageIndex',pageIndex);
        formData.append('pageSize',10);
        formData.append('courseId',sessionStorage.getItem("courseId"));
        $.post({
            url:("/mooc/admin/studyuserlist"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data) {
                if (data.success && data.count>0) {
                    sessionStorage.setItem('study-count',data.count);
                    sessionStorage.setItem('study-page',Math.ceil(data.count/10));
                    var studyUserList = '';
                    data.studyList.map(function (item, index) {
                        studyUserList += '<tr><td user-id="'
                            + item.id+'">'
                            + item.account + '</td><td>'
                            + item.username + '</td><td gender="'
                            + item.gender+'">'
                            + (item.gender===1?'女':'男') + '</td><td>'
                            + item.mobile + '</td><td>'
                            + new Date(item.birthday).Format("yyyy-MM-dd") + '</td><td status="'
                            + item.status+'">'
                            + (item.status===1?'正常':'已注销') + '</td></tr>';
                    });
                }
                $('#study-user').html(studyUserList);
            }
        });
    }

    /*修改用户模态框*/
    $(document).on('click','.user-modify',function () {
        var $t = $(this).parent().parent().find("td");
        $("#account").val($t.eq(0).text()).attr("uid",$t.eq(0).attr("user-id"));
        $("#status").get(0).selectedIndex=$t.eq(9).attr("status");
        $("#sex").get(0).selectedIndex=$t.eq(3).attr("gender");
        $("#realname").val($t.eq(2).text());
        $("#username").val($t.eq(1).text());
        if($t.eq(4).text()==="专科"){
            $("#education").get(0).selectedIndex=0
        }else if($t.eq(4).text()==="本科"){
            $("#education").get(0).selectedIndex=1
        }else if($t.eq(4).text()==="硕士"){
            $("#education").get(0).selectedIndex=2
        }else if($t.eq(4).text()==="博士"){
            $("#education").get(0).selectedIndex=3
        }else{
            $("#education").get(0).selectedIndex=4
        }
        $("#college").val($t.eq(6).text());
        if($t.eq(5).text()==="初级讲师"){
            $("#title").get(0).selectedIndex=0
        }else if($t.eq(5).text()==="中级讲师"){
            $("#title").get(0).selectedIndex=1
        }else if($t.eq(5).text()==="高级讲师"){
            $("#title").get(0).selectedIndex=2
        }else if($t.eq(5).text()==="特级讲师"){
            $("#title").get(0).selectedIndex=3
        }else{
            $("#title").get(0).selectedIndex=4
        }
        $("#mobile").val($t.eq(7).text());
        $("#user-role").get(0).selectedIndex=$t.eq(8).attr("role");
        if ($t.eq(5).attr("sign")==="null"){
            $("#sign").val('');
        }else{
            $("#sign").val($t.eq(5).attr("sign"));
        }
        $("#modify-user").modal("show");
    });


    /*账户名*/
    $('#account').blur(function () {
        if(util.emailValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*姓名*/
    $("#realname").blur(function() {
        if($(this).val()===''||util.cnValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*昵称*/
    $("#username").blur(function() {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*手机号码*/
    $("#mobile").blur(function() {
        if($(this).val()===''||util.mobileValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*提交用户修改信息*/
    $("#submit-user").click(function () {
        var formData = new FormData();

        if(!util.emailValid($('#account').val())){
            $("#account").css({ "border":"solid red 1px" });
            toast("id","表单提示","账户名是邮箱、邮箱格式不正确！！");
            return;
        }
        if(!$('#realname').val()===undefined&&!util.cnValid($('#realname').val())){
            $("#realname").css({ "border":"solid red 1px" });
            toast("id","表单提示","姓名必须在2-5字符之间！！");
            return;
        }
        if(!util.zcValid($('#username').val())){
            $("#username").css({ "border":"solid red 1px" });
            toast("id","表单提示","昵称必须是中文、字母及-、_组合，2-12位！");
            return;
        }

        if(!$('#mobile').val()===undefined&&!util.mobileValid($('#mobile').val())){
            $("#mobile").css({ "border":"solid red 1px" });
            toast("id","表单提示","手机号码格式不正确，必须是11位数字！");
            return;
        }

        formData.append("id",$('#account').attr("uid"));
        formData.append("account",$('#account').val());
        formData.append("status",$('#status option:selected').val());
        formData.append("sex",$('#sex option:selected').val());
        formData.append("realname",$('#realname').val());
        formData.append("username",$('#username').val());
        formData.append("education",$('#education option:selected').val());
        formData.append("college",$('#college').val());
        formData.append("title",$('#title option:selected').val());
        formData.append("mobile",$('#mobile').val());
        formData.append("role",$('#user-role option:selected').val());
        formData.append("sign",$('#sign').val());
        $.ajax({
            url:("/mooc/admin/modifyinfo"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
                $('#modify-user').modal('hide');
            }
        });
    });

    /*删除指定用户*/
    $(document).on('click','.user-delete',function () {
        var formData = new FormData();
        formData.append("id",$(this).parent().parent().find("td").attr("user-id"));
        $.ajax({
            url:("/mooc/admin/deleteinfo"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*课程排序方式查询*/
    $(document).on('click','.sort',function () {
        sessionStorage.setItem("sort",$(this).attr('value'));
        CourseList(1);
    });

    /*课程模糊查询按钮*/
    $('#search-course').click(function () {
        CourseList(1);
    });

    /*分页获取课程列表*/
    function CourseList(pageIndex) {
        var formData = new FormData();
        var keyword = $('#course-name').val();
        if (keyword===''){
            keyword=null;
        }
        formData.append('keyword',keyword);
        formData.append('pageIndex',pageIndex);
        formData.append('pageSize',6);
        formData.append('sort',sessionStorage.getItem('sort'));
        $.post({
            url:("/mooc/admin/courselist"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data) {
                if (data.success && data.count>0) {
                    sessionStorage.setItem('course-count',data.count);
                    sessionStorage.setItem('course-page',Math.ceil(data.count/6));
                    /*分页课程数据*/
                    var courseList = '';
                    data.courseList.map(function (item, index) {
                        courseList+='<div class="course-content" courseid="'
                            +item.id+'"><div class="row">'
                            +'<div class="col-md-auto course-img"><img src="'
                            +item.picture+'" height="90" width="120" alt=""/></div>'
                            +'<div class="col-md-6 course-info">'
                            +'<div class="course-name">'
                            +item.courseName+'</div><div class="course-brief">'
                            +'简介：'+item.brief+'</div></div><div class="col-md-auto operator">'
                            +'<div class="classify-time-price">'
                            +'<span class="classify">'
                            +item.classifyName+'/'+item.subClassifyName+'</span><span class="time">'
                            +item.courseTime+'</span></div><div class="create-count">'
                            +'<span class="create">'
                            +'上架时间:'+new Date(item.updateTime).Format("yyyy-MM-dd")+'</span>'
                            +'<span class="count">'
                            +item.studyCount+'人在学'+'</span></div>'
                            +'<div class="weight-state-delete">'
                            +'<span class="weight">'
                            +'推荐权重：'+item.weight+'</span><button class="modify-state">'
                            +(item.onsale===1?'下架课程':'上架课程')+'</button><button class="delete-course">'
                            +'删除'+'</button></div></div></div></div>';
                    });
                }else{
                    courseList = '<h5 class="null-tip">未查询到相关数据!</h5>';
                }
                $('#list-content').html(courseList);
            }
        });
    }

    /*课程上下架*/
    $(document).on('click','.modify-state',function () {
        var formData = new FormData(),status = false;
        if($(this).text()==="上架课程"){
            status = true
        }
        formData.append("status",status);
        formData.append("id",$(this).parent().parent().parent().parent().attr("courseid"));
        $.ajax({
            url:("/mooc/admin/coursestatus"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
        return false;
    });

    /*课程删除*/
    $(document).on('click','.delete-course',function () {
        var formData = new FormData();
        formData.append("id",$(this).parent().parent().parent().parent().attr("courseid"));
        $.ajax({
            url:("/mooc/admin/deletecourse"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
        return false;
    });

    /*课程详情页部分*/
    $(document).on('click','.course-content',function () {
        sessionStorage.setItem("courseId",$(this).attr("courseid"));
        $('.content').removeClass('show');
        //加载课程信息
        getCourse();
        $("#course-details").collapse("show");
        //加载章节信息
        getSection();
        $(".nav-link,.c-manage").removeClass('active show');
        $("#section-bar").collapse('show');
        $(".first-nav").addClass('active');
    });

    /*章节、评论、答疑、统计切换*/
    $('.nav-link').click(function () {
        userState();
        $('.page').html('');
        $(".nav-link,.c-manage").removeClass('active show');
        $(this).addClass("active");
        switch ($(this).attr("class").split(' ').shift()) {
            case "section-bar":
                getSection();
                $("#section-bar").collapse('show');
                break;
            case "comment-bar":
                Comment(1,0,"comment-bar");
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('comment-page'), //必填,总页数
                    pageAmount: 10,  //每页多少条
                    dataTotal: sessionStorage.getItem('comment-count'), //总共多少条数据
                    curPage:1, //初始页码,不填默认为1
                    pageSize: 5, //分页个数,不填默认为5
                    showPageTotalFlag:true, //是否显示数据统计,不填默认不显示
                    showSkipInputFlag:true, //是否支持跳转,不填默认不显示
                    getPage: function (page) {
                        Comment(page,0,"comment-bar");
                    }
                });
                $("#comment-bar").collapse('show');
                break;
            case "question-bar":
                Comment(1,1,"question-bar");
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('comment-page'),
                    pageAmount: 10,
                    dataTotal: sessionStorage.getItem('comment-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        Comment(page,1,"comment-bar");
                    }
                });
                $("#question-bar").collapse('show');
                break;
            case "study-bar":
                studyUserList(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('study-page'),
                    pageAmount: 10,
                    dataTotal: sessionStorage.getItem('study-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        studyUserList(page,);
                    }
                });
                $("#study-bar").collapse('show');
                break;
            case "graph-bar":
                courseGraph(sessionStorage.getItem('courseId'));
                $("#graph-bar").collapse('show');
                break;
        }
    });

    /*章节信息*/
    function getSection(){
        var sectionUrl = '/mooc/section/sectionlist?id='+sessionStorage.getItem("courseId");
        $.getJSON(sectionUrl,function(data) {
            if(data.success){
                var chaptermodel = '';
                data.sectionlist.map(function(item, index) {
                    var sectionmodel = '';
                    for(var i=0;i<item.sections.length;i++){
                        sectionmodel +='<div class="section" section-id="'
                            +item.sections[i].id+'" section-name="'
                            +item.sections[i].name+'">'
                            +'<span class="icon-play-fill"></span>'
                            +'<span class="section-name">'
                            +(index+1)+'-'+(i+1)+item.sections[i].name
                            +'</span><span style="float: right">'
                            +'<a href="javascript:" class="modify-section" section-time="'
                            +item.sections[i].time+'" section-url="'
                            +item.sections[i].videoUrl+'">修改</a>'
                            +'<a href="javascript:" class="delete-section" style="margin: 0 10px;">删除</a>'
                            +'</span></div>';
                    }
                    chaptermodel +='<div class="chapter"'
                        +'><div class="chapter-name" section-name="'
                        + item.name+'" section-id="'
                        + item.id+'"><span style="float: right;font-size: 14px;font-weight:normal; ">'
                        +'<a href="javascript:" class="modify-chapter">修改</a>'
                        +'<a href="javascript:" class="delete-section" style="margin: 0 10px;">删除</a></span>'
                        +'第'+(index+1)+'章 '+item.name
                        +'</div>'
                        +sectionmodel+'</div>';
                });
                $('#section-bar').html(chaptermodel);
            }
        });
    }
    /*加载对应课程几基本信息*/
    function getCourse() {
        var courseUrl = '/mooc/course/onecourseinfo?id=' + sessionStorage.getItem("courseId");
        $.getJSON(courseUrl, function (data) {
            var course = data.course;
            if (data.success) {
                /*加载至详情页*/
                $('#curriculum-picture').attr("src",course.picture);
                $('#curriculum-name').text(course.courseName);
                $('#curriculum-brief').text(course.brief);
                $('#curriculum-classify').text(course.classifyName+'/'+course.subClassifyName);
                $('#curriculum-weight').text(course.weight);
                $('#curriculum-time').text(course.courseTime);
                $('#curriculum-create').text(new Date(course.createTime).Format("yyyy-MM-dd"));
                $('#curriculum-count').text(course.studyCount);
                /*加载至修改模态框*/
                $("#course-picture").attr("src",course.picture);
                $("#details-name").val(course.courseName).attr("cid",course.id);
                $("#teacher-name").val(course.ownerName);
                $("#course-weight").val(course.weight);
                $("#course-time").val(course.courseTime);
                $("#course-brief").val(course.brief);
            }
        });
    }

    /*修改课程基本信息*/
    $(".course-modify").click(function () {
        selectValue("#top-name");
        getCourse();
        $("#modify-course").modal("show");
    });

    /*根据一级分类变化给二级分类赋值*/
    $("#top-name").click(function () {
        selectSubValue("#sub-name");
    });


    /*课程名*/
    $("#details-name").blur(function() {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*讲师*/
    $("#teacher-name").blur(function() {
        if(util.cnValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*分类名*/
    $("#top-name").blur(function() {
        if(!util.isEmpty($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }
    });

    /*权重*/
    $("#course-weight").blur(function() {
        if(util.numValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*时常*/
    $("#course-time").blur(function() {
        if(util.numZnValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*简介*/
    $("#course-brief").blur(function() {
        if(!util.isEmpty($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });



    /*修改课程信息函数*/
    $("#save-course").click(function () {
        var formData = new FormData();
        var $c = $("#details-name");

        if($("#course-picture").attr('src')===''){
            toast("id","表单提示","课程封面不能为空！");
            return;
        }
        if ($("#course-img")[0].files[0]!==undefined){
            if (!util.photoValid($("#course-img")[0].files[0].name)||$("#course-img")[0].files[0].size>1024*1024){
                toast("id","表单提示","图片格式或大小不正确，仅支持大小不得超过1M的png、jpg、jepg图片");
                return;
            }
        }

        if(!util.zcValid($c.val())){
            $("#course-name").css({ "border":"solid red 1px"  });
            toast("id","表单通知","课程名在2-16个字符之间！");
            return;
        }
        if(!util.cnValid($("#teacher-name").val())){
            $("#course-owner").css({ "border":"solid red 1px"  });
            toast("id","表单通知","讲师名必须在2-5字符之间！");
            return;
        }

        if(util.isEmpty($("#top-name").val())){
            $("#top-name").css({ "border":"solid red 1px"  });
            toast("id","表单通知","分类不能为空！");
            return;
        }

        if(!util.numValid($("#course-weight").val())){
            $("#course-weight").css({ "border":"solid red 1px"  });
            toast("id","表单通知","权重要求为0-9之间的数字！");
            return;
        }

        if(!util.numZnValid($("#course-time").val())){
            $("#course-time").css({ "border":"solid red 1px"  });
            toast("id","表单通知","要求2-16中文、数字及-符号！");
            return;
        }
        if(util.isEmpty($("#course-brief").val())){
            $("#course-brief").css({ "border":"solid red 1px"  });
            toast("id","表单通知","简介不能为空！");
            return;
        }



        formData.append("id",$c.attr('cid'));
        formData.append("courseName",$c.val());
        formData.append("file",$("#course-img")[0].files[0]);
        formData.append("ownerName",$("#teacher-name").val());
        formData.append("classifyName",$("#top-name option:selected").text());
        formData.append("classify",$("#top-name").val());
        formData.append("subClassifyName",$("#sub-name option:selected").text());
        formData.append("subClassify",$("#sub-name").val());
        formData.append("weight",$("#course-weight").val());
        formData.append("time",$("#course-time").val());
        formData.append("brief",$("#course-brief").val());
        $.ajax({
            url:("/mooc/admin/modifycourse"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                    $("#modify-course").modal("hide");
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*修改章节*/
    $(document).on('click','.modify-section , .modify-chapter',function () {
        $("#sheet-section").css({"display":"none"});
        $("#chapter-name").val($(this).parent().parent().attr("section-name")).attr("section-id",$(this).parent().parent().attr("section-id"));
        if($(this).attr("class")!=="modify-chapter"){
            $("#video-url").val($(this).attr("section-url"));
            $("#video-time").val($(this).attr("section-time"));
            $("#sheet-section").css({"display":"block"});
        }
        $("#modify-section").modal("show");
    });

    /*章名*/
    $(document).on('blur','#chapter-name',function () {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*url*/
    $(document).on('blur','#video-url',function () {
        if(util.urlValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });


    /*时常*/
    $(document).on('blur','#video-time',function() {
        if(util.timeValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*修改章节信息函数*/
    $("#save-section").click(function () {
        var formData = new FormData();
        var $c = $("#chapter-name");

        if(!util.zcValid($c.val())){
            $c.css({ "border":"solid red 1px"  });
            toast("id","表单通知","格式不正确，要求2-16中文、字母、数字及-符号！");
            return;
        }

        if(!util.urlValid($("#video-url").val())){
            $("#video-url").css({ "border":"solid red 1px"  });
            toast("id","表单通知","url地址格式不正确！");
            return;
        }

        if(!util.timeValid($("#video-time").val())){
            $("#video-time").css({ "border":"solid red 1px"  });
            toast("id","表单通知","格式不正确，参考10:10！");
            return;
        }

        formData.append("id",$c.attr("section-id"));
        formData.append("name",$c.val());
        formData.append("url",$("#video-url").val());
        formData.append("time",$("#video-time").val());
        $.ajax({
            url:("/mooc/admin/modifysection"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                    $("#modify-section").modal("hide");
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*删除章节信息函数*/
    $(document).on("click",".delete-section",function () {
        var formData = new FormData();
        formData.append("id",$(this).parent().parent().attr("section-id"));
        $.ajax({
            url:("/mooc/admin/deletesection"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*分页加载评论*/
    function Comment(pageIndex,type,element) {
        //拼接出查询条件的url
        var url = '/mooc/course/commentlist?id='+sessionStorage.getItem("courseId")+'&pageIndex='+pageIndex+'&pageSize=10&type='+type;
        $.getJSON(url, function(data) {
            if (data.success && data.count>0) {
                /*分页加载评论*/
                sessionStorage.setItem('comment-count',data.count);
                sessionStorage.setItem('comment-page',Math.ceil(data.count/10));
                var commentList = '';
                data.courseCommentList.map(function(item, index) {
                    commentList += '<div class="details">'
                        +'<div class="comment"><img src="'
                        +item.header+'" alt="..." class="img-circle user-img">'
                        +'<span class="comment-name">'
                        +item.username+'</span>'
                        +'<span class="comment-time">'
                        +new Date(item.updateTime).Format("yyyy-MM-dd-hh:mm")+'</span></div>'
                        +'<div class="comment-text">'
                        +item.content+'<a href="javascript:" class="delete-comment" style="float:right" comment-id="'
                        +item.id+'">删除</a></div></div>';
                });
            }else{
                commentList = '<h5 class="null-tip">未查询到相关数据!</h5>';
            }
            $('#'+element).html(commentList);
        });
    }

    /*删除违规评论*/
    $(document).on("click",".delete-comment",function () {
        var formData = new FormData();
        formData.append("id",$(this).attr("comment-id"));
        $.ajax({
            url:("/mooc/admin/deletecomment"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*加载轮播图列表*/
    function CarouselList(count) {
        //拼接出查询条件的url
        var url = '/mooc/homeshow/querysitecarousel?count='+count;
        $.getJSON(url, function(data) {
            if (data.success) {
                var carousellist = '';
                data.sitecarousellist.map(function(item, index) {
                    carousellist += '<div class="carousel-content" carousel-id="'
                        +item.id+'"><div class="row">'
                        +'<div class="col-md-auto carousel-img"><img src="'
                        +item.picture+'" height="140" width="200"/></div><div class="col-md-6">'
                        +'<div class="carousel-name">'
                        +item.name+'</div><div class="carousel-url" url="'
                        +item.url+'">'
                        +'链接:'+item.url+'</div></div>'
                        +'<div class="col-md-auto"><div class="carousel-weight" weight="'
                        +item.weight+'">'
                        +'推荐权重:'+item.weight+'</div><button class="carousel-modify" enable="'
                        +item.enable+'">'
                        +'修改'+'</button><button class="carousel-delete">'
                        +'删除'+'</button></div></div></div>';
                });
                $('#carousel-content').append(carousellist);
            }
        });
    }

    /*添加、修改轮播图*/
    $(document).on('click','.carousel-modify,#add-carousel ',function () {
        if($(this).attr('id')==="add-carousel"){

        }else{
            $("#carousel-picture").attr('src',$(this).parent().parent().find('div').eq(0).find('img').attr('src'));
            $("#carousel-name").val($(this).parent().prev().find('div').eq(0).text()).attr("slid",$(this).parent().parent().parent().attr("carousel-id"));
            $("#carousel-url").val($(this).parent().prev().find('div').eq(1).attr("url"));
            $("#carousel-weight").val($(this).prev().attr('weight'));
            $("#carousel-status").get(0).selectedIndex = $(this).attr('enable');
        }
        $("#carousel").modal("show");
    });

    /*轮播图名*/
    $("#carousel-name").blur(function () {
        if(util.zcValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });

    /*url*/
    $("#carousel-url").blur(function () {
        if(util.urlValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });


    /*权重*/
    $("#carousel-weight").blur(function () {
        if(util.numValid($(this).val())){
            $(this).css({ "border":"solid #3db057 1px", "background-color":'#e8f0fe' })
        }else{
            $(this).css({ "border":"solid red 1px"})
        }
    });


    /*添加、修改轮播函数*/
    $("#save-carousel").click(function () {
        var formData = new FormData();
        var $name = $("#carousel-name");

        if($("#carousel-picture").attr('src')===''){
            toast("id","表单提示","轮播图不能为空！");
            return;
        }
        if ($("#carousel-img")[0].files[0]!==undefined){
            if (!util.photoValid($("#carousel-img")[0].files[0].name)||$("#carousel-img")[0].files[0].size>1024*1024){
                toast("id","表单提示","图片格式或大小不正确，仅支持大小不得超过1M的png、jpg、jepg图片");
                return;
            }
        }

        if(!util.zcValid($name.val())){
            $name.css({ "border":"solid red 1px"  });
            toast("id","表单通知","轮播图名格式不正确，要求2-16中文、字母、数字及-符号！");
            return;
        }

        if(!util.urlValid($("#carousel-url").val())){
            $("#video-url").css({ "border":"solid red 1px"  });
            toast("id","表单通知","url地址格式不正确！");
            return;
        }

        if(!util.numValid($("#carousel-weight").val())){
            $("#carousel-weight").css({ "border":"solid red 1px"  });
            toast("id","表单通知","权重要求为0-9之间的数字！");
            return;
        }
        formData.append("id",$name.attr("slid"));
        formData.append("file",$("#carousel-img")[0].files[0]);
        formData.append("carouselName",$name.val());
        formData.append("carouselUrl", $("#carousel-url").val());
        formData.append("weight",$("#carousel-weight").val());
        formData.append("status",$("#carousel-status").val());
        $.ajax({
            url:("/mooc/admin/carousel"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                    $("#carousel").modal("hide");
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });

    /*删除指定id轮播函数*/
    $(document).on('click','.carousel-delete',function () {
        var formData = new FormData();
        formData.append("id",$(this).parent().parent().parent().attr("carousel-id"));
        $.ajax({
            url:("/mooc/admin/deletecarousel"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data){
                if(data.success){
                    toast("id","系统消息",data.message);
                }
                else{
                    toast("id","系统消息",data.message);
                }
            }
        });
    });


    /*安全退出*/
    $(".exit").click(function () {
        exit();
    });

    /*加一级选项卡*/
    $('#classify-tip').click(function () {
        $('#icon-classify').addClass('icon-chevron-left').removeClass('icon-chevron-down');
        $('#icon-user').addClass('icon-chevron-down').removeClass('icon-chevron-left');
        $('#user-collapse').collapse('hide');
        $('#classify-collapse').collapse('show');
    });
    $('#user-tip').click(function () {
        $('#icon-user').addClass('icon-chevron-left').removeClass('icon-chevron-down');
        $('#icon-classify').addClass('icon-chevron-down').removeClass('icon-chevron-left');
        $('#classify-collapse').collapse('hide');
        $('#user-collapse').collapse('show');
    });

    /*切换子分类选项卡*/
    $(document).on('click', '.manage', function () {
        userState();
        $('.page').html('');
        $('.content').removeClass('show');
        switch ($(this).attr("class").split(' ').shift()) {
            case "main-page":
                $("#main-page").collapse('show');
                userGraph();
                break;
            case "user-manage":
                $("#user-manage").collapse('show');
                AuthUserList(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('auth-page'), //必填,总页数
                    pageAmount: 10,  //每页多少条
                    dataTotal: sessionStorage.getItem('auth-count'), //总共多少条数据
                    curPage:1, //初始页码,不填默认为1
                    pageSize: 5, //分页个数,不填默认为5
                    showPageTotalFlag:true, //是否显示数据统计,不填默认不显示
                    showSkipInputFlag:true, //是否支持跳转,不填默认不显示
                    getPage: function (page) {
                        AuthUserList(page);
                    }
                });
                break;
            case "classify-manage":
                TopClassify();
                SubClassify();
                $("#classify-manage").collapse('show');
                break;
            case "course-manage":
                $("#course-manage").collapse('show');
                sessionStorage.setItem('sort',null);
                CourseList(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('course-page'),
                    pageAmount: 6,
                    dataTotal: sessionStorage.getItem('course-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        CourseList(page);
                    }
                });
                break;
            case "carousel-manage":
                CarouselList(99);
                $("#carousel-manage").collapse('show');
                break;
            case "other-manage":
                $("#other-manage").collapse('show');
                break;
            case "message-manage":
                getMessage(1);
                new Page({
                    id: 'page',
                    pageTotal: sessionStorage.getItem('message-page'),
                    pageAmount: 10,
                    dataTotal: sessionStorage.getItem('message-count'),
                    curPage:1,
                    pageSize: 5,
                    showPageTotalFlag:true,
                    showSkipInputFlag:true,
                    getPage: function (page) {
                        getMessage(page);
                    }
                });
                $("#message-manage").collapse('show');
                break;
        }
        $(this).addClass('function-active').siblings().removeClass('function-active');
    });

    /*加载申请消息*/
    function getMessage(pageIndex) {
        var UserStateUrl = '/mooc/admin/applymessage?pageSize=10&pageIndex='+pageIndex;
        $.post(UserStateUrl, function (data) {
            var messageList = '';
            if (data.success && data.count>0) {
                sessionStorage.setItem('message-count',data.count);
                sessionStorage.setItem("message-page",Math.ceil(data.count/10));
                data.messageList.map(function(item, index) {
                    messageList +='<div class="message-content" userId="'
                        +item.sendUserId+'"><img src="'
                        +item.header
                        +'" class="message-head rounded-circle">'
                        +(item.status===0? '<span class="message-state"></span>':'')
                        +'<span class="receipt-name">'
                        +item.sendUserName
                        +'</span><span class="message-time">'
                        + new Date(item.updateTime).Format("yyyy-MM-dd hh:mm:ss")
                        +'</span><div class="message-body"><span class="receipt-content">'
                        +item.content
                        +'</span></div></div>';
                });
            }else{
                messageList = '<h5 class="null-tip">未查询到相关数据!</h5>';
            }
            $('#message-content').html(messageList);
        });
    }

    /*点击不同用户提交申请消息，跳转相应处理页面*/
    $(document).on('click','.message-content',function () {
        var userId = $(this).attr("userId");
        UserInfo(userId);
        $('.content').removeClass('show');
        $('.page').html('');
        $("#apply-manage").collapse("show");
    });

    /*根据id获取指定用户信息*/
    function UserInfo(userId) {
        var formData = new FormData();
        formData.append('userId',userId);
        $.post({
            url:("/mooc/admin/userinfo"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data) {
                if (data.success) {
                    //标题数据及表单id
                    $(".applicant-username").text(data.user.username);
                    $(".apply-page").attr("user-id",data.user.id);
                    //表单数据
                    $("#applicant-header").attr("src",data.user.header);
                    $("#applicant-account").text(data.user.account);
                    $("#applicant-username").text(data.user.username);
                    $("#applicant-realname").text(data.user.realname);
                    $("#applicant-sex").text(data.user.gender===1?'女':'男');
                    $("#applicant-birthday").text(new Date(data.user.birthday).Format("yyyy-MM-dd"));
                    $("#applicant-education").text(data.user.education);
                    $("#applicant-college").text(data.user.collegeName);
                    $("#applicant-sign").text(data.user.sign);
                    $("#applicant-certificate").attr("src",data.user.certificate);
                }else{
                    toast("id","系统通知",data.message);
                }
            }
        });
    }

    /*处理消息后结果返回服务器*/
    $(document).on('click','#refuse,#agree',function () {
        var result = false;
        if($(this).attr('id')==="agree"){
            result = true;
        }
        var formData = new FormData();
        formData.append('userId',$(this).parent().attr("user-id"));
        formData.append('result',result);
        $.post({
            url:("/mooc/admin/handleapply"),
            type:'POST',
            data:formData,
            contentType:false,
            processData:false,
            cache:false,
            success:function(data) {
                if (data.success) {
                    toast("id","系统通知",data.message);
                }else{
                    toast("id","系统通知",data.message);
                }
            }
        });
    });

    /*加载3项数据*/
    function ThreeData() {
        //拼接出查询条件的url
        var url = '/mooc/admin/threedata';
        $.getJSON(url, function(data) {
            if (data.success) {
                $("#user-progress,#user-count").text(data.userCount);
                $("#teacher-progress,#teacher-count").text(data.teacherCount);
                $("#course-progress,#course-count").text(data.courseCount);
            }
        });
    }

    /*用户*/
    function userGraph() {
        $.getJSON("/mooc/admin/graphdata", function(data) {
            var x = [],y1=[],y2=[];
            if (data.success) {
                data.userGraph.map(function (item, index) {
                    x.push(new Date(item.graphTime).Format("MM-dd"));
                    y1.push(item.count);
                });
                data.teacherGraph.map(function (item, index) {
                    y2.push(item.count);
                });
                graphData(x,y1,y2)
            }
        });
    }

    /*课程*/
    function courseGraph(courseId) {
        $.getJSON("/mooc/admin/studygraph?courseId="+courseId, function(data) {
            var categories=[],record = [];
            if (data.success) {
                data.courseGraph.map(function (item, index) {
                    categories.push(new Date(item.graphTime).Format("MM-dd"));
                    record.push(item.count);
                });
                pageData(categories,record)
            }
        });
    }

    /*加载用户柱状图数据*/
    function graphData(x,y1,y2) {
        var chart = {
            type: 'column',
            backgroundColor: 'rgba(255,255,255,0.5)'
        };
        var title = {
            text: '近一周新增用户'
        };
        var xAxis = {
            categories: [],
            crosshair: true
        };
        var yAxis = {
            min: 0,
            title: {
                text: '用户（人）'
            }
        };
        var tooltip = {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                '<td style="padding:0"><b>{point.y} 人</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        };
        var plotOptions = {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        };
        var credits = {
            enabled: false
        };

        var series= [ {
            name: '讲师用户',
            data: [],
            color:'#dc3545'
        }, {
            name: '所有用户',
            data: [],
            color:'#ffc107'
        }];
        xAxis.categories = x;
        series[1].data = y1;
        series[0].data = y2;
        var json = {};
        json.chart = chart;
        json.title = title;
        json.tooltip = tooltip;
        json.xAxis = xAxis;
        json.yAxis = yAxis;
        json.series = series;
        json.plotOptions = plotOptions;
        json.credits = credits;
        $('#report').highcharts(json);
    }

    /*加载课程折线图数据*/
    function pageData(categories,data) {
        $('#graph-bar').highcharts({
            chart: {
                type: 'line',
                backgroundColor: 'rgba(255,255,255,0.5)'
            },
            title: {
                text: '最近7天课程学习人数',
                x: -20
            },
            subtitle: {
                text: '',
                x: -20
            },
            credits: {
                enabled: false
            },
            xAxis: {
                categories: categories
            },
            yAxis: {
                title: {
                    text: "学习人数"
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#0089D2'
                }],
                //设置为false，人数不可小数
                allowDecimals:false
            },
            tooltip: {
                valueSuffix: "人"
            },
            legend: {
                layout: 'vertical',
                align: 'center',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [{
                name: "学习人数",
                data: data
            }]
        });
    }
});

function checkCarousel(img){
    if (util.photoValid(img)){
        util.previewUploadImg("carousel-img","carousel-picture");
    }else {
        toast("id",'系统通知',"请选择png、jpg、jepg格式图片");
        $("#header").val('');
    }
}
function checkCourse(img){
    if (util.photoValid(img)){
        util.previewUploadImg("course-img","course-picture");
    }else {
        toast("id",'系统通知',"请选择png、jpg、jepg格式图片");
        $("#header").val('');
    }
}

/*添加二级分类使select变化一致*/
function showCode(v){
    $(".top-code").val(v.value);
}