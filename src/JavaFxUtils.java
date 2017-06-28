import javafx.scene.paint.Color;

/**
 * Created by y.brisch on 28.06.17.
 */
public class JavaFxUtils {

  public static String toRGBCode( Color color )
  {
    return String.format( "#%02X%02X%02X",
        (int)( color.getRed() * 255 ),
        (int)( color.getGreen() * 255 ),
        (int)( color.getBlue() * 255 ) );
  }
}
