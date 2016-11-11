package com.chinaway.tms.utils;

public class GPSUtil {

	private final static double EARTH_RADIUS = 6378.137;
	
	private static double rad(double d){
	   return d * Math.PI / 180.0;
	}

	/**
	 * 返回2点距离，单位公里(km)
	 * @param lat1	纬度1
	 * @param lng1	经度1
	 * @param lat2	纬度2
	 * @param lng2	经度2
	 * @return
	 */
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;	//两点纬度之差
		double b = rad(lng1) - rad(lng2);//两点经度之差
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = Math.round(s * EARTH_RADIUS * 10000) / 10000.0;
		
		return s;
	}
}
