package com.example.flow;

import com.example.flow.displayClasses.LoginScreens.Login;
import com.example.flow.services.Empty;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void validation_is_correct() {
        assertTrue(Empty.isImputNotEmpty("dfghj"));
        assertEquals(false, Empty.isImputNotEmpty(null));
        assertEquals(false, Empty.isImputNotEmpty(""));
    }
}