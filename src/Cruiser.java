public class Cruiser extends Battleships {
    public Cruiser() {
    }

    public Cruiser(int Mpos, int Npos, boolean isVertical) {
        this.Npos = Npos;
        this.Mpos = Mpos;
        this.isVertical = isVertical;
        this.points = new int[3][];
    }

    /**
     * Генерация корабля типа Cruiser.
     *
     * @param M
     * @param N
     * @return generated Cruiser.
     */
    public Cruiser generateCruiser(int M, int N) {
        int[] coords = getShip(3, M, N);
        return new Cruiser(coords[0], coords[1], coords[2] != 0);
    }
}
