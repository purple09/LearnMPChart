package gz.smm.mpchart;

import java.util.ArrayList;

/**
 * Created by zhangdi on 16/5/3.
 */
public class Quotation_details_three_bean {
    public int code;
    public String msg;
    public Qd_three_DataAll data;

    public class Qd_three_DataAll {
        public Qd_three_Data data[];
        public String degrees;
        public String tradingday;
        public String[] trading_time;
        public int trade_unit;
        public ArrayList<SimpleGridData> scales;
        public int day_type;
    }

    public class Qd_three_Data {
        public String InstrumentId;
        public String UpdateTime;
        public float LastPrice;
        public float Volume;
        public float VolumeTotal;
        public float OpenInterest;
        public float PreSettlementPrice;
        public double Turnover;
        public boolean isAddData = false;
    }
}
