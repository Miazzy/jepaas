package com.je.core.util;

import org.apache.commons.lang.StringUtils;

/**
 * @Auther: wangmm@ketr.com.cn
 * @Date: 2019/3/3 23:12
 * @Description:
 */
public class LatLonUtil {
    private static double PI = 3.14159265;
    private static double EARTH_RADIUS = 6378137;
    private static double RAD = Math.PI / 180.0;

    /// <summary>
    /// 根据提供的经度和纬度、以及半径，取得此半径内的最大最小经纬度
    /// </summary>
    /// <param name="lat">纬度</param>
    /// <param name="lon">经度</param>
    /// <param name="raidus">半径(米)</param>
    /// <returns></returns>
    public static double[] GetAround(double lat, double lon, int raidus) {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        return new double[] { minLat, minLng, maxLat, maxLng };
    }

    /// <summary>
    /// 根据提供的两个经纬度计算距离(米)
    /// </summary>
    /// <param name="lng1">经度1</param>
    /// <param name="lat1">纬度1</param>
    /// <param name="lng2">经度2</param>
    /// <param name="lat2">纬度2</param>
    /// <returns></returns>
    public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLat1 = lat1 * RAD;
        double radLat2 = lat2 * RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2) * RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000L) /(double)10000;
        return s;
    }
    public static double GetDistance(String lng1, String lat1, String lng2, String lat2) {
        if (StringUtils.isEmpty(lng1) || StringUtils.isEmpty(lat1)
                || StringUtils.isEmpty(lng2) || StringUtils.isEmpty(lat2)) {
            return 0.0;
        } else {
            try {
                return GetDistance(Double.valueOf(lng1),Double.valueOf(lat1),
                        Double.valueOf(lng2),Double.valueOf(lat2));
            } catch (Exception e) {
                e.printStackTrace();
                return 0.0;
            }
        }
    }

    /**
     * 根据当前坐标 和目标方位 获取时钟方向
     *
     * @param lat1
     * @param lag1
     * @param lat2
     * @param lng2
     * @return
     */
    public static Double getDirection(Double lat1, Double lag1, Double lat2, Double lng2) {
        Double direction = 0.0;
        Double k1 = lng2 - lag1;
        Double k2 = lat2 - lat1;
        if (0 == k1) {
            if (k2 > 0) {
                direction = 12.0;
            } else if (k2 < 0) {
                direction = 6.0;
            } else if (k2 == 0) {
                direction = 0.0;
            }
        } else if (0 == k2) {
            if (k1 > 0) {
                direction = 3.0;
            } else if (k1 < 0) {
                direction = 9.0;
            }
        } else {
            Double k = k2 / k1;
            if (k2 > 0) {
                if (k1 > 0) {
                    double angle = 180 * Math.atan(k) / PI;
                    direction = (1.0 / 30.0) * angle;
                } else if (k1 < 0) {
                    double angle = 180 * Math.atan(-k) / PI;
                    direction = 9.0 + (1.0 / 30.0) * angle;
                }
            } else if (k2 < 0) {
                if (k1 < 0) {
                    double angle = Math.atan(k) / PI;
                    direction = 9.0 - (1.0 / 30.0) * angle;
                } else if (k1 > 0) {
                    double angle = Math.atan(k) / PI;
                    direction = 3.0 + (1.0 / 30.0) * angle;
                }
            }

        }
        return direction;
    }

}
