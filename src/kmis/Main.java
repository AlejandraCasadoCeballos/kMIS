package kmis;

import kmis.algorithm.AlgConstructive;
import kmis.constructive.GRASPGRConstructive;
import kmis.constructive.GRASPRGConstructive;
import kmis.constructive.IConstructive;
import kmis.constructive.RandomConstructive;
import kmis.localSearch.ILocalSearch;
import kmis.localSearch.LocalSearch;
import kmis.localSearch.LocalSearchEfficient;
import kmis.structure.Instance;
import kmis.structure.RandomManager;
import kmis.structure.Result;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {

    final static String pathFolder = "./test";
    static ArrayList<Instance> instances;

    final static boolean readAllFolders = false;
    final static boolean readAllInstances = true;
    final static boolean readFromInput = false;

    final static String folderIndex = "preliminar";
    final static String instanceIndex = "classe_6_32_40.txt";

    static List<String> foldersNames;
    static List<String> instancesNames;
    static String instanceFolderPath;

    static final int numSolutions=1000;

    final public static boolean DEBUG = true;
    static IConstructive randomConstructive = new RandomConstructive();
    static IConstructive graspGRConstructive =new GRASPGRConstructive();
    static  IConstructive graspRGConstructive =new GRASPRGConstructive();

    static ILocalSearch localSearch=new LocalSearch();
    static ILocalSearch localSearchEfficient=new LocalSearchEfficient();

    static float [] alphas=new float[]{/*0.25f,*/0.5f/*,0.75f,1f*/}; //alpha=1->random
    static public float alpha;

    final static int seed=13;

    public static void main(String[] args) {
        readData();
        AlgConstructive algConstructive=new AlgConstructive(numSolutions, graspRGConstructive,localSearchEfficient);

        for (float a : alphas) {
            alpha=a;
            List<Result> results = new ArrayList<>();
            for (Instance instance:instances) {
                RandomManager.setSeed(seed);
                Result result=algConstructive.execute(instance);
                results.add(result);
            }
            printResults("./results/"+algConstructive.toString()+".csv", results);
        }


    }

    private static void printResults(String path, List<Result> results) {
        try (PrintWriter pw = new PrintWriter(path)) {
            List<String> headers = new ArrayList<>(results.get(0).getKeys());
            pw.print("Instance");
            for (String header : headers) {
                pw.print(","+header);
            }
            pw.println();
            for (Result result : results) {
                pw.print(result.getInstanceName());
                for (String header : headers) {
                    pw.print(","+result.get(header));
                }
                pw.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readData(){
        instances = new ArrayList<>();
        foldersNames = Arrays.asList(new File(pathFolder).list());

        if(readFromInput){
            readInstanceFromInput();
        } else {
            if(readAllFolders) readAllFolders();
            else if (foldersNames.contains(folderIndex)) readFolder(folderIndex);
            else System.out.println("Folder index exceeds the bounds of the array");
        }
    }

    private static void readInstanceFromInput(){
        Scanner sc = new Scanner(System.in);
        String line;
        String[] lineContent;
        int elementL;
        int elementR;
        int numElementsL;
        int numElementsR;
        int numEdges;
        int numElementsSol;
        BitSet []connections;
        line = sc.nextLine();
        lineContent = line.split(" ");

        numElementsL = Integer.parseInt(lineContent[0]);
        numElementsR = Integer.parseInt(lineContent[1]);
        numEdges=Integer.parseInt(lineContent[2]);
        numElementsSol=Integer.parseInt(lineContent[3]);
        connections = new BitSet[numEdges];

        for (int i=0; i< connections.length;i++){
            connections[i]=new BitSet(numElementsR);
            line = sc.nextLine();
            lineContent = line.split(" ");
            elementL = (Integer.parseInt(lineContent[0]))-1;
            elementR = (Integer.parseInt(lineContent[1]))-1;
            connections[elementL].set(elementR);
        }
        instances.add(new Instance(connections, numElementsSol, numEdges, numElementsR, numElementsL));
    }

    private static void readAllFolders(){
        instances = new ArrayList<>();
        String [] folders =new File(pathFolder).list();

        for(String fileName : folders){
            readFolder(fileName);
        }
    }

    private static void readFolder(String fileName){
        File file;
        file=new File(pathFolder+"/"+fileName);
        if(!fileName.startsWith(".") && !fileName.startsWith("..") && file.isDirectory()){
            instancesNames = Arrays.asList(file.list());
            instanceFolderPath = file.getPath() + "/";
            if(readAllInstances) readAllInstances();
            else if (instancesNames.contains(instanceIndex)) readInstance(instanceIndex);
            else System.out.println("Instance index exceeds the bounds of the array");
        }
    }

    private static void readAllInstances(){
        for(String instanceName : instancesNames){
            readInstance(instanceName);
        }
    }

    private static void readInstance(String instanceName){
        instances.add(new Instance(instanceFolderPath +instanceName));
    }
}
