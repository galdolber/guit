package com.guit.client.binder.prefix.client;

import org.junit.Test;

public class PrefixBGwtTest extends AbstractPrefixGwtTest {
    @Override
    public String getModuleName() {
        return "com.guit.client.binder.prefix.PrefixBModule";
    }

    @Test
    public void testPrefixedViewValue(){
        assertEquals("PREFIX_B", presenter.label.getText());
    }
}
