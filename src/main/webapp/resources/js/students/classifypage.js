$(function () {
    TopClassify();
    userState();

    /*用户登录状态*/
    function userState(){
        var UserStateUrl = '/mooc/useroperator/loginstate';
        $.post(UserStateUrl,function(data) {
            if(data.success){
                $('.head-img').html('<img src="'+data.userinfo.header+'" alt="个人中心" class="rounded-circle personal-img">');
            }
        });
    }

    $(document).ready(function() {
       if (getQueryString('account')!==''){
           getCourse(null,null,null,null,getQueryString('account'));
       }else if (getQueryString('sub')!==''){
            getCourse(null,getQueryString('sub'),null,null,null);
        }else{
           getCourse(null,null,null,null,null);
       }
    });

    /*加载顶级分类列表*/
    function TopClassify() {
        var topfUrl = '/mooc/homeshow/querytopclassify';
        $.getJSON(topfUrl,function(data) {
            if(data.success){
                var topClassify = '';
                data.classifylist.map(function(item, index) {
                    topClassify +='<button type="button" class="btn-top btn btn-secondary btn-classify" data-code="'
                        +item.code+'">'
                        +item.name+'</button>';
                });
                $('#top-classify').append(topClassify);
            }
        });
    }

    /*一级分类全部按钮事件*/
    $('.all-top').click(function () {
        sessionStorage.setItem("topClassify",null);
        getCourse(null,null,null,null,null);
    });

    /*二级分类全部按钮事件*/
    $(document).on('click','.all-sub',function () {
        getCourse(sessionStorage.getItem("topClassify"),null,sessionStorage.getItem("sort"),null,null);
    });

    /*加载二级分类列表*/
    $(document).on('click','.btn-top',function (e) {
        $(".top-name").text($(this).text());
        var parentCode = e.currentTarget.dataset.code;
        course.topClassify = parentCode;
        sessionStorage.setItem("topClassify",parentCode);
        sessionStorage.setItem("subClassify",null);
        var classifyUrl = '/mooc/homeshow/querysubclassify?code='+parentCode;
        $.getJSON(classifyUrl,function(data) {
            if(data.success){
                var all = '<button type="button" class="btn all-sub btn-secondary btn-classify">全部</button>';
                var subClassify = '';
                data.subcalssifylist.map(function(item, index) {
                    subClassify +='<button type="button" class="btn-sub btn btn-secondary btn-classify" data-code="'
                        +item.code+'">'
                        +item.name+'</button>';
                });
                $('#sub-classify').html(all+subClassify);
            }
        });
    });

    /*条件加载课程*/
    function getCourse(topClassify,subClassify,sort,keyword,account) {
        var formData = new FormData();
        formData.append("account",account);
        formData.append("topClassify", topClassify);
        formData.append("subClassify", subClassify);
        formData.append("status","1");
        formData.append("sort",sort);
        formData.append("keyword", keyword);
        formData.append("pageIndex", 1);
        formData.append("pageSize",15);
        $.ajax({
            url: ("/mooc/admin/courselist"),
            type: 'POST',
            data: formData,
            contentType: false,
            processData: false,
            cache: false,
            success: function (data) {
                var course = '';
                data.courseList.map(function (item, index) {
                    course += '<div class="col-md-1-5 course" data-courseid="' +
                        item.id+'">' +
                        '<div class="simple-classify">'+item.subClassifyName+'</div>' +
                        '<div class="course-name">'+item.courseName+'</div>' +
                        '<div class="course-details"><strong>简介:</strong>'+item.brief+'</div><div>' +
                        '<span class="course-owner">'+item.ownerName+'</span>' +
                        '<span class="course-time">'+item.courseTime+'</span></div>' +
                        '<span class="course-count">'+item.studyCount+"人在学"+'</span></div>';
                });
                $('.tab-course').html(course);
            }
        })
    }

    /*模糊查询课程数*/
    $("#search").click(function () {
       var keyword = $("#keyword").val();
       getCourse(null,null,null,keyword,null);
    });

    /*监听参数变化，根据参数变化调用加载课程函数*/
    var course = { "topClassify":null, "subClassify":null, "sort":null};
    Object.defineProperties(course, {
        topClassify: {
            configurable: true,
            set: function(value) {
                topClassify = value;
                sessionStorage.setItem("subClassify",null);
                getCourse(topClassify,sessionStorage.getItem("subClassify"),sessionStorage.getItem("sort"),null,null);
            }
        },
        subClassify: {
            configurable: true,
            set: function(value) {
                subClassify = value;
                getCourse(sessionStorage.getItem("topClassify"),subClassify,sessionStorage.getItem("sort"),null,null);
            }
        },
        sort: {
            configurable: true,
            set: function(value) {
                sort = value;
                getCourse(sessionStorage.getItem("topClassify"),sessionStorage.getItem("subClassify"),sort,null,null);
            }
        }
    });

    //点击不同课程跳转相应的详情页
    $(document).on('click','.course',function(e){
        var courseid = e.currentTarget.dataset.courseid;
        window.location.href = '/mooc/course?id=' + courseid;
    });

    /*多级导航*/
    $(document).on('click','.btn-sub,.btn-popular',function (e) {
        if ($(this).attr('class').split(' ').shift()==='btn-sub'){
            $(".sub-name").text('/'+$(this).text());
            var subCode = e.currentTarget.dataset.code;
            course.subClassify = subCode;
            sessionStorage.setItem("subClassify",subCode);
        }else{
            $(".popular-name").text('/'+$(this).text());
            var sort = e.currentTarget.dataset.sort;
            course.sort = sort;
            sessionStorage.setItem("sort",sort);
        }

    });
});