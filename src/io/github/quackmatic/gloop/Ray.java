package io.github.quackmatic.gloop;

public class Ray {
	public Vec2 pos;
	public Vec2 dir;
	
	public Ray(Vec2 pos, Vec2 dir) {
		this.pos = pos.clone();
		this.dir = dir.unit();
	}
	
	public Ray(Vec2 pos, double theta) {
		this.pos = pos.clone();
		this.dir = new Vec2(Math.sin(theta), Math.cos(theta));
	}
	
	public Vec2 intersectLine(Vec2 l1, Vec2 l2)
	{
	    double xd = l2.x - l1.x, yd = l2.y - l1.y;
	    double d1 = xd * dir.y, d2 = yd * dir.x;
	    if(d1 != d2)
	    {
	        double t =
	            (yd * pos.x + xd * l1.y -
	            yd * l1.x - xd * pos.y) / (d1 - d2);
	        if(t >= 0)
	        {
	            Vec2 uv = dir.mul(t).add(pos);
	            double m = (xd != 0) ?
	                (uv.x - l1.x) / xd :
	                (uv.y - l1.y) / yd;
	            if(m >= 0 && m <= 1)
	            {
	                return new Vec2(uv.x, uv.y);
	            }
	        }
	    }
	    return null;
	}
}
