package com.laobi.lnglat;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LngLatTest {

    // 地球半径
    public static final double EARTH_RADIUS = 6378137.0; // 单位M

    private static double getRad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 两坐标（经纬度）之间距离计算
     * GD/BD地图采用此算法
     * @param startLng 起点坐标经度
     * @param startLat 起点坐标纬度
     * @param endLng 终点坐标经度
     * @param endLat 终点坐标纬度
     * @return 起点坐标与终点坐标的距离 单位:m
     */
    public static double getGreatCircleDistance(double startLng, double startLat, double endLng, double endLat) {
        double radLat1 = getRad(startLat);
        double radLat2 = getRad(endLat);
        double dy = radLat1 - radLat2; // a
        double dx = getRad(startLng) - getRad(endLng); // b
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(dy / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(dx / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000.0;
        return s;
    }

    public static void main(String[] args) throws IOException {

        //18.0779789866,108.6273193359
        //20.1694112276,111.4672851563

        double latBase = 17.8951143037;
        double lngBase = 108.1933593750;

        List<String> list = Lists.newArrayList();
        // 254
        int lngNum = 0;
        while(latBase < 20.1694112276 && lngBase < 111.4672851563) {


            // 东经106.486654度 北纬23.490295度 向东移动1000米 向北移动1000米
            double lng = getMoveLng(latBase, 1000) + lngBase;
            double lat = getMoveLat(1000) + latBase;


            List<GeoCoordinate> geoCoordinateList = new ArrayList<GeoCoordinate>();
            GeoCoordinate g = new GeoCoordinate();
            g.setLongitude(lngBase);
            g.setLatitude(latBase);
            GeoCoordinate g2 = new GeoCoordinate();
            g2.setLongitude(lng);
            g2.setLatitude(latBase);

            GeoCoordinate g3 = new GeoCoordinate();
            g3.setLongitude(lngBase);
            g3.setLatitude(lat);

            GeoCoordinate g4 = new GeoCoordinate();
            g4.setLongitude(lng);
            g4.setLatitude(lat);


            geoCoordinateList.add(g);
            geoCoordinateList.add(g2);
            geoCoordinateList.add(g3);
            geoCoordinateList.add(g4);

            GeoCoordinate  re = getCenterPoint(geoCoordinateList);

            double centerLng = re.getLongitude(); //getMoveLng(latBase, 500) + lngBase;
            double centerLat = re.getLatitude(); //getMoveLat(500) + latBase;

            list.add(latBase + "," + lat + "," + lngBase + "," + lng + "\t" + centerLat + "," + centerLng);

            if(lat > 20.1694112276) {
                lngBase = lng;
                latBase = 17.8951143037;
                lngNum++;
            } else {
                latBase = lat;
            }

        }

        IOUtils.writeLines(list, "\n", new FileOutputStream("/Users/byb/Downloads/lnglat_center.txt"));

        System.out.println(lngNum);

        System.out.println(list.size());

        System.out.println(list.get(list.size() / 2));

//        System.out.print(lat + "," + lng);
//        System.out.println("移动后两点距离（米）：" + getGreatCircleDistance(106.486654, 23.490295,lng,lat));
    }


    /**
     *  根据输入的地点坐标计算中心点
     * @param geoCoordinateList
     * @return
     */
    public static GeoCoordinate getCenterPoint(List<GeoCoordinate> geoCoordinateList) {
        int total = geoCoordinateList.size();
        double X = 0, Y = 0, Z = 0;
        for (GeoCoordinate g : geoCoordinateList) {
            double lat, lon, x, y, z;
            lat = g.getLatitude() * Math.PI / 180;
            lon = g.getLongitude() * Math.PI / 180;
            x = Math.cos(lat) * Math.cos(lon);
            y = Math.cos(lat) * Math.sin(lon);
            z = Math.sin(lat);
            X += x;
            Y += y;
            Z += z;
        }

        X = X / total;
        Y = Y / total;
        Z = Z / total;
        double Lon = Math.atan2(Y, X);
        double Hyp = Math.sqrt(X * X + Y * Y);
        double Lat = Math.atan2(Z, Hyp);
        return new GeoCoordinate(Lat * 180 / Math.PI, Lon * 180 / Math.PI);
    }

    /**
     * 根据输入的地点坐标计算中心点（适用于400km以下的场合）
     * @param geoCoordinateList
     * @return
     */
    public static GeoCoordinate getCenterPoint400(List<GeoCoordinate> geoCoordinateList) {
        // 以下为简化方法（400km以内）
        int total = geoCoordinateList.size();
        double lat = 0, lon = 0;
        for (GeoCoordinate g : geoCoordinateList) {
            lat += g.getLatitude() * Math.PI / 180;
            lon += g.getLongitude() * Math.PI / 180;
        }
        lat /= total;
        lon /= total;
        return new GeoCoordinate(lat * 180 / Math.PI, lon * 180 / Math.PI);
    }

    /**
     * 获取纬度移动latMovement米，纬度移动的度数
     * @param latMovement 单位：米
     * @return 纬度移动的度数
     */
    public static double getMoveLat(double latMovement) {
        return (180/(Math.PI * EARTH_RADIUS))*latMovement;
    }

    /**
     * 获取经度移动lngMovement米，经度移动的度数
     * @param lat 纬度
     * @param lngMovement 单位：米
     * @return 经度移动的度数
     */
    public static double getMoveLng(double lat, double lngMovement) {
        return (180/(Math.PI * EARTH_RADIUS*Math.cos(Math.toRadians(lat))))*lngMovement;
    }

}

class GeoCoordinate {

    private double latitude;
    private double longitude;

    public GeoCoordinate() {
    }

    public GeoCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
