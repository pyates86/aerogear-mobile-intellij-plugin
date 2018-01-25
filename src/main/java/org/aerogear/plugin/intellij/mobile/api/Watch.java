package org.aerogear.plugin.intellij.mobile.api;

import java.io.IOException;

public interface Watch {
    public void onError(Exception e);
    public void onSuccess(StringBuilder sbi);
}
