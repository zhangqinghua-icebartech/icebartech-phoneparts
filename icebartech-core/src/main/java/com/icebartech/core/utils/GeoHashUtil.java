package com.icebartech.core.utils;

import ch.hsr.geohash.GeoHash;
import ch.hsr.geohash.WGS84Point;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * geo_code长度和距离的对照表：
 * geohash length   width(km)
 * 1          ±2500
 * 2	        ±630
 * 3	        ±78
 * 4	        ±20
 * 5	        ±2.4
 * 6	        ±0.61
 * 7       	±0.076
 * 8       	±0.019
 */


public class GeoHashUtil {

    // geoHashCode每增加一位可以标识的区域大小
    public static final int[] geo = {Integer.MAX_VALUE, 5000000, 1260000, 156000, 40000, 4800, 1220, 152, 38};

    /**
     * 获取当前地图区域大小最大用几位code表示而不超过地图大小
     *
     * @param areaSize 地图区域大小(最长边的大小，单位：m)
     * @return
     */
    public static int calculateGeoHashCodeLength(int areaSize) {
        int i = 0;
        for (; i < geo.length; i++) {
            if (areaSize >= geo[i]) {
                return i;
            }
        }
        return i - 1;
    }

    /**
     * 获取周边块code
     *
     * @param geoHash
     * @param layer
     * @return
     */
    public static Set getAdjacent(GeoHash geoHash, int layer) {
        Set<String> out = new HashSet<>();
        out.add(geoHash.toBase32());
        if (layer > 0) {
            GeoHash[] adjacent = geoHash.getAdjacent();
            for (GeoHash hash : adjacent) {
                out.addAll(getAdjacent(hash, layer - 1));
            }
        }
        return out;
    }

    /**
     * 将Geohash字串解码成经纬值
     *
     * @param geohash 待解码的Geohash字串
     * @return 经纬值数组
     */
    public static double[] decode(String geohash) {
        GeoHash geoHash = GeoHash.fromGeohashString(geohash);
        WGS84Point point = geoHash.getPoint();
        return new double[]{point.getLatitude(), point.getLongitude()};
    }

    /**
     * 根据经纬值得到Geohash字串
     *
     * @param lat 纬度值
     * @param lon 经度值
     * @return Geohash字串
     */
    public static String encode(double lon, double lat, int numberOfCharacters) {
        return GeoHash.geoHashStringWithCharacterPrecision(lat, lon, numberOfCharacters);
    }

    public static void main(String[] args) {

        int[] zoom = {20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 25000, 50000, 100000, 200000, 500000, 1000000, 2000000, 5000000, 10000000};

        int zoomLevel = 19 - 19;
        int mapSize = zoom[zoomLevel] * 30;

        int i = 0;
        for (; i < geo.length; i++) {
            if (mapSize >= geo[i]) {
                break;
            }
        }

        int layer = (mapSize / geo[i] / 3) + 1;

        System.out.println("maxLength:" + mapSize);
        System.out.println("geoLength:" + geo[i]);
        System.out.println("layer:" + layer);


        double[] cen = {114.100859, 22.545891};


        GeoHash geoHash = GeoHash.withCharacterPrecision(cen[1], cen[0], i);

        Set<String> codes = getAdjacent(geoHash, layer);
        int c = 0;
        for (String code : codes) {
            System.out.println("renge" + ++c + ":" + code);
        }


        List<Map<String, String>> test = new ArrayList<>();
        Map<String, String> tempMap1 = new HashMap<>();
        tempMap1.put("name", "冰棍科技");
        tempMap1.put("code", encode(-35.208743, 73.422063, i));
        test.add(tempMap1);
        Map<String, String> tempMap2 = new HashMap<>();
        tempMap2.put("name", "金融基地");
        tempMap2.put("code", encode(-19.019122, 64.325389, i));
        test.add(tempMap2);
        Map<String, String> tempMap3 = new HashMap<>();
        tempMap3.put("name", "深大地铁站");
        tempMap3.put("code", encode(72.525827, -76.71877, i));
        test.add(tempMap3);
        Map<String, String> tempMap4 = new HashMap<>();
        tempMap4.put("name", "桃园地铁站");
        tempMap4.put("code", encode(129.33668, -24.694641, i));
        test.add(tempMap4);
        Map<String, String> tempMap5 = new HashMap<>();
        tempMap5.put("name", "购物公园");
        tempMap5.put("code", encode(114.097917, 22.540017, i));
        test.add(tempMap5);

        Map<String, String> tempMap6 = new HashMap<>();
        tempMap5.put("name", "东湖公园");
        tempMap5.put("code", encode(114.147144, 22.563114, i));
        test.add(tempMap5);

        for (Map<String, String> map : test) {
            System.out.print(map.get("code") + ": " + map.get("name"));
            if (CollectionUtils.contains(codes.iterator(), map.get("code"))) {
                System.out.print(" ✔ ");
            }
            System.out.println();

        }

        double[] p = decode("ws10k8m3");
        System.out.println("lat:" + p[0] + " lon:" + p[1]);
    }
}