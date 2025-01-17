/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import controller.*;
import database.DB;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterJob;
import models.ExpenseDataModel;
import models.ItemsDataModel;
import utils.xValue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javax.print.PrintService;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import javax.print.PrintServiceLookup;
import models.CartItem;
import models.EmployeeDataModel;
import models.InvoiceDataModel;
import models.UserDataModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.printing.PDFPageable;



/**
 *
 * @author F_Reza
 */
public class managerFormController implements Initializable {
    private DB db = new DB();
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private String inv_date;
    private String inv_time;
    private Alert alert;
    
    //// SET Variable
    private static int id;
    private static String path;
    private static String imagePath;
    private Image image;
    private static final String IMAGE_DIR = "Images";
    private static String getEmpDate;
    
    
    //All Form Section Start
    @FXML private StackPane main_window;
    @FXML private AnchorPane main_Form,dashboadrForm,itemsForm,posMenuForm,cullectBillForm,invoicesForm,expensesForm,reportForm,usersForm,settingsForm;
    //End

    //All Nav Section Start
    @FXML private Button dashboardBtn,itemsBtn,posMenuBtn,cullectBillBtn,invoicesBtn,expensesBtn,reportBtn,usersBtn,settingsBtn,signoutBtn;
    @FXML private Label userName,devInfoBtn;
    //End

    //Dashboard Section Start
    @FXML private Button slideshowAddBtn,slideshowRemoveBtn,slideshowNextBtn,slideshowPreviousBtn;
    @FXML private AnchorPane slideshowAnchorPane;
    @FXML private ImageView slideshowImageView;
    
    @FXML private Label todayOrderDash,todayIncomeDash,todayExpenseDash,pendingAmountDash,
            totalInvoicesDash,totalItemsDash,totalPackageDash,totalUsersDash,
            newsTextLabel,dashDateLabel,dashTimeLabel,todayLabel;
    private static final String IMAGE_FOLDER = "src/slider_img";
    private List<File> imageFiles = new ArrayList<>();
    private int currentIndex = 0;
    private Timeline slideshowTimeline;
    private int xOrder;
    private double xIncome, xExpense, xPendingAmount;
    private String[] phrases;
    private int phraseIndex = 0;
    private String admin =  "";
    Boolean isWindowStatus = false;
    //End


    // Item Section Start
    @FXML private TextField items_Name,items_UnitPrice,items_Size;
    @FXML private ComboBox<String> items_Category,items_Status;
    @FXML private ImageView items_ImageView;
    @FXML private Button items_ImportBtn;
    @FXML private TableView<ItemsDataModel> items_TableView;
    @FXML private TableColumn<ItemsDataModel, String> 
            items_Col_ItemsSn,items_Col_ItemsName,items_Col_Category,items_Col_Size,items_Col_UnitPrice,items_Col_Status,items_Col_Date;
    //End


    //POS Menu Section Start
    @FXML private AnchorPane menuAnchorPane;
    @FXML private GridPane menuGridPane;
    @FXML private ScrollPane menuScrollPane;
    @FXML private TextField menu_searchItem;
    @FXML private Button mealsBtn;
    @FXML private Button drinksBtn;
    @FXML private Button packagesBtn;
    @FXML private Button othersBtn;
    @FXML private Button allBtn;
    @FXML private Button menu_srcClearBtn;
    @FXML private TableView<CartItem> menuTableView;
    @FXML private TableColumn<CartItem, String> menu_Col_SN;
    @FXML private TableColumn<CartItem, String> menu_Col_ItemName;
    @FXML private TableColumn<CartItem, Integer> menu_Col_Qty;
    @FXML private TableColumn<CartItem, Double> menu_Col_ItemRate;
    @FXML private TableColumn<CartItem, Double> menu_Col_Price;
    @FXML private TableColumn<CartItem, Button> menu_Col_Action;
    @FXML private Label menu_SubTotal;
    @FXML private TextField menu_Discount;
    @FXML private TextField menu_OthersCharge;
    @FXML private TextField menu_Note;
    @FXML private Label menu_Total;
    @FXML private Label totalQty; 
    @FXML private ComboBox<String> menu_OrderType;
    @FXML private TextField menu_ServedBy;
    @FXML private Button menu_ClearBtn;
    @FXML private Button menu_InvoiceBtn;
    int xMaxID = 0;
    int t_Qty = 0;
    double t_Price = 0;
    double xGrandTotal = 0;
    double xDiscount = 0;
    double xOthersCharge = 0;
    String xInvID = "";
    String xNote = "";
    String xOrderType = "";
    String xServedBy = "";
    String xBillBy = "";
    String xDate = "";
    //End
	
  
    //Cullect Bill Section Start
    @FXML private GridPane cullectBillGridPane;
    @FXML private ScrollPane cullectBillScrollPane;
    @FXML private Label totalPendingAmount;
    //End

    //Invoices Section Start
    @FXML private TextField srcByInvId;
    @FXML private Label totalInvoice;
    @FXML private Label pendingInvoices;
    @FXML private Label completeInvoices;
    @FXML private Label totalPendingAmountInv;
    @FXML private TableView<InvoiceDataModel> invoice_TableView;
    @FXML private TableColumn<InvoiceDataModel, String> invoice_Col_SN;
    @FXML private TableColumn<InvoiceDataModel, String> invoice_Col_InvID;
    @FXML private TableColumn<InvoiceDataModel, Double> invoice_Col_GTotal;
    @FXML private TableColumn<InvoiceDataModel, String> invoice_Col_OrderType;
    @FXML private TableColumn<InvoiceDataModel, String> invoice_Col_ServedBy;
    @FXML private TableColumn<InvoiceDataModel, String> invoice_Col_BillBy;
    @FXML private TableColumn<InvoiceDataModel, String> invoice_Col_PaymentStatus;
    @FXML private TableColumn<InvoiceDataModel, String> invoice_Col_Date;
    @FXML private TableColumn<InvoiceDataModel, String> invoice_Col_Action;
    //End
	
    //Expense Section Start
    @FXML private Button expense_AddBtn;
    @FXML private Button expense_UpdateBtn;
    @FXML private Button expense_DeleteBtn;
    @FXML private Button expense_ClearBtn;
    @FXML private TextField expense_Amount;
    @FXML private ComboBox<String> expense_Category;
    @FXML private TextArea expense_Discription;	
    @FXML private TextField expense_By;
    @FXML private DatePicker expense_Date;	
    @FXML private TableView<ExpenseDataModel> expense_TableView;
    @FXML private TableColumn<ExpenseDataModel, String> expense_Col_Sn;
    @FXML private TableColumn<ExpenseDataModel, String> expense_Col_Amount;
    @FXML private TableColumn<ExpenseDataModel, String> expense_Col_Category;
    @FXML private TableColumn<ExpenseDataModel, String> expense_Col_Description;
    @FXML private TableColumn<ExpenseDataModel, String> expense_Col_ExpensedBy;
    @FXML private TableColumn<ExpenseDataModel, String> expense_Col_Date;  
    
    @FXML private DatePicker expenseDatePicker;
    @FXML private Label selectedDateExpense;
    @FXML private Button getExpSpecificBtn;
    @FXML private DatePicker startExpDatePicker;
    @FXML private DatePicker endExpDatePicker;
    @FXML private Label dateRangeExpense;
    @FXML private Button getExpDateRangeBtn;
    @FXML private Button getExp_ClearBtn;

    @FXML private Label todayExpense;
    @FXML private Label yesterdayExpense;
    @FXML private Label thisweekExpense;
    @FXML private Label thismonthExpense;
    @FXML private Label thisyearExpense;
    @FXML private Label totalExpense;
    //End
	
    //Reports Section Start
    @FXML private Label totalOrderTopRpt;
    @FXML private Label totalExpenseTopRpt;
    @FXML private Label totalIncomeTopRpt;
    @FXML private Label totalPendingAmountRpt;
    @FXML private Label totalInvoicesRpt;
    @FXML private Label totalItemsRpt;
    @FXML private Label totalPackageRpt;
    @FXML private Label totalUsersRpt;
    
    @FXML private Label todayOrderRpt;
    @FXML private Label yesterdayOrderRpt;
    @FXML private Label thisweekOrderRpt;
    @FXML private Label thismonthOrderRpt;
    @FXML private Label thisyearOrderRpt;
    @FXML private Label totalOrderRpt;

    @FXML private Label todayIncomeRpt;
    @FXML private Label yesterdayIncomeRpt;
    @FXML private Label thisweekIncomeRpt;
    @FXML private Label thismonthIncomeRpt;
    @FXML private Label thisyearIncomeRpt;
    @FXML private Label totalIncomeRpt;
    
    @FXML private Label todayExpenseRpt;
    @FXML private Label yesterdayExpenseRpt;
    @FXML private Label thisweekExpenseRpt;
    @FXML private Label thismonthExpenseRpt;
    @FXML private Label thisyearExpenseRpt;
    @FXML private Label totalExpenseRpt;
    
    @FXML private ComboBox tableComboBox;
    @FXML private Button exportBtn;
    @FXML private CategoryAxis xAxis,xAxis7Inc,xAxisOdr,xAxis7Ord;
    @FXML private AreaChart<String, Number> orderAreaChart,orderAreaChart7Days,incomeAreaChart,incomeAreaChart7Days;
    @FXML private PieChart expensePieChart,expensePieChartDash,expensePieChartRpt;

    //End
    
    
    //Users Section Start
    @FXML private Label userDisplayName; 
    @FXML private ImageView userImage;    
    @FXML private Label adminUserName;
    @FXML private Label userRole;
    @FXML private Label userStatus;
    @FXML private Label activeQuestion;
    @FXML private Label userDate;
    @FXML private Button editProfileBtn;
    @FXML private Button changeUserPassBtn;


    @FXML private Button empAddBtn;
    @FXML private Button empUpdateBtn;
    @FXML private Button empDeleteBtn;

    private static int emp_id;
    private static boolean check_pass;
    private static String emp_pass;
    private static Date emp_date;
    
    @FXML private TextField emp_username;
    @FXML private PasswordField emp_password;
    @FXML private ComboBox<String> emp_user_role;
    @FXML private ComboBox<String> emp_user_status;
    
    @FXML private TextField emp_passPlainText;
    @FXML private CheckBox showPassCheckBox;
    
    @FXML private TableView<EmployeeDataModel> empUser_TableView;
    @FXML private TableColumn<EmployeeDataModel, String> emp_Col_Sn;
    @FXML private TableColumn<EmployeeDataModel, String> emp_Col_UserName;
    @FXML private TableColumn<EmployeeDataModel, String> emp_Col_Password;
    @FXML private TableColumn<EmployeeDataModel, String> emp_Col_UserRole;
    @FXML private TableColumn<EmployeeDataModel, String> emp_Col_Status;
    @FXML private TableColumn<EmployeeDataModel, String> emp_Col_Date;
    //End

    //Settings Section Start
    @FXML private Button appUpBtn;
    //End

    
    //#########################################################################################################
    
