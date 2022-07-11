public class Submarine extends Battleships {
    public Submarine() {
    }

    public Submarine(int Mpos, int Npos, boolean isVertical) {
        this.Npos = Npos;
        this.Mpos = Mpos;
        this.isVertical = isVertical;
        this.points = new int[1][];
    }

    /**
     * Генерация корабля типа Submarine.
     *
     * @param M
     * @param N
     * @return generated Submarine
     */
    public Submarine generateSubmarine(int M, int N) {
        int[] coords = getShip(1, M, N);
        return new Submarine(coords[0], coords[1], coords[2] != 0);
    }
}
