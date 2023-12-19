package org.example;

import java.io.IOException;

/**
 * Clase que implementa un algoritmo específico relacionado con torneos.
 */
public class PTorneo {
    /**
     * Constructor predeterminado. No se requieren parámetros.
     */
    public PTorneo() {
        // No se requiere ninguna lógica específica en el constructor predeterminado.
    }

    /**
     * Realiza operaciones específicas del torneo para generar un horario TTP.
     *
     * @param ha      Máximo consecutivo de juegos en casa/fuera.
     * @param equipos Arreglo de equipos.
     * @param n       Número de equipos.
     * @return Matriz de horario TTP.
     * @throws IOException Si hay un error de entrada/salida.
     */
    public static int[][] PTorneo(int ha,int min, int[] equipos, int n) throws IOException {
        //nodos = el equipo que está en ese nodo del gráfico donde está nodos(1)
        //siempre el equipo n que tiene la longitud de estrella más pequeña
        int[] nodos = new int[n];
        nodos[0] = equipos[n - 1];

        //Orden coincidente de nodos con equipos
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

        //Configurar un vector de hogar lejos (valores +1 o -1)
        int[] vector = new int[n];
        int numero = -1; //número=-1 -> lejos; numero=1 -> casa

        //primera mitad del vector de local-fuera (primera ronda)
        for (a = 0; a < ((int) n / ha / 2) * ha ; a = a + ha) {
            for (b = 0; b < ha; b++)
                vector[a + b] = numero;
            numero = numero * -1;
        }

        double i = (Math.floor(n / (2 * ha))) * ha;
        for (a = (int) i; a < n / 2; a++)
            vector[a] = numero;

        //segunda mitad = espejo negativo de la primera mitad (primera ronda)
        for (a = 0; a < n / 2; a++)
            vector[a + n / 2] = -1 * vector[n / 2 - a - 1];

        //configurando una matriz para el horario
        int[][] calendario = new int[n][2 * n - 2];
        int r;
        int k;

        //rondas en la primera mitad de los torneos
        for (r = 0; r < n - 1; r++) {
            //para rondas>1, cambie el valor local/visitante en el primer nodo (si ha habido
            //un número máximo de partidos consecutivos en casa o fuera de casa
            if (r != 0 && r % ha == 0 && r >= min) {
                vector[0] = -1 * vector[0];
                vector[n - 1] = -1 * vector[n - 1];
            }

            //calendario para la ronda r
            k = n - 1;
            for (a = 0; a < n; a++) {
                calendario[nodos[a]][r] = vector[a] * (1 + nodos[k]);
                k = k - 1;
            }

            //Mueva todos menos el primer nodo en el sentido contrario a las agujas del reloj una posición
            int temp = nodos[1];
            for (a = 1; a < n - 1; a++)
                nodos[a] = nodos[a + 1];
            nodos[n - 1] = temp;
        }

        //Segunda mitad del torneo (opuesta a la ronda n-2, n-1, 1, 2, ..., n-3)
        a = n - 2;
        for (r = n - 1; r < 2 * n - 2; r++) {
            for (b = 0; b < n; b++) //b=equipo
                calendario[b][r] = -1 * calendario[b][a - 1];
            a = a + 1;
            if (a == n)
                a = 1;
        }

        //imprimir/mostrar el cronograma resultante
        System.out.println("horario del equipo:");
        for (a = 0; a < n; a++) {
            for (b = 0; b < 2 * n - 2; b++) {
                System.out.print(calendario[a][b]);
                System.out.print("\t");
            }
            System.out.print("\n");
        }
        System.out.println();
        return calendario;
    }
}
