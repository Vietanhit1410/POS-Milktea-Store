package org.example.cnjava_milkteastore.frontend.ui.management;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.cnjava_milkteastore.backend.invoice.dto.InvoiceDTO;
import org.example.cnjava_milkteastore.frontend.getapi.GetAPI;
import org.example.cnjava_milkteastore.frontend.theme.Theme;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class ReportManagement extends JPanel {

    private JTabbedPane tabbedPane;
    private JComboBox<Integer> yearComboBox;
    private JTable monthlyRevenueTable;
    private DefaultTableModel monthlyRevenueTableModel;
    private ChartPanel quarterlyGrowthChartPanel;
    private GetAPI<InvoiceDTO> invoiceAPI = new GetAPI<>(new TypeReference<>() {}, InvoiceDTO.class, new TypeReference<>() {});
    private List<InvoiceDTO> allInvoices;

    public ReportManagement() {
        setLayout(new BorderLayout());
        setBackground(Theme.BG_PINK);

        add(createTitlePanel(), BorderLayout.NORTH);
        add(createTabbedPane(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        // Khởi tạo dữ liệu
        loadData();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Theme.BG_PINK);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("📊 Báo Cáo", SwingConstants.LEFT);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setForeground(Theme.DARK_PINK);

        JLabel tabLabel = new JLabel("📈 Tab Báo Cáo", SwingConstants.RIGHT);
        tabLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tabLabel.setForeground(Color.DARK_GRAY);

        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(tabLabel, BorderLayout.EAST);
        return titlePanel;
    }

    private JTabbedPane createTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 14));
        tabbedPane.setBackground(Theme.LIGHTER_PINK);
        tabbedPane.setForeground(Theme.DARK_PINK);

        // Tab Doanh Thu Theo Tháng
        JPanel monthlyRevenuePanel = new JPanel(new BorderLayout());
        monthlyRevenuePanel.setBackground(Theme.LIGHTER_PINK);
        monthlyRevenueTable = createMonthlyRevenueTable();
        JScrollPane monthlyScrollPane = new JScrollPane(monthlyRevenueTable);
        monthlyScrollPane.setBackground(Theme.BG_PINK);
        monthlyScrollPane.getViewport().setBackground(Theme.LIGHTER_PINK);
        monthlyScrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK, 2), "Doanh Thu Theo Tháng",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16), Theme.DARK_PINK));
        monthlyRevenuePanel.add(monthlyScrollPane, BorderLayout.CENTER);
        tabbedPane.addTab("Doanh Thu Tháng", monthlyRevenuePanel);

        // Tab Tăng Trưởng Theo Quý
        JPanel quarterlyGrowthPanel = new JPanel(new BorderLayout());
        quarterlyGrowthPanel.setBackground(Theme.LIGHTER_PINK);
        quarterlyGrowthChartPanel = createQuarterlyGrowthChart();
        quarterlyGrowthPanel.add(quarterlyGrowthChartPanel, BorderLayout.CENTER);
        quarterlyGrowthPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Theme.DARK_PINK, 2), "Tăng Trưởng Theo Quý",
                TitledBorder.CENTER, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16), Theme.DARK_PINK));
        tabbedPane.addTab("Doanh Thu Quý", quarterlyGrowthPanel);

        return tabbedPane;
    }

    private JTable createMonthlyRevenueTable() {
        String[] columns = {"Tháng", "Doanh Thu"};
        monthlyRevenueTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(monthlyRevenueTableModel);
        table.setFont(Theme.FONT_TEXT);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.setBackground(Theme.LIGHTER_PINK);
        table.setForeground(Color.BLACK);
        table.setGridColor(Theme.DARK_PINK);
        table.setSelectionBackground(Theme.DARK_PINK.brighter());
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        // Điều chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);

        // Căn phải cột doanh thu
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
        table.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);

        return table;
    }

    private ChartPanel createQuarterlyGrowthChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart lineChart = ChartFactory.createLineChart(
                "Tăng Trưởng Doanh Thu Theo Quý",
                "Quý",
                "Doanh Thu (VNĐ)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Đặt màu nền xanh nhạt (#ADD8E6)
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(173, 216, 230)); // #ADD8E6
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setBackground(new Color(173, 216, 230)); // #ADD8E6
        chartPanel.setPreferredSize(new Dimension(400, 300));
        return chartPanel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(Theme.BG_PINK);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Chọn năm
        JLabel yearLabel = new JLabel("Chọn Năm:");
        yearLabel.setFont(Theme.FONT_TEXT);
        yearComboBox = new JComboBox<>();
        yearComboBox.setFont(Theme.FONT_TEXT);

        // Điền danh sách năm (2000 đến năm hiện tại)
        int currentYear = LocalDate.now().getYear();
        for (int year = 2000; year <= currentYear; year++) {
            yearComboBox.addItem(year);
        }
        yearComboBox.setSelectedItem(currentYear);

        JButton btnRefresh = new JButton("🔃 Làm Mới");
        Theme.configureButton(btnRefresh);
        btnRefresh.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnRefresh.addActionListener(e -> refreshReports());

        JButton btnExportExcel = new JButton("📄 Xuất Excel");
        Theme.configureButton(btnExportExcel);
        btnExportExcel.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnExportExcel.addActionListener(e -> {
            try {
                exportToExcel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        panel.add(yearLabel);
        panel.add(yearComboBox);
        panel.add(btnRefresh);
        panel.add(btnExportExcel);

        return panel;
    }

    private void loadData() {
        invoiceAPI.fetchFromApi(invoices -> {
            allInvoices = invoices;
            refreshReports();
        });
    }

    private void refreshReports() {
        updateMonthlyRevenueTable();
        updateQuarterlyGrowthChart();
    }

    private void updateMonthlyRevenueTable() {
        monthlyRevenueTableModel.setRowCount(0);
        int selectedYear = (Integer) yearComboBox.getSelectedItem();
        DecimalFormat df = new DecimalFormat("#,##0.00");

        // Tổng hợp doanh thu theo tháng
        Map<Integer, Double> monthlyRevenue = allInvoices.stream()
                .filter(invoice -> invoice.getDateCreated().getYear() == selectedYear)
                .collect(Collectors.groupingBy(
                        invoice -> invoice.getDateCreated().getMonthValue(),
                        Collectors.summingDouble(InvoiceDTO::getTotalPrice)
                ));

        // Điền dữ liệu cho tất cả các tháng
        for (int month = 1; month <= 12; month++) {
            String monthName = LocalDate.of(selectedYear, month, 1)
                    .getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("vi"));
            double revenue = monthlyRevenue.getOrDefault(month, 0.0);
            monthlyRevenueTableModel.addRow(new Object[]{monthName, df.format(revenue)});
        }
    }

    private void updateQuarterlyGrowthChart() {
        int selectedYear = (Integer) yearComboBox.getSelectedItem();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Tổng hợp doanh thu theo quý
        Map<Integer, Double> quarterlyRevenue = allInvoices.stream()
                .filter(invoice -> invoice.getDateCreated().getYear() == selectedYear)
                .collect(Collectors.groupingBy(
                        invoice -> (invoice.getDateCreated().getMonthValue() - 1) / 3 + 1,
                        Collectors.summingDouble(InvoiceDTO::getTotalPrice)
                ));

        // Điền dữ liệu cho biểu đồ
        for (int quarter = 1; quarter <= 4; quarter++) {
            double revenue = quarterlyRevenue.getOrDefault(quarter, 0.0);
            dataset.addValue(revenue, "Doanh Thu", "Quý " + quarter);
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                "Tăng Trưởng Doanh Thu Theo Quý (" + selectedYear + ")",
                "Quý",
                "Doanh Thu (VNĐ)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Đặt màu nền xanh nhạt (#ADD8E6)
        CategoryPlot plot = lineChart.getCategoryPlot();
        plot.setBackgroundPaint(new Color(173, 216, 230)); // #ADD8E6
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        quarterlyGrowthChartPanel.setChart(lineChart);
    }

    private void exportToExcel() throws IOException {
        int selectedYear = (Integer) yearComboBox.getSelectedItem();
        Workbook workbook = new XSSFWorkbook();
        DecimalFormat df = new DecimalFormat("#,##0.00");

        // Sheet 1: Doanh Thu Theo Tháng
        Sheet sheet1 = workbook.createSheet("Doanh Thu Theo Tháng");
        Row headerRow1 = sheet1.createRow(0);
        headerRow1.createCell(0).setCellValue("Tháng");
        headerRow1.createCell(1).setCellValue("Doanh Thu (VNĐ)");

        // Lấy dữ liệu từ monthlyRevenueTableModel
        for (int i = 0; i < monthlyRevenueTableModel.getRowCount(); i++) {
            Row row = sheet1.createRow(i + 1);
            row.createCell(0).setCellValue(monthlyRevenueTableModel.getValueAt(i, 0).toString());
            row.createCell(1).setCellValue(df.format(Double.parseDouble(monthlyRevenueTableModel.getValueAt(i, 1).toString().replace(",", ""))));
        }

        // Sheet 2: Doanh Thu Theo Quý (dựa trên dữ liệu từ quarterlyGrowthChart)
        Sheet sheet2 = workbook.createSheet("Doanh Thu Theo Quý");
        Row headerRow2 = sheet2.createRow(0);
        headerRow2.createCell(0).setCellValue("Quý");
        headerRow2.createCell(1).setCellValue("Doanh Thu (VNĐ)");

        // Tạo dữ liệu quý từ allInvoices
        Map<Integer, Double> quarterlyRevenue = allInvoices.stream()
                .filter(invoice -> invoice.getDateCreated().getYear() == selectedYear)
                .collect(Collectors.groupingBy(
                        invoice -> (invoice.getDateCreated().getMonthValue() - 1) / 3 + 1,
                        Collectors.summingDouble(InvoiceDTO::getTotalPrice)
                ));

        int rowNum = 1;
        for (int quarter = 1; quarter <= 4; quarter++) {
            Row row = sheet2.createRow(rowNum++);
            row.createCell(0).setCellValue("Quý " + quarter);
            row.createCell(1).setCellValue(df.format(quarterlyRevenue.getOrDefault(quarter, 0.0)));
        }

        // Lưu file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("BaoCao_DoanhThu_" + selectedYear + ".xlsx"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                workbook.write(outputStream);
                JOptionPane.showMessageDialog(this, "Xuất file Excel thành công!", "Thành Công", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Lỗi khi xuất file Excel: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
        workbook.close();
    }
}