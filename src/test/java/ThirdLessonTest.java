import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThirdLessonTest {
    @ParameterizedTest
    @ValueSource(strings = {"Осень", "Осень-это сны листопада..."})
    public void Ex10(String strExample){
        System.out.println("Ex10:");
        int strLength = strExample.length();
        assertTrue(strLength >15, "Тест провален. Длина строки меньше 15 символов:"+strExample);
    }
}
