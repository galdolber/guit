package com.guit.client.binder.prefix.client;

import org.junit.Test;

public class PrefixAGwtTest extends AbstractPrefixGwtTest {
    @Override
    public String getModuleName() {
        return "com.guit.client.binder.prefix.PrefixAModule";
    }

    @Test
    public void testPrefixedViewValue(){
        assertEquals("PREFIX_A", presenter.label.getText());
    }
    
    
}
