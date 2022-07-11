import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Main {
    private static List<Battleships> battleships = new Vector<Battleships>();
    static int totalShots = 0;

    public static void main(String[] args) {
        System.out.println("Hello and welcome to the Battleship game!\n" +
                "The rules of the game are pretty easy: you enter the size of the ocean,\n" +
                "the number of ships of each type and try to make all of them sink!\n" +
                "To get started, please, enter the horizontal size of the ocean (from 10 to 40)");
        int M = getSize(-1);
        System.out.println("Now please enter the vertical size of an ocean (from 10 to 40)");
        int N = getSize(-1);
        char[][] ocean = new char[M][N];
        // Заполняем океан дефолтными значениями.
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                ocean[i][j] = '·';
            }
        }
        System.out.println("Enter number of ships in format '1,2,3,1,2' where 1 - is quantity of carriers, 2 - of battleships etc (max 9 for each type).");
        int[] quantity = getQuantity(M * N);
        // Так как в массиве quantitу на соответствующем месте находится количество кораблей соотв. типа, запускаем циклы расположения кораблей.
        for (int i = 0; i < quantity[0]; i++) {
            ocean = setCarriers(ocean, M, N);
        }
        for (int i = 0; i < quantity[1]; i++) {
            ocean = setBattleships(ocean, M, N);
        }
        for (int i = 0; i < quantity[2]; i++) {
            ocean = setCruisers(ocean, M, N);
        }
        for (int i = 0; i < quantity[3]; i++) {
            ocean = setDestroyers(ocean, M, N);
        }
        for (int i = 0; i < quantity[4]; i++) {
            ocean = setSubmarines(ocean, M, N);
        }
        System.out.println("Ocean and ships are succesfully generated!");
        StartShooting(ocean, M, N);
    }

    /**
     * Реализация стрельбы по кораблям, пока все не утонут.
     *
     * @param ocean
     * @param M
     * @param N
     */
    static void StartShooting(char[][] ocean, int M, int N) {
        System.out.println("Now let's shoot! Info:\n '·' - not shot \n '#' - shot but missed \n '*' - shot and hit");
        print(ocean, M, N);
        // Так как утонувший корабль удаляется из списка, игра продолжается пока список не пуст.
        while (!battleships.isEmpty()) {
            int[] coords = getCoordinate(M, N, ocean);
            // Так как нумерация в массиве с нуля, а для user-friendly интерфейса больше подходит нумерация с единицы, вычитаем 1 у координат
            coords[0]--;
            coords[1]--;
            // Было ли попадание;
            boolean haveHit = false;
            for (var ship : battleships) {
                for (int i = 0; i < ship.points.length; i++) {
                    // Если попадание было, нужно проверить, не затонул ли после него корабль
                    if (ship.points[i][0] == coords[0] && ship.points[i][1] == coords[1]) {
                        boolean shipSunk = true;
                        for (int j = 0; j < ship.points.length; j++) {
                            // Так как при попадании в клетку корбался ей присваивается значение -1;-1, то проверяем ее на это значение
                            if (ship.points[j][0] == -1 && ship.points[j][1] == -1) {
                                continue;
                            } else if ((ship.points[j][0] != -1 || ship.points[j][1] != -1) && i != j) {
                                // Если найдена клетка корабля со значением не -1;-1 , то он не утонул
                                shipSunk = false;
                            }
                        }
                        if (shipSunk) {
                            System.out.println("You just have sunk a ship of the " + ship.getClass().toString());
                            // Помечаем клетку кораблся на карте океана подбитой.
                            ocean[coords[0]][coords[1]] = '*';
                            // Присваиваем последней клетке корабля координаты -1;-1 (для красоты).
                            ship.points[i][0] = -1;
                            ship.points[i][1] = -1;
                            haveHit = true;
                            // Удаляем затонувший корабль.
                            battleships.remove(ship);
                            break;
                        } else {
                            // Если корабль был подбит, но не затонул, выводим Hit
                            System.out.println("Hit!!");
                            ocean[coords[0]][coords[1]] = '*';
                            ship.points[i][0] = -1;
                            ship.points[i][1] = -1;
                            haveHit = true;
                            break;
                        }
                    }
                }
                if (haveHit) break;
            }
            if (!haveHit) {
                // Не было попадания в корабль.
                System.out.println("Miss!");
                ocean[coords[0]][coords[1]] = '#';
            }
            totalShots++;
            print(ocean, M, N);
        }
        System.out.println(String.format("Game over! You managed with all ships in %s shots.", totalShots));
    }

    /**
     * Получаем Х и У координаты выстрела и проверяем их корректность.
     *
     * @param M
     * @param N
     * @param ocean
     * @return
     */
    static int[] getCoordinate(int M, int N, char[][] ocean) {
        boolean isCorrect = false;
        int xcoord = 0, ycoord = 0;
        while (!isCorrect) {
            try {
                System.out.println("Enter x coordinate of your shot:");
                Scanner myscan = new Scanner(System.in);
                xcoord = myscan.nextInt();
                System.out.println("Enter y coordinate of your shot");
                Scanner myscan1 = new Scanner(System.in);
                ycoord = myscan1.nextInt();
                isCorrect = true;
                // Выдаем ошибку если координаты выходят за пределы океана или не положительные.
                if (xcoord > M || xcoord <= 0 || ycoord > N || ycoord <= 0) {
                    System.out.println(String.format("Wrong Input! Your coords must fit in the ocean: 0 < x <= %s, 0 < y <= %s", M, N));
                    isCorrect = false;
                }
                // Выдаем ошибку если эти координаты уже были введены ранее ( в клетку стреляли )
                if (ocean[xcoord - 1][ycoord - 1] == '*' || ocean[xcoord - 1][ycoord - 1] == '#') {
                    System.out.println("Wrong input! Field already shot!");
                    isCorrect = false;
                }
            } catch (Exception ex) {
                isCorrect = false;
                System.out.println("PLease try again ");
            }
        }
        return new int[]{xcoord, ycoord};
    }

    /**
     * Считывание количества кораблей с консоли и проверка введенных данных.
     *
     * @param total
     * @return
     */
    private static int[] getQuantity(int total) {
        boolean isCorrect = false;
        int[] quantity = new int[5];
        while (!isCorrect) {
            try {
                Scanner myscan = new Scanner(System.in);
                String ships = myscan.nextLine();
                if (ships.length() > 9) {
                    System.out.println("Wrong input! Use the following format: 1,2,3,1,2 . You can place not more then 9 ships of each size;");
                    isCorrect = false;
                } else {
                    quantity[0] = Character.getNumericValue(ships.charAt(0));
                    quantity[1] = Character.getNumericValue(ships.charAt(2));
                    quantity[2] = Character.getNumericValue(ships.charAt(4));
                    quantity[3] = Character.getNumericValue(ships.charAt(6));
                    quantity[4] = Character.getNumericValue(ships.charAt(8));
                    isCorrect = true;
                    //Проверяем чтобы максимальное число клеток, выделяемых под корабли и резервируемые ими области не превосходило размер океана.
                    int totalSquares = quantity[0] * 21 + quantity[1] * 18 + quantity[2] * 15 + quantity[3] * 12 + quantity[4] * 8;
                    if (totalSquares > total) {
                        System.out.println("Wrong input! Unable to place so many ships on the current size of an ocean! Please try again.");
                        isCorrect = false;
                    }
                }
            } catch (Exception ex) {
                isCorrect = false;
                System.out.println("Wrong input! PLease try again using following format: 1,2,3,2,0 ");
            }
        }
        return quantity;
    }

    /**
     * Вывод океана в консоль.
     *
     * @param ocean
     * @param M
     * @param N
     */
    private static void print(char[][] ocean, int M, int N) {
        System.out.println("Current ocean");
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                // Можно закомментить условный оператор, чтобы видеть, как распределяются корабли и забаненные клетки вокруг них.
                if (ocean[i][j] == 'b' || ocean[i][j] == '^') {
                    System.out.print('·');
                } else {
                    System.out.print(ocean[i][j]);
                }
                System.out.print('\t');
            }
            System.out.print('\n');
        }
    }

    /**
     * Ввод размеров океана их проверка корректности ввода.
     */
    private static int getSize(int M) {

        while (M < 10 || M > 40) {
            Scanner myscan = new Scanner(System.in);
            try {
                M = myscan.nextInt();
                if (M < 10 || M > 40) {
                    System.out.println("Wrong input! Must be an integer from 10 to 40! Please try again!");
                }
            } catch (Exception exception) {
                System.out.println("Wrong input! Please try again!");
                M = -1;
            }
        }
        return M;
    }

    /**
     * Размещение кораблей типa Carrier в океане.
     *
     * @param ocean
     * @param M
     * @param N
     * @return
     */
    public static char[][] setCarriers(char[][] ocean, int M, int N) {
        Carrier carr = new Carrier();
        boolean isCopied = true;
        while (isCopied) {
            isCopied = false;
            Carrier newCarrier = carr.generateCarrier(M, N);
            if (!newCarrier.isVertical) {
                for (int i = 0; i < 5; i++) {
                    // Оператор проверяет чтобы не было наложений полей сгенерированного корабля на уже существующие.
                    // Если они есть то генерация запускается заново, и так до тех пор, пока все не сгенерируется корректно.
                    if (ocean[newCarrier.Mpos][newCarrier.Npos + i] == 'b' || ocean[newCarrier.Mpos][newCarrier.Npos + i] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    // Если корбаль сгенерировался корректно, то располагаем его в океане.
                    for (int i = 0; i < 5; i++) {
                        ocean[newCarrier.Mpos][newCarrier.Npos + i] = '^';
                        // Резервируем все клетки вокруг отведенной под корабль, чтобы на в будущем опеределять наложения.
                        ocean = checkAdjacent(newCarrier, ocean, i, newCarrier.isVertical);
                        // В массив клеток корабля добавляем текующую.
                        newCarrier.points[i] = new int[]{newCarrier.Mpos, newCarrier.Npos + i};
                    }
                    //В список кораблей добавляем только что сгенерированный.
                    battleships.add(newCarrier);
                }
            } else {
                // Все описанное в этом блоке ELSE аналогично вышеуказанному, только срабатывает, если нужно сгенерировать вертикальный корабль.
                for (int i = 0; i < 5; i++) {
                    if (ocean[newCarrier.Mpos + i][newCarrier.Npos] == 'b' || ocean[newCarrier.Mpos + i][newCarrier.Npos] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 5; i++) {
                        ocean[newCarrier.Mpos + i][newCarrier.Npos] = '^';
                        ocean = checkAdjacent(newCarrier, ocean, i, newCarrier.isVertical);
                        newCarrier.points[i] = new int[]{newCarrier.Mpos + i, newCarrier.Npos};
                    }
                    battleships.add(newCarrier);
                }
            }
        }
        return ocean;
    }

    /**
     * Расположение кораблей типа Battleship в океане.
     *
     * @param ocean
     * @param M
     * @param N
     * @return
     */

    public static char[][] setBattleships(char[][] ocean, int M, int N) {
        // Методы расположения различных типов кораблей имеют похожую реализацию, поэтому, полагаю, документировать каждый излишне.
        // Полное описание всех производимых ниже действий приведено в методе setCarriers
        Battleship batt = new Battleship();
        boolean isCopied = true;
        while (isCopied) {
            isCopied = false;
            Battleship battNew = batt.generateBattleship(M, N);
            if (!battNew.isVertical) {
                for (int i = 0; i < 4; i++) {
                    if (ocean[battNew.Mpos][battNew.Npos + i] == 'b' || ocean[battNew.Mpos][battNew.Npos + i] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 4; i++) {
                        ocean[battNew.Mpos][batt.Npos + i] = '^';
                        ocean = checkAdjacent(battNew, ocean, i, battNew.isVertical);
                        battNew.points[i] = new int[]{battNew.Mpos, battNew.Npos + i};
                    }
                    battleships.add(battNew);
                }
            } else {
                for (int i = 0; i < 4; i++) {
                    if (ocean[battNew.Mpos + i][battNew.Npos] == 'b' || ocean[battNew.Mpos + i][battNew.Npos] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 4; i++) {
                        ocean[battNew.Mpos + i][battNew.Npos] = '^';
                        ocean = checkAdjacent(battNew, ocean, i, battNew.isVertical);
                        battNew.points[i] = new int[]{battNew.Mpos + i, batt.Npos};
                    }
                    battleships.add(battNew);
                }
            }
        }
        return ocean;
    }

    /**
     * Расположение кораблей типа Cruiser в океане.
     *
     * @param ocean
     * @param M
     * @param N
     * @return
     */

    public static char[][] setCruisers(char[][] ocean, int M, int N) {
        // Методы расположения различных типов кораблей имеют похожую реализацию, поэтому, полагаю, документировать каждый излишне.
        // Полное описание всех производимых ниже действий приведено в методе setCarriers
        Cruiser cruiser = new Cruiser();
        boolean isCopied = true;
        while (isCopied) {
            isCopied = false;
            Cruiser newCruiser = cruiser.generateCruiser(M, N);
            if (!newCruiser.isVertical) {
                for (int i = 0; i < 3; i++) {
                    if (ocean[newCruiser.Mpos][newCruiser.Npos + i] == 'b' || ocean[newCruiser.Mpos][newCruiser.Npos + i] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 3; i++) {
                        ocean[newCruiser.Mpos][newCruiser.Npos + i] = '^';
                        ocean = checkAdjacent(newCruiser, ocean, i, newCruiser.isVertical);
                        newCruiser.points[i] = new int[]{newCruiser.Mpos, newCruiser.Npos + i};
                    }
                    battleships.add(newCruiser);
                }
            } else {
                for (int i = 0; i < 3; i++) {
                    if (ocean[newCruiser.Mpos + i][newCruiser.Npos] == 'b' || ocean[newCruiser.Mpos + i][newCruiser.Npos] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 3; i++) {
                        ocean[newCruiser.Mpos + i][newCruiser.Npos] = '^';
                        ocean = checkAdjacent(newCruiser, ocean, i, newCruiser.isVertical);
                        newCruiser.points[i] = new int[]{newCruiser.Mpos + i, newCruiser.Npos};
                    }
                    battleships.add(newCruiser);
                }
            }
        }
        return ocean;
    }

    /**
     * Расположение кораблей типа Destroyer в океане.
     *
     * @param ocean
     * @param M
     * @param N
     * @return
     */
    public static char[][] setDestroyers(char[][] ocean, int M, int N) {
        // Методы расположения различных типов кораблей имеют похожую реализацию, поэтому, полагаю, документировать каждый излишне.
        // Полное описание всех производимых ниже действий приведено в методе setCarriers
        Destroyer destroyer = new Destroyer();
        boolean isCopied = true;
        while (isCopied) {
            isCopied = false;
            Destroyer newDestroyer = destroyer.generateDestroyer(M, N);
            if (!newDestroyer.isVertical) {
                for (int i = 0; i < 2; i++) {
                    if (ocean[newDestroyer.Mpos][newDestroyer.Npos + i] == 'b' || ocean[newDestroyer.Mpos][newDestroyer.Npos + i] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 2; i++) {
                        ocean[newDestroyer.Mpos][newDestroyer.Npos + i] = '^';
                        ocean = checkAdjacent(newDestroyer, ocean, i, newDestroyer.isVertical);
                        newDestroyer.points[i] = new int[]{newDestroyer.Mpos, newDestroyer.Npos + i};
                    }
                    battleships.add(newDestroyer);
                }
            } else {
                for (int i = 0; i < 2; i++) {
                    if (ocean[newDestroyer.Mpos + i][newDestroyer.Npos] == 'b' || ocean[newDestroyer.Mpos + i][newDestroyer.Npos] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 2; i++) {
                        ocean[newDestroyer.Mpos + i][newDestroyer.Npos] = '^';
                        ocean = checkAdjacent(newDestroyer, ocean, i, newDestroyer.isVertical);
                        newDestroyer.points[i] = new int[]{newDestroyer.Mpos + i, newDestroyer.Npos};
                    }
                    battleships.add(newDestroyer);
                }
            }
        }
        return ocean;
    }

    /**
     * Расположение кораблей типа Submarine в океане.
     *
     * @param ocean
     * @param M
     * @param N
     * @return
     */
    public static char[][] setSubmarines(char[][] ocean, int M, int N) {
        // Методы расположения различных типов кораблей имеют похожую реализацию, поэтому, полагаю, документировать каждый излишне.
        // Полное описание всех производимых ниже действий приведено в методе setCarriers
        Submarine submarine = new Submarine();
        boolean isCopied = true;
        while (isCopied) {
            isCopied = false;
            Submarine newSubmarine = submarine.generateSubmarine(M, N);
            if (!newSubmarine.isVertical) {
                for (int i = 0; i < 1; i++) {
                    if (ocean[newSubmarine.Mpos][newSubmarine.Npos + i] == 'b' || ocean[newSubmarine.Mpos][newSubmarine.Npos + i] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 1; i++) {
                        ocean[newSubmarine.Mpos][newSubmarine.Npos + i] = '^';
                        ocean = checkAdjacent(newSubmarine, ocean, i, newSubmarine.isVertical);
                        newSubmarine.points[i] = new int[]{newSubmarine.Mpos, newSubmarine.Npos + i};
                    }
                    battleships.add(newSubmarine);
                }
            } else {
                for (int i = 0; i < 1; i++) {
                    if (ocean[newSubmarine.Mpos + i][newSubmarine.Npos] == 'b' || ocean[newSubmarine.Mpos + i][newSubmarine.Npos] == '^') {
                        isCopied = true;
                        break;
                    }
                }
                if (!isCopied) {
                    for (int i = 0; i < 1; i++) {
                        ocean[newSubmarine.Mpos + i][newSubmarine.Npos] = '^';
                        ocean = checkAdjacent(newSubmarine, ocean, i, newSubmarine.isVertical);
                        newSubmarine.points[i] = new int[]{newSubmarine.Mpos + i, newSubmarine.Npos};
                    }
                    battleships.add(newSubmarine);
                }
            }
        }
        return ocean;
    }

    /**
     * Резервация смежных клеток для каждой клетки на корабле.
     *
     * @param battleships
     * @param ocean
     * @param i
     * @param isVertical
     * @return
     */
    public static char[][] checkAdjacent(Battleships battleships, char[][] ocean, int i, boolean isVertical) {
        // Такое количество Try-Catch обусловлено тем, что генерация кораблей абсолютно рандомна, он может появиться как в центре, так и сбоку.
        // Проверка на ТОЧНОЕ расположение корабля (чтоб понимать какие КОНКРЕТНО смежные клетки резервировать) была бы слишком массивна и затратна.
        // Поэтому я написал универсальный метод, который в случае, если какой-либо смежной клетки не сущетсвует,
        // просто ловит исключение и идет дальшеЮ
        if (!isVertical) {
            try {
                if (ocean[battleships.Mpos + 1][battleships.Npos + i] != '^')
                    ocean[battleships.Mpos + 1][battleships.Npos + i] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos - 1][battleships.Npos + i] != '^')
                    ocean[battleships.Mpos - 1][battleships.Npos + i] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos][battleships.Npos + 1 + i] != '^')
                    ocean[battleships.Mpos][battleships.Npos + 1 + i] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos][battleships.Npos - 1 + i] != '^')
                    ocean[battleships.Mpos][battleships.Npos - 1 + i] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos - 1][battleships.Npos + 1 + i] != '^')
                    ocean[battleships.Mpos - 1][battleships.Npos + 1 + i] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos + 1][battleships.Npos + 1 + i] != '^')
                    ocean[battleships.Mpos + 1][battleships.Npos + 1 + i] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos - 1][battleships.Npos + i - 1] != '^')
                    ocean[battleships.Mpos - 1][battleships.Npos + i - 1] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos + 1][battleships.Npos - 1 + i] != '^')
                    ocean[battleships.Mpos + 1][battleships.Npos - 1 + i] = 'b';
            } catch (Exception ex) {
            }
        } else {
            try {
                if (ocean[battleships.Mpos + 1 + i][battleships.Npos] != '^')
                    ocean[battleships.Mpos + 1 + i][battleships.Npos] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos - 1 + i][battleships.Npos] != '^')
                    ocean[battleships.Mpos - 1 + i][battleships.Npos] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos + i][battleships.Npos + 1] != '^')
                    ocean[battleships.Mpos + i][battleships.Npos + 1] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos + i][battleships.Npos - 1] != '^')
                    ocean[battleships.Mpos + i][battleships.Npos - 1] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos - 1 + i][battleships.Npos + 1] != '^')
                    ocean[battleships.Mpos - 1 + i][battleships.Npos + 1] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos + 1 + i][battleships.Npos + 1] != '^')
                    ocean[battleships.Mpos + 1 + i][battleships.Npos + 1] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos - 1 + i][battleships.Npos - 1] != '^')
                    ocean[battleships.Mpos - 1 + i][battleships.Npos - 1] = 'b';
            } catch (Exception ex) {
            }
            try {
                if (ocean[battleships.Mpos + 1 + i][battleships.Npos - 1] != '^')
                    ocean[battleships.Mpos + 1 + i][battleships.Npos - 1] = 'b';
            } catch (Exception ex) {
            }
        }
        return ocean;
    }
}
