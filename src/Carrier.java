import java.util.Random;

public class Carrier extends Battleships {

    public Carrier(int Mpos, int Npos, boolean isVertical) {
        super(Mpos, Npos, isVertical);
        this.Npos = Npos;
        this.Mpos = Mpos;
        // Т.К. Carrier имеет длину 5 клеток.
        this.points = new int[5][];
    }

    public Carrier() {
    }

    /**
     * Генерация корабля типа Carrier.
     *
     * @param M
     * @param N
     * @return generated Carrier.
     */
    public Carrier generateCarrier(int M, int N) {
        int[] coords = getShip(5, M, N);
        return new Carrier(coords[0], coords[1], coords[2] != 0);
    }
}
