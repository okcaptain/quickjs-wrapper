package com.whl.quickjs.wrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Harlon Wang on 2021/10/12.
 */
public final class JSModule {

    private static ModuleLoader sModuleLoader;

    public static abstract class ModuleLoader {
        public abstract String getModuleScript(String moduleName);

        public String convertModuleName(String moduleBaseName, String moduleName) {
            if (moduleName == null || moduleName.length() == 0 || moduleName.startsWith("http")) {
                return moduleName;
            }
            moduleName = moduleName.replace("//", "/");
            if (moduleName.startsWith("./")) {
                moduleName = moduleName.substring(2);
            }
            if (moduleName.charAt(0) == '/') {
                return moduleName;
            }
            if (moduleBaseName == null || moduleBaseName.length() == 0) {
                return moduleName;
            }
            moduleBaseName = moduleBaseName.replace("//", "/");
            if (moduleBaseName.startsWith("./")) {
                moduleBaseName = moduleBaseName.substring(2);
            }
            if (moduleBaseName.equals("/")) {
                return "/" + moduleName;
            }
            if (moduleBaseName.endsWith("/")) {
                return moduleBaseName + moduleName;
            }
            String[] parentSplit = moduleBaseName.split("/");
            String[] pathSplit = moduleName.split("/");
            List<String> parentStack = new ArrayList<>();
            List<String> pathStack = new ArrayList<>();
            Collections.addAll(parentStack, parentSplit);
            Collections.addAll(pathStack, pathSplit);
            while (!pathStack.isEmpty()) {
                String tmp = pathStack.get(0);
                if (tmp.equals("..")) {
                    pathStack.remove(0);
                    parentStack.remove(parentStack.size() - 1);
                } else {
                    break;
                }
            }
            if (!parentStack.isEmpty()) {
                parentStack.remove(parentStack.size() - 1);
            }
            StringBuilder builder = new StringBuilder();
            if (moduleBaseName.startsWith("/")) {
                builder.append("/");
            }
            for (String it : parentStack) {
                builder.append(it).append("/");
            }
            for (String it : pathStack) {
                builder.append(it).append("/");
            }
            builder.deleteCharAt(builder.length() - 1);
            return builder.toString();
        }
    }

    public static void setModuleLoader(ModuleLoader moduleLoader) {
        sModuleLoader = moduleLoader;
    }

    static String getModuleScript(String moduleName) {
        return sModuleLoader.getModuleScript(moduleName);
    }

    static String convertModuleName(String moduleBaseName, String moduleName) {
        return sModuleLoader.convertModuleName(moduleBaseName, moduleName);
    }

}
