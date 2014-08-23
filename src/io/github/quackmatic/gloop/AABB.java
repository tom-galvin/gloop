package io.github.quackmatic.gloop;

public class AABB {
	public double x, y, width, height;
	
	public AABB(double x, double y, double width, double height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean colliding(AABB aabb) {
		double horizontalDistance = Math.abs(this.x - aabb.x) - Math.abs(this.width - aabb.width) / 2; 
		double verticalDistance = Math.abs(this.y - aabb.y) - Math.abs(this.height - aabb.height) / 2;
		return (horizontalDistance < 0 || verticalDistance < 0);
	}
	
	public void deintersect(AABB aabb) {
		double horizontalDistance = Math.abs(this.x - aabb.x) - Math.abs(this.width - aabb.width) / 2; 
		double verticalDistance = Math.abs(this.y - aabb.y) - Math.abs(this.height - aabb.height) / 2;
		
		if(horizontalDistance < 0) {
			if(this.x > aabb.x) {
				this.x += horizontalDistance;
			} else {
				this.x -= horizontalDistance;
			}
		}
		
		if(verticalDistance < 0) {
			if(this.y > aabb.y) {
				this.y += verticalDistance;
			} else {
				this.y -= verticalDistance;
			}
		}
	}
}
