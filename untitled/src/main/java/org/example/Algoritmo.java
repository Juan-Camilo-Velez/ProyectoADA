package org.example;

import java.io.*;
import java.util.Scanner;


public class Algoritmo {
    public static void main(String[] args) throws IOException {

        BufferedReader EntradaUs = new BufferedReader(new
                InputStreamReader(System.in));
        String NomArch = "";

        try {
            System.out.println("Coloque el conjunto de datos en la carpeta raíz y escriba el nombre del archivo (por ejemplo: data8.txt):");
            NomArch = EntradaUs.readLine();
        } catch (FileNotFoundException e) {
            System.err.println("Error: El archivo D.txt no se encuentra en la ubicación especificada.");
            e.printStackTrace();
        }


        long HoraInicio = System.currentTimeMillis();

        Scanner LeerArch;
        LeerArch = new Scanner(new File(NomArch));


        int n = 0;
        do{
            LeerArch.nextInt();
            n++;
        }while(LeerArch.hasNext());
        n = (int) Math.sqrt(n);//saque la raíz cuadrada del número total de números enteros en el archivo para obtener el número de ciudades
        LeerArch.close();//hasNext() agotado, cierra el archivo



        //Crear matriz de distancias
        int[][] DistCiudades = new int[n][n];
        int i = 0;
        int j = 0;

        LeerArch = new Scanner(new File(NomArch));

        //Llenar matriz de distancias
        for(j=0;j<n;j++){
            for(i=0;i<n;i++){
                DistCiudades [i][j] = LeerArch.nextInt();
            }
        }
        LeerArch.close();//cerrar el archivo


        int ha = 0;
        //Máximo de partidos consecutivos en casa y fuera de casa
        while(ha > n-1 || ha <= 0){

            System.out.println("Ingrese el máximo consecutivo de juegos en casa/fuera:");
            ha = Integer.parseInt(EntradaUs.readLine());

            if(ha > n-1 || ha <= 0){
                System.out.println("Se ha introducido un valor no válido. El valor debe ser mayor que 0 y menor que (# de equipos - 1):");
            }
        }



        int [][]PesoEstrellas = new int [n][1];
        int PesoIndice = 0;


        //Obtenga pesos de estrellas para cada ciudad.
        for(j=0;j<n;j++){
            for(i=0;i<n;i++){
                PesoEstrellas[j][0] += DistCiudades[i][j];
            }
        }

        //Inicializar peso de estrella
        int PesoTiemp = 10000000;

        //Identificar el peso mínimo de las estrellas
        for(i=0;i<n;i++){
            if(PesoEstrellas[i][0]<PesoTiemp){
                PesoTiemp = PesoEstrellas[i][0];
                PesoIndice = i;
            }
        }

        System.out.println(PesoEstrellas[PesoIndice][0]+" es el peso mínimo de estrella que pertenece al índice " + PesoIndice);
        System.out.println();

        //Solucion PTorneo
        int[]CaminoEleg = new int [n+1];
        int distancia = 0;

        //Llenar la ruta inicial
        for(i=1;i<n;i++){
            if(i!=PesoIndice){
                CaminoEleg[i] = i;
            }
        }

        //Establecer el peso mínimo de la estrella como nodo inicial/final
        CaminoEleg [0] = PesoIndice;
        CaminoEleg [n] = PesoIndice;

        //calcular la distancia total
        for(i=0;i<n;i++){
            distancia += DistCiudades[CaminoEleg[i]][CaminoEleg[i+1]];
        }

        int TiempDistancia=0;
        int [] TiempCamino = new int[n+1];
        int Tiempte = 0;

        //Copiar ruta de inicio
        for(i=0;i<n+1;i++){
            TiempCamino[i] = CaminoEleg[i];
        }

        //iniciar intercambios
        int contar = 0;

        //Comenzar primero aceptar algoritmo
        while(contar!=100){

            for(j=1;j<n-1;j++){
                for(i=1;i<n-1;i++){
                    //Comience a intercambiar nodos, pero deje solo el nodo con peso mínimo de estrella
                    Tiempte=TiempCamino[j];
                    TiempCamino[j]=TiempCamino[i];
                    TiempCamino[i]=Tiempte;
                    TiempDistancia = 0;

                    //Calcular la distancia del nuevo camino propuesto.
                    for(int z=0;z<n;z++){
                        TiempDistancia += DistCiudades[TiempCamino[z]][TiempCamino[z+1]];
                    }
                    //Compruebe si la ruta propuesta es mejor y, de ser así, reemplácela
                    if(TiempDistancia<distancia){
                        for(int b=0;b<n+1;b++){
                            TiempCamino[b]=TiempCamino[b];

                        }
                        distancia = TiempDistancia;

                        System.out.println("Ruta PTorneo más corta encontrada hasta ahora: " + distancia);//a medida que se encuentran mejores caminos...

                    }

                    //revertir el cambio a Tiempte
                    else{
                        Tiempte=TiempCamino[i];
                        TiempCamino[i]=TiempCamino[j];
                        TiempCamino[j]=Tiempte;

                    }

                }

            }
            //Para el bucle while
            contar++;
        }



        System.out.println();
        System.out.println("La mejor ruta de PTorneo encontrada hasta ahora con restricción de peso de estrellas: " );

        //Imprimir la mejor ruta encontrada
        for(int a=0;a<n+1;a++){
            if(a!=n){
                System.out.print(CaminoEleg[a]+ "->");
            }
            else {
                System.out.print(CaminoEleg[a]);
            }}
        System.out.println();
        System.out.println("Con una distancia de:" + distancia);




        //El siguiente algoritmo requiere un formato específico de la solución TSP.
        int CaminoForm[] = new int [n];

        //Sólo el último nodo tiene el peso mínimo de estrella.
        for(i=0;i<CaminoForm.length;i++){
            CaminoForm[i]=CaminoEleg[i+1];
        }

        System.out.println();
        System.out.println("Reformateando la ruta para usarla en la parte 2... ");
        System.out.println();
        //Imprime la ruta reformateada
        for(int a=0;a<n;a++){
            if(a!=(n-1))
                System.out.print(CaminoForm[a]+ "->");
            else System.out.print(CaminoForm[a]);
        }
        System.out.println();
        System.out.println();

        //Crear matriz de programación TTP
        int [][]Cronograma = new int [n][2*n-2];

        //Pasar al siguiente algoritmo

        Cronograma = PTorneo.PTorneo(ha,CaminoForm,n);


        //Obtener distancias
        int []DistanEquipo= new int [n];
        int DistanTotal = 0;

        for(j=0;j<n;j++){
            for(i=0;i<(2*n-3);i++){

                if(i==0 && Cronograma[j][i]<0){
                    //Primer partido fuera de casa
                    //Nota: (programa[j][i])-1) debido a que los números de las ciudades se incrementan en +1 para evitar el problema con -0 = 0
                    DistanEquipo[j] += DistCiudades[j][Math.abs(Cronograma[j][i])-1];
                }

                //2x partidos en casa o (1 en casa y 1 fuera)
                if(Cronograma[j][i]>0){

                    //no ir a ninguna parte 2x a casa
                    if(Cronograma[j][i+1]>0){
                        DistanEquipo[j] += DistCiudades[j][j];
                    }
                    //1 en casa, 1 fuera
                    else{
                        DistanEquipo[j] += DistCiudades[j][Math.abs(Cronograma[j][i+1])-1];
                    }

                }
                //1lejos 1 en casa
                else if(Cronograma[j][i+1]>0){
                    DistanEquipo[j] += DistCiudades[Math.abs(Cronograma[j][i])-1][j];
                }

                //2 de distancia
                else{DistanEquipo[j] += DistCiudades[Math.abs(Cronograma[j][i])-1][Math.abs(Cronograma[j][i+1])-1];
                }

                //Si el último partido fue fuera de casa, regrese a casa.
                if(i==(2*n-4) && Cronograma [j][i+1]<0){
                    DistanEquipo[j] += DistCiudades[j][Math.abs(Cronograma[j][i+1])-1];
                }
            }




            //Imprime la distancia recorrida por cada equipo.
            System.out.println("Equipo "+(j+1)+ " tiene una distancia total recorrida de: " + DistanEquipo[j]);
            DistanTotal += DistanEquipo[j];
        }
        //Distancia total recorrida por todos los equipos.
        System.out.println();
        System.out.println("Distancia total de viaje del equipo: " + DistanTotal);
        System.out.println();
        //Tiempo total de cálculo
        long endTime = System.currentTimeMillis();
        System.out.println("Total time: " + (endTime-HoraInicio) + "ms");


    }


}




