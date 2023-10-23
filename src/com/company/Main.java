package com.company;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.company.Utility.*;


public class Main {

    public static void main(String[] args) throws IOException {
        getOodExcels();
        getMicroserviceExcels();
    }

    private static void getOodExcels() throws IOException {
        String root = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\";
        File file = new File(root);
        int num = 0;

        System.out.println("Creating OOD Excels:");
        for(File f : file.listFiles()) {
            String projectPath = f.getAbsolutePath();
            ArrayList<ClassInfo> classesInfo = getClassesAsClassInfo(projectPath);
            System.out.println("Creating excel " + ++num);
            writeClassInfoToExcel(classesInfo, num);
        }
    }

    private static void getMicroserviceExcels() throws IOException {
        String root = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\Micro Proj\\";
        File file = new File(root);
        int num = 0;

        System.out.println("Creating OOD Excels:");
        for(File f : file.listFiles()) {
            String projectPath = f.getAbsolutePath();
            ArrayList<MicroserviceInfo> microInfo = getClassesAsMicroserviceInfo(projectPath);
            System.out.println("Creating excel " + ++num);
            writeMicroInfoToExcel(microInfo, num);
        }
    }
}