    //// START DASHBOARD SECTION
    public void switchForm(ActionEvent event) {

        if (event.getSource() == dashboardBtn) {
            dashboadrForm.setVisible(true);
            itemsForm.setVisible(false);
            posMenuForm.setVisible(false);
            cullectBillForm.setVisible(false);
            invoicesForm.setVisible(false);
            expensesForm.setVisible(false);
            reportForm.setVisible(false);
            usersForm.setVisible(false);
            settingsForm.setVisible(false);
            
            loadAdminData(1);
            initializeSlideshow();
            loadDashTopData();
            
            loadLast7DaysIncome();
            loadLast7DaysOrder();
            loadExpensesByCategoryThisYear();
            
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();

        } else if (event.getSource() == itemsBtn) {
            dashboadrForm.setVisible(false);
            itemsForm.setVisible(true);
            posMenuForm.setVisible(false);
            cullectBillForm.setVisible(false);
            invoicesForm.setVisible(false);
            expensesForm.setVisible(false);
            reportForm.setVisible(false);
            usersForm.setVisible(false);
            settingsForm.setVisible(false);
           
            
            itemsCategoryList();
            itemsStatusList();
            itemsShowData();
            setDynamicColumnWidthForItem();
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();

        } else if (event.getSource() == posMenuBtn) {
            dashboadrForm.setVisible(false);
            itemsForm.setVisible(false);
            posMenuForm.setVisible(true);
            cullectBillForm.setVisible(false);
            invoicesForm.setVisible(false);
            expensesForm.setVisible(false);
            reportForm.setVisible(false);
            usersForm.setVisible(false);
            settingsForm.setVisible(false);

            //menuDisplayCard();
            setupCartTable();
            setDynamicColumnWidthForCartTable();
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();

        } else if (event.getSource() == cullectBillBtn) {
            dashboadrForm.setVisible(false);
            itemsForm.setVisible(false);
            posMenuForm.setVisible(false);
            cullectBillForm.setVisible(true);
            invoicesForm.setVisible(false);
            expensesForm.setVisible(false);
            reportForm.setVisible(false);
            usersForm.setVisible(false);
            settingsForm.setVisible(false);
            
            billDisplayCard();
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();

        } else if (event.getSource() == invoicesBtn) {
            dashboadrForm.setVisible(false);
            itemsForm.setVisible(false);
            posMenuForm.setVisible(false);
            cullectBillForm.setVisible(false);
            invoicesForm.setVisible(true);
            expensesForm.setVisible(false);
            reportForm.setVisible(false);
            usersForm.setVisible(false);
            settingsForm.setVisible(false);
            
            invoiceShowData();
            loadTotalPendingAmount();
            setDynamicColumnWidthForInvoiceTable();
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();

        } else if (event.getSource() == expensesBtn) {
            dashboadrForm.setVisible(false);
            itemsForm.setVisible(false);
            posMenuForm.setVisible(false);
            cullectBillForm.setVisible(false);
            invoicesForm.setVisible(false);
            expensesForm.setVisible(true);
            reportForm.setVisible(false);
            usersForm.setVisible(false);
            settingsForm.setVisible(false);
            
            expenseCategoryList();
            expenseShowData();
            setDynamicColumnWidthForExp();
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();
            loadExpenseData();
            getExpDateValidation();
            getExpDateRangeValidation();
            
            

        } else if (event.getSource() == reportBtn) {
            dashboadrForm.setVisible(false);
            itemsForm.setVisible(false);
            posMenuForm.setVisible(false);
            cullectBillForm.setVisible(false);
            invoicesForm.setVisible(false);
            expensesForm.setVisible(false);
            reportForm.setVisible(true);
            usersForm.setVisible(false);
            settingsForm.setVisible(false);
            
            loadITopDataRpt();
            loadOrderDataRpt();
            loadIncomeDataRpt();
            loadExpenseDataRpt();
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();

        } else if (event.getSource() == usersBtn) {
            dashboadrForm.setVisible(false);
            itemsForm.setVisible(false);
            posMenuForm.setVisible(false);
            cullectBillForm.setVisible(false);
            invoicesForm.setVisible(false);
            expensesForm.setVisible(false);
            reportForm.setVisible(false);
            usersForm.setVisible(true);
            settingsForm.setVisible(false);
            
            loadAdminData(1);
            empUserRoleList();
            empUserStatusList();
            empUserShowData();
            showPass();
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();

        } else if (event.getSource() == settingsBtn) {
            dashboadrForm.setVisible(false);
            itemsForm.setVisible(false);
            posMenuForm.setVisible(false);
            cullectBillForm.setVisible(false);
            invoicesForm.setVisible(false);
            expensesForm.setVisible(false);
            reportForm.setVisible(false);
            usersForm.setVisible(false);
            settingsForm.setVisible(true);
            
            empClearBtn();
            itemsClearBtn();
            expenseClearBtn();
        }

    }
    public void logout() {
    try {
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.isPresent() && option.get().equals(ButtonType.OK)) { //if (option.get().equals(ButtonType.OK)) {

            Preferences preferences = Preferences.userNodeForPackage(AuthController.class);
            //preferences.putBoolean("logInSave", false);
            //preferences.put("logUserName", ""); 
            preferences.clear();
            db.closeConnection();

            // Hide the main form
            signoutBtn.getScene().getWindow().hide();

            // Load and show the login form
            Parent root = FXMLLoader.load(getClass().getResource("/view/authPage.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            
            stage.setMinHeight(450);
            stage.setMaxHeight(450);
            stage.setMinWidth(616);
            stage.setMaxWidth(616);

            stage.setTitle("GoPoo Management System");
            stage.setScene(scene);
            stage.show();
            db.closeConnection();
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void displayUsername() {
        String xUser = xValue.username;
        if(xUser == null){
            xUser = "Arko!";
        }  
        xUser = xUser.substring(0, 1).toUpperCase() + xUser.substring(1);
        String xName = "Welcome, " + xUser + "!";     
        userName.setText(xName);
    } 
    private void loadImagesFromFolder() {
        File folder = new File(IMAGE_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] files = folder.listFiles(file -> file.isFile() && isImageFile(file));
        if (files != null) {
            for (File file : files) {
                imageFiles.add(file);
            }
        }
    }
    private boolean isImageFile(File file) {
        String lowerName = file.getName().toLowerCase();
        return lowerName.endsWith(".png") || lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg") ||
               lowerName.endsWith(".gif") || lowerName.endsWith(".bmp");
    }
    private void updateImageView() {
        if (!imageFiles.isEmpty()) {
            try (FileInputStream input = new FileInputStream(imageFiles.get(currentIndex))) {
                Image image = new Image(input);
                slideshowImageView.setImage(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            slideshowImageView.setImage(null); // Clear if no images are available
        }
    }
    private void showPreviousImage() {
        if (!imageFiles.isEmpty()) {
            currentIndex = (currentIndex - 1 + imageFiles.size()) % imageFiles.size();
            updateImageView();
        }
    }
    private void showNextImage() {
        if (!imageFiles.isEmpty()) {
            currentIndex = (currentIndex + 1) % imageFiles.size();
            updateImageView();
        }
    }
    private void importImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );
        File selectedFile = fileChooser.showOpenDialog(slideshowAnchorPane.getScene().getWindow());

        if (selectedFile != null) {
            try {
                Path targetPath = new File(IMAGE_FOLDER, selectedFile.getName()).toPath();
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
                imageFiles.add(targetPath.toFile());
                currentIndex = imageFiles.size() - 1; // Show the newly imported image
                updateImageView();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } 
    private void removeCurrentImage() {
        if (!imageFiles.isEmpty()) {
            File currentImageFile = imageFiles.get(currentIndex);
            imageFiles.remove(currentIndex);

            // Delete file from folder
            try {
                Files.delete(currentImageFile.toPath());
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Update current index and image view
            if (currentIndex >= imageFiles.size()) {
                currentIndex = 0; // Reset to start if we go out of bounds
            }
            updateImageView();
        }
    }
    private void startAutoPlay() {
        if (slideshowTimeline != null && slideshowTimeline.getStatus() == Timeline.Status.RUNNING) {
            return; // Already running
        }

        slideshowTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> showNextImage()));
        slideshowTimeline.setCycleCount(Timeline.INDEFINITE);
        slideshowTimeline.play();
    }
    
    private void deactiveMsgMax() {
        slideshowImageView.fitWidthProperty().bind(slideshowAnchorPane.widthProperty());
        slideshowImageView.fitHeightProperty().bind(slideshowAnchorPane.heightProperty());
        slideshowImageView.setPreserveRatio(false);
        slideshowImageView.setImage(image);
    }
    private void deactiveMsgDown() {
        // Unbind properties and restore fixed size with stretching to fit container
        slideshowImageView.fitWidthProperty().unbind();
        slideshowImageView.fitHeightProperty().unbind();
        slideshowImageView.setPreserveRatio(false); // Disable aspect ratio preservation
        slideshowImageView.setSmooth(true); // Smooth scaling
        slideshowImageView.setFitWidth(slideshowAnchorPane.getPrefWidth()+2);
        slideshowImageView.setFitHeight(slideshowAnchorPane.getPrefHeight()+1);
        slideshowImageView.setImage(image);
    }
    public void dynamicView() {
        // Bind the ImageView's fitWidth and fitHeight to the AnchorPane's width and height
        slideshowImageView.fitWidthProperty().bind(slideshowAnchorPane.widthProperty());
        slideshowImageView.fitHeightProperty().bind(slideshowAnchorPane.heightProperty());
        slideshowImageView.setPreserveRatio(false);
        // Load a default image if needed
        //slideshowImageView.setImage(new Image("file:src/img/slider.jpg"));
        slideshowImageView.setImage(image);
    }
    private void initializeSlideshow(){
        // Load images from the designated folder
        loadImagesFromFolder();
        startAutoPlay();
        updateImageView();
        dynamicView();
        //getTextData();

        // Set up button actions
        slideshowAddBtn.setOnAction(e -> importImage());
        slideshowRemoveBtn.setOnAction(e -> removeCurrentImage());
        slideshowNextBtn.setOnAction(e -> showNextImage());
        slideshowPreviousBtn.setOnAction(e -> showPreviousImage());

    }
    private void startDateTimeDisplay() {
        Timeline dateTimeTimeline = new Timeline(
            new KeyFrame(Duration.seconds(1), event -> {
                String dateTimed = new SimpleDateFormat("EEEE").format(new Date());
                String dateTimeD = new SimpleDateFormat("dd MMM yyyy").format(new Date());
                String dateTimeT = new SimpleDateFormat("hh:mm:ss aa").format(new Date());
                dashDateLabel.setText(dateTimeD);
                dashTimeLabel.setText(dateTimeT); 
                todayLabel.setText(dateTimed); 
            })
        );

        dateTimeTimeline.setCycleCount(Timeline.INDEFINITE);  // Run indefinitely
        dateTimeTimeline.play();  // Start the date-time display timeline
    }
    private void loadDashTopData() {
    if (todayOrderDash != null && todayIncomeDash != null && todayExpenseDash != null && 
        pendingAmountDash != null && totalInvoicesDash != null && totalItemsDash != null && 
        totalPackageDash != null && totalUsersDash != null) {
        
        xOrder = db.getTodayOrder();
        xIncome = db.getTodayIncome();
        xExpense = db.getTodayExpenses();
        xPendingAmount = db.getTotalInvoicePendingAmount();

        todayOrderDash.setText(String.valueOf(xOrder));
        todayIncomeDash.setText(String.format("%.2f TK", xIncome));
        todayExpenseDash.setText(String.format("%.2f TK", xExpense));
        pendingAmountDash.setText(String.format("%.2f TK", xPendingAmount));
        
        totalInvoicesDash.setText(""+db.getTotalInvoice());
        totalItemsDash.setText(""+db.getTotalItems());
        totalPackageDash.setText(""+db.getTotalPackage());
        int xUsers = db.gettoTalUsers() + 1;
        totalUsersDash.setText(""+xUsers);
        String xAdmin = xValue.username;
        if(xAdmin == null){
            admin = "Arko";
        }else{
            admin = xAdmin.substring(0, 1).toUpperCase() + xAdmin.substring(1);
        }
        initializePhrases();
    } else {
        System.err.println("One or more labels are not initialized!");
    }
}
    private void initializePhrases() {
        phrases = new String[] {
            " ","Hi "+admin+"! ✋ ", "NEWS UPDATE FOR TODAY - ", 
            "Today Order: " + xOrder+" ", 
            "Income: " + xIncome + " TK ", 
            "Expense: " + xExpense + " TK ", 
            "Pending Amount: " + xPendingAmount + " TK "
        };
        
        //System.out.println("--------> "+xOrder+"---- "+xIncome+"---- "+xExpense+"---- "+xPendingAmount);
    }
    private void startTypingEffect() {
        if (phrases == null || phrases.length == 0) {
            System.err.println("Phrases array is empty or null!");
            return;
        }

        if (phraseIndex >= phrases.length) {
            phraseIndex = 0;  // Loop back to the first phrase
        }

        String currentPhrase = phrases[phraseIndex];
        newsTextLabel.setText("");  // Clear text before typing a new phrase

        Timeline typingTimeline = new Timeline();
        Duration characterDelay = Duration.millis(200);  // Delay between characters

        for (int i = 0; i < currentPhrase.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(characterDelay.multiply(i + 1), event -> {
                newsTextLabel.setText(currentPhrase.substring(0, index + 1));
            });
            typingTimeline.getKeyFrames().add(keyFrame);
        }

        typingTimeline.setOnFinished(event -> {
            phraseIndex++;
            startTypingEffect();
        });

        typingTimeline.play();
    } 
    
    //// END DASHBOARD SECTION
    
    
    //// START ITEM SECTION
    private final String[] itemsCategoryList = {"Meals", "Drinks", "Packages", "Others"};
    public void itemsCategoryList() {

        List<String> xVal = new ArrayList<>();

        for (String data : itemsCategoryList) {
            xVal.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(xVal);
        items_Category.setItems(listData);
    }
    private final String[] itemsStatusList = {"Available", "Unavailable"};
    public void itemsStatusList() {

        List<String> xVal = new ArrayList<>();

        for (String data : itemsStatusList) {
            xVal.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(xVal);
        items_Status.setItems(listData);

    }
    public void itemsImportBtn() {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        
        File file = fileChooser.showOpenDialog(main_Form.getScene().getWindow());
        
        if (file != null) {
            
            path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 146, 146, false, true);
            items_ImageView.setImage(image);
            
            try {
                // Get the original file name
                String originalFileName = file.getName();
                String newImageName = "item_image_" + originalFileName;

                // Copy the selected file to the MyFolder directory with the new image name
                File destination = new File(IMAGE_DIR + File.separator + newImageName);
                Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Save the image path to the SQLite database
                imagePath = destination.getAbsolutePath();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
    private void itemInsertQry() {
        try {
            String insertData = "INSERT INTO items "
                    + "(items_name, category, size, unit_price, status, image, date) "
                    + "VALUES(?,?,?,?,?,?,?)";

            prepare = db.connection.prepareStatement(insertData);

            prepare.setString(1, items_Name.getText());
            prepare.setString(2, (String) items_Category.getSelectionModel().getSelectedItem());
            prepare.setString(3, items_Size.getText());
            prepare.setString(4, items_UnitPrice.getText());
            prepare.setString(5, (String) items_Status.getSelectionModel().getSelectedItem());
            prepare.setString(6, imagePath);
            prepare.setLong(7, System.currentTimeMillis());

            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Added!");
            alert.showAndWait();

            System.out.println("-> Item Data Inserted!.");
            itemsShowData();
            itemsClearBtn();

        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("-> "+e);
        }
    }
    public void itemsAddBtn() {
        db.getConnection();
        
        String unitPriceText = items_UnitPrice.getText();
        double itemsUnitPrice = 0.0;
        
        if (items_Name.getText().isEmpty()
                || items_Category.getSelectionModel().getSelectedItem() == null
                || items_UnitPrice.getText().isEmpty()
                || items_Status.getSelectionModel().getSelectedItem() == null
                || imagePath.isEmpty() || imagePath == null ) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else if (unitPriceText != null) {
            
            try {
                itemsUnitPrice = Double.parseDouble(unitPriceText);
                if (itemsUnitPrice > 0) {
                    itemInsertQry();
                } else {
                    // Action to handle 0 or negative values
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Unit Price should be greater than 0.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid 'Unit Price' number format: "+ unitPriceText);
                alert.showAndWait();
            }
            
        } else {
            itemInsertQry();
        }
        
    }
    private void itemUpdateQry() {
        String updateData = "UPDATE items SET "
                    + "items_name = '" + items_Name.getText() + "', "
                    + "category = '" + items_Category.getSelectionModel().getSelectedItem() + "', "
                    + "size = '" + items_Size.getText() + "', "
                    + "unit_price = '" + items_UnitPrice.getText() + "', "
                    + "status = '" + items_Status.getSelectionModel().getSelectedItem() + "', "
                    + "image = '" + imagePath + "', "
                    + "date = '" + System.currentTimeMillis() + "' WHERE id = " + 
                    items_TableView.getSelectionModel().getSelectedItem().getId();
        
        try {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE Item: " + items_Name.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                prepare = db.connection.prepareStatement(updateData);
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                System.out.println("-> Item Data Updated!");
                // TO UPDATE YOUR TABLE VIEW
                itemsShowData();
                // TO CLEAR YOUR FIELDS
                itemsClearBtn();
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("-> "+e);
        }
    }
    public void itemsUpdateBtn() {
        db.getConnection();

        String unitPriceText = items_UnitPrice.getText();
        double itemsUnitPrice = 0.0;
        
        if (items_Name.getText().isEmpty()
                || items_Category.getSelectionModel().getSelectedItem() == null
                || items_UnitPrice.getText().isEmpty()
                || items_Status.getSelectionModel().getSelectedItem() == null
                || imagePath.isEmpty() || imagePath == null 
                || items_TableView.getSelectionModel().getSelectedItem().getId() == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else if (unitPriceText != null) {
            
            try {
                itemsUnitPrice = Double.parseDouble(unitPriceText);
                if (itemsUnitPrice > 0) {
                    itemUpdateQry();
                } else {
                    // Action to handle 0 or negative values
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Unit Price should be greater than 0.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid 'Unit Price' number format: "+ unitPriceText);
                alert.showAndWait();
            }
            
        } else if(items_Name.getText() == null ? 
                items_TableView.getSelectionModel()
                        .getSelectedItem().getItemsName() == null : 
                items_Name.getText().equals(items_TableView.getSelectionModel()
                        .getSelectedItem().getItemsName())) {
            itemUpdateQry();
            
        } else {
            itemUpdateQry();
        }
    }
    public void itemsDeleteBtn() {
        if (id == 0) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Select Item");
            alert.showAndWait();

        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE Item: " + items_Name.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM items WHERE id = " + id;
                try {
                    prepare = db.connection.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully Deleted!");
                    alert.showAndWait();

                    System.out.println("-> Item Data Deleted!");
                    // TO UPDATE YOUR TABLE VIEW
                    itemsShowData();
                    // TO CLEAR YOUR FIELDS
                    itemsClearBtn();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }
    public void itemsClearBtn() {
        items_Name.setText("");
        items_Category.getSelectionModel().clearSelection();
        items_Size.setText("");
        items_UnitPrice.setText("");
        items_Status.getSelectionModel().clearSelection();
        imagePath = "";
        items_ImageView.setImage(null);
        id = 0;
    }
    public ObservableList<ItemsDataModel> itemsDataList() {

        ObservableList<ItemsDataModel> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM items";

        db.getConnection();

        try {

            prepare = db.connection.prepareStatement(sql);
            result = prepare.executeQuery();

            ItemsDataModel itemData;

            while (result.next()) {

                itemData = new ItemsDataModel(
                        result.getInt("id"),
                        result.getString("items_name"),
                        result.getString("category"),
                        result.getString("size"),
                        result.getDouble("unit_price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(itemData);

            } 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<ItemsDataModel> itemsListData;
    public void itemsShowData() {
        itemsListData = itemsDataList();

        items_Col_ItemsSn.setCellValueFactory(new PropertyValueFactory<>("itemsCode"));
        items_Col_ItemsSn.setCellValueFactory(cellData -> {
            int index = items_TableView.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });
        
        items_Col_ItemsName.setCellValueFactory(new PropertyValueFactory<>("itemsName"));
        items_Col_Category.setCellValueFactory(new PropertyValueFactory<>("category"));
        items_Col_Size.setCellValueFactory(new PropertyValueFactory<>("size"));
        items_Col_UnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        items_Col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        items_Col_Date.setCellValueFactory(new PropertyValueFactory<>("date"));

        items_TableView.setItems(itemsListData);

    }
    private void setDynamicColumnWidthForItem() {
        items_Col_ItemsSn.maxWidthProperty().bind(items_TableView.widthProperty().multiply(0.5));
        items_Col_ItemsName.maxWidthProperty().bind(items_TableView.widthProperty().multiply(1.8));
        items_Col_Category.maxWidthProperty().bind(items_TableView.widthProperty().multiply(1.2));
        items_Col_Size.maxWidthProperty().bind(items_TableView.widthProperty().multiply(1.0833333333));
        items_Col_UnitPrice.maxWidthProperty().bind(items_TableView.widthProperty().multiply(1.0833333333));
        items_Col_Status.maxWidthProperty().bind(items_TableView.widthProperty().multiply(1.0833333333));
        items_Col_Date.maxWidthProperty().bind(items_TableView.widthProperty().multiply(1.0833333333));
    }
    public void itemsSelectData() {

    ItemsDataModel itemData = items_TableView.getSelectionModel().getSelectedItem();
    int num = items_TableView.getSelectionModel().getSelectedIndex();

    if ((num - 1) < -1) {
        return;
    }

    items_Name.setText(itemData.getItemsName());
    items_Size.setText(itemData.getSize());
    items_UnitPrice.setText(String.valueOf(itemData.getUnitPrice()));
    items_Category.setValue(itemData.getCategory());
    items_Status.setValue(itemData.getStatus()); 
    
    xValue.date = String.valueOf(itemData.getDate());
    
    imagePath = itemData.getImage();
    String path = "File:" + itemData.getImage();
    id = itemData.getId();

    image = new Image(path, 146, 146, false, true);
    items_ImageView.setImage(image);
}
    //// END ITEM SECTION
    
    //// START POS MENU SECTION
    private final ObservableList<ItemsDataModel> cardListData = FXCollections.observableArrayList();
    private ObservableList<CartItem> cartData = FXCollections.observableArrayList();
    private ObservableList<String> itemData = FXCollections.observableArrayList();
    private final String[] menuOrderTypeList = {"Table", "Parcel"};
    public void menuOrderTypeList() {

        List<String> xVal = new ArrayList<>();

        for (String data : menuOrderTypeList) {
            xVal.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(xVal);
        menu_OrderType.setItems(listData);
    }
    public ObservableList<ItemsDataModel> menuGetData() {

        String sql = "SELECT * FROM items";

        ObservableList<ItemsDataModel> listData = FXCollections.observableArrayList();
        db.getConnection();

        try {
            prepare = db.connection.prepareStatement(sql);
            result = prepare.executeQuery();

            ItemsDataModel item;

            while (result.next()) {
                item = new ItemsDataModel(result.getInt("id"),
                        result.getString("items_name"),
                        result.getString("category"),
                        result.getString("size"),
                        result.getDouble("unit_price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));

                listData.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }
    public void menuDisplayCardX() { 
        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menuGridPane.getChildren().clear();
        menuGridPane.getRowConstraints().clear();
        menuGridPane.getColumnConstraints().clear();
        
        for (int q = 0; q < cardListData.size(); q++) {
            try {
                    FXMLLoader load = new FXMLLoader();
                    load.setLocation(getClass().getResource("/view/cardItem.fxml"));
                    AnchorPane pane = load.load();
                    CardItemController cardC = load.getController();
                    cardC.setData(cardListData.get(q));
                    
                    // Event handler for individual pane clicks
                    final int index = q; // This captures the index for the lambda
                    pane.setOnMouseClicked(event -> addToCart(cardListData.get(index)));

                    // Grid setup
                    if (column == 4) {
                            column = 0;
                            row += 1;
                    }
                    menuGridPane.add(pane, column++, row);
                    GridPane.setMargin(pane, new Insets(6));

            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }
    public void menuDisplayCard(int columnCount) { 
        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        menuGridPane.getChildren().clear();
        menuGridPane.getRowConstraints().clear();
        menuGridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/view/cardItem.fxml"));
                AnchorPane pane = load.load();
                CardItemController cardC = load.getController();
                cardC.setData(cardListData.get(q));

                // Event handler for individual pane clicks
                final int index = q; // This captures the index for the lambda
                pane.setOnMouseClicked(event -> addToCart(cardListData.get(index)));

                // Dynamic Grid setup based on column count
                if (column == columnCount) {
                    column = 0;
                    row += 1;
                }
                menuGridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(6));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void getDynamicDisplayCard() {
        menuDisplayCard(4);
        double thresholdWidth = 1000.0;
        menuScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            int columnCount = (newVal.doubleValue() > thresholdWidth) ? 7 : 4;
            menuDisplayCard(columnCount); // Update the display with the new column count
        });
    }
    private void displayFilteredCards(ObservableList<ItemsDataModel> filteredItems, int columnCount) {
        menuGridPane.getChildren().clear(); // Clear existing cards
        int row = 0;
        int column = 0;

        for (int i = 0; i < filteredItems.size(); i++) {
            try {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("/view/cardItem.fxml"));
                AnchorPane pane = load.load();
                CardItemController cardC = load.getController();
                cardC.setData(filteredItems.get(i));

                // Event handler for individual pane clicks
                final int index = i;
                pane.setOnMouseClicked(event -> addToCart(filteredItems.get(index)));

                if (column == columnCount) {
                    column = 0;
                    row++;
                }

                menuGridPane.add(pane, column++, row);
                GridPane.setMargin(pane, new Insets(6));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }  
    private void filterByItemName(String srcItem) {
        ObservableList<ItemsDataModel> filteredList = FXCollections.observableArrayList();

        if (srcItem == null || srcItem.isEmpty()) {
            filteredList.addAll(menuGetData());
        } else {
            for (ItemsDataModel item : menuGetData()) {
                if (item.getItemsName().toLowerCase().contains(srcItem.toLowerCase())) {
                    filteredList.add(item);
                } else if(item.getCategory().toLowerCase().contains(srcItem.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        // Calculate column count based on the current width
        int columnCount = calculateColumnCount();
        displayFilteredCards(filteredList, columnCount);
    }
    private int calculateColumnCount() {
        double thresholdWidth = 800.0; // Adjust this threshold as needed
        return menuScrollPane.getWidth() > thresholdWidth ? 7 : 4;
    }
    public void filterItemData() {
        
        displayFilteredCards(menuGetData(), 4);
        double thresholdWidth = 1000.0; 
        
        menuScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            int columnCount = (newVal.doubleValue() > thresholdWidth) ? 7 : 4;
            filterByItemName(menu_searchItem.getText()); 
        });
        
        
        menu_searchItem.textProperty().addListener((observable, oldValue, newValue) -> {
            filterByItemName(newValue);
        });
        //mealsBtn.setOnAction(event -> filterByItemName("Meals"));

        mealsBtn.setOnAction(event -> { 
            filterByItemName("Meals");
            menu_searchItem.clear(); 
        });
        drinksBtn.setOnAction(event -> { 
            filterByItemName("Drinks");
            menu_searchItem.clear(); 
        });
        packagesBtn.setOnAction(event -> { 
            filterByItemName("Packages");
            menu_searchItem.clear(); 
        });
        othersBtn.setOnAction(event -> { 
            filterByItemName("Others");
            menu_searchItem.clear(); 
        });
        allBtn.setOnAction(event -> { 
            menu_searchItem.clear(); 
            filterByItemName("");
        });
        menu_srcClearBtn.setOnAction(event -> { 
            menu_searchItem.clear(); 
            filterByItemName("");
        });
        
    }  
    private void setupCartTable() {
        
        menu_Col_SN.setCellValueFactory(new PropertyValueFactory<>("serialNumber"));
        menu_Col_SN.setCellValueFactory(cellData -> {
            int index = menuTableView.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });

        menu_Col_ItemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        menu_Col_ItemRate.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        menu_Col_Qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        menu_Col_Price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        menu_Col_Action.setCellValueFactory(new PropertyValueFactory<>("removeButton"));

        menuTableView.setItems(cartData);
    }
    private void setDynamicColumnWidthForCartTable() {
        menu_Col_SN.maxWidthProperty().bind(menuTableView.widthProperty().multiply(0.6));
        menu_Col_ItemName.maxWidthProperty().bind(menuTableView.widthProperty().multiply(4.4));
        menu_Col_ItemRate.maxWidthProperty().bind(menuTableView.widthProperty().multiply(1.5));
        menu_Col_Qty.maxWidthProperty().bind(menuTableView.widthProperty().multiply(1));
        menu_Col_Price.maxWidthProperty().bind(menuTableView.widthProperty().multiply(1.8));
        menu_Col_Action.maxWidthProperty().bind(menuTableView.widthProperty().multiply(.7));
    }
    public void addToCart(ItemsDataModel cardData) {
        //System.out.println("Clicked addToCart! -> id: "+ cardData.getId());
        setupCartTable();
        
        for (CartItem item : cartData) {
            if (Objects.equals(item.getItems().getId(), cardData.getId())) {
                item.setQuantity(item.getQuantity() + 1);
                menuTableView.refresh();
                updateSubTotal();
                updateTotalQty();
                getTotalDynamically();
                return;
            }
        }
        CartItem newItem = new CartItem(cardData, 1);
        Button removeButton = new Button("X");
        removeButton.setMinHeight(14);
        removeButton.setMaxHeight(14);
        removeButton.setMinWidth(20);
        removeButton.setMaxWidth(20);
        removeButton.setStyle("-fx-font-size: 11px; -fx-padding: 0.0; -fx-text-fill: #fff; -fx-background-color: #eb396b;");
        
        removeButton.setOnAction(e -> removeFromCart(newItem));
        newItem.setRemoveButton(removeButton);
        cartData.add(newItem);
        updateSubTotal();
        updateTotalQty();
        getTotalDynamically();
    }
    private void removeFromCart(CartItem item) {
        cartData.remove(item);
        itemData.remove(item);
        updateSubTotal();
        updateTotalQty();
        getTotalDynamically();
    }
    private void updateSubTotal() {
        double subTotal = cartData.stream().mapToDouble(CartItem::getTotalPrice).sum();
        t_Price = subTotal;
        //menu_SubTotal.setText("৳ "+subTotal+" TK");
        if(t_Price == 0) {
            menu_SubTotal.setText("৳ "+subTotal);
        } else {
            menu_SubTotal.setText("৳ "+subTotal+" TK");
        }
    } 
    private void updateTotalQty() {
        int total_Qty = cartData.stream().mapToInt(CartItem::getQuantity).sum();
        t_Qty = total_Qty;
        totalQty.setText("Total Qty: "+total_Qty);
    }
    public void cartMenuClearBtn() {
        if(cartData.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Cart Empty!");
            alert.showAndWait();
        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Clear Cart Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to CLEAR all cart?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                itemData.clear();
                cartData.clear();
                updateSubTotal();
                updateTotalQty();
                getTotalDynamically();
                t_Price = 0;
                menu_Discount.setText("");
                menu_OthersCharge.setText("");
                menu_ServedBy.setText("");
                menu_Note.setText("");
                menu_OrderType.getSelectionModel().clearSelection();    
            }
        }
        
        //expense_Amount.setText("");
        //expense_Category.getSelectionModel().clearSelection();
        //expense_Date.setValue(null);
        //expense_Date.getEditor().clear();
    } 
    private void getTotalDynamically() {
        // Display the initial total amount
        //menu_Total.setText(String.format("৳ %.2f", t_Price));
        if(t_Price == 0) {
            menu_Total.setText(String.format("৳ %.2f", t_Price));
        } else {
            menu_Total.setText(String.format("৳ %.2f Tk", t_Price));
        }
        
        // Define a method to update the total amount dynamically
        menu_Discount.textProperty().addListener((observable, oldValue, newValue) -> updateTotalAmount());
        menu_OthersCharge.textProperty().addListener((observable, oldValue, newValue) -> updateTotalAmount());
    }
    private void updateTotalAmount() {
        try {
            // Parse discount and other charge values, defaulting to 0 if fields are empty
            double discount = menu_Discount.getText().isEmpty() ? 0 : Double.parseDouble(menu_Discount.getText());
            double otherCharge = menu_OthersCharge.getText().isEmpty() ? 0 : Double.parseDouble(menu_OthersCharge.getText());

            // Calculate discounted amount based on discount percentage
            if(t_Price == 0){
                double discountedAmount = t_Price;
                menu_Total.setText(String.format("৳ %.2f TK", discountedAmount));
                
            } else {
                double discountedAmount = t_Price - discount;
                // Update the label with the new total
                //menu_Total.setText(String.format("৳ %.2f", discountedAmount));
                
                if(otherCharge == 0) {
                    double finalAmount = discountedAmount;
                    menu_Total.setText(String.format("৳ %.2f TK", finalAmount));
                } else {
                    // Add any additional charges
                    double finalAmount = discountedAmount + otherCharge;
                    
                    // Update the label with the new total
                    menu_Total.setText(String.format("৳ %.2f TK", finalAmount));
                }
            } 

        } catch (NumberFormatException e) {
            // Handle invalid input by displaying an error message
            menu_Total.setText("Invalid input");
        }
    }
    private void getMaxId() {
        db.getConnection();
        String sql = "SELECT MAX(id) FROM invoices";
        try {
            prepare = db.connection.prepareStatement(sql);
            result = prepare.executeQuery();
            if (result.next()) {
                xMaxID = result.getInt("MAX(id)");
            }
            if (xMaxID == 0) {
                xMaxID += 1;
            }else {
                xMaxID += 1;
            }
            //System.out.println("Inv ID->: "+xInvID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getXDateTime() {
        long currentTimeInMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");    
        java.sql.Date resultdate = new java.sql.Date(currentTimeInMillis);
        xDate = sdf.format(resultdate.getTime());
        //System.out.println("Time->: "+sdf.format(resultdate.getTime())); 
        //xInvID = (xMaxID+"00"+xDate);
        xInvID = (xDate+"00"+xMaxID);
    }
    private void getItemList() {
        for (CartItem item : cartData) {
            String itemList = String.format("%s, %.2f, %d, %.2f", item.getItemName(), item.getUnitPrice(), item.getQuantity(), item.getTotalPrice());
            itemData.add(itemList);
        }
        //System.out.println("Items->: "+itemData);
    }
    private void getAllTextVal() {
        xDiscount = menu_Discount.getText().isEmpty() ? 0 : Double.parseDouble(menu_Discount.getText());
        xOthersCharge = menu_OthersCharge.getText().isEmpty() ? 0 : Double.parseDouble(menu_OthersCharge.getText());
        xNote = menu_Note.getText();
        xOrderType = menu_OrderType.getSelectionModel().getSelectedItem();
        xServedBy = menu_ServedBy.getText();
        xBillBy = xValue.username.toUpperCase();
        
        if(menu_OrderType.getSelectionModel().getSelectedItem() == null){
            xOrderType = "Table";
        }
        if(menu_ServedBy.getText().isEmpty() || menu_ServedBy.getText() == null){
            xServedBy = "Mr. X";
        }
        if(xBillBy == null){
            xBillBy = "Arko";
        } 
        
        if(xDiscount == 0 && xOthersCharge == 0) {
            xGrandTotal = t_Price;
        } else {
            xGrandTotal = (t_Price - xDiscount) + xOthersCharge;
        }
    }
    public void printAllTextVal() {
        System.out.println("Inv ID->: "+xInvID+"\nItems->: "+itemData+"\nSub Total->: "
                + ""+t_Price+"\nDiscount->: "+xDiscount+"\nOthers Charge->: "+xOthersCharge+"\nGrand Total->: "
                        + ""+xGrandTotal+"\nTotal Qty->: "+t_Qty+"\nNote->: "+xNote+"\nOrder Type->: "+xOrderType+"\nServed By->: "
                                + ""+xServedBy+"\nBill By->: "+xBillBy+"\nDate->: "+System.currentTimeMillis()+
                "\n------------------------------------------------");
    }
    public void processInvoice() {
        getMaxId();
        getXDateTime();
        getItemList();
        getAllTextVal();
        //printAllTextVal();
        
        if(xGrandTotal==0 || cartData.isEmpty()){
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Cart Empty!");
            alert.showAndWait();
            System.out.println("-----> Cart Empty! ----- GrandTotal = 00");
            return;
        }
        saveInvoiceData();
        //getViewInvoiceByInvId(xInvID);
        printInvoiceByInvId(xInvID);
        
    }
    private void saveInvoiceData() {
        try {
            String insertData = "INSERT INTO invoices "
                    + "(inv_id, items, subtotal, discount, others_charge, grand_total, total_qty, "
                    + "note, order_type, served_by, bill_by, payment_status, date) "
                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            prepare = db.connection.prepareStatement(insertData);

            prepare.setString(1, xInvID);
            prepare.setString(2, itemData.toString());
            prepare.setDouble(3, t_Price);
            prepare.setDouble(4, xDiscount);
            prepare.setDouble(5, xOthersCharge);
            prepare.setDouble(6, xGrandTotal);
            prepare.setDouble(7, t_Qty);
            prepare.setString(8, xNote);
            prepare.setString(9, xOrderType);
            prepare.setString(10, xServedBy);
            prepare.setString(11, xBillBy);
            prepare.setString(12, "Pending");
            prepare.setLong(13, System.currentTimeMillis());

            prepare.executeUpdate();

            System.out.println("-> Invoice Data Inserted!.");
            
            orderSummary();
            clearInvData();

        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("-> "+e);
        }
    }
    private void orderSummary() {
        StringBuilder billContent = new StringBuilder();
        for (CartItem item : cartData) {
            billContent.append(item.getItems().getItemsName())
                       .append(" * Rate: ").append(item.getUnitPrice())
                       .append(" [ Qty: ").append(item.getQuantity())
                       .append(" ] = Total: $").append(item.getTotalPrice()).append("\n")
                       .append(" ] = Discount: $").append(item.getTotalPrice()).append("\n");
        }
        billContent.append("\nGrand Total ").append(menu_Total.getText()); 
        
        Alert billAlert = new Alert(Alert.AlertType.INFORMATION);
        billAlert.setTitle("Information Message");
        billAlert.setHeaderText("Order Summary");
        billAlert.setContentText(billContent.toString());
        billAlert.showAndWait();
        
        clearInvData();
    }
    public void clearInvData() {
        itemData.clear();
        cartData.clear();
        updateSubTotal();
        updateTotalQty();
        getTotalDynamically();
        t_Price = 0;
        t_Qty = 0;
        menu_Discount.setText("");
        menu_OthersCharge.setText("");
        menu_ServedBy.setText("");
        menu_Note.setText("");
        menu_OrderType.getSelectionModel().clearSelection();  
    }
    public void printItems() {
        String items = itemData.toString();

        // Remove brackets and split by comma and space
        String[] itemArray = items.replaceAll("[\\[\\]]", "").split(", ");

        // StringBuilder to hold the formatted output
        StringBuilder formattedOutput = new StringBuilder();

        // Loop through the array in chunks of 4 (each product group)
        for (int i = 0; i < itemArray.length; i += 4) {
            // Check if there are exactly 4 elements remaining
            if (i + 4 <= itemArray.length) {
                // Join four elements into a single string for the product group
                String product = String.join(", ", itemArray[i], itemArray[i + 1], itemArray[i + 2], itemArray[i + 3]);
                formattedOutput.append(product).append("\n");
            }
        }

        // Print the result
        System.out.println("--->"+formattedOutput.toString());
    }
    //// END POS MENU SECTION
    
    //// START CULLECT BILL SECTION
    private final ObservableList<InvoiceDataModel> cullectBillListData = FXCollections.observableArrayList();
    public ObservableList<InvoiceDataModel> getCullectBillData() {

        String sql = "SELECT * FROM invoices WHERE payment_status = 'Pending'";

        ObservableList<InvoiceDataModel> listData = FXCollections.observableArrayList();
        db.getConnection();

        try {

            prepare = db.connection.prepareStatement(sql);
            result = prepare.executeQuery();

            InvoiceDataModel invData;
            
            while (result.next()) {

                invData = new InvoiceDataModel(
                        result.getInt("id"),
			result.getString("inv_id"),
                        result.getString("items"),
                        result.getDouble("subtotal"),
                        result.getDouble("discount"),
                        result.getDouble("others_charge"),
                        result.getDouble("grand_total"),
                        result.getInt("total_qty"),
                        result.getString("note"),
                        result.getString("order_type"),
                        result.getString("served_by"),
                        result.getString("bill_by"),
                        result.getString("payment_status"),
                        result.getDate("date"));

                listData.add(invData);

            }
            db.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }
    public void billDisplayCard() { 
        db.getConnection();
        loadTotalPendingAmount();
        cullectBillListData.clear();
        cullectBillListData.addAll(getCullectBillData());
        cullectBillListData.sort((bill1, bill2) -> bill2.getDate().compareTo(bill1.getDate()));

        int row = 0;
        int column = 0;

        cullectBillGridPane.getChildren().clear();
        cullectBillGridPane.getRowConstraints().clear();
        cullectBillGridPane.getColumnConstraints().clear();
        
        for (int q = 0; q < cullectBillListData.size(); q++) {
            try {
                    FXMLLoader load = new FXMLLoader();
                    load.setLocation(getClass().getResource("/view/cullectBill.fxml")); //cullectBill cardBill
                    AnchorPane pane = load.load();
                    CullectBillController cardBill = load.getController();
                    cardBill.setData(cullectBillListData.get(q));
                    
                    // Pass reference of MainFormController to CullectBillController
                    //Add new for manager
                    cardBill.setmanagerFormController(this);
                    
                    // Grid setup
                    if (column == 1) {
                            column = 0;
                            row += 1;
                    }
                    cullectBillGridPane.add(pane, column++, row);
                    GridPane.setMargin(pane, new Insets(0));

            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }
    private void loadTotalPendingAmount() {
        // Check if any label is null before proceeding
        if (totalPendingAmount != null ) {
            totalPendingAmount.setText(String.format("Total Pending Amount: %.2f TK", db.getTotalInvoicePendingAmount()));
            totalPendingAmountInv.setText(String.format("Total Pending Amount: %.2f TK", db.getTotalInvoicePendingAmount()));
        } else {
            System.err.println("One or more labels are not initialized!");
        }
    }
    //// END CULLECT BILL SECTION
   
    
    //// START INVOICE SECTION srcByInvId
    private void loadInvoiceData() {
        // Check if any label is null before proceeding
        if (totalInvoice != null && pendingInvoices != null && completeInvoices != null) {
            totalInvoice.setText(""+db.getTotalInvoice());
            pendingInvoices.setText(""+db.getTotalPendingInvoice());
            completeInvoices.setText(""+db.getTotalCompleteInvoice());
        } else {
            System.err.println("One or more labels are not initialized!");
        }
    }
    private void filterByInvoiceID(String invID) {
        if (invID == null || invID.isEmpty()) {
            invoice_TableView.setItems(invoiceListData); // Show all items if search is empty
        } else {
            ObservableList<InvoiceDataModel> filteredList = FXCollections.observableArrayList();
            for (InvoiceDataModel invoice : invoiceListData) {
                if (invoice.getInvID().contains(invID)) {
                    filteredList.add(invoice);
                }
            }
            invoice_TableView.setItems(filteredList); // Update the TableView with the filtered list
        }
    }
    public void filterInvoiceData() {
        srcByInvId.textProperty().addListener((observable, oldValue, newValue) -> {
            filterByInvoiceID(newValue);
        });
    }
    public ObservableList<InvoiceDataModel> invoiceDataList() {
        ObservableList<InvoiceDataModel> listData = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM invoices ORDER BY id DESC";
        db.getConnection();

        try {

            prepare = db.connection.prepareStatement(sql);
            result = prepare.executeQuery();

            InvoiceDataModel invData;
            
            while (result.next()) {

                invData = new InvoiceDataModel(
                        result.getInt("id"),
			result.getString("inv_id"),
                        result.getString("items"),
                        result.getDouble("subtotal"),
                        result.getDouble("discount"),
                        result.getDouble("others_charge"),
                        result.getDouble("grand_total"),
                        result.getInt("total_qty"),
                        result.getString("note"),
                        result.getString("order_type"),
                        result.getString("served_by"),
                        result.getString("bill_by"),
                        result.getString("payment_status"),
                        result.getDate("date"));

                listData.add(invData);

            } 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<InvoiceDataModel> invoiceListData;
    public void invoiceShowData() {
        loadInvoiceData();
        filterInvoiceData();
        filterByInvoiceID(xInvID);
        invoiceListData = invoiceDataList();
        
        invoice_Col_SN.setCellValueFactory(new PropertyValueFactory<>("id"));
        invoice_Col_SN.setCellValueFactory(cellData -> {
            int index = invoice_TableView.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });

        // Setting up the columns for the TableView
        invoice_Col_InvID.setCellValueFactory(new PropertyValueFactory<>("invID"));
        invoice_Col_GTotal.setCellValueFactory(new PropertyValueFactory<>("grandTotal"));
        invoice_Col_OrderType.setCellValueFactory(new PropertyValueFactory<>("orderType"));
        invoice_Col_ServedBy.setCellValueFactory(new PropertyValueFactory<>("servedBy"));
        invoice_Col_BillBy.setCellValueFactory(new PropertyValueFactory<>("billBy"));
        invoice_Col_PaymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        invoice_Col_Date.setCellValueFactory(new PropertyValueFactory<>("date"));
        //invoice_Col_Action.setCellValueFactory(new PropertyValueFactory<>("date"));
        

        // Check if the action column is already added, to avoid duplicates
        boolean actionColumnExists = false;
        for (TableColumn<InvoiceDataModel, ?> column : invoice_TableView.getColumns()) {
            if (column.getText().equals("Actions")) {
                actionColumnExists = true;
                break;
            }
        }

        if (!actionColumnExists) {
            // Creating an action column for Edit and Delete buttons
            TableColumn<InvoiceDataModel, InvoiceDataModel> invoiceActionCol = new TableColumn<>("Actions");

            // Explicitly specify the generic types instead of using <>
            invoiceActionCol.setCellFactory((final TableColumn<InvoiceDataModel, InvoiceDataModel> param) -> new TableCell<InvoiceDataModel, InvoiceDataModel>() {
                
                private final Button viewButton = new Button("View");
                private final Button printButton = new Button("Print");
                private final HBox actionButtons = new HBox(viewButton, printButton);
                
                {
                    // Style the buttons
                    viewButton.setStyle("-fx-background-color: #00a2ed; -fx-text-fill: white;");
                    printButton.setStyle("-fx-background-color: #5b5b5b; -fx-text-fill: white;");
                    actionButtons.setSpacing(10); // Add spacing between the buttons
                    actionButtons.setAlignment(Pos.CENTER); // Center-align the buttons in the cell
                    
                    // Set up view button action
                    viewButton.setOnAction(event -> {
                        InvoiceDataModel invoice = getTableView().getItems().get(getIndex());
                        int id = invoice.getId();
                        getViewInvoice(id);
                        
                    });
                    
                    // Set up print button action
                    printButton.setOnAction(event -> {
                        
                        InvoiceDataModel invoice = getTableView().getItems().get(getIndex());
                        int id = invoice.getId();
                        String invID = invoice.getInvID();
                        //getViewInvoice(id);
                        Platform.runLater(() -> {
                            printInvoice(id,invID);
                        });
                        
                    });

                    
                }
                
                @Override
                protected void updateItem(InvoiceDataModel item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null); // If the row is empty, remove the buttons
                    } else {
                        setGraphic(actionButtons); // Otherwise, show the buttons
                    }
                }
            });

            // Add the action column to the table only once
            invoice_TableView.getColumns().add(invoiceActionCol);
            invoiceActionCol.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(1.8));
        }
        
        // Set the data for the TableView
        invoice_TableView.setItems(invoiceListData);
    }
    private void setDynamicColumnWidthForInvoiceTable() {
        invoice_Col_SN.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(0.6));
        invoice_Col_InvID.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(1.35));
        invoice_Col_GTotal.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(1.2));
        invoice_Col_OrderType.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(0.65));
        invoice_Col_ServedBy.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(1.3));
        invoice_Col_BillBy.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(1.3));
        invoice_Col_PaymentStatus.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(1));
        invoice_Col_Date.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(0.8));
        //invoice_Col_Action.maxWidthProperty().bind(invoice_TableView.widthProperty().multiply(1.8));
    }

    public void getViewInvoice(int getById) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/invoice.fxml"));
            Parent root = loader.load();
            InvoiceController invoiceController = loader.getController();
            invoiceController.setGetById(getById);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("View Invoice");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);

            stage.setMinWidth(405);
            stage.setMaxWidth(405);
            stage.setMinHeight(818);
            stage.setMaxHeight(818);
            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading invoice.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void getViewInvoiceByInvId(String getByInvId) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/invoice.fxml"));
            Parent root = loader.load();
            InvoiceController invoiceController = loader.getController();
            invoiceController.setGetByInvId(getByInvId);
            
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("View Invoice");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);

            stage.setMinWidth(405);
            stage.setMaxWidth(405);
            stage.setMinHeight(818);
            stage.setMaxHeight(818);
            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading invoice.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void printInvoiceXok(int id, String invID) {
        try {
            // Load the FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/invoice.fxml"));
            Parent root = loader.load();

            // Get the controller and populate the data
            InvoiceController invoiceController = loader.getController();
            invoiceController.setGetById(id); // Replace with the required invoice ID

            // Create a temporary scene to render the root
            Scene tempScene = new Scene(root);

            // Scale the layout to fit the desired page size (405x818)
            root.setScaleX(405.0 / root.prefWidth(-1));  // Fit the width
            root.setScaleY(818.0 / root.prefHeight(-1)); // Fit the height
            root.layout(); // Ensure the layout is applied

            // Take a high-quality snapshot
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setTransform(javafx.scene.transform.Transform.scale(3.0, 3.0)); // Increase DPI
            WritableImage snapshot = root.snapshot(snapshotParameters, null);

            // Save the snapshot to the user's Desktop in the GoPpo folder
            String userHome = System.getProperty("user.home"); // Get the user's home directory
            File goPpoFolder = new File(Paths.get(userHome, "Desktop", "GoPpo MS").toString());

            // Create the folder if it doesn't exist
            if (!goPpoFolder.exists()) {
                boolean dirCreated = goPpoFolder.mkdirs();
                if (dirCreated) {
                    System.out.println("Folder created: " + goPpoFolder.getAbsolutePath());
                } else {
                    System.out.println("Failed to create folder: " + goPpoFolder.getAbsolutePath());
                }
            }

            // Define the output PDF file
            File outputFile = new File(goPpoFolder, "invoice_" + invID + ".pdf");

            // Convert the snapshot to a BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

            // Create a PDF document
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle((float) bufferedImage.getWidth(), (float) bufferedImage.getHeight()));
                document.addPage(page);

                PDImageXObject image = LosslessFactory.createFromImage(document, bufferedImage);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
                    contentStream.drawImage(image, 0, 0, (float) bufferedImage.getWidth(), (float) bufferedImage.getHeight());
                }

                // Save the PDF to the output file
                document.save(outputFile);
                System.out.println("PDF saved as: " + outputFile.getAbsolutePath());

                // Alert user about successful save
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Invoice saved successfully.\nPDF saved in your GoPpo MS folder.");
                alert.showAndWait();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading invoice.fxml: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    public void printInvoice(int id, String invID) {
        try {
            // Load the FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/invoice.fxml"));
            Parent root = loader.load();

            // Get the controller and populate the data
            InvoiceController invoiceController = loader.getController();
            invoiceController.setGetById(id); // Replace with the required invoice ID

            // Create a temporary scene to render the root
            Scene tempScene = new Scene(root);

            // Scale the layout to fit the desired page size (405x818)
            root.setScaleX(405.0 / root.prefWidth(-1));  // Fit the width
            root.setScaleY(818.0 / root.prefHeight(-1)); // Fit the height
            root.layout(); // Ensure the layout is applied

            // Take a high-quality snapshot
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setTransform(javafx.scene.transform.Transform.scale(3.0, 3.0)); // Increase DPI
            WritableImage snapshot = root.snapshot(snapshotParameters, null);

            // Save the snapshot to the user's Desktop in the GoPpo folder
            String userHome = System.getProperty("user.home"); // Get the user's home directory
            File goPpoFolder = new File(Paths.get(userHome, "Desktop", "GoPpo MS").toString());

            // Create the folder if it doesn't exist
            if (!goPpoFolder.exists()) {
                boolean dirCreated = goPpoFolder.mkdirs();
                if (dirCreated) {
                    System.out.println("Folder created: " + goPpoFolder.getAbsolutePath());
                } else {
                    System.out.println("Failed to create folder: " + goPpoFolder.getAbsolutePath());
                }
            }

            // Define the output PDF file
            File outputFile = new File(goPpoFolder, "invoice_" + invID + ".pdf");

            // Convert the snapshot to a BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

            // Create a PDF document
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle((float) bufferedImage.getWidth(), (float) bufferedImage.getHeight()));
                document.addPage(page);

                PDImageXObject image = LosslessFactory.createFromImage(document, bufferedImage);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
                    contentStream.drawImage(image, 0, 0, (float) bufferedImage.getWidth(), (float) bufferedImage.getHeight());
                }

                // Save the PDF to the output file
                document.save(outputFile);
                System.out.println("PDF saved as: " + outputFile.getAbsolutePath());

                // Send the PDF to the default printer
                PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
                if (defaultPrinter != null) {
                    PDDocument printDocument = PDDocument.load(outputFile);
                    PrinterJob printerJob = PrinterJob.getPrinterJob();
                    printerJob.setPrintService(defaultPrinter);
                    PDFPageable pageable = new PDFPageable(printDocument);
                    printerJob.setPageable(pageable);

                    if (printerJob.printDialog()) {
                        printerJob.print();
                        System.out.println("Invoice printed successfully.");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Invoice printed successfully.\nPDF saved in your Desktop/GoPpo MS folder!.");
                        alert.showAndWait();
                    } else {
                        System.out.println("Print job canceled by the user.");
                    }

                    printDocument.close();
                } else {
                    System.out.println("No default printer configured.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("No default printer configured.");
                    alert.showAndWait();
                }

            } catch (IOException e) {
                System.out.println("Error generating PDF: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading invoice.fxml: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
    public void printInvoiceByInvId(String invID) {
        try {
            // Load the FXML layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/invoice.fxml"));
            Parent root = loader.load();

            // Get the controller and populate the data
            InvoiceController invoiceController = loader.getController();
            invoiceController.setGetByInvId(invID); // Replace with the required invoice ID

            // Create a temporary scene to render the root
            Scene tempScene = new Scene(root);

            // Scale the layout to fit the desired page size (405x818)
            root.setScaleX(405.0 / root.prefWidth(-1));  // Fit the width
            root.setScaleY(818.0 / root.prefHeight(-1)); // Fit the height
            root.layout(); // Ensure the layout is applied

            // Take a high-quality snapshot
            SnapshotParameters snapshotParameters = new SnapshotParameters();
            snapshotParameters.setTransform(javafx.scene.transform.Transform.scale(3.0, 3.0)); // Increase DPI
            WritableImage snapshot = root.snapshot(snapshotParameters, null);

            // Save the snapshot to the user's Desktop in the GoPpo folder
            String userHome = System.getProperty("user.home"); // Get the user's home directory
            File goPpoFolder = new File(Paths.get(userHome, "Desktop", "GoPpo MS").toString());

            // Create the folder if it doesn't exist
            if (!goPpoFolder.exists()) {
                boolean dirCreated = goPpoFolder.mkdirs();
                if (dirCreated) {
                    System.out.println("Folder created: " + goPpoFolder.getAbsolutePath());
                } else {
                    System.out.println("Failed to create folder: " + goPpoFolder.getAbsolutePath());
                }
            }

            // Define the output PDF file
            File outputFile = new File(goPpoFolder, "invoice_" + invID + ".pdf");

            // Convert the snapshot to a BufferedImage
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);

            // Create a PDF document
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(new PDRectangle((float) bufferedImage.getWidth(), (float) bufferedImage.getHeight()));
                document.addPage(page);

                PDImageXObject image = LosslessFactory.createFromImage(document, bufferedImage);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
                    contentStream.drawImage(image, 0, 0, (float) bufferedImage.getWidth(), (float) bufferedImage.getHeight());
                }

                // Save the PDF to the output file
                document.save(outputFile);
                System.out.println("PDF saved as: " + outputFile.getAbsolutePath());

                // Send the PDF to the default printer
                PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
                if (defaultPrinter != null) {
                    PDDocument printDocument = PDDocument.load(outputFile);
                    PrinterJob printerJob = PrinterJob.getPrinterJob();
                    printerJob.setPrintService(defaultPrinter);
                    PDFPageable pageable = new PDFPageable(printDocument);
                    printerJob.setPageable(pageable);

                    if (printerJob.printDialog()) {
                        printerJob.print();
                        System.out.println("Invoice printed successfully.");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Invoice printed successfully.\nPDF saved in your Desktop/GoPpo MS folder!.");
                        alert.showAndWait();
                    } else {
                        System.out.println("Print job canceled by the user.");
                    }

                    printDocument.close();
                } else {
                    System.out.println("No default printer configured.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("No default printer configured.");
                    alert.showAndWait();
                }

            } catch (IOException e) {
                System.out.println("Error generating PDF: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading invoice.fxml: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }


    //// END INVOICE SECTION
    
    
    //// START EXPENSE SECTION
    private final String[] expenseCategoryList = {
        "Rent/Mortgage","Living Cost","Electricity Bill","Water Bill","Emplyee Salery","Buy Goods","Cleaning Supplies","Safety Equipment",
        "Advertising","Maintenance","Furniture","Technology","Donations","Insurance","Vat/Taxes","Licenses & Permit","Others"};
    public void expenseCategoryList() {

        List<String> xVal = new ArrayList<>();

        for (String data : expenseCategoryList) {
            xVal.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(xVal);
        expense_Category.setItems(listData);
    }
    private void expenseInsertQry() {
        try {
            String insertData = "INSERT INTO expenses "
                        + "(ex_amount, ex_category, ex_description, ex_by, ex_date) "
                        + "VALUES(?,?,?,?,?)";

            prepare = db.connection.prepareStatement(insertData);

            prepare.setString(1, expense_Amount.getText());
            prepare.setString(2, (String) expense_Category.getSelectionModel().getSelectedItem());
            prepare.setString(3, expense_Discription.getText());
            prepare.setString(4, expense_By.getText());
            // Convert LocalDate to java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(expense_Date.getValue());
            // Use setDate() instead of setLong()
            prepare.setDate(5, sqlDate);
            //prepare.setLong(5, System.currentTimeMillis());

            prepare.executeUpdate();

            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Successfully Added!");
            alert.showAndWait();

            System.out.println("-> Expense Data Inserted!");
            expenseShowData();
            loadExpenseData();
            expenseClearBtn();
            
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("-> "+e);
        }
    }
    public void expenseAddBtn() {
        db.getConnection();

        String amountText = expense_Amount.getText();
        double expenseAmount = 0.0;
        
        if (expense_Amount.getText().isEmpty()
                || expense_Category.getSelectionModel().getSelectedItem() == null
                || expense_Discription.getText().isEmpty()
                || expense_By.getText().isEmpty()
                || expense_Date.getValue() == null ) {

            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        } else if (amountText != null) {
            
            try {
                expenseAmount = Double.parseDouble(amountText);
                if (expenseAmount > 0) {
                    expenseInsertQry();
                } else {
                    // Action to handle 0 or negative values
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Amount should be greater than 0.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid number
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid 'Amount' number format: "+ amountText);
                alert.showAndWait();
            }
            
        } else {
            expenseInsertQry();
        }
        
    }
    public void updateExpense(int exp_id, double exp_amount, String exp_category, String exp_discription, String exp_by, Date exp_date) {
        db.getConnection();
        
        //java.sql.Date sqlDate = java.sql.Date.valueOf(exp_date.getDaateVal());
        //long millis = sqlDate.getTime();
        
        long millis = exp_date.getTime();
        //System.out.println("-> Expense Data Value:" + millis);

        String updateData = "UPDATE expenses SET "
                    + "ex_amount = '" + exp_amount + "', "
                    + "ex_category = '" + exp_category + "', "
                    + "ex_description = '" + exp_discription + "', "
                    + "ex_by = '" + exp_by + "', "
                    + "ex_date = '" + millis + "' WHERE id = " + 
                    exp_id;
        
        try {

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE an Expense Item?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                prepare = db.connection.prepareStatement(updateData);
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                System.out.println("-> Expense Data Updated!");
                // TO UPDATE YOUR TABLE VIEW
                expenseShowData();
                loadExpenseData();
                // TO CLEAR YOUR FIELDS
                expenseClearBtn();
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("-> "+e);
        }
    }
    public void expenseClearBtn() {

        expense_Amount.setText("");
        expense_Category.getSelectionModel().clearSelection();
        expense_Discription.setText("");
        expense_By.setText("");
        expense_Date.setValue(null);
        //expense_Date.getEditor().clear();
    }   
    public void deleteExpense(int id) {
    // Confirmation alert before deletion
    alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Message");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to DELETE this Expense Item?");
    Optional<ButtonType> option = alert.showAndWait();

    // If the user confirms the deletion
    if (option.isPresent() && option.get().equals(ButtonType.OK)) {
        String deleteData = "DELETE FROM expenses WHERE id = " + id;
        try {
            // Execute the delete query
            prepare = db.connection.prepareStatement(deleteData);
            prepare.executeUpdate();

            // Show success alert
            alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success Message");
            alert.setHeaderText(null);
            alert.setContentText("Expense successfully deleted!");
            alert.showAndWait();

            System.out.println("-> Expense Data Deleted!");
            
            // Refresh the table and clear fields
            expenseShowData();
            loadExpenseData();
            expenseClearBtn();


        } catch (Exception e) {
            e.printStackTrace();
        }
    } else {
        // Show cancellation alert
//        alert = new Alert(AlertType.ERROR);
//        alert.setTitle("Cancellation Message");
//        alert.setHeaderText(null);
//        alert.setContentText("Deletion cancelled.");
//        alert.showAndWait();
    }
}
    public ObservableList<ExpenseDataModel> expenseDataList() {
        ObservableList<ExpenseDataModel> listData = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM expenses ORDER BY id DESC";
        db.getConnection();

        try {

            prepare = db.connection.prepareStatement(sql);
            result = prepare.executeQuery();

            ExpenseDataModel expData;
            
            while (result.next()) {

                expData = new ExpenseDataModel(
                        result.getInt("id"),
			result.getDouble("ex_amount"),
                        result.getString("ex_category"),
                        result.getString("ex_description"),
                        result.getString("ex_by"),
                        result.getDate("ex_date"));

                listData.add(expData);

            } 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<ExpenseDataModel> expenseListData;
    public void expenseShowData() {
        expenseListData = expenseDataList();
        
        expense_Col_Sn.setCellValueFactory(new PropertyValueFactory<>("id"));
        expense_Col_Sn.setCellValueFactory(cellData -> {
            int index = expense_TableView.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });

        // Setting up the columns for the TableView
        expense_Col_Amount.setCellValueFactory(new PropertyValueFactory<>("exAmount"));
        expense_Col_Category.setCellValueFactory(new PropertyValueFactory<>("exCategory"));
        expense_Col_Description.setCellValueFactory(new PropertyValueFactory<>("exDescription"));
        expense_Col_ExpensedBy.setCellValueFactory(new PropertyValueFactory<>("exBy"));
        expense_Col_Date.setCellValueFactory(new PropertyValueFactory<>("exDate"));

        // Set the data for the TableView
        expense_TableView.setItems(expenseListData);
    }
    private void setDynamicColumnWidthForExp() {
        expense_Col_Sn.maxWidthProperty().bind(expense_TableView.widthProperty().multiply(0.6));
        expense_Col_Amount.maxWidthProperty().bind(expense_TableView.widthProperty().multiply(1));
        expense_Col_Category.maxWidthProperty().bind(expense_TableView.widthProperty().multiply(1.6));
        expense_Col_Description.maxWidthProperty().bind(expense_TableView.widthProperty().multiply(3));
        expense_Col_ExpensedBy.maxWidthProperty().bind(expense_TableView.widthProperty().multiply(1.3));
        expense_Col_Date.maxWidthProperty().bind(expense_TableView.widthProperty().multiply(1));
    }
    private void loadExpenseData() {
        // Check if any label is null before proceeding
        if (todayExpense != null && yesterdayExpense != null && thisweekExpense != null && 
            thismonthExpense != null && thisyearExpense != null && totalExpense != null) {
            
            todayExpense.setText(String.format("%.2f TK", db.getTodayExpenses()));
            yesterdayExpense.setText(String.format("%.2f TK", db.getYesterdayExpenses()));
            thisweekExpense.setText(String.format("%.2f TK", db.getThisWeekExpenses()));
            thismonthExpense.setText(String.format("%.2f TK", db.getThisMonthExpenses()));
            thisyearExpense.setText(String.format("%.2f TK", db.getThisYearExpenses()));
            totalExpense.setText(String.format("%.2f TK", db.getTotalExpenses()));
        } else {
            System.err.println("One or more labels are not initialized!");
        }
    }
    public void getExpSpecificBtn() {
        onDateSelected();
    }
    public void onDateSelected() {
        LocalDate selectedDate = expenseDatePicker.getValue();
        if (selectedDate != null) {
            // Convert LocalDate to Date in milliseconds
            Date date = Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            long millis = date.getTime(); // Convert to milliseconds

            // Fetch and display expense for the selected date
            double expenseForSelectedDate = db.getExpensesForDate(millis);
            selectedDateExpense.setText(String.format("%.2f TK", expenseForSelectedDate));
        }
    }
    public void getExpDateRangeBtn() {
        onDateRangeSelected();
    }
    public void onDateRangeSelected() {
        LocalDate startDate = startExpDatePicker.getValue();
        LocalDate endDate = endExpDatePicker.getValue();
        
        if (startDate == null || endDate == null) {
            if (startDate == null) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Message");
            alert.setHeaderText(null);
            alert.setContentText("From date is Empty!");
            alert.showAndWait();
            } else{
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("Warning Message");
                alert.setHeaderText(null);
                alert.setContentText("To date is Empty!");
                alert.showAndWait();
            }
            return;  
        } 

        if (startDate != null && endDate != null) {
            // Validation: Check if the start date is after the end date
            if (startDate.isAfter(endDate)) {
                dateRangeExpense.setText("Invalid Date Range!");
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Date Range\nplease select 'From date' to 'To Date' correctly and get data.");
                alert.showAndWait();
                return;
            }

            // Convert LocalDate to milliseconds
            long startMillis = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime();
            long endMillis = Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).getTime() 
                             + (24 * 60 * 60 * 1000) - 1;  // Set the end time to the end of the day

            // Fetch and display expense for the selected date range
            double expenseForDateRange = db.getExpensesForDateRange(startMillis, endMillis);
            dateRangeExpense.setText(String.format("%.2f TK", expenseForDateRange));
        }
        
    }
    public void getExp_ClearBtn() {
        selectedDateExpense.setText("0.0");
        expenseDatePicker.setValue(null);
        dateRangeExpense.setText("0.0");
        startExpDatePicker.setValue(null);
        endExpDatePicker.setValue(null);
    } 
    public void getExpDateRangeValidation() {    
        // Disable previous dates
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                
                // Disable all past dates
                if (item.isBefore(startExpDatePicker.getValue())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE;"); // Optional: Grey out past dates
                }
            }
        };
        
        endExpDatePicker.setDayCellFactory(dayCellFactory);
    }
    public void getExpDateValidation() {    
        // Disable previous dates
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                
                // Disable all past dates
                if (item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE;");
                }
            }
        };
        
        expense_Date.setDayCellFactory(dayCellFactory);
    }
    public void getExpDateValidationForUpdate(DatePicker dateValue) {    
        // Disable previous dates
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                
                // Disable all past dates
                if (item.isAfter(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #EEEEEE;");
                }
            }
        };
        
        dateValue.setDayCellFactory(dayCellFactory);
    }
    /// END EXPENSE SECTION/
    
    
    //// START USERS SECTION
    public void loadAdminData(int adminId) {
    UserDataModel user = db.getAdminUserData(adminId);

    if (user != null) {
        userDisplayName.setText(user.getDisplayName());
        adminUserName.setText(user.getUserName());
        userRole.setText(user.getUserRole());
        userStatus.setText(user.getStatus());
        activeQuestion.setText(user.getQuestion());
        userDate.setText(user.getDate().toString()); // Format the date as needed
        // Load image (if stored as a file path)
        Image userImg = new Image("file:" + user.getImage());
        userImage.setImage(userImg);
        //System.out.println("-------> : "+ user.getImage());
        }
    }
    private final String[] empUserRoleList = {"Manager","Cashier"};
    public void empUserRoleList() {

        List<String> xVal = new ArrayList<>();

        for (String data : empUserRoleList) {
            xVal.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(xVal);
        emp_user_role.setItems(listData);
    }
    private final String[] empUserStatusList = {"Active","Deactive"};
    public void empUserStatusList() {

        List<String> xVal = new ArrayList<>();

        for (String data : empUserStatusList) {
            xVal.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(xVal);
        emp_user_status.setItems(listData);
    }
    public ObservableList<EmployeeDataModel> empUserDataList() {
        ObservableList<EmployeeDataModel> listData = FXCollections.observableArrayList();
        
        String sql = "SELECT * FROM employees";
        db.getConnection();

        try {

            prepare = db.connection.prepareStatement(sql);
            result = prepare.executeQuery();

            EmployeeDataModel empData;
            
            while (result.next()) {

                empData = new EmployeeDataModel(
                        result.getInt("id"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("user_role"),
                        result.getString("status"),
                        result.getDate("date"));

                listData.add(empData);
            } 
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }
    private ObservableList<EmployeeDataModel> empUserListData;
    public void empUserShowData() {
        empUserListData = empUserDataList();

        emp_Col_Sn.setCellValueFactory(new PropertyValueFactory<>("id"));
        emp_Col_Sn.setCellValueFactory(cellData -> {
            int index = empUser_TableView.getItems().indexOf(cellData.getValue()) + 1;
            return new SimpleStringProperty(String.valueOf(index));
        });
        // Setting up the columns for the TableView
        emp_Col_UserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        emp_Col_Password.setCellValueFactory(new PropertyValueFactory<>("password"));
        emp_Col_UserRole.setCellValueFactory(new PropertyValueFactory<>("userRole"));
        emp_Col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        emp_Col_Date.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Custom cell factory for the password column to show/hide password
        emp_Col_Password.setCellFactory(column -> new TableCell<EmployeeDataModel, String>() {
            private final Button showHideBtn = new Button("Show");
            private boolean isPasswordVisible = false;

            @Override
            protected void updateItem(String password, boolean empty) {
                super.updateItem(password, empty);

                if (empty || password == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    if (isPasswordVisible) {
                        setText(password); // Show the plain password
                        showHideBtn.setText("Hide");
                    } else {
                        setText(maskPassword(password)); // Show masked password
                        showHideBtn.setText("Show");
                    }

                    // Style the button: height 20, grey background
                    showHideBtn.setStyle("-fx-background-color: #e4ae61; -fx-pref-height: 20px; -fx-font-size: 10px;");

                    // Toggle the password visibility when the button is clicked
                    showHideBtn.setOnAction(e -> {
                        isPasswordVisible = !isPasswordVisible;
                        updateItem(password, false);
                    });

                    setGraphic(showHideBtn); // Add the button to the cell
                }
            }

            // Method to mask the password (replace with asterisks)
            private String maskPassword(String password) {
                StringBuilder maskedPassword = new StringBuilder();
                for (int i = 0; i < password.length(); i++) {
                    maskedPassword.append('*');  // You can use any character, like '•'
                }
                return maskedPassword.toString();
            }
        });

        // Set the list of items for the TableView
        empUser_TableView.setItems(empUserListData);
    }
    public void showPass() {
        // Listener for the show/hide password CheckBox
        showPassCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                check_pass = true;
                // Show password in plain text
                emp_passPlainText.setText(emp_password.getText()); // Display current text
                emp_passPlainText.setVisible(true); // Show plain text field
                emp_password.setVisible(false); // Hide PasswordField
            } else {
                check_pass = false;
                // Hide password
                emp_password.setText(emp_passPlainText.getText()); // Set back to masked field
                emp_password.setVisible(true); // Show PasswordField
                emp_passPlainText.setVisible(false); // Hide plain text field
            }
        });
    }  
    public void empSelectData() {
        EmployeeDataModel empData = empUser_TableView.getSelectionModel().getSelectedItem();
        int num = empUser_TableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        emp_id = empData.getId();
        emp_date = empData.getDate();
        // Populate the fields with selected data
        emp_username.setText(empData.getUserName());
        emp_password.setText(empData.getPassword()); // Set the password (masked)
        emp_user_role.setValue(empData.getUserRole());
        emp_user_status.setValue(empData.getStatus());

        // Reset CheckBox state to keep password hidden when selecting new employee
        showPassCheckBox.setSelected(false);
        emp_passPlainText.setVisible(false); // Hide plain text field initially
    }    
    public void empSelectData1() {
    EmployeeDataModel empData = empUser_TableView.getSelectionModel().getSelectedItem();
    int num = empUser_TableView.getSelectionModel().getSelectedIndex();

    if ((num - 1) < -1) {
        return;
    }

        id = empData.getId();
        emp_username.setText(empData.getUserName());
        emp_password.setText(empData.getPassword()); // Initially mask the password
        emp_user_role.setValue(empData.getUserRole());
        emp_user_status.setValue(empData.getStatus());
        getEmpDate = String.valueOf(empData.getDate());
    }
    public void empRegBtn() {
        db.getConnection();
        
        String chkusername = emp_username.getText();
        
        if (emp_username.getText().isEmpty() 
            || emp_password.getText().isEmpty()
            || emp_user_role.getSelectionModel().getSelectedItem() == null) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();

        }
        if(!chkusername.matches("^[\\w]+$")) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username: "+chkusername+"\nEnsures only word characters (letters, digits, and underscores) with no spaces");
            alert.showAndWait();
        }
        
        else{
            
            String regData = "INSERT INTO employees (username, password, user_role, status, date) VALUES (?, ?, ?, ?, ?)";

            try {

                // CHECK IF THE USERNAME IS ALREADY RECORDED IN employees Table
                String checkUsername = "SELECT username FROM employees WHERE username = '"
                        + emp_username.getText() + "'";

                prepare = db.connection.prepareStatement(checkUsername);
                result = prepare.executeQuery();
                
                // CHECK IF THE USERNAME IS ALREADY RECORDED In users Table
                String checkUsernameE = "SELECT username FROM admin WHERE username = '"
                        + emp_username.getText() + "'";

                prepare = db.connection.prepareStatement(checkUsernameE);
                ResultSet rs = prepare.executeQuery();


                if (result.next() || rs.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("'"+emp_username.getText()+"'" + " is already taken, now change username and try again!");
                    alert.showAndWait();
                } else if (emp_password.getText().length() < 6) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Password, atleast 6 characters are needed");
                    alert.showAndWait();
                } else {
                    prepare = db.connection.prepareStatement(regData);

                    prepare.setString(1, emp_username.getText());
                    prepare.setString(2, emp_password.getText());
                    prepare.setString(3, (String) emp_user_role.getSelectionModel().getSelectedItem());
                    prepare.setString(4, "Deactive");
                    prepare.setLong(5, System.currentTimeMillis());
    //                Date date = new Date();
    //                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
    //                prepare.setString(7, String.valueOf(sqlDate));

                    prepare.executeUpdate();
                    System.out.println("-> Data Inserted!");

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully registered Account!");
                    alert.showAndWait();
                    
                    empUserShowData();
                    showPass();
                    empClearBtn();
                }

            } catch (SQLException e) {
                System.out.println("-> "+e.toString());
            }
        
        }
        

    }
    private void empUpdateQry() {
        long millis = emp_date.getTime();
        String pass1 = emp_password.getText();
        String pass2 = emp_passPlainText.getText();
        if(check_pass == true){
            emp_pass = pass2;
        }
        else {
            emp_pass = pass1;
        }
        
        String updateData = "UPDATE employees SET "
                    + "username = '" + emp_username.getText() + "', "
                    + "password = '" + emp_pass + "', "
                    + "user_role = '" + emp_user_role.getSelectionModel().getSelectedItem() + "', "
                    + "status = '" + emp_user_status.getSelectionModel().getSelectedItem() + "', "
                    + "date = '" + millis + "' WHERE id = " + 
                    empUser_TableView.getSelectionModel().getSelectedItem().getId();
        
        try {

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE User Name: " + emp_username.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                prepare = db.connection.prepareStatement(updateData);
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                System.out.println("-> Item Data Updated!");
                // TO UPDATE YOUR TABLE VIEW
                empUserShowData();
                // TO CLEAR YOUR FIELDS
                empClearBtn();
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("-> "+e);
        }
    }
    public void empUpdateBtn() {
        db.getConnection();
        
        if (emp_username.getText().isEmpty() 
            || emp_password.getText().isEmpty()
            || emp_user_role.getSelectionModel().getSelectedItem() == null) {

            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Select User");
            alert.showAndWait();

        } else if(emp_username.getText() == null ? 
                empUser_TableView.getSelectionModel()
                        .getSelectedItem().getUserName() == null : 
                emp_username.getText().equals(empUser_TableView.getSelectionModel()
                        .getSelectedItem().getUserName())) {
            empUpdateQry();
            //System.out.println("-> Update Done!");
            
        } else {

            // CHECK ITEMS CODE
            String checkUsername = "SELECT username FROM employees WHERE username = '"
            + emp_username.getText() + "'";

            try {
                prepare = db.connection.prepareStatement(checkUsername);
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("'"+emp_username.getText()+"'" + " is already taken, change and try again.");
                    alert.showAndWait();
                } else {
                    empUpdateQry();
                    //System.out.println("-> Update Done!");
                }

            } catch (Exception e) {
                e.printStackTrace();
                //System.out.println("-> "+e);
            }

        }
    }
    public void empDeleteBtn() {
        if (emp_id == 0) {

            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Message");
            alert.setHeaderText(null);
            alert.setContentText("Please Select User");
            alert.showAndWait();

        } else {
            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to DELETE User: " + emp_username.getText() + "?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                String deleteData = "DELETE FROM employees WHERE id = " + emp_id;
                try {
                    prepare = db.connection.prepareStatement(deleteData);
                    prepare.executeUpdate();

                    alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success Message");
                    alert.setHeaderText(null);
                    alert.setContentText("successfully Deleted!");
                    alert.showAndWait();

                    System.out.println("-> User Deleted!");
                    // TO UPDATE YOUR TABLE VIEW
                    empUserShowData();
                    // TO CLEAR YOUR FIELDS
                    empClearBtn();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                emp_id = 0;
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled");
                alert.showAndWait();
            }
        }
    }
    public void empClearBtn() {
        emp_username.setText("");
        emp_password.setText("");
        emp_passPlainText.setText("");
        emp_user_role.getSelectionModel().clearSelection();
        emp_user_status.getSelectionModel().clearSelection();
        getEmpDate = "";
        emp_id = 0;
        id = 0;
    }
    public void editProfileFull() {
        db.getConnection();
        UserDataModel user = db.getAdminUserData(1);
        
        // Set up edit button action
        editProfileBtn.setOnAction(event -> {

            // Create a new Stage (window) for editing
            Stage editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL); // Make the stage modal
            editStage.setTitle("Edit Profile");
            editStage.initStyle(StageStyle.UTILITY); // Make the window undecorated

            // Create a layout for the new window
            VBox editLayout = new VBox(6);
            editLayout.setPadding(new Insets(6));

            // Create fields for User Name, Display Name , Image and others
            //Label userName = new Label(user.getUserName().toString()); 
 
            //TextField userNameField = new TextField(user.getUserName().toString()); 
            HBox nameBox = new HBox();
            nameBox.setPadding(new Insets(5));  // Add padding for visibility
            TextField userNameField = new TextField(user.getUserName());
            userNameField.setEditable(false);
            userNameField.setMinHeight(30);
            nameBox.getChildren().add(userNameField);
            // Apply background and text color to be visible
            userNameField.setStyle("-fx-background-color: lightgray; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
    
            TextField displayNameField = new TextField(user.getDisplayName().toString()); 
            TextField profilePhotoField = new TextField(user.getImage().toString()); 
            
            // Set preferred width and height for the TextFields
            displayNameField.setMinHeight(30);
            displayNameField.setStyle("-fx-font-size: 14px;"); 
            
            profilePhotoField.setMinHeight(30);
            profilePhotoField.setMinWidth(240);

            HBox userRoleBox = new HBox();
            userRoleBox.setPadding(new Insets(5));  // Add padding for visibility
            TextField userRoleField = new TextField(user.getUserRole());
            userRoleField.setEditable(false);
            userRoleField.setMinHeight(30);
            userRoleBox.getChildren().add(userRoleField);
            // Apply background and text color to be visible
            userRoleField.setStyle("-fx-background-color: lightgray; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
            
            HBox statusBox = new HBox();
            statusBox.setPadding(new Insets(5));  // Add padding for visibility
            TextField statusField = new TextField(user.getStatus());
            statusField.setEditable(false);
            statusField.setMinHeight(30);
            statusBox.getChildren().add(statusField);
            // Apply background and text color to be visible
            statusField.setStyle("-fx-background-color: lightgray; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
           
            HBox dateBox = new HBox();
            dateBox.setPadding(new Insets(5));  // Add padding for visibility
            TextField dateField = new TextField(user.getDate().toString());
            dateField.setEditable(false);
            dateField.setMinHeight(30);
            dateBox.getChildren().add(dateField);
            // Apply background and text color to be visible
            dateField.setStyle("-fx-background-color: lightgray; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
            
            
            // Add fields to the layout
            editLayout.getChildren().addAll( 
                new Label("User Name: *(Not Editable)"), userNameField,
                new Label("Display Name:"), displayNameField,
                new Label("Profile Photo:"), profilePhotoField,
                new Label("User Role: *(Not Editable)"), userRoleField,
                new Label("Status: *(Not Editable)"), statusField,
                new Label("Creation Date: *(Not Editable)"), dateField
            );

            // Create an Update button to confirm edits
            Button updateButton = new Button("Update");
            // Set button styling
            updateButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");

            updateButton.setOnAction(updateEvent -> {

                // Get the values from the input fields
                String displayNameText = displayNameField.getText();
                String profilePhotoText = profilePhotoField.getText();

                // Check if any fields are empty
                if (displayNameText.isEmpty()
                        ||profilePhotoText.isEmpty() ) {
                    System.out.println("-> User Data Empty!");

                    // Show alert if any field is empty
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all blank fields");
                    alert.showAndWait();

                } else {
                    // Proceed with the update query since all validations passed
                    updateProfile(displayNameText, profilePhotoText);
                    displayNameField.setText("");
                    profilePhotoField.setText("");
                    imagePath = "";
                    
                    //System.out.println("-> User Data Updated!");
                    editStage.close();
                }
            });

            // Create a Close button
            Button closeButton= new Button("Close");
            closeButton.setOnAction(closeEvent -> editStage.close());

            // Add buttons to the layout
            HBox buttonLayout = new HBox(20, updateButton, closeButton);
            editLayout.getChildren().add(buttonLayout);
            buttonLayout.setAlignment(Pos.CENTER);
            VBox.setVgrow(buttonLayout, Priority.ALWAYS);

            // Set the scene and show the stage
            Scene editScene = new Scene(editLayout, 522, 446);

            editStage.setMinWidth(522);
            editStage.setMaxWidth(522);
            editStage.setMinHeight(440);
            editStage.setMaxHeight(440);

            editStage.setScene(editScene);
            editStage.show();
        });
    }
    public void editProfile() {
        // Set up edit button action
        editProfileBtn.setOnAction(event -> {
            db.getConnection();
            UserDataModel user = db.getAdminUserData(1);

            // Create a new Stage (window) for editing
            Stage editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL); // Make the stage modal
            editStage.setTitle("Edit Profile");
            editStage.initStyle(StageStyle.UTILITY); // Make the window undecorated

            // Create a layout for the new window
            VBox editLayout = new VBox(10);
            editLayout.setPadding(new Insets(10));

            // Create fields for Display Name, Profile Photo
            TextField displayNameField = new TextField(user.getDisplayName()); 
            ImageView profilePhotoView = new ImageView();
            Button ppImportBtn = new Button();
            
            ppImportBtn.setMinHeight(10);
            ppImportBtn.setMinWidth(70);
            ppImportBtn.setMaxWidth(70);
            ppImportBtn.setText("Import"); 
            
            imagePath = user.getImage();
            String path = "File:" + user.getImage();
            id = user.getId();
            image = new Image(path, 146, 146, false, true);
            profilePhotoView.setImage(image);
            
            // Set preferred width and height for the TextFields
            displayNameField.setMinHeight(30);
            displayNameField.setStyle("-fx-font-size: 14px;"); 
            
            // Add fields to the layout
            editLayout.getChildren().addAll( 
                new Label("Display Name:"), displayNameField,
                new Label("Profile Photo:"), profilePhotoView,
                ppImportBtn
            );
            
            ppImportBtn.setOnAction(updateEvent -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
                );

                File file = fileChooser.showOpenDialog(main_Form.getScene().getWindow());

                if (file != null) {

                    //path = file.getAbsolutePath();
                    image = new Image(file.toURI().toString(), 146, 146, false, true);
                    profilePhotoView.setImage(image);

                    try {
                        // Get the original file name
                        String originalFileName = file.getName();
                        String newImageName = "pp_" + originalFileName;

                        // Copy the selected file to the MyFolder directory with the new image name
                        File destination = new File(IMAGE_DIR + File.separator + newImageName);
                        Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

                        // Save the image path to the SQLite database
                        imagePath = destination.getAbsolutePath();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });

            // Create an Update button to confirm edits
            Button updateButton = new Button("Update");
            // Set button styling
            updateButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");

            updateButton.setOnAction(updateEvent -> {

                // Get the values from the input fields
                String displayNameText = displayNameField.getText();

                // Check if any fields are empty
                if (displayNameText.isEmpty() ) {
                    System.out.println("-> User Data Empty!");

                    // Show alert if any field is empty
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all blank fields");
                    alert.showAndWait();

                } else {
                    // Proceed with the update query since all validations passed
                    updateProfile(displayNameText, imagePath);
                    
                    displayNameField.setText("");
                    imagePath = "";
                    //System.out.println("-> User Data Updated!");
                    editStage.close();
                }
            });

            // Create a Close button
            Button closeButton= new Button("Close");
            closeButton.setOnAction(closeEvent -> editStage.close());

            // Add buttons to the layout
            HBox buttonLayout = new HBox(20, updateButton, closeButton);
            editLayout.getChildren().add(buttonLayout);
            buttonLayout.setAlignment(Pos.CENTER);
            VBox.setVgrow(buttonLayout, Priority.ALWAYS);

            // Set the scene and show the stage
            Scene editScene = new Scene(editLayout, 522, 446);

            editStage.setMinWidth(522);
            editStage.setMaxWidth(522);
            editStage.setMinHeight(440);
            editStage.setMaxHeight(440);

            editStage.setScene(editScene);
            editStage.show();
        });
    }
    public void updateProfile(String displayName, String profilePhoto) {
        db.getConnection();
        int user_id = 1;
        
        String updateData = "UPDATE admin SET display_name = '"
                            + displayName + "', image = '"
                            + profilePhoto + "' WHERE id = '"
                            + user_id + "'";
        
        try {

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to UPDATE your profile info?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                prepare = db.connection.prepareStatement(updateData);
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();

                System.out.println("-> Your Profile Updated!");
                // TO UPDATE YOUR TABLE VIEW
                loadAdminData(1);
                
            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("-> "+e);
        }
    }
    public void changePassword() {
        // Set up edit button action
        changeUserPassBtn.setOnAction(event -> {
            db.getConnection();
            UserDataModel user = db.getAdminUserData(1);

            // Create a new Stage (window) for editing
            Stage editStage = new Stage();
            editStage.initModality(Modality.APPLICATION_MODAL); // Make the stage modal
            editStage.setTitle("Change Password");
            editStage.initStyle(StageStyle.UTILITY); // Make the window undecorated

            // Create a layout for the new window
            VBox editLayout = new VBox(10);
            editLayout.setPadding(new Insets(10));

            // Create fields for Name, Description, Amount, Date, and Category
            ComboBox<String> questionComboBox = new ComboBox<>();
            questionComboBox.getItems().addAll(
                "What is your favorite Color?",
                "What is your favorite food?",
                "What is your favorite person?");
            questionComboBox.setValue(user.getQuestion());
            TextField answerField = new TextField(user.getAnswer().toString()); 
            TextField passwordField = new TextField(user.getPassword().toString()); 
            
            // Set preferred width and height for the TextFields
            passwordField.setMinHeight(30);
            passwordField.setStyle("-fx-font-size: 14px;"); 

            questionComboBox.setMinHeight(30);
            questionComboBox.setMinWidth(240);

            answerField.setMinHeight(30);
            answerField.setMinWidth(240);

            // Add fields to the layout
            editLayout.getChildren().addAll(
                new Label("*Select Question? :"), questionComboBox,
                new Label("*Set Your Answer :"), answerField,
                new Label("*Set Your Password :"), passwordField
            );

            // Create an Update button to confirm edits
            Button updateButton = new Button("Update");
            // Set button styling
            updateButton.setStyle("-fx-background-color: black; -fx-text-fill: white;");

            updateButton.setOnAction(updateEvent -> {

                // Get the values from the input fields
                String questionText = questionComboBox.getValue();
                String answerText = answerField.getText();
                String passwordText = passwordField.getText();

                // Check if any fields are empty
                if (passwordText.isEmpty()
                        ||answerText.isEmpty() ) {
                    System.out.println("-> User Data Empty!");

                    // Show alert if any field is empty
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Please fill all blank fields");
                    alert.showAndWait();

                } else if (passwordField.getText().length() < 6 || passwordField.getText().length() < 6) {
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid Password, Atleast 6 characters are needed");
                    alert.showAndWait();
                } else {
                    // Proceed with the update query since all validations passed
                    updateAdminPass(passwordText, questionText, answerText);
                    
                    questionComboBox.getSelectionModel().clearSelection();
                    answerField.setText("");
                    passwordField.setText("");
                    
                    editStage.close();
                }
            });

            // Create a Close button
            Button closeButton= new Button("Close");
            closeButton.setOnAction(closeEvent -> editStage.close());

            // Add buttons to the layout
            HBox buttonLayout = new HBox(20, updateButton, closeButton);
            editLayout.getChildren().add(buttonLayout);
            buttonLayout.setAlignment(Pos.CENTER);
            VBox.setVgrow(buttonLayout, Priority.ALWAYS);

            // Set the scene and show the stage
            Scene editScene = new Scene(editLayout, 522, 446);

            editStage.setMinWidth(522);
            editStage.setMaxWidth(522);
            editStage.setMinHeight(440);
            editStage.setMaxHeight(440);

            editStage.setScene(editScene);
            editStage.show();
        });
    }
    public void updateAdminPass(String password, String question, String answer) {
        //db.getConnection();
        int user_id = 1;
        
        String updateData = "UPDATE admin SET password = '"
                            + password + "', question = '"
                            + question + "', answer = '"
                            + answer + "' WHERE id = '"
                            + user_id + "'";
        
        try {

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to change your password?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                prepare = db.connection.prepareStatement(updateData);
                prepare.executeUpdate();

                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Success Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Updated!");
                alert.showAndWait();
                
                System.out.println("-> Your Password Updated!");
                db.getAdminUserData(1);
                
//                // TO HIDE MAIN FORM 
//                changeUserPassBtn.getScene().getWindow().hide();
//                // LINK YOUR LOGIN FORM AND SHOW IT 
//                Parent root = FXMLLoader.load(getClass().getResource("/view/authPage.fxml"));
//                Stage stage = new Stage();
//                Scene scene = new Scene(root);
//                stage.setTitle("GoPpo Management System");
//                stage.setScene(scene);
//                stage.show();

            } else {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Cancelled.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("-> "+e);
        }
    }
    /// END USERS SECTION/
    
    
    //// START REPORT SECTION
    private void loadITopDataRpt() {
        // Check if any label is null before proceeding
        if (totalPendingAmountRpt != null && totalInvoicesRpt != null && 
                totalItemsRpt != null && totalPackageRpt != null && 
                totalUsersRpt != null) {
            totalPendingAmountRpt.setText(String.format("%.2f TK", db.getTotalInvoicePendingAmount()));
            totalInvoicesRpt.setText(""+db.getTotalInvoice());
            totalItemsRpt.setText(""+db.getTotalItems());
            totalPackageRpt.setText(""+db.getTotalPackage());
            int xUsers = db.gettoTalUsers() + 1;
            totalUsersRpt.setText(""+xUsers);
        } else {
            System.err.println("One or more labels are not initialized!");
        }
    }
    private void loadOrderDataRpt() {
        // Check if any label is null before proceeding
        if (todayOrderRpt != null && yesterdayOrderRpt != null && thisweekOrderRpt != null && 
            thismonthOrderRpt != null && thisyearOrderRpt != null && totalIncomeRpt != null) {
            
            todayOrderRpt.setText(""+db.getTodayOrder());
            yesterdayOrderRpt.setText(""+db.getYesterdayOrder());
            thisweekOrderRpt.setText(""+db.getThisWeekOrder());
            thismonthOrderRpt.setText(""+db.getThisMonthOrder());
            thisyearOrderRpt.setText(""+db.getThisYearOrder());
            totalOrderRpt.setText(""+db.getTotalOrder());
            totalOrderTopRpt.setText(""+db.getCompleteOrder());
        } else {
            System.err.println("One or more labels are not initialized!");
        }
    }
    private void loadIncomeDataRpt() {
        // Check if any label is null before proceeding
        if (todayIncomeRpt != null && yesterdayIncomeRpt != null && thisweekIncomeRpt != null && 
            thismonthIncomeRpt != null && thisyearIncomeRpt != null && totalIncomeRpt != null) {
            
            todayIncomeRpt.setText(String.format("%.2f TK", db.getTodayIncome()));
            yesterdayIncomeRpt.setText(String.format("%.2f TK", db.getYesterdayIncome()));
            thisweekIncomeRpt.setText(String.format("%.2f TK", db.getThisWeekIncome()));
            thismonthIncomeRpt.setText(String.format("%.2f TK", db.getThisMonthIncome()));
            thisyearIncomeRpt.setText(String.format("%.2f TK", db.getThisYearIncome()));
            totalIncomeRpt.setText(String.format("%.2f TK", db.getTotalIncome()));
            totalIncomeTopRpt.setText(String.format("%.2f TK", db.getTotalIncome()));
        } else {
            System.err.println("One or more labels are not initialized!");
        }
    }
    private void loadExpenseDataRpt() {
        // Check if any label is null before proceeding
        if (todayExpenseRpt != null && yesterdayExpenseRpt != null && thisweekExpenseRpt != null && 
            thismonthExpenseRpt != null && thisyearExpenseRpt != null && totalExpenseRpt != null) {
            
            todayExpenseRpt.setText(String.format("%.2f TK", db.getTodayExpenses()));
            yesterdayExpenseRpt.setText(String.format("%.2f TK", db.getYesterdayExpenses()));
            thisweekExpenseRpt.setText(String.format("%.2f TK", db.getThisWeekExpenses()));
            thismonthExpenseRpt.setText(String.format("%.2f TK", db.getThisMonthExpenses()));
            thisyearExpenseRpt.setText(String.format("%.2f TK", db.getThisYearExpenses()));
            totalExpenseRpt.setText(String.format("%.2f TK", db.getTotalExpenses()));
            totalExpenseTopRpt.setText(String.format("%.2f TK", db.getTotalExpenses()));
        } else {
            System.err.println("One or more labels are not initialized!");
        }
    }
    private void loadExpensesByCategory() {
        ObservableList<PieChart.Data> expensesData = db.getExpensesByCategory();

        // Calculate total for percentage calculation
        double total = expensesData.stream()
                                   .mapToDouble(PieChart.Data::getPieValue)
                                   .sum();

        // Format labels with percentages
        expensesData.forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / total) * 100);
            data.nameProperty().set("(" + percentage + ")\n"+data.getName());
            //xAxis.setTickLabelRotation(45); 
        });

        expensePieChart.setData(expensesData);
    }
    private void loadExpensesByCategoryThisYear() {
        ObservableList<PieChart.Data> expensesData = db.getExpensesByCategoryThisYear();
        if (expensesData == null || expensesData.isEmpty()) {
            //System.out.println("No expense data found for the current year.");
            expensePieChartDash.getData().clear();
            return;
        }
        double total = expensesData.stream()
                                   .mapToDouble(PieChart.Data::getPieValue)
                                   .sum();

        expensesData.forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / total) * 100);
            data.nameProperty().set(" (" + percentage + ")\n"+data.getName());
        });
        expensePieChartDash.getData().clear();
        expensePieChartDash.setData(expensesData);
        
    }
    private void loadExpensesByCategoryThisYearRpt() {
        ObservableList<PieChart.Data> expensesData = db.getExpensesByCategoryThisYear();
        if (expensesData == null || expensesData.isEmpty()) {
            //System.out.println("No expense data found for the current year.");
            expensePieChartDash.getData().clear();
            return;
        }
        double total = expensesData.stream()
                                   .mapToDouble(PieChart.Data::getPieValue)
                                   .sum();

        expensesData.forEach(data -> {
            String percentage = String.format("%.1f%%", (data.getPieValue() / total) * 100);
            data.nameProperty().set(" (" + percentage + ")\n"+data.getName());
        });
        
        expensePieChartRpt.getData().clear();
        expensePieChartRpt.setData(expensesData);
        
    }

    private void loadMonthlyIncomeData() {
      
        ObservableList<String> months = FXCollections.observableArrayList(
        "January", "February", "March", "April", "May", "June", 
        "July", "August", "September", "October", "November", "December"
        );
        xAxis.setCategories(months);

        ObservableList<XYChart.Data<String, Number>> monthlyData = db.getMonthlyIncomeData();

        if (monthlyData == null || monthlyData.isEmpty()) {
            System.out.println("No data found for the current year.");
        } else {
            for (XYChart.Data<String, Number> data : monthlyData) {
                //System.out.println(data.getXValue() + ", Total Income: " + data.getYValue());
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Income");
        series.getData().addAll(monthlyData);

        incomeAreaChart.getData().add(series);
        xAxis.setTickLabelRotation(90);  

    }
    private void loadLast7DaysIncome() {
        ObservableList<String> days = FXCollections.observableArrayList(
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
        );
        xAxis7Inc.setCategories(days);
        ObservableList<XYChart.Data<String, Number>> dailyData = db.getLast7DaysIncomeData();

        if (dailyData == null || dailyData.isEmpty()) {
            System.out.println("No data found for the last 7 days.");
        } else {
            for (XYChart.Data<String, Number> data : dailyData) {
                //System.out.println(data.getXValue() + ", Total Income: " + data.getYValue());
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Last 7 Days Income");

        series.getData().addAll(dailyData);
        incomeAreaChart7Days.getData().clear();
        incomeAreaChart7Days.getData().add(series);
        xAxis7Inc.setTickLabelRotation(45);
    }
    private void loadMonthlyOrderData() {
      
        ObservableList<String> months = FXCollections.observableArrayList(
        "January", "February", "March", "April", "May", "June", 
        "July", "August", "September", "October", "November", "December"
        );
        xAxisOdr.setCategories(months);

        ObservableList<XYChart.Data<String, Number>> monthlyData = db.getMonthlyOrderData();

        if (monthlyData == null || monthlyData.isEmpty()) {
            System.out.println("No data found for the current year.");
        } else {
            for (XYChart.Data<String, Number> data : monthlyData) {
                //System.out.println(data.getXValue() + ", Total Order: " + data.getYValue());
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Income");
        series.getData().addAll(monthlyData);

        orderAreaChart.getData().add(series);
        xAxisOdr.setTickLabelRotation(90);  

    }
    private void loadLast7DaysOrder() {
    // Define days of the week
    ObservableList<String> days = FXCollections.observableArrayList(
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    );
    xAxis7Ord.setCategories(days);

    // Fetch the data for the last 7 days' orders
    ObservableList<XYChart.Data<String, Number>> dailyOrderData = db.getLast7DaysOrderData();

    if (dailyOrderData == null || dailyOrderData.isEmpty()) {
        System.out.println("No orders found for the last 7 days.");
    } else {
        for (XYChart.Data<String, Number> data : dailyOrderData) {
            //System.out.println(data.getXValue() + ", Total Orders: " + data.getYValue());
        }
    }

    // Create and populate the series
    XYChart.Series<String, Number> series = new XYChart.Series<>();
    series.setName("Last 7 Days Orders");
    series.getData().addAll(dailyOrderData);
   

    // Update the chart
    orderAreaChart7Days.getData().clear();
    orderAreaChart7Days.getData().add(series);
    xAxis7Ord.setTickLabelRotation(45);
    //orderAreaChart7Days.lookup(".chart-title").setRotate(90);
}
    public void tableDataExport() {
        db.getConnection();
        populateTableComboBox();
        
        exportBtn.setOnAction(event -> {
            exportTableData();
        });
    }
    private void populateTableComboBox() {
    ObservableList<String> tableNames = FXCollections.observableArrayList();
    try {
        DatabaseMetaData metaData = db.connection.getMetaData();
        ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            if (!"admin".equalsIgnoreCase(tableName)) {
                // Convert the table name to uppercase before adding it to the list
                tableNames.add(tableName.toUpperCase());
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    tableComboBox.setItems(tableNames);
}
    private boolean showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);

        // Show and wait for the user to click a button
        Optional<ButtonType> result = alert.showAndWait();

        // Check if the user clicked OK or Cancel
        return result.isPresent() && result.get() == ButtonType.OK;
    }
    private void exportTableDataX() {
        String selectedTable = (String) tableComboBox.getValue();
        if (selectedTable == null || selectedTable.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No table selected!", "Please select a table to export.");
            return;
        }

        // Show confirmation dialog
        boolean confirmed = showConfirmationDialog("Export Data", "Are you sure you want to export the table data?");

        // If user clicks 'Cancel', stop execution and do not export
        if (!confirmed) {
            showAlert(Alert.AlertType.WARNING, "Cancelled", "Export Cancelled!", "You have cancelled the export.");
            return; // Return early if the user cancels
        }

        // If user clicks 'OK', proceed with the export
        File downloadsDir = new File(System.getProperty("user.home") + "/Downloads"); //Need update here Download USER Desktop and GOPpo MS folder
        if (!downloadsDir.exists()) {
            downloadsDir.mkdirs();
        }

        File exportFile = new File(downloadsDir, selectedTable.toUpperCase() + ".csv");

        try (
             Statement statement = db.connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM " + selectedTable);
             FileWriter fileWriter = new FileWriter(exportFile)) {

            // Write CSV headers
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                fileWriter.append(metaData.getColumnName(i));
                if (i < columnCount) {
                    fileWriter.append(",");
                }
            }
            fileWriter.append("\n");

            // Write CSV rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    fileWriter.append(resultSet.getString(i));
                    if (i < columnCount) {
                        fileWriter.append(",");
                    }
                }
                fileWriter.append("\n");
            }

            // Show success alert if export is successful
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data Exported", "Table data exported successfully to Downloads folder!");

        } catch (SQLException | IOException e) {
            // Handle errors and show an alert if an error occurs
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Export Failed", "An error occurred while exporting table data.");
        }
    }
    private void exportTableDataZ() {
        String selectedTable = (String) tableComboBox.getValue();
        if (selectedTable == null || selectedTable.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No table selected!", "Please select a table to export.");
            return;
        }

        // Show confirmation dialog
        boolean confirmed = showConfirmationDialog("Export Data", "Are you sure you want to export the table data?");

        // If user clicks 'Cancel', stop execution and do not export
        if (!confirmed) {
            showAlert(Alert.AlertType.WARNING, "Cancelled", "Export Cancelled!", "You have cancelled the export.");
            return; // Return early if the user cancels
        }

        // Determine the folder path: Desktop/GoPpo MS
        File desktopDir = new File(System.getProperty("user.home"), "Desktop");
        File goppoMsDir = new File(desktopDir, "GoPpo MS");
        if (!goppoMsDir.exists()) {
            goppoMsDir.mkdirs(); // Create the GoPpo MS directory if it doesn't exist
        }

        // Set the export file path
        File exportFile = new File(goppoMsDir, selectedTable.toUpperCase() + ".csv");

        try (
            Statement statement = db.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + selectedTable);
            FileWriter fileWriter = new FileWriter(exportFile)) {

            // Write CSV headers
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                fileWriter.append(metaData.getColumnName(i));
                if (i < columnCount) {
                    fileWriter.append(",");
                }
            }
            fileWriter.append("\n");

            // Write CSV rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    fileWriter.append(resultSet.getString(i));
                    if (i < columnCount) {
                        fileWriter.append(",");
                    }
                }
                fileWriter.append("\n");
            }

            // Show success alert if export is successful
            showAlert(Alert.AlertType.INFORMATION, "Success", "Data Exported", 
                "Table data exported successfully to Desktop/GoPpo MS folder!");

        } catch (SQLException | IOException e) {
            // Handle errors and show an alert if an error occurs
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Export Failed", "An error occurred while exporting table data.");
        }
    }
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private String convertTimestampToDate(long timestamp) {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//(YYYY-MM-DD HH:MM:SS)
        return sdf.format(date);
    }
    private void exportTableData() {
    String selectedTable = (String) tableComboBox.getValue();
    if (selectedTable == null || selectedTable.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, "Error", "No table selected!", "Please select a table to export.");
        return;
    }

    boolean confirmed = showConfirmationDialog("Export Data", "Are you sure you want to export the table data?");
    if (!confirmed) {
        showAlert(Alert.AlertType.WARNING, "Cancelled", "Export Cancelled!", "You have cancelled the export.");
        return;
    }

    // Define the export directory (Desktop/GoPpo MS folder)
    File desktopDir = new File(System.getProperty("user.home"), "Desktop");
    File goppoMsDir = new File(desktopDir, "GoPpo MS");
    if (!goppoMsDir.exists()) {
        goppoMsDir.mkdirs();
    }

    File exportFile = new File(goppoMsDir, selectedTable.toUpperCase() + ".csv");

    try (
        Statement statement = db.connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + selectedTable);
        FileWriter fileWriter = new FileWriter(exportFile)) {

        // Write the CSV headers
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 1; i <= columnCount; i++) {
            fileWriter.append(metaData.getColumnName(i));
            if (i < columnCount) {
                fileWriter.append(",");
            }
        }
        fileWriter.append("\n");

        // Write the CSV rows
        while (resultSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String value = resultSet.getString(i);

                // If the column is 'date', convert it from UNIX timestamp to a readable date format
                if ("date".equalsIgnoreCase(columnName) && value != null) {
                    long timestamp = Long.parseLong(value);
                    value = convertTimestampToDate(timestamp); // Convert UNIX timestamp to readable date
                }
                if ("items".equalsIgnoreCase(columnName) && value != null) {
                    value = "List Of Cart Items";
                }

                fileWriter.append(value == null ? "" : value);
                if (i < columnCount) {
                    fileWriter.append(",");
                }
            }
            fileWriter.append("\n");
        }

        // Show success alert
        showAlert(Alert.AlertType.INFORMATION, "Success", "Data Exported", "Table data exported successfully to Desktop/GoPpo MS folder!");

    } catch (SQLException | IOException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Error", "Export Failed", "An error occurred while exporting table data.");
    }
}


    private void setupDevInfoLongPress() {
        PauseTransition longPress = new PauseTransition(Duration.seconds(2));
        devInfoBtn.setOnMousePressed(event -> {
            longPress.setOnFinished(e -> getViewDevInfo());
            longPress.playFromStart();
        });
        devInfoBtn.setOnMouseReleased(event -> longPress.stop());
    }
    public void getViewDevInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/devInfo.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            Scene scene = new Scene(root);

            stage.setTitle("Developer Information");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);

            stage.setMinWidth(455);
            //stage.setMaxWidth(455);
            stage.setMinHeight(547);
            //stage.setMaxHeight(547);
            stage.setScene(scene);

            stage.show();

        } catch (IOException e) {
            System.out.println("Error loading invoice.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /// END REPORT SECTION/

    //// START SETTINGS SECTION
    public void appUpBtn(){
        showAlert(AlertType.CONFIRMATION, 
            "Information Masseges", 
            "This Features Currently Not Available!", 
            "Please Wait Until Next Update.... 👍"
        );
    }
    /// END SETTINGS SECTION/

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Create the folder if not exists
        File folder = new File("Images");
        if (!folder.exists()) { folder.mkdir(); }
       
        //Dashboard
        loadExpensesByCategory();
        loadExpensesByCategoryThisYearRpt();
        loadMonthlyIncomeData();
        loadMonthlyOrderData();
        tableDataExport();
        
        loadLast7DaysIncome();
        loadLast7DaysOrder();
        loadExpensesByCategoryThisYear();
        
        setupDevInfoLongPress();
        
        Platform.runLater(this::loadDashTopData);
        displayUsername();
        initializePhrases();
        startTypingEffect();
        initializeSlideshow();
        startDateTimeDisplay();
        
        

        
        //Items
        itemsCategoryList();
        itemsStatusList();
        itemsShowData();
        setDynamicColumnWidthForItem();
        
        //POS Memu
        getDynamicDisplayCard();
        filterItemData();
        menuOrderTypeList();
        setupCartTable();
        setDynamicColumnWidthForCartTable();
        getTotalDynamically();

        //Cullect Bill
        billDisplayCard();
        
        //Invoice
        invoiceShowData();
        setDynamicColumnWidthForInvoiceTable();
        //printItems();
        
        //Expense
        getExpDateValidation();
        getExpDateRangeValidation();
        expenseCategoryList();
        expenseShowData();
        setDynamicColumnWidthForExp();
        loadExpenseData();
        
        //Reports
        loadITopDataRpt();
        loadOrderDataRpt();
        loadIncomeDataRpt();
        loadExpenseDataRpt();
        
        //User Admin
        loadAdminData(1);
        empUserRoleList();
        empUserStatusList();
        empUserShowData();
        showPass();
        
        
        //Settings
        
        
        // Schedule logic to execute after the application is fully initialized
        Platform.runLater(() -> {
            Stage stage = (Stage) main_window.getScene().getWindow();
            if (stage != null) {
                // Add a listener for maximize/restore events
                stage.maximizedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        deactiveMsgMax();
                        isWindowStatus = true; // Window is maximized
                        //System.out.println("------> Window is maximized");
                    } else {
                        deactiveMsgDown();
                        isWindowStatus = false; // Window is restored down
                        //System.out.println("------> Window is restored down");
                    }
                });
                //System.out.println("Stage is: " + stage); // Verify the Stage is now accessible
            } else {
                //System.out.println("Stage is still null! Check your FXML loading process.");
            }
        });
        
    }

}
