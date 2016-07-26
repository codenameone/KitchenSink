package tests;

import com.codename1.testing.AbstractTest;

import com.codename1.ui.Display;

public class UnnamedTest extends AbstractTest {
    public boolean runTest() throws Exception {
        waitForFormTitle("Kitchen Sink");
        ensureVisible(new int[]{0, 1, 3, 1, 0});
        return true;
    }
}
