import org.example.Calculadora;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraTest {


    @Test
    public void somaTest(){
        Calculadora c =  new Calculadora();
        assertEquals(4, c.somar(2,2));
    }
}
