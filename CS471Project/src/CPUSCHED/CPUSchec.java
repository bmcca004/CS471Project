package CPUSCHED;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Process implements  Comparable<Process>
{

    String arrivalTime;
    String priority;
    String cpuBurstUnits;

    public Process( String arrivalTime,  String burstTime, String priority)
    {

        this.arrivalTime = arrivalTime;
        this.cpuBurstUnits = burstTime;
        this.priority = priority;
    }




    @Override
    public int compareTo(Process process)
    {
        if (Integer.valueOf(cpuBurstUnits)==Integer.valueOf(process.cpuBurstUnits))
        return 0;
        else if (Integer.valueOf(cpuBurstUnits)>Integer.valueOf(process.cpuBurstUnits))
        {
         return 1;
        }
        return -1;
    }
}

 class CPUSchec {
    double totalTime, totalWaitTime, totalBurstTime, totalTurnAroundTime, totalResponseTime = 0;
    double totalProcesses = 500;
    double averageWaitingTime = totalWaitTime/totalProcesses;
    double averageTurnAroundTime = totalTurnAroundTime/totalProcesses;
     double averageResponseTime = totalResponseTime/totalProcesses;

     double throughput, cpuUtility = 0;
     ArrayList<Process> input;
     ArrayList<Process> fifoCopy;

     ArrayList<Process> sjfCopy;
     ArrayList<Process> priorityCopy;

     String file = "C:\\Users\\chaos\\Downloads\\Datafile1-txt.txt";
     public static ArrayList<Process> readFileintoArrayList(String file) throws IOException, FileNotFoundException {
         String filePath =  file;
         ArrayList<Process> reading = new ArrayList<>();
         try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath)))
         {
             String line;
             while ((line = bufferedReader.readLine()) != null)
             {
                 String [] attributes = line.split("\t");
                 String arrival = (attributes[0].trim());
                 String burst = (attributes[1].trim());
                 String priority = (attributes[2].trim());
                 reading.add(new Process(arrival,burst,priority));

             }
         }
         return reading;

     }





     public void FIFO(ArrayList<Process>input) throws FileNotFoundException {
         totalTime =0;
         totalWaitTime=0;
         totalBurstTime=0;
         totalTurnAroundTime=0;
         totalResponseTime = 0;
         ArrayList<Process> working = new ArrayList<>();
         fifoCopy = input;


         double waitTime = 0;
         int        p_count = 0;
         double burstTime = 0.0;

         fifoCopy.remove(0);
         while (fifoCopy.isEmpty() == false)
         {

             ++totalTime;

             if (totalTime == Integer.valueOf(fifoCopy.get(0).arrivalTime)||totalTime > Integer.valueOf(fifoCopy.get(0).arrivalTime))
             {
                 burstTime = Double.valueOf(fifoCopy.get(0).cpuBurstUnits);
                 totalBurstTime =  burstTime + totalBurstTime;
                 working.add(fifoCopy.get(0));
                 fifoCopy.remove(0);
             }
             while (working.isEmpty() == false)
             {

                 int arrivalTime =Integer.valueOf(working.get(0).arrivalTime);
                 double responseTime = totalTime - arrivalTime;
                 totalResponseTime = responseTime + totalResponseTime;
                 for(int i =0; i < burstTime; ++i)
                 {
                     ++totalTime;
                 }
                 double exitTime = burstTime + totalTime;
                 double turnaroundTime = exitTime - arrivalTime;
                 waitTime = turnaroundTime - burstTime;
                 totalTurnAroundTime = totalTurnAroundTime + turnaroundTime;
                 working.remove(0);
                 ++ p_count;
                 if (p_count == 500)
                 {
                     break;
                 }




             }
             if (p_count == 500)
             {
                 break;
             }


         }
         averageTurnAroundTime = totalTurnAroundTime/totalProcesses;
         throughput = totalBurstTime/totalProcesses;
         averageWaitingTime = waitTime/totalProcesses;
         cpuUtility = (totalBurstTime/totalTime)*100;
         averageResponseTime= totalResponseTime/totalProcesses;
         PrintStream out = new PrintStream(new File("FIFO.txt"));
         PrintStream console = System.out;
         System.setOut(out);
         System.out.println("-------:FIFO Stats:--------");
         System.out.print("Total Number of Process: ");
         System.out.println(p_count);
         System.out.print("Total time: ");
         System.out.println(totalTime);
         System.out.print("Throughput: ");
         System.out.println(throughput);
         System.out.print("CPU utilization: ");
         System.out.print(cpuUtility);
         System.out.println(" %");
         System.out.print("Average waiting time: ");
         System.out.println(averageWaitingTime);
         System.out.print("Average turnaround time: ");
         System.out.println(averageTurnAroundTime);
         System.out.print("Average response time: ");
         System.out.println(averageResponseTime);



     }
     public void SJF(ArrayList<Process>input) throws FileNotFoundException {
         totalTime =0;
         totalWaitTime=0;
         totalBurstTime=0;
         totalTurnAroundTime=0;
         totalResponseTime = 0;
         int   p_count = 0;
         double burstTime =0;
         ArrayList<Process> working = new ArrayList<>();
         sjfCopy = input;
         sjfCopy.remove(0);
         while (sjfCopy.isEmpty() == false)
         {


             ++totalTime;

             if (totalTime == Integer.valueOf(sjfCopy.get(0).arrivalTime))
             {
                 burstTime = Double.valueOf(sjfCopy.get(0).cpuBurstUnits);
                 totalBurstTime =  burstTime + totalBurstTime;
                 working.add(sjfCopy.get(0));
                 sjfCopy.remove(0);
             }
             else if (totalTime > Integer.valueOf(sjfCopy.get(0).arrivalTime))
             {
                 burstTime = Double.valueOf(sjfCopy.get(0).cpuBurstUnits);
                 totalBurstTime =  burstTime + totalBurstTime;
                 working.add(sjfCopy.get(0));
                 sjfCopy.remove(0);

             }
             while (working.isEmpty() == false)
             {

                 if(working.size() >0)
                 {Collections.sort(working);}

                 int arrivalTime =Integer.valueOf(working.get(0).arrivalTime);
                 double responseTime = totalTime - arrivalTime;
                 totalResponseTime = responseTime + totalResponseTime;

                 for(int i =0; i < burstTime; ++i)
                 {
                     ++totalTime;
                 }
                 if (totalTime > Integer.valueOf(sjfCopy.get(0).arrivalTime))
                 {
                     burstTime = Double.valueOf(sjfCopy.get(0).cpuBurstUnits);
                     totalBurstTime =  burstTime + totalBurstTime;
                     working.add(sjfCopy.get(0));
                     sjfCopy.remove(0);
                     if(working.size() >0)
                     {Collections.sort(working);}

                 }
                 double exitTime = burstTime + totalTime;
                 double turnaroundTime = exitTime - arrivalTime;
                 double waitingTime = turnaroundTime - burstTime;
                  totalWaitTime = waitingTime + totalWaitTime;
                 totalTurnAroundTime = totalTurnAroundTime + turnaroundTime;

                 working.remove(0);
                 Collections.sort(working);
                 ++ p_count;
                 if (p_count == 500)
                 {
                     break;
                 }




             }
             if (p_count == 500)
             {
                 break;
             }


         }

         averageTurnAroundTime = totalTurnAroundTime/totalProcesses;
         throughput = totalBurstTime/totalProcesses;
         averageWaitingTime = totalWaitTime/totalProcesses;
         cpuUtility = (totalBurstTime/totalTime)*100;
         averageResponseTime= totalResponseTime/totalProcesses;
         PrintStream out = new PrintStream(new File("SJF.txt"));

         System.setOut(out);
         System.out.println("-------:SJF Stats:-------- ");
         System.out.print("Total Number of Process: ");
         System.out.println(p_count);
         System.out.print("Total time: ");
         System.out.println(totalTime);
         System.out.print("Throughput: ");
         System.out.println(throughput);
         System.out.print("CPU utilization: ");
         System.out.print(cpuUtility);
         System.out.println(" %");
         System.out.print("Average waiting time: ");
         System.out.println(averageWaitingTime);
         System.out.print("Average turnaround time: ");
         System.out.println(averageTurnAroundTime);
         System.out.print("Average response time: ");
         System.out.println(averageResponseTime);


     }
     public void Priority(ArrayList<Process>input) throws FileNotFoundException {


         int p_count = 0;
         double burstTime = 0;
         double countUp= 0;
         totalTime =0;
         totalWaitTime=0;
         totalBurstTime=0;
         totalTurnAroundTime=0;
         totalResponseTime = 0;
         priorityCopy=input;
         priorityCopy.remove(0);
         ArrayList<Process> working = new ArrayList<>();
         ArrayList<Process> priorityChecker = new ArrayList<>();
         //Priority and CPUburst are swapped to use the overridden compareTo
         for(int i =0; i < priorityCopy.size(); i++)
         {
             String tempt1, tempt2;
             tempt1 = priorityCopy.get(i).priority;
             tempt2 = priorityCopy.get(i).cpuBurstUnits;
             priorityCopy.get(i).cpuBurstUnits=tempt1;
             priorityCopy.get(i).priority=tempt2;
         }
         while (priorityCopy.isEmpty()==false) {
             totalTime++;
             if (totalTime == Integer.valueOf(priorityCopy.get(0).arrivalTime) && working.isEmpty() ==true )
             {

                 working.add(priorityCopy.get(0));
                 priorityCopy.remove(0);

             }

             else if (totalTime > Integer.valueOf(priorityCopy.get(0).arrivalTime) && working.isEmpty() ==true )
             {
                 working.add(priorityCopy.get(0));
                 priorityCopy.remove(0);



             }
             else if (totalTime == Integer.valueOf(priorityCopy.get(0).arrivalTime) && working.isEmpty() ==false )
             {
                 working.add(priorityCopy.get(0));
                 Collections.sort(working);
                 priorityCopy.remove(0);
             }
             else if (totalTime > Integer.valueOf(priorityCopy.get(0).arrivalTime) && working.isEmpty() ==false )
             {
                 working.add(priorityCopy.get(0));
                 Collections.sort(working);
                 priorityCopy.remove(0);




             }
             while (working.isEmpty()==false)
             {
                 burstTime=Integer.valueOf(working.get(0).priority);
                 totalBurstTime=burstTime+totalBurstTime;
                 working.remove(0);
                 ++p_count;

             }





             if(p_count == 500)
             {break;}


         }
         averageTurnAroundTime = totalTurnAroundTime/totalProcesses;
         throughput = totalBurstTime/totalProcesses;
         averageWaitingTime = totalTime/totalProcesses;
         cpuUtility = (totalBurstTime/totalTime)*100;
         averageResponseTime= totalResponseTime/totalProcesses;
         PrintStream out = new PrintStream(new File("Priority.txt"));
         PrintStream console = System.out;
         System.setOut(out);
         System.out.println("-------:Priority Stats:--------");
         System.out.print("Total Number of Process: ");
         System.out.println(p_count);
         System.out.print("Total time: ");
         System.out.println(totalTime);
         System.out.print("Throughput: ");
         System.out.println(throughput);
         System.out.print("CPU utilization: ");
         System.out.print(cpuUtility);
         System.out.println(" %");
         System.out.print("Average waiting time: ");
         System.out.println(averageWaitingTime);
         System.out.print("Average turnaround time: ");
         System.out.println(averageTurnAroundTime);
         System.out.print("Average response time: ");
         System.out.println(averageResponseTime);

     }

     public static void main(String[] args) throws IOException {
         CPUSchec cpuSchec = new CPUSchec();
         Scanner sc = new Scanner(System.in);
         PrintStream console = System.out;
         System.setOut(console);
         System.out.print("Please type in the file path: ");

         String file = sc.nextLine();


         cpuSchec.FIFO(readFileintoArrayList(file));
         cpuSchec.SJF(readFileintoArrayList(file));
         cpuSchec.Priority(readFileintoArrayList(file));





     }





}

