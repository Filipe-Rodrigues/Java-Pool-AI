package pool.utils;

import static pool.utils.Coordinate2D.*;

public class RayTracingStats {
    public Coordinate2D a;
    public Coordinate2D b;
    public Coordinate2D c;
    public Coordinate2D d;
    
    public Coordinate2D normal;
    public Coordinate2D r;
    public Coordinate2D s;
    public double t;
    public double u;
    
    public RayTracingStats(Coordinate2D a, Coordinate2D b, Coordinate2D c, Coordinate2D d, boolean bdAsVectors) {
        this.a = a;
        this.c = c;
        if (bdAsVectors) {
            this.b = sub(b, a);
            this.d = sub(d, c);
            r = b;
            s = d;
        } else {
            this.b = b;
            this.d = d;
            r = getVector(a, b);
            s = getVector(c, d);
        }
        computeDependentVariables();
    }
    
    private void computeDependentVariables() {
        normal = r.getNormal();
        Coordinate2D subtraction = getVector(a, c);
        double crossProduct = getCrossProduct(r, s);
        t = getCrossProduct(subtraction, s) / crossProduct;
        u = getCrossProduct(subtraction, r) / crossProduct;
    }
}
