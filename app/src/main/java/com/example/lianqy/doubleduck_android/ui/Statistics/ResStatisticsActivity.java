package com.example.lianqy.doubleduck_android.ui.Statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lianqy.doubleduck_android.R;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class ResStatisticsActivity extends AppCompatActivity {

    private LineChartView lineChartView;

    private ArrayList<PointValue> points = new ArrayList<PointValue>();
    private List<Line> linesList = new ArrayList<Line>();
    private Axis axisX, axisY;
    private LineChartData lineChartData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_statistic);

        setViews();
        setLineChartAttributes();
        showLineChart();
    }

    private void setLineChartAttributes() {
        /* 初始化Y轴 */
        axisY = new Axis();
        axisY.setName("营业额（单位：元）");//添加Y轴的名称
        axisY.setHasLines(true);//Y轴分割线
        axisY.setTextSize(10);//设置字体大小
        //axisY.setTextColor(Color.parseColor("#AFEEEE"));//设置Y轴颜色，默认浅灰色
        lineChartData = new LineChartData(linesList);
        lineChartData.setAxisYLeft(axisY);//设置Y轴在左边

        /* 初始化X轴 */
        axisX = new Axis();
        axisX.setHasTiltedLabels(false);//X坐标轴字体是斜的显示还是直的，true是斜的显示
        //axisX.setTextColor(Color.CYAN);//设置X轴颜色
        axisX.setName("时间（单位：星期）");//X轴名称
        axisX.setHasLines(true);//X轴分割线
        axisX.setTextSize(10);//设置字体大小
        axisX.setMaxLabelChars(1);//设置0的话X轴坐标值就间隔为1
        List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
        for (int i = 0; i < 8; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(i+""));
        }
        axisX.setValues(mAxisXValues);//填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX);//X轴在底部

        Viewport port = initViewPort(0,8);//初始化X轴10个间隔坐标
        //lineChartView.setCurrentViewportWithAnimation(port);

        loadData();//加载待显示数据

    }

    private Viewport initViewPort(float left,float right) {
        Viewport port = new Viewport();
        port.top = 150;//Y轴上限，固定(不固定上下限的话，Y轴坐标值可自适应变化)
        port.bottom = 0;//Y轴下限，固定
        port.left = left;//X轴左边界，变化
        port.right = right;//X轴右边界，变化
        return port;
    }

    //这个就是一周的营业额数据的设置
    //x: 周一（0）...周日(6)
    //y: 当天营业额的数值，chartview的上限会随着数据的变化而变化
    private void loadData() {
        for (int i = 0; i < 7; i++) {
            points.add(new PointValue(i + 1, i % 5 * 10 + 30));
        }
    }

    private void showLineChart() {
        Line line = new Line(points);
        line.setColor(Color.parseColor("#FFCD41"));//设置折线颜色
        line.setShape(ValueShape.CIRCLE);//设置折线图上数据点形状为 圆形 （共有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，true是平滑曲线，false是折线
        line.setHasLabels(true);//数据是否有标注
        line.setHasLines(true);//是否用线显示，如果为false则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 ，如果为false则没有原点只有点显示（每个数据点都是个大圆点）
        linesList.add(line);
        lineChartData = new LineChartData(linesList);
        lineChartData.setAxisYLeft(axisY);//设置Y轴在左
        lineChartData.setAxisXBottom(axisX);//X轴在底部

        lineChartView.setLineChartData(lineChartData);
    }


    private void setViews() {
        lineChartView = findViewById(R.id.chart);
    }

    //点击返回键结束该activity
    @Override
    public void onBackPressed(){
        finish();
    }
}
