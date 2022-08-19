/**
 * Created by mosaddek on 2/2/15.
 */
$(function(){
    $('#beginTime').datepicker({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
    $('#endTime').datepicker({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
    $('#newsTime').datepicker({
        language: "zh-CN",
        autoclose: true,
        format: 'yyyy-mm-dd'
    });
});