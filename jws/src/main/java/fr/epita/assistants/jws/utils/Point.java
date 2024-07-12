package fr.epita.assistants.jws.utils;

import java.util.Objects;

public class Point {
    int x;
    int y;

    public Point plus(int x, int y)
    {
        Point point = new Point(this.x,this.y);
        point.x += x;
        point.y += y;
        return point;
    }

    public Point plus(Point p)
    {
        Point point = new Point(this.x,this.y);
        point.x += p.x;
        point.y += p.y;
        return point;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public static Point[] initarray()
    {
        Point[] p = new Point[4];
        p[0] = new Point(1,1);
        p[1] = new Point(15,1);
        p[2] = new Point(15,13);
        p[3] = new Point(1,13);
        return p;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
