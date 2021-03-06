/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bank.server;

import bank.bankieren.Bank;
import bank.gui.BankierClient;
import bank.internettoegang.Balie;
import bank.internettoegang.IBalie;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author frankcoenen
 */
public class BalieServer extends Application {

    private Stage stage;
    private final double MINIMUM_WINDOW_WIDTH = 600.0;
    private final double MINIMUM_WINDOW_HEIGHT = 200.0;
    private String nameBank;

    @Override
    public void start(Stage primaryStage) throws IOException {

        try {
            stage = primaryStage;
            stage.setTitle("Bankieren");
            stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
            stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
            gotoBankSelect();

            primaryStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean startBalie(String nameBank) {
            
            FileOutputStream out = null;
            int port = 0;
            try {
                this.nameBank = nameBank;
                String address = java.net.InetAddress.getLocalHost().getHostAddress();
                if("RaboBank".equals(nameBank))
                {
                    port = 1100;
                }
                if(nameBank == "ING")
                {
                    port = 1102;
                }
                if(nameBank == "SNS")
                {
                    port = 1104;
                }
                if(nameBank == "ABN AMRO")
                {
                    port = 1106;
                }
                if(nameBank == "ASN")
                {
                    port = 1108;
                }
                Properties props = new Properties();
                //port +1
                //String rmiBalie = address + ":" + port + "/" + nameBank;
                String rmiBalie = port+"";
                String bank = nameBank;
                String ip = InetAddress.getLocalHost().getHostAddress();
                props.setProperty("port", rmiBalie);
                props.setProperty("bank", bank);
                props.setProperty("ip", ip);
                out = new FileOutputStream(nameBank + ".props");
                props.store(out, null);
                out.close();
                Registry registry = LocateRegistry.createRegistry(port);
                IBalie balie = new Balie(new Bank(nameBank, port));
                //Naming.rebind(nameBank, balie);
                registry.rebind(nameBank, balie);
               
                return true;

            } catch (IOException ex) {
                Logger.getLogger(BalieServer.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    out.close();
                } catch (IOException ex) {
                    Logger.getLogger(BalieServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return false;
    }

    public void gotoBankSelect() {
        try {
            BalieController bankSelect = (BalieController) replaceSceneContent("Balie.fxml");
            bankSelect.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(BankierClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Initializable replaceSceneContent(String fxml) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = BalieServer.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(BalieServer.class.getResource(fxml));
        AnchorPane page;
        try {
            page = (AnchorPane) loader.load(in);
        } finally {
            in.close();
        }
        Scene scene = new Scene(page, 800, 600);
        stage.setScene(scene);
        stage.sizeToScene();
        return (Initializable) loader.getController();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
