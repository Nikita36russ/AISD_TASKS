public class task1zad5 {
    public double x, y, z;

    public task1zad5(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public task1zad5 add(task1zad5 v) {
        return new task1zad5(x + v.x, y + v.y, z + v.z);
    }

    public task1zad5 sub(task1zad5 v) {
        return new task1zad5(x - v.x, y - v.y, z - v.z);
    }

    public double dot(task1zad5 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double cos(task1zad5 v) {
        return dot(v) / (length() * v.length());
    }

    @Override
    public String toString() {
        return "(" + x + "; " + y + "; " + z + ")";
    }

    public static void main(String[] args) {
        task1zad5 a = new task1zad5(1, 2, 3);
        task1zad5 b = new task1zad5(4, 5, 6);

        System.out.println("Сумма: " + a.add(b));
        System.out.println("Разность: " + a.sub(b));
        System.out.println("Скалярное произведение: " + a.dot(b));
        System.out.println("Длина a: " + a.length());
        System.out.println("Косинус: " + a.cos(b));
    }
}