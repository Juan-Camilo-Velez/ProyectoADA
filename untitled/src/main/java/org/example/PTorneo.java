package org.example;

import java.io.IOException;
/**
 * Clase que implementa el algoritmo PTorneo para resolver el problema específico.
 */
public class PTorneo {
    /**
     * Constructor predeterminado de la clase PTorneo.
     * Este constructor no realiza ninguna acción específica.
     */
    public PTorneo() {
        // Puedes agregar algún comentario aquí si es necesario.
    }

    /**
     * Método que implementa el algoritmo PTorneo para resolver el problema.
     *
     * @param ha    Descripción del parámetro ha.
     * @param min   Descripción del parámetro min.
     * @param equipos   Descripción del parámetro equipos.
     * @param n     Descripción del parámetro n.
     * @return      Una matriz con los resultados del algoritmo.
     * @throws IOException Descripción de cuándo podría ocurrir la excepción.
     */
    public static int[][] PTorneo(int ha, int min, int[] equipos, int n) throws IOException {
        int[] nodos = new int[n];
        nodos[0] = equipos[n - 1];

        int a;
        int b = 2;
        for (a = 1; a < n; a++) {
            if (a <= n / 2)
                nodos[2 * a - 1] = equipos[a - 1];
            else {
                nodos[b] = equipos[a - 1];
                b = b + 2;
            }
        }

        int[] vector = new int[n];
        int numero = -1;

        for (a = 0; a < ((int) n / ha / 2) * ha; a = a + ha) {
            for (b = 0; b < ha; b++)
                vector[a + b] = numero;
            numero = numero * -1;
        }

        double i = (Math.floor(n / (2 * ha))) * ha;
        for (a = (int) i; a < n / 2; a++)
            vector[a] = numero;

        for (a = 0; a < n / 2; a++)
            vector[a + n / 2] = -1 * vector[n / 2 - a - 1];

        int[][] calendario = new int[n][2 * n - 2];
        int r;
        int k;

        for (r = 0; r < n - 1; r++) {
            if (r != 0 && r % ha == 0 && r >= min) {
                vector[0] = -1 * vector[0];
                vector[n - 1] = -1 * vector[n - 1];
            }

            k = n - 1;
            for (a = 0; a < n; a++) {
                calendario[nodos[a]][r] = vector[a] * (1 + nodos[k]);
                k = k - 1;
            }

            int temp = nodos[1];
            for (a = 1; a < n - 1; a++)
                nodos[a] = nodos[a + 1];
            nodos[n - 1] = temp;
        }

        a = n - 2;
        for (r = n - 1; r < 2 * n - 2; r++) {
            for (b = 0; b < n; b++)
                calendario[b][r] = -1 * calendario[b][a - 1];
            a = a + 1;
            if (a == n)
                a = 1;
        }

        // Mostrar el cronograma resultante
        System.out.println("Horario del equipo:");
        for (a = 0; a < n; a++) {
            for (b = 0; b < 2 * n - 2; b++) {
                System.out.print(calendario[a][b] + "\t");
            }
            System.out.println();
        }

        return calendario;
    }
}