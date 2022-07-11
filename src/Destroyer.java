public class Destroyer extends Battleships {
    public Destroyer() {
    }

    public Destroyer(int Mpos, int Npos, boolean isVertical) {
        this.Npos = Npos;
        this.Mpos = Mpos;
        this.isVertical = isVertical;
        this.points = new int[2][];
    }

    /**
     * Генерация корабля типа Destroyer.
     *
     * @param M
     * @param N
     * @return generated Destroyer
     */
    public Destroyer generateDestroyer(int M, int N) {
        int[] coords = getShip(2, M, N);
        return new Destroyer(coords[0], coords[1], coords[2] != 0);
    }
}
