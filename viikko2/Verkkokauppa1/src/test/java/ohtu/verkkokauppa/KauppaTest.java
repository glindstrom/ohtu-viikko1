
package ohtu.verkkokauppa;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author gabriel
 */
public class KauppaTest {
    
    Pankki pankki;
    Viitegeneraattori viite;
    Varasto varasto;
    Kauppa kauppa;
    
    @Before
    public void setUp() {
        pankki = mock(Pankki.class);
        viite = mock(Viitegeneraattori.class);
        varasto = mock(Varasto.class);
        kauppa = new Kauppa(varasto, pankki, viite);
    }
    
    @Test
    public void ostoksenPaatyttyaPankinMetodiaTilisiirtoKutsutaanOikeallaAsiakkaallaTilinumerollaJaSummalla() {

        // määritellään että viitegeneraattori palauttaa viitten 42
        when(viite.uusi()).thenReturn(42);

        // määritellään että tuote numero 1 on maito jonka hinta on 5 ja saldo 10
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));             

        // tehdään ostokset
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);     // ostetaan tuotetta numero 1 eli maitoa
        kauppa.tilimaksu("pekka", "12345");

        // sitten suoritetaan varmistus, että pankin metodia tilisiirto on kutsuttu
        verify(pankki).tilisiirto(eq("pekka"), eq(42), eq("12345"), anyString(),eq(5));   
        // toistaiseksi ei välitetty kutsussa käytetyistä parametreista
    }
    
    @Test
    public void tiliSiirtoOikeinKunOstetaanKaksiEriTuotetta(){
        when(viite.uusi()).thenReturn(45);
        
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        when(varasto.saldo(2)).thenReturn(9); 
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "juusto", 6));
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto(eq("pekka"), eq(45), eq("12345"), anyString(),eq(11));
    }
    
    
    @Test
    public void tiliSiirtoOikeinKunOstetaanKaksiSamaaTuotetta(){
        when(viite.uusi()).thenReturn(53);
        
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
                
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto(eq("pekka"), eq(53), eq("12345"), anyString(),eq(10));
    }
    
    @Test
    public void tilisiirtoOikeinKunToinenTuoteVarastossaJaToinenLoppu(){
        when(viite.uusi()).thenReturn(15);
        
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        when(varasto.saldo(2)).thenReturn(0); 
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "juusto", 6));
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto(eq("pekka"), eq(15), eq("12345"), anyString(),eq(5));
        
    }
    
    @Test
    public void asioinninAloittaminenNollaaTiedot(){
        
        when(varasto.saldo(1)).thenReturn(10); 
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "maito", 5));
        
        when(varasto.saldo(2)).thenReturn(9); 
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "juusto", 6));
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("pekka", "12345");
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(),eq(5));
        
    }
    
    @Test
    public void pyydetaanUusiViiteJokaiseenMaksuun(){
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.tilimaksu("pekka", "12345");
        
        verify(viite, times(1)).uusi();
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(2);
        kauppa.tilimaksu("mikko", "1234");
        
        verify(viite, times(2)).uusi();
        
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(23);
        kauppa.tilimaksu("mika", "1111");
        
        verify(viite, times(3)).uusi();          
    }
    
    @Test
    public void tuotePalautetaanVarastoonKunPoistetaanKorista(){
        kauppa.aloitaAsiointi();
        kauppa.lisaaKoriin(1);
        kauppa.poistaKorista(1);
        
        verify(varasto, times(1)).palautaVarastoon(any(Tuote.class));
    }
}
