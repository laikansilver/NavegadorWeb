package org.example.navegadorweb;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.MalformedURLException;
import java.net.URL;

public class NavegadorController {

    @FXML
    private TabPane tabPane;

    @FXML
    private TextField urlField;

    @FXML
    private TextField searchField;

    public void initialize() {
        createNewTab("http://www.google.com");

        urlField.setOnAction(event -> handleGoAction());
        searchField.setOnAction(event -> handleSearchAction());
    }

    @FXML
    private void handleGoAction() {
        WebEngine engine = getEngineOfSelectedTab();
        if (engine != null) {
            String url = urlField.getText();
            if (isValidURL(url)) {
                engine.load(url);
            }
        }
    }

    @FXML
    private void handleSearchAction() {
        WebEngine engine = getEngineOfSelectedTab();
        if (engine != null) {
            String searchQuery = searchField.getText();
            if (!searchQuery.isEmpty()) {
                engine.load("http://www.google.com/search?q=" + searchQuery);
            }
        }
    }

    @FXML
    public void goBack() {
        WebEngine engine = getEngineOfSelectedTab();
        if (engine != null && engine.getHistory().getCurrentIndex() > 0) {
            engine.getHistory().go(-1);
        }
    }

    @FXML
    public void goForward() {
        WebEngine engine = getEngineOfSelectedTab();
        if (engine != null && engine.getHistory().getCurrentIndex() < engine.getHistory().getEntries().size() - 1) {
            engine.getHistory().go(1);
        }
    }


    @FXML
    private void handleBackAction() {
        WebEngine engine = getEngineOfSelectedTab();
        if (engine != null && engine.getHistory().getCurrentIndex() > 0) {
            engine.getHistory().go(-1);
        }
    }

    @FXML
    private void handleForwardAction() {
        WebEngine engine = getEngineOfSelectedTab();
        if (engine != null && engine.getHistory().getCurrentIndex() < engine.getHistory().getEntries().size() - 1) {
            engine.getHistory().go(1);
        }
    }

    @FXML
    private void handleNewTabAction() {
        if (tabPane.getSelectionModel().getSelectedItem().getText().equals("+")) {
            createNewTab("http://www.google.com");
        }
    }

    private void createNewTab(String url) {
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        webEngine.load(url);

        webEngine.locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                urlField.setText(newValue);
            }
        });

        Tab tab = new Tab();
        tab.setContent(webView);
        tabPane.getTabs().add(tabPane.getTabs().size() - 1, tab);
        tabPane.getSelectionModel().select(tab);
    }

    private WebEngine getEngineOfSelectedTab() {
        if (tabPane.getSelectionModel().getSelectedItem() != null) {
            WebView webView = (WebView) tabPane.getSelectionModel().getSelectedItem().getContent();
            return webView.getEngine();
        }
        return null;
    }

    private boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}