package s.ahmadsq.shhi;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ShhiTest {
    @Test
    public void email_isValid() throws Exception {

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String expected_email = "Ahmad.alqurashi95@gmail.com" ;

        if (expected_email.matches(emailPattern)){
            assertEquals(true,true);
        }else {
            assertEquals(true,false);
        }
    }
    @Test
    public void password_isValid() throws Exception {

       String password = "123456";

        if (password.length() >= 6){
            assertEquals(true,true);
        }else {
            assertEquals(true,false);
        }

    }
   }
