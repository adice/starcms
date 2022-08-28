$(document).ready(function() {
    var myChart;
    $.post("/backstage/contentcount/heatmap", function(result){
        generateHeatChart(result);
    });
    getVirtulData = function(result) {
        var data = [];
        $.each(result,function(i,j){
            data.push([
                echarts.format.formatTime('yyyy-MM-dd', i),
                j
            ]);
        });
        return data;
    };
    generateHeatChart = function (result) {
        myChart = echarts.init(document.getElementById('heatchart'), 'wonderland');
        var option;
        option = {
            title: {
                top: 30,
                left: 'center'
            },
            tooltip: {
                position: 'top',
                formatter: function (p) {
                    var format = echarts.format.formatTime('yyyy-MM-dd', p.data[0]);
                    return format + ': ' + p.data[1];
                }
            },
            visualMap: {
                min: 1,
                max: 101,
                type: 'piecewise',
                orient: 'horizontal',
                left: 'center',
                top: 65
            },
            calendar: {
                top: 120,
                left: 30,
                right: 30,
                cellSize: ['auto', 13],
                range: '2022',
                itemStyle: {
                    borderWidth: 0.5
                },
                yearLabel: { show: true }
            },
            series: {
                type: 'heatmap',
                coordinateSystem: 'calendar',
                data: getVirtulData(result)
            }
        };
        option && myChart.setOption(option);
    };
    window.onresize = function () {
        myChart.resize();
    };

});