package com.company;

import java.io.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.io.IOException;
import java.util.*;


public class Main {

    public static void main(String[] args) {

        String project1 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\4 - Netbeans v1.0.x";
        String project2 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\1 - QuickUML 2001";
        String project3 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\2 - Lexi v0.1.1 alpha";
        String project4 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\3 - JRefactory v2.6.24";
        String project5 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\5 - JUnit v3.7";
        String project6 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\8 - MapperXML v1.9.7";
        String project7 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\10 - Nutch v0.4";
        String project8 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\11 - PMD v1.8";
        String project9 = "C:\\Users\\MelikaZ\\Documents\\Term 8\\OO Programming\\project\\JHotDraw v5.1";

        ArrayList<File> classFiles = getClassesAsFiles(project1);
        writeToExcel(classFiles);

    }

    private static void writeToExcel(ArrayList<File> classFiles) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet spreadsheet = workbook.createSheet(" Classes Info ");
        XSSFRow row;
        Map<Integer, Object[]> classesInfo = new TreeMap<>();
        classesInfo.put(1,createLabels().toArray());

        for(int i=2; i<classFiles.size(); i++){
            try {
                ClassInfo clazz = new ClassInfo(classFiles.get(i));
                classesInfo.put(i, clazz.getClassInfo().toArray());
            }
            catch (Exception e){
            }
        }

        Set<Integer> keyId = classesInfo.keySet();
        int rowId = 0;

        for (Integer key : keyId) {
            row = spreadsheet.createRow(rowId++);
            Object[] objectArr = classesInfo.get(key);
            int cellId = 0;
            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellId++);
                if (obj instanceof Integer) {
                    cell.setCellValue((Integer) obj);
                } else {
                    cell.setCellValue(String.valueOf(obj));
                }
            }
        }
        try {
            FileOutputStream out = new FileOutputStream(new File("Project1.xlsx"));
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<File> getClassesAsFiles(String path){
        File file = new File(path);
        Stack<File> s = new Stack<>();
        ArrayList<File> files = new ArrayList<>();
        s.push(file);
        while (!s.empty()) {
            File tmpF = s.pop();
            if (tmpF.isFile() && tmpF.getName().endsWith(".java")) {
                files.add(tmpF);
            } else if (tmpF.isDirectory()) {
                File[] f = tmpF.listFiles();
                for (File fpp : f) {
                    s.push(fpp);
                }
            }
        }
        return files;
    }

    public static List<String> createLabels(){
        List<String> list = Arrays.asList("Project Name", "Package_Name","Class_Name", "Class_Type", "Class_Visibility",
                "Class_is_Abstract", "Class_is_Static", "Class_is_Final", "Class_Is_Interface", "Extends", "Implements",
                "Children", "Constructor", "Fields", "Methods", "Override", "has_static_method", "has_final_method",
                "Has_abstract_method", "Instantiations");
        return list;
    }

}



