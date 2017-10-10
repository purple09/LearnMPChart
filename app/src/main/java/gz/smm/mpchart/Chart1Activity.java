package gz.smm.mpchart;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Chart1Activity extends AppCompatActivity {
    private static final String TAG = "testchart";

    private TextView tv;
    private CandleStickChart mChart;
    private List<VirtualKline.DataBean> kDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart1);
        initView();
        initData();
    }

    private void initView() {
        tv = (TextView) findViewById(R.id.tv);
        mChart = (CandleStickChart) findViewById(R.id.chart1);
        mChart.setDrawGridBackground(true);
        mChart.getDescription().setEnabled(false);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setHighlightPerDragEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i(TAG, "onValueSelected");
            }

            @Override
            public void onNothingSelected() {
                Log.i(TAG, "onNothingSelected");
            }
        });
        mChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureStart");
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureEnd");
//                if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
                mChart.highlightValues(null);
                mChart.setDragXEnabled(true);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                Log.i(TAG, "onChartLongPressed");
                mChart.highlightValue(mChart.getHighlightByTouchPoint(me.getX(), me.getY()));
                mChart.setDragXEnabled(false);
            }

            @Override
            public void onChartDoubleTapped(MotionEvent me) {
                Log.i(TAG, "onChartDoubleTapped");
            }

            @Override
            public void onChartSingleTapped(MotionEvent me) {
                Log.i(TAG, "onChartSingleTapped");
            }

            @Override
            public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
                Log.i(TAG, "onChartFling");
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                Log.i(TAG, "onChartScale");
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                Log.i(TAG, "onChartTranslate");
            }
        });

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(5, true);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                if (kDatas != null && index >= 0 && index < kDatas.size()) {
                    return kDatas.get(index).getDate();
                }
                return "";
            }

            public int getDecimalDigits() {
                return 0;
            }

        });

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.setEnabled(false);
        leftAxis.setLabelCount(7, true);
//        leftAxis.setDrawGridLines(false);
//        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = mChart.getAxisRight();
//        rightAxis.setEnabled(false);
        rightAxis.setLabelCount(7, true);
    }

    private void initData() {
        getData();
    }

    private void getData() {
        String url = "http://testplatform.smm.cn/quotecenter/instrument/BTC/kline?time_type=15min&page=1&limit=500";
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.i("请求数据", "onFailure");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();
                Log.i("请求数据", "onResponse===" + str);
                final VirtualKline data = new Gson().fromJson(str, VirtualKline.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv.setText("加载完毕");
                        changeChart(data.data);
                    }
                });

            }
        });
    }

    private void changeChart(List<VirtualKline.DataBean> datas) {
        this.kDatas = datas;
        List<CandleEntry> vals = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            VirtualKline.DataBean data = datas.get(i);
            vals.add(new CandleEntry(
                    i, (float) data.highest_price, (float) data.lowest_price, (float) data.open_price, (float) data.close_price, null
            ));
        }

        CandleDataSet set1 = new CandleDataSet(vals, "Data Set");

        set1.setDrawIcons(false);
        set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
//        set1.setColor(Color.rgb(80, 80, 80));
        set1.setShadowColor(Color.DKGRAY);
        set1.setShadowWidth(0.7f);
        set1.setDecreasingColor(Color.RED);
        set1.setDecreasingPaintStyle(Paint.Style.FILL);
        set1.setIncreasingColor(Color.rgb(122, 242, 84));
        set1.setIncreasingPaintStyle(Paint.Style.FILL);
        set1.setNeutralColor(Color.BLUE);
        set1.setDrawValues(false);
        set1.setHighlightLineWidth(1f);
        set1.setHighLightColor(Color.RED);

        CandleData data = new CandleData(set1);

        mChart.setData(data);
        mChart.setVisibleXRangeMaximum(50);
        mChart.invalidate();
    }
}
