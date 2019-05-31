import dominio.Laberinto;

public class test {

    public static void main(String[] args) {

        int[][] matriz = {
                {-1, 1, 0, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 1, 7, 1, 0},
                {0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0}
        };

        Laberinto laberinto = new Laberinto( matriz );

        laberinto.resolver();
        laberinto.mostrarLaberinto();
        System.out.println(laberinto);

    }

}
