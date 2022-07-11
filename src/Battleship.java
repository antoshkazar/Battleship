public class Battleship extends Battleships {
    public Battleship() {
    }

    public Battleship(int Mpos, int Npos, boolean isVertical) {
        this.Npos = Npos;
        this.Mpos = Mpos;
        this.isVertical = isVertical;
        this.points = new int[4][];
    }

    /**
     * Генерация корабля типа Battleship.
     *
     * @param M
     * @param N
     * @return generated Battleship.
     */
    public Battleship generateBattleship(int M, int N) {
        int[] coords = getShip(4, M, N);
        return new Battleship(coords[0], coords[1], coords[2] != 0);
    }
}
