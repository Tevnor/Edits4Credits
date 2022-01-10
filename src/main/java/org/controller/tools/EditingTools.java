package org.controller.tools;

/**
 * Interface for the basic image editing tools
 */
public interface EditingTools {

    void apply(); //applies tool differs for every tool
    void backward(); //reverses last applied edit
    void forward(); //reverses backward() method if it was used before

}
