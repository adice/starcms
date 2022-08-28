$(document).ready(function () {
    var myChart;
    $.post("/backstage/contentcount/countstackedline", function (result) {
        generateChart(result);
    });
    getChannelData = function (result) {
        var data = [];
        $.each(result, function (i, j) {
            data.push(i);
        });
        return data;
    };
    getXData = function (result) {
        var data = [];
        $.each(result['全部'], function (i, j) {
            data.push([i]);
        });
        return data;
    };
    getSeriesData = function (result) {
        var dataAll = [];
        $.each(result, function (i, j) {
            var singleData = {
                name: i,
                type: 'line',
                data: []
            };
            $.each(j, function (m, n) {
                singleData.data.push(n);
            });
            dataAll.push(singleData);
        });
        return dataAll;
    };
    generateChart = function (result) {
        myChart = echarts.init(document.getElementById('stackedline'), 'wonderland');
        var option = {
            title: {
                text: '本月录入数据统计'
            },
            tooltip: {
                trigger: 'axis'
            },
            legend: {
                data: getChannelData(result)
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: getXData(result)
            },
            yAxis: {
                type: 'value'
            },
            series: getSeriesData(result)
        };
        option && myChart.setOption(option);
    };
    window.onresize = function () {
        myChart.resize();
    };

});