package gz.smm.mpchart;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.HighLightMode;
import com.github.mikephil.charting.highlight.ChartHighlighter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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
    public static final String TAG = "testchart";

    private TextView tv;
    private CandleStickChart mChart;
    private BarChart mChart2;
    private List<VirtualKline.DataBean> kDatas = new ArrayList<>();
    private int page = 1;
    private boolean isLoading = false;

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
        mChart2 = (BarChart) findViewById(R.id.chart2);

        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setHighLightMode(HighLightMode.LONG_PRESSED);
        mChart.getLegend().setEnabled(false);


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
//                Log.i(TAG, "onChartGestureStart");
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureEnd===" + mChart.getLowestVisibleX());
                if (page < 10 && mChart.getLowestVisibleX() <= 200) {
                    Log.i(TAG, "加载更多数据===" + mChart.getLowestVisibleX());
                    page++;
                    getData();
                }

//                if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
//                mChart.highlightValues(null);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                Log.i(TAG, "onChartLongPressed");
//                mChart.highlightValue(mChart.getHighlightByTouchPoint(me.getX(), me.getY()));
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
//                Log.i(TAG, "onChartFling");
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                Log.i(TAG, "onChartScale");
                Matrix matrix = mChart.getViewPortHandler().getMatrixTouch();
                mChart2.getViewPortHandler().refresh(matrix, mChart2, true);
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
//                Log.i(TAG, "onChartTranslate");
                Matrix matrix = mChart.getViewPortHandler().getMatrixTouch();
                mChart2.getViewPortHandler().refresh(matrix, mChart2, true);
               /* Matrix matrix = mChart2.getViewPortHandler().getMatrixTouch();
                matrix.postTranslate(dX, dY);
                mChart2.getViewPortHandler().refresh(matrix, mChart2, true);*/
//                mChart2.getViewPortHandler().translate(new float[]{dX, dY}, matrix);
            }
        });

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(5, true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinimum(30000);
        leftAxis.setLabelCount(7, true);
        leftAxis.setEnabled(false);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setAxisMinimum(30000);
        rightAxis.setLabelCount(7, true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        ////////////////////////////////////////////////////
        mChart2.setDrawGridBackground(false);
        mChart2.getDescription().setEnabled(false);
        mChart2.setAutoScaleMinMaxEnabled(true);
        mChart2.setHighlightPerDragEnabled(true);
        mChart2.setHighlightPerTapEnabled(true);
        mChart2.setDoubleTapToZoomEnabled(false);
        mChart2.setHighLightMode(HighLightMode.LONG_PRESSED);
        mChart2.getLegend().setEnabled(false);
        mChart2.setHighlighter(new ChartHighlighter(mChart2));
//        mChart2.addBindTouchListener(mChart.getOnTouchListener());
//        mChart.addBindTouchListener(mChart2.getOnTouchListener());

        mChart2.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i(TAG, "onValueSelected2");
            }

            @Override
            public void onNothingSelected() {
                Log.i(TAG, "onNothingSelected2");
            }
        });
        mChart2.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureStart2");
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureEnd2");
//                if(lastPerformedGesture != ChartTouchListener.ChartGesture.SINGLE_TAP)
//                mChart.highlightValues(null);
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                Log.i(TAG, "onChartLongPressed2");
//                mChart.highlightValue(mChart.getHighlightByTouchPoint(me.getX(), me.getY()));
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
                Log.i(TAG, "onChartFling2");
            }

            @Override
            public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
                Log.i(TAG, "onChartScale2");
                Matrix matrix = mChart2.getViewPortHandler().getMatrixTouch();
                mChart.getViewPortHandler().refresh(matrix, mChart, true);
            }

            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                Log.i(TAG, "onChartTranslate2");
                Matrix matrix = mChart2.getViewPortHandler().getMatrixTouch();
                mChart.getViewPortHandler().refresh(matrix, mChart, true);
            }
        });

        XAxis xAxis2 = mChart2.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setLabelCount(5, true);

        YAxis leftAxis2 = mChart2.getAxisLeft();
        leftAxis2.setLabelCount(7, true);
        leftAxis2.setAxisMinimum(0f);
        leftAxis2.setEnabled(false);

        YAxis rightAxis2 = mChart2.getAxisRight();
        rightAxis2.setLabelCount(7, true);
        rightAxis2.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);


    }

    private void initData() {
        getData();
    }

    private void getData() {
        if (isLoading)
            return;
        else
            isLoading = true;
        tv.setText("加载数据");
        String url = "http://testplatform.smm.cn/quotecenter/instrument/BTC/kline?time_type=15min&page=" + page + "&limit=500";
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
                        isLoading = false;
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
                        isLoading = false;
                        tv.setText("加载完毕");
                        if (page == 1) kDatas.clear();
                        if (data.data != null) {
                            kDatas.addAll(0, data.data);
                            changeChart();
                            changeChart2();
                        }
                    }
                });

            }
        });
    }

    private void changeChart() {
        float x = mChart.getLowestVisibleX();
        List<CandleEntry> vals = new ArrayList<>();
        for (int i = 0; i < kDatas.size(); i++) {
            VirtualKline.DataBean data = kDatas.get(i);
            vals.add(new CandleEntry(
                    i, (float) data.highest_price, (float) data.lowest_price, (float) data.open_price, (float) data.close_price, null
            ));
        }

        CandleData data = mChart.getData();
        CandleDataSet set1 = null;
        if (data != null) {
            set1 = (CandleDataSet) data.getDataSets().get(0);
            int xOffset = vals.size() - set1.getValues().size();
            if (xOffset > 0) {
                x += xOffset;
            }
            set1.setValues(vals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(50);
            Log.i(TAG, "加载数据完成===" + x);
            mChart.moveViewToX(x);
        } else {
            set1 = new CandleDataSet(vals, "Data Set");

            set1.setDrawIcons(false);
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
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
            set1.setDrawMaxMinLine(true);
            data = new CandleData(set1);
            mChart.setData(data);
            mChart.setVisibleXRangeMaximum(50);
            mChart.moveViewToX(kDatas.size() - 1);
        }
        if (page == 1) mChart.moveViewToX(kDatas.size() - 1);


    }

    private void changeChart2() {
        List<BarEntry> vals = new ArrayList<>();
        for (int i = 0; i < kDatas.size(); i++) {
            VirtualKline.DataBean data = kDatas.get(i);
            vals.add(new BarEntry(i, (float) data.volume));
        }

        BarData data = mChart2.getData();
        BarDataSet set1 = null;
        if (data != null) {
            set1 = (BarDataSet) data.getDataSets().get(0);
            set1.setValues(vals);
            mChart2.getData().notifyDataChanged();
            mChart2.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(vals, "The year 2017");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            data = new BarData(dataSets);
            data.setDrawValues(false);

            mChart2.setData(data);
            mChart2.setVisibleXRangeMaximum(50);
            mChart2.moveViewToX(kDatas.size() - 1);
        }
        mChart2.invalidate();
    }
}
