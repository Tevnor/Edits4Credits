package basic.editor.tools;

/**
 * Interface for the basic image editing tools
 */
public interface EditingTools {


    int TYPE_DRAW = 0, TYPE_VISUAL = 1, TYPE_TRANSFORM = 2;

    void apply(); //applies tool differs for every tool
    void backward(); //reverses last applied edit
    void forward(); //reverses backward() method if it was used before

}
