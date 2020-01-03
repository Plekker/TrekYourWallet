package com.example.flow;

import com.example.flow.displayClasses.LoginScreens.Login;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {


    Login login = new Login();

    @Test
    public void validation_is_correct() {
        assertEquals(true, login.validate("test@test.com","Pp12"));
    }
}