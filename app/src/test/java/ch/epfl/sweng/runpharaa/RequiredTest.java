package ch.epfl.sweng.runpharaa;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.runpharaa.utils.Required;

@RunWith(JUnit4.class)
public class RequiredTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testNonNull(){
        exception.expect(NullPointerException.class);
        Required.nonNull(null, "");
    }

    @Test
    public void testGreaterOrEqualZeroInt(){
        exception.expect(IllegalArgumentException.class);
        Required.greaterOrEqualZero(-1, "");
    }

    @Test
    public void testGreaterOrEqualZeroDouble(){
        exception.expect(IllegalArgumentException.class);
        Required.greaterOrEqualZero(-1.0, "");
    }
}
