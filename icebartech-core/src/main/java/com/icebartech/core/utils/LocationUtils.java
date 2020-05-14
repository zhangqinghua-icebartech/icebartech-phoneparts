package com.icebartech.core.utils;

import com.alibaba.fastjson.JSON;

import java.text.DecimalFormat;


/**
 * 定位相关工具类.
 *
 * @author DengZhaoyong
 * @date 2012-4-17
 */
public class LocationUtils {

    /**
     * 地球半径(M)
     */
    private static final double EARTH_RADIUS = 6378137.0;


    /**
     * 根据经纬度计算两点之间的距离.
     *
     * @param lngA A点经度
     * @param latA A点纬度
     * @param lngB B点经度
     * @param latB B点纬度
     * @return double类型距离
     */
    public static double gps2km(double lngA, double latA, double lngB, double latB) {

        double radLat1 = (latA * Math.PI / 180.0);
        double radLat2 = (latB * Math.PI / 180.0);

        double a = radLat1 - radLat2;
        double b = (lngA - lngB) * Math.PI / 180.0;

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));

        s = s * EARTH_RADIUS;

        s = Math.round(s * 10000) / 10000d;

        return s / 1000;
    }

    /**
     * 根据经纬度计算两点之间的距离.
     *
     * @param lngA A点经度
     * @param latA A点纬度
     * @param lngB B点经度
     * @param latB B点纬度
     * @return String类型距离
     */
    public static String gps2kmStr(double lngA, double latA, double lngB, double latB) {

        double s = gps2km(lngA, latA, lngB, latB);

        if (s == 0) {
            return "";
        }

        DecimalFormat format = new DecimalFormat("0.##");
        return format.format(s) + "km";
    }

    public static double gps2kmDouble(double lngA, double latA, double lngB, double latB) {
        double s = gps2km(lngA, latA, lngB, latB);

        if (s == 0) {
            return 0.0;
        }

        DecimalFormat format = new DecimalFormat("0.##");
        return Double.parseDouble(format.format(s));
    }

    /**
     * 获取距离.
     *
     * @param lng    经度
     * @param lat    纬度
     * @param objLng 当前登录用户的经度
     * @param objLat 当前登录用户的纬度
     * @return
     */
    public static String getDistance(Double lng, Double lat, Double objLng, Double objLat) {
        if (lng == null | lat == null || objLng == null
                || objLat == null) {
            return "";
        }
        return gps2kmStr(lng, lat, objLng, objLat);
    }


    /**
     * 根据用户的经纬度和要筛选的距离来组装sql语句（即粗略查询distance距离内的数据，抽象为球面矩形）
     *
     * @param la用户的纬度值
     * @param lo用户的经度值
     * @param distance 要筛选的距离，以KM为单位
     * @return
     */
    public static String graticulesDistance(double la, double lo, int distance) {
        StringBuffer sbBuffer = new StringBuffer();
        distance = distance * 1000;
        double laUp = LocationUtils.changeGeo(la, lo, distance, 0.0)[0];
        double laDown = LocationUtils.changeGeo(la, lo, distance, 180.0)[0];
        double loLeft = LocationUtils.changeGeo(la, lo, distance, 270.0)[1];
        double loRight = LocationUtils.changeGeo(la, lo, distance, 90.0)[1];
        sbBuffer.append(" and (longitude>=");
        sbBuffer.append(loLeft);
        sbBuffer.append(" and longitude<=");
        sbBuffer.append(loRight);
        sbBuffer.append(" and latitude>=");
        sbBuffer.append(laDown);
        sbBuffer.append(" and latitude<=");
        sbBuffer.append(laUp);
        sbBuffer.append(") and latitude>0 and longitude>0");
        return sbBuffer.toString();
    }

    //lat1 纬度,lon1 经度 ,d为距离,brng为与正北方夹角

    /**
     * 根据经纬度和距离计算正上方、正下方、正左方、正右方对应的经纬度
     *
     * @param lat1 纬度
     * @param lon1 经度
     * @param d    距离，m为单位
     * @param brng 与正北方夹角
     * @return
     */
    public static double[] changeGeo(double lat1, double lon1, double d, double brng) {
        double dist = d / EARTH_RADIUS;
        double lat1_rad = Math.toRadians(lat1);
        double lon1_rad = Math.toRadians(lon1);
        double lat2 = Math.asin(Math.sin(lat1_rad) * Math.cos(dist) +
                Math.cos(lat1_rad) * Math.sin(dist) * Math.cos(Math.toRadians(brng)));
        double lon2 = lon1_rad + Math.atan2(Math.sin(Math.toRadians(brng)) * Math.sin(dist) * Math.cos(lat1_rad),
                Math.cos(dist) - Math.sin(lat1_rad) * Math.sin(lat2));
        double[] r = new double[]{Math.toDegrees(lat2), Math.toDegrees(lon2)};
        return r;
    }

    public static void main(String[] args) {
        double[] request = changeGeo(22.5585624, 113.9460232, 2000f, 180.0);
        System.out.println("latitude:" + request[0] + " longitude:" + request[1]);


        System.out.println(JSON.toJSON(changeGeo(22.5585624, 113.9460232, 10000, 270.0)));

//		String str = getDistance(113.9460232, 22.5585624, 113.934, 22.5564);
//		System.out.println(str);
        //latitude:22.648393928411956 longitude:113.9460232

    }

}
