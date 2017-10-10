package gz.smm.mpchart;

import java.util.List;

/**
 * Created by guizhen on 2017/7/12.
 */

public class VirtualKline {
    public int code;
    public String msg;

    public List<DataBean> data;

    public static class DataBean {
        /**
         * instrument_id : BTC
         * time_type : 1min
         * datetime : 2017-07-11 11:50:00
         * open_price : 16785
         * close_price : 16785
         * highest_price : 16785
         * lowest_price : 16785
         * volume : 0
         * open_interest : 0
         */

        public String instrument_id;
        public String time_type;
        public String datetime;
        public double open_price;
        public double close_price;
        public double highest_price;
        public double lowest_price;
        public double volume;
        public double open_interest;

        public String getDate() {
            if (datetime == null) return "";
            if (datetime.length() >= 16) {
                return datetime.substring(11, 16);
            } else if (datetime.length() >= 10) {
                return datetime.substring(0, 10);
            } else {
                return "";
            }
        }
    }
}
