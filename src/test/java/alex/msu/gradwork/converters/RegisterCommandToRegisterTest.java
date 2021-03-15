package alex.msu.gradwork.converters;

import alex.msu.gradwork.commands.RegisterCommand;
import alex.msu.gradwork.domain.Register;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterCommandToRegisterTest {

    public static final Long ID_VALUE = 1L;
    public static final String REGISTER_NAME = "Super Register";
    RegisterCommandToRegister converter;

    @Before
    public void setUp() throws Exception {
        //converter = new RegisterCommandToRegister(noteConverter);
    }

//    @Test
//    public void testNullParameter() throws Exception {
//        assertNull(converter.convert(null));
//    }
//
//    @Test
//    public void testEmptyObject() throws Exception {
//        assertNotNull(converter.convert(new RegisterCommand()));
//    }
//
//    @Test
//    public void convert() throws Exception {
//        //given
//        RegisterCommand registerCommand = new RegisterCommand();
//        registerCommand.setId(ID_VALUE);
//        registerCommand.setName(REGISTER_NAME);
//
//        //when
//        Register register = converter.convert(registerCommand);
//
//        //then
//        assertNotNull(register);
//        assertEquals(ID_VALUE, register.getId());
//        assertEquals(REGISTER_NAME, register.getName());
//    }


}
