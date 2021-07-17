package com.whl.quickjs.wrapper;

public class QuickJSContext {

    static {
        System.loadLibrary("quickjs-android-wrapper");
    }

    private static final String UNDEFINED = "undefined.js";


    public static QuickJSContext create() {
        return new QuickJSContext();
    }

    private final long context;

    private QuickJSContext() {
        context = createContext();
    }

    public Object evaluate(String script) {
        return evaluate(script, UNDEFINED);
    }

    public Object evaluate(String script, String fileName) {
        return evaluate(context, script, fileName);
    }

    public void destroyContext() {
        destroyContext(context);
    }

    private native long createContext();
    private native void destroyContext(long context);
    private native Object evaluate(long context, String script, String fileName);

}
