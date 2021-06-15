/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package courses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.lang.Math;
import java.util.Objects;
import java.util.Random;

/**
 *
 * @author Ma 7m Ou D
 */
public class Courses {

    public int ReadCellData(int vRow, int vColumn) {
        int value = 0;
        Workbook wb = null;           //initialize Workbook null  
        try {
//reading data from a file in the form of bytes  
            FileInputStream fis = new FileInputStream("Course Evaluation.xlsx");
//constructs an XSSFWorkbook object, by buffering the whole stream into the memory  
            wb = new XSSFWorkbook(fis);
        } catch (FileNotFoundException e) {
        } catch (IOException e1) {
        }
        Sheet sheet;   //getting the XSSFSheet object at given index  
        sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(vRow); //returns the logical row  
        Cell cell = row.getCell(vColumn);
        if (cell == null) {
            return 20000;
        }
        switch (cell.getCellType()) {

            case Cell.CELL_TYPE_NUMERIC:    //field that represents number cell type  
                double temp = cell.getNumericCellValue();
                int f = (int) temp;

                value = f;
                break;
            default:
        }

        return value;
        //returns the cell value  
    }

    public static int smallest(double[] arr) {
        int index = 0;
        double min = arr[index];

        for (int i = 1; i < arr.length; i++) {

            if (arr[i] < min) {
                min = arr[i];
                index = i;
            }

        }
        return index;
    }

    public static item calcMain(item temp, ArrayList<item> arr) {

        if (arr.isEmpty()) {
            temp = null;
        } else {
            for (int j = 0; j < arr.get(0).pos.size(); j++) {
                double sum = 0;
                for (int i = 0; i < arr.size(); i++) {
                    sum += arr.get(i).pos.get(j);
                    //  System.out.println("i = " + i + "  j = " + j);
                }

                // System.out.println("first = "+i+"  j = "+j);
                sum = sum / arr.size();
                // System.out.println("secound = "+i+"  j = "+j);
                temp.pos.add(sum);
                // System.out.println("third = "+i+"  j = "+j);
            }
        }
        return temp;
    }

    public static double distance(item m, item n) {
        double value = 0;
        if (null == m.pos || null == n.pos) {
            return 0;
        }
        for (int i = 0; i < m.pos.size(); i++) {
            value += Math.pow((m.pos.get(i) - n.pos.get(i)), 2);
        }
        value = Math.sqrt(value);
        return value;
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        Courses rc = new Courses();
        ArrayList<item> items;
        Random random = new Random();
        items = new ArrayList<item>();
        for (int i = 1; i < 151; i++) {
            item row = new item();
            row.id = rc.ReadCellData(i, 0);
            int temp = rc.ReadCellData(i, 0);
            if (temp == 20000) {
                break;
            }
            for (int j = 1; j <= 20; j++) {
                double cell = rc.ReadCellData(i, j);

                row.pos.add(cell);
            }
            items.add(row);
            System.out.println(i);
        }

        System.out.print("Enter k : ");
        Scanner input = new Scanner(System.in);
        int k = input.nextInt();
        double[] div = new double[k];
        ArrayList<cluster> clusters;
        clusters = new ArrayList<cluster>();
        ArrayList<item> mainTemp = new ArrayList<item>();
        for (int i = 0; i < k; i++) {
            cluster c = new cluster();
            item t = new item();
            c.id = i + 1;
            t = items.get(random.nextInt(items.size() - 10));
            for (int d = 0; d < items.get(0).pos.size(); d++) {
                c.main.pos.add(t.pos.get(d));
            }
            mainTemp.add(t);
            clusters.add(c);
        }
        int u = 0;
        boolean a = true;
        while (a) {
            a = false;
            for (int i = 0; i < k; i++) {   //empty clusters items to refill
                clusters.get(i).node.clear();
            }
            for (int i = 0; i < items.size(); i++) {
                for (int j = 0; j < k; j++) {
                    if (clusters.get(j).main != null) {
                        div[j] = distance(clusters.get(j).main, items.get(i));
                    }

                }

                int pos = smallest(div);
                clusters.get(pos).node.add(items.get(i));
            }

            for (int i = 0; i < k; i++) {
                item newMain;
                newMain = new item();

                newMain = calcMain(newMain, clusters.get(i).node);

                clusters.get(i).main = newMain;

            }

            for (int i = 0; i < k; i++) {
                for (int j = 0; j < items.get(i).pos.size(); j++) {

                    if (clusters.get(i).main != null) {
                        if (!Objects.equals(clusters.get(i).main.pos.get(j), mainTemp.get(i).pos.get(j))) {
                            mainTemp.set(i, clusters.get(i).main);
                            a = true;
                        }
                    } else {
                        break;
                    }
                    if (a) {
                        break;
                    }
                }
                if (a) {
                    break;
                }
            }

        }
        cluster out = new cluster();
        out.id = 1000;
        clusters.add(out);
        item t = new item();
        for (int i = 0; i < k; i++) {

            if (!clusters.get(i).node.isEmpty()) {
                for (int j = 0; j < clusters.get(i).node.size(); j++) {
                    for (int s = 0; s < clusters.get(i).main.pos.size(); s++) {
                        double diff = clusters.get(i).node.get(j).pos.get(s) - clusters.get(i).main.pos.get(s);
                        if (diff > 2 || diff < -2) {
                            out.node.add(clusters.get(i).node.get(j));
                            clusters.get(i).node.remove(clusters.get(i).node.get(j));
                            break;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < k + 1; i++) {
            if (clusters.get(i).id == 1000) {
                System.out.println("*******outliers data*******");
            }
            System.out.println("*******cluster " + clusters.get(i).id + " data*******");
            if (!clusters.get(i).node.isEmpty()) {
                System.out.println(clusters.get(i).node.size());
                for (int j = 0; j < clusters.get(i).node.size(); j++) {
                    System.out.println(clusters.get(i).node.get(j).id);
                }
            } else {
                System.out.println("Empty cluster");
            }
        }

    }

}
