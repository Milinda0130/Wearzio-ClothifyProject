package controller;

import db.DBConnection;
import dto.Order;
import dto.OrderDetails;
import dto.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Rectangle;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import service.ServiceFactory;
import service.custom.OrderDetailsService;
import service.custom.OrderService;
import service.custom.ProductService;
import util.ServiceType;

import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class ReportsController implements Initializable {

    @FXML
    private AreaChart<String, Number> chartSales;

    @FXML
    private ComboBox<String> cmbSaleSortTime;

    @FXML
    private Rectangle rectangle;

    private OrderService orderService;
    private OrderDetailsService orderDetailService;
    private ProductService productService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbSaleSortTime.getItems().addAll("All Time", "Last Month", "Last Week", "Last Day");
        cmbSaleSortTime.setValue("All Time");

        orderService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERS);
        orderDetailService = ServiceFactory.getInstance().getServiceType(ServiceType.ORDERPRODUCT);
        productService = ServiceFactory.getInstance().getServiceType(ServiceType.PRODUCT);

        loadAllSalesChart();
    }

    @FXML
    void cmbSalesOnAction(ActionEvent event) {
        String selected = cmbSaleSortTime.getSelectionModel().getSelectedItem();

        switch (selected) {
            case "Last Month":
                loadSalesChart(LocalDate.now().minusMonths(1), null);
                break;
            case "Last Week":
                loadSalesChart(LocalDate.now().minusWeeks(1), null);
                break;
            case "Last Day":
                loadSalesChart(LocalDate.now().minusDays(1), null);
                break;
            case "All Time":
            default:
                loadAllSalesChart();
        }
    }

    @FXML
    void btnSalesReportsOnAction(ActionEvent event) {
//        generateReport("SalesReport.jrxml");
    }

    private void loadAllSalesChart() {
        loadSalesChart(null, null);
    }

    private void loadSalesChart(LocalDate startDate, LocalDate endDate) {
        List<Order> orders = orderService.getOrders();
        List<OrderDetails> orderDetailsList = orderDetailService.getOrderProducts();

        Map<String, Integer> salesMap = new HashMap<>();

        for (Order order : orders) {
            LocalDate orderDate = order.getDate().toLocalDate();

            if (startDate != null && orderDate.isBefore(startDate)) continue;
            if (endDate != null && orderDate.isAfter(endDate)) continue;

            for (OrderDetails od : orderDetailsList) {
                if (od.getOrderId() == order.getId()) {
                    Product product = productService.getProductById(od.getProductId());
                    if (product != null) {
                        String category = product.getCategory();
                        salesMap.put(category, salesMap.getOrDefault(category, 0) + od.getQuantity());
                    }
                }
            }
        }

        chartSales.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Sales by Category");

        for (Map.Entry<String, Integer> entry : salesMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chartSales.getData().add(series);
        adjustYAxis(chartSales, salesMap.values());
    }

    private void adjustYAxis(AreaChart<String, Number> chart, Collection<Integer> values) {
        NumberAxis yAxis = (NumberAxis) chart.getYAxis();
        yAxis.setTickUnit(1);
        yAxis.setMinorTickCount(0);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(Collections.max(values) + 5);
    }

    private void generateReport(String reportFileName) {
        try {
            // Corrected path: absolute path from classpath root
            InputStream reportStream = getClass().getResourceAsStream("/reports/" + reportFileName);

            // Handle missing resource
            if (reportStream == null) {
                System.err.println("Report file not found: /reports/" + reportFileName);
                return;
            }

            JasperDesign design = JRXmlLoader.load(reportStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    null,
                    DBConnection.getInstance().getConnection()
            );
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException e) {
            e.printStackTrace();
        }
    }
}
