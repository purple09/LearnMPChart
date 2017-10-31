package gz.smm.mpchart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.HighLightMode;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisColorFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Chart2Activity extends AppCompatActivity {
    public static final String TAG = "testfenshi";

    private LineChart lineChart;
    private CombinedChart combinedChart;
    private Quotation_details_three_bean.Qd_three_DataAll data;
    private Map<Integer, String> xMap;
    private float preSettlementPrice = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart2);
        initView();
        getData();
    }

    private void initView() {
        lineChart = (LineChart) findViewById(R.id.chart1);
        combinedChart = (CombinedChart) findViewById(R.id.chart2);

        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setHighLightMode(HighLightMode.LONG_PRESSED);
        lineChart.getLegend().setEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setMinOffset(0);
        lineChart.setExtraTopOffset(10);
        lineChart.setExtraBottomOffset(10);

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i(TAG, "onValueSelected");
            }

            @Override
            public void onNothingSelected() {
                Log.i(TAG, "onNothingSelected");
            }
        });
        lineChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureStart");
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureEnd===");
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                Log.i(TAG, "onChartLongPressed");
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


        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        combinedChart.setDrawGridBackground(false);
        combinedChart.getDescription().setEnabled(false);
        combinedChart.setDoubleTapToZoomEnabled(false);
        combinedChart.setHighLightMode(HighLightMode.LONG_PRESSED);
        combinedChart.getLegend().setEnabled(false);
        combinedChart.setDragEnabled(false);
        combinedChart.setScaleEnabled(false);
        combinedChart.setMinOffset(0);
        combinedChart.setExtraTopOffset(10);
        combinedChart.setExtraBottomOffset(10);

        combinedChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.i(TAG, "onValueSelected");
            }

            @Override
            public void onNothingSelected() {
                Log.i(TAG, "onNothingSelected");
            }
        });
        combinedChart.setOnChartGestureListener(new OnChartGestureListener() {
            @Override
            public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureStart");
            }

            @Override
            public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
                Log.i(TAG, "onChartGestureEnd===");
            }

            @Override
            public void onChartLongPressed(MotionEvent me) {
                Log.i(TAG, "onChartLongPressed");
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

    }


    private void getData() {
        String url = "http://testplatform.smm.cn/quotecenter/instrument/cu1712/timeline";
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        final Request request = new Request.Builder()
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
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String str = response.body().string();
                Log.i("请求数据", "onResponse===" + str);
                final Quotation_details_three_bean result = new Gson().fromJson(str, Quotation_details_three_bean.class);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result.data != null) {
                            data = result.data;
                            handleData();
                        }
                    }
                });

            }
        });
    }

    private void handleData() {
        int xTotal = 0;
        xMap = new ArrayMap<>();
        String[] timeBuf;

        Map<String, String> scalesMap = new ArrayMap<>();
        for (SimpleGridData scale : data.scales) {
            scalesMap.put(scale.position, scale.show);
        }

        List<String> timePeriods = new ArrayList<>();
        for (String str : data.trading_time) {
            String[] ss = str.split(",");
            for (String s : ss) {
                timePeriods.add(s);
            }
        }

        for (String timePeriod : timePeriods) {
            int periodTotal;
            timeBuf = timePeriod.split("-");
            String show1 = scalesMap.get(timeBuf[0]);
            if (!TextUtils.isEmpty(show1)) {
                scalesMap.remove(timeBuf[0]);
                xMap.put(xTotal, show1);
            }
            periodTotal = computeTotal(timeBuf[0], timeBuf[1]);
            xTotal += periodTotal;
            String show2 = scalesMap.get(timeBuf[1]);
            if (!TextUtils.isEmpty(show2)) {
                scalesMap.remove(timeBuf[1]);
                xMap.put(xTotal, show2);
            }
        }

        List<Entry> lineFenshi = new ArrayList<>();
        List<Entry> lineAverge = new ArrayList<>();
        List<Entry> lineOpenInterest = new ArrayList<>();
        List<BarEntry> barVolume = new ArrayList<>();
        for (int i = 0; i < data.data.length; i++) {
            Quotation_details_three_bean.Qd_three_Data qdData = data.data[i];
            if (preSettlementPrice == 0 && qdData.PreSettlementPrice > 0) {
                preSettlementPrice = qdData.PreSettlementPrice;
            }
            float value;
            if (qdData.LastPrice == 0 || qdData.PreSettlementPrice == 0) {
                value = 0;
            } else {
                value = (qdData.LastPrice - qdData.PreSettlementPrice) / qdData.PreSettlementPrice * 100;
            }
            lineFenshi.add(new Entry(i, value));
            lineAverge.add(new Entry(i, (float) (qdData.Turnover / qdData.VolumeTotal / data.trade_unit)));
            lineOpenInterest.add(new Entry(i, qdData.OpenInterest));
            int color;
            if (i == 0 || qdData.Volume >= data.data[i-1].Volume) {
                color = Color.RED;
            } else {
                color = Color.GREEN;
            }
            barVolume.add(new BarEntry(i, qdData.Volume, color));
        }
        //分时线
        LineDataSet setFenshi = new LineDataSet(lineFenshi, "分时线");
        setFenshi.setDrawIcons(false);
        setFenshi.setAxisDependency(YAxis.AxisDependency.RIGHT);
        setFenshi.setDrawValues(false);
        setFenshi.setColor(Color.BLACK);
        setFenshi.setDrawMaxMinLine(false);
        setFenshi.setDrawCircles(false);
        //均线
        LineDataSet setAverge = new LineDataSet(lineAverge, "均线");
        setAverge.setDrawIcons(false);
        setAverge.setAxisDependency(YAxis.AxisDependency.LEFT);
        setAverge.setDrawValues(false);
        setAverge.setColor(Color.YELLOW);
        setAverge.setDrawMaxMinLine(false);
        setAverge.setDrawCircles(false);
        //持仓量线
        LineDataSet setOpenInterest = new LineDataSet(lineOpenInterest, "均线");
        setOpenInterest.setDrawIcons(false);
        setOpenInterest.setAxisDependency(YAxis.AxisDependency.RIGHT);
        setOpenInterest.setDrawValues(false);
        setOpenInterest.setColor(Color.BLACK);
        setOpenInterest.setDrawMaxMinLine(false);
        setOpenInterest.setDrawCircles(false);
        //交易量柱状图
        BarDataSet setVolume = new BarDataSet(barVolume, "持仓量");
        setVolume.setDrawIcons(false);
        setVolume.setAxisDependency(YAxis.AxisDependency.LEFT);
        setVolume.setDrawValues(false);
        setVolume.setDrawMaxMinLine(false);
        setVolume.setColors();
        //计算 x 坐标
        float[] xCustomPositions = new float[xMap.size()];
        Integer[] sets = new Integer[xMap.size()];
        xMap.keySet().toArray(sets);
        for (int i = 0; i < sets.length; i++) {
            xCustomPositions[i] = sets[i];
        }
        //图1 x 坐标
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum(xTotal);
        xAxis.setCustomPositions(xCustomPositions);
        xAxis.setDrawLabels(false);

        //图2 x 坐标
        XAxis xAxis2 = combinedChart.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setAxisMaximum(xTotal);
        xAxis2.setCustomPositions(xCustomPositions);
        xAxis2.setValueFormatter(xAxisValueFormatter);
        xAxis2.setAvoidFirstLastClipping(true);

        //计算 图1 y 坐标
        float diff1 = Math.abs(setFenshi.getYMax());
        float diff2 = Math.abs(setFenshi.getYMin());
        float diffRight = diff1 > diff2 ? diff1 : diff2;
        diffRight *= 1.1;
        float diffLeft = preSettlementPrice * diffRight / 100;
        //图1 左y 坐标
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setLabelCount(5, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        leftAxis.setAxisMaximum(preSettlementPrice + diffLeft);
        leftAxis.setAxisMinimum(preSettlementPrice - diffLeft);
        leftAxis.setAxisColorFormatter(leftColorFormatter);
        //图1 右y 坐标
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setLabelCount(5, true);
        rightAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        rightAxis.setAxisMaximum(diffRight);
        rightAxis.setAxisMinimum(-diffRight);
        rightAxis.setValueFormatter(rightYAxisValueFormatter);
        rightAxis.setAxisColorFormatter(rightColorFormatter);

        //图2 左y 坐标
        YAxis leftAxis2 = combinedChart.getAxisLeft();
        leftAxis2.setLabelCount(4, true);
        leftAxis2.setAxisMinimum(0);
        leftAxis2.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        //图2 右y 坐标
        YAxis rightAxis2 = combinedChart.getAxisRight();
        rightAxis2.setLabelCount(4, true);
        rightAxis2.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        //图1数据
        lineChart.setData(new LineData(setFenshi, setAverge));
        lineChart.invalidate();

        //图2数据
        CombinedData combinedData = new CombinedData();
        combinedData.setData(new LineData(setOpenInterest));
        combinedData.setData(new BarData(setVolume));
        combinedChart.setData(combinedData);
        combinedChart.invalidate();


    }

    private IAxisColorFormatter leftColorFormatter = new IAxisColorFormatter() {
        @Override
        public int getLableColor(float value, AxisBase axis) {
            int color;
            if (value == preSettlementPrice) {
                color = Color.BLACK;
            } else if (value > preSettlementPrice) {
                color = Color.RED;
            } else {
                color = Color.GREEN;
            }
            return color;
        }
    };

    private IAxisColorFormatter rightColorFormatter = new IAxisColorFormatter() {
        @Override
        public int getLableColor(float value, AxisBase axis) {
            int color;
            if (value == 0) {
                color = Color.BLACK;
            } else if (value > 0) {
                color = Color.RED;
            } else {
                color = Color.GREEN;
            }
            return color;
        }
    };

    private IAxisValueFormatter xAxisValueFormatter = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            String show = xMap.get((int) value);
            if (show == null) show = "NONE";
            return show;
        }
    };

    private IAxisValueFormatter rightYAxisValueFormatter = new IAxisValueFormatter() {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return formatNum(value) + "%";
        }
    };

    private int computeTotal(String start, String end) {
        int[] starts = str2int(start.split(":"));
        int[] ends = str2int(end.split(":"));
        return ends[0] * 60 + ends[1] - starts[0] * 60 - starts[1] + 1;
    }

    private int[] str2int(String[] strs) {
        int[] ints = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {
            ints[i] = Integer.valueOf(strs[i]);
        }
        return ints;
    }

    private String formatNum(float value) {
        return format.format(value);
    }

    private static DecimalFormat format;

    static {
        DecimalFormatSymbols d = new DecimalFormatSymbols();
        d.setDecimalSeparator('.');
        d.setGroupingSeparator(',');
        format = new DecimalFormat("#0.##", d);
    }

}
