package com.accelerator.metro.utils;

/**
 * Created by Nicholas on 2016/8/7.
 */
public class XiAnTicketUtil {

    XiAnTicketUtil() {
        throw new RuntimeException("Stub!");
    }

    /**
     * 计算价格
     *
     * @param startId 起点站id
     * @param endId   终点站id
     * @return 价格
     */
    public static int XiAnTicket(int startId, int endId) {

        int stationCount = XiAnSection(startId, endId);

        if (stationCount <= 6) {
            return 2;
        } else if (stationCount <= 10 && stationCount > 6) {
            return 3;
        } else if (stationCount <= 16 && stationCount > 10) {
            return 4;
        } else if (stationCount >= 17) {
            return 5;
        }

        return -1;

    }

    /**
     * 计算站点
     *
     * @param startId 起点站id
     * @param endId   终点站id
     * @return 经过的站点数
     */
    public static int XiAnSection(int startId, int endId) {

        if (startId < 20 && endId < 20) {
            return Math.abs(startId - endId);
        }

        if (startId >= 20 && endId >= 20 && startId <= 40 && endId <= 40) {
            return Math.abs(startId - endId);
        } else {

            if (startId <= 19) {
                int a = Math.abs(startId - 10);
                int b = Math.abs(endId - 29);
                return a + b;
            } else {
                int a = Math.abs(startId - 29);
                int b = Math.abs(endId - 10);
                return a + b;
            }

        }

    }
}
