import java.util.Random;

public class Battleships {
    // Начальные координаты корабля.
    int Npos, Mpos;
    // Расположение относительно начальных координат - вертикально или горизонтально.
    boolean isVertical;
    // Список всех клеток корабля.
    int[][] points;

    public Battleships() {
    }

    public Battleships(int Mpos, int Npos, boolean isVertical) {
        this.Npos = Npos;
        this.Mpos = Mpos;
        this.isVertical = isVertical;
    }

    /**
     * Универсальный метод рандомной генерации корабля.
     *
     * @param size
     * @param M
     * @param N
     * @return
     */
    public int[] getShip(int size, int M, int N) {
        // Size - размер корабля, который нужно сгенерировать(1-5)
        Random random = new Random();
        boolean isVertical = random.nextBoolean();
        if (size == M && size == N) {
            Npos = 0;
            Mpos = 0;
        } else if (size == M) {
            if (isVertical) {
                Mpos = 0;
                Npos = random.nextInt(N);
            } else {
                // Проверяем чтобы Координаты корабля позволяли расположить его не выходя за границу.
                Npos = random.nextInt(N - size);
                Mpos = 0;
            }
        } else if (size == N) {
            if (isVertical) {
                Mpos = random.nextInt(M - size);
                Npos = 0;
            } else {
                Npos = 0;
                Mpos = random.nextInt(M);
            }
        } else {
            if (isVertical) {
                // Проверяем чтобы Координаты корабля позволяли расположить его не выходя за границу океана.
                Mpos = random.nextInt(M - size);
                Npos = random.nextInt(N);
            } else {
                // Проверяем чтобы Координаты корабля позволяли расположить его не выходя за границу океана.
                Npos = random.nextInt(N - size);
                Mpos = random.nextInt(M);
            }
        }
        // Возвращаем массив с характеристиками сгенерированного корабля.
        int[] coords = new int[]{Mpos, Npos, isVertical ? 1 : 0};
        return coords;
    }

}
