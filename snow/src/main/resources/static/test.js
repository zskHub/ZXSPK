function helloWord() {
    alert("helloword")
}
function getDate() {
    $.ajax({
        url:"/snowTest/findUserList",
        type:"post",
        success:function (data) {
            alert(data)
        },
        error:function () {
            alert("出错了")
        }
    });
}