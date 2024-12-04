package com.example.backend_integrador.service.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {

    public byte[] generateDailyOrdersReport(List<Map<String, Object>> analysis) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("PEDIDOS DIARIOS");

            // Crear el título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Reporte Diario de Pedidos");
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2)); // Fusionar celdas
            titleCell.setCellStyle(titleStyle);

            // Encabezados
            Row headerRow = sheet.createRow(1);
            String[] headers = { "Fecha", "Total de Órdenes", "Día de la Semana" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);

                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                headerFont.setColor(IndexedColors.WHITE.getIndex());
                headerStyle.setFont(headerFont);
                headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowIndex = 2; // Empezar después de título y encabezado
            for (Map<String, Object> record : analysis) {
                Row row = sheet.createRow(rowIndex++);
                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setAlignment(HorizontalAlignment.CENTER);

                // Formato de fecha
                CellStyle dateStyle = workbook.createCellStyle();
                CreationHelper createHelper = workbook.getCreationHelper();
                dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                dateStyle.setAlignment(HorizontalAlignment.CENTER);

                // Alternar colores en las filas
                if (rowIndex % 2 == 0) {
                    dataStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
                    dataStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                }

                // Llenar datos
                Cell dateCell = row.createCell(0);
                dateCell.setCellValue(record.get("fecha").toString());
                dateCell.setCellStyle(dateStyle);

                Cell ordersCell = row.createCell(1);
                ordersCell.setCellValue(Integer.parseInt(record.get("total_ordenes").toString()));
                ordersCell.setCellStyle(dataStyle);

                Cell dayOfWeekCell = row.createCell(2);
                dayOfWeekCell.setCellValue(record.get("dia_semana").toString());
                dayOfWeekCell.setCellStyle(dataStyle);
            }

            // Ajustar automáticamente las columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir a byte[]
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    public byte[] generateDetailedOrdersReport(List<Map<String, Object>> detailedOrders) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("PEDIDOS CON DETALLES");

            // Crear el título
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Reporte Detallado de Pedidos");
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7)); // Fusionar celdas para el título
            titleCell.setCellStyle(titleStyle);

            // Encabezados
            Row headerRow = sheet.createRow(1);
            String[] headers = { "Fecha", "Order ID", "Estado", "Box ID", "Table Cronos ID", "Producto", "Cantidad",
                    "Precio" };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);

                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                headerFont.setColor(IndexedColors.WHITE.getIndex());
                headerStyle.setFont(headerFont);
                headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int rowIndex = 2;
            for (Map<String, Object> record : detailedOrders) {
                Row row = sheet.createRow(rowIndex++);

                // Extraer y manejar datos nulos o vacíos
                String fecha = record.get("fecha") != null ? record.get("fecha").toString() : "N/A";
                String ordersId = record.get("ordersId") != null ? record.get("ordersId").toString() : "N/A";
                String estado = record.get("estado") != null ? record.get("estado").toString() : "N/A";
                String boxId = record.get("boxId") != null ? record.get("boxId").toString() : "N/A";
                String tableCronosId = record.get("tableCronosId") != null ? record.get("tableCronosId").toString()
                        : "N/A";
                String productName = record.get("productName") != null ? record.get("productName").toString() : "N/A";
                int quantity = record.get("quantity") != null ? Integer.parseInt(record.get("quantity").toString()) : 0;
                double price = record.get("price") != null ? Double.parseDouble(record.get("price").toString()) : 0.0;

                // Llenar las celdas
                row.createCell(0).setCellValue(fecha);
                row.createCell(1).setCellValue(ordersId);
                row.createCell(2).setCellValue(estado);
                row.createCell(3).setCellValue(boxId);
                row.createCell(4).setCellValue(tableCronosId);
                row.createCell(5).setCellValue(productName);
                row.createCell(6).setCellValue(quantity);
                row.createCell(7).setCellValue(price);
            }

            // Ajustar columnas automáticamente
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir a byte[]
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

}
