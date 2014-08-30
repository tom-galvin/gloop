package io.github.quackmatic.gloop;

public class Vec2 {
	public double x, y;
	
	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static Vec2 add(Vec2 v1, Vec2 v2) {
		return new Vec2(v1.x + v2.x, v1.y + v2.y);
	}
	
	public static Vec2 sub(Vec2 v1, Vec2 v2) {
		return new Vec2(v1.x - v2.x, v1.y - v2.y);
	}
	
	public static Vec2 mul(Vec2 v, double d) {
		return new Vec2(v.x * d, v.y * d);
	}
	
	public static Vec2 div(Vec2 v, double d) {
		return new Vec2(v.x / d, v.y / d);
	}
	
	public static double cross(Vec2 v1, Vec2 v2) {
		return v1.x * v2.y - v1.y * v2.x;
	}
	
	public static double dot(Vec2 v1, Vec2 v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	public static double magSquared(Vec2 v) {
		return v.x * v.x + v.y * v.y;
	}
	
	public static double mag(Vec2 v) {
		return Math.sqrt(Vec2.magSquared(v));
	}
	
	public static Vec2 unit(Vec2 v) {
		double mag = Vec2.mag(v);
		if(mag == 0) return Vec2.zero();
		else return Vec2.div(v, mag);
	}
	
	public static Vec2 rotate(Vec2 v, double theta) {
		double sinTheta = Math.sin(theta);
		double cosTheta = Math.cos(theta);
		
		return new Vec2(
				cosTheta * v.x - sinTheta * v.y,
				sinTheta * v.x + cosTheta * v.y);
	}
	
	public static Vec2 rotate(Vec2 v, double theta, Vec2 c) {
		return v.sub(c).rotate(theta).add(c);
	}
	
	public static Vec2 zero() {
		return new Vec2(0.0, 0.0);
	}
	
	public static double angle(Vec2 v) {
		return Math.atan2(v.y, v.x);
	}
	
	public static double angleTo(Vec2 v1, Vec2 v2) {
		double divisor = v1.mag() * v2.mag();
		if(divisor > 0) {
			return Vec2.dot(v1, v2) / divisor;
		} else {
			return 0;
		}
	}
	
	public static Vec2 projectAlong(Vec2 v, Vec2 vp) {
		return vp.unit().mul(v.mag() * v.angleTo(vp));
	}
	
	public static Vec2 reflectOver(Vec2 v, Vec2 axis) {
		return axis
				.mul(
					2.0 * Vec2.dot(axis, v) / 
					Vec2.magSquared(axis))
				.sub(v);
	}
	
	public static boolean clockwiseABC(Vec2 a, Vec2 b, Vec2 c) {
		return b.sub(a).cross(c.sub(b)) < 0;
	}
	
	public Vec2 add(Vec2 v) {
		return Vec2.add(this, v);
	}
	
	public Vec2 sub(Vec2 v) {
		return Vec2.sub(this, v);
	}
	
	public Vec2 mul(double d) {
		return Vec2.mul(this, d);
	}
	
	public Vec2 div(double d) {
		return Vec2.div(this, d);
	}
	
	public double cross(Vec2 v) {
		return Vec2.cross(this, v);
	}
	
	public double dot(Vec2 v) {
		return Vec2.dot(this, v);
	}
	
	public double magSquared() {
		return Vec2.magSquared(this);
	}
	
	public double mag() {
		return Vec2.mag(this);
	}
	
	public Vec2 unit() {
		return Vec2.unit(this);
	}
	
	public Vec2 rotate(double theta) {
		return Vec2.rotate(this, theta);
	}
	
	public Vec2 rotate(double theta, Vec2 c) {
		return Vec2.rotate(this, theta, c);
	}
	
	public double angle() {
		return Vec2.angle(this);
	}
	
	public double angleTo(Vec2 v) {
		return Vec2.angleTo(this, v);
	}
	
	public Vec2 projectAlong(Vec2 vp) {
		return Vec2.projectAlong(this, vp);
	}
	
	public Vec2 reflectOver(Vec2 axis) {
		return Vec2.reflectOver(this, axis);
	}
	
	public Vec2 clone() {
		return new Vec2(this.x, this.y);
	}
}
