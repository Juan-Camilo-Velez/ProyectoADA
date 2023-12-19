package org.example;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Scanner;

public class Algortimo {
    public static void main(String[] args) throws IOException {

        // Crear un selector de archivos
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccione el archivo D.txt");

        // Filtrar para mostrar solo archivos .txt
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos de texto (*.txt)", "txt");
        fileChooser.setFileFilter(filter);

        // Mostrar el selector de archivos
        int result = fileChooser.showOpenDialog(null);

        // Verificar si se seleccionó un archivo
        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("Operación cancelada por el usuario.");
            return;
        }

        // Obtener el archivo seleccionado
        File selectedFile = fileChooser.getSelectedFile();
        String fileName = selectedFile.getAbsolutePath();

        long startTime = System.currentTimeMillis();

        Scanner readFile;
        try {
            readFile = new Scanner(selectedFile);
        } catch (FileNotFoundException e) {
            System.err.println("Error: El archivo no se encuentra en la ubicación especificada.");
            e.printStackTrace();
            return;
        }

        int n = 0;
        do {
            readFile.nextInt();
            n++;
        } while (readFile.hasNext());
        n = (int) Math.sqrt(n);

        readFile.close();



        //Create distance matrix
        int[][] cityDistances = new int[n][n];
        int i = 0;
        int j = 0;

        readFile = new Scanner(new File(fileName));

        //Populate distance matrix
        for(j=0;j<n;j++){
            for(i=0;i<n;i++){
                cityDistances [i][j] = readFile.nextInt();
            }
        }
        readFile.close();//close file


        int ha = 0;
        // Max consecutive home and away games
        while (ha > n - 1 || ha <= 0) {
            try {
                System.out.println("Ingrese el máximo consecutivo de juegos en casa/fuera:");
                ha = Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine());

                if (ha > n - 1 || ha <= 0) {
                    System.out.println("Se ha introducido un valor no válido. El valor debe ser mayor que 0 y menor que (# de equipos - 1):");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




        int [][]starWeight = new int [n][1];
        int indexWeight = 0;


        //Obtain star weights for each city
        for(j=0;j<n;j++){
            for(i=0;i<n;i++){
                starWeight[j][0] += cityDistances[i][j];
            }
        }

        //Initialize star weight
        int tempWeight = 10000000;

        //Identify minimum star weight
        for(i=0;i<n;i++){
            if(starWeight[i][0]<tempWeight){
                tempWeight = starWeight[i][0];
                indexWeight = i;
            }
        }

        System.out.println(starWeight[indexWeight][0]+" is the minimum star weight which belongs to index " + indexWeight);
        System.out.println();

        //TSP Solver
        int[]chosenPath = new int [n+1];
        int distance = 0;

        //Populate initial path
        for(i=1;i<n;i++){
            if(i!=indexWeight){
                chosenPath[i] = i;
            }
        }

        //Set minimum star weight as beginning/end node
        chosenPath [0] = indexWeight;
        chosenPath [n] = indexWeight;

        //calculate total distance
        for(i=0;i<n;i++){
            distance += cityDistances[chosenPath[i]][chosenPath[i+1]];
        }

        int tempDistance=0;
        int [] tempPath = new int[n+1];
        int tempHold = 0;

        //Copy starting path
        for(i=0;i<n+1;i++){
            tempPath[i] = chosenPath[i];
        }

        //start swaps
        int count = 0;

        //Begin first accept algorithm
        while(count!=100){

            for(j=1;j<n-1;j++){
                for(i=1;i<n-1;i++){
                    //Begin swapping nodes, but leave minimum star weight node alone
                    tempHold=tempPath[j];
                    tempPath[j]=tempPath[i];
                    tempPath[i]=tempHold;
                    tempDistance = 0;

                    //Calculate distance of new proposed path
                    for(int z=0;z<n;z++){
                        tempDistance += cityDistances[tempPath[z]][tempPath[z+1]];
                    }
                    //Check if the proposed path is better, and if so replace
                    if(tempDistance<distance){
                        for(int b=0;b<n+1;b++){
                            chosenPath[b]=tempPath[b];

                        }
                        distance = tempDistance;

                        System.out.println("Shortest TSP path found so far: " + distance);//as better paths are found...

                    }

                    //revert change to tempPath
                    else{
                        tempHold=tempPath[i];
                        tempPath[i]=tempPath[j];
                        tempPath[j]=tempHold;

                    }

                }

            }
            //For the while loop
            count++;
        }



        System.out.println();
        System.out.println("Best TSP path found so far with star weight constraint: " );

        //Print best path found
        for(int a=0;a<n+1;a++){
            if(a!=n){
                System.out.print(chosenPath[a]+ "->");
            }
            else {
                System.out.print(chosenPath[a]);
            }}
        System.out.println();
        System.out.println("With a distance of: " + distance);




        //The next algorithm requires a specific format of the TSP solution
        int pathFormatted[] = new int [n];

        //Only the last node is the minimum star weight
        for(i=0;i<pathFormatted.length;i++){
            pathFormatted[i]=chosenPath[i+1];
        }

        System.out.println();
        System.out.println("Reformatting path for use in part 2... ");
        System.out.println();
        //Print out the reformatted path
        for(int a=0;a<n;a++){
            if(a!=(n-1))
                System.out.print(pathFormatted[a]+ "->");
            else System.out.print(pathFormatted[a]);
        }
        System.out.println();
        System.out.println();

        //Create TTP schedule matrix
        int [][]schedule = new int [n][2*n-2];

        //Pass onto the next algorithm

        schedule = PTorneo.PTorneo(ha,pathFormatted,n);

        //Get distances
        int []teamDistance= new int [n];
        int totalDistance = 0;

        for(j=0;j<n;j++){
            for(i=0;i<(2*n-3);i++){

                if(i==0 && schedule[j][i]<0){
                    //First game away
                    //Note: (schedule[j][i])-1) due to the city numbers being increased by +1 to avoid the issue with -0 = 0
                    teamDistance[j] += cityDistances[j][Math.abs(schedule[j][i])-1];
                }

                //2x home games or (1home 1away)
                if(schedule[j][i]>0){

                    //not going anywhere 2x home
                    if(schedule[j][i+1]>0){
                        teamDistance[j] += cityDistances[j][j];
                    }
                    //1 home, 1 away
                    else{
                        teamDistance[j] += cityDistances[j][Math.abs(schedule[j][i+1])-1];
                    }

                }
                //1away 1home
                else if(schedule[j][i+1]>0){
                    teamDistance[j] += cityDistances[Math.abs(schedule[j][i])-1][j];
                }

                //2 away
                else{teamDistance[j] += cityDistances[Math.abs(schedule[j][i])-1][Math.abs(schedule[j][i+1])-1];
                }

                //If last game away return home
                if(i==(2*n-4) && schedule [j][i+1]<0){
                    teamDistance[j] += cityDistances[j][Math.abs(schedule[j][i+1])-1];
                }
            }




            //Print out each team's travel distance
            System.out.println("Team "+(j+1)+ " has a total travel distance of: " + teamDistance[j]);
            totalDistance += teamDistance[j];
        }
        //Total distance traveled by all teams
        System.out.println();
        System.out.println("Total Team Travel Distance: " + totalDistance);
        System.out.println();
        //Total computation time
        long endTime = System.currentTimeMillis();
        System.out.println("Total time: " + (endTime-startTime) + "ms");


    }


}




