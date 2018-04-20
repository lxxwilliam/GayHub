package com.calabar.commons.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExportWord {
    String[] titles;
    List<Object[]> datas;

    public ExportWord(String[] titles, List<Object[]> datas) {
        this.titles = titles;
        this.datas = datas;
    }


    public void export(OutputStream outputStream)  {
        int cols = titles.length;
        int rows = datas.size() + 1;


        XWPFDocument xwp = new XWPFDocument();

        XWPFTable tab = xwp.createTable(rows, cols);
        tab.setCellMargins(50, 0, 50, 3000);
        for(int i=0;i<titles.length;i++){
            tab.getRow(0).getCell(i).setText(titles[i]);
        }
        for (int j = 0; j <rows-1 ; j++) {
            for (int i = 0; i <cols ; i++) {
                if(datas.get(j)!=null&&datas.get(j)[i]!=null)
                tab.getRow(j+1).getCell(i).setText(datas.get(j)[i].toString());
            }
        }
        System.out.println(xwp.getTables().size());
        xwp.insertTable(0, tab);
        try {
            xwp.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
