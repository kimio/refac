/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rule;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author felipekn
 */
public class RulesObjectiveC extends Rules{
    private int positionEndRowImplementation = 0;
    private int methodLineStart = 0;
    private String methodTextStart = "";
    
    public RulesObjectiveC(){
        super();
    }
    @Override
    public void verify(){
        currentImplementationRow();
        verifyString();
        spaceBetweenMethodSignature();
        movePointerOn();
        code.append(row).append("\n");
    }
    public String getNewStringVarsWithImplementation(){
        StringBuilder varsBuilder = new StringBuilder();
        Set<String> keys = vars.keySet();
        keys.forEach((key) -> {
            varsBuilder.append(vars.get(key));
        });
        varsBuilder.append("\n");
        return 
                code.replace(
                        positionEndRowImplementation,
                        positionEndRowImplementation+1, 
                        varsBuilder.toString()).toString();
    }
    //Validation
    private void currentImplementationRow(){
        if(positionEndRowImplementation<1){
            if(row.contains("@implementation")){
                positionEndRowImplementation = code.toString().length()+row.length();
            }
        }
    }
    private void verifyString(){
        if(row.contains("@\"")&&!row.contains("NSLocalizedString")&&!row.contains("static NSString")&&!row.contains("@\"\"")){
            
            Pattern pattern = Pattern.compile("@\"(.+?)\"");
            Matcher matcher = pattern.matcher(row);
            matcher.find();

            String valueString = matcher.group(1);
            String replaceWithVar = "@\""+valueString+"\"";
            String var = "_VAR"+rowNumber;
            this.vars.put(var, "\nstatic NSString *const "+var+" = "+replaceWithVar+";");
            
            row = row.replaceAll(replaceWithVar, var);
        }else if(row.contains("@\"\"")&&!row.contains("NSLocalizedString")&&!row.contains("static NSString")){
            row = row.replace("@\"\"", "[NSString string]");
        }
    }
    private void spaceBetweenMethodSignature(){
        if(row.contains("-(")){
            row = row.replace("-(", "- (");
        }
    }
    private void movePointerOn(){
        if(row.contains("*)")&&!row.contains(" *)")){
            row = row.replace("*)", " *)");
        }
    }
    
    //Kind of Errors
    private void pointerError(){
        if(row.contains(" * ")){
            errors.put(keyPointer+rowNumber+1, row);
        }
    }
    
    private void methodMaxRows(){
        if(row.length()>0){
            if(row.substring(0,1).equals("-")){
                methodLineStart = rowNumber;
                methodTextStart = row;
            }
            else if(methodLineStart>0&&row.substring(0,1).equals("}")){
                //30 method lines and 2 header and footer lines 
                if((rowNumber-methodLineStart)>32){
                    errors.put(keyMethodMaxLines+(methodLineStart+1)+lines+(rowNumber-methodLineStart-2), methodTextStart);
                }
                methodLineStart = 0;
            }
        }
    }
    
    //Log Error
    @Override
    public void setError() {
        pointerError();
        methodMaxRows();
    }
    
}
