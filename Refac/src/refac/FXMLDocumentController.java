package refac;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import rule.RulesObjectiveC;

/**
 *
 * @author felipekn
 */
public class FXMLDocumentController implements Initializable {
    //selected file to refactore
    private File fileSelected = null;
    
    @FXML
    public TextField file;
    
    @FXML
    public TextArea file_refactored;
    
    @FXML
    public TextArea before_refactor;
    
    @FXML
    public Label about;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        about.setText(aboutThisSoftware);
    }   
    @FXML
    private void importFile(ActionEvent event) throws InterruptedException {
        List<File> files = util.Util.openFileChooser(Refac.currentStage, "Select file to refactore", false);
        for (File file_import : files) {
            file.setText(file_import.getAbsolutePath());
            fileSelected = file_import;
        }
    }

    /***
     * Refactor file selected
     * @param event
     * @throws InterruptedException 
     */
    @FXML
    private void refactorFile(ActionEvent event) throws InterruptedException {
        String fileText = util.Util.readFile(fileSelected);
        before_refactor.setText(fileText);
        readFileAndSetTextArea(fileText);
    }
    /***
     * Reading file and refactoring with rules
     * @param fileText 
     */
    private void readFileAndSetTextArea(String fileText){
        RulesObjectiveC rules = new RulesObjectiveC();
        int i=0;
        for(String row : fileText.split("\n")){
            //set the row on rules
            rules.setRow(row,i);
            i++;
        }
        file_refactored.setText(rules.getNewStringVarsWithImplementation());
    }
    
    //not remove :(
    final String aboutThisSoftware = 
        "Update Objective-c files\n\n"+
        "Feel free to update this software!\n"+
        "Author: Felipe Kimio Nishikaku";
    
}
