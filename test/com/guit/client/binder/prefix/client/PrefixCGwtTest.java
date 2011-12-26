package com.guit.client.binder.prefix.client;

import org.junit.Test;

public class PrefixCGwtTest extends AbstractPrefixGwtTest {
    @Override
    public String getModuleName() {
        return "com.guit.client.binder.prefix.PrefixCModule";
    }

    @Test
    public void testPrefixedViewValue(){
        assertEquals("PREFIX_A", presenter.label.getText()); // Fallback value!
    }
}
