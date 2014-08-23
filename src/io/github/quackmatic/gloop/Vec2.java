package io.github.quackmatic.gloop;

public class Vec2 {
	double x, y;
	
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
	
	public static double dot(Vec2 v1, Vec2 v2) {
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	public static double mag(Vec2 v) {
		return Math.sqrt(v.x * v.x + v.y * v.y);
	}
	
	public static Vec2 unit(Vec2 v) {
		return Vec2.div(v, Vec2.mag(v));
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
	
	public double dot(Vec2 v) {
		return Vec2.dot(this, v);
	}
	
	public double mag() {
		return Vec2.mag(this);
	}
	
	public Vec2 unit() {
		return Vec2.unit(this);
	}
}
