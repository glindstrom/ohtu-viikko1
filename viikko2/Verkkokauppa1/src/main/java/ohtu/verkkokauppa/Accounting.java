
package ohtu.verkkokauppa;

import java.util.ArrayList;

/**
 *
 * @author gabriel
 */
public interface Accounting
{

    ArrayList<String> getTapahtumat();

    void lisaaTapahtuma(String tapahtuma);
    
}
